angular.module('platform').controller('PlatformCompanyAddLogController', function ($scope, $modalInstance, service, $tkPopupService, $tkLoginfoTypeService, $tkLoginfoService) {
	$scope.company = angular.copy(service);
	
	$tkLoginfoTypeService.findAll().then(function(result) {
		$scope.types = result;
		
		$scope.company.type = result[0].id;
	});
	
	$scope.ok = function (company) {
		var errorCount = formVali(document.getElementById("companyForm"));
		if(errorCount != 0){
	 		return false;
		}
		
		if(!company.id){
			$modalInstance.close();
		} else {
			var log = {
					userLoginfo: {
						companyId: company.id,
						title: company.title,
						resume: company.log,
						flag: company.type,
						time: new Date(),
						creator: window.sessionStorage.getItem("userName"),
						pjtId: 0,
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