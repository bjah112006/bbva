package bbva.ws.api;

import iist.ws.ServiceWFTarjetas;
import iist.ws.ServiceWFTarjetasService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceRef;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.bbva.hi.cttactpass.cvalnrocttcli.MSCTTACTPASSPortType;
import pe.com.bbva.hi.cttactpass.cvalnrocttcli.MSCTTACTPASSSOAPHTTPService;
import pe.com.bbva.scepe20.client.CtBodyRq;
import pe.com.bbva.scepe20.client.CtHeader;
import pe.com.bbva.scepe20.client.CtVerificarExisteContratoNACARRq;
import pe.com.bbva.scepe20.client.CtVerificarExisteContratoNACARRs;
import pe.com.bbva.scepe20.client.SCEPE20PortType;
import pe.com.bbva.scepe20.client.SCEPE20Service;
import pe.com.grupobbva.sce.tc84.CtExtraerTipoCambioRq;
import pe.com.grupobbva.sce.tc84.CtExtraerTipoCambioRs;
import pe.com.grupobbva.sce.tc84.SCETC84PortType;
import pe.com.grupobbva.sce.tc84.SCETC84Service;
import bbva.ws.api.view.BBVAFacadeLocal;

import com.bbva.general.service.TablaGeneral;
import com.bbva.general.service.TablaGeneralService;
import com.ibm.bbva.entities.ParametrosConf;
import com.ibm.bbva.harec.client.DatosGeneralesXPersonaRq;
import com.ibm.bbva.harec.client.DatosGeneralesXpersonaRs;
import com.ibm.bbva.harec.client.HarecService;
import com.ibm.bbva.harec.client.HarecService_Service;
import com.ibm.bbva.reniec.message.ConsultaPorDNIRequest;
import com.ibm.bbva.reniec.message.ConsultaPorDNIResponse;
import com.ibm.bbva.reniec.message.WSPersonaReniec;
import com.ibm.bbva.reniec.message.WSPersonaReniec_Service;
import com.ibm.bbva.service.Constantes;
import com.ibm.bbva.session.ParametrosConfBeanLocal;



/**
 * Session Bean implementation class BBVAFacade
 */
@Singleton
@Local(BBVAFacadeLocal.class)
public class BBVAFacade implements BBVAFacadeLocal {
	
	private static final Logger LOG = LoggerFactory.getLogger(BBVAFacade.class);
	
	@WebServiceRef(value=HarecService_Service.class)
	private HarecService harec;
	
	@WebServiceRef(value=WSPersonaReniec_Service.class)
	private WSPersonaReniec reniec;	
	
	@WebServiceRef(value=ServiceWFTarjetasService.class)
	private ServiceWFTarjetas wTFTarjetas;
	
	@WebServiceRef(value=SCEPE20Service.class)
	private SCEPE20PortType sce;
	
	@WebServiceRef(value=SCETC84Service.class)
	private SCETC84PortType tc84;
	
	@WebServiceRef(value=TablaGeneralService.class)
	private TablaGeneral tablaGeneral;
	
	@WebServiceRef(value=MSCTTACTPASSSOAPHTTPService.class)
	private MSCTTACTPASSPortType cttactpass;
	
    public BBVAFacade() {
        // TODO Auto-generated constructor stub
    }
    
    //Emi
    private ParametrosConfBeanLocal parametrosConfBean;
    private String WFTARJETAS_SERVICE_URL= "";
    private String SCE_SERVICE_URL= "";
    private String TIPOCAMBIO_SERVICE_URL= "";
    private String HAREC_SERVICE_URL= "";
    private String RENIEC_SERVICE_URL= "";
    private String TABLAGENERAL_SERVICE_URL= "";
    private String PACKPYME_SERVICE_URL= ""; 
    
    @PostConstruct
    public void init() {
    		
		try{
			parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
			WFTARJETAS_SERVICE_URL = parametrosConfBean.buscarPorVariable(Constantes.COD_APLICATIVO_WFTARJETAS, Constantes.NOMBRE_VARIABLE_WFTARJETAS).getValorVariable();
			
		}catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}
		
		try{
			parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
			SCE_SERVICE_URL = parametrosConfBean.buscarPorVariable(Constantes.COD_APLICATIVO_SCE, Constantes.NOMBRE_VARIABLE_SCE).getValorVariable();
			
		}catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}
		
		try{
			parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
			TIPOCAMBIO_SERVICE_URL = parametrosConfBean.buscarPorVariable(Constantes.COD_APLICATIVO_TIPOCAMBIO, Constantes.NOMBRE_VARIABLE_TIPOCAMBIO).getValorVariable();
			
		}catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}
		
		try{
			parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");			
			HAREC_SERVICE_URL = parametrosConfBean.buscarPorVariable(Constantes.COD_APLICATIVO_HAREC, Constantes.NOMBRE_VARIABLE_HAREC).getValorVariable();
			
		}catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}
		
		try{
			parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
			RENIEC_SERVICE_URL = parametrosConfBean.buscarPorVariable(Constantes.COD_APLICATIVO_RENIEC, Constantes.NOMBRE_VARIABLE_RENIEC).getValorVariable();
			
		}catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}
		
		try{
			parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
			TABLAGENERAL_SERVICE_URL = parametrosConfBean.buscarPorVariable(Constantes.COD_APLICATIVO_TABLAGENERAL, Constantes.NOMBRE_VARIABLE_TABLAGENERAL).getValorVariable();
			
		}catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}
		
		try{
			parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
			PACKPYME_SERVICE_URL = parametrosConfBean.buscarPorVariable(Constantes.COD_APLICATIVO_PACKPYME, Constantes.NOMBRE_VARIABLE_PACKPYME).getValorVariable();
		}catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}
				
		((BindingProvider) wTFTarjetas).getRequestContext().put(
				BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				WFTARJETAS_SERVICE_URL);
		
		((BindingProvider) sce).getRequestContext().put(
				BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				SCE_SERVICE_URL);
		
		((BindingProvider) tc84).getRequestContext().put(
				BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				TIPOCAMBIO_SERVICE_URL);
		
		((BindingProvider) harec).getRequestContext().put(
				BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				HAREC_SERVICE_URL);
		
		((BindingProvider) reniec).getRequestContext().put(
				BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				RENIEC_SERVICE_URL);
		
		((BindingProvider) tablaGeneral).getRequestContext().put(
				BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				TABLAGENERAL_SERVICE_URL);
		
		((BindingProvider) cttactpass).getRequestContext().put(
				BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				PACKPYME_SERVICE_URL);
    }
    
    public String WFTarjetas_devolverEstadoEntregaTC(String numeroContrato,String numeroTarjeta){
    	return wTFTarjetas.devolverEstadoEntregaTC(numeroContrato, numeroTarjeta);
    }
    
    public CtVerificarExisteContratoNACARRs SCEPE20_verificarExisteContratoNACAR(CtVerificarExisteContratoNACARRq verificarExisteContratoNACARRq){
    	LOG.info("Metodo SCEPE20_verificarExisteContratoNACAR");
    	try{
    		return sce.verificarExisteContratoNACAR(verificarExisteContratoNACARRq);
    	}catch (Exception e) {
			LOG.info("e = "+e.getMessage());
			return null;
		}
    	
    }
    
    public CtExtraerTipoCambioRs extraerTipoCambio(CtExtraerTipoCambioRq extraerTipoCambioRq){
    	return tc84.extraerTipoCambio(extraerTipoCambioRq);
    }
    
	@Override
	public boolean verificarExisteContratoNacar(String codEmpleado, String codCliente, String nroContrato) {
		CtVerificarExisteContratoNACARRq inVerificarExisteContratoNACAR = new CtVerificarExisteContratoNACARRq();
		CtHeader rh = new CtHeader();
		rh.setUsuario(codEmpleado);
		boolean result;
		
		CtBodyRq rq = new CtBodyRq();
		
		rq.setCodigoCentral(codCliente);
		rq.setNumeroContrato(nroContrato);
		
		inVerificarExisteContratoNACAR.setHeader(rh);
		inVerificarExisteContratoNACAR.setData(rq);	
		
		CtVerificarExisteContratoNACARRs outVerificarExisteContratoNACAR = null;
		
		try{
			outVerificarExisteContratoNACAR = SCEPE20_verificarExisteContratoNACAR(inVerificarExisteContratoNACAR);
		}catch (Exception e) {
			//logger.error(e);
		}		
		
		if (outVerificarExisteContratoNACAR==null || !outVerificarExisteContratoNACAR.getHeader().getCodigo().equals(Constantes.VERIFICAR_NUMERO_CONTRATO_OK)){
			LOG.info("result es nulo o igual a 00 ");
			result = false;
		}else{
		    result = true;
		}
		LOG.info("result : "+result);
		return result;
	}
	
	//Emi
	public DatosGeneralesXpersonaRs consultaDatosHarec(DatosGeneralesXPersonaRq inDatosGeneralesXPersona){
		LOG.info("Metodo consultaDatosHarec");
		try{
			return harec.obtenerDatosXPersona(inDatosGeneralesXPersona);
		}catch (Exception e) {
			LOG.info("e = "+e.getMessage());
			return null;
		}	   	
	}
	
	public ConsultaPorDNIResponse consultaDatosReniec(ConsultaPorDNIRequest inConsultaReniecPorDNI){
	   	return reniec.consultaPorDNI(inConsultaReniecPorDNI);	   	
	}

	public String validacionNumeroContradoClientePackPyme(String numeroContrato, String codigoTipoDoi, String numDoi, String codigoEmpleado, String fechaEnvio, String idSession){
		HashMap<String,String> parameteres = new HashMap<String, String>(); ;
		for (ParametrosConf parametrosConf : parametrosConfBean.buscarPorAplicativo(Constantes.COD_APLICATIVO_PACKPYME)) {
			parameteres.put(parametrosConf.getNombreVariable(), parametrosConf.getValorVariable());
		}

		pe.com.bbva.hi.cttactpass.cvalnrocttcli.IntegrationRequest request = new pe.com.bbva.hi.cttactpass.cvalnrocttcli.IntegrationRequest();
		pe.com.bbva.hi.cttactpass.cvalnrocttcli.IntegrationResponse response = null;
		
		pe.com.bbva.hi.cttactpass.cabecera.RequestHeader requestHeader = new pe.com.bbva.hi.cttactpass.cabecera.RequestHeader();
		requestHeader.setCodigoEmpresa(parameteres.get(Constantes.PACKPYME_CABECERA_CODEMPRESA));
		requestHeader.setCodigoTerminalEmpresa(parameteres.get(Constantes.PACKPYME_CABECERA_CODTERMINALEMPRESA));
		requestHeader.setCanal(parameteres.get(Constantes.PACKPYME_CABECERA_CANAL));
		requestHeader.setCodigoAplicacion(parameteres.get(Constantes.PACKPYME_CABECERA_CODAPLICACION));
		requestHeader.setUsuario(codigoEmpleado);
		requestHeader.setFechaHoraEnvio(fechaEnvio);
		requestHeader.setIdSesion(idSession);

	       DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
	       //get current date time with Date()
	       Date date = new Date();
	       String fecha=dateFormat.format(date);
	       
	       LOG.info(fecha);
	       
	  //requestHeader.setIdPeticionEmpresa(parameteres.get(Constantes.PACKPYME_CABECERA_IDPETICIONEMPRESA));
	 // Cambiado en este formato - "AAMMDD" + "CodigoEmpresa" + "IdPeticionEmpresa" + "NumeroOperacionEmpresa"
		requestHeader.setIdPeticionEmpresa(fecha+parameteres.get(Constantes.PACKPYME_CABECERA_CODEMPRESA)+
				parameteres.get(Constantes.PACKPYME_CABECERA_CODTERMINALEMPRESA)+parameteres.get(Constantes.PACKPYME_CABECERA_IDPETICIONEMPRESA));
			
		requestHeader.setIdPeticionBanco(parameteres.get(Constantes.PACKPYME_CABECERA_IDPETICIONBANCO));
		requestHeader.setIdOperacion(parameteres.get(Constantes.PACKPYME_CABECERA_IDOPERACION));
		requestHeader.setIdServicio(parameteres.get(Constantes.PACKPYME_CABECERA_IDSERVICIO));
		requestHeader.setIdInterconexion(parameteres.get(Constantes.PACKPYME_CABECERA_IDINTERCONEXION));
		//Body
		request.setRequestHeader(requestHeader);
		request.setNumContrato(numeroContrato);
		request.setTipoDocumento(codigoTipoDoi);
		request.setNumDocumento(numDoi);
		request.setUsuario(codigoEmpleado);
		response = cttactpass.validacionNumeroContradoCliente(request);
		return response.getCodigoRetorno();
	}
	
	/**
	 * Tabla General
	 */
	public List<com.bbva.general.entities.Oficina> getOficinas(String codOficina, String descOficina){
		List<com.bbva.general.entities.Oficina> listaOficinasWS = (List<com.bbva.general.entities.Oficina>) tablaGeneral.getOficinas(codOficina, descOficina);
		return listaOficinasWS;
	}
	
	public List<com.bbva.general.entities.Territorio> getTerritorioListado(){
		List<com.bbva.general.entities.Territorio> listaTerritoriosWS = tablaGeneral.getTerritorioListado().getTerritorio();
		return listaTerritoriosWS;
	}
	public List<com.bbva.general.entities.Centro> getCentroListado(String codigoOficina){
		List<com.bbva.general.entities.Centro> listaOficinasWS = tablaGeneral.getCentroListado(codigoOficina).getCentro();
		return listaOficinasWS;
	}
	
	public List<com.bbva.general.entities.Feriado> getFeriadoListado(){
		List<com.bbva.general.entities.Feriado> listaFeriadosWS = tablaGeneral.getFeriadoListado().getFeriado();
		return listaFeriadosWS;
	}
}