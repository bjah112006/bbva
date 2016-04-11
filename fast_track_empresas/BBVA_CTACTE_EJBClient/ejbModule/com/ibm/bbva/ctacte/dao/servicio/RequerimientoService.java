package com.ibm.bbva.ctacte.dao.servicio;

import java.sql.SQLException;
import java.util.List;

import com.ibm.bbva.ctacte.bean.Attachment;
import com.ibm.bbva.ctacte.model.RequerimientoModel;

public interface RequerimientoService {
	
	public List<RequerimientoModel> listaRequerimiento(RequerimientoModel filtro)throws SQLException;
	public RequerimientoModel Registrar(RequerimientoModel requerimiento, Attachment attachment);
	public RequerimientoModel Actualizar(RequerimientoModel requerimiento, Attachment attachment);
	public RequerimientoModel obtener(RequerimientoModel obj)throws SQLException;
	
}
