abstractControllers.controller('ConsultaSolicitudController', ['$scope', '$http', 'ConsultaSolicitudes', 'bonitaConfig', 'DateUtil',
function ConsultaSolicitudController($scope, $http, ConsultaSolicitudes, bonitaConfig, DateUtil) {
	$scope.tiposDocumento = [
		{"id": "rootprocessinstanceid", "name": "Número Solicitud"},
		{"id": "num_doi_cliente", "name": "Número DOI Cliente"},
		{"id": "num_rvgl", "name": "Número RVGL"},
		{"id": "num_tramite", "name": "Número Solicitud Pre-Impreso"}
	];
    $scope.estaciones = [
        {"id": "OFICINA", "name": "Oficina"},
        {"id": "FUVEX", "name": "Fuvex"},
        {"id": "MESA DE CONTROL", "name": "Mesa Control"},
        {"id": "RIESGO", "name": "Riesgo"},
        {"id": "CPM", "name": "CPM"}
    ];
    $scope.nroDocumento = '';
    $scope.tipoFiltro = 'tiposDocumento';
    $scope.tipoDocumento = {};
    $scope.estacion = {};
    $scope.disabled = {
        tipoDocumento: false,
        estacion: true
    };

    $scope.$watch(function(scope) { return scope.tipoFiltro }, function(newValue, oldValue) {
        $scope.disabled.tipoDocumento = (newValue == 'estacion');
        $scope.disabled.estacion = (newValue == 'tiposDocumento');

		if(newValue == 'estacion') {
			$scope.nroDocumento = '';
    		$scope.tipoDocumento = {};
		} else {
			$scope.estacion = {};
		}
    });

	var columnDefs = [
        {headerName: "N° Solicitud", field: "rootprocessinstanceid", width: 80, cellRenderer: function(params) {
            var resultElement = document.createElement("a");

/*
            		if(data.solicitudes[i].idArchivada!=""){
            			row += '<tr><td><a href="' + obtenerContexto('homepage') + '?id=' + data.solicitudes[i].idArchivada + '&_p=' + data.solicitudes[i].variable + '&_pf=1">' + data.solicitudes[i].nroSolicitud + '</a></td>';
            		}else{
            			row += '<tr><td><a href="' + obtenerContexto('homepage') + '?id=' + data.solicitudes[i].nroSolicitud + '&_p=' + data.solicitudes[i].variable + '&_pf=1">' + data.solicitudes[i].nroSolicitud + '</a></td>';
            		}
*/
			resultElement.target="top"
			resultElement.href = bonitaConfig.getBonitaUrl() + "/portal/homepage?id=" + params.value + '&_p=casemoredetails&_pf=1';
			resultElement.innerHTML = params.value;
            return resultElement;
        }},
		{headerName: "RUC", field: "num_doi_cliente", width: 100},
        {headerName: "Raz\u00F3n Social", field: "nombre_cliente", width: 350},
        {headerName: "Estado", field: "estado_solicitud", width: 150},
        {headerName: "Tipo Oferta", field: "oferta_aprobada", width: 160},
        {headerName: "Oficina", field: "ofi_registro", width: 160},
        {headerName: "Fecha Ingreso", field: "startdate", width: 100, cellRenderer: function(params) {
			var resultElement = document.createElement("span");
			resultElement.innerHTML = DateUtil.toString(DateUtil.longToDate(params.value), DateUtil.DDMMYYYYHHmmss);
            return resultElement;
		}},
        {headerName: "Usuario", field: "userTask", width: 200},
        {headerName: "RVGL", field: "num_rvgl", width: 180},
		{headerName: "Producto", field: "producto", width: 180},
		{headerName: "Campa\u00F1a", field: "campania", width: 150},
		{headerName: "Clasif. Cliente", field: "clte_clasificacion", width: 150},
		{headerName: "ABN. Registrante", field: "usu_registrante", width: 150}
    ];

	$scope.gridInstances = {
		columnDefs: columnDefs,
        rowSelection: 'single',
        rowSelected: function(row){
			// console.log(row);
		},
        selectionChanged: function(){
			// console.log('selection changed, ' + $scope.gridHumanTask.selectedRows.length + ' rows selected');
		},
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
            previous: '&lsaquo;',
            // for set filter
            selectAll: 'daSelect Allen',
            searchOoo: 'daSearch...',
            blanks: 'daBlanc',
            // for number filter and string filter
            filterOoo: 'daFilter...',
            // for number filter
            equals: 'Igual',
            lessThan: 'daLessThan',
            greaterThan: 'daGreaterThan',
            // for text filter
            contains: 'Contiene',
            startsWith: 'Comienza con',
            endsWith: 'Termina con',
            // tool panel
            columns: 'laColumns',
            pivotedColumns: 'laPivot Cols',
            pivotedColumnsEmptyMessage: 'la please drag cols to here',
            valueColumns: 'laValue Cols',
            valueColumnsEmptyMessage: 'la please drag cols to here'
        }
    };

	$scope.pageSize = 6;
	$scope.buscar = function(){
		var parameters = {
			p: 0,
			c: 1,
			f: "username=" + bonitaConfig.getUsername() + ",",
			u: bonitaConfig.getUsername()
		};

		if($scope.tipoFiltro == 'estacion') {
			parameters.f += "estacion='" + $scope.estacion["select"]["id"] + "'";
		} else {
			parameters.f +=  $scope.tipoDocumento["select"]["id"] + "='" + $scope.nroDocumento + "'";
		}

		ConsultaSolicitudes.get(parameters).$promise.then(function(solicitudes){
			var dataSource = {
				rowCount: solicitudes.totalSolicitudes,
				pageSize: $scope.pageSize,
            	getRows: function (params) {
					var pag = params.startRow / $scope.pageSize;
					parameters.p = pag;
					parameters.c = $scope.pageSize;
					ConsultaSolicitudes.get(parameters).$promise.then(function(solicitudes){
						params.successCallback(solicitudes.solicitudes, -1);
					});
    	        }
	        };

			$scope.gridInstances.api.setDatasource(dataSource);
		});
	};
}]);