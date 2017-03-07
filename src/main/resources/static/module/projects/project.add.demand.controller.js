angular.module('platform').controller('PlatformProjectAddDemandController', function ($scope, $modalInstance, service, $tkPopupService, $tkLoginfoService) {
	$scope.project = angular.copy(service);

	$scope.ok = function (project) {
		var errorCount = formVali(document.getElementById("projectForm"));
		if(errorCount != 0){
	 		return false;
		}
		
		if(!project.id){
			$modalInstance.close();
		} else {
			var log = {
					userLoginfo: {
						pjtId: project.id,
						title: project.title,
						resume: project.demand,
						flag: 100,
						time: new Date(),
						creator: window.sessionStorage.getItem("userName"),
						companyId: 0,
						linkmanId: 0,
						planResume: "",
						taskId: 0,
						userLoginfokey: ""
					},
					upFileList: []
			}
			$tkLoginfoService.saveLog(log).then(function(result) {
				$modalInstance.close();
				
				$tkPopupService.popup(result.msg);
			});
    	};
	}
	
	$scope.cancel = function () {
    	$modalInstance.dismiss('cancel');
	}
});