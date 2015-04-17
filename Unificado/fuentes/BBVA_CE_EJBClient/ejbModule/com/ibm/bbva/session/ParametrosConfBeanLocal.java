package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.ParametrosConf;

public interface ParametrosConfBeanLocal {
	
	public List<ParametrosConf> buscarTodos();
	public List<ParametrosConf> buscarPorAplicativo(int codigoAplicativo);
	public ParametrosConf buscarPorVariable(int codigoAplicativo, String nombreVariable);
}
