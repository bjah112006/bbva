package com.ibm.bbva.tabla.core.assembler;

import java.util.Map;

import com.ibm.bbva.tabla.dto.RegistroTablaDTO;
import com.ibm.bbva.tabla.vo.RegistroTablaVO;



public class RegistroTablaAssembler {

	public static RegistroTablaVO assembleVO(RegistroTablaDTO dto) {
		RegistroTablaVO vo = new RegistroTablaVO();
		if (dto != null) {
			Map<String, Object> datosRegistro = dto.getDatosRegistro();
			return new RegistroTablaVO(datosRegistro);
		}
		return vo;
	}

	public static RegistroTablaDTO assembleDTO(RegistroTablaVO vo) {
		RegistroTablaDTO dto = new RegistroTablaDTO();
		dto.setDatosRegistro(vo.getDatosRegistro());
		return dto;
	}
}
