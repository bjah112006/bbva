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
import com.ibm.bbva.tabla.dto.DatosHistAntiguoDTO;
import com.ibm.bbva.tabla.ejb.impl.TablaFacadeBean;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;
import com.ibm.bbva.tabla.util.vo.ConvertHistorial;
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
	
	/*FIX ERIKA ABREGU 27/06/2015 */
	private TablaFacadeBean tablaFacadeBean = null;
	
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
			LOG.info("ListTabla tamaño::::"+this.getListTabla().size());
		}else{
			this.setNumRegistros("0 registros encontrados");
			LOG.info("ListTabla es nulo");
		}  		
	}
	
	public String  seleccionaFila() {
		eliminarObjetosSession();
		
		String idHistorial = getRequestParameter("idHistorial");
		//FIX ERIKA ABREGU
		String origen = getRequestParameter("origen");
		String idExpediente = getRequestParameter("idExpediente");
		
		Expediente expediente = obtenerExpediente(idHistorial, origen, idExpediente);
		
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
	        // regresa a la primera página si cambia el orden
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
		} else if ("lineaCreditoSolicitado".equals(columna)) {
			comparator = ComparatorFactory.lineaCreditoSolicitadoHistorial(orden);
		} else if ("lineaCreditoAprobado".equals(columna)) {
			comparator = ComparatorFactory.lineaCreditoAprobadoHistorial(orden);
		} else if ("oficina".equals(columna)) {
			comparator = ComparatorFactory.oficinaHistorial(orden);
		} else if ("territorio".equals(columna)) {
			comparator = ComparatorFactory.territorioHistorial(orden);
		} else if ("codigoRGVL".equals(columna)) {
			comparator = ComparatorFactory.codigoRGVLHistorial(orden);
		} else if ("numeroContrato".equals(columna)) {
			comparator = ComparatorFactory.numeroContratoHistorial(orden);
		} else if ("observacion".equals(columna)) {
			comparator = ComparatorFactory.observacionHistorial(orden);
		} else if ("fechaRegistro".equals(columna)) {
			comparator = ComparatorFactory.fechaRegistroHistorial(orden);
		}
		if (comparator != null) {
			Collections.sort(listTabla, comparator);
		}
	}
	
	private Expediente obtenerExpediente(String historial, String origen, String idExpediente) {
		/**FIX ERIKA ABREGU 05/07/2015
		 * ADICIONAR METODOS DE OBTENER DETALLE DE ANTIGUO CS
		 */
		Expediente expedienteHistorico= null;
		if(origen.equals(Constantes.EXPEDIENTE_ANTIGUO)){
			LOG.info("Metodo obtenerExpediente Antiguo, historial = "+historial);
			long idHistorial = Long.parseLong(historial);
			//Historial historialBD = historialBean.buscarPorId(idHistorial);
			//Expediente expedienteBD = expedienteBean.buscarPorId(historialBD.getExpediente().getId());
			if (this.tablaFacadeBean == null) {
				this.tablaFacadeBean = new TablaFacadeBean();
			}
			DatosHistAntiguoDTO historialBD = tablaFacadeBean.getBuscarHistAntiguoPorId(idExpediente);
			expedienteHistorico =  ConvertHistorial.convertToExpedienteVO(historialBD);
			
		}else{
			LOG.info("Metodo obtenerExpediente, historial = "+historial);
			long idHistorial = Long.parseLong(historial);
			//return ConvertExpediente.convertToExpedienteVO(historialBean.buscarPorId(idHistorial));
			Historial historialBD = historialBean.buscarPorId(idHistorial);
			Expediente expedienteBD = expedienteBean.buscarPorId(historialBD.getExpediente().getId());
			expedienteHistorico =  ConvertExpediente.convertToExpedienteVO(historialBD, expedienteBD);
			
			/**FIX ERIKA ABREGU 05/07/2015
			 * ADICIONAL LA VARIABLE ORIGEN A EXPEDIENTEHISTORICO
			 */
			if(expedienteHistorico!=null){
				expedienteHistorico.setOrigen(origen!=null?origen:null);
			}
		}
		
		
		
		 
		
		/**
		 * Obtiene URL Documento CM - 21 Julio 2014 (Maydeline chanchari)
		 * */
//		LOG.info("Consulta CM");
//		objDocumentoExpTc= new DocumentoExpTc();
//		objDocumentoExpTc.setExpediente(new Expediente());
//		//objDocumentoExpTc.getExpediente().setClienteNatural(new ClienteNatural());
//		objDocumentoExpTc.getExpediente().setClienteNatural(expedienteHistorico.getClienteNatural());
//	
//		long tiempoInicio = System.currentTimeMillis();
//		try{
//			listaDocumentosCM = facade.obtenerListaDocumentoCM(objDocumentoExpTc);
//		}catch(Exception e){
//			LOG.error(e.getMessage(), e);
//		}
//		long tiempoFin = System.currentTimeMillis();
//		LOG.info("TablaBandejaHistMB.obtenerExpediente obtenerListaDocumentoCM demora: " + (tiempoFin - tiempoInicio) + " milisegundos");
//		
//		Map<String, Object> mapListDocumentosCM =new TreeMap<String, Object> ();
//		if(listaDocumentosCM!=null)
//			for(Documento objDocumento: listaDocumentosCM){
//				mapListDocumentosCM.put(String.valueOf(objDocumento.getId()), objDocumento);
//			}
//		else
//			LOG.info("listaDocumentosCM es nulo");
		addObjectSession(Constantes.EXPEDIENTE_SESION_HISTORICO, expedienteHistorico );
//		addObjectSession(Constantes.EXPEDIENTE_LISTA_DOCUMENTO_CM, mapListDocumentosCM);
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
		// TODO Apéndice de método generado automáticamente
		
	}

	
}