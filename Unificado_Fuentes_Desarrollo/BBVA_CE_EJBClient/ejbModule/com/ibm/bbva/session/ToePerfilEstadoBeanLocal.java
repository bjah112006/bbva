package com.ibm.bbva.session;

import java.util.List;

import javax.ejb.Local;
import com.ibm.bbva.entities.ToePerfilEstado;

@Local
public interface ToePerfilEstadoBeanLocal {

	List<ToePerfilEstado> validaRegistroUnico(long idProducto, long idTarea,
			long idPerfil, long idEstado, long idOferta, String idFlujo, String nomColumna);
	
	public List<ToePerfilEstado> buscarRegistroToePerfilEstado(long idEstadoExpediente,long idPerfil, 
														 long idTipoOferta,String tipoFlujo, long idProducto,long idTarea);
	
	public List<ToePerfilEstado> buscarRegistroToePerfilEstadoTarea1(long idEstadoExpediente,long idPerfil, 
			 long idTipoOferta,String tipoFlujo, long idProducto,long idTarea);
	
	
}
