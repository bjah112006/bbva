package com.ibm.mant.tabla.vo;

import java.util.ArrayList;
import java.util.Collection;

import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.as.core.helpers.TipoDato;
import com.ibm.as.core.vo.AbstractVO;

public class ColumnaVO extends AbstractVO {
	
	private static final Logger LOG = LoggerFactory.getLogger(ColumnaVO.class);

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

	// Otros Campos
	private String valorRegistro;
	private Boolean tipoCampoOT;
	private Boolean tipoCampoIT;
	private Boolean tipoCampoITA;
	private Boolean tipoCampoSOM;
	private Boolean tipoCampoCHK;
	private Boolean tipoCampoEDI;
	private Boolean valorMostrarValorCheck;	
	private Boolean tipoCampoLBL;
	
	private String tipoStyle;

	private Collection<PosibleValorVO> posiblesValores;
	private Collection<SelectItem> posiblesValoresSI;
	
	private String busqueda;

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
		LOG.info("*****************************************************");
		LOG.info("METODO determinarTipoCampo DE CLASE COLUMNA VO");
		LOG.info("****************************************************");
		this.tipoCampoOT = false;
		this.tipoCampoIT = false;
		this.tipoCampoITA = false;
		this.tipoCampoSOM = false;
		this.tipoCampoCHK = false;
		this.tipoCampoLBL = false;
		this.tipoCampoEDI = false;
		
		this.tipoStyle="";
		
		this.valorMostrarValorCheck = false;
		if (this.tipoDato != null) {
			if (this.esLlavePrimaria) {
				// No se renderiza
				this.tipoCampoOT = false;
				if (TipoDato.LISTA.toString().equals(tipoDato.toUpperCase())) {
					this.tipoCampoSOM = true;
					this.tipoStyle=TipoDato.ALFANUMERICO.toString().toLowerCase().trim();
				}else if (TipoDato.STRING.toString().equals(
						tipoDato.toUpperCase()) || TipoDato.ALFANUMERICO.toString().equals(
						tipoDato.toUpperCase())) {
					    if (longitudMaxima != null && longitudMaxima > 50) {					    	
					    }else{
					       this.tipoCampoIT = true;
					       this.tipoStyle=this.tipoDato.toLowerCase().trim();
					    }
				}else if(TipoDato.INTEGER.toString().equals(tipoDato.toUpperCase())){
					this.tipoStyle=this.tipoDato.toLowerCase().trim();	
				}
			} else {
				if (TipoDato.LISTA.toString().equals(tipoDato.toUpperCase())) {
					this.tipoCampoSOM = true;
					this.tipoStyle=TipoDato.ALFANUMERICO.toString().toLowerCase().trim();
				} else if (TipoDato.BOOLEAN.toString().equals(tipoDato.toUpperCase())) {					
					this.tipoCampoCHK = true;
				} else if (TipoDato.BLOB.toString().equals(tipoDato.toUpperCase())) {
					this.tipoCampoIT = false;
					this.tipoCampoITA = false;
					this.tipoCampoEDI = true;
				} else {
					if(TipoDato.INTEGER.toString().equals(tipoDato.toUpperCase())){
						this.tipoStyle=this.tipoDato.toLowerCase().trim();	
					}
					if (TipoDato.STRING.toString().equals(
							tipoDato.toUpperCase()) || TipoDato.ALFANUMERICO.toString().equals(
							tipoDato.toUpperCase())) {

						if (longitudMaxima != null && longitudMaxima > 50) {
							this.tipoCampoIT = false;
							this.tipoCampoITA = true;													
						} else {
							this.tipoCampoIT = true;
							this.tipoCampoITA = false;	
							this.tipoStyle=this.tipoDato.toLowerCase().trim();
						}
						 
					} else {
						this.tipoCampoIT = true;
						this.tipoCampoITA = false;	
						this.tipoStyle=this.tipoDato.toLowerCase().trim();						
					}
				}
			}
		}
		
		if (this.tipoCampoOT || this.tipoCampoIT || this.tipoCampoITA || this.tipoCampoSOM || this.tipoCampoCHK || this.tipoCampoEDI) {
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

	public String getTipoStyle() {
		return tipoStyle;
	}

	public void setTipoStyle(String tipoStyle) {
		this.tipoStyle = tipoStyle;
	}

	public Boolean getTipoCampoEDI() {
		return tipoCampoEDI;
	}

	public void setTipoCampoEDI(Boolean tipoCampoEDI) {
		this.tipoCampoEDI = tipoCampoEDI;
	}
	
}
