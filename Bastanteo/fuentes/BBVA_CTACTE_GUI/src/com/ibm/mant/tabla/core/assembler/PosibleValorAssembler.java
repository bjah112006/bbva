package com.ibm.mant.tabla.core.assembler;

import com.ibm.mant.tabla.dto.PosibleValorDTO;
import com.ibm.mant.tabla.vo.PosibleValorVO;




public class PosibleValorAssembler {

	static final long serialVersionUID = 1243526157593L;

	public static PosibleValorVO assembleVO(PosibleValorDTO dto) {
		PosibleValorVO vo = new PosibleValorVO();

		vo.setId( dto.getId() );
		vo.setValor( dto.getValor() );
		vo.setIdColumna( dto.getIdColumna() );
		vo.setEtiqueta( dto.getEtiqueta() );

		return vo;
	}
	public static PosibleValorDTO assembleDTO(PosibleValorVO vo) {
		PosibleValorDTO dto = new PosibleValorDTO();

		dto.setId( vo.getId() );
		dto.setValor( vo.getValor() );
		dto.setIdColumna( vo.getIdColumna() );
		dto.setEtiqueta( vo.getEtiqueta() );

		return dto;
	}
}
