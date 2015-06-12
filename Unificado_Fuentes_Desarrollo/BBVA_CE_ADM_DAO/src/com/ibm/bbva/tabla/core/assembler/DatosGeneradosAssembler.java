package com.ibm.bbva.tabla.core.assembler;

import com.ibm.bbva.tabla.dto.ParametrosConfDTO;
import com.ibm.bbva.tabla.reporte.dto.DatosGeneradosDTO;
import com.ibm.bbva.tabla.reporte.vo.DatosGeneradosVO;
import com.ibm.bbva.tabla.vo.ParametrosConfVO;

public class DatosGeneradosAssembler {

	static final long serialVersionUID = 1243526157593L;

	public static DatosGeneradosVO assembleVO(DatosGeneradosDTO dto) {
		DatosGeneradosVO vo =new DatosGeneradosVO();
		
		vo.setValor(dto.getValor());
		vo.setTc(formatDecimal(dto.getTc()));
		vo.setTe(formatDecimal(dto.getTe()));
		vo.setTipoConcepto(dto.getTipoConcepto());
		vo.setFlujo(dto.getFlujo());
		
		return vo;
	}
	public static DatosGeneradosDTO assembleDTO(DatosGeneradosVO vo) {
		DatosGeneradosDTO dto = new DatosGeneradosDTO();

		dto.setValor( vo.getValor() );
		dto.setTc( vo.getTc() );
		dto.setTe( vo.getTe() );
		dto.setTipoConcepto( vo.getTipoConcepto() );
		dto.setFlujo(vo.getFlujo());

		return dto;
	}
	
	public static String formatDecimal(String cad){
		if(cad!=null && cad.trim().length()>0){
			Double d=Double.parseDouble(cad);
			return d.toString();			
		}
		return null;
	}
}