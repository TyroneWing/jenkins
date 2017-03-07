angular.module('platform').directive('tkPagination',function(){
            return {
                            restrict: 'EA',
                            template:  '<ul class="pagination pagination-sm inline">'+
                                                        '<li ng-repeat="item in pageList track by $index">'+
                                                                        '<a ng-class="{\'bg-blue\': item == (pages.currentPage+1)}" ng-click="changePage(item)">{{ item }}</a>'+
                                                        '</li>'+
                                              '</ul>',
                            replace: true,
                            scope: {
                                            pages: '='
                            },
                            link: function(scope, element, attrs) {
                                            // 变更当前页
                                            scope.changePage = function(item){
                                                            if (item == "首页") {
                                                                        scope.firstPage();
                                                            } else if (item == "«") {
                                                                        scope.prevGroup();
                                                            } else if (item == "»") {
                                                                        scope.nextGroup();
                                                            } else if (item == "末页"){
                                                                        scope.lastPage();
                                                            } else {
                                                                        scope.pages.currentPage = item;
                                                                        scope.jumpToPage();
                                                            }
                                            };

                                            // pageList数组
                                            function getPagination() {
                                            				if(scope.pages == null) {
                                            					return;
                                            				}
                                            	
                                                            scope.pageList = [];

                                                            // 总页数
                                                            scope.totalPages = Math.ceil(scope.pages.totalElements / scope.pages.pageSize);
                                                            
                                                            // 只显示5个页码
                                                            scope.pageLength = 5;

                                                            if (scope.totalPages <= 0) {
                                                                        return scope.pageList.push["1"];
                                                            }
                                                            
                                                            if (scope.pages.pageGroup < 0) {
                                                                        scope.pages.pageGroup = 0;
                                                            }
                                                                
                                                            if(scope.pages.pageGroup > 0){
                                                                        scope.pageList.push("首页");
                                                                        scope.pageList.push("«");
                                                            }
                                                            
                                                            // 当前页组的页数
                                                            var curGroupPages = (scope.totalPages - scope.pages.pageGroup*scope.pageLength) > scope.pageLength ? scope.pageLength : (scope.totalPages - scope.pages.pageGroup * scope.pageLength);
                                                            
                                                            for (var i = 1; i <= curGroupPages; i++) {
                                                                        scope.pageList.push(scope.pages.pageGroup * scope.pageLength + i);
                                                            }
                                                            
                                                            if ((scope.totalPages - scope.pages.pageGroup*scope.pageLength) > scope.pageLength) {
                                                                        scope.pageList.push("»");
                                                                        scope.pageList.push("末页");
                                                            }
                                            }

                                            scope.jumpToPage = function(item) {
                                                            scope.pages.currentPage = scope.pages.currentPage - 1;
                                            }

                                            // prevGroup
                                            scope.prevGroup = function(){
                                                            scope.pages.pageGroup -= 1;
                                                            scope.pages.currentPage = scope.pages.pageGroup * scope.pageLength + scope.pageLength - 1;
                                            };

                                            // nextGroup
                                            scope.nextGroup = function(){
                                                           scope.pages.pageGroup += 1;
                                                           scope.pages.currentPage = scope.pages.pageGroup * scope.pageLength;
                                            };

                                            // firstPage
                                            scope.firstPage = function() {
                                                        scope.pages.pageGroup = 0;
                                                        scope.pages.currentPage = 0;
                                            }

                                            // lastPage
                                            scope.lastPage = function() {
                                                        scope.pages.pageGroup = Math.ceil(scope.totalPages / scope.pageLength) - 1;
                                                        scope.pages.currentPage = scope.totalPages  - 1;
                                            }
                   
                                            scope.$watch('pages.currentPage + pages.pageSize + pages.totalElements', getPagination);
                            }
            }
});