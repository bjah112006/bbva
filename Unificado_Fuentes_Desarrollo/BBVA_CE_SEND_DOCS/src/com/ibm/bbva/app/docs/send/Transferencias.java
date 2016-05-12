package com.ibm.bbva.app.docs.send;


import com.ibm.bbva.app.docs.send.FTP;

public class Transferencias{
	
	public static void main(String[] args) {
		
		FTP ftp = new FTP(args[0], args[1], args[2], args[3],
				args[4], args[5], args[6], args[7], args[8], args[9], 
				args[10], args[11], args[12], args[13],
				args[14], args[15] ,args[16], args[17], args[18]);
		
		ftp.init();
	
	}
	
	
		
	
	
		
}
