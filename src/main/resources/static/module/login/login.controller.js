var login = angular.module('login', ['ngRoute', 'ui.bootstrap']);

angular.module('login').controller('LoginController', function($scope, $rootScope, $http,
	$location, $window) {
	
	$scope.keydown = function($event) {
		if ($event.keyCode == 13) {
			$scope.login();
		};
	}

	$scope.login = function() {
		$http.get("/capi/User/loginCheck?username=" + $scope.username + "&passwd=" + $scope.passwd).success(function(data) {
			if (data.obj != null) {
				$window.sessionStorage.setItem("userId", data.obj.id);
				$window.sessionStorage.setItem("userName", data.obj.name);
				//$window.sessionStorage.setItem("userRealName", data.obj.realName);
				//$window.sessionStorage.setItem("userLevel", data.obj.userLevel);
				$window.sessionStorage.setItem("roleId", data.obj.roleId);

//				    var Days = 30; 
//				    var exp = new Date(); 
//				    exp.setTime(exp.getTime() + Days*24*60*60*1000); 
//				    document.cookie =  + "="+ data.obj.id; 
			}
			
			if (data.msg == "error") {
				$scope.passwdHelpBlock = "用户名不存在/密码错误";
			} else if (data.msg == "locked") {
				$scope.passwdHelpBlock = "该用户已被锁定，请联系管理员";
			// } else if (data.msg == "验证码错误") {
			// 	$('#verifyCode').attr("src","../../capi/users/verifyCode?'"+Math.random());
			// 	$scope.verifyCode = "";
			// 	$scope.verifyCodeHelpBlock = "验证码错误";
			} else {

				$window.location.href = "./index.html#/platform/projects";
			}
		});
	}

	$scope.jumpUrl = function(url) {
		$window.location.href = url;
	}
	
	$scope.refresh = function($event) {
	}
});