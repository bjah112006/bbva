// @copyright(disclaimer)
// IBM CM e-client
// (c ) Copyright IBM Corp. 2000. All Rights Reserved.
//
// US Government Users Restricted Rights
// Use, duplication or disclosure restricted by GSA ADP Schedule
// Contract with IBM Corp.  Licensed Materials - Property of IBM
//
// DISCLAIMER OF WARRANTIES :
//
// Permission is granted to copy and modify this code provided that both
// the copyright notice and this permission notice appear in all copies
// and modified versions.

/*********************************************************
	Browser Detector
**********************************************************/

	if(navigator.appName == "Microsoft Internet Explorer" && parseInt(navigator.appVersion) >= 4) this.browser = "ie4"
	else if(navigator.appName == "Netscape" && parseInt(navigator.appVersion) >= 4 && parseInt(navigator.appVersion) < 5) this.browser = "nav4"
	else if(navigator.appName == "Netscape" && parseInt(navigator.appVersion) >= 5) this.browser = "nav6"



	var appletWin = null;
	var appletAlive = false;

	function openDoc(docid, inAppletViewer)
	{
	    if ( inAppletViewer == "true" )
	    {
	   		if (isAppletAlive())
	   		{  
 	       		appletWin.focus();
    			appletWin.document.applets[0].loadDocument(docid);
   			}
   			else
   			{
      			openAppletViewer(docid, 'resizable=1,scrollbars=1');       
   			}
   		}
   		else
   		{
   		    var viewURL = "DocWriter?docid=" + docid;
			window.open(viewURL, "viewWin", "resizable=1,scrollbars=1");	
   		}
	}

	function openAppletViewer( pidString, windowFeatures )
	{
		var appletWinURL = 'AppletViewer.jsp?document=' + pidString;

		appletWin = window.open(appletWinURL, "appletWin", windowFeatures);      	
		appletAlive = "true";
		setCookie("appletAlive", "true");
	}

	function isAppletAlive()
	{
		appletAlive = getCookie("appletAlive");
   
   		if (appletAlive == "true")
   		{
   			appletWin = window.open("", "appletWin");
   			return true;
   		}
   		else
   		{	
   			appletWin = null;
   			return false;
   		}   
	}

	function appletWindowClosed()
	{
		appletAlive = "false";
		setCookie("appletAlive", "false");	
   		appletWin = null;   
	}	

	function begin()
	{   
		appletAlive = getCookie("appletAlive");
   
   		if (appletAlive == "true")
   		{
   			appletWin = window.open("", "appletWin");
   		}
   		else
   		{	
   			appletWin = null;
   		}   
	}	

	function setCookie(name, value) 
	{
		var argv = setCookie.arguments;
		var argc = setCookie.arguments.length;
		var expires = (argc > 2) ? argv[2] : null;
		var path = (argc > 3) ? argv[3] : null;
		var domain = (argc > 4) ? argv[4] : null;
		var secure = (argc > 5) ? argv[5] : false;
		document.cookie = name + "=" + escape (value) +
			((expires == null) ? "" : ("; expires=" +expires.toGMTString())) +
			((path == null) ? "" : ("; path=" + path)) +
			((domain == null) ? "" : ("; domain=" + domain)) +
			((secure == true) ? "; secure" : "");
	}

	function getCookie(name) 
	{

		var cookies = document.cookie;

		var search = name + "=";
		var setStr = null;
		var offset = 0;
		var end = 0;
		if (cookies.length > 0) 
		{
			offset = document.cookie.indexOf(search);
			if (offset != -1) 
			{
				offset += search.length;
				end = cookies.indexOf(";", offset)
				if (end == -1) 
				{
					end = cookies.length;
				}
				setStr = unescape(cookies.substring(offset, end));
			}
		}
		return(setStr);
	}

	function openDocLoad(docid)
	{	    
    	document.applets[0].loadDocument(docid);   		
   		
	}
