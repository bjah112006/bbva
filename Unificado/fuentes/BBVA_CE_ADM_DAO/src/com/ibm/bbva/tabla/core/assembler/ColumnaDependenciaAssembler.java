package com.ibm.bbva.tabla.core.assembler;

import com.ibm.bbva.tabla.dto.ColumnaDependenciaDTO;
import com.ibm.bbva.tabla.vo.ColumnaDependenciaVO;

public class ColumnaDependenciaAssembler {

	static final long serialVersionUID = 1243526157593L;

	public static ColumnaDependenciaVO assembleVO(ColumnaDependenciaDTO dto) {
		ColumnaDependenciaVO vo = new ColumnaDependenciaVO();

		vo.setId( dto.getId() );
		vo.setIdColumnaPadre( dto.getIdColumnaPadre() );
		vo.setIdTablaHijo( dto.getIdTablaHijo() );
		vo.setIdColumnaHijo( dto.getIdColumnaHijo() );		
		vo.setNombreColumna( dto.getNombreColumna() );
		vo.setValorColumna( dto.getValorColumna() );
		vo.setNombreColumnaBsq( dto.getNombreColumnaBsq() );
		vo.setFlagDependenciaCruzada( dto.getFlagDependenciaCruzada());
		vo.setNombreColumnaDependencia( dto.getNombreColumnaDependencia());
		vo.setIdPadre( dto.getIdPadre());
		
		return vo;
	}
	public static ColumnaDependenciaDTO assembleDTO(ColumnaDependenciaVO vo) {
		ColumnaDependenciaDTO dto = new ColumnaDependenciaDTO();

		dto.setId( vo.getId() );
		dto.setIdColumnaPadre( vo.getIdColumnaPadre() );
		dto.setIdTablaHijo( vo.getIdTablaHijo() );
		dto.setIdColumnaHijo( vo.getIdColumnaHijo() );		
		dto.setNombreColumna( vo.getNombreColumna() );
		dto.setValorColumna( vo.getValorColumna() );
		dto.setNombreColumnaBsq( vo.getNombreColumnaBsq() );
		dto.setFlagDependenciaCruzada( vo.getFlagDependenciaCruzada());
		dto.setNombreColumnaDependencia( vo.getNombreColumnaDependencia());
		dto.setIdPadre( vo.getIdPadre());

		return dto;
	}
}
