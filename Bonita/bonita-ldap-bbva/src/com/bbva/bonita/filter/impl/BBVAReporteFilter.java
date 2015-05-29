package com.bbva.bonita.filter.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.bbva.bonita.wrapper.impl.CharResponseWrapper;

public class BBVAReporteFilter implements Filter {

	private static final Logger logger = Logger.getLogger("BBVAReporteFilter");
	private static final String xRequestWith = "X-Requested-With";
	private static final String facesRequest = "Faces-Request";
	
	@Override
	public void destroy() {
	}

	@Override
	public synchronized void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		String writeHTML = "";
		boolean isAjaxRequest = true;
		
		HttpServletRequest rq = (HttpServletRequest) request;
		CharResponseWrapper charResponseWrapper = new CharResponseWrapper((HttpServletResponse) response); 
		filterChain.doFilter(request, charResponseWrapper);
		writeHTML = charResponseWrapper.toString();
		
		isAjaxRequest = isAjaxRequest && rq.getHeader(xRequestWith) != null;
		isAjaxRequest = isAjaxRequest && rq.getHeader(xRequestWith).equalsIgnoreCase("XMLHttpRequest");
		isAjaxRequest = isAjaxRequest || rq.getHeader(facesRequest) != null;
		isAjaxRequest = isAjaxRequest && rq.getHeader(facesRequest).equalsIgnoreCase("partial/ajax");
		
		if(!isAjaxRequest) {
			Document doc = Jsoup.parse(writeHTML);
			doc.outputSettings().prettyPrint(false);
			Elements elements = doc.getElementsByTag("head");
			if(elements != null && !elements.isEmpty()) {
				elements.append("<script type='text/javascript' src='bbva/bbva.js'></script>");
				elements.append("<link rel='stylesheet' type='text/css' href='bbva/bbva.css' />");
				writeHTML = doc.outerHtml();
			}
		}

		logger.info("Interceptado por el filtro BBVAReporteFilter");
		// logger.info(writeHTML);
		PrintWriter out = response.getWriter();
		out.write(writeHTML);
		out.close();
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
