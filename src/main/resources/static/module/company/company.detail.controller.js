angular.module('platform').controller('PlatformCompanyDetailController', function ($scope, $routeParams, $location, $tkCompanyService, $tkLoginfoService) {
	var companyId = $routeParams.param;

	//--------------------------------------------- 客户信息 ---------------------------------------------//
	$tkCompanyService.findById(companyId).then(function(datas) {
		if(datas.err == 0) {
			$scope.company = datas.obj.company;
			$scope.salesmanList = datas.obj.companySalesmanList;
			$scope.linkmanList = datas.obj.companyLinkmanList;
			$scope.projectList = datas.obj.projectList;
			$scope.logList = datas.obj.userLoginfoList;
		} else {
			$scope.company = {};
			$scope.salesmanList = [];
			$scope.linkmanList = [];
			$scope.projectList = [];
			$scope.logList = [];
		}
	});
	
	//--------------------------------------------- 跟进记录 ---------------------------------------------//
//	$tkLoginfoService.findByList(companyId, null, null, "", "").then(function(datas) {
//		$scope.logList = [];
//		if(datas.err == 0) {
//			$scope.logList = datas.obj;
//		}
//	});

	//--------------------------------------------- 跳转到编辑页面 ---------------------------------------------//
	$scope.modifyCompany = function(cid) {
		$location.path("/platform/companies/addOrModify/modify_"+cid);
	}
});