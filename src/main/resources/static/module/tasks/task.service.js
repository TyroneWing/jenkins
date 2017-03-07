angular.module('platform').service("$tkTaskService", function(TKServerConfig, $tkRequestService){
	this.pageSize = 10;

	var tasksControllerUrl = TKServerConfig.controllerUrl + "Task"

	// 用于用户详情
	this.findById = function(uid) {
		var additionalUrl = "/get/"+uid;
		
		return $tkRequestService.get(tasksControllerUrl + additionalUrl);
	}

	this.findByPage = function(page, size) {
		var additionalUrl = "/list?page="+page+"&size="+(size == null ? this.pageSize : size);
		
		return $tkRequestService.get(tasksControllerUrl + additionalUrl);
	}

	this.findByProject = function(projectId) {
		var additionalUrl = "/listPjtTask/" + projectId;

		return $tkRequestService.get(tasksControllerUrl + additionalUrl);
	}
	
	this.deleteTask = function(uid) {
		var additionalUrl = "/delete/"+uid;
		
		return $tkRequestService.post(tasksControllerUrl + additionalUrl);
	}
	
	this.saveTask = function(task){
		var additionalUrl = "/add";
		
		return $tkRequestService.post(tasksControllerUrl + additionalUrl, task);
	}
	
	this.updateTask = function(task){
		var additionalUrl = "/modify";
		
		return $tkRequestService.post(tasksControllerUrl + additionalUrl, task);
	}

	this.findReminds = function() {
		var additionalUrl = "/listRemind/" + this.pageSize;

		return $tkRequestService.get(tasksControllerUrl + additionalUrl);
	}
});