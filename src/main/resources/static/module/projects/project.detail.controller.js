
angular.module('platform').controller('PlatformProjectDetailController', function ($scope, $routeParams, $location, $tkProjectService, $tkLoginfoService) {
	var projectId = $routeParams.param;

	$tkProjectService.findById(projectId).then(function(datas) {
		$scope.project = datas.project;
		$scope.userList = datas.pjtUsersList;
	});
	
	//----------------------------------------------- 需求列表 -----------------------------------------------//
	$tkLoginfoService.findByList(null, projectId, null, "", "").then(function(datas) {
		$scope.logList = [];
		if(datas.err == 0) {
			$scope.logList = datas.obj;
		}
	});

	$scope.modifyProject = function(pid) {
		$location.path("/platform/projects/addOrModify/modify_"+pid);
	}
});