package com.ibm.bbva.session;

import java.util.Date;
import java.util.List;
import com.ibm.bbva.entities.TipoCambioCE;

/**
 * @author rAngulo
 */

public interface TipoCambioCEBeanLocal {

	public TipoCambioCE create(TipoCambioCE  tipoCambioCE);
	
	public TipoCambioCE buscarPorFecha(Date fecha);
	
	public List<TipoCambioCE> buscarTodos();	
	
	public TipoCambioCE buscarTop();
}
