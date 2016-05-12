package com.ibm.bbva.app.applet;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import com.ibm.bbva.app.applet.constructor.Archivo;
import com.ibm.bbva.app.applet.constructor.ArchivoApplet;
import com.ibm.bbva.app.applet.constructor.Parametros;
import com.ibm.bbva.app.ftp.FTPArchivos;
import com.ibm.bbva.app.ftp.FTPListener;
import com.ibm.bbva.app.ftp.FTPRunnable;
import com.ibm.bbva.app.log.SimpleLogger;

public class FTP extends ArchivoApplet implements FTPListener {

	private static final SimpleLogger LOG = SimpleLogger.getLogger(FTP.class);
	
	//private JProgressBar progressBar = new JProgressBar ();
	//private List<JLabel> listLabels = new ArrayList<JLabel>();
	
	private int bytes;
	private String host;
    private String user;
    private String password;
    private int periodo;
    private int tasaKBytes;
    private String escaneosPath;
    private String numeroexpediente;
    private String listaDocumentosAdjuntos;
    private String logPath;
    private HashSet<String> avanzados;
    private int tamanioArchivo;
    private String codConv;

    private String servidor;
    private String puerto;
    private String servidorPDF;
    private String puertoPDF;
    private int numReintentos;
    private String carpetaTemporal;
    private String descargados;
    private String transferencias;
    private String cleanTransferDir;
    private String nombreJarVersion;
    
	
	public FTP() {
		bytes = 0;
		//progressBar.setStringPainted(true);
		//Container container = getContentPane();
		//container.setLayout(new GridLayout (1, 1));
		//container.add(progressBar);
		
	}
	
	@Override
	public void init() {
		super.init();
		LOG.info("Entro LoadParameters");  
        loadParameters();
        LOG.info("Entro Upload");
        upload();
        cerrarVentana();
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
        //logPath = getParameter("pathLog");
    	logPath = getParameter("log");
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
        escaneosPath = getParameter("escaneosPath");
        //escaneosPath = getParameter("transferencias");
        LOG.info(escaneosPath);
        numeroexpediente = getParameter("prefix");
        LOG.info(numeroexpediente);
        listaDocumentosAdjuntos = getParameter("listaDocumentosAdjuntos");
        LOG.info(listaDocumentosAdjuntos);
        descargados = getParameter("descargados");
        LOG.info(descargados);
        transferencias = getParameter("transferencias");
        LOG.info(transferencias);
        cleanTransferDir = getParameter("cleanTransferDir");
        LOG.info(cleanTransferDir);
        carpetaTemporal = getParameter("carpetaTemporal");
        LOG.info(carpetaTemporal);
        nombreJarVersion = getParameter("nombreJarVersion");
        LOG.info(nombreJarVersion);
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
        	LOG.error("numReintentos="+strNumReintentos+". Se usará el valor cero por defecto.");
        }
    }
    
    /*public void upload() {
    	LOG.info("ejecutarSubidaDeArchivos " + host);
        if (codConv!=null && !"".equals(codConv)) {
        	StringTokenizer st = new StringTokenizer (codConv, ";");
        	while (st.hasMoreTokens()) {
        		String codigo = st.nextToken();
        		String texto = "<html><head></head><body>"+obtenerTexto (codigo)+"</body></html>";
        		LOG.info("texto: " + texto);
        		Archivo.getInstance().guardarTextoPDF(codigo, texto);
        	}
        }
        File[] arrFile = new File(escaneosPath).listFiles();
        LOG.info("ESCANEOS PATH:  " + escaneosPath);
        LOG.info("TAMAÑO : " + arrFile.length);
        if (arrFile.length == 0 && (codConv==null || "".equals(codConv))) {
        	LOG.info("Llamando al JS: accionCerrar()");
        	cerrarVentana();
        } else {
        	int posIni = 0;
    		int posFin = 0;
    		String id = "";
    		String caracter1 = ";";
    		String caracter2 = "|";
            for (File arch : arrFile) {
                LOG.info("Poniendo en cola : " + arch.getName());
                if (listaDocumentosAdjuntos!=null && !listaDocumentosAdjuntos.isEmpty()){
                	LOG.info("listaDocumentosAdjuntos:" + listaDocumentosAdjuntos);
	                //posIni = listaDocumentosAdjuntos.indexOf(caracter1,listaDocumentosAdjuntos.indexOf(arch.getName().substring(0, arch.getName().length()-5)));
	        		//posFin = listaDocumentosAdjuntos.indexOf(caracter2,listaDocumentosAdjuntos.indexOf(arch.getName().substring(0, arch.getName().length()-5)));
                	int posicionPunto = arch.getName().indexOf(".");
                	posIni = listaDocumentosAdjuntos.indexOf(caracter1,listaDocumentosAdjuntos.indexOf(arch.getName().substring(0, posicionPunto)));
                	posFin = listaDocumentosAdjuntos.indexOf(caracter2,listaDocumentosAdjuntos.indexOf(arch.getName().substring(0, posicionPunto)));
                	LOG.info("posIni:" + posIni);
	        		LOG.info("posFin:" + posFin);
	        		id=listaDocumentosAdjuntos.substring(posIni+1,posFin);
	        		LOG.info("id doc : " + id);
            	}
                String remoteFile = numeroexpediente + "-" + arch.getName();
                FTPArchivos.agregarArchivo(arch, host, user, numeroexpediente + "-" + arch.getName()+ "-" + id, password, 
                		periodo, tasaKBytes, numReintentos);
                LOG.info("Salio enviar archivo");
                JLabel labelArchivo = new JLabel("- "+remoteFile+": En cola de transferencia.", JLabel.LEFT);
                labelArchivo.setForeground(Color.black);
                //getContentPane().add(labelArchivo);
                listLabels.add(labelArchivo);
            }
            //String servicioPDF = "http://"+servidorPDF+":"+puertoPDF+"/BBVA_ConvertFiles/services/ConvertirArchivos/wsdl/ConvertirArchivos.wsdl";
            //Thread thread = new Thread (new FTPRunnable (this, avanzados, servicioPDF));
            Thread thread = new Thread (new FTPRunnable (this));
            thread.start();
        }

    }*/
    
    public void upload() {
    	LOG.info("ejecutarSubidaDeArchivos para docs_send");
    	LOG.info("buscando carpeta local......");
    	//File dirN = new File(rutaDestino,"BBVASendDocsFTP.jar");
    	//LOG.info("dirN......" + dirN);
    	//File dirN = new File("D:\\ContratacionElectronica\\Lib_TC\\","BBVASendDocsFTP.jar");
    	//File dirN = new File(Parametros.DIRECTORIO_BASE + Parametros.DIRECTORIO_LIB,Parametros.JAR_TRANSFERENCIAS);
    	File dirN = new File(Parametros.DIRECTORIO_BASE + Parametros.DIRECTORIO_LIB,nombreJarVersion+".jar");
    	//String javaHome="C:\\Program Files (x86)\\Java\\jdk1.7.0.79\\bin\\";
    	//File dirN = new File("D:\\ContratacionElectronica\\Transferencias_TC");
		if (dirN.exists()) {
			
			LOG.info("carpeta encontrada......");
			try {
				LOG.info("Se ejecuta JAR de la ruta......" + dirN);
				/*LOG.info("Con parametros......" + codConv +" "+ servidor +" "+  puerto +" "+  servidorPDF +" "+  puertoPDF +" "+  logPath +" "+ 
							host +" "+  user +" "+  password +" "+  periodo +" "+  tasaKBytes +" "+  escaneosPath +" "+  numeroexpediente +" "+ 
						    listaDocumentosAdjuntos +" "+  numReintentos +" "+  descargados +" "+  transferencias +" "+  cleanTransferDir +" "+ 
							carpetaTemporal);*/
				LOG.info("TODO JUNTOS......" + 
						"cmd /c start "+dirN+" \""+codConv+"\" \""+servidor+"\" \""+puerto+"\" \""+servidorPDF+"\" \""+puertoPDF+"\" \""+logPath+"\" \""+host+"\" \""+user+"\" \""+password+"\" \""+periodo+"\" \""+tasaKBytes+"\" \""+escaneosPath+"\" \""+numeroexpediente+"\" \""+listaDocumentosAdjuntos+"\" \""+numReintentos+"\" \""+descargados+"\" \""+transferencias+"\" \""+cleanTransferDir+"\" \""+carpetaTemporal+"\"");
				//Runtime.getRuntime().exec("java -jar "+dirN+"BBVASendDocsFTP.jar codConv servidor puerto servidorPDF puertoPDF logPath host user password periodo tasaKBytes escaneosPath numeroexpediente listaDocumentosAdjuntos numReintentos descargados transferencias cleanTransferDir carpetaTemporal");
				//Runtime.getRuntime().exec(javaHome+"java -jar "+dirN+" \""+codConv+"\" \""+servidor+"\" \""+puerto+"\" \""+servidorPDF+"\" \""+puertoPDF+"\" \""+logPath+"\" \""+host+"\" \""+user+"\" \""+password+"\" \""+periodo+"\" \""+tasaKBytes+"\" \""+escaneosPath+"\" \""+numeroexpediente+"\" \""+listaDocumentosAdjuntos+"\" \""+numReintentos+"\" \""+descargados+"\" \""+transferencias+"\" \""+cleanTransferDir+"\" \""+carpetaTemporal+"\"");
				
				Runtime.getRuntime().exec("cmd /c start "+dirN+" \""+codConv+"\" \""+servidor+"\" \""+puerto+"\" \""+servidorPDF+"\" \""+puertoPDF+"\" \""+logPath+"\" \""+host+"\" \""+user+"\" \""+password+"\" \""+periodo+"\" \""+tasaKBytes+"\" \""+escaneosPath+"\" \""+numeroexpediente+"\" \""+listaDocumentosAdjuntos+"\" \""+numReintentos+"\" \""+descargados+"\" \""+transferencias+"\" \""+cleanTransferDir+"\" \""+carpetaTemporal+"\"");
								
				LOG.info("SE TERMINO LA EJECUCION DEL JAR LOCAL::");
			} catch (IOException e) {
				// TODO Bloque catch generado automáticamente
				e.printStackTrace();
			}
			LOG.info("jar ejecutado......");
		}else{
			LOG.info("carpeta  no encontrada......");
		}
    	

    }
    
    private String obtenerTexto (String codigo) {
    	try {
    		StringBuilder sb = new StringBuilder ("http://");
    		sb.append(servidor).append(":").append(puerto);
    		sb.append("/BBVA_CE_CTACTE_Servicio/ObtenerDocumentoServlet?expediente=");
    		sb.append(numeroexpediente).append("&codigo=").append(codigo);
    		String urlServlet = sb.toString();
    		System.out.println("url-servlet : "+urlServlet);
            URL url = new URL (urlServlet);
            URLConnection connection = url.openConnection();
            BufferedReader bis = new BufferedReader (new InputStreamReader(connection.getInputStream()));
            String linea = null;
            StringBuilder builder = new StringBuilder ();
            while ((linea=bis.readLine()) != null) {
            	builder.append(linea);
            }
            return builder.toString();
        } catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
    
    /*public void errorArchivo(String archivo, int reintentos) {
		for (JLabel labelArchivo : listLabels) {
			if (labelArchivo.getText().indexOf(archivo) > -1) {
				labelArchivo.setText("- "+archivo+": Error en transferencia ("+reintentos+" reintentos restantes).");
				labelArchivo.setForeground(Color.red);
				break;
			}
		}
	}
    
    /*public void exitoArchivo(String archivo) {
		for (JLabel labelArchivo : listLabels) {
			if (labelArchivo.getText().indexOf(archivo) > -1) {
				labelArchivo.setText("- "+archivo+": Transferencia exitosa.");
				labelArchivo.setForeground(Color.green);
				break;
			}
		}
	}*/
    

	/*public void setAvance(int bytesLeidos) {
		bytes += bytesLeidos;
		progressBar.setValue(bytes);
	}*/

	public void cerrarVentana() {
		try {
            this.getAppletContext().showDocument(new URL("javascript:accionCerrar()"));
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

	/*public void nuevoArchivo(String archivo) {
		//texto1.setText(archivo);
		for (JLabel labelArchivo : listLabels) {
			if (labelArchivo.getText().indexOf(archivo) > -1) {
				labelArchivo.setText("- "+archivo+": Transfiriendo...");
				labelArchivo.setForeground(Color.blue);
				break;
			}
		}
	}*/

	/*public void setTamanio(int tamanio) {
		this.tamanioArchivo = tamanio;
		progressBar.setMaximum(tamanioArchivo);
	}*/
	
}
