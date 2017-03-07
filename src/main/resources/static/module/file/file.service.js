angular.module('platform').service("$tkFilesService", function($http, $location, $q, TKServerConfig, $tkRequestService){
	
	var fileControllerUrl = TKServerConfig.controllerUrl + "Resource";
	
	this.uploadFile = function(file) {
		var deferred = $q.defer();
		
		var form = new FormData();
		var additionalUrl = "/fileUpload";
		
        form.append("file", file);
                
        $http.post(fileControllerUrl + additionalUrl, form, {headers: {'Content-Type': undefined}}).success(function (resp) {
	    		deferred.resolve(resp);
        }).error(function (resp) {
    		deferred.resolve([]);
    		$location.path("/platform/error");
        });
		
		return deferred.promise;
	}
	
	this.deleteFile = function(fileName) {
		var url = fileControllerUrl + "/delete/" + fileName;
		return $tkRequestService.deleted(url);
	}
	
	this.getFile = function(fileName) {
		var url = fileControllerUrl + "/download/" + fileName;
		return $tkRequestService.request(url, null, "GET");
	}
});