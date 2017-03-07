
angular.module('platform').controller('PlatformUserChangePwdController', function ($scope, $modalInstance, service, $tkUsersService, $tkPopupService, $tkRolesService, $tkDeptsService) {
	$scope.user = angular.copy(service);
	
	$scope.ok = function (user) {
		var errorCount = formVali(document.getElementById("userForm"));
		if(errorCount != 0){
	 		return false;
		}
		
		if(!user.id){
			
			
			
		} else {
			
		}
	};

	$scope.cancel = function () {
        	$modalInstance.dismiss('cancel');
	}
});