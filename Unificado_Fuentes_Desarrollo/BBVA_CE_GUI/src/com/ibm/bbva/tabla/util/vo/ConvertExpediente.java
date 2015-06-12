package com.ibm.bbva.tabla.util.vo;

import java.sql.Timestamp;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.ExpedienteTC;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.HistorialLog;
import com.ibm.bbva.entities.Horario;
import com.ibm.bbva.session.HorarioBeanLocal;
import com.ibm.bbva.util.AyudaToe;
import com.ibm.bbva.util.Util;

public class ConvertExpediente {
	
	private static final Logger LOG = LoggerFactory.getLogger(ConvertExpediente.class);

//	public static Expediente convertToExpedienteVO(Historial historial) {
	public static Expediente convertToExpedienteVO(Historial historial, Expediente expedienteBD) {
		Expediente vo = new Expediente();
		vo.setExpedienteTC(new ExpedienteTC());
//		vo.setId(historial.getId());
		vo.setId(expedienteBD.getId());
		vo.setClienteNatural(historial.getClienteNatural());
		vo.setEstado(historial.getEstado());
		vo.setProducto(historial.getProducto());
		vo.setEmpleado(historial.getEmpleado());
		vo.getExpedienteTC().setTipoOferta(historial.getTipoOferta());
		vo.getExpedienteTC().setCodPreEval(historial.getCodPreEval());
		vo.getExpedienteTC().setSubproducto(historial.getSubproducto());
		vo.getExpedienteTC().setLineaCredSol(historial.getLineaCredSol());
		vo.getExpedienteTC().setTipoMonedaSol(historial.getTipoMonedaSol());
		vo.getExpedienteTC().setTipoEnvio(historial.getTipoEnvio());
		vo.getExpedienteTC().setSolTasaEsp(historial.getSolTasaEsp());
		vo.getExpedienteTC().setVerifLab(historial.getVerifLab());
		vo.getExpedienteTC().setVerifDom(historial.getVerifDom());
		vo.getExpedienteTC().setZonaPel(historial.getZonaPel());
		vo.getExpedienteTC().setFlagComentario(historial.getFlagComentario());
		vo.setComentario(historial.getComentario());
		vo.getExpedienteTC().setLineaConsumo(historial.getLineaConsumo());
		vo.getExpedienteTC().setPorcentajeEndeudamiento(historial.getPorcentajeEndeudamiento());
		vo.getExpedienteTC().setRiesgoCliente(historial.getRiesgoCliente());
		vo.getExpedienteTC().setClasificacionSbs(historial.getClasificacionSbs());
		vo.getExpedienteTC().setSbsConyuge(historial.getSbsConyuge());
		vo.getExpedienteTC().setClasificacionBanco(historial.getClasificacionBanco());
		vo.getExpedienteTC().setBancoConyuge(historial.getBancoConyuge());
		vo.getExpedienteTC().setRvgl(historial.getRvgl());
		vo.getExpedienteTC().setNroCta(historial.getNroCta());
		vo.getExpedienteTC().setTipoBuro(historial.getTipoBuro());
		vo.getExpedienteTC().setTipoScoring(historial.getTipoScoring());
		vo.getExpedienteTC().setEstadoTipoResol(historial.getEstadoTipoResol());
		vo.getExpedienteTC().setFlagDelegacion(historial.getFlagDelegacion());
		vo.getExpedienteTC().setComentarioRechazo(historial.getComentarioRechazo());
		vo.getExpedienteTC().setComentarioDelegacion(historial.getComentarioDelegacion());
		vo.getExpedienteTC().setLineaCredAprob(historial.getLineaCredAprob());
		vo.getExpedienteTC().setTipoMonedaAprob(historial.getTipoMonedaAprob());
		vo.getExpedienteTC().setFlagSolTasaEsp(historial.getFlagSolTasaEsp());
		vo.getExpedienteTC().setTasaEsp(historial.getTasaEsp());
		vo.getExpedienteTC().setFlagModifScore(historial.getFlagModifScore());
		vo.getExpedienteTC().setFlagExcDelegacion(historial.getFlagExcDelegacion());
		vo.getExpedienteTC().setEstadoEnvWorkflowTc(historial.getEstadoEnvWorkflowTc());
		vo.getExpedienteTC().setComentarioAyuMem(historial.getComentarioAyuMem());
		vo.setNumTerminal(historial.getNumTerminal());
		vo.setFechaEnvio(historial.getFechaEnvio());
		vo.setFechaProgramada(historial.getFechaProgramada());
		vo.setFechaFin(historial.getFechaFin());
		vo.setAccion(historial.getAccion());
		vo.getExpedienteTC().setTarea(historial.getTarea());
		vo.getExpedienteTC().setEmpleado(expedienteBD.getExpedienteTC().getEmpleado());//debe ser el empleado de creación
		vo.getExpedienteTC().setNroContrato(historial.getNroContrato());
		vo.getExpedienteTC().setOficina(expedienteBD.getExpedienteTC().getOficina());//debe ser la oficina de creación
		vo.getExpedienteTC().setFechaT1(historial.getFechaT1());
		vo.getExpedienteTC().setFechaT2(historial.getFechaT2());
		vo.getExpedienteTC().setFechaT3(historial.getFechaT3());
		vo.getExpedienteTC().setNumTarjeta(expedienteBD.getExpedienteTC().getNumTarjeta());
		vo.getExpedienteTC().setClienteNaturalConyuge(historial.getClienteNaturalConyuge());
		vo.getExpedienteTC().setFlagDevolucion(historial.getFlagDevolucion());
		vo.getExpedienteTC().setFlagRetraer(historial.getFlagRetraer());
		vo.getExpedienteTC().setTipoResolucion(historial.getTipoResolucion());
        
		vo.getExpedienteTC().setPlazoSolicitado(expedienteBD.getExpedienteTC().getPlazoSolicitado());
		vo.getExpedienteTC().setPlazoSolicitadoApr(expedienteBD.getExpedienteTC().getPlazoSolicitadoApr());
		
		return vo;
	}
	
	public static Historial convertToHistorialVO(Expediente vo) {
		Historial historial = new Historial();
		
		historial.setExpediente(vo);
		historial.setClienteNatural(vo.getClienteNatural());
		historial.setEstado(vo.getEstado());
		historial.setProducto(vo.getProducto());
		historial.setEmpleado(vo.getEmpleado());
		historial.setTipoOferta(vo.getExpedienteTC().getTipoOferta());
		historial.setCodPreEval(vo.getExpedienteTC().getCodPreEval());
		historial.setSubproducto(vo.getExpedienteTC().getSubproducto());
		historial.setLineaCredSol(vo.getExpedienteTC().getLineaCredSol());
		historial.setTipoMonedaSol(vo.getExpedienteTC().getTipoMonedaSol());
		historial.setTipoEnvio(vo.getExpedienteTC().getTipoEnvio());
		historial.setSolTasaEsp(vo.getExpedienteTC().getSolTasaEsp());
		historial.setVerifLab(vo.getExpedienteTC().getVerifLab());
		historial.setVerifDom(vo.getExpedienteTC().getVerifDom());
		historial.setZonaPel(vo.getExpedienteTC().getZonaPel());
		historial.setFlagComentario(vo.getExpedienteTC().getFlagComentario());
		historial.setComentario(vo.getComentario());
		historial.setLineaConsumo(vo.getExpedienteTC().getLineaConsumo());
		historial.setPorcentajeEndeudamiento(vo.getExpedienteTC().getPorcentajeEndeudamiento());
		historial.setRiesgoCliente(vo.getExpedienteTC().getRiesgoCliente());
		historial.setClasificacionSbs(vo.getExpedienteTC().getClasificacionSbs());
		historial.setSbsConyuge(vo.getExpedienteTC().getSbsConyuge());
		historial.setClasificacionBanco(vo.getExpedienteTC().getClasificacionBanco());
		historial.setBancoConyuge(vo.getExpedienteTC().getBancoConyuge());
		historial.setRvgl(vo.getExpedienteTC().getRvgl());
		historial.setNroCta(vo.getExpedienteTC().getNroCta());
		historial.setTipoBuro(vo.getExpedienteTC().getTipoBuro());
		historial.setTipoScoring(vo.getExpedienteTC().getTipoScoring());
		historial.setEstadoTipoResol(vo.getExpedienteTC().getEstadoTipoResol());		
		historial.setFlagDelegacion(vo.getExpedienteTC().getFlagDelegacion());
		historial.setComentarioRechazo(vo.getExpedienteTC().getComentarioRechazo());
		historial.setComentarioDelegacion(vo.getExpedienteTC().getComentarioDelegacion());
		historial.setLineaCredAprob(vo.getExpedienteTC().getLineaCredAprob());
		historial.setTipoMonedaAprob(vo.getExpedienteTC().getTipoMonedaAprob());
		historial.setFlagSolTasaEsp(vo.getExpedienteTC().getFlagSolTasaEsp());
		historial.setTasaEsp(vo.getExpedienteTC().getTasaEsp());
		historial.setFlagModifScore(vo.getExpedienteTC().getFlagModifScore());
		historial.setFlagExcDelegacion(vo.getExpedienteTC().getFlagExcDelegacion());
		historial.setEstadoEnvWorkflowTc(vo.getExpedienteTC().getEstadoEnvWorkflowTc());
		historial.setComentarioAyuMem(vo.getExpedienteTC().getComentarioAyuMem());
		historial.setNumTerminal(vo.getNumTerminal());
		historial.setFechaEnvio(vo.getFechaEnvio());
		historial.setFechaProgramada( vo.getFechaProgramada() );
		historial.setFechaFin( vo.getFechaFin() );
		historial.setAccion( vo.getAccion() );
		historial.setTarea(vo.getExpedienteTC().getTarea());
		historial.setEmpleadoResp(vo.getExpedienteTC().getEmpleado());	  
		historial.setNroContrato(vo.getExpedienteTC().getNroContrato());
		
		historial.setFechaT1(vo.getExpedienteTC().getFechaT1());
		historial.setFechaT2(vo.getExpedienteTC().getFechaT2());
		historial.setFechaT3(vo.getExpedienteTC().getFechaT3());
		historial.setClienteNaturalConyuge(vo.getExpedienteTC().getClienteNaturalConyuge());
		historial.setFlagDevolucion(vo.getExpedienteTC().getFlagDevolucion());
		historial.setFlagRetraer(vo.getExpedienteTC().getFlagRetraer());
		historial.setTipoResolucion(vo.getExpedienteTC().getTipoResolucion());
		
		LOG.info("vo.getDevolucionTareas(): " + vo.getDevolucionTareas());
		if (vo.getDevolucionTareas()!=null && vo.getDevolucionTareas().size()>0){
				LOG.info("vo.getDevolucionTareas().get(0).getMotivoDevolucion(): " + vo.getDevolucionTareas().get(0).getMotivoDevolucion().getId());
				historial.setMotivoDevolucion(vo.getDevolucionTareas().get(0).getMotivoDevolucion());
		}
		
		historial.setTiempoCola(calculoTiempoCola(vo.getEmpleado().getOficina().getId(), vo.getExpedienteTC().getFechaT1(),vo.getExpedienteTC().getFechaT2()));
		historial.setTiempoEjecucion(calculoTiempoEjecucion(vo.getExpedienteTC().getFechaT2(),vo.getExpedienteTC().getFechaT3()));
		historial.setOficina(vo.getExpedienteTC().getOficina());
		historial.setFechaTipoCambioExp(vo.getExpedienteTC().getFechaTipoCambioExp());
		historial.setTipoCambioExp(vo.getExpedienteTC().getTipoCambioExp());
		
		if(vo.getEmpleado()!=null && vo.getEmpleado().getPerfil()!=null){
			historial.setPerfil(vo.getEmpleado().getPerfil());
			LOG.info("Perfil seteado = "+historial.getPerfil().getDescripcion());
		}
			
		historial.setMotivoDevolucion(vo.getMotivoDevolucion());
		
		return historial;
	}
	
	public static double calculoTiempoCola(long oficina, Timestamp fechaT1, Timestamp fechaT2) { 
        double tiempoCola = 0; 
        HorarioBeanLocal horarioBean; 
        
        if (oficina>0) {                         
            AyudaToe ayudaToe; 
            //AyudaHorario ayudaHorario; 
            try { 
                ayudaToe = new AyudaToe(oficina); 
                //ayudaHorario = new AyudaHorario(oficina); 
        
                if (fechaT1!=null && fechaT2!=null) { 
                        
                    horarioBean = (HorarioBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.HorarioBeanLocal"); 
                    
                    List<Horario> hxo = horarioBean.buscarHorarioNormalXoficina(oficina); 
                    LOG.info("calculoTiempoCola - Existe Horario : "+(hxo==null || hxo.size()==0?"No":"Si")); 
                    if (hxo == null || hxo.size()==0) { 
                            tiempoCola = Util.minutesBetween(new DateTime(fechaT1), new DateTime(fechaT2)); 
                            tiempoCola = Util.round(tiempoCola,2).doubleValue(); 
                    }else{                                         
                        tiempoCola = Util.round(ayudaToe.calculoTiempo(fechaT1, fechaT2),2).doubleValue(); 
                    } 
                    //tiempoCola = ayudaHorario.calculoTiempo(fechaT1, fechaT2); 
                    LOG.info("calculoTiempoCola - tiempoCola : "+tiempoCola); 
                } 
                    
            } catch (NamingException e) {
                LOG.error(e.getMessage(), e);
            } 
        }         
        return tiempoCola; 
	} 

	public static double calculoTiempoEjecucion(Timestamp fechaT2, Timestamp fechaT3) { 
        //double tiempoEjecucion = 0; 
        double minutosEjec = 0; 
        
        if (fechaT2!=null && fechaT3!=null) { 
                minutosEjec = Util.minutesBetween(new DateTime(fechaT2), new DateTime(fechaT3)); 
                minutosEjec = Util.round(minutosEjec,2).doubleValue(); 
                //minutosEjec = Util.minutesBetweenEntero(new DateTime(fechaT2), new DateTime(fechaT3)); 
                //tiempoEjecucion = Util.round(minutosEjec,1).doubleValue(); 
                LOG.info("calculoTiempoEjecucion - minutosEjec : "+minutosEjec); 
        }         
        //return tiempoEjecucion; 
        return minutosEjec; 
	}
	
	public static HistorialLog convertToHistorialLogVO(Expediente vo) {
		HistorialLog historial = new HistorialLog();
		
		historial.setExpediente(vo);
		historial.setClienteNatural(vo.getClienteNatural());
		//historial.setEstado(vo.getEstado());
		historial.setProducto(vo.getProducto());
		//historial.setEmpleado(vo.getEmpleado());
		historial.setTipoOferta(vo.getExpedienteTC().getTipoOferta());
		historial.setCodPreEval(vo.getExpedienteTC().getCodPreEval());
		historial.setSubproducto(vo.getExpedienteTC().getSubproducto());
		historial.setLineaCredSol(vo.getExpedienteTC().getLineaCredSol());
		historial.setTipoMonedaSol(vo.getExpedienteTC().getTipoMonedaSol());
		historial.setTipoEnvio(vo.getExpedienteTC().getTipoEnvio());
		historial.setSolTasaEsp(vo.getExpedienteTC().getSolTasaEsp());
		historial.setVerifLab(vo.getExpedienteTC().getVerifLab());
		historial.setVerifDom(vo.getExpedienteTC().getVerifDom());
		historial.setZonaPel(vo.getExpedienteTC().getZonaPel());
		historial.setFlagComentario(vo.getExpedienteTC().getFlagComentario());
		historial.setComentario(vo.getComentario());
		historial.setLineaConsumo(vo.getExpedienteTC().getLineaConsumo());
		historial.setPorcentajeEndeudamiento(vo.getExpedienteTC().getPorcentajeEndeudamiento());
		historial.setRiesgoCliente(vo.getExpedienteTC().getRiesgoCliente());
		historial.setClasificacionSbs(vo.getExpedienteTC().getClasificacionSbs());
		historial.setSbsConyuge(vo.getExpedienteTC().getSbsConyuge());
		historial.setClasificacionBanco(vo.getExpedienteTC().getClasificacionBanco());
		historial.setBancoConyuge(vo.getExpedienteTC().getBancoConyuge());
		historial.setRvgl(vo.getExpedienteTC().getRvgl());
		historial.setNroCta(vo.getExpedienteTC().getNroCta());
		historial.setTipoBuro(vo.getExpedienteTC().getTipoBuro());
		historial.setTipoScoring(vo.getExpedienteTC().getTipoScoring());
		historial.setEstadoTipoResol(vo.getExpedienteTC().getEstadoTipoResol());		
		historial.setFlagDelegacion(vo.getExpedienteTC().getFlagDelegacion());
		historial.setComentarioRechazo(vo.getExpedienteTC().getComentarioRechazo());
		historial.setComentarioDelegacion(vo.getExpedienteTC().getComentarioDelegacion());
		historial.setLineaCredAprob(vo.getExpedienteTC().getLineaCredAprob());
		historial.setTipoMonedaAprob(vo.getExpedienteTC().getTipoMonedaAprob());
		historial.setFlagSolTasaEsp(vo.getExpedienteTC().getFlagSolTasaEsp());
		historial.setTasaEsp(vo.getExpedienteTC().getTasaEsp());
		historial.setFlagModifScore(vo.getExpedienteTC().getFlagModifScore());
		historial.setFlagExcDelegacion(vo.getExpedienteTC().getFlagExcDelegacion());
		historial.setEstadoEnvWorkflowTc(vo.getExpedienteTC().getEstadoEnvWorkflowTc());
		historial.setComentarioAyuMem(vo.getExpedienteTC().getComentarioAyuMem());
		historial.setNumTerminal(vo.getNumTerminal());
		historial.setFechaEnvio(vo.getFechaEnvio());
		historial.setFechaProgramada( vo.getFechaProgramada() );
		historial.setFechaFin( vo.getFechaFin() );
		historial.setAccion( vo.getAccion() );
		//historial.setTarea(vo.getExpedienteTC().getTarea());
		historial.setEmpleadoResp(vo.getExpedienteTC().getEmpleado());	  
		historial.setNroContrato(vo.getExpedienteTC().getNroContrato());
		
		//historial.setFechaT1(vo.getExpedienteTC().getFechaT1());
		//historial.setFechaT2(vo.getExpedienteTC().getFechaT2());
		//historial.setFechaT3(vo.getExpedienteTC().getFechaT3());
		historial.setClienteNaturalConyuge(vo.getExpedienteTC().getClienteNaturalConyuge());
		historial.setFlagDevolucion(vo.getExpedienteTC().getFlagDevolucion());
		historial.setFlagRetraer(vo.getExpedienteTC().getFlagRetraer());
		historial.setTipoResolucion(vo.getExpedienteTC().getTipoResolucion());
		
		LOG.info("vo.getDevolucionTareas(): " + vo.getDevolucionTareas());
		if (vo.getDevolucionTareas()!=null && vo.getDevolucionTareas().size()>0){
				LOG.info("vo.getDevolucionTareas().get(0).getMotivoDevolucion(): " + vo.getDevolucionTareas().get(0).getMotivoDevolucion().getId());
				historial.setMotivoDevolucion(vo.getDevolucionTareas().get(0).getMotivoDevolucion());
		}
		
		historial.setTiempoCola(calculoTiempoCola(vo.getEmpleado().getOficina().getId(), vo.getExpedienteTC().getFechaT1(),vo.getExpedienteTC().getFechaT2()));
		historial.setTiempoEjecucion(calculoTiempoEjecucion(vo.getExpedienteTC().getFechaT2(),vo.getExpedienteTC().getFechaT3()));
		historial.setOficina(vo.getExpedienteTC().getOficina());
		historial.setFechaTipoCambioExp(vo.getExpedienteTC().getFechaTipoCambioExp());
		historial.setTipoCambioExp(vo.getExpedienteTC().getTipoCambioExp());
		
		/*if(vo.getEmpleado()!=null && vo.getEmpleado().getPerfil()!=null){
			historial.setPerfil(vo.getEmpleado().getPerfil());
			LOG.info("Perfil seteado = "+historial.getPerfil().getDescripcion());
		}*/
			
		historial.setMotivoDevolucion(vo.getMotivoDevolucion());
		
		return historial;
	}
	
}