abrirConsulta = function(page) {
	var height = $(window).height() - $("#header").outerHeight() - 70;
	
    var __xhr = $.ajax({
        type: "post", 
        dataType: 'html',
        cache: false,
        async: false,
        url: obtenerContexto("bbva/consulta.html"),
		beforeSend: function(xhr) {
			window.parent.document.getElementById("initloader").style.display = "block";
		},
        success: function(data) {
        	var bodyContent = $("#body");
			$(".current").removeClass("current");
			if(page == "documents") {
				$(".bbva-documento").addClass("current");
			}

        	bodyContent.html(data);
            bodyContent.find("#panelIzq").css("height", height + "px");

            if($("#panelAngular").length > 0) {
                var url = document.URL;
                var tmp = url.split("bonita");
            	$("#panelAngular").attr("src", tmp[0] + "bonita/apps/wfpyme/home/#/" + page);
            }

			$(window).resize();
        },
        error: function (xhr, ajaxOptions, thrownError) {
			window.parent.document.getElementById("initloader").style.display = "none";
            alert("Error al abrir pantalla de consulta");
        }
    });
};

validarDocumento = function() {
	var longitudPermitida = 10; //Expresada en MB
	var size = longitudPermitida*1024*1024;
	
	$('input[type="file"]').each(function() {
		var $this = $(this);
	    if ($this.val() != '') {
	    	if(size < this.files[0].size){
	    		alert("Documento supera los " + longitudPermitida + " MB permitidos. Por favor seleccione otro archivo.");
	    		//console.log("Documento supera los " + longitudPermitida + " MB permitidos. Por favor seleccione otro archivo.");
	    		$(".bonita_form_button").attr("disabled", "disabled");
	    	}else{
	    		$(".bonita_form_button").removeAttr('disabled');
	    	}
	    } 
	});
}

obtenerContexto = function(_url) {
    var context = "bonita/portal"; 
    var url = document.URL;
    var tmp = url.split(context);

    return tmp[0] + context + "/" + _url;
};

$(window).resize(function(){
	if($("#panelAngular").length > 0) {
		$("#panelAngular").css("width", ($(window).width() - ($("#panelConsulta").outerWidth())) + "px");
		$("#panelAngular").css("height", ($(window).height() - $("#header").outerHeight()) + "px");
	}
});

$(document).ready(function(){
});

$(document).bind('DOMNodeInserted', function(event) {
	if(event.target.nodeName == 'LI') {
		if($(event.target).hasClass("processlistinguser")) {
			$("<li class='bbva-documento'><a class='bbva-documento' href='javascript: void(0);' onclick='abrirConsulta(\"documents\")'>Documentos</a></li>").insertAfter($(event.target));
		}
		/*if($(event.target).hasClass("processlistinguser")) {
            $("<li class='bbva-reporte'><a class='bbva-reporte' href='javascript: void(0);' onclick='abrirReporte()'>Reporte</a></li>").insertAfter($(event.target));
            $("body").append(createDialog());
        } else if($(event.target).hasClass("bbva-reporte")) {
            $("<li class='bbva-consulta'><a class='bbva-consulta' href='javascript: void(0);' onclick='abrirConsulta(\"\")'>Consulta</a></li>").insertAfter($(event.target));
        } else if($(event.target).hasClass("bbva-consulta")) {
            $("<li class='bbva-cuadro'><a class='bbva-cuadro' href='javascript: void(0);' onclick='abrirConsulta(\"cuadromando\")'>Cuadro de Mando</a></li>").insertAfter($(event.target));
        } else if($(event.target).hasClass("bbva-cuadro")) {
            $("<li class='bbva-documento'><a class='bbva-documento' href='javascript: void(0);' onclick='abrirConsulta(\"documents\")'>Documentos</a></li>").insertAfter($(event.target));
        }*/
    }
	validarDocumento();
});


