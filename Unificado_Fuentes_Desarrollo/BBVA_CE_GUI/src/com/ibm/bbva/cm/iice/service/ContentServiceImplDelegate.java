/**
 * ContentServiceImplDelegate.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ibm.bbva.cm.iice.service;

public interface ContentServiceImplDelegate extends java.rmi.Remote {
    public java.lang.String delete(com.ibm.bbva.cm.iice.service.Documento documento) throws java.rmi.RemoteException;
    public java.lang.String deleteAll(com.ibm.bbva.cm.iice.service.Documento[] documentos) throws java.rmi.RemoteException;
    public com.ibm.bbva.cm.iice.service.Documento find(com.ibm.bbva.cm.iice.service.Documento documento) throws java.rmi.RemoteException;
    public com.ibm.bbva.cm.iice.service.Documento[] findAll(com.ibm.bbva.cm.iice.service.Documento documento) throws java.rmi.RemoteException;
    public com.ibm.bbva.cm.iice.service.Documento findAsImage(com.ibm.bbva.cm.iice.service.Documento documento, java.lang.String mimeType) throws java.rmi.RemoteException;
    public java.lang.String insert(com.ibm.bbva.cm.iice.service.Documento documento) throws java.rmi.RemoteException;
    public java.lang.String insert_PID(com.ibm.bbva.cm.iice.service.Documento documento) throws java.rmi.RemoteException;
    public java.lang.String insertAll(com.ibm.bbva.cm.iice.service.Documento[] documentos) throws java.rmi.RemoteException;
    public java.lang.String update(com.ibm.bbva.cm.iice.service.Documento documento) throws java.rmi.RemoteException;
    public java.lang.String actualizarTipoDoc(java.lang.Integer id, java.lang.String tipoDoc) throws java.rmi.RemoteException;
}
