package com.ibm.bbva.util.cvs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.EstadoCivil;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.entities.Producto;
import com.ibm.bbva.entities.Segmento;
import com.ibm.bbva.entities.Subproducto;
import com.ibm.bbva.entities.TipoBuro;
import com.ibm.bbva.entities.TipoCliente;
import com.ibm.bbva.entities.TipoDoi;
import com.ibm.bbva.entities.TipoMoneda;
import com.ibm.bbva.entities.TipoOferta;
import com.ibm.bbva.entities.TipoScoring;
import com.ibm.bbva.entities.VerificacionExp;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.VerificacionExpBeanLocal;

public class ReporteCVSServlet extends HttpServlet {
	
	@EJB
	private ExpedienteBeanLocal expedienteBean;
	@EJB
	private VerificacionExpBeanLocal verificacionExpBean;
	
	
	private static final long serialVersionUID = 1L;
	private static final int COLUMNAS_REPORTE = 49;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		try {
			System.out.println("doGet : ");
			long idOficina = Long.parseLong(request.getParameter("selProducto"));
			Date fechaInicio = Constantes.FORMATO_FECHA.parse(request.getParameter("fecInicio") + " 00:00");
			Date fechaFin = Constantes.FORMATO_FECHA.parse(request.getParameter("fecFin") + " 23:59");
			response.setHeader("Cache-Control","private");
			response.setHeader("Pragma","private");
			response.addDateHeader("Expires", 0);
			response.setContentType("text/csv");
			response.addHeader("Content-disposition", "attachment; filename=reporte.csv");

			List<Object[]> lista = reporte(idOficina, fechaInicio, fechaFin);
						
			FormatoFactory ff = new FormatoFactory(lista.isEmpty() ? 0 : lista.get(0).length);
			CSVWriterIBM writer = new CSVWriterIBM(response.getWriter(), ',');
			writer.writeAll(lista, ff);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			throw new ServletException(e); 
		}
	}
	
	private ArrayList<Object[]> reporte(long idOficina, Date fechaInicio, Date fechaFin) {
		
		List<Expediente> listaExp = expedienteBean.reporteHistPlano(idOficina, fechaInicio, fechaFin);
		
		ArrayList<Object[]> list = new ArrayList<Object[]>();
		Object[] titulos = new Object[COLUMNAS_REPORTE];
		titulos[0] = "NÚMERO EXPEDIENTE";
		titulos[1] = "CODIGO SEGMENTO CLIENTE";
		titulos[2] = "DESCRIPCIÓN SEGMENTO CLIENTE";
		titulos[3] = "CORRELATIVO ESTADO";
		titulos[4] = "NOMBRE ESTADO";
		titulos[5] = "NOMBRE PRODUCTO";
		titulos[6] = "CÓDIGO USUARIO CREACIÓN";
		titulos[7] = "NOMBRE USUARIO CREACIÓN";
		titulos[8] = "FECHA CREACIÓN";
		titulos[9] = "CORRELATIVO OFICINA";
		titulos[10] = "NOMBRE OFICINA";
		titulos[11] = "CORRELATIVO SUBPRODUCTO";
		titulos[12] = "NOMBRE SUBPRODUCTO";
		titulos[13] = "CORRELATIVO CLIENTE";
		titulos[14] = "APELLIDO PATERNO CLIENTE";
		titulos[15] = "APELLIDO MATERNO CLIENTE";
		titulos[16] = "NOMBRES CLIENTE";
		titulos[17] = "TIPO DOCUMENTO IDENTIDAD";
		titulos[18] = "NÚMERO DOCUMENTO IDENTIDAD";
		titulos[19] = "TIPO CLIENTE";
		titulos[20] = "INGRESO NETO MENSUAL";
		titulos[21] = "ESTADO CIVIL";
		titulos[22] = "PERSONA EXPUESTA PÚBLICA";
		titulos[23] = "PAGO HABIENTE";
		titulos[24] = "TIPO OFERTA";
		titulos[25] = "MONEDA IMPORTE SOLICITADO";
		titulos[26] = "IMPORTE SOLICITADO";
		titulos[27] = "MONEDA IMPORTE APROBADO";
		titulos[28] = "IMPORTE APROBADO";
		titulos[29] = "TIPO DE RESOLUCIÓN";
		titulos[30] = "CÓDIGO PREEVALUADOR";
		titulos[31] = "CÓDIGO RVGL";
		titulos[32] = "LÍNEA CONSUMO";
		titulos[33] = "RIESGO CLIENTE GRUPAL";
		titulos[34] = "PORCENTAJE ENDEUDAMIENTO";
		titulos[35] = "CÓDIGO CONTRATO";
		titulos[36] = "GRUPO BURO";
		titulos[37] = "CLASIFICACIÓN SBS TITULAR";
		titulos[38] = "CLASIFICACIÓN SBS BANCO";
		titulos[39] = "CLASIFICACIÓN BANCO CÓNYUGE";
		titulos[40] = "CLASIFICACIÓN SBS CÓNYUGE";
		titulos[41] = "SCORING";
		titulos[42] = "TASA ESPECIAL";
		titulos[43] = "FLAG VERIF. DOMICILIARIA";
		titulos[44] = "EST. VERIF. DOMICILIARIA";
		titulos[45] = "FLAG VERIF. LABORAL";
		titulos[46] = "EST. VERIF. LABORAL";
		titulos[47] = "INDICADOR DELAGACIÓN";
		titulos[48] = "CÓDIGO USUARIO ACTUAL";
		
		list.add(titulos);
		
		for (Expediente e :listaExp) {
			Object[] objs = new Object[COLUMNAS_REPORTE];

			e.setVerificacionExpLab(verificacionExpBean.reporteHistPlano(e.getId(), Long.valueOf(Constantes.CODIGO_VERIFICACION_LABORAL)));		
			e.setVerificacionExpDom(verificacionExpBean.reporteHistPlano(e.getId(), Long.valueOf(Constantes.CODIGO_VERIFICACION_DOMICILIARIA)));

			objNulos (e);
			
		    objs[0] = e.getId();
		    objs[1] = e.getClienteNatural().getSegmento().getCodigo();
		    objs[2] = e.getClienteNatural().getSegmento().getDescripcion();
		    objs[3] = e.getEstado().getId();
		    objs[4] = e.getEstado().getDescripcion();
		    objs[5] = e.getProducto().getDescripcion();
		    objs[6] = e.getEmpleado().getCodigo();
		    objs[7] = e.getEmpleado().getNombres();
		    objs[8] = e.getFecRegistro();
		    objs[9] = e.getEmpleado().getOficina().getId();
		    objs[10] = e.getEmpleado().getOficina().getDescripcion();
		    objs[11] = e.getExpedienteTC().getSubproducto().getId();
		    objs[12] = e.getExpedienteTC().getSubproducto().getDescripcion();
		    objs[13] = e.getClienteNatural().getId();
		    objs[14] = e.getClienteNatural().getApePat();
		    objs[15] = e.getClienteNatural().getApeMat();
		    objs[16] = e.getClienteNatural().getNombre();
		    objs[17] = e.getClienteNatural().getTipoDoi().getDescripcion();
		    objs[18] = e.getClienteNatural().getNumDoi();
		    objs[19] = e.getClienteNatural().getTipoCliente().getDescripcion();
		    objs[20] = e.getClienteNatural().getIngNetoMensual();
		    objs[21] = e.getClienteNatural().getEstadoCivil().getDescripcion();
		    objs[22] = e.getClienteNatural().getPerExpPub();
		    objs[23] = e.getClienteNatural().getPagoHab();
		    objs[24] = e.getExpedienteTC().getTipoOferta().getDescripcion();
		    objs[25] = e.getExpedienteTC().getTipoMonedaSol().getDescripcion();
		    objs[26] = e.getExpedienteTC().getLineaCredSol();		    
		    objs[27] = e.getExpedienteTC().getTipoMonedaAprob().getDescripcion();
		    objs[28] = e.getExpedienteTC().getLineaCredAprob();
		    objs[29] = e.getExpedienteTC().getTipoResolucion();
		    objs[30] = e.getExpedienteTC().getCodPreEval();
		    objs[31] = e.getExpedienteTC().getRvgl();
		    objs[32] = e.getExpedienteTC().getLineaConsumo();
		    objs[33] = e.getExpedienteTC().getRiesgoCliente();
		    objs[34] = e.getExpedienteTC().getPorcentajeEndeudamiento();
		    objs[35] = e.getExpedienteTC().getNroContrato();
		    objs[36] = e.getExpedienteTC().getTipoBuro().getDescripcion();
		    objs[37] = e.getExpedienteTC().getClasificacionBanco();
		    objs[38] = e.getExpedienteTC().getClasificacionSbs();
		    objs[39] = e.getExpedienteTC().getBancoConyuge();
		    objs[40] = e.getExpedienteTC().getSbsConyuge();
		    objs[41] = e.getExpedienteTC().getTipoScoring().getDescripcion();
		    objs[42] = e.getExpedienteTC().getTasaEsp();
		    objs[43] = e.getExpedienteTC().getVerifDom();
		    objs[44] = e.getVerificacionExpDom().getFlagVerif();
		    objs[45] = e.getExpedienteTC().getVerifLab();
		    objs[46] = e.getVerificacionExpLab().getFlagVerif();
		    objs[47] = e.getExpedienteTC().getFlagDelegacion();
		    objs[48] = e.getExpedienteTC().getEmpleado().getCodigo();
		    
		    list.add(objs);
		}
		
		return list;
	}
	
	private void objNulos(Expediente e) {
		if (e.getClienteNatural()==null) {
			e.setClienteNatural(new ClienteNatural());				
		}
	    if (e.getClienteNatural().getSegmento()==null) {
		    e.getClienteNatural().setSegmento(new Segmento());
	    }
	    if (e.getClienteNatural().getTipoDoi()==null) {
		    e.getClienteNatural().setTipoDoi(new TipoDoi());
	    }
	    if (e.getClienteNatural().getTipoCliente()==null) {
	    	e.getClienteNatural().setTipoCliente(new TipoCliente());
	    }
	    if (e.getClienteNatural().getEstadoCivil()==null) {
	    	e.getClienteNatural().setEstadoCivil(new EstadoCivil());
	    }
	    if (e.getEstado()==null) {
	    	e.setEstado(new Estado());
	    }			    
	    if (e.getProducto()==null) {
	    	e.setProducto(new Producto());
	    }
	    if (e.getEmpleado()==null) {
	    	e.setEmpleado(new Empleado());
	    }
	    if (e.getEmpleado().getOficina()==null) {
	    	e.getEmpleado().setOficina(new Oficina());
	    }			
	    if (e.getExpedienteTC().getTipoMonedaAprob()==null) {
	    	e.getExpedienteTC().setTipoMonedaAprob(new TipoMoneda());
	    }	
	    if (e.getExpedienteTC().getSubproducto()==null) {
	    	e.getExpedienteTC().setSubproducto(new Subproducto());
	    }				
	    if (e.getExpedienteTC().getTipoOferta()==null) {
	    	e.getExpedienteTC().setTipoOferta(new TipoOferta());
	    }				
	    if (e.getExpedienteTC().getTipoMonedaSol()==null) {
	    	e.getExpedienteTC().setTipoMonedaSol(new TipoMoneda());
	    }
	    if (e.getExpedienteTC().getTipoMonedaAprob()==null) {
	    	e.getExpedienteTC().setTipoMonedaAprob(new TipoMoneda());
	    }
	    if (e.getExpedienteTC().getTipoBuro()==null) {
	    	e.getExpedienteTC().setTipoBuro(new TipoBuro());
	    }
	    if (e.getExpedienteTC().getTipoScoring()==null) {
	    	e.getExpedienteTC().setTipoScoring(new TipoScoring());
	    }		    
	    if (e.getExpedienteTC().getEmpleado()==null) {
	    	e.getExpedienteTC().setEmpleado(new Empleado());
	    }	    
	    if (e.getVerificacionExpDom()==null) {
	    	e.setVerificacionExpDom(new VerificacionExp());
	    }
	    if (e.getVerificacionExpLab()==null) {
	    	e.setVerificacionExpLab(new VerificacionExp());
	    }
	}
}
