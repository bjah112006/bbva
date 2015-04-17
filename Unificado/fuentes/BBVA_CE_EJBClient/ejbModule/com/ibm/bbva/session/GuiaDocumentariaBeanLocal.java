package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.DatosCorreo;
import com.ibm.bbva.entities.GuiaDocumentaria;
import com.ibm.bbva.entities.Persona;

public interface GuiaDocumentariaBeanLocal {
	
	public List<GuiaDocumentaria> buscarGuiaDoc(GuiaDocumentaria g);
	public List<GuiaDocumentaria> obtenerGuiaDoc(GuiaDocumentaria g, boolean isConyugue, List<String> categoriaRenta);
	public List<GuiaDocumentaria> obtenerGuiaDocOrden(GuiaDocumentaria g, boolean isConyugue, List<String> categoriaRenta);
	public Persona obtenerPersonaPorCodigo(String codigo);
	public List<GuiaDocumentaria> buscarTodos();
	public List<GuiaDocumentaria> resultadoBusqueda(GuiaDocumentaria g);
	public void delete(GuiaDocumentaria entity);
	public void update(GuiaDocumentaria guidaDocumentaria);
	public GuiaDocumentaria create(GuiaDocumentaria guidaDocumentaria);
	public GuiaDocumentaria buscarPorId(long id);
}
