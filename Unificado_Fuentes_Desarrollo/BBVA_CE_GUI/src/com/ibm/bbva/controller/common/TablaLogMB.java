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
import com.ibm.bbva.entities.Log;
import com.ibm.bbva.session.LogBeanLocal;
import com.ibm.bbva.tabla.util.vo.TablaLog;


@SuppressWarnings("serial")
@ManagedBean(name="tablaLog")
@RequestScoped
public class TablaLogMB extends AbstractTablaMBean {

	@EJB
	private LogBeanLocal logBean;
	
	private Expediente expediente;
	private Log log;
	private List<Log> listLog;
	private HtmlDataTable tablaBinding;
	private List<TablaLog> listTabla;

	private static final Logger LOG = LoggerFactory.getLogger(TablaLogMB.class);
	
	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public List<Log> getListLog() {
		return listLog;
	}

	public void setListLog(List<Log> listLog) {
		this.listLog = listLog;
	}

	public List<TablaLog> getListTabla() {
		return listTabla;
	}

	public void setListTabla(List<TablaLog> listTabla) {
		this.listTabla = listTabla;
	}

	public TablaLogMB() {
	}
	
	@PostConstruct
	public void obtenerDatos() {
		/*Obtiene Datos de Expediente*/
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		
		if (expediente != null && expediente.getId() > 0) {
			listLog = logBean.buscarPorIdExpediente(expediente.getId());
			if (listLog == null) {
				listLog = new ArrayList<Log> ();
			}
		} else {
			listLog = new ArrayList<Log> ();
		}
		
		iniciar(Constantes.PAGINA_ACTUAL_LOG, listLog.size(), 
				Constantes.FILAS_TABLA);
	}

	protected void mostrarTabla(int indiceInicioFila, int filas) {
		if (listLog.size()>0) {
			listTabla = new ArrayList<TablaLog>();
			int indiceFilaFinal = indiceInicioFila + filas;
			for(int i=indiceInicioFila, n=listLog.size(); i<n && i<indiceFilaFinal; i++){
				Log logFil = listLog.get(i);
				TablaLog tablaLog = new TablaLog();
				tablaLog.setCodigoUsuario(logFil.getEmpleado().getCodigo());
				tablaLog.setPerfilUsuario(logFil.getEmpleado().getPerfil().getDescripcion());
				Timestamp fechaHora =  logFil.getFecRegistro();
				if(fechaHora!= null){
					SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy"); 
					SimpleDateFormat sdf2 = new SimpleDateFormat ("hh:mm:ss a");
					tablaLog.setFecha(sdf.format(fechaHora));
					tablaLog.setHora(sdf2.format(fechaHora));
				}
				tablaLog.setHistorial(logFil.getDescripcion());
				tablaLog.setNumTerminal(logFil.getNumTerminal());
				if (logFil.getEstado() != null) {
				    tablaLog.setEstado(logFil.getEstado().getDescripcion());
				}
				listTabla.add(tablaLog);
			}
		}
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public void setTablaBinding(HtmlDataTable tablaBinding) {
		this.tablaBinding = tablaBinding;
	}

	public HtmlDataTable getTablaBinding() {
		return tablaBinding;
	}

}