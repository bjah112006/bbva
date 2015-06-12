package com.ibm.bbva.tabla;

import java.util.Collection;

import com.ibm.bbva.tabla.helper.TablaException;
import com.ibm.bbva.tabla.vo.RegistroTablaVO;
import com.ibm.bbva.tabla.vo.TablaParametricaVO;
import com.ibm.bbva.tabla.vo.TablaVO;




public interface TablaBInterface {

	public Collection<TablaVO> getTablasParametrizables() throws TablaException;

	public TablaParametricaVO getDatosYEstructuraTablaByIdTabla(Integer idTabla)
			throws TablaException;

	public void actualizarRegistroTabla(RegistroTablaVO registroTablaVO) throws TablaException;

	public void crearRegistroTabla(RegistroTablaVO registroTablaVO) throws TablaException;

	public void eliminarRegistroTabla(RegistroTablaVO registroTablaVO) throws TablaException;
	
	public Collection<RegistroTablaVO> buscarRegistroTabla(RegistroTablaVO registroTablaVO) throws TablaException;
}
