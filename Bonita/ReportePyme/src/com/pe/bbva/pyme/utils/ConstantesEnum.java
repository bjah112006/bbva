package com.pe.bbva.pyme.utils;

public enum ConstantesEnum {   

	ESTADO_ACTIVO(				Integer.valueOf(1),"1"),
	ESTADO_INACTIVO(			Integer.valueOf(2),"0"),
	PROCESO_SIN_DIA(			Integer.valueOf(3),"00"),
	FRECUENCIA_DIARIA(			Integer.valueOf(4),"D"),
	FRECUENCIA_MENSUAL(			Integer.valueOf(5),"M"),
	FORMATO_EXTENSION(			Integer.valueOf(6),".xlsx"),
	FORMATO_FECHA_COMPLETA(		Integer.valueOf(7),"yyyy-MM-dd HH:mm:ss"),
	TRIGGER_IDENTITY(			Integer.valueOf(8),"simple"),
	NOMBRE_ARCHIVO_CONF(		Integer.valueOf(9),"configWFFastPyme.properties"),
	CATALINA_HOME(				Integer.valueOf(10),"CATALINA_HOME"),
	CARPETA_CONF(				Integer.valueOf(11),"conf"),
	PARAM_FRECUENCIA_EJECUCION(	Integer.valueOf(12),"frecuencia_ejecucion"),
	PARAM_DIA_EJECUCION(		Integer.valueOf(13),"dia_ejecucion"),
	PARAM_HORA_EJECUCION(		Integer.valueOf(14),"hora_ejecucion"),
	PARAM_RUTA_SALIDA(			Integer.valueOf(15),"ruta_salida"),
	PARAM_USER_NAME_BONITA(		Integer.valueOf(16),"user_name_bonita"),
	PARAM_PASSWORD_BONITA(		Integer.valueOf(17),"password_bonita"),
	PARAM_ID_PROCESO(			Integer.valueOf(18),"id_proceso"),
	PARAM_BONITA_HOME(			Integer.valueOf(19),"bonita_home"),
	PARAM_NOMBRE_SALIDA(		Integer.valueOf(20),"nombre_salida"),
	FORMATO_DIA(				Integer.valueOf(21),"dd"),
	FORMATO_HORA(				Integer.valueOf(22),"HHmm"),
	FORMATO_FECHA_CADENA(		Integer.valueOf(23),"yyyyMMddHHmmss"),	
	NOMBRE_HOJA_EXCEL(			Integer.valueOf(24),"Reporte WF Fast Pyme"),
	ENCABEZADO_COLUM_01(		Integer.valueOf(25),"N\u00famero Solicitud"),
	ENCABEZADO_COLUM_02(		Integer.valueOf(26),"Tipo DOI"),
	ENCABEZADO_COLUM_03(		Integer.valueOf(27),"N\u00famero DOI"),
	ENCABEZADO_COLUM_04(		Integer.valueOf(29),"Nombre Cliente"),
	ENCABEZADO_COLUM_05(		Integer.valueOf(30),"Nombre tarea"),
	ENCABEZADO_COLUM_06(		Integer.valueOf(31),"Estado"),
	ENCABEZADO_COLUM_07(		Integer.valueOf(32),"Tipo Oferta"),
	ENCABEZADO_COLUM_08(		Integer.valueOf(33),"Oficina"),
	ENCABEZADO_COLUM_09(		Integer.valueOf(34),"Fecha Llegada"),
	ENCABEZADO_COLUM_10(		Integer.valueOf(35),"Fecha Env\u00edo"),
	ENCABEZADO_COLUM_11(		Integer.valueOf(36),"Rol"),
	ENCABEZADO_COLUM_12(		Integer.valueOf(37),"Usuario"),
	ENCABEZADO_COLUM_13(		Integer.valueOf(38),"RVGL"),
	ENCABEZADO_COLUM_14(		Integer.valueOf(39),"N\u00famero Contrato"),
	ENCABEZADO_COLUM_15(		Integer.valueOf(40),"N\u00famero Garant\u00eda"),
	ENCABEZADO_COLUM_16(		Integer.valueOf(41),"Dictamen"),
	ENCABEZADO_COLUM_17(		Integer.valueOf(41),"Producto"),
	ENCABEZADO_COLUM_18(		Integer.valueOf(41),"Campaña"),
	ENCABEZADO_COLUM_19(		Integer.valueOf(41),"Clasf. Cliente"),
	ENCABEZADO_COLUM_20(		Integer.valueOf(41),"ABN Registrante"),
	ENCABEZADO_COLUM_21(		Integer.valueOf(41),"Num. Preimpreso"),
	ENCABEZADO_COLUM_22(		Integer.valueOf(41),"Causal Obs. GMC"),
	ENCABEZADO_COLUM_23(		Integer.valueOf(41),"Causal Cancelacion"),
	ENCABEZADO_COLUM_24(		Integer.valueOf(41),"Moneda"),
	ENCABEZADO_COLUM_25(		Integer.valueOf(41),"Monto"),
	ENCABEZADO_COLUM_26(		Integer.valueOf(41),"Plazo"),
	ENCABEZADO_COLUM_27(		Integer.valueOf(41),"Tasa"),
	EST_SOL_INICIALIZANDO(		Integer.valueOf(42),"initializing"),
	EST_SOL_EN_EJECUCION(		Integer.valueOf(43),"executing"),
	EST_SOL_TERMINADO(			Integer.valueOf(44),"completed"),
	EST_SOL_POR_INICIAR(		Integer.valueOf(45),"ready"),
	EST_SOL_INICIALIZANDO_TR(	Integer.valueOf(46),"INICIALIZANDO"),
	EST_SOL_EN_EJECUCION_TR(	Integer.valueOf(47),"EN EJECUCION"),
	EST_SOL_TERMINADO_TR(		Integer.valueOf(48),"TERMINADO"),
	EST_SOL_POR_INICIAR_TR(		Integer.valueOf(49),"POR INICIAR"),
	USUARIO_SISTEMA_NOMBRE(		Integer.valueOf(50),"sistema"),
	USUARIO_SISTEMA_ROL(		Integer.valueOf(51),""),
	CADENA_VACIA(				Integer.valueOf(52),""),
	FILTRO_ESTADO_COMPLETADO(	Integer.valueOf(53),"completed"),
	FILTRO_TIPO_NODO_GATE(		Integer.valueOf(55),"gate"),
	PROP_BONITA_HOME(			Integer.valueOf(56),"bonita.home"),
	PARAM_SERVER_NAME(			Integer.valueOf(57),"server_url"),
	PARAM_APPLICATION_NAME(		Integer.valueOf(58),"application_name"),
	PROP_SERVER_NAME(			Integer.valueOf(59),"server.url"),
	PROP_APPLICATION_NAME(		Integer.valueOf(60),"application.name"),
	PROCESS_NAME(				Integer.valueOf(61),"FAST NEGOCIOS"),
	USER_SYSTEM(				Integer.valueOf(62),"sistema");
	
	private Integer codigo;   
    private String nombre;
	
	/**
	 * Constructor de la Clase
	 * @param codigo , Codigo de la Constante
	 * @param nombre , Nombre de la Constante
	 */
	private ConstantesEnum(	Integer codigo,String nombre){
		
		this.codigo=codigo;
		this.nombre=nombre;
		
	}

	/**
	 * Metodo que retorna el Codigo de la Constante
	 * @return Codigo de la Constante
	 */
	public Integer getCodigo() {
		return codigo;
	}

	/**
	 * Metodo que asigna el Codigo de la Constante
	 * @param codigo , Codigo de la Constante
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	/**
	 * Metodo que retorna el Nombre de la Constante
	 * @return Nombre de la Constante
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Metodo que asigna el Nombre de la Constante
	 * @param nombre , Nombre de la Constante
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}