'use strict';

bonitaApp.directive('oficinaRegistrar', function () {
    return {
        restrict: 'E',
        scope: {
            desactivar: "=",
            solicitud: "=",
            documento: "=",
            propuesta: "=",
            etiqueta: "=",
            funcion: "=",
            monedaRiesgoGrupo: "=",
            monedaRiesgoTotal: "=",
            monedaPropuesta: "=",
            monedaOportunidad: "=",
            listaDocumentos: "=",
            listaPropuestas: "=",
            listaEtiquetas: "=",
            listaMonedas: "=",
            listaGuiaDocumentaria: "=",
            listaFunciones: "=",
            ocultarObservaciones: "=",
            registrar: "&"
        },
        templateUrl: 'src/js/directives/solicitud/template/oficina/registrar.html'
    };
});