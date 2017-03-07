angular.module('platform').service("$tkProjectService", function(TKServerConfig, $tkRequestService){
	this.pageSize = 10;

	var projectsControllerUrl = TKServerConfig.controllerUrl + "Project"
	
	this.findByPage = function(page, size) {
		var additionalUrl = "/list?page="+page+"&size="+(size == null ? this.pageSize : size);
		
		return $tkRequestService.get(projectsControllerUrl + additionalUrl);
	}

	this.findById = function(pid) {
		var additionalUrl = "/get/"+pid;
		
		return $tkRequestService.get(projectsControllerUrl + additionalUrl);
	}

	this.saveProject = function(project) {
		var additionalUrl = "/add";
		
		return $tkRequestService.post(projectsControllerUrl + additionalUrl, project);
	}

	this.deleteProject = function(pid) {
		var additionalUrl = "/delete/"+pid;
		
		return $tkRequestService.post(projectsControllerUrl + additionalUrl);
	}

	this.updateProject = function(project) {
		var additionalUrl = "/modify";
		
		return $tkRequestService.post(projectsControllerUrl + additionalUrl, project);
	}
});