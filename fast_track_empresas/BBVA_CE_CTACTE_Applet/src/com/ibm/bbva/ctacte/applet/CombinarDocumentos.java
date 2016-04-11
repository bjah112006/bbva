package com.ibm.bbva.ctacte.applet;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import com.ibm.bbva.convert.ConvertirArchivos;
import com.ibm.bbva.convert.ConvertirArchivosService;
import com.ibm.bbva.ctacte.applet.constructor.AppApplet;
import com.ibm.bbva.ctacte.applet.constructor.Archivo;
import com.ibm.bbva.ctacte.applet.constructor.Parametros;
import com.ibm.bbva.ctacte.log.SimpleLogger;
import com.ibm.bbva.ctacte.pdf.VisorPDF;
import com.itextpdf.text.pdf.PdfReader;

public class CombinarDocumentos extends AppApplet {

	private static final long serialVersionUID = 1L;
	private static final SimpleLogger LOG = SimpleLogger.getLogger(CombinarDocumentos.class);

	
	private JPanel jPanel1 = new JPanel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JPanel jPanel2 = new JPanel(); // Documento Anterior
    //String listaValores[] = {"Página 001", "Página 002", "Página 003", "Página 003","Página 004","Página 005","Página 006","Página 007"};
    private JButton jButton1 = new JButton();
    private JButton jButton2 = new JButton();
    private JButton jButton3 = new JButton();
    private JButton jButton4 = new JButton();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JList jList2 = new JList();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private JList jList1 = new JList();
    private JButton jButton5 = new JButton();
    private JButton jButton6 = new JButton();
    private JButton jButton7 = new JButton();
    private JPanel jPanel3 = new JPanel();  // Documento Adicional
    private JScrollPane jScrollPane3 = new JScrollPane();
    private JList jList3 = new JList();
    private JButton jButton8 = new JButton();    
	private JButton jButton9 = new JButton();
    private JPanel jPanel4 = new JPanel();
    private JButton jButton12 = new JButton();
    private JLabel jLabel1 = new JLabel();
    private JPanel jPanel5 = new JPanel();
    private GridBagLayout gridBagLayout2 = new GridBagLayout();
    private JLabel jLabel2 = new JLabel();
    private JPanel jPanel6 = new JPanel();
    private GridBagLayout gridBagLayout3 = new GridBagLayout();
    private JLabel jLabel3 = new JLabel();
    private JPanel jPanel7 = new JPanel();
    private GridBagLayout gridBagLayout5 = new GridBagLayout();
    private FlowLayout flowLayout1 = new FlowLayout();

    private SortedListModel sourceListModel1;
    private SortedListModel sourceListModel2;
    private SortedListModelResult destListModel;
    private String PRIMER_DOCUMENTO = "Doc 1";
    private String SEGUNDO_DOCUMENTO = "Doc 2";
    private String PAGINA = "Pag";
    private String[] rutasDocs=null;
    private String RUTA_DOCUMENTO_UNO;
    private String RUTA_DOCUMENTO_DOS;
    private String DOCUMENTO_RESULTANTE;
    public static final String EXTENSION_ARCHIVO = ".pdf";
    OutputStream pdfMerged = null;
    
    
    private VisorPDF pdfResultante = new VisorPDF();
    private VisorPDF pdfAnterior = null;
    private VisorPDF pdfAdicional = null;
    //variables de negocio
    private String codigoDocumento;

    public CombinarDocumentos() throws HeadlessException {
        super();
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
    	
        //Seteando los SortedListModel a las listas
    	sourceListModel1 = new SortedListModel();
    	sourceListModel2 = new SortedListModel();
    	destListModel = new SortedListModelResult();
        jList1 = new JList(sourceListModel1);
        jList3 = new JList(sourceListModel2);
        jList2 = new JList(destListModel);
        //FIn Seteo
    	
        this.setSize(new Dimension(900, 600));
        jPanel1.setLayout(gridBagLayout1);
        jPanel1.setAlignmentX((float) 0.0);
        jPanel1.setAlignmentY((float) 0.0);
        jButton1.setText(">>");
        jButton1.setFont(new Font("Century Gothic", 1, 11));
        jButton2.setText(">");
        jButton2.setFont(new Font("Century Gothic", 1, 11));
        jButton3.setText("<<");
        jButton3.setFont(new Font("Century Gothic", 1, 11));
        jButton4.setText("<");
        jButton4.setFont(new Font("Century Gothic", 1, 11));
        jList1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        jList2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        jList3.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        jButton5.setText("v");
        jButton5.setFont(new Font("Century Gothic", 1, 11));
        jButton6.setText("^");
        jButton6.setFont(new Font("Century Gothic", 1, 11));
        jButton7.setText("Pre-visualizar combinación");
        jPanel3.setLayout(gridBagLayout2);
        jPanel5.setLayout(flowLayout1);
        jPanel3.setBorder(BorderFactory.createTitledBorder(""));
        jButton8.setText(">>");
        jButton8.setFont(new Font("Century Gothic", 1, 11));
        jButton9.setText(">");
        jButton9.setFont(new Font("Century Gothic", 1, 11));
        jPanel4.setLayout(gridBagLayout3);
        jPanel4.setBorder(BorderFactory.createTitledBorder(""));
        jButton12.setText("Terminar combinación");
        jButton12.setEnabled(false);

        jLabel1.setText("Documento Anterior");
        jLabel2.setText("Documento Adicional");
        jLabel3.setText("Documento combinado");
        jPanel2.setBorder(BorderFactory.createTitledBorder(""));
        jPanel2.setLayout(gridBagLayout5);
        jPanel2.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                   new Insets(0, 0, 0, 0), 0, 0));
        jPanel2.add(jPanel5, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.95, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(jPanel2, new GridBagConstraints(0, 0, 1, 3, 0.4, 0.5, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 190, 250));
        jPanel1.add(jButton1, new GridBagConstraints(2, 1, 1, 1, 0.01, 0.15, GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                    new Insets(0, 0, 12, 0), 1, 0));
        jPanel1.add(jButton2, new GridBagConstraints(2, 1, 1, 1, 0.01, 0.15, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(77, 0, 0, 0), 8, 1));
        jPanel1.add(jButton3, new GridBagConstraints(2, 3, 1, 1, 0.01, 0.15, GridBagConstraints.NORTH, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 10));
        jPanel1.add(jButton4, new GridBagConstraints(2, 2, 1, 1, 0.01, 0.15, GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 8, 10));
        jScrollPane1.getViewport().add(jList2, null);
        jPanel1.add(jScrollPane1, new GridBagConstraints(3, 0, 1, 6, 0.09, 0.9, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 90, 470));
        jScrollPane2.getViewport().add(jList1, null);
        jPanel1.add(jScrollPane2, new GridBagConstraints(1, 1, 1, 2, 0.09, 0.5, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 14, 0), 90, 250));
        jPanel1.add(jButton5, new GridBagConstraints(4, 2, 1, 1, 0.01, 0.5, GridBagConstraints.NORTH, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 4, 0));
        jPanel1.add(jButton6, new GridBagConstraints(4, 1, 1, 1, 0.01, 0.5, GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 2, 0));
        jPanel1.add(jButton7, new GridBagConstraints(5, 0, 1, 1, 0.3, 0.1, GridBagConstraints.SOUTH, GridBagConstraints.NONE,
                   new Insets(0, 0, 2, 0), 0, 0));
        jPanel1.add(jPanel3, new GridBagConstraints(0, 3, 1, 3, 0.4, 0.5, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 190, 250));
        jScrollPane3.getViewport().add(jList3, null);
        jPanel1.add(jScrollPane3, new GridBagConstraints(1, 3, 1, 2, 0.09, 0.5, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 90, 250));
        jPanel1.add(jButton8, new GridBagConstraints(2, 4, 1, 1, 0.01, 0.5, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(9, 0, 37, 0), 1, 0));
        jPanel1.add(jButton9, new GridBagConstraints(2, 4, 1, 1, 0.01, 0.01, GridBagConstraints.NORTH, GridBagConstraints.NONE,
                    new Insets(64, 0, 0, 0), 8, 0));
        jPanel1.add(jPanel4, new GridBagConstraints(5, 1, 1, 8, 0.4, 0.8, GridBagConstraints.NORTH, GridBagConstraints.NONE,
                    new Insets(24, 0, 38, 0), 190, 400));
        jPanel1.add(jButton12, new GridBagConstraints(5, 4, 1, 1, 0.3, 0.1, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(85, 0, 0, 0), 0, 0));
        jPanel3.add(jLabel2, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.05, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));

        jPanel3.add(jPanel6, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.95, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        
        jPanel4.add(jLabel3, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.05, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));

        jPanel4.add(jPanel7, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.95, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        
        this.getContentPane().add(jPanel1, BorderLayout.CENTER);
        
        jButton2.addActionListener(new Boton2AddListener());
        jButton1.addActionListener(new Boton1AddListener());
        jButton9.addActionListener(new Boton9AddListener());
        jButton8.addActionListener(new Boton8AddListener());
        jButton4.addActionListener(new Boton4AddListener()); //<
        jButton3.addActionListener(new Boton3AddListener()); // <<
        jButton6.addActionListener(new Boton6AddListener()); // boton arriba
        jButton5.addActionListener(new Boton5AddListener()); // boton abajo
        jButton7.addActionListener(new Boton7AddListener()); // previsualizar
        jButton12.addActionListener(new Boton12AddListener()); // terminar combinacion

        //Agregando el PDF Resultante al panel contenedor
        jPanel7.setLayout(new BorderLayout());
        jPanel7.add(pdfResultante, BorderLayout.CENTER);
        
	}
    
    
    @Override
	public void init() {
		super.init();
		 //Recibiendo los parametros
		String codigoExpediente = getParameter(Parametros.ID_EXPEDIENTE); // Id del expediente
		String formOrigen = getParameter(Parametros.FORM_ORIGEN);
		String servidorPDF = getParameter(Parametros.SERVIDOR_CONV_PDF);
		String puertoPDF = getParameter(Parametros.PUERTO_CONV_PDF);
		codigoDocumento = getParameter(Parametros.CODIGO_DOC);
		
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("+++codigoDocumento+++"+codigoDocumento);
		
		codigoExpediente = (codigoExpediente == null?"":codigoExpediente);
		codigoDocumento = (codigoDocumento == null?"":codigoDocumento);
		
		String nomDocumento = "";
		File file =null;
		if(formOrigen!=null && !formOrigen.equalsIgnoreCase("")){ // escaneado en migracion de archivos historicos, entonces codigoDocumento tendra valores similares a D12
			nomDocumento = codigoDocumento+"-"+formOrigen;			
		}
		LOG.info("codigoExpediente :" + codigoExpediente);
		LOG.info("codigoDocumento :" + codigoDocumento);
		try {
			Archivo.getInstance().limpiarDescargados();
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("descarga....");
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("codigoExpediente : "+codigoExpediente);
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("codigoDocumento : "+codigoDocumento);
			findDocumento (Integer.parseInt(codigoExpediente),codigoDocumento);
			if (!descargarArchivosHist()){
				findDocumentoNombrar(Integer.parseInt(codigoExpediente),codigoDocumento,codigoDocumento.concat("-HIST"));
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
        //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("APPLET nomDocumento-->"+nomDocumento);
        
        file = Archivo.getInstance().ubicarArchivo(nomDocumento);
        byte[] mybyteArray = Util.fileToByteArray(file);
      /*  String servicioPDF = "http://"+servidorPDF+":"+puertoPDF+"/BBVA_ConvertFiles/services/ConvertirArchivos/wsdl/ConvertirArchivos.wsdl";
        //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("servicioPDF : "+servicioPDF);
        ConvertirArchivosService cas = null;
		try {
			cas = new ConvertirArchivosService (new URL (servicioPDF));
			ConvertirArchivos convertirArchivos = cas.getConvertirArchivos();
			mybyteArray = convertirArchivos.convertirPDF(mybyteArray, file.getName());
			Util.byteArrayToFile(file, mybyteArray);
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Convirtio archivo : "+file.getAbsolutePath());
		} catch (Exception e1) {
			e1.printStackTrace();
		}*/
        RUTA_DOCUMENTO_UNO = file.getAbsolutePath(); // "C:/Archivos/doc03.pdf";
        file = Archivo.getInstance().ubicarArchivo(codigoDocumento);
        RUTA_DOCUMENTO_DOS =  file.getAbsolutePath(); //"C:/Archivos/doc04.pdf";
        
        // Instanciando los objetos asociados a los PDFs
        rutasDocs= new String[]{RUTA_DOCUMENTO_UNO,RUTA_DOCUMENTO_DOS};
        
        DOCUMENTO_RESULTANTE = Archivo.strTransferencias;
      //Creando el nombre del file resultante
        DOCUMENTO_RESULTANTE = (DOCUMENTO_RESULTANTE.concat("\\")).concat(codigoDocumento).concat(EXTENSION_ARCHIVO); 

	    //La cadena que debe contener cada descripcion de hoja debe ser: Doc1 o Doc2 según sea el caso  
        int[] tamaDocs = getNumPaginas(2,rutasDocs);
        String[] paginas=generateDocumentoPaginaList(PRIMER_DOCUMENTO,1,tamaDocs[0]); //posicion del primer doc, num hojas del primer doc
        //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("# paginas del primer DOC-->"+paginas);
        sourceListModel1.addAll(paginas);        
        paginas = generateDocumentoPaginaList(SEGUNDO_DOCUMENTO,2,tamaDocs[1]);
        //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("# paginas del segundo DOC-->"+paginas);
        sourceListModel2.addAll(paginas);
        
        //Cargando los PDF
        pdfAnterior = new VisorPDF();
		try {
			pdfAnterior.cargarPDF(RUTA_DOCUMENTO_UNO, 0.4f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		jPanel5.setLayout(new BorderLayout());
		jPanel5.add(pdfAnterior, BorderLayout.CENTER);
		
        
        pdfAdicional = new VisorPDF();
		try {
			pdfAdicional.cargarPDF(RUTA_DOCUMENTO_DOS, 0.4f);
		} catch (Exception e) {
		}
		jPanel6.setLayout(new BorderLayout());
		jPanel6.add(pdfAdicional, BorderLayout.CENTER);
	}

	public int[] getNumPaginas(int numDoc,String[] docsPdfs){
    	
    	int[] numPaginas = new int[numDoc];
    	
    	try{
		      for (int i = 0; i < docsPdfs.length; i++) {
		    	  PdfReader pdfReader = new PdfReader(docsPdfs[i]); // (pdf);
			      numPaginas[i] = pdfReader.getNumberOfPages();
		      }
		   
		    
    	}catch(Exception e){
    		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println(e.getMessage());
    	}
    	return numPaginas;
    }
    
    public String[] generateDocumentoPaginaList(String desDoc,int numDoc,int numPag){
    	String[] listDocPagina = new String[numPag];
    	NumberFormat formatter; 
    	for (int i = 0; i < numPag; i++) {

    		formatter = new DecimalFormat("000");  
    		String strCadena = formatter.format(i+1);
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("cadena formateada-->"+strCadena);
    		strCadena = desDoc + "-" + PAGINA + " "+strCadena;
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("strCadena-->"+strCadena);
    		listDocPagina[i]=strCadena;
 		}
    
    	return listDocPagina;
    }
    
    
    //Implementacion de la operativa
    public void addDestinationElements(SortedListModel model, Object newValue[]) {
        fillListModel(model, newValue);
      }

    public void addDestinationElements(SortedListModelResult model,Object newValue[]) {
    	fillListModel(model, newValue);
      }

    private void fillListModel(SortedListModel model, Object newValues[]) {
    	model.addAll(newValues);
      }
    
    private void fillListModel(SortedListModelResult model, Object newValues[]) {
    	model.addAll(newValues);
      }
    
    //Para borrar lo seleccionado del JList y del modelo
    private void clearSourceSelected(SortedListModel model,JList cmpList) {
        Object selected[] = cmpList.getSelectedValues();
        for (int i = selected.length - 1; i >= 0; --i) {
        	model.removeElement(selected[i]);
        }
        cmpList.getSelectionModel().clearSelection();
      }

    private void clearSourceSelected(SortedListModelResult model,JList cmpList) {
        Object selected[] = cmpList.getSelectedValues();
        for (int i = selected.length - 1; i >= 0; --i) {
        	model.removeElement(selected[i]);
        }
        cmpList.getSelectionModel().clearSelection();
      }

    private class Boton2AddListener implements ActionListener { // > DOc 1
        public void actionPerformed(ActionEvent e) {
          Object selected[] = jList1.getSelectedValues();
          addDestinationElements(destListModel,selected);
          clearSourceSelected(sourceListModel1,jList1);
        }
     }
    
    private class Boton1AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	int[] indices = new int[sourceListModel1.getSize()];
        	for (int i=0;i< sourceListModel1.getSize();i++){
        		indices[i]=i;
        	}
          jList1.setSelectedIndices(indices);
          Object selected[] = jList1.getSelectedValues();
          addDestinationElements(destListModel,selected);
          clearSourceSelected(sourceListModel1,jList1);
        }
     }
    
    private class Boton9AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
          Object selected[] = jList3.getSelectedValues();
          addDestinationElements(destListModel,selected);
          clearSourceSelected(sourceListModel2,jList3);
        }
     }
    
    private class Boton8AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	int[] indices = new int[sourceListModel2.getSize()];
        	for (int i=0;i< sourceListModel2.getSize();i++){
        		indices[i]=i;
        	}        		
          jList3.setSelectedIndices(indices);
          Object selected[] = jList3.getSelectedValues();
          addDestinationElements(destListModel,selected);
          clearSourceSelected(sourceListModel2,jList3);
         }
     }

    private class Boton4AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
          Object selected[] = jList2.getSelectedValues();
          int cantHojasDoc1 = contSheetbyString(selected,PRIMER_DOCUMENTO);
          int cantHojasDoc2 = contSheetbyString(selected,SEGUNDO_DOCUMENTO);
          Object selectedDoc1[] = new Object[cantHojasDoc1];
          Object selectedDoc2[] = new Object[cantHojasDoc2];
          separarListaHojas(selected,selectedDoc1,PRIMER_DOCUMENTO);
          separarListaHojas(selected,selectedDoc2,SEGUNDO_DOCUMENTO);
          addDestinationElements(sourceListModel1,selectedDoc1);
          addDestinationElements(sourceListModel2,selectedDoc2);
          clearSourceSelected(destListModel,jList2);
        }
     }
    
    private class Boton3AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int[] indices = new int[destListModel.getSize()];
        	for (int i=0;i< destListModel.getSize();i++){
        		indices[i]=i;
        	}
          jList2.setSelectedIndices(indices);
          
	      Object selected[] = jList2.getSelectedValues();
	      int cantHojasDoc1 = contSheetbyString(selected,PRIMER_DOCUMENTO);
	      int cantHojasDoc2 = contSheetbyString(selected,SEGUNDO_DOCUMENTO);
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("cantidad hojas doc 01-->"+cantHojasDoc1);
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("cantidad hojas doc 02-->"+cantHojasDoc2);
	      Object selectedDoc1[] = new Object[cantHojasDoc1];
	      Object selectedDoc2[] = new Object[cantHojasDoc2];
          separarListaHojas(selected,selectedDoc1,PRIMER_DOCUMENTO);
          separarListaHojas(selected,selectedDoc2,SEGUNDO_DOCUMENTO);
	      addDestinationElements(sourceListModel1,selectedDoc1);
          addDestinationElements(sourceListModel2,selectedDoc2);
          clearSourceSelected(destListModel,jList2);
        }
     }
    
    private class Boton6AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
                  
	      Object selected[] = jList2.getSelectedValues();
	      int indice=jList2.getSelectedIndex();
	      if (indice >0){
	    	  
	    	  Object objectToDown=null;
	    	  objectToDown = destListModel.getElementAt(indice-1);
		      int posIniObjectsToKeepPos = jList2.getSelectedValues().length+indice;
		      int posFinObjectsToKeepPos = destListModel.getSize()-1;
		      Object[] objectsToKeepPos = new Object[posFinObjectsToKeepPos-posIniObjectsToKeepPos+1];
		      
		      //Recolectando Objectos que mantienen posicion
		      for (int i = 0; i < posFinObjectsToKeepPos-posIniObjectsToKeepPos+1; i++) {
		    	  objectsToKeepPos[i] = destListModel.getElementAt(posIniObjectsToKeepPos+i);
		    	  //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Elemento ["+i+"]-->"+ (String)objectsToKeepPos[i]);
		      }
		      
		      //Elimino objecto que baja posicion
	    	  destListModel.removeElement(objectToDown);
		      
		      //Elimino objectos que mantienen su posicion
		      for (int i = 0; i < objectsToKeepPos.length; i++) {	    	  
		    	  destListModel.removeElement(objectsToKeepPos[i]);
		      }
		      
		      //Añado Objeto que baja posicioon
	    	  destListModel.add(objectToDown);
		      
		      //Añado objetos que mantienen su posicion
		      for (int i = 0; i < objectsToKeepPos.length; i++) {
		    	  destListModel.add(objectsToKeepPos[i]);
			  }
		      
		      //Mantener seleccionados los elementos
		      int []indicesSeleccionados=new int[jList2.getSelectedValues().length];
		      for (int i = 0; i < selected.length; i++) {
		    	  indicesSeleccionados[i]=indice-1+i;
			  }
		      jList2.setSelectedIndices(indicesSeleccionados);
		
	      }
        }
     }

    private class Boton5AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
                  
	      Object selected[] = jList2.getSelectedValues();
	      int indice=jList2.getSelectedIndex();
	      int indiceSleccionadoInferior=jList2.getMaxSelectionIndex();
//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("indiceSleccionadoInferior-->"+indiceSleccionadoInferior);
	      if (indiceSleccionadoInferior < destListModel.getSize()-1){
	    	  
		      Object[] objectsToDown = jList2.getSelectedValues();
		      int posIniObjectsToKeepPos = indiceSleccionadoInferior+2;
		      int posFinObjectsToKeepPos = destListModel.getSize()-1;
		      Object[] objectsToKeepPos = new Object[posFinObjectsToKeepPos-posIniObjectsToKeepPos+1];
		      
		      //Recolectando Objectos que mantienen posicion
		      for (int i = 0; i < posFinObjectsToKeepPos-posIniObjectsToKeepPos+1; i++) {
		    	  objectsToKeepPos[i] = destListModel.getElementAt(posIniObjectsToKeepPos+i);
		      }
		      
		      //Elimino objecto que baja posicion
		      for (int i = 0; i < objectsToDown.length; i++) {
		    	  destListModel.removeElement(objectsToDown[i]);
		      }
		      
		      //Elimino objectos que mantienen su posicion
		      for (int i = 0; i < objectsToKeepPos.length; i++) {	    	  
		    	  destListModel.removeElement(objectsToKeepPos[i]);
		      }

		      //Añado objetos que baja posicioon
		      for (int i = 0; i < objectsToDown.length; i++) {
		    	  destListModel.add(objectsToDown[i]);
			  }

		      //Añado Objeto que mantienen su posicion
		      for (int i = 0; i < objectsToKeepPos.length; i++) {
		    	  destListModel.add(objectsToKeepPos[i]);
			  }
		      
		      //Mantener seleccionados los elementos
		      int []indicesSeleccionados=new int[jList2.getSelectedValues().length];
		      for (int i = 0; i < selected.length; i++) {
		    	  indicesSeleccionados[i]=indice+1+i;
			  }
		      jList2.setSelectedIndices(indicesSeleccionados);
		      
	      }
        }
     }
    
    private class Boton7AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	List<String> mapaHojas = null;
        	try{
	            mapaHojas = new ArrayList<String>();
	        	mapaHojas = darFormato();
	    	    try {
	    			pdfMerged = new FileOutputStream(DOCUMENTO_RESULTANTE);
	    		} catch (FileNotFoundException ex) {
	    			ex.printStackTrace();
	    		}	    		
	        	CrearPDF.concatPDFs(rutasDocs, mapaHojas, pdfMerged, true);
	            
	    		try {
	    			pdfResultante.cargarPDF(DOCUMENTO_RESULTANTE, 0.4f);
	    			jButton12.setEnabled(true);
	    		} catch (Exception exc) {
	    			exc.printStackTrace();
	    		}
	    		
	    		
        	} catch (Exception ex) {
		      ex.printStackTrace();
		    }
        }
     }

    private class Boton12AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	//System.exit(0);
        	//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("codigoDocumento-->"+codigoDocumento);
        	//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("pdfResultante "+pdfResultante.isTienePDF());
        	if (!pdfResultante.isTienePDF()) {
        		codigoDocumento = "";
        	}
        	ejecutarFuncion("actualizarDocumentoCombinado", codigoDocumento);
        	
        	int respuesta = JOptionPane.showConfirmDialog(null,"¿Desea terminar la operació?", "Completar", JOptionPane.YES_NO_OPTION ); 
        	switch(respuesta) { 
        		case JOptionPane.YES_OPTION:         			
        			pdfResultante.cerrar();
        		    pdfAnterior.cerrar();
        		    pdfAdicional.cerrar();
        			ejecutarFuncion("cerrarPopup");
        			break; 
        		case JOptionPane.NO_OPTION: 
        			break; 
        	}
        	
        }
     }

    @Override
	public void destroy() {
		super.destroy();
		pdfResultante.cerrar();
	    pdfAnterior.cerrar();
	    pdfAdicional.cerrar();
	}

	private List<String> darFormato(){
    	List<String> mapaHojas = new ArrayList<String>();
	    for (int i = 0; i < destListModel.getSize(); i++) {
	    	String hojax = new String();
	    	hojax = (String)destListModel.getElementAt(i);
	    	hojax = hojax.replace("Doc", "");
	    	hojax = hojax.replace("Pag", "");
	    	hojax = hojax.replace("-", "|");
		    mapaHojas.add(hojax);
	    	//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("hojax ["+i+"]-->"+ hojax);
	    }	      
    	return mapaHojas;
    } 

    private int contSheetbyString(Object []listOriginal,String nombreDocumento){
    	int contador=0;
	  	for (int i = 0; i < listOriginal.length; i++) {
	  		if ((String)listOriginal[i] !=null)
	  			if (((String)listOriginal[i]).contains(nombreDocumento))
	  				contador = contador+1;
		}
    	return contador;
    }
    
    private void separarListaHojas(Object []listOriginal,Object []hojasDoc,String nombreDocumento){
	  	int j=0;
	  	for (int i = 0; i < listOriginal.length; i++) {
	  		if ((String)listOriginal[i] !=null){
	  			if (((String)listOriginal[i]).contains(nombreDocumento)){
	  				hojasDoc[j] = (String)listOriginal[i];
	  				j++;
	  			}
	  		}
		}
  	}
    
	protected boolean descargarArchivosHist () {
		String descargar = getParameter(Parametros.TRAMA_DESCARGAR_ARCHIST);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("descargar-->"+descargar);
		
    	if (!(descargar==null || descargar.trim().equals(""))) {
    		StringTokenizer st = new StringTokenizer (descargar, ";");
    		st.hasMoreTokens();    		
    		String tipoDocumento = st.nextToken().trim();
    		String nombreArchivo = st.nextToken().trim();
    		Integer idCM = Integer.valueOf(st.nextToken().trim());
    		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("tipoDocumento "+tipoDocumento);
    		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("nombreArchivo "+nombreArchivo);
    		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("idCM "+idCM);
    		try {
    			findDocumentoNombreArch (tipoDocumento,nombreArchivo, idCM);
    		} catch (Exception e) {
				e.printStackTrace();
			}
    	
    		ejecutarFuncion("confirmarInicioApplet");
    		
    		return true;
		}
    	return false;
	}  
    
}

