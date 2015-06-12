package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.DatosCorreoDestin;


public interface DatosCorreoDestinBeanLocal {
	
	public List<DatosCorreoDestin> resultadoBusqueda(long idTarea, long idProducto);
	public DatosCorreoDestin buscarPorId(long id);
	public DatosCorreoDestin create(DatosCorreoDestin datosCorreoDestin);
	public void delete(DatosCorreoDestin datosCorreoDestin);
	public void update(DatosCorreoDestin datosCorreoDestin);
	public List<DatosCorreoDestin> buscarPorIdDatosCorreo(long idCabecera);
	public List<DatosCorreoDestin> buscarTodos();
	public DatosCorreoDestin buscarPorIdPerfilAndCabecera(String codigo,long idCabecera);
	
}
