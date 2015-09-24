abstractControllers.controller('CuadroMandoController', ['$scope', '$http', 'Listado', function CuadroMandoController($scope, $http, Listado) {
	$scope.tipoRed = {};
	$scope.centroNegocio = {};
	$scope.tiposRed = [];
	$scope.centroNegocios = [];

	Listado.get({"tipoConsulta": "red"}).$promise.then(function(request){
		$scope.tiposRed = request.tipoRed;
		$scope.centroNegocios = [];
	});

	$scope.buscarCentros = function(item, model) {
		Listado.get({"tipoConsulta": "areas", "tipoRed": item.value}).$promise.then(function(request){
			request.areas.splice(0, 0, {"val_column1": "[Todos]"});
			$scope.centroNegocio = {"select": {"val_column1": "[Todos]"}};
			$scope.centroNegocios = request.areas;
		});
	};

	var columnDefs = [
        {headerName: "Centro Negocio", field: "codigo_centro_negocio", width: 250, cellRenderer: function(params) {
            var resultElement = document.createElement("a");

            elements = angular.fromJson(params.value);
			resultElement.target="_top"
			resultElement.href = bonitaConfig.getBonitaUrl() + elements.url;
			resultElement.innerHTML = elements.value;
            return resultElement;
        }},
		{headerName: "Val. Documentaci\u00F3n", field: "validar_documentacion" , width: 150},
        {headerName: "Asig. Evaluaci\u00F3n"  , field: "asignar_evaluacion"    , width: 150},
        {headerName: "Eval. Sol. Campo"       , field: "evaluar_riesgo_campo"  , width: 150},
        {headerName: "Eval. Sol. Mesa"        , field: "evaluar_riesgo_mesa"   , width: 150},
        {headerName: "Autorizar Sol."         , field: "autorizar_evealuacion" , width: 150},
        {headerName: "Total"                  , field: "total"                 , width: 150}
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

	$scope.buscar = function(){
		$timeout(function(){
			window.parent.document.getElementById("initloader").style.display = "block";
		}, 0);

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

		Listado.get(parameters).$promise.then(function(request){
			var dataSource = {
				rowCount: request.totalDetalleSolicitudAreas,
				pageSize: request.totalDetalleSolicitudAreas,
            	getRows: function (params) {
					var pag = params.startRow / $scope.pageSize;
					parameters.p = pag;
					parameters.c = $scope.pageSize;
					Listado.get(parameters).$promise.then(function(request){
						params.successCallback(solicitudes.solicitudes, -1);
					});
    	        }
	        };

			$scope.gridInstances.api.setDatasource(dataSource);
		});
	};


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