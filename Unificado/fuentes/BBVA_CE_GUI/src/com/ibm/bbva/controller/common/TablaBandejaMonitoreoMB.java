package com.ibm.bbva.controller.common;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bpd.RemoteUtils;

import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.cm.service.impl.Documento;
import com.ibm.bbva.controller.AbstractSortPagDataTableMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;
import com.ibm.bbva.util.ExpedienteTCWrapper;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "tablaBandejaMonitoreo")
@RequestScoped
public class TablaBandejaMonitoreoMB extends AbstractSortPagDataTableMBean {
	
	@EJB
	private ExpedienteBeanLocal expedienteBean;
	@EJB
	private TareaBeanLocal tareaBean;
	@EJB
	private FacadeLocal facade;
	
	private HtmlDataTable tablaBinding;	
	private String numRegistros;
	private List<ExpedienteTCWPS> listTabla;
	private boolean renderedRb;
	private boolean process;
	private boolean content;
	private boolean log;
	
	private boolean ejecDblClick = false;
	private String cadExpSelecc;
	private String actionCheck;
	
	private List<Documento> listaDocumentosCM;
	private DocumentoExpTc objDocumentoExpTc;
	
	private static final Logger LOG = LoggerFactory.getLogger(TablaBandejaMonitoreoMB.class);
			
	public TablaBandejaMonitoreoMB() {
		
	}
	
	@PostConstruct
	public void init() {
		LOG.info("en el metodo init de TablaBandejaMonitoreoMB");
		
		listTabla = (List<ExpedienteTCWPS>) getObjectSession(Constantes.LISTA_BANDEJA_MONITOREO);
		
		this.renderedRb = false;
		this.process = false;
		this.content = false;
		this.log = false;
		
		this.ejecDblClick = true;
		
		if(listTabla != null && listTabla.size() > 0){
			this.setNumRegistros(String.valueOf(this.getListTabla().size())+" registros encontrados");
			LOG.info("ListTabla tamaño::::"+this.getListTabla().size());
		}else{
			this.setNumRegistros("0 registros encontrados");
			LOG.info("ListTabla es nulo");
		}
		
		mostrarColumnas();
	}
	
	public void actualizarLista(){
		LOG.info("en el metodo actualizarLista de TablaBandejaMonitoreoMB");
		
		listTabla = (List<ExpedienteTCWPS>) getObjectSession(Constantes.LISTA_BANDEJA_MONITOREO);
		
		if(listTabla != null && listTabla.size() > 0){
			this.setNumRegistros(String.valueOf(this.getListTabla().size())+" registros encontrados");
			LOG.info("ListTabla tamaño::::"+this.getListTabla().size());
		}else{
			this.setNumRegistros("0 registros encontrados");
			LOG.info("ListTabla es nulo");
		}
		
		mostrarColumnas();
	}
	
	private void mostrarColumnas(){
		String tipoBusqueda = getObjectSession(Constantes.TIPO_BUSQUEDA_BM) == null ? null : (String)getObjectSession(Constantes.TIPO_BUSQUEDA_BM);
		if(tipoBusqueda != null){
			if("3".equals(tipoBusqueda)){ // content
				this.renderedRb = true;
				this.content = true;
				this.process = false;
				this.log = false;
			}
			else if("2".equals(tipoBusqueda)){ // process
				this.renderedRb = true;
				this.content = false;
				this.process = true;
				this.log = false;
			}
			else{ // log de errores
				this.renderedRb = false;
				this.content = false;
				this.process = false;
				this.log = true;
			}
		}
	}
	
	public String seleccionaFila() {
		LOG.info("seleccionaFila");		
		LOG.info("actionCheck::::"+actionCheck);
		String codigo = getRequestParameter("codigo");
		
		Expediente expediente = obtenerExpediente(codigo);
		
		addObjectSession(Constantes.EXPEDIENTE_SESION, expediente);

		for(ExpedienteTCWPS expedienteTCWPS : listTabla){
			if(expedienteTCWPS.getCodigo().equals(String.valueOf(expediente.getId()))){
				addObjectSession(Constantes.EXPEDIENTE_PROCESO_SESION, expedienteTCWPS);
				break;
			}
		}
		
		addObjectSession(Constantes.NOMBRE_BANDEJA_SESION, getNombreJSPPrincipal());
		
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		
		String tipoBusqueda = getObjectSession(Constantes.TIPO_BUSQUEDA_BM) == null ? null : (String)getObjectSession(Constantes.TIPO_BUSQUEDA_BM);
		
		return "/visualizarExpediente/formVisualizarExpediente?" +
				"bandejaMonitoreo=true" +
				"&tipo=" + tipoBusqueda + 
				"&faces-redirect=true";
	}
	
	public void setTablaBinding(HtmlDataTable tablaBinding) {
		this.tablaBinding = tablaBinding;
	}

	public HtmlDataTable getTablaBinding() {
		return tablaBinding;
	}
	
	/**
	 * Copiado y modificado de 
	 * BuscarBandejaMB
	 * @param codigo código de Expediente (ID)
	 * @return
	 */
	private Expediente obtenerExpediente(String codigo) {
		Expediente expediente = expedienteBean.buscarPorId(Long.parseLong(codigo));
		
		/**
		 * Obtiene URL Documento CM - 21 Julio 2014 (Maydeline chanchari)
		 * */
		System.out.println("Consulta CM");
		objDocumentoExpTc= new DocumentoExpTc();
		objDocumentoExpTc.setExpediente(expediente);
		
		if(objDocumentoExpTc.getExpediente() != null)
			objDocumentoExpTc.getExpediente().setClienteNatural(new ClienteNatural());
	
		try{
			listaDocumentosCM = facade.obtenerListaDocumentoCM(objDocumentoExpTc);
		}catch(Exception e){
			e.getStackTrace();
		}
		
		Map<String, Object> mapListDocumentosCM =new TreeMap<String, Object> ();
		if(listaDocumentosCM!=null)
			for(Documento objDocumento: listaDocumentosCM){
				mapListDocumentosCM.put(String.valueOf(objDocumento.getId()), objDocumento);
			}
		else
			System.out.println("listaDocumentosCM es nulo");
		
		addObjectSession(Constantes.EXPEDIENTE_LISTA_DOCUMENTO_CM, mapListDocumentosCM);
		return expediente;
	}
	
	public String getNumRegistros() {
		return numRegistros;
	}

	public void setNumRegistros(String numRegistros) {
		this.numRegistros = numRegistros;
	}
	
	public List<ExpedienteTCWPS> getListTabla() {
		return listTabla;
	}

	public void setListTabla(List<ExpedienteTCWPS> listTabla) {
		this.listTabla = listTabla;
	}

	public boolean isRenderedRb() {
		return renderedRb;
	}

	public void setRenderedRb(boolean renderedRb) {
		this.renderedRb = renderedRb;
	}

	public boolean isProcess() {
		return process;
	}

	public void setProcess(boolean process) {
		this.process = process;
	}

	public boolean isContent() {
		return content;
	}

	public void setContent(boolean content) {
		this.content = content;
	}

	public boolean isLog() {
		return log;
	}

	public void setLog(boolean log) {
		this.log = log;
	}

	@Override
	public void ordenar(ActionEvent event) {
		// TODO Auto-generated method stub
	}

	public boolean isEjecDblClick() {
		return ejecDblClick;
	}

	public void setEjecDblClick(boolean ejecDblClick) {
		this.ejecDblClick = ejecDblClick;
	}

	public String getCadExpSelecc() {
		return cadExpSelecc;
	}

	public void setCadExpSelecc(String cadExpSelecc) {
		this.cadExpSelecc = cadExpSelecc;
	}

	public String getActionCheck() {
		return actionCheck;
	}

	public void setActionCheck(String actionCheck) {
		this.actionCheck = actionCheck;
	}

	@Override
	public void actualiarAyudaHorario(ActionEvent event) {
		// TODO Apéndice de método generado automáticamente
		
	}

}