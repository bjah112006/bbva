package com.ibm.mant.tabla.core.assembler;

import com.ibm.mant.tabla.dto.ColumnaDTO;
import com.ibm.mant.tabla.vo.ColumnaVO;




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
		
		return dto;
	}
}
