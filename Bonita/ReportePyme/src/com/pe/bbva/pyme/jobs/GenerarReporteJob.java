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
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.pe.bbva.pyme.dao.ISolicitudDAO;
import com.pe.bbva.pyme.dao.impl.BonitaClientRest;
import com.pe.bbva.pyme.dao.impl.SolicitudDAOImpl;
import com.pe.bbva.pyme.model.Config;
import com.pe.bbva.pyme.model.Solicitud;
import com.pe.bbva.pyme.utils.ConstantesEnum;
import com.pe.bbva.pyme.utils.Utils;

public class GenerarReporteJob extends QuartzJobBean {

    private static final Logger LOG = LoggerFactory.getLogger(GenerarReporteJob.class);

    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOG.debug("Verificando hora de ejecucion...");
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
        try {
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
            LOG.debug("Frecuencia: " + frecuenciaEjecucion 
                    + ", Dia: " + diaEjecucion 
                    + ", Hora: " + horaEjecucion 
                    + ", Dia actual: " + diaActual 
                    + ", Hora actual: " + horaActual);
            if (ConstantesEnum.FRECUENCIA_DIARIA.getNombre().equalsIgnoreCase(frecuenciaEjecucion)) {
                verificado = horaEjecucion.equals(horaActual);
            } else {
                verificado = horaEjecucion.equals(horaActual) && diaEjecucion.equals(diaActual);
            }
        } catch(NullPointerException e) {
            LOG.info("Error al verificar si se ejecuta la tarea: (NullPointerException)");
            LOG.debug("Error ejecutando tarea principal:", e);
        } catch(Exception e) {
            LOG.error("Error ejecutando tarea principal:", e);
        }
        return verificado;
    }

    private List<Solicitud> obtenerDatosReporte(List<Config> params) throws Exception {
        List<Solicitud> listaSolicitudes = new ArrayList<Solicitud>();
        ISolicitudDAO iSolicitudDAO = new SolicitudDAOImpl();
        listaSolicitudes = iSolicitudDAO.listarSolicitudes();
        return listaSolicitudes;
    }

    private void generarReporteExcel() throws Exception {
        BonitaClientRest bonitaClientRest = new BonitaClientRest();
        bonitaClientRest.init();
        String path = bonitaClientRest.obtainValue(BonitaClientRest.URL_REPORTE_OUTPUT);
        List<Config> params = bonitaClientRest.obtainConfig(BonitaClientRest.CONFIGURACION_REPORTE);
        bonitaClientRest.logout();

        List<Solicitud> solicitudes = obtenerDatosReporte(params);
        
        String fileName = path
                .concat(File.separator)
                .concat(BonitaClientRest.getProperty(ConstantesEnum.PARAM_NOMBRE_SALIDA.getNombre()))
                .concat(Utils.convertirFechaActualEnCadena(ConstantesEnum.FORMATO_FECHA_CADENA.getNombre()))
                .concat(ConstantesEnum.FORMATO_EXTENSION.getNombre());
//        String fileName = "D:\\Reporte_".concat(Utils.convertirFechaActualEnCadena(ConstantesEnum.FORMATO_FECHA_CADENA.getNombre())).concat(ConstantesEnum.FORMATO_EXTENSION.getNombre());
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
            
            Cell cell27 = row.createCell(27);
            cell27.setCellValue(solicitud.getCentro_negocio_riesgos());
            
//            for (int i = 0; i < params.size(); i++) {
//                cell = row.createCell(i);
//                cell.setCellValue(solicitud.get(params.get(i).getValColumn2()));
//            }
        }
        
        FileOutputStream fos = new FileOutputStream(fileName);
        workbook.write(fos);
        fos.close();
        workbook.close();
        LOG.debug(fileName + " escrito satisfactoriamente");
    }
    
    
}