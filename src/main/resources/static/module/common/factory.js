angular.module('platform').factory("$tkRequestService", function($http, $location, $q) {
	var factory = {};

	// http get 用于restful接口 查询操作
	factory.find = function(url, pageId, pageSize) {
		var additionalUrl = "";
		
		if (pageId >= 0 && pageSize > 0) {
			if(url.indexOf("?") != -1) {
				additionalUrl = "&page=" + pageId + "&size=" + pageSize;
			} else {
				additionalUrl = "?page=" + pageId + "&size=" + pageSize;
			}
			url = url + additionalUrl;
		}
		
		var deferred = $q.defer();
		
		$http.get(url).success(function (resp) {
		    	if (resp._embedded != null) {
		    		var temp = null;
		    		for(var key in resp._embedded) {
		    			var temp = {
    						datas: resp._embedded[key],
	    					page: resp.page
		    			};
		    			break;
		    		}
		    		deferred.resolve(temp);
		    	} else {
		    		deferred.resolve({
		    			datas: [],
		    			page: {
		    				size: pageSize,
		    				totalElements: 0
		    			}
		    		});
		    	}
	    	}).error(function (data) {
		    	deferred.reject(null);
		    	$location.path("/platform/error");
	    	});
		
		return deferred.promise;
	}

	// http post 用于restful接口 新增操作
	factory.save = function(url, object) {
		var deferred = $q.defer();

		$http.post(url, object).success(function() {
			deferred.resolve({code:0, msg:"添加成功！"});
		}).error(function() {
			deferred.reject({code:-1, msg:"添加失败！"});
		});

		return deferred.promise;
	}

	// http patch 用于restful接口 更新操作
	factory.update = function(url, object) {
		var deferred = $q.defer();

		$http.patch(url, object).success(function() {
			deferred.resolve({code:0, msg:"修改成功！"});
		}).error(function() {
			deferred.reject({code:0, msg:"修改失败！"});
		});

		return deferred.promise;
	}

	// http delete 用于restful接口 删除操作
	factory.deleted = function(url) {
		var deferred = $q.defer();
		
		$http.delete(url).success(function () {
	    		deferred.resolve({code:0, msg:"删除成功！"});
	    	}).error(function () {
		    	deferred.reject({code:0, msg:"删除失败！"});
	   	});
		
		return deferred.promise;
	}

	factory.get = function(url) {
		var deferred = $q.defer();

		$http.get(url).success(function (resp) {
    			deferred.resolve(resp);
    		}).error(function (data) {
	    		deferred.reject(null);
	    		$location.path("/platform/error");
	    	});
		
		return deferred.promise;
	}

	factory.post = function(url, object) {
		var deferred = $q.defer();

		if(object != null) {
			$http.post(url, object).success(function (resp) {
		    		deferred.resolve(resp);
		    	}).error(function (data) {
		    		deferred.reject(null);
			    	$location.path("/platform/error");
		    	});
		} else {
			$http.post(url).success(function (resp) {
	    			deferred.resolve(resp);
		    	}).error(function (data) {
		    		deferred.reject(null);
			    	$location.path("/platform/error");
		    	});
		}

	    	return deferred.promise;
	}
	
	return factory;
});