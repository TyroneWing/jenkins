angular.module('platform').service("$tkLoginfoService", function(TKServerConfig, $tkRequestService){
	this.pageSize = 10;

	var workControllerUrl = TKServerConfig.controllerUrl + "UserLoginfo"
	
	this.findByList = function(companyId, pjtId, taskId, creator, flag) {
		var additionalUrl = "/listFromParam?companyId="+(companyId == null ? 0 : companyId)+"&pjtId="+(pjtId == null ? 0 : pjtId)
		+"&taskId="+(taskId == null ? 0 : taskId)+"&creator="+creator+"&flag="+flag;
		
		return $tkRequestService.get(workControllerUrl + additionalUrl);
	}
	
	this.findById = function(lid) {
		var additionalUrl = "/get/"+lid;
		
		return $tkRequestService.get(workControllerUrl + additionalUrl);
	}
	
	this.saveLog = function(log){
		var additionalUrl = "/add";
		
		return $tkRequestService.post(workControllerUrl + additionalUrl, log);
	}
	
	this.updateLog = function(log){
		var additionalUrl = "/modify";
		
		return $tkRequestService.post(workControllerUrl + additionalUrl, log);
	}
	
	this.deleteLog = function(lid) {
		var additionalUrl = "/delete/"+lid;
		
		return $tkRequestService.post(workControllerUrl + additionalUrl);
	}
});