package com.ibm.bbva.ctacte.ftp;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class FTPArchivos {

	public final static Queue<FTPFileWrapper> colaArchivos = new ConcurrentLinkedQueue<FTPFileWrapper>();

    public static void agregarArchivo(File archive, String host, String user, String remoteFile, String password, int periodo, int tasaKBytes, String carpetaFTP, int reintentos) {
        colaArchivos.add(new FTPFileWrapper(host, user, password, remoteFile, archive, periodo, tasaKBytes, carpetaFTP, reintentos));
    }
}
