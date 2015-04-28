package com.ibm.bbva.ctacte.applet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.ibm.bbva.ctacte.applet.constructor.Archivo;
import com.ibm.bbva.ctacte.applet.constructor.ArchivoApplet;
import com.ibm.bbva.ctacte.ftp.FTPArchivos;
import com.ibm.bbva.ctacte.ftp.FTPListener;
import com.ibm.bbva.ctacte.ftp.FTPRunnable;
import com.ibm.bbva.ctacte.log.SimpleLogger;

public class ReenvioDocumentosFTP extends ArchivoApplet implements FTPListener {
	
	private static final SimpleLogger LOG = SimpleLogger.getLogger(ReenvioDocumentosFTP.class);
	
	//private JLabel texto1 = new JLabel ();
	private JProgressBar progressBar = new JProgressBar ();
	private JButton botonReenviar = new JButton();
	
	private int bytes;
	private String host;
    private String user;
    private String password;
    private int periodo;
    private int tasaKBytes;
	private String numeroexpediente;
	private String logPath;
    private int tamanioArchivo;
    private String carpetaFTP;

	public ReenvioDocumentosFTP() {
		bytes = 0;
		
		this.setSize(new Dimension(300, 70));
		JPanel pane = new JPanel();
		pane.setBackground(Color.white);
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//texto1.setText("Archivo");
		//texto1.setHorizontalAlignment(JLabel.CENTER);
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.NONE;
		c.ipadx = 0;
		c.ipady = 0;
		c.insets = new Insets(5,0,10,0);
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		c.weightx = 1.0;
		c.weighty = 0.0;
		botonReenviar.setText("Reenviar");
		botonReenviar.setEnabled(false);
		botonReenviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                upload(e);
            }
        });
		pane.add(botonReenviar, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 0;
		c.ipady = 0;
		c.insets = new Insets(0,0,0,0);
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1.0;
		c.weighty = 1.0;
		progressBar.setStringPainted(true);
		progressBar.setVisible(false);
		pane.add(progressBar, c);
		
		this.getContentPane().add(pane, BorderLayout.CENTER);
	}
	
	@Override
	public void init() {
		super.init();
		LOG.info("Entro LoadParameters");
        loadParameters();
	}
	
	public void loadParameters() {
        this.logPath = getParameter("pathLog");
        LOG.info(logPath);  
        this.host=getParameter("host");
        LOG.info(host);
        this.user = getParameter("user");
        LOG.info(user);
        this.password = getParameter("password");
        LOG.info(password);
        this.periodo = Integer.parseInt((getParameter("periodo") == null ? "0" : getParameter("periodo")));
        LOG.info(periodo);
        this.tasaKBytes = Integer.parseInt(getParameter("tasaKBytes") == null ? "0" : getParameter("tasaKBytes"));
        LOG.info(tasaKBytes);
        carpetaFTP = getParameter("carpetaFTP");
        LOG.info(carpetaFTP);
        this.numeroexpediente = getParameter("prefix");
        LOG.info(numeroexpediente);
    }
	
	public String obtenerListaDocsBackup() {
		/* Necesito ejecución privilegiada porque por defecto métodos invocados 
		 * desde Javascript se ejecutan como Sandbox */
		String result = (String) AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				StringBuilder arch = new StringBuilder();
				File[] archs = Archivo.getInstance().obtenerListaDocsBackup(numeroexpediente);
				LOG.info("numero de archivos:"+archs.length);
				if (archs != null && archs.length > 0) {
					for (File f : archs) {
						arch.append(f.getName());
						arch.append(",");
					}
				}
				if (!arch.toString().trim().equals("")) {
					botonReenviar.setEnabled(true);
					return arch.substring(0, arch.length() - 1);
				} else {
					botonReenviar.setEnabled(false);
					return "";
				}
			}
		});
		return result;
	}
	
	public void abrirDocumentoBackup(final String nombreDoc) throws IOException {
		AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				try {
					Archivo.getInstance().abrirArchivoBackup(nombreDoc);
				} catch (IOException e) {
					LOG.error("No se pudo abrir el archivo.", e);
				}
				return null;
			}
		});
	}
	
	public void upload(ActionEvent e) {
		progressBar.setVisible(true);
    	LOG.info("ejecutarSubidaDeArchivos " + host);
        File[] arrFile = Archivo.getInstance().obtenerListaDocsBackup(numeroexpediente);
        LOG.info("TAMAÑO : " + arrFile.length);
        if (arrFile.length == 0) {
        	/*LOG.info("Llamando al JS: accionCerrar()");
        	cerrarVentana();*/
        	// TODO decir que no hay nada para subir
        } else {
            for (File arch : arrFile) {
            	String nombreFTP = arch.getName();
            	nombreFTP = nombreFTP.substring(0, nombreFTP.lastIndexOf("."));
                LOG.info("Poniendo en cola : " + arch.getName());
                FTPArchivos.agregarArchivo(arch, host, user, nombreFTP, password, 
                		periodo, tasaKBytes, carpetaFTP, 1);
                LOG.info("Salio enviar archivo");
            }
            Thread thread = new Thread (new FTPRunnable(this));
            thread.start();
        }
    }

	public void cerrarVentana() {
		// TODO Apéndice de método generado automáticamente
	}

	public void cerrarVentana(List<String> archivosError) {
		// TODO Apéndice de método generado automáticamente		
	}

	public void limpiarTransferDir() {
		// no se va a limpiar la carpeta Backups al terminar
	}

	public void nuevoArchivo(String archivo) {
		//texto1.setText(archivo);
	}

	public void setAvance(int bytesLeidos) {
		bytes += bytesLeidos;
		progressBar.setValue(bytes);
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
