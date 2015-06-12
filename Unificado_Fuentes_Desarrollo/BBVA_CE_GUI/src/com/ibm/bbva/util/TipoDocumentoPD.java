package com.ibm.bbva.util;

import java.util.ArrayList;
import java.util.List;

import com.ibm.bbva.entities.TipoDocumento;

/**
 * 
 * @author rangulo
 * @date 22/08/2014
 *
 */
public class TipoDocumentoPD {
	
	private TipoDocumento tipoDocumento = null;
	private List<AyudaPanelDocumentos> lstAyudaPanelDocumentos = null;
	private String obligatorio = "";

	public TipoDocumentoPD() {
		lstAyudaPanelDocumentos = new ArrayList<AyudaPanelDocumentos>();
	}

	
	
	/**
	 * @return el lstAyudaPanelDocumentos
	 */
	public List<AyudaPanelDocumentos> getLstAyudaPanelDocumentos() {
		return lstAyudaPanelDocumentos;
	}



	/**
	 * @param lstAyudaPanelDocumentos el lstAyudaPanelDocumentos a establecer
	 */
	public void setLstAyudaPanelDocumentos(
			List<AyudaPanelDocumentos> lstAyudaPanelDocumentos) {
		this.lstAyudaPanelDocumentos = lstAyudaPanelDocumentos;
	}



	public void addAyudaPanelDocumento(AyudaPanelDocumentos ayudaPanelDocumentos){
		this.lstAyudaPanelDocumentos.add(ayudaPanelDocumentos);
	}

	/**
	 * @return el tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento el tipoDocumento a establecer
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}



	public String getObligatorio() {
		return obligatorio;
	}



	public void setObligatorio(String obligatorio) {
		this.obligatorio = obligatorio;
	}

	
	
	
	
	

}
