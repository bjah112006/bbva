var 
DateUtil = {
	yyyymmdd: 'yyyyMMdd',
	DDMMYYYY: 'dd/MM/yyyy',
	DDMMYYYYHHmmss: 'dd/MM/yyyy HH:mm:ss',
	MMDDYYYY: 'MM/dd/yyyy',
	YYYYMMDD: 'yyyy/dd/MM',
	parse: function(val, format) {
		var dd = 1, mm = 1, yy = 1900;
		
		if(format == this.DDMMYYYY) {
			dd = parseInt(val.substring(0,2), 10);
			mm = parseInt(val.substring(3,5), 10);
			yy = parseInt(val.substring(6,10), 10);
		} else if(format == this.MMDDYYYY) {
			mm = parseInt(val.substring(0,2), 10);
			dd = parseInt(val.substring(3,5), 10);
			yy = parseInt(val.substring(6,10), 10);
		} else if(format == this.YYYYMMDD) {
			yy = parseInt(val.substring(0,4), 10);
			mm = parseInt(val.substring(5,7), 10);
			dd = parseInt(val.substring(8,10), 10);
		} else if(format == this.yyyymmdd) {
			yy = parseInt(val.substring(0,4), 10);
			mm = parseInt(val.substring(4,6), 10);
			dd = parseInt(val.substring(6,8), 10);
		}
		
		return new Date(yy, mm - 1, dd, 0, 0, 0);
	},
	compareTo: function(val1, val2, format) {
		return (this.parse(val1, format).getTime() > this.parse(val2, format).getTime());
	},
	addDays: function(date, numDays) {
		return new Date(date.getTime() + (numDays * 24 * 3600 * 1000));
	},
	lastDay: function(val, format) {
		var date = this.parse(val, format);
		
		d = 1;
		m = date.getMonth() + 1;
		y = (m == 12 ? 1 : 0) + date.getFullYear();
		m = (m == 12 ? 0 : m);
		
		date = new Date(y, m, d, 0, 0, 0);
		
		return this.addDays(date, -1);
	},
	toString: function(date, format) {
		var dd = '01', mm = '01', yy = '1900', hh = "00", nn = "00", ss = "", val = "01/01/1900";
		
		if(format == this.DDMMYYYY) {
			dd = date.getDate();
			mm = date.getMonth() + 1;
			yy = date.getFullYear();
			
			val  = (dd < 10 ? '0' : '') + dd + "/";
			val += (mm < 10 ? '0' : '') + mm + "/";
			val += yy;
		} else if(format == this.DDMMYYYYHHmmss) {
			dd = date.getDate();
			mm = date.getMonth() + 1;
			yy = date.getFullYear();
			hh = date.getHours();
			nn = date.getMinutes();
			ss = date.getSeconds();
			
			val  = (dd < 10 ? '0' : '') + dd + "/";
			val += (mm < 10 ? '0' : '') + mm + "/";
			val += yy;
			val += " ";
			val += (hh < 10 ? '0' : '') + hh + ":";
			val += (nn < 10 ? '0' : '') + nn + ":";
			val += (ss < 10 ? '0' : '') + ss;
		}
		
		return val;
	},
	longToDate: function(milliseconds) {
		return new Date(milliseconds);
	}
},

buscarArchivo = function() {
	var fecha = $("#txtFiltroFecha").val();
	abrirReporte(fecha);
},

abrirReporte = function(fecha) {
	if(fecha == undefined){
		fecha = "";
	}
	var __xhr = $.ajax({
        type: "post", 
        dataType: 'json',
        cache: false,
        url: obtenerContexto('listFile?fecha=' + fecha),
        data: {"method": "list"},
        success: function(data) {
            var html = "";
            var row = "";
            for(var i=0; i < data.files.length; i++) {
                row = '<div class="tr tr_' + (i + 1) + ' ' + (i % 2 == 0? 'odd' : 'even') + '">';
                row += '<div class="td td_reporte td_1 odd even"><a href="' + obtenerContexto('listFile') + '?method=download&file=/' + data.files[i].filename + '">' + data.files[i].filename + '</a></div>';
                row += '<div class="td td_fecha td_2 td_last odd even">' + DateUtil.toString(DateUtil.longToDate(data.files[i].datecreate), DateUtil.DDMMYYYY) + '</div>';
                row += '</div>';
                
                html += row;
            }
            
            $("#tblReporte").html(html);
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert("Error al descargar el listado de reportes");   
        }
    });
    
    $('#bbva-panel').css("display", "block");
},

obtenerContexto = function(_url) {
    var context = "bonita/portal"; 
    var url = document.URL;
    var tmp = url.split(context);

    return tmp[0] + context + "/" + _url;
},

createTable = function() {
    var table = "";
        table +='<div class="datatable table_view_details">';
        table +='    <div class="table">';
        table +='        <div class="thead">';
        table +='            <div class="tr odd tr_1">';
        table +='                <div class="th th_reporte _1 odd even th_1 th_type_title">Reporte</div>';
        table +='                <div class="th th_fecha _2 even odd th_2 th_type_title th_last">Fecha</div>';
        table +='            </div>';
        table +='        </div>';
        table +='        <div id="tblReporte" class="tbody">'; // current
        table +='            <div class="tr tr_1 tr_last even"><div class="td td_reporte td_1 odd even">No existen reportes</div><div class="td td_fecha td_2 td_last even odd">&nbsp;</div></div>';
        table +='        </div>';
        table +='    </div>';
        table +='</div>';
    return table;
},

createDialog = function() {
    var html = '';
        html += '<div id="bbva-panel" style="display: none;" class="popupwrapper">';
        html += '    <div class="popupframe" style="position: absolute; z-index: 1001; width: 100%;">';
        html += '        <div class="popupcontainer" style="top: 0px; left: 0px;">';
        html += '            <div class="header popupcontainerheader" id="popupcontainerheader"><a class="close_popup" onclick=\'$("#bbva-panel").css("display", "none");\'>Close popup</a></div>';
        html += '            <div class="body popupcontainerbody" id="popup">';
        html += '                <div class="page">';
        html += '                    <div class="header"><h1>Reportes</h1></div>';
        html += '                    <div class="itemlistingpage">';
        html += '                     <div class="tables_panel">';
        html += '                    	<form class="form form_search search" onSubmit="buscarArchivo(); return false;">';
        html += '                    		<div class="formentries" style="width:50%">';
        html += '                    			<div class="formentry formentry_query query text">';
        html += '                    				<div class="label"><label title="Introduzca fecha del archivo que desea buscar" for="txtFiltroFecha"></label></div>';
        html += '                    				<div class="input"><input type="text" name="txtFiltroFecha" title="Introduzca fecha del archivo que desea buscar" id="txtFiltroFecha" maxlength="10" placeholder="Búsqueda Fecha..."/></div>';
        html += '                    			</div>';
        html += '                    		</div>';
        html += '                    		<div class="formactions" style="width:52%"><a class="search btn btn-action" href="#">Búsqueda</a></div>';
        html += '                    	</form>';
        html += '                     </div>';
        html += '                    </div>';
        html += '                    <div class="body">';
        html += '                       <div class="itemlistingpage"><div class="tables_panel">' + createTable() + '</div></div>';
        html += '                    </div>';
        html += '                  </div>';
        html += '              </div>';
        html += '        </div>';
        html += '    </div>';
        html += '    <div class="popupoverlay" style="position: fixed; z-index: 1000; top: 0px; left: 0px; height: 100%; width: 100%;"></div>';
        html += '</div>';
    
    return html;
};

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
			if(page == "") {
				$(".bbva-consulta").addClass("current");
			} else if(page == "cuadromando"){
				$(".bbva-cuadro").addClass("current");
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
            $("<li class='bbva-reporte'><a class='bbva-reporte' href='javascript: void(0);' onclick='abrirReporte()'>Reporte</a></li>").insertAfter($(event.target));
            $("body").append(createDialog());
        } else if($(event.target).hasClass("bbva-reporte")) {
            $("<li class='bbva-consulta'><a class='bbva-consulta' href='javascript: void(0);' onclick='abrirConsulta(\"\")'>Consulta</a></li>").insertAfter($(event.target));
        } else if($(event.target).hasClass("bbva-consulta")) {
            $("<li class='bbva-cuadro'><a class='bbva-cuadro' href='javascript: void(0);' onclick='abrirConsulta(\"cuadromando\")'>Cuadro de Mando</a></li>").insertAfter($(event.target));
        }
    }
	validarDocumento();
});


