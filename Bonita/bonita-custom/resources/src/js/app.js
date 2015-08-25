'use strict';

var bonitaApp = angular.module('bonitaApp', ['ngRoute', 'ngSanitize', 'ngBonita', 'abstractControllers', 'ui.bootstrap', 'ui.select', 'angularGrid', "chart.js"]);
var abstractControllers = angular.module('abstractControllers', []);
var SessionController = bonitaApp.controller('SessionController', ['$scope', 'BonitaSession', 'User', 'bonitaConfig', '$location', '$q', 
	function SessionController($scope, BonitaSession, User, bonitaConfig, $location, $q) {

	$scope.userName = "Anonimo";
	/*
	$scope.menu = 'consulta';
	
	$scope.$watch(function(scope) { return scope.menu }, function(newValue, oldValue) {
		$location.path(newValue);
		$location.replace();
    });
	*/	

	BonitaSession.getCurrent().$promise.then(function (session) {
		var deferred = $q.defer();
		
		if (typeof session.user_id === 'undefined') {
			deferred.reject('No active session found');
		} else {
			// Save basic session data
			User.get({"id": session.user_id}).$promise.then(function (user) {
				$scope.userName = user.firstname + " " + user.lastname;
				bonitaConfig.setUsername(user.userName);
				bonitaConfig.setUserId(session.user_id);
			});
			
			deferred.resolve(session);
		}
	});
}]);

bonitaApp.config(function($routeProvider, $httpProvider) {
	$routeProvider.when('/', {
		controller : 'ConsultaSolicitudController',
		templateUrl : 'src/views/consultaSolicitud.html'
	}).when('/consulta', {
		controller : 'ConsultaSolicitudController',
		templateUrl : 'src/views/consultaSolicitud.html'
	}).when('/cuadromando', {
		controller : 'CuadroMandoController',
		templateUrl : 'src/views/cuadroMando.html'
	}).when('/restAPI', {
		controller : 'RestAPIController',
		templateUrl : 'src/views/restAPI.html'
	}).when('/restAPI/detail/:id', {
		controller : 'RestAPIDetailController',
		templateUrl : 'src/views/restAPIDetail.html'
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