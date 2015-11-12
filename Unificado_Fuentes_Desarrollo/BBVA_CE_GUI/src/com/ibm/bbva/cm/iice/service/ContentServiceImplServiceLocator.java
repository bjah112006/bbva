/**
 * ContentServiceImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ibm.bbva.cm.iice.service;

public class ContentServiceImplServiceLocator extends org.apache.axis.client.Service implements com.ibm.bbva.cm.iice.service.ContentServiceImplService {

    public ContentServiceImplServiceLocator() {
    }


    public ContentServiceImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ContentServiceImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ContentServiceImplPort
    private java.lang.String ContentServiceImplPort_address = "http://118.180.14.26:80/BBVA_CE_CM_WAS_MIG/ContentServiceImplService";

    public java.lang.String getContentServiceImplPortAddress() {
        return ContentServiceImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ContentServiceImplPortWSDDServiceName = "ContentServiceImplPort";

    public java.lang.String getContentServiceImplPortWSDDServiceName() {
        return ContentServiceImplPortWSDDServiceName;
    }

    public void setContentServiceImplPortWSDDServiceName(java.lang.String name) {
        ContentServiceImplPortWSDDServiceName = name;
    }

    public com.ibm.bbva.cm.iice.service.ContentServiceImplDelegate getContentServiceImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ContentServiceImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getContentServiceImplPort(endpoint);
    }

    public com.ibm.bbva.cm.iice.service.ContentServiceImplDelegate getContentServiceImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.ibm.bbva.cm.iice.service.ContentServiceImplPortBindingStub _stub = new com.ibm.bbva.cm.iice.service.ContentServiceImplPortBindingStub(portAddress, this);
            _stub.setPortName(getContentServiceImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setContentServiceImplPortEndpointAddress(java.lang.String address) {
        ContentServiceImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.ibm.bbva.cm.iice.service.ContentServiceImplDelegate.class.isAssignableFrom(serviceEndpointInterface)) {
                com.ibm.bbva.cm.iice.service.ContentServiceImplPortBindingStub _stub = new com.ibm.bbva.cm.iice.service.ContentServiceImplPortBindingStub(new java.net.URL(ContentServiceImplPort_address), this);
                _stub.setPortName(getContentServiceImplPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ContentServiceImplPort".equals(inputPortName)) {
            return getContentServiceImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://impl.service.cm.bbva.ibm.com/", "ContentServiceImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://impl.service.cm.bbva.ibm.com/", "ContentServiceImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ContentServiceImplPort".equals(portName)) {
            setContentServiceImplPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
