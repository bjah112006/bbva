bonitaApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

bonitaApp.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(file, uploadUrl, rowSelect, bonitaConfig, scope, $modalInstance){
        var fd = new FormData();
        fd.append('file', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(result){
        	//TODO: ACTUALIZAMOS EL DOCUMENTO
        	$http.put(bonitaConfig.getBonitaUrl() + '/API/bpm/caseDocument/' + rowSelect.document_mapping_id, { 
				'file': result,
				'fileName': file.name
			}).success(function(result) {
			    alert("Se actualizó el documento");
			    $modalInstance.close();
			    scope.buscarDocumentos();
			}).error(function(error) {
				console.log(error);
			});
        })
        .error(function(error){
        });
    }
}]);

abstractControllers.controller('DialogUpdateController', ['$scope', '$modalInstance', 'rowSelect', '$http', 'bonitaConfig', 'fileUpload', 'scope',
 function DialogUpdateController($scope, $modalInstance, rowSelect, $http, bonitaConfig, fileUpload, scope) {
	$scope.rowSelect = rowSelect;
	$scope.ok = function () {
		if(confirm("¿Está seguro que desea actualizar el documento?")){
			var file = $scope.myFile;
			var uploadUrl = bonitaConfig.getBonitaUrl() + "/portal/fileUpload";
			fileUpload.uploadFileToUrl(file, uploadUrl, rowSelect, bonitaConfig, scope, $modalInstance);
		}
  	};

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};
}]);

abstractControllers.controller('DialogDeleteController', ['$scope', '$modalInstance', 'rowSelect', '$http', 'bonitaConfig', 'scope',
 function DialogDeleteController($scope, $modalInstance, rowSelect, $http, bonitaConfig, scope) {
	$scope.rowSelect = rowSelect;
	$scope.ok = function () {
		// TODO: ELIMINAR DOCUMENTO
		$http.delete(bonitaConfig.getBonitaUrl() + '/API/bpm/caseDocument/' + rowSelect.document_mapping_id);
		$modalInstance.close();
		scope.buscarDocumentos();
  	};

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};
	
}]);

abstractControllers.controller('ConsultaDocumentosController', ['$scope', '$http', 'ConsultaDocumentos', 'bonitaConfig', 'DateUtil', '$timeout', '$modal', 
function ConsultaDocumentosController($scope, $http, ConsultaDocumentos, bonitaConfig, DateUtil, $timeout, $modal) {
	$scope.nroSolicitud = '';
 
	var columnDefs = [
	    {headerName: "Fecha", field: "fecha_documento", width: 150},
        {headerName: "Nombre Archivo", field: "filename", width: 250, cellRenderer: function(params) {
            var resultElement = document.createElement("a");
			resultElement.href = bonitaConfig.getBonitaUrl() + "/portal/documentDownload?fileName=" + params.value + "&contentStorageId=" + params.data.documentid;
			resultElement.innerHTML = params.value;
            return resultElement;
        }},
        {headerName: "", field: "", width: 100, cellRenderer: function(params) {
        	var html = "<label ng-click='editar()' style='cursor:pointer;'>Editar<label/>";
            var resultElement = document.createElement("div");
    		resultElement.innerHTML = html;

    		params.$scope.editar = function(){
    			$timeout(function(){
    				/*console.log($scope.gridDocuments.selectedRows[0]);
    				console.log(params.value);*/
    				$scope.rowSelect = $scope.gridDocuments.selectedRows[0];
    				$scope.abrirActualizar();
    			}, 0)
    		};
            return resultElement;
         }},
         {headerName: "", field: "documentid", width: 100, cellRenderer: function(params) {
         	var html = "<label ng-click='eliminar()' style='cursor:pointer;'>Eliminar<label/>";
             var resultElement = document.createElement("div");
     		resultElement.innerHTML = html;

     		params.$scope.eliminar = function(){
     			$timeout(function(){
     				/*console.log($scope.gridDocuments.selectedRows[0]);
     				console.log(params.value);*/
     				$scope.rowSelect = $scope.gridDocuments.selectedRows[0];
     				$scope.abrirEliminar();
     			}, 0)
     		};
             return resultElement;
          }}
    ];
	
	$scope.abrirActualizar = function (size) {
		var modalInstance = $modal.open({
			animation: $scope.animationsEnabled,
			templateUrl: 'dialogContent.html',
			controller: 'DialogUpdateController',
			size: size,
			resolve: {
				rowSelect: function () {
					return $scope.rowSelect;
				},
				scope: function() {
					return $scope;
				}
			}
		});
	};
	
	$scope.abrirEliminar = function (size) {
		var modalInstance = $modal.open({
			animation: $scope.animationsEnabled,
			templateUrl: 'dialogConfirmation.html',
			controller: 'DialogDeleteController',
			size: size,
			resolve: {
				rowSelect: function () {
					return $scope.rowSelect;
				},
				scope: function() {
					return $scope;
				}
			}
		});
	};
	
	$scope.rowSelect = {};

	$scope.gridDocuments = {
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
	$scope.buscarDocumentos = function(){
		$timeout(function(){
			window.parent.document.getElementById("initloader").style.display = "block";
		}, 0);

		var parameters = {
			p: 0,
			c: 1,
			f: "username=" + bonitaConfig.getUsername() + ",",
			u: bonitaConfig.getUsername()
		};
		
		parameters.f +=  "instance='" + $scope.nroSolicitud + "'";
	
		ConsultaDocumentos.get(parameters).$promise.then(function(documentosSolicitudes){
			var dataSource = {
				rowCount: documentosSolicitudes.totalDocumentos,
				pageSize: $scope.pageSize,
            	getRows: function (params) {
					var pag = params.startRow / $scope.pageSize;
					parameters.p = pag;
					parameters.c = $scope.pageSize;
					ConsultaDocumentos.get(parameters).$promise.then(function(documentosSolicitudes){
						params.successCallback(documentosSolicitudes.documentosSolicitudes, -1);
					});
    	        }
	        };

			$scope.gridDocuments.api.setDatasource(dataSource);
		});
	};
}]);