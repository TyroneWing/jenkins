angular.module('platform').service("$tkCompanyService", function(TKServerConfig, $tkRequestService){
	this.pageSize = 10;

	var companyControllerUrl = TKServerConfig.controllerUrl + "Company"

	this.findByPage = function(page, size) {
		var additionalUrl = "/list?page="+page+"&size="+(size == null ? this.pageSize : size)+"&showLoginfo=false";

		return $tkRequestService.get(companyControllerUrl + additionalUrl);
	}
	
	this.findById = function(cid) {
		var additionalUrl = "/getInfo/"+cid;
		
		return $tkRequestService.get(companyControllerUrl + additionalUrl);
	}
	
	this.saveCompany = function(company){
		var additionalUrl = "/add";
		
		return $tkRequestService.post(companyControllerUrl + additionalUrl, company);
	}
	
	this.updateCompany = function(company){
		var additionalUrl = "/modify";
		
		return $tkRequestService.post(companyControllerUrl + additionalUrl, company);
	}
	
	this.deleteCompany = function(cid) {
		var additionalUrl = "/delete/"+cid;
		
		return $tkRequestService.post(companyControllerUrl + additionalUrl);
	}
});