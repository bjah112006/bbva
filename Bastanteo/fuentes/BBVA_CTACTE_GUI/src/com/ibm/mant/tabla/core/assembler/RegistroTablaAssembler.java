package com.ibm.mant.tabla.core.assembler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.mant.tabla.dto.RegistroTablaDTO;
import com.ibm.mant.tabla.vo.RegistroTablaVO;



public class RegistroTablaAssembler {
	
	private static final Logger LOG = LoggerFactory.getLogger(RegistroTablaAssembler.class);

	public static RegistroTablaVO assembleVO(RegistroTablaDTO dto) {
		RegistroTablaVO vo = new RegistroTablaVO();
		if (dto != null) {
			Map<String, Object> datosRegistro = dto.getDatosRegistro();
			LOG.info("Map<String, Object> datosRegistro: " + datosRegistro.values());
			LOG.info("datosRegistro.keySet" + datosRegistro.keySet());
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
