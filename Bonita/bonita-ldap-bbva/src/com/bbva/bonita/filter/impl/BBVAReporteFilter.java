package com.bbva.bonita.filter.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.identity.UserMembership;
import org.bonitasoft.engine.identity.UserMembershipCriterion;
import org.bonitasoft.engine.session.APISession;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.bbva.bonita.wrapper.impl.CharResponseWrapper;

public class BBVAReporteFilter implements Filter {

	private static final Logger logger = Logger.getLogger("BBVAReporteFilter");
	private static final String xRequestWith = "X-Requested-With";
	private static final String facesRequest = "Faces-Request";
	private static final String MANAGER_GROUP = "GESTION";
	private static final String SOPORTE_GROUP = "SOPORTE";
	
	@Override
	public void destroy() {
	}

	@Override
	public synchronized void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		String writeHTML = "";
		boolean isAjaxRequest = true;
		boolean visibleReport=false;
		boolean visibleDocument = false;
		
		final HttpServletRequestAccessor requestAccessor = new HttpServletRequestAccessor((HttpServletRequest) request);
		final APISession apiSession = requestAccessor.getApiSession();
		
		HttpServletRequest rq = (HttpServletRequest) request;
		CharResponseWrapper charResponseWrapper = new CharResponseWrapper((HttpServletResponse) response); 
		filterChain.doFilter(request, charResponseWrapper);
		writeHTML = charResponseWrapper.toString();
		
		isAjaxRequest = isAjaxRequest && rq.getHeader(xRequestWith) != null;
		isAjaxRequest = isAjaxRequest && rq.getHeader(xRequestWith).equalsIgnoreCase("XMLHttpRequest");
		isAjaxRequest = isAjaxRequest || rq.getHeader(facesRequest) != null;
		isAjaxRequest = isAjaxRequest && rq.getHeader(facesRequest).equalsIgnoreCase("partial/ajax");
		
		String userName = apiSession.getUserName();
		logger.info("getParameter username: "+userName);
		if(userName!=null){
			try {
				IdentityAPI identityAPI=TenantAPIAccessor.getIdentityAPI(apiSession);
				visibleReport=isMemberFromManagerGroup(identityAPI,userName,MANAGER_GROUP);
				visibleDocument = isMemberFromManagerGroup(identityAPI,userName,SOPORTE_GROUP);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Error en TenantAPIAccessor.getIdentityAPI(apiSession).", e);
			}
		}
		
		if(visibleReport && !isAjaxRequest) {
			logger.info("Usuario "+userName+" tiene acceso al reporte.");
			Document doc = Jsoup.parse(writeHTML);
			doc.outputSettings().prettyPrint(false);
			Elements elements = doc.getElementsByTag("head");
			if(elements != null && !elements.isEmpty()) {
				elements.append("<script type='text/javascript' src='bbva/bbva.js'></script>");
				elements.append("<link rel='stylesheet' type='text/css' href='bbva/bbva.css' />");
				writeHTML = doc.outerHtml();
			}
		}
		
		if(visibleDocument){
			logger.info("Usuario "+userName+" tiene acceso a la consulta y actualizacion de documentos.");
			Document doc = Jsoup.parse(writeHTML);
			doc.outputSettings().prettyPrint(false);
			Elements elements = doc.getElementsByTag("head");
			if(elements != null && !elements.isEmpty()) {
				elements.append("<script type='text/javascript' src='bbva/soporte.js'></script>");
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
	
	private boolean isMemberFromManagerGroup(IdentityAPI identityAPI,String userName, String groupName) {
		String group = "";
		try {
			User user=identityAPI.getUserByUserName(userName);
			List<UserMembership> lst_membership = identityAPI.getUserMemberships(user.getId(), -1, 10, UserMembershipCriterion.GROUP_NAME_ASC);
			if (lst_membership != null) {
				for (UserMembership membership : lst_membership) {
					group = membership.getGroupName();
					logger.info("Group name of "+userName+ " is "+group);
					if(group.equalsIgnoreCase(groupName)){
						return true;
					}
				}
			}
		} catch (Exception e) {
            logger.log(Level.SEVERE, "Error en TenantAPIAccessor.getIdentityAPI(apiSession).", e);
		}
		return false;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
