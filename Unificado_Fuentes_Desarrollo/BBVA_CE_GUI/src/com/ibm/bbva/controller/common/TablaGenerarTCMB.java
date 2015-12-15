package com.ibm.bbva.controller.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.tabla.ejb.impl.TablaFacadeBean;
import com.ibm.bbva.tabla.reporte.vo.DatosGeneradosConsVO;
import com.ibm.bbva.tabla.reporte.vo.DatosGeneradosHisVO;

@SuppressWarnings("serial")
@ManagedBean(name = "tablaGenerarTC")
@RequestScoped
public class TablaGenerarTCMB {

	private static final Logger LOG = LoggerFactory.getLogger(TablaGenerarTCMB.class);
	private TablaFacadeBean tablaFacadeBean = null;
	private DatosGeneradosConsVO datosGeneradosCons;
	private DatosGeneradosHisVO datosGeneradosHis;
	private List<DatosGeneradosConsVO> listDatosGeneradosCons;
	private List<DatosGeneradosHisVO> listDatosGeneradosHis;
	private Map<String, Object[]> mapDatosGeneradosHis;
	
	private boolean viewTabla;
	
	public TablaGenerarTCMB() {
	}
	
	@PostConstruct
    public void init() {
		
	}	

	public String buscarTC(ArrayList arrayList){
		
		String sTipoReport=arrayList.get(8).toString(); //Tipo de reporte
		if(sTipoReport!=null) {
			if(sTipoReport.equals(Constantes.ID_TC_CONSOLIDADO)) {
				listDatosGeneradosCons = this.generarDatosConsolidadoTC(arrayList);
			}
			else {
				listDatosGeneradosHis = this.generarDatosHistoricoTC(arrayList);
			}
			this.viewTabla=true;
		}else {
			this.viewTabla=false;
		}
		
		return null;
	}
	
	/*Recuperar data para reporte Excel, en caso del Reporte Historico es mediante HashMap
	 * */
	public String buscarTCMap(ArrayList arrayList){
		
		String sTipoReport=arrayList.get(8).toString(); //Tipo de reporte
		if(sTipoReport!=null) {
			if(sTipoReport.equals(Constantes.ID_TC_CONSOLIDADO)) {
				listDatosGeneradosCons = this.generarDatosConsolidadoTC(arrayList);
			}
			else {
				mapDatosGeneradosHis = this.generarDatosHistoricoTCMap(arrayList);
			}
			this.viewTabla=true;
		}else {
			this.viewTabla=false;
		}
		
		return null;
	}
	
	public List<DatosGeneradosConsVO> generarDatosConsolidadoTC (ArrayList arrayList) {

		List<DatosGeneradosConsVO> listDatosGeneradosCons=null;
		
		if (this.tablaFacadeBean == null) {
			this.tablaFacadeBean = new TablaFacadeBean();
		}	
	
		if(arrayList!=null){
			listDatosGeneradosCons=tablaFacadeBean.getGenerarDatosConsolidadoTC(arrayList);
		}
		return listDatosGeneradosCons;
	}

	public List<DatosGeneradosHisVO> generarDatosHistoricoTC (ArrayList arrayList) {

		List<DatosGeneradosHisVO> listDatosGeneradosHis=null;
		
		if (this.tablaFacadeBean == null) {
			this.tablaFacadeBean = new TablaFacadeBean();
		}	
	
		if(arrayList!=null){
			listDatosGeneradosHis=tablaFacadeBean.getGenerarDatosHistoricoTC(arrayList);
		}
		return listDatosGeneradosHis;
	}
	
	/*Generacion de Datos Historicos para Reporte mediante HashMap
	 * */
	public Map<String, Object[]> generarDatosHistoricoTCMap (ArrayList arrayList) {

		Map<String, Object[]> mapDatosGeneradosHisDTO=null;
		
		if (this.tablaFacadeBean == null) {
			this.tablaFacadeBean = new TablaFacadeBean();
		}	
	
		if(arrayList!=null){
			mapDatosGeneradosHisDTO=tablaFacadeBean.getGenerarDatosHistoricoTCMap(arrayList);
		}
		return mapDatosGeneradosHisDTO;
	}
	
	
	public boolean getViewTabla() {
		return this.viewTabla;
	}
	
	public void setViewTabla(boolean viewTabla) {
		this.viewTabla = viewTabla;
	}
	
	public List<DatosGeneradosConsVO> getListDatosGeneradosCons() {
		return this.listDatosGeneradosCons;
	}
	
	public void setListDatosGeneradosCons(List<DatosGeneradosConsVO> listDatosGeneradosCons) {
		this.listDatosGeneradosCons = listDatosGeneradosCons;
	}
	
	public List<DatosGeneradosHisVO> getListDatosGeneradosHis() {
		return this.listDatosGeneradosHis;
	}
	
	public void setListDatosGeneradosHis(List<DatosGeneradosHisVO> listDatosGeneradosHis) {
		this.listDatosGeneradosHis = listDatosGeneradosHis;
	}
	
	/***********************************************************************************************************************
	 ************************************************************************************************************************
	 ***********************************************************************************************************************
	 * AYUDA GENERACION EXCEL
	 * REPORTE TOE
	 * ABRIL 2014
	 ***********************************************************************************************************************
	 ************************************************************************************************************************
	 ************************************************************************************************************************
	 * **/
	
	public void pintarTitulos(Sheet sheet, int firsRow, int lastRow, int firstCol, int lastCol){
        sheet.addMergedRegion(new CellRangeAddress(lastRow, lastRow, firstCol, lastCol));
	}
	
	public void pintarTitulos(SXSSFSheet sheet, int firsRow, int lastRow, int firstCol, int lastCol){
        sheet.addMergedRegion(new CellRangeAddress(lastRow, lastRow, firstCol, lastCol));
	}
	
	public Row crearFila(Sheet sheet, HSSFWorkbook wb, int numFila){
		Row tmpRow =sheet.createRow((short) numFila);
		return tmpRow;		
	}
	
	public Row crearFila(SXSSFSheet sheet, SXSSFWorkbook wb, int numFila){
		Row tmpRow =sheet.createRow((short) numFila);
		return tmpRow;		
	}
	
	public void createTituloCell(HSSFWorkbook wb, Row row, int column, 
			short halign, short valign,String strContenido) {

	    CreationHelper ch = wb.getCreationHelper();
	        Cell cell = row.createCell(column);
	        cell.setCellValue(ch.createRichTextString(strContenido));
	       
	        HSSFFont cellFont = wb.createFont();
	        cellFont.setFontHeightInPoints((short)9);
	        cellFont.setFontName(HSSFFont.FONT_ARIAL);
	        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	       
	        CellStyle cellStyle = wb.createCellStyle();
	        cellStyle.setAlignment(halign);
	        cellStyle.setVerticalAlignment(valign);
	        cellStyle.setFont(cellFont);
	        cell.setCellStyle(cellStyle);
	    }
	
	public void createTituloCell(SXSSFWorkbook wb, Row row, int column, 
			short halign, short valign,String strContenido) {

	    	CreationHelper ch = wb.getCreationHelper();
	        Cell cell = row.createCell(column);
	        cell.setCellValue(ch.createRichTextString(strContenido));
	       
	        Font cellFont = wb.createFont();
	        cellFont.setFontHeightInPoints((short)9);
	        cellFont.setFontName(HSSFFont.FONT_ARIAL);
	        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	       
	        XSSFCellStyle cellStyle = (XSSFCellStyle) wb.createCellStyle();
	        cellStyle.setAlignment(halign);
	        cellStyle.setVerticalAlignment(valign);
	        cellStyle.setFont(cellFont);
	        cell.setCellStyle(cellStyle);
	    }
	

	public void createDataCell(HSSFWorkbook wb, Row row, int column, 
			short halign, short valign,String strContenido) {

	    CreationHelper ch = wb.getCreationHelper();
	        Cell cell = row.createCell(column);
	        cell.setCellValue(ch.createRichTextString(strContenido));
	       
	        HSSFFont cellFont = wb.createFont();
	        cellFont.setFontHeightInPoints((short)9);
	        cellFont.setFontName(HSSFFont.FONT_ARIAL);
	        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	       
	        CellStyle cellStyle = wb.createCellStyle();
	        cellStyle.setAlignment(halign);
	        cellStyle.setVerticalAlignment(valign);
	        cellStyle.setFont(cellFont);
	        cell.setCellStyle(cellStyle);
	    }
	
	public Map<String, Object[]> getMapDatosGeneradosHis() {
		return mapDatosGeneradosHis;
	}

	public void setMapDatosGeneradosHis(Map<String, Object[]> mapDatosGeneradosHis) {
		this.mapDatosGeneradosHis = mapDatosGeneradosHis;
	}
	
}