var ge;  //this is the hugly but very friendly global var for the gantt editor
$(function() {

       //load templates
      $("#ganttemplates").loadTemplates();

      // here starts gantt initialization
      ge = new GanttMaster();
      var workSpace = $("#workSpace");
      workSpace.css({width:$(window).width() - 60, height:$(window).height() - 215});
      ge.init(workSpace);

      //inject some buttons (for this demo only)
      $(".ganttButtonBar div").addClass('buttons');
      //overwrite with localized ones
      loadI18n();

      //simulate a data load from a server.
      loadGanttFromServer();


      //fill default Teamwork roles if any
      if (!ge.roles || ge.roles.length == 0) {
              setRoles();
      }

      //fill default Resources roles if any
      if (!ge.resources || ge.resources.length == 0) {
              setResource();
      }

      $(window).resize(function(){
              workSpace.css({width:$(window).width() - 60,height:$(window).height() - 215});
              workSpace.trigger("resize.gantt");
      }).oneTime(150,"resize",function(){$(this).trigger("resize")});

});


function loadGanttFromServer() {
      $.ajax({
            type: "GET",
            url: IPAddress+"Task/listPjtTask/"+GetQueryString("pid"),
            async: true,
            dataType: 'json',
            success: function(result, status) {
                    var datas = {
                          "tasks": function() {
                                var temp = [];
                                for(var i=0; i<result.length; i++) {
                                        temp.push({
                                              "id": result[i].id,
                                              "name": result[i].tilte,
                                              "code": result[i].code,
                                              "level": result[i].level,
                                              "status": result[i].state,
                                              "canWrite": true,
                                              "start": new Date(result[i].planStart).getTime(),
                                              "duration": recomputeDuration(result[i].planStart, result[i].planEnd),
                                              "end": new Date(result[i].planEnd).getTime(),
                                              "collapsed":false,
                                              "assigs":[],
                                              "hasChild": result[i].hasChild,
                                              "depends": result[i].frontNum,
                                              "description": result[i].resume,
                                              "manager": result[i].manager,
                                              "depends": result[i].frontNum,
                                              "progress": result[i].progress,
                                              "orderId": result[i].orderId
                                        });
                                }
                                return temp;
                          }(),
                          "selectedRow": 0,
                          "canWrite": true,
                          "canWriteOnParent": true
                    }

                    datas.tasks.sort(function(a, b) {
                           return a.orderId - b.orderId;
                    });
                    //console.log(datas);
                    ge.loadProject(datas);
                    ge.checkpoint();
            }
      });
}

function saveGanttOnServer() {
      if(!ge.canWrite)
            return;

      saveInDatabase();
}

//-------------------------------------------  Create some demo data ------------------------------------------------------
function setRoles() {
      ge.roles = [
              {
                    id:"tmp_1",
                    name:"Project Manager"
              },
              {
                    id:"tmp_2",
                    name:"Worker"
              },
              {
                    id:"tmp_3",
                    name:"Stakeholder/Customer"
              }
      ];
}

function setResource() {
        $.ajax({
            type: "GET",
            url: IPAddress + "/User/listAll",
            async: true,
            dataType: 'json',
            success: function(result, status) {
                  ge.resources = result;
            }
    });
}

function clearGantt() {
        ge.reset();
}

function loadI18n() {
        GanttMaster.messages = {
              "CANNOT_WRITE":                  "不可编辑",
              "CHANGE_OUT_OF_SCOPE":"NO_RIGHTS_FOR_UPDATE_PARENTS_OUT_OF_EDITOR_SCOPE",
              "START_IS_MILESTONE":"START_IS_MILESTONE",
              "END_IS_MILESTONE":"END_IS_MILESTONE",
              "TASK_HAS_CONSTRAINTS":"TASK_HAS_CONSTRAINTS",
              "GANTT_ERROR_DEPENDS_ON_OPEN_TASK":"GANTT_ERROR_DEPENDS_ON_OPEN_TASK",
              "GANTT_ERROR_DESCENDANT_OF_CLOSED_TASK":"GANTT_ERROR_DESCENDANT_OF_CLOSED_TASK",
              "TASK_HAS_EXTERNAL_DEPS":"TASK_HAS_EXTERNAL_DEPS",
              "GANTT_ERROR_LOADING_DATA_TASK_REMOVED":"GANTT_ERROR_LOADING_DATA_TASK_REMOVED",
              "ERROR_SETTING_DATES":"ERROR_SETTING_DATES",
              "CIRCULAR_REFERENCE":"CIRCULAR_REFERENCE",
              "CANNOT_DEPENDS_ON_ANCESTORS":"CANNOT_DEPENDS_ON_ANCESTORS",
              "CANNOT_DEPENDS_ON_DESCENDANTS":"CANNOT_DEPENDS_ON_DESCENDANTS",
              "INVALID_DATE_FORMAT":"日期格式错误",
              "TASK_MOVE_INCONSISTENT_LEVEL":"TASK_MOVE_INCONSISTENT_LEVEL",

              "GANTT_QUARTER_SHORT":"trim.",
              "GANTT_SEMESTER_SHORT":"sem."
        };
}


//-------------------------------------------  Get project file as JSON (used for migrate project from gantt to Teamwork) ------------------------------------------------------
function getFile() {
      $("#gimBaPrj").val(JSON.stringify(ge.saveProject()));
      $("#gimmeBack").submit();
      $("#gimBaPrj").val("");

      /*var uriContent = "data:text/html;charset=utf-8," + encodeURIComponent(JSON.stringify(prj));
      neww=window.open(uriContent,"dl");*/
}

// 添加0级的任务
function addLevelTask() {
      var factory = new TaskFactory();
      var temp = factory.build("tmp_" + new Date().getTime(), "", "", 0, new Date().getTime(), 1);

      var task = ge.addTask(temp, ge.tasks.length);

      task.rowElement.click();
      ge.editor.openFullEditor(task, task.rowElement);
}

function saveInDatabase() {
      // 先删除要删除的
      console.log(ge.deletedTaskIds);

      // 再保存和更新
      var prj = ge.saveProject();
      var datas = {
             pjtId: parseInt(GetQueryString("pid")),
             taskList: function() {
                    var arr = [];
                    for(var i=0; i<prj.tasks.length; i++) {
                           var temp = {
                        		 "pjtId": parseInt(GetQueryString("pid")),
                                 "tilte": prj.tasks[i].name,
                                 "level": prj.tasks[i].level,
                                 "planStart": new Date(prj.tasks[i].start).format("yyyy-MM-dd"),
                                 "planEnd": new Date(prj.tasks[i].end).format("yyyy-MM-dd"),
                                 "planTime": prj.tasks[i].duration,
                                 "hasChild": prj.tasks[i].hasChild,
                                 "frontNum": prj.tasks[i].depends == "" ? 0 : prj.tasks[i].depends,
                                 "resume": prj.tasks[i].description,
                                 "progress": prj.tasks[i].progress,
                                 "manager": prj.tasks[i].manager,
                                 "state": function() {
                                        var state = 0;
                                        if(typeof(prj.tasks[i].status) == "number") {
                                               state = prj.tasks[i].status;
                                        } else {
                                              switch(prj.tasks[i].status) {
                                                      case "STATUS_ACTIVE":
                                                            state = 10;
                                                            break;
                                                      case "STATUS_DONE":
                                                            state = 1;
                                                            break;
                                                      case "STATUS_FAILED":
                                                            state = 2;
                                                            break;
                                                      case "STATUS_SUSPENDED":
                                                            state = 3;
                                                            break;
                                                       case "STATUS_UNDEFINED":
                                                            state = 0;
                                                            break;
                                              }
                                        }
                                        return state;
                                 }(),
                                 "orderId": (prj.tasks[i].orderId == "" ||  prj.tasks[i].orderId == null) ? (ge.getTask(prj.tasks[i].id).getRow() + 1) : prj.tasks[i].orderId
                           }

                           var taskId = prj.tasks[i].id + "";
                           if(taskId.indexOf("_") <= -1) {
                                  temp.id = prj.tasks[i].id;
                           }

                           arr.push(temp);
                    }
                    return arr;
             }()
      }
      //console.log(datas);
      $.ajax({
            type: 'POST',
            url: IPAddress + 'Task/modifyBatch',
            contentType: 'application/json',
            data: JSON.stringify(datas),
            success: function(result) {
                    console.log(result);
            }
      });
}

function feedback(taskId) {
      var feedbackEditor = $.JST.createFromTemplate({}, "TASK_FEEDBACK");

      var task = ge.getTask(taskId);

      feedbackEditor.find("#start").val(new Date(task.start).format());
      feedbackEditor.find("#end").val(new Date(task.end).format());

      feedbackEditor.find("#start").click(function () {
            $(this).dateField({
                    inputField:$(this),
                    callback:  startChangeCallback
              });
      }).blur(function () {
            var inp = $(this);
            if (!Date.isValid(inp.val())) {
                    alert(GanttMaster.messages["INVALID_DATE_FORMAT"]);
                    inp.val(inp.getOldValue());
             } else {
                    startChangeCallback(Date.parseString(inp.val()))
            }
      });

      feedbackEditor.find("#end").click(function () {
             $(this).dateField({
                    inputField:$(this),
                    callback:  endChangeCallback
             });
      }).blur(function () {
             var inp = $(this);
             if (!Date.isValid(inp.val())) {
                    alert(GanttMaster.messages["INVALID_DATE_FORMAT"]);
                    inp.val(inp.getOldValue());
             } else {
                    endChangeCallback(Date.parseString(inp.val()))
             }
      });

      function startChangeCallback(date) {
             date.clearTime();
      }

      function endChangeCallback(end) {
             var start = Date.parseString(feedbackEditor.find("#start").val());
             end.setHours(23, 59, 59, 999);

             if (end.getTime() < start.getTime()) {
                    var dur = task.duration;
                    start = incrementDateByWorkingDays(end.getTime(), -dur);
                    feedbackEditor.find("#start").val(new Date(computeStart(start)).format());
             } else {
                    feedbackEditor.find("#duration").val(recomputeDuration(start.getTime(), end.getTime()));
             }
      }

      feedbackEditor.find("#saveButton").click(function () {
      });

      var ndo = createBlackPage(600, 500).append(feedbackEditor);
}

function editResources(){
        //make resource editor
        var resourceEditor = $.JST.createFromTemplate({}, "RESOURCE_EDITOR");
        var resTbl=resourceEditor.find("#resourcesTable");

        for (var i=0;i<ge.resources.length;i++){
              var res=ge.resources[i];
              resTbl.append($.JST.createFromTemplate(res, "RESOURCE_ROW"));
        }


        //bind add resource
        resourceEditor.find("#addResource").click(function(){
              resTbl.append($.JST.createFromTemplate({id:"new",name:"resource"}, "RESOURCE_ROW"));
        });

        //bind save event
        resourceEditor.find("#resSaveButton").click(function(){
              var newRes=[];
              //find for deleted res
              for (var i=0;i<ge.resources.length;i++){
                    var res=ge.resources[i];
                    var row = resourceEditor.find("[resId="+res.id+"]");
                    if (row.size()>0){
                          //if still there save it
                          var name = row.find("input[name]").val();
                          if (name && name!="") {
                                  res.name=name;
                          }
                          newRes.push(res);
                    } else {
                          //remove assignments
                          for (var j=0;j<ge.tasks.length;j++){
                                  var task=ge.tasks[j];
                                  var newAss=[];
                                  for (var k=0;k<task.assigs.length;k++){
                                          var ass=task.assigs[k];
                                          if (ass.resourceId!=res.id) {
                                                newAss.push(ass);
                                          }
                                  }
                                  task.assigs=newAss;
                          }
                    }
              }

              //loop on new rows
              resourceEditor.find("[resId=new]").each(function(){
                    var row = $(this);
                    var name = row.find("input[name]").val();
                    if (name && name!="") {
                        newRes.push (new Resource("tmp_"+new Date().getTime(),name));
                    }
              });

              ge.resources=newRes;

              closeBlackPopup();
              ge.redraw();
        });

        var box = resourceEditor.find(".box-body");
        box.css({height: 500 - 85, overflow: "auto"});
        var ndo = createBlackPage(400, 500).append(resourceEditor);
}