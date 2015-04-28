package com.ibm.bbva.ctacte.servicio.proceso;

import hiper.spring.beans.hfirmas.webservices.xsd.PoderFirmaActivacion;
import hiper.spring.beans.hfirmas.webservices.xsd.RpteActivacionBE;
import hiper.webservices.impl.hfirmas.WSServicioSFPHttpSoap11EndpointProxy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.internet.InternetAddress;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.grupobbva.accpj.pagser.CtBodyRq;
import pe.com.grupobbva.accpj.pagser.CtHeader;
import pe.com.grupobbva.accpj.pagser.CtPagoServicioRq;
import pe.com.grupobbva.accpj.pagser.CtPagoServicioRs;
import pe.com.grupobbva.accpj.pagser.PagoServicio_PortProxy;
import pe.com.grupobbva.sce.qsp5.CtInqPerfilUsuarioRq;
import pe.com.grupobbva.sce.qsp5.CtInqPerfilUsuarioRs;
import pe.com.grupobbva.sce.qsp5.SCE_QSP5_PortProxy;
import clientecontent.ClienteContent;

import com.ibm.bbva.cm.service.Documento;
import com.ibm.bbva.ctacte.bean.AuditoriaCriteriosSupervision;
import com.ibm.bbva.ctacte.bean.CobroComision;
import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.Participe;
import com.ibm.bbva.ctacte.bean.PerfilBalanceo;
import com.ibm.bbva.ctacte.bean.TareaPerfil;
import com.ibm.bbva.ctacte.bean.ViewNumeroExpedientesEmpleado;
import com.ibm.bbva.ctacte.bean.ViewPesoDocumentoExpediente;
import com.ibm.bbva.ctacte.bean.ViewPesoParticipeExpediente;
import com.ibm.bbva.ctacte.business.ExpedienteBusiness;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.AuditoriaBastanteoDAO;
import com.ibm.bbva.ctacte.dao.AuditoriaClienteDAO;
import com.ibm.bbva.ctacte.dao.AuditoriaCriteriosSupervisionDAO;
import com.ibm.bbva.ctacte.dao.CobroComisionDAO;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.EstadoExpedienteDAO;
import com.ibm.bbva.ctacte.dao.EstadoTareaDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.OperacionDAO;
import com.ibm.bbva.ctacte.dao.ParticipeDAO;
import com.ibm.bbva.ctacte.dao.PerfilBalanceoDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;
import com.ibm.bbva.ctacte.dao.TareaPerfilDAO;
import com.ibm.bbva.ctacte.dao.ViewNumeroExpedientesEmpleadoDAO;
import com.ibm.bbva.ctacte.dao.ViewPesoDocumentoExpedienteDAO;
import com.ibm.bbva.ctacte.dao.ViewPesoParticipeExpedienteDAO;
import com.ibm.bbva.ctacte.servicio.proceso.bean.EmpleadoCE;
import com.ibm.bbva.ctacte.servicio.proceso.util.ProcesoUtil;
import com.ibm.bbva.ctacte.servicio.util.Constantes;
import com.ibm.bbva.ctacte.util.EJBLocator;
import com.ibm.bbva.ctacte.util.ParametrosSistema;

public class CEProcessService {
	
	private static final Logger LOG = LoggerFactory.getLogger(CEProcessService.class);
	
	private ExpedienteBusiness expedienteBusiness;
	private ViewNumeroExpedientesEmpleadoDAO viewNumeroExpedientesEmpleadoDAO;
	private ViewPesoParticipeExpedienteDAO viewPesoParticipeExpedienteDAO;
	private ViewPesoDocumentoExpedienteDAO viewPesoDocumentoExpedienteDAO;
	private TareaPerfilDAO tareaPerfilDAO;
	private PerfilBalanceoDAO perfilBalanceoDAO;
	private EmpleadoDAO empleadoDAO;
	private AuditoriaCriteriosSupervisionDAO auditoriaCriteriosSupervisionDAO;
	private AuditoriaClienteDAO auditoriaClienteDAO;
	private AuditoriaBastanteoDAO auditoriaBastanteoDAO;
	private ExpedienteDAO expedienteDAO;
	private ParticipeDAO participeDAO;
	private CobroComisionDAO cobroComisionDAO;
	private OperacionDAO operacionDAO;
	private EstadoExpedienteDAO estadoExpedienteDAO;
	private EstadoTareaDAO estadoTareaDAO;
	private TareaDAO tareaDAO;
	private DocumentoExpDAO documentoExpDAO;
	
	public CEProcessService() throws NamingException {
		super();
		expedienteBusiness = EJBLocator.getExpedienteBusiness();
		viewNumeroExpedientesEmpleadoDAO = EJBLocator.getViewNumeroExpedientesEmpleadoDAO();
		viewPesoParticipeExpedienteDAO = EJBLocator.getViewPesoParticipeExpedienteDAO();
		viewPesoDocumentoExpedienteDAO = EJBLocator.getViewPesoDocumentoExpedienteDAO();
		tareaPerfilDAO = EJBLocator.getTareaPerfilDAO();
		perfilBalanceoDAO = EJBLocator.getPerfilBalanceoDAO();
		empleadoDAO = EJBLocator.getEmpleadoDAO();
		auditoriaCriteriosSupervisionDAO = EJBLocator.getAuditoriaCriteriosSupervisionDAO();
		auditoriaClienteDAO = EJBLocator.getAuditoriaClienteDAO();
		auditoriaBastanteoDAO = EJBLocator.getAuditoriaBastanteoDAO();
		expedienteDAO = EJBLocator.getExpedienteDAO();
		participeDAO = EJBLocator.getParticipeDAO();
		cobroComisionDAO = EJBLocator.getCobroComisionDAO();
		operacionDAO = EJBLocator.getOperacionDAO();
		estadoExpedienteDAO = EJBLocator.getEstadoExpedienteDAO();
		estadoTareaDAO = EJBLocator.getEstadoTareaDAO();
		tareaDAO = EJBLocator.getTareaDAO();
		documentoExpDAO = EJBLocator.getDocumentoExpDAO();
	}
	
	public Integer dentroPlazoSubsanacion(Date dtFechaRegistroExpediente, Date dtFechaUltimoBastanteo){
		return expedienteBusiness.dentroPlazoSubsanacion(dtFechaRegistroExpediente, dtFechaUltimoBastanteo);
	}
	
	public ViewNumeroExpedientesEmpleado[] obtenerNumExpedientesxEmpleado(Integer intIdProducto, Integer intIdTerritorio, Integer intIdTarea){
		try{
			//List<ViewNumeroExpedientesEmpleado> listnumExpedientesxEmpleado = viewNumeroExpedientesEmpleadoDAO.findListaEmpleadosSinExpedientes(intIdProducto, intIdTerritorio, intIdTarea);
			List<ViewNumeroExpedientesEmpleado> listnumExpedientesxEmpleado = viewNumeroExpedientesEmpleadoDAO.findListaNumeroExpedientesxEmpleado(intIdProducto, intIdTerritorio, intIdTarea);
			return listnumExpedientesxEmpleado.toArray( new ViewNumeroExpedientesEmpleado[listnumExpedientesxEmpleado.size()]);
		} catch (Exception ex) {
			LOG.error("",ex);
			return null;
		}
	}
	
	public ViewPesoParticipeExpediente obtenerEmpleadoxPesoParticipe(Integer intIdProducto, Integer intIdTerritorio, Integer intIdTarea){
		try{
			ViewPesoParticipeExpediente viewPesoParticipeExpediente = new ViewPesoParticipeExpediente();
			
			//Verificar sin existen empleados sin expedientes asignados
			int intNumEmpleadosSinExpedientes;
			ViewNumeroExpedientesEmpleado viewNumeroExpedientesEmpleado = new ViewNumeroExpedientesEmpleado();
			List<ViewNumeroExpedientesEmpleado> listnumExpedientesxEmpleado = viewNumeroExpedientesEmpleadoDAO.findListaEmpleadosSinExpedientes(intIdProducto, intIdTerritorio, intIdTarea);
			intNumEmpleadosSinExpedientes = listnumExpedientesxEmpleado.size();
			if (intNumEmpleadosSinExpedientes > 0){
				int indice = (int) Math.floor(Math.random()*(intNumEmpleadosSinExpedientes-1));
				viewNumeroExpedientesEmpleado = listnumExpedientesxEmpleado.get(indice);
				viewPesoParticipeExpediente.setCodEmpleado(viewNumeroExpedientesEmpleado.getCodEmpleado());
				viewPesoParticipeExpediente.setDesPerfil(viewNumeroExpedientesEmpleado.getDesPerfil());
				viewPesoParticipeExpediente.setIdEmpleado(viewNumeroExpedientesEmpleado.getIdEmpleado());
				viewPesoParticipeExpediente.setIdPerfil(viewNumeroExpedientesEmpleado.getIdPerfil());
				viewPesoParticipeExpediente.setIdProducto(viewNumeroExpedientesEmpleado.getIdProducto());
				viewPesoParticipeExpediente.setIdTarea(viewNumeroExpedientesEmpleado.getIdTarea());
				viewPesoParticipeExpediente.setIdTerritorio(viewNumeroExpedientesEmpleado.getIdTerritorio());
				viewPesoParticipeExpediente.setSumPesoParticipeExpediente(viewNumeroExpedientesEmpleado.getNumExpedientesEmpleado());
			}else{
				List<ViewPesoParticipeExpediente> listViewPesoParticipeExpediente = viewPesoParticipeExpedienteDAO.findListaPesoParticipesxExpediente(intIdProducto, intIdTerritorio, intIdTarea);
				viewPesoParticipeExpediente = listViewPesoParticipeExpediente.get(0);
			}
			return viewPesoParticipeExpediente;
		} catch (Exception ex) {
			LOG.error("",ex);
			return null;
		}
	}
	
	public ViewPesoDocumentoExpediente obtenerEmpleadoxPesoDocumento(Integer intIdProducto, Integer intIdTerritorio, Integer intIdTarea){
		try{
			ViewPesoDocumentoExpediente viewPesoDocumentoExpediente = new ViewPesoDocumentoExpediente();
			//Verificar sin existen empleados sin expedientes asignados
			int intNumEmpleadosSinExpedientes;
			ViewNumeroExpedientesEmpleado viewNumeroExpedientesEmpleado = new ViewNumeroExpedientesEmpleado();
			List<ViewNumeroExpedientesEmpleado> listnumExpedientesxEmpleado = viewNumeroExpedientesEmpleadoDAO.findListaEmpleadosSinExpedientes(intIdProducto, intIdTerritorio, intIdTarea);
			intNumEmpleadosSinExpedientes = listnumExpedientesxEmpleado.size();
			if (intNumEmpleadosSinExpedientes > 0){
				int indice = (int) Math.floor(Math.random()*(intNumEmpleadosSinExpedientes-1));
				viewNumeroExpedientesEmpleado = listnumExpedientesxEmpleado.get(indice);
				viewPesoDocumentoExpediente.setCodEmpleado(viewNumeroExpedientesEmpleado.getCodEmpleado());
				viewPesoDocumentoExpediente.setDesPerfil(viewNumeroExpedientesEmpleado.getDesPerfil());
				viewPesoDocumentoExpediente.setIdEmpleado(viewNumeroExpedientesEmpleado.getIdEmpleado());
				viewPesoDocumentoExpediente.setIdPerfil(viewNumeroExpedientesEmpleado.getIdPerfil());
				viewPesoDocumentoExpediente.setIdProducto(viewNumeroExpedientesEmpleado.getIdProducto());
				viewPesoDocumentoExpediente.setIdTarea(viewNumeroExpedientesEmpleado.getIdTarea());
				viewPesoDocumentoExpediente.setIdTerritorio(viewNumeroExpedientesEmpleado.getIdTerritorio());
				viewPesoDocumentoExpediente.setSumPesoDocumentoExpediente(viewNumeroExpedientesEmpleado.getNumExpedientesEmpleado());
			}else{
				List<ViewPesoDocumentoExpediente> listViewPesoDocumentoExpediente = viewPesoDocumentoExpedienteDAO.findListaPesoDocumentoxExpediente(intIdProducto, intIdTerritorio, intIdTarea);
				viewPesoDocumentoExpediente = listViewPesoDocumentoExpediente.get(0);
			}
			return viewPesoDocumentoExpediente;
		} catch (Exception ex) {
			LOG.error("",ex);
			return null;
		}
	}
	
	public EmpleadoCE obtenerEmpleado(Integer intIdExpediente, Integer intIdProducto, Integer intIdTerritorio, Integer intIdTarea){
		// Si la Tarea es "Verificar y Realizar Bastanteo" y es nueva modificatoria se utiliza el perfil "Pool de Migracion". 
		if (intIdTarea.intValue() == ConstantesBusiness.ID_TAREA_VERIFICAR_REALIZAR_BASTANTEO) {
			Expediente expediente = expedienteDAO.load(intIdExpediente);
			LOG.info("CODIGO OPERACION: "+expediente.getOperacion().getCodigoOperacion());
			if (ConstantesBusiness.CODIGO_MODIFICATORIA_BASTANTEO.equals(expediente.getOperacion().getCodigoOperacion())) {
//				List<Expediente> lstExpedientesAntiguos = expedienteDAO
//						.findByCodigoCentralSinIdExpediente(expediente
//								.getCliente().getCodigoCentral(), expediente
//								.getId());
//				LOG.info("# de expedienes: "+lstExpedientesAntiguos.size());
//				if (lstExpedientesAntiguos == null || lstExpedientesAntiguos.isEmpty()) {
//					intIdTarea = ConstantesBusiness.ID_TAREA_VERIFICAR_REALIZAR_BASTANTEO_2;
//				}
				LOG.info("FLAG_EXP_MIGRADO: "+expediente.getCliente().getFlagExpMigrado());
				if (!"1".equals(expediente.getCliente().getFlagExpMigrado())) {
					intIdTarea = ConstantesBusiness.ID_TAREA_VERIFICAR_REALIZAR_BASTANTEO_2;
				}
			}
		}
		
		TareaPerfil tareaPerfil = tareaPerfilDAO.findTareaPerfilxTarea(intIdTarea);
		
		Integer intIdPerfil = tareaPerfil.getPerfil().getId();
		//+POR SOLICITUD BBVA+System.out..println("intIdPerfil: " + intIdPerfil);
		
		LOG.info("ID PERFIL: "+intIdPerfil);
		
		ProcesoUtil procesoUtil = new ProcesoUtil();
		
		try{
			EmpleadoCE empleadoCE = new EmpleadoCE();
			ViewNumeroExpedientesEmpleado viewNumeroExpedientesEmpleado = new ViewNumeroExpedientesEmpleado();
			
			ViewPesoDocumentoExpediente viewPesoDocumentoExpediente = new ViewPesoDocumentoExpediente();
	
			ViewPesoParticipeExpediente viewPesoParticipeExpediente = new ViewPesoParticipeExpediente();
			
			Integer intTipoBalanceo=0;
			
			PerfilBalanceo perfilBalanceo = new PerfilBalanceo();
			perfilBalanceo = perfilBalanceoDAO.findObtenerTipoBalanceo(intIdPerfil);
			intTipoBalanceo = perfilBalanceo.getIdTipoBalanceo();
			
			//+POR SOLICITUD BBVA+System.out..println("intTipoBalanceo: " + intTipoBalanceo);
			LOG.info("intTipoBalanceo: "+intTipoBalanceo);
			
			//Verificar sin existen empleados sin expedientes asignados		
			int intNumEmpleadosSinExpedientes;
			List<ViewNumeroExpedientesEmpleado> listEmpleadoSinExpedientes = viewNumeroExpedientesEmpleadoDAO.findListaEmpleadosSinExpedientes(intIdProducto, intIdTerritorio, intIdTarea);
			intNumEmpleadosSinExpedientes = listEmpleadoSinExpedientes.size();
			if (intNumEmpleadosSinExpedientes > 0){
				int indice = (int) Math.floor(Math.random()*(intNumEmpleadosSinExpedientes-1));
				viewNumeroExpedientesEmpleado = listEmpleadoSinExpedientes.get(indice);
				empleadoCE = procesoUtil.copiarEmpleadoCE(viewNumeroExpedientesEmpleado);
				//Asignar Carga de trabajo
				//+POR SOLICITUD BBVA+System.out..println("ObtenerEmpleado");
				//+POR SOLICITUD BBVA+System.out..println("intIdExpediente: " + intIdExpediente);
				//+POR SOLICITUD BBVA+System.out..println("empleadoCE.getIdEmpleado(): " + empleadoCE.getIdEmpleado());
				//+POR SOLICITUD BBVA+System.out..println("intIdTarea: " + intIdTarea);
				Integer resultado = procesoUtil.grabarEmpleadoExpedienteTareaProceso(intIdExpediente,empleadoCE.getIdEmpleado(),intIdTarea);
				return empleadoCE;
			}
			switch (intTipoBalanceo){
				//Balanceo Por Numero de Expedientes x Empleado
				case 1: 
					List<ViewNumeroExpedientesEmpleado> listnumExpedientesxEmpleado = viewNumeroExpedientesEmpleadoDAO.findListaNumeroExpedientesxEmpleado(intIdProducto, intIdTerritorio, intIdTarea);
					viewNumeroExpedientesEmpleado = listnumExpedientesxEmpleado.get(0);
				case 2:
					List<ViewPesoDocumentoExpediente> listViewPesoDocumentoExpediente = viewPesoDocumentoExpedienteDAO.findListaPesoDocumentoxExpediente(intIdProducto, intIdTerritorio, intIdTarea);
					viewPesoDocumentoExpediente = listViewPesoDocumentoExpediente.get(0);
					viewNumeroExpedientesEmpleado.setCodEmpleado(viewPesoDocumentoExpediente.getCodEmpleado());
					viewNumeroExpedientesEmpleado.setNomEmpleado(viewPesoDocumentoExpediente.getNomEmpleado());
					viewNumeroExpedientesEmpleado.setDesPerfil(viewPesoDocumentoExpediente.getDesPerfil());
					viewNumeroExpedientesEmpleado.setIdEmpleado(viewPesoDocumentoExpediente.getIdEmpleado());
					viewNumeroExpedientesEmpleado.setIdPerfil(viewPesoDocumentoExpediente.getIdPerfil());
					viewNumeroExpedientesEmpleado.setIdProducto(viewPesoDocumentoExpediente.getIdProducto());
					viewNumeroExpedientesEmpleado.setIdTarea(viewPesoDocumentoExpediente.getIdTarea());
					viewNumeroExpedientesEmpleado.setIdTerritorio(viewPesoDocumentoExpediente.getIdTerritorio());
					viewNumeroExpedientesEmpleado.setNumExpedientesEmpleado(viewPesoDocumentoExpediente.getSumPesoDocumentoExpediente());
				case 3:
					List<ViewPesoParticipeExpediente> listViewPesoParticipeExpediente = viewPesoParticipeExpedienteDAO.findListaPesoParticipesxExpediente(intIdProducto, intIdTerritorio, intIdTarea);
					viewPesoParticipeExpediente = listViewPesoParticipeExpediente.get(0);
					viewNumeroExpedientesEmpleado.setCodEmpleado(viewPesoParticipeExpediente.getCodEmpleado());
					viewNumeroExpedientesEmpleado.setNomEmpleado(viewPesoParticipeExpediente.getNomEmpleado());
					viewNumeroExpedientesEmpleado.setDesPerfil(viewPesoParticipeExpediente.getDesPerfil());
					viewNumeroExpedientesEmpleado.setIdEmpleado(viewPesoParticipeExpediente.getIdEmpleado());
					viewNumeroExpedientesEmpleado.setIdPerfil(viewPesoParticipeExpediente.getIdPerfil());
					viewNumeroExpedientesEmpleado.setIdProducto(viewPesoParticipeExpediente.getIdProducto());
					viewNumeroExpedientesEmpleado.setIdTarea(viewPesoParticipeExpediente.getIdTarea());
					viewNumeroExpedientesEmpleado.setIdTerritorio(viewPesoParticipeExpediente.getIdTerritorio());
					viewNumeroExpedientesEmpleado.setNumExpedientesEmpleado(viewPesoParticipeExpediente.getSumPesoParticipeExpediente());
			}
			empleadoCE = procesoUtil.copiarEmpleadoCE(viewNumeroExpedientesEmpleado);
			Integer resultado = procesoUtil.grabarEmpleadoExpedienteTareaProceso(intIdExpediente,empleadoCE.getIdEmpleado(),intIdTarea);
			return empleadoCE;
		} catch (Exception ex) {
			LOG.error("Error obteniendo empleado, se asigna el usuario al azar.", ex);
			try {
				List<Empleado> empleados = empleadoDAO.getEmpleadosCarterizacion(intIdProducto.intValue(), 
						intIdTerritorio.intValue(), intIdPerfil.intValue());
				EmpleadoCE empleadoCE = new EmpleadoCE();
				if (empleados.isEmpty()) {
					String userAdmin = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty("usuarioAdminProcess");
					LOG.warn("No se encontró usuario, se usará el usuario por defecto: "+userAdmin);
					empleadoCE.setCodEmpleado(userAdmin);
					empleadoCE.setNomEmpleado(userAdmin);
				} else {
					int numEmp = empleados.size();
					//+POR SOLICITUD BBVA+System.out..println("Numero empleados: "+numEmp);
					Random random = new Random ();
					Empleado empleado = empleados.get(random.nextInt(numEmp));
					
					empleadoCE.setCodEmpleado(empleado.getCodigo());
					empleadoCE.setDesPerfil(empleado.getPerfil().getDescripcion());
					empleadoCE.setIdEmpleado(empleado.getId());
					empleadoCE.setIdPerfil(intIdPerfil);
					empleadoCE.setIdProducto(intIdProducto);
					empleadoCE.setIdTarea(intIdTarea);
					empleadoCE.setIdTerritorio(intIdTerritorio);
					empleadoCE.setNomEmpleado(empleado.getNombres()+" "+empleado.getApepat()+" "+empleado.getApemat());
					empleadoCE.setNumExpedientesEmpleado(0);
					procesoUtil.obtenerDatosAdicionales(empleadoCE, empleado);
					Integer resultado = procesoUtil.grabarEmpleadoExpedienteTareaProceso(intIdExpediente,empleadoCE.getIdEmpleado(),intIdTarea);
					return empleadoCE;
				}
				return empleadoCE;
			} catch (Exception e) {
				String userAdmin = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty("usuarioAdminProcess");
				LOG.error("Error asignando usuario al azar, se usará el usuario por defecto: "+userAdmin, e);
				EmpleadoCE empleadoCE = new EmpleadoCE();
				empleadoCE.setCodEmpleado(userAdmin);
				empleadoCE.setNomEmpleado(userAdmin);
				return empleadoCE;
			}
		}
	}
	
	public Integer verificarCriterioSupervision(String strCodigoCentral, String strResultadoBastanteo){
		try{
			LOG.info("verificarCriterioSupervision(String strCodigoCentral, String strResultadoBastanteo)");
			LOG.info("strCodigoCentral : "+strCodigoCentral);
			LOG.info("strResultadoBastanteo : "+strResultadoBastanteo);
			Integer intverificarCriterioSupervision = 0;
			AuditoriaCriteriosSupervision auditoriaCriteriosSupervision = new AuditoriaCriteriosSupervision();
			
			auditoriaCriteriosSupervision = auditoriaCriteriosSupervisionDAO.findObtenerCriterioSupervision(Constantes.CS_SIN_AUDITORIA_BASTANTEO);
			LOG.info("auditoriaCriteriosSupervision.getIndicador() : "+auditoriaCriteriosSupervision.getIndicador());
			if (Constantes.VALOR_UNO.equals(auditoriaCriteriosSupervision.getIndicador())){
				intverificarCriterioSupervision = Constantes.VALOR_CERO;
			}else{
				auditoriaCriteriosSupervision = auditoriaCriteriosSupervisionDAO.findObtenerCriterioSupervision(Constantes.CS_BASTANTEO_COMPLETO);
				LOG.info("auditoriaCriteriosSupervision.getIndicador() : "+auditoriaCriteriosSupervision.getIndicador());
				if (Constantes.VALOR_UNO.equals(auditoriaCriteriosSupervision.getIndicador())){
					intverificarCriterioSupervision = Constantes.VALOR_UNO;
				}else{
					AuditoriaCriteriosSupervision auditoriaCriteriosCodCent = auditoriaCriteriosSupervisionDAO.
							findObtenerCriterioSupervision(Constantes.CS_CODIGO_CENTRAL);
					LOG.info("auditoriaCriteriosSupervision.getIndicador() : "+auditoriaCriteriosCodCent.getIndicador());
					AuditoriaCriteriosSupervision auditoriaCriteriosResBast = auditoriaCriteriosSupervisionDAO.
							findObtenerCriterioSupervision(Constantes.CS_RESULTADO_BASTANTEO);
					LOG.info("auditoriaCriteriosSupervision.getIndicador() : "+auditoriaCriteriosResBast.getIndicador());
					
					Integer intverificarCriterioCodCent = Constantes.VALOR_CERO;
					Integer intverificarCriterioResBast = Constantes.VALOR_CERO;
					
					if (Constantes.VALOR_UNO.equals(auditoriaCriteriosCodCent.getIndicador())) {
						if (auditoriaClienteDAO.findExisteCodigoCentral(strCodigoCentral)){
							intverificarCriterioCodCent = Constantes.VALOR_UNO;
						} else {
							intverificarCriterioCodCent = Constantes.VALOR_CERO;
						}
					}
					
					if (Constantes.VALOR_UNO.equals(auditoriaCriteriosResBast.getIndicador())) {
						if (auditoriaBastanteoDAO.findExisteResultadoBastanteo(strResultadoBastanteo)){
							intverificarCriterioResBast = Constantes.VALOR_UNO;
						}else{
							intverificarCriterioResBast = Constantes.VALOR_CERO;
						}
					}
					
					if (Constantes.VALOR_UNO.equals(intverificarCriterioCodCent) || 
							Constantes.VALOR_UNO.equals(intverificarCriterioResBast)) {
						intverificarCriterioSupervision = Constantes.VALOR_UNO;
					} else {
						intverificarCriterioSupervision = Constantes.VALOR_CERO;
					}
				}
			}
			LOG.info("Return : "+intverificarCriterioSupervision);
			return intverificarCriterioSupervision;
		} catch (Exception ex) {
			LOG.error("",ex);
			return null;
		}
	}
	
	public Integer activarFirmas(Integer intIdExpediente, String tipoOperacion, String usuarioResponsable){
		try{
			LOG.debug("activarFirmas(Integer intIdExpediente, String tipoOperacion, String usuarioResponsable)");
			Integer resultado = 0;
			Expediente expediente = new Expediente();
			expediente = expedienteDAO.load(intIdExpediente);
			
			/**********INICIO: FIX EXISTE FIRMA ASOCIADA MIGRACION**********/
			List<Participe> listParticipeUnicos = participeDAO.findByExpedienteParticipesUnicos(intIdExpediente);
			String existeFirmaAsociada = ConstantesBusiness.NO_EXISTE_FIRMA_ASOCIADA;
			for (Participe p : listParticipeUnicos) {
				if (ConstantesBusiness.FLAG_FIRMA_ASOCIADA.equals(p.getFlagFirmaAsociada())) {
					existeFirmaAsociada = ConstantesBusiness.EXISTE_FIRMA_ASOCIADA;
				}
			}
			LOG.info("FlagExisteFirmaAsociada: " + existeFirmaAsociada);
			if (existeFirmaAsociada.equals(ConstantesBusiness.NO_EXISTE_FIRMA_ASOCIADA)) {
				LOG.info("No se ejecuta la activación de firmas porque ningún partícipe tiene el flag de firma asociada.");
				return 0;
			}
			/**********FIN: FIX EXISTE FIRMA ASOCIADA MIGRACION**********/
			
			String resultadoBastanteo = expediente.getResultado();
			
			WSServicioSFPHttpSoap11EndpointProxy portType = new WSServicioSFPHttpSoap11EndpointProxy ();
			Properties properties = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF);
			// http://118.180.34.15:9080/SFPWS/services/WSServicioSFP.WSServicioSFPHttpSoap11Endpoint/
			LOG.debug("URL Servicio SFP: {}", properties.getProperty(ConstantesParametros.SERVICIO_SFP));
			String url = properties.getProperty(ConstantesParametros.SERVICIO_SFP);
			portType._getDescriptor().setEndpoint(url);
					
			//List<Participe> listParticipeUnicos = participeDAO.findByExpedienteParticipesUnicos(intIdExpediente);
			List<RpteActivacionBE> lstRpteActivacionBE = new ArrayList<RpteActivacionBE>();
			LOG.debug("***Metodo activarFirmas CEProcessService.java***");
			LOG.debug("NroExpediente: " + intIdExpediente);
			LOG.debug("TipoOperacion: " + tipoOperacion);
			LOG.debug("Usuario: " + usuarioResponsable);
			for (int j=0; j<listParticipeUnicos.size();j++){
				RpteActivacionBE rpteActivacion_BE = new RpteActivacionBE();
				Participe participeUnico = listParticipeUnicos.get(j);
				LOG.debug("participeUnico.getCodigoCentral(): " + participeUnico.getCodigoCentral());
				LOG.debug("participeUnico.getFlagFirmaAsociada().toString(): " + participeUnico.getFlagFirmaAsociada().toString());
				
				rpteActivacion_BE.setCodigoCentral(participeUnico.getCodigoCentral());
				rpteActivacion_BE.setDescripcionError("");
				rpteActivacion_BE.setIndicadorActivacion(participeUnico.getFlagFirmaAsociada().toString());
				lstRpteActivacionBE.add(rpteActivacion_BE);
			}
			
			String numeroExpediente = intIdExpediente.toString();
			//Poder_Firma_Activacion pfa = portType.activarPoderes(numeroExpediente, tipoOperacion, listaRepresentantes.toArray(new RpteActivacion_BE[0]), resultadoBastanteo, "P014773");
			Integer inttipoOperacion = Integer.parseInt(tipoOperacion.trim());
			String tipoOperacionNew = inttipoOperacion.toString();
			String resultadoBastanteoNew = "0";
			/*
			 * Cuando resultado bastanteo es nulo es porque esta en cambio de firma o revocatoria
			 * 
			 */
			if (resultadoBastanteo != null) {
				if (resultadoBastanteo.toUpperCase().compareToIgnoreCase(ConstantesBusiness.ACCION_EXPEDIENTE_APROBADO)==0){
					resultadoBastanteoNew = ConstantesBusiness.CODIGO_BASTANTEO_APROBADO;
				}
				if (resultadoBastanteo.toUpperCase().compareToIgnoreCase(ConstantesBusiness.ACCION_EXPEDIENTE_APROBADO_PARCIAL)==0){
					resultadoBastanteoNew = ConstantesBusiness.CODIGO_BASTANTEO_APROBADO_PARCIAL;
				}
				if (resultadoBastanteo.toUpperCase().compareToIgnoreCase(ConstantesBusiness.ACCION_EXPEDIENTE_OBSERVADO)==0){
					resultadoBastanteoNew = ConstantesBusiness.CODIGO_BASTANTEO_OBSERVADO;
				}
			}
			if (tipoOperacionNew.toUpperCase().compareToIgnoreCase(ConstantesBusiness.OPERACION_REVOCATORIA_IBM)==0){
				tipoOperacionNew = ConstantesBusiness.OPERACION_REVOCATORIA_SFP;
			}
			LOG.debug("ResultadoBastanteo: " + resultadoBastanteoNew);
			LOG.debug("tipoOperacionNew: " + tipoOperacionNew);
			LOG.debug("arrayRpteActivacion_BE[0].getCodigo_central(): " + lstRpteActivacionBE.get(0).getCodigoCentral());
			LOG.debug("Invocar al metodo de Activacion de Firmas y Poderes");
			//Poder_Firma_Activacion pfa = portType.activarPoderes(numeroExpediente, tipoOperacionNew, arrayRpteActivacion_BE, resultadoBastanteoNew, "P014773");
			PoderFirmaActivacion pfa = portType.activarPoderes(numeroExpediente, tipoOperacionNew, lstRpteActivacionBE, resultadoBastanteoNew, usuarioResponsable);
			//Poder_Firma_Activacion pfa = new Poder_Firma_Activacion();
			//pfa.setExito_activacion_poder(false);
			//pfa.setDsc_mensaje("01NO OK por error en Firmas");
			if (pfa!=null){
				LOG.debug("pfa.getExito_activacion_poder(): " + pfa.isExitoActivacionPoder());
				LOG.info("pfa.getDsc_mensaje(): " + pfa.getDscMensaje());
				if (pfa.isExitoActivacionPoder()){
					resultado = 1;
					List<Participe> listParticipe = participeDAO.findByExpedienteParticipes(intIdExpediente);
					Participe participe = new Participe();
					
					RpteActivacionBE rpteActivacionBE = new RpteActivacionBE();
					List<RpteActivacionBE> lista = pfa.getListaRepresentantesOutput();
					for(int k=0;k<lista.size();k++){
						rpteActivacionBE = lista.get(k);
						for(int i=0;i<listParticipe.size();i++){
							participe = listParticipe.get(i);
							if (rpteActivacionBE.getCodigoCentral().compareToIgnoreCase(participe.getCodigoCentral())==0){
								String flagFirmaActiva = rpteActivacionBE.getIndicadorActivacion();
								LOG.debug("rpteActivacionBE.getCodigo_central(): " + rpteActivacionBE.getCodigoCentral());
								LOG.debug("flagFirmaActiva[0]: " + flagFirmaActiva.substring(0, 1));
								participe.setFlagFirmaActiva(flagFirmaActiva.substring(0, 1));
								participeDAO.update(participe);
							}
						}
					}
				}else{
					LOG.info("Servicio Activacion False");
					String codTipoErrorActivacion ="";
					String detalleTipoErrorActivacion ="";
					String perfilSupervisor="";
					String asunto = ConstantesBusiness.ASUNTO_CORREO_ACTIVACION_FIRMAS;
					String codigoCliente=expediente.getCliente().getCodigoCentral();
					String mensaje = "";
					if (pfa.getDscMensaje()!=null){
						codTipoErrorActivacion = pfa.getDscMensaje().substring(0, 2);
						detalleTipoErrorActivacion = pfa.getDscMensaje().substring(2,pfa.getDscMensaje().length());
						
						if(codTipoErrorActivacion.equals(ConstantesBusiness.CODIGO_ERROR_POR_FIRMAS_SERVICIO_SFP)){
							perfilSupervisor= ConstantesBusiness.CODIGO_PERFIL_SUPERVISOR_FIRMA;
						}else if(codTipoErrorActivacion.equals(ConstantesBusiness.CODIGO_ERROR_POR_PODERES_SERVICIO_SFP)){
							perfilSupervisor= ConstantesBusiness.CODIGO_PERFIL_SUPERVISOR_ABOGADO_BASTANTEO;
						}else if(codTipoErrorActivacion.equals(ConstantesBusiness.CODIGO_ERROR_POR_SISTEMA_SERVICIO_SFP)){
							perfilSupervisor= ConstantesBusiness.CODIGO_PERFIL_ADMINISTRADOR;
						}
						LOG.info("perfilSupervisor: " + perfilSupervisor);
						List<Empleado> listEmp = empleadoDAO.getEmpleadosPorPerfil(Integer.parseInt(perfilSupervisor));
						List<InternetAddress> listAddresses = new ArrayList<InternetAddress> ();
						for (Empleado e : listEmp) {
							//Listar correos:  
							if ((e.getCorreo()!=null) || (e.getCorreo().trim()!="")){
								listAddresses.add(new InternetAddress(e.getCorreo()));
								LOG.info("e.getCorreo(): " + e.getCorreo());
							}
						}
						mensaje = "Expediente: " + intIdExpediente.toString() + " - Codigo Central: " + codigoCliente + " - Detalle: " + detalleTipoErrorActivacion;
						//*************Enviar Correo***********************
						//intIdExpediente
						ProcesoUtil procesoUtil = new ProcesoUtil();
						
						
						InternetAddress[] arrAddresses = listAddresses.toArray(new InternetAddress[0]);
						procesoUtil.enviarCorreo(asunto, arrAddresses, mensaje);
					}
				}
			}
			
			return resultado;
		} catch (Exception ex) {
			LOG.error("",ex);
			return 0;
		}
	}
	
	public String obtenerEstadoBastanteo(Integer intIdExpediente){
		String strEstadoBastanteo = "";
		try{
			Expediente expediente = new Expediente();
			expediente = expedienteDAO.load(intIdExpediente);
			if (expediente.getResultado()!=null)
				strEstadoBastanteo = expediente.getFlagIndicadorBastanteo().toString();
			return strEstadoBastanteo;
		
		}catch (Exception ex) {
			LOG.error("",ex);
			return strEstadoBastanteo;
		}
	}
	
	
	public CobroComision obtenerHoraReintento(Integer id){
		CobroComision cobroComision = new CobroComision();
		try{
			LOG.info("obtenerHoraReintento({})", id);
			cobroComision = cobroComisionDAO.findAll().get(0);
			LOG.info("obtenerHoraReintento({})", cobroComision);
			return cobroComision;
		}catch (Exception ex) {
			LOG.error("",ex);
			return cobroComision;
		}
	}
	
//	public String cobroComisionInmediato(String strIdExpediente,String codEmpleadoResponsable){
//		LOG.info("cobroComisionInmediato({},{})", strIdExpediente, codEmpleadoResponsable);
//		return "0";
//	}
		
	public String cobroComisionInmediato(String strIdExpediente,String codEmpleadoResponsable){
		String strCobroComision = "-2";
		try{
			LOG.debug("Servicio Cobro Comision (strIdExpediente): " + strIdExpediente);
			Expediente expediente = new Expediente();
			
			int idExpediente = Integer.parseInt(strIdExpediente.trim());
			expediente = expedienteDAO.load(idExpediente);
			String strcodCentralCliente = expediente.getCliente().getCodigoCentral();
			LOG.debug("strcodCentralCliente : " + strcodCentralCliente);
			String strnumCuenta = expediente.getNumeroCuentaCobro();
			LOG.debug("strnumCuenta : " + strnumCuenta);
//			/*Empleado empleado = new Empleado();
//			EmpleadoDAO empleadoDAO = DAOFactory.getInstance().getEmpleadoDAO();
//			empleado = empleadoDAO.findByCodigo(codEmpleadoResponsable);
//			String codOficina = empleado.getOficina().getCodigo();*/
//			
//			
//			/*Inicio Obtener Centro Contable*/
//			
			LOG.debug("codEmpleadoResponsable : " + codEmpleadoResponsable);
			pe.com.grupobbva.sce.qsp5.CtHeader rhCC = new pe.com.grupobbva.sce.qsp5.CtHeader();
			rhCC.setUsuario(codEmpleadoResponsable);
			LOG.debug("usuCC : " + codEmpleadoResponsable);
			pe.com.grupobbva.sce.qsp5.CtBodyRq rqCC = new pe.com.grupobbva.sce.qsp5.CtBodyRq();
			rqCC.setUsuario(codEmpleadoResponsable);
			LOG.debug("usuCC : " + codEmpleadoResponsable);
			CtInqPerfilUsuarioRq qCC = new CtInqPerfilUsuarioRq();
			qCC.setHeader(rhCC);
			qCC.setData(rqCC);
			LOG.debug("rhCC : " + rhCC);
			
			SCE_QSP5_PortProxy servCC = new SCE_QSP5_PortProxy();
			//http://118.180.36.26:7802/sce/qsp5/
			Properties properties = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF);
			String urlQSP5 = properties.getProperty(ConstantesParametros.SERVICIO_QSP5);
			servCC._getDescriptor().setEndpoint(urlQSP5);
			
			LOG.debug("codEmpleadoResponsable: " + codEmpleadoResponsable);
			LOG.debug("Invocando callQSP5");
			CtInqPerfilUsuarioRs sCC = servCC.callQSP5(qCC);
			LOG.debug("sCC.getHeader().getCodigo(): " + sCC.getHeader().getCodigo());
			LOG.debug("sCC.getHeader().getDescripcion(): " + sCC.getHeader().getDescripcion());
			String codOficina = sCC.getData().getOficOperativa();
			if (sCC.getHeader().getCodigo().compareTo(ConstantesBusiness.CODIGO_OK_SERVICIO_SCE_QSP5)==0){
			
				String strCentroCosto = "";
				LOG.debug("sCC : " + sCC);
				if (sCC!=null){
					strCentroCosto = sCC.getData().getOficOperativa();
					LOG.debug("strCentroCosto : " + strCentroCosto);
				}
				/*Fin Obtener Centro Contable*/
				LOG.debug("strCentroCosto: " + strCentroCosto);
				
				CtHeader rh = new CtHeader();
				rh.setUsuario(codEmpleadoResponsable);
				LOG.debug("Servicio Cobro Comision CtHeader (usu): " + codEmpleadoResponsable);
				
				CtBodyRq rq = new CtBodyRq();
				rq.setCentroContable(strCentroCosto);
				LOG.debug("Servicio Cobro Comision CtBodyRq (centroContable): " + strCentroCosto);
				rq.setCodCentralCliente(strcodCentralCliente);
				LOG.debug("Servicio Cobro Comision CtBodyRq (codCentralCliente): " + strcodCentralCliente);
				rq.setDescCargo(Constantes.DESC_CARGO_SERVICIO_COBRO_COMISION);
				LOG.debug("Servicio Cobro Comision CtBodyRq (descCargo): " + Constantes.DESC_CARGO_SERVICIO_COBRO_COMISION);
				rq.setNumCuenta(strnumCuenta);
				LOG.debug("Servicio Cobro Comision CtBodyRq (numCuenta): " + strnumCuenta);
				rq.setOficinaUsuario(codOficina);
				LOG.debug("Servicio Cobro Comision CtBodyRq (oficinaUsuario): " + codOficina);
				rq.setReferencia(strIdExpediente);
				LOG.debug("Servicio Cobro Comision CtBodyRq (referencia): " + strIdExpediente);
				//rq.setRegistroUsuario(registroUsuario);
				//LOG.debug("Servicio Cobro Comision CtBodyRq (registroUsuario): " + registroUsuario);
				rq.setRegistroUsuario(codEmpleadoResponsable);
				
				CtPagoServicioRq q = new CtPagoServicioRq();
				q.setHeader(rh);
				q.setData(rq);
				
				CtPagoServicioRs s = new CtPagoServicioRs();
				
				PagoServicio_PortProxy serv = new PagoServicio_PortProxy();
				//http://118.180.36.26:7820/accpj/pagser/
				String urlStr = properties.getProperty(ConstantesParametros.SERVICIO_PAGO_SERVICIO);
				LOG.info("accpj/pagser : "+urlStr);
				String urlPS = urlStr;
				serv._getDescriptor().setEndpoint(urlPS);
				
				s = serv.pagarServicio(q);
				
				strCobroComision = s.getHeader().getCodigo();
				//+POR SOLICITUD BBVA+System.out..println("Pago Servicio : "+strCobroComision);
				String descError = "";
				if (!Constantes.PAGO_SERVICIO_CORRECTO.equals(strCobroComision)){
					descError = s.getHeader().getDescripcion();
				}
				LOG.info("Servicio Cobro Comision (idExpediente): " + strIdExpediente);
				LOG.info("Servicio Cobro Comision (codigoError): " + strCobroComision);
				LOG.info("Servicio Cobro Comision (descError): " + descError);
			}
			return strCobroComision;
		
		}catch (Exception ex) {
			strCobroComision = Constantes.ERROR_COD_SERVICIO_COBRO_COMISION;
			LOG.error("Servicio Cobro Comision Error (idExpediente): " + strIdExpediente);
			LOG.error("Servicio Cobro Comision Error (codigoError): " + strCobroComision);
			LOG.error("Servicio Cobro Comision Error (descError ex): ",ex);
			return strCobroComision;
		}
	}
	
	public boolean crearPreRegistroSubsanacion (String codigoExpediente) {
		Expediente expediente = expedienteDAO.load(Integer.parseInt(codigoExpediente));
		Expediente expPreRegMod = new Expediente ();
		expPreRegMod.setEmpleado(expediente.getEmpleado());
		expPreRegMod.setProducto(expediente.getProducto());
		expPreRegMod.setSubproducto(expediente.getSubproducto());
		Date fechaActual = new Date ();
		expPreRegMod.setFechaRegistro(fechaActual);
		expPreRegMod.setFechaEnvio(fechaActual);
		expPreRegMod.setFlagCorreo(expediente.getFlagCorreo());
		expPreRegMod.setFlagSmstexto(expediente.getFlagSmstexto());
		expPreRegMod.setCuentaCobroComision(expediente.getCuentaCobroComision());
		expPreRegMod.setNumeroCuentaCobro(expediente.getNumeroCuentaCobro());
		expPreRegMod.setEstadoCuenta(expediente.getEstadoCuenta());
		expPreRegMod.setCampania(expediente.getCampania());
		expPreRegMod.setCliente(expediente.getCliente());
		expPreRegMod.setOficina(expediente.getOficina());
		expPreRegMod.setOperacion(operacionDAO.findByCodOperacion(ConstantesBusiness.CODIGO_SUBSANACION_BASTANTEO));
		expPreRegMod.setEstado(estadoExpedienteDAO.load(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_PREREGISTRO));
		expPreRegMod.setCodTipoPj(expediente.getCodTipoPj());
		expPreRegMod.setDesTipoPj(expediente.getDesTipoPj());
		expPreRegMod.setNumTerminal(expediente.getNumTerminal());
		
		// Toda Subsanación de Bastanteo deberá estar asociada a la modificatoria/nuevo bastanteo que fue observado y la originó
		expPreRegMod.setIdExpUltBastanteo(Integer.parseInt(codigoExpediente));
		
		ExpedienteTarea expedienteTarea = new ExpedienteTarea ();
		expedienteTarea.setExpediente(expPreRegMod);
		expedienteTarea.setEstadoTarea(estadoTareaDAO.load(ConstantesBusiness.ID_ESTADO_TAREA_PREREGISTRO));
		expedienteTarea.setTarea(tareaDAO.load(ConstantesBusiness.ID_TAREA_PRE_REGISTRO));
		HashSet<ExpedienteTarea> set = new HashSet<ExpedienteTarea>();
		set.add(expedienteTarea);
		expPreRegMod.setExpedienteTareas(set);
		expedienteDAO.save(expPreRegMod);
		LOG.debug("nuevo expediente - "+expPreRegMod.getId());
		return true;
	}
	
	public boolean crearPreRegistroRevocatoria (String codigoExpediente) {
		Expediente expediente = expedienteDAO.load(Integer.parseInt(codigoExpediente));
		Expediente expPreRegMod = new Expediente ();
		expPreRegMod.setEmpleado(expediente.getEmpleado());
		expPreRegMod.setSubproducto(expediente.getSubproducto());
		Date fechaActual = new Date ();
		expPreRegMod.setFechaRegistro(fechaActual);
		expPreRegMod.setFechaEnvio(fechaActual);
		expPreRegMod.setFlagCorreo(expediente.getFlagCorreo());
		expPreRegMod.setFlagSmstexto(expediente.getFlagSmstexto());
		expPreRegMod.setCuentaCobroComision(expediente.getCuentaCobroComision());
		expPreRegMod.setNumeroCuentaCobro(expediente.getNumeroCuentaCobro());
		expPreRegMod.setEstadoCuenta(expediente.getEstadoCuenta());
		expPreRegMod.setCampania(expediente.getCampania());
		expPreRegMod.setCliente(expediente.getCliente());
		expPreRegMod.setOficina(expediente.getOficina());
		expPreRegMod.setOperacion(operacionDAO.findByCodOperacion(ConstantesBusiness.CODIGO_MODIFICATORIA_BASTANTEO));
		expPreRegMod.setEstado(estadoExpedienteDAO.load(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_PREREGISTRO));
		expPreRegMod.setCodTipoPj(expediente.getCodTipoPj());
		expPreRegMod.setDesTipoPj(expediente.getDesTipoPj());
		expPreRegMod.setNumTerminal(expediente.getNumTerminal());
		
		ExpedienteTarea expedienteTarea = new ExpedienteTarea ();
		expedienteTarea.setExpediente(expPreRegMod);
		expedienteTarea.setEstadoTarea(estadoTareaDAO.load(ConstantesBusiness.ID_ESTADO_TAREA_PREREGISTRO));
		expedienteTarea.setTarea(tareaDAO.load(ConstantesBusiness.ID_TAREA_PRE_REGISTRO));
		HashSet<ExpedienteTarea> set = new HashSet<ExpedienteTarea>();
		set.add(expedienteTarea);
		expPreRegMod.setExpedienteTareas(set);
		expedienteDAO.save(expPreRegMod);
		LOG.debug("nuevo expediente - "+expPreRegMod.getId());
		return true;
	}
	
	public boolean EliminarEmpleadoExpedienteTareaProceso(Integer idExpediente, Integer idEmpleado, Integer idTarea){
		try{
			boolean resultado = true;
			ProcesoUtil procesoUtil = new ProcesoUtil();
			LOG.debug("EliminarEmpleadoExpedienteTareaProceso");
			LOG.debug("idExpediente: " + idExpediente);
			LOG.debug("idEmpleado: " + idEmpleado);
			LOG.debug("intIdTarea: " + idTarea);
			resultado = procesoUtil.EliminarEmpleadoExpedienteTareaProceso(idExpediente,idEmpleado,idTarea);
			return resultado;
		}catch (Exception ex) {
			return false;
		}
	}
	
	public String eliminarDocumentosCM(Integer idExpediente) {
		
		List <DocumentoExp> listdocumentoExp= documentoExpDAO.findByExpdiente(idExpediente);
		Documento[] documentos = listdocumentoExp.toArray(new Documento[listdocumentoExp.size()]);
		ClienteContent clienteContent = new ClienteContent(ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).getProperty(ConstantesParametros.SERVICIO_CONTENT));
		return clienteContent.eliminarDocumentosCM(documentos);
	}	
}
