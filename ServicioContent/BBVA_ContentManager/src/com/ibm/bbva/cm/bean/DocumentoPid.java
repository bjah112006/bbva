package com.ibm.bbva.cm.bean;

import java.util.Arrays;
import java.util.Calendar;

public class DocumentoPid {

	private Integer id;
	private String tipo;
	private Boolean mandatorio;
	private Calendar fechaExpiracion;
	private byte[] contenido;
	private String urlContent;
	private String pid;
	
	// Propiedades agregadas a partir de los requerimientos funcionales
	private String tipoDoi;
	private String numDoi;
	private String codCliente;

	// Parametros adicionales por Cta Cte
	private Calendar fechaCreacion;
	private String nombreArchivo;
	private String origen;

	public DocumentoPid() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Boolean getMandatorio() {
		return mandatorio;
	}

	public void setMandatorio(Boolean mandatorio) {
		this.mandatorio = mandatorio;
	}

	public byte[] getContenido() {
		return contenido;
	}

	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}

	public Calendar getFechaExpiracion() {
		return fechaExpiracion;
	}

	public void setFechaExpiracion(Calendar fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}

	public String getTipoDoi() {
		return tipoDoi;
	}

	public void setTipoDoi(String tipoDoi) {
		this.tipoDoi = tipoDoi;
	}

	public String getNumDoi() {
		return numDoi;
	}

	public void setNumDoi(String numDoi) {
		this.numDoi = numDoi;
	}

	public String getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(String codCliente) {
		this.codCliente = codCliente;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public Calendar getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Calendar fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getUrlContent() {
		return urlContent;
	}

	public void setUrlContent(String urlContent) {
		this.urlContent = urlContent;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DocumentoPid [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (tipo != null) {
			builder.append("tipo=");
			builder.append(tipo);
			builder.append(", ");
		}
		if (mandatorio != null) {
			builder.append("mandatorio=");
			builder.append(mandatorio);
			builder.append(", ");
		}
		if (fechaExpiracion != null) {
			builder.append("fechaExpiracion=");
			builder.append(fechaExpiracion);
			builder.append(", ");
		}
		if (contenido != null) {
			builder.append("contenido=");
			builder.append(Arrays.toString(contenido));
			builder.append(", ");
		}
		if (urlContent != null) {
			builder.append("urlContent=");
			builder.append(urlContent);
			builder.append(", ");
		}
		if (pid != null) {
			builder.append("pid=");
			builder.append(pid);
			builder.append(", ");
		}
		if (tipoDoi != null) {
			builder.append("tipoDoi=");
			builder.append(tipoDoi);
			builder.append(", ");
		}
		if (numDoi != null) {
			builder.append("numDoi=");
			builder.append(numDoi);
			builder.append(", ");
		}
		if (codCliente != null) {
			builder.append("codCliente=");
			builder.append(codCliente);
			builder.append(", ");
		}
		if (fechaCreacion != null) {
			builder.append("fechaCreacion=");
			builder.append(fechaCreacion);
			builder.append(", ");
		}
		if (nombreArchivo != null) {
			builder.append("nombreArchivo=");
			builder.append(nombreArchivo);
			builder.append(", ");
		}
		if (origen != null) {
			builder.append("origen=");
			builder.append(origen);
		}
		builder.append("]");
		return builder.toString();
	}

}
