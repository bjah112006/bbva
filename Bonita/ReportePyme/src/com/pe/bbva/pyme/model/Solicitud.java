package com.pe.bbva.pyme.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Solicitud {
    
    private Map<String, String> props;
    private static final String ESTADO = "estado";
    private static final String FECHA_LLEGADA = "fechaLlegada";
    private static final String NOMBRE_TAREA = "nombreTarea";
    private static final String FECHA_ENVIO = "fechaEnvio";
    private static final String USUARIO_EJECUTOR_TAREA = "usuarioEjecutorTarea";
    private static final String ROL_EJECUTOR_TAREA = "rolEjecutorTarea";
    private static final String NRO_SOLICITUD = "nroSolicitud";
    
    public Solicitud() {
        super();
        props = new LinkedHashMap<String, String>();
    }

    public int size() {
        return props.size();
    }

    public boolean isEmpty() {
        return props.isEmpty();
    }

    public boolean containsKey(Object key) {
        return props.containsKey(key);
    }

    public String get(Object key) {
        return props.get(key);
    }

    public String put(String key, String value) {
        return props.put(key, value);
    }

    public void clear() {
        props.clear();
    }

    public String getEstado() {
        return props.get(ESTADO);
    }

    public void setEstado(String estado) {
        props.put(ESTADO, estado);
    }
    
    public void setFechaLlegada(String fechaLlegada) {
        props.put(FECHA_LLEGADA, fechaLlegada);
    }

    public void setNombreTarea(String nombreTarea) {
        props.put(NOMBRE_TAREA, nombreTarea);
    }

    public void setFechaEnvio(String fechaEnvio) {
        props.put(FECHA_ENVIO, fechaEnvio);
    }

    public void setUsuarioEjecutorTarea(String usuarioEjecutorTarea) {
        props.put(USUARIO_EJECUTOR_TAREA, usuarioEjecutorTarea);
    }

    public void setRolEjecutorTarea(String rolEjecutorTarea) {
        props.put(ROL_EJECUTOR_TAREA, rolEjecutorTarea);
    }

    public void setNroSolicitud(String nroSolicitud) {
        props.put(NRO_SOLICITUD, nroSolicitud);
    }
}
