angular.module('platform').service("$tkOptService", function(TKServerConfig, $tkRequestService){
	this.pageSize = 10;

	var optsControllerUrl = TKServerConfig.controllerUrl + "Opt"
	
	this.findBySize = function(size) {
		var additionalUrl = "/listHome/"+size;
		
		return $tkRequestService.get(optsControllerUrl + additionalUrl);
	}
});