package com.ibm.bbva.ctacte.controller.form;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.util.RespuestaServicioDTO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="mensaje")
@ViewScoped
public class MensajeMB extends AbstractMBean{
	
	private static final long serialVersionUID = 584115224652554718L;
	private static final Logger LOG = LoggerFactory.getLogger(MensajeMB.class);
	
	private String texto;
	private String textoRespuesta;
	
	@PostConstruct
	public void iniciar () {
		LOG.info("iniciar ()");
		texto = getRequestParameter(ConstantesAdmin.PARAMETRO_MENSAJE);
		
		RespuestaServicioDTO rpta = (RespuestaServicioDTO) Util.getObjectSession(ConstantesAdmin.RESPUESTA_SERVICIO_SESION);
		String exonerado = (String) Util.getObjectSession(ConstantesAdmin.RESPUESTA_FLAG_COBRO_COMISION_SESION);
		LOG.info("Exonerado Sesion: " + exonerado);
		if (rpta != null) {
			//textoRespuesta = "Servicio Cobro Comisi�n: "+rpta.getCodigo()+", "+rpta.getDescripcion();
			//textoRespuesta = "Servicio Cobro Comisi�n: "+(rpta.getCodigo().equalsIgnoreCase("0")?"S�":"NO  Motivo:"+rpta.getCodigo()+" Descripci�n:"+rpta.getDescripcion());
			if (rpta.getCodigo().equalsIgnoreCase("0")) {
				textoRespuesta = "El cobro de comisi�n de bastanteo se realiz� correctamente.";
			} else {
				textoRespuesta = "El cobro de comisi�n fall�. Verificar la tarea de Gesti�n de Cobro de Comisi�n en su bandeja de pendientes.";
			}
			Util.removeObjectSession(ConstantesAdmin.RESPUESTA_SERVICIO_SESION);
		}
		
		if(exonerado!=null && exonerado.length()>0){
			textoRespuesta = "Exonerado del cobro de comisi�n de bastanteo de poderes por incluir una Cuenta Proveedor.";
		}
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getTexto() {
		return texto;
	}

	public String getTextoRespuesta() {
		return textoRespuesta;
	}

	public void setTextoRespuesta(String textoRespuesta) {
		this.textoRespuesta = textoRespuesta;
	}
}
