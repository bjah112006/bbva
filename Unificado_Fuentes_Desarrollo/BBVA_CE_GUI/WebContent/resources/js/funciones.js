/* Inhabilita la ventana padre al usar el scroll */
/* si existe una ventana popup activa            */ 
$(window).scroll(function(){
	var popup = $("#HX_DLG_SCRATCH_0");
	if (popup != null) {
		$(popup).css("height", ($(this).height() + $(this).scrollTop())+"px");
	}
}); 

$(document).ready(function(){	
   /*Funcion que permite el ingreso - solo Numeros*/
   $(".soloNumeros").keypress(function (event) {
       if (event.which > 31 && (event.which < 48 || event.which > 57)) {
    	   event.preventDefault();
       }     
   });
   
   $( ".formatoFecha" ).datepicker({
	   autoSize: true,
	   buttonImage: "/BBVA_CE_GUI/resources/images/1x1.gif",
	   buttonImageOnly: true,
	   changeMonth: true,
	   changeYear: true,
	   constrainInput: true,
	   dateFormat: "dd/mm/yy",
	   firstDay: 1,
	   selectOtherMonths: true,
	   showOn: "button",
	   showOtherMonths: true,
	   closeText: 'Cerrar',
	   prevText: '&#x3c;Ant',
	   nextText: 'Sig&#x3e;',
	   currentText: 'Hoy',
	   monthNames: ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'],
	   monthNamesShort: ['Ene','Feb','Mar','Abr','May','Jun','Jul','Ago','Sep','Oct','Nov','Dic'],
	   dayNames: ['Domingo','Lunes','Martes','Mi&eacute;rcoles','Jueves','Viernes','S&aacute;bado'],
	   dayNamesShort: ['Dom','Lun','Mar','Mi&eacute;','Juv','Vie','S&aacute;b'],
	   dayNamesMin: ['D','L','M','M','J','V','S'],
	   weekHeader: 'Sm',
	   yearRange:'2014:+10'
   });
   /*Valida el formato de fecha  */	
   /*$(".formatoFecha").keypress(function (event) {
     
     var valor = this.value;
     
     if (event.which  > 31 && (event.which  < 47 || event.which  > 57)) {
	    event.preventDefault(); 
     }
     else
     {
       if (validaFecha(this) == 1) {
    	  event.preventDefault(); 
          return;
       }
       if (event.which  == 47) {    	    
          if (valor.length ==1) valor = '0' + valor;          
          if (valor.length ==4) valor = valor.substring(0, 3) + '0' + valor.substring(3, 4);
    	  if (valor.length ==0 || (valor.length ==1 && valor.substring(0, 1) == '0') || valor.length == 3 || valor.length > 5) event.preventDefault();
       }  
       if (valor.length == 2 || valor.length == 5) {
    	  if (event.which >=48 && event.keyCode <= 57) {
    		  valor = valor + '/';  
    	  }          
          this.value = valor;
          return;
       }
     }
   });*/
   
   /*Funcion que permite el ingreso - solo Hora*/
   $(".soloHora").keypress(function (event) {     
       var valor = this.value;

       if (event.which > 31 && (event.which < 47 || event.which > 57)) {
       	   event.preventDefault(); 
       }
       else
       {
        if (validaHora(this) == 1) {
       	   event.preventDefault();            
           return;
        }
        if (valor.length == 2 ) {
           if(event.which==13)  
        	   valor = valor + ':00';
           else  
        	   valor = valor + ':';
           this.value=valor;
           return;
        }        
       }         
   });   
   
   /*Valida el formato de nro cuenta  */	
   $(".formatoNroCuenta").keypress(function (event) {    
     var valor = this.value;     
     if (event.which  > 31 && (event.which  < 47 || event.which  > 57)) {
	    event.preventDefault(); 
     }
     else
     {    	 
       if (valor.length == 4 || valor.length == 9 || valor.length == 12) {
    	  valor = valor + '-';    	            
          this.value = valor;
          return;
       }
     }
   });   
   
   $(".soloNumerosConDecimales").keypress(function (event) {
	    if(window.event) // IE
	    {
	        charCode = event.keyCode;
	        if (charCode!=46 && charCode > 31 && (charCode < 48 || charCode > 57)) {
	            window.event.returnValue=0;
	        }else{
		        if (autonumerico(this, event) ==1) {
		            window.event.returnValue=0;
		        } 
	        }
	    }
	    else if(event.which) // Netscape/Firefox/Opera
	    {
	        charCode = event.which;
	        if (charCode!=46 && charCode > 31 && (charCode < 48 || charCode > 57)) {
	        	event.preventDefault();
	        }else{	        
		        if (autonumerico(this, event) ==1) {
		        	event.preventDefault();
		        }	  
	        }
	    }
	}); 
   
   $(".soloNumerosConDecimales").keyup(function (event) {
	    if(window.event) // IE
	    {
	        charCode = event.keyCode;
	        if (charCode!=46 && charCode > 31 && (charCode < 48 || charCode > 57)) {
	            window.event.returnValue=0;
	        }else{
		        if (autonumerico(this, event) ==1) {
		            window.event.returnValue=0;
		        } 
	        }
	    }
	    else if(event.which) // Netscape/Firefox/Opera
	    {
	        charCode = event.which;
	        if (charCode!=46 && charCode > 31 && (charCode < 48 || charCode > 57)) {
	        	event.preventDefault();
	        }else{	        
		        if (autonumerico(this, event) ==1) {
		        	event.preventDefault();
		        }	  
	        }
	    }
	});    
 });

function devolverFocoRegistrarExp(){
	if (typeof activeElementName != "undefined" && activeElementName != null && activeElementName != "undefined" && activeElementName != ""){
		if(document.getElementById(activeElementName) != null){
			if(typeof document.getElementById(activeElementName).value != "undefined" && document.getElementById(activeElementName).value != null && document.getElementById(activeElementName).value != "undefined"){
				setTimeout(function(){
					if(document.getElementById(activeElementName).style.display != "none" && document.getElementById(activeElementName).disabled != true){
						document.getElementById(activeElementName).focus();
					}
			    }, 1000);
			}
		}
	}
	procesandoEst='';
}

   
 /*Valida dias de Fecha Ingresada*/
 function validaFecha(campo) {
   var valor = campo.value;
   var sDay=valor.substr(0,2);
   var iDay=parseInt(sDay,10);
   var sMonth=valor.substr(3,2);
   var iMonth=parseInt(sMonth,10);
   var sYear=valor.substr(6,4);
   var iYear=parseInt(sYear,10);

   if (iDay > 31) {
	   campo.value = '';
       return 1;
    }    
    if (iMonth > 12) {
    	campo.value = sDay;
        return 1;
    }
    if (sYear.length==4 && iYear < 1900) {
    	campo.value = sDay+'/'+sMonth;	        
        return 1;
    }
    if(iMonth == 2 && iDay > 29) {
    	campo.value = sDay;	        
        return 1;
    }

    if((iDay >= 1 && iDay <= 31) && (iMonth >= 1 && iMonth <= 12))
    {   if((iMonth == 4 || iMonth == 6 || iMonth == 9 || iMonth == 11) && (iDay > 30)) {
    	   campo.value = sDay+'/'+sMonth;
           return 1;
        }
        if(iMonth == 2 && sYear.length >= 4)
        {   if((iYear%4 == 0) && (iYear%100 != 0 || iYear%400 == 0))
            {
                if (iDay > 29) {
                	campo.value = sDay+'/'+sMonth;	                    
                    return 1;
                }
            }
            else
            {   if (iDay > 28) {
            	   campo.value = sDay+'/'+sMonth;
	               return 1;
	            }
	        }
	    }
    }
	return 0;
}
 
/*Valida Hora Ingresada*/
 function validaHora(campo) {
   var valor = campo.value;
   var sHora=valor.substr(0,2);
   var iHora=parseInt(sHora,10);
   var sMin=valor.substr(3,2);
   var iMin=parseInt(sMin,10);
   
   if(iHora > 23){
 	 campo.value='';
      return 1;
   }
   
   //alert('iMin: '+sMin.length+' : '+iMin);
   if(iMin > 59 || (sMin.length==1 && iMin >= 6)){
 	 campo.value='';
     return 1;
   }
   return 0;
 }
 
 function clickGuardar(form) {
	   var id = form.id;   

	   verificar(form);
	   
	   $("#"+id+"\\:btnEjecutaAppletGuardar").click();
	}
 
/*abre popup para copiar archivos escaneados*/
function clickAceptar(form) {
   var id = form.id;   

   verificar(form);
   
   $("#"+id+"\\:btnEjecutaApplet").click();
}
function clickAceptarCu30(form) {
   var id = form.id;  
      
   verificar(form);
   
   $("#"+id+"\\:btnEjecutaAppletCu30").click();   
}
function clickAceptarGenerico(form, idBoton) {
	var id = form.id;
	verificar(form);
	$("#"+id+"\\:"+idBoton).click();
}

function obtenerApplet () {
	if (navigator.userAgent.indexOf('MSIE') !=-1) {
		return document.appletBrowser;
	} else {
		
	}
}

function autonumerico(o, event) {
   var plantilla = "###,###,###.##";

   if(window.event) // IE
       charCode = event.keyCode;
   else if(event.which) // Netscape/Firefox/Opera
       charCode = event.which;

   if (plantilla.length > 0 ) {
	   var pos = plantilla.indexOf(".");
	   var nentera = plantilla.substr(0, pos).length;
	   var ndecimal = plantilla.substr(pos + 1).length;
	   
	   var valor = o.value;
	   if (charCode==44 || (event.type === 'keypress' && charCode == 46) || (charCode >= 48 && charCode <= 57)) {
		   valor = valor + String.fromCharCode(charCode);   
	   }	   
	   
	   var epunto = valor.indexOf(".");

	    if (epunto==-1) {
	      if (valor.length > nentera) {
	         o.value = valor.substr(0,nentera)+'.'+valor.substr(nentera);
	         return 1;
	      }else{	
	    	  valor = valor.replace(/,/g, "");
	    	  if (valor.length > 3 && valor.length <= 6){
   		          o.value = valor.substr(0, valor.length - 3) + ',' + valor.substr(valor.length - 3);
	    		  return 1;
	    	  }
	    	  if (valor.length > 6 && valor.length <= 9){
    		      o.value = valor.substr(0, valor.length - 6)+','+valor.substr( valor.length - 6, 3) + ',' + valor.substr(valor.length - 3);
	    		  return 1;
	    	  }	
	      }
	   }else{
	      if (event.type === 'keypress' && charCode==46 && valor.lastIndexOf(".")>epunto) return 1;
	      if (valor.substr(epunto + 1).length > ndecimal) return 1;
	   }
	   o.value = valor;
	   return 1;
   }
}

//$(document).keydown(function (e) {
$(".habilitaEnter").keydown(function(e){
	alert(e.keyCode);
	  // if(e.keyCode==27 || e.keyCode==13){
		if (e.keyCode==27){
	       //console.info("nada");
	       e.preventDefault();
	   }
});


$(".deshabilitaEnter").keypress(function (event) {
	alert(event.which);
	if (event.which==27 || event.which==13){
		event.preventDefault();
	}
});

function mensajeApplet (mensaje) {
	alert (mensaje);
}

/* Para el modal de Procesando */
var modal = (function(){
	var 
	method = {},
	$overlay,
	$modal,
	$content,
	$close;

	// Center the modal in the viewport
	method.center = function () {
		var top, left;

		top = Math.max($(window).height() - $modal.outerHeight(), 0) / 2;
		left = Math.max($(window).width() - $modal.outerWidth(), 0) / 2;

		$modal.css({
			top:top + $(window).scrollTop(), 
			left:left + $(window).scrollLeft()
		});
	};

	// Open the modal
	method.open = function (settings) {
		$content.empty().append(settings.content);

		$modal.css({
			width: settings.width || 'auto', 
			height: settings.height || 'auto'
		});

		method.center();
		$(window).bind('resize.modal', method.center);
		$modal.show();
		$overlay.show();
	};

	// Close the modal
	method.close = function () {
		$modal.hide();
		$overlay.hide();
		$content.empty();
		$(window).unbind('resize.modal');
	};

	// Generate the HTML and add it to the document
	$overlay = $('<div id="overlay"></div>');
	$modal = $('<div id="modal"></div>');
	$content = $('<div id="content"></div>');
	//$close = $('<a id="close" href="#">close</a>');

	$modal.hide();
	$overlay.hide();
	$modal.append($content, $close);

	$(document).ready(function(){
		$('body').append($overlay, $modal);						
	});

	/*$close.click(function(e){
		e.preventDefault();
		method.close();
	});*/

	return method;
}());

// Wait until the DOM has loaded before querying the document
/*$(document).ready(function(){

	$.get('ajax.html', function(data){
		modal.open({content: data});
	});

	$('a#howdy').click(function(e){
		modal.open({content: "Hows it going?"});
		e.preventDefault();
	});
});*/

function mostrarProcesando (event) {
	var elem = document.getElementsByTagName('span');
    var spanApplet = null;
    var img;
    for(var i = 0; i < elem.length; i++) {
        att = elem[i].getAttribute("id");
        if(att != null){
	        if(att.indexOf('appletContentRefresh') != -1) {
	         	spanApplet = elem[i];
		      	break;
	        }
        }
    }



	if (event.status == 'begin') {
		//$('input[type=checkbox]').attr('disabled','true');
		//$('input[type=checkbox]').css("display", "none");
		//$("#frmVerificarConformidadExpediente\\:appletContentRefresh").css("display", "none");
		if(spanApplet != null){
        	$(spanApplet).css("display", "none");
    	}

		
		img = '<img src="/BBVA_CE_GUI/resources/images/loading.gif" height="60" width="60"><span style="text-align:center; display:block; margin-top:5px">Procesando...</span>';
		procesandoEst='1';
		modal.open({content: img});
    }
	if (event.status == 'success') {
		//$('input[type=checkbox]').removeAttr('disabled');
		//$('input[type=checkbox]').css("display", "");
		//$("#frmVerificarConformidadExpediente\\:appletContentRefresh").css("display", "block");
		if(spanApplet != null){
        	$(spanApplet).css("display", "block");
    	}

		
		if (typeof activeElementName != "undefined" && activeElementName != null && activeElementName != "undefined" && activeElementName != ""){
			if(document.getElementById(activeElementName) != null){
				if(typeof document.getElementById(activeElementName).value != "undefined" && document.getElementById(activeElementName).value != null && document.getElementById(activeElementName).value != "undefined"){
					if(document.getElementById(activeElementName).style.display != "none" && document.getElementById(activeElementName).disabled != true){
						document.getElementById(activeElementName).focus();
					}
				}
				else{
					setTimeout(function(){
						document.getElementById(activeElementName).focus();
				    }, 200);
				}
			}
		}
				
		modal.close();

    }
}

function onkeydownNumeroConDecimales(e) {
	e = e || window.event;
	var charCode = (e.which) ? e.which : e.keyCode;
	if (!((charCode >= 48 && charCode <= 57) || (charCode >= 96 && charCode <= 105) || charCode == 190 || charCode == 110 || charCode == 8 || charCode == 9 || charCode == 37 || charCode == 39 || charCode == 46)) {
		if (e.preventDefault) {
			e.preventDefault();
		} else {
			e.returnValue = false;
		}
	}
}

function validate_formato(valor) {
	var arrayTasa = valor.split(".");
	if (arrayTasa.length == 1 || arrayTasa.length == 2) {
		if (arrayTasa.length == 1) {
			if (arrayTasa[0] != "" && parseInt(arrayTasa[0]) != 0 && arrayTasa[0].length <= 3)
				return true;
			else 
				return false;
		} else {
			if (arrayTasa[0] != "" && arrayTasa[0].length <= 3 && arrayTasa[1] != "" && arrayTasa[1].length == 1)
				return true;
			else 
				return false;
		}
	} else 
		return false;
}

function disableEnterKey(e) {
	var key;
	var okEvent='1';
	
	if (procesandoEst != null) {
		if (procesandoEst == '1') {
			okEvent='2';
		}
	}
	if (okEvent == '1') {
		if (window.event)
			key = window.event.keyCode;     //IE
		else
			key = e.which;     //firefox
		if(key == 13)
			return false;
		else
			return true;
	} else {
		return false;
	}
}
