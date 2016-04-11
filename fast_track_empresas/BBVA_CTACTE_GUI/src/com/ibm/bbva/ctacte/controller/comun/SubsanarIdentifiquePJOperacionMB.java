package com.ibm.bbva.ctacte.controller.comun;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.Participe;
import com.ibm.bbva.ctacte.business.ClienteBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.ISubsanarIdentifiquePJOperacion;
import com.ibm.bbva.ctacte.controller.form.SubsanarDocumentoMB;
import com.ibm.bbva.ctacte.controller.form.SubsanarFirmasMB;
import com.ibm.bbva.ctacte.dao.ClienteDAO;
import com.ibm.bbva.ctacte.dao.ParticipeDAO;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.bbva.ctacte.wrapper.ClientePJCoreWrapper;

@ManagedBean(name="subsanarIdentifiquePJOperacion")
@ViewScoped
public class SubsanarIdentifiquePJOperacionMB extends AbstractMBean{
	
	private static final long serialVersionUID = -6399120168925416691L;
	private static final Logger LOG = LoggerFactory.getLogger(SubsanarIdentifiquePJOperacionMB.class);
	@ManagedProperty(value="#{subsanarDocumento}")
	private SubsanarDocumentoMB subsanarDocumento;
	@ManagedProperty(value="#{subsanarFirmas}")
	private SubsanarFirmasMB subsanarFirmas;
	private ISubsanarIdentifiquePJOperacion iSubsanarIdentifiquePJOperacion;
	private List<Cliente> listaCliente;
	private List<Participe> listaParticipe;
	private ClientePJCoreWrapper clienteWrapper;
	private boolean mostrarTabla;
	private Expediente expediente;
	private int idTarea;
	private String esMigrado;
	@EJB
	private ClienteBusiness buscarCliente;
	//private Cliente cliente;
	@EJB
	private ClienteDAO clienteDAO;
	@EJB
	private ParticipeDAO participeDAO;
	
	@PostConstruct
	public void iniciar(){
		LOG.info("iniciar()");
		String pagina=getNombrePrincipal();
		LOG.info("Pagina actual{}",pagina);
		if ("formSubsanarDocumentos".equals(pagina)){
			LOG.info("Entro al if (formSubsanarDocumentos.equals(pagina))");
			iSubsanarIdentifiquePJOperacion=subsanarDocumento;
			LOG.info("Salio del if (formSubsanarDocumentos.equals(pagina))");
		}else if ("formSubsanarFirmas".equals(pagina)){
			iSubsanarIdentifiquePJOperacion=subsanarFirmas;
		} 
		LOG.info("entrara a buscar el expediente");
		expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		LOG.info("ya debio encontrar el expediente");
		LOG.info("id expediente"+expediente.getId());
		/*for (ExpedienteTarea expedienteTarea : expediente.getExpedienteTareas()) {
			if (ConstantesAdmin.FORM_SUBSANAR_DOCUMENTOS.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				idTarea = expedienteTarea.getTarea().getId().intValue();
				break;
			}else if (ConstantesAdmin.FORM_SUBSANAR_FIRMAS.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				idTarea=expedienteTarea.getTarea().getId().intValue();
			}
		}*/
		
		listaCliente=clienteDAO.findByExpediente(expediente);	
		
		LOG.info("Razon Social"+listaCliente.get(0).getRazonSocial());
			  
		listaParticipe=participeDAO.findByExpediente(expediente);
		
		
		/*crearListaClientes(expediente);
		crearListaParticipes(expediente);*/
		Cliente c = buscarCliente.getDatosCliente(expediente.getCliente().getId());
		if (c!=null) {
		   try {
				esMigrado = (!c.getFlagOrigenSFP().equalsIgnoreCase("1")) ? "No" : "Si"; 
			} catch (Exception e) {
				LOG.info("Error al verificar si el cliente es migrado", e);
			}
		}
		
	}	
	
	/*private void crearListaClientes(Expediente expediente){
		ClienteDAO clienteDAO=DAOFactory.getInstance().getClienteDAO();
		listaCliente=clienteDAO.findByExpediente(expediente);		
	}
	
	private void crearListaParticipes(Expediente expediente){
		ParticipeDAO participeDAO=DAOFactory.getInstance().getParticipeDAO();	  
		listaParticipe=participeDAO.findByExpediente(expediente);
	}
	*/
	public SubsanarDocumentoMB getSubsanarDocumento() {
		return subsanarDocumento;
	}

	public void setSubsanarDocumento(SubsanarDocumentoMB subsanarDocumento) {
		this.subsanarDocumento = subsanarDocumento;
	}

	public ISubsanarIdentifiquePJOperacion getISubsanarIdentifiquePJOperacion() {
		return iSubsanarIdentifiquePJOperacion;
	}

	public void setISubsanarIdentifiquePJOperacion(
			ISubsanarIdentifiquePJOperacion subsanarIdentifiquePJOperacion) {
		iSubsanarIdentifiquePJOperacion = subsanarIdentifiquePJOperacion;
	}	

	public void setClienteWrapper(ClientePJCoreWrapper clienteWrapper) {
		this.clienteWrapper = clienteWrapper;
	}

	public ClientePJCoreWrapper getClienteWrapper() {
		return clienteWrapper;
	}

	public void setMostrarTabla(boolean mostrarTabla) {
		this.mostrarTabla = mostrarTabla;
	}

	public boolean isMostrarTabla() {
		return mostrarTabla;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setListaCliente(List<Cliente> listaCliente) {
		this.listaCliente = listaCliente;
	}

	public List<Cliente> getListaCliente() {
		return listaCliente;
	}	

	public void setListaParticipe(List<Participe> listaParticipe) {
		this.listaParticipe = listaParticipe;
	}

	public List<Participe> getListaParticipe() {
		return listaParticipe;
	}

	public SubsanarFirmasMB getSubsanarFirmas() {
		return subsanarFirmas;
	}

	public void setSubsanarFirmas(SubsanarFirmasMB subsanarFirmas) {
		this.subsanarFirmas = subsanarFirmas;
	}

	public int getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}

	public String getEsMigrado() {
		return esMigrado;
	}

	public void setEsMigrado(String esMigrado) {
		this.esMigrado = esMigrado;
	}


}
