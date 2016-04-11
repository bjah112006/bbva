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

/**
 * Servlet implementation class ServletReport
 */
@WebServlet("/ServletReport")
public class ServletReport extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletReport() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service(request, response);
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        PrintWriter writer = null;

        if ("downloadPDF".equalsIgnoreCase(method)) {
            try {
                File file = new File(request.getParameter("file"));
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
                OutputStream out = response.getOutputStream();
                String delete = request.getParameter("delete");

                FileInputStream input = new FileInputStream(file);
                BufferedOutputStream output = new BufferedOutputStream(out);
                int read = 0;
                int i = 0;
                byte[] buffer = new byte[1024];
                while ((read = input.read(buffer)) >= 0) {
                    output.write(buffer, 0, read);
                    i++;
                    if ((i % 4) == 0) {
                        output.flush();
                    }
                }
                input.close();
                output.close();
                if ("true".equalsIgnoreCase(delete)) {
                    file.delete();
                    String archivo = request.getParameter("file").replace(".pdf",".jasper");
                    File fileJasper = new File(archivo);
                    fileJasper.delete();
                    archivo = archivo.replace(".jasper",".jrxml");
                    File fileXml = new File(archivo);
                    fileXml.delete();
                }

            } catch (Exception e) {
                response.setContentType("application/pdf");
                e.printStackTrace(writer);
            }
        }
        
        if ("downloadExcel".equalsIgnoreCase(method)) {
            try {
                File file = new File(request.getParameter("file"));
                response.setContentType("application/xlsx");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
                OutputStream out = response.getOutputStream();
                String delete = request.getParameter("delete");

                FileInputStream input = new FileInputStream(file);
                BufferedOutputStream output = new BufferedOutputStream(out);
                int read = 0;
                int i = 0;
                byte[] buffer = new byte[1024];
                while ((read = input.read(buffer)) >= 0) {
                    output.write(buffer, 0, read);
                    i++;
                    if ((i % 4) == 0) {
                        output.flush();
                    }
                }
                input.close();
                output.close();
                if ("true".equalsIgnoreCase(delete)) {
                    file.delete();
//                    String archivo = request.getParameter("file").replace(".xlsx",".jasper");
//                    File fileJasper = new File(archivo);
//                    fileJasper.delete();
//                    archivo = archivo.replace(".jasper",".jrxml");
//                    File fileXml = new File(archivo);
//                    fileXml.delete();
                }

            } catch (Exception e) {
                response.setContentType("application/xlsx");
                e.printStackTrace(writer);
            }
        }
        return;
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

}
