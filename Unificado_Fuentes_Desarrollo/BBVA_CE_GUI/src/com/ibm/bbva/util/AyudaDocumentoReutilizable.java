package com.ibm.bbva.util;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.DocumentoReutilizableMB;
import com.ibm.bbva.controller.common.GuiaDocumentariaMB;
import com.ibm.bbva.entities.GuiaDocumentaria;
import com.ibm.bbva.tabla.util.vo.DocumentoReutilizable;

public class AyudaDocumentoReutilizable {

	public String cargaDocumentoReutilizable(GuiaDocumentariaMB guiaDoc,DocumentoReutilizableMB documentoReutilizable) {
        //LOG.info("cargaDocumentoReutilizable");
		List<DocumentoReutilizable> lista = new ArrayList<DocumentoReutilizable>();
		StringBuffer sb2 = new StringBuffer();
		String documentosReutilizables = "";		

		/* Si existen documentos previos cargados en la lista */
		List<DocumentoReutilizable> listaAnterior = (List<DocumentoReutilizable>) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get(
						Constantes.LISTA_DOC_REUTILIZABLES);

		/* Cargar nuevamente la lista segun filtros */
		if (documentoReutilizable != null) {
			lista = documentoReutilizable.obtenerDatos();			
		} else {
			documentoReutilizable = new DocumentoReutilizableMB();
			lista = new ArrayList<DocumentoReutilizable>();
		}
		
		/* Inicializar la lista para filtrar segun guia doc */
		documentoReutilizable.setListaDocumentoReutilizable(new ArrayList<DocumentoReutilizable>());

		if (lista != null && lista.size()>0) {
			for (DocumentoReutilizable listaDr : lista) {
				for (GuiaDocumentaria listaGuiaDoc : guiaDoc.getListaGuiaDoc()) {
					if (listaDr.getId() == listaGuiaDoc.getTipoDocumento().getId()) {
						if (listaAnterior != null) {
							/*
							 * Recuperar items selecionados de lista previa en
							 * lista actual
							 */
							for (DocumentoReutilizable listaAnt : listaAnterior) {
								if (listaDr.getId() == listaAnt.getId()) {
									listaDr.setSeleccion(listaAnt.isSeleccion());
									break;
								}
							}
						}
						documentoReutilizable.getListaDocumentoReutilizable().add(listaDr);
						if (lista.size() > 0  && listaDr.isSeleccion()) {
							sb2.append(listaDr.getCodigoDocumento());
							sb2.append(",");
							sb2.append(listaDr.getIdExpediente());
							sb2.append(",");
						}
						break;
					}
				}
			}
			
			if (sb2.length() > 0) {
				documentosReutilizables = sb2.substring(0, sb2.lastIndexOf(",")).toString();
			}
		}
		
		documentoReutilizable.addObjectSession(Constantes.LISTA_DOC_REUTILIZABLES,
				documentoReutilizable.getListaDocumentoReutilizable());
		
		documentoReutilizable.addObjectSession(Constantes.DOCUMENTO_REUTILIZABLE_APPLET,
				documentosReutilizables);
		
		return documentosReutilizables;
	}
}