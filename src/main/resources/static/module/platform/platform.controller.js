angular.module('platform').controller('PlatformController', function($scope, $rootScope, $compile, $http, $window, $tkPopupService) {
	$scope.userName = $window.sessionStorage.getItem("userName");
	
	//--------------------------------------------- 退出登录 ---------------------------------------------//
	$scope.logout = function() {
		$tkPopupService.popup("确定要退出吗？").then(function() {
			$http.get("/capi/User/logout").success(function(data) {
				$window.location.href = "./login.html";
				
				$window.sessionStorage.removeItem("userId");
				$window.sessionStorage.removeItem("userName");
				$window.sessionStorage.removeItem("userRealName");
				$window.sessionStorage.removeItem("userLevel");
				
				$scope.conn.clear();
				$scope.conn.close();
	        }).error(function (data) {
	        	//do nothing
	        });
		});
	}
	
	//--------------------------------------------- 水气切换 ---------------------------------------------//
	/*$rootScope.mediumTypeId = "1";
	$rootScope.mediumTypeName = "water";
	
	$scope.changeMediumType = function(type) {
		$tkMediumTypeService.getMediumType(type).then(function(data) {
			if(data != null) {
				if(data.id != null) {
					$rootScope.mediumTypeId = data.id;
					$rootScope.mediumTypeName = type;
				} else {
					$tkPopupService.popup("没有对应的类型");
				}
			} else {
				$tkPopupService.popup("没有对应的类型");
			}
		});
	}*/	
});