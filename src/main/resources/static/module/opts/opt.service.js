angular.module('platform').service("$tkOptService", function(TKServerConfig, $tkRequestService){
	this.pageSize = 10;

	var optsControllerUrl = TKServerConfig.controllerUrl + "Opt"



	this.findReminds = function() {
		var additionalUrl = "/listRemind/" + this.pageSize;

		return $tkRequestService.get(optsControllerUrl + additionalUrl);
	}
});