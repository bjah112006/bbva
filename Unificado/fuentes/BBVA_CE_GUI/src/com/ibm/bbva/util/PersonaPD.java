package com.ibm.bbva.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.soap.providers.com.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.PanelDocumentosMB;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Persona;

/**
 * 
 * @author rangulo
 * @date 22/08/2014
 *
 */
public class PersonaPD {
	
	private static final Logger LOG = LoggerFactory.getLogger(PersonaPD.class);	
	private Persona persona  = null;
	private List<TipoDocumentoPD> lstTipoDocumentoPDs = null;
	
	

	public PersonaPD() {
		lstTipoDocumentoPDs = new ArrayList<TipoDocumentoPD>();
	}

	/**
	 * @return el lstTipoDocumentoPDs
	 */
	public List<TipoDocumentoPD> getLstTipoDocumentoPDs() {
		return lstTipoDocumentoPDs;
	}

	/**
	 * @param lstTipoDocumentoPDs el lstTipoDocumentoPDs a establecer
	 */
	public void setLstTipoDocumentoPDs(List<TipoDocumentoPD> lstTipoDocumentoPDs) {
		this.lstTipoDocumentoPDs = lstTipoDocumentoPDs;
	}
	
	public void addTipoDocumentoPD(TipoDocumentoPD tipoDocumentoPD){
		this.lstTipoDocumentoPDs.add(tipoDocumentoPD);
	}

	/**
	 * @return el persona
	 */
	public Persona getPersona() {
		return persona;
	}

	/**
	 * @param persona el persona a establecer
	 */
	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public void ordernarTipoDocumentos() {
		
		List<TipoDocumentoPD> lstTipoDocOblig = new ArrayList<TipoDocumentoPD>();
		List<TipoDocumentoPD> lstTipoDocNoObllig = new ArrayList<TipoDocumentoPD>();
		
		Set<String> resultSet = new HashSet<String>();
		List<TipoDocumentoPD> lstTipoDocumentoPDsTmp = new ArrayList<TipoDocumentoPD>();
		
		for(TipoDocumentoPD tipoDocumentoPD : this.lstTipoDocumentoPDs){
			resultSet.add(String.valueOf(tipoDocumentoPD.getTipoDocumento().getId()));
		}
		
		for(String idTipoDocumento : resultSet){
			for(TipoDocumentoPD tipoDocumentoPD : this.lstTipoDocumentoPDs){
				if(String.valueOf(tipoDocumentoPD.getTipoDocumento().getId()).equals(idTipoDocumento)){
					lstTipoDocumentoPDsTmp.add(tipoDocumentoPD);
					break;
				}
			}
		}
		
		this.lstTipoDocumentoPDs.clear();
		this.lstTipoDocumentoPDs = lstTipoDocumentoPDsTmp;
		
		for (TipoDocumentoPD tipoDocumentoPD : this.lstTipoDocumentoPDs) {
			if (tipoDocumentoPD.getObligatorio().equals(Constantes.CODIGO_DOCUMENTO_OBLIGATORIO)) {
				lstTipoDocOblig.add(tipoDocumentoPD);
			}else{
				lstTipoDocNoObllig.add(tipoDocumentoPD);
			}
		}
		
		Collections.sort(lstTipoDocOblig, new Comparator<TipoDocumentoPD>() {
			public int compare(TipoDocumentoPD td1, TipoDocumentoPD td2) {
				String des1 = td1.getTipoDocumento().getDescripcion();
				String des2 = td2.getTipoDocumento().getDescripcion();
				return des1.compareTo(des2);
			}
		});
		
		Collections.sort(lstTipoDocNoObllig, new Comparator<TipoDocumentoPD>() {
			public int compare(TipoDocumentoPD td1, TipoDocumentoPD td2) {
				String des1 = td1.getTipoDocumento().getDescripcion();
				String des2 = td2.getTipoDocumento().getDescripcion();
				return des1.compareTo(des2);
			}
		});
	
		this.lstTipoDocumentoPDs.clear();
		LOG.info("lstTipoDocumentoPDs.size:" + lstTipoDocumentoPDs.size());
		for (TipoDocumentoPD tipoDocumentoPD : lstTipoDocOblig) {
			this.lstTipoDocumentoPDs.add(tipoDocumentoPD);
			LOG.info("Oblig. TD:" + tipoDocumentoPD.getTipoDocumento().getDescripcion());
		}
		
		for (TipoDocumentoPD tipoDocumentoPD : lstTipoDocNoObllig) {
			this.lstTipoDocumentoPDs.add(tipoDocumentoPD);
			LOG.info("No oblig. TD:" + tipoDocumentoPD.getTipoDocumento().getDescripcion());
		}
		
	}

}
