package com.ibm.bbva.ctacte.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;

public class ObtenerDocumentoServlet extends HttpServlet {
	
	private static final Logger LOG = LoggerFactory.getLogger(ObtenerDocumentoServlet.class);
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ExpedienteDAO expedienteDAO;
	
    public ObtenerDocumentoServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idExpediente = request.getParameter("expediente");
		String codigo = request.getParameter("codigo");
		LOG.info("expediente: " +  idExpediente);
		LOG.info("codigo: " +  codigo);
		
		Expediente expediente = expedienteDAO.load(Integer.parseInt(idExpediente));
		PrintWriter pw = response.getWriter();
		if (ConstantesBusiness.CODIGO_DOCUMENTO_DICTAMEN.equals(codigo)) {
			LOG.info(new String(expediente.getDictamenBastanteo()));
			pw.write(new String(expediente.getDictamenBastanteo()));
		} else if (ConstantesBusiness.CODIGO_DOCUMENTO_INSTRUCCIONES.equals(codigo)) {
			LOG.info(new String(expediente.getInstruccionesBastanteo()));
			pw.write(new String(expediente.getInstruccionesBastanteo()));
		}
		pw.close();
	}

}
