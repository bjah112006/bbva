package com.ibm.bbva.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bean.ExpedienteTCWPSWeb;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Ans;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.session.AnsBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;

public class AyudaHistorial {
	
	private static final Logger LOG = LoggerFactory.getLogger(AyudaHistorial.class);
	
	private HistorialBeanLocal historialBean;
	private AnsBeanLocal ansBean;
	
	public AyudaHistorial() {
		try {			
			historialBean = (HistorialBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.HistorialBeanLocal");
			ansBean = (AnsBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.AnsBeanLocal");
		} catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void asignarFecha (Historial historialVO, ExpedienteTCWPSWeb expedienteTC) {		
		historialVO.setFechaFin(Timestamp.valueOf(Util.parseDateString(new Date(),"yyyy-MM-dd hh:mm:ss.SSS").toString()));
		long fecEnv = expedienteTC.getActivado().getTimeInMillis();
		historialVO.setFechaEnvio(new Timestamp(fecEnv));
		
		DateTime dateTime = new DateTime (fecEnv);
		
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
		
		Integer minVer = null;
		Ans ans = null;
		if (!producto.equals("") && !tarea.equals("") && !tipoOferta.equals("") && !(grupoSegmento.equals("") || grupoSegmento.equals("0"))) {
			/**
			 * COMENTADO PARA VERIFICAR
			 * */
			//ans = ansBean.buscarPorId(Long.parseLong(producto), Long.parseLong(tarea), Long.parseLong(tipoOferta), Long.parseLong(grupoSegmento));
			if (ans!=null)
			   minVer = ans.getVerdeMinutos().intValue();
		}
		
		dateTime = dateTime.plusMinutes(minVer==null ? 0 : minVer);
		
		historialVO.setFechaProgramada(Timestamp.valueOf(Util.parseDateString(dateTime.toDate(),"yyyy-MM-dd hh:mm:ss.SSS").toString()));	
		//LOG.info("--------------------");
		//LOG.info("min "+minVer);
		//LOG.info(historialVO.getFechaFin());
		//LOG.info(historialVO.getFechaEnvio());
		//LOG.info(historialVO.getFechaProgramada());
		//LOG.info("--------------------");
	}
	
	public Historial actualizaRetraer(long idExpediente, String flagRetraer) {
		/*Si el expediente tiene el flag = R->Retraer*/
		Historial h = null;
		if (flagRetraer	.equals(Constantes.FLAGRETRAERR)) {
			List<Historial> listh = historialBean.buscarPorIdExpRetraer(idExpediente, Constantes.FLAGRETRAERN);
			if (listh.size()>0) {
			    h = listh.get(0);
			    h.setFlagRetraer(Constantes.FLAGRETRAERX);
			    historialBean.edit(h);
			}
		}
		return h;
	}
}