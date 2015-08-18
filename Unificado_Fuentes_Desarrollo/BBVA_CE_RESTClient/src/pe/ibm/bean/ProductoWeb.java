package pe.ibm.bean;

import java.io.Serializable;

public class ProductoWeb implements Serializable  {

	private static final long serialVersionUID = -5523136998418632550L;

	private String idProducto;
	private String producto;
	private String subProducto;

	public ProductoWeb() {
		
		this.idProducto = "";
		this.producto = "";
		this.subProducto = "";
	}
	public ProductoWeb(String idProducto, String producto, String subProducto) {
		super();
		this.idProducto = idProducto;
		this.producto = producto;
		this.subProducto = subProducto;
	}

	public String getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
	}
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	public String getSubProducto() {
		return subProducto;
	}
	public void setSubProducto(String subProducto) {
		this.subProducto = subProducto;
	}
	
}
