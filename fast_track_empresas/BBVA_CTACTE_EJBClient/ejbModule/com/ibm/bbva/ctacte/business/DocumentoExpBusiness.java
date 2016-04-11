package com.ibm.bbva.ctacte.business;

import java.util.List;

import com.ibm.bbva.ctacte.bean.CasoNegocio;
import com.ibm.bbva.ctacte.bean.GuiaDocument;
import com.ibm.bbva.ctacte.bean.Operacion;

public interface DocumentoExpBusiness {
	
	public List<GuiaDocument> obtenerDocumentosExp (String codProd, String codTipoPj, Operacion operacion, CasoNegocio casoNegocio);
	public List<GuiaDocument> obtenerDocumentosExp (String codTipoPj, Operacion operacion, CasoNegocio casoNegocio);

}
