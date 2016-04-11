package com.ibm.bbva.ctacte.controller.comun;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ConsultaCC;
import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.EstadoExpediente;
import com.ibm.bbva.ctacte.bean.EstadoTarea;
import com.ibm.bbva.ctacte.bean.EstudioAbogado;
import com.ibm.bbva.ctacte.bean.Oficina;
import com.ibm.bbva.ctacte.bean.Operacion;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.bean.Territorio;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractTablaMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.EstadoExpedienteDAO;
import com.ibm.bbva.ctacte.dao.EstadoTareaDAO;
import com.ibm.bbva.ctacte.dao.EstudioAbogadoDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.OficinaDAO;
import com.ibm.bbva.ctacte.dao.OperacionDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;
import com.ibm.bbva.ctacte.dao.TerritorioDAO;
import com.ibm.bbva.ctacte.dto.ExpedienteDTO;
import com.ibm.bbva.ctacte.enums.EnmTipoArchivo;
import com.ibm.bbva.ctacte.enums.EnumTextAlign;
import com.ibm.bbva.ctacte.enums.EnumTipoDatoJava;
import com.ibm.bbva.ctacte.enums.EnumVerticalAlign;
import com.ibm.bbva.ctacte.util.ColumnaDinamica;
import com.ibm.bbva.ctacte.util.ParametrosSistema;
import com.ibm.bbva.ctacte.util.ReporteDinamico;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.bbva.ctacte.vo.ReporteVO;
import com.ibm.bbva.ctacte.vo.TareaBandejaVO;

@ManagedBean (name="reporte")
@SessionScoped
public class ReporteMB extends AbstractTablaMBean {

	private static final long serialVersionUID = -5542744867434188121L;
	private static final String SUF_PENDIENTES = "_PD";
	private static final Logger LOG = LoggerFactory.getLogger(ReporteMB.class);
	private ReporteVO reporteVO;

	private ReporteVO reporteAnsVO;

	private List<Tarea> tareas;
	private List<EstadoTarea> estados;
	private List<EstadoExpediente> estadosExpediente;
	private List<Operacion> operaciones;
	private List<ExpedienteDTO> expedientes;
	private List<ExpedienteDTO> expedientesAns;
	private List<Oficina> oficinas;
	private List<Territorio> territorios;
	private List<SelectItem> oficinaItemsExp;
	private boolean mostrarTareaActual;
	private String totalFilas;
	private String totalFilasAns;

	private boolean activeExport;
	private boolean activeExportAns;

	@EJB
	private ExpedienteDAO expedienteDao;

	@EJB
	private EstadoExpedienteDAO estadoExpedienteDAO;

	@EJB
	private TareaDAO tareaDAO;

	@EJB
	private EstadoTareaDAO estadoTareaDAO;

	@EJB
	private OperacionDAO operacionDAO;


	@EJB
	private EmpleadoDAO empleadoDAO;

	@EJB
	private TerritorioDAO TerritorioDAO;

	@EJB
	private EstudioAbogadoDAO estudioAbogadoDAO;

	@EJB
	private OficinaDAO	oficinaDAO;

	@PostConstruct
	public void iniciar() {
		LOG.info("{iniciar}");
		inicializarFiltros();
	}

	private void inicializarFiltros() {
		reporteAnsVO = new ReporteVO();
		expedientesAns = new ArrayList<ExpedienteDTO>();
		expedientes = new ArrayList<ExpedienteDTO>();
		expedientes.clear();
		LOG.info("{inicializarFiltros()}");
		reporteVO = new ReporteVO();
		tareas = tareaDAO.findAll();
		estados = estadoTareaDAO.findAll();
		operaciones = operacionDAO.findAll();
		estadosExpediente = estadoExpedienteDAO.findAll();
		oficinas = oficinaDAO.findByIdTerritorio(0);
		territorios = TerritorioDAO.findAllOrderedByCodigo();
		activeExport = true;
		mostrarTareaActual =true;
		totalFilas="";

		this.oficinaItemsExp = Util.crearItems(
				oficinas, false, "id",
				"codigo,descripcion", "{0} {1}", true);

	}

	public void abilitarTareaActual(AjaxBehaviorEvent event){

		if((Integer)((UIOutput)event.getSource()).getValue()==2){
			mostrarTareaActual =false;
			reporteVO.setIdTarea(0);
		}else{
			reporteVO.setIdTarea(0);
			mostrarTareaActual =true;
		}
	}

	public void buscar(){
		LOG.info("{buscar()}");
		//debe buscar lo siguiente: codigo de expedente, codigo central cliente, codigo oficina, ogicina, rpo operacion, persona juridida, caso de negocio
		//estado, fecha vastanteo, fecha dictamen , fecha cierre firmals, fecja cierre expedente, morivo obsercado documentos, OBSERVADO bastanteo, ovserado firmas.

		String inQuery  = "";
		if(reporteVO.getIdEstadoExpediente()==2){
			inQuery = getAllDataByFilterPORCESS(reporteVO);
			if(!inQuery.isEmpty()){
				List<ExpedienteDTO> expedienteDBTMP = getAllDataByFilterBD(reporteVO, inQuery);
				expedientes.clear();
				expedientes.addAll(expedienteDBTMP);
			}else{
				expedientes.clear();
			}
		}else{
			List<ExpedienteDTO> expedienteDBTMP = getAllDataByFilterBD(reporteVO, inQuery);
			expedientes.clear();
			expedientes.addAll(expedienteDBTMP);
		}

		if(!expedientes.isEmpty()){
			activeExport = false;
		}else{
			activeExport = true;
		}

		//		if( expedientes.size() ==0){
		//			totalFilas = "No se descargará el excel ya que se encontraron " + expedientes.size()+ " registros.";
		//		}else{			
		//			totalFilas = expedientes.size()+ " registros encontrados.";
		exportar();

		//		}

	}

	private List<ExpedienteDTO> getAllDataByFilterBD(ReporteVO vo, String inQuery) {
		List<ExpedienteDTO> list = new ArrayList<ExpedienteDTO>();
		return expedienteDao.getAllDataByFilterBD(vo, inQuery);
	}

	private String getAllDataByFilterPORCESS(ReporteVO vo) {
		String expedientes = "";
		List<ExpedienteDTO> list = new ArrayList<ExpedienteDTO>();


		List<TareaBandejaVO> tareas = new ArrayList<TareaBandejaVO>();

		ConsultaCC consulta = new ConsultaCC();
		TareaBandejaVO filtro;

		consulta.setConsiderarUsuarios(false);
		filtro =  new TareaBandejaVO();
		filtro.setCodEstadoTarea(String.valueOf(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE));
		filtro.setCodTarea(reporteVO.getIdTarea()==0 ? "" : String.valueOf(reporteVO.getIdTarea()));
		filtro.setCodCentral(reporteVO.getCodigoCentral());
		filtro.setOperacion(String.valueOf(reporteVO.getIdOperacion()));
		filtro.setRazSoc(reporteVO.getRazonSocial());
		filtro.setGestor(reporteVO.getResponsable());
		filtro.setOficinaExp(String.valueOf(reporteVO.getIdOficina()));
		filtro.setExpediente(reporteVO.getNumeroExpediente());
		filtroProcess(filtro, consulta);
		tareas = cargaRegProcess(consulta);

		if(!tareas.isEmpty()){
			expedientes = expedientes +  "( ";
			for(TareaBandejaVO tarea : tareas){
				expedientes = expedientes +  tarea.getExpediente()+", ";
			}
			expedientes = expedientes.substring(0,expedientes.length()-2);
			expedientes = expedientes +" )";
		}


		return expedientes;
	}

	private List<TareaBandejaVO> cargaRegProcess(ConsultaCC consulta) {
		List<TareaBandejaVO> lista = new ArrayList<TareaBandejaVO>();
		LOG.info("*********************cargaRegProcess(ConsultaCC consulta)*********************");
		RemoteUtils bandejaTareasBDelegate = new RemoteUtils();
		List<ExpedienteCC> expedientes = null;

		try {
			expedientes = bandejaTareasBDelegate.obtenerInstanciasTareasPorUsuarioCC(consulta);
		} catch (RuntimeException e) {
			LOG.error("Error al obtener las tareas", e);
			expedientes = Collections.EMPTY_LIST;
		}
		LOG.info("Expedientes : {}", expedientes.size());
		for (ExpedienteCC expedienteCC : expedientes) {
			TareaBandejaVO tareaBandeja = crearTareaBandejaVO(expedienteCC); // al crear tareabandeja llena la fechAsignacion
			//SEQ_CE_IBM_TAREAS_REASIG_CC+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("tareaBandeja.getCodSemaforo() "	+ tareaBandeja.getCodSemaforo());
			lista.add(tareaBandeja);
		}


		LOG.info("listaTareas Process: " + lista.size());
		return lista;
	}

	public void limpiar(){
		inicializarFiltros();	
	}


	public void filtroProcess(TareaBandejaVO filtro,ConsultaCC  consulta ) {

		// consulta.setIdEstadoExpediente(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO+"");
		LOG.info("*********************BandejaMB-filtroProcess()**************************");
		LOG.info("*********************consulta.getIdEstadoExpediente()**********"+consulta.getIdEstadoExpediente());
		if (!(filtro.getExpediente() == null || filtro.getExpediente().equals(""))) {
			LOG.info("f1- filtro.getExpediente(): " + filtro.getExpediente());
			consulta.setCodigoExpediente(filtro.getExpediente());
		}
		if (!(filtro.getCodEstadoExp() == null || filtro.getCodEstadoExp().equals("0"))) {
			LOG.info("f2-filtro.getCodEstadoExp(): " + filtro.getCodEstadoExp());
			consulta.setIdEstadoExpediente(filtro.getCodEstadoExp());
		}
		if (!(filtro.getCodEstadoTarea() == null || filtro.getCodEstadoTarea().equals("0"))) {
			LOG.info("f3- filtro.getCodEstadoTarea(): " + filtro.getCodEstadoTarea());
			consulta.setIdEstadoTarea(filtro.getCodEstadoTarea());
		}
		if (!(filtro.getCodTarea() == null || filtro.getCodTarea().equals(""))) {
			LOG.info("f4-filtro.getCodTarea(): " + filtro.getCodTarea());
			consulta.setNumeroTarea(filtro.getCodTarea());
		}
		if (!(filtro.getNomTarea() == null || filtro.getNomTarea().equals("0"))) {
			LOG.info("f5-filtro.getNomTarea(): " + filtro.getNomTarea());
			consulta.setNombreTarea(filtro.getNomTarea());
		}
		if (!(filtro.getCodCentral() == null || filtro.getCodCentral().equals(""))) {
			LOG.info("f6-filtro.getCodCentral(): " + filtro.getCodCentral());
			consulta.setCodCentralCliente(filtro.getCodCentral());
		}
		if (!(filtro.getOperacion() == null || filtro.getOperacion().equals("0"))) {
			LOG.info("f7-filtro.getOperacion(): " + filtro.getOperacion());
			consulta.setIdOperacion(filtro.getOperacion());
		}
		if (!(filtro.getNumDoi() == null || filtro.getNumDoi().equals(""))) {
			LOG.info("f8-filtro.getNumDoi(): " + filtro.getNumDoi());
			consulta.setNumDOICliente(filtro.getNumDoi());
		}
		if (!(filtro.getRazSoc() == null || filtro.getRazSoc().equals(""))) {
			LOG.info("f9-filtro.getRazSoc(): " + filtro.getRazSoc());
			consulta.setRazonSocialCliente(filtro.getRazSoc());
		}
		if (!(filtro.getResponsable() == null || filtro.getResponsable().equals(""))) {
			LOG.info("f10-filtro.getResponsable(): " + filtro.getResponsable());
			consulta.setCodUsuarioActual(filtro.getResponsable());
		}
		if (!(filtro.getOficinaExp() == null || filtro.getOficinaExp().equals("0"))) {
			LOG.info("f11-filtro.getOficinaExp(): " + filtro.getOficinaExp());
			consulta.setCodOficina(filtro.getOficinaExp());
		}
		if (!(filtro.getTerritorioExp() == null || filtro.getTerritorioExp().equals("0"))) {
			LOG.info("f12-filtro.getTerritorioExp(): " + filtro.getTerritorioExp());
			consulta.setIdTerritorio(filtro.getTerritorioExp());
		}
		if (!(filtro.getAbogadoEstudio() == null|| filtro.getAbogadoEstudio().equals("0") || filtro.getAbogadoEstudio().equals(""))) {
			LOG.info("f13-filtro.getAbogadoEstudio(): " + filtro.getAbogadoEstudio());
			consulta.setCodUsuarioAbogado(filtro.getAbogadoEstudio());
		}
		if (!(filtro.getGestor() == null || filtro.getGestor().equals(""))) {
			LOG.info("f14-filtro.getGestor(): " + filtro.getGestor());
			consulta.setCodUsuarioResponsable(filtro.getGestor());
		}
		if (!(filtro.getEstudio() == null || filtro.getEstudio().equals("0") || filtro.getEstudio().equals(""))) {
			LOG.info("f15-filtro.getEstudio(): " + filtro.getEstudio());
			consulta.setCodEstudioAbogado(filtro.getEstudio());
		}

		if (!(filtro.getStrFechaAsignacion().getLimInf() == null || filtro
				.getStrFechaAsignacion().getLimInf().equals(""))) {
			LOG.info("f16: "
					+ filtro.getStrFechaAsignacion().getLimInf());
			consulta.setFechaAsignacionInf(filtro.getStrFechaAsignacion()
					.getLimInf());
		}
		if (!(filtro.getStrFechaAsignacion().getLimSup() == null || filtro
				.getStrFechaAsignacion().getLimSup().equals(""))) {
			LOG.info("f17: "
					+ filtro.getStrFechaAsignacion().getLimSup());
			consulta.setFechaAsignacionSup(filtro.getStrFechaAsignacion()
					.getLimSup());
		}

		if (!(filtro.getStrFechaInicio().getLimInf() == null || filtro
				.getStrFechaInicio().getLimInf().equals(""))) {
			LOG.info("f18: " + filtro.getStrFechaInicio().getLimInf());
			consulta.setFechaInicioInf(filtro.getStrFechaInicio().getLimInf());
		}
		if (!(filtro.getStrFechaInicio().getLimSup() == null || filtro
				.getStrFechaInicio().getLimSup().equals(""))) {
			LOG.info("f19: " + filtro.getStrFechaInicio().getLimSup());
			consulta.setFechaInicioSup(filtro.getStrFechaInicio().getLimSup());
		}		
		if (!(filtro.getOficinaTarea() == null || filtro.getOficinaTarea()
				.equals("0"))) {
			LOG.info("f20: " + filtro.getOficinaTarea());
			consulta.setCodOficinaTarea(filtro.getOficinaTarea());
		}
		if (!(filtro.getTerritorioTarea() == null || filtro
				.getTerritorioTarea().equals("0"))) {
			LOG.info("f21: " + filtro.getTerritorioTarea());
			consulta.setIdTerritorioTarea(filtro.getTerritorioTarea());
		}
	}


	public void exportar(){

		try {
			String rutaPlantilla = (String) ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).get(ConstantesParametros.RUTA_PLANTILLA_REPORTE);
			ReporteDinamico reporte = new ReporteDinamico();
			URL imgURL =  ReporteMB.class.getResource("/imagenes/logo.gif");
			//            URL imgURLPlantilla =  ReporteMB.class.getResource("/imagenes");
			reporte.instanciarReporte("REPORTE DE EXPEDIENTES", "EXPEDIENTES DE BASTANTEO", "Lista de expedientes", rutaPlantilla, "EXPEDIENTES",1100, imgURL, "templateExcelH");
			reporte.setTipo(EnmTipoArchivo.Excel.getTipoArchivo());
			reporte.insertarColumnas(obtenerColumnas());
			if(expedientes.size()==0){
				ExpedienteDTO exp =  new ExpedienteDTO();
				exp.setIdExpediente("Sin registros.");
				expedientes.add(exp);
			}
			String ExcelGenerado = reporte.generarReporte(expedientes);
			String contextPath = "/" + FacesContext.getCurrentInstance().getExternalContext().getContextName() + "/ServletReport?method=downloadExcel&delete=true&file=";
			FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + rutaPlantilla + File.separator + ExcelGenerado + ".xlsx");
			//            String myUrl = "http://localhost:9081/BBVA_CTACTE_GUI/ServletReport?method=downloadExcel&delete=true&file=" +rutaPlantilla + File.separator +ExcelGenerado+ ".xlsx";
			//            String results = doHttpUrlConnectionAction(myUrl);
			//			LOG.info(results);
		} catch (Exception ex) {
			LOG.error("", ex);
		}


		LOG.info("{exportar()}");
	}



	private List<ColumnaDinamica> obtenerColumnas() {

		List<ColumnaDinamica> columnas = new ArrayList<ColumnaDinamica>();
		ColumnaDinamica a = new ColumnaDinamica();
		a.setNombreAtributo("idExpediente");
		a.instanciarColumna("EXPEDIENTE", EnumTipoDatoJava.String, "");
		a.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		a.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		a.setDimencion(20, 10);
		a.setInicioFinal(0, 0);
		columnas.add(a);

		ColumnaDinamica b= new ColumnaDinamica();
		b.setNombreAtributo("fechaRegistro");
		b.instanciarColumna("FECHA REGISTRO", EnumTipoDatoJava.String, "");
		b.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		b.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		b.setDimencion(20, 10);
		b.setInicioFinal(0, 0);
		columnas.add(b);

		ColumnaDinamica c= new ColumnaDinamica();
		c.setNombreAtributo("codigoCentral");
		c.instanciarColumna("CODIGO CENTRAL", EnumTipoDatoJava.String, "");
		c.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		c.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		c.setDimencion(20, 10);
		c.setInicioFinal(0, 0);
		columnas.add(c);


		ColumnaDinamica k= new ColumnaDinamica();
		k.setNombreAtributo("razonSocial");
		k.instanciarColumna("RAZON SOCIAL", EnumTipoDatoJava.String, "");
		k.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		k.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		k.setDimencion(20, 20);
		k.setInicioFinal(0, 0);
		columnas.add(k);


		ColumnaDinamica d= new ColumnaDinamica();
		d.setNombreAtributo("codigoOficina");
		d.instanciarColumna("COD OFICINA", EnumTipoDatoJava.String, "");
		d.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		d.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		d.setDimencion(20, 10);
		d.setInicioFinal(0, 0);
		columnas.add(d);

		ColumnaDinamica e= new ColumnaDinamica();
		e.setNombreAtributo("descripcionOficina");
		e.instanciarColumna("OFICINA", EnumTipoDatoJava.String, "");
		e.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		e.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		e.setDimencion(20, 20);
		e.setInicioFinal(0, 0);
		columnas.add(e);

		ColumnaDinamica f= new ColumnaDinamica();
		f.setNombreAtributo("codigoTerritorio");
		f.instanciarColumna("COD TERRITORIO", EnumTipoDatoJava.String, "");
		f.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		f.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		f.setDimencion(20, 10);
		f.setInicioFinal(0, 0);
		columnas.add(f);

		ColumnaDinamica g= new ColumnaDinamica();
		g.setNombreAtributo("descripcionTerritorio");
		g.instanciarColumna("TERRITORIO", EnumTipoDatoJava.String, "");
		g.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		g.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		g.setDimencion(20, 20);
		g.setInicioFinal(0, 0);
		columnas.add(g);


		ColumnaDinamica h= new ColumnaDinamica();
		h.setNombreAtributo("operacion");
		h.instanciarColumna("OPERACION", EnumTipoDatoJava.String, "");
		h.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		h.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		h.setDimencion(20, 15);
		h.setInicioFinal(0, 0);
		columnas.add(h);


		ColumnaDinamica a4= new ColumnaDinamica();
		a4.setNombreAtributo("casoNegocio");
		a4.instanciarColumna("CASO USO NEG.", EnumTipoDatoJava.String, "");
		a4.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		a4.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		a4.setDimencion(20, 15);
		a4.setInicioFinal(0, 0);
		columnas.add(a4);

		ColumnaDinamica i= new ColumnaDinamica();
		i.setNombreAtributo("gestor");
		i.instanciarColumna("RESPONSABLE", EnumTipoDatoJava.String, "");
		i.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		i.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		i.setDimencion(20, 15);
		i.setInicioFinal(0, 0);
		columnas.add(i);

		ColumnaDinamica j= new ColumnaDinamica();
		j.setNombreAtributo("nombresCompletos");
		j.instanciarColumna("NOMBRE RESPONSABLE", EnumTipoDatoJava.String, "");
		j.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		j.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		j.setDimencion(20, 15);
		j.setInicioFinal(0, 0);
		columnas.add(j);


		ColumnaDinamica l= new ColumnaDinamica();
		l.setNombreAtributo("estadoExpediente");
		l.instanciarColumna("ESTADO EXPEDIENTE", EnumTipoDatoJava.String, "");
		l.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		l.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		l.setDimencion(20, 15);
		l.setInicioFinal(0, 0);
		columnas.add(l);

		ColumnaDinamica m= new ColumnaDinamica();
		m.setNombreAtributo("codigoPJ");
		m.instanciarColumna("TIPO PJ", EnumTipoDatoJava.String, "");
		m.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		m.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		m.setDimencion(20, 5);
		m.setInicioFinal(0, 0);
		columnas.add(m);


		ColumnaDinamica n= new ColumnaDinamica();
		n.setNombreAtributo("descripcionTipoPJ");
		n.instanciarColumna("DES. TIPO PJ", EnumTipoDatoJava.String, "");
		n.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		n.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		n.setDimencion(20, 15);
		n.setInicioFinal(0, 0);
		columnas.add(n);


		ColumnaDinamica o= new ColumnaDinamica();
		o.setNombreAtributo("migrado");
		o.instanciarColumna("MIGRADO", EnumTipoDatoJava.String, "");
		o.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		o.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		o.setDimencion(20, 10);
		o.setInicioFinal(0, 0);
		columnas.add(o);

		ColumnaDinamica p= new ColumnaDinamica();
		p.setNombreAtributo("exonerado");
		p.instanciarColumna("EXONERADO COMISION", EnumTipoDatoJava.String, "");
		p.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		p.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		p.setDimencion(20, 10);
		p.setInicioFinal(0, 0);
		columnas.add(p);

		ColumnaDinamica q= new ColumnaDinamica();
		q.setNombreAtributo("resultadoBastanteo");
		q.instanciarColumna("RESULTADO BASTANTEO", EnumTipoDatoJava.String, "");
		q.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		q.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		q.setDimencion(20, 20);
		q.setInicioFinal(0, 0);
		columnas.add(q);

		ColumnaDinamica r= new ColumnaDinamica();
		r.setNombreAtributo("dictamenBastanteo");
		r.instanciarColumna("DICTAMEN BASTANTEO", EnumTipoDatoJava.String, "");
		r.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		r.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		r.setDimencion(20, 40);
		r.setInicioFinal(0, 0);
		columnas.add(r);

		ColumnaDinamica s= new ColumnaDinamica();
		s.setNombreAtributo("fechaMesaDocumentoAprobado");
		s.instanciarColumna("FECHA MESA DOCUMENTO", EnumTipoDatoJava.String, "");
		s.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		s.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		s.setDimencion(20, 20);
		s.setInicioFinal(0, 0);
		columnas.add(s);

		ColumnaDinamica t= new ColumnaDinamica();
		t.setNombreAtributo("fechaMesaFirmasAprobado");
		t.instanciarColumna("FECHA MESA FIRMAS", EnumTipoDatoJava.String, "");
		t.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		t.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		t.setDimencion(20, 20);
		t.setInicioFinal(0, 0);
		columnas.add(t);

		ColumnaDinamica w= new ColumnaDinamica();
		w.setNombreAtributo("fechaAbogadoBastanteo");
		w.instanciarColumna("FECHA ABOGADO BASTANTEO", EnumTipoDatoJava.String, "");
		w.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		w.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		w.setDimencion(20, 20);
		w.setInicioFinal(0, 0);
		columnas.add(w);

		ColumnaDinamica a1= new ColumnaDinamica();
		a1.setNombreAtributo("fechaVerifResultadoTramite");
		a1.instanciarColumna("FECHA VERIFICAR RESULTADO TRAMITE", EnumTipoDatoJava.String, "");
		a1.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		a1.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		a1.setDimencion(20, 20);
		a1.setInicioFinal(0, 0);
		columnas.add(a1);

		ColumnaDinamica y= new ColumnaDinamica();
		y.setNombreAtributo("flagObservadoMesaDocumentos");
		y.instanciarColumna("OBSERVADO POR MESA DOCUMENTO", EnumTipoDatoJava.String, "");
		y.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		y.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		y.setDimencion(20, 20);
		y.setInicioFinal(0, 0);
		columnas.add(y);

		ColumnaDinamica z= new ColumnaDinamica();
		z.setNombreAtributo("flagObservadoMesaFirmas");
		z.instanciarColumna("OBSERVADO POR FIRMAS", EnumTipoDatoJava.String, "");
		z.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		z.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		z.setDimencion(20, 20);
		z.setInicioFinal(0, 0);
		columnas.add(z);

		ColumnaDinamica z3= new ColumnaDinamica();
		z3.setNombreAtributo("flagCierreAutomatico");
		z3.instanciarColumna("CERRADO AUTOMATICAMENTE", EnumTipoDatoJava.String, "");
		z3.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		z3.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		z3.setDimencion(20, 20);
		z3.setInicioFinal(0, 0);
		columnas.add(z3);


		return columnas;
	}





	private TareaBandejaVO crearTareaBandejaVO(ExpedienteCC expedienteCC) {
		TareaBandejaVO tareaBandeja = new TareaBandejaVO();
		tareaBandeja.setExpedienteCC(expedienteCC);
		tareaBandeja.setId(expedienteCC.getCodigoExpediente()
				+ expedienteCC.getDatosFlujoCtaCte().getIdTarea()
				+ SUF_PENDIENTES);
		tareaBandeja.setTerritorioExp(expedienteCC.getDesTerritorio());
		tareaBandeja.setCodTarea(expedienteCC.getDatosFlujoCtaCte()
				.getIdTarea());
		tareaBandeja.getStrFechaAsignacion().setTarget(
				expedienteCC.getActivado().getTime());
		// tareaBandeja.getStrFechaAtencion().setTarget(expedienteCC.getFechaAtencion());
		tareaBandeja.getStrFechaInicio().setTarget(
				expedienteCC.getFechaRegistro());// fecha en la q se registro el
		// expediente
		// tareaBandeja.getStrFechaTermino().setTarget(Util.parseStringToDate("24/05/2012",
		// "dd/MM/yyyy"));
		// tareaBandeja.setCodSemaforo("verde.png");
		tareaBandeja.setDscCliente(expedienteCC.getRazonSocialCliente());
		tareaBandeja.setDscEstadoTarea(expedienteCC.getEstadoTarea());
		tareaBandeja.setDscEstadoExp(expedienteCC.getEstadoExpediente());
		tareaBandeja.setExpediente(expedienteCC.getCodigoExpediente());
		tareaBandeja.setNomTarea(expedienteCC.getDatosFlujoCtaCte()
				.getNombreTarea());
		tareaBandeja.setOficinaExp(expedienteCC.getDesOficina());
		tareaBandeja.setOperacion(expedienteCC.getDesOperacion());
		tareaBandeja.setResponsable(expedienteCC.getNomUsuarioActual());
		tareaBandeja.setCodResponsable(expedienteCC.getCodUsuarioActual());
		tareaBandeja.setExpedienteCC(expedienteCC);
		tareaBandeja.setTerritorioTarea(expedienteCC.getDesTerritorio());
		tareaBandeja.setOficinaTarea(expedienteCC.getDesOficina());

		if (expedienteCC.getDatosFlujoCtaCte().getIdTarea().equals("12") ||
				expedienteCC.getDatosFlujoCtaCte().getIdTarea().equals("13") ||
				expedienteCC.getDatosFlujoCtaCte().getIdTarea().equals("14")) {

			Empleado empAB = empleadoDAO.findByCodigo(expedienteCC.getCodUsuarioActual());
			if (empAB != null && empAB.getEstudio() != null) {
				EstudioAbogado estudioDAO = estudioAbogadoDAO.findByIdEstudio(empAB.getEstudio().getId());
				tareaBandeja.setEstudio(estudioDAO.getDescripcion().trim());
				tareaBandeja.setAbogadoEstudio(empAB.getNombres().trim()+" "+empAB.getApepat().trim()+" "+empAB.getApemat().trim());
			}
		}else{
			tareaBandeja.setAbogadoEstudio(expedienteCC.getDatosFlujoCtaCte().getNomUsuarioAbogado());
			tareaBandeja.setEstudio(expedienteCC.getDatosFlujoCtaCte().getNomEstudioAbogado());
		}		

		return tareaBandeja;
	}

	/**
	 * Reporte de ANS
	 * method buscarAns()
	 * method limpiarAns()
	 * method exportarAns()
	 * @param event
	 */

	public void buscarAns(){
		expedientesAns =  expedienteDao.getReporteAns(reporteAnsVO);
		exportarAns();
	}

	public void limpiarAns(){
		inicializarFiltros();
	}

	public void exportarAns(){

		try {
			String rutaPlantilla = (String) ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).get(ConstantesParametros.RUTA_PLANTILLA_REPORTE);
			ReporteDinamico reporte = new ReporteDinamico();
			URL imgURL =  ReporteMB.class.getResource("/imagenes/logo.gif");
			reporte.instanciarReporte("REPORTE DE ANS", "REPORTE DE ANS POR TAREA", "Lista de tareas", rutaPlantilla, "ANS" ,1100, imgURL, "templateExcelH");
			reporte.setTipo(EnmTipoArchivo.Excel.getTipoArchivo());
			reporte.insertarColumnas(obtenerColumnasAns());
			if(expedientesAns.size()==0){
				ExpedienteDTO exp =  new ExpedienteDTO();
				exp.setIdExpediente("Sin registros.");
				expedientesAns.add(exp);
			}
			String ExcelGenerado = reporte.generarReporte(expedientesAns);
			String contextPath = "/" + FacesContext.getCurrentInstance().getExternalContext().getContextName() + "/ServletReport?method=downloadExcel&delete=true&file=";
			FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + rutaPlantilla + File.separator + ExcelGenerado + ".xlsx");
			//	            String myUrl = "http://localhost:9081/BBVA_CTACTE_GUI/ServletReport?method=downloadExcel&delete=true&file=" +rutaPlantilla + File.separator +ExcelGenerado+ ".xlsx";
			//	            String results = doHttpUrlConnectionAction(myUrl);
			//				LOG.info(results);
		} catch (Exception ex) {
			LOG.error("", ex);
		}


		LOG.info("{exportar()}");
	}

	private List<ColumnaDinamica> obtenerColumnasAns() {
		List<ColumnaDinamica> columnas = new ArrayList<ColumnaDinamica>();
		ColumnaDinamica a = new ColumnaDinamica();
		a.setNombreAtributo("idExpediente");
		a.instanciarColumna("EXPEDIENTE", EnumTipoDatoJava.String, "");
		a.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		a.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		a.setDimencion(20, 10);
		a.setInicioFinal(0, 0);
		columnas.add(a);



		ColumnaDinamica c= new ColumnaDinamica();
		c.setNombreAtributo("codigoCentral");
		c.instanciarColumna("CODIGO CENTRAL", EnumTipoDatoJava.String, "");
		c.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		c.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		c.setDimencion(20, 10);
		c.setInicioFinal(0, 0);
		columnas.add(c);


		ColumnaDinamica k= new ColumnaDinamica();
		k.setNombreAtributo("razonSocial");
		k.instanciarColumna("RAZON SOCIAL", EnumTipoDatoJava.String, "");
		k.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		k.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		k.setDimencion(20, 20);
		k.setInicioFinal(0, 0);
		columnas.add(k);

		ColumnaDinamica f= new ColumnaDinamica();
		f.setNombreAtributo("tareaDescripcion");
		f.instanciarColumna("TAREA", EnumTipoDatoJava.String, "");
		f.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		f.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		f.setDimencion(20, 25);
		f.setInicioFinal(0, 0);
		columnas.add(f);

		ColumnaDinamica d= new ColumnaDinamica();
		d.setNombreAtributo("gestor");
		d.instanciarColumna("GESTOR", EnumTipoDatoJava.String, "");
		d.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		d.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		d.setDimencion(20, 10);
		d.setInicioFinal(0, 0);
		columnas.add(d);

		ColumnaDinamica e= new ColumnaDinamica();
		e.setNombreAtributo("nombresCompletos");
		e.instanciarColumna("NOMBRE GESTOR", EnumTipoDatoJava.String, "");
		e.setAlineacionInterior(EnumTextAlign.TextAlign_Left);
		e.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		e.setDimencion(20, 20);
		e.setInicioFinal(0, 0);
		columnas.add(e);



		ColumnaDinamica g= new ColumnaDinamica();
		g.setNombreAtributo("fechaEnvio");
		g.instanciarColumna("FECHA INICIO", EnumTipoDatoJava.String, "");
		g.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		g.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		g.setDimencion(20, 10);
		g.setInicioFinal(0, 0);
		columnas.add(g);


		ColumnaDinamica h= new ColumnaDinamica();
		h.setNombreAtributo("fechaFin");
		h.instanciarColumna("FECHA FIN", EnumTipoDatoJava.String, "");
		h.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		h.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		h.setDimencion(20, 10);
		h.setInicioFinal(0, 0);
		columnas.add(h);


		ColumnaDinamica a4= new ColumnaDinamica();
		a4.setNombreAtributo("ansEsperado");
		a4.instanciarColumna("T. ESPERADO", EnumTipoDatoJava.String, "");
		a4.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		a4.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		a4.setDimencion(20, 10);
		a4.setInicioFinal(0, 0);
		columnas.add(a4);

		ColumnaDinamica j= new ColumnaDinamica();
		j.setNombreAtributo("ansReal");
		j.instanciarColumna("TIEMPO REAL", EnumTipoDatoJava.String, "");
		j.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		j.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		j.setDimencion(20, 10);
		j.setInicioFinal(0, 0);
		columnas.add(j);

		ColumnaDinamica m= new ColumnaDinamica();
		m.setNombreAtributo("cumpleAns");
		m.instanciarColumna("CUMPLE ANS", EnumTipoDatoJava.String, "");
		m.setAlineacionInterior(EnumTextAlign.TextAlign_Center);
		m.setAlineacionVertical(EnumVerticalAlign.VerticalAlign_Middle);
		m.setDimencion(20, 10);
		m.setInicioFinal(0, 0);
		columnas.add(m);
		return columnas;
	}

	public void obtenerOficinaPorTerritorio(AjaxBehaviorEvent event){
		LOG.info("obtenerOficinaPorTerritorio"  + "reporteVO.getIdTerritorio()" + reporteAnsVO.getIdTerritorio());
		//		 if (reporteAnsVO.getIdTerritorio() == 0) {
		//				this.oficinaItemsExp = Util.listaVacia();
		//			} else {
		crearListaOficinasExp(Integer.parseInt(String.valueOf(reporteAnsVO.getIdTerritorio())));
		//			}
	}

	private void crearListaOficinasExp(Integer idTerritorio) {
		this.oficinaItemsExp = Util.crearItems(
				oficinaDAO.findByIdTerritorio(idTerritorio), false, "id",
				"codigo,descripcion", "{0} {1}", true);
	}

	public List<SelectItem> getTerritoriosSelectValue() {
		return Util.crearItems(territorios, false, "id",
				"codigo,descripcion", "{0} {1}", true);
	}

	public List<SelectItem> getEstadosExpedienteSelectValue() {
		return Util.crearItems(estadosExpediente, "id", "descripcion");
	}

	//	public List<SelectItem> getOficinasSelectValue() {
	//		return Util.crearItems(oficinas, "id", "descripcion");
	//	}

	public List<SelectItem> getTareasSelectValue() {
		return Util.crearItems(tareas, "id", "descripcion");
	}

	public List<SelectItem> getEstadostareaSelectValue() {
		return Util.crearItems(estados, "id", "descripcion");
	}

	public List<SelectItem> getOperacionesSelectValue() {
		return Util.crearItems(operaciones, "id", "descripcion");
	}

	public ReporteVO getReporteVO() {
		return reporteVO;
	}

	public void setReporteVO(ReporteVO reporteVO) {
		this.reporteVO = reporteVO;
	}

	public List<Tarea> getTareas() {
		return tareas;
	}

	public void setTareas(List<Tarea> tareas) {
		this.tareas = tareas;
	}

	public List<EstadoTarea> getEstados() {
		return estados;
	}

	public void setEstados(List<EstadoTarea> estados) {
		this.estados = estados;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<ExpedienteDTO> getExpedientes() {
		return expedientes;
	}

	public void setExpedientes(List<ExpedienteDTO> expedientes) {
		this.expedientes = expedientes;
	}

	public boolean isActiveExport() {
		return activeExport;
	}

	public void setActiveExport(boolean activeExport) {
		this.activeExport = activeExport;
	}

	public List<EstadoExpediente> getEstadosExpediente() {
		return estadosExpediente;
	}

	public void setEstadosExpediente(List<EstadoExpediente> estadosExpediente) {
		this.estadosExpediente = estadosExpediente;
	}

	public List<Operacion> getOperaciones() {
		return operaciones;
	}

	public void setOperaciones(List<Operacion> operaciones) {
		this.operaciones = operaciones;
	}

	public List<Oficina> getOficinas() {
		return oficinas;
	}

	public void setOficinas(List<Oficina> oficinas) {
		this.oficinas = oficinas;
	}

	public List<Territorio> getTerritorios() {
		return territorios;
	}

	public void setTerritorios(List<Territorio> territorios) {
		this.territorios = territorios;
	}

	public List<SelectItem> getOficinaItemsExp() {
		return oficinaItemsExp;
	}

	public void setOficinaItemsExp(List<SelectItem> oficinaItemsExp) {
		this.oficinaItemsExp = oficinaItemsExp;
	}

	public boolean isMostrarTareaActual() {
		return mostrarTareaActual;
	}

	public void setMostrarTareaActual(boolean mostrarTareaActual) {
		this.mostrarTareaActual = mostrarTareaActual;
	}

	public String getTotalFilas() {
		return totalFilas;
	}

	public void setTotalFilas(String totalFilas) {
		this.totalFilas = totalFilas;
	}

	public String getTotalFilasAns() {
		return totalFilasAns;
	}

	public void setTotalFilasAns(String totalFilasAns) {
		this.totalFilasAns = totalFilasAns;
	}

	public boolean isActiveExportAns() {
		return activeExportAns;
	}

	public void setActiveExportAns(boolean activeExportAns) {
		this.activeExportAns = activeExportAns;
	}

	public List<ExpedienteDTO> getExpedientesAns() {
		return expedientesAns;
	}

	public void setExpedientesAns(List<ExpedienteDTO> expedientesAns) {
		this.expedientesAns = expedientesAns;
	}

	public ReporteVO getReporteAnsVO() {
		return reporteAnsVO;
	}

	public void setReporteAnsVO(ReporteVO reporteAnsVO) {
		this.reporteAnsVO = reporteAnsVO;
	}






}
