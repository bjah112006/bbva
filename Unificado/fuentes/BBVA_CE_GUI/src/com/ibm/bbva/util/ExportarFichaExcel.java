
package com.ibm.bbva.util;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.ibm.bbva.tabla.reporte.vo.DatosGeneradosVO;


public class ExportarFichaExcel extends VisualizacionInterfaz{
    private List<DatosGeneradosVO> lstContactoFichaVO=new ArrayList<DatosGeneradosVO>();
    private String pathApp;
    public static final String DIRECTORIO_EXCEL = "C:\\";
    public static final String NOMBRE_EXCEL_TEMPORAL = "pruebaExcel";
    public static final String RUTA_EXCEL = "C:\\pruebaExcel.xls";
    private String[] columnas = {"OBJ","TC","TE"};
    
    public ExportarFichaExcel(){}

    public ExportarFichaExcel(List<DatosGeneradosVO> listDatosGeneradosVO) {
        try{
            lstContactoFichaVO = listDatosGeneradosVO;
            System.out.println("ExportarFichaExcel");

            file = Util.guardarArchivoEnRutaReturnFile(RUTA_EXCEL,
                                      this.devuelveArregloColumnas(),
                                      this.devuelveArregloDatos());
            
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private ArrayList devuelveArregloColumnas(){
        ArrayList list = new ArrayList();
        for(String columna:this.columnas){
            list.add(columna);
        }
        return list;
    }
    private ArrayList devuelveArregloDatos(){
        ArrayList list = new ArrayList();
        ArrayList listTotal = new ArrayList();
        if(lstContactoFichaVO.size()>0 || lstContactoFichaVO!=null){
            for(int i=0; i<this.lstContactoFichaVO.size();i++){
            	DatosGeneradosVO c = this.lstContactoFichaVO.get(i);
                list = new ArrayList();
                //list.add("" + c.getConcepto());
                list.add("" + c.getTc());
                list.add("" + c.getTe());
                listTotal.add(list);
            }
        }
        return listTotal;
    }

    public void abrirExcel(HttpServletResponse response)  {
         byte[] bytes=null;
         try{
            response.setHeader("Content-disposition", "attachment; filename=fichasExportar.xls");
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            /*PONER EN EL SERVICE*/
            /*String path = "c:\\";
            File folder = new File(path);*/
            File file = new File(RUTA_EXCEL);
            InputStream is = new FileInputStream(file);
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                throw new Exception("Archivo muy pesado");
            }
            bytes = new byte[(int)length];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file "+file.getName());
            }
            is.close();
            bos.write(bytes);
            bos.close();
            //this.setByteArray();
            file.delete();
            file=null;
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getPathApp() {
        return pathApp;
    }
    public void setPathApp(String pathApp) {
        this.pathApp = pathApp;
    }

    public byte[] getByteArray(){
        byte[] ret = null;
        try {
            ret =  FileUtils.readFileToByteArray(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ret;
    }
}