package com.pe.bbva.pyme.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pe.bbva.pyme.dao.ISolicitudDAO;
import com.pe.bbva.pyme.dao.impl.BonitaClientRest;
import com.pe.bbva.pyme.dao.impl.SolicitudDAOImpl;
import com.pe.bbva.pyme.model.Solicitud;
import com.pe.bbva.pyme.utils.ConstantesEnum;
import com.pe.bbva.pyme.utils.Utils;

public class GenerarReporteJob implements Job {
	
	private static final Logger LOG = LoggerFactory.getLogger(GenerarReporteJob.class);
	
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		LOG.info("Verificando hora de ejecucion...");
		try {
			//1. Leer archivo de configuracion
			Properties props = leerArchivoConfiguracion();
			//2. Verificar si debe lanzarse la tarea programada
			if(verificarHorarioTareaProgramada(props)){
				LOG.info("Es hora de generacion de descarga...");
				//3. Obtener datos del reporte
				List<Solicitud> solicitudes = obtenerDatosReporte(props);
				//4. Generar reporte
				generarReporteExcel(solicitudes,props);
				}
			}catch (Exception e) {
				LOG.error("Error ejecutando tarea principal:",e);
			}
		}
	
	private Properties leerArchivoConfiguracion() throws IOException{
		Properties props = new Properties();
		String propFileName = ConstantesEnum.NOMBRE_ARCHIVO_CONF.getNombre();		
	    String propertyHome = System.getenv(ConstantesEnum.CATALINA_HOME.getNombre());
	    String filePath = propertyHome!=null?propertyHome.concat(File.separator).concat(ConstantesEnum.CARPETA_CONF.getNombre()).concat(File.separator).concat(propFileName):ConstantesEnum.CADENA_VACIA.getNombre();
	    File archivoConfiguracion = new File(filePath);
	    if(archivoConfiguracion.exists() && !archivoConfiguracion.isDirectory()) { 
	    	props.load(new FileInputStream(archivoConfiguracion));
	    }else{
	    	InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			if (inputStream != null){
				props.load(inputStream);
			}
	    }				
		return props;
	}

	private boolean verificarHorarioTareaProgramada(Properties props) {
		boolean verificado = false;
		//1. Obtenemos la frecuencia, dia y hora de ejecucion segun la configuracion.
		String frecuenciaEjecucion = props.getProperty(ConstantesEnum.PARAM_FRECUENCIA_EJECUCION.getNombre());
		String diaEjecucion = ConstantesEnum.FRECUENCIA_MENSUAL.getNombre().equalsIgnoreCase(frecuenciaEjecucion)?props.getProperty("dia_ejecucion"):ConstantesEnum.PROCESO_SIN_DIA.getNombre();
		String horaEjecucion = props.getProperty(ConstantesEnum.PARAM_HORA_EJECUCION.getNombre());		
		//2. Obtenemos el dia y hora del sistema
		Date date = new Date();
		DateFormat dateFormatDia = new SimpleDateFormat(ConstantesEnum.FORMATO_DIA.getNombre());
		DateFormat dateFormatHora = new SimpleDateFormat(ConstantesEnum.FORMATO_HORA.getNombre());
		String diaActual = dateFormatDia.format(date);
		String horaActual = dateFormatHora.format(date);	
		//3. Se actualiza el dia de ejecucion para casos especiales
		Integer diasMes = Utils.getDiasMesActual();
		diaEjecucion = Integer.parseInt(diaEjecucion)>diasMes?diasMes.toString():diaEjecucion;
		//4. se comparan para saber si debe ejecutar la tarea programada
		if(ConstantesEnum.FRECUENCIA_DIARIA.getNombre().equalsIgnoreCase(frecuenciaEjecucion)){
			verificado = horaEjecucion.equals(horaActual);
		}else{
			verificado = horaEjecucion.equals(horaActual) && diaEjecucion.equals(diaActual);
		}
		return verificado;
	}

	private List<Solicitud> obtenerDatosReporte(Properties props) throws Exception {
		List<Solicitud> listaSolicitudes = new ArrayList<Solicitud>();		
		ISolicitudDAO iSolicitudDAO = new SolicitudDAOImpl(props); 
		listaSolicitudes = iSolicitudDAO.listarSolicitudes();				
		return listaSolicitudes;
	}

	private void generarReporteExcel(List<Solicitud> solicitudes, Properties props) throws Exception{
		//String fileName = props.getProperty(ConstantesEnum.PARAM_RUTA_SALIDA.getNombre()).concat(File.separator).concat(props.getProperty(ConstantesEnum.PARAM_NOMBRE_SALIDA.getNombre())).concat(Utils.convertirFechaActualEnCadena(ConstantesEnum.FORMATO_FECHA_CADENA.getNombre())).concat(ConstantesEnum.FORMATO_EXTENSION.getNombre());
		BonitaClientRest bonitaClientRest = new BonitaClientRest();
		String path = bonitaClientRest.obtainValue(BonitaClientRest.URL_REPORTE_OUTPUT);
		bonitaClientRest.logout();
		String fileName = path.concat(File.separator).concat(props.getProperty(ConstantesEnum.PARAM_NOMBRE_SALIDA.getNombre())).concat(Utils.convertirFechaActualEnCadena(ConstantesEnum.FORMATO_FECHA_CADENA.getNombre())).concat(ConstantesEnum.FORMATO_EXTENSION.getNombre());
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(ConstantesEnum.NOMBRE_HOJA_EXCEL.getNombre());	
		int rowIndex = 0;
	    Row row = null;
	    Cell cell = null;
	    Font bold = workbook.createFont();
	    bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
	    bold.setFontHeightInPoints((short) 11);
	    CellStyle csBold = workbook.createCellStyle();
	    csBold.setFont(bold);
	    row = sheet.createRow(rowIndex++);
	    int[ ] columnWidth = { 5000, 2500, 5000, 8000, 6000, 4000, 4800, 6000, 5000, 5000, 2500, 2500, 2500, 8000, 5000, 8000,
	    					   4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000};
	    String[ ] columnName = { ConstantesEnum.ENCABEZADO_COLUM_01.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_02.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_03.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_04.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_05.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_06.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_07.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_08.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_09.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_10.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_11.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_12.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_13.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_14.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_15.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_16.getNombre(),
	    		
	    		ConstantesEnum.ENCABEZADO_COLUM_17.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_18.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_19.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_20.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_21.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_22.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_23.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_24.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_25.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_26.getNombre(),
	    		ConstantesEnum.ENCABEZADO_COLUM_27.getNombre()};
	    for (int i = 0; i < columnWidth.length; i++) {
	    	sheet.setColumnWidth(i, columnWidth[i]);
	    	cell = row.createCell(i);
		    cell.setCellValue(columnName[i]);
		    cell.setCellStyle(csBold);
		}
		Iterator<Solicitud> iterator = solicitudes.iterator();		
		while(iterator.hasNext()){
			Solicitud solicitud = iterator.next();
            row = sheet.createRow(rowIndex++);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(solicitud.getNroSolicitud());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(solicitud.getTipoDOICliente());
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(solicitud.getNroDOICliente());
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(solicitud.getNombreCliente());
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(solicitud.getNombreTarea());
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(solicitud.getEstado());
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(solicitud.getTipoOferta());
			Cell cell7 = row.createCell(7);
			cell7.setCellValue(solicitud.getOficinaSolicitud());
			Cell cell8 = row.createCell(8);
			cell8.setCellValue(solicitud.getFechaLlegada());
			Cell cell9 = row.createCell(9);
			cell9.setCellValue(solicitud.getFechaEnvio());
			Cell cell10 = row.createCell(10);
			cell10.setCellValue(solicitud.getRolEjecutorTarea());
			Cell cell11 = row.createCell(11);
			cell11.setCellValue(solicitud.getUsuarioEjecutorTarea());
			Cell cell12 = row.createCell(12);
			cell12.setCellValue(solicitud.getNroRVGL());
			Cell cell13 = row.createCell(13);
			cell13.setCellValue(solicitud.getNroContrato());
			Cell cell14 = row.createCell(14);
			cell14.setCellValue(solicitud.getNroGarantia());
			Cell cell15 = row.createCell(15);
			cell15.setCellValue(solicitud.getDictamen());       
			
			Cell cell16 = row.createCell(16);
			cell16.setCellValue(solicitud.getProducto());
			Cell cell17 = row.createCell(17);
			cell17.setCellValue(solicitud.getCampania());
			Cell cell18 = row.createCell(18);
			cell18.setCellValue(solicitud.getClasificacion_clte());
			Cell cell19 = row.createCell(19);
			cell19.setCellValue(solicitud.getAbn_registante());
			Cell cell20 = row.createCell(20);
			cell20.setCellValue(solicitud.getNum_preimpreso());
			Cell cell21 = row.createCell(21);
			cell21.setCellValue(solicitud.getCausal_devol_gmc());
			Cell cell22 = row.createCell(22);
			cell22.setCellValue(solicitud.getCausal_clte_cancela());
			Cell cell23 = row.createCell(23);
			cell23.setCellValue(solicitud.getMoneda());
			Cell cell24 = row.createCell(24);
			cell24.setCellValue(solicitud.getMonto());
			Cell cell25 = row.createCell(25);
			cell25.setCellValue(solicitud.getPlazo());
			Cell cell26 = row.createCell(26);
			cell26.setCellValue(solicitud.getTasa());
        }
		FileOutputStream fos = new FileOutputStream(fileName);
	    workbook.write(fos);
	    fos.close();
	    workbook.close();
	    LOG.debug(fileName + " escrito satisfactoriamente");
		    	
	}	
}