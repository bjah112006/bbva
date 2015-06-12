package com.ibm.bbva.app.applet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import netscape.javascript.JSObject;

import org.apache.commons.io.FileUtils;

import com.ibm.bbva.app.applet.bean.TipoDocumento;
import com.ibm.bbva.app.applet.constructor.AppApplet;
import com.ibm.bbva.app.applet.constructor.Archivo;
import com.ibm.bbva.app.applet.constructor.FiltroPDF;
import com.ibm.bbva.app.applet.constructor.Parametros;
import com.ibm.bbva.app.log.SimpleLogger;
import com.ibm.bbva.combo.WideComboBox;

public class AgregarDocumentos extends AppApplet {

	private static final long serialVersionUID = 1L;
	private static final SimpleLogger LOG = SimpleLogger.getLogger(AgregarDocumentos.class);
	
    private JPanel jPanel1 = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private JLabel jLabel3 = new JLabel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    //private SteppedComboBox jComboBox1 = new SteppedComboBox();
    //private SteppedComboBox jComboBox2 = new SteppedComboBox();
    private WideComboBox jComboBox1 = new WideComboBox();
    private WideComboBox jComboBox2 = new WideComboBox();
    private WideComboBox jComboBox3 = new WideComboBox(); // Modificacion tipo de documento.
    //private JComboBox jComboBox1 = new JComboBox();
    //private JComboBox jComboBox2 = new JComboBox();
    private JTextField jTextField1 = new JTextField();
    private JButton jButton1 = new JButton();
    private JButton jButton2 = new JButton();
    private JPanel jPanel2 = new JPanel();
    private JButton jButton3 = new JButton();
    private JButton jButton4 = new JButton();
    private JButton jButton5 = new JButton();
    private JButton jButton6 = new JButton(); // Modificacion tipo de documento.
    private FlowLayout flowLayout1 = new FlowLayout();
    
    private File direccionActual;
    private static File DirScaneados; // Directorio Origen
    private static File DirTransferencias;
    private TipoDocumento documentoSel;
    private TipoDocumento documentoSelCambio;
    private TipoDocumento tipoDocumentoMC;

    public AgregarDocumentos() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        //this.setSize(new Dimension(614, 300));
        this.setSize(new Dimension(700, 160));
        flowLayout1.setAlignment(FlowLayout.RIGHT);
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
        jButton3.setText("Escanear");
        //jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ibm/bbva/app/icons/escaner2.JPG")));
        jButton4.setText("Actualizar");
        //jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ibm/bbva/app/icons/refresh.jpg"))); // TODO icono de actualizar
        //jButton4.setEnabled(false);
        jButton2.setText("Aceptar");
        jButton5.setText("Guardar Cambio");
        jButton6.setText("Guardar Cambio");// Modificacion Tipo Documento
        
        Dimension d = new Dimension();
        d.width = 50;
        d.height= 100;
    	jComboBox1.setPreferredSize(d);
    	jComboBox1.setPrototypeDisplayValue("0123456789");
        Dimension d2 = new Dimension();
        d2.width = 300;
        d2.height= 24;
        jComboBox2.setPreferredSize(d2);
        jComboBox2.setPrototypeDisplayValue("0123456789");
        Dimension d3 = new Dimension();
        d3.width = 500;
        d3.height= 24;
        jComboBox3.setPreferredSize(d3);
        jComboBox3.setPrototypeDisplayValue("0123456789");
        
    	jPanel2.setBackground(Color.white);
        jPanel2.setLayout(flowLayout1);
        jPanel2.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        jPanel2.add(jButton3, null);
        jPanel2.add(jButton4, null);
        jPanel2.add(jComboBox2, null);
        jPanel2.add(jButton5, null);
    	//jComboBox1.setPopupWidth(600);        
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
        jButton5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	modificar(e);
            }
        });
        jButton6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	modificarTipoDocumento(e);            	
            }
        });
        
        flowLayout1.setAlignment(0);
        jPanel1.add(jPanel2, new GridBagConstraints(0, 0, 4, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
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
        
        // MOdificacion Tipo de Documento
        jPanel1.add(jComboBox3, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        jPanel1.add(jButton6, new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 5), 0, 0));
    
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
        jComboBox2.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent evt) {
				Object obj = evt.getItem();
				if (obj != null && obj instanceof TipoDocumento &&
						evt.getStateChange() == ItemEvent.SELECTED) {
			        TipoDocumento td = (TipoDocumento) obj;
			        documentoSelCambio = td;
			        LOG.info("documento seleccionado cambio: "+documentoSelCambio.getDescripcion());
			        jButton5.setEnabled(true);
				} else {
		        	jButton5.setEnabled(false);
		        }
			}
        });
        jComboBox3.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent evt) {
				Object obj = evt.getItem();
				if (obj != null && obj instanceof TipoDocumento &&
						evt.getStateChange() == ItemEvent.SELECTED) {
			        TipoDocumento td = (TipoDocumento) obj;
			        tipoDocumentoMC = td;
			        LOG.info("tipo documento seleccionado cambio: " + tipoDocumentoMC.getDescripcion());
			        jButton6.setEnabled(true);
				} else {
		        	jButton6.setEnabled(false);
		        }
			}
        });
    }

	public void init () {
    	super.init();
    	LOG.info("init ()");
    	/*String strAppHist = getParameter(Parametros.APPLET_HISTORICO);
    	boolean appletHistorico = (strAppHist==null || "".equals(strAppHist)) ? false : Boolean.parseBoolean(strAppHist);
    	if (appletHistorico) {
    		jButton3.setText("Escanear Histórico");
    		jLabel1.setText("Agregar Documentos Históricos");
    	}*/
    	String tipoVisualizacion = getParameter(Parametros.TIPO_VISUALIZACION);
    	mostrarPaneles(tipoVisualizacion);
    	
    	String tramaDocumentos = getParameter(Parametros.TRAMA_DOCUMENTOS);
    	String tramaDocumentosReutilizables = getParameter(Parametros.TRAMA_DOCUMENTOS_REUTILIZABLES);
    	String tramaDocumentosContent = getParameter(Parametros.TRAMA_DOCUMENTOS_CONTENT);
    	String tramaTipoDocumentos = getParameter(Parametros.TRAMA_TIPO_DOCUMENTOS);    		
    	actualizarDocumentos(tramaDocumentos, tramaDocumentosReutilizables, tramaDocumentosContent, tramaTipoDocumentos);
    	
    	jButton1.setEnabled(false);
    	jButton2.setEnabled(false);
    	jButton5.setEnabled(false);
    	jTextField1.setText("");
    	
    	System.out.println("devolviendo el foco");
    	ejecutarFuncion("devolverFocoRegistrarExp"); // devolver el foco en la pantalla de registro de expedicnte
    }
	
	private void mostrarPaneles(String tipoVisualizacion) {
		if (tipoVisualizacion.equals("1")) {
			jComboBox2.setVisible(false);
			jButton5.setVisible(false);
			jComboBox3.setVisible(false);
			jButton6.setVisible(false);
		} else {
			if (tipoVisualizacion.equals("2")) {
				//jComboBox2.setVisible(true);
				jComboBox2.setVisible(false); //validar si ya no se usa
				//jButton5.setVisible(true);
				jButton5.setVisible(false); //validar si ya no se usa
				jComboBox3.setVisible(true);
				jButton6.setVisible(true);
			} else {
				// TODO ocultar todo
				jPanel2.setVisible(false);
			}
		}
	}
    
    private void actualizarDocumentos(String tramaDocumentos, String tramaDocumentosReutilizables, String tramaDocumentosContent, String tramaTipoDocumentos) {
    	//LOG.info("actualizarDocumentos (String tramaDocumentos)");
    	//LOG.info("tramaDocumentos : "+tramaDocumentos);
    	jComboBox1.removeAllItems();
    	jComboBox2.removeAllItems();
    	boolean existeItems = false;
    	String tramaDocumentosTransferencias = Archivo.getInstance().obtenerListaDocsTransferencias();
    	List<TipoDocumento> lstTipoDocumento = new ArrayList<TipoDocumento>();
    	List<TipoDocumento> lstTipoDocumentoReutilizable = new ArrayList<TipoDocumento>();
    	List<TipoDocumento> lstTipoDocumentoContent = new ArrayList<TipoDocumento>();
    	List<TipoDocumento> lstTipoDocumentoTransferencias = new ArrayList<TipoDocumento>();
    	List<TipoDocumento> lstTipoDocumentoMC = new ArrayList<TipoDocumento>();
    	if (tramaDocumentosReutilizables!=null) {
    		StringTokenizer tokenizer = new StringTokenizer(tramaDocumentosReutilizables, ",");
    		while (tokenizer.hasMoreTokens()) {
    			TipoDocumento td = new TipoDocumento();
	    		td.setCodigoDocumento(tokenizer.nextToken());
	    		td.setDescripcion(tokenizer.nextToken());
	    		lstTipoDocumentoReutilizable.add(td);
    		}
    	}
    	if (tramaDocumentosContent!=null) {
    		StringTokenizer tokenizer = new StringTokenizer(tramaDocumentosContent, ",");
    		while (tokenizer.hasMoreTokens()) {
    			TipoDocumento td = new TipoDocumento();
	    		td.setCodigoDocumento(tokenizer.nextToken());
	    		lstTipoDocumentoContent.add(td);
    		}
    	}
    	if (tramaDocumentosTransferencias!=null) {
    		StringTokenizer tokenizer = new StringTokenizer(tramaDocumentosTransferencias, ",");
    		while (tokenizer.hasMoreTokens()) {
    			TipoDocumento td = new TipoDocumento();
	    		td.setCodigoDocumento(tokenizer.nextToken());
	    		lstTipoDocumentoTransferencias.add(td);
    		}
    	}
    	if (tramaDocumentos!=null) {
    		System.out.println("tramaDocumentos:"+tramaDocumentos);
    		StringTokenizer tokenizer = new StringTokenizer(tramaDocumentos, ",");
    		while (tokenizer.hasMoreTokens()) {
    			TipoDocumento td = new TipoDocumento();
	    		td.setCodigoDocumento(tokenizer.nextToken());
	    		td.setDescripcion(tokenizer.nextToken());
	    		td.setObligatorio(tokenizer.nextToken().equals("1"));
	    		if (!lstTipoDocumentoReutilizable.contains(td) && !lstTipoDocumentoContent.contains(td) && !lstTipoDocumentoTransferencias.contains(td)) {
	    			lstTipoDocumento.add(td);
	    		}
    		}
    	}
    	Collections.sort(lstTipoDocumento);
    	if (lstTipoDocumento.size() > 0) {
    		existeItems = true;
	    	for (TipoDocumento td : lstTipoDocumento) {
	    		
	    		TipoDocumento tdAux = new TipoDocumento();
	    		tdAux.setCodigoDocumento(td.getCodigoDocumento());
	    		tdAux.setDescripcion(td.getCodigoDocumento()+"-"+td.getDescripcion());
	    		tdAux.setObligatorio(td.isObligatorio());
	    		jComboBox1.addItem(tdAux);
	    		jComboBox2.addItem(tdAux);
	    	}
    	}
    	
    	//Modificacion tipo de documentos
		if (tramaTipoDocumentos != null) {
			System.out.println("tramaTipoDocumentos:" + tramaTipoDocumentos);
			StringTokenizer tokenizer = new StringTokenizer(
					tramaTipoDocumentos, ",");
			while (tokenizer.hasMoreTokens()) {
				TipoDocumento td = new TipoDocumento();
				td.setCodigoDocumento(tokenizer.nextToken());
				td.setDescripcion(tokenizer.nextToken());
				lstTipoDocumentoMC.add(td);
			}
		}
    	Collections.sort(lstTipoDocumentoMC);
    	if (lstTipoDocumentoMC.size() > 0) {
	    	for (TipoDocumento td : lstTipoDocumentoMC) {	    		
	    		TipoDocumento tdAux = new TipoDocumento();
	    		tdAux.setCodigoDocumento(td.getCodigoDocumento());
	    		tdAux.setDescripcion(td.getCodigoDocumento()+"-"+td.getDescripcion());	    		
	    		jComboBox3.addItem(tdAux);	    		
	    	}
//	    	jComboBox3.setEnabled(true);
//	    	jButton6.setEnabled(true);
    	}else{
//    		jComboBox3.setEnabled(false);
//	    	jButton6.setEnabled(false);
    	}
    	jComboBox3.setEnabled(false);
    	jButton6.setEnabled(false);
    	
    	jComboBox1.setSelectedItem(null);
    	jComboBox2.setSelectedItem(null);
    	jComboBox3.setSelectedItem(null);
    	//
    	if (existeItems) {
    		jComboBox1.setEnabled(true);
    		jComboBox2.setEnabled(true);
        	jButton3.setEnabled(true);
        	jButton5.setEnabled(true);
    	} else {
    		jComboBox1.setEnabled(false);
    		jComboBox2.setEnabled(false);
        	//jButton3.setEnabled(false);
        	jButton5.setEnabled(false);
    	}
    	
//    	Dimension d = jComboBox1.getPreferredSize();
//    	jComboBox1.setPreferredSize(new Dimension(100, d.height));
//    	jComboBox1.setPopupWidth(d.width);
    	//System.out.println("CAMBIANDO TAMAÑO DEL COMBO");
    	
    	
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
    		//String formOrigen = getParameter(Parametros.FORM_ORIGEN);
    		//String nomDocumento = codCentralCliente+"_"+codOperacion+"_"+codigoDoc+formOrigen;
    		
    		String nomDocumento =null;
    		
    		/*if (formOrigen != null && !formOrigen.equalsIgnoreCase("")){
    			nomDocumento = codigoDoc +"-"+formOrigen; 
    		}else*/	
    		nomDocumento = codigoDoc;
    		
    		long tamanoArchivo = FileUtils.sizeOf(direccionActual); // en bytes
    		long tamanoMaximo = Long.parseLong(getParameter(Parametros.TAMANO_MAXIMO_PDF))*1000000; // el parámetro está en MB
    		if (tamanoArchivo < tamanoMaximo) {
	    		Archivo.getInstance().copiarArchivoATranferencias(direccionActual, nomDocumento);
				jButton1.setEnabled(false);
	        	jButton2.setEnabled(false);
	        	jTextField1.setText("");
	        	TipoDocumento documentoSelTemp = documentoSel; // se dispara el listener antes de remover y cambiaba la referencia a documentoSel
	        	jComboBox1.removeItem(documentoSelTemp);
	        	jComboBox2.removeItem(documentoSelTemp);
	        	documentoSel = null;
	        	documentoSelCambio = null;
	        	jComboBox1.setSelectedItem(null);
	        	jComboBox2.setSelectedItem(null);
	        	ejecutarFuncion("actualizarPanelDocumentos", Archivo.getInstance().obtenerListaDocsTransferencias());
    		} else{
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
    	//StringBuilder sb = new StringBuilder ();
		//boolean esPrimero = true;
		List<TipoDocumento> lstTipoDocumentoEscaneados = new ArrayList<TipoDocumento>();
    	for (int i=0,n=jComboBox1.getItemCount(); i<n; i++) {
    		TipoDocumento td = (TipoDocumento)jComboBox1.getItemAt(i);
    		String codDoc = td.getCodigoDocumento();
    		System.out.println("codDoc:::::"+codDoc);
    		File origen = Archivo.getInstance().obtenerArchivo(codDoc);
    		System.out.println("origen:::::::"+origen.getAbsolutePath());
    		if (origen.exists()) {
	    		long tamanoArchivo = FileUtils.sizeOf(origen); // en bytes
	    		System.out.println("tamanoArchivo:::"+tamanoArchivo);
	    		long tamanoMaximo = Long.parseLong(getParameter(Parametros.TAMANO_MAXIMO_PDF))*1000000; // el parámetro está en MB
	    		System.out.println("tamanoMaximo:::"+tamanoMaximo);
	    		if (tamanoArchivo < tamanoMaximo) {
		    		if (Archivo.getInstance().actualizarEscaneado(codDoc)) {
			    		/*if (!esPrimero) {
			    			sb.append(",");
			    		}
			    		sb.append(codDoc);
			    		esPrimero = false;*/
		    			lstTipoDocumentoEscaneados.add(td);
		    		}
	    		} else{
	    			LOG.error("El tamaño del archivo " + codDoc + Archivo.getInstance().EXTENSION_ARCHIVO + " es de " + tamanoArchivo + " bytes. El tamaño máximo permitido es de " + tamanoMaximo + " bytes");
	    			mensaje("No se puede adjuntar el archivo " + codDoc + Archivo.getInstance().EXTENSION_ARCHIVO + " porque excede el tamaño máximo de " + getParameter(Parametros.TAMANO_MAXIMO_PDF) + " MB");
	    		}

    		}
    	}
    	for (TipoDocumento docEscaneado : lstTipoDocumentoEscaneados) {
    		jComboBox1.removeItem(docEscaneado);
    	}
    	ejecutarFuncion("actualizarPanelDocumentos", Archivo.getInstance().obtenerListaDocsTransferencias());
    	//ejecutarFuncion("actualizarDocumentos", sb.toString());
    	//LOG.info("Va a ejecutar la comparacion de archivos");    	
    	//ejecutarMatch();
    	//LOG.info("Ejecuto la comparacion de archivos");
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
	    	jButton4.setEnabled(true);
    	} catch (Exception ex) {
    		LOG.error("No se puede escanear los archivos", ex);
    		mensaje("No se puede escanear los archivos");
		}
	}
    
    private void modificar(ActionEvent e) {
    	LOG.info("modificar(ActionEvent e)");
    	try {
    		String codigoDocNuevo = documentoSelCambio.getCodigoDocumento();
    		JSObject window = JSObject.getWindow(this);
    		String tramaDocumentoCambio = (String) window.eval("obtenerTramaDocumentoCambio()");
    		LOG.info("Se obtuvo trama de Documento a modificar: "+tramaDocumentoCambio);
    		if (tramaDocumentoCambio != null && !tramaDocumentoCambio.trim().equals("")) {
	    		StringTokenizer tokenizer = new StringTokenizer(tramaDocumentoCambio, ",");
	    		if (tokenizer.countTokens() == 2) {
		    		int i=0;
		    		String codigoDocAntiguo = "";
		    		String flagCm = "0";
		    		while (tokenizer.hasMoreTokens()) {
		    			String token = tokenizer.nextToken();
		    			switch (i) {
							case 0:
								codigoDocAntiguo = token;
								break;
							case 1:
								flagCm = token;
								break;
							default:
								break;
						}
		    			i++;
		    		}
		    		if (flagCm.equals("1")) {
		    			ejecutarFuncion("modificarDocumentoContent", codigoDocAntiguo+","+codigoDocNuevo);
		    		} else {
		    			File archivoOrigen = Archivo.getInstance().ubicarArchivo(codigoDocAntiguo);
		    			Archivo.getInstance().moverArchivoATranferencias(archivoOrigen, codigoDocNuevo);
		    			ejecutarFuncion("actualizarAppletYPanelDocumentos", Archivo.getInstance().obtenerListaDocsTransferencias());
		    		}
	    		} else {
	    			mensaje("Debe seleccionar el tipo documento");
	    		}
    		} else {
    			mensaje("Debe seleccionar el tipo documento");
    		}
			/*Archivo.getInstance().copiarArchivoATranferencias(direccionActual, nomDocumento);
			jButton1.setEnabled(false);
        	jButton2.setEnabled(false);
        	jTextField1.setText("");
        	TipoDocumento documentoSelTemp = documentoSel; // se dispara el listener antes de remover y cambiaba la referencia a documentoSel
        	jComboBox1.removeItem(documentoSelTemp);
        	jComboBox2.removeItem(documentoSelTemp);
        	documentoSel = null;
        	documentoSelCambio = null;
        	jComboBox1.setSelectedItem(null);
        	jComboBox2.setSelectedItem(null);
        	ejecutarFuncion("actualizarPanelDocumentos", Archivo.getInstance().obtenerListaDocsTransferencias());*/
        	
		} catch (IOException e1) {
			LOG.error("No se puede adjuntar el archivo", e1);
			mensaje("No se puede adjuntar el archivo");
		}
    }
    
    public void modificarTipoDocumento(ActionEvent e){
    	LOG.info("modificarTipoDocumento(ActionEvent e)");
    	try {
    		LOG.info("tipoDocumentoMC:" + tipoDocumentoMC);
    		int confirmacion = JOptionPane.showConfirmDialog(this,    				
    				"¿Desea modificar el Tipo de Documento?",
    				"Tipo de Documento",
    				JOptionPane.YES_NO_OPTION);
    		if (confirmacion == ConstantesApplet.MODIFICAR_TIPO_DOCUMENTO) {
    			String codigoDocNuevo = tipoDocumentoMC.getCodigoDocumento();
        		JSObject window = JSObject.getWindow(this);
        		String tramaDocumentoCambio = (String) window.eval("obtenerTramaDocumentoCambio()");
        		LOG.info("Se obtuvo trama de Documento a modificar: "+tramaDocumentoCambio);
        		if (tramaDocumentoCambio != null && !tramaDocumentoCambio.trim().equals("")) {
        			System.out.println("tramaDocumentoCambio:" + tramaDocumentoCambio);
    	    		StringTokenizer tokenizer = new StringTokenizer(tramaDocumentoCambio, ",");
    	    		if (tokenizer.countTokens() == 2) {
    		    		int i=0;
    		    		String codigoDocAntiguo = "";
    		    		String flagCm = "0";
    		    		while (tokenizer.hasMoreTokens()) {
    		    			String token = tokenizer.nextToken();
    		    			switch (i) {
    							case 0:
    								codigoDocAntiguo = token;
    								break;
    							case 1:
    								flagCm = token;
    								break;
    							default:
    								break;
    						}
    		    			i++;
    		    		}
    		    		System.out.println("flagCm:" + flagCm);
    		    		if (flagCm.equals("1")) {
    		    			ejecutarFuncion("modificarDocumentoContent", codigoDocAntiguo+","+codigoDocNuevo);
    		    		} else {
    		    			File archivoOrigen = Archivo.getInstance().ubicarArchivo(codigoDocAntiguo);
    		    			Archivo.getInstance().moverArchivoATranferencias(archivoOrigen, codigoDocNuevo);
    		    			ejecutarFuncion("actualizarAppletYPanelDocumentos", Archivo.getInstance().obtenerListaDocsTransferencias());
    		    		}
    	    		} else {
    	    			mensaje("Debe seleccionar el tipo documento");
    	    		}
        		} else {
        			mensaje("Debe seleccionar el tipo documento");
        		}
    			
			}else{
				jComboBox3.setSelectedItem(null);
				jButton6.setEnabled(false);
			}
			
		} catch (Exception e2) {
			LOG.error("No se puede modificar el tipo de documento");
			mensaje("No se puede modificar el tipo de documento");
		}
    }
    
    public String verificarCompleto(String trama) {
    	System.out.println("En el metodo del applet: verificarCompleto");
    	System.out.println("trama:::::" + trama);
    	String retorno = "";
    	String documentosFaltantes = "";
    	int totalDocFaltantes = jComboBox1.getItemCount();
    	System.out.println("totalDocFaltantes:" + totalDocFaltantes);
    	if (totalDocFaltantes == 0) {
    		retorno = "1";
    	} else {
    		if(trama == null || "".equals(trama)){
	    		retorno = "1";
	    		for (int i=0; i<totalDocFaltantes; i++) {
	    			System.out.println("for totalDocFaltantes");
	    			System.out.println("obligatorio:" +((TipoDocumento) jComboBox1.getItemAt(i)).getCodigoDocumento());
	    			System.out.println("obligatorio:" +((TipoDocumento) jComboBox1.getItemAt(i)).isObligatorio());
	    			if (((TipoDocumento) jComboBox1.getItemAt(i)).isObligatorio()) {
	    				System.out.println("Entro If");
	    				String[] valores = ((TipoDocumento) jComboBox1.getItemAt(i)).getDescripcion().split("-");
    					if (valores.length == 2)
    						documentosFaltantes = documentosFaltantes + valores[1].trim() +  "\n";
    					else {
    						String datoConcat = "";
    						int index = 0;
    						for (String valor : valores) {
    							if (index > 0) {
    								if (datoConcat.equals(""))
										datoConcat = valor;
									else
										datoConcat = datoConcat + " - " + valor;
    							}
								index++;
							}
    						documentosFaltantes = documentosFaltantes + datoConcat.trim() +  "\n";
    					}
	    			}
	    		}
	    		if (documentosFaltantes.trim().equals("")) {
	    			retorno = "1";
	    		} else {
	    			retorno = documentosFaltantes;
	    		}
    		}
    		else{
    			retorno = "1";
    			String tramaDocs[] = trama.split(",");
    			for (int i=0; i<totalDocFaltantes; i++) {
    				if(((TipoDocumento) jComboBox1.getItemAt(i)).isObligatorio()){
	    				boolean cargado = false;
	    				for (int j=0; j<tramaDocs.length; j++) {
	    					if(((TipoDocumento) jComboBox1.getItemAt(i)).getCodigoDocumento().equals(tramaDocs[j])){
	    						cargado = true;
	    						break;
	    					}
	    				}
	    				if(!cargado){
	    					String[] valores = ((TipoDocumento) jComboBox1.getItemAt(i)).getDescripcion().split("-");
	    					if (valores.length == 2)
	    						documentosFaltantes = documentosFaltantes + valores[1].trim() +  "\n";
	    					else {
	    						String datoConcat = "";
	    						int index = 0;
	    						for (String valor : valores) {
	    							if (index > 0) {
	    								if (datoConcat.equals(""))
											datoConcat = valor;
										else
											datoConcat = datoConcat + " - " + valor;
	    							}
									index++;
								}
	    						documentosFaltantes = documentosFaltantes + datoConcat.trim() +  "\n";
	    					}
	    				}
    				}
    			}
    			if (documentosFaltantes.trim().equals("")) {
	    			retorno = "1";
	    		} else {
	    			retorno = documentosFaltantes;
	    		}
    		}
    	}
    	System.out.println("retorno:" + retorno);
    	return retorno;
    }
    
    /*
    public String verificarCompleto(String trama) {
    	System.out.println("en el metodo del applet: verificarCompleto");
    	System.out.println("trama:::::"+trama);
    	String retorno = "0";
    	int totalDocFaltantes = jComboBox1.getItemCount();
    	System.out.println("totalDocFaltantes:" + totalDocFaltantes);
    	if (totalDocFaltantes == 0) {
    		retorno = "1";
    	} else {
    		if(trama == null || "".equals(trama)){
	    		retorno = "1";
	    		for (int i=0; i<totalDocFaltantes; i++) {
	    			System.out.println("for totalDocFaltantes");
	    			System.out.println("obligatorio:" +((TipoDocumento) jComboBox1.getItemAt(i)).getCodigoDocumento());
	    			System.out.println("obligatorio:" +((TipoDocumento) jComboBox1.getItemAt(i)).isObligatorio());
	    			if (((TipoDocumento) jComboBox1.getItemAt(i)).isObligatorio()) {
	    				System.out.println("Entro If");
	    				retorno = "0";
	    				break;
	    			}
	    		}
    		}
    		else{
    			retorno = "1";
    			String tramaDocs[] = trama.split(",");
    			for (int i=0; i<totalDocFaltantes; i++) {
    				if(((TipoDocumento) jComboBox1.getItemAt(i)).isObligatorio()){
	    				boolean cargado = false;
	    				for (int j=0; j<tramaDocs.length; j++) {
	    					if(((TipoDocumento) jComboBox1.getItemAt(i)).getCodigoDocumento().equals(tramaDocs[j])){
	    						cargado = true;
	    						break;
	    					}
	    				}
	    				if(!cargado){
	    					retorno = "0";
		    				break;
	    				}
    				}
    			}
    		}
    	}
    	System.out.println("retorno:" + retorno);
    	return retorno;
    }
    */

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
	
	public void habilitarTipoDocumento() {
		this.jComboBox3.setEnabled(true);
		this.jButton6.setEnabled(false);
		jComboBox3.setSelectedItem(null);
	}
	
	public void start() {
		LOG.info("starting... ");
    }

    public void stop() {
    	LOG.info("stopping... ");
    }

    public void destroy() {
    	LOG.info("preparing for unloading...");
    }

}
