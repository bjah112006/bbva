package com.ibm.bbva.cm.iice.service;

public class ContentServiceImplDelegateProxy implements com.ibm.bbva.cm.iice.service.ContentServiceImplDelegate {
  private String _endpoint = null;
  private com.ibm.bbva.cm.iice.service.ContentServiceImplDelegate contentServiceImplDelegate = null;
  
  public ContentServiceImplDelegateProxy() {
    _initContentServiceImplDelegateProxy();
  }
  
  public ContentServiceImplDelegateProxy(String endpoint) {
    _endpoint = endpoint;
    _initContentServiceImplDelegateProxy();
  }
  
  private void _initContentServiceImplDelegateProxy() {
    try {
      contentServiceImplDelegate = (new com.ibm.bbva.cm.iice.service.ContentServiceImplServiceLocator()).getContentServiceImplPort();
      if (contentServiceImplDelegate != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)contentServiceImplDelegate)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)contentServiceImplDelegate)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (contentServiceImplDelegate != null)
      ((javax.xml.rpc.Stub)contentServiceImplDelegate)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.ibm.bbva.cm.iice.service.ContentServiceImplDelegate getContentServiceImplDelegate() {
    if (contentServiceImplDelegate == null)
      _initContentServiceImplDelegateProxy();
    return contentServiceImplDelegate;
  }
  
  public java.lang.String delete(com.ibm.bbva.cm.iice.service.Documento documento) throws java.rmi.RemoteException{
    if (contentServiceImplDelegate == null)
      _initContentServiceImplDelegateProxy();
    return contentServiceImplDelegate.delete(documento);
  }
  
  public java.lang.String deleteAll(com.ibm.bbva.cm.iice.service.Documento[] documentos) throws java.rmi.RemoteException{
    if (contentServiceImplDelegate == null)
      _initContentServiceImplDelegateProxy();
    return contentServiceImplDelegate.deleteAll(documentos);
  }
  
  public com.ibm.bbva.cm.iice.service.Documento find(com.ibm.bbva.cm.iice.service.Documento documento) throws java.rmi.RemoteException{
    if (contentServiceImplDelegate == null)
      _initContentServiceImplDelegateProxy();
    return contentServiceImplDelegate.find(documento);
  }
  
  public com.ibm.bbva.cm.iice.service.Documento[] findAll(com.ibm.bbva.cm.iice.service.Documento documento) throws java.rmi.RemoteException{
    if (contentServiceImplDelegate == null)
      _initContentServiceImplDelegateProxy();
    return contentServiceImplDelegate.findAll(documento);
  }
  
  public com.ibm.bbva.cm.iice.service.Documento findAsImage(com.ibm.bbva.cm.iice.service.Documento documento, java.lang.String mimeType) throws java.rmi.RemoteException{
    if (contentServiceImplDelegate == null)
      _initContentServiceImplDelegateProxy();
    return contentServiceImplDelegate.findAsImage(documento, mimeType);
  }
  
  public java.lang.String insert(com.ibm.bbva.cm.iice.service.Documento documento) throws java.rmi.RemoteException{
    if (contentServiceImplDelegate == null)
      _initContentServiceImplDelegateProxy();
    return contentServiceImplDelegate.insert(documento);
  }
  
  public java.lang.String insert_PID(com.ibm.bbva.cm.iice.service.Documento documento) throws java.rmi.RemoteException{
    if (contentServiceImplDelegate == null)
      _initContentServiceImplDelegateProxy();
    return contentServiceImplDelegate.insert_PID(documento);
  }
  
  public java.lang.String insertAll(com.ibm.bbva.cm.iice.service.Documento[] documentos) throws java.rmi.RemoteException{
    if (contentServiceImplDelegate == null)
      _initContentServiceImplDelegateProxy();
    return contentServiceImplDelegate.insertAll(documentos);
  }
  
  public java.lang.String update(com.ibm.bbva.cm.iice.service.Documento documento) throws java.rmi.RemoteException{
    if (contentServiceImplDelegate == null)
      _initContentServiceImplDelegateProxy();
    return contentServiceImplDelegate.update(documento);
  }
  
  public java.lang.String actualizarTipoDoc(java.lang.Integer id, java.lang.String tipoDoc) throws java.rmi.RemoteException{
    if (contentServiceImplDelegate == null)
      _initContentServiceImplDelegateProxy();
    return contentServiceImplDelegate.actualizarTipoDoc(id, tipoDoc);
  }
  
  
}