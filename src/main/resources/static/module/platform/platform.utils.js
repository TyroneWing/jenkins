angular.module('platform').constant('TKServerConfig', {
	controllerUrl: "../capi/"
});

angular.module('platform').service("$tkUtilService", function() {	
	this.getGroupPageBtns = function(groupId, total) {
		var groupPageBtns = [];
		
		if (total <= 0) {
			return groupPageBtns;
		}
		
		if (groupId < 0) {
			groupId = 0;
		}
		
		var maxGroupId = Math.ceil(total / 5) - 1;
		
		if (groupId > maxGroupId) {
			groupId = maxGroupId;
		}
		
		if(groupId > 0){
			groupPageBtns.push("首页");
		}
		
		if (groupId > 0) {
			groupPageBtns.push("«");
		}
		
		var curGroupPages = (total - groupId*5) > 5 ? 5 : (total - groupId*5);
		
		for (var i = 1; i <= curGroupPages; i++) {
			groupPageBtns.push(groupId*5 + i);
		}
		
		if ((total - groupId*5) > 5) {
			groupPageBtns.push("»");
			groupPageBtns.push("末页");
		}
		
		return groupPageBtns;
	}
});