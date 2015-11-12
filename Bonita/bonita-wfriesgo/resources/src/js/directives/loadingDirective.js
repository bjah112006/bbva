'use strict';

bonitaApp.directive('loading', ['$http', '$timeout', function ($http, $timeout) {
    return {
        restrict: 'A',
        link: function (scope, elm, attrs) {
            scope.isLoading = function () {
                return $http.pendingRequests.length > 0;
            };

            scope.$watch(scope.isLoading, function (v) {
				var initLoader = window.parent.document.getElementById("initloader");
                if(v){
					$timeout(function(){
						if(initLoader != null) {
							initLoader.style.display = "block";
						}
					}, 0);
                    elm.removeClass("hide");
                }else{
					$timeout(function(){
						if(initLoader != null) {
							initLoader.style.display = "none";
						}
					}, 0);
                    elm.addClass("hide");
                }
            });
        }
    };
}]);