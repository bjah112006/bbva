package com.ibm.mant.tabla.core.assembler;

import com.ibm.mant.tabla.dto.TablaDTO;
import com.ibm.mant.tabla.vo.TablaVO;




public class TablaAssembler {

	static final long serialVersionUID = 1243526157593L;

	public static TablaVO assembleVO(TablaDTO dto) {
		TablaVO vo = new TablaVO();

		vo.setId( dto.getId() );
		vo.setNombre( dto.getNombre() );
		vo.setNombreMostrar( dto.getNombreMostrar() );
		vo.setTipo( dto.getTipo());		
		vo.setBotonAgregar( dto.getBotonAgregar());
		vo.setBotonActualizar( dto.getBotonActualizar());
		vo.setBotonEliminar( dto.getBotonEliminar());
		vo.setOrden(dto.getOrden());

		return vo;
	}
	public static TablaDTO assembleDTO(TablaVO vo) {
		TablaDTO dto = new TablaDTO();

		dto.setId( vo.getId() );
		dto.setNombre( vo.getNombre() );
		dto.setNombreMostrar( vo.getNombreMostrar() );
		dto.setTipo( vo.getTipo());
		dto.setBotonAgregar( vo.getBotonAgregar());
		dto.setBotonActualizar( vo.getBotonActualizar());
		dto.setBotonEliminar( vo.getBotonEliminar());
		
		return dto;
	}
}
