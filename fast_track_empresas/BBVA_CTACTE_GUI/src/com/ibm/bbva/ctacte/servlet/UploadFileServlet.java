package com.ibm.bbva.ctacte.servlet;

import java.io.File; 
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.service.ContentServiceImplPortProxy;
import com.ibm.bbva.cm.service.Documento;
import com.ibm.bbva.ctacte.bean.ParametrosConf;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.ParametrosConfDAO;
import com.ibm.bbva.ctacte.util.ParametrosSistema;

@WebServlet("/uploadFileServlet/*")
public class UploadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(UploadFileServlet.class);
	@EJB
	private ParametrosConfDAO parametrosConfDAO;
	
		
	private int maxFileSize =-1;
	private int maxMemSize = 10 * 1024;

	public UploadFileServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ParametrosConf paramTamañoMax=parametrosConfDAO.obtener(ConstantesParametros.CODIGO_CONF, ConstantesParametros.TAMANO_MAXIMO_ARCHIVO_MB);
		String strTamañoMaxArchivoMb=paramTamañoMax.getValorVariable();
		maxFileSize=Integer.parseInt(strTamañoMaxArchivoMb);
		
		// Check that we have a file upload request
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxMemSize);
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File("/tmp"));
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		upload.setSizeMax(maxFileSize*1024*1024);

		// Parse the request to get file items.
		List<FileItem> fileItems;
		try {
			fileItems = upload.parseRequest(request);
			byte[] bytes = IOUtils.toByteArray(fileItems.get(0).getInputStream());
			PrintWriter out = response.getWriter(); 
			if(!fileItems.get(0).getName().isEmpty()){

				LOG.info(fileItems.get(0).getFieldName());
				LOG.info("queryString: {}", request.getQueryString());
				LOG.info("RequestURI: {}", request.getRequestURI());
				response.setContentType("text/html");

				///
				String parts[] = request.getRequestURI().split("\\/");
				LOG.info(" parts[] :"+ parts[0]+parts[1]+parts[2]+parts[3]+parts[4]+parts[5]);

				Documento documento = new Documento();
				documento.setCodCliente(parts[3]);
				documento.setContenido(bytes);

				LOG.info("fileItems.get(0) :"+fileItems.get(0));
				LOG.info("fileItems.get(0).getName() :"+fileItems.get(0).getName());
				String[]tipo=fileItems.get(0).getName().split("\\.");			
				GregorianCalendar c = new GregorianCalendar();
				c.setTime(new Date());
				XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
				documento.setFechaCreacion(date2);		
				documento.setMandatorio(true);
				documento.setNombreArchivo(fileItems.get(0).getName());
				documento.setOrigen(ConstantesBusiness.ORIGEN_ARCHIVO);
				documento.setTipo(ConstantesBusiness.TIPO_ARCHIVO); 
				
//				documento.setTipo(tipo[1]); 


				documento.setTipoDoi(parts[5]);
				documento.setNumDoi(parts[4]);

				String url	= (String) ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).get(ConstantesParametros.SERVICIO_CONTENT);
				ContentServiceImplPortProxy serviceIBMBBVA = new ContentServiceImplPortProxy();
				serviceIBMBBVA._getDescriptor().setEndpoint(url);
				serviceIBMBBVA.insertPID(documento);
				
				documento = serviceIBMBBVA.find(documento);



				if(tipo[1].equalsIgnoreCase("zip")){
					documento.setUrlContent(documento.getUrlContent().concat("&content-type=application%2Fzip"));
					documento.setUrlContent(documento.getUrlContent().replace("%23?", "document.zip?"));
				}


				LOG.info("UPLOADSERVLET :"+documento.getUrlContent() + "', '" + fileItems.get(0).getName());
				out.println("<html>");
				out.println("<head>");
				out.println("</head>");
				out.println("<body>");
				//				out.println("<p>Archivo: <a href='" + documento.getUrlContent() + "'>" + fileItems.get(0).getName() + "</a></p>");
				out.println("<script type='text/javascript'>parent.enviar('" + documento.getUrlContent() + "', '" + fileItems.get(0).getName() + "');</script>");
				out.println("</body>");
				out.println("</html>");

			}else{
				out.println("<html>");
				out.println("<head>");
				out.println("</head>");
				out.println("<body>");
				//				out.println("<p>Archivo: <a href='" + documento.getUrlContent() + "'>" + fileItems.get(0).getName() + "</a></p>");
				out.println("<script type='text/javascript'>parent.enviar('', '');</script>");
				out.println("</body>");
				out.println("</html>");
			}
		} catch (Exception e) {
			LOG.error("", e);
		}
	}
}
