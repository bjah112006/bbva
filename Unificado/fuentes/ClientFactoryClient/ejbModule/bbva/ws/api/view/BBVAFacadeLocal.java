package bbva.ws.api.view;

import java.util.List;

import pe.com.bbva.scepe20.client.CtVerificarExisteContratoNACARRq;
import pe.com.bbva.scepe20.client.CtVerificarExisteContratoNACARRs;
import pe.com.grupobbva.sce.tc84.CtExtraerTipoCambioRq;
import pe.com.grupobbva.sce.tc84.CtExtraerTipoCambioRs;

import com.bbva.general.entities.Centro;
import com.bbva.general.entities.Feriado;
import com.bbva.general.entities.Oficina;
import com.bbva.general.entities.Territorio;
import com.ibm.bbva.harec.client.DatosGeneralesXPersonaRq;
import com.ibm.bbva.harec.client.DatosGeneralesXpersonaRs;
import com.ibm.bbva.reniec.message.ConsultaPorDNIRequest;
import com.ibm.bbva.reniec.message.ConsultaPorDNIResponse;

public interface BBVAFacadeLocal {

	//ClienteReniecDTO Reniec_consultaReniec(String numeroDOI, String oficina, String usuario);
	
	String WFTarjetas_devolverEstadoEntregaTC(String numeroContrato, String numeroTarjeta);
	
	CtVerificarExisteContratoNACARRs SCEPE20_verificarExisteContratoNACAR(CtVerificarExisteContratoNACARRq verificarExisteContratoNACARRq);
	
	CtExtraerTipoCambioRs extraerTipoCambio(CtExtraerTipoCambioRq extraerTipoCambioRq);	
	
	public boolean verificarExisteContratoNacar(String codEmpleado, String codCliente, String nroContrato);
	
	//Emi
	DatosGeneralesXpersonaRs consultaDatosHarec(DatosGeneralesXPersonaRq inDatosGeneralesXPersona);
	
	ConsultaPorDNIResponse consultaDatosReniec(ConsultaPorDNIRequest inConsultaReniecPorDNI);
	
	List<Oficina> getOficinas(String codOficina, String descOficina);
	
	List<Territorio> getTerritorioListado();
	
	List<Centro> getCentroListado(String codigoOficina);
	
	List<Feriado> getFeriadoListado();
	
	String validacionNumeroContradoClientePackPyme(String numeroContrato, String codigoTipoDoi, String numDoi, String codigoEmpleado, String fechaEnvio, String idSession);

}
