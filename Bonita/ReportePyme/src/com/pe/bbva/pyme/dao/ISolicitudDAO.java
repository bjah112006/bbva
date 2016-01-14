package com.pe.bbva.pyme.dao;

import java.util.List;

import com.pe.bbva.pyme.model.Config;
import com.pe.bbva.pyme.model.Solicitud;

public interface ISolicitudDAO {
	
	public List<Solicitud> listarSolicitudes(List<Config> params) throws Exception;
	
	public List<Solicitud> listarSolicitudes() throws Exception;
    
}
