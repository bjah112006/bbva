package com.ibm.bbva.controller.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlDataTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractTablaMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.tabla.dto.DatosDetalleHistoricoIiceDTO;
import com.ibm.bbva.tabla.dto.DatosDetalleObservacionesIiceDTO;
import com.ibm.bbva.tabla.ejb.impl.TablaFacadeBean;
import com.ibm.bbva.tabla.util.vo.ConvertHistorial;
import com.ibm.bbva.tabla.util.vo.TablaObservacion;


@SuppressWarnings("serial")
@ManagedBean(name="tablaObservaciones")
@RequestScoped
public class TablaObservacionesMB extends AbstractTablaMBean {

	@EJB
	private HistorialBeanLocal historialBean;
	
	private Expediente expediente;
	private Historial historial;
	private List<Historial> listHistorial;
	private HtmlDataTable tablaBinding;
	private List<TablaObservacion> listTabla;
	
	/*FIX ERIKA ABREGU 27/06/2015 */
	private TablaFacadeBean tablaFacadeBean = null;
	private List<DatosDetalleObservacionesIiceDTO> listDetalleObservacionesIICE;

	private static final Logger LOG = LoggerFactory.getLogger(TablaObservacionesMB.class);
	
	public TablaObservacionesMB() {
	}
	
	@PostConstruct
	public void obtenerDatos() {
		/*Obtiene Datos de Expediente*/
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		
		if (expediente != null && expediente.getId() > 0) {
			/**FIX ERIKA ABREGU 05/07/2015
			 * ADICIONAR METODOS DE OBTENER DETALLE DE ANTIGUO CS
			 */
			if(Constantes.EXPEDIENTE_ANTIGUO.equals(expediente.getOrigen())){
				LOG.info("Metodo obtenerDatos de TablaObservacionesMB Antiguo = "+expediente.getId());
					
				//preparando parametros
				ArrayList<Object> listaParametros = new ArrayList<Object>();
				listaParametros.add(new Long(expediente.getId()));
					
					
				if (this.tablaFacadeBean == null) {
					this.tablaFacadeBean = new TablaFacadeBean();
				}
					
				listDetalleObservacionesIICE = tablaFacadeBean.getGenerarDetalleObservIICE(listaParametros);
				if (listDetalleObservacionesIICE == null) {
					listDetalleObservacionesIICE = new ArrayList<DatosDetalleObservacionesIiceDTO> ();
				}
				listHistorial =  ConvertHistorial.convertToObservaciones(listDetalleObservacionesIICE);
			}else{
				listHistorial = historialBean.buscarPorIdExpediente(expediente.getId());
			}
			/**FIX ERIKA ABREGU 05/07/2015
			 * FIN DE ADICIONAR METODOS DE OBTENER DETALLE DE ANTIGUO CS
			*/
			
			if (listHistorial == null) {
				listHistorial = new ArrayList<Historial> ();
			}
		} else {
			listHistorial = new ArrayList<Historial> ();
		}
		
		iniciar(Constantes.PAGINA_ACTUAL_OBSERVACIONES, listHistorial.size(), 
				Constantes.FILAS_TABLA);
		
	}

	protected void mostrarTabla(int indiceInicioFila, int filas) {
		LOG.info("mostrar tabla TABLA OBSERVACIONES");
		if(listHistorial.size()>0){
			LOG.info("listHistorial tamaño:::"+listHistorial.size());
			listTabla = new ArrayList<TablaObservacion>();
			int indiceFilaFinal = indiceInicioFila + filas;
			for(int i=indiceInicioFila, n=listHistorial.size(); i<n && i<indiceFilaFinal; i++){
				if ((listHistorial.get(i).getComentario()!=null && !listHistorial.get(i).getComentario().trim().equals("")) ||
						(listHistorial.get(i).getComentarioRechazo()!=null && !listHistorial.get(i).getComentarioRechazo().trim().equals("")) || 
						listHistorial.get(i).getMotivoDevolucion()!=null) {
					Historial hist = listHistorial.get(i);
					TablaObservacion tablaObs = new TablaObservacion();
					tablaObs.setCodigoUsuario(hist.getEmpleado().getCodigo());
					tablaObs.setPerfilUsuario(hist.getEmpleado().getPerfil().getDescripcion());
					Timestamp fechaHora =  hist.getFecRegistro();
					if(fechaHora!= null){
						SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy"); 
						SimpleDateFormat sdf2 = new SimpleDateFormat ("hh:mm:ss a");
						tablaObs.setFecha(sdf.format(fechaHora));
						tablaObs.setHora(sdf2.format(fechaHora));
					}
					tablaObs.setComentario(hist.getComentario());
					if (hist.getMotivoDevolucion()!=null) 
						tablaObs.setComentario_motivo(hist.getMotivoDevolucion().getDescripcion());
					tablaObs.setComentario_rechazo(hist.getComentarioRechazo());
					LOG.info("tablaObs.getComentario(): " + tablaObs.getComentario());
					LOG.info("tablaObs.getComentario_motivo(): " + tablaObs.getComentario_motivo());
					LOG.info("tablaObs.getComentario_rechazo(): " + tablaObs.getComentario_rechazo());
					listTabla.add(tablaObs);
				}				
			}
		}
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public void setHistorial(Historial historial) {
		this.historial = historial;
	}

	public Historial getHistorial() {
		return historial;
	}

	public void setListHistorial(List<Historial> listHistorial) {
		this.listHistorial = listHistorial;
	}

	public List<Historial> getListHistorial() {
		return listHistorial;
	}

	public void setTablaBinding(HtmlDataTable tablaBinding) {
		this.tablaBinding = tablaBinding;
	}

	public HtmlDataTable getTablaBinding() {
		return tablaBinding;
	}

	public List<TablaObservacion> getListTabla() {
		return listTabla;
	}

	public void setListTabla(List<TablaObservacion> listTabla) {
		this.listTabla = listTabla;
	}
}
