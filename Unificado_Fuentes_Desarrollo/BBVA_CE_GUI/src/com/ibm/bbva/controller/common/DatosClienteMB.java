package com.ibm.bbva.controller.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.form.RegistrarExpedienteMB;
import com.ibm.bbva.entities.CategoriaRenta;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.EstadoCivil;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.GrupoSegmento;
import com.ibm.bbva.entities.Persona;
import com.ibm.bbva.entities.Segmento;
import com.ibm.bbva.entities.TipoCliente;
import com.ibm.bbva.entities.TipoDoi;
import com.ibm.bbva.session.CategoriaRentaBeanLocal;
import com.ibm.bbva.session.ClienteNaturalBeanLocal;
import com.ibm.bbva.session.EstadoCivilBeanLocal;
import com.ibm.bbva.session.SegmentoBeanLocal;
import com.ibm.bbva.session.TipoClienteBeanLocal;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "datosCliente")
@RequestScoped
public class DatosClienteMB extends AbstractMBean {

	@EJB
	private EstadoCivilBeanLocal estadoCivilbean;
	@EJB
	private CategoriaRentaBeanLocal categoriaRentabean;		
	@EJB
	private SegmentoBeanLocal segmentobean;
	@EJB
	private TipoClienteBeanLocal tipoClientebean;	
	@EJB
	private ClienteNaturalBeanLocal clienteNaturalbean;

	private TipoCliente tipoCliente;	
	private Segmento segmento;
	private ClienteNatural clienteNatural;
	private List<SelectItem> listaEstadoCivil;
	public String idEstadoCivil;
	private List<SelectItem> listaCategoriaRenta;
	public List<String> caracAdicionales;
	private List<String> categoriasRenta;	
	private String codigoTipoOferta;
	
	private static final Logger LOG = LoggerFactory.getLogger(DatosClienteMB.class);
	private boolean itemDisabledcaractPep = true;
	private boolean itemDisabledcaractAval = true;
	public boolean renderedNombre=false;
	public boolean renderedRazonSocial=false;
	public boolean renderedApellidos=false;

	public DatosClienteMB() {		
	}
	
	@PostConstruct
    public void init() {
		tipoCliente = new TipoCliente();
		segmento = new Segmento();
		segmento.setGrupoSegmento(new GrupoSegmento());
		
		listaEstadoCivil = Util.crearItems(estadoCivilbean.buscarTodos(), true, "id", "descripcion");				
		listaCategoriaRenta = Util.crearItems(categoriaRentabean.buscarTodos(), "id", "descripcion");

		recortarDatosLista();
		
		Collections.sort(listaCategoriaRenta, new Util.SecletItemComparator(true));  
		
		caracAdicionales = new ArrayList<String> ();
		categoriasRenta = new ArrayList<String> ();
		
		Expediente expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		if (expediente.getClienteNatural() != null && expediente.getClienteNatural().getId() > 0) {
			
			ClienteNatural filtro = new ClienteNatural();
			filtro = clienteNaturalbean.buscarPorId(expediente.getClienteNatural().getId());
			
			iniciarDatos (filtro);
		} else {
			clienteNatural = new ClienteNatural();
			clienteNatural.setTipoDoi(new TipoDoi());
			clienteNatural.setCodCliente(Constantes.CODIGO_CLIENTE_NUEVO);
		}
	}
	
	public List<SelectItem> getListaEstadoCivil() {
		return listaEstadoCivil;
	}

	public void setListaEstadoCivil(List<SelectItem> listaEstadoCivil) {
		this.listaEstadoCivil = listaEstadoCivil;
	}

	public TipoCliente getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(TipoCliente tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public ClienteNatural getClienteNatural() {
		return clienteNatural;
	}

	public void setClienteNatural(ClienteNatural clienteNatural) {
		this.clienteNatural = clienteNatural;
	}

	public Segmento getSegmento() {
		return segmento;
	}

	public void setSegmento(Segmento segmento) {
		this.segmento = segmento;
	}

	public List<SelectItem> getListaCategoriaRenta() {
		return listaCategoriaRenta;
	}

	public void setListaCategoriaRenta(List<SelectItem> listaCategoriaRenta) {
		this.listaCategoriaRenta = listaCategoriaRenta;
	}

	public List<String> getCaracAdicionales() {
		return caracAdicionales;
	}

	public void setCaracAdicionales(List<String> caracAdicionales) {
			this.caracAdicionales = caracAdicionales;
	}

	public List<String> getCategoriasRenta() {
		return categoriasRenta;
	}

	public void setCategoriasRenta(List<String> categoriasRenta) {
		this.categoriasRenta = categoriasRenta;
	}
	
	public String getIdEstadoCivil() {
		return idEstadoCivil;
	}

	public void setIdEstadoCivil(String idEstadoCivil) {
		this.idEstadoCivil = idEstadoCivil;
	}
	
	public void iniciarDatos (ClienteNatural clienteNatural) {
		this.clienteNatural = clienteNatural;
		
		Integer idEstCiv = clienteNatural.getEstadoCivil()!=null ? Long.valueOf(clienteNatural.getEstadoCivil().getId()).intValue() : null;
		idEstadoCivil = idEstCiv==null ? null : String.valueOf(idEstCiv);
		
		if (Constantes.CHECK_SELECCIONADO.equals(clienteNatural.getPerExpPub())) {
			caracAdicionales.add("1");
		}
		if (Constantes.CHECK_SELECCIONADO.equals(clienteNatural.getPagoHab())) {
			caracAdicionales.add("2");
		}
		if (Constantes.CHECK_SELECCIONADO.equals(clienteNatural.getAval())) {
			caracAdicionales.add("3");
		}
		if (Constantes.CHECK_SELECCIONADO.equals(clienteNatural.getSubrog())) {
			caracAdicionales.add("4");
		}
		
		//fix2 erika abregu
		if (Constantes.CHECK_SELECCIONADO.equals(clienteNatural.getMonocuota())) {
			caracAdicionales.add("5");
		}
		
		if(clienteNatural!=null && clienteNatural.getSegmento()!=null && clienteNatural.getSegmento().getId() > 0){			
			segmento = segmentobean.buscarPorId(clienteNatural.getSegmento().getId());
		}else{
			for(Segmento vo : segmentobean.buscarTodos()){
				if(vo.getDescripcion().equals(Constantes.SEGMENTO_NINGUNO_DESCRIPCION)){
					segmento = vo;
				}
			}
		}		
		
		if (clienteNatural.getTipoCliente().getId() > 0 ) {
		    tipoCliente = tipoClientebean.buscarPorId(clienteNatural.getTipoCliente().getId());
		}
		
		//Long idCliNat = clienteNatural.getId();
		if (clienteNatural.getCategoriasRenta() != null) {
			for (CategoriaRenta ccvo : clienteNatural.getCategoriasRenta()) {
				categoriasRenta.add(String.valueOf(ccvo.getId()));
			}
		}
	}

	public boolean esValido () {
		String jspPrinc = getNombreJSPPrincipal();
		String formulario = "";
		if (jspPrinc.equals("formRegistrarExpediente")) {
			formulario = "frmRegistrarExpediente";
		}
		boolean existeError = false;
		// validar fecha vencimiento
		Date fechaVenc = clienteNatural.getFecVenDoi();
		Date fechaActual = new Date ();
		if (fechaVenc == null) {
			addMessageError(formulario + ":fecVenDoi", 
					"com.ibm.bbva.common.datosClienteNuevo.msg.fecVenDoi.vacio");
			existeError = true;
		} else if (!fechaVenc.after(fechaActual)) {
			addMessageError(formulario + ":fecVenDoi", 
					"com.ibm.bbva.common.datosClienteNuevo.msg.fecVenDoi.incorrecto");
			existeError = true;
		}
		// validar estado civil
		if (idEstadoCivil==null || idEstadoCivil.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			addMessageError(formulario + ":estadoCivil", 
					"com.ibm.bbva.common.datosClienteNuevo.msg.estadoCivil");
			existeError = true;
		}
		
		// validar categorias renta
		if (categoriasRenta.isEmpty()) {
			addMessageError(formulario + ":categoriasRenta", 
					"com.ibm.bbva.common.datosClienteNuevo.msg.categoriasRenta");
			existeError = true;
		}
		
		//if (this.getCodigoTipoOferta()!=null && this.getCodigoTipoOferta().equals(Constantes.CODIGO_OFERTA_APROBADO)) {
				String correo = getClienteNatural().getCorreo();
				String celular = getClienteNatural().getCelular();
				boolean correoVacio=false;
				boolean celularVacio=false;
				// validar correo
				if ((correo.trim().equals("") || correo.trim().length()<1 || correo.trim().length()>50) && 
						(celular.trim().equals("") || celular.trim().length()<1 || celular.trim().length()>14)) {
				//if (correo.isEmpty() && celular.isEmpty()) {
					addMessageError(formulario + ":dcnCelularN", 
							"com.ibm.bbva.common.datosClienteNuevo.msg.celularCorreo");
					existeError = true;
				}else{
					if (correo.isEmpty() && !celular.isEmpty()){
						correoVacio=true;
					}else if (celular.isEmpty() && !correo.isEmpty()){
						celularVacio=true;
					}
		
					if (!correoVacio){
						boolean validoMail=Util.validarEmail(correo);
						if (!validoMail){
							addMessageError(formulario + ":dcnCorreoN", 
									"com.ibm.bbva.common.datosClienteNuevo.msg.correoIncorrecto");
							existeError = true;
						}
					}
					
					
					// validar celular
					if (!celularVacio){
						if (celular.isEmpty()) {
							addMessageError(formulario + ":dcnCelularN", 
									"com.ibm.bbva.common.datosClienteNuevo.msg.celular");
							existeError = true;
						}
					}
						
				}
	  //  }
		return !existeError;
	}
	
	public void cambiarEstadoCivil(AjaxBehaviorEvent event) {
		FacesContext ctx = FacesContext.getCurrentInstance();  
		RegistrarExpedienteMB regExpMB = null;
		ProductoNuevoMB productoNuevo  = null;
		PanelDocumentosMB panelDocumento=null;
		
		regExpMB = (RegistrarExpedienteMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "registrarExpediente");
				
		productoNuevo = (ProductoNuevoMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "productoNuevo");
		
		panelDocumento = (PanelDocumentosMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");		
		
		/*Guia Documentaria*/
		panelDocumento.cargarDocumentosPanel(event);
		
		/*Datos Conyuge*/
		//DatosConyugeMB datosConyuge = (DatosConyugeMB)  
		//		 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "datosConyuge");
		
		/*Buscar Conyuge*/ //JBTA
		BuscarConyugeMB buscarConyuge = (BuscarConyugeMB) //JBTA
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarConyuge"); //JBTA
		
		Expediente expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		// esto solo debería funcionar para pre-registro
		if (expediente.getClienteNatural() != null) {
			if (getIdEstadoCivil() != null && !getIdEstadoCivil().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
				EstadoCivil estadoCivil = estadoCivilbean.buscarPorId(Long.parseLong(getIdEstadoCivil()));
				if (estadoCivil != null) {
					expediente.getClienteNatural().setEstadoCivil(estadoCivil);
				}
			}
		}
		addObjectSession(Constantes.EXPEDIENTE_SESION, expediente);
		
		LOG.info("Aki 1");
		if (!isCasado()) {
			regExpMB.visualizarPanelConyuge(false);
			regExpMB.visualizarDatosPanelConyuge(false);
		//	datosConyuge.setMostrarDatosConyuge(false);	
			LOG.info("no es casado");
			productoNuevo.setRenderedConyuge(true);	
			productoNuevo.getExpediente().getExpedienteTC().setSbsConyuge(0);
			productoNuevo.cambiarTipoOferta(null);
			regExpMB.setMostrarPanelConyuge(false);
		} else {
			LOG.info("casado con tipo oferta = "+productoNuevo.getCodTipoOferta());
			if (productoNuevo.getCodTipoOferta()!= null 
					&& productoNuevo.getCodTipoOferta().equals(Constantes.CODIGO_OFERTA_APROBADO)) {
				//datosConyuge.setMostrarDatosConyuge(true);
				regExpMB.visualizarPanelConyuge(true);
				//regExpMB.visualizarDatosPanelConyuge(true);
				regExpMB.setMostrarPanelConyuge(true);
				
				productoNuevo.setRenderedConyuge(false);
				productoNuevo.validaPautaClasificacion(getIdEstadoCivil());
				buscarConyuge.setTipoDOISeleccionado(Constantes.CODIGO_TIPO_DOI_DNI); //JBTA
				LOG.info("tipo oferta aprobado");
				
			}else {
				regExpMB.setMostrarPanelConyuge(false);
			//	datosConyuge.setMostrarDatosConyuge(false);	
				LOG.info("tipo oferta 	NO aprobado");
			}
		}
		
		if (isSoltero())
			productoNuevo.setRenderedSoltero(true);
		else
			productoNuevo.setRenderedSoltero(false);

	}


	public void cambiarCaracteristicas(AjaxBehaviorEvent event) {
		FacesContext ctx = FacesContext.getCurrentInstance();  
		PanelDocumentosMB panelDocumento = null;
		
		panelDocumento = (PanelDocumentosMB)  
 		        ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
		
		List<String> codigo = (List<String>) getCaracAdicionales();
		this.setCaracAdicionales(codigo);		
		
		/*Guia Documentaria*/
		panelDocumento.cargarDocumentosPanel(event);
	}
	
	public void cambiarCategorias(AjaxBehaviorEvent event) {
		FacesContext ctx = FacesContext.getCurrentInstance();  
		PanelDocumentosMB panelDocumento = null;
		
		panelDocumento = (PanelDocumentosMB)  
 		        ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");
				
		List<String> codigo = (List<String>) getCategoriasRenta();
		this.setCategoriasRenta(codigo);	
		
		/*Guia Documentaria*/
		panelDocumento.cargarDocumentosPanel(event);		
	}
	
	public boolean isCasado() {
		if (getIdEstadoCivil() == null
				|| getIdEstadoCivil().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)
				|| !(getIdEstadoCivil().equals(Constantes.ESTADO_CIVIL_CASADO.toString())
				|| getIdEstadoCivil().equals(Constantes.ESTADO_CIVIL_CONVIVIENTE.toString()))) {
			return false;
		}
		return true;
	}
	
	public boolean isSoltero() {
		if (!getIdEstadoCivil().equals(Constantes.ESTADO_CIVIL_SOLTERO)) {
			return false;
		}
		return true;
	}
	
	private String seleccion (String val) {
		return caracAdicionales.contains(val) ? 
				Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO;
	}
	
	public void copiarDatosClienteNatural (ClienteNatural clienteNaturalVO) {
		clienteNaturalVO.setTipoDoi(clienteNatural.getTipoDoi());
		clienteNaturalVO.setNumDoi(clienteNatural.getNumDoi());
		
		clienteNaturalVO.setPerExpPub(seleccion("1"));
		clienteNaturalVO.setPagoHab(seleccion("2"));
		clienteNaturalVO.setAval(seleccion("3"));
		clienteNaturalVO.setSubrog(seleccion("4"));
		
		clienteNaturalVO.setNombre(clienteNatural.getNombre());
		clienteNaturalVO.setApePat(clienteNatural.getApePat());
		clienteNaturalVO.setApeMat(clienteNatural.getApeMat());
		clienteNaturalVO.setFecVenDoi(clienteNatural.getFecVenDoi());	
		if (idEstadoCivil!=null && !idEstadoCivil.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			clienteNaturalVO.setEstadoCivil(new EstadoCivil());
			clienteNaturalVO.getEstadoCivil().setId(Integer.parseInt(idEstadoCivil));
		}else{
			clienteNaturalVO.setEstadoCivil(null);
		}
		
		clienteNaturalVO.setTipoCliente(tipoCliente);
		clienteNaturalVO.setSegmento(segmento);
		clienteNaturalVO.setCodCliente(clienteNatural.getCodCliente());
		clienteNaturalVO.setPersona(new Persona());
		clienteNaturalVO.getPersona().setId(Long.valueOf(Constantes.CODIGO_TIPO_PERSONA_TITULAR));	
		
		clienteNaturalVO.setCorreo(clienteNatural.getCorreo());
		clienteNaturalVO.setCelular(clienteNatural.getCelular());
	}
	
	public void recortarDatosLista() {
		for (SelectItem lista: listaCategoriaRenta) {
			lista.setLabel(lista.getLabel().substring(0, 7)+".");			
		}
	}

	public String getCodigoTipoOferta() {
		return codigoTipoOferta;
	}

	public void setCodigoTipoOferta(String codigoTipoOferta) {
		this.codigoTipoOferta = codigoTipoOferta;
	}	

	public boolean isItemDisabledcaractPep() {
		return itemDisabledcaractPep;
	}

	public void setItemDisabledcaractPep(boolean itemDisabledcaractPep) {
		this.itemDisabledcaractPep = itemDisabledcaractPep;
	}

	public boolean isItemDisabledcaractAval() {
		return itemDisabledcaractAval;
	}

	public void setItemDisabledcaractAval(boolean itemDisabledcaractAval) {
		this.itemDisabledcaractAval = itemDisabledcaractAval;
	}

	public boolean isRenderedNombre() {
		return renderedNombre;
	}

	public void setRenderedNombre(boolean renderedNombre) {
		this.renderedNombre = renderedNombre;
	}

	public boolean isRenderedRazonSocial() {
		return renderedRazonSocial;
	}

	public void setRenderedRazonSocial(boolean renderedRazonSocial) {
		this.renderedRazonSocial = renderedRazonSocial;
	}

	public boolean isRenderedApellidos() {
		return renderedApellidos;
	}

	public void setRenderedApellidos(boolean renderedApellidos) {
		this.renderedApellidos = renderedApellidos;
	}	
	
	
	
	
}
