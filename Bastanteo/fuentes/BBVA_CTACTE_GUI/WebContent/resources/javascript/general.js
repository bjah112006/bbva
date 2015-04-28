function mensajeApplet (mensaje) {
	alert (mensaje);
}

function applet (idApplet) {
	if (navigator.userAgent.indexOf('MSIE') != -1) {
		return document[idApplet];
	} else {
		return document.getElementById(idApplet);
	}
}

function popupPDF (direccion) {
	window.open(direccion, 'WindowName', 
			'dependent=yes, menubar=no, height=600,width=950, toolbar=no, resizable=1');
}

function getCallbackParams(data) {
	//return eval("("+data.responseXML.getElementsByTagName("extension")[0].textContent+")");
	return eval("("+$(data.responseXML).find("extension[id='custom']").text()+")");
}

function errorAjaxWebSeal(data) {
	//alert("type:" + data.type + ", status:" + data.status + ", serverErrorName:" + data.serverErrorName + ", serverErrorMessage" + data.serverErrorMessage + ", responseCode:" + data.responseCode);
	if(data.status != null && data.status != "undefined" && data.status == "malformedXML"){
		location.href = $("[id='form:paginaInicio']").val();
	}
}