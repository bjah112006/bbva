package com.ibm.bbva.ctacte.business.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.CasoNegocio;
import com.ibm.bbva.ctacte.bean.Documento;
import com.ibm.bbva.ctacte.bean.GuiaDocument;
import com.ibm.bbva.ctacte.bean.GuiaDocumentId;
import com.ibm.bbva.ctacte.bean.Operacion;
import com.ibm.bbva.ctacte.business.DocumentoExpBusiness;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.DocumentoDAO;
import com.ibm.bbva.ctacte.dao.GuiaDocumentDAO;

/**
 * Session Bean implementation class DocumentoExpBusinessImpl
 */
@Stateless
@Local(DocumentoExpBusiness.class)
public class DocumentoExpBusinessImpl implements DocumentoExpBusiness {
	
	private static final Logger LOG = LoggerFactory.getLogger(DocumentoExpBusinessImpl.class);
	
	@EJB
	private GuiaDocumentDAO gddao;
	@EJB
	private DocumentoDAO dodao;

    /**
     * Default constructor. 
     */
    public DocumentoExpBusinessImpl() {
    }

	@Override
	public List<GuiaDocument> obtenerDocumentosExp(String codProd,
			String codTipoPj, Operacion operacion, CasoNegocio casoNegocio) {
		LOG.info("obtenerDocumentosExp (String codProd, String codTipoPj, Operacion operacion)");
		
		LOG.info("tipoPJ : {} - idOperacion : {}", codTipoPj, operacion);
		List<GuiaDocument> listaGDReadOnly = gddao.findByTipoPJOperacionCasoNegocio(codTipoPj, operacion.getId(), casoNegocio.getId());
		List<GuiaDocument> listaGD = new ArrayList<GuiaDocument>();
		listaGD.addAll(listaGDReadOnly);
		LOG.info("Items guia documentaria : {}", listaGD.size());
		
		if (ConstantesBusiness.CODIGO_NUEVO_BASTANTEO.equals(operacion.getCodigoOperacion())) {
			for (GuiaDocument gd : listaGD) {
				if (ConstantesBusiness.FLAG_OBLIGATORIO.equals(gd.getFlagObligatorio())) {
					gd.setFlagReqEscaneo(ConstantesBusiness.FLAG_REQUIERE_ESCANEO);
				}
			}
		}
		LOG.info("Producto --> {}", codProd);
		if (ConstantesBusiness.CODIGO_TIPO_CUENTA_PLAZO.equals(codProd)) {
			Documento documento = dodao.findByCodigo(ConstantesBusiness.CODIGO_DOCUMENTO_VOUCHER_PAGO_COMISION_BASTANTEO);
			GuiaDocument gd = new GuiaDocument ();
			GuiaDocumentId gdID = new GuiaDocumentId ();
			gdID.setCodTipoPj(codTipoPj);
			gdID.setIdDocumentoFk(documento.getId());
			gdID.setIdOperacionFk(operacion.getId());
			gdID.setIdCasoNegocioFk(casoNegocio.getId());
			gd.setId(gdID);
			gd.setDocumento(documento);
			gd.setOperacion(operacion);
			gd.setCasoNegocio(casoNegocio);
			gd.setFlagReqEscaneo(ConstantesBusiness.FLAG_REQUIERE_ESCANEO);
			gd.setFlagObligatorio(ConstantesBusiness.FLAG_OBLIGATORIO);
			listaGD.add(gd);
		}
		return listaGD;
	}

	@Override
	public List<GuiaDocument> obtenerDocumentosExp(String codTipoPj,
			Operacion operacion, CasoNegocio casoNegocio) {
//		LOG.info("obtenerDocumentosExp (String codProd, String codTipoPj, Operacion operacion)");
//		
//		LOG.info("tipoPJ : {} - idOperacion : {}", codTipoPj, operacion);
		List<GuiaDocument> listaGD = obtenerDocumentosExp(null, codTipoPj, operacion, casoNegocio);
//		LOG.info("Items guia documentaria : {}", listaGD.size());
//		for (GuiaDocument gd : listaGD) {
//			long lngDias=0;
//			long lngPlazoDias = 0;
//			long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
//			lngDias = (gd. - dtFechaUltimoBastanteo.getTime()) / MILLSECS_PER_DAY;
////			if (ConstantesBusiness.FLAG_OBLIGATORIO.equals(gd.getFlagObligatorio())
////					|| gd.getDocumento().getFlagVigencia()) {
//				gd.setFlagReqEscaneo(ConstantesBusiness.FLAG_REQUIERE_ESCANEO);
////			}
//		}
		return listaGD;
	}

}
