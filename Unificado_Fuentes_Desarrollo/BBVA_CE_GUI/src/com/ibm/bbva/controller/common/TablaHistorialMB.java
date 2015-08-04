package com.ibm.bbva.controller.common;

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
import com.ibm.bbva.tabla.dto.DatosAyudaMemoriaIiceDTO;
import com.ibm.bbva.tabla.dto.DatosDetalleHistoricoIiceDTO;
import com.ibm.bbva.tabla.ejb.impl.TablaFacadeBean;
import com.ibm.bbva.tabla.util.vo.ConvertHistorial;
import com.ibm.bbva.tabla.util.vo.TablaHistorica;


@SuppressWarnings("serial")
@ManagedBean(name="tablaHistorial")
@RequestScoped
public class TablaHistorialMB extends AbstractTablaMBean {
	
	@EJB
	private HistorialBeanLocal historialBean;

	private Expediente expediente;
	private List<Historial> listHistorial = new ArrayList<Historial>();
	private HtmlDataTable tablaBinding;
	private List<TablaHistorica> listTabla;
	
	/*FIX ERIKA ABREGU 27/06/2015 */
	private TablaFacadeBean tablaFacadeBean = null;
	private List<DatosDetalleHistoricoIiceDTO> listDetalleHistoricoIICE;

	private static final Logger LOG = LoggerFactory.getLogger(TablaHistorialMB.class);
	
	public TablaHistorialMB() {
	}
	
	@PostConstruct
	public void obtenerDatos() {
		LOG.info("obtenerDatos");
		/*Obtiene Datos de Expediente*/
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		
		// lista del historial
		if (expediente != null && expediente.getId() > 0) {
			/**FIX ERIKA ABREGU 05/07/2015
			 * ADICIONAR METODOS DE OBTENER DETALLE DE ANTIGUO CS
			 */
			if(Constantes.EXPEDIENTE_ANTIGUO.equals(expediente.getOrigen())){
				LOG.info("Metodo obtenerDatos de TablaHistorialMB Antiguo = "+expediente.getId());
					
				//preparando parametros
				ArrayList<Object> listaParametros = new ArrayList<Object>();
				listaParametros.add(new Long(expediente.getId()));
					
					
				if (this.tablaFacadeBean == null) {
					this.tablaFacadeBean = new TablaFacadeBean();
				}
					
				listDetalleHistoricoIICE = tablaFacadeBean.getGenerarDetalleHistorialIICE(listaParametros);
				if (listDetalleHistoricoIICE == null) {
					listDetalleHistoricoIICE = new ArrayList<DatosDetalleHistoricoIiceDTO> ();
				}
				listHistorial =  ConvertHistorial.convertToDetalleHistorial(listDetalleHistoricoIICE);
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
		
		iniciar(Constantes.PAGINA_ACTUAL_HISTORIAL, listHistorial.size(), 
				Constantes.FILAS_TABLA);
	}
	
	protected void mostrarTabla (int indiceInicioFila, int filas) {
		LOG.info("mostrarTabla");
		if(listHistorial.size()>0){
			listTabla = new ArrayList<TablaHistorica>();
			int indiceFilaFinal = indiceInicioFila + filas;
			
			for(int i=indiceInicioFila, n=listHistorial.size(); i<n && i<indiceFilaFinal; i++){
				Historial hist = listHistorial.get(i);
				TablaHistorica tablaHist = new TablaHistorica();
				tablaHist.setNumTerminal(hist.getNumTerminal());
				tablaHist.setCodigoUsuario(hist.getEmpleado().getCodigo());
				//tablaHist.setPerfilUsuario(hist.getEmpleado().getPerfil().getDescripcion());
				tablaHist.setPerfilUsuario(hist.getPerfil()==null?"":hist.getPerfil().getDescripcion());
				tablaHist.setNombresCompletosEmpleado(hist.getEmpleado().getNombresCompletos());
				SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss aa");
				if (hist.getFechaT1()!=null) {
					//tablaHist.setFechaEnvio(sdf.format(hist.getFechaEnvio()));
					tablaHist.setFechaEnvio(sdf.format(hist.getFechaT1()));
				}
				if (hist.getFechaT3() != null) {
					//tablaHist.setFechaFin(sdf.format(hist.getFechaFin()));
					tablaHist.setFechaFin(sdf.format(hist.getFechaT3()));
				}
				if (hist.getFechaT2() != null) {
					//tablaHist.setFechaProgramada(sdf.format(hist.getFechaProgramada()));
					tablaHist.setFechaProgramada(sdf.format(hist.getFechaT2()));
				}
				tablaHist.setEstadoDescripcion(hist.getEstado().getDescripcion());
				listTabla.add(tablaHist);
			}
		}
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
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

	public List<TablaHistorica> getListTabla() {
		return listTabla;
	}

	public void setListTabla(List<TablaHistorica> listTabla) {
		this.listTabla = listTabla;
	}
}
