angular.module('platform').controller('PlatformProjectController', function($scope, $location, $modal, $filter, $tkPopupService, $tkProjectService, $tkTaskService, $tkOptService) {
	$scope.pages = {
		currentPage: 0,
		pageSize: 10,
		pageGroup: 0
	}

	getProjects($scope.pages.currentPage, $scope.pages.pageSize);
	
	function getProjects(page, size) {
		$tkProjectService.findByPage(page, size).then(function(datas) {
			$scope.projects = [];
			updateProjectList(datas);
		});
	}

	function updateProjectList(datas) {
		$scope.projects = datas.dataList;
		
		$scope.pages.totalPages = datas.totalPages;
		$scope.pages.totalElements = datas.totalElements;
	}

	//--------------------------------------------- 页数改变---------------------------------------------//
	$scope.$watch('pages.currentPage + pages.pageSize', function() {
		if($scope.pages.totalElements != null) {
			getProjects($scope.pages.currentPage, $scope.pages.pageSize);
		}
	});
	
	//--------------------------------------------- 删除项目 ---------------------------------------------//
	$scope.deleteProject = function(index , pid) {
		$tkPopupService.popup("确定要删除该项目吗？").then(function() {
			$('#project-table').loader('show','<i class="fa fa-2x fa-spinner fa-spin"></i>');
			$tkProjectService.deleteProject(pid).then(function(result){
				$('#project-table').loader('hide');
				$tkPopupService.popup(result.obj);
				if(result.err == 0) {
					$scope.projects.splice(index, 1);
				}
			}, function(result) {
				$('#project-table').loader('hide');
				$tkPopupService.popup(result.obj);
			});
		});
	}

	//--------------------------------------------- 添加项目 ---------------------------------------------//
	$scope.addOrModifyProject = function(action, pid) {
		if(action == 'add') {
			$location.path("/platform/projects/addOrModify/add");
		} else {
			$location.path("/platform/projects/addOrModify/modify_"+pid);
		}
	}

	//--------------------------------------------- 查询任务提醒 ---------------------------------------------//
	$tkTaskService.findReminds().then(function(datas) {
		$scope.reminds = datas;
//		for(var i=0; i<$scope.reminds.length; i++) {
//			$scope.reminds[i].warningDays = DateDiff($scope.reminds[i].planEnd, new Date().Format("yyyy-MM-dd"));
//		}
	});

	//--------------------------------------------- 添加新需求 ---------------------------------------------//
	$scope.addDemand = function(projectId) {
		var dispatchTaskModalInstance = $modal.open({
	        	animation: true,
	        	templateUrl: './module/projects/project.add.demand.html',
	        	controller: 'PlatformProjectAddDemandController',
	        	size: 'md',
	        	backdrop: true,
	        	windowClass: 'custom-modal',
	        	resolve: {
	            	service: function () {
	                	if(!projectId) {
	                		return {id: ""};
	                	} else {
	                		return {id: projectId};
	                	}
	            	}
	        	}
		});

		dispatchTaskModalInstance.result.then(function () {
				// 执行save或update
		}, function () {
	    		// 窗口消失
	    		//$log.info('Modal dismissed at: ' + new Date());
		});
	}

	//--------------------------------------------- 查询任务提醒里面的任务 ---------------------------------------------//
	$scope.findTask = function(taskId) {
		$scope.task = $filter('filter')($scope.reminds, {id: taskId})[0];
		$scope.task.consumingTime = recomputeDuration($scope.task.planStart, $scope.task.planEnd);
		$('#projectDetail').modal('show');
	}
	
	//--------------------------------------------- 查询操作记录 ---------------------------------------------//
	$tkOptService.findBySize(4).then(function(datas) {
		$scope.opts = datas;
	});
});