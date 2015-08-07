abstractControllers.controller('CuadroMandoController', ['$scope', '$http', function CuadroMandoController($scope, $http) {
    $scope.bar = {
        labels: ["Download Sales", "In-Store Sales", "Mail-Order Sales"],
        data: [
            [300, 500, 100]
        ],
        series: ['Series A']
    };

    $scope.pie = {
        labels: ["Download Sales", "In-Store Sales", "Mail-Order Sales"],
        data: [300, 500, 100],
        onclick: function(evt, points){
            // var activePoints = this.getSegmentsAtEvent(evt);
            // console.log(points);
            console.log(evt);
            console.log(evt[0].label);
            console.log(evt[0].value);

            angular.forEach(evt, function(value, key) {
              console.log(key + ': ' + value);
            });

            // => activePoints is an array of segments on the canvas that are at the same position as the click event.
        }
    };
}]);