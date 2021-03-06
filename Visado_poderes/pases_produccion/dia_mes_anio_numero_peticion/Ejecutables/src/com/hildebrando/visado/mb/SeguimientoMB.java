package com.hildebrando.visado.mb;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.BeansException;

import com.bbva.common.listener.SpringInit.SpringInit;
import com.bbva.common.util.ConstantesVisado;
import com.bbva.persistencia.generica.dao.Busqueda;
import com.bbva.persistencia.generica.dao.GenericDao;
import com.bbva.persistencia.generica.dao.SolicitudDao;
import com.bbva.persistencia.generica.dao.impl.ConsultasJDBCDaoImpl;
import com.bbva.persistencia.generica.util.Utilitarios;
import com.grupobbva.bc.per.tele.ldap.serializable.IILDPeUsuario;
import com.hildebrando.visado.dto.AgrupacionSimpleDto;
import com.hildebrando.visado.dto.ComboDto;
import com.hildebrando.visado.dto.Moneda;
import com.hildebrando.visado.dto.SeguimientoDTO;
import com.hildebrando.visado.dto.TipoDocumento;
import com.hildebrando.visado.modelo.TiivsAgrupacionPersona;
import com.hildebrando.visado.modelo.TiivsHistSolicitud;
import com.hildebrando.visado.modelo.TiivsHistSolicitudId;
import com.hildebrando.visado.modelo.TiivsMiembro;
import com.hildebrando.visado.modelo.TiivsMultitabla;
import com.hildebrando.visado.modelo.TiivsNivel;
import com.hildebrando.visado.modelo.TiivsOficina1;
import com.hildebrando.visado.modelo.TiivsOperacionBancaria;
import com.hildebrando.visado.modelo.TiivsParametros;
import com.hildebrando.visado.modelo.TiivsPersona;
import com.hildebrando.visado.modelo.TiivsSolicitud;
import com.hildebrando.visado.modelo.TiivsSolicitudAgrupacion;
import com.hildebrando.visado.modelo.TiivsSolicitudNivel;
import com.hildebrando.visado.modelo.TiivsSolicitudOperban;
import com.hildebrando.visado.modelo.TiivsTerritorio;
import com.hildebrando.visado.service.NivelService;

@ManagedBean(name = "seguimientoMB")
@SessionScoped
public class SeguimientoMB 
{
	/*List<TiivsOficina1> listaOficinasValidar ;//= new ArrayList<TiivsOficina1>();
	
    public List<TiivsOficina1> getListaOficinasValidar() {
		return this.listaOficinasValidar;
	}

	public void setListaOficinasValidar(List<TiivsOficina1> listaOficinasValidar) {
		this.listaOficinasValidar = listaOficinasValidar;
	}*/

	/*CAMBIO HVB 23/07/2014
	 *SE MAPEA LA PROPIEDAD DE INFODEPLOY PARA OBTENER LA INFORMACION QUE SE CARGA/RESETEA EN LA APLICACION
	 */
	@ManagedProperty(value="#{infoDeployMB}")
	private InfoDeployMB infoDeployMB;
	
	public InfoDeployMB getInfoDeployMB() {
		return infoDeployMB;
	}

	public void setInfoDeployMB(InfoDeployMB infoDeployMB) {
		this.infoDeployMB = infoDeployMB;
	}
	@ManagedProperty(value = "#{pdfViewerMB}")
	private PDFViewerMB pdfViewerMB;
	private List<TiivsSolicitud> solicitudes;
	private List<TiivsAgrupacionPersona> lstAgrupPer;
	private List<TiivsHistSolicitud> lstHistorial;
	private List<String> lstEstudioSelected;
	private List<String> lstEstadoNivelSelected;
	private List<String> lstTipoSolicitudSelected;
	private List<String> lstEstadoSelected;
	private List<String> lstNivelSelected;
	private List<String> lstSolicitudesSelected;
	private List<String> lstSolicitudesxOpeBan;
	private List<AgrupacionSimpleDto> lstAgrupacionSimpleDto;
	private String codSolicitud;
	private String textoTotalResultados;
	private String idImporte;
	private String idTerr;
	private String idTiposFecha;
	private String idOpeBan;
	private String idCodOfi;
	private String idCodOfi1;
	private String txtCodOfi;
	private String txtNomOfi;
	private String txtNomOficina;
	private String txtCodOficina;
	private String txtNomTerritorio;
	private String txtNomApoderado;
	private String txtNomPoderdante;
	private String nroDOIApoderado;
	private String nroDOIPoderdante;
	private Boolean noHabilitarExportar;
	private Boolean noMostrarFechas;
	private Boolean bRevision=false;
	private Boolean bDelegados=false;
	private Boolean bRevocatoria=false;
	private Boolean mostrarEstudio;
	private Boolean mostrarUsuario;
	private Date fechaInicio;
	private Date fechaFin;
	private TiivsOficina1 oficina;
	private Boolean mostrarColumna=true;
	private String nombreArchivoExcel;
	private String rutaArchivoExcel;
	private StreamedContent file;  
	private IILDPeUsuario usuario;
	private String PERFIL_USUARIO ;
	private boolean bloquearOficina=false;
	private boolean mostrarFechaEstado=false;
	private NivelService nivelService;
	private TiivsPersona objTiivsPersonaBusquedaNomApod;
	private TiivsPersona objTiivsPersonaBusquedaNomPoder;
	private List<SeguimientoDTO> lstSeguimientoDTO;
	private TiivsSolicitud selectedSolicitud;	
	@ManagedProperty(value = "#{combosMB}")
	private CombosMB combosMB;
	public static Logger logger = Logger.getLogger(SeguimientoMB.class);
	
	//Se agrega abajo
	private String tipoRegistro;
	private String poderdante;
	private String apoderdante;
	private String sentenciaOficina = null;
	
	private LazyDataModel<TiivsSolicitud> solicitudesPag;
	private String solicitudService="solicitudService";
	

	//**********CAMBIO HVB 18/07/2014*******
	//**********Se borran variables y metodos del constructor no necesarios en especial cargasolicitudes()*******


	
	public SeguimientoMB()
	{
		//**********CAMBIO HVB 21/07/2014*******
		//**********medicion de tiempo*******
		long inicio = System.currentTimeMillis();
		
		logger.debug("====================== SeguimientoMB ======================");
		usuario = (IILDPeUsuario) Utilitarios.getObjectInSession("USUARIO_SESION");	
		PERFIL_USUARIO=(String) Utilitarios.getObjectInSession("PERFIL_USUARIO");
//		solicitudes = new ArrayList<TiivsSolicitud>();
		lstAgrupPer= new ArrayList<TiivsAgrupacionPersona>();
		lstNivelSelected = new ArrayList<String>();
		lstEstudioSelected = new ArrayList<String>();
		lstTipoSolicitudSelected = new ArrayList<String>();
		lstSolicitudesxOpeBan = new ArrayList<String>();
		lstAgrupacionSimpleDto = new ArrayList<AgrupacionSimpleDto>();
		lstSeguimientoDTO = new ArrayList<SeguimientoDTO>();
		nivelService = new NivelService();
		oficina= new TiivsOficina1();
		lstSolicitudesSelected = new ArrayList<String>();
		lstHistorial = new ArrayList<TiivsHistSolicitud>();
		this.lstTipoSolicitudSelected = new ArrayList<String>();
		this.lstEstadoNivelSelected = new ArrayList<String>();
		this.lstEstadoSelected = new ArrayList<String>();
		
		this.codSolicitud="";
		this.idImporte="";
		this.idTiposFecha = "";
		this.idOpeBan = "";
		this.nroDOIApoderado="";
		this.txtNomApoderado="";
		this.nroDOIPoderdante="";
		this.txtNomPoderdante="";
		
//		combosMB= new CombosMB();
		
		/*combosMB.cargarMultitabla();
		combosMB.cargarCombosMultitabla(ConstantesVisado.CODIGO_MULTITABLA_IMPORTES);
		combosMB.cargarCombosNoMultitabla();*/
		
		/*
		// Carga combo Rango Importes
		combosMB.cargarCombosMultitabla(ConstantesVisado.CODIGO_MULTITABLA_IMPORTES);
		// Carga combo Estados
		combosMB.cargarCombosMultitabla(ConstantesVisado.CODIGO_MULTITABLA_ESTADOS);
		// Carga combo Estados Nivel
		combosMB.cargarCombosMultitabla(ConstantesVisado.CODIGO_MULTITABLA_ESTADOS_NIVEL);
		// Carga combo Tipos de Fecha
		combosMB.cargarCombosMultitabla(ConstantesVisado.CODIGO_MULTITABLA_TIPOS_FECHA);
		// Carga lista de monedas
		combosMB.cargarCombosMultitabla(ConstantesVisado.CODIGO_MULTITABLA_MONEDA);
		// Carga lista de tipos de persona
		combosMB.cargarCombosMultitabla(ConstantesVisado.CODIGO_MULTITABLA_TIPO_REGISTRO_PERSONA);
		*/
		
		
//		cargarSolicitudes();
		
//		if (solicitudes.size() == 0) 
//		{
//			setearTextoTotalResultados(ConstantesVisado.MSG_TOTAL_SIN_REGISTROS,solicitudes.size());
//			setNoHabilitarExportar(true);
//		} 
//		else  
//		{
//			setearTextoTotalResultados(ConstantesVisado.MSG_TOTAL_REGISTROS + solicitudes.size() + ConstantesVisado.MSG_REGISTROS,solicitudes.size());
//			setNoHabilitarExportar(false);
//		}
		
		//setearCamposxPerfil();	//[14.08]GD-Incidencias: Se comenta linea	
		generarNombreArchivo();
		
		//Carga estado del flujo
		//combosMB.traerEstadosFlujoSolicitud();
		//setNoMostrarFechas(true);

		//Se agrega abajo
//		obtenerTipoRegistro(); -- SE AGREGA EN EL POSCONSTRUCTOR 24/07/2014 HVB
		obtenerEtiquetasTipoRegistro();
		//**********CAMBIO HVB 21/07/2014*******
		//**********medicion de tiempo*******
		logger.debug("Tiempo de respuesta METODO seguimientoMB: " + (System.currentTimeMillis()-inicio)/1000.0 + " segundos ------------- seguimientoMB()");
	}
	
	/*
	 * CAMBIO HVB 24/07/2014 
	 * SE CREA EL METODO POSCONSTRUCTOR PARA QUE SE PUEDAN USAR LOS ATRIBUTOS DE OTROS MANAGED BEANS
	 */
	@PostConstruct
	public void posConstructor()
	{
		long inicio = System.currentTimeMillis();
		obtenerTipoRegistro();
		setearCamposxPerfil(); //[14.08]GD-Incidencias: Se agrega linea
		logger.debug("Tiempo de respuesta METODO seguimientoMB: " + (System.currentTimeMillis()-inicio)/1000.0 + " segundos ------------- posConstructor()");
	}
	
	//**********CAMBIO HVB 18/07/2014*******
	//**********Se agrega metodo actualizarListas() para alinearlo con el procedimiento de consultas*******

	public String actualizarListas(){
		
		logger.debug("==== actualizarListas() ===");
		
		solicitudes = new ArrayList<TiivsSolicitud>();
		
		cargarSolicitudes();
		if (solicitudes.size() == 0) 
		{
			setearTextoTotalResultados(ConstantesVisado.MSG_TOTAL_SIN_REGISTROS,solicitudes.size());
			setNoHabilitarExportar(true);
		} 
		else 
		{
			setearTextoTotalResultados(ConstantesVisado.MSG_TOTAL_REGISTROS + solicitudes.size() + ConstantesVisado.MSG_REGISTROS,solicitudes.size());
			setNoHabilitarExportar(false);
		}
		//---- DEBERIA IR EL METODO PARA ACTUALIZAR LOS COMBOS DINAMICOS HVB 18/07/2014
		//combosMB = new CombosMB();	
		return "/faces/paginas/bandejaSeguimiento.xhtml";
	}
	
	//SE AGREGA METODO PARA OBTENER EL TIPO DE REGISTRO POR BD
	/*
	 * CAMBIO HVB 24/07/2014 
	 * SE OPTIMIZA EL METODO EVITANDO LLAMAR A LA MULTITABLA A LA BD
	 * SE UTILIZA LA LISTA CARGADA AL INICIAR EL APLICATIVO
	 * SE CONCATENA LOS TEXTOS CON SB EN VEZ DE STRING (TIEMPO)
	 */
	private void obtenerTipoRegistro() {
		Integer contador = 0;
		try 
		{
			tipoRegistro = "";
		StringBuilder registroPersonasBuilder = new StringBuilder();
		
			for (ComboDto registroPersona : infoDeployMB.getLstTipoRegistroPersona()) {
					contador++;
				if (contador.compareTo(infoDeployMB.getLstTipoRegistroPersona().size()) == 0) {
					registroPersonasBuilder.append(registroPersona.getDescripcion());
					} else {
					registroPersonasBuilder.append(registroPersona.getDescripcion());
					registroPersonasBuilder.append(" ");
					registroPersonasBuilder.append(ConstantesVisado.SLASH);
					registroPersonasBuilder.append(" ");
					}
				}
			tipoRegistro = registroPersonasBuilder.toString();
			}
		catch (Exception e) 
		{
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+ "Tipo de Registros Personas: ",e);
		}
//		GenericDao<TiivsMultitabla, Object> multiDAO = (GenericDao<TiivsMultitabla, Object>) SpringInit
//				.getApplicationContext().getBean("genericoDao");
//		Busqueda filtroMultitabla = Busqueda.forClass(TiivsMultitabla.class);
//		filtroMultitabla.add(Restrictions.eq("id.codMult",ConstantesVisado.CODIGO_MULTITABLA_TIPO_REGISTRO_PERSONA));
//		List<TiivsMultitabla> listaMultiTabla = new ArrayList<TiivsMultitabla>();
//		Integer contador = 0;
//		try {
//			listaMultiTabla = multiDAO.buscarDinamico(filtroMultitabla);
//			tipoRegistro = "";
//			if (listaMultiTabla.size() > 0) {
//				for (TiivsMultitabla multitabla : listaMultiTabla) {
//					contador++;
//					if (contador.compareTo(listaMultiTabla.size()) == 0) {
//						tipoRegistro += multitabla.getValor1();
//					} else {
//						tipoRegistro += multitabla.getValor1() + " / ";
//					}
//				}
//			}
//
//		} catch (Exception e) {
//			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+ "de multitablas: ",e);
//		}
	}
	
	//METODO PARA OBTENER ETIQUETAS DE TIPO DE REGISTRO DESDE BD Y MOSTRARLO EN GRILLA
	private void obtenerEtiquetasTipoRegistro() {
		obtenerPonderdante();
		obtenerAponderdante();
	}
	
	/*
	 * CAMBIO HVB 24/07/2014 
	 * SE OPTIMIZA EL METODO HACIENDO USO DE LOS STRINGBUILDER PARA CONCATENAR EN VEZ DEL MISMO STRING
	 */
	private void obtenerPonderdante(){
		GenericDao<TiivsMultitabla, Object> multiDAO = (GenericDao<TiivsMultitabla, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtroMultitabla = Busqueda.forClass(TiivsMultitabla.class);
		filtroMultitabla.add(Restrictions.eq("valor3", ConstantesVisado.R1_PODERDANTE));
		List<TiivsMultitabla> listaMultiTabla = new ArrayList<TiivsMultitabla>();
		Integer contador = 0;
		try {
			listaMultiTabla = multiDAO.buscarDinamico(filtroMultitabla);
			poderdante = "";
			StringBuilder sbPoderdante = new StringBuilder();
			if(listaMultiTabla.size()>0){
				for(TiivsMultitabla multitabla:listaMultiTabla){
					contador++;
					if(contador.compareTo(listaMultiTabla.size())==0){
						sbPoderdante.append(multitabla.getValor1());
//						poderdante += multitabla.getValor1();	
					}else
					{
						sbPoderdante.append(multitabla.getValor1());
						sbPoderdante.append(" ");
						sbPoderdante.append(ConstantesVisado.SLASH);
						sbPoderdante.append(" ");
//						poderdante += multitabla.getValor1() + " / ";
					}
				}
				poderdante = sbPoderdante.toString();
			}
		} catch (Exception e) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+ "de multitablas: ",e);
		}
	}
	
	/*
	 * CAMBIO HVB 24/07/2014 
	 * SE OPTIMIZA EL METODO HACIENDO USO DE LOS STRINGBUILDER PARA CONCATENAR EN VEZ DEL MISMO STRING
	 */
	
	private void obtenerAponderdante(){
		GenericDao<TiivsMultitabla, Object> multiDAO = (GenericDao<TiivsMultitabla, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtroMultitabla = Busqueda.forClass(TiivsMultitabla.class);
		filtroMultitabla.add(Restrictions.eq("valor3", ConstantesVisado.R2_APODERADO));
		List<TiivsMultitabla> listaMultiTabla = new ArrayList<TiivsMultitabla>();
		Integer contador = 0;
		try {
			listaMultiTabla = multiDAO.buscarDinamico(filtroMultitabla);
			apoderdante = "";
			StringBuilder sbApoderdante = new StringBuilder();
			if(listaMultiTabla.size()>0){
				for(TiivsMultitabla multitabla:listaMultiTabla){
					contador++;
					if(contador.compareTo(listaMultiTabla.size())==0)
					{
//						apoderdante += multitabla.getValor1();
						sbApoderdante.append(multitabla.getValor1());
					}else
					{
						sbApoderdante.append(multitabla.getValor1());
						sbApoderdante.append(" ");
						sbApoderdante.append(ConstantesVisado.SLASH);
						sbApoderdante.append(" ");
//						apoderdante += multitabla.getValor1() + " / ";
					}
				}
				apoderdante = sbApoderdante.toString();
			}
		} catch (Exception e) {
			logger.debug(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+ "de multitablas: " + e);
		}
	}
	
	public void setearCamposxPerfil()
	{
		//Seteo de campo Oficina en caso de que el grupo del usuario logueado sea de Perfil Oficina
		//String grupoOfi = (String) Utilitarios.getObjectInSession("GRUPO_OFI");
		String codOfi="";
		String desOfi="";
		String textoOficina="";
		
		if (PERFIL_USUARIO!=null)
		{
			if (PERFIL_USUARIO.equals(ConstantesVisado.OFICINA))
			{
				codOfi=usuario.getBancoOficina().getCodigo().trim();
				desOfi=usuario.getBancoOficina().getDescripcion();
					
				TiivsTerritorio terr=buscarTerritorioPorOficina(codOfi);
				textoOficina =codOfi + " "  + desOfi+ " (" + terr.getCodTer() + "-" + terr.getDesTer() + ")";
			}
			
			if (textoOficina.compareTo("")!=0)
			{
				logger.info("Texto Oficina a setear: " + textoOficina);
				setTxtNomOficina(textoOficina);
				
				TiivsOficina1 tmpOfi = new TiivsOficina1();
				tmpOfi.setCodOfi(usuario.getBancoOficina().getCodigo().trim());
				tmpOfi.setDesOfi(textoOficina);
				
				String nomDetallado = usuario.getBancoOficina().getCodigo().trim() + "-" + usuario.getBancoOficina().getDescripcion().trim();
				
				logger.debug("Nombre Detallado de Oficina: " + nomDetallado);
				
				tmpOfi.setNombreDetallado(nomDetallado);
							
				setOficina(tmpOfi);
				setBloquearOficina(true);
			}
			
			if (PERFIL_USUARIO.equals(ConstantesVisado.SSJJ))
			{
				setMostrarEstudio(true);
				setMostrarFechaEstado(true);
			}
			else
			{
				setMostrarEstudio(false);
				setMostrarFechaEstado(false);
			}
			
			//[28-10] Mejora: Ocultar columnas en seguimiento historial
			if(PERFIL_USUARIO.equals(ConstantesVisado.OFICINA)){
				//logger.debug("== MEJORA: No se mostrara 'Usuario' porque es: "+PERFIL_USUARIO);
				setMostrarUsuario(false);
			}else{
				//logger.debug("== MEJORA: Se mostrara 'Usuario' porque es: "+PERFIL_USUARIO);
				setMostrarUsuario(true);
			}
			
		}		
	}	
	
	/** Descripcion: M�todo que se encarga de cargar las solicitudes de visado 
	 * en la grilla
	 * @author  Cesar La Rosa
	 * @version: 1.0
	**/
	/*
	 * CAMBIO 25/07/2014 HVB
	 * SE CAMBIA LA FORMA DE CONSULTA DE SOLICITUDES DE HIBERNATE A JDBC
	 */
	public void cargarSolicitudes() 
	{
//		GenericDao<TiivsSolicitud, Object> solicDAO = (GenericDao<TiivsSolicitud, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
//		Busqueda filtroSol = Busqueda.forClass(TiivsSolicitud.class);
//		filtroSol.setMaxResults(1000);
		StringBuilder sbFiltro = new StringBuilder();
						
		if (PERFIL_USUARIO!=null)
		{
			if(PERFIL_USUARIO.equals(ConstantesVisado.ABOGADO) )
			{
//				filtroSol.createAlias(ConstantesVisado.NOM_TBL_ESTUDIO,	ConstantesVisado.ALIAS_TBL_ESTUDIO);
//				filtroSol.add(Restrictions.eq(ConstantesVisado.ALIAS_COD_ESTUDIO, buscarEstudioxAbogado()));
				sbFiltro.append("S.COD_ESTUDIO = ");
				sbFiltro.append(buscarEstudioxAbogado());
			}
			else if (PERFIL_USUARIO.equals(ConstantesVisado.OFICINA))
			{
				sbFiltro.append("S.COD_OFI = ");
				sbFiltro.append(usuario.getBancoOficina().getCodigo().trim());
//				filtroSol.createAlias(ConstantesVisado.NOM_TBL_OFICINA,	ConstantesVisado.ALIAS_TBL_OFICINA);
				setBloquearOficina(true);
				setMostrarColumna(false);
//				filtroSol.add(Restrictions.eq(ConstantesVisado.CAMPO_COD_OFICINA_ALIAS, usuario.getBancoOficina().getCodigo().trim()));
			}
		}
				
//		filtroSol.addOrder(Order.asc(ConstantesVisado.CAMPO_COD_SOLICITUD));
		
		try {
			long inicio = System.currentTimeMillis();
//			solicitudes = solicDAO.buscarDinamico(filtroSol);	
			ConsultasJDBCDaoImpl jdbcDAO = new ConsultasJDBCDaoImpl();
			
			solicitudes = jdbcDAO.consultarSolicitud(infoDeployMB.getPmaxResultadosConsulta(),sbFiltro.toString());
			logger.debug("===========================================================================================================");
			logger.debug("[GSI]-Tiempo de respuesta de consulta de solicitudes: " + (System.currentTimeMillis()-inicio)/1000 + " segundos");
			logger.debug("[GSI]-Cantidad de registros: " + (solicitudes!=null?solicitudes.size():0));
			logger.debug("===========================================================================================================");
			
			if(solicitudes!=null){
				logger.debug(ConstantesVisado.MENSAJE.TAMANHIO_LISTA+"de solicitudes es:"+solicitudes.size());
			}
		} catch (Exception ex) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR+"al buscar/cargarSolicitudes: ",ex);
		}
		
		actualizarDatosGrilla();
		/*
		String grupoOfi = (String) Utilitarios.getObjectInSession("GRUPO_OFI");
		
		if (grupoOfi!=null)
		{
			if (grupoOfi.compareTo("")!=0)
			{
				
			}
		}*/
	}
	
	
	public String buscarEstudioxAbogado()
	{
		String codEstudio ="";
		GenericDao<TiivsMiembro, Object> mDAO = (GenericDao<TiivsMiembro, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtroM = Busqueda.forClass(TiivsMiembro.class);
		filtroM.add(Restrictions.eq(ConstantesVisado.CAMPO_COD_MIEMBRO, usuario.getUID()));
		List<TiivsMiembro> result = new ArrayList<TiivsMiembro>();
		
		try {
			result = mDAO.buscarDinamico(filtroM);			
		} catch (Exception ex) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR+"al buscarEstudioxAbogado: ",ex);
		}
		
		if (result!=null)
		{
			codEstudio = result.get(0).getEstudio();
		}
		
		return codEstudio;
	}
	
	@SuppressWarnings("unchecked")
	public void liberarSolicitud()
	{
		logger.debug("=== liberarSolicitud() ===");
		String codigoSolicitud=Utilitarios.capturarParametro("prm_codSoli");
		logger.info("codigoSolicitud : "+codigoSolicitud);
		
		GenericDao<TiivsSolicitud, Object> solicDAO = (GenericDao<TiivsSolicitud, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtroSol = Busqueda.forClass(TiivsSolicitud.class);
		filtroSol.add(Restrictions.eq(ConstantesVisado.CAMPO_COD_SOLICITUD, codigoSolicitud));
		
		try {
			solicitudes = solicDAO.buscarDinamico(filtroSol);			
		} catch (Exception ex) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"solicitudes en liberarSolicitud(): ",ex);
		}
		
		if (solicitudes.size()==1)
		{
			if (solicitudes.get(0).getEstado().trim().equals(ConstantesVisado.ESTADOS.ESTADO_COD_RESERVADO_T02))
			{
				 try {
					  TiivsSolicitud solicitud =new TiivsSolicitud();
					  solicitud.setCodSoli(codigoSolicitud);	  
					  SolicitudDao<TiivsSolicitud, Object> solicitudService = (SolicitudDao<TiivsSolicitud, Object>) SpringInit.getApplicationContext().getBean("solicitudEspDao");
					  solicitud= solicitudService.obtenerTiivsSolicitud(solicitud);
					  solicitud.setEstado(ConstantesVisado.ESTADOS.ESTADO_COD_ENVIADOSSJJ_T02);
					  solicitud.setFechaEstado(new Timestamp(new Date().getTime()));
					  solicitud.setDescEstado(Utilitarios.obternerDescripcionEstado(ConstantesVisado.ESTADOS.ESTADO_COD_ENVIADOSSJJ_T02));
					  					  
					  solicitudService.modificar(solicitud);
					  
					  registrarHistorial(solicitud);
				} catch (BeansException e) {
					logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR+"en liberarSolicitud: ",e);
				} catch (Exception e) {
					logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR+"al modificar la solicitud: ",e);
				}
			}
		}
		cargarSolicitudes();
	}
	public void registrarHistorial(TiivsSolicitud solicitud) throws Exception {
		SolicitudDao<String, Object> serviceMaxMovi = (SolicitudDao<String, Object>) SpringInit
				.getApplicationContext().getBean("solicitudEspDao");
		String numeroMovimiento = serviceMaxMovi
				.obtenerMaximoMovimiento(solicitud.getCodSoli());

		int num = 0;
		if (!numeroMovimiento.equals("")) {
			num = Integer.parseInt(numeroMovimiento) + 1;
		} else {
			num = 1;
		}
		numeroMovimiento = num + "";
		logger.info("Numero de Movimiento a registrar para el CodSolicitud : "
				+ solicitud.getCodSoli());
		TiivsHistSolicitud objHistorial = new TiivsHistSolicitud();
		objHistorial.setId(new TiivsHistSolicitudId(solicitud.getCodSoli(),
				numeroMovimiento));
		objHistorial.setEstado(solicitud.getEstado());
		//[24-10] Se agrega el nombreCompleto del usuario para su registro en el historial
		  String nombreCompleto="".concat(usuario.getNombre()!=null?usuario.getNombre():"")
					.concat(" ").concat(usuario.getApellido1()!=null?usuario.getApellido1():"")
					.concat(" ").concat(usuario.getApellido2()!=null?usuario.getApellido2():"");
		objHistorial.setNomUsuario(nombreCompleto);
		
		objHistorial.setObs(solicitud.getObs());
		objHistorial.setFecha(new Timestamp(new Date().getTime()));
		objHistorial.setRegUsuario(usuario.getUID());
		GenericDao<TiivsHistSolicitud, Object> serviceHistorialSolicitud = (GenericDao<TiivsHistSolicitud, Object>) SpringInit
				.getApplicationContext().getBean("genericoDao");
		serviceHistorialSolicitud.insertar(objHistorial);
	}
	public String obtenerDescripcionClasificacion(String idTipoClasificacion) {
		String descripcion = "";
		for (ComboDto z : combosMB.getLstClasificacionPersona()) {
			if (z.getKey().trim().equals(idTipoClasificacion)) {
				descripcion = z.getDescripcion();
				break;
			}
		}
		return descripcion;
	}

	public String obtenerDescripcionTipoRegistro(String idTipoTipoRegistro) {
		String descripcion = "";
		/*CAMBIO HVB 23/07/2014
		 *SE CAMBIA A LA VARIABLE INFODEPLOY POR COMBOSMB PARA OBTENER LA INFORMACION QUE SE CARGA/RESETEA EN LA APLICACION
		 */
		for (ComboDto z : infoDeployMB.getLstTipoRegistroPersona()) {
			if (z.getKey().trim().equals(idTipoTipoRegistro)) {
				descripcion = z.getDescripcion();
				break;
			}
		}
		
//		for (ComboDto z : combosMB.getLstTipoRegistroPersona()) {
//			if (z.getKey().trim().equals(idTipoTipoRegistro)) {
//				descripcion = z.getDescripcion();
//				break;
//			}
//		}
		return descripcion;
	}
	
	public String obtenerDescripcionDocumentos(String idTipoDocumentos) {
		String descripcion = "";
		/*CAMBIO HVB 23/07/2014
		 *SE CAMBIA A LA VARIABLE INFODEPLOY POR COMBOSMB PARA OBTENER LA INFORMACION QUE SE CARGA/RESETEA EN LA APLICACION
		 */
		for (TipoDocumento z : infoDeployMB.getLstTipoDocumentos()) {
			if (z.getCodTipoDoc().trim().equals(idTipoDocumentos)) {
				descripcion = z.getDescripcion();
				break;
			}
		}
		
//		for (TipoDocumento z : combosMB.getLstTipoDocumentos()) {
//			if (z.getCodTipoDoc().trim().equals(idTipoDocumentos)) {
//				descripcion = z.getDescripcion();
//				break;
//			}
//		}
		return descripcion;
	}

	
	private void actualizarDatosGrilla() 
	{	
		long inicio = System.currentTimeMillis();
		String cadena="";

		List<TiivsNivel> listaNiveles = null;
		List<TiivsSolicitudNivel> listaSolicitudNiveles = null;
		List<TiivsSolicitudAgrupacion> listaSolicitudAgrupacion = null;
		
		// Se obtiene y setea la descripcion del Estado en la grilla
		if(solicitudes!=null){
			logger.debug("[ActDatosGrilla]-Hay  "+solicitudes.size() +" solicitud(es) como resultado.");
			/*
			 * CAMBIO 25/07/2014 HVB
			 * SE COMENTA METODO DE OPERACIONES BANCARIAS PUESTO QUE SE OBTIENE LAS OPERACIONES CONCATENADAS DE SOLICITUD
			 * SE COMENTA LISTANIVELES PUESTO QUE SE OBTIENE LOS NIVELES CONCATENADOS DE SOLICITUD
			 * SE COMENTA LISTASOLICITUDNIVELES PUESTO QUE SE OBTIENE LOS NIVELES CONCATENADOS DE SOLICITUD
			 * SE COMENTA LISTASOLICITUDAGRUPACION PUESTO QUE SE OBTIENE LOS HEREDEROS Y TITULARES CONCATENADOS DE SOLICITUD
			 */
//			listarOperacionesBancarias();
//			listaNiveles = nivelService.buscarNiveles();
//			listaSolicitudNiveles = listarSolicitudNiveles();
//			listaSolicitudAgrupacion = listarSolicitudAgrupacion();
		}else{
			return;
		}
		
		long inicioLOOP = System.currentTimeMillis();
		for (TiivsSolicitud tmpSol : solicitudes) 
		{
			if (tmpSol.getEstado().trim().equals(ConstantesVisado.ESTADOS.ESTADO_COD_RESERVADO_T02))
			{
				if (PERFIL_USUARIO.equals(ConstantesVisado.ABOGADO) || PERFIL_USUARIO.equals(ConstantesVisado.OFICINA))
				{
					tmpSol.setbLiberado(false);
				}
				else
				{
					tmpSol.setbLiberado(true);
				}
			}
			else
			{
				tmpSol.setbLiberado(false);
			}
			
			// Se obtiene y setea la descripcion del Estado en la grilla
			if (tmpSol.getEstado() != null) {
				String estado=buscarEstadoxCodigo(tmpSol.getEstado().trim());
				tmpSol.setTxtEstado(estado);
				//logger.debug("[ActDatosGrilla]-TxtEstado:"+tmpSol.getTxtEstado());
			}
			
			// Se obtiene la moneda y se coloca las iniciales en la columna Importe Total
			if (tmpSol.getMoneda() != null) 
			{
				String moneda = buscarAbrevMoneda(tmpSol.getMoneda());
				tmpSol.setTxtImporte(moneda.concat(ConstantesVisado.DOS_PUNTOS).concat(String.valueOf(tmpSol.getImporte())));
			} 
			else 
			{
				if (tmpSol.getImporte() != 0) 
				{
					tmpSol.setTxtImporte(String.valueOf(tmpSol.getImporte()));
				}
			}
			
			/*
			 * CAMBIO 31/07/2014 HVB
			 * CONCATENACION DE NIVELES
			 */
			StringBuilder sbNiveles = new StringBuilder();
			int nivelesEncontrados  = 0;
			
			for(TiivsSolicitudNivel solxNivel:combosMB.getLstSolNiveles())
			{
				if(solxNivel.getTiivsSolicitud().getCodSoli().equals(tmpSol.getCodSoli()))
				{
					if(nivelesEncontrados > 0)
					sbNiveles.append(",");
					
					sbNiveles.append(buscarNivelxCodigo(solxNivel.getCodNiv()));
					nivelesEncontrados++;
				}
				
			}
			
			tmpSol.setTxtNivel(sbNiveles.toString());
			
			//Cargar data de poderdantes
		/*	cadena="";
			for (TiivsAgrupacionPersona tmp: combosMB.getLstTiposPersona())
			{
				if (tmp.getCodSoli().equals(tmpSol.getCodSoli()) && tmp.getClasifPer().equals(ConstantesVisado.PODERDANTE))
				{
					if (tmp.getClasifPer().equals(combosMB.getLstTipoRegistroPersona().get(0).getKey()))
					{
						cadena += devolverDesTipoDOI(tmp.getTiivsPersona().getTipDoi()) + ConstantesVisado.DOS_PUNTOS + tmp.getTiivsPersona().getNumDoi() +
								  ConstantesVisado.GUION + tmp.getTiivsPersona().getApePat() + " " + tmp.getTiivsPersona().getApeMat() + " " + 
								  tmp.getTiivsPersona().getNombre() + ConstantesVisado.SLASH + ConstantesVisado.SALTO_LINEA;
					}
				}
			}
			tmpSol.setTxtPoderdante(cadena);*/
			
			//Cargar data de poderdantes
			List<TiivsPersona> lstPoderdantes = new ArrayList<TiivsPersona>();
			List<TiivsPersona> lstApoderdantes = new ArrayList<TiivsPersona>();
			AgrupacionSimpleDto agrupacionSimpleDto  =new AgrupacionSimpleDto(); 
			 List<TiivsPersona>lstPersonas=new ArrayList<TiivsPersona>();
			TiivsPersona objPersona=new TiivsPersona();
			
		    lstPoderdantes = new ArrayList<TiivsPersona>();
		    lstApoderdantes = new ArrayList<TiivsPersona>();
		   
		    /*
			 * CAMBIO 25/07/2014 HVB **INICIO***
			 * SE COMENTA BUCLE PARA OBTENER HEREDEROS_APODERADOS/TITULARES PUESTO QUE LA CONCATENACION 
			 * SE TRAE DE LA TABLA SOLICITUD
			 */
		    /*
		    if(listaSolicitudAgrupacion!=null && !listaSolicitudAgrupacion.isEmpty()){
		    	//for (TiivsSolicitudAgrupacion x : tmpSol.getTiivsSolicitudAgrupacions()) 
		    	for (TiivsSolicitudAgrupacion x : listaSolicitudAgrupacion)
			    {
				   for (TiivsAgrupacionPersona d : x.getTiivsAgrupacionPersonas()) 
				   {
					    if (tmpSol.getCodSoli().equals(d.getCodSoli()) && tmpSol.getCodSoli().equals(x.getId().getCodSoli()))
					    {
					    	//logger.info("d.getTiivsPersona() "+d.getTiivsPersona().getTipDoi());
					    	lstAgrupacionSimpleDto = new ArrayList<AgrupacionSimpleDto>();
						    objPersona=new TiivsPersona();
						    objPersona=d.getTiivsPersona();
						    objPersona.setTipPartic(d.getTipPartic());
						    objPersona.setsDesctipPartic(this.obtenerDescripcionTipoRegistro(d.getTipPartic().trim()));
						    objPersona.setClasifPer(d.getClasifPer());
						    objPersona.setsDescclasifPer(this.obtenerDescripcionClasificacion(d.getClasifPer().trim()));
						    objPersona.setsDesctipDoi(this.obtenerDescripcionDocumentos(d.getTiivsPersona().getTipDoi().trim()));
						    lstPersonas.add(objPersona);
							  
						    if(d.getTipPartic().trim().equals(ConstantesVisado.PODERDANTE))
						    {
								lstPoderdantes.add(d.getTiivsPersona());
						    }
							else if(d.getTipPartic().trim().equals(ConstantesVisado.APODERADO))
							{
								lstApoderdantes.add(d.getTiivsPersona());
							}else if(d.getTipPartic().trim().equals(ConstantesVisado.TIPO_PARTICIPACION.CODIGO_HEREDERO))
							{
								lstApoderdantes.add(d.getTiivsPersona());
							}
						    
						    agrupacionSimpleDto = new AgrupacionSimpleDto();
							agrupacionSimpleDto.setId(new TiivsSolicitudAgrupacionId(
									tmpSol.getCodSoli(), x.getId()
											.getNumGrupo()));
							agrupacionSimpleDto.setLstPoderdantes(lstPoderdantes);
							agrupacionSimpleDto.setLstApoderdantes(lstApoderdantes);
							agrupacionSimpleDto.setsEstado(Utilitarios
									.obternerDescripcionEstado(x.getEstado().trim()));
							agrupacionSimpleDto.setLstPersonas(lstPersonas);
							lstAgrupacionSimpleDto.add(agrupacionSimpleDto);
					    }
				   }
			    }
		    }
		    */
		    /*
			 * CAMBIO 25/07/2014 HVB ***FIN****
			 */
		    
		    
		    /*
			 * CAMBIO 25/07/2014 HVB **INICIO***
			 * SE COMENTA BUCLE PARA CONCATENAR LOS PODERDANTES PUESTO QUE SE TRAE LA CADENA DE 
			 * TABLA SOLICITUD
			 */
		    
		    /*
		    cadena="";
		    for (TiivsPersona tmpPoder: lstPoderdantes)
		    {
		    	cadena += devolverDesTipoDOI(tmpPoder.getTipDoi()) + ConstantesVisado.DOS_PUNTOS + tmpPoder.getNumDoi() +
						  ConstantesVisado.GUION + (tmpPoder.getApePat()==null?"":tmpPoder.getApePat()) + " " + (tmpPoder.getApeMat()==null?"":tmpPoder.getApeMat()) + " " + 
						  (tmpPoder.getNombre()==null?"":tmpPoder.getNombre()) + ConstantesVisado.SLASH + ConstantesVisado.SALTO_LINEA;
		    	
		    }
		    
		    tmpSol.setTxtPoderdante(cadena);
		    */
		    
		    /*
			 * CAMBIO 25/07/2014 HVB **FIN***
			 */
		    
		    //logger.debug("[ActDatosGrilla]-TxtPoderdante:"+tmpSol.getTxtPoderdante());
			
			// Cargar data de apoderados
			//cadena=""; --CAMBIO 25/07/2014 HVB 
			/*for (TiivsAgrupacionPersona tmp: combosMB.getLstTiposPersona())
			{
				if (tmp.getCodSoli().equals(tmpSol.getCodSoli()) && tmp.getClasifPer().equals(ConstantesVisado.APODERADO))
				{
					if (tmp.getClasifPer().equals(combosMB.getLstTipoRegistroPersona().get(0).getKey()))
					{
						cadena += devolverDesTipoDOI(tmp.getTiivsPersona().getTipDoi()) + ConstantesVisado.DOS_PUNTOS + tmp.getTiivsPersona().getNumDoi() +
								  ConstantesVisado.GUION + tmp.getTiivsPersona().getApePat() + " " + tmp.getTiivsPersona().getApeMat() + " " + 
								  tmp.getTiivsPersona().getNombre() + ConstantesVisado.SLASH + ConstantesVisado.SALTO_LINEA;
					}
				}
			}*/
			
		    /*
			 * CAMBIO 25/07/2014 HVB **INICIO***
			 * SE COMENTA BUCLE PARA CONCATENAR LOS APODERDANTES PUESTO QUE SE TRAE LA CADENA DE 
			 * TABLA SOLICITUD
			 */
		    /*
			for (TiivsPersona tmpApor: lstApoderdantes)
		    {
		    	cadena += devolverDesTipoDOI(tmpApor.getTipDoi()) + ConstantesVisado.DOS_PUNTOS + tmpApor.getNumDoi() +
						  ConstantesVisado.GUION + (tmpApor.getApePat()== null ?"":tmpApor.getApePat()) + " " + (tmpApor.getApeMat()==null?"":tmpApor.getApeMat()) + " " + 
						  (tmpApor.getNombre()==null?"": tmpApor.getNombre()) + ConstantesVisado.SLASH + ConstantesVisado.SALTO_LINEA;
		    }
			
			tmpSol.setTxtApoderado(cadena);
			*/
		    /*
			 * CAMBIO 25/07/2014 HVB **FIN***
			 */
		    
			//logger.debug("[ActDatosGrilla]-TxtApoderado:"+tmpSol.getTxtApoderado());
			//Carga las operaciones bancarias asociadas a una solicitud
//			cadena=""; --CAMBIO HVB 25/07/2014
			
			// [16-10] Se agrega para mostrar las Operaciones Bancarias en la grilla, antes no se mostraba.
			//listarOperacionesBancarias();
			
			
//			long inicioOper = System.currentTimeMillis();
//			if(tmpSol.getLstSolicBancarias()!=null){
		    /*
			 * CAMBIO 25/07/2014 HVB **INICIO***
			 * SE COMENTA BUCLE PARA CONCATENAR LAS OPERACIONES BANCARIAS PUESTO QUE SE TRAE LA CADENA DE
			 * TABLA SOLICITUD
			 */
		    /*
			if(lstSolOperBan!=null && !lstSolOperBan.isEmpty()){
				for (TiivsSolicitudOperban tmp: lstSolOperBan)
//				for (TiivsSolicitudOperban tmp: tmpSol.getLstSolicBancarias())
				{
					if (tmp.getId().getCodSoli().equals(tmpSol.getCodSoli()))
					{
						cadena +=  devolverDesOperBan(tmp.getId().getCodOperBan())  + ConstantesVisado.SLASH + ConstantesVisado.SALTO_LINEA;
					}
				}				
			}
			
			tmpSol.setTxtOpeBan(cadena);
			*/
		    /*
			 * CAMBIO 25/07/2014 HVB **FIN***
			 */
		    
//			logger.debug("Tiempo de respuesta seteando operaciones bancarias: " + (System.currentTimeMillis()-inicioOper)/1000 + " segundos");
			//logger.debug("[ActDatosGrilla]-TxtOpeBan:"+tmpSol.getTxtOpeBan());
			
			//Ordenar niveles
//			String cadNiveles = ""; --CAMBIO 25/07/2014 HVB
			/*int codNivelTMP = 0;
			String codNiv ="";
			List<TiivsSolicitudNivel> tmpListaOrdenada = new ArrayList<TiivsSolicitudNivel>();
			
			//Ordenar niveles
			for (Iterator iterator = tmpSol.getTiivsSolicitudNivels().iterator(); iterator.hasNext();) 
			{
				TiivsSolicitudNivel tmp = (TiivsSolicitudNivel) iterator.next();
				
				if (codNivelTMP < tmp.getId());
				{
					codNivelTMP = tmp.getId();
					codNiv=tmp.getCodNiv();
					
					TiivsSolicitudNivel tmpSolN = new TiivsSolicitudNivel();
					tmpSolN.setId(tmp.getId());
					tmpSolN.setCodNiv(codNiv);
										
					tmpListaOrdenada.add(tmpSolN);
				}
			}*/
			
			
			//Generar lista de niveles string
//			long inicioNivel = System.currentTimeMillis();
//			List<String> tmpLista = new ArrayList<String>(); ---CAMBIO 25/07/2014 HVB
			
			/*for (Iterator iterator = tmpSol.getTiivsSolicitudNivels().iterator(); iterator.hasNext();) 
			{
				TiivsSolicitudNivel tmp = (TiivsSolicitudNivel) iterator.next();
				
				//String nivel = nivelService.buscarNivelxCodigo(tmp.getCodNiv()); ////////////////
				String nivel = buscarDescripcionXNivel(tmp.getCodNiv(), listaNiveles);
				
				tmpLista.add(nivel);
			}*/
			
		    /*
			 * CAMBIO 25/07/2014 HVB **INICIO***
			 * SE COMENTA BUCLE PARA CONCATENAR LAS NIVELES PUESTO QUE SE TRAE DE LA			 
			 * TABLA SOLICITUD LA CADEN
			 */
			
//			if(listaSolicitudNiveles!=null && !listaSolicitudNiveles.isEmpty()){
//				for(TiivsSolicitudNivel solNivel:listaSolicitudNiveles){
//					if(solNivel.getTiivsSolicitud()!=null && 
//							solNivel.getTiivsSolicitud().getCodSoli().compareTo(tmpSol.getCodSoli())==0){
//						String nivel = buscarDescripcionXNivel(solNivel.getCodNiv(), listaNiveles);
//						tmpLista.add(nivel);
//					}
//				}
//			}
			
//			Collection<String> unsorted = tmpLista;
//			List<String> sorted = Utilitarios.ordenarLista(unsorted);
//			
//			
//			for (Iterator iterator = sorted.iterator(); iterator.hasNext();) 
//			{
				//TiivsSolicitudNivel tmp = (TiivsSolicitudNivel) iterator.next();
				
				///String nivel = nivelService.buscarNivelxCodigo(tmp.getCodNiv());
								
//				if (cadNiveles.length()>0)
//				{
//					cadNiveles = cadNiveles.concat(",").concat(iterator.next().toString());
//				}
//				else
//				{
//					cadNiveles = cadNiveles.concat(iterator.next().toString());
//				}
//			}
//			
//			if (cadNiveles.endsWith(","))
//			{
//				cadNiveles = cadNiveles.substring(0,cadNiveles.length()-1);
//			}
			
			//logger.info("Niveles encontrados:" + cadNiveles);
			
//			tmpSol.setTxtNivel(cadNiveles);
		    /*
			 * CAMBIO 25/07/2014 HVB **FIN***
			 */
//			logger.debug("Tiempo de respuesta seteando niveles: " + (System.currentTimeMillis()-inicioNivel)/1000 + " segundos");
			
			//Proceso para obtener los niveles de cada solicitud
		/*	if (tmpSol.getImporte() != 0) 
			{
				if (combosMB.getLstNivel().size() > 0) 
				{
					String txtNivelTMP = "";
					String descripcion = buscarDescripcionMoneda(tmpSol.getMoneda());
					//logger.debug("Moneda encontrada: " + descripcion);

					for (TiivsNivel tmp : combosMB.getLstNivel()) 
					{
						if (tmp.getMoneda().equalsIgnoreCase(tmpSol.getMoneda())) 
						{
							if (tmp.getDesNiv().equalsIgnoreCase(ConstantesVisado.CAMPO_NIVEL1)) 
							{
								importeTMP = tmpSol.getImporte();
								rangoIni = Double.valueOf(tmp.getRangoInicio());
								rangoFin = Double.valueOf(tmp.getRangoFin());

								if (importeTMP.compareTo(rangoIni) >= 0 && importeTMP.compareTo(rangoFin) <= 0) 
								{
									txtNivelTMP += ConstantesVisado.CAMPO_NIVEL1;
								}
							}

							if (tmp.getDesNiv().equalsIgnoreCase(ConstantesVisado.CAMPO_NIVEL2)) 
							{
								importeTMP = tmpSol.getImporte();
								rangoIni =  Double.valueOf(tmp.getRangoInicio());
								rangoFin =  Double.valueOf(tmp.getRangoFin());

								if (importeTMP.compareTo(rangoIni) >= 0 && importeTMP.compareTo(rangoFin) <= 0) 
								{
									if (txtNivelTMP.length() > 0) 
									{
										txtNivelTMP += "," + ConstantesVisado.CAMPO_NIVEL2;
									} 
									else 
									{
										txtNivelTMP += ConstantesVisado.CAMPO_NIVEL2;
									}
								}
							}

							if (tmp.getDesNiv().equalsIgnoreCase(ConstantesVisado.CAMPO_NIVEL3)) 
							{
								importeTMP = tmpSol.getImporte();
								rangoIni = Double.valueOf(tmp.getRangoInicio());
								rangoFin = Double.valueOf(tmp.getRangoFin());

								if (importeTMP.compareTo(rangoIni) >= 0 && importeTMP.compareTo(rangoFin) <= 0) 
								{
									if (txtNivelTMP.length() > 0) 
									{
										txtNivelTMP += "," 	+ ConstantesVisado.CAMPO_NIVEL3;
									} 
									else 
									{
										txtNivelTMP += ConstantesVisado.CAMPO_NIVEL3;
									}
								}
							}
							
							if (tmp.getDesNiv().equalsIgnoreCase(ConstantesVisado.CAMPO_NIVEL4)) 
							{
								importeTMP = tmpSol.getImporte();
								rangoIni = Double.valueOf(tmp.getRangoInicio());
								rangoFin = Double.valueOf(tmp.getRangoFin());

								if (importeTMP.compareTo(rangoIni) >= 0 && importeTMP.compareTo(rangoFin) <= 0) 
								{
									if (txtNivelTMP.length() > 0) 
									{
										txtNivelTMP += "," 	+ ConstantesVisado.CAMPO_NIVEL4;
									} 
									else 
									{
										txtNivelTMP += ConstantesVisado.CAMPO_NIVEL4;
									}
								}
							}
						}

					}
					
					tmpSol.setTxtNivel(txtNivelTMP);
				}
				else
				{
					
					 * txtNivelTMP+=ConstantesVisado.CAMPO_NIVEL4;
					 * tmpSol.setTxtNivel(txtNivelTMP);
					 
				}
			}*/
			/*else 
			{
				logger.debug("No se pudo obtener los rangos de los niveles para la solicitud. Verificar base de datos!!");
			}*/
		}
		
		logger.debug("Tiempo de respuesta actualizando grilla de solicitudes: " + (System.currentTimeMillis()-inicio)/1000 + " segundos");
		logger.debug("Tiempo de respuesta loop: " + (System.currentTimeMillis()-inicioLOOP)/1000 + " segundos");
	}
	
	private String buscarDescripcionXNivel(String codigo, List<TiivsNivel> listaNiveles){
		if(listaNiveles!=null && !listaNiveles.isEmpty()){
			for(TiivsNivel nivel:listaNiveles){
				if(codigo!=null && codigo.compareTo(nivel.getCodNiv())==0){
					return nivel.getDesNiv();
				}
			}
		}
		return null;
	}
	
	public String obtenerGenerador()
	{
		String resultado="";
		
		if (usuario != null)
		{
			resultado = usuario.getUID() + ConstantesVisado.GUION + usuario.getNombre() +  ConstantesVisado.ESPACIO_BLANCO + 
						usuario.getApellido1() + ConstantesVisado.ESPACIO_BLANCO + usuario.getApellido2();
		}
		else
		{
			logger.debug("Error al obtener datos del usuario de session para mostrar en el excel");
		}
		return resultado;
	}
	
	public void exportarExcelPOI()
	{
		crearExcel();
	}
	
	public void generarNombreArchivo() 
	{
		String grupoSSJJ = (String) Utilitarios.getObjectInSession("GRUPO_JRD");
		String grupoADM = (String) Utilitarios.getObjectInSession("GRUPO_ADM");
		String grupoOFI = (String) Utilitarios.getObjectInSession("GRUPO_OFI");
		String rol="";
		
		if (grupoSSJJ!=null)
		{
			rol="SSJJ";
		}
		else if (grupoADM!=null)
		{
			rol="ADM";
		}
		else if (grupoOFI!=null)
		{
			rol="OFI";
		}
		
		setNombreArchivoExcel("Solicitudes_Visado "	+ Utilitarios.obtenerFechaArchivoExcel() + ConstantesVisado.UNDERLINE + rol);
	}

	
	private void crearExcel() 
	{
		try 
		{
			// Defino el Libro de Excel
			HSSFWorkbook wb = new HSSFWorkbook();

			// Creo la Hoja en Excel
			Sheet sheet = wb.createSheet(Utilitarios.obtenerFechaArchivoExcel());

			// quito las lineas del libro para darle un mejor acabado
			sheet.setDisplayGridlines(false);
			//sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

			// creo una nueva fila
			Row trow = sheet.createRow((short) 0);
			Utilitarios.crearTituloCell(wb, trow, 4, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, ConstantesVisado.TITULO_CABECERA_EXCEL,12);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 6));
			
			//Se crea la leyenda de quien genero el archivo y la hora respectiva
			Row rowG = sheet.createRow((short) 1);
			Utilitarios.crearCell(wb, rowG, 10, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_GENERADOR, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			Utilitarios.crearCell(wb, rowG, 11, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, obtenerGenerador(),  true, false,true,HSSFColor.GREY_25_PERCENT.index);
			
			Row rowG1 = sheet.createRow((short) 2);
			Utilitarios.crearCell(wb, rowG1, 10, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_FECHA_HORA, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			Utilitarios.crearCell(wb, rowG1, 11, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, Utilitarios.obtenerFechaHoraActual(),  true, false,true,HSSFColor.GREY_25_PERCENT.index);
			
			//Genera celdas con los filtros de busqueda
			Row row2 = sheet.createRow((short) 4);
			
			Utilitarios.crearCell(wb, row2, 1, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_NRO_SOL, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			if (getCodSolicitud()!=null)
			{
				Utilitarios.crearCell(wb, row2, 2, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, getCodSolicitud(), true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row2, 2, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Utilitarios.crearCell(wb, row2, 4, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_ESTADO, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			if (lstEstadoSelected!=null)
			{
				String cadena = "";
								
				int j=0;
				int cont=1;
				
				for (;j<=lstEstadoSelected.size()-1;j++)
				{
					if (lstEstadoSelected.size()>1)
					{
						if (cont==lstEstadoSelected.size())
						{
							cadena=cadena.concat(buscarEstadoxCodigo((lstEstadoSelected.get(j).toString())));
						}
						else
						{
							cadena=cadena.concat(buscarEstadoxCodigo(lstEstadoSelected.get(j).toString()).concat(","));
							cont++;
						}
					}
					else
					{
						cadena = buscarEstadoxCodigo(lstEstadoSelected.get(j).toString());
					}		
				}
				
				Utilitarios.crearCell(wb, row2, 5, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, cadena, true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row2, 5, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Utilitarios.crearCell(wb, row2, 7, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_IMPORTE, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			
			if (getIdImporte().equals(ConstantesVisado.ID_RANGO_IMPORTE_MENOR_CINCUENTA)) 
			{
				Utilitarios.crearCell(wb, row2, 8, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER,  ConstantesVisado.RANGOS_IMPORTE.RANGO_IMPORTE_NRO_1, true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else if (getIdImporte().equals(ConstantesVisado.ID_RANGO_IMPORTE_MAYOR_CINCUENTA_MENOR_CIENTO_VEINTE)) 
			{
				Utilitarios.crearCell(wb, row2, 8, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER,  ConstantesVisado.RANGOS_IMPORTE.RANGO_IMPORTE_NRO_2, true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else if (getIdImporte().equals(ConstantesVisado.ID_RANGO_IMPORTE_MAYOR_CIENTO_VEINTE_MENOR_DOSCIENTOS_CINCUENTA)) 
			{
				Utilitarios.crearCell(wb, row2, 8, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER,  ConstantesVisado.RANGOS_IMPORTE.RANGO_IMPORTE_NRO_3, true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else if (getIdImporte().equals(ConstantesVisado.ID_RANGO_IMPORTE_MAYOR_DOSCIENTOS_CINCUENTA)) 
			{
				Utilitarios.crearCell(wb, row2, 8, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER,  ConstantesVisado.RANGOS_IMPORTE.RANGO_IMPORTE_NRO_4, true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row2, 8, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Utilitarios.crearCell(wb, row2, 10, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_TIPO_SOL, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			
			if (lstTipoSolicitudSelected!=null)
			{
				String cadena = "";
								
				int j=0;
				int cont=1;
				
				for (;j<=lstTipoSolicitudSelected.size()-1;j++)
				{
					if (lstTipoSolicitudSelected.size()>1)
					{
						if (cont==lstTipoSolicitudSelected.size())
						{
							cadena=cadena.concat(buscarTipoSolxCodigo((lstTipoSolicitudSelected.get(j).toString())));
						}
						else
						{
							cadena=cadena.concat(buscarTipoSolxCodigo(lstTipoSolicitudSelected.get(j).toString()).concat(","));
							cont++;
						}
					}
					else
					{
						cadena = buscarTipoSolxCodigo(lstTipoSolicitudSelected.get(j).toString());
					}		
				}
				
				Utilitarios.crearCell(wb, row2, 11, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, cadena, true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row2, 11, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Row row3 = sheet.createRow((short) 5);
			Utilitarios.crearCell(wb, row3, 1, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_TIPO_FECHA, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			
			if (getIdTiposFecha().compareTo("")!=0)
			{
				String cadena = "";
				cadena= buscarTipoFechaxCodigo(getIdTiposFecha());
								
				Utilitarios.crearCell(wb, row3, 2, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, cadena, true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row3, 2, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Utilitarios.crearCell(wb, row3, 4, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_FECHA_INICIO, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			
			if (getFechaInicio()!=null)
			{
				SimpleDateFormat sf1 = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
				String sFechaInicio = sf1.format(getFechaInicio());
				
				Utilitarios.crearCell(wb, row3, 5, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, sFechaInicio, true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row3, 5, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Utilitarios.crearCell(wb, row3, 7, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_FECHA_FIN, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			
			if (getFechaFin()!=null)
			{
				SimpleDateFormat sf1 = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
				String sFechaFin = sf1.format(getFechaFin());
				
				Utilitarios.crearCell(wb, row3, 8, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER,sFechaFin, true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row3, 8, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Utilitarios.crearCell(wb, row3, 10, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_TIPO_OPE, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			
			if (getIdOpeBan().compareTo("")!=0)
			{
				Utilitarios.crearCell(wb, row3, 11, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, buscarOpeBanxCodigo(getIdOpeBan()), true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row3, 11, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Row row5 = sheet.createRow((short) 7);
			
			Utilitarios.crearCell(wb, row5, 1, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_DOI_APODERADO, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			
			if (getNroDOIApoderado()!=null)
			{
				Utilitarios.crearCell(wb, row5, 2, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, getNroDOIApoderado(), true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row5, 2, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Utilitarios.crearCell(wb, row5, 4, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_APODERADO, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			
			if (getTxtNomApoderado()!=null)
			{
				Utilitarios.crearCell(wb, row5, 5, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, getTxtNomApoderado(), true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row5, 5, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Utilitarios.crearCell(wb, row5, 7, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_DOI_PODERDANTE, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			
			if (getNroDOIPoderdante()!=null)
			{
				Utilitarios.crearCell(wb, row5, 8, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, getNroDOIPoderdante(), true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row5, 8, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Utilitarios.crearCell(wb, row5, 10, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_PODERDANTE, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			
			if (getTxtNomPoderdante()!=null)
			{
				Utilitarios.crearCell(wb, row5, 11, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, getTxtNomPoderdante(), true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row5, 11, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Row row6 = sheet.createRow((short) 8);
			Utilitarios.crearCell(wb, row6, 1, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_OFICINA, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			
			if (getOficina()!=null)
			{
				Utilitarios.crearCell(wb, row6, 2, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, getOficina().getDesOfi(), true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row6, 2, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Utilitarios.crearCell(wb, row6, 4, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_NIVEL, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			
			if (getLstNivelSelected()!=null)
			{
				String cadena = "";
				
				int j=0;
				int cont=1;
				
				for (;j<=getLstNivelSelected().size()-1;j++)
				{
					if (getLstNivelSelected().size()>1)
					{
						if (cont==getLstNivelSelected().size())
						{
							cadena=cadena.concat(getLstNivelSelected().get(j).toString());
						}
						else
						{
							cadena=cadena.concat(getLstNivelSelected().get(j).toString()).concat(",");
							cont++;
						}
					}
					else
					{
						cadena = getLstNivelSelected().get(j).toString();
					}		
				}
				
				Utilitarios.crearCell(wb, row6, 5, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, cadena, true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row6, 5, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Utilitarios.crearCell(wb, row6, 7, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_ESTADO_NIVEL, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			
			if (getLstEstadoNivelSelected()!=null)
			{
				String cadena = "";
				
				int j=0;
				int cont=1;
				
				for (;j<=getLstEstadoNivelSelected().size()-1;j++)
				{
					if (getLstEstadoNivelSelected().size()>1)
					{
						if (cont==getLstEstadoNivelSelected().size())
						{
							cadena=cadena.concat(buscarEstNivelxCodigo((getLstEstadoNivelSelected().get(j).toString())));
						}
						else
						{
							cadena=cadena.concat(buscarEstNivelxCodigo(getLstEstadoNivelSelected().get(j).toString()).concat(","));
							cont++;
						}
					}
					else
					{
						cadena = buscarEstNivelxCodigo(getLstEstadoNivelSelected().get(j).toString());
					}		
				}
				
				Utilitarios.crearCell(wb, row6, 8, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, cadena, true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row6, 8, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Utilitarios.crearCell(wb, row6, 10, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_ESTUDIO,false, false,false,HSSFColor.GREY_25_PERCENT.index);
			
			if (getLstEstudioSelected()!=null)
			{
				String cadena = "";
				
				int j=0;
				int cont=1;
				
				for (;j<=getLstEstudioSelected().size()-1;j++)
				{
					if (getLstEstudioSelected().size()>1)
					{
						if (cont==getLstEstudioSelected().size())
						{
							cadena=cadena.concat(buscarEstudioxCodigo((getLstEstudioSelected().get(j).toString())));
						}
						else
						{
							cadena=cadena.concat(buscarEstudioxCodigo(getLstEstudioSelected().get(j).toString()).concat(","));
							cont++;
						}
					}
					else
					{
						cadena = buscarEstudioxCodigo(getLstEstudioSelected().get(j).toString());
					}		
				}
				
				Utilitarios.crearCell(wb, row6, 11, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, cadena, true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row6, 11, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Row row7 = sheet.createRow((short) 9);
			Utilitarios.crearCell(wb, row7, 1, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_REVISION, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			
			if (bRevision)
			{
				Utilitarios.crearCell(wb, row7, 2, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "Si", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row7, 2, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "No", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Utilitarios.crearCell(wb, row7, 4, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_DELEGADO, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			
			if (bDelegados)
			{
				Utilitarios.crearCell(wb, row7, 5, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "Si", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row7, 5, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "No", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			
			Utilitarios.crearCell(wb, row7, 7, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, ConstantesVisado.ETIQUETA_FILTRO_BUS_REVOCATORIA, false, false,false,HSSFColor.GREY_25_PERCENT.index);
			
			if (bRevocatoria)
			{
				Utilitarios.crearCell(wb, row7, 8, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "Si", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
			else
			{
				Utilitarios.crearCell(wb, row7, 8, CellStyle.ALIGN_LEFT,CellStyle.VERTICAL_CENTER, "No", true, false,true,HSSFColor.GREY_25_PERCENT.index);
			}
		
			if (solicitudes.size()==0)
			{
				logger.info("Sin registros para exportar");
			}
			else
			{
				// Se crea la cabecera de la tabla de resultados
				Row rowT = sheet.createRow((short) 12);
				// Se crea el estilo de las celdas 
				CellStyle cellStyleCab = wb.createCellStyle();
				cellStyleCab.setAlignment(CellStyle.ALIGN_LEFT);
				cellStyleCab.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
				// Creo las celdas de mi fila, se puede poner un dise�o a la celda
				Utilitarios.crearCell2(wb, rowT, 0, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_ITEM, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 1, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_NRO_SOLICITUD, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 2, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_COD_OFICINA, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 3, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_OFICINA, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 4, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_TERRITORIO, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 5, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_ESTADO, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 6, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_IMPORTE, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 7, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_PODERDANTE, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 8, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_APODERADO, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 9, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_TIPO_SOL, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 10, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_TIPO_OPE, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 11, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_ESTUDIO, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 12, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_NIVEL, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 13, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_FECHA_ENVIO, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 14, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_FECHA_RPTA, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 15, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_FECHA_ESTADO, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 16, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_COMISION, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 17, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_LIBERADO, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 18, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_DELEGADO, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 19, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_EN_REVISION, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				Utilitarios.crearCell2(wb, rowT, 20, cellStyleCab, ConstantesVisado.ETIQUETA_COLUMNA_REVOCATORIA, true, true,false,HSSFColor.GREY_25_PERCENT.index);
				
				int numReg=13;
				int contador=0;
				for (TiivsSolicitud tmp: solicitudes)
				{
					contador++;
					//Columna Item en Excel
					Row row = sheet.createRow((short) numReg);
					CellStyle cellStyleDet = wb.createCellStyle();
					cellStyleDet.setAlignment(CellStyle.ALIGN_LEFT);
					cellStyleDet.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
					if (contador<=9)
					{
						Utilitarios.crearCell2(wb, row, 0, cellStyleDet, ConstantesVisado.TRES_CEROS + contador, true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					else if (contador<=99 && contador >9)
					{
						Utilitarios.crearCell2(wb, row, 0, cellStyleDet, ConstantesVisado.DOS_CEROS + contador, true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					else if(contador>=99 && contador<999)
					{
						Utilitarios.crearCell2(wb, row, 0, cellStyleDet, ConstantesVisado.CERO + contador, true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					else
					{
						Utilitarios.crearCell2(wb, row, 0, cellStyleDet, String.valueOf(contador), true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					
					//Columna Nro Solicitud en Excel
					Utilitarios.crearCell2(wb, row, 1, cellStyleDet, tmp.getCodSoli(), true, false,true,HSSFColor.GREY_25_PERCENT.index);
										
					//Columna Cod Oficina en Excel
					Utilitarios.crearCell2(wb, row, 2, cellStyleDet, Utilitarios.validarCampoNull(tmp.getTiivsOficina1().getCodOfi()),true, false,true,HSSFColor.GREY_25_PERCENT.index);
					
					//Columna Oficina en Excel
					Utilitarios.crearCell2(wb, row, 3, cellStyleDet, Utilitarios.validarCampoNull(tmp.getTiivsOficina1().getDesOfi()),true, false,true,HSSFColor.GREY_25_PERCENT.index);
					
					//Columna Territorio en Excel
					Utilitarios.crearCell2(wb, row, 4, cellStyleDet, buscarDesTerritorio(tmp.getTiivsOficina1().getTiivsTerritorio().getCodTer()),true, false,true,HSSFColor.GREY_25_PERCENT.index);
					
					//Columna Estado en Excel
					Utilitarios.crearCell2(wb, row, 5, cellStyleDet, tmp.getTxtEstado(), true, false,true,HSSFColor.GREY_25_PERCENT.index);
					
					//Columna Importe en Excel
					Utilitarios.crearCell2(wb, row, 6, cellStyleDet, tmp.getTxtImporte(), true, false,true,HSSFColor.GREY_25_PERCENT.index);
					
					//Columna Poderdante en Excel
					Utilitarios.crearCell2(wb, row, 7, cellStyleDet, tmp.getTxtPoderdante(), true, false,true,HSSFColor.GREY_25_PERCENT.index);
					
					//Columna Apoderado en Excel
					Utilitarios.crearCell2(wb, row, 8, cellStyleDet, tmp.getTxtApoderado(), true, false,true,HSSFColor.GREY_25_PERCENT.index);
					
					//Columna Tipo Solicitud en Excel
					Utilitarios.crearCell2(wb, row, 9, cellStyleDet, Utilitarios.validarCampoNull(tmp.getTiivsTipoSolicitud().getDesTipServicio()), true, false,true,HSSFColor.GREY_25_PERCENT.index);
					
					//Columna Operaciones Bancarias en Excel
					Utilitarios.crearCell2(wb, row, 10, cellStyleDet, tmp.getTxtOpeBan(), true, false,true,HSSFColor.GREY_25_PERCENT.index);
					
					//Columna Estudio en Excel
					if (tmp.getTiivsEstudio()!=null)
					{
						Utilitarios.crearCell2(wb, row, 11, cellStyleDet, Utilitarios.validarCampoNull(tmp.getTiivsEstudio().getDesEstudio()) , true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					else
					{
						Utilitarios.crearCell2(wb, row, 11, cellStyleDet, Utilitarios.validarCampoNull(null),true,false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					
					//Columna Nivel en Excel
					Utilitarios.crearCell2(wb, row, 12, cellStyleDet, tmp.getTxtNivel(), true, false,true,HSSFColor.GREY_25_PERCENT.index);
					
					//Columna Fecha Envio en Excel
					if (tmp.getFechaEnvio()!=null)
					{
						Utilitarios.crearCell2(wb, row, 13, cellStyleDet, String.valueOf(tmp.getFechaEnvio()), true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					else
					{
						Utilitarios.crearCell2(wb, row, 13, cellStyleDet, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
							
					//Columna Fecha Rpta en Excel
					if (tmp.getFechaRespuesta()!=null)
					{
						Utilitarios.crearCell2(wb, row, 14, cellStyleDet, String.valueOf(tmp.getFechaRespuesta()), true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					else
					{
						Utilitarios.crearCell2(wb, row, 14, cellStyleDet, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					
					//Columna Fecha Estado en Excel
					if (tmp.getFechaEstado()!=null)
					{
						Utilitarios.crearCell2(wb, row, 15, cellStyleDet, String.valueOf(tmp.getFechaEstado()), true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					else
					{
						Utilitarios.crearCell2(wb, row, 15, cellStyleDet, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					
					//Columna Comision en Excel
					if (tmp.getComision()!=null)
					{
						Utilitarios.crearCell2(wb, row, 16, cellStyleDet, String.valueOf(tmp.getComision()), true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					else
					{
						Utilitarios.crearCell2(wb, row, 16, cellStyleDet, "", true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					
					//Columna Liberado
					if (tmp.getEstado().trim().equals(ConstantesVisado.ESTADOS.ESTADO_COD_RESERVADO_T02))
					{
						Utilitarios.crearCell2(wb, row, 17, cellStyleDet, "No", true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					else
					{
						if (tmp.getEstado().trim().equals(ConstantesVisado.ESTADOS.ESTADO_COD_ENVIADOSSJJ_T02))
						{
							Utilitarios.crearCell2(wb, row, 17, cellStyleDet, "Si", true, false,true,HSSFColor.GREY_25_PERCENT.index);
						}
						else
						{
							Utilitarios.crearCell2(wb, row, 17, cellStyleDet, "No", true, false,true,HSSFColor.GREY_25_PERCENT.index);
						}
					}
					
					//Columna Delegado
					if (validarSolicitudConDelegacion(tmp.getCodSoli()))
					{
						Utilitarios.crearCell2(wb, row, 18, cellStyleDet, "Si", true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					else
					{
						Utilitarios.crearCell2(wb, row, 18, cellStyleDet, "No", true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					
					//Columna En Revision
					if (validarSolicitudEnRevision(tmp.getCodSoli()))
					{
						Utilitarios.crearCell2(wb, row, 19, cellStyleDet, "Si", true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					else
					{
						Utilitarios.crearCell2(wb, row, 19, cellStyleDet, "No", true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					
					//Columna Revocatoria
					if (validarSolicitudRevocada(tmp.getCodSoli()))
					{
						Utilitarios.crearCell2(wb, row, 20, cellStyleDet, "Si", true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					else
					{
						Utilitarios.crearCell2(wb, row, 20, cellStyleDet, "No", true, false,true,HSSFColor.GREY_25_PERCENT.index);
					}
					
					numReg++;
				}
			}
			
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.setColumnWidth(3,256*20);
			sheet.setColumnWidth(4,256*20);
			sheet.setColumnWidth(6,256*15);
			sheet.setColumnWidth(7,256*20);
			sheet.setColumnWidth(8,256*20);
			sheet.autoSizeColumn(9);
			sheet.setColumnWidth(10,256*20);
			sheet.setColumnWidth(11,256*12);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(13);
			sheet.autoSizeColumn(14);
			sheet.autoSizeColumn(15);
			sheet.autoSizeColumn(16);
			sheet.autoSizeColumn(17);
			sheet.autoSizeColumn(18);
			sheet.autoSizeColumn(19);
			sheet.autoSizeColumn(20);
						
			//Se crea el archivo con la informacion y estilos definidos previamente
			String strRuta="";
			if (obtenerRutaExcel().compareTo("")!=0)
			{
				
				logger.info("Parametros recogidos para exportar");
				logger.info("Ruta: " + obtenerRutaExcel());
				logger.info("Nombre Archivo Excel: " + getNombreArchivoExcel());
				
				strRuta = obtenerRutaExcel() + getNombreArchivoExcel() + ConstantesVisado.EXTENSION_XLS;
				logger.info("Nombre strRuta: " + strRuta);
				FileOutputStream fileOut = new FileOutputStream(strRuta);
				wb.write(fileOut);
				
				fileOut.close();
				
				logger.debug("Ruta final donde encontrar el archivo excel: " + strRuta);
				
				setRutaArchivoExcel(strRuta);
			}
						
		} catch (Exception e) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_EXCEPCION+"al generar archivo excel:" ,e);
		}	
	}
	
	public void abrirExcel()
	{
		try {
			exportarExcelPOI();
			//Abrir archivo excel				
			if (rutaArchivoExcel!=null && rutaArchivoExcel.length()>0)
			{
				Desktop.getDesktop().open(new File(rutaArchivoExcel));
			}
		} catch (IOException e) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR+"IO al abrir archivo excel",e);
		} catch (Exception e1){
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR+"al abrir archivo excel",e1);
		}
	}
	
	public void descargarArchivo()
	{
		exportarExcelPOI();
		InputStream stream=null;
		
		try {
			stream = new FileInputStream(rutaArchivoExcel);
		} catch (FileNotFoundException e) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR+"al obtener archivo excel, ya que no existe: ",e);
		} catch (Exception e) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR+"al obtener archivo excel: ",e);
		}
		
		if (stream!=null)
		{
			file = new DefaultStreamedContent(stream, "application/excel", nombreArchivoExcel+ConstantesVisado.EXTENSION_XLS);
		}
	}
	
	public String obtenerRutaExcel()
	{
		String res="";
		
		TiivsParametros tmp = pdfViewerMB.getParametros();		
		res=tmp.getRutaArchivoExcel();
		
//		for (TiivsParametros tmp: pdfViewerMB.getParametros())
//		{
//			if (usuario.getUID().equals(tmp.getCodUsuario()))
//			{				
//				break;
//			}
//		}
		
		if (res.compareTo("")==0)
		{
			logger.debug("No se encontro el parametro de ruta para exportar excel para el usuario: " + usuario.getUID());
		}
		else
		{
			logger.debug("Parametro ruta encontrada para el usuario: " + usuario.getUID() + " es: " +  res);
		}
		
		return res;
	}
	
	public void limpiar()
	{
		setCodSolicitud("");
		setLstEstadoSelected(null);
		setIdImporte("");
		setLstTipoSolicitudSelected(null);
		setIdTiposFecha("");
		setFechaInicio(null);
		setFechaFin(null);
		setIdOpeBan("");
		setNroDOIApoderado("");
		setObjTiivsPersonaBusquedaNomApod(null);
		setNroDOIPoderdante("");
		setObjTiivsPersonaBusquedaNomPoder(null);
		
		if (PERFIL_USUARIO!=null)
		{
			if (!PERFIL_USUARIO.equals(ConstantesVisado.OFICINA))
			{
				setOficina(null);
			}
		}
		
		setLstNivelSelected(null);
		setLstEstadoNivelSelected(null);
		setLstEstudioSelected(null);
		setbRevision(false);
		setbDelegados(false);
		setbRevocatoria(false);
		
		
	}
		
	// Descripcion: Metodo que se encarga de buscar las solicitudes de acuerdo a
	// los filtros seleccionados.
	// @Autor: Cesar La Rosa
	// @Version: 4.0
	// @param: -
	// Corregir: nivel, poder, apod, exportacion filtros de combos con mas de un valor
	/*
	 * CAMBIO 25/07/2014 HVB
	 * SE CAMBIA LA CONSULTA DE HIBERNATE POR JDBC PARA OPTIMIZAR TIEMPO
	 */
	@SuppressWarnings("unchecked")
	public void busquedaSolicitudes() 
	{
		logger.debug("===== busquedaSolicitudes() ===");
//		GenericDao<TiivsSolicitud, Object> solicDAO = (GenericDao<TiivsSolicitud, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
//		Busqueda filtroSol = Busqueda.forClass(TiivsSolicitud.class);
		StringBuilder sbFiltros = new StringBuilder();

		long inicio = System.currentTimeMillis();
		// solicitudes = new ArrayList<TiivsSolicitud>();

		// 1. Filtro por codigo de solicitud (funciona)
		if (getCodSolicitud().compareTo("") != 0) 
		{
			logger.debug("[Seguimiento-busqSolicit]-CodSolicitud:"+getCodSolicitud());
			sbFiltros.append("COD_SOLI LIKE '");
			sbFiltros.append(ConstantesVisado.SIMBOLO_PORCENTAJE);
			sbFiltros.append(getCodSolicitud().concat(ConstantesVisado.SIMBOLO_PORCENTAJE).concat("'"));
			//String filtroNuevo = ConstantesVisado.SIMBOLO_PORCENTAJE + getCodSolicitud().concat(ConstantesVisado.SIMBOLO_PORCENTAJE);
//			filtroSol.add(Restrictions.like(ConstantesVisado.CAMPO_COD_SOLICITUD,"%"+filtroNuevo));
		}

		// 2. Filtro por estado (si funciona, validar que campo estado en BD no tenga espacios en blanco)
		if (lstEstadoSelected.size() > 0) 
		{
			int ind = 0;
			if(sbFiltros.length() > 0)
				sbFiltros.append(" AND ");

			sbFiltros.append("ESTADO IN (");
			for (; ind <= lstEstadoSelected.size() - 1; ind++) 
			{
				logger.info("Filtro por estados: " + lstEstadoSelected.get(ind));
				if(ind != lstEstadoSelected.size() - 1)
				sbFiltros.append(lstEstadoSelected.get(ind).concat(","));
			
				else
					sbFiltros.append(lstEstadoSelected.get(ind));	
			}
			sbFiltros.append(")");
//			filtroSol.add(Restrictions.in(ConstantesVisado.CAMPO_ESTADO,lstEstadoSelected));
		}

		// 3. Filtro por importe (funciona)
		if (getIdImporte().compareTo("") != 0) 
		{
			if(sbFiltros.length() > 0)
				sbFiltros.append(" AND ");
			
			sbFiltros.append("IMPORTE ");
			if (getIdImporte().equals(ConstantesVisado.ID_RANGO_IMPORTE_MENOR_CINCUENTA)) 
			{
				logger.debug("[Seguimiento-busqSolicit]-IdImporte:"+getIdImporte());
				sbFiltros.append("<=");
				sbFiltros.append(ConstantesVisado.VALOR_RANGO_CINCUENTA);
//				filtroSol.add(Restrictions.le(ConstantesVisado.CAMPO_IMPORTE,(ConstantesVisado.VALOR_RANGO_CINCUENTA)));
			}

			if (getIdImporte().equals(ConstantesVisado.ID_RANGO_IMPORTE_MAYOR_CINCUENTA_MENOR_CIENTO_VEINTE)) 
			{
				logger.debug("[Seguimiento-busqSolicit]-IdImporte:"+getIdImporte());
				sbFiltros.append(">");
				sbFiltros.append(ConstantesVisado.VALOR_RANGO_CINCUENTA);
				sbFiltros.append(" AND IMPORTE ");
				sbFiltros.append("<=");
				sbFiltros.append(ConstantesVisado.VALOR_RANGO_CIENTO_VEINTE);
//				filtroSol.add(Restrictions.gt(ConstantesVisado.CAMPO_IMPORTE,ConstantesVisado.VALOR_RANGO_CINCUENTA));
//				filtroSol.add(Restrictions.le(ConstantesVisado.CAMPO_IMPORTE,ConstantesVisado.VALOR_RANGO_CIENTO_VEINTE));
			}

			if (getIdImporte().equals(ConstantesVisado.ID_RANGO_IMPORTE_MAYOR_CIENTO_VEINTE_MENOR_DOSCIENTOS_CINCUENTA)) 
			{
				logger.debug("[Seguimiento-busqSolicit]-IdImporte:"+getIdImporte());
				sbFiltros.append(">");
				sbFiltros.append(ConstantesVisado.VALOR_RANGO_CIENTO_VEINTE);
				sbFiltros.append(" AND IMPORTE ");
				sbFiltros.append("<=");
				sbFiltros.append(ConstantesVisado.VALOR_RANGO_DOSCIENTOS_CINCUENTA);
//				filtroSol.add(Restrictions.gt(ConstantesVisado.CAMPO_IMPORTE,(ConstantesVisado.VALOR_RANGO_CIENTO_VEINTE)));
//				filtroSol.add(Restrictions.le(ConstantesVisado.CAMPO_IMPORTE,(ConstantesVisado.VALOR_RANGO_DOSCIENTOS_CINCUENTA)));
			}

			if (getIdImporte().equals(ConstantesVisado.ID_RANGO_IMPORTE_MAYOR_DOSCIENTOS_CINCUENTA)) 
			{
				logger.debug("[Seguimiento-busqSolicit]-IdImporte:"+getIdImporte());
				sbFiltros.append(">");
				sbFiltros.append(ConstantesVisado.VALOR_RANGO_DOSCIENTOS_CINCUENTA);
//				filtroSol.add(Restrictions.gt(ConstantesVisado.CAMPO_IMPORTE,(ConstantesVisado.VALOR_RANGO_DOSCIENTOS_CINCUENTA)));
			}
		}

		// 4. Filtro por tipo de solicitud (funciona)
		if (lstTipoSolicitudSelected.size() > 0) {
			int ind = 0;
			if(sbFiltros.length() > 0)
				sbFiltros.append(" AND ");

			sbFiltros.append("COD_TIPO_SOLIC IN (");
			for (; ind <= lstTipoSolicitudSelected.size() - 1; ind++) 
			{
				logger.info("Filtro por tipo de solicitud: " + lstTipoSolicitudSelected.get(ind));
				
				if(ind != lstTipoSolicitudSelected.size() - 1)
					sbFiltros.append(lstTipoSolicitudSelected.get(ind).concat(","));
					
					else
						sbFiltros.append(lstTipoSolicitudSelected.get(ind));
			}
			sbFiltros.append(")");
//			filtroSol.createAlias(ConstantesVisado.NOM_TBL_TIPO_SOLICITUD,	ConstantesVisado.ALIAS_TBL_TIPO_SOLICITUD);
//			filtroSol.add(Restrictions.in(ConstantesVisado.CAMPO_COD_TIPO_SOL_ALIAS,lstTipoSolicitudSelected));
		}

		// 5. Filtro por tipo de fecha (no funciona)
		if (getIdTiposFecha().compareTo("") != 0) 
		{
			if (getIdTiposFecha().equalsIgnoreCase(ConstantesVisado.TIPO_FECHA_ENVIO)) // Es fecha de envio
			{
				if (getFechaFin().before(getFechaInicio()))
				{
					logger.debug(ConstantesVisado.MENSAJE.FECHINICIO_DEBE_SER_MENOR_FECHFIN);
					Utilitarios.mensajeInfo("", ConstantesVisado.MENSAJE.FECHINICIO_DEBE_SER_MENOR_FECHFIN);
				}
				else
				{
					logger.info("[Seguimiento-busqSolicit]-FechaEnvio");
					
				 	try {
						SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
						String fecIni = formatter.format(getFechaInicio());
						Date minDate = formatter.parse(fecIni);
						String fecFin = formatter.format(getFechaFin());
						Date maxDate = formatter.parse(fecFin);
						Date rangoFin = new Date(maxDate.getTime() + TimeUnit.DAYS.toMillis(1));
						
						logger.info("[Seguimiento-busqSolicit]-FechaEnvio-Inicio: " + minDate);
						logger.info("[Seguimiento-busqSolicit]-FechaEnvio-Fin: " + rangoFin);
						
						if(sbFiltros.length() > 0)
							sbFiltros.append(" AND ");
						
						sbFiltros.append("FECHA_ENVIO BETWEEN TO_TIMESTAMP('");
						sbFiltros.append(new Timestamp(minDate.getTime()));
						sbFiltros.append("','YYYY-MM-DD HH24:MI:SS:FF')");
						sbFiltros.append(" AND TO_TIMESTAMP('");
						sbFiltros.append(new Timestamp(rangoFin.getTime()));
						sbFiltros.append("','YYYY-MM-DD HH24:MI:SS:FF')");

//						sbFiltros.append("FECHA_ENVIO BETWEEN '");
//						sbFiltros.append(new Timestamp(minDate.getTime()));
//						sbFiltros.append("' AND '");
//						sbFiltros.append(new Timestamp(rangoFin.getTime()));
//						sbFiltros.append("'");
//						filtroSol.add(Restrictions.ge(ConstantesVisado.CAMPO_FECHA_ENVIO, minDate));
//						filtroSol.add(Restrictions.le(ConstantesVisado.CAMPO_FECHA_ENVIO, rangoFin));
						
						////[21-10] [DC] Mejora: Se quita los filtros 'Estado' en el filtro 'Fecha Envio'
						//Verificar que el campo estado no tenga espacios en blanco en BD
						//filtroSol.add(Restrictions.eq(ConstantesVisado.CAMPO_ESTADO,ConstantesVisado.ESTADOS.ESTADO_COD_ENVIADOSSJJ_T02));
						//filtroSol.addOrder(Order.asc(ConstantesVisado.CAMPO_COD_SOLICITUD));
						
					} catch (ParseException e) {
						logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR+"al parsear la Fecha Envio: ",e);
					}
				 	
//				 	filtroSol.addOrder(Order.asc(ConstantesVisado.CAMPO_COD_SOLICITUD));
				}
			}
			if (getIdTiposFecha().equalsIgnoreCase(ConstantesVisado.TIPO_FECHA_RPTA)) // Sino es fecha de respuesta
			{
				if (getFechaFin().before(getFechaInicio()))
				{
					logger.debug(ConstantesVisado.MENSAJE.FECHINICIO_DEBE_SER_MENOR_FECHFIN);
					Utilitarios.mensajeInfo("", ConstantesVisado.MENSAJE.FECHINICIO_DEBE_SER_MENOR_FECHFIN);
				}
				else
				{
					logger.info("[Seguimiento-busqSolicit]-Fecha Respuesta");
									
					try {
						SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
						String fecIni = formatter.format(getFechaInicio());
						Date minDate = formatter.parse(fecIni);
						String fecFin = formatter.format(getFechaFin());
						Date maxDate = formatter.parse(fecFin);
						Date rangoFin = new Date(maxDate.getTime() + TimeUnit.DAYS.toMillis(1));
						
						logger.info("[FechaRpta]-Inicio: " + minDate);
						logger.info("[FechaRpta]-Fin: " + rangoFin);
						
						if(sbFiltros.length() > 0)
							sbFiltros.append(" AND ");
						
						sbFiltros.append("FECHA_ENVIO BETWEEN TO_TIMESTAMP('");
						sbFiltros.append(new Timestamp(minDate.getTime()));
						sbFiltros.append("','YYYY-MM-DD HH24:MI:SS:FF')");
						sbFiltros.append(" AND TO_TIMESTAMP('");
						sbFiltros.append(new Timestamp(rangoFin.getTime()));
						sbFiltros.append("','YYYY-MM-DD HH24:MI:SS:FF')");

//						sbFiltros.append("FECHA_RESPUESTA BETWEEEN '");
//						sbFiltros.append(minDate);
//						sbFiltros.append("' AND '");
//						sbFiltros.append(rangoFin);
//						sbFiltros.append("'");
//						filtroSol.add(Restrictions.ge(ConstantesVisado.CAMPO_FECHA_RPTA, minDate));
//						filtroSol.add(Restrictions.le(ConstantesVisado.CAMPO_FECHA_RPTA, rangoFin));
						
						//[21-10] [DC] Mejora: Se quita los filtros 'Estado' en el filtro 'Fecha Rpta'
						/*List<String> tmpEstados = new ArrayList<String>();
						tmpEstados.add(ConstantesVisado.ESTADOS.ESTADO_COD_ACEPTADO_T02);
						tmpEstados.add(ConstantesVisado.ESTADOS.ESTADO_COD_RECHAZADO_T02);
						tmpEstados.add(ConstantesVisado.ESTADOS.ESTADO_COD_EN_VERIFICACION_A_T02);
						
						//Verificar que el campo estado no tenga espacios en blanco en BD
						filtroSol.add(Restrictions.in(ConstantesVisado.CAMPO_ESTADO,tmpEstados));*/
						
					} catch (ParseException e) {
						logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR+"al convertir la Fecha Rpta: ",e);
					}
	
//					filtroSol.addOrder(Order.asc(ConstantesVisado.CAMPO_COD_SOLICITUD));
				}
			}
		}
		
		
		

		// 6. Filtro por operacion bancaria (funciona)
		if (getIdOpeBan().compareTo("") != 0) 
		{
			lstSolicitudesxOpeBan.clear();
			//logger.info("====> combosMB.getLstSolOperBan(): "+combosMB.getLstSolOperBan().size());
			for (TiivsSolicitudOperban opeBanTMP: combosMB.getLstSolOperBan())
			{
				if (opeBanTMP.getId().getCodOperBan().equals(getIdOpeBan()))
				{
					lstSolicitudesxOpeBan.add(opeBanTMP.getId().getCodSoli());
				}
			}
			
			logger.info("Filtro por operacion bancaria");
			
			if (lstSolicitudesxOpeBan.size()>0)
			{
				//logger.info("===> lstSolicitudesxOpeBan: "+lstSolicitudesxOpeBan.size());
				
				int ind = 0;
				if(sbFiltros.length() > 0)
					sbFiltros.append(" AND ");

				sbFiltros.append("COD_SOLI IN (");
				for (; ind <= lstSolicitudesxOpeBan.size() - 1; ind++) 
				{
					logger.info("Filtro operacion " + "[" + ind + "]   codSolicitud:" + lstSolicitudesxOpeBan.get(ind));
				
					if(ind != lstSolicitudesxOpeBan.size() - 1)
						sbFiltros.append(lstSolicitudesxOpeBan.get(ind).concat(","));
						
						else
							sbFiltros.append(lstSolicitudesxOpeBan.get(ind));
				}
				sbFiltros.append(")");
//				filtroSol.add(Restrictions.in(ConstantesVisado.CAMPO_COD_SOLICITUD,lstSolicitudesxOpeBan));
			}
			else
			{
				logger.info("No se selecciono ninguna operacion bancaria");
				sbFiltros.append(" AND COD_SOLI = ''");
//				filtroSol.add(Restrictions.eq(ConstantesVisado.CAMPO_COD_SOLICITUD,""));
			}			
		}

		// 8. Filtro por nombre de oficina (funciona)
		if (!PERFIL_USUARIO.equals(ConstantesVisado.OFICINA))
		{   
			/*if(listaOficinasValidar!=null){
				if(listaOficinasValidar.size()!=0){
			// LISTA OFICINAS*/
			if (getOficina() != null) 
			{
				if(getOficina().getDesOfi()!=null){
					logger.info("Filtro Oficina: " + getOficina().getCodOfi());		
						if(sbFiltros.length() > 0)
						sbFiltros.append(" AND ");
					sbFiltros.append("DES_OFI LIKE '");
					sbFiltros.append(ConstantesVisado.SIMBOLO_PORCENTAJE);
					sbFiltros.append(getOficina().getDesOfi().concat(ConstantesVisado.SIMBOLO_PORCENTAJE).concat("'"));
//					filtroSol.createAlias(ConstantesVisado.NOM_TBL_OFICINA,	ConstantesVisado.ALIAS_TBL_OFICINA);
//					String filtroNuevo = ConstantesVisado.SIMBOLO_PORCENTAJE + getOficina().getDesOfi().concat(ConstantesVisado.SIMBOLO_PORCENTAJE);
//					filtroSol.add(Restrictions.like(ConstantesVisado.CAMPO_NOM_OFICINA_ALIAS, filtroNuevo));
				}			
			}else{
				// [09-10] Se agrega 'else'
				if(sentenciaOficina!=null){
						if(sbFiltros.length() > 0)
						sbFiltros.append(" AND ");
					sbFiltros.append("DES_OFI LIKE '");
					sbFiltros.append(ConstantesVisado.SIMBOLO_PORCENTAJE);
					sbFiltros.append(sentenciaOficina.concat(ConstantesVisado.SIMBOLO_PORCENTAJE).concat("'"));
//					filtroSol.createAlias(ConstantesVisado.NOM_TBL_OFICINA,	ConstantesVisado.ALIAS_TBL_OFICINA);
//					String filtroNuevo = ConstantesVisado.SIMBOLO_PORCENTAJE + sentenciaOficina.concat(ConstantesVisado.SIMBOLO_PORCENTAJE);
//					filtroSol.add(Restrictions.like(ConstantesVisado.CAMPO_NOM_OFICINA_ALIAS, filtroNuevo));
					sentenciaOficina = null;
				}
			}
			/*
			}else{
				
				if(listaOficinasValidar.size()==0){
					 FacesContext facesContext = FacesContext.getCurrentInstance();
						FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No es una oficina valida", "No es una oficina valida");  
					    facesContext.addMessage(":frm:pnlBndSol:idMsmBndSolicitud", msg);    
				}
				
				
			}
		  }*/
		}
		// 11. Filtro por numero de documento de apoderado (funciona)
		if (getNroDOIApoderado().compareTo("") != 0) 
		{
			logger.debug("[Seguimiento-busqSolicit]-getNroDOIApoderado:"+getNroDOIApoderado());
			List<String> lstSolicitudes = new ArrayList<String>();
			String[] tipPartic = {ConstantesVisado.APODERADO, ConstantesVisado.TIPO_PARTICIPACION.CODIGO_HEREDERO};
			
			GenericDao<TiivsAgrupacionPersona, Object> agrupacDAO = (GenericDao<TiivsAgrupacionPersona, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
			Busqueda filtro = Busqueda.forClass(TiivsAgrupacionPersona.class);
			filtro.createAlias("tiivsPersona", "p");
			filtro.add(Restrictions.eq("p.numDoi", getNroDOIApoderado()));
			filtro.add(Restrictions.in("tipPartic", tipPartic));
			try {
				List<TiivsAgrupacionPersona> agrupacionesPersona = agrupacDAO.buscarDinamico(filtro);
				if(agrupacionesPersona.size()>0)
					{
						if(sbFiltros.length() > 0)
						sbFiltros.append(" AND ");
						sbFiltros.append("COD_SOLI IN (");
						int index = 0;
						
						for (TiivsAgrupacionPersona tmp: agrupacionesPersona)
						{
//							lstSolicitudes.add(tmp.getCodSoli());
							
							if(index != agrupacionesPersona.size() - 1)
								sbFiltros.append(tmp.getCodSoli().concat(","));
								
								else
									sbFiltros.append(tmp.getCodSoli());	
							
							index++;
					}
						
						sbFiltros.append(")");
				}	
//				filtroSol.add(Restrictions.in(ConstantesVisado.CAMPO_COD_SOLICITUD,lstSolicitudes));
			} catch (Exception e) {
				logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR+"al consultar por DOI Apoderado: " , e );
			}
		}

		// 12. Filtro por nombre de apoderado (funciona)
		if (objTiivsPersonaBusquedaNomApod!=null) 
		{
			logger.info("Filtro por apoderado: " + objTiivsPersonaBusquedaNomApod.getNombreCompletoMayuscula());
			List<String> lstSolicitudes = new ArrayList<String>();
			int ind=0;
			lstSolicitudes = obtenerSolicitudesxFiltroPersonas(objTiivsPersonaBusquedaNomApod,ConstantesVisado.CAMPO_APODERADO);
			
			if(lstSolicitudes.size()>0){
				if(sbFiltros.length() > 0)
					sbFiltros.append(" AND ");	
				sbFiltros.append("COD_SOLI IN (");
				for (; ind <= lstSolicitudes.size() - 1; ind++) 
				{
					logger.info("Solicitudes encontradas" + "[" + ind + "]" + lstSolicitudes.get(ind));
					
					if(ind != lstSolicitudes.size() - 1)
						sbFiltros.append(lstSolicitudes.get(ind).concat(","));
						
						else
							sbFiltros.append(lstSolicitudes.get(ind));	
				}
				sbFiltros.append(")");
//				filtroSol.add(Restrictions.in(ConstantesVisado.CAMPO_COD_SOLICITUD, lstSolicitudes));	
			}else{
				if(sbFiltros.length() > 0)
					sbFiltros.append(" AND ");
				sbFiltros.append("COD_SOLI = NULL");
//				filtroSol.add(Restrictions.eq(ConstantesVisado.CAMPO_COD_SOLICITUD, null));
			}
		}
		
		// 13. Filtro por numero de documento de poderdante (funciona)
		if (getNroDOIPoderdante().compareTo("") != 0) 
		{
			logger.debug("[Seguimiento-busqSolicit]-getNroDOIPoderdante:"+getNroDOIPoderdante());
			List<String> lstSolicitudes = new ArrayList<String>();
			String[] tipPartic = {ConstantesVisado.PODERDANTE};
			
			GenericDao<TiivsAgrupacionPersona, Object> agrupacDAO = (GenericDao<TiivsAgrupacionPersona, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
			Busqueda filtro = Busqueda.forClass(TiivsAgrupacionPersona.class);
			filtro.createAlias("tiivsPersona", "p");
			filtro.add(Restrictions.eq("p.numDoi", getNroDOIPoderdante()));
			filtro.add(Restrictions.in("tipPartic", tipPartic));
			try {
				List<TiivsAgrupacionPersona> agrupacionesPersona = agrupacDAO.buscarDinamico(filtro);
				if(agrupacionesPersona.size()>0)
				{
						if(sbFiltros.length() > 0)
						sbFiltros.append(" AND ");
					sbFiltros.append("COD_SOLI IN (");
					int index = 0;
					for (TiivsAgrupacionPersona tmp: agrupacionesPersona)
					{
//						lstSolicitudes.add(tmp.getCodSoli());
						if(index != agrupacionesPersona.size() - 1)
							sbFiltros.append(tmp.getCodSoli().concat(","));
							
							else
								sbFiltros.append(tmp.getCodSoli());	
						
						index++;
					}
					sbFiltros.append(")");
				}	
//				filtroSol.add(Restrictions.in(ConstantesVisado.CAMPO_COD_SOLICITUD,lstSolicitudes));
			} catch (Exception e) {
				logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR+"al consultar por DOI Poderdante: " , e );
			}
		}

		// 14. Filtro por nombre de poderdante (funciona)
		if (objTiivsPersonaBusquedaNomPoder!=null) 
		{
			logger.info("Filtro por poderdante: " + objTiivsPersonaBusquedaNomPoder.getNombreCompletoMayuscula());
			List<String> lstSolicitudes = new ArrayList<String>();
			int ind=0;
			lstSolicitudes = obtenerSolicitudesxFiltroPersonas(objTiivsPersonaBusquedaNomPoder,ConstantesVisado.CAMPO_PODERDANTE);
			
			if(lstSolicitudes.size()>0){
					if(sbFiltros.length() > 0)
					sbFiltros.append(" AND ");
				sbFiltros.append("COD_SOLI IN (");
				
				for (; ind <= lstSolicitudes.size() - 1; ind++)
				{
					logger.info("Solicitudes encontradas" + "[" + ind + "]" + lstSolicitudes.get(ind));
					
					if(ind != lstSolicitudes.size() - 1)
						sbFiltros.append(lstSolicitudes.get(ind).concat(","));
						
						else
							sbFiltros.append(lstSolicitudes.get(ind));	
				}
				sbFiltros.append(")");
//				filtroSol.add(Restrictions.in(ConstantesVisado.CAMPO_COD_SOLICITUD,lstSolicitudes));
			}else{
//				filtroSol.add(Restrictions.eq(ConstantesVisado.CAMPO_COD_SOLICITUD, null));
				if(sbFiltros.length() > 0)
					sbFiltros.append(" AND ");
				sbFiltros.append("COD_SOLI = NULL");
			}
			
		}

		// 15. Filtro por nivel (funciona)
		if (lstNivelSelected.size() > 0) 
		{
			/*lstSolicitudesSelected.clear();
			for (TiivsSolicitud sol : solicitudes) 
			{
				if (sol.getTxtNivel() != null && sol.getTxtNivel().length() > 0) 
				{
					if (lstNivelSelected.get(0).indexOf(sol.getTxtNivel()) != -1) 
					{
							lstSolicitudesSelected.add(sol.getCodSoli());
					}
				}
			}
			
			int ind = 0;

			for (; ind <= lstNivelSelected.size() - 1; ind++) 
			{
				logger.info("Filtro nivel" + "[" + ind + "]" + lstNivelSelected.get(ind));
			}
			*/
			int ind = 0;
			List<String> lstCodNiv = new ArrayList<String>();
			for (; ind <= lstNivelSelected.size() - 1; ind++) 
			{
				logger.info("Filtro nivel" + "[" + ind + "]" + lstNivelSelected.get(ind));
				String nivel = nivelService.buscarNivelxDescrip(lstNivelSelected.get(ind));
				
				if (nivel!=null)
				{
					lstCodNiv.add(nivel);
				}
			}
						
			List<String>lstSolicitudesxNivel = obtenerSolicitudesxFiltroNivels(lstCodNiv);
			
			if(lstSolicitudesxNivel.size() > 0)
			{
				if(sbFiltros.length() > 0)
					sbFiltros.append(" AND ");
				sbFiltros.append("COD_SOLI IN (");
				int index = 0;
				for(;index <= lstSolicitudesxNivel.size()-1;index++)
				{
					if(ind != lstSolicitudesxNivel.size() - 1)
						sbFiltros.append(lstSolicitudesxNivel.get(ind).concat(","));
						
						else
							sbFiltros.append(lstSolicitudesxNivel.get(ind));	
				}
				sbFiltros.append(")");
			}
//			filtroSol.add(Restrictions.in(ConstantesVisado.CAMPO_COD_SOLICITUD,	obtenerSolicitudesxFiltroNivels(lstCodNiv)));
		}
		
		// 16. Filtro por estado nivel (funciona)
		if (lstEstadoNivelSelected.size() > 0)
		{
			GenericDao<TiivsSolicitudNivel, Object> busqSolNivDAO = (GenericDao<TiivsSolicitudNivel, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
			Busqueda filtro = Busqueda.forClass(TiivsSolicitudNivel.class);
			List<TiivsSolicitudNivel> lstSolNivel = new ArrayList<TiivsSolicitudNivel>();
			/*for(int i=0;i<lstEstadoNivelSelected.size() ; i++){
				if(lstEstadoNivelSelected.get(i).equals("0001")){
					lstEstadoNivelSelected.add("");
				}
			}*/
			
			filtro.add(Restrictions.in(ConstantesVisado.CAMPO_ESTADO_NIVEL, lstEstadoNivelSelected));
			
			try {
				lstSolNivel = busqSolNivDAO.buscarDinamico(filtro);
			} catch (Exception e) {
				logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"los estados de los niveles en las solicitudes: ",e);
			}
			
			lstSolicitudesSelected.clear();
			
			for (TiivsSolicitudNivel sol : lstSolNivel) 
			{
				lstSolicitudesSelected.add(sol.getTiivsSolicitud().getCodSoli());
			}
			
			if(lstEstadoNivelSelected.size() > 0)
			{
			int ind = 0;
				if(sbFiltros.length() > 0)
					sbFiltros.append(" AND ");
				sbFiltros.append("COD_SOLI IN (");
				for (; ind <= lstSolicitudesSelected.size() - 1; ind++) 
			{
				logger.info("Filtro estado nivel" + "[" + ind + "]" + lstEstadoNivelSelected.get(ind));
			
					if(ind != lstSolicitudesSelected.size() - 1)
						sbFiltros.append(lstSolicitudesSelected.get(ind).concat(","));
						
						else
							sbFiltros.append(lstSolicitudesSelected.get(ind));	
				}
				sbFiltros.append(")");
			}
//			filtroSol.add(Restrictions.in(ConstantesVisado.CAMPO_COD_SOLICITUD,	lstSolicitudesSelected));
		}
		
		// 17. Filtro por estudio (funciona)
		if (lstEstudioSelected.size() > 0) {
			
			// filtroSol.add(Restrictions.eq(ConstantesVisado.CAMPO_ESTUDIO,
			// getIdEstudio()));
			
			int ind = 0;
			if(sbFiltros.length() > 0)
				sbFiltros.append(" AND ");
			sbFiltros.append("E.COD_ESTUDIO IN (");
			for (; ind <= lstEstudioSelected.size() - 1; ind++) 
			{
				logger.info("Filtro estudio" + "[" + ind + "]" + lstEstudioSelected.get(ind));
			
				if(ind != lstEstudioSelected.size() - 1)
					sbFiltros.append(lstEstudioSelected.get(ind).concat(","));
					
					else
						sbFiltros.append(lstEstudioSelected.get(ind));	
			}
			sbFiltros.append(")");
//			filtroSol.createAlias(ConstantesVisado.NOM_TBL_ESTUDIO,	ConstantesVisado.ALIAS_TBL_ESTUDIO);
//			filtroSol.add(Restrictions.in(ConstantesVisado.CAMPO_COD_ESTUDIO_ALIAS, lstEstudioSelected));
		}

		// 19. Filtrar solicitudes con Revision (funciona)
		if (getbRevision()) 
		{
			String codigoSolicEnRevision = /*buscarCodigoEstado(*/ConstantesVisado.ESTADOS.ESTADO_COD_EN_REVISION_T02;/*)*/
			GenericDao<TiivsHistSolicitud, Object> busqHisDAO = (GenericDao<TiivsHistSolicitud, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
			Busqueda filtro = Busqueda.forClass(TiivsHistSolicitud.class);
			filtro.add(Restrictions.eq(ConstantesVisado.CAMPO_ESTADO,codigoSolicEnRevision));

			try {
				lstHistorial = busqHisDAO.buscarDinamico(filtro);
			} catch (Exception e) {
				logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"el historial de las solicitudes",e);
			}
			
			lstSolicitudesSelected.clear();
			for (TiivsHistSolicitud tmp: lstHistorial)
			{
				if (lstHistorial!=null && lstHistorial.size()>0)
				{
					lstSolicitudesSelected.add(tmp.getId().getCodSoli());
				}
			}
						
			if (lstSolicitudesSelected.size() > 0) 
			{
				int index = 0;
				if(sbFiltros.length() > 0)
					sbFiltros.append(" AND ");
				sbFiltros.append("COD_SOLI IN (");
				
				for(;index <= lstSolicitudesSelected.size()-1;index++)
				{
					if(index != lstSolicitudesSelected.size() - 1)
						sbFiltros.append(lstSolicitudesSelected.get(index).concat(","));
						
						else
							sbFiltros.append(lstSolicitudesSelected.get(index));	
				}
				sbFiltros.append(")");
//				filtroSol.add(Restrictions.in(ConstantesVisado.CAMPO_COD_SOLICITUD, lstSolicitudesSelected));
			} 
			else 
			{
				logger.info("No hay solicitudes en el historial con estado En Revision");
				if(sbFiltros.length() > 0)
					sbFiltros.append(" AND ");
				sbFiltros.append("COD_SOLI = ''");
//				filtroSol.add(Restrictions.eq(ConstantesVisado.CAMPO_COD_SOLICITUD,""));
			}
		}
		
		// 20. Filtrar solicitudes que se hayan Delegado (funciona)
		if (getbDelegados()) 
		{
			logger.debug("[Filtro_Busq]-getbDelegados(): "+getbDelegados());
			String codigoSolicVerA = /*buscarCodigoEstado(*/ConstantesVisado.ESTADOS.ESTADO_COD_EN_VERIFICACION_A_T02;// CAMPO_ESTADO_VERIFICACION_A
			String codigoSolicVerB = /*buscarCodigoEstado(*/ConstantesVisado.ESTADOS.ESTADO_COD_EN_VERIFICACION_B_T02;//CAMPO_ESTADO_VERIFICACION_B

			GenericDao<TiivsHistSolicitud, Object> busqHisDAO = (GenericDao<TiivsHistSolicitud, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
			Busqueda filtro = Busqueda.forClass(TiivsHistSolicitud.class);
			filtro.add(Restrictions.or(Restrictions.eq(ConstantesVisado.CAMPO_ESTADO, codigoSolicVerA),Restrictions.eq(ConstantesVisado.CAMPO_ESTADO,codigoSolicVerB)));
			filtro.add(Restrictions.eq(ConstantesVisado.CAMPO_NIVEL_ROL, ConstantesVisado.CODIGO_CAMPO_TIPO_ROL_DELEGADO));
			filtro.add(Restrictions.or(Restrictions.eq(ConstantesVisado.CAMPO_NIVEL_ESTADO, ConstantesVisado.ESTADOS.ESTADO_COD_Desaprobado_T09),
					   				   Restrictions.eq(ConstantesVisado.CAMPO_NIVEL_ESTADO,ConstantesVisado.ESTADOS.ESTADO_COD_Aprobado_T09)));

			try {
				lstHistorial = busqHisDAO.buscarDinamico(filtro);
			} catch (Exception e) {
				logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"el historial de las solicitudes",e);
			}
			
			lstSolicitudesSelected.clear();
			if (lstHistorial!=null) 
			{
				// Colocar aqui la logica para filtrar los niveles aprobados o rechazados
				/*GenericDao<TiivsSolicitudNivel, Object> busqSolNivDAO = (GenericDao<TiivsSolicitudNivel, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
				Busqueda filtro2 = Busqueda.forClass(TiivsSolicitudNivel.class);
				List<TiivsSolicitudNivel> lstSolNivel = new ArrayList<TiivsSolicitudNivel>();
								
				try {
					lstSolNivel = busqSolNivDAO.buscarDinamico(filtro2);
				} catch (Exception e) {
					logger.info("Error al buscar los estados de los niveles en las solicitudes");
				}
				
				for (TiivsSolicitudNivel tmp: lstSolNivel)
				{
					for (TiivsHistSolicitud hist: lstHistorial)
					{
						if (tmp.getTiivsSolicitud().getCodSoli().equals(hist.getId().getCodSoli()))
						{
							lstSolicitudesSelected.add(hist.getId().getCodSoli());
						}
					}
				}*/
				logger.debug(ConstantesVisado.MENSAJE.TAMANHIO_LISTA+"historial-delegado es: "+lstHistorial.size());
				for (TiivsHistSolicitud hist: lstHistorial)
				{
					lstSolicitudesSelected.add(hist.getId().getCodSoli());
					
				}
				logger.debug(ConstantesVisado.MENSAJE.TAMANHIO_LISTA+"lstSolicitudesSelected-delegado es: "+lstSolicitudesSelected.size());
				
				if(lstSolicitudesSelected.size()==0){
//					filtroSol.add(Restrictions.eq(ConstantesVisado.CAMPO_COD_SOLICITUD,""));
					if(sbFiltros.length() > 0)
						sbFiltros.append(" AND ");
					sbFiltros.append("COD_SOLI = ''");
				}
				else
				{
//					filtroSol.add(Restrictions.in(ConstantesVisado.CAMPO_COD_SOLICITUD,	lstSolicitudesSelected));
					if(sbFiltros.length() > 0)
						sbFiltros.append(" AND ");
					sbFiltros.append("COD_SOLI IN (");
					int index = 0;
					for(;index <= lstSolicitudesSelected.size() -1 ;index++)
					{
						if(index != lstSolicitudesSelected.size() - 1)
							sbFiltros.append(lstSolicitudesSelected.get(index).concat(","));
							
							else
								sbFiltros.append(lstSolicitudesSelected.get(index));	
					}
					sbFiltros.append(")");
				}
			}
			else
			{
				logger.info("No hay solicitudes en el historial con delegacion de niveles");
				if(sbFiltros.length() > 0)
					sbFiltros.append(" AND ");
				sbFiltros.append("COD_SOLI = ''");
//				filtroSol.add(Restrictions.eq(ConstantesVisado.CAMPO_COD_SOLICITUD,""));
			}
		}

		// 21. Filtrar solicitudes que se hayan Revocado (funciona)
		if (getbRevocatoria()) 
		{
			List<TiivsSolicitudAgrupacion> lstRev = new ArrayList<TiivsSolicitudAgrupacion>();
			GenericDao<TiivsSolicitudAgrupacion, Object> busqSolAgrp = (GenericDao<TiivsSolicitudAgrupacion, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
			Busqueda filtro = Busqueda.forClass(TiivsSolicitudAgrupacion.class);
			filtro.add(Restrictions.eq(ConstantesVisado.CAMPO_ESTADO,ConstantesVisado.ESTADOS.ESTADO_COD_REVOCADO_3));
			
			try {
				lstRev = busqSolAgrp.buscarDinamico(filtro);
			} catch (Exception e) {
				logger.error("Error al buscar en solicitud agrupacion",e);
			}
			
			lstSolicitudesSelected.clear();
			for (TiivsSolicitudAgrupacion tmp: lstRev)
			{
				if (lstRev!=null && lstRev.size()>0)
				{
					lstSolicitudesSelected.add(tmp.getId().getCodSoli());
				}
			}
			
			if (lstRev.size()>0)
			{
//				filtroSol.add(Restrictions.in(ConstantesVisado.CAMPO_COD_SOLICITUD, lstSolicitudesSelected));
				if(sbFiltros.length() > 0)
					sbFiltros.append(" AND ");
				sbFiltros.append("COD_SOLI IN (");
				int index = 0;
				for(;index <= lstSolicitudesSelected.size() -1 ; index++)
				{
					if(index != lstSolicitudesSelected.size() - 1)
						sbFiltros.append(lstSolicitudesSelected.get(index).concat(","));
						
						else
							sbFiltros.append(lstSolicitudesSelected.get(index));
				}
				sbFiltros.append(")");
			}
			else 
			{
				logger.info("No hay solicitudes con combinaciones revocadas");
				if(sbFiltros.length() > 0)
					sbFiltros.append(" AND ");
				sbFiltros.append("COD_SOLI = ''");
//				filtroSol.add(Restrictions.eq(ConstantesVisado.CAMPO_COD_SOLICITUD,""));
			}
		}
		
		if(PERFIL_USUARIO.equals(ConstantesVisado.ABOGADO) )
		{
			if(sbFiltros.length() > 0)
				sbFiltros.append(" AND ");
			// CAMBIO 08/08/2014 HVB SE AGREGA EL PREFIJO S PARA CODIGO DE ESTUDIO
			sbFiltros.append("S.COD_ESTUDIO = ");
			sbFiltros.append(buscarEstudioxAbogado());
//			filtroSol.createAlias(ConstantesVisado.NOM_TBL_ESTUDIO,	ConstantesVisado.ALIAS_TBL_ESTUDIO);
//			filtroSol.add(Restrictions.eq(ConstantesVisado.ALIAS_COD_ESTUDIO, buscarEstudioxAbogado()));
		}
		else if (PERFIL_USUARIO.equals(ConstantesVisado.OFICINA))
		{
			if(sbFiltros.length() > 0)
				sbFiltros.append(" AND ");
			// CAMBIO 08/08/2014 HVB SE AGREGA EL PREFIJO S PARA CODIGO DE OFICINA
			sbFiltros.append("S.COD_OFI = ");
			sbFiltros.append(usuario.getBancoOficina().getCodigo().trim());
//			filtroSol.createAlias(ConstantesVisado.NOM_TBL_OFICINA, ConstantesVisado.ALIAS_TBL_OFICINA);
//			filtroSol.add(Restrictions.eq(ConstantesVisado.CAMPO_COD_OFICINA_ALIAS_FILTRO, usuario.getBancoOficina().getCodigo().trim()));
		}
		
//		filtroSol.addOrder(Order.asc(ConstantesVisado.CAMPO_COD_SOLICITUD));
		
		// Buscar solicitudes de acuerdo a criterios seleccionados
		logger.debug("[Seguimiento-busqSolicit]-Antes de hacer el query.");
		
		try {
//			solicitudes = null;
			ConsultasJDBCDaoImpl jdbcDAO = new ConsultasJDBCDaoImpl();
			// 31/07/2014 HVB SE LE COLOCA MENOS UNO CUANDO NO HAY UN MAXIMO DE REGISTROS A TRAER POR LA CONSULTA
			solicitudes = jdbcDAO.consultarSolicitud(-1,sbFiltros.toString());
        } catch (Exception ex) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"las solicitudes en la Bandeja de Seguimiento: ",ex);
		}
		//logger.debug("[Seguimiento-busqSolicit]-Despues de hacer el query.");
		if(solicitudes!=null){
			logger.debug("[Seguimiento-busqSolicit]-Se han encontrado: [ "+solicitudes.size()+"] solicitud(es).");
		}else{
			logger.debug("[Seguimiento-busqSolicit]-Es null");
		}
		
		if (solicitudes.size() == 0) 
		{
			setearTextoTotalResultados(ConstantesVisado.MSG_TOTAL_SIN_REGISTROS,solicitudes.size());
			setNoHabilitarExportar(true);
			logger.debug("[Seguimiento-busqSolicit]-"+ConstantesVisado.MSG_TOTAL_SIN_REGISTROS+": ["+solicitudes.size()+"]");
		} 
		else 
		{
			setearTextoTotalResultados(ConstantesVisado.MSG_TOTAL_REGISTROS + solicitudes.size() + ConstantesVisado.MSG_REGISTROS,solicitudes.size());
			logger.debug("[Seguimiento-busqSolicit]-"+ConstantesVisado.MSG_TOTAL_REGISTROS +"encontrados: "+solicitudes.size());
			
			
			
			//listarOperacionesBancarias();
			actualizarDatosGrilla();
			setNoHabilitarExportar(false);
		}
		logger.debug("Tiempo de respuesta en B�squeda Solicituds: " + (System.currentTimeMillis()-inicio)/1000 + " segundos");
	}
	

	List<TiivsSolicitudOperban> lstSolOperBan;
	
	public List<TiivsSolicitudOperban> getLstSolOperBan() {
		return this.lstSolOperBan;
	}

	public void setLstSolOperBan(List<TiivsSolicitudOperban> lstSolOperBan) {
		this.lstSolOperBan = lstSolOperBan;
	}

	
	public List<TiivsSolicitudOperban> listarOperacionesBancarias(){
	// Carga data de Operaciones Bancarias por Solicitud
			GenericDao<TiivsSolicitudOperban, Object> operBanDAO = (GenericDao<TiivsSolicitudOperban, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
			Busqueda filtroOperBan = Busqueda.forClass(TiivsSolicitudOperban.class);
			filtroOperBan.addOrder(Order.asc(ConstantesVisado.CAMPO_ID_CODIGO_SOLICITUD_ALIAS));
			
			lstSolOperBan =new ArrayList<TiivsSolicitudOperban>();
			
			try {
				long inicio = System.currentTimeMillis();
				 lstSolOperBan = operBanDAO.buscarDinamico(filtroOperBan);
				 logger.debug("Tiempo respuesta de consulta de Operaciones Bancarias: " + (System.currentTimeMillis()-inicio)/1000 + " segundos");
			} catch (Exception e) {
				logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CARGA_LISTA+"de operaciones bancarias: ",e);
			}
			return lstSolOperBan;
	}
	
	private List<TiivsSolicitudNivel> listarSolicitudNiveles(){
		GenericDao<TiivsSolicitudNivel, Object> operBanDAO = (GenericDao<TiivsSolicitudNivel, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtroOperBan = Busqueda.forClass(TiivsSolicitudNivel.class);
		
		
		List<TiivsSolicitudNivel> lstSolNiveles =new ArrayList<TiivsSolicitudNivel>();
		
		try {
			long inicio = System.currentTimeMillis();
			lstSolNiveles = operBanDAO.buscarDinamico(filtroOperBan);
			 logger.debug("Tiempo respuesta de consulta Solicitudes Niveles: " + (System.currentTimeMillis()-inicio)/1000 + " segundos");
		} catch (Exception e) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CARGA_LISTA+"de solicitudes de niveles: ",e);
		}
		return lstSolNiveles;
	}
	
	private List<TiivsSolicitudAgrupacion> listarSolicitudAgrupacion(){
		GenericDao<TiivsSolicitudAgrupacion, Object> operBanDAO = (GenericDao<TiivsSolicitudAgrupacion, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtroOperBan = Busqueda.forClass(TiivsSolicitudAgrupacion.class);
		
		
		List<TiivsSolicitudAgrupacion> lstSolAgrupacion =new ArrayList<TiivsSolicitudAgrupacion>();
		
		try {
			long inicio = System.currentTimeMillis();
			lstSolAgrupacion = operBanDAO.buscarDinamico(filtroOperBan);
			 logger.debug("Tiempo respuesta de consulta Solicitudes agrupaciones: " + (System.currentTimeMillis()-inicio)/1000 + " segundos");
		} catch (Exception e) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CARGA_LISTA+"de solicitudes agrupaciones: ",e);
		}
		return lstSolAgrupacion;
	}
	
	public List<String> obtenerSolicitudesxFiltroPersonas(TiivsPersona filtroPer, String tipo)
	{
		List<TiivsAgrupacionPersona> lstAgrpPerTMP = new ArrayList<TiivsAgrupacionPersona>();
		
		GenericDao<TiivsAgrupacionPersona, Object> service = (GenericDao<TiivsAgrupacionPersona, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtro = Busqueda.forClass(TiivsAgrupacionPersona.class);
		filtro.createAlias("tiivsPersona", "persona");
		
		if(filtroPer != null)
		{
			if(filtroPer.getNombreCompletoMayuscula().compareTo("")!=0)
			{
//				filtro.add(Restrictions.eq("persona.nombre", filtroPer.getNombreCompletoMayuscula().split(" ")[0]));
//				filtro.add(Restrictions.eq("persona.apePat", filtroPer.getNombreCompletoMayuscula().split(" ")[1]));
//				filtro.add(Restrictions.eq("persona.apeMat", filtroPer.getNombreCompletoMayuscula().split(" ")[2]));				
				filtro.add(Restrictions.eq("persona.nombre", filtroPer.getNombre()));
				if(filtroPer.getApePat()!=null)
					filtro.add(Restrictions.eq("persona.apePat", filtroPer.getApePat()));
				if(filtroPer.getApeMat()!=null)
					filtro.add(Restrictions.eq("persona.apeMat", filtroPer.getApeMat()));
				
				
				if (tipo.equals(ConstantesVisado.CAMPO_PODERDANTE))
				{
					filtro.add(Restrictions.eq(ConstantesVisado.CAMPO_TIPO_PARTIC,ConstantesVisado.PODERDANTE));
				}
				else
				{
					//02/10/2013
					String apoderado[] = {ConstantesVisado.APODERADO, ConstantesVisado.TIPO_PARTICIPACION.CODIGO_HEREDERO};
					filtro.add(Restrictions.in(ConstantesVisado.CAMPO_TIPO_PARTIC,apoderado));
				}
			}
		}
		
		try {
			lstAgrpPerTMP = service.buscarDinamico(filtro.addOrder(Order.desc("codSoli")));
		} catch (Exception e) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"la lista de AgrupPersonas -solicitudes",e);
		}
		
		List<String> tmpAgrupSol = new ArrayList<String>();
		
		for (TiivsAgrupacionPersona tmp: lstAgrpPerTMP)
		{
			if (tmp!=null)
			{
				tmpAgrupSol.add(tmp.getCodSoli());
			}
		}
		
		return tmpAgrupSol;
	}
	
	public List<String> obtenerSolicitudesxFiltroNivels(List<String> lstTMP)
	{
		List<TiivsSolicitudNivel> lstSolNivTMP = new ArrayList<TiivsSolicitudNivel>();
		
		GenericDao<TiivsSolicitudNivel, Object> service = (GenericDao<TiivsSolicitudNivel, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtro = Busqueda.forClass(TiivsSolicitudNivel.class);
		
		if(lstTMP != null)
		{
			filtro.add(Restrictions.in(ConstantesVisado.CAMPO_COD_NIVEL, lstTMP));
		}
		
		try {
			lstSolNivTMP = service.buscarDinamico(filtro);
		} catch (Exception e) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"la lista de Solicitudes por Nivel",e);
		}
		
		List<String> tmpSol = new ArrayList<String>();
		
		for (TiivsSolicitudNivel tmp: lstSolNivTMP)
		{
			if (tmp!=null)
			{
				tmpSol.add(tmp.getTiivsSolicitud().getCodSoli());
			}
		}
		
		return tmpSol;
	}
	
	@SuppressWarnings("unchecked")
	public void busquedaSolicitudxCodigo(String codigo) 
	{
		logger.info("==== busquedaSolicitudxCodigo() =====");
		logger.debug("[BandejaSeg-buscarSolicitudCod]-codigo:"+codigo);
		GenericDao<TiivsSolicitud, Object> solicDAO = (GenericDao<TiivsSolicitud, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtroSol = Busqueda.forClass(TiivsSolicitud.class);
		filtroSol.add(Restrictions.eq(ConstantesVisado.CAMPO_COD_SOLICITUD, codigo));
		
		// Actualizar datos de la solicitud de acuerdo al codigo.
		try {
			solicitudes = solicDAO.buscarDinamico(filtroSol);
		} catch (Exception ex) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR+"al busquedaSolicitudxCodigo: ",ex);
		}

		if (solicitudes.size() == 0) 
		{
			logger.debug("[BandejaSeg-buscarSolicitudCod]-rpta:"+ConstantesVisado.MSG_TOTAL_SIN_REGISTROS);
			setearTextoTotalResultados(ConstantesVisado.MSG_TOTAL_SIN_REGISTROS,solicitudes.size());
			setNoHabilitarExportar(true);
		} 
		else 
		{
			setearTextoTotalResultados(ConstantesVisado.MSG_TOTAL_REGISTROS + solicitudes.size() + ConstantesVisado.MSG_REGISTROS,solicitudes.size());
			logger.debug("[BandejaSeg-buscarSolicitudCod]-rpta:"+ConstantesVisado.MSG_TOTAL_REGISTROS+"encontrados:["+solicitudes.size()+"]");
			actualizarDatosGrilla();
			setNoHabilitarExportar(false);
		}
	}
	
	public Boolean validarSolicitudConDelegacion(String codSoli)
	{
		boolean bEncontrado=false;
		
		String codigoSolicVerA = /*buscarCodigoEstado(*/ConstantesVisado.CAMPO_ESTADO_VERIFICACION_A;
		String codigoSolicVerB = /*buscarCodigoEstado(*/ConstantesVisado.CAMPO_ESTADO_VERIFICACION_B;

		GenericDao<TiivsHistSolicitud, Object> busqHisDAO = (GenericDao<TiivsHistSolicitud, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtro = Busqueda.forClass(TiivsHistSolicitud.class);
		filtro.add(Restrictions.or(Restrictions.eq(ConstantesVisado.CAMPO_ESTADO, codigoSolicVerA),Restrictions.eq(ConstantesVisado.CAMPO_ESTADO,codigoSolicVerB)));

		try {
			lstHistorial = busqHisDAO.buscarDinamico(filtro);
		} catch (Exception e) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"el historial de solicitudes: ",e);
		}
		
		lstSolicitudesSelected.clear();
		if (lstHistorial.size() > 0) 
		{
			// Colocar aqui la logica para filtrar los niveles aprobados o rechazados
			GenericDao<TiivsSolicitudNivel, Object> busqSolNivDAO = (GenericDao<TiivsSolicitudNivel, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
			Busqueda filtro2 = Busqueda.forClass(TiivsSolicitudNivel.class);
			List<TiivsSolicitudNivel> lstSolNivel = new ArrayList<TiivsSolicitudNivel>();
							
			try {
				lstSolNivel = busqSolNivDAO.buscarDinamico(filtro2);
			} catch (Exception e) {
				logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"los estados de los nivel en las solicitudes: ",e);
			}
			
			for (TiivsSolicitudNivel tmp: lstSolNivel)
			{
				for (TiivsHistSolicitud hist: lstHistorial)
				{
					if (tmp.getTiivsSolicitud().getCodSoli().equals(hist.getId().getCodSoli()))
					{
						lstSolicitudesSelected.add(hist.getId().getCodSoli());
					}
				}
			}
		}
		
		int ind=0;
		
		for (;ind<=lstSolicitudesSelected.size()-1;ind++)
		{
			if (lstSolicitudesSelected.get(ind).equals(codSoli))
			{
				bEncontrado=true;
				break;
			}
		}
		return bEncontrado;
	}
	
	public Boolean validarSolicitudEnRevision(String codSoli)
	{
		boolean bEncontrado=false;
		String codigoSolicEnRevision = /*buscarCodigoEstado(*/ConstantesVisado.ESTADOS.ESTADO_COD_EN_REVISION_T02;//CAMPO_ESTADO_EN_REVISION
		GenericDao<TiivsHistSolicitud, Object> busqHisDAO = (GenericDao<TiivsHistSolicitud, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtro = Busqueda.forClass(TiivsHistSolicitud.class);
		filtro.add(Restrictions.eq(ConstantesVisado.CAMPO_ESTADO,codigoSolicEnRevision));

		try {
			lstHistorial = busqHisDAO.buscarDinamico(filtro);
		} catch (Exception e) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"el historial de solicitudes en validarSolicitudEnRevision(): ",e);
		}
		
		lstSolicitudesSelected.clear();
		for (TiivsHistSolicitud tmp: lstHistorial)
		{
			if (lstHistorial!=null && lstHistorial.size()>0)
			{
				lstSolicitudesSelected.add(tmp.getId().getCodSoli());
			}
		}
		
		int ind=0;
		
		for (;ind<=lstSolicitudesSelected.size()-1;ind++)
		{
			if (lstSolicitudesSelected.get(ind).equals(codSoli))
			{
				bEncontrado=true;
				break;
			}
		}
		return bEncontrado;
	}
	
	public Boolean validarSolicitudRevocada(String codSoli)
	{
		boolean bEncontrado=false;
		String codigoSolicRevocado = /*buscarCodigoEstado(*/ConstantesVisado.ESTADOS.ESTADO_COD_REVOCADO_T02;//CAMPO_ESTADO_REVOCADO
		GenericDao<TiivsHistSolicitud, Object> busqHisDAO = (GenericDao<TiivsHistSolicitud, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtro = Busqueda.forClass(TiivsHistSolicitud.class);
		filtro.add(Restrictions.eq(ConstantesVisado.CAMPO_ESTADO,codigoSolicRevocado));

		try {
			lstHistorial = busqHisDAO.buscarDinamico(filtro);
		} catch (Exception e) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"el historial de solicitudes en validarSolicitudRevocada(): ",e);
		}
		
		lstSolicitudesSelected.clear();
		for (TiivsHistSolicitud tmp: lstHistorial)
		{
			if (lstHistorial!=null && lstHistorial.size()>0)
			{
				lstSolicitudesSelected.add(tmp.getId().getCodSoli());
			}
		}
		
		int ind=0;
		
		for (;ind<=lstSolicitudesSelected.size()-1;ind++)
		{
			if (lstSolicitudesSelected.get(ind).equals(codSoli))
			{
				bEncontrado=true;
				break;
			}
		}
		return bEncontrado;
	}
	
	public String buscarCodigoEstado(String estado) 
	{
		/*int i = 0;
		String codigo = "";
		for (; i < combosMB.getLstEstado().size(); i++) {
			if (combosMB.getLstEstado().get(i).getDescripcion().equalsIgnoreCase(estado)) {
				codigo = combosMB.getLstEstado().get(i).getCodEstado();
				break;
			}
		}
		return codigo;*/
		
		String resultado="";
		List<TiivsMultitabla> tmpLista = new ArrayList<TiivsMultitabla>();
		GenericDao<TiivsMultitabla, Object> busDAO = (GenericDao<TiivsMultitabla, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtro = Busqueda.forClass(TiivsMultitabla.class);
		filtro.add(Restrictions.eq("id.codMult", ConstantesVisado.CODIGO_MULTITABLA_ESTADOS));
		filtro.add(Restrictions.eq("id.codElem", estado));
		//filtro.add(Restrictions.eq("id.valor1", estado));
		logger.debug("[buscarCodigoEstado]-estado:"+estado);
		try {
			tmpLista = busDAO.buscarDinamico(filtro);
			if(tmpLista!=null){
				logger.debug(ConstantesVisado.MENSAJE.TAMANHIO_LISTA+"estados es:["+tmpLista.size()+"].");
			}
		} catch (Exception e) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"los codigos de estados:",e);
		}
		
		if (tmpLista.size()>0)
		{
			resultado = tmpLista.get(0).getId().getCodElem();
		}
		
		return resultado;
	}
	
	/*
	 * CAMBIO 25/07/2014 HVB
	 * SE EVITA LLAMAR CONTINUAMENTE A LA BD, SE OBTIENE LOS ESTADOS
	 * DEL LISTADO CARGADO DESDE INICIO DE APLICACION
	 */
	public String buscarEstadoxCodigo(String codigo) 
	{
		/*int i = 0;
		String res = "";
		for (; i < combosMB.getLstEstado().size(); i++) {
			if (combosMB.getLstEstado().get(i).getCodEstado().equalsIgnoreCase(codigo)) {
				res = combosMB.getLstEstado().get(i).getDescripcion();
				break;
			}
		}
		return res;*/
		String resultado="";
		for(TiivsMultitabla estadoSolicitud:infoDeployMB.getLstEstadosSolicitudes())
		{
			if(estadoSolicitud.getId().getCodElem().equals(codigo))
		{
				resultado = estadoSolicitud.getValor1().toUpperCase();
				break;
			}
		}
//		String resultado="";
//		List<TiivsMultitabla> tmpLista = new ArrayList<TiivsMultitabla>();
//		GenericDao<TiivsMultitabla, Object> busDAO = (GenericDao<TiivsMultitabla, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
//		Busqueda filtro = Busqueda.forClass(TiivsMultitabla.class);
//		filtro.add(Restrictions.eq("id.codMult", ConstantesVisado.CODIGO_MULTITABLA_ESTADOS));
//		filtro.add(Restrictions.eq("id.codElem", codigo));
//		
//		try {
//			tmpLista = busDAO.buscarDinamico(filtro);
//		} catch (Exception e) {
//			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"la lista de Estados por codigo: ",e);
//		}
		
//		if (tmpLista.size()>0)
//		{
//			resultado = tmpLista.get(0).getValor1().toUpperCase();
//		}
		
		return resultado;
	}
	
	public String buscarNivelxCodigo(String codigo) 
	{
		int i = 0;
		String res = "";
		for (; i < combosMB.getLstNivel().size(); i++) {
			if (combosMB.getLstNivel().get(i).getCodNiv().equalsIgnoreCase(codigo)) {
				res = combosMB.getLstNivel().get(i).getDesNiv();
				break;
			}
		}
		return res;
	}
	
	public String buscarNivelxAbrev(String codigo) 
	{
		int i = 0;
		String res = "";
		for (; i < combosMB.getLstNivel().size(); i++) {
			if (combosMB.getLstNivel().get(i).getDesNiv().equalsIgnoreCase(codigo)) {
				res = combosMB.getLstNivel().get(i).getCodNiv();
				break;
			}
		}
		return res;
	}
	
	public String buscarEstNivelxCodigo(String codigo) 
	{
		int i = 0;
		String res = "";
		for (; i < combosMB.getLstEstadoNivel().size(); i++) {
			if (combosMB.getLstEstadoNivel().get(i).getCodigoEstadoNivel().equalsIgnoreCase(codigo)) {
				res = combosMB.getLstEstadoNivel().get(i).getDescripcion();
				break;
			}
		}
		return res;
	}
	
	public String buscarEstudioxCodigo(String codigo) 
	{
		int i = 0;
		String res = "";
		for (; i < combosMB.getLstEstudio().size(); i++) {
			if (combosMB.getLstEstudio().get(i).getCodEstudio().equalsIgnoreCase(codigo)) {
				res = combosMB.getLstEstudio().get(i).getDesEstudio();
				break;
			}
		}
		return res;
	}
	
	public String buscarOpeBanxCodigo(String codigo) 
	{
		int i = 0;
		String res = "";
		for (; i < combosMB.getLstOpeBancaria().size(); i++) {
			if (combosMB.getLstOpeBancaria().get(i).getCodOperBan().equalsIgnoreCase(codigo)) {
				res = combosMB.getLstOpeBancaria().get(i).getDesOperBan();
				break;
			}
		}
		return res;
	}
	
	public String buscarTipoSolxCodigo(String codigo)
	{
		int i = 0;
		String res = "";
		for (; i < combosMB.getLstTipoSolicitud().size(); i++) {
			if (combosMB.getLstTipoSolicitud().get(i).getCodTipSolic().equalsIgnoreCase(codigo)) {
				res = combosMB.getLstTipoSolicitud().get(i).getDesTipServicio();
				break;
			}
		}
		return res;
	}
	
	public String buscarTipoFechaxCodigo(String codigo)
	{
		int i = 0;
		String res = "";
		/*CAMBIO HVB 23/07/2014
		 *SE CAMBIA A LA VARIABLE INFODEPLOY POR COMBOSMB PARA OBTENER LA INFORMACION QUE SE CARGA/RESETEA EN LA APLICACION
		 */
		for (; i < infoDeployMB.getLstTiposFecha().size(); i++) {
			if (infoDeployMB.getLstTiposFecha().get(i).getCodigoTipoFecha().equalsIgnoreCase(codigo)) {
				res = infoDeployMB.getLstTiposFecha().get(i).getDescripcion();
				break;
			}
		}
		
//		for (; i < combosMB.getLstTiposFecha().size(); i++) {
//			if (combosMB.getLstTiposFecha().get(i).getCodigoTipoFecha().equalsIgnoreCase(codigo)) {
//				res = combosMB.getLstTiposFecha().get(i).getDescripcion();
//				break;
//			}
//		}
		return res;
	}
	
	public void buscarOficinaPorTerritorio(ValueChangeEvent e) 
	{
		if (e.getNewValue() != null) 
		{
			logger.debug("Buscando oficina por territorio: " + e.getNewValue());
			
			GenericDao<TiivsOficina1, Object> ofiDAO = (GenericDao<TiivsOficina1, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
			Busqueda filtroOfic = Busqueda.forClass(TiivsOficina1.class);
			filtroOfic.add(Restrictions.eq(ConstantesVisado.CAMPO_COD_TERR_NO_ALIAS, e.getNewValue()));

			List<TiivsOficina1> lstTmp = new ArrayList<TiivsOficina1>();

			try {
				lstTmp = ofiDAO.buscarDinamico(filtroOfic);
				combosMB.setLstOficina(ofiDAO.buscarDinamico(filtroOfic));
				combosMB.setLstOficina1(ofiDAO.buscarDinamico(filtroOfic));
			} catch (Exception exp) {
				logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"la lista de Oficinas: ",exp);
			}

			if (lstTmp.size() == 1) {
				setTxtCodOficina(lstTmp.get(0).getCodOfi());
				setTxtNomOficina(lstTmp.get(0).getDesOfi());
			}
		} 
		else 
		{
			// Carga combo de Territorio
			GenericDao<TiivsTerritorio, Object> terrDAO = (GenericDao<TiivsTerritorio, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
			Busqueda filtroTerr = Busqueda.forClass(TiivsTerritorio.class);

			try {
				combosMB.setLstTerritorio(terrDAO.buscarDinamico(filtroTerr));
			} catch (Exception e1) {
				logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"la lista de Territorios: ",e1);
			}

			// Cargar combos de oficina
			GenericDao<TiivsOficina1, Object> ofiDAO = (GenericDao<TiivsOficina1, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
			Busqueda filtroOfic = Busqueda.forClass(TiivsOficina1.class);

			try {
				combosMB.setLstOficina(ofiDAO.buscarDinamico(filtroOfic));
				combosMB.setLstOficina1(ofiDAO.buscarDinamico(filtroOfic));

			} catch (Exception exp) {
				logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"la lista de Oficinas: ",exp);
			}
		}
	}
	
	public void habilitarFechas(ValueChangeEvent e) {
		if (e.getNewValue() != null) {
			setNoMostrarFechas(false);
		} else {
			setNoMostrarFechas(true);
		}
	}
	
	public String devolverDesTipoDOI(String codigo)
	{
		String resultado="";
		if (codigo!= null) {
			/*CAMBIO HVB 23/07/2014
			 *SE CAMBIA A LA VARIABLE INFODEPLOY POR COMBOSMB PARA OBTENER LA INFORMACION QUE SE CARGA/RESETEA EN LA APLICACION
			 */
			for (TipoDocumento tmp: infoDeployMB.getLstTipoDocumentos()){
				if (codigo.equalsIgnoreCase(tmp.getCodTipoDoc())) {
					resultado = tmp.getDescripcion();
					break;
				}
			}
			
//			for (TipoDocumento tmp: combosMB.getLstTipoDocumentos()){
//				if (codigo.equalsIgnoreCase(tmp.getCodTipoDoc())) {
//					resultado = tmp.getDescripcion();
//					break;
//				}
//			}
		}
		return resultado;
	}
	
	public String devolverDesOperBan(String codigo)
	{
		String resultado="";
		if (codigo!= null) {
			for (TiivsOperacionBancaria tmp: combosMB.getLstOpeBancaria()){
				if (codigo.equalsIgnoreCase(tmp.getCodOperBan())) {
					resultado = tmp.getDesOperBan();
					break;
				}
			}
		}
		return resultado;
	}
	
	private void setearTextoTotalResultados(String cadena, int total) {
		if (total == 1) {
			setTextoTotalResultados(ConstantesVisado.MSG_TOTAL_REGISTROS+ total + ConstantesVisado.MSG_REGISTRO);
		} else {
			setTextoTotalResultados(cadena);
		}
	}
	
	public String buscarDescripcionMoneda(String codMoneda) {
		String descripcion = "";
		/*CAMBIO HVB 23/07/2014
		 *SE CAMBIA A LA VARIABLE INFODEPLOY POR COMBOSMB PARA OBTENER LA INFORMACION QUE SE CARGA/RESETEA EN LA APLICACION
		 */
		for (Moneda tmpMoneda : infoDeployMB.getLstMoneda()) {
			if (tmpMoneda.getCodMoneda().equalsIgnoreCase(codMoneda)) {
				descripcion = tmpMoneda.getDesMoneda();
				break;
			}
		}
		
//		for (Moneda tmpMoneda : combosMB.getLstMoneda()) {
//			if (tmpMoneda.getCodMoneda().equalsIgnoreCase(codMoneda)) {
//				descripcion = tmpMoneda.getDesMoneda();
//				break;
//			}
//		}
		return descripcion;
	}
	
	public String buscarAbrevMoneda(String codigo) {
		int i = 0;
		String descripcion = "";
		/*CAMBIO HVB 23/07/2014
		 *SE CAMBIA A LA VARIABLE INFODEPLOY POR COMBOSMB PARA OBTENER LA INFORMACION QUE SE CARGA/RESETEA EN LA APLICACION
		 */
		for (; i <= infoDeployMB.getLstMoneda().size() - 1; i++) 
		{
			if (infoDeployMB.getLstMoneda().get(i).getCodMoneda().equalsIgnoreCase(codigo)) {
				if (infoDeployMB.getLstMoneda().get(i).getDesMoneda().equalsIgnoreCase(ConstantesVisado.CAMPO_SOLES_TBL_MONEDA)) 
				{
					descripcion = ConstantesVisado.CAMPO_ABREV_SOLES;
				} 
				else if (infoDeployMB.getLstMoneda().get(i).getDesMoneda().equalsIgnoreCase(ConstantesVisado.CAMPO_DOLARES_TBL_MONEDA)) 
				{
					descripcion = ConstantesVisado.CAMPO_ABREV_DOLARES;
				} 
				else if (infoDeployMB.getLstMoneda().get(i).getDesMoneda().equalsIgnoreCase(ConstantesVisado.CAMPO_EUROS_TBL_MONEDA)) 
				{
					descripcion = ConstantesVisado.CAMPO_ABREV_EUROS;
				} 
				else 
				{
					descripcion = infoDeployMB.getLstMoneda().get(i).getDesMoneda();
				}
				break;
			}
		}

//		for (; i <= combosMB.getLstMoneda().size() - 1; i++) 
//		{
//			if (combosMB.getLstMoneda().get(i).getCodMoneda().equalsIgnoreCase(codigo)) {
//				if (combosMB.getLstMoneda().get(i).getDesMoneda().equalsIgnoreCase(ConstantesVisado.CAMPO_SOLES_TBL_MONEDA)) 
//				{
//					descripcion = ConstantesVisado.CAMPO_ABREV_SOLES;
//				} 
//				else if (combosMB.getLstMoneda().get(i).getDesMoneda().equalsIgnoreCase(ConstantesVisado.CAMPO_DOLARES_TBL_MONEDA)) 
//				{
//					descripcion = ConstantesVisado.CAMPO_ABREV_DOLARES;
//				} 
//				else if (combosMB.getLstMoneda().get(i).getDesMoneda().equalsIgnoreCase(ConstantesVisado.CAMPO_EUROS_TBL_MONEDA)) 
//				{
//					descripcion = ConstantesVisado.CAMPO_ABREV_EUROS;
//				} 
//				else 
//				{
//					descripcion = combosMB.getLstMoneda().get(i).getDesMoneda();
//				}
//				break;
//			}
//		}

		return descripcion;
	}
	
	@SuppressWarnings("unchecked")
	public List<TiivsOficina1> completeCodOficina(String query) 
	{	
		List<TiivsOficina1> results = new ArrayList<TiivsOficina1>();
		List<TiivsOficina1> oficinas = new ArrayList<TiivsOficina1>();
		GenericDao<TiivsOficina1, Object> oficinaDAO = (GenericDao<TiivsOficina1, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtro = Busqueda.forClass(TiivsOficina1.class);
		try {
			oficinas = oficinaDAO.buscarDinamico(filtro);
		} catch (Exception e) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"la lista de Oficinas en completeCodOficina: ",e);
		}

		for (TiivsOficina1 oficina : oficinas) 
		{
			if (oficina.getCodOfi() != null) 
			{
				String texto = oficina.getCodOfi();

				if (texto.contains(query.toUpperCase())) 
				{
					results.add(oficina);
				}

			}
		}
		
	/*if(results.size()==0){
		    FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No es una oficina valida", "No es una oficina valida");  
		    facesContext.addMessage(":frm:pnlBndSol:idMsmBndSolicitud", msg); 
		listaOficinasValidar=new ArrayList<TiivsOficina1>();
		listaOficinasValidar.addAll(results);
		
		
	}*/
		return results;
	}
	
	public List<TiivsOficina1> completeNomOficina(String query) 
	{	
		sentenciaOficina = query;
		List<TiivsOficina1> results = new ArrayList<TiivsOficina1>();
		for (TiivsOficina1 oficina : combosMB.getLstOficina1()) {
			if (oficina.getCodOfi() != null) {
				//String texto = oficina.getDesOfi();		
				String texto = oficina.getCodOfi().concat(" ").
						concat(oficina.getDesOfi()!=null?oficina.getDesOfi().toUpperCase():"");
				if (texto.contains(query.toUpperCase())) {
					oficina.setNombreDetallado(oficina.getCodOfi()+"-"+oficina.getDesOfi());
					results.add(oficina);
				}
			}
		}
		/*
		if(results.size()==0){
		 FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No es una oficina valida", "No es una oficina valida");  
		    facesContext.addMessage(":frm:pnlBndSol:idMsmBndSolicitud", msg);    
		
		
		}
		listaOficinasValidar=new ArrayList<TiivsOficina1>();
		listaOficinasValidar.addAll(results);*/
		return results;
	}
	
	/**
	 * Metodo encargado de mostrar la lista de Personas registradas para
	 * el filtro autocompletable de la bandeja de consulta
	 * @param query Representa el caracter de consulta ingresado por el usuario
	 * @return lstTiivsPersonaResultado Es la lista de {@link TiivsPersona }
	 * */
	public List<TiivsPersona> completePersona(String query) 
	{
		List<TiivsPersona> lstTiivsPersonaResultado = new ArrayList<TiivsPersona>();
		List<TiivsPersona> lstTiivsPersonaBusqueda = new ArrayList<TiivsPersona>();

		GenericDao<TiivsPersona, Object> service = (GenericDao<TiivsPersona, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtro = Busqueda.forClass(TiivsPersona.class);
		try {
			lstTiivsPersonaBusqueda = service.buscarDinamico(filtro);
			if(lstTiivsPersonaBusqueda!=null){
				logger.debug("[completePersona]-Se han encontrado: "+lstTiivsPersonaBusqueda.size()+" personas.");
			}
		} catch (Exception e) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CARGA_LISTA+"de Personas,",e);
		}

		for (TiivsPersona pers : lstTiivsPersonaBusqueda) 
		{
			if(pers!= null)
			{	
				//if (pers.getApePat()!=null && pers.getApeMat()!=null)
				//{
					if (pers.getNombre() != null || pers.getApePat()!=null)
						//&& pers.getApePat().toUpperCase() != ""
						//&& pers.getApeMat().toUpperCase() != ""
					{
						
						String nombreCompletoMayuscula = "".concat(pers.getNombre()!=null?pers.getNombre().toUpperCase():"")
								.concat(" ").concat(pers.getApePat()!=null?pers.getApePat().toUpperCase():"")
								.concat(" ").concat(pers.getApeMat()!=null?pers.getApeMat().toUpperCase():"");
								/*pers.getNombre().toUpperCase()+ " " 
								+ pers.getApePat().toUpperCase() + " "
								+ pers.getApeMat().toUpperCase();*/
						String nombreCompletoMayusculaBusqueda = "".concat(pers.getApePat()!=null?pers.getApePat().toUpperCase():"")
								.concat(" ").concat(pers.getApeMat()!=null?pers.getApeMat().toUpperCase():"")
								.concat(" ").concat(pers.getNombre()!=null?pers.getNombre().toUpperCase():"");
								/*pers.getApePat().toUpperCase()+ " "
								+ pers.getApeMat().toUpperCase()
								+ " " + pers.getNombre().toUpperCase();*/
						if (nombreCompletoMayusculaBusqueda.contains(query.toUpperCase())) {
							pers.setNombreCompletoMayuscula(nombreCompletoMayuscula);
							lstTiivsPersonaResultado.add(pers);
						}
					}
				//}
				
			}
		}

		return lstTiivsPersonaResultado;
	}
	
	public void buscarOficinaPorCodigo(ValueChangeEvent e) 
	{
		logger.debug("Buscando oficina por codigo: " + e.getNewValue());		
		GenericDao<TiivsOficina1, Object> ofiDAO = (GenericDao<TiivsOficina1, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtroOfic = Busqueda.forClass(TiivsOficina1.class);
		filtroOfic.add(Restrictions.eq(ConstantesVisado.CAMPO_COD_OFICINA,e.getNewValue()));

		List<TiivsOficina1> lstTmp = new ArrayList<TiivsOficina1>();
		try {
			lstTmp = ofiDAO.buscarDinamico(filtroOfic);
		} catch (Exception exp) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"el nombre de la oficina por codigo: ",exp);
		}
		
		if (lstTmp.size() == 1) 
		{
			setTxtNomOficina(lstTmp.get(0).getDesOfi());
			setTxtNomTerritorio(buscarDesTerritorio(lstTmp.get(0).getTiivsTerritorio().getCodTer()));
		}
	}
	
	public String buscarDesTerritorio(String codigoTerritorio) 
	{
		String resultado = "";
		GenericDao<TiivsTerritorio, Object> terrDAO = (GenericDao<TiivsTerritorio, Object>) SpringInit
				.getApplicationContext().getBean("genericoDao");
		Busqueda filtroTerr = Busqueda.forClass(TiivsTerritorio.class);
		filtroTerr.add(Restrictions.eq(ConstantesVisado.CAMPO_COD_TERRITORIO,codigoTerritorio));

		List<TiivsTerritorio> lstTmp = new ArrayList<TiivsTerritorio>();
		try {
			lstTmp = terrDAO.buscarDinamico(filtroTerr);
		} catch (Exception exp) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"la descripcion de territorios: ",exp);
		}

		if (lstTmp.size() == 1) {
			resultado = lstTmp.get(0).getDesTer();
		}
		return resultado;
	}
	
	/*Probar cuando la tabla miembro tenga el registro 0237 (con cualquiera)*/
	public TiivsTerritorio buscarTerritorioPorOficina(String codOficina)
	{   
		TiivsTerritorio terrTMP = new TiivsTerritorio();
		try {			
			int i=0;
			int j=0;
			String codTerr="";
			String desTerr="";
			
			for (;i<combosMB.getLstOficina().size();i++){
				if (combosMB.getLstOficina().get(i).getCodOfi().trim().equals(codOficina)){
					codTerr=combosMB.getLstOficina().get(i).getTiivsTerritorio().getCodTer();
					break;
				}
			}
			logger.info("Cod Territorio encontrado:" + codTerr);
			
			if (codTerr.length()>0) {
				for (;j<combosMB.getLstTerritorio().size();j++){
					if (combosMB.getLstTerritorio().get(j).getCodTer().equals(codTerr)){
						desTerr=combosMB.getLstTerritorio().get(j).getDesTer();
						break;
					}
				}
						
				terrTMP.setCodTer(codTerr);
				terrTMP.setDesTer(desTerr);
			}
			
		} catch (Exception e) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"el territorioPorOficina: ",e);
		}
		return terrTMP;
	}
	
	public void buscarOficinaPorNombre(ValueChangeEvent e) 
	{
		GenericDao<TiivsOficina1, Object> ofiDAO = (GenericDao<TiivsOficina1, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtroOfic = Busqueda.forClass(TiivsOficina1.class);
		filtroOfic.add(Restrictions.eq(ConstantesVisado.CAMPO_COD_OFICINA,e.getNewValue()));

		List<TiivsOficina1> lstTmp = new ArrayList<TiivsOficina1>();
		try {
			lstTmp = ofiDAO.buscarDinamico(filtroOfic);
		} catch (Exception exp) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+"el buscarOficinaPorNombre(): ",exp);
		}

		if (lstTmp.size() == 1) {
			setTxtCodOficina(lstTmp.get(0).getCodOfi());
			setTxtNomTerritorio(buscarDesTerritorio(lstTmp.get(0).getTiivsTerritorio().getCodTer()));
		}
	}
	
	public void obtenerHistorialSolicitud(){
		logger.info("=== obtenerHistorialSolicitud() ===");
		logger.info("[Obt_Historial]-CodSol:" + selectedSolicitud.getCodSoli());
		
		String sCodSolicitud=selectedSolicitud.getCodSoli();
		
		GenericDao<TiivsHistSolicitud, Object> histDAO = (GenericDao<TiivsHistSolicitud, Object>) SpringInit.getApplicationContext().getBean("genericoDao");
		Busqueda filtroHist = Busqueda.forClass(TiivsHistSolicitud.class);
		filtroHist.add(Restrictions.eq("id.codSoli",sCodSolicitud));
		filtroHist.addOrder(Order.desc("fecha"));
		
		List<TiivsHistSolicitud> lstHist = new ArrayList<TiivsHistSolicitud>();

		try {
			lstHist = histDAO.buscarDinamico(filtroHist);
			if(lstHist!=null){
				logger.debug("Numero de registros historial encontrados:"+lstHist.size());
			}
		} catch (Exception exp) {
			logger.error(ConstantesVisado.MENSAJE.OCURRE_ERROR_CONSULT+" el Historial de la Solicitud ",exp);
		}
		
		if(lstHist!=null && lstHist.size()>0){
			lstSeguimientoDTO = new ArrayList<SeguimientoDTO>();
			
			for(TiivsHistSolicitud h : lstHist){
				SeguimientoDTO seg = new SeguimientoDTO();
				String estado = h.getEstado();
				if(estado!=null)
					seg.setEstado(buscarEstadoxCodigo(estado.trim()));
				
				
				String desEstadoNivel = "";
				String desRolNivel = "";
				Integer iCodNivel = 0;
				String descripcionNivel = "";

				if (h.getNivel() != null) {
					if (h.getNivelRol() != null && h.getNivelRol().trim().equals(ConstantesVisado.CODIGO_CAMPO_TIPO_ROL_RESPONSABLE)) {
						desRolNivel = "Responsable";
					}
					if (h.getNivelRol() != null && h.getNivelRol().trim().equals(ConstantesVisado.CODIGO_CAMPO_TIPO_ROL_DELEGADO)) {
						desRolNivel = "Delegado";
					}
					if (h.getNivelEstado() != null && h.getNivelEstado().trim().equals(ConstantesVisado.ESTADOS.ESTADO_COD_Desaprobado_T09)) {
						desEstadoNivel = ConstantesVisado.ESTADOS.ESTADO_Desaprobado_T09;
					}
					if (h.getNivelEstado() != null && h.getNivelEstado().trim().equals(ConstantesVisado.ESTADOS.ESTADO_COD_Aprobado_T09)) {
						desEstadoNivel = ConstantesVisado.ESTADOS.ESTADO_Aprobado_T09;
					}
					iCodNivel = Integer.parseInt(h.getNivel());
					descripcionNivel = "Nivel " + iCodNivel + " " + desRolNivel + ": " + desEstadoNivel;
				}
				
				seg.setNivel(descripcionNivel);
				seg.setFecha(h.getFecha());
				seg.setUsuario(h.getNomUsuario());
				seg.setRegUsuario(h.getRegUsuario());
				seg.setObs(h.getObs());
				seg.setCodSoli(h.getId().getCodSoli());
				seg.setMovimiento(h.getId().getMovimiento());
				lstSeguimientoDTO.add(seg);				
			}
		}
		
		
	}
	
	
	
	public List<TiivsSolicitud> getSolicitudes() {
		return solicitudes;
	}

	public void setSolicitudes(List<TiivsSolicitud> solicitudes) {
		this.solicitudes = solicitudes;
	}

	public CombosMB getCombosMB() {
		return combosMB;
	}

	public void setCombosMB(CombosMB combosMB) {
		this.combosMB = combosMB;
	}

	public List<TiivsAgrupacionPersona> getLstAgrupPer() {
		return lstAgrupPer;
	}

	public void setLstAgrupPer(List<TiivsAgrupacionPersona> lstAgrupPer) {
		this.lstAgrupPer = lstAgrupPer;
	}

	public String getTextoTotalResultados() {
		return textoTotalResultados;
	}

	public void setTextoTotalResultados(String textoTotalResultados) {
		this.textoTotalResultados = textoTotalResultados;
	}

	public Boolean getNoHabilitarExportar() {
		return noHabilitarExportar;
	}

	public void setNoHabilitarExportar(Boolean noHabilitarExportar) {
		this.noHabilitarExportar = noHabilitarExportar;
	}

	public String getCodSolicitud() {
		return codSolicitud;
	}

	public void setCodSolicitud(String codSolicitud) {
		this.codSolicitud = codSolicitud;
	}

	public List<String> getLstEstadoSelected() {
		return lstEstadoSelected;
	}

	public void setLstEstadoSelected(List<String> lstEstadoSelected) {
		this.lstEstadoSelected = lstEstadoSelected;
	}

	public String getIdImporte() {
		return idImporte;
	}

	public void setIdImporte(String idImporte) {
		this.idImporte = idImporte;
	}

	public List<String> getLstTipoSolicitudSelected() {
		return lstTipoSolicitudSelected;
	}

	public void setLstTipoSolicitudSelected(List<String> lstTipoSolicitudSelected) {
		this.lstTipoSolicitudSelected = lstTipoSolicitudSelected;
	}

	public String getIdTiposFecha() {
		return idTiposFecha;
	}

	public void setIdTiposFecha(String idTiposFecha) {
		this.idTiposFecha = idTiposFecha;
	}

	public Boolean getNoMostrarFechas() {
		return noMostrarFechas;
	}

	public void setNoMostrarFechas(Boolean noMostrarFechas) {
		this.noMostrarFechas = noMostrarFechas;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getIdOpeBan() {
		return idOpeBan;
	}

	public void setIdOpeBan(String idOpeBan) {
		this.idOpeBan = idOpeBan;
	}

	public String getTxtCodOfi() {
		return txtCodOfi;
	}

	public void setTxtCodOfi(String txtCodOfi) {
		this.txtCodOfi = txtCodOfi;
	}

	public String getTxtNomOfi() {
		return txtNomOfi;
	}

	public void setTxtNomOfi(String txtNomOfi) {
		this.txtNomOfi = txtNomOfi;
	}

	public String getTxtNomOficina() {
		return txtNomOficina;
	}

	public void setTxtNomOficina(String txtNomOficina) {
		this.txtNomOficina = txtNomOficina;
	}

	public String getTxtNomTerritorio() {
		return txtNomTerritorio;
	}

	public void setTxtNomTerritorio(String txtNomTerritorio) {
		this.txtNomTerritorio = txtNomTerritorio;
	}

	public String getIdCodOfi() {
		return idCodOfi;
	}

	public void setIdCodOfi(String idCodOfi) {
		this.idCodOfi = idCodOfi;
	}

	public String getTxtCodOficina() {
		return txtCodOficina;
	}

	public void setTxtCodOficina(String txtCodOficina) {
		this.txtCodOficina = txtCodOficina;
	}

	public String getIdCodOfi1() {
		return idCodOfi1;
	}

	public void setIdCodOfi1(String idCodOfi1) {
		this.idCodOfi1 = idCodOfi1;
	}

	public String getNroDOIApoderado() {
		return nroDOIApoderado;
	}

	public void setNroDOIApoderado(String nroDOIApoderado) {
		this.nroDOIApoderado = nroDOIApoderado;
	}

	public String getTxtNomApoderado() {
		return txtNomApoderado;
	}

	public void setTxtNomApoderado(String txtNomApoderado) {
		this.txtNomApoderado = txtNomApoderado;
	}

	public String getNroDOIPoderdante() {
		return nroDOIPoderdante;
	}

	public void setNroDOIPoderdante(String nroDOIPoderdante) {
		this.nroDOIPoderdante = nroDOIPoderdante;
	}

	public String getTxtNomPoderdante() {
		return txtNomPoderdante;
	}

	public void setTxtNomPoderdante(String txtNomPoderdante) {
		this.txtNomPoderdante = txtNomPoderdante;
	}

	public List<String> getLstNivelSelected() {
		return lstNivelSelected;
	}

	public void setLstNivelSelected(List<String> lstNivelSelected) {
		this.lstNivelSelected = lstNivelSelected;
	}

	public List<String> getLstEstadoNivelSelected() {
		return lstEstadoNivelSelected;
	}

	public void setLstEstadoNivelSelected(List<String> lstEstadoNivelSelected) {
		this.lstEstadoNivelSelected = lstEstadoNivelSelected;
	}

	public List<String> getLstEstudioSelected() {
		return lstEstudioSelected;
	}

	public void setLstEstudioSelected(List<String> lstEstudioSelected) {
		this.lstEstudioSelected = lstEstudioSelected;
	}

	public String getIdTerr() {
		return idTerr;
	}

	public void setIdTerr(String idTerr) {
		this.idTerr = idTerr;
	}

	public List<String> getLstSolicitudesSelected() {
		return lstSolicitudesSelected;
	}

	public void setLstSolicitudesSelected(List<String> lstSolicitudesSelected) {
		this.lstSolicitudesSelected = lstSolicitudesSelected;
	}

	public Boolean getMostrarEstudio() {
		return mostrarEstudio;
	}

	public void setMostrarEstudio(Boolean mostrarEstudio) {
		this.mostrarEstudio = mostrarEstudio;
	}

	public Boolean getMostrarUsuario() {
		return mostrarUsuario;
	}

	public void setMostrarUsuario(Boolean mostrarUsuario) {
		this.mostrarUsuario = mostrarUsuario;
	}

	public List<String> getLstSolicitudesxOpeBan() {
		return lstSolicitudesxOpeBan;
	}

	public void setLstSolicitudesxOpeBan(List<String> lstSolicitudesxOpeBan) {
		this.lstSolicitudesxOpeBan = lstSolicitudesxOpeBan;
	}

	public TiivsOficina1 getOficina() {
		return oficina;
	}

	public void setOficina(TiivsOficina1 oficina) {
		this.oficina = oficina;
	}

	public Boolean getMostrarColumna() {
		return mostrarColumna;
	}

	public void setMostrarColumna(Boolean mostrarColumna) {
		this.mostrarColumna = mostrarColumna;
	}

	public String getNombreArchivoExcel() {
		return nombreArchivoExcel;
	}

	public void setNombreArchivoExcel(String nombreArchivoExcel) {
		this.nombreArchivoExcel = nombreArchivoExcel;
	}

	public Boolean getbRevision() {
		return bRevision;
	}

	public void setbRevision(Boolean bRevision) {
		this.bRevision = bRevision;
	}

	public Boolean getbDelegados() {
		return bDelegados;
	}

	public void setbDelegados(Boolean bDelegados) {
		this.bDelegados = bDelegados;
	}

	public Boolean getbRevocatoria() {
		return bRevocatoria;
	}

	public void setbRevocatoria(Boolean bRevocatoria) {
		this.bRevocatoria = bRevocatoria;
	}

	public List<SeguimientoDTO> getLstSeguimientoDTO() {
		return lstSeguimientoDTO;
	}

	public void setLstSeguimientoDTO(List<SeguimientoDTO> lstSeguimientoDTO) {
		this.lstSeguimientoDTO = lstSeguimientoDTO;
	}

	public TiivsSolicitud getSelectedSolicitud() {
		return selectedSolicitud;
	}

	public void setSelectedSolicitud(TiivsSolicitud selectedSolicitud) {
		this.selectedSolicitud = selectedSolicitud;
	}
	

	public List<TiivsHistSolicitud> getLstHistorial() {
		return lstHistorial;
	}

	public void setLstHistorial(List<TiivsHistSolicitud> lstHistorial) {
		this.lstHistorial = lstHistorial;
	}

	public PDFViewerMB getPdfViewerMB() {
		return pdfViewerMB;
	}

	public void setPdfViewerMB(PDFViewerMB pdfViewerMB) {
		this.pdfViewerMB = pdfViewerMB;
	}

	public String rutaArchivoExcel() {
		return rutaArchivoExcel;
	}

	public void setRutaArchivoExcel(String rutaArchivoExcel) {
		this.rutaArchivoExcel = rutaArchivoExcel;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public IILDPeUsuario getUsuario() {
		return usuario;
	}

	public void setUsuario(IILDPeUsuario usuario) {
		this.usuario = usuario;
	}

	public String getPERFIL_USUARIO() {
		return PERFIL_USUARIO;
	}

	public void setPERFIL_USUARIO(String pERFIL_USUARIO) {
		PERFIL_USUARIO = pERFIL_USUARIO;
	}

	public boolean isBloquearOficina() {
		return bloquearOficina;
	}

	public void setBloquearOficina(boolean bloquearOficina) {
		this.bloquearOficina = bloquearOficina;
	}

	public List<AgrupacionSimpleDto> getLstAgrupacionSimpleDto() {
		return lstAgrupacionSimpleDto;
	}

	public void setLstAgrupacionSimpleDto(
			List<AgrupacionSimpleDto> lstAgrupacionSimpleDto) {
		this.lstAgrupacionSimpleDto = lstAgrupacionSimpleDto;
	}

	public NivelService getNivelService() {
		return nivelService;
	}

	public void setNivelService(NivelService nivelService) {
		this.nivelService = nivelService;
	}

	public TiivsPersona getObjTiivsPersonaBusquedaNomApod() {
		return objTiivsPersonaBusquedaNomApod;
	}

	public void setObjTiivsPersonaBusquedaNomApod(
			TiivsPersona objTiivsPersonaBusquedaNomApod) {
		this.objTiivsPersonaBusquedaNomApod = objTiivsPersonaBusquedaNomApod;
	}

	public TiivsPersona getObjTiivsPersonaBusquedaNomPoder() {
		return objTiivsPersonaBusquedaNomPoder;
	}

	public void setObjTiivsPersonaBusquedaNomPoder(
			TiivsPersona objTiivsPersonaBusquedaNomPoder) {
		this.objTiivsPersonaBusquedaNomPoder = objTiivsPersonaBusquedaNomPoder;
	}

	public boolean isMostrarFechaEstado() {
		return mostrarFechaEstado;
	}

	public void setMostrarFechaEstado(boolean mostrarFechaEstado) {
		this.mostrarFechaEstado = mostrarFechaEstado;
	}

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getPoderdante() {
		return poderdante;
	}

	public void setPoderdante(String poderdante) {
		this.poderdante = poderdante;
	}

	public String getApoderdante() {
		return apoderdante;
	}

	public void setApoderdante(String apoderdante) {
		this.apoderdante = apoderdante;
	}
	
	

	public LazyDataModel<TiivsSolicitud> getSolicitudesPag() {
		return solicitudesPag;
	}

	public void setSolicitudesPag(LazyDataModel<TiivsSolicitud> solicitudesPag) {
		this.solicitudesPag = solicitudesPag;
	}
	
}
