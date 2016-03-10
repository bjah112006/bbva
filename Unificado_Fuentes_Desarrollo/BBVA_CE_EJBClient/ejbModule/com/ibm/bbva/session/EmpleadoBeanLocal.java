package com.ibm.bbva.session;

import java.util.List;

import javax.ejb.Local;

import com.ibm.bbva.entities.Empleado;

@Local
public interface EmpleadoBeanLocal {
	
	public Empleado buscarPorCodigo(String codigo);
	
	public Empleado buscarPorId(long id);
	
	public Empleado obtenerPorId(long id);
	
	//public List<Empleado> buscarPorIdTipoCategoria(long idTipoCategoria);
	
	public List<Empleado> buscarPorIdPerfil(long idPerfil);
	
	//public Empleado calculoCarga(long idTerritorio,long idProducto, String flagActivo);
	
	public Empleado calculaPeso(long idTerritorio,long idProducto, String flagActivo, Long idTarea);
	
	public List<Empleado> buscarPorIdOficina(long idOficina);
	
	public List<Empleado> buscarPorOficinaPerfil(long idOficina, long idPerfil);

	List<Empleado> buscarGerenteActivoPorOficinaPerfil(long idOficina,
			long idPerfil);
	
	List<Empleado> buscarGerenteActivoPorOficinaPerfil(long idOficina, long idPerfil, long idEmpleado);
	
	List<Empleado> buscarGerenteInactivoPorOficinaPerfil(long idOficina,
			long idPerfil);
	
	List<Empleado> buscarGerenteInactivoPorOficinaPerfilMarca(long idOficina, long idPerfil, String marca); 
	
	List<Empleado> buscarGerenteTemporalPorOficinaPerfil(long idOficina,
			long idPerfil);
	
	List<Empleado> buscarSubGerenteTemporalPorOficinaPerfil(long idOficina,
			long idPerfil);
	
	List<Empleado> buscarSubGerenteTemporalPorOficinaYOfiTemp(long idOficina, long idPerfilTemporal);
	
	List<Empleado> buscarPorPerfilEmpleadoActivo(long idPerfil, long idOficina);

	List<Empleado> buscarPorIdTipoCategoria(long idTipoCategoria, long idPerfil);

	List<Empleado> buscarPorPerfilActivo(long idPerfil);
	
	public Empleado buscarEmpCreadorPorIdExpediente(long idExpediente);
	
	public void edit(Empleado empleado);
	
	public Empleado create(Empleado empleado);
	
	public Empleado buscarPerfilxEmpleado(long idEmpleado);
	
	
	public long countEmpleadoCartActiv(long idEmpleado);

	public List<Empleado> buscarPorPerfil_CartProd(long idPerfil,
			List<Long> listIdsProd);

	public List<Empleado> buscarPorPerfilEmpleadoActivo(long idPerfil, long idOficina,
			List<Long> listIdsProd);
	
	public void ejecutarDescargaLDAP(long idLogJob);
	
	List<Empleado> buscarDebenReasignarse();
	
	//fix2 erika abregu
	List<Empleado> buscarPorIdTipoCategoriaActivo(long idTipoCategoria, long idPerfil);
	
}
