package com.ibm.bbva.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.ejb.EJB;

import org.apache.openjpa.lib.log.Log;

import pe.ibm.bean.Cliente;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bean.Producto;
import bbva.ws.api.view.BBVAFacadeLocal;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Ans;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.entities.TipoCliente;
import com.ibm.bbva.messages.Mensajes;
import com.ibm.bbva.session.AnsBeanLocal;
import com.ibm.bbva.session.ClienteNaturalBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.RetraccionTareaBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.session.TipoClienteBeanLocal;

@SuppressWarnings("serial")
public class ExpedienteTCWrapper extends ExpedienteTCWPS implements Serializable {
	
	private ExpedienteTCWPS expedienteTC;
	private AyudaHorario ayudaHorario;
	private BBVAFacadeLocal service;
	private TareaBeanLocal tareaBean;
	private ExpedienteBeanLocal expedienteBean;
	private boolean seleccion;
	private TipoClienteBeanLocal tipoClienteBean;
	private AnsBeanLocal ansBean;
	private boolean opVisualizacion;
	
	//private MyLogger logger = MyLogger.getLogger(ExpedienteTCWrapper.class);
	
	public ExpedienteTCWrapper(ExpedienteTCWPS expedienteTC, AyudaHorario ayudaHorario,
			TareaBeanLocal tareaBean, BBVAFacadeLocal service, 
			ExpedienteBeanLocal expedienteBean,
			TipoClienteBeanLocal tipoClienteBean,
			AnsBeanLocal ansBean) {
		this.expedienteTC = expedienteTC;
		this.ayudaHorario = ayudaHorario;
		this.service = service;
		this.tareaBean = tareaBean;
		this.expedienteBean = expedienteBean;		
		this.tipoClienteBean = tipoClienteBean;
		this.ansBean = ansBean;
	}
	
	public ExpedienteTCWPS getExpedienteTC () {
		return expedienteTC;
	}
	
	public Timestamp getFechaActivado() {
		return new Timestamp(expedienteTC.getActivado().getTimeInMillis());
	}
	
	public String getFlRetraer() {
		return expedienteTC.getFlagRetraer()==null ? "0" : expedienteTC.getFlagRetraer();
	}
	
	public String getUrlColor () {
		//Tarea tar = tareaBean.buscarPorId(Long.parseLong(expedienteTC.getIdTarea()));
		
		System.out.println("Exp :" + expedienteTC.getCodigo());
		System.out.println("Oficina :" + expedienteTC.getIdOficina());
		System.out.println("producto :" + expedienteTC.getProducto().getIdProducto());
		System.out.println("tarea :" + expedienteTC.getIdTarea());
		System.out.println("tipoOferta :" + expedienteTC.getIdTipoOferta());
		System.out.println("grupoSegmento :" + expedienteTC.getIdGrupoSegmento());
		
		String producto = "", tarea = "",tipoOferta = "",grupoSegmento = "";
		if(expedienteTC.getProducto()!=null) {
		   producto = Util.validarCampoLong(""+expedienteTC.getProducto().getIdProducto())==null?"":""+expedienteTC.getProducto().getIdProducto();
		}
		if(expedienteTC.getIdTarea()!=null) {
		   tarea = Util.validarCampoLong(""+expedienteTC.getIdTarea())==null?"":""+expedienteTC.getIdTarea();
		}
		if(expedienteTC.getIdTipoOferta()!=null) {
		   tipoOferta = Util.validarCampoLong(""+expedienteTC.getIdTipoOferta())==null?"":""+expedienteTC.getIdTipoOferta();
		}		
		if(expedienteTC.getIdGrupoSegmento()!=null) {
		   grupoSegmento = Util.validarCampoLong(""+expedienteTC.getIdGrupoSegmento())==null?"":""+expedienteTC.getIdGrupoSegmento();
		}
		Ans tar = null;		
		if (!producto.equals("") && !tarea.equals("") && !tipoOferta.equals("") && !(grupoSegmento.equals("") || grupoSegmento.equals("0"))) {
			 tar = ansBean.buscarPorId(Long.parseLong(producto), Long.parseLong(tarea), Long.parseLong(tipoOferta), Long.parseLong(grupoSegmento));
		}
		
		
		if (tar!=null) {
			System.out.println("tar :" + tar.getId());
			System.out.println("tar-verde :" + tar.getVerdeMinutos());
			System.out.println("tar-amarillo :" + tar.getAmarilloMinutos());
		}
		
		if (tar == null) {
			return "/resources/images/rojo.png";
			//return "";
		}
		int minutos =0;
		
		if(ayudaHorario!=null && expedienteTC.getActivado()!=null)
			minutos = ayudaHorario.calcularMinutos(expedienteTC.getActivado(), 
				Calendar.getInstance());

		System.out.println("Activado "+expedienteTC.getActivado().getTime());
		System.out.println("Instance "+Calendar.getInstance().getTime());
		System.out.println("minutos "+minutos);
		
		if (tar.getVerdeMinutos().intValue() >= minutos) {
			return "/resources/images/verde.png";
		} else if (tar.getAmarilloMinutos().intValue() >= minutos) {
			return "/resources/images/amarillo.png";
		} else {
			return "/resources/images/rojo.png";
		}
	}
	
	public boolean isRenderEstadoTarea () {
		return Constantes.ID_TAREA_MOSTRAR_ESTADO_TARJETA.equals(expedienteTC.getIdTarea());
	}
	
	public String getEstadoTarjeta () {
		String estado = ""; 
		String result = null;
		try {
			Expediente expedienteVO = expedienteBean.buscarPorId(Long.parseLong(expedienteTC.getCodigo()));
			//logger.info("expedienteVO.getNroContrato() :" + expedienteVO.getExpedienteTC().getNroContrato());
			//logger.info("expedienteVO.getNumTarjeta() :" + expedienteVO.getExpedienteTC().getNumTarjeta());
			System.out.println("expedienteTC.getCodigo() :" + expedienteTC.getCodigo());
			if(expedienteVO!=null){
				System.out.println("expedienteVO.getNroContrato() :" + expedienteVO.getExpedienteTC().getNroContrato());
				System.out.println("expedienteVO.getNumTarjeta() :" + expedienteVO.getExpedienteTC().getNumTarjeta());
				
				if(expedienteVO!=null && expedienteVO.getExpedienteTC()!=null && 
						expedienteVO.getExpedienteTC().getNroContrato()!=null && !expedienteVO.getExpedienteTC().getNroContrato().trim().equals("") && 
						expedienteVO.getExpedienteTC().getNumTarjeta()!=null && !expedienteVO.getExpedienteTC().getNumTarjeta().trim().equals("")){
					result = service.WFTarjetas_devolverEstadoEntregaTC(expedienteVO.getExpedienteTC().getNroContrato(), expedienteVO.getExpedienteTC().getNumTarjeta());
					//logger.info("resultado servicio estado tarjeta : "+ result);
					//logger.info("tamaño array estado tarjeta : "+ result.split("\\|").length);
					if (result.split("\\|").length > 3) {
						estado = result.split("\\|")[3];
					}				
				}				
			}
			
			//logger.info("estado tarjeta : "+ estado);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return estado;
	}
	
	public Integer getOrden() {		
		int orden = 0;		
		TipoCliente tipoCliente = new TipoCliente();		
		tipoCliente = tipoClienteBean.buscarPorCodigo("81100");	//CAMBIAR POR VARIABLE CUANDO SE CREE EN PROCESS	
        if ( tipoCliente != null ) {
           orden = Integer.valueOf(tipoCliente.getOrden());
        }
		return orden;
	}
	
	public Integer getOrden(String codigo) {		
		int orden = 0;
		TipoCliente tipoCliente = new TipoCliente();
		if (codigo==null || codigo.trim().equals(""))
			codigo = "1";
		tipoCliente = tipoClienteBean.buscarPorCodigo(codigo);	
        if ( tipoCliente != null ) {
           orden = Integer.valueOf(tipoCliente.getOrden());
        }
		return orden;
	}
	
	public Integer getNroDevoluciones() {
		//int devoluciones=0;
		return expedienteTC.getNumeroDevoluciones()==null?0:expedienteTC.getNumeroDevoluciones();
		
		/*LLAMAR A LA LOGICA DE DEVOLUCIONES*/
		
		
		//return devoluciones;
	}
	
	public boolean isRenderDevoluciones () {
		return Constantes.ID_TAREA_MOSTRAR_ESTADO_TARJETA.equals(expedienteTC.getIdTarea());
	}
	
	public boolean isTarea28 () {
		return Constantes.ID_TAREA_ERROR_ENVIO_ARCHIVOS.equals(expedienteTC.getIdTarea());
	}
	
	public String getMensajeTarea28 () {
		return Mensajes.getMensaje("com.ibm.bbva.common.tablaBandejaPend.errorArchivos");
	}

	public String getTaskID() {
		return expedienteTC.getTaskID();
	}

	public String getCodigo() {
		return expedienteTC.getCodigo();
	}

	public String getEstado() {
		return expedienteTC.getEstado();
	}

	public Calendar getActivado() {
		return expedienteTC.getActivado();
	}

	public String getAccion() {
		return expedienteTC.getAccion();
	}

	public String getVerificacionDomiciliaria() {
		return expedienteTC.getVerificacionDomiciliaria();
	}

	public String getModificacionScoring() {
		return expedienteTC.getModificacionScoring();
	}

	public String getScoringAprobado() {
		return expedienteTC.getScoringAprobado();
	}

	public String getDevueltoPor() {
		return expedienteTC.getDevueltoPor();
	}

	public String getSegmento() {
		return expedienteTC.getSegmento();
	}

	public String getTipoOferta() {
		return expedienteTC.getTipoOferta();
	}

	public String getMoneda() {
		return expedienteTC.getMoneda();
	}

	public Cliente getCliente() {
		return expedienteTC.getCliente();
	}

	public Producto getProducto() {
		return expedienteTC.getProducto();
	}

	public String getCodigoRVGL() {
		return expedienteTC.getCodigoRVGL();
	}

	public String getCodigoPreEvaluador() {
		return expedienteTC.getCodigoPreEvaluador();
	}

	public String getNombreNavegacionWeb() {
		return expedienteTC.getNombreNavegacionWeb();
	}
	
	public String getIdOficina() {
		return expedienteTC.getIdOficina();
	}

	public String getIdTerritorio() {
		return expedienteTC.getIdTerritorio();
	}

	public String getCodigoEmpleadoResponsable() {
		return expedienteTC.getCodigoEmpleadoResponsable();
	}

	public String getCodigoUsuarioActual() {
		return expedienteTC.getCodigoUsuarioActual();
	}
	
	public String getNombreUsuarioActual() {
		System.out.println("expedienteTC.getNombreUsuarioActual() ::: "+expedienteTC.getNombreUsuarioActual());
		return expedienteTC.getNombreUsuarioActual();
	}	

	public String getNombreCompletoUsuarioActual() {
		System.out.println("getCodigoUsuarioActual ::: "+expedienteTC.getCodigo());
		Expediente objExpediente = expedienteBean.buscarPorId(Long.parseLong(expedienteTC.getCodigo()));
		if(objExpediente!=null && objExpediente.getEmpleado()!=null){
			System.out.println("NombresCompletos ::: "+objExpediente.getEmpleado().getNombresCompletos());
			return objExpediente.getEmpleado().getNombresCompletos();			
		}else{
			return this.getNombreUsuarioActual();
		}

				
	}
	
	public String getIdTarea() {
		return expedienteTC.getIdTarea();
	}

	public String getIdPerfilUsuarioActual() {
		return expedienteTC.getIdPerfilUsuarioActual();
	}

	public String getNombreUsuarioAnterior() {
		return expedienteTC.getNombreUsuarioAnterior();
	}

	public String getPerfilUsuarioAnterior() {
		return expedienteTC.getPerfilUsuarioAnterior();
	}

	public String getCodigoUsuarioAnterior() {
		return expedienteTC.getCodigoUsuarioAnterior();
	}

	public String getNumeroContrato() {
		return expedienteTC.getNumeroContrato();
	}

	public boolean isSeleccion() {
		return seleccion;
	}

	public void setSeleccion(boolean seleccion) {
		this.seleccion = seleccion;
	}
	
	public String getPerfilUsuarioActual() {
		return expedienteTC.getPerfilUsuarioActual();
	}

	public Double getLineaCredito() {
		return expedienteTC.getLineaCredito();
	}
	
	public Double getMontoAprobado() {
		return expedienteTC.getMontoAprobado();
	}	

	public String getOficina() {
		return expedienteTC.getDesOficina();
	}
	
	public String getTerritorio() {
		return expedienteTC.getDesTerritorio();
	}	
	
	public String getObservacion() {
		return expedienteTC.getObservacion();
	}	
	
	public String getCodigoTarea() {
		return expedienteTC.getIdTarea();
	}

	public boolean isOpVisualizacion() {
		return opVisualizacion;
	}

	public void setOpVisualizacion(boolean opVisualizacion) {
		this.opVisualizacion = opVisualizacion;
	}

	public AyudaHorario getAyudaHorario() {
		return ayudaHorario;
	}

	public void setAyudaHorario(AyudaHorario ayudaHorario) {
		this.ayudaHorario = ayudaHorario;
	}

	public void setExpedienteTC(ExpedienteTCWPS expedienteTC) {
		this.expedienteTC = expedienteTC;
	}		
	
	
}
