package com.ibm.bbva.session;

import java.util.Date;
import java.util.List;

import com.ibm.bbva.entities.Expediente;

public interface ExpedienteBeanLocal {
	
	public Expediente buscarPorId(long id);

	public List<Expediente> buscarPorIdCliente(long idCliente);
	
    public Expediente create(Expediente expediente);
	
	public void edit(Expediente expediente);
	
	public List<Expediente> reporteHistPlano (long idOficina, Date fechaInicio, Date fechaFin);
	
	public int actualizarExpediente(Long idEmpleado, Long idExpediente);
	
	public List<Expediente> buscarPorIdEmpleado(long idUsuario);

	public Expediente buscarPorIdExpediente(long idExpediente);

	public int actualizarEstadoExpediente(String estado, Long idExpediente);
}
