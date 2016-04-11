package com.ibm.bbva.messages;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;

public final class Mensajes {

    private static final String BASE_NAME = "com.ibm.bbva.messages.Messages";
    private static ResourceBundle resourceBundle;
    
    static {
    	resourceBundle = ResourceBundle.getBundle(BASE_NAME);
    }

    /**
     * @param id Identificador del mensaje en el archivo de propiedades 
     * configurado en el faces-config
     * @param params Valores que se reemplazaran en el mensaje
     * @return Mensaje
     */
    public static String getMensaje (String id, Object... params) {
        String msg = resourceBundle.getString(id);
        return (params==null ? msg : MessageFormat.format(msg, params));
    }

    /**
     * Obtiene un mensaje de error
     * @param id Identificador del mensaje en el archivo de propiedades 
     * configurado en el faces-config
     * @param params Valores que se reemplazaran en el mensaje
     * @return Mensaje
     */
    public static FacesMessage getFacesMessageError (String id, Object... params) {
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                getMensaje(id, params));
    }

    /**
     * Obtiene un mensaje fatal
     * @param id Identificador del mensaje en el archivo de propiedades 
     * configurado en el faces-config
     * @param params Valores que se reemplazaran en el mensaje
     * @return Mensaje
     */
    public static FacesMessage getFacesMessageFatal (String id, Object... params) {
        return new FacesMessage(FacesMessage.SEVERITY_FATAL, "",
                getMensaje(id, params));
    }

    /**
     * Obtiene un mensaje de informacion
     * @param id Identificador del mensaje en el archivo de propiedades 
     * configurado en el faces-config
     * @param params Valores que se reemplazaran en el mensaje
     * @return Mensaje
     */
    public static FacesMessage getFacesMessageInfo (String id, Object... params) {
        return new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                getMensaje(id, params));
    }

    /**
     * Obtiene un mensaje de alerta 
     * @param id Identificador del mensaje en el archivo de propiedades 
     * configurado en el faces-config
     * @param params Valores que se reemplazaran en el mensaje
     * @return Mensaje
     */
    public static FacesMessage getFacesMessageWarn (String id, Object... params) {
        return new FacesMessage(FacesMessage.SEVERITY_WARN, "",
                getMensaje(id, params));
    }
}
