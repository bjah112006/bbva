package com.ibm.bbva.tabla.core.assembler;

import com.ibm.bbva.tabla.dto.ParametrosConfDTO;
import com.ibm.bbva.tabla.vo.ParametrosConfVO;

public class ParametrosConfAssembler {

	static final long serialVersionUID = 1243526157593L;

	public static ParametrosConfVO assembleVO(ParametrosConfDTO dto) {
		ParametrosConfVO vo = new ParametrosConfVO();

		vo.setId( dto.getId() );
		vo.setCodigoAplicativo( dto.getCodigoAplicativo() );
		vo.setNombreVariable( dto.getNombreVariable() );
		vo.setValorVariable( dto.getValorVariable() );

		return vo;
	}
	public static ParametrosConfDTO assembleDTO(ParametrosConfVO vo) {
		ParametrosConfDTO dto = new ParametrosConfDTO();

		dto.setId( vo.getId() );
		dto.setCodigoAplicativo( vo.getCodigoAplicativo() );
		dto.setNombreVariable( vo.getNombreVariable() );
		dto.setValorVariable( vo.getValorVariable() );

		return dto;
	}
}
