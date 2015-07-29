'use strict';

var bonitaApp = angular.module('bonitaApp', ['ngRoute', 'ngSanitize', 'ngBonita', 'abstractControllers', 'ui.bootstrap', 'ui.select', 'angularGrid']);
var abstractControllers = angular.module('abstractControllers', []);
var SessionController = bonitaApp.controller('SessionController', ['$scope', 'BonitaSession', function SessionController($scope, BonitaSession) {
	$scope.userName = "Anonimo";
	/*
	BonitaSession.getCurrent().$promise.then(function (session) {
		if (typeof session.user_id === 'undefined') {
			deferred.reject('No active session found');
		} else {
			// Save basic session data
			$scope.userName = session.user_name;
			bonitaConfig.setUsername(session.user_name);
			bonitaConfig.setUserId(session.user_id);
			// bonitaAuthentication.isLogged = true;
			deferred.resolve(session);
		}
	});
	*/
	$scope.menuModel = {
		consulta : true,
		cuadro : false
	};

	$scope.titulo = "Consulta de Solicitudes";
}]);

bonitaApp.config(function($routeProvider, $httpProvider) {
	$routeProvider.when('/', {
		controller : 'ConsultaSolicitudController',
		templateUrl : 'src/views/consultaSolicitud.html'
	});
	
	var statusLoad, statusUnload;
	
	statusLoad = function (data, headersGetter) {
		// console.log("Load");
		// console.log(data);
		// console.log(headersGetter());
		return data;
	};
	
	statusUnload = function (data, headers) {
		// console.log("Unload");
		// console.log(data);
		// console.log(headers());
		return data;
	};
	
	// console.log($httpProvider);
	
	$httpProvider.defaults.transformResponse.push(statusUnload);
	$httpProvider.defaults.transformRequest.push(statusLoad);
});