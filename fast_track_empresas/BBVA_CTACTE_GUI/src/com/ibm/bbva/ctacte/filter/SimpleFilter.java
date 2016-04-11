package com.ibm.bbva.ctacte.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.util.ParametrosSistema;

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
//					text = text.replaceAll("\"/BBVA_CTACTE_GUI", "\"/BBVA_CTACTE_GUI");
				}
				response.getWriter().write(text);

			}
		}
		// long tiempoFin = System.currentTimeMillis();
		// System.out.println("demora: " + (tiempoFin - tiempoInicio) + " milisegundos");
	}
}