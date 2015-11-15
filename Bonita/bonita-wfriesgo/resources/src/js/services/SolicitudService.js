'use strict';

bonitaApp.factory('ListadoService', function() {
    return {
        listaMonedas: [
            {id: 1, value: 'PEN'}, // Nuevos Soles
            {id: 2, value: 'USD'}  // Dolares Americanos
        ],
        listaPropuestas: [
            {id: 1, value: 'Programa Financiero'},
            {id: 2, value: 'Pr\u00F3rroga de P.F'},
            {id: 3, value: 'O.P. Corto Plazo'},
            {id: 4, value: 'O.P Largo Plazo'},
            {id: 5, value: 'O.P Actualizaci\u00F3n'},
            {id: 6, value: 'O.P. Pr\u00F3rroga - Renov.'},
            {id: 7, value: 'O.P. Modificación'},
            {id: 8, value: 'O.P 100% Garantizado'},
            {id: 9, value: 'Refinanciado / Judicial'}
        ],
        listaDocumentos: [
            {id: 'L', value: 'DNI'},
            {id: 'R', value: 'RUC'}
        ],
        listaGuiaDocumentaria: [
            {id: 1, value: 'Grupo 1', documentos: [
                {id: 1, value: 'Documento 1'},
                {id: 2, value: 'Documento 2'},
                {id: 3, value: 'Documento 3'}
            ]},
            {id: 2, value: 'Grupo 2', documentos: [
                {id: 4, value: 'Documento 4'},
                {id: 5, value: 'Documento 5'},
                {id: 6, value: 'Documento 6'}
            ]},
            {id: 3, value: 'Grupo 3', documentos: [
                {id: 7, value: 'Documento 7'},
                {id: 8, value: 'Documento 8'},
                {id: 9, value: 'Documento 9'}
            ]}
        ],
        listaEtiquetas: [
            {id: 1, value: 'Crecer'},
            {id: 2, value: 'Mantener'},
            {id: 3, value: 'Reducir'},
            {id: 4, value: 'Extinguir'}
        ],
        listaTiposEvaluadores: [
            {id: 'AN', value: 'Analista'},
            {id: 'JG', value: 'Jefe de Grupo'},
            {id: 'SG', value: 'Subgerente'}
        ],
        listaAmbitos: [
            {id: 'AN', value: 'Analista'},
            {id: 'JG', value: 'Jefe de Grupo'},
            {id: 'SG', value: 'Subgerente'},
            {id: 'CT', value: 'CTO'},
            {id: 'CE', value: 'CEC'},
            {id: 'GC', value: 'GCR'}
        ],
        listaUsuarios: [
            {id: 1, value: 'Analista 1'},
            {id: 2, value: 'Jefe de Grupo 1'},
            {id: 3, value: 'Subgerente 1'}
        ]
    }
});

bonitaApp.factory('SolicitudService', function() {
    var service = {};
    
    service.buscar = function(_solicitud) {
        return [];
    };

    service.obtener = function(_id) {
        return {};
    };
    
    service.oficinaRegistrar = function(_solicitud) {
        console.log("oficinaRegistrar");
    };
    
    service.riesgosAsignar = function(_solicitud) {
        console.log("riesgosAsignar");
    };
    
    service.riesgosRevisar = function(_solicitud) {
        console.log("riesgosRevisar");
    };
    
    service.riesgosEvaluar = function(_solicitud) {
        console.log("riesgosEvaluar");
    };
    
    service.riesgosCambiar  = function(_solicitud) {
        console.log("riesgosCambiar");
    };
    
    service.riesgosDictaminar = function(_solicitud) {
        console.log("riesgosDictaminar");
    };
    
    service.oficinaRevisar = function(_solicitud) {
        console.log("oficinaRevisar");
    };
    
    service.oficinaSubsanar = function(_solicitud) {
        console.log("oficinaSubsanar");
    };
    
    return service;
});
