package com.ibm.bbva.ctacte.applet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class CrearPDF {

		  public static void concatPDFs(String[] streamOfPDFFiles, List<String> mapaHoja, OutputStream outputStream, boolean paginate) {

		    Document document = new Document();
		    try {
		      PdfReader[] readers = new PdfReader[streamOfPDFFiles.length];
		      for (int i = 0; i < streamOfPDFFiles.length; i++) {
		    	  //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("RUTA-->"+streamOfPDFFiles[i]);
		    	  PdfReader pdfReader;
		    	  try {
		    		  pdfReader = new PdfReader(streamOfPDFFiles[i]); // (pdf);
		    	  } catch (Exception e) {
		    		  pdfReader = null;
		    	  }
			      readers[i] = pdfReader;
		      }


		      // Create a writer for the outputstream
		      PdfWriter writer = PdfWriter.getInstance(document, outputStream);

		      document.open();
		      BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		      PdfContentByte cb = writer.getDirectContent(); // Holds the PDF

		      PdfImportedPage page = null;

		      Iterator<String> iteratorMapaHojas = mapaHoja.iterator();
		      
		      while (iteratorMapaHojas.hasNext())
		      {
			      String strCad = iteratorMapaHojas.next();
			      String strDocumento = null;
			      String strPagina = null;
			      int i = strCad.indexOf("|");
			      int numDocumento,numPagina;     
			      strDocumento = strCad.substring(0,i);
			      strPagina = strCad.substring(i+1);

//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("strDocumento-->"+strDocumento);
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("strPagina-->"+strPagina);
			      
			      numDocumento = Integer.parseInt(strDocumento.trim());
			      numPagina = Integer.parseInt(strPagina.trim());

			      PdfReader pdfReader = readers[numDocumento-1];
			      if (pdfReader != null) {
			          document.newPage();
			          page = writer.getImportedPage(pdfReader, numPagina);
			          cb.addTemplate(page, 0, 0);
			          if (paginate) {
			            cb.beginText();
			            cb.setFontAndSize(bf, 9);
			            cb.endText();
			          }
			          
			          pdfReader.close();
			      }
		      }
		      
		      writer.close();
		      outputStream.flush();
		      document.close();
		      outputStream.close();
		    } catch (Exception e) {
		      e.printStackTrace();
		    } finally {
		      if (document.isOpen())
		        document.close();
		      try {
		        if (outputStream != null)
		          outputStream.close();
		      } catch (IOException ioe) {
		        ioe.printStackTrace();
		      }
		    }
		  }
		



}
