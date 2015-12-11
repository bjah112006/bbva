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
            {id: 1, value: 'Obligatorio Generales', documentos: [
                {id: 1, value: 'Reporte de Cr\u00E9dito Propuesto'},
                {id: 2, value: 'Reporte de Cr\u00E9dito de L\u00EDnea Anterior'},
                {id: 3, value: 'Posici\u00F3n del Cliente / Grupo Econ\u00F3mico'},
                {id: 4, value: 'Programa Financiero para importes mayores a USD 1.5M (PFA / PF 2.9)'},
                {id: 5, value: 'EEFF Metodizados'},
                {id: 6, value: 'Hoja de Rating Nacar'},
                {id: 7, value: 'EEFF Sunat con detalles o Auditado de cierre'},
                {id: 8, value: 'EEFF Situacional con detalles (Abril en adelante y con no mas de 3 meses de desfase)'},
                {id: 9, value: 'Informe comercial vigente (no mayor a 6 meses)'}
            ]},
            {id: 2, value: 'Opcionales / Sugeridas', documentos: [
                {id: 10, value: 'PDT\'s mensuales para contraste de las ventas'},
                {id: 11, value: 'Informe de visita'},
                {id: 12, value: 'Broschure / Memoria de la empresa'},
                {id: 13, value: 'En caso de grupo econ\u00F3mico incluir EEFF consolidado o combinado (plantilla)'},
                {id: 14, value: 'Pack gesti\u00F3n para dimensionamiento de l\u00EDneas'}
            ]},
            {id: 3, value: 'Obligatorio seg\u00FAn corresponda', documentos: [
                {id: 15, value: 'Reporte de Crédito de Operaciones Puntuales Vigentes'},
                {id: 16, value: 'En caso de contar con fiador solitario adjuntar declaraci\u00F3n patrimonial de fiadores'},
                {id: 17, value: 'En caso de contar con comfort letter de matriz o fianza solidaria de relacionadas, incluir EEFF mas recientes'},
                {id: 18, value: 'En caso de ser empresa global BBVA contar con opini\u00F3n del gestor principal'},
                {id: 19, value: 'En caso de empresas agr\u00EDcolas incluir informe de perito'},
                {id: 20, value: 'En caso de l\u00EDnea para cartas fianzas incluir Back Log de proyectos  ejecutados y en ejecuci\u00F3n.'},
                {id: 21, value: 'En caso de l\u00EDnea para cartas fianzas incluir Pipeline de proyectos por ejecutar.'},
                {id: 22, value: 'En caso de l\u00EDnea para operaciones de mediano plazo incluir Flujo de caja con supuestos'},
                {id: 23, value: 'En caso de l\u00EDnea de descuento de letras incluir detalle de protestos (infocorp) y efectividad (nacar)'},
                {id: 24, value: 'En caso de l\u00EDnea para comercio exterior incluir reporte de Adex Data Trade'},
                {id: 25, value: 'En caso de l\u00EDnea de sobregiro incluir saldos medios y resumen de ingresos/egresos en cuenta corriente'}
            ]}
        ],
        listaEtiquetas: [
            {id: 1, value: 'Crecer'},
            {id: 2, value: 'Mantener'},
            {id: 3, value: 'Reducir'},
            {id: 4, value: 'Extinguir'}
        ],
        listaFunciones: [
            {id: 1, value: 'Ejecutivo Corporativo Local'},
            {id: 2, value: 'Ejecutivo Mediana Empresa'},
            {id: 3, value: 'Ejecutivo Gran Empresa'},
            {id: 4, value: 'Ejecutivo de Financiación Estructurada'},
            {id: 5, value: 'Ejecutivo BIBEC'}
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
    
    service.riesgosControl = function(_solicitud) {
        console.log("riesgosControl");
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
