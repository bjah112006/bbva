package com.bbva.bonita.util;

public class Constante {

    public static final String CODIGO_FILTRO_NRO_SOLICITUD = "01";
    public static final String CODIGO_FILTRO_DOI_CLIENTE = "02";
    public static final String CODIGO_FILTRO_NRO_RVGL = "03";
    public static final String CODIGO_FILTRO_SOL_PREIMPRESO = "04";
    public static final String PARAMETRO_VARIABLE = "casemoredetails";
    public static final String ID_TABLA_ESTACION = "16";
    
    public class EstadoSolicitud {
    	//TODO: CPM
    	public static final String DESEMBOLSADO = "DESEMBOLSADO";
    	public static final String EN_ESPERA_TRAMITE = "EN ESPERA TRAMITE";
    	public static final String APROBADO_CON_MODIFICACION = "APROBADO CON MODIFICACION";
    	public static final String APROBADO_SIN_MODIFICACION = "APROBADO SIN MODIFICACION";
    	
    	//TODO: RIESGOS
    	public static final String ENVIADO_A_EVALUACION = "ENVIADO A EVALUACION";
    	public static final String ENVIADO_ASIGNACION_EVALUADOR = "ENVIADO ASIGNACION EVALUADOR";
    	public static final String ENVIADO_A_VISITA_CAMPO = "ENVIADO A VISITA CAMPO";
    	public static final String TRANSFERIDO = "TRANSFERIDO";
    	public static final String VISITA_DE_CAMPO_REALIZADO = "VISITA DE CAMPO REALIZADO";
    	public static final String AUTORIZACION_ESCALADA = "AUTORIZACION ESCALADA";
    	
    	//TODO: MESA CONTROL
    	public static final String ENVIADO_A_MESA_CONTROL = "ENVIADO A MESA CONTROL";
    	
    	//TODO: OFICINA/FUVEX
    	public static final String DEVUELTO_POR_MESA_CONTROL = "DEVUELTO POR MESA CONTROL";
    	public static final String APROBADO_CON_MODIFICACION_POR_CONFIRMAR = "APROBADO CON MODIFICACION POR CONFIRMAR";
    	public static final String REQUISITO_OBSERVADO = "REQUISITO OBSERVADO";
    }
}
