package com.ibm.bbva.tabla.core.assembler;

import com.ibm.bbva.tabla.dto.ColumnaDTO;
import com.ibm.bbva.tabla.vo.ColumnaVO;




public class ColumnaAssembler {

	static final long serialVersionUID = 1243526157593L;

	public static ColumnaVO assembleVO(ColumnaDTO dto) {
		ColumnaVO vo = new ColumnaVO();

		vo.setId( dto.getId() );
		vo.setNombre( dto.getNombre() );
		vo.setEsLlavePrimaria( dto.isEsLlavePrimaria() );
		vo.setIdTabla( dto.getIdTabla() );
		vo.setEsRequerido( dto.isEsRequerido() );
		vo.setLongitudMaxima( dto.getLongitudMaxima() );
		vo.setNombreMostrar( dto.getNombreMostrar() );
		vo.setTipoDato( dto.getTipoDato() );
		vo.setEsLlaveForanea(dto.isEsLlaveForanea());
		vo.setBusqueda(dto.getBusqueda());
		vo.setValorRegistro(dto.getValorRegistro());
		vo.setPlantilla(dto.getPlantilla());
		vo.setOrdenColumna(dto.getOrdenColumna());
		vo.setFlagDependencia(dto.getFlagDependencia());
		
		if(vo.getEsLlavePrimaria())
			vo.setSoloLectura(true);

		return vo;
	}
	public static ColumnaDTO assembleDTO(ColumnaVO vo) {
		ColumnaDTO dto = new ColumnaDTO();

		dto.setId( vo.getId() );
		dto.setNombre( vo.getNombre() );
		dto.setEsLlavePrimaria( vo.isEsLlavePrimaria() );
		dto.setIdTabla( vo.getIdTabla() );
		dto.setEsRequerido( vo.isEsRequerido() );
		dto.setLongitudMaxima( vo.getLongitudMaxima() );
		dto.setNombreMostrar( vo.getNombreMostrar() );
		dto.setTipoDato( vo.getTipoDato() );
		dto.setEsLlaveForanea(vo.isEsLlaveForanea());
		dto.setBusqueda(vo.getBusqueda());
		dto.setValorRegistro(vo.getValorRegistro());
		dto.setPlantilla(vo.getPlantilla());
		dto.setOrdenColumna(vo.getOrdenColumna());
		dto.setFlagDependencia(vo.getFlagDependencia());
		
		return dto;
	}
}
