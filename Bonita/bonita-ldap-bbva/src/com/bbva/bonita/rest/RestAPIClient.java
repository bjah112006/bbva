package com.bbva.bonita.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class RestAPIClient {

	private static final Logger logger = Logger.getLogger("RestAPIClient");
	private HttpClient httpClient;
	private HttpContext httpContext;
	private String bonitaURI;

	public RestAPIClient(String bonitaURI) {
		this.httpClient = new DefaultHttpClient();
		this.bonitaURI = bonitaURI;
	}

	private int ensureStatusOk(HttpResponse response) {
		if (response.getStatusLine().getStatusCode() != 201
				&& response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatusLine().getStatusCode() + " : "
					+ response.getStatusLine().getReasonPhrase());
		}
		return response.getStatusLine().getStatusCode();
	}
	
	public String consumeResponseIfNecessary(HttpResponse response) {
		if (response.getEntity() != null) {
			BufferedReader rd;
			try {
				rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				return result.toString();
			} catch (Exception e) {
				throw new RuntimeException("Failed to consume response.", e);
			}
		} else {
			return "";
		}
	}

	public int consumeResponse(HttpResponse response, boolean printResponse) {
		String responseAsString = consumeResponseIfNecessary(response);
		if (printResponse) {
			logger.info(responseAsString);
		}

		return ensureStatusOk(response);
	}

	// URL-encoded entity as input
	public int executePostRequest(String apiURI, UrlEncodedFormEntity entity) {
		try {
			logger.info(bonitaURI + apiURI);
			HttpPost postRequest = new HttpPost(bonitaURI + apiURI);
			postRequest.setEntity(entity);
			HttpResponse response = httpClient.execute(postRequest, httpContext);
			return consumeResponse(response, true);
		} catch (HttpHostConnectException e) {
			throw new RuntimeException("Bonita bundle may not have been started, or the URL is invalid. Please verify hostname and port number. URL used is: "+ bonitaURI + apiURI, e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "executePostRequest", e);
			throw new RuntimeException(e);
		}
	}
	
	public HttpResponse executeGetRequest(String apiURI) {
		try {
			logger.info(bonitaURI + apiURI);
			HttpGet getRequest = new HttpGet(bonitaURI + apiURI);
			HttpResponse response = httpClient.execute(getRequest, httpContext);
			return response;
		} catch (HttpHostConnectException e) {
			throw new RuntimeException("Bonita bundle may not have been started, or the URL is invalid. Please verify hostname and port number. URL used is: "+ bonitaURI + apiURI, e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "executeGetRequest", e);
			throw new RuntimeException(e);
		}
	}
	
	// string as input
	public HttpResponse executePostRequest(String apiURI, String payloadAsString) {
		try {
			logger.info(bonitaURI + apiURI);
			HttpPost postRequest = new HttpPost(bonitaURI + apiURI);
			StringEntity input = new StringEntity(payloadAsString);
			input.setContentType("application/json");
			postRequest.setEntity(input);
			HttpResponse response = httpClient.execute(postRequest, httpContext);
			return response;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "executePostRequest", e);
			throw new RuntimeException(e);
		}
	}

	public HttpResponse executePutRequest(String apiURI, String payloadAsString) {
		try {
			logger.info(bonitaURI + apiURI);
			HttpPut putRequest = new HttpPut(bonitaURI + apiURI);
			StringEntity input = new StringEntity(payloadAsString);
			input.setContentType("application/json");
			putRequest.setEntity(input);
			HttpResponse response = httpClient.execute(putRequest, httpContext);
			return response;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "executePostRequest", e);
			throw new RuntimeException(e);
		}
	}
	
	public HttpResponse executePutRequest(String apiURI) {
		try {
			logger.info(bonitaURI + apiURI);
			HttpPut putRequest = new HttpPut(bonitaURI + apiURI);
			HttpResponse response = httpClient.execute(putRequest, httpContext);
			return response;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "executePostRequest", e);
			throw new RuntimeException(e);
		}
	}
	
	public void loginAs(String username, String password) {
		try {
			CookieStore cookieStore = new BasicCookieStore();
			httpContext = new BasicHttpContext();
			httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

			String loginURL = "loginservice";

			// If you misspell a parameter you will get a HTTP 500 error
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("username", username));
			urlParameters.add(new BasicNameValuePair("password", password));
			urlParameters.add(new BasicNameValuePair("redirect", "false"));

			// UTF-8 is mandatory otherwise you get a NPE
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(urlParameters, "utf-8");
			executePostRequest(loginURL, entity);
		} catch (Exception e) {
			System.out.println("loginAs");
			logger.log(Level.SEVERE, "loginAs", e);
			throw new RuntimeException(e);
		}
	}
	
	public void shutdown() {
		httpClient.getConnectionManager().shutdown();
	}
	
}
