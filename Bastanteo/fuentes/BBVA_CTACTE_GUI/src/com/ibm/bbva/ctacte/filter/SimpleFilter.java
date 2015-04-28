package com.ibm.bbva.ctacte.filter;

import javax.servlet.*;
import javax.servlet.http.*;

import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.util.ParametrosSistema;

import java.io.*;

public class SimpleFilter implements Filter {
	
	private FilterConfig filterConfig = null;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public void destroy() {
		filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
					throws IOException, ServletException {
		//long tiempoInicio = System.currentTimeMillis();
		
		ServletResponse newResponse = response;

		if (request instanceof HttpServletRequest) {
			newResponse = new CharResponseWrapper((HttpServletResponse) response);
		}

		chain.doFilter(request, newResponse);
		
		String junction	= (String) ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).get(ConstantesParametros.JUNCTION_NAME_CTACTE);
		// System.out.println("junction::::"+junction);
		if(junction == null || junction.isEmpty()){
			junction = "";
		} else {
			junction = "/" + junction;
		}

		if (newResponse instanceof CharResponseWrapper) {
			String text = newResponse.toString();
			if(text!= null){
				//System.out.println("text::::"+text);
				String contentType = newResponse.getContentType();
				if(contentType != null && contentType.toLowerCase().indexOf("text/xml") != -1){
					text = text.replaceAll("\"/BBVA_CTACTE_GUI", "\""+junction+"/BBVA_CTACTE_GUI");
				}
				response.getWriter().write(text);

			}
		}
		// long tiempoFin = System.currentTimeMillis();
		// System.out.println("demora: " + (tiempoFin - tiempoInicio) + " milisegundos");
	}
}