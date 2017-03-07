//var app = angular.module('platform', ['ngRoute', 'ui.bootstrap', 'ngSanitize']);

'use strict';

angular.module('platform').config(['$routeProvider', function($routeProvider) {
	$routeProvider
		// projects
		.when('/platform/projects', {
			templateUrl: 'module/projects/project.list.html',
        			controller: 'PlatformProjectController'
		})
		.when('/platform/projects/addOrModify/:param', {
			templateUrl: 'module/projects/project.add.html',
        			controller: 'PlatformProjectAddOrModifyController'
		})
		.when('/platform/projects/detail/:param', {
			templateUrl: 'module/projects/project.detail.html',
        			controller: 'PlatformProjectDetailController'
		})
		// company
		.when('/platform/companies', {
			templateUrl: 'module/company/company.list.html',
        			controller: 'PlatformCompaniesController'
		})
		.when('/platform/companies/addOrModify/:param', {
			templateUrl: 'module/company/company.add.html',
        			controller: 'PlatformCompanyAddOrModifyController'
		})
		.when('/platform/companies/detail/:param', {
			templateUrl: 'module/company/company.detail.html',
        			controller: 'PlatformCompanyDetailController'
		})
		// user
		.when('/platform/users', {
			templateUrl: 'module/users/user.list.html',
        			controller: 'PlatformUsersController'
		})
		// error
		.when('/platform/error', {
			templateUrl: 'module/error/error.html'
		})
		// unauthority
		.when('/platform/unauthorized', {
	    		templateUrl: 'module/error/unauthority.html'
	    	}) 
		/*.otherwise({
			redirectTo: '/platform/relatimemonitor/water'
		})*/
}]);

angular.module('platform').run(['$rootScope', '$window', '$location', '$log', function ($rootScope, $window, $location, $log) {
	var locationChangeStartOff = $rootScope.$on('$locationChangeStart', locationChangeStart);

    	function locationChangeStart(event) {
    		$rootScope.$broadcast('changePageEvent', "");
    	}
}]);
