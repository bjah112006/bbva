'use strict';

bonitaApp.directive('loading', ['$http', '$timeout', function ($http, $timeout) {
    return {
        restrict: 'A',
        link: function (scope, elm, attrs) {
            scope.isLoading = function () {
                return $http.pendingRequests.length > 0;
            };

            scope.$watch(scope.isLoading, function (v) {
                if(v){
					$timeout(function(){
						window.parent.document.getElementById("initloader").style.display = "block";
					}, 0);
                    elm.removeClass("hide");
                }else{
					$timeout(function(){
						window.parent.document.getElementById("initloader").style.display = "none";
					}, 0);
                    elm.addClass("hide");
                }
            });
        }
    };
}]);