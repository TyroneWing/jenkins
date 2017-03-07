angular.module('platform').controller('PlatformCompanyAddOrModifyController', function ($scope, $routeParams, $filter, $location, $timeout, 
		$tkPopupService, $tkUsersService, $tkProjectService, $tkCompanyService) {
	$scope.company = {};

	//----------------------------------------------- 客户类型 -----------------------------------------------//
	$scope.typeOptions = [{
		id: 1,
		name: '本公司'
	}, {
		id: 2,
		name: '客户'
	}, {
		id: 3,
		name: '供应商'
	}];
	
	//----------------------------------------------- 行业类型 -----------------------------------------------//
	$scope.industryOptions = [{
		id: 0,
		name: '无'
	}, {
		id: 1,
		name: '环保业'
	}, {
		id: 2,
		name: '化工业'
	}, {
		id: 3,
		name: '机械制造业'
	}, {
		id: 4,
		name: '医疗业'
	}];
	
	//----------------------------------------------- 判断是新增还是修改 -----------------------------------------------//
	if($routeParams.param.indexOf("_") > -1) {
		$scope.companyId = $routeParams.param.split("_")[1];
		
		$tkCompanyService.findById($scope.companyId).then(function(datas) {
			$scope.company = datas.obj.company;
			$scope.salesmanList = datas.obj.companySalesmanList;
			$scope.linkmanList = datas.obj.companyLinkmanList;
		});
	} else {
		$scope.company.flag = $scope.typeOptions[0].id;
		$scope.company.industryId = $scope.industryOptions[1].id;
	};
	
	//----------------------------------------------- 选择联系人 -----------------------------------------------//
	$scope.linkmanList = [];
	$scope.addLinkman = function(linkman) {
		$scope.linkmanList.push({
			name: linkman.name,
			mobile: linkman.mobile,
			main: function() {
				if($scope.linkmanList.length > 0) {
					return false;
				} else {
					return true;
				}
			}()
		});
		
		$('#linkman-name').val("");
		$('#linkman-tel').val("");
	}
	
	$scope.removeLinkman = function(index) {
		$scope.linkmanList.splice(index, 1);
	}
	
	//----------------------------------------------- 选择跟进人 -----------------------------------------------//
	$scope.salesmanList = [];
	$tkUsersService.findAll().then(function(datas) {
		$scope.users = datas;
	});
	
	$scope.addSalesman = function(id, name, mobile) {
		$scope.salesmanList.push({
			userId: id,
			name: name,
			mobile: mobile,
			main: function() {
				if($scope.salesmanList.length > 0) {
					return false;
				} else {
					return true;
				}
			}()
		});
		
		for(var i=0; i<$scope.users.length; i++) {
			if($scope.users[i].id == id) {
				$scope.users.splice(i, 1);
			}
		}
		
		$('#linkman-name').val("");
		$('#linkman-tel').val("");
	}
	
	$scope.removeSalesman = function(index) {
		$scope.users.push($scope.salesmanList[index]);
		
		$scope.salesmanList.splice(index, 1);
	}
	
	//----------------------------------------------- 关联项目 -----------------------------------------------//
	$scope.pages = {
			currentPage: 0,
			pageSize: 3,
			pageGroup: 0
	}
	
	getProjects($scope.pages.currentPage, $scope.pages.pageSize);
	
	function getProjects(page, size) {
		$tkProjectService.findByPage(page, size).then(function(datas) {
			$scope.projects = [];
			updateProjectList(datas);
		});
	}

	function updateProjectList(datas) {
		$scope.projects = datas.dataList;
		
		$scope.pages.totalPages = datas.totalPages;
		$scope.pages.totalElements = datas.totalElements;
	}
	
	$scope.$watch('pages.currentPage + pages.pageSize', function() {
		if($scope.pages.totalElements != null) {
			getProjects($scope.pages.currentPage, $scope.pages.pageSize);
		}
	});
	
	var selectedProject = [];
	$scope.projectList = [];
	$scope.updateSelection = function($event, projectId, projectName) {
		var checkbox = $event.target;
		var action = (checkbox.checked ? 'add' : 'remove');

		updateSelected(action, projectId, projectName);
	}

	var updateSelected = function(action, projectId, projectName) {
		if(action == 'add' && selectedProject.indexOf(projectId) == -1) {
			selectedProject.push(projectId);
			$scope.projectList.push({
				id: projectId,
				name: projectName
			});
		}

		if(action == 'remove' && selectedProject.indexOf(projectId) != -1) {
			var index = selectedProject.indexOf(projectId);
			selectedProject.splice(index,1);
			$scope.projectList.splice(index,1);
		}
	}
	
	$scope.isSelected = function(projectId) {
		return selectedProject.indexOf(projectId) >= 0;
	}
	
	//----------------------------------------------- 添加客户 -----------------------------------------------//
	$scope.addCompany = function(company) {
		if($scope.linkmanList.length <= 0) {
			$tkPopupService.popup("客户联系人不能为空");
			return false;
		}
		
		if($scope.salesmanList.length <= 0) {
			$tkPopupService.popup("客户跟进人不能为空");
			return false;
		}
		
		var datas = {
				company: {
					name: company.name,
					www: (company.www == null ? "" : company.www),
					areaCode: (company.areaCode == null ? "" : company.areaCode),
					code: "",
					shortName: "",
					address: (company.address == null ? "" : company.address),
					email: (company.email == null ? "" : company.email),
					fax: (company.email == null ? "" : company.email),
					tel: (company.tel == null ? "" : company.tel),
					industryId: company.industryId,
					flag: company.flag,
					creator: window.sessionStorage.getItem("userName"),
					resume: (company.resume == null ? "" : company.resume)
				},
				companyLinkmanList: $scope.linkmanList,
				companySalesmanList: $scope.salesmanList
		}
		
		if($scope.companyId == null) {
			$tkCompanyService.saveCompany(datas).then(function(result) {
				//console.log(result);
				$tkPopupService.popup(result.msg);
				if(result.err == 0) {
					$location.path("/platform/companies");
				}
			});
		} else {
			datas.company.id = $scope.companyId;
			$tkCompanyService.updateCompany(datas).then(function(result) {
				//console.log(result);
				$tkPopupService.popup(result.msg);
				
				if(result.err == 0) {
					$location.path("/platform/companies");
				}
			});
		}
	};
});