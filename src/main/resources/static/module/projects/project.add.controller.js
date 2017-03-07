angular.module('platform').controller('PlatformProjectAddOrModifyController', function ($scope, $routeParams, $filter, $location, $timeout, $tkPopupService, $tkFilesService, $tkProjectService, $tkDeptsService, $tkUsersService) {
	$scope.project = {};

	if($routeParams.param.indexOf("_") > -1) {
		$scope.projectId = $routeParams.param.split("_")[1];
		$tkProjectService.findById($scope.projectId).then(function(datas) {
			$scope.project = datas.project;

			for(var i=0; i<datas.pjtUsersList.length; i++) {
				selectedUser.push(datas.pjtUsersList[i].id);
				$scope.userList.push({
					userId: datas.pjtUsersList[i].id,
					name: datas.pjtUsersList[i].name
				});
			}
		});

		$timeout(function() {
			$('#realStart').daterangepicker({
				showDropdowns: true,
				autoApply: true,												
				singleDatePicker: true,
				minDate: moment(),
				opens: 'left',
				format : 'YYYY-MM-DD'
			});
			
			$('#realEnd').daterangepicker({
				showDropdowns: true,
				autoApply: true,
				singleDatePicker: true,
				minDate: moment(),
				opens: 'right',
				format : 'YYYY-MM-DD'
			});
		}, 1000);
		
	}

	//----------------------------------------------- 选择负责人 -----------------------------------------------//
	$tkUsersService.findAll().then(function(datas) {
		$scope.userOptions = datas;

		if($routeParams.param.indexOf("_") > -1) {
			$scope.manager = $filter('filter')(datas, {id: $scope.project.manager})[0];
		}
	});

	//----------------------------------------------- 按部门查询人员 -----------------------------------------------//
	$tkDeptsService.findAllWithUsers().then(function(datas) {
		$scope.groups = datas;
	});

	//----------------------------------------------- 选择人员 -----------------------------------------------//
	// 已选择人员
	var selectedUser = [];
	$scope.userList = [];

	$scope.updateSelection = function($event, userId, userName) {
		var checkbox = $event.target;
		var action = (checkbox.checked ? 'add' : 'remove');

		updateSelected(action, userId, userName);
	}

	var updateSelected = function(action, userId, userName) {
		if(action == 'add' && selectedUser.indexOf(userId) == -1) {
			selectedUser.push(userId);
			$scope.userList.push({
				userId: userId,
				name: userName
			});
		}

		if(action == 'remove' && selectedUser.indexOf(userId) != -1) {
			var index = selectedUser.indexOf(userId);
			selectedUser.splice(index,1);
			$scope.userList.splice(index,1);
		}
	}

	$scope.removeUser = function(index) {
		selectedUser.splice(index,1);
		$scope.userList.splice(index,1);
	}

	$scope.selectAll = function($event) {
		var checkbox = $event.target;
		var action = (checkbox.checked ? 'add' : 'remove');

		if(action == 'add' ) {
			for(var i=0; i<$scope.groups.length; i++) {
				for(j=0; j<$scope.groups[i].userList.length; j++) {
					selectedUser.push($scope.groups[i].userList[j].id);
					$scope.userList.push({
						userId: $scope.groups[i].userList[j].id,
						name: $scope.groups[i].userList[j].name
					});
				}
			}
		}

		if(action == 'remove') {
			selectedUser = [];
			$scope.userList = [];
		}
	}

	$scope.isSelected = function(userId) {
		return selectedUser.indexOf(userId) >= 0;
	}
	
	//----------------------------------------------- 上传文件 -----------------------------------------------//
	// 已上传文件
	$scope.fileList = [];
	
	$scope.doUpload = function(file) {
		var fileObj = document.getElementById(file);

		$tkFilesService.uploadFile(fileObj.files[0]).then(function(result){
			$tkPopupService.popup(result.msg);
			
			if(result.err == 0) {
				$scope.fileList.push({
					name: result.obj.name
				});
			}
			
			$('#uploadFile').modal('hide');
		});
	}

	//----------------------------------------------- 添加项目 -----------------------------------------------//
	$scope.addProject = function(project) {
		var datas = {
			project: {
				"code": project.code,
				"name": project.name,
				"level": 1,
				"state": 0,
				"manager": project.manager,
				"planStart": project.planStart,
				"planEnd": project.planEnd,
				"createTime": new Date().Format("yyyy-MM-dd hh:mm:ss"),
				"creator": 1,
				"resume": project.resume,
				"consumingTime": project.consumingTime
			},
			pjtUsersList: $scope.userList
		}

		if($scope.projectId == null) {
			$tkProjectService.saveProject(datas).then(function(result) {
				//console.log(result);
				$tkPopupService.popup(result.obj);
				if(result.err == 0) {
					$location.path("/platform/projects");
				}
			});
		} else {
			datas.project.id = $scope.projectId;
			$tkProjectService.updateProject(datas).then(function(result) {
				//console.log(result);
				$tkPopupService.popup(result.obj);
				if(result.err == 0) {
					$location.path("/platform/projects");
				}
			});
		}
	}

	//----------------------------------------------- 日期选择 -----------------------------------------------//
	$('#planStart').daterangepicker({
		showDropdowns: true,
		autoApply: true,												
		singleDatePicker: true,
		minDate: moment(),
		opens: 'left',
		format : 'YYYY-MM-DD'
	});
	
	$('#planEnd').daterangepicker({
		showDropdowns: true,
		autoApply: true,
		singleDatePicker: true,
		minDate: moment(),
		opens: 'right',
		format : 'YYYY-MM-DD'
	});

	$scope.$watch("project.planStart + project.planEnd", function() {
		if($scope.project.planStart != null) {
			$('#planEnd').daterangepicker({
				showDropdowns: true,
				autoApply: true,
				singleDatePicker: true,
				minDate: $scope.project.planStart,
				opens: 'right',
				format : 'YYYY-MM-DD'
			});
		}

		if($scope.project.planStart != null && $scope.project.planEnd != null) {
			// var numOfday = DateDiff($scope.project.planStart, $scope.project.planEnd);
			
			// $scope.project.consumingTime = numOfday + 1;
			$scope.project.consumingTime = recomputeDuration($scope.project.planStart, $scope.project.planEnd);
		}
	});
});