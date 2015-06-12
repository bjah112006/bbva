package com.ibm.bbva.util;

import java.util.Date;

import com.ibm.bbva.entities.GuiaDocumentaria;

public class AyudaPanelDocumentos {

	private String descripcionTipoDoc;
	private String strPersona;
	private String colEliminar;
	private String colModificar;
	private boolean colObservar;
	private boolean std;
	private boolean docEscaneado;
	private String urlDocumento;
	private boolean docTransferencias;
	private String codigoTipoDoc;
	private Date feRegistro;
	private boolean visOpElimina;
	private boolean visOpModificar;
	private boolean visOpObservar;
	private long idCm;
	private long idDocExp;
	private boolean docGuia;
	private String flagEscaneado;
	
	private GuiaDocumentaria objGuiaDocumentaria;
	
	public AyudaPanelDocumentos(){
		
	}
	
	/* Método para mandar data el applet al modificar documento*/
	public String getTramaDocumentoCambio() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.codigoTipoDoc);
		sb.append(",");
		if (docEscaneado)
			sb.append("1");
		else
			sb.append("0");
		
		return sb.toString();
	}
	
	public String getDescripcionTipoDoc() {
		return descripcionTipoDoc;
	}
	public void setDescripcionTipoDoc(String descripcionTipoDoc) {
		this.descripcionTipoDoc = descripcionTipoDoc;
	}
	public String getStrPersona() {
		return strPersona;
	}
	public void setStrPersona(String strPersona) {
		this.strPersona = strPersona;
	}

	public GuiaDocumentaria getObjGuiaDocumentaria() {
		return objGuiaDocumentaria;
	}

	public void setObjGuiaDocumentaria(GuiaDocumentaria objGuiaDocumentaria) {
		this.objGuiaDocumentaria = objGuiaDocumentaria;
	}

	public boolean isStd() {
		return std;
	}

	public void setStd(boolean std) {
		this.std = std;
	}

	public boolean isDocEscaneado() {
		return docEscaneado;
	}

	public void setDocEscaneado(boolean docEscaneado) {
		this.docEscaneado = docEscaneado;
	}

	public String getColEliminar() {
		return colEliminar;
	}

	public void setColEliminar(String colEliminar) {
		this.colEliminar = colEliminar;
	}

	public String getColModificar() {
		return colModificar;
	}

	public void setColModificar(String colModificar) {
		this.colModificar = colModificar;
	}

	public String getUrlDocumento() {
		return urlDocumento;
	}

	public void setUrlDocumento(String urlDocumento) {
		this.urlDocumento = urlDocumento;
	}

	public boolean isDocTransferencias() {
		return docTransferencias;
	}

	public void setDocTransferencias(boolean docTransferencias) {
		this.docTransferencias = docTransferencias;
	}

	public String getCodigoTipoDoc() {
		return codigoTipoDoc;
	}

	public void setCodigoTipoDoc(String codigoTipoDoc) {
		this.codigoTipoDoc = codigoTipoDoc;
	}

	public Date getFeRegistro() {
		return feRegistro;
	}

	public void setFeRegistro(Date feRegistro) {
		this.feRegistro = feRegistro;
	}

	public boolean isVisOpElimina() {
		return visOpElimina;
	}

	public void setVisOpElimina(boolean visOpElimina) {
		this.visOpElimina = visOpElimina;
	}

	public boolean isVisOpModificar() {
		return visOpModificar;
	}

	public void setVisOpModificar(boolean visOpModificar) {
		this.visOpModificar = visOpModificar;
	}

	public long getIdCm() {
		return idCm;
	}

	public void setIdCm(long idCm) {
		this.idCm = idCm;
	}	

	public boolean isColObservar() {
		return colObservar;
	}

	public void setColObservar(boolean colObservar) {
		this.colObservar = colObservar;
	}

	public boolean isVisOpObservar() {
		return visOpObservar;
	}

	public void setVisOpObservar(boolean visOpObservar) {
		this.visOpObservar = visOpObservar;
	}

	public long getIdDocExp() {
		return idDocExp;
	}

	public void setIdDocExp(long idDocExp) {
		this.idDocExp = idDocExp;
	}
	
	public boolean isDocGuia() {
		return docGuia;
	}

	public void setDocGuia(boolean docGuia) {
		this.docGuia = docGuia;
	}

	public String getFlagEscaneado() {
		return flagEscaneado;
	}

	public void setFlagEscaneado(String flagEscaneado) {
		this.flagEscaneado = flagEscaneado;
	}
	
	
}
