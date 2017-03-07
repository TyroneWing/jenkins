angular.module('platform').controller('PlatformPopupController', function ($scope, $modalInstance, popup) {
	$scope.msg = popup.msg;
	
	$scope.ok = function () {
		$modalInstance.close();
   	 };

	$scope.cancel = function () {
    		$modalInstance.dismiss('cancel');
	}
});