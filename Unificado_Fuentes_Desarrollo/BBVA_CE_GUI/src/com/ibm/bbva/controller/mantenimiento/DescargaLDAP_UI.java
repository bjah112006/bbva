package com.ibm.bbva.controller.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.session.CarterizacionCEBeanLocal;
import com.ibm.bbva.session.DescargaLDAPBeanLocal;
import com.ibm.bbva.session.DescargaLDAPCarterizBeanLocal;
import com.ibm.bbva.session.MutitablaBeanLocal;
import com.ibm.bbva.tabla.util.vo.DescargaLDAPVO;
import com.ibm.bbva.util.Util;

import com.ibm.bbva.controller.*;
import com.ibm.bbva.entities.CarterizacionCE;
import com.ibm.bbva.entities.DescargaLDAP;
import com.ibm.bbva.entities.DescargaLDAPCarteriz;


@ManagedBean(name = "descargaLDAP")
@SessionScoped
public class DescargaLDAP_UI extends AbstractSortPagDataTableMBean
{

	private static final Logger LOG = LoggerFactory.getLogger(DescargaLDAP_UI.class);
	
	@EJB
	private CarterizacionCEBeanLocal carterizacionCEBean;
	
	@EJB
	private MutitablaBeanLocal multitablaBeanLocal;
	
	@EJB
	private DescargaLDAPBeanLocal descargaLDAPBeanLocal;
	
	@EJB
	private DescargaLDAPCarterizBeanLocal descargaLDAPCarterizBeanLocal;
	
	private String filtroSeleccionadoTipo;
	private String filtroSeleccionadoCodigo;
	private String filtroSeleccionadoCarterizacion;
	private String filtroSeleccionadoEstado;
	
	private String edicionId;
	private String edicionTipo;
	private String edicionCodigo;
	private String edicionEstado;
	private List<String> edicionCarterizacion;
	
	private List<SelectItem> listaTipo;
	private List<SelectItem> listaCarterizacionFiltro;
	private List<SelectItem> listaCarterizacionEdicion;
	private List<SelectItem> listaEstado;
	
	private List<DescargaLDAPVO> listaDescargaLDAP;

	public DescargaLDAP_UI() 
	{
		super();
	}
	
	public String agregar() 
	{	
		this.setEdicionId("");
		this.setEdicionTipo(null);
		this.setEdicionCodigo("");
		this.setEdicionEstado("A");	
		this.edicionCarterizacion = new ArrayList<String>();
		return "/descargaLDAP/formManActualizaDescargaLDAP?faces-redirect=true";
	}
	
	public String guardarRegistro() 
	{
		try
		{
			if(this.getEdicionTipo().equals("-1"))
			{
				super.addComponentMessage(null, "Seleccione un Tipo");
				return null;
			}
			else if(this.getEdicionCodigo().trim().length() == 0)
			{
				super.addComponentMessage(null, "Ingrese un código");
				return null;
			}
			else if(this.getEdicionCarterizacion().size() == 0)
			{
				super.addComponentMessage(null, "Seleccione almenos una carterización");
				return null;
			}
			else if(this.getEdicionEstado().equals("-1"))
			{
				super.addComponentMessage(null, "Seleccione un estado");
				return null;
			}
			
			if(edicionId == null || edicionId.length() == 0)
			{
				DescargaLDAP objDescargaLDAP = new DescargaLDAP();
				objDescargaLDAP.setTipo(edicionTipo);
				objDescargaLDAP.setCodigo(edicionCodigo);
				objDescargaLDAP.setEstado(edicionEstado);
				objDescargaLDAP = descargaLDAPBeanLocal.create(objDescargaLDAP);
				
				DescargaLDAPCarteriz objDescargaLDAPCarteriz = null;
				CarterizacionCE objCarterizacionCE = null;
				for(String idCarterizacion : edicionCarterizacion)
				{
					objDescargaLDAPCarteriz = new DescargaLDAPCarteriz();
					objDescargaLDAPCarteriz.setDescargaLDAP(objDescargaLDAP);
					objCarterizacionCE = new CarterizacionCE();
					objCarterizacionCE.setId(Long.parseLong(idCarterizacion));
					objDescargaLDAPCarteriz.setCarterizacion(objCarterizacionCE);
					descargaLDAPCarterizBeanLocal.create(objDescargaLDAPCarteriz);
				}						
								
			}
			else
			{			
				DescargaLDAP objDescargaLDAP = descargaLDAPBeanLocal.buscarPorId(Long.valueOf(edicionId));							
				objDescargaLDAP.setTipo(edicionTipo);
				objDescargaLDAP.setCodigo(edicionCodigo);
				objDescargaLDAP.setEstado(edicionEstado);
				descargaLDAPBeanLocal.edit(objDescargaLDAP);
				descargaLDAPCarterizBeanLocal.eliminarPorDescargaLDAP(objDescargaLDAP.getId());
				
				DescargaLDAPCarteriz objDescargaLDAPCarteriz = null;
				CarterizacionCE objCarterizacionCE = null;
				for(String idCarterizacion : edicionCarterizacion)
				{
					objDescargaLDAPCarteriz = new DescargaLDAPCarteriz();
					objDescargaLDAPCarteriz.setDescargaLDAP(objDescargaLDAP);
					objCarterizacionCE = new CarterizacionCE();
					objCarterizacionCE.setId(Long.parseLong(idCarterizacion));
					objDescargaLDAPCarteriz.setCarterizacion(objCarterizacionCE);
					descargaLDAPCarterizBeanLocal.create(objDescargaLDAPCarteriz);
				}
				
			}
			
			this.buscarDescargaLDAP();
			return "/descargaLDAP/formManConsultaDescargaLDAP?faces-redirect=true";
		}
		catch(Exception ex)
		{
			super.addComponentMessage(null,
					"Error al registrar en base de datos");
			return null;		
		}		
							
	}
	
	public String editar() 
	{
		try
		{					
			if(this.obtenerElementoSeleccionado() != null)
			{
				DescargaLDAP objDescargaLDAP = descargaLDAPBeanLocal.buscarPorId(Long.valueOf(this.obtenerElementoSeleccionado().getId()));
				this.setEdicionId(String.valueOf(objDescargaLDAP.getId()));
				this.setEdicionTipo(objDescargaLDAP.getTipo());
				this.setEdicionCodigo(objDescargaLDAP.getCodigo());
				this.setEdicionEstado(objDescargaLDAP.getEstado());
				
				this.edicionCarterizacion = new ArrayList<String>();
				for(DescargaLDAPCarteriz objDescargaLDAPCarteriz : objDescargaLDAP.getDescargaLDAPCarterizaciones())
				{
					this.edicionCarterizacion.add(String.valueOf(objDescargaLDAPCarteriz.getCarterizacion().getId()));					
				}	
										
				return "/descargaLDAP/formManActualizaDescargaLDAP?faces-redirect=true";
			}
			else
			{
				return null;
			}
		}
		catch(Exception ex)
		{
			super.addComponentMessage(null,
					"Error al consultar en base de datos");
			return null;		
		}
							
	}
	
	public String buscar() 
	{
		try
		{
			this.buscarDescargaLDAP();
		}
		catch(Exception ex)
		{
			super.addComponentMessage(null,
					"Error al realizar unas búsqueda");
		}
			
		return "/descargaLDAP/formManConsultaDescargaLDAP?faces-redirect=true";
							
	}
	
	public String cancelar() 
	{
		return "/descargaLDAP/formManConsultaDescargaLDAP?faces-redirect=true";
	}
		
	public String limpiar()
	{
		this.setListaDescargaLDAP(null);
		this.setFiltroSeleccionadoCarterizacion(null);
		this.setFiltroSeleccionadoCodigo(null);
		this.setFiltroSeleccionadoEstado(null);
		this.setFiltroSeleccionadoTipo(null);
		return null;
	}
	
	public void buscarDescargaLDAP()
	{
		List<DescargaLDAP> listaDescargaLDAP = this.descargaLDAPBeanLocal.buscar(this.getFiltroSeleccionadoTipo(),
																				 this.getFiltroSeleccionadoCodigo(), 
																				 this.getFiltroSeleccionadoCarterizacion(),
																				 this.getFiltroSeleccionadoEstado());
		this.listaDescargaLDAP = new ArrayList<DescargaLDAPVO>();
		DescargaLDAPVO objDescargaLDAPVO = null;
		StringBuilder sbCarterizacion = null;
		for(DescargaLDAP objDescargaLDAP : listaDescargaLDAP)
		{
			objDescargaLDAPVO = new DescargaLDAPVO();
			objDescargaLDAPVO.setId(String.valueOf(objDescargaLDAP.getId()));			
			for(SelectItem objSelectItem :  listaTipo)
			{
				if(String.valueOf(objSelectItem.getValue()).equals(objDescargaLDAP.getTipo()))
				{
					objDescargaLDAPVO.setTipo(objSelectItem.getLabel());
					break;
				}
			}	
			objDescargaLDAPVO.setCodigo(objDescargaLDAP.getCodigo());
			sbCarterizacion = new StringBuilder();
			for(DescargaLDAPCarteriz objDescargaLDAPCarteriz : objDescargaLDAP.getDescargaLDAPCarterizaciones())
			{
				if(sbCarterizacion.length() == 0)
				{
					sbCarterizacion.append(objDescargaLDAPCarteriz.getCarterizacion().getCodigo());
				}
				else
				{
					sbCarterizacion.append(", " + objDescargaLDAPCarteriz.getCarterizacion().getCodigo());
				}
			}
			objDescargaLDAPVO.setCarterizacion(sbCarterizacion.toString());
			objDescargaLDAPVO.setFlagActivo(objDescargaLDAP.getEstado().equals("A"));
			this.listaDescargaLDAP.add(objDescargaLDAPVO);
		}		
	}
	
	public DescargaLDAPVO obtenerElementoSeleccionado()
	{
		if(this.getListaDescargaLDAP() != null && this.getListaDescargaLDAP().size() > 0)
		{
			for(DescargaLDAPVO objDescargaLDAPVO : this.getListaDescargaLDAP())
			{
				if(objDescargaLDAPVO.isSeleccion())
				{
					return objDescargaLDAPVO;
				}
			}
		}
		
		return null;
	}
	
	public void cargarCarterizacion()
	{
		this.listaCarterizacionFiltro = Util.crearItems(carterizacionCEBean.buscarTodos(), true, "id", "descripcion");
		this.listaCarterizacionEdicion = Util.crearItems(carterizacionCEBean.buscarTodos(), false, "id", "descripcion");
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
	
	public List<SelectItem> getListaCarterizacionFiltro() {
		return listaCarterizacionFiltro;
	}

	public void setListaCarterizacionFiltro(
			List<SelectItem> listaCarterizacionFiltro) {
		this.listaCarterizacionFiltro = listaCarterizacionFiltro;
	}

	public List<SelectItem> getListaCarterizacionEdicion() {
		return listaCarterizacionEdicion;
	}

	public void setListaCarterizacionEdicion(
			List<SelectItem> listaCarterizacionEdicion) {
		this.listaCarterizacionEdicion = listaCarterizacionEdicion;
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

	public List<String> getEdicionCarterizacion() {
		return edicionCarterizacion;
	}

	public void setEdicionCarterizacion(List<String> edicionCarterizacion) {
		this.edicionCarterizacion = edicionCarterizacion;
	}

	public String getEdicionId() {
		return edicionId;
	}

	public void setEdicionId(String edicionId) {
		this.edicionId = edicionId;
	}
	
	public List<DescargaLDAPVO> getListaDescargaLDAP() {
		return listaDescargaLDAP;
	}

	public void setListaDescargaLDAP(List<DescargaLDAPVO> listaDescargaLDAP) {
		this.listaDescargaLDAP = listaDescargaLDAP;
	}

	@Override
	public void ordenar(ActionEvent event) {
		// TODO Apéndice de método generado automáticamente
		
	}

	@Override
	public void actualiarAyudaHorario(ActionEvent event) {
		// TODO Apéndice de método generado automáticamente
		
	}
		
}
