angular.module('platform').service("$tkPopupService", function($modal){
	this.popup = function(msg) {
	    var dispatchTaskModalInstance = $modal.open({
	                    animation: true,
	                    templateUrl: './module/popup/popup.tpl.html',
	                    controller: 'PlatformPopupController',
	                    size: 'sm',
	                    backdrop: false,
	                    windowClass: 'confirm-dialog',
	                    resolve: {
	                    	popup: function () {
	                                    	return {msg: msg};
	                                    }
	                    }
	    });
		
		return dispatchTaskModalInstance.result;
	}
});