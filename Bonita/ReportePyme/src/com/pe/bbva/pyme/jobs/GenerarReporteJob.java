package com.pe.bbva.pyme.jobs;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import com.pe.bbva.pyme.model.Config;
import com.pe.bbva.pyme.model.Solicitud;
import com.pe.bbva.pyme.utils.ConstantesEnum;
import com.pe.bbva.pyme.utils.Utils;

public class GenerarReporteJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(GenerarReporteJob.class);

    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        LOG.info("Verificando hora de ejecucion...");
        try {
            // 1. Leer archivo de configuracion
            // 2. Verificar si debe lanzarse la tarea programada
            if (verificarHorarioTareaProgramada()) {
                LOG.info("Es hora de generacion de descarga...");
                // 3. Obtener datos del reporte
                // 4. Generar reporte
                generarReporteExcel();
            }
        } catch (Exception e) {
            LOG.error("Error ejecutando tarea principal:", e);
        }
    }

    private boolean verificarHorarioTareaProgramada() {
        boolean verificado = false;
        // 1. Obtenemos la frecuencia, dia y hora de ejecucion segun la configuracion.
        String frecuenciaEjecucion = BonitaClientRest.getProperty(ConstantesEnum.PARAM_FRECUENCIA_EJECUCION.getNombre());
        String diaEjecucion = ConstantesEnum.FRECUENCIA_MENSUAL.getNombre().equalsIgnoreCase(frecuenciaEjecucion) ? BonitaClientRest.getProperty("dia_ejecucion") : ConstantesEnum.PROCESO_SIN_DIA.getNombre();
        String horaEjecucion = BonitaClientRest.getProperty(ConstantesEnum.PARAM_HORA_EJECUCION.getNombre());
        // 2. Obtenemos el dia y hora del sistema
        Date date = new Date();
        DateFormat dateFormatDia = new SimpleDateFormat(ConstantesEnum.FORMATO_DIA.getNombre());
        DateFormat dateFormatHora = new SimpleDateFormat(ConstantesEnum.FORMATO_HORA.getNombre());
        String diaActual = dateFormatDia.format(date);
        String horaActual = dateFormatHora.format(date);
        // 3. Se actualiza el dia de ejecucion para casos especiales
        Integer diasMes = Utils.getDiasMesActual();
        diaEjecucion = Integer.parseInt(diaEjecucion) > diasMes ? diasMes.toString() : diaEjecucion;
        // 4. se comparan para saber si debe ejecutar la tarea programada
        if (ConstantesEnum.FRECUENCIA_DIARIA.getNombre().equalsIgnoreCase(frecuenciaEjecucion)) {
            verificado = horaEjecucion.equals(horaActual);
        } else {
            verificado = horaEjecucion.equals(horaActual) && diaEjecucion.equals(diaActual);
        }
        return verificado;
    }

    private List<Solicitud> obtenerDatosReporte(List<Config> params) throws Exception {
        List<Solicitud> listaSolicitudes = new ArrayList<Solicitud>();
        ISolicitudDAO iSolicitudDAO = new SolicitudDAOImpl();
        listaSolicitudes = iSolicitudDAO.listarSolicitudes(params);
        return listaSolicitudes;
    }

    private void generarReporteExcel() throws Exception {
        BonitaClientRest bonitaClientRest = new BonitaClientRest();
        bonitaClientRest.init();
        String path = bonitaClientRest.obtainValue(BonitaClientRest.URL_REPORTE_OUTPUT);
        List<Config> params = bonitaClientRest.obtainConfig(BonitaClientRest.CONFIGURACION_REPORTE);
        bonitaClientRest.logout();

        List<Solicitud> solicitudes = obtenerDatosReporte(params);
        
//        String fileName = path
//                .concat(File.separator)
//                .concat(BonitaClientRest.getProperty(ConstantesEnum.PARAM_NOMBRE_SALIDA.getNombre()))
//                .concat(Utils.convertirFechaActualEnCadena(ConstantesEnum.FORMATO_FECHA_CADENA.getNombre()))
//                .concat(ConstantesEnum.FORMATO_EXTENSION.getNombre());
        String fileName = "D:\\Reporte_".concat(Utils.convertirFechaActualEnCadena(ConstantesEnum.FORMATO_FECHA_CADENA.getNombre())).concat(ConstantesEnum.FORMATO_EXTENSION.getNombre());
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
        
        for (int i = 0; i < params.size(); i++) {
            sheet.setColumnWidth(i, Integer.parseInt(params.get(i).getIdReference()) * 10);
            cell = row.createCell(i);
            cell.setCellValue(params.get(i).getValColumn1());
            cell.setCellStyle(csBold);
        }
        
        Iterator<Solicitud> iterator = solicitudes.iterator();
        while (iterator.hasNext()) {
            Solicitud solicitud = iterator.next();
            row = sheet.createRow(rowIndex++);
            
            for (int i = 0; i < params.size(); i++) {
                cell = row.createCell(i);
                cell.setCellValue(solicitud.get(params.get(i).getValColumn2()));
            }
        }
        
        FileOutputStream fos = new FileOutputStream(fileName);
        workbook.write(fos);
        fos.close();
        workbook.close();
        LOG.debug(fileName + " escrito satisfactoriamente");
    }
    
    
}