package com.ibm.bbva.app.ftp;

import java.io.File;

public class FTPFileWrapper {
	
    
	  private String host;
	  private String user;
	  private String password;
	  private String remoteFile;  
	  private File file;
	  private int periodo;
	  private int tasaKBytes;
	  private int reintentos;

	  public FTPFileWrapper(String host, String user, String password, String remoteFile, File file , int periodo , int tasaKBytes, int reintentos) {
	        this.host = host;
	        this.user = user;
	        this.password = password;
	        this.remoteFile = remoteFile;
	        this.file = file;
	        this.tasaKBytes = tasaKBytes;
	        this.periodo = periodo;
	        this.reintentos = reintentos;
	    }

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getRemoteFile() {
			return remoteFile;
		}

		public void setRemoteFile(String remoteFile) {
			this.remoteFile = remoteFile;
		}

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}

		public int getPeriodo() {
			return periodo;
		}

		public void setPeriodo(int periodo) {
			this.periodo = periodo;
		}

		public int getTasaKBytes() {
			return tasaKBytes;
		}

		public void setTasaKBytes(int tasaKBytes) {
			this.tasaKBytes = tasaKBytes;
		}

		public int getReintentos() {
			return reintentos;
		}

		public void setReintentos(int reintentos) {
			this.reintentos = reintentos;
		}
		
		

}
