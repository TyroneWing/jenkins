angular.module('platform').service("$tkDeptsService", function(TKServerConfig, $tkRequestService){
	this.pageSize = 10;

	var deptsControllerUrl = TKServerConfig.controllerUrl + "Dept"

	this.findAll = function() {
		var additionalUrl = "/listAll";

		return $tkRequestService.get(deptsControllerUrl + additionalUrl);
	}

	this.findAllWithUsers = function() {
		var additionalUrl = "/listDeptUsers";

		return $tkRequestService.get(deptsControllerUrl + additionalUrl);
	}
});