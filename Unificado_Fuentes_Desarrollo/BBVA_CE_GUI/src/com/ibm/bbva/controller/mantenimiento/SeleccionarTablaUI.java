package com.ibm.bbva.controller.mantenimiento;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.ConstantesAdmin;
import com.ibm.bbva.tabla.ejb.impl.TablaFacadeBean;
import com.ibm.bbva.tabla.util.vo.EstadoTarea;
import com.ibm.bbva.tabla.vo.TablaVO;

@ManagedBean (name="seleccionarTabla")
@SessionScoped
public class SeleccionarTablaUI extends AbstractMBean{
	
	private static final Logger LOG = LoggerFactory.getLogger(SeleccionarTablaUI.class);

	private static final long serialVersionUID = -3113108903719076529L;
	private TablaFacadeBean tablaFacadeBean = null;
	private Collection<SelectItem> tablas;
	private Long codigoTablaSeleccionada;
	private List<EstadoTarea> listaEsTarea;
	private String strCodigoSeleccionado;
	/**
	 * Costructor Se encarga de cargar la lista de tablas parametricas y de
	 * remover de sesión el bean de consultar tabla, para que al consultar una
	 * nueva se limpien los datos y la estructura de la tabla cargada
	 * previamente.
	 */
	public SeleccionarTablaUI() {		
		super();		
		this.cargarTablas();
//		this.cargarTablaEstadoTarea();
		//this.removeSessionBean("consultarTabla");
	}
	
	/**
	 * Regla de navegación. Conduce a la página consultarTablaIndex.jsp
	 * 
	 * @return
	 */
	public String consultar() {
		String nombreTabla=(String)getObjectSession(ConstantesAdmin.NOMBRE_TABLA);
		LOG.info("nombreTabla:::::"+nombreTabla);
		String tblEstadoTarea = (String)getObjectSession(ConstantesAdmin.TABLA_ESTADO_TAREA);
		String pageReturn="";
		//if (codigoTablaSeleccionada==65){
		if (nombreTabla!=null){
			FacesContext ctx = FacesContext.getCurrentInstance();
			if (nombreTabla.equals("TBL_CE_IBM_DATOS_CORREO")){
				buscarCorreoUI buscarCorreo = (buscarCorreoUI)  
						ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarCorreo");
				buscarCorreo.limpiar();
				removeObjectSession(ConstantesAdmin.NOMBRE_TABLA);
				return "/mantenimiento/formManConsultaCorreos?faces-redirect=true";
			}else if(nombreTabla.equals("TBL_CE_IBM_GUIA_DOCUMENTARIA")){
				buscarGuiaDocumentaria buscarGuia = (buscarGuiaDocumentaria)  
						ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarGuia");
				buscarGuia.inicio();
				buscarGuia.limpiar();
				removeObjectSession(ConstantesAdmin.NOMBRE_TABLA);
				return "/mantenimiento/formManGuiaDocumentaria?faces-redirect=true";
			}else if (nombreTabla.equals("TBL_CE_IBM_MENSAJE_CE")){
				BuscarMensajesPersonalizadosUI buscarMensajes = (BuscarMensajesPersonalizadosUI)  
						ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarMensajes");
				buscarMensajes.inicio();
				removeObjectSession(ConstantesAdmin.NOMBRE_TABLA);
				return "/mantenimiento/formManMensajesPersonalizados?faces-redirect=true";
			}else if (nombreTabla.equals("TBL_CE_IBM_ANS")){
				removeObjectSession(ConstantesAdmin.NOMBRE_TABLA);
				return "/mantenimiento/formManConsultaAns?faces-redirect=true";
			}else{
				removeObjectSession(ConstantesAdmin.NOMBRE_TABLA);
				pageReturn="/mantenimiento/formManConsultaTabla?faces-redirect=true";
			}
		}else if(tblEstadoTarea!=null){
				listaEsTarea= new ArrayList<EstadoTarea>();
				listaEsTarea = (List<EstadoTarea>) getObjectSession(ConstantesAdmin.LISTA_ESTADO_TAREA);			
				removeObjectSession(ConstantesAdmin.TABLA_ESTADO_TAREA);
				removeObjectSession(ConstantesAdmin.LISTA_ESTADO_TAREA);
				pageReturn="/mantenimiento/formManConsultaEstadoTarea?faces-redirect=true";
			}
		addObjectSession(ConstantesAdmin.RETORNA_PANTALA_MANTENIMIENTO, "seleccionarMantenimiento");
		return pageReturn;
	}

	
	
	/**
	 * Carga la lista de tablas paramétricas.
	 */
	private void cargarTablas() {		
		String tipoTablaMantenimiento =  (String) getObjectSession(ConstantesAdmin.TIPO_TABLA_SESION);		
		try {			
			this.tablas = new ArrayList<SelectItem>();			
			
			if (this.tablaFacadeBean == null) {				
				this.tablaFacadeBean = new TablaFacadeBean();
			}
			
			Collection<TablaVO> tablasVO = this.tablaFacadeBean
			.getTablasParametrizables();

			if (tablasVO != null && tablasVO.size() > 0) {
				for (TablaVO vo : tablasVO) {
					/*if (tipoTablaMantenimiento!=null && (tipoTablaMantenimiento.equals(vo.getTipo())
							|| "0".equals(vo.getTipo()))) {*/
					if (99>=(vo.getId()) && vo.getTipo()!=null) {												
						SelectItem si = new SelectItem();
						si.setLabel(vo.getNombreMostrar());
						si.setValue(vo.getId());
						this.tablas.add(si);
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	/**
	 * @return the tablas
	 */
	public Collection<SelectItem> getTablas() {
		return tablas;
	}

	/**
	 * @param tablas
	 *            the tablas to set
	 */
	public void setTablas(Collection<SelectItem> tablas) {
		this.tablas = tablas;
	}

	public Long getCodigoTablaSeleccionada() {
		return codigoTablaSeleccionada;
	}

	public void setCodigoTablaSeleccionada(Long codigoTablaSeleccionada) {
		this.codigoTablaSeleccionada = codigoTablaSeleccionada;
	}

	public List<EstadoTarea> getListaEsTarea() {
		return listaEsTarea;
	}

	public void setListaEsTarea(List<EstadoTarea> listaEsTarea) {
		this.listaEsTarea = listaEsTarea;
	}

	public String getStrCodigoSeleccionado() {
		return strCodigoSeleccionado;
	}

	public void setStrCodigoSeleccionado(String strCodigoSeleccionado) {
		this.strCodigoSeleccionado = strCodigoSeleccionado;
	}		
	
}