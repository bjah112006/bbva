<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.net.InetAddress"%>
<%@page import="org.apache.http.HttpResponse"%>
<%@page import="org.apache.http.message.BasicNameValuePair"%>
<%@page import="org.apache.http.NameValuePair"%>
<%@page import="org.apache.http.client.protocol.ClientContext"%>
<%@page import="org.apache.http.protocol.HttpContext"%>
<%@page import="org.apache.http.protocol.BasicHttpContext"%>
<%@page import="org.apache.http.impl.client.BasicCookieStore"%>
<%@page import="org.apache.http.impl.client.DefaultHttpClient"%>
<%@page import="org.apache.http.client.CookieStore"%>
<%@page import="org.apache.http.client.entity.UrlEncodedFormEntity"%>
<%@page import="org.apache.http.client.methods.HttpPost"%>
<%@page import="org.apache.http.client.HttpClient"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Salvavida</title>
</head>
<body>
<%
String date = request.getParameter("date");
InetAddress ip = InetAddress.getLocalHost();
String url = "http://" + ip.getHostAddress() + ":8080/ReportePyme/scheduler/execute/" + request.getParameter("jobName") + ".json";
CookieStore cookieStore = new BasicCookieStore();
HttpContext httpContext = new BasicHttpContext();
httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
HttpClient httpClient = new DefaultHttpClient();
HttpPost postRequest = new HttpPost(url);
%>
Ejecut&oacute;: <b><%=url%></b><br/>
<b><%=request.getQueryString()%></b><br/>
<%
//If you misspell a parameter you will get a HTTP 500 error
List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
urlParameters.add(new BasicNameValuePair("date", date));

// UTF-8 is mandatory otherwise you get a NPE
UrlEncodedFormEntity entity = new UrlEncodedFormEntity(urlParameters, "utf-8");
postRequest.setEntity(entity);

HttpResponse httpResponse = httpClient.execute(postRequest, httpContext);
String responseAsString = "";
if (httpResponse.getEntity() != null) {
    BufferedReader rd;
    try {
        rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        responseAsString = result.toString();
    } catch (Exception e) {
        responseAsString = "Failed to consume response. [" + e.getMessage() + "]";
    }
} else {
    responseAsString = "No se obtuvo entidad de respuesta";
}
%>
Resultado: <b><%=responseAsString%></b>
</body>
</html>