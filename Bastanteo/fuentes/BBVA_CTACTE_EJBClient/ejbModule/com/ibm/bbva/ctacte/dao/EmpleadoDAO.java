package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Empleado;

public interface EmpleadoDAO extends IGenericDAO<Empleado, Integer> {

	public List<Empleado> getEmpleados (String tipoEmpleado);
	public List<Empleado> getAbogadosBastanteo(String tipoEmpleado);
	public Empleado findByCodigo(String codigo);
	public List<Empleado> findByEstudio (Integer idEstudioAbog);
	public List<Empleado> getEmpleadosPorPerfil (int idPerfil);
	public List<Empleado> getPosiblesOwners (String codUsuario);
	public List<Empleado> getEmpleadosCarterizacion (int idProducto, int idTerritorio, int idPerfil);
	public List<Empleado> getPosiblesNombres (String nombreCompleto);
	public List<Empleado> getEmpleadosPorPerfilOficina (int idPerfil, int idOficina);
	public List<Empleado> getEmpleadosPorPerfiles(List<Integer> idPerfiles);
	public List<Empleado> getEmpleadosPorPerfilesYEstudio(List<Integer> idPerfiles, Integer idEstudioAbog);
	public List<Empleado> getPosiblesOwnersPorEstudio(String codUsuario, Integer idEstudioAbog);
	
	public List<Empleado> getAllNotInLDAP();
	public List<Empleado> getAllInLDAP();
	public List<Empleado> search(Empleado empleado);
}
