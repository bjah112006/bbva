package com.ibm.bbva.controller;

@SuppressWarnings("serial")
public abstract class AbstractLinksMBean extends AbstractMBean {

	protected void eliminarObjetosSession () {
		removeObjectSession(Constantes.EXPEDIENTE_SESION);
		removeObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE);
		removeObjectSession(Constantes.EXPEDIENTE_PROCESO_SESION);
		removeObjectSession(Constantes.PAGINA_ACTUAL_AYUDA_MEMORIA);
		removeObjectSession(Constantes.PAGINA_ACTUAL_HISTORIAL);
		removeObjectSession(Constantes.PAGINA_ACTUAL_OBSERVACIONES);
		removeObjectSession(Constantes.PAGINA_ACTUAL_LOG);
		removeObjectSession(Constantes.AYUDA_MEMORIA_SESION);
		removeObjectSession(Constantes.TIPO_MENSAJE);
		removeObjectSession(Constantes.FLAG_COPIA_ARCHIVO_SESION);
		removeObjectSession(Constantes.ID_EXPEDIENTE_SESION);
		removeObjectSession(Constantes.ESTADO_TARJETA);
		removeObjectSession(Constantes.REPORTE_TERRITORIO);
		removeObjectSession(Constantes.EXISTEN_TRANSFERENCIAS);	
		//removeObjectSession(Constantes.LISTA_USUARIOS_ASIG);
		removeObjectSession(Constantes.LISTA_DOC_EXP_ADJ);
		removeObjectSession(Constantes.LISTA_AYUDA_PANEL_DOCUMENTOS);
		//removeObjectSession(Constantes.LISTA_USUARIOS_ROL_ASIG);
		removeObjectSession(Constantes.CLEANTRANSFERDIR);
		removeObjectSession(Constantes.REPORTE_ESTADO);
		removeObjectSession(Constantes.REPORTE_PERFIL);
		removeObjectSession(Constantes.FLAG_NACAR_SESION);
		removeObjectSession(Constantes.EXPEDIENTE_LISTA_DOCUMENTO_CM);
		removeObjectSession(Constantes.COD_PRODUCTO_SESION);
		removeObjectSession(Constantes.LIS_BANDASIGN_ASIGNA);
		removeObjectSession(Constantes.NUEVO_CLIENTE);
		removeObjectSession(Constantes.CONSIDERAR_TAREA_1);
		removeObjectSession(Constantes.MSJ_BANDASIGN_ASIGNA);
		removeObjectSession(Constantes.LISTA_HORARIO_OFICINA);
		
		removeObjectSession(Constantes.BAND_LIST_PROD);
		removeObjectSession(Constantes.BAND_LIST_TIPDOI);
		removeObjectSession(Constantes.BAND_LIST_SEG);
		removeObjectSession(Constantes.BAND_LIST_TIPOFERTA);
		removeObjectSession(Constantes.BAND_LIST_TERR);
		removeObjectSession(Constantes.BAND_LIST_EST);
		
		
		//removeObjectSession(Constantes.PARAMETROS_SESION);
	}
	
}