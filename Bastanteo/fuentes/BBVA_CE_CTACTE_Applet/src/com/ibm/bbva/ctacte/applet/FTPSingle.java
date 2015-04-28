package com.ibm.bbva.ctacte.applet;

import java.awt.Container;
import java.awt.GridLayout;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import com.ibm.bbva.ctacte.applet.constructor.Archivo;
import com.ibm.bbva.ctacte.applet.constructor.ArchivoApplet;
import com.ibm.bbva.ctacte.applet.constructor.Parametros;
import com.ibm.bbva.ctacte.ftp.FTPArchivos;
import com.ibm.bbva.ctacte.ftp.FTPListener;
import com.ibm.bbva.ctacte.ftp.FTPRunnable;
import com.ibm.bbva.ctacte.log.SimpleLogger;

public class FTPSingle extends ArchivoApplet implements FTPListener {

	private static final SimpleLogger LOG = SimpleLogger.getLogger(FTPSingle.class);
	
	private JLabel texto1 = new JLabel ();
	private JProgressBar progressBar = new JProgressBar ();
	
	private int bytes;
	private String host;
    private String user;
    private String password;
    private int periodo;
    private int tasaKBytes;
    private String escaneosPath;
    private String numeroexpediente;
    private String logPath;
    private HashSet<String> avanzados;
    private int tamanioArchivo;

    private String servidor;
    private String puerto;
    private String servidorPDF;
    private String puertoPDF;
    private String codDocumento;
    private String carpetaFTP;
	
	public FTPSingle() {
		bytes = 0;
		progressBar.setStringPainted(true);
		Container container = getContentPane();
		container.setLayout(new GridLayout (1, 1));
		//container.add(texto1);
		container.add(progressBar);
		texto1.setText("Archivo");
		texto1.setHorizontalAlignment(JLabel.CENTER);
	}
	
	@Override
	public void init() {
		super.init();
		LOG.info("Entro LoadParameters");  
        loadParameters();
        LOG.info("Entro Upload");
        upload();
	}
    
    public void loadParameters() {
    	LOG.info("entro al load parameters");
//    	String puerto = getParameter(Parametros.PUERTO_SERV_WEB);
    	servidor = getParameter(Parametros.SERVIDOR_SERV_WEB);
    	puerto = getParameter(Parametros.PUERTO_SERV_WEB);
    	servidorPDF = getParameter(Parametros.SERVIDOR_CONV_PDF);
        puertoPDF = getParameter(Parametros.PUERTO_CONV_PDF);
        logPath = getParameter("pathLog");
        LOG.info(logPath);  
        host=getParameter("host");
        LOG.info(host);
        user = getParameter("user");
        LOG.info(user);
        password = getParameter("password");
        LOG.info(password);
        periodo = Integer.parseInt((getParameter("periodo") == null ? "0" : getParameter("periodo")));
        LOG.info(periodo);
        tasaKBytes = Integer.parseInt(getParameter("tasaKBytes") == null ? "0" : getParameter("tasaKBytes"));
        LOG.info(tasaKBytes);
        carpetaFTP = getParameter("carpetaFTP");
        LOG.info(carpetaFTP);
        escaneosPath = getParameter("escaneosPath");
        LOG.info(escaneosPath);
        numeroexpediente = getParameter("prefix");
        LOG.info(numeroexpediente);
        avanzados = new HashSet<String> (10);
        codDocumento = getParameter("codDocumento");
    }
    
    public void upload() {
    	LOG.info("ejecutarSubidaDeArchivos " + host);
        File fileUpload = new File(escaneosPath, codDocumento + ".pdf");
        LOG.info("ESCANEOS PATH:  " + escaneosPath);
        LOG.info("FILE UPLOAD : " + fileUpload.getAbsolutePath());
        if (!fileUpload.exists()) {
        	LOG.info("Llamando al JS: accionCerrar()");
        	cerrarVentana();
        } else {
            LOG.info("Poniendo en cola : " + fileUpload.getName());
            FTPArchivos.agregarArchivo(fileUpload, host, user, numeroexpediente + "-" + fileUpload.getName(), password, 
            		periodo, tasaKBytes, carpetaFTP, 1);
            LOG.info("Salio enviar archivo");
            String servicioPDF = "http://"+servidorPDF+":"+puertoPDF+"/BBVA_ConvertFiles/services/ConvertirArchivos/wsdl/ConvertirArchivos.wsdl";
            Thread thread = new Thread (new FTPRunnable (this, avanzados, servicioPDF));
            thread.start();
        }

    }

	public void setAvance(int bytesLeidos) {
		bytes += bytesLeidos;
		progressBar.setValue(bytes);
	}

	public void cerrarVentana() {
		try {
			LOG.info("cerrarVentana()");
            this.getAppletContext().showDocument(new URL("javascript:cerrarVentana()"));
        } catch (MalformedURLException ex) {
        	LOG.error("error javascript", ex);
        }
	}
	
	public void cerrarVentana(List<String> archivosError) {
		try {
			LOG.info("cerrarVentana - archivosError.size: "+archivosError.size());
			StringBuilder mensaje = new StringBuilder("Los siguientes archivos no se pudieron copiar al FTP: ");
			for (int i=0; i<archivosError.size(); i++) {
				String nombre = archivosError.get(i);
				mensaje.append(nombre);
				if (i < archivosError.size()-1) {
					mensaje.append(", ");
				} else {
					mensaje.append(". ");
				}
			}
			mensaje.append("Los archivos han sido copiados en la ruta ");
			mensaje.append(Archivo.strBackup);
			LOG.info("Mensaje: "+mensaje.toString());
            this.getAppletContext().showDocument(new URL("javascript:cerrarVentanaError('"+mensaje.toString()+"')"));
        } catch (MalformedURLException ex) {
        	LOG.error("error javascript", ex);
        }
	}

	public void limpiarTransferDir() {
		File dirTarget = new File(escaneosPath);
        if (dirTarget.exists()) {
            for (File f : dirTarget.listFiles()) {
                f.delete();
            }
        }
	}

	public void nuevoArchivo(String archivo) {
		texto1.setText(archivo);
	}

	public void setTamanio(int tamanio) {
		this.tamanioArchivo = tamanio;
		progressBar.setMaximum(tamanioArchivo);
	}
	
	public void errorArchivo(String archivo, int reintentos) {
	}
	public void exitoArchivo(String archivo) {
	}
	
}
