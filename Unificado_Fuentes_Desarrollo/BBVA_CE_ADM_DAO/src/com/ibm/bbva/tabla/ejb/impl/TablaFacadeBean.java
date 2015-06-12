package com.ibm.bbva.tabla.ejb.impl;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.exception.ObjectNotFoundException;
import com.ibm.as.core.helpers.TipoDato;
import com.ibm.bbva.tabla.core.assembler.ColumnaAssembler;
import com.ibm.bbva.tabla.core.assembler.ColumnaDependenciaAssembler;
import com.ibm.bbva.tabla.core.assembler.DatosGeneradosAssembler;
import com.ibm.bbva.tabla.core.assembler.ParametrosConfAssembler;
import com.ibm.bbva.tabla.core.assembler.PosibleValorAssembler;
import com.ibm.bbva.tabla.core.assembler.RegistroTablaAssembler;
import com.ibm.bbva.tabla.core.assembler.TablaAssembler;
import com.ibm.bbva.tabla.core.dao.DAOFactory;
import com.ibm.bbva.tabla.dao.ColumnaDAO;
import com.ibm.bbva.tabla.dao.ColumnaDependenciaDAO;
import com.ibm.bbva.tabla.dao.DatosGeneradosConsDAO;
import com.ibm.bbva.tabla.dao.DatosGeneradosHisDAO;
import com.ibm.bbva.tabla.dao.ParametrosConfDAO;
import com.ibm.bbva.tabla.dao.PosibleValorDAO;
import com.ibm.bbva.tabla.dao.RegistroTablaDAO;
import com.ibm.bbva.tabla.dao.TablaDAO;
import com.ibm.bbva.tabla.dao.ValorForaneoDAO;
import com.ibm.bbva.tabla.dao.ValorForaneoDataDAO;
import com.ibm.bbva.tabla.dto.ColumnaDTO;
import com.ibm.bbva.tabla.dto.ColumnaDependenciaDTO;
import com.ibm.bbva.tabla.dto.DatosGeneradosConsDTO;
import com.ibm.bbva.tabla.dto.DatosGeneradosHisDTO;
import com.ibm.bbva.tabla.dto.ParametrosConfDTO;
import com.ibm.bbva.tabla.dto.PosibleValorDTO;
import com.ibm.bbva.tabla.dto.RegistroTablaDTO;
import com.ibm.bbva.tabla.dto.TablaDTO;
import com.ibm.bbva.tabla.dto.ValorForaneoDTO;
import com.ibm.bbva.tabla.dto.ValorForaneoDataDTO;
import com.ibm.bbva.tabla.helper.TablaException;
import com.ibm.bbva.tabla.reporte.dao.DatosGeneradosDAO;
import com.ibm.bbva.tabla.reporte.dao.DatosPorcentajeDAO;
import com.ibm.bbva.tabla.reporte.dao.DatosUnidadDAO;
import com.ibm.bbva.tabla.reporte.dto.DatosGeneradosDTO;
import com.ibm.bbva.tabla.reporte.dto.DatosPorcentajeDTO;
import com.ibm.bbva.tabla.reporte.dto.DatosUnidadDTO;
import com.ibm.bbva.tabla.reporte.vo.DatosGeneradosConsVO;
import com.ibm.bbva.tabla.reporte.vo.DatosGeneradosHisVO;
import com.ibm.bbva.tabla.reporte.vo.DatosGeneradosVO;
import com.ibm.bbva.tabla.reporte.vo.DatosPorcentajeVO;
import com.ibm.bbva.tabla.reporte.vo.DatosUnidadVO;
import com.ibm.bbva.tabla.service.Constantes;
import com.ibm.bbva.tabla.vo.ColumnaDependenciaVO;
import com.ibm.bbva.tabla.vo.ColumnaVO;
import com.ibm.bbva.tabla.vo.ParametrosConfVO;
import com.ibm.bbva.tabla.vo.PosibleValorVO;
import com.ibm.bbva.tabla.vo.RegistroTablaVO;
import com.ibm.bbva.tabla.vo.TablaParametricaVO;
import com.ibm.bbva.tabla.vo.TablaVO;

/**
 * Bean implementation class for Enterprise Bean: TablaFacade
 */
public class TablaFacadeBean {
	
	private static final Logger LOG = LoggerFactory.getLogger(TablaFacadeBean.class);

	private static final long serialVersionUID = -2543633865357289787L;

	private static DAOFactory daoFactory = DAOFactory.getDefaultDAOFactory();
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public Collection<TablaVO> getTablasParametrizables() throws TablaException {
		Collection<TablaDTO> tablasDTO = new ArrayList<TablaDTO>();
		Collection<TablaVO> tablasVO = new ArrayList<TablaVO>();
		try {			
			TablaDAO tablaDAO = daoFactory.getTablaDAO();			
			TablaDTO filter = new TablaDTO();			
			tablasDTO = tablaDAO.read(filter);			
			if (tablasDTO != null && tablasDTO.size() > 0) {				
				for (TablaDTO dto : tablasDTO) {					
					if (tablasVO == null) {						
						tablasVO = new ArrayList<TablaVO>();
					}					
					TablaVO vo = TablaAssembler.assembleVO(dto);					
					tablasVO.add(vo);
				}
			}
		} catch (DataAccessException e) {
			throw new TablaException(e);
		}
		return tablasVO;
	}

	public TablaParametricaVO getDatosYEstructuraTablaByIdTabla(Integer idTabla)
			throws TablaException {
		try {
			TablaParametricaVO tablaParametricaVO = null;

			TablaVO tablaVO = null;
			TablaDTO tablaDTO = null;
			Collection<ColumnaDTO> columnasDTO = null;
			Collection<ColumnaVO> columnasVO = null;
			Collection<RegistroTablaDTO> registrosDTO = null;
			Collection<RegistroTablaVO> registrosVO = null;
			Collection<RegistroTablaVO> registrosVOtmp = new ArrayList<RegistroTablaVO>();
			ColumnaDAO columnaDAO = TablaFacadeBean.daoFactory.getColumnaDAO();
			RegistroTablaDAO registroTablaDAO = TablaFacadeBean.daoFactory
					.getRegistroTablaDAO();
			TablaDAO tablaDAO = TablaFacadeBean.daoFactory.getTablaDAO();
			if (idTabla != null) {
				tablaDTO = tablaDAO.findById(idTabla);
				ColumnaDTO filter = new ColumnaDTO();
				filter.setIdTabla(idTabla);
				columnasDTO = columnaDAO.read(filter);
				registrosDTO = registroTablaDAO.getRegistrosTablaByNombreTabla(
						tablaDTO.getNombre(), columnasDTO);
				
				/**
				 * 16/12/14
				 * @devniel
				 * Ocultar la tarea 26 solo para los resultados del Mantenimiento
				 * de Tarea
				 */
				
				if(tablaDTO.getNombre() != null){
					if(tablaDTO.getNombre().equals("TBL_CE_IBM_TAREA")){
						for(RegistroTablaDTO registro : registrosDTO){
							if(registro.getDatosRegistro()  != null){
								if(registro.getDatosRegistro().get("CODIGO") != null){
									if(registro.getDatosRegistro().get("CODIGO").equals("26")){
										registrosDTO.remove(registro);
										break;
									}
								}
							}
						}
					}
				}
				
				if(tablaDTO.getNombre() != null){
					if(tablaDTO.getNombre().equals("TBL_CE_IBM_TAREA_PERFIL")){
						for(RegistroTablaDTO registro : registrosDTO){
							if(registro.getDatosRegistro()  != null){
								if(registro.getDatosRegistro().get("ID_TAREA_FK") != null){
									if(registro.getDatosRegistro().get("ID_TAREA_FK").equals(new BigDecimal(26))){
										registrosDTO.remove(registro);
										break;
									}
								}
							}
						}
					}
				}
							
				
				tablaVO = TablaAssembler.assembleVO(tablaDTO);	
				columnasVO = this.assembleColumnasVO(columnasDTO);
				registrosVO = this.assembleRegistrosTablaVO(registrosDTO);
				if (idTabla != Constantes.COD_TABLA_GUIA_DOCUMENTARIA
						&& idTabla != Constantes.COD_TABLA_OFICINA
						&& idTabla != Constantes.COD_TABLA_EMPLEADO
						&& idTabla != Constantes.COD_TABLA_CARTERIZACION_TERRITORIO
						&& idTabla != Constantes.COD_TABLA_CARTERIZACION_EMPLEADO) {
					this.llenarValorMostrarRegistros(registrosVO, columnasVO);
					this.llenarPosiblesValoresPorColumna(columnasVO);
				}else{
					int i=0;
					for(RegistroTablaVO tmpVo : registrosVO){
						if(i<10){
							registrosVOtmp.add(tmpVo);
						}else{
							break;
						}
						i++;
					}
					this.llenarValorMostrarRegistros(registrosVOtmp, columnasVO);
					this.llenarPosiblesValoresPorColumna(columnasVO);
				}
				
				tablaParametricaVO = new TablaParametricaVO(tablaVO,
						columnasVO, registrosVO);
			}

			return tablaParametricaVO;
		} catch (DataAccessException e) {
			throw new TablaException(e);
		} catch (ObjectNotFoundException e) {
			throw new TablaException(e);
		}
	}

	private void llenarPosiblesValoresPorColumna(Collection<ColumnaVO> columnas)
			throws DataAccessException, ObjectNotFoundException {
		LOG.info("llenarPosiblesValoresPorColumna");
		if (columnas != null && columnas.size() > 0) {
			PosibleValorDAO dao = TablaFacadeBean.daoFactory.getPosibleValorDAO();
			ValorForaneoDAO dao2= TablaFacadeBean.daoFactory.getValorForaneoDAO();
			ValorForaneoDataDAO dao3= TablaFacadeBean.daoFactory.getValorForaneoDataDAO();
			ColumnaDependenciaDAO dao4= TablaFacadeBean.daoFactory.getColumnaDependenciaDAO();
			
			for (ColumnaVO columna : columnas) {
				Collection<PosibleValorVO> posiblesValoresVO = null;	
				ColumnaDependenciaVO columnaDependenciaVO = null;
				//LOG.info("columna.isEsLlaveForanea():" + columna.isEsLlaveForanea());
				//LOG.info("columna.getTipoDato().toUpperCase():" + columna.getTipoDato().toUpperCase());
				if (columna != null && columna.getTipoDato() != null) {
					if ("LISTA".equals(columna.getTipoDato().toUpperCase()) && !columna.isEsLlaveForanea()) {
						PosibleValorDTO filter = new PosibleValorDTO();
						filter.setIdColumna(columna.getId());
						Collection<PosibleValorDTO> posiblesValoresDTO = dao
								.read(filter);
						posiblesValoresVO = assemblePosiblesValoresVO(posiblesValoresDTO);
						columna.setPosiblesValores(posiblesValoresVO);
					}
				if ("LISTA".equals(columna.getTipoDato().toUpperCase()) && columna.isEsLlaveForanea()) {
						//LOG.info("LISTA y LLAVE FORANEA");
						ValorForaneoDTO valorForaneoDTO= dao2.findById(columna.getId());
						Collection<ValorForaneoDataDTO> dataDTO=null;
						if (valorForaneoDTO.getNombreColumna().contains("-")){
							dataDTO=dao3.getValoresForaneosConcat(valorForaneoDTO.getNombreTabla(),
									valorForaneoDTO.getNombreColumna(),
									valorForaneoDTO.getValorColumna());
						}else{
							dataDTO=dao3.getValoresForaneos(valorForaneoDTO.getNombreTabla(),
								valorForaneoDTO.getNombreColumna(),
								valorForaneoDTO.getValorColumna());
						}
						
						ColumnaDependenciaDTO columnaDependenciaDTO = dao4.findIdColumnaHijo(columna.getId());
						columnaDependenciaVO = assembleColumnaDependenciaVO(columnaDependenciaDTO);
						
						if (columnaDependenciaVO!=null && columnaDependenciaVO.getIdColumnaHijo()!=null &&
								columnaDependenciaVO.getIdColumnaHijo().intValue() == columna.getId()) {
							posiblesValoresVO = null;
						}else{								
							Collection<PosibleValorDTO> posiblesValoresDTO = new ArrayList<PosibleValorDTO>();
							int i=0;
							for (ValorForaneoDataDTO dto:dataDTO)
							{
								i++;
								PosibleValorDTO aux=new PosibleValorDTO();
								aux.setId(i);
								aux.setIdColumna(columna.getId());
								aux.setValor(dto.getValor().toString());
								aux.setEtiqueta(dto.getNombre());
								posiblesValoresDTO.add(aux);
							}		
							
							posiblesValoresVO = assemblePosiblesValoresVO(posiblesValoresDTO);
						}
						columna.setPosiblesValores(posiblesValoresVO);
					}
				}
			}
		}
	}
	
	public Collection<PosibleValorVO> llenarPosiblesValoresPorColumnaDependencia(ColumnaDependenciaVO columna) {		
		Collection<ValorForaneoDataDTO> dataDTO=null;
		Collection<PosibleValorVO> posiblesValoresVO = null;
		TablaDAO dao1= TablaFacadeBean.daoFactory.getTablaDAO();
		ValorForaneoDataDAO dao2= TablaFacadeBean.daoFactory.getValorForaneoDataDAO();		
		TablaDTO tablaDTO;
		Collection<ColumnaDependenciaVO> columnaDependenciaVO = null;
		try {
			tablaDTO = dao1.findById(columna.getIdTablaHijo());
			dataDTO=dao2.getValoresForaneosConcat(tablaDTO.getNombre(), columna.getNombreColumna(), columna.getValorColumna());	
			Collection<PosibleValorDTO> posiblesValoresDTO = new ArrayList<PosibleValorDTO>();
			int i=0;
			for (ValorForaneoDataDTO dto:dataDTO)
			{
				i++;
				PosibleValorDTO aux=new PosibleValorDTO();
				aux.setId(i);
				aux.setIdColumna(columna.getId());
				aux.setValor(dto.getValor().toString());
				aux.setEtiqueta(dto.getNombre());
				posiblesValoresDTO.add(aux);
			}		
			
			posiblesValoresVO = assemblePosiblesValoresVO(posiblesValoresDTO);			
			
		} catch (DataAccessException e) {
			// TODO Bloque catch generado automáticamente
			LOG.error(e.getMessage(), e);
		} catch (ObjectNotFoundException e) {
			// TODO Bloque catch generado automáticamente
			LOG.error(e.getMessage(), e);
		}
		return posiblesValoresVO;
	}
	
	public void llenarValorMostrarRegistros( Collection<RegistroTablaVO> registros, Collection<ColumnaVO> columnas) throws DataAccessException, ObjectNotFoundException{
		if( registros != null && registros.size() > 0 && columnas != null && columnas.size() > 0 ){
			PosibleValorDAO dao = TablaFacadeBean.daoFactory.getPosibleValorDAO();
			ValorForaneoDAO dao2= TablaFacadeBean.daoFactory.getValorForaneoDAO();
			ValorForaneoDataDAO dao3= TablaFacadeBean.daoFactory.getValorForaneoDataDAO();
			for( RegistroTablaVO vo : registros ){
				for( ColumnaVO columnaVO : columnas ){
					if( columnaVO != null && columnaVO.getTipoDato() != null && TipoDato.LISTA.toString().equals(columnaVO.getTipoDato().toUpperCase())
							&& !columnaVO.isEsLlaveForanea()){
						PosibleValorDTO filter = new PosibleValorDTO();
						Map<String, Object> registro = vo.getDatosRegistro();
						filter.setIdColumna(columnaVO.getId());
						filter.setValor( (String)registro.get(columnaVO.getNombre()) );
						Collection<PosibleValorDTO> posiblesValores = dao.read(filter);
						if( posiblesValores != null && posiblesValores.size() > 0 ){
							PosibleValorDTO posibleValor = (posiblesValores.iterator().next());
							if( posibleValor != null ){
								String etiqueta = posibleValor.getEtiqueta();
								registro.put(columnaVO.getNombre()+"_ETIQUETA", etiqueta);
							}
						}
					}
					if( columnaVO != null && columnaVO.getTipoDato() != null && TipoDato.LISTA.toString().equals(columnaVO.getTipoDato().toUpperCase())
							&& columnaVO.isEsLlaveForanea()){
						Map<String, Object> registro = vo.getDatosRegistro();
						ValorForaneoDTO valorForaneoDTO= dao2.findById(columnaVO.getId());	
						Integer valorColumna=((BigDecimal)registro.get(columnaVO.getNombre())) == null ? null : ((BigDecimal)registro.get(columnaVO.getNombre())).intValue();
						if (valorColumna!=null && valorColumna!=Constantes.CODIGO_CODIGO_CAMPO_VACIO) {
							ValorForaneoDataDTO valorForaneo=null;
							if (valorForaneoDTO.getNombreColumna().contains("-")){
								valorForaneo=dao3.getValorByIdConcat(valorForaneoDTO.getNombreTabla(),valorForaneoDTO.getNombreColumna(),valorForaneoDTO.getValorColumna(),valorColumna);
							}else{
								valorForaneo=dao3.getValorById(valorForaneoDTO.getNombreTabla(),valorForaneoDTO.getNombreColumna(),valorForaneoDTO.getValorColumna(),valorColumna);
							}
								
							if (valorForaneo!=null){
								String etiqueta=valorForaneo.getNombre();
								registro.put(columnaVO.getNombre()+"_ETIQUETA", etiqueta);
							}
						}
						
					}
					if( columnaVO != null && columnaVO.getTipoDato() != null && TipoDato.DATE.toString().equals(columnaVO.getTipoDato().toUpperCase())){
						Map<String, Object> registro = vo.getDatosRegistro();
						Date valor =  (Date)registro.get(columnaVO.getNombre());
						if (valor == null) {
							registro.put(columnaVO.getNombre(), null);//TODO
						}else{
							registro.put(columnaVO.getNombre(), sdf.format(valor));//TODO	
						}
						
					}
					
					/**
					 * @author Daniel Flores
					 * @date 11/12/2014
					 * Para que no se muestre un checkbox marcado si el valor
					 * del boolean es NULO.
					 */
					
					if(columnaVO != null && columnaVO.getTipoDato() != null && TipoDato.BOOLEAN.toString().equals(columnaVO.getTipoDato().toUpperCase())){
						Map<String, Object> registro = vo.getDatosRegistro();
						String valor = (String) registro.get(columnaVO.getNombre());
						if(valor == null){
							registro.put(columnaVO.getNombre(), "0");
						}
					}
				}
			}
		}
	}

	public void crearRegistroTabla(RegistroTablaVO registroTablaVO)
			throws TablaException {
		try {
			String nombreTabla = registroTablaVO.getNombreTabla();
			Collection<ColumnaVO> columnasVO = registroTablaVO.getColumnas();
			Collection<ColumnaDTO> columnas = this
					.assembleColumnasDTO(columnasVO);
			RegistroTablaDTO registroTablaDTO = RegistroTablaAssembler
					.assembleDTO(registroTablaVO);
			RegistroTablaDAO dao = TablaFacadeBean.daoFactory
					.getRegistroTablaDAO();
			dao.create(nombreTabla, columnas, registroTablaDTO);
		} catch (DataAccessException e) {
			throw new TablaException(e);
		}

	}

	public void actualizarRegistroTabla(RegistroTablaVO registroTablaVO)
			throws TablaException {
		try {
			String nombreTabla = registroTablaVO.getNombreTabla();
			Collection<ColumnaVO> columnasVO = registroTablaVO.getColumnas();
			Collection<ColumnaDTO> columnas = this
					.assembleColumnasDTO(columnasVO);
			RegistroTablaDTO registroTablaDTO = RegistroTablaAssembler
					.assembleDTO(registroTablaVO);
			RegistroTablaDAO dao = TablaFacadeBean.daoFactory
					.getRegistroTablaDAO();
			dao.update(nombreTabla, columnas, registroTablaDTO);
		} catch (DataAccessException e) {
			throw new TablaException(e);
		}
	}

	public void eliminarRegistroTabla(RegistroTablaVO registroTablaVO)
			throws TablaException {
		try {
			String nombreTabla = registroTablaVO.getNombreTabla();
			RegistroTablaDTO registroTablaDTO = RegistroTablaAssembler
					.assembleDTO(registroTablaVO);
			RegistroTablaDAO dao = TablaFacadeBean.daoFactory
					.getRegistroTablaDAO();
			dao.delete(nombreTabla, registroTablaDTO);
		} catch (DataAccessException e) {
			throw new TablaException(e);
		}

	}

	private Collection<ColumnaVO> assembleColumnasVO(
			Collection<ColumnaDTO> columnasDTO) {
		Collection<ColumnaVO> columnasVO = null;
		if (columnasDTO != null && columnasDTO.size() > 0) {
			columnasVO = new ArrayList<ColumnaVO>();
			for (ColumnaDTO dto : columnasDTO) {
				ColumnaVO vo = ColumnaAssembler.assembleVO(dto);
				vo.determinarTipoCampo();
				columnasVO.add(vo);
			}
		}
		return columnasVO;
	}

	private Collection<ColumnaDTO> assembleColumnasDTO(
			Collection<ColumnaVO> columnasVO) {
		Collection<ColumnaDTO> columnasDTO = null;
		if (columnasVO != null && columnasVO.size() > 0) {
			columnasDTO = new ArrayList<ColumnaDTO>();
			for (ColumnaVO vo : columnasVO) {
				ColumnaDTO dto = ColumnaAssembler.assembleDTO(vo);
				columnasDTO.add(dto);
			}
		}
		return columnasDTO;
	}

	private Collection<RegistroTablaVO> assembleRegistrosTablaVO(
			Collection<RegistroTablaDTO> registrosTablaDTO) {
		Collection<RegistroTablaVO> registrosTablaVO = null;
		if (registrosTablaDTO != null && registrosTablaDTO.size() > 0) {
			registrosTablaVO = new ArrayList<RegistroTablaVO>();
			for (RegistroTablaDTO dto : registrosTablaDTO) {
				registrosTablaVO.add(RegistroTablaAssembler.assembleVO(dto));
			}
		}
		return registrosTablaVO;
	}

	
	private RegistroTablaVO assembleRegistrosTablaVO(
			RegistroTablaDTO registrosTablaDTO) {
		RegistroTablaVO registrosTablaVO = null;
		if (registrosTablaDTO != null) {
			registrosTablaVO = new RegistroTablaVO();
			registrosTablaVO = RegistroTablaAssembler.assembleVO(registrosTablaDTO);
		}
		return registrosTablaVO;
	}	
	
	private Collection<PosibleValorVO> assemblePosiblesValoresVO(
			Collection<PosibleValorDTO> posiblesValoresDTO) {
		Collection<PosibleValorVO> posiblesValoresVO = null;
		if (posiblesValoresDTO != null && posiblesValoresDTO.size() > 0) {
			posiblesValoresVO = new ArrayList<PosibleValorVO>();
			for (PosibleValorDTO dto : posiblesValoresDTO) {
				posiblesValoresVO.add(PosibleValorAssembler.assembleVO(dto));
			}
		}
		return posiblesValoresVO;
	}
	
	private ColumnaDependenciaVO assembleColumnaDependenciaVO(
			ColumnaDependenciaDTO columnaDependenciaDTO) {
		ColumnaDependenciaVO columnaDependenciaVO = null;
		if (columnaDependenciaDTO != null) {
			columnaDependenciaVO = new ColumnaDependenciaVO();
			columnaDependenciaVO = ColumnaDependenciaAssembler.assembleVO(columnaDependenciaDTO);
		}
		return columnaDependenciaVO;
	}	

	private Collection<ColumnaDependenciaVO> assembleColumnasDependenciasVO(
			Collection<ColumnaDependenciaDTO> columnaDependenciaDTO) {
		Collection<ColumnaDependenciaVO> columnaDependenciaVO = null;
		if (columnaDependenciaDTO != null && columnaDependenciaDTO.size() > 0) {
			columnaDependenciaVO = new ArrayList<ColumnaDependenciaVO>();
			for (ColumnaDependenciaDTO dto : columnaDependenciaDTO) {
				ColumnaDependenciaVO vo = ColumnaDependenciaAssembler.assembleVO(dto);
				columnaDependenciaVO.add(vo);
			}
		}
		return columnaDependenciaVO;
	}
	
	
	public Collection<RegistroTablaVO> buscarRegistroTabla(RegistroTablaVO registroTablaVO)
			throws TablaException {
		Collection<RegistroTablaVO> registrosVO = new ArrayList<RegistroTablaVO>();;
		Collection<RegistroTablaDTO> registrosDTO = null;
		try {
			String nombreTabla = registroTablaVO.getNombreTabla();			
			Collection<ColumnaVO> columnasVO = registroTablaVO.getColumnas();			
			Collection<ColumnaDTO> columnas = this.assembleColumnasDTO(columnasVO);			
			RegistroTablaDTO registroTablaDTO = RegistroTablaAssembler.assembleDTO(registroTablaVO);			
			RegistroTablaDAO dao = TablaFacadeBean.daoFactory.getRegistroTablaDAO();
			registrosDTO = dao.buscar(nombreTabla, columnas, registroTablaDTO);			
			registrosVO = this.assembleRegistrosTablaVO(registrosDTO);		
			
			this.llenarValorMostrarRegistros(registrosVO, columnasVO);
												
		} catch (DataAccessException e) {
			throw new TablaException(e);
		} catch (ObjectNotFoundException e) {
			// TODO Bloque catch generado automáticamente
			LOG.error(e.getMessage(), e);
		}
		return registrosVO;
	}
	
	public Collection<RegistroTablaVO> getDatosByIdTablaRegistro(Integer idTabla, Map<String, Object> registro)
			throws TablaException {
		try {
			TablaDTO tablaDTO = null;
			Collection<ColumnaDTO> columnasDTO = null;
			Collection<ColumnaVO> columnasVO = null;
			Collection<RegistroTablaDTO> registrosDTO = null;
			Collection<RegistroTablaVO> registrosVO = null;
			ColumnaDAO columnaDAO = TablaFacadeBean.daoFactory.getColumnaDAO();
			RegistroTablaDAO registroTablaDAO = TablaFacadeBean.daoFactory.getRegistroTablaDAO();
			TablaDAO tablaDAO = TablaFacadeBean.daoFactory.getTablaDAO();
			if (idTabla != null) {
				tablaDTO = tablaDAO.findById(idTabla);
				ColumnaDTO filter = new ColumnaDTO();
				filter.setIdTabla(idTabla);
				columnasDTO = columnaDAO.read(filter);				
				RegistroTablaDTO registroTablaDTO = new RegistroTablaDTO();								
				registroTablaDTO.setDatosRegistro(registro);				
				registrosDTO = registroTablaDAO.buscarRegistroTabla(tablaDTO.getNombre(), columnasDTO, registroTablaDTO);				
				columnasVO = this.assembleColumnasVO(columnasDTO);
				registrosVO = this.assembleRegistrosTablaVO(registrosDTO);
			}
			return registrosVO;
		} catch (DataAccessException e) {
			throw new TablaException(e);
		} catch (ObjectNotFoundException e) {
			throw new TablaException(e);
		}
	}
	
	public Collection<RegistroTablaVO> getDatosByIdTablaRegistros(Integer idTabla, Map<String,  ArrayList<Object>> registro)
			throws TablaException {
		try {
			TablaDTO tablaDTO = null;
			Collection<ColumnaDTO> columnasDTO = null;
			Collection<ColumnaVO> columnasVO = null;
			Collection<RegistroTablaDTO> registrosDTO = null;
			Collection<RegistroTablaVO> registrosVO = null;
			ColumnaDAO columnaDAO = TablaFacadeBean.daoFactory.getColumnaDAO();
			RegistroTablaDAO registroTablaDAO = TablaFacadeBean.daoFactory.getRegistroTablaDAO();
			TablaDAO tablaDAO = TablaFacadeBean.daoFactory.getTablaDAO();
			if (idTabla != null) {
				tablaDTO = tablaDAO.findById(idTabla);
				ColumnaDTO filter = new ColumnaDTO();
				filter.setIdTabla(idTabla);
				columnasDTO = columnaDAO.read(filter);				
				RegistroTablaDTO registroTablaDTO = new RegistroTablaDTO();								
				registroTablaDTO.setDatosRegistros(registro);				
				registrosDTO = registroTablaDAO.buscarRegistrosTabla(tablaDTO.getNombre(), columnasDTO, registroTablaDTO);				
				columnasVO = this.assembleColumnasVO(columnasDTO);
				registrosVO = this.assembleRegistrosTablaVO(registrosDTO);
			}
			return registrosVO;
		} catch (DataAccessException e) {
			throw new TablaException(e);
		} catch (ObjectNotFoundException e) {
			throw new TablaException(e);
		}
	}
	
	public ColumnaDependenciaVO getDatosColumnaDependencia(Integer idColumna, String flDependenciaCruzada) {
		ColumnaDependenciaVO columnaDependenciaVO = null;
		ColumnaDependenciaDAO columnaDependenciaDAO = daoFactory.getColumnaDependenciaDAO();
		try {
			ColumnaDependenciaDTO columnaDependenciaDTO = columnaDependenciaDAO.buscarPorDependencia(idColumna, flDependenciaCruzada);
			columnaDependenciaVO = this.assembleColumnaDependenciaVO(columnaDependenciaDTO);
		} catch (DataAccessException e) {
			// TODO Bloque catch generado automáticamente
			LOG.error(e.getMessage(), e);
		} catch (ObjectNotFoundException e) {
			// TODO Bloque catch generado automáticamente
			LOG.error(e.getMessage(), e);
		}
		return columnaDependenciaVO;
	}
	
	public Collection<ColumnaDependenciaVO> getDatosColumnaDependenciaL(Integer idColumna, String flDependenciaCruzada, Integer idPadre) {
		Collection<ColumnaDependenciaVO> columnaDependenciaVO = null;
		ColumnaDependenciaDAO columnaDependenciaDAO = daoFactory.getColumnaDependenciaDAO();
		try {
			List<ColumnaDependenciaDTO> listColumnaDependenciaDTO = columnaDependenciaDAO.buscarPorDependenciaL(idColumna, flDependenciaCruzada, idPadre);
			columnaDependenciaVO = this.assembleColumnasDependenciasVO(listColumnaDependenciaDTO);
		} catch (DataAccessException e) {
			// TODO Bloque catch generado automáticamente
			LOG.error(e.getMessage(), e);
		} catch (ObjectNotFoundException e) {
			// TODO Bloque catch generado automáticamente
			LOG.error(e.getMessage(), e);
		}
		return columnaDependenciaVO;
	}
	
	private ParametrosConfVO assembleParametrosConfVO(
			ParametrosConfDTO parametrosConfDTO) {
		ParametrosConfVO parametrosConfVO = null;
		if (parametrosConfDTO != null) {
			parametrosConfVO = new ParametrosConfVO();
			parametrosConfVO = ParametrosConfAssembler.assembleVO(parametrosConfDTO);
		}
		return parametrosConfVO;
	}	
	
	public ParametrosConfVO getParametrosConf(int codigoAplicativo, String nombreVariable) {
		ParametrosConfVO parametrosConfVO = null;
		
		ParametrosConfDAO parametrosConfDAO = daoFactory.getParametrosConfDAO();
  
		try {
			ParametrosConfDTO parametrosConfDTO = parametrosConfDAO.findByVariable(codigoAplicativo, nombreVariable);
			parametrosConfVO = this.assembleParametrosConfVO(parametrosConfDTO);			
			
		} catch (DataAccessException e) {
			// TODO Bloque catch generado automáticamente
			LOG.error(e.getMessage(), e);
		} catch (ObjectNotFoundException e) {
			// TODO Bloque catch generado automáticamente
			LOG.error(e.getMessage(), e);
		}
		return parametrosConfVO;
	}
		
	/**
	 * Metodo generador de valores de tiempo Tc Te
	 * */
	public List<DatosGeneradosVO> getGenerarDatosGenericoTOE(ArrayList pParameters){
		DatosGeneradosDAO objDatosGeneradosDAO=daoFactory.getDatosGeneradosDAO();
		List<DatosGeneradosVO> listDatosGeneradosVO=null;
		try{
			List<DatosGeneradosDTO> listDatosGeneradosDTO=objDatosGeneradosDAO.generarDatosGenerico(pParameters);
			listDatosGeneradosVO=this.assembleDatosGeneradosVO(listDatosGeneradosDTO);
			
		}catch (DataAccessException e) {
			LOG.error(e.getMessage(), e);
		}
		
		return listDatosGeneradosVO;
	}
	
	/**
	 * Metodo generador de valor porcentaje TOE
	 * */
	public List<DatosPorcentajeVO> getGenerarDatosPorcentajeTOE(ArrayList pParameters){
		DatosPorcentajeDAO objDatosPorcentajeDAO=daoFactory.getDatosPorcentajeDAO();
		List<DatosPorcentajeVO> listDatosPorcentajeVO=null;
		try{
			//LOG.info("getGenerarDatosPorcentajeTOE 1");
			List<DatosPorcentajeDTO> listDatosPorcentajeDTO=objDatosPorcentajeDAO.generarDatosPorcentaje(pParameters);
				
			if(listDatosPorcentajeDTO!=null)
					//LOG.info("tamaño listDatosPorcentajeDTO = "+listDatosPorcentajeDTO.size());
			
			listDatosPorcentajeVO=this.assembleDatosPorcentajeVO(listDatosPorcentajeDTO);	
			
		}catch (DataAccessException e) {
			LOG.error(e.getMessage(), e);
			LOG.info("e = "+e.getMessage());
		}
		return listDatosPorcentajeVO;
	}	
	
	/**
	 * Metodo generador de valor unidad TOE
	 * */
	public List<DatosUnidadVO> getGenerarValorUnidadTOE(ArrayList pParameters){
		DatosUnidadDAO objDatosUnidadDAO=daoFactory.getDatosUnidadDAO();
		List<DatosUnidadVO> listDatosUnidadVO=null;
		try{
			List<DatosUnidadDTO> listDatosUnidadDTO=objDatosUnidadDAO.generarDatosUnidad(pParameters);
			listDatosUnidadVO=this.assembleDatosUnidadVO(listDatosUnidadDTO);	
			
		}catch (DataAccessException e) {
			LOG.error(e.getMessage(), e);
		}
		return listDatosUnidadVO;
	}		
	
	/**
	 * Convertidor de DTO a VO
	 * */
	
	private List<DatosUnidadVO> assembleDatosUnidadVO (
			List<DatosUnidadDTO> listDatosUnidadDTO){
		List<DatosUnidadVO> listDatosUnidadVO = null;
		DatosUnidadVO objDatosUnidadVO=null;
	
		if (listDatosUnidadDTO != null && listDatosUnidadDTO.size() > 0) {
			listDatosUnidadVO = new ArrayList<DatosUnidadVO>();				
			for (DatosUnidadDTO dto : listDatosUnidadDTO) {
				objDatosUnidadVO=new DatosUnidadVO();
				objDatosUnidadVO.setTipo(dto.getTipo());
				objDatosUnidadVO.setFlujo(dto.getFlujo());
				objDatosUnidadVO.setAceptado(dto.getAceptado());
				objDatosUnidadVO.setTotal(dto.getTotal());
				listDatosUnidadVO.add(objDatosUnidadVO);
			}
		}
		
	 return listDatosUnidadVO;
		
	}
	
	
	private List<DatosPorcentajeVO> assembleDatosPorcentajeVO (
			List<DatosPorcentajeDTO> listDatosPorcentajeDTO){
		List<DatosPorcentajeVO> listDatosPorcentajeVO = null;
		DatosPorcentajeVO objDatosPorcentajeVO=null;
	
		if (listDatosPorcentajeDTO != null && listDatosPorcentajeDTO.size() > 0) {
			listDatosPorcentajeVO = new ArrayList<DatosPorcentajeVO>();				
			for (DatosPorcentajeDTO dto : listDatosPorcentajeDTO) {
				objDatosPorcentajeVO=new DatosPorcentajeVO();
				objDatosPorcentajeVO.setValorRol(dto.getValorRol());
				objDatosPorcentajeVO.setValorFlujo(dto.getValorFlujo());
				objDatosPorcentajeVO.setValorPorcentaje(dto.getValorPorcentaje());
				listDatosPorcentajeVO.add(objDatosPorcentajeVO);
			}
		}
		
	 return listDatosPorcentajeVO;
		
	}
	
	private List<DatosGeneradosVO> assembleDatosGeneradosVO(
			List<DatosGeneradosDTO> listDatosGeneradosDTO) {
		List<DatosGeneradosVO> listDatosGeneradosVO = null;
		DatosGeneradosVO objDatosGeneradosVO=null;
		
		if (listDatosGeneradosDTO != null && listDatosGeneradosDTO.size() > 0) {
				listDatosGeneradosVO = new ArrayList<DatosGeneradosVO>();				
				for (DatosGeneradosDTO dto : listDatosGeneradosDTO) {
					objDatosGeneradosVO=DatosGeneradosAssembler.assembleVO(dto);
					listDatosGeneradosVO.add(objDatosGeneradosVO);
				}
		}
		return listDatosGeneradosVO;
	}
	
	/**
	 * Metodo generador de datos reporte consolidado TC
	 * */
	public List<DatosGeneradosConsVO> getGenerarDatosConsolidadoTC(ArrayList pParameters){
		DatosGeneradosConsDAO objDatosGeneradosConsDAO=daoFactory.getDatosGeneradosConsDAO();
		List<DatosGeneradosConsVO> listDatosGeneradosCons=null;
		try{
			List<DatosGeneradosConsDTO> listDatosGeneradosConsDTO=objDatosGeneradosConsDAO.generarDatosConsolidado(pParameters);
			listDatosGeneradosCons=this.assembleDatosGeneradosCons(listDatosGeneradosConsDTO);	
			
		}catch (DataAccessException e) {
			LOG.error(e.getMessage(), e);
		}
		return listDatosGeneradosCons;
	}
	
	/**
	 * Metodo generador de datos reporte historico TC
	 * */
	public List<DatosGeneradosHisVO> getGenerarDatosHistoricoTC(ArrayList pParameters){
		DatosGeneradosHisDAO objDatosGeneradosHisDAO=daoFactory.getDatosGeneradosHisDAO();
		List<DatosGeneradosHisVO> listDatosGeneradosHis=null;
		try{
			List<DatosGeneradosHisDTO> listDatosGeneradosHisDTO=objDatosGeneradosHisDAO.generarDatosHistorico(pParameters);
			listDatosGeneradosHis=this.assembleDatosGeneradosHis(listDatosGeneradosHisDTO);	
			
		}catch (DataAccessException e) {
			LOG.error(e.getMessage(), e);
		}
		return listDatosGeneradosHis;
	}
	
	private List<DatosGeneradosConsVO> assembleDatosGeneradosCons(List<DatosGeneradosConsDTO> listDatosGeneradosConsDTO) {
		List<DatosGeneradosConsVO> listDatosGeneradosCons = null;
		DatosGeneradosConsVO objDatosGeneradosCons=null;
		
		if (listDatosGeneradosConsDTO != null && listDatosGeneradosConsDTO.size() > 0) {
				listDatosGeneradosCons = new ArrayList<DatosGeneradosConsVO>();				
				for (DatosGeneradosConsDTO dto : listDatosGeneradosConsDTO) {
					objDatosGeneradosCons=new DatosGeneradosConsVO();
					objDatosGeneradosCons.setNumeroExpediente(dto.getNumeroExpediente());
					objDatosGeneradosCons.setCodigoSegmentoCliente(dto.getCodigoSegmentoCliente());
					objDatosGeneradosCons.setDescripcionSegmentoCliente(dto.getDescripcionSegmentoCliente());
					objDatosGeneradosCons.setCorrelativoEstado(dto.getCorrelativoEstado());
					objDatosGeneradosCons.setNombreEstado(dto.getNombreEstado());
					objDatosGeneradosCons.setNombreProducto(dto.getNombreProducto());
					objDatosGeneradosCons.setCodigoUsuarioCreacion(dto.getCodigoUsuarioCreacion());
					objDatosGeneradosCons.setNombreUsuarioCreacion(dto.getNombreUsuarioCreacion());
					objDatosGeneradosCons.setFechaCreacion(dto.getFechaCreacion());
					objDatosGeneradosCons.setCorrelativoOficina(dto.getCorrelativoOficina());
					objDatosGeneradosCons.setCodigoOficina(dto.getCodigoOficina());
					objDatosGeneradosCons.setNombreOficina(dto.getNombreOficina());
					objDatosGeneradosCons.setCodigoGarantia(dto.getCodigoGarantia());
					objDatosGeneradosCons.setDescripcionGarantia(dto.getDescripcionGarantia());
					objDatosGeneradosCons.setCorrelativoSubproducto(dto.getCorrelativoSubproducto());
					objDatosGeneradosCons.setNombreSubproducto(dto.getNombreSubproducto());
					objDatosGeneradosCons.setCorrelativoCliente(dto.getCorrelativoCliente());
					objDatosGeneradosCons.setApellidoPaternoCliente(dto.getApellidoPaternoCliente());
					objDatosGeneradosCons.setApellidoMaternoCliente(dto.getApellidoMaternoCliente());
					objDatosGeneradosCons.setNombresCliente(dto.getNombresCliente());
					objDatosGeneradosCons.setTipoDocumentoIdentidad(dto.getTipoDocumentoIdentidad());
					objDatosGeneradosCons.setNumeroDocumentoIdentidad(dto.getNumeroDocumentoIdentidad());
					objDatosGeneradosCons.setTipoCliente(dto.getTipoCliente());
					objDatosGeneradosCons.setIngresoNetoMensual(dto.getIngresoNetoMensual());
					objDatosGeneradosCons.setEstadoCivil(dto.getEstadoCivil());
					objDatosGeneradosCons.setPersonaExpuestaPublica(dto.getPersonaExpuestaPublica());
					objDatosGeneradosCons.setPagoHabiente(dto.getPagoHabiente());
					objDatosGeneradosCons.setSubrogado(dto.getSubrogado());
					objDatosGeneradosCons.setTipoOferta(dto.getTipoOferta());
					objDatosGeneradosCons.setMonedaImporteSolicitado(dto.getMonedaImporteSolicitado());
					objDatosGeneradosCons.setImporteSolicitado(dto.getImporteSolicitado());
					objDatosGeneradosCons.setMonedaImporteAprobado(dto.getMonedaImporteAprobado());
					objDatosGeneradosCons.setImporteAprobado(dto.getImporteAprobado());
					objDatosGeneradosCons.setPlazoSolicitado(dto.getPlazoSolicitado());
					objDatosGeneradosCons.setPlazoAprobado(dto.getPlazoAprobado());
					objDatosGeneradosCons.setTipoResolucion(dto.getTipoResolucion());
					objDatosGeneradosCons.setCodigoPreevaluador(dto.getCodigoPreevaluador());
					objDatosGeneradosCons.setCodigoRvgl(dto.getCodigoRvgl());
					objDatosGeneradosCons.setLineaConsumo(dto.getLineaConsumo());
					objDatosGeneradosCons.setRiesgoClienteGrupal(dto.getRiesgoClienteGrupal());
					objDatosGeneradosCons.setPorcentajeEndeudamiento(dto.getPorcentajeEndeudamiento());
					objDatosGeneradosCons.setCodigoContrato(dto.getCodigoContrato());
					objDatosGeneradosCons.setGrupoBuro(dto.getGrupoBuro());
					objDatosGeneradosCons.setClasificacionSbsTitular(dto.getClasificacionSbsTitular());
					objDatosGeneradosCons.setClasificacionBancoTitular(dto.getClasificacionBancoTitular());
					objDatosGeneradosCons.setClasificacionSbsConyuge(dto.getClasificacionSbsConyuge());
					objDatosGeneradosCons.setClasificacionBancoConyuge(dto.getClasificacionBancoConyuge());
					objDatosGeneradosCons.setScoring(dto.getScoring());
					objDatosGeneradosCons.setTasaEspecial(dto.getTasaEspecial());
					objDatosGeneradosCons.setFlagVerifDomiciliaria(dto.getFlagVerifDomiciliaria());
					objDatosGeneradosCons.setEstadoVerifDomiciliaria(dto.getEstadoVerifDomiciliaria());
					objDatosGeneradosCons.setFlagVerifLaboral(dto.getFlagVerifLaboral());
					objDatosGeneradosCons.setEstadoVerifLaboral(dto.getEstadoVerifLaboral());
					objDatosGeneradosCons.setFlagDps(dto.getFlagDps());
					objDatosGeneradosCons.setEstadoDps(dto.getEstadoDps());
					objDatosGeneradosCons.setModificarTasa(dto.getModificarTasa());
					objDatosGeneradosCons.setModificarScoring(dto.getModificarScoring());
					objDatosGeneradosCons.setIndicadorDelegacion(dto.getIndicadorDelegacion());
					objDatosGeneradosCons.setIndicadorExclusionDelegacion(dto.getIndicadorExclusionDelegacion());
					objDatosGeneradosCons.setNivelComplejidad(dto.getNivelComplejidad());
					objDatosGeneradosCons.setNroDevoluciones(dto.getNroDevoluciones());
					objDatosGeneradosCons.setCodigoUsuarioActual(dto.getCodigoUsuarioActual());
					listDatosGeneradosCons.add(objDatosGeneradosCons);
				}
		}
		return listDatosGeneradosCons;
	}
	
	private List<DatosGeneradosHisVO> assembleDatosGeneradosHis(List<DatosGeneradosHisDTO> listDatosGeneradosHisDTO) {
		List<DatosGeneradosHisVO> listDatosGeneradosHis = null;
		DatosGeneradosHisVO objDatosGeneradosHis=null;
		
		if (listDatosGeneradosHisDTO != null && listDatosGeneradosHisDTO.size() > 0) {
				listDatosGeneradosHis = new ArrayList<DatosGeneradosHisVO>();				
				for (DatosGeneradosHisDTO dto : listDatosGeneradosHisDTO) {
					objDatosGeneradosHis=new DatosGeneradosHisVO();
					objDatosGeneradosHis.setNroExpediente(dto.getNroExpediente());
					objDatosGeneradosHis.setNumeroRegistro(dto.getNumeroRegistro());
					objDatosGeneradosHis.setUsuarioActual(dto.getUsuarioActual());
					objDatosGeneradosHis.setEstadoExpediente(dto.getEstadoExpediente());
					objDatosGeneradosHis.setCodigoEstado(dto.getCodigoEstado());
					objDatosGeneradosHis.setPerfil(dto.getPerfil());
					objDatosGeneradosHis.setTarea(dto.getTarea());
					objDatosGeneradosHis.setAccion(dto.getAccion());
					objDatosGeneradosHis.setCodigoProducto(dto.getCodigoProducto());
					objDatosGeneradosHis.setNombreProducto(dto.getNombreProducto());
					objDatosGeneradosHis.setNombreTipoOferta(dto.getNombreTipoOferta());
					objDatosGeneradosHis.setCodigoSubProducto(dto.getCodigoSubProducto());
					objDatosGeneradosHis.setNombreSubProducto(dto.getNombreSubProducto());
					objDatosGeneradosHis.setCodigoOficina(dto.getCodigoOficina());
					objDatosGeneradosHis.setNombreOficina(dto.getNombreOficina());
					objDatosGeneradosHis.setCodigoTerritorio(dto.getCodigoTerritorio());
					objDatosGeneradosHis.setNombreTerritorio(dto.getNombreTerritorio());
					objDatosGeneradosHis.setFechaHoraLlegada(dto.getFechaHoraLlegada());
					objDatosGeneradosHis.setFechaHoraInicioTrabajo(dto.getFechaHoraInicioTrabajo());
					objDatosGeneradosHis.setFechaHoraEnvio(dto.getFechaHoraEnvio());
					objDatosGeneradosHis.setTiempoEjecucionTe(dto.getTiempoEjecucionTe());
					objDatosGeneradosHis.setTiempoColaTc(dto.getTiempoColaTc());
					objDatosGeneradosHis.setTiempoProcesoTp(dto.getTiempoProcesoTp());
					objDatosGeneradosHis.setCumplioAns(dto.getCumplioAns());
					objDatosGeneradosHis.setFlagDevolucion(dto.getFlagDevolucion());
					objDatosGeneradosHis.setFlagRetraer(dto.getFlagRetraer());
					objDatosGeneradosHis.setTerminal(dto.getTerminal());
					objDatosGeneradosHis.setObservacion(dto.getObservacion());
					objDatosGeneradosHis.setMotivoRechazo(dto.getMotivoRechazo());
					objDatosGeneradosHis.setComentario(dto.getComentario());
					objDatosGeneradosHis.setAns(dto.getAns());
					listDatosGeneradosHis.add(objDatosGeneradosHis);
				}
		}
		return listDatosGeneradosHis;
	}	
	
	public boolean getDatosColumnaDependenciaPadre(Integer idPadre) {
		ColumnaDependenciaVO columnaDependenciaVO = null;
		ColumnaDependenciaDAO columnaDependenciaDAO = daoFactory.getColumnaDependenciaDAO();
		try {
			ColumnaDependenciaDTO  columnaDependenciaDTO = columnaDependenciaDAO.findById(idPadre);
			columnaDependenciaVO = this.assembleColumnaDependenciaVO(columnaDependenciaDTO);
			if (columnaDependenciaVO==null)
				return false;
		} catch (DataAccessException e) {
			// TODO Bloque catch generado automáticamente
			LOG.error(e.getMessage(), e);
		} catch (ObjectNotFoundException e) {
			// TODO Bloque catch generado automáticamente
			LOG.error(e.getMessage(), e);
		}
		return true;
	}
}