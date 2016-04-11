package com.ibm.bbva.ctacte.util;

import java.io.Serializable;

import com.ibm.bbva.ctacte.enums.EnumTextAlign;
import com.ibm.bbva.ctacte.enums.EnumTipoDatoJava;
import com.ibm.bbva.ctacte.enums.EnumVerticalAlign;

public class ColumnaDinamica implements Serializable {
	private static final long serialVersionUID = -6358644011978005641L;

	private String nombre;
	private String nombreAtributo;
	private Integer x;
	private Integer y;
	private Integer alto;
	private Integer ancho;
	private EnumVerticalAlign alineacionVertical;
	private EnumTextAlign alineacionInterior;
	private String tipoDato;
	private String formato;

	public ColumnaDinamica() {

	}

	public void instanciarColumna(String nombre, EnumTipoDatoJava tipoDato,
			String formato) {
		this.tipoDato = tipoDato.getTipoDato();
		this.nombre = nombre;
		this.formato = formato;
	}

	public void setInicioFinal(Integer x, Integer y) {
		this.x = x;
		this.y = y;
	}

	public void setDimencion(Integer alto, Integer ancho) {
		this.alto = alto;
		this.ancho = ancho;
	}

	public void setPosicion(EnumVerticalAlign alineacionVertical,
			EnumTextAlign alineacionInterior) {
		this.alineacionVertical = alineacionVertical;
		this.alineacionInterior = alineacionInterior;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Integer getAlto() {
		return alto;
	}

	public void setAlto(Integer alto) {
		this.alto = alto;
	}

	public Integer getAncho() {
		return ancho;
	}

	public void setAncho(Integer ancho) {
		this.ancho = ancho;
	}

	public EnumVerticalAlign getAlineacionVertical() {
		return alineacionVertical;
	}

	public void setAlineacionVertical(EnumVerticalAlign alineacionVertical) {
		this.alineacionVertical = alineacionVertical;
	}

	public EnumTextAlign getAlineacionInterior() {
		return alineacionInterior;
	}

	public void setAlineacionInterior(EnumTextAlign alineacionInterior) {
		this.alineacionInterior = alineacionInterior;
	}

	public String getTipoDato() {
		return tipoDato;
	}

	public void setTipoDato(String tipoDato) {
		this.tipoDato = tipoDato;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getNombreAtributo() {
		return nombreAtributo;
	}

	public void setNombreAtributo(String nombreAtributo) {
		this.nombreAtributo = nombreAtributo;
	}

}
