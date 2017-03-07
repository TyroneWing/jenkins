(function(document,$,angular){
	angular.element(document).ready(function() {
		// var roleId = window.sessionStorage.getItem("roleId");
		
		// $.ajax({
		//       url: '../capi/authorities/findMenusByRoleId/'+roleId,
		//       type: "GET",
		//       dataType: 'json'
	 //  	}).then(function(data) {
	  		var tmp = ['View'];
	  		// for(var i=0; i<data.length; i++) {
		   //  		 tmp.push(data[i].nameEN);
	    // 	 	}
	  		
	  		angular.module('platform').run(['$rootScope', 'angularPermission', function($rootScope, angularPermission){
				$rootScope.userPermissionList = tmp;
				//console.log($rootScope.userPermissionList);
		    	 	angularPermission.setPermissions($rootScope.userPermissionList);
			}]);
			 
			angular.bootstrap(document, ['platform']);
	  	// });
	});
})(document,jQuery,angular);