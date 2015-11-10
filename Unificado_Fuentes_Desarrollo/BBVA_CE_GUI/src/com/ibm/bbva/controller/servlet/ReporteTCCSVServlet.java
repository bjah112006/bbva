package com.ibm.bbva.controller.servlet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.joda.time.DateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.TablaGenerarTCMB;
import com.ibm.bbva.tabla.reporte.vo.DatosGeneradosConsVO;
import com.ibm.bbva.tabla.reporte.vo.DatosGeneradosHisVO;
import com.ibm.bbva.tabla.util.vo.ListaReportePorcentajeToe;
import com.ibm.bbva.tabla.util.vo.ListaReporteToe;
import com.ibm.bbva.tabla.util.vo.ListaReporteUnidadToe;
import com.ibm.bbva.util.AyudaDatosReporte;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class ReporteTCCSVServlet
 */
@WebServlet("/ReporteTCCSVServlet")
public class ReporteTCCSVServlet extends HttpServlet {
	private static final long serialVersionUID = 4440011247408877539L;
	/**
	 * Variables para Reporte Historico-Consolidado
	 * */	
	private List<DatosGeneradosConsVO> listDatosGeneradosCons;
	private List<DatosGeneradosHisVO> listDatosGeneradosHis;
	/**
	 * Variables para Reporte TOE
	 * */
	private List<List<ListaReporteToe>> listaReporte_Prod_TipoOferta_Flujo;
	private List<List<ListaReportePorcentajeToe>> listPorc_Prod_TipOferta_Flujo;
	private List<List<ListaReporteUnidadToe>> listUnid_Prod_TipOferta_Flujo;
	private ArrayList<String> listaPerfiles;
	private ArrayList<String> listaRoles;
	private ArrayList<String> listaTiempo_tc_te;
	private String producto;
	private String tipoOferta;
	
	
	private static final double CHARACTER_WIDTH = 7.5;
	private static final double PADDING = 5;
	private static final double ADJUST_START = 5;
	private static final double ADJUST_PER_UNIT = 0.5;
	private static final double EXCEL_LEADING = 182d; // Only applies to Arial 10pt
	
	private static final double EXCEL_FACTOR = 256d;
	
	private static final Logger LOG = LoggerFactory.getLogger(ReporteTCCSVServlet.class);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReporteTCCSVServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String accion = request.getParameter("accion")==null?"":request.getParameter("accion");
		if(accion.equals("REPORTETC")) {
			generarReporteTC(request, response);
		}else
			if(accion.equals("REPORTETOE")) {
				generarReporteTOE(request, response);
			}
	}
	
	private String generarReporteTOE(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		try {
		
			ArrayList arrayList = new ArrayList();
			String fechaInicio = request.getParameter("fechaInicio")==null?"":request.getParameter("fechaInicio");
			String fechaFin = request.getParameter("fechaFin")==null?"":request.getParameter("fechaFin");
			String codProducto = request.getParameter("codProducto")==null?"-1":request.getParameter("codProducto");
			String codPerfil = request.getParameter("codPerfil")==null?"-1":request.getParameter("codPerfil");
			String codTipoOferta = request.getParameter("codTipoOferta")==null?"-1":request.getParameter("codTipoOferta");
			String codEstado = request.getParameter("codEstado")==null?"-1":request.getParameter("codEstado");
			
			String codTerritorio = "-1";
			
			if(request.getParameter("codTerritorio")==null)
				codTerritorio = "-1";
			else{
				if(request.getParameter("codTerritorio").equals("undefined"))
					codTerritorio = "-1";
				else
					codTerritorio = request.getParameter("codTerritorio");
			}
			
			String codOficina = "-1";
			
			if(request.getParameter("codOficina")==null)
				codOficina = "-1";
			else{
				if(request.getParameter("codOficina").equals("undefined"))
					codOficina = "-1";
				else
					codOficina = request.getParameter("codOficina");
			}
			
			String tipoReporteTOE = request.getParameter("tipoReporteTOE")==null?"-1":request.getParameter("tipoReporteTOE");
			String nombreArchivo="Reporte TOE_"+fechaInicio+"_"+fechaFin;
			nombreArchivo = nombreArchivo.replace("/", "-");
		
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date fInicio = formatter.parse(fechaInicio);
			Date fFin = formatter.parse(fechaFin);
			
			LOG.info("tipoReporteTOE->"+tipoReporteTOE);
			arrayList.add(fInicio);
			arrayList.add(fFin);
			arrayList.add(Long.parseLong(codProducto));
			arrayList.add(Long.parseLong(codPerfil));
			arrayList.add(Long.parseLong(codTipoOferta));
			arrayList.add(Long.parseLong(codEstado));
			arrayList.add(Long.parseLong(codTerritorio));
			arrayList.add(Long.parseLong(codOficina));
			arrayList.add(Integer.parseInt(tipoReporteTOE));
			
 
			
			AyudaDatosReporte objAyudaDatosReporte=new AyudaDatosReporte();
			String result= objAyudaDatosReporte.buscarGenerarTOE(arrayList);
			

				
			if(tipoReporteTOE.equals("0")){
				setearValoresListas(objAyudaDatosReporte);
			    // Defino el Libro de Excel
			     HSSFWorkbook wb = new HSSFWorkbook();

			     // Creo la Hoja en Excel
			     Sheet sheet = wb.createSheet(Constantes.DESCRIPCION_HOJA_EXCEL);

			     // quito las lineas del libro para darle un mejor acabado
			     sheet.setDisplayGridlines(false);
			            
				 /**
				 * Comienza creación de encabezado
				 * */
			     int numColTituloPrincipal=1;
			     int numFirstColTituloRol=2; 
			     int numLastColTituloRol=2; 
			     int numColTituloRol=1;
			     int numFilaTitulo1=1;
			     int firsRow=1;
			     int lastRow=1;
			     int firstCol=1;
			     int lastCol=18;	
			     
			     int cont = 0;
		         //Creación de Titulo de reporte 
			      for(List<ListaReporteToe> listReporteToe : listaReporte_Prod_TipoOferta_Flujo){

			        if(listReporteToe!=null && listReporteToe.size()>0){
			        	objAyudaDatosReporte.pintarTitulos(sheet, firsRow, lastRow, firstCol, lastCol);
					    Row t1row = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1);
					    objAyudaDatosReporte.createTituloCell(wb, t1row, numColTituloPrincipal, CellStyle.ALIGN_CENTER,
					    		CellStyle.VERTICAL_CENTER, Constantes.DESCRIPCION_TITULO_REPORTE_TOE);
					    firsRow++;
					    lastRow++;
					    
					    objAyudaDatosReporte.pintarTitulos(sheet, firsRow, lastRow, firstCol, lastCol);
					    Row t2row = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1+1);				            
					    objAyudaDatosReporte.createTituloCell(wb, t2row, numColTituloRol, CellStyle.ALIGN_CENTER,
						        CellStyle.VERTICAL_CENTER, Constantes.DESCRIPCION_SUBTITULO_1+" "+fechaInicio+" AL "+fechaFin+getTituloTabla(cont, objAyudaDatosReporte));
					    
					    //Creación de Titulo Rol
					    Row titRolRow = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1+3);
					    objAyudaDatosReporte.crearTituloCabecera_Rol_Perfil_Tiempo(wb, sheet, titRolRow, numFirstColTituloRol, numLastColTituloRol, numFilaTitulo1+3, 0, 200, 1, listaRoles);
					            	
						//Creación de Titulo Perfiles
					    Row titPerRow = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1+4);
					    objAyudaDatosReporte.crearTituloCabecera_Rol_Perfil_Tiempo(wb, sheet, titPerRow, numFirstColTituloRol, numLastColTituloRol, numFilaTitulo1+4, 0, 200, 0, listaPerfiles);
					            
						//Creación de Titulo tiempos tc y te
					    Row titTiempRow = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1+5);
					    objAyudaDatosReporte.crearTituloCabecera_Rol_Perfil_Tiempo(wb, sheet, titTiempRow, numFirstColTituloRol, numLastColTituloRol, numFilaTitulo1+5, 0, 200, 2, listaTiempo_tc_te);	
					            
					    if(listReporteToe!=null && listReporteToe.size()>0){
					    	//Creación de Tiempos Objetivos 
					    	objAyudaDatosReporte.crearContenido_Tiempo(wb, sheet, numFirstColTituloRol-1, 0, 300, listReporteToe, numFilaTitulo1);	
					    }
						//Creación de Porcentaje TOE
					    //if(listPorc_Prod_TipOferta_Flujo!=null && listPorc_Prod_TipOferta_Flujo.size()>0)
					    List<ListaReportePorcentajeToe> listPorcentaje = listPorc_Prod_TipOferta_Flujo.get(cont);
					    Row filPorc = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1+9);
					    objAyudaDatosReporte.crearPorcentajeTOE(wb, sheet, filPorc, numFirstColTituloRol-1, numLastColTituloRol+16, numFilaTitulo1+9, 0, 300, 2, listPorcentaje);
					    
					    // Creación de Unidades CPM y RIESGO

					    // CPM
					    List<ListaReporteUnidadToe> listaUnidad = listUnid_Prod_TipOferta_Flujo.get(cont);
					    Row filCMP = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1+11);
					    objAyudaDatosReporte.crearCeldaTitulo(wb, filCMP, numFirstColTituloRol, CellStyle.ALIGN_CENTER,
					   	        CellStyle.VERTICAL_CENTER, listaUnidad.get(0).getColumn1(), true, true, 200, 200);
					    
					    objAyudaDatosReporte.crearCeldaTitulo(wb, filCMP, numFirstColTituloRol+1, CellStyle.ALIGN_CENTER,
					   	        CellStyle.VERTICAL_CENTER, listaUnidad.get(0).getColumn2(), true, true, 200, 2);
					    
					    // RIESGO
					    Row filRiesgo = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1+12);
					    objAyudaDatosReporte.crearCeldaTitulo(wb, filRiesgo, numFirstColTituloRol, CellStyle.ALIGN_CENTER,
					   	        CellStyle.VERTICAL_CENTER, listaUnidad.get(1).getColumn1(), true, true, 200, 200);
					    
					    objAyudaDatosReporte.crearCeldaTitulo(wb, filRiesgo, numFirstColTituloRol+1, CellStyle.ALIGN_CENTER,
					   	        CellStyle.VERTICAL_CENTER, listaUnidad.get(1).getColumn2(), true, true, 200, 2);
					    
					    numFilaTitulo1=numFilaTitulo1+16;
					    firsRow=numFilaTitulo1;
					    lastRow=numFilaTitulo1;
			        	}
			        
			        	cont++;
			         }
			            
			         Sheet ssheet = wb.getSheetAt(0);
			         ssheet.autoSizeColumn(0);
			         ssheet.autoSizeColumn(1);
			         ssheet.setColumnWidth(2, 256 *20);

					 response.setHeader("Cache-Control","private");
					 response.setHeader("Pragma","private");
					 response.addDateHeader("Expires", 0);
					 response.setContentType("application/vnd.ms-excel");  
						
					 response.addHeader("Content-Disposition", "filename="+nombreArchivo+".xls");
					 try {
						wb.write(response.getOutputStream());
					} catch (IOException e) {
						// TODO Bloque catch generado automáticamente
						LOG.error(e.getMessage(), e);
					}		
				
			}else{
				setearValoresListasEspecifico(objAyudaDatosReporte);
				
				if(result==null || !result.equals("1")){
				    // Defino el Libro de Excel
				     HSSFWorkbook wb = new HSSFWorkbook();

				     // Creo la Hoja en Excel
				     Sheet sheet = wb.createSheet(Constantes.DESCRIPCION_HOJA_EXCEL);

				     // quito las lineas del libro para darle un mejor acabado
				     sheet.setDisplayGridlines(false);
				            
					 /**
					 * Comienza creación de encabezado
					 * */
				     int numColTituloPrincipal=1;
				     int numFirstColTituloRol=2; 
				     int numLastColTituloRol=2; 
				     int numColTituloRol=1;
				     int numFilaTitulo1=1;
				     int firsRow=1;
				     int lastRow=1;
				     int firstCol=1;
				     int lastCol=4;	
				     
				     int cont = 0;
			         //Creación de Titulo de reporte 
				     if(listaReporte_Prod_TipoOferta_Flujo!=null)
				      for(List<ListaReporteToe> listReporteToe : listaReporte_Prod_TipoOferta_Flujo){
				    	 
				        if(listReporteToe!=null && listReporteToe.size()>0){
				        	 LOG.info("listReporteToe:::"+listReporteToe.size());
						    Row t1row = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1);
						    objAyudaDatosReporte.pintarTitulos(sheet, firsRow, lastRow, firstCol, lastCol);
						    objAyudaDatosReporte.createTituloCell(wb, t1row, numColTituloPrincipal, CellStyle.ALIGN_CENTER,
						    		CellStyle.VERTICAL_CENTER, Constantes.DESCRIPCION_TITULO_REPORTE_TOE_ESPECIFICO);				    
						    firsRow++;
						    lastRow++;
						    numFilaTitulo1++;
						    
						    LOG.info("subtitulos");
						    objAyudaDatosReporte.pintarTitulos(sheet, firsRow, lastRow, firstCol, lastCol-2);
						    Row t2row1 = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1+1);				            
						    objAyudaDatosReporte.createTituloCell(wb, t2row1, numColTituloRol, CellStyle.ALIGN_LEFT,
							        CellStyle.VERTICAL_CENTER, Constantes.DESCRIPCION_SUBTITULO_1_1+" : "+objAyudaDatosReporte.getProductoEspecifico());
						   // Row t2row1_2 = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1+1);

						    firsRow++;
						    lastRow++;
						    objAyudaDatosReporte.pintarTitulos(sheet, firsRow, lastRow, firstCol, lastCol-2);
						    Row t2row2 = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1+2);				            
						    objAyudaDatosReporte.createTituloCell(wb, t2row2, numColTituloRol, CellStyle.ALIGN_LEFT,
							        CellStyle.VERTICAL_CENTER, Constantes.DESCRIPCION_SUBTITULO_2+" : "+objAyudaDatosReporte.getTipoOfertaEspecifico());				    
						    	
						    numFilaTitulo1++;
						    
						    //Creación de Titulo Unidad
						    Row titRolRow = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1+3);
						    objAyudaDatosReporte.crearTituloCabecera_Rol_Perfil_Tiempo(wb, sheet, titRolRow, numFirstColTituloRol, numLastColTituloRol+1, numFilaTitulo1+3, 0, 200, 0, listaRoles);
						            	
							//Creación de Titulo Perfiles
						    Row titPerRow = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1+4);
						    objAyudaDatosReporte.crearTituloCabecera_Rol_Perfil_Tiempo(wb, sheet, titPerRow, numFirstColTituloRol, numLastColTituloRol+1, numFilaTitulo1+4, 0, 200, 0, listaPerfiles);
						            
						    
							//Creación de Titulo tiempos tc y te
						    Row titTiempRow = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1+5);
						    objAyudaDatosReporte.crearTituloCabecera_Rol_Perfil_Tiempo(wb, sheet, titTiempRow, numFirstColTituloRol, numLastColTituloRol, numFilaTitulo1+5, 0, 200, 2, listaTiempo_tc_te);	
						            
						    if(listReporteToe!=null && listReporteToe.size()>0){
						    	//Creación de Tiempos Objetivos 
						    	objAyudaDatosReporte.crearContenido_TiempoEspecifico(wb, sheet, numFirstColTituloRol-1, 0, 300, listReporteToe, numFilaTitulo1);	
						    }
							//Creación de Porcentaje TOE
						    List<ListaReportePorcentajeToe> listPorcentaje = listPorc_Prod_TipOferta_Flujo.get(cont);
						    Row filPorc = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1+9);
						    objAyudaDatosReporte.crearPorcentajeTOE_especifico(wb, sheet, filPorc, numFirstColTituloRol-1, numLastColTituloRol+3, numFilaTitulo1+9, 0, 300, 2, listPorcentaje);
						    
						    // Creación de Unidades CPM y RIESGO

						    
						    numFilaTitulo1=numFilaTitulo1+16;
						    firsRow=numFilaTitulo1;
						    lastRow=numFilaTitulo1;
				        	}
				        
				        	cont++;
				         }
				            
				         Sheet ssheet = wb.getSheetAt(0);
				         ssheet.autoSizeColumn(0);
				         ssheet.autoSizeColumn(1);
				         ssheet.setColumnWidth(2, 256 *20);
				         ssheet.setColumnWidth(3, 256 *20);
				         //ssheet.setro(3, 256 *20);
						 response.setHeader("Cache-Control","private");
						 response.setHeader("Pragma","private");
						 response.addDateHeader("Expires", 0);
						 response.setContentType("application/vnd.ms-excel");  
							
						//response.addHeader("Content-Disposition", "filename="+nombreArchivo+".xls");
						 response.addHeader("Content-Disposition", "attachment; filename=\""+nombreArchivo+".xls\"");
						 try {
							 LOG.info("Imprimir");
							wb.write(response.getOutputStream());
						} catch (IOException e) {
							// TODO Bloque catch generado automáticamente
							LOG.error(e.getMessage(), e);
						}						
				}

				
			}
				

				 
		} catch (Exception e) {
		             LOG.error(e.getMessage(), e);
		}
		return null;
	}
	
	private String generaTOEEspecifico(HttpServletResponse response, AyudaDatosReporte objAyudaDatosReporte, 
			String fechaInicio, String fechaFin, String nombreArchivo){
	
		
			 return null;
	}
	
	private String generarTOEGeneral(HttpServletResponse response, AyudaDatosReporte objAyudaDatosReporte, 
			String fechaInicio, String fechaFin, String nombreArchivo){

		
			 return null;
	}
	
	private String generarReporteTC(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			TablaGenerarTCMB tablaGenerarTC = new TablaGenerarTCMB();
			//BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
			String nombreArchivo = "";
			String tituloReporte = "";
			String rangoFechas = "";
			ArrayList arrayList = new ArrayList();
			String fechaInicio = request.getParameter("fechaInicio")==null?"":request.getParameter("fechaInicio");
			String fechaFin = request.getParameter("fechaFin")==null?"":request.getParameter("fechaFin");
			String codProducto = request.getParameter("codProducto")==null?"-1":request.getParameter("codProducto");
			String codPerfil = request.getParameter("codPerfil")==null?"-1":request.getParameter("codPerfil");
			String codTipoOferta = request.getParameter("codTipoOferta")==null?"-1":request.getParameter("codTipoOferta");
			String codEstado = request.getParameter("codEstado")==null?"-1":request.getParameter("codEstado");
			
			String codTerritorio = "-1";
			
			if(request.getParameter("codTerritorio")==null)
				codTerritorio = "-1";
			else{
				if(request.getParameter("codTerritorio").equals("undefined"))
					codTerritorio = "-1";
				else
					codTerritorio = request.getParameter("codTerritorio");
			}
			
			String codOficina = "-1";
			
			if(request.getParameter("codOficina")==null)
				codOficina = "-1";
			else{
				if(request.getParameter("codOficina").equals("undefined"))
					codOficina = "-1";
				else
					codOficina = request.getParameter("codOficina");
			}
			
			String tipoReporteTC = request.getParameter("tipoReporteTC")==null?"-1":request.getParameter("tipoReporteTC");
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date fInicio = formatter.parse(fechaInicio);
			Date fFin = formatter.parse(fechaFin);
			arrayList.add(fInicio);
			arrayList.add(fFin);
			arrayList.add(Long.parseLong(codProducto));
			arrayList.add(Long.parseLong(codPerfil));
			arrayList.add(Long.parseLong(codTipoOferta));
			arrayList.add(Long.parseLong(codEstado));
			arrayList.add(Long.parseLong(codTerritorio));
			arrayList.add(Long.parseLong(codOficina));
			arrayList.add(Integer.parseInt(tipoReporteTC));
									
			if(tipoReporteTC.equals(Constantes.ID_TC_CONSOLIDADO)){
				nombreArchivo = Constantes.TITULO_REPORTE_TC_CONSOLIDADO;
				tituloReporte = Constantes.TITULO_REPORTE_TC_CONSOLIDADO;
			}else {
				nombreArchivo = Constantes.TITULO_REPORTE_TC_HISTORICO;
				tituloReporte = Constantes.TITULO_REPORTE_TC_HISTORICO;
			}
						
			nombreArchivo = nombreArchivo +"_"+ fechaInicio +"_"+ fechaFin;
			nombreArchivo = nombreArchivo.replace("/", "-");
			//response.addHeader("Content-disposition", "filename=" + nombreArchivo + ".csv");
						
			rangoFechas = "Rango de fecha: del " + fechaInicio + " al " + fechaFin;
			//rangoFechas = rangoFechas.replace("/", "-");
			tituloReporte = EliminarCaracterExt(tituloReporte);
			
			tablaGenerarTC.buscarTC(arrayList);
			
			// Defino el Libro de Excel
		    HSSFWorkbook wb = new HSSFWorkbook();
	
		     // Creo la Hoja en Excel
		    Sheet sheet = wb.createSheet(Constantes.DESCRIPCION_HOJA_EXCEL2);
			
		    int numColTituloPrincipal=1;
		    int numFirstColTituloRol=2; 
		    int numLastColTituloRol=2; 
		    int numColTituloRol=1;
		    int numFilaTitulo1=1;
		    int firsRow=1;
		    int lastRow=1;
		    int firstCol=1;
		    int lastCol=18;
		     
		    int cont = 0;

        	tablaGenerarTC.pintarTitulos(sheet, firsRow, lastRow, firstCol, lastCol);
		    Row t1row = tablaGenerarTC.crearFila(sheet, wb, numFilaTitulo1);
		    tablaGenerarTC.createTituloCell(wb, t1row, numColTituloPrincipal, CellStyle.ALIGN_CENTER,
		    		CellStyle.VERTICAL_CENTER, tituloReporte);
		    firsRow++;
		    lastRow++;
		    
		    tablaGenerarTC.pintarTitulos(sheet, firsRow, lastRow, firstCol, lastCol);
		    Row t2row = tablaGenerarTC.crearFila(sheet, wb, numFilaTitulo1+1);
		    tablaGenerarTC.createTituloCell(wb, t2row, numColTituloRol, CellStyle.ALIGN_CENTER,
			        CellStyle.VERTICAL_CENTER, rangoFechas);
		    		    
			if(tipoReporteTC.equals(Constantes.ID_TC_CONSOLIDADO)){ // Consolidado
				listDatosGeneradosCons = tablaGenerarTC.getListDatosGeneradosCons();
				//generaTituloReporte(writer,60,tituloReporte,10,rangoFechas,9);
				//generaDatosConsolidadoCsv(writer);
				generaDatosConsolidadoXls(sheet, wb, numFilaTitulo1);
			}else { // Historico
				listDatosGeneradosHis = tablaGenerarTC.getListDatosGeneradosHis();
				//generaTituloReporte(writer,27,tituloReporte,10,rangoFechas,9);
				//generarDatosHistoricoCsv(writer);
				generarDatosHistoricoXls(sheet, wb, numFilaTitulo1);
			}
			//writer.flush();
						
			Sheet ssheet = wb.getSheetAt(0);
	        ssheet.autoSizeColumn(0);
	        ssheet.autoSizeColumn(1);
	        ssheet.setColumnWidth(2, 256 *20);
	         
			response.setHeader("Cache-Control","private");
			response.setHeader("Pragma","private");
			response.addDateHeader("Expires", 0);
			//response.setContentType("text/csv");
			response.setContentType("application/vnd.ms-excel");  
			
			response.addHeader("Content-Disposition", "filename="+nombreArchivo+".xls");
			wb.write(response.getOutputStream());
		}
		catch(Exception ex) {
			LOG.error(ex.getMessage(), ex);
		}
		return null;
	}
	
	private String generarDatosHistoricoCsv(BufferedWriter writer) throws IOException{
		StringBuilder sb = new StringBuilder();
		String replaceComa = " ";
		//Generamos los encabezados
		sb.append("NRO_EXPEDIENTE"); 
		sb.append(",");
		sb.append("NUMERO_REGISTRO");
		sb.append(",");
		sb.append("USUARIO_ACTUAL");
		sb.append(",");
		sb.append("ESTADO_EXPEDIENTE");
		sb.append(",");
		sb.append("CODIGO_ESTADO");
		sb.append(",");
		sb.append("PERFIL");
		sb.append(",");
		sb.append("CODIGO_PRODUCTO");
		sb.append(",");
		sb.append("NOMBRE_PRODUCTO");
		sb.append(",");
		sb.append("CODIGO_SUB_PRODUCTO");
		sb.append(",");
		sb.append("NOMBRE_SUB_PRODUCTO");
		sb.append(",");
		sb.append("CODIGO_OFICINA");
		sb.append(",");
		sb.append("NOMBRE_OFICINA");
		sb.append(",");
		sb.append("CODIGO_TERRITORIO");
		sb.append(",");
		sb.append("NOMBRE_TERRITORIO");
		sb.append(",");
		sb.append("FECHA_HORA_LLEGADA");
		sb.append(",");
		sb.append("FECHA_HORA_INICIO_TRABAJO");
		sb.append(",");
		sb.append("FECHA_HORA_ENVIO");
		sb.append(",");
		sb.append("TIEMPO_EJECUCION_TE");
		sb.append(",");
		sb.append("TIEMPO_COLA_TC");
		sb.append(",");
		sb.append("TIEMPO_PROCESO_TP");
		sb.append(",");
		sb.append("CUMPLIO_ANS");
		sb.append(",");
		sb.append("FLAG_DEVOLUCION");
		sb.append(",");
		sb.append("FLAG_RETRAER");
		sb.append(",");
		sb.append("TERMINAL");
		sb.append(",");
		sb.append("OBSERVACION");
		sb.append(",");
		sb.append("MOTIVO_RECHAZO");
		sb.append(",");
		sb.append("COMENTARIO");
		sb.append(",");
		sb.append("ANS");
		writer.append(sb);
		writer.newLine();
		
		if(listDatosGeneradosHis != null) {
			//Recorremos los datos
			for (DatosGeneradosHisVO obj : listDatosGeneradosHis) {
				if (obj != null) {
					obj.setNroExpediente(obj.getNroExpediente()==null?"":obj.getNroExpediente());
					obj.setNumeroRegistro(obj.getNumeroRegistro()==null?"":obj.getNumeroRegistro());
					obj.setUsuarioActual(obj.getUsuarioActual()==null?"":obj.getUsuarioActual());
					obj.setEstadoExpediente(obj.getEstadoExpediente()==null?"":obj.getEstadoExpediente());
					obj.setCodigoEstado(obj.getCodigoEstado()==null?"":obj.getCodigoEstado());
					obj.setPerfil(obj.getPerfil()==null?"":obj.getPerfil());
					obj.setCodigoProducto(obj.getCodigoProducto()==null?"":obj.getCodigoProducto());
					obj.setNombreProducto(obj.getNombreProducto()==null?"":obj.getNombreProducto());
					obj.setCodigoSubProducto(obj.getCodigoSubProducto()==null?"":obj.getCodigoSubProducto());
					obj.setNombreSubProducto(obj.getNombreSubProducto()==null?"":obj.getNombreSubProducto());
					obj.setCodigoOficina(obj.getCodigoOficina()==null?"":obj.getCodigoOficina());
					obj.setNombreOficina(obj.getNombreOficina()==null?"":obj.getNombreOficina());
					obj.setCodigoTerritorio(obj.getCodigoTerritorio()==null?"":obj.getCodigoTerritorio());
					obj.setNombreTerritorio(obj.getNombreTerritorio()==null?"":obj.getNombreTerritorio());
					obj.setFechaHoraLlegada(obj.getFechaHoraLlegada()==null?"":obj.getFechaHoraLlegada());
					obj.setFechaHoraInicioTrabajo(obj.getFechaHoraInicioTrabajo()==null?"":obj.getFechaHoraInicioTrabajo());
					obj.setFechaHoraEnvio(obj.getFechaHoraEnvio()==null?"":obj.getFechaHoraEnvio());
					obj.setTiempoEjecucionTe(obj.getTiempoEjecucionTe()==null?"":obj.getTiempoEjecucionTe());
					obj.setTiempoColaTc(obj.getTiempoColaTc()==null?"":obj.getTiempoColaTc());
					obj.setTiempoProcesoTp(obj.getTiempoProcesoTp()==null?"":obj.getTiempoProcesoTp());
					obj.setCumplioAns(obj.getCumplioAns()==null?"":obj.getCumplioAns());
					obj.setFlagDevolucion(obj.getFlagDevolucion()==null?"":obj.getFlagDevolucion());
					obj.setFlagRetraer(obj.getFlagRetraer()==null?"":obj.getFlagRetraer());
					obj.setTerminal(obj.getTerminal()==null?"":obj.getTerminal());
					obj.setObservacion(obj.getObservacion()==null?"":obj.getObservacion());
					obj.setMotivoRechazo(obj.getMotivoRechazo()==null?"":obj.getMotivoRechazo());
					obj.setComentario(obj.getComentario()==null?"":obj.getComentario());
					obj.setAns(obj.getAns()==null?"":obj.getAns());
					
					sb = new StringBuilder();
					sb.append(obj.getNroExpediente().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getNumeroRegistro().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getUsuarioActual().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getEstadoExpediente().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getCodigoEstado().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getPerfil().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getCodigoProducto().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getNombreProducto().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getCodigoSubProducto().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getNombreSubProducto().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getCodigoOficina().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getNombreOficina().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getCodigoTerritorio().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getNombreTerritorio().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getFechaHoraLlegada().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getFechaHoraInicioTrabajo().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getFechaHoraEnvio().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getTiempoEjecucionTe().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getTiempoColaTc().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getTiempoProcesoTp().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getCumplioAns().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getFlagDevolucion().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getFlagRetraer().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getTerminal().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getObservacion().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getMotivoRechazo().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getComentario().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getAns().replace(",", replaceComa));
					sb.append(",");
					String sinCaractExt = EliminarCaracterExt(sb.toString());
					writer.append(sinCaractExt);
					writer.newLine();
				}
			}
		}
		return null;
	}
	
	private String generaDatosConsolidadoCsv(BufferedWriter writer) throws IOException{
		StringBuilder sb = new StringBuilder();
		String replaceComa = " ";
		
		//Generamos los encabezados
		sb.append("NUMERO_EXPEDIENTE");
		sb.append(",");
		sb.append("CODIGO_SEGMENTO_CLIENTE");
		sb.append(",");
		sb.append("DESCRIPCION_SEGMENTO_CLIENTE");
		sb.append(",");
		sb.append("CORRELATIVO_ESTADO");
		sb.append(",");
		sb.append("NOMBRE_ESTADO");
		sb.append(",");
		sb.append("NOMBRE_PRODUCTO");
		sb.append(",");
		sb.append("CODIGO_USUARIO_CREACION");
		sb.append(",");
		sb.append("NOMBRE_USUARIO_CREACION");
		sb.append(",");
		sb.append("FECHA_CREACION");
		sb.append(",");
		sb.append("CORRELATIVO_OFICINA");
		sb.append(",");
		sb.append("CODIGO_OFICINA");
		sb.append(",");
		sb.append("NOMBRE_OFICINA");
		sb.append(",");
		sb.append("CODIGO_GARANTIA");
		sb.append(",");
		sb.append("DESCRIPCION_GARANTIA");
		sb.append(",");
		sb.append("CORRELATIVO_SUBPRODUCTO");
		sb.append(",");
		sb.append("NOMBRE_SUBPRODUCTO");
		sb.append(",");
		sb.append("CORRELATIVO_CLIENTE");
		sb.append(",");
		sb.append("APELLIDO_PATERNO_CLIENTE");
		sb.append(",");
		sb.append("APELLIDO_MATERNO_CLIENTE");
		sb.append(",");
		sb.append("NOMBRES_CLIENTE");
		sb.append(",");
		sb.append("TIPO_DOCUMENTO_IDENTIDAD");
		sb.append(",");
		sb.append("NUMERO_DOCUMENTO_IDENTIDAD");
		sb.append(",");
		sb.append("TIPO_CLIENTE");
		sb.append(",");
		sb.append("INGRESO_NETO_MENSUAL");
		sb.append(",");
		sb.append("ESTADO_CIVIL");
		sb.append(",");
		sb.append("PERSONA_EXPUESTA_PUBLICA");
		sb.append(",");
		sb.append("PAGO_HABIENTE");
		sb.append(",");
		sb.append("SUBROGADO");		
		sb.append(",");
		sb.append("TIPO_OFERTA");
		sb.append(",");
		sb.append("MONEDA_IMPORTE_SOLICITADO");
		sb.append(",");
		sb.append("IMPORTE_SOLICITADO");
		sb.append(",");
		sb.append("MONEDA_IMPORTE_APROBADO");
		sb.append(",");
		sb.append("IMPORTE_APROBADO");
		sb.append(",");
		sb.append("PLAZO_SOLICITADO");
		sb.append(",");
		sb.append("PLAZO_APROBADO");
		sb.append(",");
		sb.append("TIPO_RESOLUCION");
		sb.append(",");
		sb.append("CODIGO_PREEVALUADOR");
		sb.append(",");
		sb.append("CODIGO_RVGL");
		sb.append(",");
		sb.append("LINEA_CONSUMO");
		sb.append(",");
		sb.append("RIESGO_CLIENTE_GRUPAL");
		sb.append(",");
		sb.append("PORCENTAJE_ENDEUDAMIENTO");
		sb.append(",");
		sb.append("CODIGO_CONTRATO");
		sb.append(",");
		sb.append("GRUPO_BURO");
		sb.append(",");
		sb.append("CLASIFICACION_SBS_TITULAR");
		sb.append(",");
		sb.append("CLASIFICACION_BANCO_TITULAR");
		sb.append(",");
		sb.append("CLASIFICACION_SBS_CONYUGE");
		sb.append(",");
		sb.append("CLASIFICACION_BANCO_CONYUGE");
		sb.append(",");
		sb.append("SCORING");
		sb.append(",");
		sb.append("TASA_ESPECIAL");
		sb.append(",");
		sb.append("FLAG_VERIF_DOMICILIARIA");
		sb.append(",");
		sb.append("ESTADO_VERIF_DOMICILIARIA");
		sb.append(",");
		sb.append("FLAG_VERIF_LABORAL");
		sb.append(",");
		sb.append("ESTADO_VERIF_LABORAL");
		sb.append(",");
		sb.append("FLAG_DPS");
		sb.append(",");
		sb.append("ESTADO_DPS");
		sb.append(",");
		sb.append("MODIFICAR_TASA");
		sb.append(",");
		sb.append("MODIFICAR_SCORING");
		sb.append(",");
		sb.append("INDICADOR_DELEGACION");
		sb.append(",");
		sb.append("INDICADOR_EXCLUSION_DELEGACION");
		sb.append(",");
		sb.append("NIVEL_COMPLEJIDAD");
		sb.append(",");
		sb.append("NRO_DEVOLUCIONES");
		sb.append(",");
		sb.append("CODIGO_USUARIO_ACTUAL");
		writer.append(sb);
		writer.newLine();
		
		if(listDatosGeneradosCons != null) {
			//Recorremos los datos
			for (DatosGeneradosConsVO obj : listDatosGeneradosCons) {
				if (obj != null) {
					obj.setNumeroExpediente(obj.getNumeroExpediente()==null?"":obj.getNumeroExpediente());
					obj.setCodigoSegmentoCliente(obj.getCodigoSegmentoCliente()==null?"":obj.getCodigoSegmentoCliente());
					obj.setDescripcionSegmentoCliente(obj.getDescripcionSegmentoCliente()==null?"":obj.getDescripcionSegmentoCliente());
					obj.setCorrelativoEstado(obj.getCorrelativoEstado()==null?"":obj.getCorrelativoEstado());
					obj.setNombreEstado(obj.getNombreEstado()==null?"":obj.getNombreEstado());
					obj.setNombreProducto(obj.getNombreProducto()==null?"":obj.getNombreProducto());
					obj.setCodigoUsuarioCreacion(obj.getCodigoUsuarioCreacion()==null?"":obj.getCodigoUsuarioCreacion());
					obj.setNombreUsuarioCreacion(obj.getNombreUsuarioCreacion()==null?"":obj.getNombreUsuarioCreacion());
					obj.setFechaCreacion(obj.getFechaCreacion()==null?"":obj.getFechaCreacion());
					obj.setCorrelativoOficina(obj.getCorrelativoOficina()==null?"":obj.getCorrelativoOficina());
					obj.setCodigoOficina(obj.getCodigoOficina()==null?"":obj.getCodigoOficina());
					obj.setNombreOficina(obj.getNombreOficina()==null?"":obj.getNombreOficina());
					obj.setCodigoGarantia(obj.getCodigoGarantia()==null?"":obj.getCodigoGarantia());
					obj.setDescripcionGarantia(obj.getDescripcionGarantia()==null?"":obj.getDescripcionGarantia());
					obj.setCorrelativoSubproducto(obj.getCorrelativoSubproducto()==null?"":obj.getCorrelativoSubproducto());
					obj.setNombreSubproducto(obj.getNombreSubproducto()==null?"":obj.getNombreSubproducto());
					obj.setCorrelativoCliente(obj.getCorrelativoCliente()==null?"":obj.getCorrelativoCliente());
					obj.setApellidoPaternoCliente(obj.getApellidoPaternoCliente()==null?"":obj.getApellidoPaternoCliente());
					obj.setApellidoMaternoCliente(obj.getApellidoMaternoCliente()==null?"":obj.getApellidoMaternoCliente());
					obj.setNombresCliente(obj.getNombresCliente()==null?"":obj.getNombresCliente());
					obj.setTipoDocumentoIdentidad(obj.getTipoDocumentoIdentidad()==null?"":obj.getTipoDocumentoIdentidad());
					obj.setNumeroDocumentoIdentidad(obj.getNumeroDocumentoIdentidad()==null?"":obj.getNumeroDocumentoIdentidad());
					LOG.info("NumeroDocumentoIdentidad::::"+obj.getNumeroDocumentoIdentidad());
					obj.setTipoCliente(obj.getTipoCliente()==null?"":obj.getTipoCliente());
					obj.setIngresoNetoMensual(obj.getIngresoNetoMensual()==null?"":obj.getIngresoNetoMensual());
					obj.setEstadoCivil(obj.getEstadoCivil()==null?"":obj.getEstadoCivil());
					obj.setPersonaExpuestaPublica(obj.getPersonaExpuestaPublica()==null?"":obj.getPersonaExpuestaPublica());
					obj.setPagoHabiente(obj.getPagoHabiente()==null?"":obj.getPagoHabiente());
					obj.setSubrogado(obj.getSubrogado()==null?"":obj.getSubrogado());
					obj.setTipoOferta(obj.getTipoOferta()==null?"":obj.getTipoOferta());
					obj.setMonedaImporteSolicitado(obj.getMonedaImporteSolicitado()==null?"":obj.getMonedaImporteSolicitado());
					obj.setImporteSolicitado(obj.getImporteSolicitado()==null?"":obj.getImporteSolicitado());
					obj.setMonedaImporteAprobado(obj.getMonedaImporteAprobado()==null?"":obj.getMonedaImporteAprobado());
					obj.setImporteAprobado(obj.getImporteAprobado()==null?"":obj.getImporteAprobado());
					obj.setPlazoSolicitado(obj.getPlazoSolicitado()==null?"":obj.getPlazoSolicitado());
					obj.setPlazoAprobado(obj.getPlazoAprobado()==null?"":obj.getPlazoAprobado());
					obj.setTipoResolucion(obj.getTipoResolucion()==null?"":obj.getTipoResolucion());
					obj.setCodigoPreevaluador(obj.getCodigoPreevaluador()==null?"":obj.getCodigoPreevaluador());
					obj.setCodigoRvgl(obj.getCodigoRvgl()==null?"":obj.getCodigoRvgl());
					obj.setLineaConsumo(obj.getLineaConsumo()==null?"":obj.getLineaConsumo());
					obj.setRiesgoClienteGrupal(obj.getRiesgoClienteGrupal()==null?"":obj.getRiesgoClienteGrupal());
					obj.setPorcentajeEndeudamiento(obj.getPorcentajeEndeudamiento()==null?"":obj.getPorcentajeEndeudamiento());
					obj.setCodigoContrato(obj.getCodigoContrato()==null?"":obj.getCodigoContrato());
					obj.setGrupoBuro(obj.getGrupoBuro()==null?"":obj.getGrupoBuro());
					obj.setClasificacionSbsTitular(obj.getClasificacionSbsTitular()==null?"":obj.getClasificacionSbsTitular());
					obj.setClasificacionBancoTitular(obj.getClasificacionBancoTitular()==null?"":obj.getClasificacionBancoTitular());
					obj.setClasificacionSbsConyuge(obj.getClasificacionSbsConyuge()==null?"":obj.getClasificacionSbsConyuge());
					obj.setClasificacionBancoConyuge(obj.getClasificacionBancoConyuge()==null?"":obj.getClasificacionBancoConyuge());
					obj.setScoring(obj.getScoring()==null?"":obj.getScoring());
					obj.setTasaEspecial(obj.getTasaEspecial()==null?"":obj.getTasaEspecial());
					obj.setFlagVerifDomiciliaria(obj.getFlagVerifDomiciliaria()==null?"":obj.getFlagVerifDomiciliaria());
					obj.setEstadoVerifDomiciliaria(obj.getEstadoVerifDomiciliaria()==null?"":obj.getEstadoVerifDomiciliaria());
					obj.setFlagVerifLaboral(obj.getFlagVerifLaboral()==null?"":obj.getFlagVerifLaboral());
					obj.setEstadoVerifLaboral(obj.getEstadoVerifLaboral()==null?"":obj.getEstadoVerifLaboral());
					obj.setFlagDps(obj.getFlagDps()==null?"":obj.getFlagDps());
					obj.setEstadoDps(obj.getEstadoDps()==null?"":obj.getEstadoDps());
					obj.setModificarTasa(obj.getModificarTasa()==null?"":obj.getModificarTasa());
					obj.setModificarScoring(obj.getModificarScoring()==null?"":obj.getModificarScoring());
					obj.setIndicadorDelegacion(obj.getIndicadorDelegacion()==null?"":obj.getIndicadorDelegacion());
					obj.setIndicadorExclusionDelegacion(obj.getIndicadorExclusionDelegacion()==null?"":obj.getIndicadorExclusionDelegacion());
					obj.setNivelComplejidad(obj.getNivelComplejidad()==null?"":obj.getNivelComplejidad());
					obj.setNroDevoluciones(obj.getNroDevoluciones()==null?"":obj.getNroDevoluciones());
					obj.setCodigoUsuarioActual(obj.getCodigoUsuarioActual()==null?"":obj.getCodigoUsuarioActual());

					sb = new StringBuilder();
					sb.append(obj.getNumeroExpediente().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getCodigoSegmentoCliente().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getDescripcionSegmentoCliente().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getCorrelativoEstado().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getNombreEstado().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getNombreProducto().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getCodigoUsuarioCreacion().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getNombreUsuarioCreacion().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getFechaCreacion().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getCorrelativoOficina().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getCodigoOficina().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getNombreOficina().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getCodigoGarantia().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getDescripcionGarantia().replace(",",replaceComa));
					sb.append(",");
					sb.append(obj.getCorrelativoSubproducto().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getNombreSubproducto().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getCorrelativoCliente().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getApellidoPaternoCliente().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getApellidoMaternoCliente().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getNombresCliente().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getTipoDocumentoIdentidad().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getNumeroDocumentoIdentidad().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getTipoCliente().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getIngresoNetoMensual().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getEstadoCivil().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getPersonaExpuestaPublica().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getPagoHabiente().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getSubrogado().replace(",", replaceComa));
					sb.append(",");					
					sb.append(obj.getTipoOferta().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getMonedaImporteSolicitado().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getImporteSolicitado().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getMonedaImporteAprobado().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getImporteAprobado().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getPlazoSolicitado().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getPlazoAprobado().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getTipoResolucion().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getCodigoPreevaluador().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getCodigoRvgl().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getLineaConsumo().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getRiesgoClienteGrupal().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getPorcentajeEndeudamiento().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getCodigoContrato().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getGrupoBuro().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getClasificacionSbsTitular().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getClasificacionBancoTitular().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getClasificacionSbsConyuge().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getClasificacionBancoConyuge().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getScoring().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getTasaEspecial().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getFlagVerifDomiciliaria().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getEstadoVerifDomiciliaria().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getFlagVerifLaboral().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getEstadoVerifLaboral().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getFlagDps().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getEstadoDps().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getModificarTasa().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getModificarScoring().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getIndicadorDelegacion().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getIndicadorExclusionDelegacion().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getNivelComplejidad().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getNroDevoluciones().replace(",", replaceComa));
					sb.append(",");
					sb.append(obj.getCodigoUsuarioActual().replace(",", replaceComa));
					sb.append(",");
					String sinCaractExt = EliminarCaracterExt(sb.toString());
					writer.append(sinCaractExt);
					writer.newLine();
				}
			}
		}
		return null;
	}
	
	private String generaDatosConsolidadoXls(Sheet sheet, HSSFWorkbook wb, int numFilaTitulo1) throws IOException{
		StringBuilder sb = new StringBuilder();
		String replaceComa = " ";
				
		//Generamos los encabezados
		AyudaDatosReporte objAyudaDatosReporte=new AyudaDatosReporte();
	
  		
		Row titCabRow = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1 + 3);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 0, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "NUMERO_EXPEDIENTE", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CODIGO_SEGMENTO_CLIENTE", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 2, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "DESCRIPCION_SEGMENTO_CLIENTE", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 3, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CORRELATIVO_ESTADO", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 4, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "NOMBRE_ESTADO", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 5, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "NOMBRE_PRODUCTO", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 6, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CODIGO_USUARIO_CREACION", true, true, 0, 200);	    
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 7, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "NOMBRE_USUARIO_CREACION", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 8, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "FECHA_CREACION", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 9, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CORRELATIVO_OFICINA", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CODIGO_OFICINA", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 11, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "NOMBRE_OFICINA", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 12, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CODIGO_GARANTIA", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 13, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "DESCRIPCION_GARANTIA", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 14, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CORRELATIVO_SUBPRODUCTO", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 15, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "NOMBRE_SUBPRODUCTO", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 16, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CORRELATIVO_CLIENTE", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 17, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "APELLIDO_PATERNO_CLIENTE", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 18, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "APELLIDO_MATERNO_CLIENTE", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 19, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "NOMBRES_CLIENTE", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 20, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "TIPO_DOCUMENTO_IDENTIDAD", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 21, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "NUMERO_DOCUMENTO_IDENTIDAD", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 22, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "TIPO_CLIENTE", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 23, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "INGRESO_NETO_MENSUAL", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 24, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "ESTADO_CIVIL", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 25, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "PERSONA_EXPUESTA_PUBLICA", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 26, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "PAGO_HABIENTE", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 27, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "SUBROGADO", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 28, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "TIPO_OFERTA", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 29, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "MONEDA_IMPORTE_SOLICITADO", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 30, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "IMPORTE_SOLICITADO", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 31, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "MONEDA_IMPORTE_APROBADO", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 32, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "IMPORTE_APROBADO", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 33, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "PLAZO_SOLICITADO", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 34, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "PLAZO_APROBADO", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 35, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "TIPO_RESOLUCION", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 36, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CODIGO_PREEVALUADOR", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 37, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CODIGO_RVGL", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 38, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "LINEA_CONSUMO", true, true, 0, 200);	    
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 39, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "RIESGO_CLIENTE_GRUPAL", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 40, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "PORCENTAJE_ENDEUDAMIENTO", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 41, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CODIGO_CONTRATO", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 42, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "GRUPO_BURO", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 43, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CLASIFICACION_SBS_TITULAR", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 44, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CLASIFICACION_BANCO_TITULAR", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 45, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CLASIFICACION_SBS_CONYUGE", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 46, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CLASIFICACION_BANCO_CONYUGE", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 47, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "SCORING", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 48, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "TASA_ESPECIAL", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 49, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "FLAG_VERIF_DOMICILIARIA", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 50, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "ESTADO_VERIF_DOMICILIARIA", true, true, 0, 200);	    
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 51, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "FLAG_VERIF_LABORAL", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 52, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "ESTADO_VERIF_LABORAL", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 53, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "FLAG_DPS", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 54, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "ESTADO_DPS", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 55, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "MODIFICAR_TASA", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 56, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "MODIFICAR_SCORING", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 57, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "INDICADOR_DELEGACION", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 58, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "INDICADOR_EXCLUSION_DELEGACION", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 59, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "NIVEL_COMPLEJIDAD", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 60, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "NRO_DEVOLUCIONES", true, true, 0, 200);
	    objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 61, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CODIGO_USUARIO_ACTUAL", true, true, 0, 200);
	    	   
	    int totalColumns = 62;
		int numFilaData = 5;
	    
		if(listDatosGeneradosCons != null) {
			//Recorremos los datos
			HSSFFont cellFont = wb.createFont();
			for (DatosGeneradosConsVO obj : listDatosGeneradosCons) {
				if (obj != null) {
					
					Row rowData = objAyudaDatosReporte.crearFila(sheet, wb, numFilaData );
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 0, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getNumeroExpediente());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCodigoSegmentoCliente());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 2, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getDescripcionSegmentoCliente());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 3, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCorrelativoEstado());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 4, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getNombreEstado());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 5, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getNombreProducto());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 6, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCodigoUsuarioCreacion());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 7, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getNombreUsuarioCreacion());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 8, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getFechaCreacion());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 9, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCorrelativoOficina());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCodigoOficina());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 11, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getNombreOficina());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 12, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCodigoGarantia());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 13, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getDescripcionGarantia());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 14, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCorrelativoSubproducto());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 15, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getNombreSubproducto());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 16, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCorrelativoCliente());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 17, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getApellidoPaternoCliente());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 18, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getApellidoMaternoCliente());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 19, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getNombresCliente());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 20, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getTipoDocumentoIdentidad());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 21, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getNumeroDocumentoIdentidad());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 22, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getTipoCliente());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 23, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getIngresoNetoMensual());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 24, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getEstadoCivil());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 25, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getPersonaExpuestaPublica());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 26, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getPagoHabiente());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 27, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getSubrogado());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 28, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getTipoOferta());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 29, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getMonedaImporteSolicitado());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 30, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getImporteSolicitado());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 31, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getMonedaImporteAprobado());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 32, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getImporteAprobado());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 33, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getPlazoSolicitado());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 34, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getPlazoAprobado());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 35, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getTipoResolucion());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 36, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCodigoPreevaluador());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 37, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCodigoRvgl());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 38, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getLineaConsumo());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 39, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getRiesgoClienteGrupal());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 40, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getPorcentajeEndeudamiento());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 41, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCodigoContrato());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 42, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getGrupoBuro());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 43, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getClasificacionSbsTitular());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 44, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getClasificacionBancoTitular());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 45, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getClasificacionSbsConyuge());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 46, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getClasificacionBancoConyuge());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 47, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getScoring());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 48, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getTasaEspecial());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 49, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getFlagVerifDomiciliaria());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 50, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getEstadoVerifDomiciliaria());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 51, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getFlagVerifLaboral());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 52, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getEstadoVerifLaboral());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 53, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getFlagDps());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 54, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getEstadoDps());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 55, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getModificarTasa());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 56, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getModificarScoring());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 57, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getIndicadorDelegacion());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 58, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getIndicadorExclusionDelegacion());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 59, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getNivelComplejidad());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 60, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getNroDevoluciones());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 61, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCodigoUsuarioActual());
				    
				    numFilaData++;				    
		        }
			}
		}
		
  		for(int i=0; i<totalColumns; i++){
  			sheet.autoSizeColumn(i);
  		}
		return null;
	}
		
	
	private String generarDatosHistoricoXls(Sheet sheet, HSSFWorkbook wb, int numFilaTitulo1) throws IOException{
		
		int totalColumns = 26;
				
		//Generamos los encabezados
		AyudaDatosReporte objAyudaDatosReporte=new AyudaDatosReporte();	
  		
		Row titCabRow = objAyudaDatosReporte.crearFila(sheet, wb, numFilaTitulo1 + 3);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 0, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "NRO_EXPEDIENTE", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "NUMERO_REGISTRO", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 2, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "USUARIO_ACTUAL", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 3, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "ESTADO_EXPEDIENTE", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 4, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CODIGO_ESTADO", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 5, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "PERFIL", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 6, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "TAREA", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 7, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "ACCION", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 8, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CODIGO_PRODUCTO", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 9, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "NOMBRE_PRODUCTO", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "NOMBRE_TIPO_OFERTA", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 11, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CODIGO_SUB_PRODUCTO", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 12, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "NOMBRE_SUB_PRODUCTO", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 13, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CODIGO_OFICINA", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 14, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "NOMBRE_OFICINA", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 15, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CODIGO_TERRITORIO", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 16, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "NOMBRE_TERRITORIO", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 17, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "FECHA_HORA_LLEGADA", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 18, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "FECHA_HORA_INICIO_TRABAJO", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 19, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "FECHA_HORA_ENVIO", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 20, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "TIEMPO_EJECUCION_TE", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 21, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "TIEMPO_COLA_TC", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 22, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "TIEMPO_PROCESO_TP", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 23, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "CUMPLIO_ANS", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 24, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "FLAG_DEVOLUCION", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 25, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "FLAG_RETRAER", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 26, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "TERMINAL", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 27, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "OBSERVACION", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 28, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "MOTIVO_DEVOLUCION_RECHAZO", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 29, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "COMENTARIO_DEVOLUCION_RECHAZO", true, true, 0, 200);
		objAyudaDatosReporte.crearCeldaTitulo(wb, titCabRow, 30, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, "ANS", true, true, 0, 200);
		int numFilaData = 5;

		if(listDatosGeneradosHis != null) {
			//Recorremos los datos
			HSSFFont cellFont = wb.createFont();
			for (DatosGeneradosHisVO obj : listDatosGeneradosHis) {
				if (obj != null) {

					Row rowData = objAyudaDatosReporte.crearFila(sheet, wb, numFilaData );
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 0, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getNroExpediente());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getNumeroRegistro());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 2, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getUsuarioActual());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 3, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getEstadoExpediente());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 4, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCodigoEstado());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 5, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getPerfil());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 6, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getTarea());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 7, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getAccion());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 8, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCodigoProducto());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 9, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getNombreProducto());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 10, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getNombreTipoOferta());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 11, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCodigoSubProducto());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 12, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getNombreSubProducto());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 13, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCodigoOficina());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 14, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getNombreOficina());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 15, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCodigoTerritorio());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 16, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getNombreTerritorio());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 17, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getFechaHoraLlegada());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 18, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getFechaHoraInicioTrabajo());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 19, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getFechaHoraEnvio());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 20, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getTiempoEjecucionTe());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 21, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getTiempoColaTc());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 22, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getTiempoProcesoTp());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 23, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getCumplioAns());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 24, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getFlagDevolucion());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 25, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getFlagRetraer());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 26, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getTerminal());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 27, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getComentario());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 28, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getMotivoRechazo());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 29, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getObservacion());
				    objAyudaDatosReporte.crearCeldaData(cellFont,wb, rowData, 30, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, obj.getAns());
				    
				    numFilaData++;
		        }
			}
		}
		
  		for(int i=0; i<totalColumns; i++){
  			/*
  			if(i==28){
  				//pixeles=364
  				double width = convertPixelsToColumnUnits(364);
  				width = EXCEL_LEADING + (width * EXCEL_FACTOR);
  				sheet.setColumnWidth(i, 5000);  				
  			}*/
  				
  			sheet.autoSizeColumn(i);
  		}
		return null;
	}
	
	public static double convertPixelsToColumnUnits(double numberOfPixels) {
		numberOfPixels -= PADDING;
		double last = Math.floor(numberOfPixels / CHARACTER_WIDTH);
		
		while (convertColumnUnitsToPixels(last) < numberOfPixels)
			last++;
		numberOfPixels -= (ADJUST_START - (ADJUST_PER_UNIT * last));
		
		return numberOfPixels / CHARACTER_WIDTH;
	}
	
	public static double convertColumnUnitsToPixels(double numberOfUnits) {
		return (numberOfUnits * CHARACTER_WIDTH) + PADDING + (ADJUST_START -

			(ADJUST_PER_UNIT * numberOfUnits)); 
	}
	
	
	/**
	 * Otros metodos de reporte Historico-Consolidado
	 * */
	private String generaTituloReporte(BufferedWriter writer, int totColumna, String sTitulo, int posTitulo, String sRango, int posRango) throws IOException{
		//Titulo reporte
		StringBuilder sb = new StringBuilder();
		for(int i=1; i<=totColumna; i++) {
			if(i==posTitulo) {
				sb.append(sTitulo);
			}else {
				sb.append("");
			}
			sb.append(",");
		}
		writer.append(sb);
		writer.newLine();
		
		//Titulo rango
		sb = new StringBuilder();
		for(int i=1; i<=totColumna; i++) {
			if(i==posRango) {
				sb.append(sRango);
			}else {
				sb.append("");
			}
			sb.append(",");
		}
		writer.append(sb);
		writer.newLine();
		writer.newLine();
		return null;
	}
	private static String EliminarCaracterExt(String input) {
	    // Cadena de caracteres original a sustituir.
	    String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
	    // Cadena de caracteres ASCII que reemplazarán los originales.
	    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
	    String output = input;
	    for (int i=0; i<original.length(); i++) {
	        // Reemplazamos los caracteres especiales.
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }
	    //Eliminamos los saltos de lineas
	    output = output.replaceAll("[\n\r]"," ");
	    return output;
	}
	
	/**
	 * Otros metodos de reporte TOE
	 * */
	
	public void setearValoresListas(AyudaDatosReporte objAyuda){

		this.listaPerfiles=objAyuda.getGenerar_listaPerfiles();
		this.listaRoles=objAyuda.getGenerar_listaRoles();
		this.listaTiempo_tc_te=objAyuda.getListaTiempo_tc_te();
		this.listaReporte_Prod_TipoOferta_Flujo=objAyuda.getListaReporte_Prod_TipoOferta_Flujo();
		this.listPorc_Prod_TipOferta_Flujo=objAyuda.getListPorc_Prod_TipOferta_Flujo();
		this.listUnid_Prod_TipOferta_Flujo = objAyuda.getListUnid_Prod_TipOferta_Flujo();
	}//
	
	public void setearValoresListasEspecifico(AyudaDatosReporte objAyuda){

		this.listaPerfiles=objAyuda.obtListaPerfilTxt();
		this.listaRoles=objAyuda.obtListaEspecifica();
		this.listaTiempo_tc_te=objAyuda.cargarListaTiempo_tc_Te_Especificos();
		this.listaReporte_Prod_TipoOferta_Flujo=objAyuda.getListaReporte_Prod_TipoOferta_Flujo();
		LOG.info("listaReporte_Prod_TipoOferta_Flujo tamano:::"+listaReporte_Prod_TipoOferta_Flujo.size());
		this.listPorc_Prod_TipOferta_Flujo=objAyuda.getListPorc_Prod_TipOferta_Flujo();
		//this.producto=
		//this.tipoOferta=
	}
	
	/**
	 * Devuelve el titulo de cada tabla.
	 * */
	
	private String getTituloTabla(int indice,AyudaDatosReporte objAyudaDatosReporte){
		switch (indice) {
		case 0:
			objAyudaDatosReporte.setTituloTabla1(
			objAyudaDatosReporte.obtTituloEncabezadoTablas(true,Constantes.DESCRIPCION_TIPO_PRODUCTO_TC, 
					Constantes.DESCRIPCION_TIPO_OFERTA_APROBADO, Constantes.DESCRIPCION_TIPO_FLUJO_REGULAR));
			return objAyudaDatosReporte.getTituloTabla1();
		case 1:
			objAyudaDatosReporte.setTituloTabla2(
					objAyudaDatosReporte.obtTituloEncabezadoTablas(true,Constantes.DESCRIPCION_TIPO_PRODUCTO_TC, 
					Constantes.DESCRIPCION_TIPO_OFERTA_APROBADO, Constantes.DESCRIPCION_TIPO_FLUJO_REPROCESO));	
			return objAyudaDatosReporte.getTituloTabla2();
		case 2:
			objAyudaDatosReporte.setTituloTabla3(
					objAyudaDatosReporte.obtTituloEncabezadoTablas(true,Constantes.DESCRIPCION_TIPO_PRODUCTO_TC, 
					Constantes.DESCRIPCION_TIPO_OFERTA_REGULAR, Constantes.DESCRIPCION_TIPO_FLUJO_REGULAR));
	
			return objAyudaDatosReporte.getTituloTabla3();
		case 3:
			objAyudaDatosReporte.setTituloTabla4(
					objAyudaDatosReporte.obtTituloEncabezadoTablas(true,Constantes.DESCRIPCION_TIPO_PRODUCTO_TC, 
					Constantes.DESCRIPCION_TIPO_OFERTA_REGULAR, Constantes.DESCRIPCION_TIPO_FLUJO_REPROCESO));
			return objAyudaDatosReporte.getTituloTabla4();
		case 4:
			objAyudaDatosReporte.setTituloTabla5(
					objAyudaDatosReporte.obtTituloEncabezadoTablas(true,Constantes.DESCRIPCION_TIPO_PRODUCTO_PLD, 
					Constantes.DESCRIPCION_TIPO_OFERTA_APROBADO, Constantes.DESCRIPCION_TIPO_FLUJO_REGULAR));	

			return objAyudaDatosReporte.getTituloTabla5();
		case 5:
			objAyudaDatosReporte.setTituloTabla6(
					objAyudaDatosReporte.obtTituloEncabezadoTablas(true,Constantes.DESCRIPCION_TIPO_PRODUCTO_PLD, 
					Constantes.DESCRIPCION_TIPO_OFERTA_APROBADO, Constantes.DESCRIPCION_TIPO_FLUJO_REPROCESO));
			return objAyudaDatosReporte.getTituloTabla6();
		case 6:
			objAyudaDatosReporte.setTituloTabla7(
					objAyudaDatosReporte.obtTituloEncabezadoTablas(true,Constantes.DESCRIPCION_TIPO_PRODUCTO_PLD, 
					Constantes.DESCRIPCION_TIPO_OFERTA_REGULAR, Constantes.DESCRIPCION_TIPO_FLUJO_REGULAR));

			return objAyudaDatosReporte.getTituloTabla7();
		case 7:
			objAyudaDatosReporte.setTituloTabla8(
					objAyudaDatosReporte.obtTituloEncabezadoTablas(true,Constantes.DESCRIPCION_TIPO_PRODUCTO_PLD, 
					Constantes.DESCRIPCION_TIPO_OFERTA_REGULAR, Constantes.DESCRIPCION_TIPO_FLUJO_REPROCESO));	
			return objAyudaDatosReporte.getTituloTabla8();
		default : 
			objAyudaDatosReporte.setTituloTabla8(
					objAyudaDatosReporte.obtTituloEncabezadoTablas(true,Constantes.DESCRIPCION_TIPO_PRODUCTO_PLD, 
					Constantes.DESCRIPCION_TIPO_OFERTA_REGULAR, Constantes.DESCRIPCION_TIPO_FLUJO_REPROCESO));	
			return objAyudaDatosReporte.getTituloTabla8();
		}
	}
}
