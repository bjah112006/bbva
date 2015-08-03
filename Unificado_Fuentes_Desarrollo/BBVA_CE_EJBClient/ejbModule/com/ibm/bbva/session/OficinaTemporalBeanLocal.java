package com.ibm.bbva.session;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.ibm.bbva.entities.OficinaTemporal;

@Local
public interface OficinaTemporalBeanLocal 
{
	public void edit(OficinaTemporal oficinaTemporal);
	
	public OficinaTemporal create(OficinaTemporal oficinaTemporal);
	
	public List<OficinaTemporal> buscar(long idEmpleado,
										Date fechaInicio,
										Date fechaFin,
										String horaInicio,
										String horaFin,
										String oficina,
										String estado);
	
	public List<OficinaTemporal> buscarTraslape(long idOficinaTemporal,
												long idEmpleado,
												Date fechaInicio,
												Date fechaFin,
												String horaInicio,
												String horaFin,												
												String estado);
	
	public OficinaTemporal buscarPorId(long id);
	
	public List<OficinaTemporal> obtenerActual(long idEmpleado);
	
}
