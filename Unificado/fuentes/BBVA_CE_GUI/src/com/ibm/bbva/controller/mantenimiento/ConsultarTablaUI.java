package com.ibm.bbva.controller.mantenimiento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.el.ValueExpression;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import pe.ibm.bpd.RemoteUtils;

import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.exception.GenericException;
import com.ibm.as.core.exception.ObjectNotFoundException;
import com.ibm.as.core.helpers.TipoDato;
import com.ibm.as.core.util.log.Logger;
import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.ConstantesAdmin;
import com.ibm.bbva.entities.CartEmpleadoCE;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.EstadoTareaCE;
import com.ibm.bbva.entities.Mensajes;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.ToePerfilEstado;
import com.ibm.bbva.session.CartEmpleadoCEBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.EstadoTareaCEBeanLocal;
import com.ibm.bbva.session.MensajesBeanLocal;
import com.ibm.bbva.session.NivelBeanLocal;
import com.ibm.bbva.session.OficinaBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.TipoCategoriaBeanLocal;
import com.ibm.bbva.session.ToePerfilEstadoBeanLocal;
import com.ibm.bbva.session.VistaExpedienteCantidadBeanLocal;
import com.ibm.bbva.tabla.ejb.impl.TablaFacadeBean;
import com.ibm.bbva.tabla.helper.TablaException;
import com.ibm.bbva.tabla.service.Constantes;
import com.ibm.bbva.tabla.util.vo.EmpleadoVO;
import com.ibm.bbva.tabla.util.vo.EstadoTarea;
import com.ibm.bbva.tabla.vo.ColumnaDependenciaVO;
import com.ibm.bbva.tabla.vo.ColumnaVO;
import com.ibm.bbva.tabla.vo.PosibleValorVO;
import com.ibm.bbva.tabla.vo.RegistroTablaVO;
import com.ibm.bbva.tabla.vo.TablaParametricaVO;
import com.ibm.bbva.util.Util;
//import com.bbva.tabla.TablaBDelegate;

@ManagedBean(name = "consultarTabla")
@SessionScoped
public class ConsultarTablaUI extends AbstractMBean {

	// private TablaBDelegate tablaBDelegate;
	private TablaParametricaVO tablaParametricaVO;
	private Integer codigoTablaSeleccionada = -1;
	private RegistroTablaVO registroSeleccionado;
	private HtmlDataTable tablaBinding;
	private boolean modoGuardar;
	private boolean modoActualizar;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private boolean botonBuscar;

	private boolean habBotonAgregar;
	private boolean habBotonActualizar;
	private boolean habBotonEliminar;

	private String registro;
	private boolean blnmostrarconsultaldap;
	private boolean blnmodoinactivo;
	private boolean blnmostrarmsgerror;
	private boolean blnmostrarmsgadv;
	private String cantidadExpedientesEmpleado;
	private Map<Integer, Object> mapCantidadExpPorEmpleado;
	private String mensajeOficinaLDAP;
	private boolean blnmodoinactivoCantExp;
	private List<EstadoTarea> listaEsTarea;
	private TablaFacadeBean tablaFacadeBean = null;
	@EJB
	private EmpleadoBeanLocal empleadobean;
	@EJB
	private PerfilBeanLocal perfilbean;
	@EJB
	private OficinaBeanLocal oficinabean;
	@EJB
	private NivelBeanLocal nivelbean;
	@EJB
	private TipoCategoriaBeanLocal tipocategoriabean;
	@EJB
	private VistaExpedienteCantidadBeanLocal vistaexpedientecantidadbean;
	@EJB
	private CartEmpleadoCEBeanLocal cartEmpleadoCEBean;
	@EJB
	private EstadoTareaCEBeanLocal estadoTareaCEBean;
	@EJB
	private ToePerfilEstadoBeanLocal toePerfilEstadoBean;
	@EJB
	private PerfilBeanLocal perfilBean;
	@EJB
	private MensajesBeanLocal mensajeBean;
	private EmpleadoVO empleadoVO;
	private List<SelectItem> listOficina;
	private List<SelectItem> listPerfil;
	private List<SelectItem> listNivel;
	private List<SelectItem> listTipoCategoria;

	private String id;
	private String textoMensajeCartEmp;

	/**
	 * Constructor
	 */
	public ConsultarTablaUI() {
		super();
		if (this.tablaParametricaVO == null) {
			this.tablaParametricaVO = new TablaParametricaVO(null, null, null);
			this.empleadoVO = new EmpleadoVO();
		}
	}

	/**
	 * Al seleccionar una nueva tabla, este método se encarga de cargar los
	 * datos y la estructura de la la nueva tabla seleccionada.
	 * 
	 * @param codigoTablaSeleccionada
	 */
	public void setCodigoTablaSeleccionada(Integer codigoTablaSeleccionada) {
		try {
			if (codigoTablaSeleccionada != -1) {
				if (this.tablaFacadeBean == null) {
					this.tablaFacadeBean = new TablaFacadeBean();
				}
				if (codigoTablaSeleccionada == 56) {
					addObjectSession(ConstantesAdmin.TABLA_ESTADO_TAREA,String.valueOf(codigoTablaSeleccionada));
					mostrarEstadoTarea();
					this.codigoTablaSeleccionada = codigoTablaSeleccionada;
				} else {
					this.tablaParametricaVO = this.tablaFacadeBean
							.getDatosYEstructuraTablaByIdTabla(codigoTablaSeleccionada);
					this.cargarEstructuraTabla();
					String nombreTabla = tablaParametricaVO.getTablaVO().getNombre();
					addObjectSession(ConstantesAdmin.NOMBRE_TABLA, nombreTabla);
					this.codigoTablaSeleccionada = codigoTablaSeleccionada;
					this.botonBuscar = this.habilitarBotonBuscar(tablaParametricaVO);
					this.habBotonAgregar = this.tablaParametricaVO.getTablaVO().getBotonAgregar() == null || this.tablaParametricaVO.getTablaVO().getBotonAgregar().equals("0") ? true: false;
					this.habBotonActualizar = this.tablaParametricaVO.getTablaVO().getBotonActualizar() == null || this.tablaParametricaVO.getTablaVO().getBotonActualizar().equals("0") ? true : false;
					this.habBotonEliminar = this.tablaParametricaVO.getTablaVO().getBotonEliminar() == null || this.tablaParametricaVO.getTablaVO().getBotonEliminar().equals("0") ? true: false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Carga la estructura de columnas de la tabla parametrica seleccioanda para
	 * mostrarla en pantalla.
	 */

	@SuppressWarnings("unchecked")
	private void cargarEstructuraTabla() {
		// if (this.tablaBinding == null) {
		this.tablaBinding = new HtmlDataTable();
		// }
		System.out.println("ENTRO AL METODO cargarEstructuraTabla");
		if (this.tablaParametricaVO != null
				&& this.tablaParametricaVO.getColumnasVO() != null
				&& this.tablaParametricaVO.getColumnasVO().size() > 0) {
			List<UIComponent> hijosTabla = tablaBinding.getChildren();
			System.out.println("tablaParametricaVO: " + tablaParametricaVO.getColumnasVO());

			ValueExpression ve = null;
			HtmlSelectBooleanCheckbox cbSelectColumna1 = new HtmlSelectBooleanCheckbox();
			HtmlOutputText header1 = new HtmlOutputText();
			header1.setValue("Sel.");
			ve = FacesContext
					.getCurrentInstance()
					.getApplication()
					.getExpressionFactory()
					.createValueExpression(
							FacesContext.getCurrentInstance().getELContext(),
							"#{item.seleccionado}", String.class);
			System.out.println("ve: " + ve);

			cbSelectColumna1.setValueExpression("value", ve);
			cbSelectColumna1.setOnclick("cambiarEstado(this)");
			cbSelectColumna1.setDisabled(false);
			cbSelectColumna1.setId("rowSelect1");
			System.out.println("cbSelectColumna1: " + cbSelectColumna1.getOnclick() + " ID_: " + cbSelectColumna1.getId());
			UIColumn uColumn1 = new UIColumn();
			uColumn1.getChildren().add(cbSelectColumna1);
			uColumn1.setHeader(header1);
			hijosTabla.add(uColumn1);

			String nombreTabla = this.tablaParametricaVO.getTablaVO()
					.getNombre();
			System.out.println("nombreTabla: " + nombreTabla);

			// Solo para la tabla TBL_CE_IBM_EMPLEADO_CE
			/*
			 * if (nombreTabla.equals(Constantes.TBL_CE_IBM_EMPLEADO_CE)){
			 * this.mapCantidadExpPorEmpleado =
			 * Util.cantidadExpPorEmpleado(vistaexpedientecantidadbean); }
			 */
			for (ColumnaVO columnaVO : this.tablaParametricaVO.getColumnasVO()) {
				if (columnaVO != null) {
					UIColumn uColumn = new UIColumn();

					HtmlOutputText column1Text = new HtmlOutputText();
					HtmlSelectBooleanCheckbox cbSelect = new HtmlSelectBooleanCheckbox();
					String nombreColumna = null;

					boolean estado = false;

					if (columnaVO.getTipoDato() != null) {
						if (TipoDato.LISTA.toString().equals(columnaVO.getTipoDato().toUpperCase()))
							nombreColumna = columnaVO.getNombre() + "_ETIQUETA";// TODO
						else {
							if (TipoDato.BOOLEAN.toString().equals(columnaVO.getTipoDato().toUpperCase())) {
								nombreColumna = columnaVO.getNombre();
								estado = true;
							} else
								nombreColumna = columnaVO.getNombre();
						}
					}
					if (estado == false) {
						ve = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createValueExpression(
										FacesContext.getCurrentInstance().getELContext(),"#{item.datosRegistro." + nombreColumna+ "}", String.class);
						column1Text.setValueExpression("value", ve);
						column1Text.setStyle("width:400px");
						uColumn.getChildren().add(column1Text);
					} else {
						ve = FacesContext.getCurrentInstance().getApplication().getExpressionFactory()
								.createValueExpression(FacesContext.getCurrentInstance().getELContext(),
										"#{consultarTabla.nameStado(item.datosRegistro."+ nombreColumna + ")}",String.class);
						cbSelect.setValueExpression("value", ve);
						cbSelect.setDisabled(true);
						uColumn.getChildren().add(cbSelect);
					}
					HtmlOutputText header = new HtmlOutputText();
					header.setValue(columnaVO.getNombreMostrar());
					uColumn.setHeader(header);
					hijosTabla.add(uColumn);
					
					if (columnaVO.getPosiblesValores() != null) {
						List<SelectItem> lstPerfil = new ArrayList<SelectItem>();
						if (nombreTabla.equals("TBL_CE_IBM_EMPLEADO_CE") && columnaVO.getNombre().equals("ID_PERFIL_FK")){
							ArrayList<PosibleValorVO> pvs = new ArrayList<PosibleValorVO>();
							lstPerfil=Util.crearItems(perfilBean.buscarTodos(),"id","descripcion");
							PosibleValorVO valor = new PosibleValorVO();
							valor.setEtiqueta("");
							valor.setValor(ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO);
							pvs.add(valor);
							for (int i=0 ; i<lstPerfil.size(); i++){
								Perfil perfil = new Perfil();
								PosibleValorVO valorTmp= new PosibleValorVO();
								perfil.setCodigo((String)lstPerfil.get(i).getValue());
								perfil.setDescripcion((String)lstPerfil.get(i).getLabel());
								valorTmp.setValor(perfil.getCodigo());
								valorTmp.setEtiqueta(perfil.getDescripcion());
								pvs.add(valorTmp);
								columnaVO.setPosiblesValores(pvs);
							}
						}else{
							
							ArrayList<PosibleValorVO> pvs = new ArrayList<PosibleValorVO>();
							PosibleValorVO valor = new PosibleValorVO();
							valor.setEtiqueta("");
							valor.setValor(ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO);
							pvs.add(valor);
							pvs.addAll(columnaVO.getPosiblesValores());
							columnaVO.setPosiblesValores(pvs);
						
						}
					}
				}
			}

			nombreTabla = this.tablaParametricaVO.getTablaVO().getNombre();
			// Solo para la tabla TBL_CE_IBM_EMPLEADO_CE
			/*
			 * if (nombreTabla.equals(Constantes.TBL_CE_IBM_EMPLEADO_CE)){
			 * HtmlOutputText column1Text = new HtmlOutputText(); UIColumn
			 * uColumn = new UIColumn(); HtmlOutputText headerE = new
			 * HtmlOutputText(); headerE.setValue("Cant. Exp."); ve =
			 * FacesContext
			 * .getCurrentInstance().getApplication().getExpressionFactory
			 * ().createValueExpression
			 * (FacesContext.getCurrentInstance().getELContext
			 * (),"#{consultarTabla.cantExpEmpleado(item.datosRegistro.ID)}"
			 * ,String.class); column1Text.setValueExpression("value", ve);
			 * column1Text.setStyle("width:400px");
			 * uColumn.getChildren().add(column1Text);
			 * uColumn.setHeader(headerE); hijosTabla.add(uColumn); } for
			 * (UIComponent ui:hijosTabla){
			 * System.out.println("ID: "+ui.getId()+
			 * " CHILDCOUNT: "+ui.getChildCount()); }
			 */
		}
	}

	public boolean nameStado(String parm) {
		if (parm.trim().equals("0"))
			return false;
		else
			return true;
	}

	/*
	 * public String cantExpEmpleado(String parm){ try{ Integer intId =
	 * Integer.parseInt(parm); return
	 * mapCantidadExpPorEmpleado.get(intId).toString();
	 * 
	 * }catch(Exception e){ System.err.println("error: " + e.getMessage());
	 * return "0"; } }
	 */

	/**
	 * Se encarga de invocar a la pagina de creación de nuevos registros,
	 * creando una nueva instancia para el registro que se va a crear.
	 * 
	 * @return regla de navegación - conduce a la página
	 *         agregarEditarRegistroTablaIndex.jsp
	 */
	public String agregar() {
		addObjectSession(ConstantesAdmin.RETORNA_PANTALA_MANTENIMIENTO,
				"consultarMantenimiento");
		Object valor = "";
		String nombreTabla = this.tablaParametricaVO.getTablaVO().getNombre();

		this.registroSeleccionado = new RegistroTablaVO();
		if (this.tablaParametricaVO != null) {
			if (this.tablaParametricaVO.getRegistrosVO() == null) {
				this.tablaParametricaVO.setRegistrosVO(new ArrayList<RegistroTablaVO>());
			}

			if (this.tablaParametricaVO.getColumnasVO() != null) {
				for (ColumnaVO columnaVO : this.tablaParametricaVO.getColumnasVO()) {
					valor = this.registroSeleccionado.getDatosRegistro().get(columnaVO.getNombre());
					columnaVO.setValorRegistro(valor == null ? "" : valor.toString());

					if (nombreTabla.equals(Constantes.TBL_CE_IBM_CART_EMPLEADO_CE) && columnaVO.getNombre().equals("CODIGO")) {
						columnaVO.setSoloLectura(true);
					} else {
						columnaVO.setSoloLectura(false);
					}
				}
			}
		}

		this.modoGuardar = true;
		this.modoActualizar = false;
		this.textoMensajeCartEmp = null;

		if (nombreTabla.equals(Constantes.TBL_CE_IBM_EMPLEADO_CE)) {
			// System.out.println("Agregar Tabla TBL_CE_IBM_EMPLEADO_CE");
			this.mensajeOficinaLDAP = "";
			this.registro = "";
			this.empleadoVO = new EmpleadoVO();
			this.blnmostrarconsultaldap = true;
			this.blnmodoinactivo = true;
			this.blnmodoinactivoCantExp = false;
			cargarListasEmpleado();
			return "/mantenimiento/formManTablaEmpleado?faces-redirect=true";
		} else {
			// System.out.println("Otra Tabla");
			return "/mantenimiento/formManActualizaTabla?faces-redirect=true";
		}

	}

	/**
	 * Determina el registro que el usuario seleccionó para actualizar, pone la
	 * página en modo de actualización y retorna la regla de navegación
	 * respectiva.
	 * 
	 * @return regla de navegación
	 */
	public String editar() {
		addObjectSession(ConstantesAdmin.RETORNA_PANTALA_MANTENIMIENTO,
				"consultarMantenimiento");
		if (this.validarSeleccionRegistro()) {
			if (this.tablaParametricaVO != null
					&& this.tablaParametricaVO.getColumnasVO() != null) {
				for (ColumnaVO columnaVO : this.tablaParametricaVO.getColumnasVO()) {
					//if(!columnaVO.getFlagNoEdit()){
						columnaVO.setValorRegistro(this.registroSeleccionado.getDatosRegistro().get(columnaVO.getNombre()) == null ? null: this.registroSeleccionado.getDatosRegistro().get(columnaVO.getNombre()).toString());

						if (columnaVO.getFlagDependencia().equals(Constantes.FLAG_DEPENDENCIA)) {
							if (tablaFacadeBean.getDatosColumnaDependenciaPadre(columnaVO.getId()))
								cambiar(columnaVO, columnaVO.getValorRegistro());
						}						
				//	}


				}
			}

			this.modoGuardar = false;
			this.modoActualizar = true;
			this.textoMensajeCartEmp = null;

			String nombreTabla = this.tablaParametricaVO.getTablaVO().getNombre();
			
			if (nombreTabla.equals(Constantes.TBL_CE_IBM_EMPLEADO_CE)) {
				
				// System.out.println("Editar Tabla TBL_CE_IBM_EMPLEADO_CE");
				this.mensajeOficinaLDAP = "";
				this.blnmostrarconsultaldap = false;
				this.blnmodoinactivo = false;
				this.blnmodoinactivoCantExp = false;
				cargarListasEmpleado();
				obtenerDatosEmpleado();
				// String valorRegistro =
				// this.registroSeleccionado.getDatosRegistro().get("ID").toString();
				// long cantexp =
				// Util.cantidadExpedientesEmpleado(Integer.parseInt(valorRegistro),vistaexpedientecantidadbean);
				String codigo = this.registroSeleccionado.getDatosRegistro()
						.get("CODIGO").toString();
				System.out.println("codigo : " + codigo);
				RemoteUtils remoteUtils = new RemoteUtils();
				long cantexp = remoteUtils.countConsultaListaTareasTC(codigo);
				
				if (cantexp > 0) {
					this.blnmostrarmsgadv = true;
					this.blnmodoinactivoCantExp = true;
				}
				this.cantidadExpedientesEmpleado = String.valueOf(cantexp);
				return "/mantenimiento/formManTablaEmpleado?faces-redirect=true";
				
			} else if(nombreTabla.equals(Constantes.TBL_CE_IBM_CART_EMPLEADO_CE)){
				
				for (ColumnaVO columnaVO : this.tablaParametricaVO.getColumnasVO()) {
					if(columnaVO.getNombre().toUpperCase().equals("CODIGO")){
						if(modoActualizar)
							columnaVO.setSoloLectura(true);
					}
				
				}
				
				return "/mantenimiento/formManActualizaTabla?faces-redirect=true";
				
			} else {
				return "/mantenimiento/formManActualizaTabla?faces-redirect=true";
			}
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
		
		String pantallaMantenimiento = (String) getObjectSession(ConstantesAdmin.RETORNA_PANTALA_MANTENIMIENTO);
		if (pantallaMantenimiento.equals("seleccionarMantenimiento")) {
			removeObjectSession(ConstantesAdmin.NOMBRE_TABLA);
			return "/mantenimiento/formManTablasMaestras?faces-redirect=true";
		} else {
			addObjectSession(ConstantesAdmin.RETORNA_PANTALA_MANTENIMIENTO,
					"seleccionarMantenimiento");
			return "/mantenimiento/formManConsultaTabla?faces-redirect=true";
		}
	}

	public String cancelarEstadoTarea() {
		codigoTablaSeleccionada = -1;
		this.limpiarEstadoTarea();
		return "/mantenimiento/formManTablasMaestras?faces-redirect=true";

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
			super.addComponentMessage(null,
					"Debe seleccionar un registro de la tabla.");
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
		String resultado = "";
		this.textoMensajeCartEmp = null;
		
		// System.out.println("this.modoGuardar: " + this.modoGuardar);
		// System.out.println("this.modoActualizar: " + this.modoActualizar);
		String nombreTabla = this.tablaParametricaVO.getTablaVO().getNombre();
		Integer codigoTabla = this.tablaParametricaVO.getTablaVO().getId();
		// Solo para la tabla TBL_CE_IBM_EMPLEADO_CE
		if (nombreTabla.equals(Constantes.TBL_CE_IBM_EMPLEADO_CE)) {
			this.mensajeOficinaLDAP = "";
			if (Util.validarEmpleado(this.empleadoVO)) {
				String nombresCompletos = this.empleadoVO.getApepat() + " "
						+ this.empleadoVO.getApemat() + " "
						+ this.empleadoVO.getNombres();
				this.empleadoVO.setNombresCompletos(nombresCompletos);
				Empleado empleado = Util.mapearEmpleadoVOAEmpleadoBD(
						this.empleadoVO, oficinabean, perfilbean, nivelbean,
						tipocategoriabean);
				if (this.modoGuardar) {
					if (!validacionesAdicionales()) {
						this.blnmostrarmsgerror = true;
						super.addComponentMessage(null, "El código de cliente "
								+ this.empleadoVO.getCodigo() + " ya existe");
						return null;
					} else {
						try {
							empleadobean.create(empleado);
						} catch (Exception e) {
							this.blnmostrarmsgerror = true;
							super.addComponentMessage(null,
									"Error al registrar en base de datos");
							return null;
						}
					}
				} else {
					if (this.modoActualizar) {

						if (Long.parseLong(this.cantidadExpedientesEmpleado) > 0 && !validaFlagActivoEmpleado()) {
							super.addComponentMessage(null, "No puede inactivar el empleado, existen expedientes pendientes");
							return null;
						}

						long count = cantidadEmpleadosCartActivos();
						// if (!validaEmpleadoActivoCart()) {
						if (count == 0) {
							if (!validaIdPerfilEmpleado()) {
								Mensajes mensaje = mensajeBean.buscarPorId(Long.valueOf(com.ibm.bbva.controller.Constantes.ID_MENSAJE_MANT_CART_EMP_PERFIL));
								if (mensaje!=null && mensaje.getContenido()!=null) {
									textoMensajeCartEmp = new String(mensaje.getContenido());
								} else {
									super.addComponentMessage(null, "No puede cambiar el perfil, no existe otro empleado activo con carterizacion activa");
								}
								return null;
							}
							if (!validaFlagActivoEmpleado()) {
								Mensajes mensaje = mensajeBean.buscarPorId(Long.valueOf(com.ibm.bbva.controller.Constantes.ID_MENSAJE_MANT_CART_EMP_ACTIVO));
								if (mensaje!=null && mensaje.getContenido()!=null) {
									textoMensajeCartEmp = new String(mensaje.getContenido());
								} else {
									super.addComponentMessage(null, "No puede inactivar el empleado, no existe otro empleado activo con carterizacion activa");
								}
								return null;
							}

						}

						try {
							empleadobean.edit(empleado);
							
						} catch (Exception e) {
							this.blnmostrarmsgerror = true;
							super.addComponentMessage(null,"Error al actualizar en base de datos");
							return null;
						}
					}
				}
				this.setCodigoTablaSeleccionada(codigoTabla);
				this.limpiar();
				this.limpiarSelecion();
				addObjectSession(ConstantesAdmin.RETORNA_PANTALA_MANTENIMIENTO,	"seleccionarMantenimiento");
				return "/mantenimiento/formManConsultaTabla?faces-redirect=true";
			} else {
				this.blnmostrarmsgerror = true;
				super.addComponentMessage(null,
						"Debe completar los campos obligatorios");
				return null;
			}
		}

		// Validacion solo para la tabla "TBL_CE_IBM_CART_EMPLEADO_CE"
		if (nombreTabla.equals(Constantes.TBL_CE_IBM_CART_EMPLEADO_CE)) {
			if (this.modoActualizar) {
				if (!validaActualizaCartEmpleado()) {
					super.addComponentMessage(null,"No puede inactivar la carterización, solo existe un empleado asociado activo");
					return null;
				}
			}
		}

		// Validacion solo para la tabla "TBL_CE_IBM_TOE_PERFIL_ESTADO"
		if (nombreTabla.equals(Constantes.TBL_CE_IBM_TOE_PERFIL_ESTADO)) {
			if (!validaRegistroUnicoIndTOE()) {
				super.addComponentMessage(null, "Registro ya existe");
				return null;
			}
		}

		// Para las demás tablas utilizará el mantenimiento genérico
		if (this.modoGuardar) {
			resultado = this.guardarNuevoRegistro();
			this.limpiar();
			// return resultado;
			addObjectSession(ConstantesAdmin.RETORNA_PANTALA_MANTENIMIENTO,	"seleccionarMantenimiento");
			return "/mantenimiento/formManConsultaTabla?faces-redirect=true";
		} else {
			if (this.modoActualizar) {
				resultado = this.actualizarRegistro();
				this.limpiar();
				// return resultado;
				addObjectSession(ConstantesAdmin.RETORNA_PANTALA_MANTENIMIENTO,	"seleccionarMantenimiento");
				return "/mantenimiento/formManConsultaTabla?faces-redirect=true";
			}
		}

		this.limpiar();
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
		Logger.log(this.getClass(), "guardarNuevoRegistro()", Logger.Level.DEBUG, "Iniciando el almacenamiento del registro");
		if (this.validarGuardarRegistro()) {
			/*
			 * Se limpia el registro seleccionado de la tabla de registros
			 */
			this.limpiarSelecion();
			if (this.tablaParametricaVO != null && this.tablaParametricaVO.getColumnasVO() != null
					&& this.tablaParametricaVO.getColumnasVO().size() > 0) {
				if (this.registroSeleccionado != null && this.registroSeleccionado.getDatosRegistro() != null) {
					for (ColumnaVO columnaVO : this.tablaParametricaVO.getColumnasVO()) {
						/*
						 * Se actualizan en la tabla los datos ingresados por el
						 * usuario.
						 */
						this.registroSeleccionado.getDatosRegistro().put(columnaVO.getNombre(),columnaVO.getValorRegistro());

						if (columnaVO.getValorRegistro() != null && columnaVO.getTipoDato() != null && TipoDato.LISTA.toString().equals(
										columnaVO.getTipoDato().toUpperCase())) {

							if (ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO.equals(columnaVO.getValorRegistro())) {
								this.registroSeleccionado.getDatosRegistro().put(columnaVO.getNombre(), null);
							}
							this.registroSeleccionado.getDatosRegistro().put(columnaVO.getNombre() + "_ETIQUETA",// TODO
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
				registroSeleccionado.setNombreTabla(this.tablaParametricaVO.getTablaVO().getNombre());
				registroSeleccionado.setColumnas(this.tablaParametricaVO.getColumnasVO());
				Logger.log(this.getClass(), "guardarNuevoRegistro()",
						Logger.Level.DEBUG,
						"Antes de crear el registro en la tabla "
								+ this.tablaParametricaVO);
				this.tablaFacadeBean.crearRegistroTabla(registroSeleccionado);
				Logger.log(this.getClass(), "guardarNuevoRegistro()",Logger.Level.DEBUG,"Despues de crear el registro en la tabla "
								+ this.tablaParametricaVO);

				// Se agrega el registro creado en memoria y se le asigna el id
				// generado
				this.tablaParametricaVO.getRegistrosVO().add(registroSeleccionado);

			} catch (Exception e) {
				e.printStackTrace();
				// super.addErrorMessage(e);
				super.addErrorMessage(genericExceptionFromSpecificException(e,"Crear Nuevo Registro"));
				return null;
			}
			return "/mantenimiento/formManTablasMaestras?faces-redirect=true";
		}
		return null;
	}

	/**
	 * Método para obtener el mensaje de error con base a una excepción
	 * entregada. Autor: Hernán Fúquene Fecha: 2010/04/08 Comentario: Se hace
	 * con el ánimo de centralizar los mensajes enviados al front y facilitar
	 * mensajes amigables a los usuarios. Versión 1: Se hace para el
	 * almacenamiento de un nuevo registro.
	 */
	private GenericException genericExceptionFromSpecificException(Exception e,
			String operation) {

		if (e instanceof TablaException) {
			return new GenericException("Se ha presentado un error al ejecutar la operación:" + operation + ". "+ "Se recomienda verificar la integridad de los datos del registro ingresado.");
		}
		return new GenericException("Se ha presentado un error al ejecutar la operación:"+ operation+ ". "+ "Verificar el registro de anotaciones del servidor de aplicaciones.");
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
								this.registroSeleccionado.getDatosRegistro().put(columnaVO.getNombre(), null);
							}
							this.registroSeleccionado.getDatosRegistro().put(columnaVO.getNombre() + "_ETIQUETA",// TODO
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
				e.printStackTrace();
				// super.addErrorMessage(e);
				super.addErrorMessage(genericExceptionFromSpecificException(e,
						"Actualizar Registro"));
				return null;
			}
			return "/mantenimiento/formManTablasMaestras?faces-redirect=true";
		}
		return null;
	}

	/**
	 * Se encarga de ejecutar la lógica asociada a la eliminación de un
	 * registro.
	 * 
	 * @return Regla de navegación.
	 */
	public String eliminar() {
		if (this.validarSeleccionRegistro()) {
			if (this.tablaParametricaVO != null
					&& this.tablaParametricaVO.getRegistrosVO() != null) {
				// this.tablaParametricaVO.getRegistrosVO().remove(this.registroSeleccionado);

				// se invoca al servicio que elimina el registro
				if (this.tablaFacadeBean == null) {
					this.tablaFacadeBean = new TablaFacadeBean();
				}
				try {
					registroSeleccionado.setNombreTabla(this.tablaParametricaVO
							.getTablaVO().getNombre());

					// Validacion solo para la tabla
					// "TBL_CE_IBM_CART_EMPLEADO_CE"
					String nombreTabla = this.tablaParametricaVO.getTablaVO()
							.getNombre();
					if (nombreTabla
							.equals(Constantes.TBL_CE_IBM_CART_EMPLEADO_CE)) {
						boolean result = validaEliminaCartEmpleado();
						if (!result) {
							super.addComponentMessage(
									null,
									"No puede Eliminar el registro, Existe un solo empleado asignado a la carterización seleccionada");
							return null;
						}
					}

					// Validacion solo para la tabla "TBL_CE_IBM_EMPLEADO_CE"
					if (nombreTabla.equals(Constantes.TBL_CE_IBM_EMPLEADO_CE)) {
						boolean result = validaEliminaEmpleado();
						if (!result) {
							super.addComponentMessage(
									null,
									"No puede Eliminar el registro, Existe una carterización asociada al empleado seleccionado");
							return null;
						}
					}

					this.tablaFacadeBean
							.eliminarRegistroTabla(registroSeleccionado);
					this.tablaParametricaVO.getRegistrosVO().remove(
							this.registroSeleccionado);

				} catch (Exception e) {
					e.printStackTrace();
					// super.addErrorMessage(e);
					super.addErrorMessage(genericExceptionFromSpecificException(
							e, "Eliminar Registro"));
				}
			}
			this.resetPaginador();
			return "/mantenimiento/formManConsultaTabla?faces-redirect=true";
		}
		return null;
	}

	private boolean validaEliminaEmpleado() {
		boolean result = false;
		Map<String, Object> registro = registroSeleccionado.getDatosRegistro();
		String value = registro.get(Constantes.CAMPO_ID_TBL_CE_IBM_EMPLEADO_CE)
				.toString();
		List<CartEmpleadoCE> cartEmpleadoCE = cartEmpleadoCEBean
				.buscarPorIdEmpleado(Long.valueOf(value));

		result = (cartEmpleadoCE == null || cartEmpleadoCE.size() == 0) ? true
				: false;
		return result;
	}

	private boolean validaEmpleadoActivoCart() {
		boolean result = false;
		Map<String, Object> registro = registroSeleccionado.getDatosRegistro();
		String value = registro.get(Constantes.CAMPO_ID_TBL_CE_IBM_EMPLEADO_CE)
				.toString();
		List<CartEmpleadoCE> cartEmpleadoCE = cartEmpleadoCEBean
				.buscarPorIdEmpleadoActivo(Long.valueOf(value));

		result = (cartEmpleadoCE.isEmpty() || cartEmpleadoCE.size() == 0) ? true
				: false;
		return result;
	}

	public long cantidadEmpleadosCartActivos() {
		long contador = 0;
		Map<String, Object> registro = registroSeleccionado.getDatosRegistro();
		String value = registro.get(Constantes.CAMPO_ID_TBL_CE_IBM_EMPLEADO_CE)
				.toString();
		contador = empleadobean.countEmpleadoCartActiv(Long.valueOf(value));
		return contador;
	}

	private boolean validaEliminaCartEmpleado() {
		boolean result = false;

		Map<String, Object> registro = registroSeleccionado.getDatosRegistro();

		String value1 = registro
				.get(Constantes.CAMPO_ID_CARTERIZACION_FK_TBL_CE_IBM_CART_EMPLEADO_CE)
				.toString();
		String value2 = registro.get(
				Constantes.CAMPO_ID_EMPLEADO_FK_TBL_CE_IBM_CART_EMPLEADO_CE)
				.toString();

		result = cartEmpleadoCEBean.contarPorIdCaractIdEmpleado(
				Long.valueOf(value1), Long.valueOf(value2)) > 1 ? true : false;
		return result;
	}

	private boolean validaFlagActivoEmpleado() {
		boolean result = false;
		for (ColumnaVO columnaVO : this.tablaParametricaVO.getColumnasVO()) {
			if (columnaVO.getNombre().equals(
					Constantes.CAMPO_FLAG_ACTIVO_TBL_CE_IBM_EMPLEADO_CE)
					&& empleadoVO.getFlagActivo() == true) {
				result = true;
				break;
			}
		}
		return result;
	}

	private boolean validaIdPerfilEmpleado() {
		boolean result = false;

		for (ColumnaVO columnaVO : this.tablaParametricaVO.getColumnasVO()) {
			if (columnaVO.getNombre().equals(
					Constantes.CAMPO_ID_PERFIL_FK_TBL_CE_IBM_EMPLEADO_CE)
					&& columnaVO.getValorRegistro().equals(
							empleadoVO.getIdPerfilFk().toString())) {
				result = true;
				break;
			}
		}

		return result;
	}

	private boolean validaActualizaCartEmpleado() {
		boolean result = false;

		result = validaEliminaCartEmpleado();
		if (!result) {
			for (ColumnaVO columnaVO : this.tablaParametricaVO.getColumnasVO()) {
				if (columnaVO
						.getNombre()
						.equals(Constantes.CAMPO_FLAG_ACTIVO_TBL_CE_IBM_CART_EMPLEADO_CE)
						&& columnaVO.getValorRegistro().equals("1")) {
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * Realiza las validacones adicionales previas a la actualización o
	 * inserción de un registro de una tabla. Esta validaciones se hacen con
	 * respecto a reglas externas independientes de la estructura de la tabla.
	 * 
	 * @return
	 */
	private boolean validacionesAdicionales() {
		boolean correcto = true;
		String valorRegistro = "";
		valorRegistro = this.empleadoVO.getCodigo();
		for (RegistroTablaVO registro : this.tablaParametricaVO
				.getRegistrosVO()) {
			if (registro != null
					&& registro.getDatosRegistro() != null
					&& registro
							.getDatosRegistro()
							.get(Constantes.CAMPO_CODIGO_TBL_CE_IBM_EMPLEADO_CE)
							.toString().toUpperCase().equals(valorRegistro)) {
				correcto = false;
			}
		}

		if (correcto) {
			Empleado empleado = empleadobean.buscarPorCodigo(valorRegistro);
			if (empleado != null) {
				correcto = false;
			}
		}

		System.out.println("correcto:" + correcto);
		return correcto;
	}

	private boolean validaRegistroUnicoIndTOE() {
		boolean correcto = true;

		List<ToePerfilEstado> toePerfilEstado = null;

		String idProducto = "", idTarea = "", idPerfil = "", idEstado = "", idOferta = "", idFlujo = "", nomColumna = "";

		for (ColumnaVO registro : this.tablaParametricaVO.getColumnasVO()) {
			if (registro
					.getNombre()
					.equals(Constantes.CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_PRODUCTO)) {
				idProducto = registro.getValorRegistro();
			}
			if (registro.getNombre().equals(
					Constantes.CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_TAREA)) {
				idTarea = registro.getValorRegistro();
			}
			if (registro.getNombre().equals(
					Constantes.CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_PERFIL)) {
				idPerfil = registro.getValorRegistro();
			}
			if (registro.getNombre().equals(
					Constantes.CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_ESTADO)) {
				idEstado = registro.getValorRegistro();
			}
			if (registro.getNombre().equals(
					Constantes.CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_OFERTA)) {
				idOferta = registro.getValorRegistro();
			}
			if (registro.getNombre().equals(
					Constantes.CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_FLUJO)) {
				idFlujo = registro.getValorRegistro();
			}
			if (registro
					.getNombre()
					.equals(Constantes.CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_COLUMNA)) {
				nomColumna = registro.getValorRegistro();
			}
		}
		toePerfilEstado = toePerfilEstadoBean.validaRegistroUnico(
				Long.parseLong(idProducto), Long.parseLong(idTarea),
				Long.parseLong(idPerfil), Long.parseLong(idEstado),
				Long.parseLong(idOferta), idFlujo, nomColumna);

		if (this.modoGuardar) {
			if (!toePerfilEstado.isEmpty() || toePerfilEstado.size() > 0) {
				correcto = false;
			}
		} else {
			if (this.modoActualizar) {
				Map<String, Object> registro = registroSeleccionado
						.getDatosRegistro();
				String idProductoS = registro
						.get(Constantes.CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_PRODUCTO)
						.toString();
				String idTareaS = registro
						.get(Constantes.CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_TAREA)
						.toString();
				String idPerfilS = registro
						.get(Constantes.CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_PERFIL)
						.toString();
				String idEstadoS = registro
						.get(Constantes.CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_ESTADO)
						.toString();
				String idOfertaS = registro
						.get(Constantes.CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_OFERTA)
						.toString();
				String idFlujoS = registro
						.get(Constantes.CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_FLUJO)
						.toString();
				String nomColumnaS = registro
						.get(Constantes.CAMPO_ID_TBL_CE_IBM_TOE_PERFIL_ESTADO_ID_COLUMNA)
						.toString();

				if (!(idProductoS.equals(idProducto)
						&& idTareaS.equals(idTarea)
						&& idPerfilS.equals(idPerfil)
						&& idEstadoS.equals(idEstado)
						&& idOfertaS.equals(idOferta)
						&& idFlujoS.equals(idFlujo) && nomColumnaS
							.equals(nomColumna))) {

					if (!toePerfilEstado.isEmpty()
							|| toePerfilEstado.size() > 0) {
						correcto = false;
					}
				}
			}
		}
		return correcto;
	}

	/**
	 * Obtiene de BD los datos del Empleado
	 * 
	 * @return
	 */
	private void obtenerDatosEmpleado() {
		String valorRegistro = "";
		for (ColumnaVO columnaVO : this.tablaParametricaVO.getColumnasVO()) {
			if (columnaVO.getNombre().equals("CODIGO")) {
				valorRegistro = columnaVO.getValorRegistro();
			}
		}
		if (valorRegistro != null && !valorRegistro.isEmpty()) {
			Empleado objEmpleado = empleadobean.buscarPorCodigo(valorRegistro);
			this.empleadoVO = Util.mapearEmpleadoBDAEmpleadoVO(objEmpleado);
			// System.out.println("getApemat:" + this.empleadoVO.getApemat());
			// System.out.println("getApepat:" + this.empleadoVO.getApepat());
			// System.out.println("getNombres:" + this.empleadoVO.getNombres());
			// System.out.println("getNombresCompletos:" +
			// this.empleadoVO.getNombresCompletos());
			// System.out.println("getId:" + this.empleadoVO.getId());
			// System.out.println("getIdOficinaFk:" +
			// this.empleadoVO.getIdOficinaFk());
			// System.out.println("getIdPerfilFk:" +
			// this.empleadoVO.getIdPerfilFk());
			// System.out.println("getIdTipoCategoriaFk:" +
			// this.empleadoVO.getIdTipoCategoriaFk());
			// System.out.println("getFecegr:" + this.empleadoVO.getFecegr());
			// System.out.println("getFecing:" + this.empleadoVO.getFecing());

		}

	}

	/**
	 * Obtiene de BD los datos del Empleado
	 * 
	 * @return
	 */
	private void cargarListasEmpleado() {
		this.listOficina = Util.crearItemsConcat(oficinabean.buscarTodos(),	true, "id", "codigo", "descripcion");
		/***Inicio Req-281***/
		this.listPerfil = Util.crearItems(perfilbean.buscarTodos(), true, "id",	"descripcion");
		//this.listPerfil = Util.crearItems(perfilbean.buscarPorProceso(), true, "id",	"descripcion");
		/***Inicio Req-281***/
		this.listNivel = Util.crearItems(nivelbean.buscarTodos(), true, "id", "descripcion");
		this.listTipoCategoria = Util.crearItems(
				tipocategoriabean.buscarTodos(), true, "id", "descripcion");
	}

	/**
	 * Invoca el método respectivo para consultar Empleado LDAP
	 * 
	 * @return Regla de navegación.
	 */
	public String consultarEmpleadoLDAP() {
		this.registro = this.registro.toUpperCase();
		this.empleadoVO = Util
				.consultarEmpleadoLDAP(this.registro, oficinabean);
		this.mensajeOficinaLDAP = "";
		String msjConsultarEmpleadoLDAP = "";
		this.blnmostrarmsgerror = false;
		if (this.empleadoVO.getCodigo() == null
				|| this.empleadoVO.getCodigo().isEmpty()) {
			msjConsultarEmpleadoLDAP = "Ocurrió un error al consultar LDAP";
			this.blnmostrarmsgerror = true;
			super.addComponentMessage(null, msjConsultarEmpleadoLDAP);
			return null;
		}
		if (this.empleadoVO.getCodigo() != null
				&& this.empleadoVO.getApepat() == null) {
			msjConsultarEmpleadoLDAP = "No se encontró el registro inscrito en el banco";
			this.blnmostrarmsgerror = true;
			super.addComponentMessage(null, msjConsultarEmpleadoLDAP);
			return null;
		}
		if (this.blnmostrarmsgerror == false
				&& this.empleadoVO.getIdOficinaFk() == null) {
			this.mensajeOficinaLDAP = "No se encontró el código de Oficina "
					+ this.empleadoVO.getCodOficina()
					+ " enviado por LDAP en la lista de oficinas configuradas activas";
		}
		return "/mantenimiento/formManTablaEmpleado?faces-redirect=true";
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
						// No se realizan validaciones
					} else {

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
									"registroTabla:columnasTablaParametricaDTB:"
											+ numeroFila + ":valorRegistro1",
									mensaje);
							mensajes.add(mensaje);
							correcto = false;
						}

						/*
						 * Se valida la longitud del campo
						 */
						if (columnaVO.getValorRegistro() != null
								&& columnaVO.getValorRegistro().toString()
										.length() > columnaVO
										.getLongitudMaxima().intValue()) {
							String mensaje = "El campo "
									+ columnaVO.getNombreMostrar()
									+ " excede su longitud máxima de "
									+ columnaVO.getLongitudMaxima()
									+ " caracteres.";
							super.addComponentMessage(
									"registroTabla:columnasTablaParametricaDTB:"
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
								if (TipoDato.STRING.toString().equals(
										columnaVO.getTipoDato().toUpperCase())) {
									tipoCorrecto = TipoDato.STRING
											.getNombreMostrar();
									String dato = (String) columnaVO
											.getValorRegistro();
									if (dato == null || dato.length() == 0) {
										columnaVO.setValorRegistro("");
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
								if (TipoDato.INTEGER.toString().equals(
										columnaVO.getTipoDato().toUpperCase())) {
									tipoCorrecto = TipoDato.INTEGER
											.getNombreMostrar();
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
										Date fecha = sdf
												.parse((String) columnaVO
														.getValorRegistro());
									}
								}
							}
						} catch (Exception e) {
							String mensaje = "El campo "
									+ columnaVO.getNombreMostrar()
									+ " debe ser de tipo " + tipoCorrecto;
							super.addComponentMessage(
									"registroTabla:columnasTablaParametricaDTB:"
											+ numeroFila + ":valorRegistro1",
									mensaje);
							mensajes.add(mensaje);
							correcto = false;
						}
					}
				}
				numeroFila++;
			}
		}
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
				/*int i=0;
				for(int i=0; i<RegistroTablaVO tmpVo : this.tablaParametricaVO.getRegistrosVO()){
					if(i<10){
						registrosVOtmp.add(tmpVo);
					}
				}	*/			
				this.tablaBinding.setFirst(tablaBinding.getFirst()
						- tablaBinding.getRows());
			}
		}
	}

	/**
	 * Actualiza la paginación de la tabla a la página inmediatamente anterior a
	 * la actual.
	 */
	public String pagePrevious() {
		this.tablaBinding.setFirst(tablaBinding.getFirst()
				- tablaBinding.getRows());
		return "ok";
	}

	/**
	 * Actualiza la paginación de la tabla a la página siguiente a la actual.
	 */
	public String pageNext() {
		int idTabla = this.tablaParametricaVO.getTablaVO().getId().intValue();
		if (idTabla == Constantes.COD_TABLA_GUIA_DOCUMENTARIA
				|| idTabla == Constantes.COD_TABLA_OFICINA
				|| idTabla == Constantes.COD_TABLA_EMPLEADO
				|| idTabla == Constantes.COD_TABLA_CARTERIZACION_TERRITORIO
				|| idTabla == Constantes.COD_TABLA_CARTERIZACION_EMPLEADO) {
			int j=tablaBinding.getFirst()+tablaBinding.getRows();
			int i=0;
			boolean flag=false;
			Collection<RegistroTablaVO> registrosVOtmp = new ArrayList<RegistroTablaVO>();
			for(RegistroTablaVO tmpVo : this.tablaParametricaVO.getRegistrosVO()){
				
				if (i==j || flag) {
					flag=true;
					if(j<(tablaBinding.getFirst()
							+ tablaBinding.getRows()+tablaBinding.getRows())){
						registrosVOtmp.add(tmpVo);
					}else{
						break;
					}
					j++;
				} else {
					i++;
				}
			}
			TablaFacadeBean tablaFacadeBean = new TablaFacadeBean();	
				
			try {
				tablaFacadeBean.llenarValorMostrarRegistros(registrosVOtmp, this.tablaParametricaVO.getColumnasVO());
			} catch (DataAccessException e) {
				e.printStackTrace();
			} catch (ObjectNotFoundException e) {
				e.printStackTrace();
			}
		}
			
		this.tablaBinding.setFirst(tablaBinding.getFirst()
				+ tablaBinding.getRows());
		return "ok";
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

	public boolean habilitarBotonBuscar(TablaParametricaVO tablaParametricaVO) {
		System.out.println("habilitarBotonBuscar: "
				+ tablaParametricaVO.getColumnasVO() + " REGISTRO: "
				+ tablaParametricaVO.getRegistrosVO() + " TABLA: "
				+ tablaParametricaVO.getTablaVO());
		int totFilas = tablaParametricaVO.getColumnasVO() == null ? 0 : tablaParametricaVO.getColumnasVO().size();
		System.out.println("TOTAL FILAS: " + totFilas);
		int cont = 0;
		boolean resultado = true;
		if (tablaParametricaVO.getColumnasVO()!=null){
			for (ColumnaVO col : tablaParametricaVO.getColumnasVO()) {
				System.out.println("getBusqueda: " + col.getBusqueda());
	
				if (col.getBusqueda().equals("0")) {
					cont++;
				}
			}
		}
		System.out.println("cont: " + cont + " totFilas: " + totFilas);
		if (totFilas == cont) {
			resultado = false;
		}
		System.out.println("RESULTADO::: " + resultado);
		return resultado;
	}

	public String buscar() {
		try {
			this.registroSeleccionado = new RegistroTablaVO();
			registroSeleccionado.setNombreTabla(this.tablaParametricaVO
					.getTablaVO().getNombre());
			registroSeleccionado.setColumnas(this.tablaParametricaVO
					.getColumnasVO());
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
							if (ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO
									.equals(columnaVO.getValorRegistro())) {
								this.registroSeleccionado.getDatosRegistro()
										.put(columnaVO.getNombre(), null);
							}
							this.registroSeleccionado.getDatosRegistro().put(
									columnaVO.getNombre() + "_ETIQUETA",// TODO
									columnaVO.getValorMostrarValorLista());
						}
						if (columnaVO.getValorRegistro() != null
								&& columnaVO.getValorRegistro().equals("")
								&& columnaVO.getTipoDato() != null
								&& TipoDato.STRING.toString().equals(
										columnaVO.getTipoDato().toUpperCase())) {
							this.registroSeleccionado.getDatosRegistro().put(
									columnaVO.getNombre(), null);
						}
					}
				}
			}

			Collection<RegistroTablaVO> resultadoBusqueda = this.tablaFacadeBean
					.buscarRegistroTabla(registroSeleccionado);
			cargarEstructuraTabla();
			this.tablaParametricaVO.getRegistrosVO().clear();
			if (!(resultadoBusqueda == null || resultadoBusqueda.isEmpty())) {
				this.tablaParametricaVO.getRegistrosVO().addAll(
						resultadoBusqueda);
			} else {
				this.tablaParametricaVO.getRegistrosVO().addAll(
						new ArrayList<RegistroTablaVO>());
				super.addErrorMessage("No se encontró registros");
			}
		} catch (Exception e) {
			e.printStackTrace();
			super.addErrorMessage(genericExceptionFromSpecificException(e,
					"Buscar Registro"));
			return null;
		}
		return "/mantenimiento/formManConsultaTabla?faces-redirect=true";
	}

	public String limpiarEstadoTarea() {
		EstadoTarea estadoss = new EstadoTarea();
		estadoss.setEstado("");
		estadoss.setTarea("");
		return null;
	}

	public String limpiar() {

		Collection<ColumnaDependenciaVO> columna = null;
		
		for (ColumnaVO col1 : this.tablaParametricaVO.getColumnasVO()) {
			col1.setValorRegistro("");

			columna = this.tablaFacadeBean.getDatosColumnaDependenciaL(
					col1.getId(), "0", null);
			if (columna != null) {
				for (ColumnaDependenciaVO colr : columna) {
					for (ColumnaVO col2 : this.tablaParametricaVO
							.getColumnasVO()) {
						if (colr.getIdColumnaHijo().intValue() == col2.getId()
								.intValue()) {
							col2.setPosiblesValores(null);
							col2.setPosiblesValoresSI(null);
							break;
						}
					}
					// Recursivo
					columna = this.tablaFacadeBean.getDatosColumnaDependenciaL(
							colr.getIdColumnaHijo(), "0", null);
					if (columna != null) {
						for (ColumnaDependenciaVO cold : columna) {
							for (ColumnaVO col3 : this.tablaParametricaVO
									.getColumnasVO()) {
								if (cold.getIdColumnaHijo().intValue() == col3
										.getId().intValue()) {
									col3.setPosiblesValores(null);
									col3.setPosiblesValoresSI(null);
								}
							}
						}
					}
					break;
				}
			}
		}

		limpiarSelecion();

		return null;
	}

	public String limpiarCambiar(ColumnaVO c) {

		Collection<ColumnaDependenciaVO> columna = null;

		columna = this.tablaFacadeBean.getDatosColumnaDependenciaL(c.getId(),
				"0", null);
		if (columna != null) {
			for (ColumnaDependenciaVO colr : columna) {
				for (ColumnaVO col2 : this.tablaParametricaVO.getColumnasVO()) {
					if (colr.getIdColumnaHijo().intValue() == col2.getId()
							.intValue()) {
						col2.setPosiblesValores(null);
						col2.setPosiblesValoresSI(null);
						col2.setValorRegistro("");
						break;
					}
				}
				// Recursivo
				columna = this.tablaFacadeBean.getDatosColumnaDependenciaL(
						colr.getIdColumnaHijo(), "0", null);
				if (columna != null) {
					for (ColumnaDependenciaVO cold : columna) {
						for (ColumnaVO col3 : this.tablaParametricaVO
								.getColumnasVO()) {
							if (cold.getIdColumnaHijo().intValue() == col3
									.getId().intValue()) {
								col3.setPosiblesValores(null);
								col3.setPosiblesValoresSI(null);
								col3.setValorRegistro("");
							}
						}
					}
				}
				break;
			}
		}
		return null;
	}

	public void cambiar(ColumnaVO co1, String valor) {
		System.out.println("co1 : " + co1.getNombre() + "-" + "valor : "
				+ valor);

		limpiarCambiar(co1);

		String nombreTabla = this.tablaParametricaVO.getTablaVO().getNombre();

		if (nombreTabla.equals(Constantes.TBL_CE_IBM_CART_EMPLEADO_CE)
				&& co1.getNombre().equals("ID_EMPLEADO_FK")) {
			Empleado emp = empleadobean.buscarPorId(Long.parseLong(valor));

			for (ColumnaVO columnaVO : this.tablaParametricaVO.getColumnasVO()) {
				if (columnaVO.getNombre().equals("CODIGO")) {
					columnaVO.setValorRegistro(emp.getCodigo());
					break;
				}
			}
		}

		/* Busca solo si es dependencia cruzada */
		if (co1.getFlagDependencia().equals(Constantes.FLAG_DEPENDENCIA)) {
			Collection<ColumnaDependenciaVO> columna1 = null;
			Collection<RegistroTablaVO> registroTablaBsq = null;

			if (!valor.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
				columna1 = this.tablaFacadeBean.getDatosColumnaDependenciaL(
						co1.getId(), "1", 0);
				// System.out.println("columna1 : "+columna1);
				if (columna1 != null) {
					Map<String, Object> registroBsq = new HashMap<String, Object>();
					for (ColumnaDependenciaVO c : columna1) {
						registroBsq.put(c.getNombreColumnaBsq(), valor);

						try {
							registroTablaBsq = this.tablaFacadeBean
									.getDatosByIdTablaRegistro(
											c.getIdTablaHijo(), registroBsq);
							buscarColumnaDependenciaCero(co1, valor,
									registroTablaBsq, c.getId());
							// System.out.println("registroTablaBsq : "+registroTablaBsq);
						} catch (TablaException e1) {
							// TODO Bloque catch generado automáticamente
							e1.printStackTrace();
						}
					}
				} else {
					buscarColumnaDependenciaCero(co1, valor, null, null);
				}
			}
			// buscarColumnaDependenciaCero(co1, valor, null);
		}
	}

	private void buscarColumnaDependenciaCero(ColumnaVO co1, String valor,
			Collection<RegistroTablaVO> registroTablaBsq, Integer idPadre) {
		Collection<ColumnaDependenciaVO> columnaL = null;
		Map<String, ArrayList<Object>> registros = new HashMap<String, ArrayList<Object>>();
		Map<String, Object> registro = new HashMap<String, Object>();

		columnaL = this.tablaFacadeBean.getDatosColumnaDependenciaL(
				co1.getId(), "0", idPadre);
		for (ColumnaDependenciaVO columna : columnaL) {
			if (columna != null) {
				// Carga nuevamente la lista hijo con los nuevos valores

				List<String> listaNombreColumnaBsq = null;
				if (columna.getNombreColumnaBsq().contains(",")) {
					listaNombreColumnaBsq = Arrays.asList(columna
							.getNombreColumnaBsq().split(","));
				}
				if (listaNombreColumnaBsq != null
						&& listaNombreColumnaBsq.size() > 0) {
					for (ColumnaVO columnaVO : this.tablaParametricaVO
							.getColumnasVO()) {
						for (String s : listaNombreColumnaBsq) {
							if (columnaVO.getNombre().equals(
									s.toString().trim())) {
								if (s.toString()
										.trim()
										.equals(Constantes.CAMPO_FLAG_ACTIVO_TBL_CE_IBM_EMPLEADO_CE)) {
									registro.put(s.toString().trim(),
											Constantes.FLAG_ACTIVO_UNO);
								} else {
									registro.put(s.toString().trim(),
											columnaVO.getValorRegistro());
								}
							}
						}
					}
				} else {

					if (registroTablaBsq == null || registroTablaBsq.isEmpty()) {
						registro.put(columna.getNombreColumnaBsq(), valor);
					} else {
						for (RegistroTablaVO r2 : registroTablaBsq) {
							// System.out.println("NombreColumnaBsq 1 : "+columna.getNombreColumnaBsq());
							// System.out.println("NombreColumnaBsq 2 : "+r2.getDatosRegistro().get(columna.getNombreColumnaDependencia()));

							if (registroTablaBsq.size() == 1) {
								registro.put(
										columna.getNombreColumnaBsq(),
										r2.getDatosRegistro()
												.get(columna
														.getNombreColumnaDependencia()));
							} else {
								agregar(registros,
										columna.getNombreColumnaBsq(),
										r2.getDatosRegistro()
												.get(columna
														.getNombreColumnaDependencia()));
							}
						}
					}
				}

				for (ColumnaVO co : tablaParametricaVO.getColumnasVO()) {
					if (columna.getIdColumnaHijo().intValue() == co.getId()
							.intValue()) {
						if (this.tablaFacadeBean == null) {
							this.tablaFacadeBean = new TablaFacadeBean();
						}
						try {
							Collection<RegistroTablaVO> registroTabla = null;

							if (registroTablaBsq == null
									|| registroTablaBsq.size() == 1) {
								registroTabla = this.tablaFacadeBean
										.getDatosByIdTablaRegistro(
												columna.getIdTablaHijo(),
												registro);
							} else {
								registroTabla = this.tablaFacadeBean
										.getDatosByIdTablaRegistros(
												columna.getIdTablaHijo(),
												registros);
							}

							Collection<PosibleValorVO> posiblesValores = new ArrayList<PosibleValorVO>();

							if (registroTabla == null
									|| registroTabla.size() == 0) {
								co.setPosiblesValores(null);
								co.setPosiblesValoresSI(null);

								// Recursivo
								Collection<ColumnaDependenciaVO> columnaR = null;
								columnaR = this.tablaFacadeBean
										.getDatosColumnaDependenciaL(
												columna.getIdColumnaHijo(),
												"0", idPadre);
								if (columnaR != null) {
									for (ColumnaDependenciaVO colR : columnaR) {
										for (ColumnaVO coR : tablaParametricaVO
												.getColumnasVO()) {
											if (colR.getIdColumnaHijo()
													.intValue() == coR.getId()
													.intValue()) {
												coR.setPosiblesValores(null);
												coR.setPosiblesValoresSI(null);
												break;
											}
										}
									}
								}
								break;
							}

							if (columna.getNombreColumna().contains("-")) {
								// posiblesValores.addAll(this.tablaFacadeBean.llenarPosiblesValoresPorColumnaDependencia(columna));

								String campo1 = columna.getNombreColumna()
										.substring(
												0,
												columna.getNombreColumna()
														.indexOf("-"));
								String campo2 = columna.getNombreColumna()
										.substring(
												columna.getNombreColumna()
														.indexOf("-") + 1,
												columna.getNombreColumna()
														.length());

								for (RegistroTablaVO r : registroTabla) {
									PosibleValorVO posibleValorVO = new PosibleValorVO();
									posibleValorVO.setEtiqueta(r
											.getDatosRegistro().get(campo1)
											.toString()
											+ " - "
											+ r.getDatosRegistro().get(campo2)
													.toString());
									posibleValorVO.setValor(r
											.getDatosRegistro()
											.get(columna.getValorColumna())
											.toString());
									posiblesValores.add(posibleValorVO);
								}
								co.setPosiblesValores(posiblesValores);
							} else {
								for (RegistroTablaVO r : registroTabla) {
									PosibleValorVO posibleValorVO = new PosibleValorVO();
									posibleValorVO.setEtiqueta(r
											.getDatosRegistro()
											.get(columna.getNombreColumna())
											.toString());
									posibleValorVO.setValor(r
											.getDatosRegistro()
											.get(columna.getValorColumna())
											.toString());
									posiblesValores.add(posibleValorVO);
								}
								co.setPosiblesValores(posiblesValores);
							}

							Collection<SelectItem> posiblesValoresSI = new ArrayList<SelectItem>();

							SelectItem si = new SelectItem();
							si.setValue(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
							si.setLabel(" ");
							posiblesValoresSI.add(si);

							for (PosibleValorVO vo : posiblesValores) {
								if (co.getPosiblesValoresSI() == null) {
									co.setPosiblesValoresSI(new ArrayList<SelectItem>());
								}
								si = new SelectItem();
								si.setValue(vo.getValor());
								si.setLabel(vo.getEtiqueta());
								posiblesValoresSI.add(si);
							}
							if(posiblesValoresSI!=null)
								System.out.println("posiblesValoresSI tamaño::::"+posiblesValoresSI.size());
							
							co.setPosiblesValoresSI(posiblesValoresSI);

						} catch (TablaException e) {
							// TODO Bloque catch generado automáticamente
							e.printStackTrace();
						}
						break;
					}
				}
			}
		}
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private void agregar(Map<String, ArrayList<Object>> mapa, String id,
			Object valor) {
		ArrayList<Object> lista = mapa.get(id);
		if (lista == null) {
			lista = new ArrayList<Object>();
			mapa.put(id, lista);
		}
		lista.add(valor);
	}

	public EmpleadoVO getEmpleadoVO() {
		return empleadoVO;
	}

	public void setEmpleadoVO(EmpleadoVO empleadoVO) {
		this.empleadoVO = empleadoVO;
	}

	public List<SelectItem> getListOficina() {
		return listOficina;
	}

	public void setListOficina(List<SelectItem> listOficina) {
		this.listOficina = listOficina;
	}

	public List<SelectItem> getListPerfil() {
		return listPerfil;
	}

	public void setListPerfil(List<SelectItem> listPerfil) {
		this.listPerfil = listPerfil;
	}

	public List<SelectItem> getListNivel() {
		return listNivel;
	}

	public void setListNivel(List<SelectItem> listNivel) {
		this.listNivel = listNivel;
	}

	public List<SelectItem> getListTipoCategoria() {
		return listTipoCategoria;
	}

	public void setListTipoCategoria(List<SelectItem> listTipoCategoria) {
		this.listTipoCategoria = listTipoCategoria;
	}

	public String getRegistro() {
		return registro;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

	public boolean isBlnmostrarconsultaldap() {
		return blnmostrarconsultaldap;
	}

	public void setBlnmostrarconsultaldap(boolean blnmostrarconsultaldap) {
		this.blnmostrarconsultaldap = blnmostrarconsultaldap;
	}

	public boolean isBlnmodoinactivo() {
		return blnmodoinactivo;
	}

	public void setBlnmodoinactivo(boolean blnmodoinactivo) {
		this.blnmodoinactivo = blnmodoinactivo;
	}

	public boolean isBlnmostrarmsgerror() {
		return blnmostrarmsgerror;
	}

	public void setBlnmostrarmsgerror(boolean blnmostrarmsgerror) {
		this.blnmostrarmsgerror = blnmostrarmsgerror;
	}

	public String getCantidadExpedientesEmpleado() {
		return cantidadExpedientesEmpleado;
	}

	public void setCantidadExpedientesEmpleado(
			String cantidadExpedientesEmpleado) {
		this.cantidadExpedientesEmpleado = cantidadExpedientesEmpleado;
	}

	public boolean isBlnmostrarmsgadv() {
		return blnmostrarmsgadv;
	}

	public void setBlnmostrarmsgadv(boolean blnmostrarmsgadv) {
		this.blnmostrarmsgadv = blnmostrarmsgadv;
	}

	public Map<Integer, Object> getMapCantidadExpPorEmpleado() {
		return mapCantidadExpPorEmpleado;
	}

	public void setMapCantidadExpPorEmpleado(
			Map<Integer, Object> mapCantidadExpPorEmpleado) {
		this.mapCantidadExpPorEmpleado = mapCantidadExpPorEmpleado;
	}

	public String getMensajeOficinaLDAP() {
		return mensajeOficinaLDAP;
	}

	public void setMensajeOficinaLDAP(String mensajeOficinaLDAP) {
		this.mensajeOficinaLDAP = mensajeOficinaLDAP;
	}

	public boolean isBlnmodoinactivoCantExp() {
		return blnmodoinactivoCantExp;
	}

	public void setBlnmodoinactivoCantExp(boolean blnmodoinactivoCantExp) {
		this.blnmodoinactivoCantExp = blnmodoinactivoCantExp;
	}

	public List<EstadoTarea> getListaEsTarea() {
		return listaEsTarea;
	}

	public void setListaEsTarea(List<EstadoTarea> listaEsTarea) {
		this.listaEsTarea = listaEsTarea;
	}

	public String getTextoMensajeCartEmp() {
		return textoMensajeCartEmp;
	}

	public void setTextoMensajeCartEmp(String textoMensajeCartEmp) {
		this.textoMensajeCartEmp = textoMensajeCartEmp;
	}

	public void mostrarEstadoTarea() {
		this.tablaBinding = new HtmlDataTable();
		List<EstadoTareaCE> listaEstadoTarea = new ArrayList<EstadoTareaCE>();
		listaEsTarea = new ArrayList<EstadoTarea>();
		listaEstadoTarea = estadoTareaCEBean.buscar();
		listaEstadoTarea = listaEstadosUnicos();
		for (EstadoTareaCE est : listaEstadoTarea) {
			EstadoTarea estadoss = new EstadoTarea();
			if (est != null) {
				estadoss.setEstado(est.getEstadoCE().getDescripcion());
				estadoss.setTarea(est.getTarea().getDescripcion());
				System.out.println("NUEVOSSSSESTADO: " + estadoss.getEstado()
						+ " TAREA: " + estadoss.getTarea());
				listaEsTarea.add(estadoss);
			}
		}
		addObjectSession(ConstantesAdmin.LISTA_ESTADO_TAREA, listaEsTarea);
	}

	private List<EstadoTareaCE> listaEstadosUnicos() {
		List<EstadoTareaCE> lista = estadoTareaCEBean.buscar();
		Set<EstadoTareaCE> set = new TreeSet<EstadoTareaCE>(
				new Comparator<EstadoTareaCE>() {
					public int compare(EstadoTareaCE o1, EstadoTareaCE o2) {
						return (o1.getEstadoCE().getDescripcion() + o1
								.getTarea().getDescripcion())
								.compareToIgnoreCase((o2.getEstadoCE()
										.getDescripcion() + o2.getTarea()
										.getDescripcion()));
					}
				});
		for (EstadoTareaCE estado : lista) {
			set.add(estado);
		}
		return new ArrayList<EstadoTareaCE>(set);
	}
}