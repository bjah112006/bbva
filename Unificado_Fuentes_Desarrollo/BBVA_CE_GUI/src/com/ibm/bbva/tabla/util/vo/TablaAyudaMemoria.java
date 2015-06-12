package com.ibm.bbva.tabla.util.vo;

public class TablaAyudaMemoria {
	private String codigoUsuario;
	private String perfilUsuario;
	private String fechaHora;
	private String comentario;
	private String colEliminar;
	private String colEditar;
	private String idAyudaMemoria;
	private boolean visOpElimina = false;
	private boolean visOpModificar = false;
	
	public String getCodigoUsuario() {
		return codigoUsuario;
	}
	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}
	public String getPerfilUsuario() {
		return perfilUsuario;
	}
	public void setPerfilUsuario(String perfilUsuario) {
		this.perfilUsuario = perfilUsuario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public String getComentario() {
		return comentario;
	}
	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}
	public String getFechaHora() {
		return fechaHora;
	}
	public String getColEliminar() {
		return colEliminar;
	}
	public void setColEliminar(String colEliminar) {
		this.colEliminar = colEliminar;
	}
	public String getColEditar() {
		return colEditar;
	}
	public void setColEditar(String colEditar) {
		this.colEditar = colEditar;
	}
	public String getIdAyudaMemoria() {
		return idAyudaMemoria;
	}
	public void setIdAyudaMemoria(String idAyudaMemoria) {
		this.idAyudaMemoria = idAyudaMemoria;
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
	
}
