package pe.ibm.bean;

import java.io.Serializable;

public class ContentManager implements Serializable {

	private static final long serialVersionUID = 6106777699898136054L;

	private String codigo;
	private String estado;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
