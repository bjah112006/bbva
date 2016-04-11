package com.ibm.bbva.ctacte.applet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.io.FileUtils;

import com.ibm.bbva.combo.SteppedComboBox;
import com.ibm.bbva.ctacte.applet.bean.TipoDocumento;
import com.ibm.bbva.ctacte.applet.constructor.AppApplet;
import com.ibm.bbva.ctacte.applet.constructor.Archivo;
import com.ibm.bbva.ctacte.applet.constructor.FiltroPDF;
import com.ibm.bbva.ctacte.applet.constructor.Parametros;
import com.ibm.bbva.ctacte.log.SimpleLogger;

public class AgregarDocumentos extends AppApplet {

	private static final long serialVersionUID = 1L;
	private static final SimpleLogger LOG = SimpleLogger.getLogger(AgregarDocumentos.class);
	
    private JPanel jPanel1 = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private JLabel jLabel3 = new JLabel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private SteppedComboBox jComboBox1 = new SteppedComboBox();
    private JTextField jTextField1 = new JTextField();
    private JButton jButton1 = new JButton();
    private JButton jButton2 = new JButton();
    private JPanel jPanel2 = new JPanel();
    private JButton jButton3 = new JButton();
    private JButton jButton4 = new JButton();
    private FlowLayout flowLayout1 = new FlowLayout();
    
    private File direccionActual;
    private static File DirScaneados; // Directorio Origen
    private static File DirTransferencias;
    private TipoDocumento documentoSel;
    

    public AgregarDocumentos() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(614, 300));
        jPanel1.setLayout(gridBagLayout1);
        jPanel1.setBackground(Color.white);
        jLabel1.setText("Agregar Documentos");
        jLabel2.setText("Tipo de Documento");
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setOpaque(true);
        jLabel2.setBackground(new Color(34, 102, 187));
        jLabel2.setForeground(Color.white);
        jLabel3.setText("Adjuntar Documento");
        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel3.setOpaque(true);
        jLabel3.setBackground(new Color(34, 102, 187));
        jLabel3.setForeground(Color.white);
        jButton1.setText("Examinar...");
        jPanel2.setBackground(Color.white);
        jPanel2.setLayout(flowLayout1);
        jButton3.setText("Escanear");
        jButton4.setText("Actualizar");
        //jButton4.setEnabled(false); // pidieron que el botón de "Actualizar" esté activo siempre
        jButton2.setText("Aceptar");
        Dimension d = new Dimension();
        d.width = 50;
        d.height= 100;
    	jComboBox1.setPreferredSize(d);
    	jComboBox1.setPrototypeDisplayValue("0123456789");
    	//jComboBox1.setPopupWidth(600);      
    	//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("prueba 6");
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                examinar(e);
            }
        });
        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aceptar(e);
            }
        });
        jButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                escanear(e);
            }
        });
        jButton4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizar(e);
            }
        });
        flowLayout1.setAlignment(0);
        jPanel1.add(jLabel1, new GridBagConstraints(0, 1, 4, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(5, 5, 5, 5), 0, 0));
        jPanel1.add(jLabel2, new GridBagConstraints(0, 2, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(5, 5, 5, 5), 0, 0));
        jPanel1.add(jLabel3, new GridBagConstraints(1, 2, 3, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(5, 0, 5, 5), 0, 0));
        jPanel1.add(jComboBox1, new GridBagConstraints(0, 3, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 5, 5), 0, 0));
        jPanel1.add(jTextField1, new GridBagConstraints(1, 3, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 5), 0, 0));
        jPanel1.add(jButton1, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 5), 0, 0));
        jPanel1.add(jButton2, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 5), 0, 0));
        jPanel2.add(jButton3, null);
        jPanel2.add(jButton4, null);
        jPanel1.add(jPanel2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 0, 0), 0, 0));
        this.getContentPane().add(jPanel1, BorderLayout.CENTER);
        jTextField1.setEditable(false);
        jComboBox1.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent evt) {
				Object obj = evt.getItem();
				if (obj != null && obj instanceof TipoDocumento &&
						evt.getStateChange() == ItemEvent.SELECTED) {
			        TipoDocumento td = (TipoDocumento) obj;
			        documentoSel = td;
			        LOG.info("documento seleccionado : "+documentoSel.getDescripcion());
			        jButton1.setEnabled(true);
			        jButton2.setEnabled(false);
				} else {
		        	jButton1.setEnabled(false);
		        	jButton2.setEnabled(false);
		        }
	        	jTextField1.setText("");
			}
        });
    }

	public void init () {
    	super.init();
    	LOG.info("init ()");
    	String strAppHist = getParameter(Parametros.APPLET_HISTORICO);
    	boolean appletHistorico = (strAppHist==null || "".equals(strAppHist)) ? false : Boolean.parseBoolean(strAppHist);
    	if (appletHistorico) {
    		jButton3.setText("Escanear Histórico");
    		jLabel1.setText("Agregar Documentos Históricos");
    	}
    	String tramaDocumentos = getParameter(Parametros.TRAMA_DOCUMENTOS);
    	actualizarDocumentos (tramaDocumentos);
    	jButton1.setEnabled(false);
    	jButton2.setEnabled(false);
    	jTextField1.setText("");
    }
    
    private void actualizarDocumentos (String tramaDocumentos) {
    	LOG.info("actualizarDocumentos (String tramaDocumentos)");
    	LOG.info("tramaDocumentos : "+tramaDocumentos);
    	jComboBox1.removeAllItems();
    	boolean existeItems = false;
    	if (tramaDocumentos!=null) {
	    	StringTokenizer tokenizer = new StringTokenizer (tramaDocumentos, ";");
	    	while (tokenizer.hasMoreTokens()) {
	    		TipoDocumento td = new TipoDocumento ();
	    		td.setCodigoDocumento(tokenizer.nextToken());
	    		td.setDescripcion(tokenizer.nextToken());
	    		jComboBox1.addItem(td);
	    		existeItems = true;
	    	}
    	}
    	jComboBox1.setSelectedItem(null);
    	//
    	if (existeItems) {
    		jComboBox1.setEnabled(true);
        	jButton3.setEnabled(true);
    	} else {
    		jComboBox1.setEnabled(false);
        	jButton3.setEnabled(false);
    	}
//    	Dimension d = jComboBox1.getPreferredSize();
//    	jComboBox1.setPreferredSize(new Dimension(100, d.height));
//    	jComboBox1.setPopupWidth(d.width);
    	//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("CAMBIANDO TAMAÑO DEL COMBO");
    	
    }

    private void examinar(ActionEvent e) {
    	LOG.info("examinar(ActionEvent e)");
    	JFileChooser jfc;
    	if (direccionActual == null) {
    		jfc = new JFileChooser ();
    	} else {
    		jfc = new JFileChooser (direccionActual);
    	}
    	jfc.setAcceptAllFileFilterUsed(false);
    	jfc.setFileFilter(new FiltroPDF());
    	jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    	int opcion = jfc.showOpenDialog(this);
    	if (opcion == JFileChooser.APPROVE_OPTION) {
    		direccionActual = jfc.getSelectedFile();
    		jTextField1.setText(direccionActual.getAbsolutePath());
    		jButton2.setEnabled(true);
    	}
    }

    private void aceptar(ActionEvent e) {
    	LOG.info("aceptar(ActionEvent e)");
    	try {
    		String codigoDoc = documentoSel.getCodigoDocumento();
    		String formOrigen = getParameter(Parametros.FORM_ORIGEN);
//    		String nomDocumento = codCentralCliente+"_"+codOperacion+"_"+codigoDoc+formOrigen;
    		
    		String nomDocumento =null;
    		
    		if (formOrigen != null && !formOrigen.equalsIgnoreCase("")){
    			nomDocumento = codigoDoc +"-"+formOrigen; 
    		}else	
    			nomDocumento = codigoDoc;
    		
    		long tamanoArchivo = FileUtils.sizeOf(direccionActual); // en bytes
    		long tamanoMaximo = Long.parseLong(getParameter(Parametros.TAMANO_MAXIMO_PDF))*1000000; // el parámetro está en MB
    		if (tamanoArchivo < tamanoMaximo) {
    			Archivo.getInstance().copiarArchivoATranferencias(direccionActual, nomDocumento);
    			jButton1.setEnabled(false);
            	jButton2.setEnabled(false);
            	jTextField1.setText("");
            	jComboBox1.removeItem(documentoSel);
            	documentoSel = null;
            	jComboBox1.setSelectedItem(null);
            	ejecutarFuncion("actualizarDocumentos", codigoDoc);
    		} else {
    			LOG.error("El tamaño del archivo es de "+tamanoArchivo+" bytes. El tamaño máximo permitido es de "+tamanoMaximo+" bytes");
    			mensaje("No se puede adjuntar el archivo porque excede el tamaño máximo de "+getParameter(Parametros.TAMANO_MAXIMO_PDF)+" MB");
    		}
        	
		} catch (IOException e1) {
			LOG.error("No se puede adjuntar el archivo", e1);
			mensaje("No se puede adjuntar el archivo");
		}
    }
    
    private void actualizar(ActionEvent e) {
		LOG.info ("actualizar(ActionEvent e)");
    	StringBuilder sb = new StringBuilder ();
		boolean esPrimero = true;
    	for (int i=0,n=jComboBox1.getItemCount(); i<n; i++) {
    		TipoDocumento td = (TipoDocumento)jComboBox1.getItemAt(i);
    		String codDoc = td.getCodigoDocumento();
    		if (Archivo.getInstance().actualizarEscaneado(codDoc)) {
	    		if (!esPrimero) {
	    			sb.append(",");
	    		}
	    		sb.append(codDoc);
	    		esPrimero = false;
    		}
    	}
    	ejecutarFuncion("actualizarDocumentos", sb.toString());
    	LOG.info("Va a ejecutar la comparacion de archivos");    	
//    	ejecutarMatch();
    	LOG.info("Ejecuto la comparacion de archivos");
	}

    private void escanear(ActionEvent e) {
    	LOG.info("escanear(ActionEvent e)");
    	try {
    		//Archivo.getInstance().escanear(getParameter(Parametros.PATH_API_ESCANEO));
    		String flagEscaneoWeb = getParameter(Parametros.FLAG_ESCANEO_WEB);
    		    		
    		if (flagEscaneoWeb != null && flagEscaneoWeb.equals("1")) {
    			StringBuilder sb = new StringBuilder();
    			sb.append(getParameter(Parametros.URL_ESCANEO_WEB));
    			sb.append("?idEmpresa=");
    			sb.append(getParameter(Parametros.ID_EMPRESA_ESCANEO_WEB));
    			sb.append("&idSistema=");
    			sb.append(getParameter(Parametros.ID_SISTEMA_ESCANEO_WEB));
    			sb.append("&txLogin=");
    			sb.append(getParameter(Parametros.CODIGO_EMPLEADO_ESCANEO_WEB));
    			URL url = new URL(sb.toString());
    			
    			Archivo.getInstance().escanearWeb(url, this);
    		} else {
    			Archivo.getInstance().escanear(ConstantesApplet.RUTA_API_ESCANEO);
    		}
	    	//jButton4.setEnabled(true); // ya no es necesario porque siempre va a estar activo
    	} catch (Exception ex) {
    		LOG.error("No se puede escanear los archivos", ex);
    		mensaje("No se puede escanear los archivos");
		}
	}

	public static File getDirScaneados() {
		return DirScaneados;
	}

	public static void setDirScaneados(File dirScaneados) {
		DirScaneados = dirScaneados;
	}

	public static File getDirTransferencias() {
		return DirTransferencias;
	}

	public static void setDirTransferencias(File dirTransferencias) {
		DirTransferencias = dirTransferencias;
	}
}
