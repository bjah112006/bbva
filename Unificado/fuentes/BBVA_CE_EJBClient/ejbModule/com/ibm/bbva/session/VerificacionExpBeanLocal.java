package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.VerificacionExp;

public interface VerificacionExpBeanLocal {
	public VerificacionExp buscarPorId(long id);
	
	public List<VerificacionExp> buscarPorExpediente(long id);
	
	public List<VerificacionExp> buscarPorExpTipoVer(long idExp, long idTipoVer);

    public VerificacionExp create(VerificacionExp verificacionExp);
	
	public void edit(VerificacionExp verificacionExp);	
	
	public VerificacionExp reporteHistPlano(long idExp, long idTipoVer);
}
