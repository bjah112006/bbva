package com.ibm.mant.mantenimiento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.exception.GenericException;
import com.ibm.as.core.helpers.TipoDato;
import com.ibm.as.core.util.CommonUtils;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.mant.tabla.ejb.impl.TablaFacadeBean;
import com.ibm.mant.tabla.helper.TablaException;
import com.ibm.mant.tabla.vo.ColumnaVO;
import com.ibm.mant.tabla.vo.PosibleValorVO;
import com.ibm.mant.tabla.vo.RegistroTablaVO;
import com.ibm.mant.tabla.vo.TablaParametricaVO;
//import com.bbva.tabla.TablaBDelegate;
//import com.exadel.htmLib.components.UITable;

@ManagedBean (name="consultarTabla")
@SessionScoped
public class ConsultarTablaUI extends AbstractMBean{
	
	private static final Logger LOG = LoggerFactory.getLogger(ConsultarTablaUI.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private TablaBDelegate tablaBDelegate;
	private TablaParametricaVO tablaParametricaVO;
	private Integer codigoTablaSeleccionada = -1;
	private RegistroTablaVO registroSeleccionado;
	private HtmlDataTable tablaBinding;
	private HtmlDataTable tablaBinding2;
	private boolean modoGuardar;
	private boolean modoActualizar;
	private boolean modoConsultar;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private boolean botonBuscar;
	
	private boolean habBotonAgregar;
	private boolean habBotonActualizar;
	private boolean stadoCheck = false;
	private boolean habBotonEliminar;
	private TablaFacadeBean tablaFacadeBean = null;
	private String msjPaginacion="Página ";
	private String msjPaginacionError="";
	private String txtIrAPag="";
	private String msjError="";

	/**
	 * Constructor 
	 */
	public ConsultarTablaUI(){
		super();
		if (this.tablaParametricaVO == null) {
			
			this.tablaParametricaVO = new TablaParametricaVO(null, null, null);
		}
	//	if (this.tablaParametricaVO != null && this.tablaParametricaVO.getRegistrosVO()!=null && this.tablaParametricaVO.getColumnasVO()!=null){
	//	LOG.info("-------------Cant. de registros = "+this.tablaParametricaVO.getRegistrosVO().size());
	//	LOG.info("-------------Cant. de columnas = "+this.tablaParametricaVO.getColumnasVO().size());
	//	}else
			//LOG.info("-------------es nulo");
	}

	
	
	/**
	 * Al seleccionar una nueva tabla, este método se encarga de cargar los
	 * datos y la estructura de la la nueva tabla seleccionada.
	 * 
	 * @param codigoTablaSeleccionada
	 */
	public void setCodigoTablaSeleccionada(Integer codigoTablaSeleccionada) {
		try {
			
			LOG.info("setCodigoTablaSeleccionada: " + codigoTablaSeleccionada);
			if (codigoTablaSeleccionada != -1) {
				if (this.tablaFacadeBean == null) {
					this.tablaFacadeBean = new TablaFacadeBean();
				}
				this.tablaParametricaVO = this.tablaFacadeBean
						.getDatosYEstructuraTablaByIdTabla(codigoTablaSeleccionada);
				this.cargarEstructuraTabla();
				
				// Agregar a sesión para que luego pueda ser extraido por
				// SeleccionarTablaUI y evitar usar AJAX. 
				
				String nombreTabla = tablaParametricaVO.getTablaVO().getNombre();
				addObjectSession(ConstantesAdmin.NOMBRE_TABLA, nombreTabla);
				
				this.codigoTablaSeleccionada = codigoTablaSeleccionada;
				this.botonBuscar = this.habilitarBotonBuscar(tablaParametricaVO);
				LOG.info("this.tablaParametricaVO.getTablaVO().getBotonAgregar(): " + this.tablaParametricaVO.getTablaVO().getBotonAgregar());
				LOG.info("this.tablaParametricaVO.getTablaVO().getBotonActualizar(): " + this.tablaParametricaVO.getTablaVO().getBotonActualizar());
				LOG.info("this.tablaParametricaVO.getTablaVO().getBotonEliminar(): " + this.tablaParametricaVO.getTablaVO().getBotonEliminar());
				this.habBotonAgregar = this.tablaParametricaVO.getTablaVO().getBotonAgregar()== null || this.tablaParametricaVO.getTablaVO().getBotonAgregar().equalsIgnoreCase("0")?true:false;
				this.habBotonActualizar = this.tablaParametricaVO.getTablaVO().getBotonActualizar()== null || this.tablaParametricaVO.getTablaVO().getBotonActualizar().equalsIgnoreCase("0")?true:false;
				this.habBotonEliminar = this.tablaParametricaVO.getTablaVO().getBotonEliminar()== null || this.tablaParametricaVO.getTablaVO().getBotonEliminar().equalsIgnoreCase("0")?true:false;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * Carga la estructura de columnas de la tabla parametrica seleccioanda para
	 * mostrarla en pantalla.
	 */
	
	@SuppressWarnings("unchecked")
	private void cargarEstructuraTabla() {
		//if (this.tablaBinding == null) {
			this.tablaBinding = new HtmlDataTable();
			this.setMsjPaginacionError("");
		//}

		if (this.tablaParametricaVO != null
				&& this.tablaParametricaVO.getColumnasVO() != null
				&& this.tablaParametricaVO.getColumnasVO().size() > 0) {
			List<UIComponent> hijosTabla = tablaBinding.getChildren();
			
			ValueExpression ve = null;
			HtmlSelectBooleanCheckbox cbSelectColumna1=new HtmlSelectBooleanCheckbox();
			HtmlOutputText header1 = new HtmlOutputText();
			header1.setValue("Seleccionar");
			ve = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createValueExpression(FacesContext.getCurrentInstance().getELContext(),"#{item.seleccionado}",String.class);
			cbSelectColumna1.setValueExpression("value", ve);
			cbSelectColumna1.setOnclick("cambiarEstado(this)");
			cbSelectColumna1.setDisabled(false);
			cbSelectColumna1.setId("rowSelect1");
			
			
			UIColumn uColumn1 = new UIColumn();
			uColumn1.getChildren().add(cbSelectColumna1);
			uColumn1.setHeader(header1);			
			hijosTabla.add(uColumn1);
			
			LOG.info("----------------------CANTIDAD DE ColumnaVO="+ this.tablaParametricaVO.getColumnasVO().size());
			LOG.info("-------------CANTIDAD DE REGISTROS = "+this.tablaParametricaVO.getRegistrosVO().size());
			for (ColumnaVO columnaVO : this.tablaParametricaVO.getColumnasVO()) {
				if (columnaVO != null) {
					UIColumn uColumn = new UIColumn();
									
					HtmlOutputText column1Text = new HtmlOutputText();
					HtmlSelectBooleanCheckbox cbSelect=new HtmlSelectBooleanCheckbox();
					String nombreColumna = null;
					/*
					if (columnaVO.getTipoDato() != null
							&& TipoDato.LISTA.toString().equals(
									columnaVO.getTipoDato().toUpperCase())) {
						nombreColumna = columnaVO.getNombre() + "_ETIQUETA";//TODO
					} else {
						nombreColumna = columnaVO.getNombre();
					}
					
					ValueBinding vb = FacesContext.getCurrentInstance()
							.getApplication().createValueBinding(
									"#{item.datosRegistro." + nombreColumna
											+ "}");
					*/
					boolean estado=false;
					
					if (columnaVO.getTipoDato() != null){
						if(TipoDato.LISTA.toString().equals(
								columnaVO.getTipoDato().toUpperCase()))
							nombreColumna = columnaVO.getNombre() + "_ETIQUETA";//TODO
						else{
							if(TipoDato.BOOLEAN.toString().equals(columnaVO.getTipoDato().toUpperCase())){
								nombreColumna = columnaVO.getNombre();
								estado=true;
							}
							else
								nombreColumna = columnaVO.getNombre();
						} 
							
					}
//					ValueBinding vb=null;
					
					if (estado==false){
//						vb = FacesContext.getCurrentInstance()
//						.getApplication().createValueBinding(
//								"#{item.datosRegistro." + nombreColumna
//										+ "}");	
//						column1Text.setValueBinding("value", vb);
						
						ve = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createValueExpression(FacesContext.getCurrentInstance().getELContext(),"#{item.datosRegistro." + nombreColumna + "}",String.class);
						column1Text.setValueExpression("value", ve);
						column1Text.setStyle("width:400px");
						if (columnaVO.getTipoDato() != null
								&& TipoDato.BLOB.toString().equals(
										columnaVO.getTipoDato().toUpperCase())) {
							column1Text.setEscape(false);
						} else {
							column1Text.setEscape(true);
						}
						uColumn.getChildren().add(column1Text);
						
					}else{
					/*	vb = FacesContext.getCurrentInstance()
						.getApplication().createValueBinding(
								"#{consultarTabla.nameStado(item.datosRegistro." + nombreColumna
										+ ")}");	*/
						
//						vb = FacesContext.getCurrentInstance()
//						.getApplication().createValueBinding(
//								"#{consultarTabla.nameStado(item.datosRegistro." + nombreColumna
//										+ ")}");
//						cbSelect.setValueBinding("value", vb);
						ve = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createValueExpression(FacesContext.getCurrentInstance().getELContext(),"#{consultarTabla.nameStado(item.datosRegistro." + nombreColumna + ")}",String.class);
						cbSelect.setValueExpression("value", ve);
						cbSelect.setDisabled(true);
						uColumn.getChildren().add(cbSelect);
					}		

					//column1Text.setValueBinding("value", vb);
					
					//uColumn.getChildren().add(column1Text);

					HtmlOutputText header = new HtmlOutputText();
					header.setValue(columnaVO.getNombreMostrar());
					
					//LOG.info("Style 1= "+header.getStyle());
					
					
					//LOG.info("Style 2= "+header.getStyle());
					uColumn.setHeader(header);
					
					hijosTabla.add(uColumn);
					
					if (columnaVO.getPosiblesValores()!=null) {
						ArrayList<PosibleValorVO> pvs = new ArrayList<PosibleValorVO>();
						PosibleValorVO valor = new PosibleValorVO ();
						valor.setEtiqueta("");
						valor.setValor(ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO);
						pvs.add(valor);
						pvs.addAll(columnaVO.getPosiblesValores());
						columnaVO.setPosiblesValores(pvs);
					}
				}
			}
		}
		
		msjPaginacion="Pag. "+((this.tablaBinding.getFirst()/10)+1)+" de "+totalPagina();
	}
	
	public boolean nameStado(String parm){
		if(parm.trim().equals("0"))
			return false;
		else
			return true;
	}
	
	/*public String nameStado(String parm){
		if(parm.trim().equals("0"))
			return "No";
		else
			return "Si";
	}*/	

	/**
	 * Se encarga de invocar a la pagina de creación de nuevos registros,
	 * creando una nueva instancia para el registro que se va a crear.
	 * 
	 * @return regla de navegación - conduce a la página
	 *         agregarEditarRegistroTablaIndex.jsp
	 */
	public String agregar() {
		addObjectSession(ConstantesAdmin.RETORNA_PANTALA_MANTENIMIENTO, "consultarMantenimiento");
		Object valor="";
		this.setTxtIrAPag("");
		this.registroSeleccionado = new RegistroTablaVO();
		if (this.tablaParametricaVO != null) {
			if (this.tablaParametricaVO.getRegistrosVO() == null) {
				this.tablaParametricaVO
						.setRegistrosVO(new ArrayList<RegistroTablaVO>());
			}

			if (this.tablaParametricaVO.getColumnasVO() != null) {
				for (ColumnaVO columnaVO : this.tablaParametricaVO
						.getColumnasVO()) {
					valor = this.registroSeleccionado.getDatosRegistro().get(columnaVO.getNombre());
					columnaVO.setValorRegistro(valor == null ? "" : valor.toString());
				}
			}
		}
		
		this.modoGuardar = true;
		this.modoActualizar = false;		
		this.modoConsultar = false;
		return "actualizaTabla";
	}
	
	/**
	 * Determina el registro que el usuario seleccionó para actualizar, pone la
	 * página en modo de actualización y retorna la regla de navegación
	 * respectiva.
	 * 
	 * @return regla de navegación
	 * @throws DataAccessException 
	 */
	public String consultar() throws DataAccessException {
		addObjectSession(ConstantesAdmin.RETORNA_PANTALA_MANTENIMIENTO, "consultarMantenimiento");
		this.setTxtIrAPag("");
		String nombresColumnas = "";
		String nombreColumnaId = "";
		Integer valorColumnaId = 0;
		String nombreTabla = "";
		Boolean flagConsultarBLOB = false;
		String xmlData = "";
		if (this.validarSeleccionRegistro()) {
			if (this.tablaParametricaVO != null
					&& this.tablaParametricaVO.getColumnasVO() != null) {
				nombreTabla = this.tablaParametricaVO.getTablaVO().getNombre();
				for (ColumnaVO columnaVO : this.tablaParametricaVO
						.getColumnasVO()) {
					
					if (columnaVO.getEsLlavePrimaria()){
						nombreColumnaId = columnaVO.getNombre().toString();
						valorColumnaId = Integer.parseInt(this.registroSeleccionado
								.getDatosRegistro().get(columnaVO.getNombre()).toString());
					}
					
					if (columnaVO.getTipoDato().equalsIgnoreCase("BLOB")){
						nombresColumnas = nombresColumnas + "UTL_RAW.CAST_TO_VARCHAR2(" + columnaVO.getNombre().toString() + ") AS " + columnaVO.getNombre().toString() + ",";
						flagConsultarBLOB = true;
					}else{
						nombresColumnas = nombresColumnas + columnaVO.getNombre().toString() + ",";
					}
				}
				
				if (flagConsultarBLOB){
					nombresColumnas = nombresColumnas.substring(0, nombresColumnas.length()-1);
					xmlData = this.tablaFacadeBean.getRegistroTablaById(nombreTabla, nombresColumnas, nombreColumnaId, valorColumnaId);
					LOG.info("xmlData Consultar: " + xmlData);
				}
				for (ColumnaVO columnaVO : this.tablaParametricaVO
						.getColumnasVO()) {
						
					if (columnaVO.getTipoDato().equalsIgnoreCase("BLOB")){
						columnaVO.setValorRegistro(xmlData);
					}else{
						columnaVO.setValorRegistro(this.registroSeleccionado
								.getDatosRegistro().get(columnaVO.getNombre())== null ? null :this.registroSeleccionado
										.getDatosRegistro().get(columnaVO.getNombre()).toString());
					
					}
				}
				
			}
			
			
			
			
			
			this.modoGuardar = false;
			this.modoActualizar = false;			
			this.modoConsultar = true;
			return "actualizaTabla";
		}
		return null;
	}

	/**
	 * Determina el registro que el usuario seleccionó para actualizar, pone la
	 * página en modo de actualización y retorna la regla de navegación
	 * respectiva.
	 * 
	 * @return regla de navegación
	 */
	public String editar() {
		addObjectSession(ConstantesAdmin.RETORNA_PANTALA_MANTENIMIENTO, "consultarMantenimiento");
		this.setTxtIrAPag("");
		if (this.validarSeleccionRegistro()) {
			if (this.tablaParametricaVO != null
					&& this.tablaParametricaVO.getColumnasVO() != null) {
				for (ColumnaVO columnaVO : this.tablaParametricaVO
						.getColumnasVO()) {
					columnaVO.setValorRegistro(this.registroSeleccionado
							.getDatosRegistro().get(columnaVO.getNombre())== null ? null :this.registroSeleccionado
									.getDatosRegistro().get(columnaVO.getNombre()).toString());
				}
			}

			this.modoGuardar = false;
			this.modoActualizar = true;			
			this.modoConsultar = false;
			return "actualizaTabla";
		}
		return null;
	}

	/**
	 * Ejecuta la acción asociada al botón cancelar. Retorna a la pantalla
	 * anterior.
	 * 
	 * @return Regla de navegación
	 */
	public String cancelar() {		
		codigoTablaSeleccionada = -1;
		this.limpiarSelecion();
		this.limpiar();
		this.setTxtIrAPag("");
		
		String pantallaMantenimiento =  (String) getObjectSession(ConstantesAdmin.RETORNA_PANTALA_MANTENIMIENTO);
		LOG.info("pantallaMantenimiento : "+pantallaMantenimiento);
		if (pantallaMantenimiento.equals("seleccionarMantenimiento")) {
			//redirectAction("../mantenimiento/seleccionarTabla");
			return "/mantenimiento/seleccionarTabla?faces-redirect=true";
		}else{
			addObjectSession(ConstantesAdmin.RETORNA_PANTALA_MANTENIMIENTO, "seleccionarMantenimiento");
			//redirectAction("../mantenimiento/consultaTabla");
			return "/mantenimiento/consultaTabla?faces-redirect=true";
		}	
	}

	/**
	 * Valida que el usuario haya seleccionado un registro de la tabla.
	 * 
	 * @return Resultado de la validación: true ó false
	 */
	private boolean validarSeleccionRegistro() {
		boolean correcto = true;
		this.registroSeleccionado = this.obtenerRegistroSeleccionado();
		if (this.registroSeleccionado == null) {
			correcto = false;
			//super.addComponentMessage(null,"Debe seleccionar un registro de la tabla.");
		}
		return correcto;
	}

	/**
	 * Invoca el método respectivo para guardar un registro según la página se
	 * encuentre en modo guardar o actualizar.
	 * 
	 * @return Regla de navegación.
	 */
	public String guardarRegistro() {
		String resultado="";
		LOG.info("eNTRO A METODO guardarRegistro con = "+this.modoGuardar);
		if (this.modoGuardar) {
			LOG.info("modoGuardar");
			resultado = this.guardarNuevoRegistro(); 
			LOG.info("resultado = "+resultado);
			if(resultado!=null){
				this.limpiar();
				//return resultado;
				//redirectAction("../mantenimiento/consultaTabla");
				return "/mantenimiento/consultaTabla?faces-redirect=true";
			}

		} else {
			if (this.modoActualizar) {
				LOG.info("modoActualizar");
				resultado = this.actualizarRegistro();
				if(resultado!=null){
					this.limpiar();
					//return resultado;
					//redirectAction("../mantenimiento/consultaTabla");	
					return "/mantenimiento/consultaTabla?faces-redirect=true";
				}
			}
		}
		//this.limpiar();
		return null;		
	}

	/**
	 * Se encarga de guardar el registro ingresado por el usuario insertándolo
	 * en base de datos y actualizando las variables respectivas en memoria.
	 * 
	 * @return Regla de navegación.
	 */
	private String guardarNuevoRegistro() {
		/*
		 * Se realizan todas la validaciones necesarias para guardar un nuevo
		 * registro.
		 */
		LOG.info("Metodo guardarNuevoRegistro");
		LOG.debug("Iniciando el almacenamiento del registro");
		LOG.info("Antes d validarGuardarRegistro");
		if (this.validarGuardarRegistro()) {
			/*
			 * Se limpia el registro seleccionado de la tabla de registros
			 */
			LOG.info("Despues d validarGuardarRegistro");
			
			this.limpiarSelecion();
			if (this.tablaParametricaVO != null
					&& this.tablaParametricaVO.getColumnasVO() != null
					&& this.tablaParametricaVO.getColumnasVO().size() > 0) {
				if (this.registroSeleccionado != null
						&& this.registroSeleccionado.getDatosRegistro() != null) {
					for (ColumnaVO columnaVO : this.tablaParametricaVO
							.getColumnasVO()) {
						/*
						 * Se actualizan en la tabla los datos ingresados por el
						 * usuario.
						 */
						this.registroSeleccionado.getDatosRegistro().put(
								columnaVO.getNombre(),
								columnaVO.getValorRegistro());
						
						if (columnaVO.getValorRegistro() != null
								&& columnaVO.getTipoDato() != null
								&& TipoDato.LISTA.toString().equals(
										columnaVO.getTipoDato().toUpperCase())) {
							
							if (ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO.equals(columnaVO.getValorRegistro())) {								
								this.registroSeleccionado.getDatosRegistro().put(
										columnaVO.getNombre(),
										null);
							}
							this.registroSeleccionado.getDatosRegistro().put(
									columnaVO.getNombre() + "_ETIQUETA",//TODO
									columnaVO.getValorMostrarValorLista());
						}
					}
				}
			}

			// se invoca al servicio que guarda el registro
			if (this.tablaFacadeBean == null) {
				this.tablaFacadeBean = new TablaFacadeBean();
			}
			try {
				registroSeleccionado.setNombreTabla(this.tablaParametricaVO
						.getTablaVO().getNombre());
				registroSeleccionado.setColumnas(this.tablaParametricaVO
						.getColumnasVO());
				LOG.debug("Antes de crear el registro en la tabla " + this.tablaParametricaVO);
				this.tablaFacadeBean.crearRegistroTabla(registroSeleccionado);
				LOG.debug("Despues de crear el registro en la tabla " + this.tablaParametricaVO);

				// Se agrega el registro creado en memoria y se le asigna el id
				// generado
				this.tablaParametricaVO.getRegistrosVO().add(
						registroSeleccionado);

			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				this.setMsjError("");
				//super.addErrorMessage(e);
				super.addErrorMessage(genericExceptionFromSpecificException(e, "Crear Nuevo Registro"));
				return null;
			}
			return "seleccionarTabla";
		}
		if (this.registroSeleccionado != null
				&& this.registroSeleccionado.getDatosRegistro() != null){
			LOG.info("Si existe registroSeleccionado");
		}else
			LOG.info("No existe registroSeleccionado");
		return null;
	}
	
	/**
	 * Método para obtener el mensaje de error con base a una excepción entregada.
	 * Autor: Hernán Fúquene
	 * Fecha: 2010/04/08
	 * Comentario: Se hace con el ánimo de centralizar los mensajes enviados al front y facilitar mensajes
	 * amigables a los usuarios.
	 * Versión 1: Se hace para el almacenamiento de un nuevo registro.
	 */
	private GenericException genericExceptionFromSpecificException (Exception e, String operation){
		
		if (e instanceof TablaException){
			LOG.info("\n");
			LOG.info("1--- "+e.getLocalizedMessage());
			LOG.info("\n");
			LOG.info("2--- "+e.getStackTrace());
			LOG.info("\n");
			LOG.info("3--- "+e.fillInStackTrace().toString());
			LOG.info("\n");
			LOG.info("4--- "+e.hashCode());	
			LOG.info("\n");
			LOG.info("5--- "+e.getMessage());
			LOG.info("\n");
			//LOG.info("6--- "+e.);			
		//	LOG.info(""+e.);	
			return new GenericException("Error en BBDD al intentar ejecutar la operación.");			
	//	return new GenericException("Se ha presentado un error al ejecutar la operación:" + operation +". " +
	//	"Se recomienda verificar la integridad de los datos del registro ingresado.");
		}
		return new GenericException("Se ha presentado un error al ejecutar la operación:" + operation +". " +
				"Verificar el registro de anotaciones del servidor de aplicaciones.");
		
		
		
	}
	
	/**
	 * Se encarga de ejecutar la lógica asosociada al almacenamiento de un nuevo
	 * registro
	 */
	private String actualizarRegistro() {
		/*
		 * Se hacen las validaciones resectivas previas a la actualización de un
		 * registro
		 */
		LOG.info("entro a actualizarRegistro");
		LOG.info("Antes de validarGuardarRegistro de actualizarRegistro");
		if (this.validarGuardarRegistro()) {
			this.limpiarSelecion();
			if (this.tablaParametricaVO != null
					&& this.tablaParametricaVO.getColumnasVO() != null
					&& this.tablaParametricaVO.getColumnasVO().size() > 0) {
				if (this.registroSeleccionado != null
						&& this.registroSeleccionado.getDatosRegistro() != null) {
					for (ColumnaVO columnaVO : this.tablaParametricaVO
							.getColumnasVO()) {
						this.registroSeleccionado.getDatosRegistro().put(
								columnaVO.getNombre(),
								columnaVO.getValorRegistro());
						if (columnaVO.getValorRegistro() != null
								&& columnaVO.getTipoDato() != null
								&& TipoDato.LISTA.toString().equals(
										columnaVO.getTipoDato().toUpperCase())) {
							
							if (ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO.equals(columnaVO.getValorRegistro())) {								
								this.registroSeleccionado.getDatosRegistro().put(
										columnaVO.getNombre(),
										null);
							}						
							this.registroSeleccionado.getDatosRegistro().put(
									columnaVO.getNombre() + "_ETIQUETA",//TODO
									columnaVO.getValorMostrarValorLista());
						}
					}
				}
			}

			// se invoca al servicio que guarda el registro
			if (this.tablaFacadeBean == null) {
				this.tablaFacadeBean = new TablaFacadeBean();
			}
			try {
				registroSeleccionado.setNombreTabla(this.tablaParametricaVO
						.getTablaVO().getNombre());
				registroSeleccionado.setColumnas(this.tablaParametricaVO
						.getColumnasVO());
				this.tablaFacadeBean
						.actualizarRegistroTabla(registroSeleccionado);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				//super.addErrorMessage(e);
				super.addErrorMessage(genericExceptionFromSpecificException(e, "Actualizar Registro"));
				return null;
			}
			return "seleccionarTabla";
		}
		LOG.info("return ");
		return null;
	}

	/**
	 * Se encarga de ejecutar la lógica asociada a la eliminación de un
	 * registro.
	 * 
	 * @return Regla de navegación.
	 */
	public String eliminar() {
		this.setTxtIrAPag("");
		if (this.validarSeleccionRegistro()) {
			if (this.tablaParametricaVO != null && this.tablaParametricaVO.getRegistrosVO() != null) {
				//this.tablaParametricaVO.getRegistrosVO().remove(this.registroSeleccionado);

				// se invoca al servicio que elimina el registro
				if (this.tablaFacadeBean == null) {
					this.tablaFacadeBean = new TablaFacadeBean();
				}
				try {
					registroSeleccionado.setNombreTabla(this.tablaParametricaVO.getTablaVO().getNombre());
					this.tablaFacadeBean.eliminarRegistroTabla(registroSeleccionado);
					this.tablaParametricaVO.getRegistrosVO().remove(this.registroSeleccionado);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
					//super.addErrorMessage(e);
					super.addErrorMessage(genericExceptionFromSpecificException(e, "Eliminar Registro"));
				}
			}
			this.resetPaginador();
			return "consultaTabla";
		}
		return null;
	}

	/**
	 * Realiza las validacones necesarias previas a la actualización o inserción
	 * de un registro de una tabla. Esta validaciones se hacen con respespecto a
	 * la metadata de la respectiva tabla.
	 * 
	 * @return
	 */
	private boolean validarGuardarRegistro() {
		boolean correcto = true;
		Collection<String> mensajes = new ArrayList<String>();
		
		if(this.getMsjError()==null)
			this.setMsjError("");
		
		if (this.registroSeleccionado != null
				&& this.tablaParametricaVO != null
				&& this.tablaParametricaVO.getColumnasVO() != null) {
			int numeroFila = 0;
			for (ColumnaVO columnaVO : this.tablaParametricaVO.getColumnasVO()) {

				if (columnaVO != null) {

					/*
					 * Si el campo es de salida se omiten las validaciones para
					 * la columna de salida o que sean llaves primarias
					 */
					if (columnaVO.getTipoCampoOT()
							|| columnaVO.isEsLlavePrimaria()) {
							
					} else {
						LOG.info(""+numeroFila+" Entro a si no");
						/*
						 * Se valida si el campo es requerido
						 */
						if (columnaVO.isEsRequerido()
								&& (columnaVO.getValorRegistro() == null || columnaVO
										.getValorRegistro().toString().trim()
										.equals(""))) {
							
							String mensaje = "El campo "
									+ columnaVO.getNombreMostrar()
									+ " es requerido.";
							
							super.addComponentMessage(
									"form:columnasTablaParametricaDTB:"
											+ numeroFila + ":valorRegistro1",
									mensaje);
							
							mensajes.add(mensaje);
							this.setMsjError("\n"+this.getMsjError()+"\n "+mensaje);
							correcto = false;
						}

						/*
						 * Se valida la longitud del campo
						 */
						if (columnaVO.getValorRegistro() != null
								&& columnaVO.getLongitudMaxima() != null
								&& columnaVO.getValorRegistro().toString()
										.length() > columnaVO
										.getLongitudMaxima().intValue()) {
							String mensaje = "El campo "
									+ columnaVO.getNombreMostrar()
									+ " excede su longitud máxima de "
									+ columnaVO.getLongitudMaxima()
									+ " caracteres.";
							
							this.setMsjError("\n"+this.getMsjError()+"\n "+mensaje);
							super.addComponentMessage(
									"form:columnasTablaParametricaDTB:"
											+ numeroFila + ":valorRegistro1",
									mensaje);
							mensajes.add(mensaje);
							correcto = false;
						}

						/*
						 * Se valida el tipo de dato
						 */
						String tipoCorrecto = null;
						try {
							if (columnaVO.getTipoDato() != null) {
								if (TipoDato.ALFANUMERICO.toString().equals(
										columnaVO.getTipoDato().toUpperCase()) || 
									TipoDato.STRING.toString().equals(
											columnaVO.getTipoDato().toUpperCase())) {
									tipoCorrecto = TipoDato.ALFANUMERICO
											.getNombreMostrar();
									String dato = (String) columnaVO
											.getValorRegistro();
									if (dato == null || dato.length() == 0) {
										columnaVO.setValorRegistro("");
										LOG.info("Se borro el campo 1"+columnaVO.getNombre());
									}
								} else {
									/*
									 * Si el String el de longitud cero se asume
									 * que es nulo
									 */
									String dato = (String) columnaVO
											.getValorRegistro();
									if (dato != null && dato.length() == 0) {
										columnaVO.setValorRegistro(null);
										LOG.info("Se borro el campo 2"+columnaVO.getNombre());
									}
								}
								if (TipoDato.LONG.toString().equals(
										columnaVO.getTipoDato().toUpperCase())) {
									tipoCorrecto = TipoDato.LONG
											.getNombreMostrar();
									if (columnaVO.getValorRegistro() != null) {
										@SuppressWarnings("unused")
										Long dato = Long
												.parseLong((String) columnaVO
														.getValorRegistro());
									}
								}
								LOG.info("Valor de integer = "+columnaVO.getTipoDato().toUpperCase());
								if (TipoDato.INTEGER.toString().equals(
										columnaVO.getTipoDato().toUpperCase())) {
									tipoCorrecto = TipoDato.INTEGER
											.getNombreMostrar();
									LOG.info("Valor de getValorRegistro");
									if (columnaVO.getValorRegistro() != null) {
										@SuppressWarnings("unused")
										Integer dato = Integer
												.parseInt((String) columnaVO
														.getValorRegistro());											
									}
								}
								if (TipoDato.DATE.toString().equals(
										columnaVO.getTipoDato().toUpperCase())) {
									tipoCorrecto = TipoDato.DATE
											.getNombreMostrar();
									if (columnaVO.getValorRegistro() != null) {
										sdf.setLenient(false);
										@SuppressWarnings("unused")
										Date fecha = sdf.parse((String)columnaVO.getValorRegistro());
									}
								}
							}
							
						} catch (Exception e) {
							String mensaje = "El campo "
									+ columnaVO.getNombreMostrar()
									+ " debe ser de tipo " + tipoCorrecto;
							super.addComponentMessage(
									"form:columnasTablaParametricaDTB:"
											+ numeroFila + ":valorRegistro1",
									mensaje);
							//this.setMsjError("\n"+this.getMsjError()+"\n "+mensaje);
							mensajes.add(mensaje);
							correcto = false;
						}
					}
				}
				//LOG.info("valor de registro  = "+columnaVO.getValorRegistro()+ " con campo "+columnaVO.getNombre());
				numeroFila++;
			}
		}
		LOG.info("CORRECTO ES = "+correcto);
		return correcto;
	}

	/**
	 * Limpia el campo seleccionado en la tabla.
	 */
	private void limpiarSelecion() {
		if (this.tablaParametricaVO != null
				&& this.tablaParametricaVO.getRegistrosVO() != null
				&& this.tablaParametricaVO.getRegistrosVO().size() > 0) {
			for (RegistroTablaVO registro : this.tablaParametricaVO
					.getRegistrosVO()) {
				if (registro != null) {
					registro.setSeleccionado(Boolean.FALSE);
				}
			}
		}
	}

	/**
	 * Extrae la información del registro selecciona por el usuario.
	 * 
	 * @return RegistroTablaVO Registro seleccionado.
	 */
	private RegistroTablaVO obtenerRegistroSeleccionado() {
		if (this.tablaParametricaVO != null
				&& this.tablaParametricaVO.getRegistrosVO() != null
				&& this.tablaParametricaVO.getRegistrosVO().size() > 0) {
			for (RegistroTablaVO registro : this.tablaParametricaVO
					.getRegistrosVO()) {
				if (registro != null) {
					if (registro.getSeleccionado() != null
							&& registro.getSeleccionado()) {
						return registro;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Actualiza la página actual de la tabla según el número de registros de
	 * esta.
	 */
	private void resetPaginador() {
		if (this.getTablaParametricaVO() != null
				&& this.getTablaParametricaVO().getRegistrosVO() != null) {
			int tamanhoTabla = this.getTablaParametricaVO().getRegistrosVO()
					.size();
			if (tablaBinding.getFirst() > tamanhoTabla) {
				this.tablaBinding.setFirst(tablaBinding.getFirst()
						- tablaBinding.getRows());
				
				this.stadoCheck = false;
			}
		}
	}
	/**
	 * Calculo de total de registro
	 */
	public int totalRegistro(){
		int totalReg=0;
		
		if ((this.tablaParametricaVO.getRegistrosVO().size()%10)==0)
			totalReg=(this.tablaParametricaVO.getRegistrosVO().size()-10);
		else
			totalReg=this.tablaParametricaVO.getRegistrosVO().size()-(this.tablaParametricaVO.getRegistrosVO().size()%10);
		
	return totalReg;
	
	}
	/**
	 * Calculo de total de pagina
	 */	
	public int totalPagina(){
		int totalPag=0;
		
		if (this.tablaParametricaVO.getRegistrosVO() != null) {
			if ((this.tablaParametricaVO.getRegistrosVO().size()%10)==0)
				totalPag=this.tablaParametricaVO.getRegistrosVO().size()/10;
			else
				totalPag=(this.tablaParametricaVO.getRegistrosVO().size()/10)+1;
		}
		
		return totalPag;
	}	
	
	/**
	 * Actualiza la paginación de la tabla a la página inicial
	 */
	public String pageFirst() {
		this.setTxtIrAPag("");
		this.setMsjPaginacionError("");
		msjPaginacion="Pag. ";
	//	LOG.info("::::::::::::::::::::::::::::::::PRIMERO");
	//	LOG.info("::::::::::::::::::::::::::::::::tablaBinding.getFirst() Inicial ="+tablaBinding.getFirst());
	//	LOG.info("::::::::::::::::::::::::::::::::tablaBinding.getRows()="+tablaBinding.getRows());

		this.tablaBinding.setFirst(0);
	//	LOG.info("::::::::::::::::::::::::::::::::tablaBinding.getFirst() Final="+tablaBinding.getFirst());
		
		msjPaginacion=msjPaginacion+" "+((this.tablaBinding.getFirst()/10)+1)+" de "+totalPagina();
		this.stadoCheck = false;
		
		return "ok";
	}
	
	

	/**
	 * Actualiza la paginación de la tabla a la página inmediatamente anterior a
	 * la actual.
	 */
	public String pagePrevious() {
		this.setTxtIrAPag("");
		this.setMsjPaginacionError("");
		msjPaginacion="Pag. ";
	//	LOG.info("::::::::::::::::::::::::::::::::ANTERIOR");
	//	LOG.info("::::::::::::::::::::::::::::::::tablaBinding.getFirst() Inicial ="+tablaBinding.getFirst());
	//	LOG.info("::::::::::::::::::::::::::::::::tablaBinding.getRows()="+tablaBinding.getRows());
		
		this.tablaBinding.setFirst(tablaBinding.getFirst()
				- tablaBinding.getRows());
	//	LOG.info("::::::::::::::::::::::::::::::::tablaBinding.getFirst() Final="+tablaBinding.getFirst());

		msjPaginacion=msjPaginacion+" "+((this.tablaBinding.getFirst()/10)+1)+" de "+totalPagina();
		this.stadoCheck = false;
		
		return "ok";
	}

	/**
	 * Actualiza la paginación de la tabla a la página siguiente a la actual.
	 */
	public String pageNext() {
		this.setTxtIrAPag("");
		this.setMsjPaginacionError("");
		msjPaginacion="Pag. ";
	//	LOG.info("::::::::::::::::::::::::::::::::SIGUIENTE");
	///	LOG.info("::::::::::::::::::::::::::::::::tablaBinding.getFirst() Inicial="+tablaBinding.getFirst());
	//	LOG.info("::::::::::::::::::::::::::::::::tablaBinding.getRows()="+tablaBinding.getRows());
		
		this.tablaBinding.setFirst(tablaBinding.getFirst()
				+ tablaBinding.getRows());
		
	//	LOG.info("::::::::::::::::::::::::::::::::tablaBinding.getFirst() Final="+tablaBinding.getFirst());
		msjPaginacion=msjPaginacion+" "+((this.tablaBinding.getFirst()/10)+1)+" de "+totalPagina();
		this.stadoCheck = false;
		return "ok";
	}
	
	/**
	 * Actualiza la paginación de la tabla a la página final
	 */
	public String pageLast() {
		this.setMsjPaginacionError("");
		this.setTxtIrAPag("");
		msjPaginacion="Pag. ";
		//LOG.info("::::::::::::::::::::::::::::::::ULTIMO");
		//LOG.info("::::::::::::::::::::::::::::::::tablaBinding.getFirst() Inicial ="+tablaBinding.getFirst());
		//LOG.info("::::::::::::::::::::::::::::::::tablaBinding.getRows()="+tablaBinding.getRows());

		/*this.tablaBinding.setFirst(tablaBinding.getFirst()
				+ tablaBinding.getRows());*/

		this.tablaBinding.setFirst(totalRegistro());
		//LOG.info("::::::::::::::::::::::::::::::::tablaBinding.getFirst() Inicial ="+tablaBinding.getFirst());
		msjPaginacion=msjPaginacion+" "+((this.tablaBinding.getFirst()/10)+1)+" de "+totalPagina();
		this.stadoCheck = false;
		return "ok";
	}
	
	public void btnIrPag(){
	//	RequestContext context = RequestContext.getCurrentInstance();

		String tempMsj=this.msjPaginacion;
		this.setMsjPaginacionError("");
		msjPaginacion="Pag. ";
		if(this.getTxtIrAPag().trim().length()>0){
			if(CommonUtils.isNumeric(this.getTxtIrAPag().trim())){
				if(Integer.parseInt(this.getTxtIrAPag().trim())<=totalPagina() && Integer.parseInt(this.getTxtIrAPag().trim())>0){
					this.tablaBinding.setFirst((Integer.parseInt(this.getTxtIrAPag().trim())-1)*10);
					msjPaginacion=msjPaginacion+" "+((this.tablaBinding.getFirst()/10)+1)+" de "+totalPagina();
				}else					
					this.setMsjPaginacionError("Número de Página no existe.");			
			}else
				this.setMsjPaginacionError("Debe ingresar valor númerico para búsqueda de página");
		}else
			this.setMsjPaginacionError("Ingrese número de página");
			
		if(this.getMsjPaginacionError().trim().length()>0)
			this.msjPaginacion=tempMsj;	
		
		this.stadoCheck = false;
	}
	
	/**
	 * @return the codigoTabla
	 */
	public Integer getCodigoTablaSeleccionada() {
		return codigoTablaSeleccionada;
	}

	/**
	 * @return the tablaBinding
	 */
	public HtmlDataTable getTablaBinding() {
		return tablaBinding;
	}

	/**
	 * @param tablaBinding
	 *            the tablaBinding to set
	 */
	public void setTablaBinding(HtmlDataTable tablaBinding) {
		this.tablaBinding = tablaBinding;
	}

	/**
	 * @return el tablaBinding2
	 */
	public HtmlDataTable getTablaBinding2() {
		return tablaBinding2;
	}

	/**
	 * @param tablaBinding2 el tablaBinding2 a establecer
	 */
	public void setTablaBinding2(HtmlDataTable tablaBinding2) {
		this.tablaBinding2 = tablaBinding2;
	}

	/**
	 * @return the tablaParametricaVO
	 */
	public TablaParametricaVO getTablaParametricaVO() {
		return tablaParametricaVO;
	}

	/**
	 * @param tablaParametricaVO
	 *            the tablaParametricaVO to set
	 */
	public void setTablaParametricaVO(TablaParametricaVO tablaParametricaVO) {
		this.tablaParametricaVO = tablaParametricaVO;
	}
		
	public boolean habilitarBotonBuscar(TablaParametricaVO tablaParametricaVO){
		if (tablaParametricaVO.getColumnasVO() != null) {
			int totFilas = tablaParametricaVO.getColumnasVO().size();
			int cont=0;
			boolean resultado=true;
			
			for (ColumnaVO col : tablaParametricaVO.getColumnasVO()){
			    if (col.getBusqueda().equals("0")){
			    	cont++;
			    }
			}
			if (totFilas == cont){
				resultado=false;
			}	
			return resultado;
		} else {
			return false;
		}
	}
	
	public String buscar() {				
		try {
			this.registroSeleccionado = new RegistroTablaVO();
			registroSeleccionado.setNombreTabla(this.tablaParametricaVO.getTablaVO().getNombre());
			registroSeleccionado.setColumnas(this.tablaParametricaVO.getColumnasVO());
			
			if (this.tablaParametricaVO != null
					&& this.tablaParametricaVO.getColumnasVO() != null
					&& this.tablaParametricaVO.getColumnasVO().size() > 0) {
				if (this.registroSeleccionado != null
						&& this.registroSeleccionado.getDatosRegistro() != null) {
					for (ColumnaVO columnaVO : this.tablaParametricaVO
							.getColumnasVO()) {
						/*
						 * Se actualizan en la tabla los datos ingresados por el
						 * usuario.
						 */
						this.registroSeleccionado.getDatosRegistro().put(
								columnaVO.getNombre(),
								columnaVO.getValorRegistro());
						LOG.info("columna: "+columnaVO.getNombre()+" - "+
								columnaVO.getValorRegistro()+" - "+
								columnaVO.getTipoDato());
						if (columnaVO.getValorRegistro() != null
								&& columnaVO.getTipoDato() != null
								&& TipoDato.LISTA.toString().equals(
										columnaVO.getTipoDato().toUpperCase())) {
							LOG.info("   columna in: "+columnaVO.getNombre());
							if (ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO.equals(columnaVO.getValorRegistro())) {								
								this.registroSeleccionado.getDatosRegistro().put(
										columnaVO.getNombre(),
										null);
							}
							this.registroSeleccionado.getDatosRegistro().put(
									columnaVO.getNombre() + "_ETIQUETA",//TODO
									columnaVO.getValorMostrarValorLista());
						}
						if (columnaVO.getValorRegistro() != null
								&& columnaVO.getValorRegistro().trim().equals("")
								&& columnaVO.getTipoDato() != null
								&& (TipoDato.STRING.toString().equals(
										columnaVO.getTipoDato().toUpperCase()) || 
									TipoDato.ALFANUMERICO.toString().equals(
											columnaVO.getTipoDato().toUpperCase()))){
							this.registroSeleccionado.getDatosRegistro().put(
									columnaVO.getNombre(), null);
						}
					}
				}
			}			
			
			Collection<RegistroTablaVO> resultadoBusqueda = this.tablaFacadeBean.buscarRegistroTabla(registroSeleccionado);
			this.tablaParametricaVO.getRegistrosVO().clear();
			if (!(resultadoBusqueda == null || resultadoBusqueda.isEmpty())) {
				this.tablaParametricaVO.getRegistrosVO().addAll(resultadoBusqueda);
			}else{
				this.tablaParametricaVO.getRegistrosVO().addAll(new ArrayList<RegistroTablaVO>());
				//super.addErrorMessage("No se encontró registros");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			//super.addErrorMessage(genericExceptionFromSpecificException(e, "Buscar Registro"));
			return null;			
		}
		return null;
	}

	public String limpiar() {		
		for (ColumnaVO col : this.tablaParametricaVO.getColumnasVO()){
			col.setValorRegistro("");
		}	
		
		limpiarSelecion();
		
		return null;
	}
	public boolean isBotonBuscar() {
		return botonBuscar;
	}

	public void setBotonBuscar(boolean botonBuscar) {
		this.botonBuscar = botonBuscar;
	}

	public boolean isHabBotonAgregar() {
		return habBotonAgregar;
	}

	public void setHabBotonAgregar(boolean habBotonAgregar) {
		this.habBotonAgregar = habBotonAgregar;
	}

	public boolean isHabBotonActualizar() {
		return habBotonActualizar;
	}

	public void setHabBotonActualizar(boolean habBotonActualizar) {
		this.habBotonActualizar = habBotonActualizar;
	}

	public boolean isHabBotonEliminar() {
		return habBotonEliminar;
	}

	public void setHabBotonEliminar(boolean habBotonEliminar) {
		this.habBotonEliminar = habBotonEliminar;
	}
	
	public boolean isModoActualizar() {
		return modoActualizar;
	}

	public void setModoActualizar(boolean modoActualizar) {
		this.modoActualizar = modoActualizar;
	}
	
	public boolean isModoConsultar() {
		return modoConsultar;
	}

	public void setModoConsultar(boolean modoConsultar) {
		this.modoConsultar = modoConsultar;
	}

	public String getMsjPaginacion() {
		return msjPaginacion;
	}

	public void setMsjPaginacion(String msjPaginacion) {
		this.msjPaginacion = msjPaginacion;
	}

	public String getTxtIrAPag() {
		return txtIrAPag;
	}

	public void setTxtIrAPag(String txtIrAPag) {
		this.txtIrAPag = txtIrAPag;
	}

	public String getMsjError() {
		return msjError;
	}

	public void setMsjError(String msjError) {
		this.msjError = msjError;
	}

	public boolean isStadoCheck() {
		return stadoCheck;
	}

	public void setStadoCheck(boolean stadoCheck) {
		this.stadoCheck = stadoCheck;
	}
	
	public void estadoCambiar(){
		LOG.info("stado inicial = "+stadoCheck);
		if(this.stadoCheck==false)
			this.stadoCheck =true;
		else
			this.stadoCheck =false;
		LOG.info("stado final = "+stadoCheck);
		//return null;
	}
	
	public void getEstadoCambiar(){
		LOG.info("stado inicial = "+stadoCheck);
		if(this.stadoCheck==false)
			this.stadoCheck =true;
		else
			this.stadoCheck =false;
		LOG.info("stado final = "+stadoCheck);
		//return null;
	}

	public String getMsjPaginacionError() {
		return msjPaginacionError;
	}

	public void setMsjPaginacionError(String msjPaginacionError) {
		this.msjPaginacionError = msjPaginacionError;
	}	
	
}
