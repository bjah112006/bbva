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
            monedaRiesgoGrupo: "=",
            monedaRiesgoTotal: "=",
            monedaPropuesta: "=",
            monedaOportunidad: "=",
            listaDocumentos: "=",
            listaPropuestas: "=",
            listaEtiquetas: "=",
            listaMonedas: "=",
            listaGuiaDocumentaria: "=",
            ocultarObservaciones: "=",
            registrar: "&"
        },
        templateUrl: 'src/js/directives/solicitud/template/oficina/registrar.html'
    };
});