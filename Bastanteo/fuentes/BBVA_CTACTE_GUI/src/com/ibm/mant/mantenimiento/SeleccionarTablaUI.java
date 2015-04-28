package com.ibm.mant.mantenimiento;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.mant.tabla.ejb.impl.TablaFacadeBean;
import com.ibm.mant.tabla.vo.TablaVO;
//import com.bbva.tabla.TablaBDelegate;

@ManagedBean (name="seleccionarTabla")
@RequestScoped
public class SeleccionarTablaUI extends AbstractMBean{

	private static final Logger LOG = LoggerFactory.getLogger(SeleccionarTablaUI.class);
	
	private static final long serialVersionUID = -3113108903719076529L;
	private TablaFacadeBean tablaFacadeBean = null;
	
	private Collection<SelectItem> tablas;
	private Long codigoTablaSeleccionada;
	
	private String mensajeError;

	/**
	 * Costructor Se encarga de cargar la lista de tablas parametricas y de
	 * remover de sesión el bean de consultar tabla, para que al consultar una
	 * nueva se limpien los datos y la estructura de la tabla cargada
	 * previamente.
	 */
	public SeleccionarTablaUI() {		
		super();		
		this.cargarTablas();
		//this.removeSessionBean("consultarTabla");
	}
	
	/**
	 * Regla de navegación. Conduce a la página consultarTablaIndex.jsp
	 * 
	 * @return
	 */
	public String consultar() {	
		LOG.info("consultar()");

		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		LOG.info("Parámetros : " + params.toString());
		
		String codigoTabla = params.get("codigotabla");
		  
		LOG.info("Código de tabla a consultar : " + codigoTabla);
		
		String nombreTabla = (String)getObjectSession(ConstantesAdmin.NOMBRE_TABLA);
		
		LOG.info("NOMBRE TABLA : " + nombreTabla);
		
		String pageReturn="";
		
		// Si la tabla es TBL_CE_IBM_EMPLEADO
		
		if(nombreTabla != null){
			FacesContext ctx = FacesContext.getCurrentInstance();
			
			if (nombreTabla.equals("TBL_CE_IBM_EMPLEADO")){
				LOG.info("Cargando consultaTabla para empleado ...");
				BuscarEmpleadoUI buscarEmpleadoUI = (BuscarEmpleadoUI) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarEmpleado");
				buscarEmpleadoUI.init();
				
				// Retornar vista y terminar método
				return "empleados/consultaTabla?faces-redirect=true";
			} else if (nombreTabla.equals("TBL_CE_IBM_PARTICIPE_CC")) {
				return "participes/obtenerParticipes?faces-redirect=true";
			} else if (nombreTabla.equals("TBL_CE_IBM_CARTERIZACION")){
				LOG.info("Cargando consultaTabla para carterizacion ...");
				ConsultarCarterizacionUI consultarCarterizacionUI = (ConsultarCarterizacionUI) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "consultarCarterizacion");
				consultarCarterizacionUI.init();
				
				// Retornar vista y terminar método
				return "carterizacion/consultaCarterizacion?faces-redirect=true";
			} else if (nombreTabla.equals("TBL_CE_IBM_PERFIL_X_NIVEL_CC")){
				BusquedaAsignacionPerfilesUI busquedaAsignacionPerfilesUI = (BusquedaAsignacionPerfilesUI) ctx.getApplication().getVariableResolver().resolveVariable(ctx, "busquedaAsignacionPerfiles");
				busquedaAsignacionPerfilesUI.init();
				
				// Retornar vista y terminar método
				return "asignacionPerfiles/busqueda?faces-redirect=true";
			}
		}
		
		addObjectSession(ConstantesAdmin.RETORNA_PANTALA_MANTENIMIENTO, "seleccionarMantenimiento");
		
		return "consultaTabla";
	}
	
	/**
	 * Carga la lista de tablas paramétricas.
	 */
	private void cargarTablas() {		
		//String tipoTablaMantenimiento = "0";		
		String tipoTablaMantenimiento = (String) getObjectSession(ConstantesAdmin.TIPO_TABLA_SESION);
		LOG.info("tipoTablaMantenimiento: "+tipoTablaMantenimiento);
		try {			
			this.tablas = new ArrayList<SelectItem>();			
			
			if (this.tablaFacadeBean == null) {				
				this.tablaFacadeBean = new TablaFacadeBean();
			}
			
			Collection<TablaVO> tablasVO = this.tablaFacadeBean
			.getTablasParametrizables();
			
			//LOG.info("6");
			if (tablasVO != null && tablasVO.size() > 0) {
				//LOG.info("7"+tablasVO.size());
				//LOG.info("tipoTablaMantenimiento: "+tipoTablaMantenimiento);
				for (TablaVO vo : tablasVO) {
					//LOG.info("8");
					//LOG.info("vo.getTipo(): "+vo.getTipo());
					if (tipoTablaMantenimiento!=null && vo.getId()>=100  && vo.getTipo().compareTo("1")==0) {
						//LOG.info("9");
						SelectItem si = new SelectItem();
						//LOG.info("10");
						si.setLabel(vo.getNombreMostrar());
						//LOG.info("11");
						si.setValue(vo.getId());
						//LOG.info("12");
						this.tablas.add(si);
						//LOG.info("13");
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
	
	/*  mensaje de error    */
	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
}
