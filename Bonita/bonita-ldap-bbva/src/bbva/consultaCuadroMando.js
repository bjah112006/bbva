$(document).bind('DOMNodeInserted', function(event) {
    if(event.target.nodeName == 'LI') {
        if($(event.target).hasClass("bbva-consulta")) {
            $("<li class='bbva-cuadro'><a class='bbva-cuadro' href='javascript: void(0);' onclick='abrirConsulta(\"cuadromando\")'>Cuadro de Mando</a></li>").insertAfter($(event.target));
        }
    }
});


