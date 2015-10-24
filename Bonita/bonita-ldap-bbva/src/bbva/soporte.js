$(document).bind('DOMNodeInserted', function(event) {
    if (event.target.nodeName == 'LI') {
        if ($(event.target).hasClass("processlistinguser")) {
            $("<li class='bbva-documento'><a class='bbva-documento' href='javascript: void(0);' onclick='abrirConsulta(\"documents\")'>Documentos</a></li>").insertAfter($(event.target));
        }
    }
});