package com.ibm.bbva.ctacte.wrapper;

import java.io.Serializable;
import java.util.Date;

import javax.faces.event.ValueChangeEvent;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.ctacte.bean.Documento;
import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.EstadoExpediente;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.GuiaDocument;
import com.ibm.bbva.ctacte.bean.Motivo;
import com.ibm.bbva.ctacte.cm.ConsultaContentManager;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.VerificaDocumentacionDigitalizadaMB;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.util.EJBLocator;
import com.ibm.bbva.ctacte.util.Util;

public class DocumentoExpWrapper1 implements Serializable {

	private static final long serialVersionUID = -5076425262742822889L;

	private static final Logger LOG = LoggerFactory.getLogger(DocumentoExpWrapper1.class);	
	
	private DocumentoExp documentoExp;
	private VerificaDocumentacionDigitalizadaMB verificaDocDigitalizada;
	private boolean motivoRechazoSeleccionado;
	private boolean disReqEscaneo;
	private int tipoVisor;
	private int idTarea;
	private String rutaCM;
	private com.ibm.bbva.cm.service.Documento[] arrayDocumentoCM;
	private com.ibm.bbva.cm.service.Documento documentoCM;
	private String idMotivo = ConstantesAdmin.CODIGO_CAMPO_VACIO;
	private Integer nroPagina; // El número de página del último documento histórico
	private boolean disEliminarDoc = false;
	
	public DocumentoExpWrapper1(GuiaDocument guiaDocument, String codigoCentral, String operacion) {
		LOG.info("DocumentoExpWrapper1(GuiaDocument guiaDocument, String codigoCentral, String operacion)");
		documentoExp = Util.crearDocumentoExp(guiaDocument);
		EstadoExpediente estadoExpediente = ((Expediente)Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION)).getEstado();
		iniciar (codigoCentral, operacion, estadoExpediente, true);
	}
	
	public DocumentoExpWrapper1(DocumentoExp documentoExp, String codigoCentral, String operacion, int idTarea) {
		LOG.info("DocumentoExpWrapper1(DocumentoExp documentoExp, String codigoCentral, String operacion, int idTarea");
		this.idTarea = idTarea;
		this.documentoExp = documentoExp;
		iniciar (codigoCentral, operacion, documentoExp.getExpediente().getEstado(), false);
	}
	
	private void iniciar (String codigoCentral, String operacion, EstadoExpediente estadoExpediente, boolean reutilizarDocs) {
		LOG.info("iniciar (String codigoCentral, String operacion, EstadoExpediente estadoExpediente): {}, {}, {}", 
				new Object[]{codigoCentral, operacion, estadoExpediente});
		Documento documento = documentoExp.getDocumento();
		ConsultaContentManager consultaCM = new ConsultaContentManager ();
		try {
			String codDoc = documento.getCodigoDocumento();
			LOG.info("documento.getCodigoDocumento(): {}", codDoc);
			this.documentoCM = null;
			if (!reutilizarDocs && ConstantesBusiness.FLAG_ESCANEADO.equals(documentoExp.getFlagEscaneado())) {
				LOG.info("Busco documento tipo "+codDoc+" con idCm="+documentoExp.getIdCm());
				//nunca devuelve null para poder reconocer los documentos que no llegaron a subir al content
				this.documentoCM = consultaCM.CM_ObtenerDocumentItemType(documentoExp.getIdCm()); 
			} else if (!reutilizarDocs) {
				this.documentoCM = null;
				LOG.info("No hay documento escaneado");
//			} else if (reutilizarDocs) {
//				LOG.info("Busco último documento histórico (tipo="+codDoc+",codCentral="+codigoCentral+")");
//				this.documentoCM = consultaCM.CM_DocumentoxCliente(codDoc, codigoCentral);
			}
			LOG.info("existe documento : {}", documento);
			// TODO revisar funcionalidad de reutilizar documentos
//			if (reutilizarDocs && documentoCM != null) {
//				boolean esPreRegistro = false;
//				boolean estaEnBDyCM = false;
//				if (estadoExpediente != null) {
//					esPreRegistro = ConstantesBusiness.ID_ESTADO_EXPEDIENTE_PREREGISTRO==estadoExpediente.getId();
//					DocumentoExpDAO documentoExpDAO = EJBLocator.getDocumentoExpDAO();
//					DocumentoExp documentoExp = documentoExpDAO.findByCodDocExp(codDoc, 
//							((Expediente)Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION)).getId());
//					estaEnBDyCM = ConstantesBusiness.FLAG_ESTA_EN_CONTENT.equals(documentoExp.getFlagCm());
//				}
//				LOG.info("esPreRegistro : {}, estaEnBDyCM : {}", esPreRegistro, estaEnBDyCM);
//				if (ConstantesBusiness.CODIGO_MODIFICATORIA_BASTANTEO.equals(operacion) || 
//						ConstantesBusiness.CODIGO_CAMBIO_FIRMAS.equals(operacion) ||
//						ConstantesBusiness.CODIGO_REVOCATORIA_ESPECIFICA.equals(operacion) ||
//						ConstantesBusiness.CODIGO_SUBSANACION_BASTANTEO.equals(operacion)) {
//					if ((ConstantesBusiness.FLAG_OBLIGATORIO.equals(documentoExp.getFlagObligatorio()) &&
//							!ConstantesBusiness.FLAG_REQUIERE_ESCANEO.equals(documentoExp.getFlagReqEscaneo())) ||
//							(esPreRegistro && estaEnBDyCM)){
//						agregarDocumento(documento);
//					}
//				} else if (ConstantesBusiness.CODIGO_NUEVO_BASTANTEO.equals(operacion)) {
//					if (esPreRegistro && estaEnBDyCM){
//						agregarDocumento(documento);
//					}
//				}
//			}

		} catch (Exception e1) {
			LOG.error("No se pudo obtener el archivo actual : "+documento.getCodigoDocumento(), e1);
		}
		try {
			arrayDocumentoCM = consultaCM.CM_historialDocumentoxCliente(
					documentoExp.getDocumento().getCodigoDocumento(),
					codigoCentral,
					ConstantesBusiness.NUM_DOC_HISTORICOS_RECIBIDOS_CM,
					documentoExp);
			LOG.info("cantidad documentos historicos: {}", arrayDocumentoCM==null ? 0 : arrayDocumentoCM.length);
			
			if (documento.getFlagNroPagina().equals(Constantes.CHECK_SELECCIONADO) && arrayDocumentoCM != null && arrayDocumentoCM.length > 0) {
				DocumentoExpDAO documentoExpDAO = EJBLocator.getDocumentoExpDAO();
				DocumentoExp ultimoDocumentoHist = documentoExpDAO.findByIdCm(arrayDocumentoCM[0].getId());
				if (ultimoDocumentoHist != null) {
					nroPagina = ultimoDocumentoHist.getNroPagina();
				} else {
					nroPagina = null;
				}
			}
		} catch (Exception e) {
			arrayDocumentoCM = new com.ibm.bbva.cm.service.Documento[0];
			LOG.error("Error obteniendo archivos historicos", e);
		}
		/*if (ConstantesBusiness.CODIGO_SUBSANACION_BASTANTEO.equals(operacion)) {
			Date fecVig = documentoExp.getFechaVigencia();
			Date fecAct = new Date ();
			if (ConstantesBusiness.FLAG_VIGENCIA_DOCUMENTO.equals(documento.getFlagVigencia()) &&
					fecVig != null && fecVig.before(fecAct)) {
				disReqEscaneo = true;
				documentoExp.setFlagReqEscaneo(ConstantesBusiness.FLAG_REQUIERE_ESCANEO);
			} else {
				disReqEscaneo = false;
			}
		} else {*/
			disReqEscaneo = true;
		//}
		actualizarVisor ();
	}

	private void agregarDocumento(Documento documento) {
		LOG.info("Documento CM encontrado: {}", documentoCM.getNombreArchivo());
		documentoExp.setFlagCm(ConstantesBusiness.FLAG_ESTA_EN_CONTENT);
		documentoExp.setIdCm(documentoCM.getId());
		documentoExp.setFlagEscaneado(ConstantesBusiness.FLAG_ESCANEADO);
		documentoExp.setFechaEscaneo(documentoCM.getFechaCreacion().toGregorianCalendar().getTime());
		documentoExp.setNombreArchivo(documentoCM.getNombreArchivo());
		documentoExp.setDocPesoPromKB(documento.getPesoPromHojaKB());
		Integer diasVigencia = documento.getVigenciaDias();
		if (ConstantesBusiness.FLAG_VIGENCIA_DOCUMENTO.equals(documento.getFlagVigencia())
				&& diasVigencia != null) {
			DateTime fechaHoy = new DateTime (new Date());
			Date fechaVigencia = fechaHoy.plusDays(diasVigencia).toDate();
			documentoExp.setFechaVigencia(fechaVigencia);
		}
		LOG.info("Documento Exp encontrado: {}", documentoExp.getNombreArchivo());
	}
	
	public void actualizarVisor () {
		LOG.info("actualizarVisor ()");
		Documento documento = documentoExp.getDocumento();
		boolean esTipoVisorAvanzado = ConstantesBusiness.TIPO_VISOR_AVANZADO.equals(documento.getTipoVisor());
		boolean estaEscaneado = ConstantesBusiness.FLAG_ESCANEADO.equals(documentoExp.getFlagEscaneado());
		boolean estaEnContent = documentoCM != null;
		LOG.info("estaEscaneado : "+estaEscaneado);
		LOG.info("estaEnContent : "+estaEnContent);
		LOG.info("esTipoVisorAvanzado : "+esTipoVisorAvanzado);
		if (estaEnContent && esTipoVisorAvanzado) {
			if (idTarea==ConstantesBusiness.ID_TAREA_VERIFICAR_REALIZAR_BASTANTEO ||
					idTarea == ConstantesBusiness.ID_TAREA_REVISAR_APROBAR_BASTANTEO) {
				tipoVisor = 2;
				rutaCM = ConstantesBusiness.RUTA_CM_VISOR;
			} else {
				tipoVisor = 1;
				rutaCM = estaEnContent ? documentoCM.getUrlContent() : "";
			}
		} else if (estaEnContent && !esTipoVisorAvanzado) {
			tipoVisor = 1;
			rutaCM = estaEnContent ? documentoCM.getUrlContent() : "";
		} else if (estaEscaneado) {
			tipoVisor = 3;
		}		
		LOG.info("rutaCM:"+rutaCM);
	}
	
	public void seleccionarMotivo (ValueChangeEvent ae) {
		LOG.info("seleccionarMotivo (ValueChangeEvent ae)");
		String strValue = (String)ae.getNewValue();
		LOG.info("strValue-->"+strValue);
		
		//Actualizando el valor idMotivo en DocExpediente
		strValue = (strValue==null?"-1":strValue);
		setIdMotivo(strValue);
		Motivo motivo = new Motivo();
		motivo.setId(Integer.valueOf(strValue));
		getDocumentoExp().setMotivo(motivo);
		//Si se seleccionó cualquier valor que no sea campo vacío
		if (!strValue.equalsIgnoreCase(ConstantesAdmin.CODIGO_CAMPO_VACIO))
			setMotivoRechazoSeleccionado(true);
		else
			setMotivoRechazoSeleccionado(false);
		
		//Recorre todos los checks buscando si existe uno marcado en donde NO tiene motivo de Rechazo seleccionado
		verificaDocDigitalizada.verificarSeleccionMotivoRechazo();
	}

	public DocumentoExp getDocumentoExp() {
		return documentoExp;
	}

	public void setDocumentoExp(DocumentoExp documentoExp) {
		this.documentoExp = documentoExp;
	}

	public boolean isReqEscaneo() {
		return ConstantesBusiness.FLAG_REQUIERE_ESCANEO.equals(documentoExp.getFlagReqEscaneo());
	}

	public void setReqEscaneo(boolean reqEscaneo) {
		if (reqEscaneo) {
			documentoExp.setFlagReqEscaneo(ConstantesBusiness.FLAG_REQUIERE_ESCANEO);
		} else {
			documentoExp.setFlagReqEscaneo(ConstantesBusiness.FLAG_NO_REQUIERE_ESCANEO);
		}
	}
	
	public boolean isObligatorio() {
		return ConstantesBusiness.FLAG_OBLIGATORIO.equals(documentoExp.getFlagObligatorio());
	}
	
	public void setObligatorio(boolean obligatorio) {
		if (obligatorio) {
			documentoExp.setFlagObligatorio(ConstantesBusiness.FLAG_OBLIGATORIO);
		} else {
			documentoExp.setFlagObligatorio(ConstantesBusiness.FLAG_NO_OBLIGATORIO);
		}
	}

	public String getRutaCM() {
		return rutaCM;
	}

	public void setRutaCM(String rutaCM) {
		this.rutaCM = rutaCM;
	}

	public void setIdMotivo(String idMotivo) {
		this.idMotivo = idMotivo;
	}

	public String getIdMotivo() {
		if (getDocumentoExp().getMotivo()!=null)
			return getDocumentoExp().getMotivo().getId()+"";
		return idMotivo;
	}

	public void setMotivoRechazoSeleccionado(boolean motivoRechazoSeleccionado) {
		this.motivoRechazoSeleccionado = motivoRechazoSeleccionado;
	}

	public boolean isMotivoRechazoSeleccionado() {
		return motivoRechazoSeleccionado;
	}

	public VerificaDocumentacionDigitalizadaMB getVerificaDocDigitalizada() {
		return verificaDocDigitalizada;
	}

	public void setVerificaDocDigitalizada(
			VerificaDocumentacionDigitalizadaMB verificaDocDigitalizada) {
		this.verificaDocDigitalizada = verificaDocDigitalizada;
	}

	public void setArrayDocumentoCM(com.ibm.bbva.cm.service.Documento[] arrayDocumentoCM) {
		this.arrayDocumentoCM = arrayDocumentoCM;
	}

	public com.ibm.bbva.cm.service.Documento[] getArrayDocumentoCM() {
		return arrayDocumentoCM;
	}

	public void setDocumentoCM(com.ibm.bbva.cm.service.Documento documentoCM) {
		this.documentoCM = documentoCM;
	}

	public com.ibm.bbva.cm.service.Documento getDocumentoCM() {
		return documentoCM;
	}

	public void setDisReqEscaneo(boolean disReqEscaneo) {
		this.disReqEscaneo = disReqEscaneo;
	}

	public boolean isDisReqEscaneo() {
		return disReqEscaneo;
	}

	public void setTipoVisor(int tipoVisor) {
		this.tipoVisor = tipoVisor;
	}

	public int getTipoVisor() {
		return tipoVisor;
	}

	public Integer getNroPagina() {
		return nroPagina;
	}

	public void setNroPagina(Integer nroPagina) {
		this.nroPagina = nroPagina;
	}

	public boolean isDisEliminarDoc() {
		return disEliminarDoc;
	}

	public void setDisEliminarDoc(boolean disEliminarDoc) {
		this.disEliminarDoc = disEliminarDoc;
	}
	
}
