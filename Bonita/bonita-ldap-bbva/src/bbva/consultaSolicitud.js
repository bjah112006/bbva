$(document).bind('DOMNodeInserted', function(event) {
    if (event.target.nodeName == 'LI') {
        if ($(event.target).hasClass("processlistinguser")) {
            $("<li class='bbva-reporte'><a class='bbva-reporte' href='javascript: void(0);' onclick='abrirReporte()'>Reporte</a></li>").insertAfter($(event.target));
            $("body").append(createDialog());
        } else if ($(event.target).hasClass("bbva-reporte")) {
            $("<li class='bbva-consulta'><a class='bbva-consulta' href='javascript: void(0);' onclick='abrirConsulta(\"\")'>Consulta</a></li>").insertAfter($(event.target));
        }
    }
});