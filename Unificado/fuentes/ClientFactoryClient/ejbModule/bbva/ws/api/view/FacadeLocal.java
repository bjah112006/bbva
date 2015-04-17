package bbva.ws.api.view;

import java.util.List;

import com.ibm.bbva.cm.service.impl.Documento;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.service.business.client.DocumentoCM;
import com.ibm.bbva.service.business.client.EmpleadoDTO;
import com.ibm.bbva.service.business.client.ExpedienteDTO;
import com.ibm.bbva.service.business.client.PerfilDTO;

public interface FacadeLocal {

	String Content_delete(Documento documento);

	void Content_deleteAll(List<Documento> documentos);

	Documento Content_find(Documento documento);

	Documento Content_findAsImage(Documento documento, String mimeType);

	String Content_insert(Documento documento);

	String Content_insertAll(List<Documento> documentos);

	String Content_update(Documento documento);

	String Content_actualizarTipoDoc(int id, String tipoDoc);

	public boolean ServiceIBMBBVA_delegacionRiesgosWS(Integer idTipoCategoria,
			Long idExpediente);

	public DocumentoCM ServiceIBMBBVA_consultarDocumentoExpediente(
			Long idExpediente, String codigoTipoDoc);

	public boolean ServiceIBMBBVA_actualizarDocumentoExpTC(
			DocumentoCM objDocumentoCM);

	public EmpleadoDTO ServiceIBMBBVA_distribucionCarga(Long idTerritorio,
			Long idExpediente);

	public boolean ServiceIBMBBVA_pautasClasificacionMemoria(Long idTipoOferta, Integer idEstadoCivilTitular, Long idPersonaTitular, Long idPersonaConyuge, Double sbsTitular, Double sbsConyuge);

	public boolean ServiceIBMBBVA_retraerTareas(Long idAccion, Long salida, Long llegada);

	public Documento obtenerDocumentoCM(DocumentoExpTc documentoExp);
	
	public String eliminarDocumentoCM(DocumentoExpTc documentoExp);

	PerfilDTO ServiceIBMBBVA_lineaMaximaMemoria(ExpedienteDTO objExpedienteDTO);

	boolean ServiceIBMBBVA_delegacionOficinaMemoriaWS(
			ExpedienteDTO objExpedienteDTO);

	public List<Documento> obtenerListaDocumentoCM(DocumentoExpTc documentoExp);
}
