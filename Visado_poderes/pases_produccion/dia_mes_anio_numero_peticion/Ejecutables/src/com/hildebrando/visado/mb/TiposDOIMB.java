package com.hildebrando.visado.mb;

import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.bbva.common.util.ConstantesVisado;
import com.bbva.persistencia.generica.util.Utilitarios;
import com.hildebrando.visado.modelo.TiivsMultitabla;
import com.hildebrando.visado.modelo.TiivsMultitablaId;
import com.hildebrando.visado.service.TiposDoiService;

@ManagedBean(name = "tiposDOIMB")
@SessionScoped
public class TiposDOIMB {
	public static Logger logger = Logger.getLogger(DocumentoMB.class);
	private TiposDoiService tiposDoiService;
	private TiivsMultitabla documento;
	private List<TiivsMultitabla> documentos;
	private List<TiivsMultitabla> documentoEditar;
	//private boolean bValor4;
	private boolean bMsgActualizar;
	private boolean bValidacion;

	public TiposDOIMB() {
		documento = new TiivsMultitabla();
		TiivsMultitablaId documentoId = new TiivsMultitablaId();
		documento.setId(documentoId);
		//bValor4 = false;
		bMsgActualizar = false;
		bValidacion = false;
		tiposDoiService = new TiposDoiService();
		listarDocumentos();
		obtenerMaximo();

	}
	
	public void obtenerMaximo() {
		logger.info("DocumentoMB : obtenerMaximo");
		String secuencial = null;
		int parseSecuencial = 0;
		try {

			secuencial = tiposDoiService.obtenerMaximo();
			parseSecuencial = Integer.parseInt(secuencial) + 1;
			secuencial = String.valueOf(parseSecuencial);

			if (secuencial.length() == 1) {
				secuencial = "000" + secuencial;
			} else {
				if (secuencial.length() == 2) {
					secuencial = "00" + secuencial;
				} else {
					if (secuencial.length() == 3) {
						secuencial = "0" + secuencial;
					} else {
						secuencial = secuencial;
					}
				}

			}
			logger.info("DocumentoMB : secuencial" + " " + secuencial);
			documento.getId().setCodElem(secuencial);
		} catch (Exception e) {
			logger.error("DocumentoMB :" , e);
		}
	}

	public void registrar() {
		logger.info("DocumentoMB : Registrar");
		if(documento.getValor1().isEmpty()){
			bValidacion = false;
		}else{
			bValidacion=true;
		}
		if(isbValidacion()==true){
			try {
				documento.getId().setCodMult(
						ConstantesVisado.CODIGO_MULTITABLA_DOCUMENTO);
				/*if (isbValor4() == true) {
					documento.setValor4(ConstantesVisado.VALOR4_OBLIGATORIO_SI);
				} else {
					documento.setValor4(ConstantesVisado.VALOR4_OBLIGATORIO_NO);
				}*/
				tiposDoiService.registrar(documento);
				if (isbMsgActualizar() == true) {
					Utilitarios.mensajeInfo("NIVEL", "Se actualiz� correctamente");
				} else {
					Utilitarios.mensajeInfo("NIVEL", "Se registr� correctamente");
				}
				documento = new TiivsMultitabla();
				
				TiivsMultitablaId documentoId = new TiivsMultitablaId();
				documento.setId(documentoId);
				obtenerMaximo();
				bMsgActualizar = false;
				bValidacion = false;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("DocumentoMB :" + e.getLocalizedMessage());
				Utilitarios
						.mensajeError("Error", "Error al registrar el Documento");
			}
		}else{
			Utilitarios
			.mensajeError("Error", "El campo descripci�n es requerido");
		}
		
	}

	public void listarDocumentos() {
		logger.info("DocumentoMB : listarDocumentos");
		try {
			documentos = tiposDoiService.listarDocumentos(/*tipoDocumento*/);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("DocumentoMB :" + e.getLocalizedMessage());
		}
	}
	
	
	public void limpiarCampos() {
		logger.info("DocumentoMB : limpiarCampos");
		documento = new TiivsMultitabla();
		TiivsMultitablaId documentoId = new TiivsMultitablaId();
		documento.setId(documentoId);
		obtenerMaximo();
		bMsgActualizar = false;
	}
	

	public void editarDocumento() {
		logger.info("DocumentoMB : listarDocumentos");
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		String codMultitabla;
		String codElemento;
		codMultitabla = params.get("codMultitabla");
		codElemento = params.get("codElemento");
		try {
			documentoEditar = tiposDoiService.editarDocumento(codMultitabla,
					codElemento);
			for (int i = 0; i < documentoEditar.size(); i++) {
				documento.setId(documentoEditar.get(i).getId());
				documento.setValor1(documentoEditar.get(i).getValor1());
				documento.setValor2(documentoEditar.get(i).getValor2());
				documento.setValor4(documentoEditar.get(i).getValor4());
				documento.setValor5(documentoEditar.get(i).getValor5());
			}
			/*if (documento.getValor4().equals(
					ConstantesVisado.VALOR4_OBLIGATORIO_SI)) {
				bValor4 = true;
			} else {
				bValor4 = false;
			}*/
			bMsgActualizar = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("DocumentoMB : listarDocumentos: "
					+ ex.getLocalizedMessage());
		}
	}

	public TiivsMultitabla getDocumento() {
		return documento;
	}

	public void setDocumento(TiivsMultitabla documento) {
		this.documento = documento;
	}

	

	public List<TiivsMultitabla> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(List<TiivsMultitabla> documentos) {
		this.documentos = documentos;
	}

	public List<TiivsMultitabla> getDocumentoEditar() {
		return documentoEditar;
	}

	public void setDocumentoEditar(List<TiivsMultitabla> documentoEditar) {
		this.documentoEditar = documentoEditar;
	}

	public boolean isbMsgActualizar() {
		return bMsgActualizar;
	}

	public void setbMsgActualizar(boolean bMsgActualizar) {
		this.bMsgActualizar = bMsgActualizar;
	}

	public boolean isbValidacion() {
		return bValidacion;
	}

	public void setbValidacion(boolean bValidacion) {
		this.bValidacion = bValidacion;
	}
	
	
}
