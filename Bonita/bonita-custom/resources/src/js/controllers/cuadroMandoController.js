abstractControllers.controller('CuadroMandoController', ['$scope', '$http', '$timeout', 'Listado', 'highchartsNG', function CuadroMandoController($scope, $http, $timeout, Listado, highchartsNG) {
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

			resultElement.target="_top"
			resultElement.href = "javascript:void(0);";
			resultElement.onclick = function() {
				console.log("Hola Mundo")
			};
			resultElement.innerHTML = params.value;
            return resultElement;
        }},
		{headerName: "Val. Documentaci\u00F3n", field: "validar_documentacion" , width: 150},
        {headerName: "Asig. Evaluaci\u00F3n"  , field: "asignar_evaluacion"    , width: 150},
        {headerName: "Eval. Sol. Campo"       , field: "evaluar_riesgo_campo"  , width: 150},
        {headerName: "Eval. Sol. Mesa"        , field: "evaluar_riesgo_mesa"   , width: 150},
        {headerName: "Autorizar Sol."         , field: "autorizar_evaluacion"  , width: 150},
        {headerName: "Total"                  , field: "total"                 , width: 150}
    ];

	$scope.gridInstances = {
		columnDefs: columnDefs,
        rowSelection: 'single',
        rowSelected: function(row){}, /* console.log(row); */
        selectionChanged: function(){}, /* console.log('selection changed, ' + $scope.gridHumanTask.selectedRows.length + ' rows selected'); */
		angularCompileRows: true,
        rowData: null
    };

    $scope.bar = {
		hide: true,
        labels: ["Download Sales", "In-Store Sales", "Mail-Order Sales"],
        data: [
            [300, 500, 100]
        ],
        series: ['Series A']
    };

    $scope.chartConfigPie = {
		hide: true,
		loading: false,
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
        title: {
            text: 'Centros de Negocio'
        },
		subtitle: {
            text: 'Demo'
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.y:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.y:.1f} %',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    },
                    connectorColor: 'silver'
                }
            }
        },
		legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -40,
            y: 80,
            floating: true,
            borderWidth: 1,
            backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
            shadow: true
        },
        series: [{
			type: "pie",
            name: "Centro de Negocio",
            data: []
        }]
    };

	highchartsNG.ready(function(){
		Highcharts.getOptions().colors = Highcharts.map(Highcharts.getOptions().colors, function (color) {
        	return {
            	radialGradient: {
                	cx: 0.5,
                	cy: 0.3,
                	r: 0.7
            	},
	            stops: [
    	            [0, color],
        	        [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
            	]
        	};
	    });
	}, this);
	
	$scope.buscar = function(){
		$timeout(function(){
			var initLoader = window.parent.document.getElementById("initloader");
			if(initLoader != null) {
				initLoader.style.display = "block";
			}
		}, 0);

		var parameters = {
			tipoConsulta: "detalle",
			centroNegocio: $scope.centroNegocio.select.val_column1
		};

		Listado.get(parameters).$promise.then(function(request){
			var total = {
				codigo_centro_negocio: "Total",
				validar_documentacion: 0,
				asignar_evaluacion: 0,
				evaluar_riesgo_campo: 0,
				evaluar_riesgo_mesa: 0,
				autorizar_evaluacion: 0,
				total: 0
			};

			var data = [];
			for(var i in request.detalleSolicitudAreas) {
				var row = request.detalleSolicitudAreas[i];
				row.total = row.validar_documentacion
					+ row.asignar_evaluacion
					+ row.evaluar_riesgo_campo
					+ row.evaluar_riesgo_mesa
					+ row.autorizar_evaluacion;

				total.validar_documentacion += row.validar_documentacion;
				total.asignar_evaluacion += row.asignar_evaluacion;
				total.evaluar_riesgo_campo += row.evaluar_riesgo_campo;
				total.evaluar_riesgo_mesa += row.evaluar_riesgo_mesa;
				total.autorizar_evaluacion += row.evaluar_riesgo_mesa;
				total.total += row.total;

				data.push({"name": row.codigo_centro_negocio, "y" :row.total});
			}
			request.detalleSolicitudAreas.push(total);

			$scope.gridInstances.rowData = request.detalleSolicitudAreas;
			$scope.gridInstances.api.onNewRows();

			$scope.chartConfigPie.hide = false;
			$scope.chartConfigPie.series[0].data = data;
		});
	};
}]);