package com.ibm.bbva.tabla;

import java.util.Collection;


import com.ibm.bbva.tabla.helper.TablaException;
import com.ibm.bbva.tabla.vo.RegistroTablaVO;
import com.ibm.bbva.tabla.vo.TablaParametricaVO;
import com.ibm.bbva.tabla.vo.TablaVO;
import com.ibm.as.core.exception.ServiceLocationException;
import com.ibm.as.core.view.BaseBusinessDelegate;



public class TablaBDelegate extends BaseBusinessDelegate implements
		TablaBInterface {

	public Collection<TablaVO> getTablasParametrizables()
			throws TablaException {

		try {
			TablaBInterface interfaz = null;
			interfaz = (TablaBInterface) getLocalBean("java:comp/env/ejb/TablaFacade");
			return interfaz.getTablasParametrizables();
		} catch (ServiceLocationException e) {
			throw new TablaException(e);
		}
	}

	public TablaParametricaVO getDatosYEstructuraTablaByIdTabla(Integer idTabla)
			throws TablaException {
		
		try {
			TablaBInterface interfaz = null;
			interfaz = (TablaBInterface) getLocalBean("java:comp/env/ejb/TablaFacade");
			return interfaz.getDatosYEstructuraTablaByIdTabla(idTabla);
		} catch (ServiceLocationException e) {
			throw new TablaException(e);
		}
	}

	public void actualizarRegistroTabla(RegistroTablaVO registroTablaVO)
			throws TablaException {
		try {
			TablaBInterface interfaz = null;
			interfaz = (TablaBInterface) getLocalBean("java:comp/env/ejb/TablaFacade");
			interfaz.actualizarRegistroTabla(registroTablaVO);
		} catch (ServiceLocationException e) {
			throw new TablaException(e);
		}
	}

	public void crearRegistroTabla(RegistroTablaVO registroTablaVO)
			throws TablaException {
		try {
			TablaBInterface interfaz = null;
			interfaz = (TablaBInterface) getLocalBean("java:comp/env/ejb/TablaFacade");
			interfaz.crearRegistroTabla(registroTablaVO);
		} catch (ServiceLocationException e) {
			throw new TablaException(e);
		}
	}

	public void eliminarRegistroTabla(RegistroTablaVO registroTablaVO)
			throws TablaException {
		try {
			TablaBInterface interfaz = null;
			interfaz = (TablaBInterface) getLocalBean("java:comp/env/ejb/TablaFacade");
			interfaz.eliminarRegistroTabla(registroTablaVO);
		} catch (ServiceLocationException e) {
			throw new TablaException(e);
		}
	}

	public Collection<RegistroTablaVO> buscarRegistroTabla(RegistroTablaVO registroTablaVO)
	throws TablaException {
		Collection<RegistroTablaVO> registrosVO = null;
		try {
			TablaBInterface interfaz = null;
			interfaz = (TablaBInterface) getLocalBean("java:comp/env/ejb/TablaFacade");
			registrosVO = interfaz.buscarRegistroTabla(registroTablaVO);
		} catch (ServiceLocationException e) {
			throw new TablaException(e);
		}
		return registrosVO;
	}
}
