package com.ibm.bbva.ctacte.ftp;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class FTPInputStream extends ByteArrayInputStream {

	private FTPListener listener;
	private int periodo;
	
	public FTPInputStream(byte[] buf, int periodo, FTPListener listener) {
		super(buf);
		this.listener = listener;
	}

	@Override
    public int read(byte[] b) throws IOException {
        try {          
            Thread.sleep(periodo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        int byteRead = super.read(b);
        listener.setAvance(byteRead);
        return byteRead;
    }

}
