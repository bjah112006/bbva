/**
 * Inicializa el escaner.
 * 
 */
function inicializarEscaner(comando, parametros) {
	var mensajeError = "Error al activar el escaner: ";
	
	try {
		resetearVisorDocumentos();
		var resultado = processManager.StartProcess(comando, parametros);

		var valores = resultado.split('*');
		if (valores.length != 2) {
			alert(mensajeError + "Respuesta no válida.");
			return false;				
		}
			
		var codigo = valores[0];
		if (codigo == '1') {
			var mensaje = valores[1];
			alert(mensajeError + mensaje);
			return false;
		} 
	} catch (e) {
		alert(mensajeError + e);
	}
	
	return false;
}

function compruebaExtension(archivo) { 
	   extensiones= new Array(".pdf"); 
	   extension = (archivo.substring(archivo.lastIndexOf("."))).toLowerCase(); 
	   permitida = false; 
	   for (var i = 0; i < extensiones.length; i++) { 
	         if (extensiones[i] == extension) { 
	         permitida = true; 
	         break; 
	         } 
	    }
	   if (!permitida) { 
	    	  alert("Compruebe la extensi\u00f3n de los archivos a subir. \n S\u00f3lo se pueden subir archivos con extensiones: " 
	    			  + extensiones.join());
	   }
	  
	   return permitida; 
	} 