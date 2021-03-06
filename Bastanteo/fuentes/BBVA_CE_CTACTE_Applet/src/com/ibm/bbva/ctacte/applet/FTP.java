package com.ibm.bbva.ctacte.applet;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import com.ibm.bbva.ctacte.applet.constructor.Archivo;
import com.ibm.bbva.ctacte.applet.constructor.ArchivoApplet;
import com.ibm.bbva.ctacte.applet.constructor.Parametros;
import com.ibm.bbva.ctacte.ftp.FTPArchivos;
import com.ibm.bbva.ctacte.ftp.FTPListener;
import com.ibm.bbva.ctacte.ftp.FTPRunnable;
import com.ibm.bbva.ctacte.log.SimpleLogger;

public class FTP extends ArchivoApplet implements FTPListener {

	private static final SimpleLogger LOG = SimpleLogger.getLogger(FTP.class);
	
	//private JLabel texto1 = new JLabel ();
	private JProgressBar progressBar = new JProgressBar ();
	private List<JLabel> listLabels = new ArrayList<JLabel>();
	
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
    private String codConv;

    private String servidor;
    private String puerto;
    private String servidorPDF;
    private String puertoPDF;
    private String carpetaFTP;
    private int numReintentos;
	
	public FTP() {
		bytes = 0;
		progressBar.setStringPainted(true);
		Container container = getContentPane();
		container.setLayout(new GridLayout (0, 1));
		//container.add(texto1);
		container.add(progressBar);
		//texto1.setText("Archivo");
		//texto1.setHorizontalAlignment(JLabel.CENTER);
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
    	codConv = getParameter(Parametros.CODIGOS_CONVERSION_PDF);
    	servidor = getParameter(Parametros.SERVIDOR_SERV_WEB);
    	puerto = getParameter(Parametros.PUERTO_SERV_WEB);
    	servidorPDF = getParameter(Parametros.SERVIDOR_CONV_PDF);
        puertoPDF = getParameter(Parametros.PUERTO_CONV_PDF);
    	LOG.info("servidor : "+servidor+"  --  codConv : "+codConv);
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
        String tramaTIF = getParameter("tramaTIF");
        avanzados = new HashSet<String> (10);
        if (tramaTIF!=null && !tramaTIF.equals("")) {
	        StringTokenizer st = new StringTokenizer (tramaTIF, ";");
	        while (st.hasMoreTokens()) {
	        	avanzados.add(st.nextToken());
	        }
        }
        String strNumReintentos = getParameter("numReintentos");
        try {
        	numReintentos = Integer.parseInt(strNumReintentos);
        } catch (Exception ex) {
        	numReintentos = 0;
        	LOG.error("numReintentos="+strNumReintentos+". Se usar� el valor cero por defecto.");
        }
    }
    
    public void upload() {
    	LOG.info("ejecutarSubidaDeArchivos " + host);
        if (codConv!=null && !"".equals(codConv)) {
        	StringTokenizer st = new StringTokenizer (codConv, ";");
        	while (st.hasMoreTokens()) {
        		String codigo = st.nextToken();
        		LOG.info("codigo: " + codigo);
        		String texto = "<html><head></head><body>"+obtenerTexto (codigo)+"</body></html>";
        		LOG.info("texto: " + texto);
        		Archivo.getInstance().guardarTextoPDF(codigo, texto);
        	}
        }
        File[] arrFile = new File(escaneosPath).listFiles();
        LOG.info("ESCANEOS PATH:  " + escaneosPath);
        LOG.info("TAMA�O : " + arrFile.length);
        if (arrFile.length == 0 && (codConv==null || "".equals(codConv))) {
        	LOG.info("Llamando al JS: accionCerrar()");
        	cerrarVentana();
        } else {
            for (File arch : arrFile) {
                LOG.info("Poniendo en cola : " + arch.getName());
                String remoteFile = numeroexpediente + "-" + arch.getName();
                FTPArchivos.agregarArchivo(arch, host, user, remoteFile, password, 
                		periodo, tasaKBytes, carpetaFTP, numReintentos);
                LOG.info("Salio enviar archivo");
                JLabel labelArchivo = new JLabel("- "+remoteFile+": En cola de transferencia.", JLabel.LEFT);
                labelArchivo.setForeground(Color.black);
                getContentPane().add(labelArchivo);
                listLabels.add(labelArchivo);
            }
            String servicioPDF = "http://"+servidorPDF+":"+puertoPDF+"/BBVA_ConvertFiles/services/ConvertirArchivos/wsdl/ConvertirArchivos.wsdl";
            Thread thread = new Thread (new FTPRunnable (this, avanzados, servicioPDF));
            thread.start();
        }

    }
    
    private String obtenerTexto (String codigo) {
    	try {
    		StringBuilder sb = new StringBuilder ("http://");
    		sb.append(servidor).append(":").append(puerto);
    		sb.append("/BBVA_CTACTE_Servicio/ObtenerDocumentoServlet?expediente=");
    		sb.append(numeroexpediente).append("&codigo=").append(codigo);
    		String urlServlet = sb.toString();
    		LOG.info("url-servlet : "+urlServlet);
            URL url = new URL (urlServlet);
            URLConnection connection = url.openConnection();
            BufferedReader bis = new BufferedReader (new InputStreamReader(connection.getInputStream()));
            String linea = null;
            StringBuilder builder = new StringBuilder ();
            while ((linea=bis.readLine()) != null) {
            	LOG.info("linea : "+linea);
            	builder.append(linea);
            }
            LOG.info("builder.toString(): " + builder.toString());
            return builder.toString();
        } catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }

	public void setAvance(int bytesLeidos) {
		bytes += bytesLeidos;
		progressBar.setValue(bytes);
	}

	public void cerrarVentana() {
		try {
			LOG.info("cerrarVentana()");
            JSObject window = JSObject.getWindow(this);
            window.eval("cerrarVentana()");
		} catch (JSException ex) {
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
		//texto1.setText(archivo);
		for (JLabel labelArchivo : listLabels) {
			if (labelArchivo.getText().indexOf(archivo) > -1) {
				labelArchivo.setText("- "+archivo+": Transfiriendo...");
				labelArchivo.setForeground(Color.blue);
				break;
			}
		}
	}
	
	public void errorArchivo(String archivo, int reintentos) {
		for (JLabel labelArchivo : listLabels) {
			if (labelArchivo.getText().indexOf(archivo) > -1) {
				labelArchivo.setText("- "+archivo+": Error en transferencia ("+reintentos+" reintentos restantes).");
				labelArchivo.setForeground(Color.red);
				break;
			}
		}
	}
	
	public void exitoArchivo(String archivo) {
		for (JLabel labelArchivo : listLabels) {
			if (labelArchivo.getText().indexOf(archivo) > -1) {
				labelArchivo.setText("- "+archivo+": Transferencia exitosa.");
				labelArchivo.setForeground(Color.green);
				break;
			}
		}
	}

	public void setTamanio(int tamanio) {
		this.tamanioArchivo = tamanio;
		progressBar.setMaximum(tamanioArchivo);
	}
	
}
