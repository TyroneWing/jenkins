angular.module('platform').controller('PlatformCompaniesController', function ($scope, $modal, $log, $timeout, $location, $tkPopupService, $tkCompanyService) {
	$scope.pages = {
		currentPage: 0,
		pageSize: 10,
        pageGroup: 0
	}
	
	getCompanies($scope.pages.currentPage);
	
	function getCompanies(page){
		$scope.companies = [];
		$tkCompanyService.findByPage(page).then(function(companies){
			updateCompanyShow(companies);
		});
	}
	
	function updateCompanyShow(companies) {
		$scope.companies = companies.dataList;
		
		$scope.pages.totalPages = companies.totalPages;
		$scope.pages.totalElements = companies.totalElements;
	}
	
	//--------------------------------------------- 删除客户 ---------------------------------------------//
	$scope.deleteCompany = function(index , cid) {
		$tkPopupService.popup("确定要删除该客户吗？").then(function() {
			$('#company-table').loader('show','<i class="fa fa-2x fa-spinner fa-spin"></i>');
			$tkCompanyService.deleteCompany(cid).then(function(result){
				$('#company-table').loader('hide');
				$tkPopupService.popup(result.obj);
				if(result.err == 0) {
					$scope.companies.splice(index, 1);
				} else{
				}
			}, function(result) {
				$('#company-table').loader('hide');
				$tkPopupService.popup(result.obj);
			});
		});
	}
	
	//--------------------------------------------- 新增或修改客户---------------------------------------------//
	$scope.addOrModifyCompany = function(action, pid) {
		if(action == 'add') {
			$location.path("/platform/companies/addOrModify/add");
		} else {
			$location.path("/platform/companies/addOrModify/modify_"+pid);
		}
	}
	
	//--------------------------------------------- 页数改变---------------------------------------------//
	$scope.$watch('pages.currentPage + pages.pageSize', function() {
		if($scope.pages.totalElements != null) {
			getCompanies($scope.pages.currentPage);
		}
	});
	
	//--------------------------------------------- 添加跟进记录 ---------------------------------------------//
	$scope.addLog = function(companyId) {
		var dispatchTaskModalInstance = $modal.open({
	        	animation: true,
	        	templateUrl: './module/company/company.add.log.html',
	        	controller: 'PlatformCompanyAddLogController',
	        	size: 'md',
	        	backdrop: true,
	        	windowClass: 'custom-modal',
	        	resolve: {
	            	service: function () {
	                	if(!companyId) {
	                		return {id: ""};
	                	} else {
	                		return {id: companyId};
	                	}
	            	}
	        	}
		});

		dispatchTaskModalInstance.result.then(function () {
				// 执行save或update
		}, function () {
	    		// 窗口消失
	    		//$log.info('Modal dismissed at: ' + new Date());
		});
	}
});