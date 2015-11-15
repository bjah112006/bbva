'use strict';

abstractControllers.controller('SolicitudController', ['$scope', '$routeParams', '$location', 'SolicitudService', 'ListadoService',
function SolicitudController($scope, $routeParams, $location, SolicitudService, ListadoService) {
    var buscarLista = function(_lista, _campo, _id) {
        var value, valorBuscado;
        for (var i = 0; i < _lista.length; i++) {
            value = _lista[i];
            if(value[_campo] === _id) {
                valorBuscado = value;
                break;
            }
        }
        
        return valorBuscado;
    }
    
    $scope.uri = $routeParams.estacion + "/" + $routeParams.fase;
    $scope.solicitud = {
        nroSolicitud: $routeParams.id,
        codigoOficina: '0243',
        nombreOficina: 'Oficina 0243',
        codigoCliente: 'JQ42552617',
        razonSocial: 'JUAN QUEDENA',
        nroDocumento: '42552617',
        tipoDocumento: 'L',
        tipoPropuesta: 3,
        montoRiesgoGrupo: 1,
        montoRiesgoTotal: 2,
        montoPropuesta: 3,
        montoOportunidad: 4,
        monedaRiesgoGrupo: 1,
        monedaRiesgoTotal: 2,
        monedaPropuesta: 1,
        monedaOportunidad: 2,
        grupoEconomico: "GRUPO 1",
        nroRVGL: "1984",
        etiqueta: 1,
        correoAsistente: "jquedena@gmail.com",
        observacionesFinales: "Aqui acaba el registro de solicitud",
        tipoEvaluador: 'AN',
        tipoAmbitoEvaluador: 'SG',
        evaluador: 3,
        comentarioAsignacion: "Se asigna al subgerente",
        revisionAprobada: "A",
        comentarioRevisionRiesgos: "Haber que marcas",
        evaluacion: [
            {
                ambito: "AN",
                montoRiesgoGrupo: 10,
                montoRiesgoTotal: 20,
                montoPropuesta: 30,
                montoOportunidad: 40,
                monedaRiesgoGrupo: 2,
                monedaRiesgoTotal: 1,
                monedaPropuesta: 2,
                monedaOportunidad: 1,
                comitePipeline: "N",
                evaluador: "Pepe Perez",
                comentario: "Esto puede ser con arreglos :'(",
                accion: "D"
            }
        ],
        
        comentarioSubsanacion: "Acaba de ser corregida la solicitud",
        comentarioRevisionOficina: "Aqui se cierra la solicitud"
    };
    $scope.mostrar = {};
    $scope.eventos = {};
    
    $scope.listaMonedas = ListadoService.listaMonedas;
    $scope.listaEtiquetas = ListadoService.listaEtiquetas;
    $scope.listaPropuestas = ListadoService.listaPropuestas;
    $scope.listaDocumentos = ListadoService.listaDocumentos;
    $scope.listaGuiaDocumentaria = ListadoService.listaGuiaDocumentaria;

    $scope.propuesta         = { select: buscarLista($scope.listaPropuestas, 'id', $scope.solicitud.tipoPropuesta    ) };
    $scope.documento         = { select: buscarLista($scope.listaDocumentos, 'id', $scope.solicitud.tipoDocumento    ) };
    $scope.monedaRiesgoGrupo = { select: buscarLista($scope.listaMonedas   , 'id', $scope.solicitud.monedaRiesgoGrupo) };
    $scope.monedaRiesgoTotal = { select: buscarLista($scope.listaMonedas   , 'id', $scope.solicitud.monedaRiesgoTotal) };
    $scope.monedaPropuesta   = { select: buscarLista($scope.listaMonedas   , 'id', $scope.solicitud.monedaPropuesta  ) };
    $scope.monedaOportunidad = { select: buscarLista($scope.listaMonedas   , 'id', $scope.solicitud.monedaOportunidad) };
    $scope.etiqueta          = { select: buscarLista($scope.listaEtiquetas , 'id', $scope.solicitud.etiqueta         ) };
    
    $scope.$watch(function(scope) { return scope.uri }, function(newValue, oldValue) {
        $scope.mostrar.oficinaRegistrar   = (newValue != 'oficina/registrar' );
        $scope.mostrar.riesgosAsignar     = (newValue != 'riesgos/asignar'   );
        $scope.mostrar.riesgosRevisar     = (newValue != 'riesgos/revisar'   );
        $scope.mostrar.riesgosEvaluar     = {};
        $scope.mostrar.riesgosEvaluar.btn = (newValue != 'riesgos/evaluar'   );
        $scope.mostrar.riesgosEvaluar.pnl = !(
                                                newValue == 'riesgos/evaluar'    ||
                                                newValue == 'riesgos/cambiar'    ||
                                                newValue == 'riesgos/dictaminar' ||
                                                newValue == 'oficina/revisar'
                                            );
        $scope.mostrar.riesgosEvaluar.rw1 = (newValue != 'riesgos/evaluar'   );
        $scope.mostrar.riesgosEvaluar.rw2 = (newValue == 'riesgos/evaluar'   );
        $scope.mostrar.riesgosCambiar     = (newValue != 'riesgos/cambiar'   );
        $scope.mostrar.riesgosDictaminar  = (newValue != 'riesgos/dictaminar');
        $scope.mostrar.oficinaRevisar     = (newValue != 'oficina/revisar'   );
        $scope.mostrar.oficinaSubsanar    = (newValue != 'oficina/subsanar'  );
    });

    if(!$scope.mostrar.riesgosAsignar) {
        $scope.listaTipoEvaluador       = ListadoService.listaTiposEvaluadores;
        $scope.listaTipoAmbitoEvaluador = ListadoService.listaAmbitos;
        $scope.listaEvaluador           = ListadoService.listaUsuarios;

        $scope.tipoEvaluador       = { select: buscarLista($scope.listaTipoEvaluador       , 'id', $scope.solicitud.tipoEvaluador       ) };
        $scope.tipoAmbitoEvaluador = { select: buscarLista($scope.listaTipoAmbitoEvaluador , 'id', $scope.solicitud.tipoAmbitoEvaluador ) };
        $scope.evaluador           = { select: buscarLista($scope.listaEvaluador           , 'id', $scope.solicitud.evaluador           ) };
    }
    
    if(!$scope.mostrar.riesgosEvaluar) {
        $scope.listaTipoAmbitoEvaluador = ListadoService.listaAmbitos;
        
        $scope.evaluacion = {
            ambito            : { select: buscarLista($scope.listaTipoAmbitoEvaluador, 'id', $scope.solicitud.evaluacion[0].ambito            ) },
            monedaRiesgoGrupo : { select: buscarLista($scope.listaMonedas            , 'id', $scope.solicitud.evaluacion[0].monedaRiesgoGrupo ) },
            monedaRiesgoTotal : { select: buscarLista($scope.listaMonedas            , 'id', $scope.solicitud.evaluacion[0].monedaRiesgoTotal ) },
            monedaPropuesta   : { select: buscarLista($scope.listaMonedas            , 'id', $scope.solicitud.evaluacion[0].monedaPropuesta   ) },
            monedaOportunidad : { select: buscarLista($scope.listaMonedas            , 'id', $scope.solicitud.evaluacion[0].monedaOportunidad ) }
        };
    }
    
    $scope.eventos.oficinaRegistrar  = function(){ SolicitudService.oficinaRegistrar ($scope.solicitud); $location.path("solicitud/riesgos/asignar/" + $scope.solicitud.nroSolicitud); $location.replace(); };
    $scope.eventos.riesgosAsignar    = function(){ SolicitudService.riesgosAsignar   ($scope.solicitud); $location.path("solicitud/riesgos/revisar/" + $scope.solicitud.nroSolicitud); $location.replace(); };
    $scope.eventos.riesgosRevisar    = function(){ SolicitudService.riesgosRevisar   ($scope.solicitud); $location.path(($scope.solicitud.revisionAprobada === "A" ? "solicitud/riesgos/evaluar/" : "solicitud/oficina/subsanar/") + $scope.solicitud.nroSolicitud); $location.replace(); };
    $scope.eventos.riesgosEvaluar    = function(){ SolicitudService.riesgosEvaluar   ($scope.solicitud); $location.path("solicitud/riesgos/dictaminar/" + $scope.solicitud.nroSolicitud); $location.replace(); };
    $scope.eventos.riesgosCambiar    = function(){ SolicitudService.riesgosCambiar   ($scope.solicitud); $location.path("solicitud/riesgos/dictaminar/" + $scope.solicitud.nroSolicitud); $location.replace(); };
    $scope.eventos.riesgosDictaminar = function(){ SolicitudService.riesgosDictaminar($scope.solicitud); $location.path("solicitud/oficina/revisar/" + $scope.solicitud.nroSolicitud); $location.replace(); };
    $scope.eventos.oficinaRevisar    = function(){ SolicitudService.oficinaRevisar   ($scope.solicitud); alert("Solicitud Cerrada");/* $location.path("solicitud/riesgos/asignar/" + $scope.solicitud.nroSolicitud); $location.replace(); */ };
    $scope.eventos.oficinaSubsanar   = function(){ SolicitudService.oficinaSubsanar  ($scope.solicitud); $location.path("solicitud/riesgos/revisar/" + $scope.solicitud.nroSolicitud); $location.replace(); };

    // oficina/registrar
    // riesgos/asignar
    // riesgos/revisar
    // riesgos/evaluar
    // riesgos/cambiar 
    // riesgos/dictaminar
    // oficina/revisar 
    // oficina/subsanar
}]);
