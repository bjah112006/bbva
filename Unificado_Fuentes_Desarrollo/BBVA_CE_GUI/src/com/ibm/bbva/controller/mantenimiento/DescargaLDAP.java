package com.ibm.bbva.controller.mantenimiento;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.session.CarterizacionCEBeanLocal;
import com.ibm.bbva.session.MutitablaBeanLocal;
import com.ibm.bbva.util.Util;

import com.ibm.bbva.controller.*;


@ManagedBean(name = "descargaLDAP")
@SessionScoped
public class DescargaLDAP extends AbstractMBean
{

	private static final Logger LOG = LoggerFactory.getLogger(DescargaLDAP.class);
	
	@EJB
	private CarterizacionCEBeanLocal carterizacionCEBean;
	
	@EJB
	private MutitablaBeanLocal multitablaBeanLocal;
	
	private String filtroSeleccionadoTipo;
	private String filtroSeleccionadoCodigo;
	private String filtroSeleccionadoCarterizacion;
	private String filtroSeleccionadoEstado;
	
	private String edicionTipo;
	private String edicionCodigo;
	private String edicionEstado;
	
	private List<SelectItem> listaTipo;
	private List<SelectItem> listaCarterizacion;
	private List<SelectItem> listaEstado;
	
	public DescargaLDAP() 
	{
		super();
	}
	
	public String agregar() 
	{	
		return "/descargaLDAP/formManActualizaDescargaLDAP?faces-redirect=true";
	}
	
	public String cancelar() 
	{
		return "/descargaLDAP/formManConsultaDescargaLDAP?faces-redirect=true";
	}
		
	
	public void cargarCarterizacion()
	{
		this.listaCarterizacion = Util.crearItems(carterizacionCEBean.buscarTodos(), true, "id", "descripcion");	
	}
	
	public void cargarTipo()
	{
		this.listaTipo = Util.crearItems(multitablaBeanLocal.buscarPorPadre(Constantes.PARAMETRO_TIPO_DESCARGA_LDAP), true, "codigo", "nombre");	
	}
	
	public void cargarEstado()
	{
		this.listaEstado = Util.crearItems(multitablaBeanLocal.buscarPorPadre(Constantes.PARAMETRO_ESTADO_DESCARGA_LDAP), true, "codigo", "nombre");	
	}
	
	public String getFiltroSeleccionadoTipo() {
		return filtroSeleccionadoTipo;
	}
	public void setFiltroSeleccionadoTipo(String filtroSeleccionadoTipo) {
		this.filtroSeleccionadoTipo = filtroSeleccionadoTipo;
	}
	public String getFiltroSeleccionadoCodigo() {
		return filtroSeleccionadoCodigo;
	}
	public void setFiltroSeleccionadoCodigo(String filtroSeleccionadoCodigo) {
		this.filtroSeleccionadoCodigo = filtroSeleccionadoCodigo;
	}
	public String getFiltroSeleccionadoCarterizacion() {
		return filtroSeleccionadoCarterizacion;
	}
	public void setFiltroSeleccionadoCarterizacion(
			String filtroSeleccionadoCarterizacion) {
		this.filtroSeleccionadoCarterizacion = filtroSeleccionadoCarterizacion;
	}
	public String getFiltroSeleccionadoEstado() {
		return filtroSeleccionadoEstado;
	}
	public void setFiltroSeleccionadoEstado(String filtroSeleccionadoEstado) {
		this.filtroSeleccionadoEstado = filtroSeleccionadoEstado;
	}
	public List<SelectItem> getListaTipo() {
		return listaTipo;
	}
	public void setListaTipo(List<SelectItem> listaTipo) {
		this.listaTipo = listaTipo;
	}
	public List<SelectItem> getListaCarterizacion() {
		return listaCarterizacion;
	}
	public void setListaCarterizacion(List<SelectItem> listaCarterizacion) {
		this.listaCarterizacion = listaCarterizacion;
	}
	public List<SelectItem> getListaEstado() {
		return listaEstado;
	}
	public void setListaEstado(List<SelectItem> listaEstado) {
		this.listaEstado = listaEstado;
	}

	public String getEdicionTipo() {
		return edicionTipo;
	}

	public void setEdicionTipo(String edicionTipo) {
		this.edicionTipo = edicionTipo;
	}

	public String getEdicionCodigo() {
		return edicionCodigo;
	}

	public void setEdicionCodigo(String edicionCodigo) {
		this.edicionCodigo = edicionCodigo;
	}

	public String getEdicionEstado() {
		return edicionEstado;
	}

	public void setEdicionEstado(String edicionEstado) {
		this.edicionEstado = edicionEstado;
	}
		
}
