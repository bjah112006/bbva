package com.ibm.bbva.servlet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.bbva.jobs.LDAPCarga;
import com.ibm.bbva.thread.ThreadCargaLdap;

/**
 * Servlet implementation class StartUpCargaLadap
 */
@WebServlet("/StartUpCargaLadap")
public class StartUpCargaLadap extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StartUpCargaLadap() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 service(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        PrintWriter writer = null;
        
        if ("cargaLdap".equalsIgnoreCase(method)) {
        	try {
        		ThreadCargaLdap carga =  new ThreadCargaLdap();
        		carga.start();
			} catch(Exception e) {
				response.setContentType("text/plain");
				e.printStackTrace(writer);
				
			}
        	
        	return;
        }
	}

}
