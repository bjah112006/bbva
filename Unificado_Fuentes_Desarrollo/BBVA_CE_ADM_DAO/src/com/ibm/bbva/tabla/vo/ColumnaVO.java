package com.ibm.bbva.tabla.vo;

import java.util.ArrayList;
import java.util.Collection;

import javax.faces.model.SelectItem;

import com.ibm.as.core.helpers.TipoDato;
import com.ibm.as.core.vo.AbstractVO;

public class ColumnaVO extends AbstractVO {

	static final long serialVersionUID = 1243526157593L;

	private Integer id;
	private String nombre;
	private Boolean esLlavePrimaria;
	private Integer idTabla;
	private Boolean esRequerido;
	private Integer longitudMaxima;
	private String nombreMostrar;
	private String tipoDato;
	private Boolean esLlaveForanea;
	private String busqueda;
	private String plantilla;
	private String ordenColumna;
	private String flagDependencia;
	
	// Otros Campos
	private String valorRegistro;
	private Boolean tipoCampoOT;
	private Boolean tipoCampoIT;
	private Boolean tipoCampoITA;
	private Boolean tipoCampoSOM;
	private Boolean tipoCampoCHK;
	private Boolean valorMostrarValorCheck;	
	private Boolean tipoCampoLBL;	
	
	private Boolean soloLectura;	

	private Collection<PosibleValorVO> posiblesValores;
	private Collection<SelectItem> posiblesValoresSI;	
	
	/**
	 * Constructor
	 */
	public ColumnaVO() {
		super();
	}

	/**
	 * Determina el tipo de control en la interfaz grafica que se usará para
	 * representar a la columna según el tipo de dato de esta.
	 */
	public void determinarTipoCampo() {
		this.tipoCampoOT = false;
		this.tipoCampoIT = false;
		this.tipoCampoITA = false;
		this.tipoCampoSOM = false;
		this.tipoCampoCHK = false;
		this.tipoCampoLBL = false;
		this.valorMostrarValorCheck = false;
		if (this.tipoDato != null) {
			if (this.esLlavePrimaria) {
				// No se renderiza
				this.tipoCampoOT = false;
				if (TipoDato.LISTA.toString().equals(tipoDato.toUpperCase())) {
					this.tipoCampoSOM = true;
				}else if (TipoDato.STRING.toString().equals(
						tipoDato.toUpperCase())) {
					    if (longitudMaxima != null && longitudMaxima > 50) {					    	
					    }else{
					       this.tipoCampoIT = true;
					    }
				}
			} else {
				if (TipoDato.LISTA.toString().equals(tipoDato.toUpperCase())) {
					this.tipoCampoSOM = true;
				} else if (TipoDato.BOOLEAN.toString().equals(tipoDato.toUpperCase())) {					
					this.tipoCampoCHK = true;
				} else {
					if (TipoDato.STRING.toString().equals(
							tipoDato.toUpperCase())) {
						if (longitudMaxima != null && longitudMaxima > 50) {
							this.tipoCampoIT = false;
							this.tipoCampoITA = true;													
						} else {
							this.tipoCampoIT = true;
							this.tipoCampoITA = false;							
						}
					} else {
						this.tipoCampoIT = true;
						this.tipoCampoITA = false;						
					}
				}
			}
		}
		
		if (this.tipoCampoOT || this.tipoCampoIT || this.tipoCampoITA || this.tipoCampoSOM || this.tipoCampoCHK) {
			tipoCampoLBL=true;
		}
	}

	/**
	 * Carga el combo de los posibles valores de una columna.
	 * 
	 * @return the posiblesValoresSI
	 */
	public Collection<SelectItem> getPosiblesValoresSI() {
		if (posiblesValoresSI == null || posiblesValoresSI.size() == 0) {
			if (posiblesValores != null && posiblesValores.size() > 0) {
								
				for (PosibleValorVO vo : posiblesValores) {
					if (posiblesValoresSI == null) {
						posiblesValoresSI = new ArrayList<SelectItem>();
					}
					SelectItem si = new SelectItem();
					si.setValue(vo.getValor());
					si.setLabel(vo.getEtiqueta());
					posiblesValoresSI.add(si);
				}
			}
		}
		return posiblesValoresSI;
	}

	public String getValorMostrarValorLista() {
		String valorMostrar = "";
		if (this.valorRegistro != null && posiblesValoresSI != null
				&& posiblesValores.size() > 0) {
			for (SelectItem si : this.posiblesValoresSI) {
				if (si != null) {
					if (valorRegistro.equals(si.getValue())) {
						valorMostrar = si.getLabel();
					}
				}
			}
		}
		return valorMostrar;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Boolean isEsLlavePrimaria() {
		return esLlavePrimaria;
	}

	public void setEsLlavePrimaria(Boolean esLlavePrimaria) {
		this.esLlavePrimaria = esLlavePrimaria;
	}

	public Integer getIdTabla() {
		return idTabla;
	}

	public void setIdTabla(Integer idTabla) {
		this.idTabla = idTabla;
	}

	public Boolean isEsRequerido() {
		return esRequerido;
	}

	public void setEsRequerido(Boolean esRequerido) {
		this.esRequerido = esRequerido;
	}

	public Integer getLongitudMaxima() {
		return longitudMaxima;
	}

	public void setLongitudMaxima(Integer longitudMaxima) {
		this.longitudMaxima = longitudMaxima;
	}

	public String getNombreMostrar() {
		return nombreMostrar;
	}

	public void setNombreMostrar(String nombreMostrar) {
		this.nombreMostrar = nombreMostrar;
	}

	public String getTipoDato() {
		return tipoDato;
	}

	public void setTipoDato(String tipoDato) {
		this.tipoDato = tipoDato;
	}

	/**
	 * @return the valorRegistro
	 */
	public String getValorRegistro() {
		return valorRegistro;
	}

	/**
	 * @param valorRegistro
	 *            the valorRegistro to set
	 */
	public void setValorRegistro(String valorRegistro) {
		this.valorRegistro = valorRegistro;
	}

	/**
	 * @return the tipoCampoOT
	 */
	public Boolean getTipoCampoOT() {
		return tipoCampoOT;
	}

	/**
	 * @param tipoCampoOT
	 *            the tipoCampoOT to set
	 */
	public void setTipoCampoOT(Boolean tipoCampoOT) {
		this.tipoCampoOT = tipoCampoOT;
	}

	/**
	 * @return the tipoCampoIT
	 */
	public Boolean getTipoCampoIT() {
		return tipoCampoIT;
	}

	/**
	 * @param tipoCampoIT
	 *            the tipoCampoIT to set
	 */
	public void setTipoCampoIT(Boolean tipoCampoIT) {
		this.tipoCampoIT = tipoCampoIT;
	}

	/**
	 * @return the tipoCampoITA
	 */
	public Boolean getTipoCampoITA() {
		return tipoCampoITA;
	}

	/**
	 * @param tipoCampoITA
	 *            the tipoCampoITA to set
	 */
	public void setTipoCampoITA(Boolean tipoCampoITA) {
		this.tipoCampoITA = tipoCampoITA;
	}

	/**
	 * @return the tipoCampoSOM
	 */
	public Boolean getTipoCampoSOM() {
		return tipoCampoSOM;
	}

	/**
	 * @param tipoCampoSOM
	 *            the tipoCampoSOM to set
	 */
	public void setTipoCampoSOM(Boolean tipoCampoSOM) {
		this.tipoCampoSOM = tipoCampoSOM;
	}

	/**
	 * @return the posiblesValores
	 */
	public Collection<PosibleValorVO> getPosiblesValores() {
		return posiblesValores;
	}

	/**
	 * @param posiblesValores
	 *            the posiblesValores to set
	 */
	public void setPosiblesValores(Collection<PosibleValorVO> posiblesValores) {
		this.posiblesValores = posiblesValores;
	}

	/**
	 * @param posiblesValoresSI
	 *            the posiblesValoresSI to set
	 */
	public void setPosiblesValoresSI(Collection<SelectItem> posiblesValoresSI) {
		this.posiblesValoresSI = posiblesValoresSI;
	}

	/**
	 * @return the esLlavePrimaria
	 */
	public Boolean getEsLlavePrimaria() {
		return esLlavePrimaria;
	}

	/**
	 * @return the esRequerido
	 */
	public Boolean getEsRequerido() {
		return esRequerido;
	}

	public void setEsLlaveForanea(Boolean esLlaveForanea) {
		this.esLlaveForanea = esLlaveForanea;
	}

	public Boolean isEsLlaveForanea() {
		return esLlaveForanea;
	}
	
	public Boolean getTipoCampoCHK() {
		return tipoCampoCHK;
	}

	public void setTipoCampoCHK(Boolean tipoCampoCHK) {
		this.tipoCampoCHK = tipoCampoCHK;
	}
	
	public Boolean getValorMostrarValorCheck() {
		valorMostrarValorCheck=false;	
		if (valorRegistro!=null && valorRegistro.equals("1") ){
			valorMostrarValorCheck = true;
		}	
		return valorMostrarValorCheck;
	}

	public void setValorMostrarValorCheck(Boolean valorMostrarValorCheck) {
		this.valorRegistro = "0";		
		if (valorMostrarValorCheck) {
			this.valorRegistro = "1";
		}
		this.valorMostrarValorCheck = valorMostrarValorCheck;
	}

	public String getBusqueda() {
		return busqueda;
	}

	public void setBusqueda(String busqueda) {
		this.busqueda = busqueda;
	}
	
	public Boolean getTipoCampoLBL() {
		return tipoCampoLBL;
	}

	public void setTipoCampoLBL(Boolean tipoCampoLBL) {
		this.tipoCampoLBL = tipoCampoLBL;
	}

	public String getPlantilla() {
		return plantilla == null ? "" : plantilla;
	}

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

	public String getOrdenColumna() {
		return ordenColumna;
	}

	public void setOrdenColumna(String ordenColumna) {
		this.ordenColumna = ordenColumna;
	}

	public String getFlagDependencia() {
		return flagDependencia;
	}

	public void setFlagDependencia(String flagDependencia) {
		this.flagDependencia = flagDependencia;
	}

	public Boolean getSoloLectura() {
		return soloLectura;
	}

	public void setSoloLectura(Boolean soloLectura) {
		this.soloLectura = soloLectura;
	}
}
