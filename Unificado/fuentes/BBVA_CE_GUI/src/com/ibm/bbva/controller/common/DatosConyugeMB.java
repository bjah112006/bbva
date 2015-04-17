package com.ibm.bbva.controller.common;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.GrupoSegmento;
import com.ibm.bbva.entities.Segmento;
import com.ibm.bbva.session.ClienteNaturalBeanLocal;
import com.ibm.bbva.session.SegmentoBeanLocal;



@SuppressWarnings("serial")
@ManagedBean(name = "datosConyuge")
@RequestScoped
public class DatosConyugeMB extends AbstractMBean {
	
	@EJB
	private ClienteNaturalBeanLocal clienteNaturalbean;
	@EJB
	private SegmentoBeanLocal segmentobean;
	
	private ClienteNatural clienteNatural;
	private String mensajeOperacion;
	private boolean renderedTxtOper;
	public boolean mostrarDatosConyuge;
	public boolean mostrarCampos;
	private String estadoCivilCasado;
	private String estadoCivilConviviente;
	private String tipoOferta;
	public boolean mostrarDatosConyugePrincipal;
	private Segmento segmento;
	private boolean mostrarTituloConyugeA;
	
	private static final Logger LOG = LoggerFactory.getLogger(DatosConyugeMB.class);
	
	public DatosConyugeMB() {		
	}
	
	@PostConstruct
    public void init() {
		//this.mensajeOperacion=null;
		LOG.info("init  de DatosConyugeMB");
		
		Expediente expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		estadoCivilCasado=Constantes.EST_CIVIL_CASADO;
		estadoCivilConviviente=Constantes.EST_CIVIL_CONVIVIENTE;
		tipoOferta=Constantes.CODIGO_OFERTA_APROBADO;
		segmento = new Segmento();
		segmento.setGrupoSegmento(new GrupoSegmento());
		
		//expediente.getId() > 0 && 
		//expediente.getExpedienteTC().getClienteNaturalConyuge().getId()>0 && 
	
		//(expediente.getClienteNatural().getEstadoCivil().getCodigo().equals(estadoCivilCasado) || 
	//	expediente.getClienteNatural().getEstadoCivil().getCodigo().equals(estadoCivilConviviente))
		
		if (expediente != null &&  
				expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getClienteNaturalConyuge()!=null && 
				(expediente.getExpedienteTC().getClienteNaturalConyuge().getNumDoi()!=null && 
				!expediente.getExpedienteTC().getClienteNaturalConyuge().getNumDoi().trim().equals(""))) {
			LOG.info("Numero DOI:::"+expediente.getExpedienteTC().getClienteNaturalConyuge().getNumDoi());
			if(expediente.getExpedienteTC().getClienteNaturalConyuge().getId()>0){
				this.clienteNatural = clienteNaturalbean.buscarPorId(expediente.getExpedienteTC().getClienteNaturalConyuge().getId());	
				LOG.info("EXISTE ID CONYUGE = "+expediente.getExpedienteTC().getClienteNaturalConyuge().getId());
				//LOG.info("NUMERO DOI "+expediente.getExpedienteTC().getClienteNaturalConyuge().getNumDoi());
			}else{
				this.clienteNatural=expediente.getExpedienteTC().getClienteNaturalConyuge();
				LOG.info("NO EXISTE ID CONYUGE");
			}

			this.setMensajeOperacion("");
			this.setRenderedTxtOper(false);
			this.setMostrarDatosConyuge(true);
			this.mostrarCampos=true;
			//mostrarDatosConyugePrincipal=true;
			LOG.info("ENTRO EN OPCION 1");
			addObjectSession("showDatosConyuge", "1");
		} else {
			if(expediente != null && expediente.getExpedienteTC()!=null && 
					expediente.getExpedienteTC().getClienteNaturalConyuge()!=null){
				this.clienteNatural = clienteNaturalbean.buscarPorId(expediente.getExpedienteTC().getClienteNaturalConyuge().getId());	
				LOG.info("ENTRO EN OPCION 2");
				//mostrarDatosConyugePrincipal=true;
				this.setMostrarDatosConyuge(true);
				this.mostrarCampos=true;
				addObjectSession("showDatosConyuge", "1");

			}else{

				clienteNatural = new ClienteNatural();
		
				LOG.info("ENTRO EN OPCION 3");
				//mostrarDatosConyugePrincipal=true;
				this.mostrarCampos=true;
				this.setMostrarDatosConyuge(false);
				addObjectSession("showDatosConyuge", "0");
				this.setRenderedTxtOper(true);	
				
			}
		}

		iniciaDatos();
		
	}
	
	public void iniciaDatos(){
		if(clienteNatural!=null && clienteNatural.getSegmento()!=null && clienteNatural.getSegmento().getId() > 0){			
			segmento = segmentobean.buscarPorId(clienteNatural.getSegmento().getId());
		}else{
			for(Segmento vo : segmentobean.buscarTodos()){
				if(vo.getDescripcion().equals(Constantes.SEGMENTO_NINGUNO_DESCRIPCION)){
					segmento = vo;
				}
			}
		}		
	}
	
	public void copiarDatos(Expediente objExpediente){
		LOG.info("estadoCivilCasado ="+estadoCivilCasado);
		LOG.info("expedienteTC = "+objExpediente.getExpedienteTC()==null?" ES NULO ":"NO ES NULO");
		LOG.info("getClienteNaturalConyuge = "+objExpediente.getExpedienteTC().getClienteNaturalConyuge()==null?" ES NULO ":"NO ES NULO");
		//LOG.info("getEstadoCivil = "+objExpediente.getClienteNatural().getEstadoCivil().getCodigo()==null?" ES NULO ":objExpediente.getClienteNatural().getEstadoCivil().getCodigo());
		
		//objExpediente.getExpedienteTC().getClienteNaturalConyuge().getId()>0 &&
		LOG.info("NUMERO DOI - COPIAR DATOS ... "+objExpediente.getExpedienteTC().getClienteNaturalConyuge().getNumDoi());
//		(objExpediente.getClienteNatural().getEstadoCivil().getCodigo().equals(estadoCivilCasado) || 
//				objExpediente.getClienteNatural().getEstadoCivil().getCodigo().equals(estadoCivilConviviente))
		if (objExpediente != null && 
				objExpediente.getExpedienteTC()!=null && objExpediente.getExpedienteTC().getClienteNaturalConyuge()!=null &&
				(objExpediente.getExpedienteTC().getClienteNaturalConyuge().getNumDoi()!=null && 
				!objExpediente.getExpedienteTC().getClienteNaturalConyuge().getNumDoi().trim().equals(""))) {
			LOG.info("Si cumple con condicion");
			//List<ClienteNatural>  listClienteNatural= clienteNaturalbean.buscarPorTipoNumDoi(objExpediente.getExpedienteTC().getClienteNaturalConyuge());
			
			//if(listClienteNatural.size()>0){
			//	this.clienteNatural = listClienteNatural.get(0);
				this.clienteNatural=objExpediente.getExpedienteTC().getClienteNaturalConyuge();
				this.setMensajeOperacion("");
				this.mostrarCampos=true;
				this.setMostrarDatosConyuge(true);
			//}else
				/*{
				LOG.info("no hay registros en lista");
				this.clienteNatural=new ClienteNatural();
				this.setMensajeOperacion("No se encontró datos de conyuge");
				this.mostrarCampos=false;
			}
			*/
				
			//this.clienteNatural = clienteNaturalbean.buscarPorId(objExpediente.getExpedienteTC().getClienteNaturalConyuge().getId());			
			
			this.setRenderedTxtOper(false);
			this.setMostrarDatosConyuge(true);
		} else {
			LOG.info("No cumple con condicion");
			
			clienteNatural = new ClienteNatural();
			this.setMensajeOperacion("No se encontró datos de conyuge");
			this.mostrarCampos=false;
			this.setRenderedTxtOper(false);
			this.setMostrarDatosConyuge(true);
			
		}
	}

	public ClienteNatural getClienteNatural() {
		return clienteNatural;
	}

	public void setClienteNatural(ClienteNatural clienteNatural) {
		this.clienteNatural = clienteNatural;
	}

	public String getMensajeOperacion() {
		return mensajeOperacion;
	}

	public void setMensajeOperacion(String mensajeOperacion) {
		this.mensajeOperacion = mensajeOperacion;
	}

	public boolean isRenderedTxtOper() {
		return renderedTxtOper;
	}

	public void setRenderedTxtOper(boolean renderedTxtOper) {
		this.renderedTxtOper = renderedTxtOper;
	}

	public String getEstadoCivilCasado() {
		return estadoCivilCasado;
	}

	public void setEstadoCivilCasado(String estadoCivilCasado) {
		this.estadoCivilCasado = estadoCivilCasado;
	}

	public String getEstadoCivilConviviente() {
		return estadoCivilConviviente;
	}

	public void setEstadoCivilConviviente(String estadoCivilConviviente) {
		this.estadoCivilConviviente = estadoCivilConviviente;
	}

	public String getTipoOferta() {
		return tipoOferta;
	}

	public void setTipoOferta(String tipoOferta) {
		this.tipoOferta = tipoOferta;
	}

	public boolean isMostrarDatosConyuge() {
		return mostrarDatosConyuge;
	}

	public void setMostrarDatosConyuge(boolean mostrarDatosConyuge) {
		this.mostrarDatosConyuge = mostrarDatosConyuge;
	}

	public ClienteNaturalBeanLocal getClienteNaturalbean() {
		return clienteNaturalbean;
	}

	public void setClienteNaturalbean(ClienteNaturalBeanLocal clienteNaturalbean) {
		this.clienteNaturalbean = clienteNaturalbean;
	}

	public boolean isMostrarCampos() {
		return mostrarCampos;
	}

	public void setMostrarCampos(boolean mostrarCampos) {
		this.mostrarCampos = mostrarCampos;
	}

	public boolean isMostrarDatosConyugePrincipal() {
		String result=(String) getObjectSession("showDatosConyuge");
		LOG.info("result:::"+result);
		mostrarDatosConyugePrincipal=((result!=null && result.equals("1"))?true:false);
		LOG.info("mostrarDatosConyugePrincipal:::"+mostrarDatosConyugePrincipal);
		return mostrarDatosConyugePrincipal;
	}

	public void setMostrarDatosConyugePrincipal(boolean mostrarDatosConyugePrincipal) {
		this.mostrarDatosConyugePrincipal = mostrarDatosConyugePrincipal;
	}

	public Segmento getSegmento() {
		return segmento;
	}

	public void setSegmento(Segmento segmento) {
		this.segmento = segmento;
	}

	public boolean isMostrarTituloConyugeA() {
		return mostrarTituloConyugeA;
	}

	public void setMostrarTituloConyugeA(boolean mostrarTituloConyugeA) {
		this.mostrarTituloConyugeA = mostrarTituloConyugeA;
	}	
	
	
}
