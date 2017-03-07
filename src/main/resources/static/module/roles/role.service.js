angular.module('platform').service("$tkRolesService", function(TKServerConfig, $tkRequestService){
	this.pageSize = 10;

	var rolesControllerUrl = TKServerConfig.controllerUrl + "Role"

	this.findAll = function() {
		var additionalUrl = "/listAll";

		return $tkRequestService.get(rolesControllerUrl + additionalUrl);
	}
});