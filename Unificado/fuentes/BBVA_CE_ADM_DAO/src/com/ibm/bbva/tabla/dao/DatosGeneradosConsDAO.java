package com.ibm.bbva.tabla.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.as.core.dto.AbstractDTO;
import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.util.log.BaseLogger;
import com.ibm.bbva.tabla.core.dao.BaseDAO;
import com.ibm.bbva.tabla.dto.DatosGeneradosConsDTO;

public class DatosGeneradosConsDAO extends BaseDAO{

	private static DatosGeneradosConsDAO instance = null;
	
	private DatosGeneradosConsDAO() {
	}
	
	public static DatosGeneradosConsDAO getInstance() {
		if ( instance==null )
			 instance = new DatosGeneradosConsDAO();
		return instance;
	}
	
	public List<DatosGeneradosConsDTO> generarDatosConsolidado(ArrayList pParameters) throws DataAccessException {		
		List<DatosGeneradosConsDTO> listDatosGeneradosConsDTO=null;
		DatosGeneradosConsDTO objDatosGeneradosConsDTO=null;
		
		CallableStatement stmt=getSQLStoredProcedureGenerico(this.getEntityName(),pParameters);
		
		try{
			stmt.execute();			
	        ResultSet results = (ResultSet) stmt.getObject(1);
	        listDatosGeneradosConsDTO=new ArrayList<DatosGeneradosConsDTO>();
	        
	        while (results.next()) {
	        	
	            objDatosGeneradosConsDTO=new DatosGeneradosConsDTO();
	            try{
	                objDatosGeneradosConsDTO.setNumeroExpediente(results.getString("NUMERO_EXPEDIENTE"));
	                objDatosGeneradosConsDTO.setCodigoSegmentoCliente(results.getString("CODIGO_SEGMENTO_CLIENTE"));
	                objDatosGeneradosConsDTO.setDescripcionSegmentoCliente(results.getString("DESCRIPCION_SEGMENTO_CLIENTE"));
	                objDatosGeneradosConsDTO.setCorrelativoEstado(results.getString("CORRELATIVO_ESTADO"));
	                objDatosGeneradosConsDTO.setNombreEstado(results.getString("NOMBRE_ESTADO"));
	                objDatosGeneradosConsDTO.setNombreProducto(results.getString("NOMBRE_PRODUCTO"));
	                objDatosGeneradosConsDTO.setCodigoUsuarioCreacion(results.getString("CODIGO_USUARIO_CREACION"));
	                objDatosGeneradosConsDTO.setNombreUsuarioCreacion(results.getString("NOMBRE_USUARIO_CREACION"));
	                objDatosGeneradosConsDTO.setFechaCreacion(results.getString("FECHA_CREACION"));
	                objDatosGeneradosConsDTO.setCorrelativoOficina(results.getString("CORRELATIVO_OFICINA"));
	                objDatosGeneradosConsDTO.setCodigoOficina(results.getString("CODIGO_OFICINA"));
	                objDatosGeneradosConsDTO.setNombreOficina(results.getString("NOMBRE_OFICINA"));
	                objDatosGeneradosConsDTO.setCodigoGarantia(results.getString("CODIGO_GARANTIA"));
	                objDatosGeneradosConsDTO.setDescripcionGarantia(results.getString("DESCRIPCION_GARANTIA"));
	                objDatosGeneradosConsDTO.setCorrelativoSubproducto(results.getString("CORRELATIVO_SUBPRODUCTO"));
	                objDatosGeneradosConsDTO.setNombreSubproducto(results.getString("NOMBRE_SUBPRODUCTO"));
	                objDatosGeneradosConsDTO.setCorrelativoCliente(results.getString("CORRELATIVO_CLIENTE"));
	                objDatosGeneradosConsDTO.setApellidoPaternoCliente(results.getString("APELLIDO_PATERNO_CLIENTE"));
	                objDatosGeneradosConsDTO.setApellidoMaternoCliente(results.getString("APELLIDO_MATERNO_CLIENTE"));
	                objDatosGeneradosConsDTO.setNombresCliente(results.getString("NOMBRES_CLIENTE"));
	                objDatosGeneradosConsDTO.setTipoDocumentoIdentidad(results.getString("TIPO_DOCUMENTO_IDENTIDAD"));
	                objDatosGeneradosConsDTO.setNumeroDocumentoIdentidad(results.getString("NUMERO_DOCUMENTO_IDENTIDAD"));
	                objDatosGeneradosConsDTO.setTipoCliente(results.getString("TIPO_CLIENTE"));
	                objDatosGeneradosConsDTO.setIngresoNetoMensual(results.getString("INGRESO_NETO_MENSUAL"));
	                objDatosGeneradosConsDTO.setEstadoCivil(results.getString("ESTADO_CIVIL"));
	                objDatosGeneradosConsDTO.setPersonaExpuestaPublica(results.getString("PERSONA_EXPUESTA_PUBLICA"));
	                objDatosGeneradosConsDTO.setPagoHabiente(results.getString("PAGO_HABIENTE"));
	                objDatosGeneradosConsDTO.setSubrogado(results.getString("SUBROGADO"));
	                objDatosGeneradosConsDTO.setTipoOferta(results.getString("TIPO_OFERTA"));
	                objDatosGeneradosConsDTO.setMonedaImporteSolicitado(results.getString("MONEDA_IMPORTE_SOLICITADO"));
	                objDatosGeneradosConsDTO.setImporteSolicitado(results.getString("IMPORTE_SOLICITADO"));
	                objDatosGeneradosConsDTO.setMonedaImporteAprobado(results.getString("MONEDA_IMPORTE_APROBADO"));
	                objDatosGeneradosConsDTO.setImporteAprobado(results.getString("IMPORTE_APROBADO"));
	                objDatosGeneradosConsDTO.setPlazoSolicitado(results.getString("PLAZO_SOLICITADO")); 
	                objDatosGeneradosConsDTO.setPlazoAprobado(results.getString("PLAZO_APROBADO"));
	                objDatosGeneradosConsDTO.setTipoResolucion(results.getString("TIPO_RESOLUCION"));
	                objDatosGeneradosConsDTO.setCodigoPreevaluador(results.getString("CODIGO_PREEVALUADOR"));
	                objDatosGeneradosConsDTO.setCodigoRvgl(results.getString("CODIGO_RVGL"));
	                objDatosGeneradosConsDTO.setLineaConsumo(results.getString("LINEA_CONSUMO"));
	                objDatosGeneradosConsDTO.setRiesgoClienteGrupal(results.getString("RIESGO_CLIENTE_GRUPAL"));
	                objDatosGeneradosConsDTO.setPorcentajeEndeudamiento(results.getString("PORCENTAJE_ENDEUDAMIENTO"));
	                objDatosGeneradosConsDTO.setCodigoContrato(results.getString("CODIGO_CONTRATO")); 
	                objDatosGeneradosConsDTO.setGrupoBuro(results.getString("GRUPO_BURO"));
	                objDatosGeneradosConsDTO.setClasificacionSbsTitular(results.getString("CLASIFICACION_SBS_TITULAR"));
	                objDatosGeneradosConsDTO.setClasificacionBancoTitular(results.getString("CLASIFICACION_BANCO_TITULAR"));
	                objDatosGeneradosConsDTO.setClasificacionSbsConyuge(results.getString("CLASIFICACION_SBS_CONYUGE"));
	                objDatosGeneradosConsDTO.setClasificacionBancoConyuge(results.getString("CLASIFICACION_BANCO_CONYUGE"));
	                objDatosGeneradosConsDTO.setScoring(results.getString("SCORING"));
	                objDatosGeneradosConsDTO.setTasaEspecial(results.getString("TASA_ESPECIAL"));
	                objDatosGeneradosConsDTO.setFlagVerifDomiciliaria(results.getString("FLAG_VERIF_DOMICILIARIA"));
	                objDatosGeneradosConsDTO.setEstadoVerifDomiciliaria(results.getString("ESTADO_VERIF_DOMICILIARIA"));
	                objDatosGeneradosConsDTO.setFlagVerifLaboral(results.getString("FLAG_VERIF_LABORAL"));
	                objDatosGeneradosConsDTO.setEstadoVerifLaboral(results.getString("ESTADO_VERIF_LABORAL"));
	                objDatosGeneradosConsDTO.setFlagDps(results.getString("FLAG_DPS"));
	                objDatosGeneradosConsDTO.setEstadoDps(results.getString("ESTADO_DPS"));
	                objDatosGeneradosConsDTO.setModificarTasa(results.getString("MODIFICAR_TASA"));
	                objDatosGeneradosConsDTO.setModificarScoring(results.getString("MODIFICAR_SCORING")); 
	                objDatosGeneradosConsDTO.setIndicadorDelegacion(results.getString("INDICADOR_DELEGACION"));
	                objDatosGeneradosConsDTO.setIndicadorExclusionDelegacion(results.getString("INDICADOR_EXCLUSION_DELEGACION"));
	                objDatosGeneradosConsDTO.setNivelComplejidad(results.getString("NIVEL_COMPLEJIDAD"));
	                objDatosGeneradosConsDTO.setNroDevoluciones(results.getString("NRO_DEVOLUCIONES"));
	                objDatosGeneradosConsDTO.setCodigoUsuarioActual(results.getString("CODIGO_USUARIO_ACTUAL"));
	            } catch(Throwable t){
	            	
	            }
	            listDatosGeneradosConsDTO.add(objDatosGeneradosConsDTO);

	        }
	        
	        results.close();
	        stmt.close();	
	        
		}catch (SQLException e) {
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error SQLException obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
		} catch (Exception e){
			BaseLogger.log(this.getClass(), "executeSQLStoredProcedure", BaseLogger.Level.ERROR, "Error Exception obteniendo la conexion para executeSQLStoredProcedure(query): " + e.getMessage());
		}

		return listDatosGeneradosConsDTO;
	}

	@Override
	protected <T extends AbstractDTO> List<T> processResult(ResultSet rs)
			throws DataAccessException {
		// TODO Apéndice de método generado automáticamente
		return null;
	}

	@Override
	protected String processResultReturnString(ResultSet rs)
			throws DataAccessException {
		// TODO Apéndice de método generado automáticamente
		return null;
	}

	@Override
    protected String getEntityName() {
        return "CONELE.PG_CE_REPORTE_TC.SP_SEL_REPORTETC";
    }
	
}
