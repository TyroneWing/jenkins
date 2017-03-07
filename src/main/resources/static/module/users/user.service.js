angular.module('platform').service("$tkUsersService", function(TKServerConfig, $tkRequestService){
	this.pageSize = 10;

	var usersControllerUrl = TKServerConfig.controllerUrl + "User"

	// 用于用户详情
	this.findById = function(uid) {
		var additionalUrl = "/get/"+uid;
		
		return $tkRequestService.get(usersControllerUrl + additionalUrl);
	}

	this.findByPage = function(page, size) {
		var additionalUrl = "/list?page="+page+"&size="+(size == null ? this.pageSize : size);
		
		return $tkRequestService.get(usersControllerUrl + additionalUrl);
	}

	this.findAll = function() {
		var additionalUrl = "/listAll";

		return $tkRequestService.get(usersControllerUrl + additionalUrl);
	}
	
	this.deleteUser = function(uid) {
		var additionalUrl = "/delete/"+uid;
		
		return $tkRequestService.post(usersControllerUrl + additionalUrl);
	}
	
	this.saveUser = function(user){
		var additionalUrl = "/add";
		
		return $tkRequestService.post(usersControllerUrl + additionalUrl, user);
	}
	
	this.updateUser = function(user){
		var additionalUrl = "/modify";
		
		return $tkRequestService.post(usersControllerUrl + additionalUrl, user);
	}
});