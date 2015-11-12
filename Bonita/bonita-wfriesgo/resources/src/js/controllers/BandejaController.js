abstractControllers.controller('BandejaController', ['$scope', '$routeParams',
function BandejaController($scope, $routeParams) {
	$scope.tipo = $routeParams.tipo == undefined ? 'pendientes' : ($routeParams.tipo == 'asignacion' ? 'asignaci\u00F3n' : $routeParams.tipo);
	$scope.filtros = {};
	
	$scope.$watch(function(scope) { return scope.tipo }, function(newValue, oldValue) {
		$scope.visibleEstacion = newValue == 'pendientes';
		$scope.visibleNuevo = newValue != 'pendientes';
		console.log($scope);
    });
	
	var columnDefs = [
        {headerName: "Semf.", field: "url", width: 80, cellRenderer: function(params) {
            var resultElement = document.createElement("a");
            elements = angular.fromJson(params.value);
            resultElement.target="_top"
            resultElement.href = bonitaConfig.getBonitaUrl() + elements.url;
            resultElement.innerHTML = elements.value;
            return resultElement;
        }},
        {headerName: "Nro. Sol.", field: "num_doi_cliente", width: 100},
        {headerName: "Tarea", field: "nombre_cliente", width: 350},
        {headerName: "Cliente", field: "estado_solicitud", width: 150},
        {headerName: "RVGL", field: "num_rvgl", width: 180}
    ];

	$scope.gridPendientes = {
		columnDefs: columnDefs,
		rows: [{nombre_cliente: "dddd"}],
        rowSelection: 'single',
        angularCompileRows: true,
        localeText: {
            // for filter panel
            page: 'P\u00E1gina',
            more: 'm\u00E1s',
            to: 'a',
            of: 'de',
            next: '&rsaquo;',
            last: '&raquo;',
            first: '&laquo;',
            previous: '&lsaquo;'
        }
	};
}]);
