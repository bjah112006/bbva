package com.ibm.mant.tabla.ejb.impl;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.exception.ObjectNotFoundException;
import com.ibm.as.core.helpers.TipoDato;
import com.ibm.as.core.util.CommonUtils;
import com.ibm.mant.tabla.core.assembler.ColumnaAssembler;
import com.ibm.mant.tabla.core.assembler.PosibleValorAssembler;
import com.ibm.mant.tabla.core.assembler.RegistroTablaAssembler;
import com.ibm.mant.tabla.core.assembler.TablaAssembler;
import com.ibm.mant.tabla.core.dao.DAOFactory;
import com.ibm.mant.tabla.dao.ColumnaDAO;
import com.ibm.mant.tabla.dao.PosibleValorDAO;
import com.ibm.mant.tabla.dao.RegistroTablaDAO;
import com.ibm.mant.tabla.dao.TablaDAO;
import com.ibm.mant.tabla.dao.ValorForaneoDAO;
import com.ibm.mant.tabla.dao.ValorForaneoDataDAO;
import com.ibm.mant.tabla.dto.ColumnaDTO;
import com.ibm.mant.tabla.dto.PosibleValorDTO;
import com.ibm.mant.tabla.dto.RegistroTablaDTO;
import com.ibm.mant.tabla.dto.TablaDTO;
import com.ibm.mant.tabla.dto.ValorForaneoDTO;
import com.ibm.mant.tabla.dto.ValorForaneoDataDTO;
import com.ibm.mant.tabla.helper.TablaException;
import com.ibm.mant.tabla.service.Constantes;
import com.ibm.mant.tabla.vo.ColumnaVO;
import com.ibm.mant.tabla.vo.PosibleValorVO;
import com.ibm.mant.tabla.vo.RegistroTablaVO;
import com.ibm.mant.tabla.vo.TablaParametricaVO;
import com.ibm.mant.tabla.vo.TablaVO;

/**
 * Bean implementation class for Enterprise Bean: TablaFacade
 */
public class TablaFacadeBean {
	
	private static final Logger LOG = LoggerFactory.getLogger(TablaFacadeBean.class);

	private static final long serialVersionUID = -2543633865357289787L;

	private static DAOFactory daoFactory = DAOFactory.getDefaultDAOFactory();
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public String getRegistroTablaById(String nombreTabla,String nombreColumna,String nombreColumnaId,Integer id) throws DataAccessException {
		RegistroTablaDAO registroTablaDAO = TablaFacadeBean.daoFactory
				.getRegistroTablaDAO();
		String xmlData = registroTablaDAO.getRegistroTablaById(nombreTabla, nombreColumna, nombreColumnaId, id);
		LOG.info("xmlData: " + xmlData);
		return xmlData;
	}
	
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
		
		String tipoOrden=null;
		Integer columOrdenPos=null;
		String columOrdenNom=null;
		
		try {
			TablaParametricaVO tablaParametricaVO = null;

			TablaVO tablaVO = null;
			TablaDTO tablaDTO = null;
			Collection<ColumnaDTO> columnasDTO = null;
			Collection<ColumnaVO> columnasVO = null;
			Collection<RegistroTablaDTO> registrosDTO = null;
			Collection<RegistroTablaVO> registrosVO = null;
			ColumnaDAO columnaDAO = TablaFacadeBean.daoFactory.getColumnaDAO();
			RegistroTablaDAO registroTablaDAO = TablaFacadeBean.daoFactory
					.getRegistroTablaDAO();
			TablaDAO tablaDAO = TablaFacadeBean.daoFactory.getTablaDAO();
			if (idTabla != null) {
				tablaDTO = tablaDAO.findById(idTabla);
				
				ColumnaDTO filter = new ColumnaDTO();
				filter.setIdTabla(idTabla);
				columnasDTO = columnaDAO.read(filter);
				
				if(tablaDTO.getOrden()!=null){
					
					int pos=tablaDTO.getOrden().toString().trim().indexOf("-");
					
					if(pos>0){
						columOrdenPos=Integer.parseInt(tablaDTO.getOrden().toString().trim().substring(0,pos));
						
						if(!CommonUtils.isNumeric(""+columOrdenPos)){
							columOrdenPos=null;
						}
					}else{
						if(pos!=0)
							if(CommonUtils.isNumeric(tablaDTO.getOrden().toString().trim().substring(0,tablaDTO.getOrden().toString().trim().length())))
								columOrdenPos=Integer.parseInt(tablaDTO.getOrden().toString().trim().substring(0,tablaDTO.getOrden().toString().trim().length()));
					}
					
					if(columnasDTO!=null && columnasDTO.size()>0 && columOrdenPos!=null){
						
						for (ColumnaDTO dto : columnasDTO){
														
							if(dto.getId().compareTo(columOrdenPos)==0 && dto.getIdTabla().compareTo(tablaDTO.getId())==0){
								columOrdenNom=dto.getNombre().toString().trim();
								LOG.info("columOrdenNom: " + columOrdenNom);
								if(pos>0)
									tipoOrden=tablaDTO.getOrden().toString().trim().substring(pos+1,tablaDTO.getOrden().toString().trim().length());
								if(tipoOrden!=null){
									if(tipoOrden.toUpperCase().equals("ASC") || tipoOrden.toUpperCase().equals("DESC"))
										LOG.info("");
									else	tipoOrden=null;
								}
								
								break;
							}
						}						
					}					
				}
				if (columnasDTO != null && columnasDTO.size() > 0) {
					registrosDTO = registroTablaDAO.getRegistrosTablaByNombreTabla(
							tablaDTO.getNombre(), columnasDTO,columOrdenNom, tipoOrden);
				}
				
				//Setea los datos de la tabla TABLA al objeto tablaVO
				tablaVO = TablaAssembler.assembleVO(tablaDTO);
				//Setea los datos de la tabla COLUMNA al objeto ColumnasVO
				columnasVO = this.assembleColumnasVO(columnasDTO);
				//Setea los datos de la tabla consultada al objeto tablaVO-MAP<String,Object>
				registrosVO = this.assembleRegistrosTablaVO(registrosDTO);
				
				//Agrega map adicional al objeto registro-Map<String,Object> conteniendo la ETIQUETA de la tabla externa
				this.llenarValorMostrarRegistros(registrosVO, columnasVO);
				//Setea todos los registros de la tabla POSIBLE VALOR y de la consulta a Valores Foraneos de la columna LISTA en el objeto PosibleValor<Collection>
				this.llenarPosiblesValoresPorColumna(columnasVO);
				
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
		if (columnas != null && columnas.size() > 0) {
			PosibleValorDAO dao = TablaFacadeBean.daoFactory
					.getPosibleValorDAO();
			ValorForaneoDAO dao2= TablaFacadeBean.daoFactory
					.getValorForaneoDAO();
			ValorForaneoDataDAO dao3= TablaFacadeBean.daoFactory
			.getValorForaneoDataDAO();
			
			for (ColumnaVO columna : columnas) {
				Collection<PosibleValorVO> posiblesValoresVO = null;
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
						ValorForaneoDTO valorForaneoDTO= dao2
							.findById(columna.getId());
						Collection<ValorForaneoDataDTO> dataDTO=dao3.getValoresForaneos(valorForaneoDTO.getNombreTabla(),
								valorForaneoDTO.getNombreColumna(),
								valorForaneoDTO.getValorColumna());
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
						columna.setPosiblesValores(posiblesValoresVO);
					}
				}
			}
		}
	}
	
	private void llenarValorMostrarRegistros( Collection<RegistroTablaVO> registros, Collection<ColumnaVO> columnas) throws DataAccessException, ObjectNotFoundException{
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
						LOG.info("Valor Foraneo columnaVO.getId(): " + columnaVO.getId());
						LOG.info("Valor Foraneo columnaVO.getNombre(): " + columnaVO.getNombre());
						LOG.info("Valor Foraneo registro.get(columnaVO.getNombre()): " + registro.get(columnaVO.getNombre()));
						
						Integer valorColumna=((BigDecimal)registro.get(columnaVO.getNombre())) == null ? null : ((BigDecimal)registro.get(columnaVO.getNombre())).intValue();
						if (valorColumna!=null && valorColumna!=Constantes.CODIGO_CODIGO_CAMPO_VACIO) {
							ValorForaneoDataDTO valorForaneo=dao3.getValorById(valorForaneoDTO.getNombreTabla(),valorForaneoDTO.getNombreColumna(),valorForaneoDTO.getValorColumna(),valorColumna);
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
			LOG.error(e.getMessage(), e);
		}
		return registrosVO;
	}
}
