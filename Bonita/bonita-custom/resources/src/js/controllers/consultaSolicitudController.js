abstractControllers.controller('ConsultaSolicitudController', ['$scope', '$http', 'ConsultaSolicitudes', 'bonitaConfig', 'DateUtil', '$timeout',
function ConsultaSolicitudController($scope, $http, ConsultaSolicitudes, bonitaConfig, DateUtil, $timeout) {
	$scope.disabledConsultar = true;
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

    var container = window.parent.document.getElementById("panelAngular");
    if(container != null) {
        // console.log(container.offsetWidth);
        $scope.witdh = container.offsetWidth - 135;
    } else {
        $scope.witdh = 820;
    }

    $scope.cambioValor= function(item, model) {
        $scope.disabledConsultar = false;
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

		$scope.disabledConsultar = true;
    });

    var columnDefs = [
        {headerName: "N° Solicitud", field: "url", width: 80, cellRenderer: function(params) {
            var resultElement = document.createElement("a");
            elements = angular.fromJson(params.value);
            resultElement.target="_top"
            resultElement.href = bonitaConfig.getBonitaUrl() + elements.url;
            resultElement.innerHTML = elements.value;
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

    var parameters = {};

    $scope.pageSize = 10;
    $scope.buscar = function(){
        $timeout(function(){
            var initLoader = window.parent.document.getElementById("initloader");
            if(initLoader != null) {
                initLoader.style.display = "block";
            }
        }, 0);

        parameters = {
        	p: -1,
	        c: 1,
    	    f: "",
        	r1: 0,
	        r2: 0,
    	    n: "S",
        	u: bonitaConfig.getUsername()
	    };

		if($scope.tipoFiltro == 'estacion') {
            parameters.f = "estacion=" + $scope.estacion["select"]["id"];
        } else {
            parameters.f =  $scope.tipoDocumento["select"]["id"] + "=" + $scope.nroDocumento;
        }

        ConsultaSolicitudes.get(parameters).$promise.then(function(request){
            var dataSource = {
                rowCount: request.totalSolicitudes,
                pageSize: $scope.pageSize,
                getRows: function (params) {
                    var pagOld = parameters.p;
                    var pagNew = params.startRow / $scope.pageSize;
                    parameters.p = pagNew;
                    parameters.c = $scope.pageSize;
                    parameters.n = pagNew >= pagOld ? 'S' : 'A';
                    console.log(parameters);
                    
                    ConsultaSolicitudes.get(parameters).$promise.then(function(request){
                        parameters.r1 = request.ids1;
                        parameters.r2 = request.ids2;
                        params.successCallback(request.solicitudes, request.totalSolicitudes);
                    });
                }
            };

            $scope.gridInstances.api.setDatasource(dataSource);
        });
    };
}]);