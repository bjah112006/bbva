package com.pe.bbva.pyme.servlet;

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

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public DownloadServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	service(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service(request, response);
    }
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getParameter("url");
        PrintWriter writer = null;

        try {
            File file = new File(url);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            OutputStream out = response.getOutputStream();
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
        } catch (Exception e) {
            response.setContentType("application/octet-stream");
            e.printStackTrace(writer);
        }
    }

}
