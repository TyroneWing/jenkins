
angular.module('platform').controller('PlatformUserDetailController', function ($scope, $modalInstance, service, $tkUsersService, $tkPopupService, $tkRolesService, $tkDeptsService) {
	$scope.user = angular.copy(service);

	//----------------------------------------------- 性别列表 -----------------------------------------------//
	$scope.sexOptions = [{
		value: true,
		name: '男'
	}, {
		value: false,
		name: '女'
	}];

	if(service.id == "") {
		$scope.user.sex = true;
	}

	//----------------------------------------------- 角色列表 -----------------------------------------------//
	$tkRolesService.findAll().then(function(datas) {
		$scope.roleOptions = datas;

		if(service.id == "") {
			$scope.user.roleId = $scope.roleOptions[$scope.roleOptions.length - 1].id;
		}
	});

	//----------------------------------------------- 部门列表 -----------------------------------------------//
	$tkDeptsService.findAll().then(function(datas) {
		$scope.deptOptions = datas;
	});

	$scope.ok = function (user) {
		var errorCount = formVali(document.getElementById("userForm"));
		if(errorCount != 0){
	 		return false;
		}
		
		if(!user.id){
			//console.log(myuser);
			delete user.id;
			var u = angular.copy(user);
			u.code = 0;
			
			$tkUsersService.saveUser(u).then(function(result){
				if(result.err == 0) {
					$tkPopupService.popup("新增成功");
				} else {
					$tkPopupService.popup("新增失败");
				}

				$modalInstance.close();
			});
		} else {
			// 修改用户
			$tkUsersService.updateUser(user).then(function(result){
				if(result.err == 0) {
					$tkPopupService.popup("修改成功");
				} else {
					$tkPopupService.popup("修改失败");
				}

				$modalInstance.close();
			});
		}
    	};

    	$scope.cancel = function () {
	        	$modalInstance.dismiss('cancel');
    	}
});