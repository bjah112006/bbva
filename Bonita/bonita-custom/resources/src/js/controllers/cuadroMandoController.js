abstractControllers.controller('CuadroMandoController', 
['$scope', '$http', '$timeout', 'Listado', 'highchartsNG', 'bonitaConfig', 'DateUtil', function CuadroMandoController($scope, $http, $timeout, Listado, highchartsNG, bonitaConfig, DateUtil) {
    $scope.tipoRed = {};
    $scope.centroNegocio = {};
    $scope.gestorOrigen = {};
    $scope.gestor = {};

    $scope.tiposRed = [];
    $scope.centroNegocios = [];
    $scope.gestores = [];

    var container = window.parent.document.getElementById("panelAngular");
    if(container != null) {
        // console.log(container.offsetWidth);
        $scope.witdh = container.offsetWidth - 135;
    } else {
        $scope.witdh = 820;
    }

    mostrarDetalle = function(mostrar) {
        if(mostrar) {
            if($("#detalle").hasClass("hide")) {
                $("#detalle").removeClass("hide");
            }
        } else {
            if(!$("#detalle").hasClass("hide")) {
                $("#detalle").addClass("hide");
            }
        }
    };


    Listado.get({"tipoConsulta": "red"}).$promise.then(function(request){
        $scope.tiposRed = request.tipoRed;
        $scope.gestores = request.usuarioPorAreas;
        $scope.centroNegocios = [];
    });

    $scope.buscarCentros = function(item, model) {
        Listado.get({"tipoConsulta": "areas", "tipoRed": item.value}).$promise.then(function(request){
            request.areas.splice(0, 0, {"val_column1": "[Todos]"});
            $scope.centroNegocio = {"select": {"val_column1": "[Todos]"}};
            $scope.centroNegocios = request.areas;
            $scope.disabledBuscar = false;
            mostrarDetalle(false);
            $scope.chartConfig.hide = true;
            $scope.chartDetalle.hide = true;
            $scope.gridInstances.rowData = [];
            $scope.gridInstances.api.onNewRows();
        });
    };

    $scope.asignar = function() {
        /* Listado.get({"tipoConsulta": "areas", "tipoRed": item.value}).$promise.then(function(request){
            request.areas.splice(0, 0, {"val_column1": "[Todos]"});
            $scope.centroNegocio = {"select": {"val_column1": "[Todos]"}};
            $scope.centroNegocios = request.areas;
            $scope.disabledBuscar = false;
            mostrarDetalle(false);
            $scope.chartConfig.hide = true;
            $scope.gridInstances.rowData = [];
            $scope.gridInstances.api.onNewRows();
        }); */
    };

    $scope.cambioCentroNegocio = function(item, model) {
        mostrarDetalle(false);
        $scope.chartConfig.hide = true;
        $scope.chartDetalle.hide = true;
        $scope.gridInstances.rowData = [];
        $scope.gridInstances.api.onNewRows();
    };

    var columnDetalleDefs = [
        {headerName: "N° Solicitud", field: "rootprocessinstanceid", width: 80, cellRenderer: function(params) {
            var resultElement = document.createElement("a");
            resultElement.target="_top"
            resultElement.href = bonitaConfig.getBonitaUrl() + "/portal/homepage#?id=" + params.value + "&_p=casemoredetails&_pf=1";
            resultElement.innerHTML = params.value;
            return resultElement;
        }},
        {headerName: "C\u00F3digo Central", field: "codigo_cliente", width: 100},
        {headerName: "RVGL", field: "num_rvgl", width: 180},
        {headerName: "Raz\u00F3n Social", field: "nombre_cliente", width: 350},
        {headerName: "Oficina", field: "ofi_registro", width: 160},
        {headerName: "Fecha Envio", field: "startdate", width: 100, cellRenderer: function(params) {
            var resultElement = document.createElement("span");
            resultElement.innerHTML = DateUtil.toString(DateUtil.longToDate(params.value), DateUtil.DDMMYYYYHHmmss);
            return resultElement;
        }},
        {headerName: "Producto", field: "producto", width: 180}
    ];

    var columnDefs = [
        {headerName: '', width: 30, checkboxSelection: true},
        {headerName: "Centro Negocio", field: "codigo_centro_negocio", width: 250, cellRenderer: function(params) {
            var resultElement = document.createElement("a");

            resultElement.target="_top"
            resultElement.href = "javascript:void(0);";
            resultElement.onclick = function() {
                var parameters = {
                    tipoConsulta: "detalleCentroNegocio",
                    centroNegocio: $(this).text()
                };
                Listado.get(parameters).$promise.then(function(request){
                    var _catergories = [];
                    var _series = [{"name": "Centro Negocio: " + parameters.centroNegocio, data: []}];
                    
                    for(var i in request.detalleSolicitudAreas) {
                        _catergories.push(request.detalleSolicitudAreas[i].username == null ? "No Disponible" : request.detalleSolicitudAreas[i].username);
                        _series[0].data.push(request.detalleSolicitudAreas[i].cant);
                    }
                    
                    $scope.chartConfig.hide = true;
                    $scope.chartDetalle.hide = false;
                    $scope.chartDetalle.xAxis.categories = _catergories;
                    $scope.chartDetalle.series = _series;
                    
                    // console.log(_series);
                    // console.log(_catergories);
                    // console.log(request);
                });
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

    $scope.gridDetalle = {
        columnDefs: columnDetalleDefs,
        rowSelection: 'single',
        rowSelected: function(row){}, /* console.log(row); */
        selectionChanged: function(){}, /* console.log('selection changed, ' + $scope.gridHumanTask.selectedRows.length + ' rows selected'); */
        angularCompileRows: true,
        rowData: null,
        rowSelection: 'multiple',
        suppressRowClickSelection: true
    };

    $scope.gridInstances = {
        columnDefs: columnDefs,
        rowSelection: 'single',
        rowSelected: function(row){}, /* console.log(row); */
        selectionChanged: function(){}, /* console.log('selection changed, ' + $scope.gridHumanTask.selectedRows.length + ' rows selected'); */
        angularCompileRows: true,
        rowData: null
    };

    $scope.chartDetalle = {
        hide: true,
        loading: false,
        chart: {
            type: 'bar'
        },
        title: { text: '' },
        subtitle: { text: '' },
        xAxis: {
            categories: [],
            title: {
                text: null
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Solicitudes',
                align: 'high'
            },
            labels: {
                overflow: 'justify'
            }
        },
        tooltip: {
            valueSuffix: ' solicitudes'
        },
        plotOptions: {
            bar: {
                dataLabels: {
                    enabled: true
                }
            },
            series: {
                cursor: 'pointer',
                events: {
                    click: function (event) {
                        mostrarDetalle(true);
                        $("#detalle").find(".legend").html("Solicitudes del gestor: <b>" + event.point.category + "</b>");
                        var parameters = {
                            tipoConsulta: "detalleGestor",
                            centroNegocio: event.point.series.name.replace("Centro Negocio: ", ""),
                            username: event.point.category
                        };
                        Listado.get(parameters).$promise.then(function(request){
                            $scope.gridDetalle.rowData = request.detalleSolicitudAreas;
                            $scope.gridDetalle.api.onNewRows();
                        });
                        /*
                        console.log(this);
                        console.log(event.point);
                        alert(this.name + ' clicked\n' +
                              'Alt: ' + event.altKey + '\n' +
                              'Control: ' + event.ctrlKey + '\n' +
                              'Shift: ' + event.shiftKey + '\n' + 
                              'Category: ' + event.point.category + '\n' +
                              'value y: ' + event.point.y + '\n' +
                              'value x: ' + event.point.x + '\n' +
                              'Serie: ' + event.point.series.name);
                        */
                    }
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
        credits: {
            enabled: false
        },
        series: []
    };

    $scope.chartConfig = {
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
            text: ''
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.y:.0f}</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '{point.name}: {point.y:.0f}',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                }
            }
        },
        series: [{
            "name": "Centro de Negocio",
            "data": []
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

    loadingPage = function() {
        $timeout(function(){
            var initLoader = window.parent.document.getElementById("initloader");
            if(initLoader != null) {
                initLoader.style.display = "block";
            }
        }, 0);
    };

    $scope.disabledAsignar = true;
    $scope.disabledBuscar = true;
    $scope.buscar = function(){
        loadingPage();

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

            $scope.chartConfig.hide = false;
            $scope.chartConfig.series[0].data = data;
        });
    };
}]);