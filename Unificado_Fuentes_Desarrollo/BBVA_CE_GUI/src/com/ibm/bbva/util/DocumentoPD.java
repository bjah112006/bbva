package com.ibm.bbva.util;

import java.util.Date;

/**
 * 
 * @author rangulo
 * @date 22/08/2014
 *
 */
public class DocumentoPD {
	
	private String descripcionTipoDoc = "";
	private String colEliminar = "";
	private String colModificar = "";
	private boolean colObservar = false;
	private boolean std = false;
	private boolean docEscaneado = false;
	private String urlDocumento = "";
	private boolean docTransferencias = false;
	private String codigoTipoDoc = "";
	private Date feRegistro = new Date();
	private boolean visOpElimina = false;
	private boolean visOpModificar = false;
	private boolean visOpObservar = false;
	private long idCm = 0L;
	/**
	 * @return el descripcionTipoDoc
	 */
	public String getDescripcionTipoDoc() {
		return descripcionTipoDoc;
	}
	/**
	 * @param descripcionTipoDoc el descripcionTipoDoc a establecer
	 */
	public void setDescripcionTipoDoc(String descripcionTipoDoc) {
		this.descripcionTipoDoc = descripcionTipoDoc;
	}
	/**
	 * @return el colEliminar
	 */
	public String getColEliminar() {
		return colEliminar;
	}
	/**
	 * @param colEliminar el colEliminar a establecer
	 */
	public void setColEliminar(String colEliminar) {
		this.colEliminar = colEliminar;
	}
	/**
	 * @return el colModificar
	 */
	public String getColModificar() {
		return colModificar;
	}
	/**
	 * @param colModificar el colModificar a establecer
	 */
	public void setColModificar(String colModificar) {
		this.colModificar = colModificar;
	}
	/**
	 * @return el colObservar
	 */
	public boolean isColObservar() {
		return colObservar;
	}
	/**
	 * @param colObservar el colObservar a establecer
	 */
	public void setColObservar(boolean colObservar) {
		this.colObservar = colObservar;
	}
	/**
	 * @return el std
	 */
	public boolean isStd() {
		return std;
	}
	/**
	 * @param std el std a establecer
	 */
	public void setStd(boolean std) {
		this.std = std;
	}
	/**
	 * @return el docEscaneado
	 */
	public boolean isDocEscaneado() {
		return docEscaneado;
	}
	/**
	 * @param docEscaneado el docEscaneado a establecer
	 */
	public void setDocEscaneado(boolean docEscaneado) {
		this.docEscaneado = docEscaneado;
	}
	/**
	 * @return el urlDocumento
	 */
	public String getUrlDocumento() {
		return urlDocumento;
	}
	/**
	 * @param urlDocumento el urlDocumento a establecer
	 */
	public void setUrlDocumento(String urlDocumento) {
		this.urlDocumento = urlDocumento;
	}
	/**
	 * @return el docTransferencias
	 */
	public boolean isDocTransferencias() {
		return docTransferencias;
	}
	/**
	 * @param docTransferencias el docTransferencias a establecer
	 */
	public void setDocTransferencias(boolean docTransferencias) {
		this.docTransferencias = docTransferencias;
	}
	/**
	 * @return el codigoTipoDoc
	 */
	public String getCodigoTipoDoc() {
		return codigoTipoDoc;
	}
	/**
	 * @param codigoTipoDoc el codigoTipoDoc a establecer
	 */
	public void setCodigoTipoDoc(String codigoTipoDoc) {
		this.codigoTipoDoc = codigoTipoDoc;
	}
	/**
	 * @return el feRegistro
	 */
	public Date getFeRegistro() {
		return feRegistro;
	}
	/**
	 * @param feRegistro el feRegistro a establecer
	 */
	public void setFeRegistro(Date feRegistro) {
		this.feRegistro = feRegistro;
	}
	/**
	 * @return el visOpElimina
	 */
	public boolean isVisOpElimina() {
		return visOpElimina;
	}
	/**
	 * @param visOpElimina el visOpElimina a establecer
	 */
	public void setVisOpElimina(boolean visOpElimina) {
		this.visOpElimina = visOpElimina;
	}
	/**
	 * @return el visOpModificar
	 */
	public boolean isVisOpModificar() {
		return visOpModificar;
	}
	/**
	 * @param visOpModificar el visOpModificar a establecer
	 */
	public void setVisOpModificar(boolean visOpModificar) {
		this.visOpModificar = visOpModificar;
	}
	/**
	 * @return el visOpObservar
	 */
	public boolean isVisOpObservar() {
		return visOpObservar;
	}
	/**
	 * @param visOpObservar el visOpObservar a establecer
	 */
	public void setVisOpObservar(boolean visOpObservar) {
		this.visOpObservar = visOpObservar;
	}
	/**
	 * @return el idCm
	 */
	public long getIdCm() {
		return idCm;
	}
	/**
	 * @param idCm el idCm a establecer
	 */
	public void setIdCm(long idCm) {
		this.idCm = idCm;
	}
	
	
	

}
