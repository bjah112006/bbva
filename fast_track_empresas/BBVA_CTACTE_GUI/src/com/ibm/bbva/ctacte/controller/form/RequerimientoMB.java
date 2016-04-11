package com.ibm.bbva.ctacte.controller.form;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Attachment;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.MultiTabla;
import com.ibm.bbva.ctacte.bean.ParametrosConf;
import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;
import com.ibm.bbva.ctacte.business.ClienteBusiness;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractTablaMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.ReporteMB;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.MultiTablaDAO;
import com.ibm.bbva.ctacte.dao.ParametrosConfDAO;
import com.ibm.bbva.ctacte.dao.servicio.RequerimientoService;
import com.ibm.bbva.ctacte.enums.EnmTipoArchivo;
import com.ibm.bbva.ctacte.enums.EnumTextAlign;
import com.ibm.bbva.ctacte.enums.EnumTipoDatoJava;
import com.ibm.bbva.ctacte.enums.EnumVerticalAlign;
import com.ibm.bbva.ctacte.exepciones.ConexionServicioException;
import com.ibm.bbva.ctacte.exepciones.ErrorObteniendoDatosException;
import com.ibm.bbva.ctacte.exepciones.ParametroIlegalException;
import com.ibm.bbva.ctacte.model.RequerimientoModel;
import com.ibm.bbva.ctacte.util.ColumnaDinamica;
import com.ibm.bbva.ctacte.util.ParametrosSistema;
import com.ibm.bbva.ctacte.util.ReporteDinamico;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean(name = "requerimientoMB")
@ViewScoped
public class RequerimientoMB extends AbstractTablaMBean{

	private static final long serialVersionUID = -2967268033872326199L;
	private static final Logger LOG = LoggerFactory.getLogger(RequerimientoMB.class);

	private List<RequerimientoModel> listRequerimientoModels;	
	private List<SelectItem> listaCategoriaReclamo;	
	private List<SelectItem> listaTipoOperacion;	
	private List<SelectItem> listaEstados;	
	private List<SelectItem> listaDictamenReclamo;
	private List<SelectItem> listaClasificacion;
	private List<SelectItem> listaAbogados;


	private RequerimientoModel filtro;
	private RequerimientoModel objecto=new RequerimientoModel();;
	private List<ClientePJCore> clientes;
	private ClientePJCore cliente;
	private Empleado empleado;
	private Attachment attachment;
	private List<Empleado> abogados;

	private Integer tamañoMaxArchivo;


	@EJB
	RequerimientoService requerimientoService;

	@EJB
	private MultiTablaDAO multiTablaDAO;

	@EJB
	private ParametrosConfDAO parametrosConfDAO;

	@EJB
	private ClienteBusiness clienteBusiness;

	@EJB
	private EmpleadoDAO empleadoDAO;

	private String linkSFP;
	@PostConstruct
	public void iniciar(){	
		LOG.info("@PostConstruct- Iniciar()");


		limpiarResultados();

		ParametrosConf paramTamañoMax=parametrosConfDAO.obtener(ConstantesParametros.CODIGO_CONF, ConstantesParametros.TAMANO_ARCHIVO_RC);
		String strTamañoMaxArchivoMb=paramTamañoMax.getValorVariable();
		tamañoMaxArchivo=Integer.parseInt(strTamañoMaxArchivoMb);	
		
		linkSFP=parametrosConfDAO.obtener(ConstantesParametros.CODIGO_CONF, ConstantesParametros.LINK_SFP_RC).getValorVariable();
		listaEstados=new ArrayList<SelectItem>();
		listaDictamenReclamo=new ArrayList<SelectItem>();
		listaCategoriaReclamo=new ArrayList<SelectItem>();
		listaTipoOperacion=new ArrayList<SelectItem>();
		listaClasificacion=new ArrayList<SelectItem>();
		listaAbogados=new ArrayList<SelectItem>();
		filtro=new RequerimientoModel();

		abogados=empleadoDAO.getEmpleadosPorPerfil(Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_ABOGADO_REVISION));


		clientes=new ArrayList<ClientePJCore>();
		cliente=new ClientePJCore();
		attachment=new Attachment();
		filtro.setShowCategoriaReclamo(false);
		filtro.setShowDictamenReclamo(false);
		filtro.setDisableBtnRegistrar(false);
		cargarListaTipoOperacion();
		cargarListaCategoriaReclamo();
		cargarListaEstados();
		cargarListaDictamenReclamo();
		cargarListaClasificacion();

		

		empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		LOG.info("@PostConstruct- objecto : "+objecto + " @PostConstruct- filtro : " +filtro);

		//CARGA VALORES PARA LA PANTALLA DE absolverReclamoConsulta.xhtml y verificar////
		filtro.setId(Long.parseLong(getRequestParameter("idReclamoConsulta")==null?"0":getRequestParameter("idReclamoConsulta")));
		objecto.setId(getRequestParameter("idObjecto")==null?null:(Long.parseLong(getRequestParameter("idObjecto"))));
		objecto.setMensajeError(getRequestParameter("mensajeError")==null?null:(getRequestParameter("mensajeError")));
		try {
			if(filtro.getId()!=null && !(filtro.getId().compareTo(0L)==0)){

				if(empleado.getPerfil().getCodigo().equals(ConstantesBusiness.CODIGO_PERFIL_ABOGADO_REVISION)){
					LOG.info("RequerimientoMB - iniciar -empleado.getFlagAbogado() true" + empleado.getFlagAbogado());
					filtro.setEsAbogado(true);
				}else{
					LOG.info("RequerimientoMB - iniciar -empleado.getFlagAbogado() false " + empleado.getFlagAbogado());
					filtro.setEsAbogado(false);
				}

				objecto=requerimientoService.obtener(filtro);

				cliente.setCodigoCentral(objecto.getCod_central());
				cliente.setNumeroDOI(objecto.getNro_doi());
				cliente.setTipoDOI(objecto.getTipoDoi());

				LOG.info("objecto for absolverReclamoConsulta : " + objecto);
			}
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			LOG.error("",e);
		}

		//Valores por defecto del paginador
		setRowsPerPage(new Integer(5));
		List<SelectItem> listPaginas = new ArrayList<SelectItem>();
		listPaginas.add(new SelectItem("5", "5"));
		listPaginas.add(new SelectItem("10", "10"));
		listPaginas.add(new SelectItem("15", "15"));
		setListOfSelectItems(listPaginas);
		
		
		String currentPage=String.valueOf(getCurrentPage());
		LOG.info("currentPage()",currentPage);
		LOG.info("getCurrentPage()",getCurrentPage());
	}

	public void limpiarResultados() {
		listRequerimientoModels=new ArrayList<RequerimientoModel>();
		super.pageFirst(null);
	}

	public void limpiarFiltros(AjaxBehaviorEvent event){
		filtro=new RequerimientoModel();
		clientes=(new ArrayList<ClientePJCore>());
	}

	private void inicializarDatos(){
		filtro=new RequerimientoModel();
		clientes=(new ArrayList<ClientePJCore>());
		objecto=new RequerimientoModel();
	}

	public void cerrarConsultaReclamo(){
		if(validarVersion(objecto)){
			objecto.setEstado(Long.valueOf(ConstantesBusiness.CODIGO_HIJO_ESTADO_CERRADO));
			requerimientoService.Registrar(objecto, attachment);
			irBandeja();
		}else{
			LOG.info("LA VERSION DEL RECLAMO / CONSULTA NO ES LA ULTIMA, VUELVA A CARGAR EL DOCUMENTO.");
			redirectAction("../reclamo/registroError",new String[] {"mensajeError","El reclamo / consulta ya fue modificado por otro usuario."});
		}

	}
	public void solicitarReconsideracion(){
		if(validarVersion(objecto)){	
			objecto.setEstado(ConstantesBusiness.CODIGO_HIJO_ESTADO_RECONSIDERADO);
			objecto.setFecha_reconsidera(new Date());
			objecto.setUsuario_reconsidera(empleado.getCodigo());
			objecto.setReconsideracion(true);
			attachment.setEstado_ref(multiTablaDAO.obtenerxEntero(ConstantesBusiness.CODIGO_HIJO_ESTADO_RECONSIDERADO).getNombre());

			requerimientoService.Registrar(objecto, attachment);
			irBandeja();
		}else{
			LOG.info("LA VERSION DEL RECLAMO / CONSULTA NO ES LA ULTIMA, VUELVA A CARGAR EL DOCUMENTO.");
			redirectAction("../reclamo/registroError",new String[] {"mensajeError","El reclamo / consulta ya fue modificado por otro usuario."});
		}
	}

	public void registrarAbsolucion(){
		LOG.info("registrarAbsolucion objecto: " + objecto);
		if(validarVersion(objecto)){
			if(ConstantesBusiness.CODIGO_HIJO_TIPO_OPERACION_CONSULTA.compareTo(objecto.getTipo_operacion())==0){
				objecto.setFecha_absuelve(new Date());
				objecto.setUsuario_absuelve(empleado.getCodigo());
				objecto.setEstado(ConstantesBusiness.CODIGO_HIJO_ESTADO_CERRADO);
				attachment.setEstado_ref(multiTablaDAO.obtenerxEntero(ConstantesBusiness.CODIGO_HIJO_ESTADO_CERRADO).getNombre());
			}else if(ConstantesBusiness.CODIGO_HIJO_TIPO_OPERACION_RECLAMO.compareTo(objecto.getTipo_operacion())==0){
				LOG.info("objecto.isReconsideracion() :"+objecto.isReconsideracion());
				if(objecto.getIdResultado().compareTo(ConstantesBusiness.CODIGO_HIJO_DICTAMEN_PROCEDENTE)==0){
					if(objecto.isReconsideracion()){
						objecto.setFecha_absuelve_reconsidera(new Date());
						objecto.setUsuario_absuelve_reconsidera(empleado.getCodigo());
						objecto.setEstado(ConstantesBusiness.CODIGO_HIJO_ESTADO_CERRADO);
						attachment.setEstado_ref(multiTablaDAO.obtenerxEntero(ConstantesBusiness.CODIGO_HIJO_ESTADO_CERRADO).getNombre());
					}else{
						objecto.setFecha_absuelve(new Date());
						objecto.setUsuario_absuelve(empleado.getCodigo());
						objecto.setEstado(ConstantesBusiness.CODIGO_HIJO_ESTADO_CERRADO);
						attachment.setEstado_ref(multiTablaDAO.obtenerxEntero(ConstantesBusiness.CODIGO_HIJO_ESTADO_CERRADO).getNombre());
					}				
				}else{
					if(objecto.isReconsideracion()){
						objecto.setFecha_absuelve_reconsidera(new Date());
						objecto.setUsuario_absuelve_reconsidera(empleado.getCodigo());
						objecto.setEstado(ConstantesBusiness.CODIGO_HIJO_ESTADO_CERRADO);
						attachment.setEstado_ref(multiTablaDAO.obtenerxEntero(ConstantesBusiness.CODIGO_HIJO_ESTADO_CERRADO).getNombre());
					}else{
						objecto.setFecha_absuelve(new Date());
						objecto.setUsuario_absuelve(empleado.getCodigo());
						objecto.setEstado(ConstantesBusiness.CODIGO_HIJO_ESTADO_DICTAMINADO);
						attachment.setEstado_ref(multiTablaDAO.obtenerxEntero(ConstantesBusiness.CODIGO_HIJO_ESTADO_DICTAMINADO).getNombre());
					}
				}
			}
			//Agregar envio de correo.
			requerimientoService.Registrar(objecto, attachment);

			try {
				List<InternetAddress> listAddresses = new ArrayList<InternetAddress> ();

				Empleado empleado2=empleadoDAO.findByCodigo(objecto.getUsuario_registra());

				if(empleado2.getCorreo()!=null && !empleado2.getCorreo().trim().isEmpty()){
					listAddresses.add(new InternetAddress(empleado2.getCorreo()));
					InternetAddress[] arrAddresses = listAddresses.toArray(new InternetAddress[0]);
					//				String mensaje="Se ha registrado una respuesta para la consulta o reclamo "+objecto.getId()+" en el sistema de Fast track empresas.";
					ParametrosConf parametroMensaje=parametrosConfDAO.obtener(ConstantesParametros.CODIGO_CONF, ConstantesBusiness.MENSAJE_CORREO);

					String mensaje=parametroMensaje.getValorVariable();
					mensaje=mensaje.replace("NROCR",String.valueOf(objecto.getId()));		

					enviarCorreo("Respuesta de Consulta / reclamo "+objecto.getId(), arrAddresses, mensaje);
				}
			} catch (AddressException e) {
				LOG.info("",e);
			} catch (Exception e){
				LOG.info("Error al enviar el correo",e);
			}


			irBandeja();
		}else{
			LOG.info("LA VERSION DEL RECLAMO / CONSULTA NO ES LA ULTIMA, VUELVA A CARGAR EL DOCUMENTO.");
			redirectAction("../reclamo/registroError",new String[] {"mensajeError","El reclamo / consulta ya fue modificado por otro usuario."});
		}
	}

	private boolean validarVersion(RequerimientoModel r){
		RequerimientoModel req=new RequerimientoModel();
		try {
			req=requerimientoService.obtener(r);
		} catch (SQLException e) {
			LOG.error("",e);
		}
		if(r.getVersion()==req.getVersion()){
			return true;
		}else{
			return false;
		}

	}

	public void verReclamoConsulta(){		
		LOG.info("verReclamoConsulta - empleado - abogado : "+empleado.getFlagAbogado());		
		//		try {
		String pagina;
		if(empleado.getPerfil().getCodigo().equals(ConstantesBusiness.CODIGO_PERFIL_ABOGADO_REVISION)){ //ES ABOGADO
			pagina = "../reclamo/absolverReclamoConsulta";
			redirectAction(pagina,new String[] {"idReclamoConsulta",filtro.getId().toString()});
		}else{
			pagina = "../reclamo/verificarResultado";
			redirectAction(pagina,new String[] {"idReclamoConsulta",filtro.getId().toString()});
		}
	}

	public void asignarAbogado(AjaxBehaviorEvent event){
		LOG.info("asignarAbogado",objecto);

		try {
			RequerimientoModel req=new RequerimientoModel();
			req=requerimientoService.obtener(objecto);

			req.setIdAbogado(objecto.getIdAbogado());
			req.setNombreAbogadoAsignado(empleadoDAO.findByCodigo(objecto.getIdAbogado()).getNombresCompletos());		
			requerimientoService.Actualizar(req, null);
			objecto.setNombreAbogadoAsignado(req.getNombreAbogadoAsignado());
		} catch (Exception e) {
			LOG.info("",e);
			objecto.setNombreAbogadoAsignado("NO ASIGNADO");
		}

	}

	public void registrar(){
		LOG.info("REGISTRAR");	
		//		if(validarDatosRegistrar()){
		objecto.setCod_central(clientes.get(0).getCodigoCentral());
		objecto.setRazon_social(clientes.get(0).getRazonSocial());
		objecto.setNro_doi(clientes.get(0).getNumeroDOI());
		objecto.setEstado(ConstantesBusiness.CODIGO_HIJO_ESTADO_ENVIADO);
		objecto.setEs_migrado("S"); // CONSULTAR DE DONDE SALE EL CAMPO
		objecto.setUsuario_registra(empleado.getCodigo());
		objecto.setNombre_usuario_registra(empleado.getNombresCompletos());
		objecto.setVersion(1); 
		objecto.setReconsideracion(false);
		objecto.setFecha_registro(new Date());
		objecto.setFecha_absuelve(null);
		objecto.setFecha_absuelve_reconsidera(null);
		objecto.setFecha_reconsidera(null);
		objecto.setFechaInicio(null);
		objecto.setFechafin(null);
		objecto.setTipoDoi(clientes.get(0).getTipoDOI());
		objecto.setCod_oficina(empleado.getOficina().getCodigo());

		attachment.setEstado_ref(multiTablaDAO.obtenerxEntero(ConstantesBusiness.CODIGO_HIJO_ESTADO_ENVIADO).getNombre());
		LOG.info("object MB :"+objecto);
		LOG.info("attachment MB :"+attachment.getRuta_archivo() + " "+attachment.getNombre_archivo());

		requerimientoService.Registrar(objecto, attachment);			
		LOG.info("object MB Despues de Registrar :"+objecto);

		redirectAction("../reclamo/registroExito",new String[] {"idObjecto",objecto.getId().toString()});

		//		}
	}
	private void irBandeja(){
		String pagina;
		pagina = "../reclamo/bandeja";
		redirectAction(pagina);
	}

	public void buscarCliente(AjaxBehaviorEvent event){
		LOG.info("buscar (ActionEvent event)");
		setClientes(new ArrayList<ClientePJCore>());
		if(!filtro.getCod_central().isEmpty()){
			try {
				setClientes(clienteBusiness.buscarCodigoCentral(filtro.getCod_central(), empleado.getCodigo()));
				cliente=clientes.get(0);
				LOG.info("TAMAÑO :" +clientes.size()+"_");
				LOG.info("cliente.getCodigoCentral()" + cliente.getCodigoCentral());
				LOG.info("cliente.getDatosSFP().getOficinaRegistral()" + cliente.getDatosSFP().getOficinaRegistral());
				LOG.info("cliente.getDatosSFP().getCodigoCentral()" + cliente.getDatosSFP().getCodigoCentral());
				LOG.info("cliente.getDatosSFP().getTipoPJ()" + cliente.getDatosSFP().getTipoPJ());
				objecto.setEs_migrado(cliente.getDatosSFP().getTipoPJ()==null?"NO":cliente.getDatosSFP().getTipoPJ()==""?"NO":"SI");
			} catch (ParametroIlegalException e) {
				LOG.error("",e);
				mensajeInfoPrincipal("tabView:botonBuscar", 
						"Codigo Central no existe");
			} catch (ConexionServicioException e) {
				LOG.error("",e);
				mensajeInfoPrincipal("tabView:botonBuscar", 
						"Error de conexion");
			} catch (ErrorObteniendoDatosException e) {
				LOG.error("",e);
				mensajeInfoPrincipal("tabView:botonBuscar", 
						"Error al obtener los datos, vuelva a intentarlo");
			}
		}else{
			try {
				setClientes(clienteBusiness.buscarNumeroDOI(filtro.getNro_doi(), empleado.getCodigo()));
				cliente=clientes.get(0);
			} catch (ParametroIlegalException e) {
				LOG.error("",e);
				mensajeInfoPrincipal("tabView:botonBuscar", 
						"Nro Doi no existe");
			} catch (ConexionServicioException e) {
				LOG.error("",e);
				mensajeInfoPrincipal("tabView:botonBuscar", 
						"Error de conexion");
			} catch (ErrorObteniendoDatosException e) {
				LOG.error("",e);
				mensajeInfoPrincipal("tabView:botonBuscar", 
						"Error al obtener los datos, vuelva a intentarlo");
			}
		}

	}
	public void buscarConsultaReclamo(AjaxBehaviorEvent event){
		LOG.info("buscarConsultaReclamo : ");				
		try {
			filtro.setCod_oficina(empleado.getPerfil().getCodigo().equals(ConstantesBusiness.CODIGO_PERFIL_ABOGADO_REVISION)?null:empleado.getOficina().getCodigo());
			LOG.info("FILTRO MB : "+filtro.toString());
			LOG.info("OFICINA MB : "+(empleado.getFlagAbogado().equalsIgnoreCase("1")?null:empleado.getOficina().getCodigo()));

			listRequerimientoModels= requerimientoService.listaRequerimiento(filtro);
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			LOG.error("",e);
		}
		LOG.info("listRequerimientoModels : "+listRequerimientoModels.toString());
	}

	public void exportar(){

		try {
			String rutaPlantilla = (String) ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).get(ConstantesParametros.RUTA_PLANTILLA_REPORTE);
			ReporteDinamico reporte = new ReporteDinamico();
			URL imgURL =  ReporteMB.class.getResource("/imagenes/logo.gif");
			reporte.instanciarReporte("REPORTE DE CONSULTA Y RECLAMO", "", "Lista de consultas/reclamos", rutaPlantilla, "CONSULTA_RECLAMO" ,1100, imgURL, "templateExcelH");
			reporte.setTipo(EnmTipoArchivo.Excel.getTipoArchivo());
			reporte.insertarColumnas(obtenerColumnas());
			
			buscarConsultaReclamo(null);
			
			if(listRequerimientoModels.size()==0){
				RequerimientoModel req =  new RequerimientoModel();
				listRequerimientoModels.add(req);
			}
			String ExcelGenerado = reporte.generarReporte(listRequerimientoModels);
			String contextPath = "/" + FacesContext.getCurrentInstance().getExternalContext().getContextName() + "/ServletReport?method=downloadExcel&delete=true&file=";
			FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + rutaPlantilla + File.separator + ExcelGenerado + ".xlsx");
		} catch (Exception ex) {
			LOG.error("", ex);
		}


		LOG.info("{exportar()}");
	}

	private List<ColumnaDinamica> obtenerColumnas() {
		List<ColumnaDinamica> columnas = new ArrayList<ColumnaDinamica>();

		ColumnaDinamica a = new ColumnaDinamica();
		a.setNombreAtributo("strId");
		a.instanciarColumna("CODIGO", EnumTipoDatoJava.String, "");
		a.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		a.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		a.setDimencion(20, 10);
		a.setInicioFinal(0, 0);
		columnas.add(a);



		ColumnaDinamica c= new ColumnaDinamica();
		c.setNombreAtributo("str_tipo_operacion");
		c.instanciarColumna("TIPO", EnumTipoDatoJava.String, "");
		c.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		c.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		c.setDimencion(20, 15);
		c.setInicioFinal(0, 0);
		columnas.add(c);


		ColumnaDinamica k= new ColumnaDinamica();
		k.setNombreAtributo("cod_central");
		k.instanciarColumna("COD CENTRAL", EnumTipoDatoJava.String, "");
		k.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		k.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		k.setDimencion(20, 15);
		k.setInicioFinal(0, 0);
		columnas.add(k);

		ColumnaDinamica f= new ColumnaDinamica();
		f.setNombreAtributo("razon_social");
		f.instanciarColumna("RAZON SOCIAL", EnumTipoDatoJava.String, "");
		f.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		f.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		f.setDimencion(20, 20);
		f.setInicioFinal(0, 0);
		columnas.add(f);

		ColumnaDinamica d= new ColumnaDinamica();
		d.setNombreAtributo("nro_doi");
		d.instanciarColumna("NRO DOI", EnumTipoDatoJava.String, "");
		d.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		d.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		d.setDimencion(20, 10);
		d.setInicioFinal(0, 0);
		columnas.add(d);

		ColumnaDinamica e= new ColumnaDinamica();
		e.setNombreAtributo("usuario_registra");
		e.instanciarColumna("GESTOR REGISTRANTE", EnumTipoDatoJava.String, "");
		e.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		e.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		e.setDimencion(20, 10);
		e.setInicioFinal(0, 0);
		columnas.add(e);



		ColumnaDinamica g= new ColumnaDinamica();
		g.setNombreAtributo("str_oficina");
		g.instanciarColumna("OFICINA", EnumTipoDatoJava.String, "");
		g.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		g.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		g.setDimencion(20, 20);
		g.setInicioFinal(0, 0);
		columnas.add(g);


		ColumnaDinamica h= new ColumnaDinamica();
		h.setNombreAtributo("strEstado");
		h.instanciarColumna("ESTADO", EnumTipoDatoJava.String, "");
		h.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		h.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		h.setDimencion(20, 10);
		h.setInicioFinal(0, 0);
		columnas.add(h);


		ColumnaDinamica a4= new ColumnaDinamica();
		a4.setNombreAtributo("str_resultado");
		a4.instanciarColumna("DICTAMEN RECLAMO", EnumTipoDatoJava.String, "");
		a4.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		a4.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		a4.setDimencion(20, 15);
		a4.setInicioFinal(0, 0);
		columnas.add(a4);

		ColumnaDinamica j= new ColumnaDinamica();
		j.setNombreAtributo("strFechaRegistro");
		j.instanciarColumna("FECHA REG", EnumTipoDatoJava.String, "");
		j.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		j.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		j.setDimencion(20, 10);
		j.setInicioFinal(0, 0);
		columnas.add(j);

		ColumnaDinamica l= new ColumnaDinamica();
		l.setNombreAtributo("strDescripcion_reclamo_Consulta");
		l.instanciarColumna("DESCRIPCION DE RECLAMO/CONSULTA", EnumTipoDatoJava.String, "");
		l.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		l.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		l.setDimencion(20, 40);
		l.setInicioFinal(0, 0);
		columnas.add(l);

		ColumnaDinamica m= new ColumnaDinamica();
		m.setNombreAtributo("strDescripcion_absolucion");
		m.instanciarColumna("DESCRIPCION DE ABSOLUCION", EnumTipoDatoJava.String, "");
		m.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		m.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		m.setDimencion(20, 40);
		m.setInicioFinal(0, 0);
		columnas.add(m);

		ColumnaDinamica o= new ColumnaDinamica();
		o.setNombreAtributo("strMotivo_reconsideracion");
		o.instanciarColumna("DESCRIPCION DE RECONSIDERACION", EnumTipoDatoJava.String, "");
		o.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		o.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		o.setDimencion(20, 40);
		o.setInicioFinal(0, 0);
		columnas.add(o);

		ColumnaDinamica p= new ColumnaDinamica();
		p.setNombreAtributo("strDescripcion_absolucion_recon");
		p.instanciarColumna("DESCRIPCION DE ABSOLUCION DE RECONSIDERACION", EnumTipoDatoJava.String, "");
		p.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		p.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		p.setDimencion(20, 40);
		p.setInicioFinal(0, 0);
		columnas.add(p);
		return columnas;
	}


	public void selectOperacion(AjaxBehaviorEvent event){
		LOG.info("TIPO OPERACION : "+objecto.getTipo_operacion().toString());
		if(objecto.getTipo_operacion().compareTo(ConstantesBusiness.CODIGO_HIJO_TIPO_OPERACION_RECLAMO)==0){ //Reclamo
			objecto.setShowCategoriaReclamo(true);

		}else{
			objecto.setDetalle_categoria_reclamo(0L);
			filtro.setDisableBtnRegistrar(false);
			objecto.setShowCategoriaReclamo(false);
		}

	}
	public void selectCategoria(AjaxBehaviorEvent event){
		LOG.info("TIPO CATEGORIA : "+objecto.getDetalle_categoria_reclamo().toString());

		if(objecto.getDetalle_categoria_reclamo().compareTo(ConstantesBusiness.CODIGO_HIJO_CATEGORIA_RECLAMO_OPERATIVO)==0){ //Reclamo
			objecto.setTextoDetalleCategoria("Para Reclamo: Diferencia de datos de representes en WR00; Diferencia de limite de poderes y Nacar");
			filtro.setDisableBtnRegistrar(true);
		}else if (objecto.getDetalle_categoria_reclamo().compareTo(ConstantesBusiness.CODIGO_HIJO_CATEGORIA_RECLAMO_JURIDICO)==0){
			objecto.setTextoDetalleCategoria("Para Reclamo: Error u omisión de registro de poderes pendiente de revocación");
			filtro.setDisableBtnRegistrar(false);
		}else{
			objecto.setTextoDetalleCategoria("");
		}
	}
	public void mostrarDictamen(AjaxBehaviorEvent event){
		LOG.info("mostrarDictamen() "+"ESTADO : "+filtro.getEstado().toString());
		if(filtro.getEstado().compareTo(ConstantesBusiness.CODIGO_HIJO_ESTADO_DICTAMINADO)==0){ //Reclamo
			filtro.setShowDictamenReclamo(true);			
			LOG.info(""+filtro.isShowDictamenReclamo());
		}else{
			filtro.setShowDictamenReclamo(false);
			LOG.info(""+filtro.isShowDictamenReclamo());
		}
		filtro.setDictamen_reclamo(0L);
	}
	public void filtrarEstados(AjaxBehaviorEvent event){
		LOG.info("filtrarEstados() "+"TIPO OPERACION : "+filtro.getTipo_operacion().toString());
		if(filtro.getTipo_operacion().compareTo(ConstantesBusiness.CODIGO_HIJO_TIPO_OPERACION_RECLAMO)==0){ //Reclamo
			listaEstados.clear();
			listaEstados.add(new SelectItem(0,"--Seleccione--"));
			listaEstados.add(new SelectItem(ConstantesBusiness.CODIGO_HIJO_ESTADO_ENVIADO,ConstantesBusiness.LABEL_HIJO_ESTADO_ENVIADO));
			listaEstados.add(new SelectItem(ConstantesBusiness.CODIGO_HIJO_ESTADO_RECONSIDERADO,ConstantesBusiness.LABEL_HIJO_ESTADO_RECONSIDERADO));
			listaEstados.add(new SelectItem(ConstantesBusiness.CODIGO_HIJO_ESTADO_DICTAMINADO,ConstantesBusiness.LABEL_HIJO_ESTADO_DICTAMINADO));

		}else if(filtro.getTipo_operacion().compareTo(ConstantesBusiness.CODIGO_HIJO_TIPO_OPERACION_CONSULTA)==0){ //Reclamo
			listaEstados.clear();
			listaEstados.add(new SelectItem(0,"--Seleccione--"));
			listaEstados.add(new SelectItem(ConstantesBusiness.CODIGO_HIJO_ESTADO_ENVIADO,ConstantesBusiness.LABEL_HIJO_ESTADO_ENVIADO));

		}else{
			cargarListaEstados();
		}

	}

	private void cargarListaDictamenReclamo(){
		List<MultiTabla>listado=new ArrayList<MultiTabla>();	
		listado=multiTablaDAO.listaHijos(ConstantesBusiness.ID_PADRE_ESTADO_DICTAMINADO);

		//		listaDictamenReclamo.add(new SelectItem(0,"--Seleccione--"));
		for(MultiTabla mt:listado){
			listaDictamenReclamo.add(new SelectItem(mt.getEntero(),mt.getNombre()));
		}

	}
	private void cargarListaCategoriaReclamo(){
		List<MultiTabla>listado=new ArrayList<MultiTabla>();	
		listado=multiTablaDAO.listaHijos(ConstantesBusiness.ID_PADRE_CATEGORIA_RECLAMO);

		listaCategoriaReclamo.add(new SelectItem(0,"--Seleccione--"));
		for(MultiTabla mt:listado){
			listaCategoriaReclamo.add(new SelectItem(mt.getEntero(),mt.getNombre()));
		}

	}
	private void cargarListaClasificacion(){
		ParametrosConf clasificaciones=new ParametrosConf();
		clasificaciones=parametrosConfDAO.obtener(ConstantesParametros.CODIGO_CONF, ConstantesParametros.LISTA_CLASIFICACION);

		LOG.info("clasificaciones :" +clasificaciones);

		String [] obj1=clasificaciones.getValorVariable().split("\\*");
		LOG.info("obj1 :"+obj1[0]);
		for(String s:obj1){
			String [] obj2=s.split(",");
			LOG.info("obj2: "+obj2);
			LOG.info(obj2[0] + "-" + obj2[1]);
			listaClasificacion.add(new SelectItem(obj2[0],obj2[1]));
		}

	}

	private void cargarListaTipoOperacion(){
		List<MultiTabla>listado=new ArrayList<MultiTabla>();	
		listado=multiTablaDAO.listaHijos(ConstantesBusiness.ID_PADRE_TIPO_OPERACION);

		listaTipoOperacion.add(new SelectItem(0,"--Seleccione--"));
		for(MultiTabla mt:listado){
			listaTipoOperacion.add(new SelectItem(mt.getEntero(),mt.getNombre()));
		}
	}
	private void cargarListaEstados() {
		List<MultiTabla>listado=new ArrayList<MultiTabla>();	
		listado=multiTablaDAO.listaHijos(ConstantesBusiness.ID_PADRE_ESTADOS_REQUERIMIENTO);
		listaEstados=new ArrayList<SelectItem>();
		listaEstados.add(new SelectItem(0,"--Seleccione--"));
		for(MultiTabla mt:listado){
			if(!(mt.getId().compareTo(ConstantesBusiness.CODIGO_HIJO_ESTADO_CERRADO)==0)){
				listaEstados.add(new SelectItem(mt.getEntero(),mt.getNombre()));
			}
		}

	}


	public void enviarCorreo(String asunto,InternetAddress[] listAddresses,String mensaje) {
		LOG.info("enviarCorreo()");
		try {

			ParametrosSistema parametros = ParametrosSistema.getInstance();
			Properties propertiesWAS = parametros.getProperties(ParametrosSistema.CONF);
			String hostServidorCorreo;
			String puertoServidorCorreo;
			String remitente;
			hostServidorCorreo = propertiesWAS.getProperty(ConstantesParametros.HOST_SERVIDOR_CORREO);
			puertoServidorCorreo = propertiesWAS.getProperty(ConstantesParametros.PUERTO_SERVIDOR_CORREO);
			remitente = propertiesWAS.getProperty(ConstantesParametros.EMAIL_REMITENTE);


			// Get the session object
			LOG.info("Host Servidor Correo: "+hostServidorCorreo);
			LOG.info("Puerto Servidor Correo: "+puertoServidorCorreo);
			LOG.info("remitente: "+remitente);
			//Properties properties = System.getProperties();
			Properties properties = new Properties();
			properties.setProperty("mail.smtp.host", hostServidorCorreo);
			properties.setProperty("mail.smtp.port", puertoServidorCorreo);
			Session session = Session.getDefaultInstance(properties);

			// compose the message

			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(remitente));

			message.addRecipients(Message.RecipientType.TO, listAddresses);

			message.setSubject(asunto);
			StringBuilder sb = new StringBuilder();
			//sb.append("Le confirmamos que ya puede trabajar ").append(documento.getDescripcion()).append(" para el expediente ").append(numeroExpediente).append(".\n");
			sb.append(mensaje).append("\n");
			sb.append("\n");
			message.setText(sb.toString());
			LOG.info("Texto del mensaje: "+sb.toString());

			// Send message
			Transport.send(message);
			LOG.info("Se envió el correo satisfactoriamente.");

		} catch (Exception ex) {
			LOG.error("Error al enviar el correo.", ex);
		}
	}




	public List<RequerimientoModel> getListRequerimientoModels() {
		return listRequerimientoModels;
	}
	public void setListRequerimientoModels(
			List<RequerimientoModel> listRequerimientoModels) {
		this.listRequerimientoModels = listRequerimientoModels;
	}
	public List<SelectItem> getListaCategoriaReclamo() {
		return listaCategoriaReclamo;
	}
	public void setListaCategoriaReclamo(List<SelectItem> listaCategoriaReclamo) {
		this.listaCategoriaReclamo = listaCategoriaReclamo;
	}
	public RequerimientoModel getFiltro() {
		return filtro;
	}
	public void setFiltro(RequerimientoModel filtro) {
		this.filtro = filtro;
	}
	public List<SelectItem> getListaTipoOperacion() {
		return listaTipoOperacion;
	}
	public void setListaTipoOperacion(List<SelectItem> listaTipoOperacion) {
		this.listaTipoOperacion = listaTipoOperacion;
	}
	public List<ClientePJCore> getClientes() {
		return clientes;
	}
	public void setClientes(List<ClientePJCore> clientes) {
		this.clientes = clientes;
	}
	public List<SelectItem> getListaEstados() {
		return listaEstados;
	}
	public void setListaEstados(List<SelectItem> listaEstados) {
		this.listaEstados = listaEstados;
	}
	public RequerimientoModel getObjecto() {
		return objecto;
	}
	public void setObjecto(RequerimientoModel objecto) {
		this.objecto = objecto;
	}

	public List<SelectItem> getListaDictamenReclamo() {
		return listaDictamenReclamo;
	}

	public void setListaDictamenReclamo(List<SelectItem> listaDictamenReclamo) {
		this.listaDictamenReclamo = listaDictamenReclamo;
	}
	@Override
	public int getRowCount() {
		if (listRequerimientoModels != null) {
			return listRequerimientoModels.size();
		} else {
			return 0;
		}
	}

	public List<SelectItem> getListaClasificacion() {
		return listaClasificacion;
	}

	public void setListaClasificacion(List<SelectItem> listaClasificacion) {
		this.listaClasificacion = listaClasificacion;
	}

	public ClientePJCore getCliente() {
		return cliente;
	}

	public void setCliente(ClientePJCore cliente) {
		this.cliente = cliente;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public List<SelectItem> getListaAbogados() {
		listaAbogados=Util.crearItems(abogados, "codigo", "nombresCompletos");
		return listaAbogados;
	}

	public void setListaAbogados(List<SelectItem> listaAbogados) {
		this.listaAbogados = listaAbogados;
	}

	public Integer getTamañoMaxArchivo() {
		return tamañoMaxArchivo;
	}

	public void setTamañoMaxArchivo(Integer tamañoMaxArchivo) {
		this.tamañoMaxArchivo = tamañoMaxArchivo;
	}

	public String getLinkSFP() {
		return linkSFP;
	}

	public void setLinkSFP(String linkSFP) {
		this.linkSFP = linkSFP;
	}





}
