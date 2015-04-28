package com.ibm.bbva.ctacte.util;

public class RespuestaServicioDTO {

	private String codigo;
	private String descripcion;

	public RespuestaServicioDTO(String codigo, String descripcion) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
	}
	
	public RespuestaServicioDTO(pe.com.grupobbva.sce.qsp5.CtHeaderRs header) {
		super();
		this.codigo = header.getCodigo();
		this.descripcion = header.getDescripcion();
	}
	
	public RespuestaServicioDTO(pe.com.grupobbva.accpj.pagser.CtHeaderRs header) {
		super();
		this.codigo = header.getCodigo();
		this.descripcion = header.getDescripcion();
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
