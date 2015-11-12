<!--
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
** FILENAME : AppletViewer.jsp
**    
** SAMPLE   : Information Integrator for Content Version 8 Applet Viewer
**            Sample
**
** ABSTRACT : This file is included in the Applet Viewer Sample EAR
**            file (AppletViewer.EAR). This is used to start the applet
**            viewer and load the first document.
**
** Date     : Sept 25, 2003
**                                                           
** Authors  : Bryan Daniel (bryand@us.ibm.com)  
**  Jerald Schoudt
**                                                                                             
*****************************************************************************/
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ page language="java" info="ViewerApplet Frame"
         import="java.lang.String,
        com.ibm.mm.beans.*,
        com.ibm.mm.viewer.*,
        com.ibm.bbva.cm.util.*,
       java.net.URLEncoder,
       java.io.*,
javax.naming.*,
com.ibm.bbva.cm.util.ParametrosConfUtil"
         
         isThreadSafe="true"    
%>


<HTML>
<HEAD>

<TITLE>AppletViewer.jsp</TITLE>
<script language="JavaScript" src="jquery-1.8.2.min.js"></script>
<script language="JavaScript" src="cmv8client.js"></script>

<script language="JavaScript">

	$( document ).ready(function() 
	{
		$(window).bind('beforeunload', function() 
		{
			if(document.applets[0].botonGrabarHabilitado() == true)
		    {
		    	//return '¿Desea salir sin guardar los cambios aplicados al documento?';
		    	return 'Si acepta, se perder' + String.fromCharCode(225) + 'n los cambios aplicados al documento';
		    }
		});
	});
	          
	function appletDead()
	{
		//document.applets[0].killApplet();
		//document.applets[0].destroy();
	}
	
	function f_OpenCM(pId)
	{
		openDocLoad(pId,true);
	}
       
</script>

<style type="text/css">
    .BodyForApplet
    {
        background  : "#ccffff";
        padding     : 0px 0px 0px 0px;
        margin      : 0px 0px 0px 0px;
    }
</style>  

</HEAD>

<%
String document = (String) request.getParameter("document");
String nombres = (String) request.getParameter("parnom");
String pids = (String) request.getParameter("parpid");
String type = (String) request.getParameter("type");

String[] arrNombres = null;
if(nombres != null){
	arrNombres = nombres.split("\\|");
}
String[] arrPids = null;

if(pids != null){
	arrPids = pids.split("\\|");
}

String descargados = ParametrosConfUtil.getInstance().getValueVA("appletCarpetaDescargados");
String transferencias = ParametrosConfUtil.getInstance().getValueVA("appletCarpetaTransferencias");
%>

<BODY class="BodyForApplet" onUnload="appletDead();" marginwidth="0" marginheight="0" >

<% 
if(arrNombres!=null && arrNombres != null){
for (int i=0; i<arrNombres.length; i++) { %>
<!--a href="#" onclick="f_OpenCM('<%= arrPids[i]%>')"><%= arrNombres[i]%></a><br-->
<% }}
%>

<applet name="appletVisorAvanzado" id="appletVisorAvanzado"
	code="AppletViewer"
	archive="AppletViewer.jar"
	scriptable="true"
	mayscript="mayscript"
	width="100%"
	height="100%">
	<PARAM  NAME="document" VALUE="<%=document%>">
	<PARAM  NAME="descargados" VALUE="<%=descargados%>">
	<PARAM  NAME="transferencias" VALUE="<%=transferencias%>">
	<PARAM  NAME="type" VALUE="<%=type%>">
	<PARAM name="separate_jvm" value="true">
</applet>

</BODY>
</HTML>