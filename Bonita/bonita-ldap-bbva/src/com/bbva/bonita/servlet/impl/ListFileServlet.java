package com.bbva.bonita.servlet.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;

public class ListFileServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger("ListFileServlet");
	private static final long serialVersionUID = 1L;
	private static Properties props;
	
	static {
		try {
			props = new Properties();
			logger.log(Level.SEVERE, System.getProperty("btm.root").replace("\\", "/") + "/conf/configWFFastPyme.properties");
			props.load(new FileInputStream(System.getProperty("btm.root").replace("\\", "/") + "/conf/configWFFastPyme.properties"));
		} catch (IOException e) {
			logger.log(Level.SEVERE, "No found properties", e);
		}
	}

	@Override
	public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		StringBuilder sb= new StringBuilder();
		File files = new File(props.getProperty("ruta_salida"));
		String method = request.getParameter("method");
		String fecha = request.getParameter("fecha");
		logger.log(Level.INFO, "=== Enviamos Fecha: " + fecha);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		if("list".equalsIgnoreCase(method)) {
			if(files.exists() && files.isDirectory()) {
				File[] filesArray = files.listFiles();
				ordenarFicheros(filesArray, fecha);
				for(File file : filesArray) {
					if(file.isFile() && !file.isHidden()) {
						if(fecha!=null && fecha!=""){
							Date date = null;
							try {
								date = formatter.parse(fecha);
							} catch (ParseException e) {
								logger.log(Level.SEVERE, "Error formatting", e);
							}
							if(DateUtils.isSameDay(date, new Date(file.lastModified()))){
								if(sb.length() > 0) {
									sb.append(", ");
								}
								sb.append("{\"filename\":\"" + file.getName() + "\", \"datecreate\":" + file.lastModified() + "}");
							}
						}else{
							if(sb.length() > 0) {
								sb.append(", ");
							}
							sb.append("{\"filename\":\"" + file.getName() + "\", \"datecreate\":" + file.lastModified() + "}");
						}
					}
				}
			}
			logger.log(Level.INFO, sb.toString());
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.write("{\"files\": ["+ sb.toString()+ "]}");
			out.close();
		} else if("download".equalsIgnoreCase(method)) {
			String fileName = request.getParameter("file");
			File file = new File(props.getProperty("ruta_salida") + fileName);
			response.setContentType("application/octet-stream");
			response.setContentLength(Long.valueOf(file.length()).intValue());
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));

	        OutputStream outStream = response.getOutputStream();
	        outStream.write(IOUtils.toByteArray(new FileInputStream(file)));
	        outStream.close();
		}
	}
	
	private void ordenarFicheros(File[] files, String fecha){
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());				
			}
		});
	}
}
