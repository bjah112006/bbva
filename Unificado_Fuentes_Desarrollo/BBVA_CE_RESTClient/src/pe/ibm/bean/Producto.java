package pe.ibm.bean;

import java.io.Serializable;

public class Producto implements Serializable  {

	private static final long serialVersionUID = -5523136998418632550L;

	private String idProducto;
//	private String producto;
//	private String subProducto;

	public Producto() {
		
		this.idProducto = "";
//		this.producto = "";
//		this.subProducto = "";
	}
	public Producto(String idProducto, String producto, String subProducto) {
		super();
		this.idProducto = idProducto;
//		this.producto = producto;
//		this.subProducto = subProducto;
	}

	public String getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
	}
	
}
