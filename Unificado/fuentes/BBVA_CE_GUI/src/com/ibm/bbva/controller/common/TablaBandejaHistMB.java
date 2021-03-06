package com.ibm.bbva.controller.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.cm.service.impl.Documento;
import com.ibm.bbva.controller.AbstractSortPagDataTableMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;
import com.ibm.bbva.tabla.util.vo.HistorialDetalle;
import com.ibm.bbva.util.ComparatorFactory;
import com.ibm.bbva.util.comparators.ComparatorBase;

@SuppressWarnings("serial")
@ManagedBean(name = "tablaBandejaHist")
@RequestScoped
public class TablaBandejaHistMB extends AbstractSortPagDataTableMBean {

	@EJB
	HistorialBeanLocal historialBean;
	
	@EJB
	ExpedienteBeanLocal expedienteBean;
	@EJB
	private FacadeLocal facade;	
	private HtmlDataTable tablaBinding;
	private List<HistorialDetalle> listTabla;
	private List<Documento> listaDocumentosCM;
	private DocumentoExpTc objDocumentoExpTc;	
	private String numRegistros;
	
	private static final Logger LOG = LoggerFactory.getLogger(TablaBandejaHistMB.class);
	
	public TablaBandejaHistMB() {
		
	}
	
	@PostConstruct
	public void init() {
		actualizarLista ();
	}
	
	public void actualizarLista () {
		listTabla = (List<HistorialDetalle>) getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		
		if(listTabla!=null){
			this.setNumRegistros(String.valueOf(this.getListTabla().size())+" registros encontrados");
			LOG.info("ListTabla tama�o::::"+this.getListTabla().size());
		}else{
			this.setNumRegistros("0 registros encontrados");
			LOG.info("ListTabla es nulo");
		}  		
	}
	
	public String  seleccionaFila() {
		eliminarObjetosSession();
		
		String idHistorial = getRequestParameter("idHistorial");
		
		Expediente expediente = obtenerExpediente(idHistorial);
		
		addObjectSession(Constantes.EXPEDIENTE_SESION, expediente);

		addObjectSession(Constantes.NOMBRE_BANDEJA_SESION, getNombreJSPPrincipal());
		listTabla=null;
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		
		return "/visualizarExpediente/formVisualizarExpediente?faces-redirect=true";
	}

	public void setTablaBinding(HtmlDataTable tablaBinding) {
		this.tablaBinding = tablaBinding;
	}

	public HtmlDataTable getTablaBinding() {
		return tablaBinding;
	}

	public List<HistorialDetalle> getListTabla() {
		return listTabla;
	}

	public void setListTabla(List<HistorialDetalle> listTabla) {
		this.listTabla = listTabla;
	}
	
	public List<Documento> getListaDocumentosCM() {
		return listaDocumentosCM;
	}

	public void setListaDocumentosCM(List<Documento> listaDocumentosCM) {
		this.listaDocumentosCM = listaDocumentosCM;
	}

	public DocumentoExpTc getObjDocumentoExpTc() {
		return objDocumentoExpTc;
	}

	public void setObjDocumentoExpTc(DocumentoExpTc objDocumentoExpTc) {
		this.objDocumentoExpTc = objDocumentoExpTc;
	}

	@Override
	public void ordenar(ActionEvent event) {
		String sortFieldAttribute = getAttribute(event, "sortField");
		if (sortFieldAttribute != null) {
	        if (sortField != null && sortField.equals(sortFieldAttribute)) {
	            sortAscending = !sortAscending;
	        } else {
	            sortField = sortFieldAttribute;
	            sortAscending = true;
	        }
	        // regresa a la primera p�gina si cambia el orden
	        dataTable.setFirst(0);
		} // si no viene el atributo sortField no cambia el ordenamiento
		
		String columna = this.sortField;
		String orden = this.sortAscending ? ComparatorBase.SORT_ORDER_ASC : ComparatorBase.SORT_ORDER_DESC;
		Comparator<HistorialDetalle>  comparator = null;
		if ("estado".equals(columna)) {
			comparator = ComparatorFactory.estadoHistorial(orden);
		} else if ("segmento".equals(columna)) {
			comparator = ComparatorFactory.segmentoHistorial(orden);
		} else if ("tipoOferta".equals(columna)) {
			comparator = ComparatorFactory.tipoOfertaHistorial(orden);
		} else if ("tipoDoi".equals(columna)) {
			comparator = ComparatorFactory.tipoDOIHistorial(orden);
		} else if ("doi".equals(columna)) {
			comparator = ComparatorFactory.numeroDOIHistorial(orden);
		} else if ("apPaterno".equals(columna)) {
			comparator = ComparatorFactory.apPaternoHistorial(orden);
		} else if ("apMaterno".equals(columna)) {
			comparator = ComparatorFactory.apMaternoHistorial(orden);
		} else if ("nombres".equals(columna)) {
			comparator = ComparatorFactory.nombreHistorial(orden);
		} else if ("producto".equals(columna)) {
			comparator = ComparatorFactory.productoHistorial(orden);
		} else if ("subProducto".equals(columna)) {
			comparator = ComparatorFactory.subProductoHistorial(orden);
		} else if ("moneda".equals(columna)) {
			comparator = ComparatorFactory.monedaHistorial(orden);
		} else if ("codigo".equals(columna)) {
			comparator = ComparatorFactory.codigoHistorial(orden);
		}
		if (comparator != null) {
			Collections.sort(listTabla, comparator);
		}
	}
	
	private Expediente obtenerExpediente(String historial) {
		LOG.info("Metodo obtenerExpediente, historial = "+historial);
		long idHistorial = Long.parseLong(historial);
//		return ConvertExpediente.convertToExpedienteVO(historialBean.buscarPorId(idHistorial));
		Historial historialBD = historialBean.buscarPorId(idHistorial);
		Expediente expedienteBD = expedienteBean.buscarPorId(historialBD.getExpediente().getId());
		Expediente expedienteHistorico =  ConvertExpediente.convertToExpedienteVO(historialBD, expedienteBD);
		
		/**
		 * Obtiene URL Documento CM - 21 Julio 2014 (Maydeline chanchari)
		 * */
		System.out.println("Consulta CM");
		objDocumentoExpTc= new DocumentoExpTc();
		objDocumentoExpTc.setExpediente(new Expediente());
		//objDocumentoExpTc.getExpediente().setClienteNatural(new ClienteNatural());
		objDocumentoExpTc.getExpediente().setClienteNatural(expedienteHistorico.getClienteNatural());
	
		long tiempoInicio = System.currentTimeMillis();
		try{
			listaDocumentosCM = facade.obtenerListaDocumentoCM(objDocumentoExpTc);
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
		}
		long tiempoFin = System.currentTimeMillis();
		LOG.info("TablaBandejaHistMB.obtenerExpediente obtenerListaDocumentoCM demora: " + (tiempoFin - tiempoInicio) + " milisegundos");
		
		Map<String, Object> mapListDocumentosCM =new TreeMap<String, Object> ();
		if(listaDocumentosCM!=null)
		for(Documento objDocumento: listaDocumentosCM){
			mapListDocumentosCM.put(String.valueOf(objDocumento.getId()), objDocumento);
		}
	else
		System.out.println("listaDocumentosCM es nulo");
		addObjectSession(Constantes.EXPEDIENTE_SESION_HISTORICO, expedienteHistorico );
		addObjectSession(Constantes.EXPEDIENTE_LISTA_DOCUMENTO_CM, mapListDocumentosCM);
		return expedienteHistorico;
	}

	public String getNumRegistros() {
		return numRegistros;
	}

	public void setNumRegistros(String numRegistros) {
		this.numRegistros = numRegistros;
	}

	@Override
	public void actualiarAyudaHorario(ActionEvent event) {
		// TODO Ap�ndice de m�todo generado autom�ticamente
		
	}

	
}