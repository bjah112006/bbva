package com.ibm.bbva.session;

import java.util.List;

import javax.ejb.Local;
import com.ibm.bbva.entities.DatosCorreo;
import com.ibm.bbva.entities.TareaPerfil;

@Local
public interface DatosCorreoBeanLocal {
	public DatosCorreo buscarPorId(long id);
	//public DatosCorreo buscarPorTareaAccion(long idTarea, String Accion, long idProducto); // obtiene los datos datos del correo por tarea
	public DatosCorreo buscarPorTareaAccion(long idTarea,long idAccion, long idProducto); // obtiene los datos datos del correo por tarea
	public void delete(DatosCorreo entity);
	public void update(DatosCorreo datosCorreo);
	public DatosCorreo create(DatosCorreo datosCorreo);
	public List<DatosCorreo> buscarTodos();
	public List<DatosCorreo> buscarPorProducto(long idProducto);
	public List<DatosCorreo> resultadoBusqueda (DatosCorreo datosCorreo, TareaPerfil tareaPerfil);
	public Long buscarExistentes(long idTarea, long idAccion, long idProducto);
	
}
