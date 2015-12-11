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
        funcion: 3,
        correoAsistente: "jquedena@gmail.com",
        correoAlterno: "jquedena@hotmail.com",
        observacionesFinales: "Aqui acaba el registro de solicitud",
        controlAprobada: "A",
        comentarioControlRiesgos: "Haber que marcas Control",
        tipoEvaluador: 'AN',
        tipoAmbitoActual: 'SG',
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
        
        dictamenAprobada: "AS",
        comitePipeline: "S",
        comentarioDictamen: "Dictamen aprobado",
        montoAprobado: 1000.25,
        monedaAprobado: 2,
        plazoAprobado: 360,
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
    $scope.listaFunciones = ListadoService.listaFunciones;

    $scope.propuesta         = { select: buscarLista($scope.listaPropuestas, 'id', $scope.solicitud.tipoPropuesta    ) };
    $scope.documento         = { select: buscarLista($scope.listaDocumentos, 'id', $scope.solicitud.tipoDocumento    ) };
    $scope.monedaRiesgoGrupo = { select: buscarLista($scope.listaMonedas   , 'id', $scope.solicitud.monedaRiesgoGrupo) };
    $scope.monedaRiesgoTotal = { select: buscarLista($scope.listaMonedas   , 'id', $scope.solicitud.monedaRiesgoTotal) };
    $scope.monedaPropuesta   = { select: buscarLista($scope.listaMonedas   , 'id', $scope.solicitud.monedaPropuesta  ) };
    $scope.monedaOportunidad = { select: buscarLista($scope.listaMonedas   , 'id', $scope.solicitud.monedaOportunidad) };
    $scope.etiqueta          = { select: buscarLista($scope.listaEtiquetas , 'id', $scope.solicitud.etiqueta         ) };
    $scope.funcion           = { select: buscarLista($scope.listaFunciones , 'id', $scope.solicitud.funcion          ) };
    
    $scope.$watch(function(scope) { return scope.uri }, function(newValue, oldValue) {
        $scope.mostrar.oficinaRegistrar   = (newValue != 'oficina/registrar' );
        $scope.mostrar.riesgosControl     = (newValue != 'riesgos/control'   );
        $scope.mostrar.riesgosAsignar     = (newValue != 'riesgos/asignar'   );
        $scope.mostrar.riesgosAsignarInfo = !(
                                                newValue == 'riesgos/revisar'    ||
                                                newValue == 'riesgos/evaluar'    ||
                                                newValue == 'riesgos/cambiar'    ||
                                                newValue == 'riesgos/dictaminar' ||
                                                newValue == 'oficina/revisar'
                                            );
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
        $scope.mostrar.riesgosDictaminar  = {};
        $scope.mostrar.riesgosDictaminar.pnl  = !(newValue == 'riesgos/dictaminar' || newValue == 'oficina/revisar');
        $scope.mostrar.riesgosDictaminar.act  = (newValue != 'riesgos/dictaminar');
        $scope.mostrar.oficinaRevisar     = (newValue != 'oficina/revisar'   );
        $scope.mostrar.oficinaSubsanar    = (newValue != 'oficina/subsanar'  );
        
        switch (newValue) {
            case "oficina/registrar"  : $scope.solicitud.estado = "En Registro"                ; break;
            case "riesgos/control"    : $scope.solicitud.estado = "Registrado"                 ; break;
            case "riesgos/asignar"    : $scope.solicitud.estado = "Revisado por GMC"           ; break;
            case "riesgos/revisar"    : $scope.solicitud.estado = "Asignado"                   ; break;
            case "riesgos/evaluar"    : $scope.solicitud.estado = "Revisado por Analista"      ; break;
            case "riesgos/cambiar"    : $scope.solicitud.estado = "Evaluado Nuevo \u00C1mbito" ; break;
            case "riesgos/dictaminar" : $scope.solicitud.estado = "Evaluado Nuevo \u00C1mbito" ; break;
            case "oficina/revisar"    : $scope.solicitud.estado = "Dictaminado"                ; break;
            case "oficina/subsanar"   : $scope.solicitud.estado = "" ; break;
        }
    });

    if(!$scope.mostrar.riesgosAsignar) {
        $scope.listaTipoEvaluador       = ListadoService.listaTiposEvaluadores;
        $scope.listaTipoAmbitoEvaluador = ListadoService.listaAmbitos;
        $scope.listaEvaluador           = ListadoService.listaUsuarios;

        $scope.tipoEvaluador       = { select: buscarLista($scope.listaTipoEvaluador       , 'id', $scope.solicitud.tipoEvaluador       ) };
        $scope.tipoAmbitoActual    = { select: buscarLista($scope.listaTipoAmbitoEvaluador , 'id', $scope.solicitud.tipoAmbitoActual    ) };
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
    
    if(!$scope.mostrar.riesgosDictaminar) {
        $scope.monedaAprobado = { select: buscarLista($scope.listaMonedas, 'id', $scope.solicitud.monedaAprobado ) };
    }
    
    $scope.eventos.oficinaRegistrar  = function(){ SolicitudService.oficinaRegistrar ($scope.solicitud); $location.path("solicitud/riesgos/control/" + $scope.solicitud.nroSolicitud); $location.replace(); };
    $scope.eventos.riesgosControl    = function(){ SolicitudService.riesgosControl   ($scope.solicitud); $location.path(($scope.solicitud.controlAprobada === "A" ? "solicitud/riesgos/asignar/" : "solicitud/oficina/subsanar/") + $scope.solicitud.nroSolicitud); $location.replace(); };
    $scope.eventos.riesgosAsignar    = function(){ SolicitudService.riesgosAsignar   ($scope.solicitud); $location.path("solicitud/riesgos/revisar/" + $scope.solicitud.nroSolicitud); $location.replace(); };
    $scope.eventos.riesgosRevisar    = function(){ SolicitudService.riesgosRevisar   ($scope.solicitud); $location.path(($scope.solicitud.revisionAprobada === "A" ? "solicitud/riesgos/evaluar/" : "solicitud/oficina/subsanar/") + $scope.solicitud.nroSolicitud); $location.replace(); };
    $scope.eventos.riesgosEvaluar    = function(){ SolicitudService.riesgosEvaluar   ($scope.solicitud); $location.path("solicitud/riesgos/dictaminar/" + $scope.solicitud.nroSolicitud); $location.replace(); };
    $scope.eventos.riesgosCambiar    = function(){ SolicitudService.riesgosCambiar   ($scope.solicitud); $location.path("solicitud/riesgos/dictaminar/" + $scope.solicitud.nroSolicitud); $location.replace(); };
    $scope.eventos.riesgosDictaminar = function(){ SolicitudService.riesgosDictaminar($scope.solicitud); $location.path("solicitud/oficina/revisar/" + $scope.solicitud.nroSolicitud); $location.replace(); };
    $scope.eventos.oficinaRevisar    = function(){ SolicitudService.oficinaRevisar   ($scope.solicitud); alert("Solicitud Cerrada");/* $location.path("solicitud/riesgos/asignar/" + $scope.solicitud.nroSolicitud); $location.replace(); */ };
    $scope.eventos.oficinaSubsanar   = function(){ SolicitudService.oficinaSubsanar  ($scope.solicitud); $location.path("solicitud/riesgos/revisar/" + $scope.solicitud.nroSolicitud); $location.replace(); };

    // oficina/registrar
    // riesgos/control
    // riesgos/asignar
    // riesgos/revisar
    // riesgos/evaluar
    // riesgos/cambiar 
    // riesgos/dictaminar
    // oficina/revisar 
    // oficina/subsanar
}]);
