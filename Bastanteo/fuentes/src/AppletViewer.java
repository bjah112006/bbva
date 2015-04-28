/****************************************************************************
**
** (c) Copyright IBM Corp. 2003  All rights reserved.                 
**                                                                   
** This sample program is owned by International Business Machines    
** Corporation or one of its subsidiaries ("IBM") and is copyrighted  
** and licensed, not sold.                                            
**                                                                   
** You may copy, modify, and distribute this sample program in any    
** form without payment to IBM,  for any purpose including developing,
** using, marketing or distributing programs that include or are      
** derivative works of the sample program. Licenses to IBM patents    
** are expressly excluded from this license.                          
** The sample program is provided to you on an "AS IS" basis, without 
** warranty of any kind.  IBM HEREBY  EXPRESSLY DISCLAIMS ALL         
** WARRANTIES EITHER EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO
** THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTIC-
** ULAR PURPOSE. Some jurisdictions do not allow for the exclusion or 
** limitation of implied warranties, so the above limitations or      
** exclusions may not apply to you.  IBM shall not be liable for any  
** damages you suffer as a result of using, modifying or distributing 
** the sample program or its derivatives.                             
**                                                                  
** Each copy of any portion of this sample program or any derivative 
** work, must include a the above copyright notice and disclaimer of
** waranty.
**
*****************************************************************************
**
** FILENAME : AppletViewer.java
**    
** SAMPLE   : Information Integrator for Content Version 8 Applet Viewer
**            Sample
**
** ABSTRACT : This file is included in the Applet Viewer Sample EAR
**            file (AppletViewer.EAR). This is the applet class and
**            is used to display documents. Users can work with annotations
**            and perform functions such as zoom in/out, rotate, print, etc.                                               
**
** Date     : Sept 25, 2003
**                                                           
** Authors  : Bryan Daniel (bryand@us.ibm.com)                 
**			  Jerald Schoudt
**
** NOTE     : AFTER MAKING CHANGES TO THIS CLASS FILE, YOU MUST PACKAGE IT
**            INTO A SIGNED JAR FILE. The AppletViewer.jsp web page instructs
**            the client Java Plugin to obtain the required class files from
**            the cmbview81.jar and AppletViewer.jar files. These files are
**            downloaded from the server to the client, and should be located
**            in the 'Web Content' folder. (The cmbview81.jar file that is
**            in the Web Content folder is Version 8.2 Fixpack 1, which was
**            the latest level at the time of this writing.  It contains the
**            II4C document viewer toolkit and is original installed by II4C
**            into the \cmbroot\lib directory. If you have a later fixpack
**            level, you may want to copy this jar from c:\cmbroot\lib
**            to the 'Web Content' folder.)
**
**            Further more, the jar file must be signed since the generic
**            doc viewer object contained in the applet will try to make
**            a direct connection to the resource manager server to retrieve
**            the document (instead of having the document first go through
**            the mid tier server). Making a connection to a machine other
**            than the web server will cause a security exception, which is
**            why you must sign the jar file.
**
**            To create a new jar file:
**            -------------------------
**            1) Go to a command window.
**
**            2) Change to the <wsad workspace>\AppletViewer\Web Content dir
**               and remove the AppletViewer.jar file. 
**
**               For example:
**               - cd "C:\Documents and Settings\admin\My Documents\
**                 wsad\workspace\AppletViewer\Web Content"
**               - del AppletViewer.jar
**                
**            3) Change to the <wsad workspace>\AppletViewer\Web Content\
**               WEB-INF\classes dir.
**
**               For example:
**               - cd "C:\Documents and Settings\admin\My Documents\wsad\
**                 workspace\AppletViewer\Web Content\WEB-INF\classes"
**
**            4) Use the "jar -cvf" command to create a new AppletViewer.jar
**               file containing the following files: 
**
**                   AppletViewer$AnnotationServicesCallbacks.class,
**                   AppletViewer$LoadedDoc.class 
**                   AppletViewer$StreamingDocServicesCallbacks.class 
**                   AppletViewer.class
**
**                The AppletViewer.jar file should be created in the
**                'Web Content' dir.
**
**                 For example:
**                 - jar -cvf "C:\Documents and Settings\admin\My Documents\
**                   wsad\workspace\AppletViewer\Web Content\AppletViewer.jar" 
**                   AppletViewer$AnnotationServicesCallbacks.class 
**                   AppletViewer$LoadedDoc.class 
**                   AppletViewer$StreamingDocServicesCallbacks.class AppletViewer.class
**
**
**             To sign the new jar file:
**             -------------------------
**            1) Go to a command window.
**            2) Change to the <wsad workspace>\AppletViewer\Web Content dir.
**
**               For example:
**               - cd "C:\Documents and Settings\admin\My Documents\
**                 wsad\workspace\AppletViewer\Web Content"
**            
**            3) Generate a test certificate by entering:
**               keytool -genkey -keystore viewer.store -alias viewercert -validity 365
**
**            4) Export the certificate by entering:
**               keytool -export -keystore viewer.store -alias viewercert -file viewer.cer
**
**            5) Sign the jar file by typing:
**               jarsigner -keystore viewer.store AppletViewer.jar viewercert 
**
**
**            AFTER CREATING A NEW SIGNED JAR FILE, YOU SHOULD REFRESH THE
**            FILES IN THE WORKSPACE. To do this, right-click on the 
**            'Web Content' folder and choose 'Refresh'.
**            
**                                                            	                                 
*****************************************************************************/


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import com.ibm.mm.beans.CMBPrivilege;
import com.ibm.mm.viewer.CMBDocument;
import com.ibm.mm.viewer.CMBGenericDocClosedEvent;
import com.ibm.mm.viewer.CMBGenericDocClosedListener;
import com.ibm.mm.viewer.CMBGenericDocOpenedEvent;
import com.ibm.mm.viewer.CMBGenericDocOpenedListener;
import com.ibm.mm.viewer.CMBGenericDocViewer2;
import com.ibm.mm.viewer.CMBStreamingDocServices;
import com.ibm.mm.viewer.CMBStreamingDocServicesCallbacks;
import com.ibm.mm.viewer.annotation.CMBAnnotationServices;
import com.ibm.mm.viewer.annotation.CMBAnnotationServicesCallbacks;
import com.ibm.mm.viewer.annotation.CMBAnnotationSet;
import com.ibm.mm.viewer.annotation.CMBPageAnnotation;
import com.ibm.mm.viewer.annotation.CMBTextAnnotation;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.lowagie.text.pdf.codec.TiffImage;

public class AppletViewer extends JApplet implements CMBGenericDocOpenedListener, CMBGenericDocClosedListener, ActionListener
{
	
    private Properties currEngineProperties  = null;
    private Properties currConfigProperties = null;
    private CMBStreamingDocServices currStreamingDocServices;
    private CMBAnnotationServices currAnnoServices;
    private CMBGenericDocViewer2 myGenDocViewer;
    private int paramNumDocs;
    private String [] docPaths;
    private InputStream annis = null;
	private boolean openingDoc = true;	
	private Vector loadedDocsList;
	
	public String descargados;
	public String transferencias;
	public String type;
	
	public boolean doOperation = false;

	/**************************************************************
   	* Initializes the applet
   	*/
    public void init()
    {	
		try 
		{
			
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Linea 1");
			// This line prevents the "Swing: checked access to system event queue" message seen in some browsers.
			getRootPane().putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
			getContentPane().setLayout(new BorderLayout());

			// Create engine properties
			currEngineProperties = new Properties();
			currEngineProperties.put("ENGINES","2");
			currEngineProperties.put("ENGINE1_CLASSNAME", "com.ibm.mm.viewer.mstech.CMBMSTechDocumentEngine");
			currEngineProperties.put("ENGINE2_CLASSNAME","com.ibm.mm.viewer.CMBJavaDocumentEngine");
			currEngineProperties.put("DOCTYPE_AUTODETECT", "true");

			// Get the viewer configuration properties
			currConfigProperties = new Properties();
			System.out.println("Linea 2");
			URL configURL = new URL(getCodeBase(), "CMBViewerConfiguration.properties");
			System.out.println("Linea 3");
			URLConnection configURLConn = configURL.openConnection();
			System.out.println("Linea 4");
			InputStream configInputStream = configURLConn.getInputStream();			
            currConfigProperties.load( configInputStream );
            configInputStream.close();
			
	        // Create document and annotation services objects
			currStreamingDocServices = new CMBStreamingDocServices(new StreamingDocServicesCallbacks(), currEngineProperties);
       		currAnnoServices = new CMBAnnotationServices(new AnnotationServicesCallbacks());

			// Create generic document viewer object
			myGenDocViewer = new CMBGenericDocViewer2(currStreamingDocServices, currAnnoServices, currConfigProperties);			
			getContentPane().add(myGenDocViewer, BorderLayout.CENTER);
			myGenDocViewer.setBackground(Color.white);
			setSize(0,0);
			
			// Add listener to generic document viewer object
			myGenDocViewer.addDocOpenedListener(this);
            myGenDocViewer.addDocClosedListener(this);
            
            // Add this class as a listener for events thrown by the new
            // "saveAs_doc" button added to the "OperationToolbar" toolbar
            JToolBar tb = myGenDocViewer.getToolBar("OperationToolbar");
			Component components[] = tb.getComponents();  
			
			// Have to go through toolbar components to
			// find button that we are interested in.                          
			for (int i = 0; i < components.length; i++)                             
			{                                                                       
				if (components[i] instanceof JButton)                                   
      			{                                                                 
      				JButton button = (JButton) components[i];                         
       				Action act = button.getAction();                                 
       				String name = (String) act.getValue("Name");
       				System.out.println("name:::::"+name);
       				
       				// If this is the "saveAs_doc" button, add
                    // this class as a listener object                                    
      				if ( name.equalsIgnoreCase("saveAs_doc") ) 
   						button.addActionListener(this); 
      				
      				if ( name.equalsIgnoreCase("Save Document") || name.equalsIgnoreCase("Guardar documento"))
      				{
      					System.out.println("listener para Save Document");
      					button.addActionListener(this);      					
      				}
      			}                                                                 
			}                         


			// Init list to keep track of loaded documents                                    
			loadedDocsList = new Vector();
			
			// Now that the GUI is initialized, load specified document
			String strDoc = getParameter("document");        	
        	loadDocument(strDoc);
        	
        	descargados = getParameter("descargados");
        	transferencias = getParameter("transferencias");
        	type = getParameter("type"); 

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	} // end init()


	/**************************************************************
   	* Loads a document and its annotations into document viewer
   	* @param docid document id
   	*/
	public void loadDocument(String docid)
	{
		try
		{
			System.out.println("loadDocument() called. docid = " + docid);			

			// Lets see if this document has already been opened.
			// If so, then lets go to that page
			//
			for (int i=0; i<loadedDocsList.size(); i++)
			{
				LoadedDoc doc = (LoadedDoc) loadedDocsList.elementAt(i);
									
				if ( docid.equals(doc.docid) )
				{
					System.out.println("Doc already loaded!!");
					myGenDocViewer.activateDocument(doc.document);
					return;
				}					
			}

			// Get mimetype of the first part.  The generic doc
			// viewer object will make calls to the document
			// services callback class to asynchronously load
			// another other parts.
			String partMimeType = getPartMimeType(docid, "0");			
			System.out.println("partMimeType = " + partMimeType);			

			// Get number of parts
			int numParts = getNumParts(docid);
			System.out.println("numParts = " + numParts);

			// Get url location of the first part of this document
			String docURL = getDocURL(docid, "0");
			System.out.println("docURL = " + docURL);			
			
			// Get inputstream from document url
			URL url1 = new URL(docURL);
			URLConnection urlConnection1 = url1.openConnection();
			InputStream docInputStream = urlConnection1.getInputStream();

			// Load document
			CMBDocument currDocument = myGenDocViewer.loadDocument(docInputStream, numParts, partMimeType, partMimeType, null, null);       		
			
        	if(currDocument == null) 
			{
	           	System.out.println("AppletViewer.loadDocument - currDocument is null");
	       	}
     		
			// Get url location of annotation part
			String annURL = getAnnotationURL(docid);
			System.out.println("annURL = " + annURL);
			
			// If there is no annotation part, then create inputstream
			// to an empty byte array. Else create inputstream to the
			// url location.
			InputStream annInputStream;
			if (annURL == null)
			{
		        annInputStream = new ByteArrayInputStream(new byte[0]);
			}
			else
			{
				URL url2 = new URL(annURL);
				URLConnection urlConnection2 = url2.openConnection();
				annInputStream = urlConnection2.getInputStream();
			}			

			// Get position of annotations
       		int annPosition = currDocument.getAnnotationPosition();					
			
			if (annPosition == 0) 
				annPosition = 14336;
			
			// Load annotations       		       		
			CMBAnnotationSet annSet= myGenDocViewer.loadAnnotations(annInputStream, "application/vnd.ibm.modcap", annPosition, 1, 0);

			// Get privilege information
			String privInfo = getPrivInfo(docid);
			System.out.println("Privilege Info : " + privInfo);

			// Keep track of what documents are currently loaded			
			LoadedDoc docInfo = new LoadedDoc(currDocument, annSet, docid, privInfo);
			loadedDocsList.addElement( docInfo );					

			// Set permissions for individual annotations. 
			//
			// The authority is still maintained in the server. For example, if
			// we set canUpdate() to true, but the access control on the server
			// does not allow this privilege, then an error will occur when the
			// user tries to save the annotation.  Use this method to keep user
			// from doing something the server will not allow.
			//
			// NOTE: Defect in current code base causes these methods not to work.
			// As a result, users can change annotations while viewing, but will
			// later get an error when they try to save their changes (if they
			// dont have the right permissions). The fix for this defect is 
			// planned to be inclded in the next fixpack.					
			java.util.Iterator iter = annSet.getIterator();
			CMBPageAnnotation page;
			while (iter.hasNext()) 
			{
				page = (CMBPageAnnotation) iter.next();
				page.setCanUpdate( docInfo.checkPriv("CMB_PRIV_MODIFY_SINGLE_ANNOTATION") );
				page.setCanShow( docInfo.checkPriv("CMB_PRIV_VIEW_ANNOTATION") );
			}
			
			// Show document in viewer
			myGenDocViewer.showDocument(currDocument, annSet, "Picture" + loadedDocsList.size());
			myGenDocViewer.setSize(500, 500);
       		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	       	
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	} // end loadDocument()
	

	/**************************************************************
   	* Returns mime type of specified part
   	* @param docid Document id
   	* @param strPartNum Part number
   	*/	
	private String getPartMimeType(String docid, String strPartNum)
	{
		String mimeType = "";
		
		try
		{
			// Create properties object to hold getPartMimeType command
			Properties properties = new Properties();
            properties.put("command", "getPartMimeType");            
            properties.put("docid", docid);
            properties.put("partNum", strPartNum);
            
			// Send command to servlet                    
		    URL url = new URL(getCodeBase(), "AppletController");
		    URLConnection urlConnection = url.openConnection();  
			sendCommand(urlConnection, properties); 

			// Get response
			ObjectInputStream objInputStream = new ObjectInputStream(urlConnection.getInputStream());
			mimeType = (String) objInputStream.readObject();	
			objInputStream.close();		
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
		
		return mimeType;

	} // end getPartMimeType
	

	/**************************************************************
   	* Returns the URL location of specified document part
   	* @param docid Document id
   	* @param strPartNum Part number
   	*/
	private String getDocURL(String docid, String strPartNum)
	{
		String docURL = "";
		
		try
		{
			// Create properties object to hold getDocURL command
			Properties properties = new Properties();
            properties.put("command", "getDocURL");
            properties.put("docid", docid);
            properties.put("partNum", strPartNum);

			// Send command to servlet
		    URL url = new URL(getCodeBase(), "AppletController");                    
		    URLConnection urlConnection = url.openConnection();  
			sendCommand(urlConnection, properties); 

			// Get response
			ObjectInputStream objInputStream = new ObjectInputStream(urlConnection.getInputStream());
			docURL = (String) objInputStream.readObject();
			objInputStream.close();			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}						
		
		return docURL;	
			
	} // end getDocURL()


	/**************************************************************
   	* Returns number of parts in specified document
   	* @param docid Document id
   	*/	
	private int getNumParts(String docid)
	{
		int numParts = 0;
		
		try
		{
			// Create properties object to hold getNumParts command
			Properties properties = new Properties();
            properties.put("command", "getNumParts");
            properties.put("docid", docid);

			// Send command to servlet
		    URL url = new URL(getCodeBase(), "AppletController");                    
		    URLConnection urlConnection = url.openConnection();  
			sendCommand(urlConnection, properties); 

			// Get response
			ObjectInputStream objInputStream = new ObjectInputStream(urlConnection.getInputStream());
			String strNumParts = (String) objInputStream.readObject();			
			objInputStream.close();
			
			numParts = new Integer(strNumParts).intValue();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}						
		
		return numParts;		
		
	} // end getNumParts


	/**************************************************************
   	* Returns URL location of annotation part for specified doc
   	* @param docid Document id
   	*/	
	private String getAnnotationURL(String docid)
	{
		String annURL = "";
		
		try
		{
			// Create properties object to hold getAnnURL command
			Properties properties = new Properties();
            properties.put("command", "getAnnURL");
            properties.put("docid", docid);

			// Send command to servlet
		    URL url = new URL(getCodeBase(), "AppletController");                    
		    URLConnection urlConnection = url.openConnection();  
			sendCommand(urlConnection, properties); 

			// Get response
			ObjectInputStream objInputStream = new ObjectInputStream(urlConnection.getInputStream());
			annURL = (String) objInputStream.readObject();
			objInputStream.close();			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}						
		
		return annURL;		

	} // end getAnnotationURL


	/**************************************************************
   	* Returns a string of privilege information
   	* @param docid Document id
   	*/
	private String getPrivInfo(String docid)
	{
		String strPrivInfo = "";
		
		try
		{
			// Create properties object to hold getPrivInfo command
			Properties properties = new Properties();
            properties.put("command", "getPrivInfo");
            properties.put("docid", docid);

			// Send command to servlet
		    URL url = new URL(getCodeBase(), "AppletController");                    
		    URLConnection urlConnection = url.openConnection();  
			sendCommand(urlConnection, properties); 

			// Get response
			ObjectInputStream objInputStream = new ObjectInputStream(urlConnection.getInputStream());
			strPrivInfo = (String) objInputStream.readObject();
			objInputStream.close();			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}						
		
		return strPrivInfo;	
			
	} // end getPrivInfo()

	/**************************************************************
   	* Sends command to the applet servlet controller
   	* @param urlConnection URL connection to servlet controller
   	* @param command Properties object which contains the task
   	*         the applet wants the servlet to perform
   	*/
	private void sendCommand(URLConnection urlConnection, Properties command)
	{
		try
		{
 			// inform the connection that we will send output and accept input
			urlConnection.setDoInput(true);          
			urlConnection.setDoOutput(true);
	        
			// Don't use a cached version of URL connection.
			urlConnection.setUseCaches (false);
			urlConnection.setDefaultUseCaches (false);

			// Specify the content type that we will send binary data
			urlConnection.setRequestProperty ("Content-Type", "application/octet-stream");
	        	        	         
			// Send command
			ObjectOutputStream objOutputStream = new ObjectOutputStream(urlConnection.getOutputStream());		       
			objOutputStream.writeObject(command);   
			objOutputStream.flush();	        
			objOutputStream.close();		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}				

	} // end sendCommand()


	/**************************************************************
   	* Start function
   	*/	
	public void start()
	{
	    return;
	} // end start()


	/**************************************************************
   	* Handles event when a user closes one of the documents
   	* being viewed in the document viewer
   	* @param event Event object
   	*/
    public void genericDocClosed(CMBGenericDocClosedEvent event)
    {
        CMBDocument document = event.getDocument();
        LoadedDoc loadedDoc = null;

		// Find document in the list of loaded docs        
		// and remove once it is found
        for (int i=0; i<loadedDocsList.size(); i++)
        {
        	loadedDoc = (LoadedDoc) loadedDocsList.elementAt(i);		
        	
        	if ( loadedDoc.document.equals(document) )
        	{
        		loadedDocsList.removeElementAt(i);	
        		break;
        	}
        }
        
    } // end genericDocClosed()
    
    
	/**************************************************************
   	* Handles event when a document is opened in the viewer
   	* by firing the 'fit in window' event.
   	* @param event Event object
   	*/    
    public void genericDocOpened(CMBGenericDocOpenedEvent event)
    {
		// Get toolbar components
        JToolBar tb = myGenDocViewer.getToolBar("OperationToolbar");
		Component components[] = tb.getComponents();  
			
		// Have to go through toolbar components to
		// find button that we are interested in.                          
		for (int i = 0; i < components.length; i++)                             
		{                                                                       
			if (components[i] instanceof JButton)                                   
    		{                                                                 
    			JButton button = (JButton) components[i];                         
       			Action act = button.getAction();                                 
       			String name = (String) act.getValue("Name");                     
                                    
                // If this is the "fit_window" button, then
                // programatically click it
   				if ( name.equalsIgnoreCase("fit_window") ) 
   				{
					button.doClick();
					break;	
   				}
   			}                                                                 
		} 
			                
    } // end genericDocOpened()


	/**************************************************************
   	* Handles the actionPerformed event. This method was added to
   	* handle when the user clicks on the custom toolbar button that
   	* was added to the operation toolbar.
   	* @param e Event object
   	*/
	public void actionPerformed(ActionEvent e)                         
 	{                              
 		System.out.println("e.getActionCommand():::::"+e.getActionCommand());
 		
		// Check if the saveAs_doc button was pressed                                    
 		if ( e.getActionCommand().equalsIgnoreCase("saveAs_doc") )
        {                                            
        	System.out.println("User pressed saveAs_doc toolbar button!"); // output to applet console
            CMBDocument doc = myGenDocViewer.getSelectedDocument(); 
            
            // Add implementation for this event. For example,
            // write doc to disk, assuming applet security
            // has been addressed via certificates/policyfiles...
        }
 		
 		if ( e.getActionCommand().equalsIgnoreCase("save_doc") )
 		{
 			System.out.println("dentro del listener del evento save_doc");
 			/*myGenDocViewer.setHabilitarGrabar(true);
 			myGenDocViewer.getSelectedDocument().setModified(true);
 			myGenDocViewer.getSelectedAnnotationSet().setDirty(true);*/
 			int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea guardar los cambios aplicados al documento?", "Confirmar", JOptionPane.YES_NO_OPTION );
 			switch(respuesta) { 
 				case JOptionPane.NO_OPTION:
 					myGenDocViewer.setHabilitarGrabar(Boolean.TRUE);
 					doOperation = false;
 					break;
 				case JOptionPane.YES_OPTION:
 					myGenDocViewer.setHabilitarGrabar(Boolean.FALSE);
 					doOperation = true; 					
 					break;
 			}
 		}
	} // end actionPerformed()                                                             



    /***************************************************************************
     * 
     * Private inner class which handles callbacks from CMBStreamingDocServies
     * and provides document services functionality to the document viewer.
     * 
     ***************************************************************************/
    private class StreamingDocServicesCallbacks extends CMBStreamingDocServicesCallbacks implements Serializable
    {

		/**************************************************************
	   	* Constructor
   		*/    	
        StreamingDocServicesCallbacks()
        {

        }

		/**************************************************************
	   	* Retrieve a background form.
   		*/        
        public InputStream getForm(CMBDocument document, String formName)
        {
      		System.out.println("***** StreamingDocServicesCallbacks.getForm called!!!");            	
            return null;
        }
        
		/**************************************************************
	   	* Returns true if the user is allowed to print the document
   		*/                
        public boolean getPrintPrivilege(CMBDocument document)
        {
            System.out.println("***** StreamingDocServicesCallbacks.getPrintPrivilege called!!!");

			try
 			{

				// Find document information and then determine if
				// specified privilege is in privString.
				for (int i=0; i<loadedDocsList.size(); i++)
				{
					LoadedDoc docInfo = (LoadedDoc) loadedDocsList.elementAt(i);					
					if ( document.equals(docInfo.document) )
					{
						// Found doc object - now check priv
		            	return docInfo.checkPriv("CMB_PRIV_PRINT");												
					}					
				}

 			}
 			catch (Exception e)
			{
				e.printStackTrace();
			}	            

			return false;        	        	
        }

		/**************************************************************
	   	* Returns an inputstream to the specified document part.
	   	* This is called by the generic document viewer when it needs
	   	* to obtain other parts of the document.
   		*/        
        public InputStream getPart(CMBDocument document, int partNumber, StringBuffer outMimeType)
        {
        	System.out.println("***** StreamingDocServicesCallbacks.getPart called!!!");
        	
        	InputStream docInputStream = null;
        	
			try
			{
				// First get the docid for this document. Since we can't directly
				// obtain the docid from the CMBDocument object, we must search
				// for it in our list of open documents.				
				String docid = "";
				for (int i=0; i<loadedDocsList.size(); i++)
				{
					LoadedDoc doc = (LoadedDoc) loadedDocsList.elementAt(i);
					
					if ( document.equals(doc.document) )
					{
						docid = doc.docid;
						break;
					}					
				}
								
				// Now that we have the docid, let's get the mimetype for this part.
				// This is done by sending a command to the applet controller
				// servlet to get this information.
		    	URL url1 = new URL(getCodeBase(), "AppletController");
			    URLConnection urlConnection1 = url1.openConnection();  
								
				Properties properties = new Properties();
	            properties.put("command", "getPartMimeType");            
    	        properties.put("docid", docid);
        	    properties.put("partNum", new Integer(partNumber).toString());                                
				sendCommand(urlConnection1, properties); 

				ObjectInputStream objInputStream1 = new ObjectInputStream(urlConnection1.getInputStream());
				String mimeType = (String) objInputStream1.readObject();	
				objInputStream1.close();		
			
				// Set the outMimeType parameter
				if(outMimeType != null)
				{
                    outMimeType.setLength(0);
                    outMimeType.append(mimeType);		
				}
				

				// Now lets get the url for this part.
				// This done by the applet controller servlet.
				URL url2 = new URL(getCodeBase(), "AppletController");
			    URLConnection urlConnection2 = url2.openConnection();  
				
				properties.clear();
    	        properties.put("command", "getDocURL");
	            properties.put("docid", docid);
	            properties.put("partNum", new Integer(partNumber).toString());
				sendCommand(urlConnection2, properties); 

				ObjectInputStream objInputStream2 = new ObjectInputStream(urlConnection2.getInputStream());
				String strDocURL = (String) objInputStream2.readObject();
				objInputStream2.close();		

				// Lastly, now that we have the part URL, create an inputstream to it.						
				URL docURL = new URL(strDocURL);
				URLConnection docURLConnection = docURL.openConnection();
				docInputStream = docURLConnection.getInputStream();

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}	       		
			
			return docInputStream;
        }

		/**************************************************************
	   	* Retrieve a document annotation. 
   		*/
        public InputStream getAnnotation(CMBDocument document, int annotationNumber)
        {
			System.out.println("***** StreamingDocServicesCallbacks.getAnnotation called!!!");
	        return null;
        }

		/**************************************************************
	   	* Retrieve a document's resources.
   		*/
        public InputStream getResources(CMBDocument document)
        {
        	// No support for OnDemand objects in the applet yet so this should never get called
        	System.out.println("***** StreamingDocServicesCallbacks.getResources called!!!");
            InputStream resourceStream = new ByteArrayInputStream(new byte[0]);
            return resourceStream;
        }

        /**************************************************************
	   	* Returns true if trace is enabled
   		*/  
        public boolean traceEnabled() 
        {		
            return false;
        }

        /**************************************************************
	   	* Writes specified trace message to standard output
   		*/  
        public void trace(String message) 
        {
            System.out.println("StreamingDocServicesCallbacks.trace -- " + message);
            return;
        }

		/**************************************************************
	   	* Sends command to applet controller servlet
   		*/        
      	private void sendCommand(URLConnection urlConnection, Properties command)
		{
			try
			{
 				// inform the connection that we will send output and accept input
				urlConnection.setDoInput(true);          
				urlConnection.setDoOutput(true);
	        
				// Don't use a cached version of URL connection.
				urlConnection.setUseCaches (false);
				urlConnection.setDefaultUseCaches (false);

				// Specify the content type that we will send binary data
				urlConnection.setRequestProperty ("Content-Type", "application/octet-stream");
	        	        	         
				// Send command
				ObjectOutputStream objOutputStream = new ObjectOutputStream(urlConnection.getOutputStream());		       
				objOutputStream.writeObject(command);   
				objOutputStream.flush();	        
				objOutputStream.close();		
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}				
		}	       
		 
    } // end StreamingDocServicesCallbacks class


    /***************************************************************************
     * 
     * Private inner class which handles callbacks from CMBAnnotationDocServices
     * and provides annotation services functionality to the document viewer.
     * 
     ***************************************************************************/
    private class AnnotationServicesCallbacks extends CMBAnnotationServicesCallbacks implements Serializable
    {

		/**************************************************************
	   	* Retrieve an annotation part.
   		*/
        public InputStream getAnnotationPart(CMBAnnotationSet as, int i)
        {
        	// CM annotations are stored as a single part, so this method is not
        	// needed for viewing CM documents (since the annotation part is already
        	// loaded in the loadDocument() method.
            System.out.println("----------AnnotationServicesCallbacks.getAnnotationPart()");
            return null;
        }

        /**************************************************************
	   	* Returns true if specified privilege is allowed.
   		*/  
        public boolean getPrivilege(CMBAnnotationSet as, int priv)
        {        	
            System.out.println("----------AnnotationServicesCallbacks.getPrivilege()");


			try
 			{

				// Find document information and then determine if
				// specified privilege is in privString.
				for (int i=0; i<loadedDocsList.size(); i++)
				{
					LoadedDoc docInfo = (LoadedDoc) loadedDocsList.elementAt(i);					
					if ( as.equals(docInfo.annSet) )
					{
						// Found doc object - now check priv
			            if (priv == CMBPrivilege.CMB_PRIV_CREATE_ANNOTATION)
			            	return docInfo.checkPriv("CMB_PRIV_CREATE_ANNOTATION");			            	

			            if (priv == CMBPrivilege.CMB_PRIV_DELETE_ANNOTATION)
							return docInfo.checkPriv("CMB_PRIV_DELETE_ANNOTATION");							
							
            			if (priv == CMBPrivilege.CMB_PRIV_VIEW_ANNOTATION)
            				return docInfo.checkPriv("CMB_PRIV_VIEW_ANNOTATION");

			            if (priv == CMBPrivilege.CMB_PRIV_PRINT)
			            	return docInfo.checkPriv("CMB_PRIV_PRINT");					
							
						break;
					}					
				}

 			}
 			catch (Exception e)
			{
				e.printStackTrace();
			}	            

			return false;
		                        
        } 

		/**************************************************************
	   	* Add an annotation part 
   		*/
        public int addAnnotationPart(CMBAnnotationSet as, byte[] data)
        {
            System.out.println("----------AnnotationServicesCallbacks.addAnnotationPart()");
            if(doOperation)
            {
				try
	 			{
	 				// First get the docid for this document. Since we can't directly
					// obtain the docid from the CMBAnnotationSet object, we must search
					// for it in our list of open documents.
					//
					String docid = "";
					for (int i=0; i<loadedDocsList.size(); i++)
					{
						LoadedDoc doc = (LoadedDoc) loadedDocsList.elementAt(i);
						
						if ( as.equals(doc.annSet) )
						{
							docid = doc.docid;
							break;
						}					
					}
	
					// Send add annotation command to servlet, along
					// with annotation byte array
	 				URL url = new URL(getCodeBase(), "AppletController");
				    URLConnection urlConnection = url.openConnection();  
									
					Properties properties = new Properties();
		            properties.put("command", "addAnnPart");            
	    	        properties.put("docid", docid);
	        	    properties.put("annotation", data);
					sendCommand(urlConnection, properties); 
					
					// Get return code from servlet
					ObjectInputStream objInputStream = new ObjectInputStream(urlConnection.getInputStream());
					String rc = (String) objInputStream.readObject();	
					objInputStream.close();	
	
					System.out.println("addAnnotationPart rc = " + rc);
					
					//updateDocumentAnnotation(docid); //para agregar las anotaciones al pdf original
					convertDocument(docid);
	 			}
	 			catch (Exception e)
				{
					e.printStackTrace();
				}
            }
            /*else 
            {
            	habilitarBotonGrabar();
            }*/
            return -1;
        }

		/**************************************************************
	   	* Updates an existing annotation.  part
   		*/
        public void updateAnnotationPart(CMBAnnotationSet as, byte[] data, int index)
        {
 			System.out.println("----------AnnotationServicesCallbacks.updateAnnotationPart()");
 			if(doOperation)
 			{
				try
	 			{
	 				// First get the docid for this document. Since we can't directly
					// obtain the docid from the CMBAnnotationSet object, we must search
					// for it in our list of open documents.
					//
					String docid = "";
					for (int i=0; i<loadedDocsList.size(); i++)
					{
						LoadedDoc doc = (LoadedDoc) loadedDocsList.elementAt(i);
						
						if ( as.equals(doc.annSet) )
						{
							docid = doc.docid;
							break;
						}					
					}
	
					// Send update annotation command to servlet, along
					// with annotation byte array
	 				URL url = new URL(getCodeBase(), "AppletController");
				    URLConnection urlConnection = url.openConnection();  
									
					Properties properties = new Properties();
		            properties.put("command", "updateAnnPart");            
	    	        properties.put("docid", docid);
	        	    properties.put("annotation", data);
	        	    properties.put("index", new Integer(index).toString());
					sendCommand(urlConnection, properties); 
					
					// Get return code from servlet
					ObjectInputStream objInputStream = new ObjectInputStream(urlConnection.getInputStream());
					String rc = (String) objInputStream.readObject();	
					objInputStream.close();	
					
					System.out.println("updateAnnotationPart rc = " + rc);
					
					//updateDocumentAnnotation(docid); //para agregar las anotaciones al pdf original
					convertDocument(docid);
	 			}
	 			catch (Exception e)
				{
					e.printStackTrace();
				}
 			}
 			/*else
 			{
 				habilitarBotonGrabar();
 			}*/
        }

        /**************************************************************
	   	* Removes an annotation part
   		*/  
        public void removeAnnotationPart(CMBAnnotationSet as, int index)
        {
            System.out.println("----------AnnotationServicesCallbacks.removeAnnotationPart()");
			if(doOperation)
			{
	            try
	 			{
	 				// First get the docid for this document. Since we can't directly
					// obtain the docid from the CMBAnnotationSet object, we must search
					// for it in our list of open documents.
					//
					String docid = "";
					for (int i=0; i<loadedDocsList.size(); i++)
					{
						LoadedDoc doc = (LoadedDoc) loadedDocsList.elementAt(i);
						
						if ( as.equals(doc.annSet) )
						{
							docid = doc.docid;
							break;
						}					
					}
	
					// Send remove annotation command to servlet
	 				URL url = new URL(getCodeBase(), "AppletController");
				    URLConnection urlConnection = url.openConnection();  
									
					Properties properties = new Properties();
		            properties.put("command", "removeAnnPart");            
	    	        properties.put("docid", docid);
	        	    properties.put("index", new Integer(index).toString());
					sendCommand(urlConnection, properties); 
					
					// Get return code from servlet
					ObjectInputStream objInputStream = new ObjectInputStream(urlConnection.getInputStream());
					String rc = (String) objInputStream.readObject();	
					objInputStream.close();	
					
					System.out.println("removeAnnotationPart rc = " + rc);
					
					//updateDocumentAnnotation(docid); //para agregar las anotaciones al pdf original
					convertDocument(docid);
	 			}
	 			catch (Exception e)
				{
					e.printStackTrace();
				}	
			}
			/*else
			{
				habilitarBotonGrabar();
			}*/
        }

        /**************************************************************
	   	* Returns true if trace is enabled
   		*/  
        public boolean traceEnabled()
        {
            return false;			
        }

        /**************************************************************
	   	* Writes specified trace message to standard output
   		*/  
        public void trace(String message)
        {
        	System.out.println("AnnotationServicesCallbacks.trace -- " + message);
        }

		/**************************************************************
	   	* Sends command to applet controller servlet
   		*/ 
      	private void sendCommand(URLConnection urlConnection, Properties command)
		{
			try
			{
 				// inform the connection that we will send output and accept input
				urlConnection.setDoInput(true);          
				urlConnection.setDoOutput(true);
	        
				// Don't use a cached version of URL connection.
				urlConnection.setUseCaches (false);
				urlConnection.setDefaultUseCaches (false);

				// Specify the content type that we will send binary data
				urlConnection.setRequestProperty ("Content-Type", "application/octet-stream");
	        	        	         
				// Send command
				ObjectOutputStream objOutputStream = new ObjectOutputStream(urlConnection.getOutputStream());		       
				objOutputStream.writeObject(command);   
				objOutputStream.flush();	        
				objOutputStream.close();		
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}				
		}	      
		  
    } // end AnnotationServicesCallbacks class
    

	/***************************************************************************
    * 
    * Private inner class which holds information about which
    * documents are currently loaded in the document viewer.
    * 
    ***************************************************************************/    
    private class LoadedDoc
    {
		public CMBDocument document;
		public CMBAnnotationSet annSet;
		public String docid;
		public String privInfo;

		public LoadedDoc(CMBDocument document, CMBAnnotationSet annSet, String docid, String privInfo)
		{
			this.document = document;
			this.annSet   = annSet;
			this.docid    = docid;
			this.privInfo = privInfo;												
		}		
		
		public boolean checkPriv(String privName)
		{
			boolean rc = false;
			
			try
			{				
				// convert strings to lower case so we can easily find chars
				privName = privName.toLowerCase();
				String tmpPrivInfoStr = privInfo.toLowerCase();
			
				// Find the "<priv>=<val>" string in the privInfo string
				int x = tmpPrivInfoStr.indexOf(privName);
				int y = tmpPrivInfoStr.indexOf(",", x);	
				if (y<0) y = tmpPrivInfoStr.length();			
				String priv = tmpPrivInfoStr.substring(x,y);
				
				// Get the val portion from the <priv>=<val> string
				x = priv.indexOf("=");
				String val = priv.substring(x+1);
				
				// If value is 2, set rc to true
				if ( val.equalsIgnoreCase("2") )
					rc = true;
			}
			catch (Exception e)
			{
				System.out.println("Error in LoadedDoc.checkPriv");
				e.printStackTrace();	
			}			

			System.out.println("LoadedDoc.checkPriv: " + privName + " = " + rc);
						
			return rc;
		}
		
	} // end LoadedDoc class
    
    
    /**
     * Habilita el boton Save Document
     */
    public void habilitarBotonGrabar()
    {
    	JToolBar tb = myGenDocViewer.getToolBar("OperationToolbar");
		Component components[] = tb.getComponents();
		
		// Have to go through toolbar components to
		// find button that we are interested in.                          
		for (int i = 0; i < components.length; i++)                             
		{                                                                       
			if (components[i] instanceof JButton)                                   
  			{                                                                 
  				JButton button = (JButton) components[i];                         
   				Action act = button.getAction();                                 
   				String name = (String) act.getValue("Name");
                if ( name.equalsIgnoreCase("Save Document") || name.equalsIgnoreCase("Guardar documento"))
  				{
                	myGenDocViewer.getSelectedDocument().setModified(true);
                	button.setEnabled(myGenDocViewer.canSave());
                	break;
  				}
  			}                                                                 
		}
    }
    
    
    /**
     * Devuelve true si el boton Save Document se encuentra habilitado. 
     * Devuelve false si el boton Save Document se encuentra deshabilitado.
     */
    public boolean botonGrabarHabilitado()
    {
    	boolean habilitado = false;
    	JToolBar tb = myGenDocViewer.getToolBar("OperationToolbar");
		Component components[] = tb.getComponents();  
		
		// Have to go through toolbar components to
		// find button that we are interested in.                          
		for (int i = 0; i < components.length; i++)                             
		{                                                                       
			if (components[i] instanceof JButton)                                   
  			{                                                                 
  				JButton button = (JButton) components[i];                         
   				Action act = button.getAction();                                 
   				String name = (String) act.getValue("Name");
                                
   				if ( name.equalsIgnoreCase("Save Document") || name.equalsIgnoreCase("Guardar documento"))
  				{
   					habilitado = button.isEnabled();
   					break;
  				}
  			}                                                                 
		}
		return habilitado;
    }
    
    
    /**
     * Metodo que crea un PDF a partir del archivo TIF con sus anotaciones
     * @param docid
     */
    public void convertDocument(String docid)
	{
		try
		{
			File dir = new File(descargados);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			dir = new File(transferencias);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			// CARGAR TIF CON ANOTACIONES
			String tmpFileName = "TMP"+String.valueOf(System.currentTimeMillis())+".tif";
			OutputStream output = new FileOutputStream(descargados + File.separator + tmpFileName);
			myGenDocViewer.exportDocument(output, true, false, "image/tiff");
			output.close();
			
			// CONVERTIR TIF A PDF
			RandomAccessFileOrArray myTiffFile = new RandomAccessFileOrArray(descargados + File.separator + tmpFileName);
	        int numberOfPages = TiffImage.getNumberOfPages(myTiffFile);
	        Document TifftoPDF = new Document();
	        TifftoPDF.setMargins(0, 0, 0, 0);
	        PdfWriter.getInstance(TifftoPDF, new FileOutputStream(transferencias + File.separator + type + "-AN.pdf"));
	        TifftoPDF.open();
	        for(int i=1; i<=numberOfPages; i++)
	        {
	            Image tempImage = TiffImage.getTiffImage(myTiffFile, i);
	            float dpiX = tempImage.getDpiX() == 0 ? 200 : tempImage.getDpiX();
	            float dpiY = tempImage.getDpiY() == 0 ? 200 : tempImage.getDpiY();
	            float factorX = 72 / dpiX;
	            float factorY = 72 / dpiY;
	            tempImage.scaleAbsolute(factorX * tempImage.getWidth(), factorY * tempImage.getHeight());
	            TifftoPDF.add(tempImage);
	        }
	        myTiffFile.close();
	        TifftoPDF.close();
	        
	        // BORRAR EL TIFF
	        File file = new File(descargados + File.separator + tmpFileName);
    		if(file.delete())
    		{
    			System.out.println(file.getName() + " eliminado");
    		}
    		else
    		{
    			file.deleteOnExit();;
    			System.out.println("Eliminacion fallo");
    		}
	        
	        System.out.println("Fin de la creacion del PDF con anotaciones");
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
    
    
    /**
     * Metodo que obtiene el documento actualizado con sus respectivas anotaciones.
     * Invoca al metodo que agregara las anotaciones al nuevo PDF
     * @param docid
     */
    public void updateDocumentAnnotation(String docid)
    {
    	try{
	    	String partMimeType = getPartMimeType(docid, "0");			
			System.out.println("partMimeType = " + partMimeType);			
	
			// Get number of parts
			int numParts = getNumParts(docid);
			System.out.println("numParts = " + numParts);
	
			// Get url location of the first part of this document
			String docURL = getDocURL(docid, "0");
			System.out.println("docURL = " + docURL);			
			
			// Get inputstream from document url
			URL url1 = new URL(docURL);
			URLConnection urlConnection1 = url1.openConnection();
			InputStream docInputStream = urlConnection1.getInputStream();
	
			// Load document
			CMBDocument currDocument = myGenDocViewer.loadDocument(docInputStream, numParts, partMimeType, partMimeType, null, null);       		
			
	    	if(currDocument == null) 
			{
	           	System.out.println("AppletViewer.loadDocument - currDocument is null");
	       	}
	 		
			// Get url location of annotation part
			String annURL = getAnnotationURL(docid);
			System.out.println("annURL = " + annURL);
			
			// If there is no annotation part, then create inputstream
			// to an empty byte array. Else create inputstream to the
			// url location.
			InputStream annInputStream;
			if (annURL == null)
			{
		        annInputStream = new ByteArrayInputStream(new byte[0]);
			}
			else
			{
				URL url2 = new URL(annURL);
				URLConnection urlConnection2 = url2.openConnection();
				annInputStream = urlConnection2.getInputStream();
			}			
	
			// Get position of annotations
	   		int annPosition = currDocument.getAnnotationPosition();					
			
			if (annPosition == 0) 
				annPosition = 14336;
			
			// Load annotations       		       		
			CMBAnnotationSet annSet= myGenDocViewer.loadAnnotations(annInputStream, "application/vnd.ibm.modcap", annPosition, 1, 0);
			
			PdfReader reader = new PdfReader("C://Prueba.pdf"); // input PDF
	        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("C://Prueba_modified.pdf")); // output PDF
			Iterator iterator = annSet.getIterator();
			while(iterator.hasNext())
			{
				CMBPageAnnotation pageAnnotation = (CMBPageAnnotation)iterator.next();
				//System.out.println("tipo:::::"+pageAnnotation.getType());
				//System.out.println("nombre:::::"+pageAnnotation.getName());
				//System.out.println("pagina:::::"+pageAnnotation.getPageNumber());
				CMBTextAnnotation textAnnotation = (CMBTextAnnotation)pageAnnotation;
				//System.out.println("texto:::::"+textAnnotation.getTextLine());
				//System.out.println("posicionX:::::"+textAnnotation.getDrawRect().getX());
				//System.out.println("posicionY:::::"+textAnnotation.getDrawRect().getY());
				//System.out.println("tamanho:::::"+textAnnotation.getFont().getSize());
				//System.out.println("tamanho2D:::::"+textAnnotation.getFont().getSize2D());
				
				if("TEXT".equals(pageAnnotation.getName().toUpperCase()))
				{
					addTextAnnotation(stamper, pageAnnotation.getPageNumber(), (float)textAnnotation.getDrawRect().getX(), (float)textAnnotation.getDrawRect().getY(), textAnnotation.getTextLine(), 12);
				}
			}
			stamper.close();
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    
    /**
     * Metodo que agrega annotaciones (en texto) al nuevo PDF
     * @param stamper
     * @param pageNumber
     * @param x
     * @param y
     * @param text
     * @param size
     */
    public void addTextAnnotation(PdfStamper stamper, int pageNumber, float x, float y, String text, float size)
    {
    	try
    	{
	    	BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED); // set font
	
            PdfContentByte over = stamper.getOverContent(pageNumber);
            
            float positionX = ((x * 2480) / 1654);
            float positionY = ((y * 3508) / 2339);
            
            // write text
            over.beginText();
            over.setFontAndSize(bf, size); // set font and size
            over.setTextMatrix(positionX, positionY); // set x,y position (0,0 is at the bottom left)
            over.showText(text); // set text
            over.endText();
	        
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
		
} // end AppletViewer class
