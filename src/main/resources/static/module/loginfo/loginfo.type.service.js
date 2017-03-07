angular.module('platform').service("$tkLoginfoTypeService", function(TKServerConfig, $tkRequestService){
	this.pageSize = 10;

	var logTypeControllerUrl = TKServerConfig.controllerUrl + "UserLogType"
	
	this.findAll = function() {
		var additionalUrl = "/list";
		
		return $tkRequestService.get(logTypeControllerUrl + additionalUrl);
	}
});