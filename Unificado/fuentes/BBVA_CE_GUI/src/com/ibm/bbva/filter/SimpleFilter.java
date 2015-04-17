package com.ibm.bbva.filter;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.session.ParametrosConfBeanLocal;

import java.io.*;

public class SimpleFilter implements Filter {
	
	private FilterConfig filterConfig = null;
	private ParametrosConfBeanLocal parametrosConfBean;

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
		
		try {
			parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String junction = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, Constantes.JUNCTION_NAME_UN).getValorVariable();
		//System.out.println("junction::::"+junction);
		if(junction == null){
			junction = "";
		}
		else{
			junction = "/"+junction;
		}

		if (newResponse instanceof CharResponseWrapper) {
			String text = newResponse.toString();
			if(text!= null){
				String contentType = newResponse.getContentType();
				System.out.println("contentType: " + contentType);
				if(contentType != null && contentType.toLowerCase().indexOf("text/xml") != -1){
					text = text.replaceAll("\"/BBVA_CE_GUI", "\""+junction+"/BBVA_CE_GUI");
				}
				//System.out.println("text::::"+text);
				response.getWriter().write(text);

			}
		}
		
		//long tiempoFin = System.currentTimeMillis();
		
		//System.out.println("demora: " + (tiempoFin - tiempoInicio) + " milisegundos");
	}
}