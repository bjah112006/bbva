package com.ibm.bbva.app.docs.send;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.ibm.bbva.app.docs.send.constructor.Archivo;
import com.ibm.bbva.app.docs.send.constructor.Parametros;
import com.ibm.bbva.app.docs.send.ftp.FTPArchivos;
import com.ibm.bbva.app.docs.send.ftp.FTPListener;
import com.ibm.bbva.app.docs.send.ftp.FTPRunnable;
import com.ibm.bbva.app.log.SimpleLogger;

//public class FTP extends ArchivoDocsSend implements FTPListener {
public class FTP extends JFrame implements FTPListener  {

	private static final SimpleLogger LOG = SimpleLogger.getLogger(FTP.class);
	

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
    private String descargados;
    public String transferencias;
    private String cleanTransferDir;
    public String carpetaTemporal;

    private String servidor;
    private String puerto;
    private String servidorPDF;
    private String puertoPDF;
    private int numReintentos;
    
    public FTP(){};
    
	
	public FTP(String pCodConversionPDF, String pServidorServWebJar, String pPuertoServWebJar, String pServidorConvPDFJar,
			String pPuertoConvPDFJar, String pLogPath, String pHost, String pUser, String pPassword, String pPeriodo, 
			String pTasaKBytes, String pEscaneosPath, String pNumeroExpediente, String pListaDocumentosAdjuntos,
			String pNumReintentos, String pDescargados, String pTransferencias,  String pCleanTransferDir, String pCarpetaTemporal) {
		
		codConv = pCodConversionPDF;
    	servidor = pServidorServWebJar;
    	puerto = pPuertoServWebJar;
    	servidorPDF = pServidorConvPDFJar;
        puertoPDF = pPuertoConvPDFJar;
    	LOG.info("servidor : "+servidor+"  --  codConv : "+codConv);
        logPath = pLogPath;
        LOG.info(logPath);  
        host=pHost;
        LOG.info(host);
        user = pUser;
        LOG.info(user);
        password = pPassword;
        LOG.info(password);
        periodo = Integer.parseInt(pPeriodo == null ? "0" : pPeriodo);
        LOG.info(periodo);
        tasaKBytes = Integer.parseInt(pTasaKBytes == null ? "0" : pTasaKBytes);
        LOG.info(tasaKBytes);
        escaneosPath = pEscaneosPath;
        LOG.info(escaneosPath);
        numeroexpediente = pNumeroExpediente;
        LOG.info(numeroexpediente);
        listaDocumentosAdjuntos = pListaDocumentosAdjuntos;
        LOG.info(listaDocumentosAdjuntos);
        String strNumReintentos = pNumReintentos;
        LOG.info(strNumReintentos);
        descargados = pDescargados;
        LOG.info(descargados);
        transferencias = pTransferencias;
        LOG.info(transferencias);
        cleanTransferDir = pCleanTransferDir;
        LOG.info(cleanTransferDir);
        carpetaTemporal = pCarpetaTemporal;
        LOG.info(carpetaTemporal);
        
	}
	
	
	
	public void init() {
		
		String carpLogJar = Parametros.CARPETA_CLIENTE_LOG_TRANSFER;
		//String ruta_carpLogJar = getParameter("logPath");
		String ruta_carpLogJar = logPath;
		//LOG.info("RUTA DE DIRECTORIO PARA LOG DEL JAR" + ruta_carpLogJar + carpLogJar); 
    	SimpleLogger.iniciar(Archivo.crearDirectorioJAR(ruta_carpLogJar+carpLogJar), true);
    	LOG.info("Se creo la carpeta para log de JAR");
    	try {
    		LOG.info("Parametro cleanTransferDir: "+cleanTransferDir);
    		Archivo.iniciar(
    			descargados,
    			transferencias,
    			cleanTransferDir);
		} catch (IOException e1) {
			e1.printStackTrace();
			LOG.error("No se puede modificar las carpetas", e1);
		}
	
        LOG.info("Entro Upload");
        upload();
      
    }
   
    
    public void upload() {
    	System.out.println("Entro a upload");
    	LOG.info("ejecutarSubidaDeArchivos " + host);
        
    	File[] arrFile = new File(transferencias).listFiles();
        LOG.info("ESCANEOS PATH:  " + transferencias);
        LOG.info("TAMAÑO : " + arrFile.length);
        if (arrFile.length == 0 && (codConv==null || "".equals(codConv))) {
        	
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
                
            }
           
            Thread thread = new Thread (new FTPRunnable (this));
            thread.start();
           
        }

    }

	public void limpiarTransferDir() {
		File dirTarget = new File(transferencias);
        if (dirTarget.exists()) {
            for (File f : dirTarget.listFiles()) {
                f.delete();
            }
        }
	}
	
	public void eliminarCarpetaTemporal() {
		LOG.info("ENTRO A ELIMINAR LA CARPETA TEMPORAL::");
		LOG.info("directorioRaiz::: " + escaneosPath);
		LOG.info("carpetaTemporal::: " + carpetaTemporal);
		File dirTarget = new File(escaneosPath+"\\");
        if (dirTarget.exists()) {
           	for (File carpeta : dirTarget.listFiles()) {
        		LOG.info("carpetaTemporal::: " + carpeta.getName());
				if(carpeta.getName().equals(carpetaTemporal)){
					File tempFile = new File(carpeta.getAbsoluteFile().toString());
					if (tempFile.exists()) {
						for (File archivo : tempFile.listFiles()) {
							archivo.delete();
							LOG.info("ARCHIVO " + archivo.getName() + " ELIMINADO");
						}
					}
					carpeta.delete();
					LOG.info("CARPETA TEMPORAL ELIMINADA::: ");
				}
			}
        	
        }
	}
	
	public void mostrarMensaje(){
		String message = "Los documentos del expediente "+numeroexpediente+" fueron enviados correctamente.";
		//String message = "Los documentos del expediente fueron adjuntados correctamente.";
		String header = "CONTRATACION SENCILLA PLD/TC";
		
		this.setSize(450,180);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();// size of the screen
		
		Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());// height of the task bar
		
		this.setLocation(scrSize.width - this.getWidth(), scrSize.height - toolHeight.bottom - this.getHeight());
		this.setLayout(new GridBagLayout());
		//this.setLayout(new BorderLayout());
		//setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
	    //        "  Events  "));
		
		GridBagConstraints constraints = new GridBagConstraints();		
		constraints.gridx = 0;		
		constraints.gridy = 0;		
		constraints.weightx = 1.0f;		
		constraints.weighty = 1.0f;		
		constraints.insets = new Insets(5, 25, 10, 5);		
		constraints.fill = GridBagConstraints.BOTH;
				
		//String headerConEstilo = "<html><body><div style=\"border-style: solid; border-width: 4px;\">" +
		/*String headerConEstilo = "<html><body>" +
				 "<div style=\"margin-top: 10spx; padding: 0pt 0.7em;\" \">"+ 
			     "<p style=\"font-size: 20pt; color: #369;\">" +
			     "<span style=\"float: left; margin-right: 0.3em;\" \"></span><strong>" +
			     header + "</strong></p></div><br/><br/>";*/
		
		/*String mensajeConEstilo = "<html><body><div style=\"border: 1px solid #fcefa1; background: #fbf9ee" +
				"url(#{resource['css/jquery-ui/images/ui-bg_glass_55_fbf9ee_1x400.png']}) 50% 50% repeat-x; color: #363636; "+
				"color: #363636 -moz-border-radius-topleft: 4px; -webkit-border-top-left-radius: 4px; -khtml-border-top-left-radius: 4px; border-top-left-radius: 4px;"+
				"-moz-border-radius-topright: 4px; -webkit-border-top-right-radius: 4px; -khtml-border-top-right-radius: 4px; border-top-right-radius: 4px;"+
				//"-moz-border-radius-bottomleft: 4px; -webkit-border-bottom-left-radius: 4px; -khtml-border-bottom-left-radius: 4px; border-bottom-left-radius: 4px;"+
				//"-moz-border-radius-bottomright: 4px; -webkit-border-bottom-right-radius: 4px; -khtml-border-bottom-right-radius: 4px; border-bottom-right-radius: 4px; \">"+ 
			    // "<p style=\"font-size: 20pt; color: #369;\">" +
			     //"<span style=\"float: left; margin-right: 0.3em; background-position: -16px -144px;\"></span>" +
			     //message + "</p></div><br/><br/> </body></html>";*/
			     //message + "</p></div><br/><br/></div> </body></html>";
		
		
		String mensajeConEstilo = "<html> <head><link rel=\"stylesheet\" href=\"lib/css/jquery-ui/jquery-ui.css\"></head>" +
				"<body><div style=\"margin-top: 20px; padding: 0pt 0.7em; text-align: center; border: 1px solid #fcefa1; background: #fbf9ee; color: #363636;\" class=\"ui-state-highlight ui-corner-all\" \">"+
				"<br /><p style=\"font-size: 20pt; color: rgb(51, 102, 153); margin: 8px; margin-top: 30px;\">" +
					     "<span style=\"float: left; margin-right: 0.3em;\" class=\"ui-icon ui-icon-info\"\"></span><strong> " +
					     header + "</strong></p> <br />" +
	     "<p style=\"font-size: 20pt; color: rgb(51, 102, 153); margin: 8px; margin-top: 5px; padding: 0pt 0.7em; width=50%;\">" +
	     "<span style=\"float: left; margin-right: 0.3em; padding: 0pt 0.7em;\" class=\"ui-icon ui-icon-info\"\"></span>" +
	     message + "</p><br /><br /><br /></div><br/><br/> </body></html>";

				
		
		Color colorMensaje= Color.decode("#fcefa1");
		Font fuente=new Font("Arial", Font.BOLD, 18);
		
		Color colorCuerpo= Color.decode("#fbf9ee");
		
		//JLabel headingLabel = new JLabel(headerConEstilo + mensajeConEstilo);
		JLabel headingLabel = new JLabel(mensajeConEstilo);
		//Border etchedRaised = BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.RED, Color.GRAY);

		
		Border line = BorderFactory.createLineBorder(colorMensaje, 2);
		Border titled = BorderFactory.createTitledBorder(line, header, TitledBorder.LEFT, TitledBorder.TOP, fuente, colorMensaje);
		  /* Un CompoundBorder lo creamos por la combinación de 2 bordes simples. */
		  //Border compound = BorderFactory.createCompoundBorder(titled, etchedRaised);
		 
		  //simplePanel.setBorder(BorderFactory.createTitledBorder("Bordes simples"));

		headingLabel.setBorder(BorderFactory.createTitledBorder(header));
		//headingLabel.setBorder(etchedRaised);
		headingLabel.setBorder(line);
		//headingLabel.setBorder(titled);
		headingLabel.setBackground(colorCuerpo);

		//headingLabel.setBorder(compound);
		//ImageIcon headingIcon = new ImageIcon("image_url");
		//headingLabel .setIcon(headingIcon); // --- use image icon you want to be as heading image.		
		//headingLabel.setOpaque(false);		
		this.add(headingLabel, constraints);	
		
		
		//this.add("TitledBorder", titled, constraints);
		//this.add("CompoundBorder", compound, constraints);
		
		
		constraints.gridx++;
		constraints.weightx = 0.0f;		
		constraints.weighty = 0.0f;	
		constraints.fill = GridBagConstraints.NONE;		
		constraints.anchor = GridBagConstraints.NORTH;	
		
		 JButton cloesButton = new JButton(new AbstractAction("X") {
	        @Override
	        public void actionPerformed(final ActionEvent e) {
	        	dispose();
	        }
	
		 });
		 cloesButton.setMargin(new Insets(1, 1, 1, 1));
		 cloesButton.setFocusable(false);
		 this.add(cloesButton, constraints);
		 //constraints.gridx = 0;
		 //constraints.gridy++;
		 //constraints.weightx = 1.0f;
		 //constraints.weighty = 1.0f;
		 //constraints.insets = new Insets(5, 5, 5, 5);
		 //constraints.fill = GridBagConstraints.BOTH;
		 //JLabel messageLabel = new JLabel("<HtMl>"+message);
		 /*String mensajeConEstilo = "<html><body><div style=\"margin-top: 10spx; padding: 0pt 0.7em;\" class=\"ui-state-highlight ui-corner-all\">"+ 
			     "<p style=\"font-size: 20pt; color: #369;\">" +
			     "<span style=\"float: left; margin-right: 0.3em;\" class=\"ui-icon ui-icon-info\"></span>" +
			     message + "</p></div></body></html>";*/
		
		//JLabel messageLabel = new JLabel(mensajeConEstilo);
		//this.add(messageLabel, constraints);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setAlwaysOnTop(true);
		this.setVisible(true);

		new Thread(){	
		      @Override
		      public void run() {
		           try {
		                  Thread.sleep(10000); // time after which pop up will be disappeared.
		        	      dispose();
		           } catch (InterruptedException e) {
		           //} catch (Exception e) {	     
		                  e.printStackTrace();
		
		           }
		
		      };
		
		}.start();
	}
	
	public void mostrarMensajeError(){
		String message = "ERROR!! Los documentos del expediente "+numeroexpediente+" no se pudo enviar.";
		//String message = "Los documentos del expediente fueron adjuntados correctamente.";
		String header = "CONTRATACION SENCILLA PLD/TC";
		
		this.setSize(450,180);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();// size of the screen
		
		Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());// height of the task bar
		
		this.setLocation(scrSize.width - this.getWidth(), scrSize.height - toolHeight.bottom - this.getHeight());
		this.setLayout(new GridBagLayout());
		//this.setLayout(new BorderLayout());
		//setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
	    //        "  Events  "));
		
		GridBagConstraints constraints = new GridBagConstraints();		
		constraints.gridx = 0;		
		constraints.gridy = 0;		
		constraints.weightx = 1.0f;		
		constraints.weighty = 1.0f;		
		constraints.insets = new Insets(5, 25, 10, 5);		
		constraints.fill = GridBagConstraints.BOTH;
				
		//String headerConEstilo = "<html><body><div style=\"border-style: solid; border-width: 4px;\">" +
		/*String headerConEstilo = "<html><body>" +
				 "<div style=\"margin-top: 10spx; padding: 0pt 0.7em;\" \">"+ 
			     "<p style=\"font-size: 20pt; color: #369;\">" +
			     "<span style=\"float: left; margin-right: 0.3em;\" \"></span><strong>" +
			     header + "</strong></p></div><br/><br/>";*/
		
		/*String mensajeConEstilo = "<html><body><div style=\"border: 1px solid #fcefa1; background: #fbf9ee" +
				"url(#{resource['css/jquery-ui/images/ui-bg_glass_55_fbf9ee_1x400.png']}) 50% 50% repeat-x; color: #363636; "+
				"color: #363636 -moz-border-radius-topleft: 4px; -webkit-border-top-left-radius: 4px; -khtml-border-top-left-radius: 4px; border-top-left-radius: 4px;"+
				"-moz-border-radius-topright: 4px; -webkit-border-top-right-radius: 4px; -khtml-border-top-right-radius: 4px; border-top-right-radius: 4px;"+
				//"-moz-border-radius-bottomleft: 4px; -webkit-border-bottom-left-radius: 4px; -khtml-border-bottom-left-radius: 4px; border-bottom-left-radius: 4px;"+
				//"-moz-border-radius-bottomright: 4px; -webkit-border-bottom-right-radius: 4px; -khtml-border-bottom-right-radius: 4px; border-bottom-right-radius: 4px; \">"+ 
			    // "<p style=\"font-size: 20pt; color: #369;\">" +
			     //"<span style=\"float: left; margin-right: 0.3em; background-position: -16px -144px;\"></span>" +
			     //message + "</p></div><br/><br/> </body></html>";*/
			     //message + "</p></div><br/><br/></div> </body></html>";
		
		
		String mensajeConEstilo = "<html> <head><link rel=\"stylesheet\" href=\"lib/css/jquery-ui/jquery-ui.css\"></head>" +
				"<body><div style=\"margin-top: 20px; padding: 0pt 0.7em; text-align: center; border: 1px solid #fcefa1; background: #fbf9ee; color: #363636;\" class=\"ui-state-highlight ui-corner-all\" \">"+
				"<br /><p style=\"font-size: 20pt; color: rgb(51, 102, 153); margin: 8px; margin-top: 30px;\">" +
					     "<span style=\"float: left; margin-right: 0.3em;\" class=\"ui-icon ui-icon-info\"\"></span><strong> " +
					     header + "</strong></p> <br />" +
	     "<p style=\"font-size: 20pt; color: rgb(51, 102, 153); margin: 8px; margin-top: 5px; padding: 0pt 0.7em; width=50%;\">" +
	     "<span style=\"float: left; margin-right: 0.3em; padding: 0pt 0.7em;\" class=\"ui-icon ui-icon-info\"\"></span>" +
	     message + "</p><br /><br /><br /></div><br/><br/> </body></html>";

				
		
		Color colorMensaje= Color.decode("#fcefa1");
		Font fuente=new Font("Arial", Font.BOLD, 18);
		
		Color colorCuerpo= Color.decode("#fbf9ee");
		
		//JLabel headingLabel = new JLabel(headerConEstilo + mensajeConEstilo);
		JLabel headingLabel = new JLabel(mensajeConEstilo);
		//Border etchedRaised = BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.RED, Color.GRAY);

		
		Border line = BorderFactory.createLineBorder(colorMensaje, 2);
		Border titled = BorderFactory.createTitledBorder(line, header, TitledBorder.LEFT, TitledBorder.TOP, fuente, colorMensaje);
		  /* Un CompoundBorder lo creamos por la combinación de 2 bordes simples. */
		  //Border compound = BorderFactory.createCompoundBorder(titled, etchedRaised);
		 
		  //simplePanel.setBorder(BorderFactory.createTitledBorder("Bordes simples"));

		headingLabel.setBorder(BorderFactory.createTitledBorder(header));
		//headingLabel.setBorder(etchedRaised);
		headingLabel.setBorder(line);
		//headingLabel.setBorder(titled);
		headingLabel.setBackground(colorCuerpo);

		//headingLabel.setBorder(compound);
		//ImageIcon headingIcon = new ImageIcon("image_url");
		//headingLabel .setIcon(headingIcon); // --- use image icon you want to be as heading image.		
		//headingLabel.setOpaque(false);		
		this.add(headingLabel, constraints);	
		
		
		//this.add("TitledBorder", titled, constraints);
		//this.add("CompoundBorder", compound, constraints);
		
		
		constraints.gridx++;
		constraints.weightx = 0.0f;		
		constraints.weighty = 0.0f;	
		constraints.fill = GridBagConstraints.NONE;		
		constraints.anchor = GridBagConstraints.NORTH;	
		
		 JButton cloesButton = new JButton(new AbstractAction("X") {
	        @Override
	        public void actionPerformed(final ActionEvent e) {
	        	dispose();
	        }
	
		 });
		 cloesButton.setMargin(new Insets(1, 1, 1, 1));
		 cloesButton.setFocusable(false);
		 this.add(cloesButton, constraints);
		 //constraints.gridx = 0;
		 //constraints.gridy++;
		 //constraints.weightx = 1.0f;
		 //constraints.weighty = 1.0f;
		 //constraints.insets = new Insets(5, 5, 5, 5);
		 //constraints.fill = GridBagConstraints.BOTH;
		 //JLabel messageLabel = new JLabel("<HtMl>"+message);
		 /*String mensajeConEstilo = "<html><body><div style=\"margin-top: 10spx; padding: 0pt 0.7em;\" class=\"ui-state-highlight ui-corner-all\">"+ 
			     "<p style=\"font-size: 20pt; color: #369;\">" +
			     "<span style=\"float: left; margin-right: 0.3em;\" class=\"ui-icon ui-icon-info\"></span>" +
			     message + "</p></div></body></html>";*/
		
		//JLabel messageLabel = new JLabel(mensajeConEstilo);
		//this.add(messageLabel, constraints);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setAlwaysOnTop(true);
		this.setVisible(true);

		new Thread(){	
		      @Override
		      public void run() {
		           try {
		                  Thread.sleep(10000); // time after which pop up will be disappeared.
		        	      dispose();
		           } catch (InterruptedException e) {
		           //} catch (Exception e) {	     
		                  e.printStackTrace();
		
		           }
		
		      };
		
		}.start();
	}



	
	/*public void mostrarMensaje(){
		String message = "Los documentos del expediente "+numeroexpediente+" fueron adjuntados correctamente.";
		String header = "CONTRATACION SENCILLA: ";
		
		this.setSize(300,125);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();// size of the screen
		
		Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());// height of the task bar
		
		this.setLocation(scrSize.width - this.getWidth(), scrSize.height - toolHeight.bottom - this.getHeight());
		this.setLayout(new GridBagLayout());		
		GridBagConstraints constraints = new GridBagConstraints();		
		constraints.gridx = 0;		
		constraints.gridy = 0;		
		constraints.weightx = 1.0f;		
		constraints.weighty = 1.0f;		
		constraints.insets = new Insets(5, 5, 5, 5);		
		constraints.fill = GridBagConstraints.BOTH;		
		JLabel headingLabel = new JLabel(header);	
		ImageIcon headingIcon = new ImageIcon("image_url");
		headingLabel .setIcon(headingIcon); // --- use image icon you want to be as heading image.		
		headingLabel.setOpaque(false);		
		this.add(headingLabel, constraints);		
		constraints.gridx++;		
		constraints.weightx = 0f;		
		constraints.weighty = 0f;		
		constraints.fill = GridBagConstraints.NONE;		
		constraints.anchor = GridBagConstraints.NORTH;		
		 JButton cloesButton = new JButton(new AbstractAction("X") {
	        @Override
	        public void actionPerformed(final ActionEvent e) {
	        	dispose();
	        }
	
		 });
		cloesButton.setMargin(new Insets(1, 4, 1, 4));
		cloesButton.setFocusable(false);
		this.add(cloesButton, constraints);
		constraints.gridx = 0;
		constraints.gridy++;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		JLabel messageLabel = new JLabel("<HtMl>"+message);
		this.add(messageLabel, constraints);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setAlwaysOnTop(true);
		this.setVisible(true);

		new Thread(){	
		      @Override
		      public void run() {
		           try {
		                  Thread.sleep(10000); // time after which pop up will be disappeared.
		        	      dispose();
		           } catch (InterruptedException e) {
		           //} catch (Exception e) {	     
		                  e.printStackTrace();
		
		           }
		
		      };
		
		}.start();
	}*/

	
	public String getTransferencias() {
		return transferencias;
	}



	public void setTransferencias(String transferencias) {
		this.transferencias = transferencias;
	}



	public String getCarpetaTemporal() {
		return carpetaTemporal;
	}



	public void setCarpetaTemporal(String carpetaTemporal) {
		this.carpetaTemporal = carpetaTemporal;
	}

		
}
