angular.module('platform').directive('tnswitchery', function () {
    	return {
	        	restrict: 'E',
	        	template: '<input type="checkbox" class="js-check-change js-switch-small" />',
	        	replace: true,
	        	scope: {
	            		index: '=',
	            		checked: '=',
	            		onchange: '&'
	        	}, 

		link : function(scope, element, attrs, controller) {
		 	var id = element.attr("id");
		 	if (!id) {
			 	id = "t"+scope.index;
			 	 element.attr("id", id);
		 	}
			 
			var smallSelector = document.querySelector("#"+id);
		    	var smallSwitchery = new Switchery(smallSelector, {size: 'small'});
		    	
		    	smallSwitchery.setPosition(!scope.checked);
		    
		    	smallSelector.onchange = function() {
			    	if (scope.onchange != null) {
			    		scope.onchange();
			    	}
		    	};
		}
	};
});