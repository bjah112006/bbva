package com.ibm.bbva.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ClienteWeb;
import pe.ibm.bean.ExpedienteTCWPSWeb;
import pe.ibm.bean.ProductoWeb;
import bbva.ws.api.view.BBVAFacadeLocal;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Ans;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.TipoCliente;
import com.ibm.bbva.messages.Mensajes;
import com.ibm.bbva.session.AnsBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.EstadoTareaCEBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.session.TipoClienteBeanLocal;

@SuppressWarnings("serial")
public class ExpedienteTCWrapper extends ExpedienteTCWPSWeb implements Serializable {
	
	private ExpedienteTCWPSWeb expedienteTC;
	private AyudaHorario ayudaHorario;
	private BBVAFacadeLocal service;
	private TareaBeanLocal tareaBean;
	private ExpedienteBeanLocal expedienteBean;
	private boolean seleccion;
	private TipoClienteBeanLocal tipoClienteBean;
	private AnsBeanLocal ansBean;
	private boolean opVisualizacion;
	
	private EstadoTareaCEBeanLocal estadoTareaCELocalBean;
	private EmpleadoBeanLocal empleadoBeanLocalBean;
	
	private String estadoTarjeta = null;
	
	private static final Logger LOG = LoggerFactory.getLogger(ExpedienteTCWrapper.class);
	
	public ExpedienteTCWrapper() {
		super();
		LOG.info("ExpedienteTCWrapper()");
		try {
			estadoTareaCELocalBean = (EstadoTareaCEBeanLocal) new InitialContext()
					.lookup("ejblocal:com.ibm.bbva.session.EstadoTareaCEBeanLocal");
			empleadoBeanLocalBean = (EmpleadoBeanLocal) new InitialContext()
					.lookup("ejblocal:com.ibm.bbva.session.EmpleadoBeanLocal");
		} catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public ExpedienteTCWrapper(ExpedienteTCWPSWeb expedienteTC, AyudaHorario ayudaHorario,
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
		
		try {
			estadoTareaCELocalBean = (EstadoTareaCEBeanLocal) new InitialContext()
					.lookup("ejblocal:com.ibm.bbva.session.EstadoTareaCEBeanLocal");
			empleadoBeanLocalBean = (EmpleadoBeanLocal) new InitialContext()
					.lookup("ejblocal:com.ibm.bbva.session.EmpleadoBeanLocal");
		} catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public ExpedienteTCWPSWeb getExpedienteTC () {
		return expedienteTC;
	}
	
	public Timestamp getFechaActivado() {
		if(expedienteTC.getActivado()!=null)
			return new Timestamp(expedienteTC.getActivado().getTimeInMillis());
		else
			return null;
	}
	
	public String getFlRetraer() {
		return expedienteTC.getFlagRetraer()==null ? "0" : expedienteTC.getFlagRetraer();
	}
	
	public String getUrlColor () {
		//Tarea tar = tareaBean.buscarPorId(Long.parseLong(expedienteTC.getIdTarea()));
		Ans tar = null;
		LOG.info("*********URL COLOR**************");
		LOG.info("Exp :" + expedienteTC.getCodigo());

		String colorVerde, colorAmarillo;
		
		if(expedienteTC.getValorAnsAmarillo()==null || expedienteTC.getValorAnsAmarillo().equals("")){
			colorAmarillo="0";
			LOG.info("Amarillo es nulo o vacío");
		}else
			colorAmarillo=expedienteTC.getValorAnsAmarillo();
		
		if(expedienteTC.getValorAnsVerde()==null || expedienteTC.getValorAnsVerde().equals("")){
			colorVerde="0";
			LOG.info("Verde es nulo o vacío");
		}else
			colorVerde=expedienteTC.getValorAnsVerde();		
		
			tar=new Ans();
			tar.setAmarilloMinutos(new BigDecimal(colorAmarillo));
			tar.setVerdeMinutos(new BigDecimal(colorVerde));

			LOG.info("tar-verde :" + tar.getVerdeMinutos());
			LOG.info("tar-amarillo :" + tar.getAmarilloMinutos());
			
			int minutos =0;
			
			if(ayudaHorario!=null && expedienteTC.getActivado()!=null)
				minutos = ayudaHorario.calcularMinutos(expedienteTC.getActivado(), 
					Calendar.getInstance());
			if(expedienteTC.getActivado()!=null)
				LOG.info("Activado "+expedienteTC.getActivado().getTime());
			else
				LOG.info("Activado es nulo");
			LOG.info("Instance "+Calendar.getInstance().getTime());
			LOG.info("minutos "+minutos);
			
			if (minutos < 0) {
				return "/resources/images/rojo.png";
			} else if (Integer.parseInt(tar.getVerdeMinutos().toString())>= minutos) {
				return "/resources/images/verde.png";
			} else if (Integer.parseInt(tar.getAmarilloMinutos().toString()) >= minutos) {
				return "/resources/images/amarillo.png";
			} else {
				return "/resources/images/rojo.png";
			}					

	}
	
	public boolean isRenderEstadoTarea () {
		return Constantes.ID_TAREA_MOSTRAR_ESTADO_TARJETA.equals(expedienteTC.getIdTarea());
	}
	
//	public String getEstadoTarjeta () {
//		if (estadoTarjeta == null) {
//			String estado = "";
//			String result = null;
//			try {
//				Expediente expedienteVO = expedienteBean.buscarPorId(Long.parseLong(expedienteTC.getCodigo()));
//				//logger.info("expedienteVO.getNroContrato() :" + expedienteVO.getExpedienteTC().getNroContrato());
//				//logger.info("expedienteVO.getNumTarjeta() :" + expedienteVO.getExpedienteTC().getNumTarjeta());
//				LOG.info("expedienteTC.getCodigo() :" + expedienteTC.getCodigo());
//				if(expedienteVO!=null){
//					LOG.info("expedienteVO.getNroContrato() :" + expedienteVO.getExpedienteTC().getNroContrato());
//					LOG.info("expedienteVO.getNumTarjeta() :" + expedienteVO.getExpedienteTC().getNumTarjeta());
//					
//					if(expedienteVO!=null && expedienteVO.getExpedienteTC()!=null && 
//							expedienteVO.getExpedienteTC().getNroContrato()!=null && !expedienteVO.getExpedienteTC().getNroContrato().trim().equals("") && 
//							expedienteVO.getExpedienteTC().getNumTarjeta()!=null && !expedienteVO.getExpedienteTC().getNumTarjeta().trim().equals("")){
//						result = service.WFTarjetas_devolverEstadoEntregaTC(expedienteVO.getExpedienteTC().getNroContrato(), expedienteVO.getExpedienteTC().getNumTarjeta());
//						//logger.info("resultado servicio estado tarjeta : "+ result);
//						//logger.info("tamaño array estado tarjeta : "+ result.split("\\|").length);
//						if (result.split("\\|").length > 3) {
//							estado = result.split("\\|")[3];
//						}				
//					}				
//				}
//				
//				//logger.info("estado tarjeta : "+ estado);
//			} catch (Exception e) {
//				estado = "";
//				LOG.error(e.getMessage(), e);
//			}
//			this.estadoTarjeta = estado;
//		}
//		return estadoTarjeta;
//	}
	
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

	public ClienteWeb getCliente() {
		return expedienteTC.getCliente();
	}

	public ProductoWeb getProducto() {
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
		LOG.info("expedienteTC.getNombreUsuarioActual() ::: "+expedienteTC.getNombreUsuarioActual());
		return expedienteTC.getNombreUsuarioActual();
	}	

	public String getNombreCompletoUsuarioActual() {
//		LOG.info("getCodigoUsuarioActual ::: "+expedienteTC.getCodigo());
//		Expediente objExpediente = expedienteBean.buscarPorId(Long.parseLong(expedienteTC.getCodigo()));
//		if(objExpediente!=null && objExpediente.getEmpleado()!=null){
//			LOG.info("NombresCompletos ::: "+objExpediente.getEmpleado().getNombresCompletos());
//			return objExpediente.getEmpleado().getNombresCompletos();
//		}else{
//			return this.getNombreUsuarioActual();
//		}
		Empleado objEmpleado = empleadoBeanLocalBean.buscarPorCodigo(expedienteTC.getCodigoUsuarioActual());
		if (objEmpleado != null) {
			return objEmpleado.getNombresCompletos();
		} else {
			return expedienteTC.getNombreUsuarioActual();
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

	public void setExpedienteTC(ExpedienteTCWPSWeb expedienteTC) {
		this.expedienteTC = expedienteTC;
	}		
	
	
}
