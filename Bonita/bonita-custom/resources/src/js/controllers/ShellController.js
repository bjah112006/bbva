abstractControllers.controller('ShellController', ['$scope', '$resource', 'bonitaConfig',
function ShellController($scope, $resource, bonitaConfig) {
    $scope.comando = "";
    $scope.response = [];
    
    $scope.tipoShell = {select: {value: "query", action: "queryDDL"}};
    
    $scope.tipoShells = [
        {"value": "sh", action: "shell"},
        {"value": "query", action: "queryDDL"}
    ];
    
    var container = window.parent.document.getElementById("panelAngular");
    if(container != null) {
        $scope.witdh = (container.offsetWidth - 135) + "px";
    } else {
        $scope.witdh = "90%";
    }
    
    $scope.ejecutar = function() {
        if($scope.comando.length != 0) {
            var Shell = $resource(bonitaConfig.getBonitaUrl() + '/API/extension/' + $scope.tipoShell.select.action);
            Shell.save({"command": $scope.comando}).$promise.then(function(request){ // .replace(/\n/g, " ").replace(/\t/g, "")
               $scope.response = request.data.lineas;
            });
        } else {
            $scope.response = ["Comando vacio"];
        }
    };
}]);