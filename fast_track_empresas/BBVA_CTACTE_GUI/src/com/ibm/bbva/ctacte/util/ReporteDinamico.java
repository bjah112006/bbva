package com.ibm.bbva.ctacte.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.ctacte.enums.EnmTipoArchivo;



public class ReporteDinamico implements Serializable{
	 private static final long serialVersionUID = 4957174451708371281L;
	   
	    private static final Logger LOG = LoggerFactory.getLogger(ReporteDinamico.class);
	    private String titulo;
	    private String subtitulo;
	    private String tituloTabla;
	    private Integer cantidadColumnas;
	    private String rutaPlantilla;
	    private String plantilla;
	    private List<ColumnaDinamica> columnas;
	    private String tipo;
	    private String  motivo;
	    private String  nombreArchivo;
	    private int maximoAncho;
	    private Map<String, Object> parametros;
	    private URL imgURL;

	    public ReporteDinamico() {
	        this.columnas = new ArrayList<ColumnaDinamica>();
	        this.parametros = new HashMap<String, Object>();
	    }

	    public void instanciarReporte(String titulo, String subtitulo, String tituloTabla, 
	    		String rutaPlantilla,  String nombreArchivo, int maximoAncho, URL imgURL, String plantilla) {
	        this.titulo = titulo;
	        this.subtitulo = subtitulo;
	        this.tituloTabla = tituloTabla;
	        this.rutaPlantilla = rutaPlantilla;
	        this.plantilla = plantilla;
	        this.nombreArchivo=nombreArchivo;
	        this.maximoAncho = maximoAncho;
	        this.imgURL = imgURL;
	        
//	        URL url = ReporteDinamico.class.getResource("/imagenes/logo.gif");
	        
	        this.parametros.put("TITULO", this.titulo);
	        this.parametros.put("SUB_TITULO", this.subtitulo);
	        this.parametros.put("CABECERA_TABLA", this.tituloTabla);
	        this.parametros.put("IMG_CABECERA", imgURL.toString());
	        
	    }

	    public void insertarColumna(ColumnaDinamica columna) {
	        this.columnas.add(columna);
	    };

	    public void insertarColumnas(List<ColumnaDinamica> columnas) {
	        this.columnas.clear();
	        this.columnas.addAll(columnas);
	    };

	    public void addParameter(String name, Object value) {
	        this.parametros.put(name, value);
	    }

	    public String generarReporte(List<?> list) {
	    	
	        int indice = 1;
	        int anchoTotal = 0;
	        int ejeX = 0;
	        String params = "";
	        String campos = "";
	        String contenido = "";
	        
	        Map<String, String> mapa;
	        
	        mapa = new HashMap<String, String>();
	        
	        for (ColumnaDinamica columna : this.columnas) {
	           
	        	this.parametros.put("COLUMNA_" + indice, columna.getNombre());
	        	 
	        	Integer anchoColumna = (this.maximoAncho * columna.getAncho() / 100);
	            anchoTotal = anchoTotal + anchoColumna;
	            
	            mapa.put("${field" + indice + "}", columna.getNombreAtributo());
	            mapa.put("${className" + indice + "}", columna.getTipoDato());
	            mapa.put("${pattern" + indice + "}", columna.getFormato());

	            mapa.put("${HEAD_COL_" + indice + "_VA}", columna.getAlineacionVertical().getTipoPosicionamiento());
	            mapa.put("${HEAD_COL_" + indice + "_TA}", columna.getAlineacionInterior().getTipoPosicionamiento());
	            mapa.put("${HEAD_COL_" + indice + "_H}", columna.getAlto().toString());
	            mapa.put("${HEAD_COL_" + indice + "_W}", anchoColumna.toString());
	            mapa.put("${HEAD_COL_" + indice + "_Y}", columna.getY().toString());
	            mapa.put("${HEAD_COL_" + indice + "_X}", String.valueOf(ejeX));

	            mapa.put("${BODY_COL_" + indice + "_VA}", columna.getAlineacionVertical().getTipoPosicionamiento());
	            mapa.put("${BODY_COL_" + indice + "_TA}", columna.getAlineacionInterior().getTipoPosicionamiento());
	            mapa.put("${BODY_COL_" + indice + "_H}", columna.getAlto().toString());
	            mapa.put("${BODY_COL_" + indice + "_W}", anchoColumna.toString());
	            mapa.put("${BODY_COL_" + indice + "_Y}", columna.getY().toString());
	            mapa.put("${BODY_COL_" + indice + "_X}", String.valueOf(ejeX));

	            params = params + "<field name=\"${field" + indice + "}\" class=\"${className" + indice + "}\"/> \n";

	            campos = campos + "<textField isStretchWithOverflow=\"true\"> \n" + "<reportElement mode=\"Opaque\" x=\"${HEAD_COL_" + indice + "_X}\" y=\"${HEAD_COL_" + indice + "_Y}\" width=\"${HEAD_COL_" + indice
	                    + "_W}\" height=\"${HEAD_COL_" + indice + "_H}\" forecolor=\"#FFFFFF\" backcolor=\"#094FA4\" uuid=\"" + UUID.randomUUID().toString() + "\" /> \n"
	                    + "<textElement textAlignment=\"${HEAD_COL_" + indice + "_TA}\" verticalAlignment=\"${HEAD_COL_" + indice + "_VA}\"/> \n" + "<textFieldExpression><![CDATA[$P{COLUMNA_" + indice
	                    + "}]]></textFieldExpression> \n" + "</textField> \n";

	            contenido = contenido + "<textField isStretchWithOverflow=\"true\" isBlankWhenNull=\"true\" ${pattern" + indice + "} > \n" + "<reportElement mode=\"Transparent\" x=\"${BODY_COL_" + indice + "_X}\" y=\"${BODY_COL_"
	                    + indice + "_Y}\" width=\"${BODY_COL_" + indice + "_W}\" height=\"${BODY_COL_" + indice + "_H}\" forecolor=\"#094FA4\" uuid=\"" + UUID.randomUUID().toString() + "\"/> \n"
	                    + "<box> \n" + "<pen lineColor=\"#00CCFF\"/> \n" + "<topPen lineWidth=\"0.5\" lineColor=\"#00CCFF\"/> \n" + "<leftPen lineWidth=\"0.5\" lineColor=\"#00CCFF\"/> \n"
	                    + "<bottomPen lineWidth=\"0.5\" lineColor=\"#00CCFF\"/> \n" + "<rightPen lineWidth=\"0.5\" lineColor=\"#00CCFF\"/> \n" + "</box> \n" + "<textElement textAlignment=\"${BODY_COL_"
	                    + indice + "_TA}\" verticalAlignment=\"${BODY_COL_" + indice + "_VA}\" markup=\"none\"> \n" + "<font  size=\"9\"/> \n" + "<paragraph lineSpacing=\"Single\"/> \n"
	                    + "</textElement>" + "<textFieldExpression><![CDATA[$F{${field" + indice + "}}]]></textFieldExpression> \n" + "</textField> \n";

	            ejeX = ejeX + anchoColumna;
	           
	            indice++;

	        }
	        
	        String tableTitle = " <textField>"
	                + "<reportElement style=\"TEXTO1\" x=\"0\" y=\"132\" width=\""+anchoTotal+"\" height=\"16\" uuid=\"0062e423-d239-440c-aa7e-0ed7e87ce787\"/>"
	                + "<textElement>"
	                + "<font size=\"13\"/>"
	                + "</textElement>"
	                + "<textFieldExpression><![CDATA[$P{CABECERA_TABLA}]]></textFieldExpression>"
	                + "</textField>";
	        
	        String documentDate = "<textField > "
	                + "<reportElement style=\"TEXTO2\" x=\"0\" y=\"116\" width=\""+anchoTotal+"\" height=\"16\" uuid=\"f3cefc83-7738-4372-9146-b8520e3c4917\"/> "
	                + "<textElement textAlignment=\"Right\"> "
	                + " <font size=\"13\"/> "
	                + "</textElement> "
	                + "<textFieldExpression><![CDATA[$P{FECHA}]]></textFieldExpression> "
	                + "</textField> ";
	        
//	        String tableTitle = " <textField>"
//	                + "<reportElement style=\"TEXTO1\" x=\"0\" y=\"132\" width=\""+anchoTotal+"\" height=\"16\" uuid=\"0062e423-d239-440c-aa7e-0ed7e87ce787\"/>"
//	                + "<textElement>"
//	                + "<font size=\"13\"/>"
//	                + "</textElement>"
//	                + "<textFieldExpression><![CDATA[$P{CABECERA_TABLA}]]></textFieldExpression>"
//	                + "</textField>";
//	        
//	        String documentDate = "<textField > "
//	                + "<reportElement style=\"TEXTO2\" x=\"0\" y=\"116\" width=\""+anchoTotal+"\" height=\"16\" uuid=\"f3cefc83-7738-4372-9146-b8520e3c4917\"/> "
//	                + "<textElement textAlignment=\"Right\"> "
//	                + " <font size=\"13\"/> "
//	                + "</textElement> "
//	                + "<textFieldExpression><![CDATA[$P{FECHA}]]></textFieldExpression> "
//	                + "</textField> ";

	        StringBuffer newContent = new StringBuffer();
	        try {

	            try {
	                File file = new File(this.rutaPlantilla + File.separator + plantilla + ".jrxml");
	                FileReader fileReader = new FileReader(file);
	                BufferedReader bufferedReader = new BufferedReader(fileReader);

	                String line;
	                while ((line = bufferedReader.readLine()) != null) {
	                    newContent.append(line);
	                    newContent.append("\n");
	                }
	                fileReader.close();
	               
	            } catch (IOException e) {
	                e.printStackTrace();
	            }

	            String content = newContent.toString();
	            content = content.replace("<tablaCabecera></tablaCabecera>", tableTitle);
	            content = content.replace("<fechaActual></fechaActual>", documentDate);
	            content = content.replace("<campos></campos>", params);
	            content = content.replace("<cabeceras></cabecera>", campos);
	            content = content.replace("<contenido></contenido>", contenido);
	            content = content.replace("anchoPagina", ""+anchoTotal+"");
	            
	            for (Map.Entry<String, String> entry : mapa.entrySet()) {
	                try {
	                    String key = entry.getKey();
	                    String value = entry.getValue();
	                    content = content.replace(key, value);
	                } catch (Exception e) {
	                    LOG.info("", e);
	                }
	            }
	            
	          

	            Calendar fecha = new GregorianCalendar();
	            int anio = fecha.get(Calendar.YEAR);
	            int mes = fecha.get(Calendar.MONTH);
	            int dia = fecha.get(Calendar.DAY_OF_MONTH);
	            int hora = fecha.get(Calendar.HOUR_OF_DAY);
	            int minuto = fecha.get(Calendar.MINUTE);
	            int segundo = fecha.get(Calendar.SECOND);
	            String sufijo = dia + "" + (mes + 1) + "" + anio + "_" + hora + "" + minuto + "" + segundo;

	            String plantillaGenerada = plantilla + "_" + sufijo;

	            File fnew = new File(this.rutaPlantilla + File.separator + plantillaGenerada + ".jrxml");
	            FileWriter f2 = new FileWriter(fnew, false);
	            f2.write(content);
	            f2.close();
	            File fjasper = null;
	            
	            JasperCompileManager.compileReportToFile(this.rutaPlantilla + File.separator + plantillaGenerada + ".jrxml", this.rutaPlantilla + File.separator + plantillaGenerada + ".jasper");

	            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	            String fechaInicioString =  df.format(new Date());
	            
	            this.parametros.put("FECHA" , "Fecha: " + fechaInicioString);
	            
	            if (this.tipo.equalsIgnoreCase(EnmTipoArchivo.PDF.getTipoArchivo())) {
	                Locale locale = new Locale("en", "US");
	                this.parametros.put(JRParameter.REPORT_LOCALE, locale);
	                byte[] bytes;
	                try {
	                    bytes = JasperExportManager.exportReportToPdf(JasperFillManager.fillReport(this.rutaPlantilla + File.separator + plantillaGenerada + ".jasper", parametros,
	                            new JRBeanCollectionDataSource(list)));
	                    
	                    if(!this.nombreArchivo.isEmpty()){
	                        plantillaGenerada = this.nombreArchivo + "_" + sufijo;
	                    }
	                    OutputStream out = new FileOutputStream(this.rutaPlantilla + File.separator + plantillaGenerada + ".pdf");
	                    out.write(bytes);
	                    out.close();
	                   
	                    fjasper = new File(this.rutaPlantilla + File.separator + plantilla + "_" + sufijo + ".jasper");
	                	
	                    return plantillaGenerada;
	                } catch (JRException e) {
	                    LOG.info("", e);
	                } finally {
	                	if(fnew!=null &&  fnew.exists()){
	                    	fnew.delete();
	                    }
	                    if(fjasper!=null && fjasper.exists()){
	                    	fjasper.delete();
	                    }
	                }
	            }
	            if (this.tipo.equalsIgnoreCase(EnmTipoArchivo.Excel.getTipoArchivo())) {
	                Locale locale = new Locale("en", "US");
	                this.parametros.put(JRParameter.REPORT_LOCALE, locale);
	                try {
	                    JasperPrint jasperPrint = JasperFillManager.fillReport(this.rutaPlantilla + File.separator + plantillaGenerada + ".jasper", parametros, new JRBeanCollectionDataSource(list));
	                    if(!this.nombreArchivo.isEmpty()){
	                        plantillaGenerada = this.nombreArchivo + "_" + sufijo;
	                    }
	                    
	                    JRXlsxExporter exporter = new JRXlsxExporter();
	                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(this.rutaPlantilla + File.separator + plantillaGenerada + ".xlsx"));
	                    SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
	                    configuration.setSheetNames(new String[]{(String) this.parametros.get("CABECERA_TABLA")});
	                    configuration.setOnePagePerSheet(false);
//	                    configuration.setMaxRowsPerSheet(10000000);
	                    
	                    configuration.setIgnorePageMargins(true);
	                    configuration.setCollapseRowSpan(true);
	                    exporter.setConfiguration(configuration);
	                    exporter.exportReport();
	                    
	                    fjasper = new File(this.rutaPlantilla + File.separator + plantilla + "_" + sufijo + ".jasper");
	                
	                    return plantillaGenerada;
	                } catch (JRException e) {
	                    LOG.info("", e);
	                } finally {
	                    if (fnew != null && fnew.exists()) {
	                        fnew.delete();
	                    }
	                    if (fjasper != null && fjasper.exists()) {
	                        fjasper.delete();
	                    }
	                }
	            }
	        } catch (Exception e2) {
	            LOG.info("", e2);
	        }
	        
	        return "";
	    }

	    public void generarReporte() {

	    }

	    public String getTitulo() {
	        return titulo;
	    }

	    public void setTitulo(String titulo) {
	        this.titulo = titulo;
	    }

	    public String getSubtitulo() {
	        return subtitulo;
	    }

	    public void setSubtitulo(String subtitulo) {
	        this.subtitulo = subtitulo;
	    }

	    public String getTituloTabla() {
	        return tituloTabla;
	    }

	    public void setTituloTabla(String tituloTabla) {
	        this.tituloTabla = tituloTabla;
	    }

	    public Integer getCantidadColumnas() {
	        return cantidadColumnas;
	    }

	    public void setCantidadColumnas(Integer cantidadColumnas) {
	        this.cantidadColumnas = cantidadColumnas;
	    }

	    public List<ColumnaDinamica> getColumnas() {
	        return columnas;
	    }

	    public void setColumnas(List<ColumnaDinamica> columnas) {
	        this.columnas = columnas;
	    }

	    public String getRutaPlantilla() {
	        return rutaPlantilla;
	    }

	    public void setRutaPlantilla(String rutaPlantilla) {
	        this.rutaPlantilla = rutaPlantilla;
	    }

	    public String getPlantilla() {
	        return plantilla;
	    }

	    public void setPlantilla(String plantilla) {
	        this.plantilla = plantilla;
	    }

	    public String getTipo() {
	        return tipo;
	    }

	    public void setTipo(String tipo) {
	        this.tipo = tipo;
	    }

	    public int getMaximoAncho() {
			return maximoAncho;
		}

		public void setMaximoAncho(int maximoAncho) {
			this.maximoAncho = maximoAncho;
		}

		public Map<String, Object> getParametros() {
			return parametros;
		}

		public void setParametros(Map<String, Object> parametros) {
			this.parametros = parametros;
		}

		public URL getImgURL() {
			return imgURL;
		}

		public void setImgURL(URL imgURL) {
			this.imgURL = imgURL;
		}

		public String getMotivo() {
	        return motivo;
	    }

	    public void setMotivo(String motivo) {
	        this.motivo = motivo;
	    }

	    public String getNombreArchivo() {
	        return nombreArchivo;
	    }

	    public void setNombreArchivo(String nombreArchivo) {
	        this.nombreArchivo = nombreArchivo;
	    }
}
