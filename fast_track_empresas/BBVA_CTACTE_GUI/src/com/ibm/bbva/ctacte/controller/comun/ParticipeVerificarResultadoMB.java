package com.ibm.bbva.ctacte.controller.comun;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.Mensaje;
import com.ibm.bbva.ctacte.bean.Participe;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractTablaMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IParticipeVerificarResultado;
import com.ibm.bbva.ctacte.controller.form.VerificarResultadoTramiteMB;
import com.ibm.bbva.ctacte.dao.MensajeDAO;
import com.ibm.bbva.ctacte.dao.ParticipeDAO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean(name="participeVerificarResultado")
@ViewScoped
public class ParticipeVerificarResultadoMB  extends AbstractTablaMBean {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ParticipeVerificarResultadoMB.class);
	@ManagedProperty(value="#{verificarResultadoTramite}")
	private VerificarResultadoTramiteMB verificarResultadoTramite;
	private IParticipeVerificarResultado iParticipeVerificarResultado;
	private Expediente expediente;	
	private List<Participe> listaParticipe;
	private String value;
	private int idTarea;
	private String textoAyudaTieneFirmas;
	@EJB
	private ParticipeDAO participeDAO;
	@EJB
	private MensajeDAO mensajeDAO;
	
	@PostConstruct
	public void iniciar(){
		LOG.info("iniciar()");
		String pagina=getNombrePrincipal();
		LOG.info("Pagina actual{}",pagina);
		if ("formVerificarResultadoTramite".equals(pagina)){
			iParticipeVerificarResultado=verificarResultadoTramite;			
		}
		expediente=(Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		LOG.info("expediente"+expediente.getId());
		crearListaParticipes(expediente);
		for (ExpedienteTarea expedienteTarea : expediente.getExpedienteTareas()) {
			if (ConstantesAdmin.FORM_VERIFICAR_RESULTADO_TRAMITE.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				idTarea = expedienteTarea.getTarea().getId().intValue();
				break;
			}
		}
		iParticipeVerificarResultado.setIParticipeVerificarResultado(this);
		
		//Valores por defecto del paginador
		setRowsPerPage(new Integer(20));
		List<SelectItem> listPaginas = new ArrayList<SelectItem>();
		listPaginas.add(new SelectItem("20", "20"));
		listPaginas.add(new SelectItem("50", "50"));
		setListOfSelectItems(listPaginas);
		
		Mensaje mensajeAyudaTieneFirmas = mensajeDAO.load(ConstantesBusiness.ID_MENSAJE_TIENE_FIRMAS);
		if (mensajeAyudaTieneFirmas != null && mensajeAyudaTieneFirmas.getContenido() != null) {
			try {
				textoAyudaTieneFirmas = new String(mensajeAyudaTieneFirmas.getContenido(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				LOG.error(e.getMessage(), e);
				textoAyudaTieneFirmas = null;
			}
		} else {
			textoAyudaTieneFirmas = null;
		}
	}
	
	private void crearListaParticipes(Expediente expediente){
		listaParticipe=participeDAO.findByExpediente(expediente);			
	}

	public VerificarResultadoTramiteMB getVerificarResultadoTramite() {
		return verificarResultadoTramite;
	}

	public void setVerificarResultadoTramite(
			VerificarResultadoTramiteMB verificarResultadoTramite) {
		this.verificarResultadoTramite = verificarResultadoTramite;
	}

	public IParticipeVerificarResultado getIParticipeVerificarResultado() {
		return iParticipeVerificarResultado;
	}

	public void setIParticipeVerificarResultado(
			IParticipeVerificarResultado participeVerificarResultado) {
		iParticipeVerificarResultado = participeVerificarResultado;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public void setListaParticipe(List<Participe> listaParticipe) {
		this.listaParticipe = listaParticipe;
	}

	public List<Participe> getListaParticipe() {
		return listaParticipe;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public int getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}

	public String getTextoAyudaTieneFirmas() {
		return textoAyudaTieneFirmas;
	}

	public void setTextoAyudaTieneFirmas(String textoAyudaTieneFirmas) {
		this.textoAyudaTieneFirmas = textoAyudaTieneFirmas;
	}

	@Override
	public int getRowCount() {
		return listaParticipe.size();
	}
		
}
