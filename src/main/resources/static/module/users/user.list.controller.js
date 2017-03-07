angular.module('platform').controller('PlatformUsersController', function ($scope, $modal, $log, $timeout, $tkPopupService, $tkUtilService, $tkUsersService) {
	$scope.pages = {
		currentPage: 0,
		pageSize: 10,
        		pageGroup: 0
	}
	
	getUsers($scope.pages.currentPage);
	
	function getUsers(page){
		$scope.users = [];
		$tkUsersService.findByPage(page).then(function(users){
			updateUserShow(users);
		});
	}
	
	function updateUserShow(users) {
		$scope.users = users.content;
		
		$scope.pages.pageSize = users.size;
		$scope.pages.totalPages = users.totalPages;
		$scope.pages.totalElements = users.totalElements;
	}
	
	//--------------------------------------------- 删除用户---------------------------------------------//
	$scope.deleteUser = function(index , uid) {
		$tkPopupService.popup("确定要删除该用户吗？").then(function() {
			$('#user-table').loader('show','<i class="fa fa-2x fa-spinner fa-spin"></i>');
			$tkUsersService.deleteUser(uid).then(function(result){
				$('#user-table').loader('hide');
				$tkPopupService.popup(result.obj);
				if(result.err == 0) {
					$scope.users.splice(index, 1);
				} else{
				}
			}, function(result) {
				$('#user-table').loader('hide');
				$tkPopupService.popup(result.obj);
			});
		});
	}
	
	//--------------------------------------------- 新增或修改用户---------------------------------------------//
	$scope.addOrModifyUser = function(id) {
		var dispatchTaskModalInstance = $modal.open({
		            	animation: true,
		            	templateUrl: './module/users/user.detail.tpl.html',
		            	controller: 'PlatformUserDetailController',
		            	size: 'md',
		            	backdrop: true,
		            	windowClass: 'custom-modal',
		            	resolve: {
		                	service: function () {
			                	if(!id) {
			                		return {id: ""};
			                	} else {
			                		return $tkUsersService.findById(id);
			                	}
		                	}
		            	}
	        	});
		
		dispatchTaskModalInstance.result.then(function () {
			// 执行save或update
			$timeout(function() {
				getUsers($scope.pages.currentPage);
			}, 500);
	        	}, function () {
		        	// 窗口消失
		        	$log.info('Modal dismissed at: ' + new Date());
	        	});
	}
	
	//--------------------------------------------- 重置密码 ---------------------------------------------//
	$scope.changePwd = function(id) {
		var dispatchTaskModalInstance = $modal.open({
		            	animation: true,
		            	templateUrl: './module/users/user.change.pwd.tpl.html',
		            	controller: 'PlatformUserChangePwdController',
		            	size: 'md',
		            	backdrop: true,
		            	windowClass: 'custom-modal',
		            	resolve: {
		                	service: function () {
			                	if(!id) {
			                		return {id: ""};
			                	} else {
			                		return $tkUsersService.findById(id);
			                	}
		                	}
		            	}
	        	});
		
		dispatchTaskModalInstance.result.then(function () {
			// 执行save或update
			$timeout(function() {
				getUsers($scope.pages.currentPage);
			}, 500);
    	}, function () {
        	// 窗口消失
        	$log.info('Modal dismissed at: ' + new Date());
    	});
	}
	
	//--------------------------------------------- 页数改变---------------------------------------------//
	$scope.$watch('pages.currentPage + pages.pageSize', function() {
		if($scope.pages.totalElements != null) {
			getUsers($scope.pages.currentPage);
		}
	});
});