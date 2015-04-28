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
** FILENAME : AppletController.java
**    
** SAMPLE   : Information Integrator for Content Version 8 Applet Viewer
**            Sample
**
** ABSTRACT : This file is included in the Applet Viewer Sample EAR
**            file (AppletViewer.EAR). When the applet requires 
**            information from the server, it will ask this servlet to
**            obtain the information. It has direct connectivity with
**            the backend server and performs functions such as retrieving
**            the url location for parts, save annotation parts, and
**            obtaining document metadata
**                                    
** Date     : Sept 25, 2003
**                                                           
** Authors  : Bryan Daniel (bryand@us.ibm.com)   
**			  Jerald Schoudt              
**                                                            	                                 
*****************************************************************************/

package com.ibm.bbva.applet.servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.util.Util;
import com.ibm.mm.beans.CMBAnnotation;
import com.ibm.mm.beans.CMBBaseConstant;
import com.ibm.mm.beans.CMBConnection;
import com.ibm.mm.beans.CMBDataManagement;
import com.ibm.mm.beans.CMBException;
import com.ibm.mm.beans.CMBItem;
import com.ibm.mm.beans.CMBNoConnectionException;
import com.ibm.mm.beans.CMBObject;
import com.ibm.mm.beans.CMBPrivilege;
import com.ibm.mm.sdk.common.DKConstantICM;


public class AppletController extends HttpServlet implements DKConstantICM {
	
	private static final Logger logger = LoggerFactory.getLogger(AppletController.class);

	
	/**************************************************************
   	* Initializes the servlet
   	* @param config The servlets configuration information
   	*/
   	public void init(ServletConfig config) throws ServletException 
   	{   		
    	super.init(config);     	     	
   	}


	/***************************************************************
   	* Process incoming requests for a HTTP POST method
   	* @param request The incoming request information
   	* @param response The outgoing response information
   	*/   
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 
	{			
		
		CMBConnection connBean = new CMBConnection();
		try
		{		
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Linea 1");
			// Get session object
			//HttpSession session = request.getSession(false);
    		// Get connection object (CMBConnection) from session
   			// (It is placed in session by index.jsp)
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Linea 2");
			//CMBConnection connBean = (CMBConnection) session.getAttribute("connection");
			connBean.setUserid(Util.leerPropiedad("cmuserid"));
			connBean.setPassword(Util.leerPropiedad("cmpassword"));
			connBean.setServerName(Util.leerPropiedad("cmserver"));
			connBean.setDsType("ICM");
			connBean.connect();
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Linea 3");
			//if ( connBean == null )
				////+POR SOLICITUD BBVA+LOG.info("ERROR : Connection object not in session!");
			
			// Get properties object that applet sends
			ObjectInputStream inputFromApplet = new ObjectInputStream(request.getInputStream());
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Linea 4");
	        Properties properties = (Properties) inputFromApplet.readObject();
	        //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Linea 5");
    	    inputFromApplet.close();
    	    //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Linea 6");
    	    // Get task applet wants servlet to do
    	    String command = properties.getProperty("command");
			logger.info("AppletController received command :'" + command + "'.");    	    
    	    //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Linea 7");
			// Handle getItemMimeType command
    	    if ( command.equalsIgnoreCase("getItemMimeType") )
    	    {
    	    	String docid = properties.getProperty("docid");
    	    	String itemMimeType = getItemMimeType(connBean, docid);
    	    	sendToApplet(response, itemMimeType);
    	    }

			// Handle getPartMimeType command
    	    if ( command.equalsIgnoreCase("getPartMimeType") )
    	    {
    	    	//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Linea 8");
    	    	String docid = properties.getProperty("docid");
    	    	logger.info("docid: " + docid);
    	    	String strPartNum = properties.getProperty("partNum");
    	    	logger.info("strPartNum: " + strPartNum);
    	    	String partMimeType = getPartMimeType(connBean, docid, strPartNum);
    	    	logger.info("partMimeType: " + partMimeType);
    	    	sendToApplet(response, partMimeType);
    	    }
    	    
    	    // Handle getDocURL command
    	    if ( command.equalsIgnoreCase("getDocURL") )
    	    {
    	    	String docid = properties.getProperty("docid");
    	    	String strPartNum = properties.getProperty("partNum");
    	    	String docURL = getDocURL(connBean, docid, strPartNum);
    	    	sendToApplet(response, docURL);
    	    }
    	    
    	    // Handle getAnnURL command
    	    if ( command.equalsIgnoreCase("getAnnURL") )
    	    {
    	    	String docid = properties.getProperty("docid");
    	    	String annURL = getAnnURL(connBean, docid);
    	    	sendToApplet(response, annURL);
    	    }
    	    
    	    // Handle getNumParts command
    	    if ( command.equalsIgnoreCase("getNumParts") )
    	    {
    	    	String docid = properties.getProperty("docid");
    	    	int numParts = getNumParts(connBean, docid);
    	    	String strNumParts = Integer.toString(numParts);
    	    	sendToApplet(response, strNumParts);
    	    }
    	    
    	    // Handle updateAnnPart command
    	    if ( command.equalsIgnoreCase("updateAnnPart") )
    	    {
    	    	String docid = properties.getProperty("docid");
    	    	byte[] annotation = (byte[]) properties.get("annotation");
    	    	String index = (String) properties.getProperty("index");
				String rc = updateAnnPart(connBean, docid, annotation, index);
				sendToApplet(response, rc);
    	    }
    	    
    	    // Handle addAnnPart command
    	    if ( command.equalsIgnoreCase("addAnnPart") )
    	    {
    	    	String docid = properties.getProperty("docid");
    	    	byte[] annotation = (byte[]) properties.get("annotation");
				String rc = addAnnPart(connBean, docid, annotation);
				sendToApplet(response, rc);
    	    }

    	    // Handle removeAnnPart command
    	    if ( command.equalsIgnoreCase("removeAnnPart") )
    	    {
    	    	String docid = properties.getProperty("docid");
    	    	String index = (String) properties.getProperty("index");
				String rc = removeAnnPart(connBean, docid, index);
				sendToApplet(response, rc);
    	    }
    	    
    	    // Handle getPrivInfo command
    	    if ( command.equalsIgnoreCase("getPrivInfo") )
    	    {
    	    	String docid = properties.getProperty("docid");
				String rc = getPrivInfo(connBean, docid);
				sendToApplet(response, rc);
    	    }    	       	    																														
		} 		
		catch (java.lang.Exception e) 
		{
			logger.error("Error has occurred in AppletController.doPost()", e);
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println(e.getMessage());
			//e.printStackTrace();
		} finally
		{
			if (connBean != null) {
				try {
					connBean.disconnect();
				} catch (CMBNoConnectionException e) {
					logger.warn(e.getMessage(), e);
				} catch (CMBException e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}
						
	} // end doPost()

	
	/***************************************************************
   	* Process incoming requests for a HTTP GET method
   	* @param request The incoming request information
   	* @param response The outgoing response information
   	*/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{					
	} // end doGet()

	
	/***************************************************************
   	* Returns the mime type for the specified document
   	* @param connBean Connection obect
   	* @param docid The document id that the mime type is needed for
   	*/
	private String getItemMimeType(CMBConnection connBean, String docid)
	{
		String itemMimeType = "";
		
		try
		{
			// Get item meta-data
	  		CMBItem itemBean = new CMBItem(docid);
	  		CMBDataManagement dataBean = connBean.getDataManagement();
	  		dataBean.setDataObject(itemBean);
	  		dataBean.retrieveItem();
	  		
	  		// Get item mime-type
			itemMimeType = dataBean.getItemMimeType();
	  			  					
		} 		
		catch (CMBException e) 
		{
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}
		catch (java.lang.Exception e) 
		{
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}	
		
		logger.info("itemMimeType = " + itemMimeType);
		return itemMimeType;
		
	} // end getMimeType()


	/***************************************************************
   	* Returns the mime type for the specified part
   	* @param connBean Connection obect
   	* @param docid The document id 
   	* @param strPartNum Part number
   	*/
	private String getPartMimeType(CMBConnection connBean, String docid, String strPartNum)
	{
		String partMimeType = "";
		
		try
		{
			// Retrieve part meta-data (not the content)
	  		CMBItem itemBean = new CMBItem(docid);
	  		CMBDataManagement dataBean = connBean.getDataManagement();
	  		dataBean.setDataObject(itemBean);
	  		dataBean.retrieveItem();
	  		
	  		// Get part mime type
	  		int partNum = new Integer(strPartNum).intValue();	  		
	  		CMBObject docPart = dataBean.getContent(partNum);	
			partMimeType = docPart.getMimeType();	  			  					
		} 		
		catch (CMBException e) 
		{
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}
		catch (java.lang.Exception e) 
		{
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}	
		
		logger.info("partMimeType = " + partMimeType);
		return partMimeType;			
		
	}
	
	
	/***************************************************************
   	* Returns the URL location of the specified document
   	* @param connBean Connection obect
   	* @param docid The document id that the mime type is needed for
   	*/
	private String getDocURL(CMBConnection connBean, String docid, String strPartNum)
	{
		String docURL = "";
		
		try
		{
			// Get item meta-data
	  		CMBItem itemBean = new CMBItem(docid);
	  		CMBDataManagement dataBean = connBean.getDataManagement();
	  		dataBean.setDataObject(itemBean);
	  		dataBean.retrieveItem();
	  		
	  		// Get document url
	  		int partNum = new Integer(strPartNum).intValue();
	  		CMBObject docPart = dataBean.getContent(partNum);	  		
	  		java.net.URL url = docPart.getDataURL();
	  		logger.info("++url : "+url.toString());
	  		
	  		docURL = changeHostByIP(url.toString());
		} 		
		catch (CMBException e) 
		{
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}
		catch (java.lang.Exception e) 
		{
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}	

 		logger.info("docURL = " + docURL);
		return docURL;							
		
	} // end getDocURL()


	/***************************************************************
   	* Returns the URL location of the annotation part for
   	* the specified document
   	* @param connBean Connection obect
   	* @param docid Document id
   	*/
	private String getAnnURL(CMBConnection connBean, String docid)
	{
		// NOTE: For Content Manager, all annotations on a document are 
		// typically encoded into a single annotation part.

		String annURL = null;
		
		try
		{
			// Get document meta-data
	  		CMBItem itemBean = new CMBItem(docid);
	  		CMBDataManagement dataBean = connBean.getDataManagement();
	  		dataBean.setDataObject(itemBean);
	  		dataBean.retrieveItem();
	
			// Determine if document has annotation parts
			int numAnnParts = dataBean.getAnnotationCount();
			
			// If there is an annotation part, then get URL
			if (numAnnParts > 0)
			{
				CMBAnnotation annBean = dataBean.getAnnotation(0);				
		  		CMBObject annPart = annBean.getComplexData();
	  			java.net.URL url = annPart.getDataURL();
	  			
	  			annURL = changeHostByIP(url.toString());		  			  		  				
			}
		} 		
		catch (CMBException e) 
		{
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}
		catch (java.lang.Exception e) 
		{
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}	

 		logger.info("annURL = " + annURL);
		return annURL;			
						
	} // end getAnnURL();
	

	/***************************************************************
   	* Returns the number of parts in the specified document
   	* @param connBean Connection obect
   	* @param docid Document id
   	*/	
	private int getNumParts(CMBConnection connBean, String docid)
	{
		int numParts = 0;
		
		try
		{
			// Get document meta-data
	  		CMBItem itemBean = new CMBItem(docid);
	  		CMBDataManagement dataBean = connBean.getDataManagement();
	  		dataBean.setDataObject(itemBean);
	  		dataBean.retrieveItem();
	  		
	  		// Get number of parts in document	  		
	  		numParts = dataBean.getContentCount();
		} 		
		catch (CMBException e) 
		{
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}
		catch (java.lang.Exception e) 
		{
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}	

 		logger.info("numParts = " + numParts);
		return numParts;
									
	} // end getNumParts()


	/***************************************************************
   	* Updates annotation in server
   	* @param connBean Connection obect
   	* @param docid Document id
   	* @param bAnnotation Annotation part data
   	* @param index Annotation index
   	*/
	private String updateAnnPart(CMBConnection connBean, String docid, byte[] bAnnotation, String index)
	{			
		String rc = "";
		
		try
		{
			// Get document meta-data
	  		CMBItem itemBean = new CMBItem(docid);
	  		CMBDataManagement dataBean = connBean.getDataManagement();
	  		dataBean.setDataObject(itemBean);
	  		dataBean.retrieveItem();	  		

			// We must checkout document before we can update its parts	  				
			if ( dataBean.isCheckedOut() )
			{
				dataBean.checkIn();
			}

			dataBean.checkOut();

			// Get annotation object				
			int numAnnotations = dataBean.getAnnotationCount();
			CMBAnnotation annotation = new CMBAnnotation(bAnnotation, 2, CMBBaseConstant.CMB_DSTYPE_ICM);
			annotation.setMimeType("application/vnd.ibm.modcap");					

			// Store annotation
			if( numAnnotations == 0)
			{
            	dataBean.addAnnotation(annotation);
			}
            else
            {            	
                dataBean.updateAnnotation(new Integer(index).intValue(), annotation);
            }
	            
            dataBean.checkIn();

			rc = "Annotation part updated!";

		} 		
		catch (CMBException e) 
		{
			rc = "Error updating annotation part: " +  e.getMessage();
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}
		catch (Exception e) 
		{
			rc = "Error updating annotation part: " +  e.getMessage();
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}	
		
		logger.info(rc);
		return rc;
						
	} // end updateAnnPart()


	/***************************************************************
   	* Adds annotation to specified document
   	* @param connBean Connection obect
   	* @param docid Document id
   	* @param bAnnotation Annotation part data
   	*/
	private String addAnnPart(CMBConnection connBean, String docid, byte[] bAnnotation)
	{			
		String rc = "";
		
		try
		{
			// Get document meta-data
	  		CMBItem itemBean = new CMBItem(docid);
	  		CMBDataManagement dataBean = connBean.getDataManagement();
	  		dataBean.setDataObject(itemBean);
	  		dataBean.retrieveItem();	  		

			// We must checkout document before we can add parts	  				
			if ( dataBean.isCheckedOut() )
			{
				dataBean.checkIn();
			}

			dataBean.checkOut();

			// Get annotation object				
			int numAnnotations = dataBean.getAnnotationCount();
			CMBAnnotation annotation = new CMBAnnotation(bAnnotation, CMBAnnotation.CMB_ANNOTATION_TYPE_BINARY);			
			annotation.setMimeType("application/vnd.ibm.modcap");					

			// Add annotation
            dataBean.addAnnotation(annotation);

            dataBean.checkIn();

			rc = "Annotation part added!";

		} 		
		catch (CMBException e) 
		{
			rc = "Error updating annotation part: " +  e.getMessage();
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}
		catch (Exception e) 
		{
			rc = "Error updating annotation part: " +  e.getMessage();
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}	
		
		logger.info(rc);
		return rc;
						
	} // end addAnnPart()
	
	
	/***************************************************************
   	* Removes annotation part of specified document
   	* @param connBean Connection obect
   	* @param docid Document id
   	* @param bAnnotation Annotation part data
   	* @param index Annotation index
   	*/
	private String removeAnnPart(CMBConnection connBean, String docid, String index)
	{			
		String rc = "";
		
		try
		{
			// Get document meta-data
	  		CMBItem itemBean = new CMBItem(docid);
	  		CMBDataManagement dataBean = connBean.getDataManagement();
	  		dataBean.setDataObject(itemBean);
	  		dataBean.retrieveItem();	  		

			// We must checkout document before we can make changes	  				
			if ( dataBean.isCheckedOut() )
			{
				dataBean.checkIn();
			}

			dataBean.checkOut();

			// Remove part
			int partNum = new Integer(index).intValue();
			dataBean.deleteAnnotation(partNum);

            dataBean.checkIn();

			rc = "Annotation part removed!";

		} 		
		catch (CMBException e) 
		{
			rc = "Error updating annotation part: " +  e.getMessage();
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}
		catch (Exception e) 
		{
			rc = "Error updating annotation part: " +  e.getMessage();
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}	
		
		logger.info(rc);
		return rc;
						
	} // end removeAnnPart()	
	

	/***************************************************************
   	* Returns a string of privilege information to applet
   	* @param connBean Connection obect
   	* @param docid Document id
   	*/	
	private String getPrivInfo(CMBConnection connBean, String docid)
	{
		String rc = "";
				
		try
		{
			// Get document meta-data
	  		CMBItem itemBean = new CMBItem(docid);
	  		CMBDataManagement dataBean = connBean.getDataManagement();
	  		dataBean.setDataObject(itemBean);
	  		dataBean.retrieveItem();	  		

			CMBPrivilege privBean = dataBean.getPrivilege();

			short createPriv =	privBean.checkAuthorization(CMBPrivilege.CMB_PRIV_CREATE_ANNOTATION);			
			rc = "CMB_PRIV_CREATE_ANNOTATION=" + createPriv;

			short deletePriv =	privBean.checkAuthorization(CMBPrivilege.CMB_PRIV_DELETE_ANNOTATION);
			rc = rc + ",CMB_PRIV_DELETE_ANNOTATION=" + deletePriv;
			
			short viewPriv = privBean.checkAuthorization(CMBPrivilege.CMB_PRIV_VIEW_ANNOTATION);
			rc = rc + ",CMB_PRIV_VIEW_ANNOTATION=" + viewPriv;
			
			short modifyPriv = privBean.checkAuthorization(CMBPrivilege.CMB_PRIV_MODIFY_SINGLE_ANNOTATION);
			rc = rc + ",CMB_PRIV_MODIFY_SINGLE_ANNOTATION=" + modifyPriv;
			
			short printPriv = privBean.checkAuthorization(CMBPrivilege.CMB_PRIV_PRINT);
			rc = rc + ",CMB_PRIV_PRINT=" + printPriv;
		}
		catch (CMBException e) 
		{
			rc = "Error getting privilege info: " +  e.getMessage();
			logger.error(e.getMessage(), e);
			//e.printStackTrace();			
		}		
		catch (Exception e) 
		{
			rc = "Error getting privilege info: " +  e.getMessage();
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}	
		
		return rc;
		
	} // end getPrivInfo()
	
	
	/***************************************************************
   	* Sends object to applet
   	* @param response The outgoing response information
   	* @param obj Object to send to applet
   	*/	
	private void sendToApplet(HttpServletResponse response, Object obj)
	{    
    	try
     	{     		
     		ObjectOutputStream outputToApplet = new ObjectOutputStream(response.getOutputStream());
	    	outputToApplet.writeObject(obj);
		    outputToApplet.flush();         
	        outputToApplet.close();    	    			    	    	     		
		}
		catch (java.lang.Exception e) 
		{
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}	
		
	} // end sendToApplet()
	
	private String changeHostByIP(String docURL) {
		InetAddress addr;
		String cadena = "";
		try {
			addr = InetAddress.getLocalHost();
			String ip = addr.getHostAddress();			
			logger.info("++ip : "+ip);
			
			int pos1 = docURL.indexOf("://");
			int pos2 = docURL.indexOf(":",pos1 + 1);
			
			cadena =  docURL.substring(0,pos1 + 3) + ip + docURL.substring(pos2);			
			logger.info("++cadena : "+cadena);		
			
		} catch (UnknownHostException e) {
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}
		return cadena;
	}	

} // end AppletController class
