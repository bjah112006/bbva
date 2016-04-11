package com.ibm.bbva.ctacte.dao;

import java.util.Date;
import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.dto.ExpedienteDTO;
import com.ibm.bbva.ctacte.vo.ReporteVO;

public interface ExpedienteDAO extends IGenericDAO<Expediente, Integer> {
	
	public List<Expediente> findByCodigoCentral (String codigoCentral);
	public List<Expediente> findByCodigoCentralSinEstado (String codigoCentral, List<Integer> estados);
	public List<Expediente> findByCodigoCentralConEstado (String codigoCentral, Integer idEstado);
	public List<Expediente> findByCodigoCentralConCodOperacion (String codigoCentral, String codOperacion);
	public List<Expediente> findByCodigoCentralSinIdExpediente (String codigoCentral, Integer idExpediente);
	public Expediente getExpediente (String codigoCentral, int idEstado, String codOperacion);
	public List<Expediente> findByEstado (String codUsuario, int idestado);
	public List<Expediente> findByFiltrofromExpediente(String nroExp,
			String estadoExp, String codCentral, String idOperacion,
			String nroDoi, String razonSocial, String codGestor,
			String idTerritorio, String idOficina, String codResponsable,
			Date fechaInicioLimInf, Date fechaInicioLimSup);
	
	
	public List<ExpedienteDTO> getAllDataByFilterBD(
			ReporteVO vo, String inQuery);
	public Expediente findByIdExpUltBastanteo(Integer id);
	public List<ExpedienteDTO> getReporteAns(ReporteVO reporteAnsVO);

}
