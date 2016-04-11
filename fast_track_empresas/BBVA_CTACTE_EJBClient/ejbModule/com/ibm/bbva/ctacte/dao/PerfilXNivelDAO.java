package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Perfil;
import com.ibm.bbva.ctacte.bean.PerfilXNivel;
import com.ibm.bbva.ctacte.bean.PerfilXNivelPK;
import com.ibm.bbva.ctacte.exepciones.ParametroIlegalException;

public interface PerfilXNivelDAO extends IGenericDAO<PerfilXNivel, PerfilXNivelPK> {
	
	public List<PerfilXNivel> search(PerfilXNivel perfilXNivel);
	public void actualizarPerfilEmpleados(PerfilXNivel perfilXNivel, boolean eliminado) throws ParametroIlegalException;
	public Perfil obtenerPerfilAsignado(String codigoEmpleado, String codcargo, String codigoOficina);

}
