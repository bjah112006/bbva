abstractControllers.controller('DialogContentController', ['$scope', '$modalInstance', 'rowSelect', function RestAPIController($scope, $modalInstance, rowSelect) {
    $scope.rowSelect = rowSelect;

    $scope.ok = function () {
        // TODO: Save fecha
        $modalInstance.close();
      };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
}]);

abstractControllers.controller('RestAPIController', ['$scope', '$log', 'HumanTaskAll', '$timeout', '$modal', function RestAPIController($scope, $log, HumanTaskAll, $timeout, $modal) {
    var columnDefs = [
        {headerName: "Prioridad", field: "priority", width: 120},
        {headerName: "Id", field: "id", width: 80},
        {headerName: "Caso", field: "caseId", width: 80, cellRenderer: function(params) {
            var resultElement = document.createElement("a");
            resultElement.href = "#/restAPI/detail/" + params.value;
            resultElement.innerHTML = params.value;
            return resultElement;
        }},
        {headerName: "Tarea", field: "name", width: 150},
        {headerName: "Detalle", field: "displayName", width: 350},
        {headerName: "Asignado el", field: "assigned_date", width: 160},
        {headerName: "Actualizado el", field: "last_update_date", width: 160}
    ];
    
    var container = window.parent.document.getElementById("panelAngular");
    if(container != null) {
        // console.log(container.offsetWidth);
        $scope.witdh = container.offsetWidth - 135;
    } else {
        $scope.witdh = window.parent.document.offsetWidth - 135;
    }

    /*
        {headerName: "", field: "caseId", width: 70, cellRenderer: rendererEditar},
        {headerName: "Asignado a", field: "assigned_id", witdh: 150}
    */
/*
    "id" : "461",
    "caseId" : "39",
    "assigned_id" : "213",
    "assigned_date" : "2015-07-24 11:07:26.892",
    "displayName" : "78678657832-Evaluar Riesgo Mesa",
    "rootCaseId" : "39",
    "last_update_date" : "2015-07-24 11:05:36.056",
    "actorId" : "48",
    "processId" : "6715907529294943771",
    "name" : "Evaluar Riesgo Mesa",
*/
    $scope.open = function (size) {
        var modalInstance = $modal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'dialogContent.html',
            controller: 'DialogContentController',
            size: size,
            resolve: {
                rowSelect: function () {
                    return $scope.rowSelect;
                }
            }
        });
    };

    $scope.rowSelect = {};

    $scope.gridHumanTask = {
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
            previous: '&lsaquo;'
        }
    };

    function rendererEditar(params) {
        var html = "<label ng-click='editar()'>Editar<label/>";
        var resultElement = document.createElement("div");
        resultElement.innerHTML = html;

        params.$scope.editar = function(){
            $timeout(function(){
                // console.log($scope.gridHumanTask.selectedRows[0]);
                $scope.rowSelect = $scope.gridHumanTask.selectedRows[0];
                $scope.open();
            }, 0)
        };

        return resultElement;
    };

    function obtainRows() {
        HumanTaskAll.getAll({'p': 0, 'c': 1}).$promise.then(function(humanTask){
            var dataSource = {
                rowCount: humanTask.totalCount,
                pageSize: 8,
                getRows: function (params) {
                    var pag = params.startRow / 8;
                    // $log.log(pag);
                    HumanTaskAll.getAll({'p': pag, 'c': 8}).$promise.then(function(humanTask){
                        // $log.log(humanTask.items);
                        params.successCallback(humanTask.items, -1);
                    });
                }
            };

            $scope.gridHumanTask.api.setDatasource(dataSource);
        });
    }

    // setTimeout(function() { obtainRows(); }, 500);
    obtainRows();
}]);

abstractControllers.controller('RestAPIDetailController', ['$scope', '$routeParams', 'CaseVariable', function RestAPIController($scope, $routeParams, CaseVariable) {
    $scope.caseId = $routeParams.id;

    var columnDefs = [
        {headerName: "Llave", field: "name", width: 150, editable: true},
        {headerName: "Valor", field: "value", width: 450, editable: true}
    ];

    $scope.gridVariable = {
        columnDefs: columnDefs,
        rowSelection: 'single',
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

    function obtainRows() {
        CaseVariable.getVariables({p: 0, c: 1, f: 'case_id=' + $scope.caseId}).$promise.then(function(caseVariable){
            var dataSource = {
                rowCount: caseVariable.totalCount,
                pageSize: 999999,
                getRows: function (params) {
                    var pag = params.startRow / 999999;
                    CaseVariable.getVariables({p: pag, c: 999999, f: 'case_id=' + $scope.caseId}).$promise.then(function(caseVariable){
                        params.successCallback(caseVariable.items, -1);
                    });
                }
            };

            $scope.gridVariable.api.setDatasource(dataSource);
        });
    }

    // setTimeout(function() { obtainRows(); }, 500);
    obtainRows();
}]);

/*
function customEditorUsingAngular(params) {
        params.$scope.setSelectionOptions = setSelectionOptions;

        var html = '<span ng-show="!editing" ng-click="startEditing()">{{data.'+params.colDef.field+'}}</span> ' +
            '<select ng-blur="editing=false" ng-change="editing=false" ng-show="editing" ng-options="item for item in setSelectionOptions" ng-model="data.'+params.colDef.field+'">';

        // we could return the html as a string, however we want to add a 'onfocus' listener, which is no possible in AngularJS
        var domElement = document.createElement("span");
        domElement.innerHTML = html;

        params.$scope.startEditing = function() {
            params.$scope.editing = true; // set to true, to show dropdown

            // put this into $timeout, so it happens AFTER the digest cycle,
            // otherwise the item we are trying to focus is not visible
            $timeout(function () {
                var select = domElement.querySelector('select');
                select.focus();
            }, 0);
        };

        return domElement;
    }
*/
