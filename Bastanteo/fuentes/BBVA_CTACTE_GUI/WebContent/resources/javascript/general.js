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

function soloNumeros(e) {
	e = e || window.event;
	var charCode = (e.which) ? e.which : e.keyCode;
	if (!((charCode >= 48 && charCode <= 57) || (charCode >= 96 && charCode <= 105) || charCode == 8 || charCode == 9 || charCode == 37 || charCode == 39 || charCode == 46)) {
		if (e.preventDefault) {
			e.preventDefault();
		} else {
			e.returnValue = false;
		}
	}
}
function soloNumerosConDecimales(e) {
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