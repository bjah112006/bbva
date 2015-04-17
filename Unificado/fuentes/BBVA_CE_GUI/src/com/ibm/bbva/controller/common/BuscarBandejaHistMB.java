package com.ibm.bbva.controller.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.Mensajes;
import com.ibm.bbva.entities.Producto;
import com.ibm.bbva.entities.Subproducto;
import com.ibm.bbva.entities.TipoDoi;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.MensajesBeanLocal;
import com.ibm.bbva.session.ProductoBeanLocal;
import com.ibm.bbva.session.SubproductoBeanLocal;
import com.ibm.bbva.session.TipoDoiBeanLocal;
import com.ibm.bbva.tabla.util.vo.HistorialDetalle;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "buscarBandejaHist")
@RequestScoped
public class BuscarBandejaHistMB extends AbstractMBean {
	
	@EJB
	private TipoDoiBeanLocal tipoDoiBean;
	@EJB
	private ProductoBeanLocal productoBean;
	@EJB
	private SubproductoBeanLocal subproductoBean;
	@EJB
	HistorialBeanLocal historialBean;
	@EJB
	private MensajesBeanLocal mensajeBean;
	
	private List<SelectItem> tiposDOI;
	private List<SelectItem> productos;
	private List<SelectItem> subProductos;
	private String tipoDOISeleccionado;
	private String productoSeleccionado;
	private String subProductoSeleccionado;	
	private String codigoExpediente;
	private String numeroDOI;
	private String apPaterno;
	private String apMaterno;
	private String nombres;
	private String codigoCliente;
	private Date fechaInicio;
	private Date fechaFin;
	private String textoMensajeSinFiltro;
	
	private static final int CARACTERES_DNI = 8;
	private static final int CARACTERES_RUC = 11;
	private static final int MAX_CARACTERES_OTROS = 10;	
	
	private static final Logger LOG = LoggerFactory.getLogger(BuscarBandejaHistMB.class);
	
	public BuscarBandejaHistMB() {
		subProductos = Util.listaVacia();
	}
	
	@PostConstruct
    public void init() {
		/*Carga Lista de Tipo Documento*/
		tiposDOI = Util.crearItems(tipoDoiBean.buscarTodos(), true, "id", "descripcion");
		
		/*Carga Lista de Tipo Producto*/
		productos = Util.crearItems(productoBean.buscarTodos(), true, "id", "descripcion");
	}

	public void cambiarSubProductos(ValueChangeEvent vce) {
		Object codigo = vce.getNewValue();
		productoSeleccionado = (String)codigo;
		if (productoSeleccionado==null || Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(productoSeleccionado)) {
			subProductos = Util.listaVacia();
		} else {
			crearListaSubProductos (Long.parseLong(productoSeleccionado));
		}
	}

	private void crearListaSubProductos (Long idSubProd) {
		subProductos = Util.crearItems(
				subproductoBean.buscarPorIdProducto(idSubProd.longValue()),
				true, "id", "descripcion");
	}
	

	
	public List<HistorialDetalle> buscar () {
		LOG.info("buscar  ");
		List<HistorialDetalle> listaDet = new ArrayList<HistorialDetalle>();
		HistorialDetalle historialDetalle = new HistorialDetalle();
		
		Historial historial = new Historial ();
		/*Expediente*/
		Expediente expediente = new Expediente();
		if (codigoExpediente!=null && !codigoExpediente.equals("")) {
		    expediente.setId(Util.validarCampoLong(codigoExpediente));
		}
		historial.setExpediente(expediente);
		/*Producto*/
		Producto producto = new Producto();
		if (productoSeleccionado!=null && !productoSeleccionado.equals("") && !productoSeleccionado.equals("-1")) {
		   producto.setId(Util.validarCampoLong(productoSeleccionado));
		}
		historial.setProducto(producto);
		/*SubProducto*/
		Subproducto subproducto = new Subproducto();
		if (subProductoSeleccionado!=null && !subProductoSeleccionado.equals("") && !subProductoSeleccionado.equals("-1")) {
		    subproducto.setId(Util.validarCampoLong(subProductoSeleccionado));
		}
		historial.setSubproducto(subproducto);
		/*Cliente*/
		ClienteNatural clienteNatural = new ClienteNatural();		
		clienteNatural.setApePat(Util.validarCampo (apPaterno));
		clienteNatural.setApeMat(Util.validarCampo (apMaterno));
		clienteNatural.setNombre(Util.validarCampo (nombres));
		clienteNatural.setCodCliente(Util.validarCampo (codigoCliente));
		clienteNatural.setNumDoi(Util.validarCampo (numeroDOI));
				
		TipoDoi tipoDoi = new TipoDoi();
		if (tipoDOISeleccionado!=null && !tipoDOISeleccionado.equals("") && !tipoDOISeleccionado.equals("-1")) {
		   tipoDoi.setId(Util.validarIdInteger(tipoDOISeleccionado));
		}
		clienteNatural.setTipoDoi(tipoDoi);		
		historial.setClienteNatural(clienteNatural);
		/*Estado*/
		Estado estado = new Estado();
		estado.setId(Constantes.ESTADO_CERRADO_TAREA_27);
		historial.setEstado(estado);
		
		if (fechaFin!=null) {
			fechaFin = Util.addDaysToDate(fechaFin, 1);
		}
		
		List<Historial> listaHistorial1 = historialBean.buscarXcriterios(historial, fechaInicio, fechaFin);

		/*Estado*/
		Estado estado2 = new Estado();
		estado2.setId(Constantes.ESTADO_CERRADO_TAREA_11);
		historial.setEstado(estado2);
		
		List<Historial> listaHistorial2 = historialBean.buscarXcriterios(historial, fechaInicio, fechaFin);
		List<Historial> listaHistorial = new ArrayList<Historial>();		
		listaHistorial.addAll(listaHistorial1);
		listaHistorial.addAll(listaHistorial2);
		for (Historial hist: listaHistorial) {
			if(hist.getExpediente() == null) continue;
			
			historialDetalle = new HistorialDetalle();
			historialDetalle.setId(hist.getId());
			historialDetalle.setIdExpediente(hist.getExpediente() == null?null:hist.getExpediente().getId());
			historialDetalle.setEstado(hist.getEstado() == null?null:hist.getEstado().getDescripcion()); 
			historialDetalle.setTipoOferta(hist.getTipoOferta()==null?"":hist.getTipoOferta().getDescripcion());
			LOG.info("hist.getTipoMonedaSol().getDescripcion()  "+hist.getTipoMonedaSol().getDescripcion());

			historialDetalle.setTipoMonedaSolicitada(hist.getTipoMonedaSol() == null?null:hist.getTipoMonedaSol().getDescripcion());
			historialDetalle.setTipoMonedaAprobada(hist.getTipoMonedaAprob()==null?"":hist.getTipoMonedaAprob().getDescripcion());
			historialDetalle.setNumeroDOI(hist.getClienteNatural()==null?null:hist.getClienteNatural().getNumDoi());
			historialDetalle.setNombre(hist.getClienteNatural()==null?null:hist.getClienteNatural().getNombre());
			historialDetalle.setApPaterno(hist.getClienteNatural()==null?null:hist.getClienteNatural().getApePat());
			historialDetalle.setApMaterno(hist.getClienteNatural()==null?null:hist.getClienteNatural().getApeMat());
			historialDetalle.setTipoDOI(hist.getClienteNatural()==null?null:hist.getClienteNatural().getTipoDoi().getDescripcion());
			historialDetalle.setSegmento(hist.getClienteNatural()==null?null:hist.getClienteNatural().getSegmento().getDescripcion());
			historialDetalle.setProducto(hist.getProducto()==null?"":hist.getProducto().getDescripcion());
			historialDetalle.setSubProducto(hist.getSubproducto()==null?"":hist.getSubproducto().getDescripcion());
			historialDetalle.setLineaCreditoSolicitado(hist.getLineaCredSol());
			historialDetalle.setLineaCreditoAprobado(hist.getLineaCredAprob());
			historialDetalle.setCodigoRGVL(hist.getRvgl());
			historialDetalle.setOficina(hist.getOficina()==null?"":hist.getOficina().getDescripcion());
			historialDetalle.setTerritorio(hist.getEmpleadoResp()==null?"":hist.getEmpleadoResp().getOficina().getTerritorio().getDescripcion());
			historialDetalle.setNumeroContrato(hist.getNroContrato());
			if(hist.getComentario()==null || !hist.getComentario().trim().equals("")){
				historialDetalle.setObservacion(Constantes.OBSERVACION_NO_REGISTRADA);
			}else{
				historialDetalle.setObservacion(Constantes.OBSERVACION_REGISTRADA);
			}
			SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy hh:mm:ss a"); 
			historialDetalle.setFechaRegistro(sdf.format(hist.getFecRegistro()));
			historialDetalle.setCorreo(hist.getClienteNatural()==null?"":hist.getClienteNatural().getCorreo());		 
			historialDetalle.setCelular(hist.getClienteNatural()==null?"":hist.getClienteNatural().getCelular());
		    listaDet.add(historialDetalle);
		}
		return listaDet;
	}
	public boolean valida() {
		String idMsgError = "frmBandejaHistorica";
		//Verificar que ingrese al menos un valor de busqueda
		if(codigoExpediente.equals(Constantes.VALOR_VACIO) && 
				tipoDOISeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)&&				
				numeroDOI.equals(Constantes.VALOR_VACIO)&&
				apPaterno.equals(Constantes.VALOR_VACIO)&&
				apMaterno.equals(Constantes.VALOR_VACIO)&&
				nombres.equals(Constantes.VALOR_VACIO)&&
				codigoCliente.equals(Constantes.VALOR_VACIO)&&
				productoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)&&
				subProductoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)&&
				fechaInicio == null &&
				fechaFin == null){
			Mensajes mensajeSinFiltro = new Mensajes();
			mensajeSinFiltro=mensajeBean.buscarPorId(Long.valueOf(Constantes.ID_MENSAJES_SIN_FILTRO));
			if (mensajeSinFiltro!=null && mensajeSinFiltro.getContenido()!=null){
				try{
					//textoMensajeSinFiltro = new String(mensajeSinFiltro.getContenido(), "UTF-8");
					textoMensajeSinFiltro = new String(mensajeSinFiltro.getContenido());
					return false;
				}catch(Exception e){
					LOG.error(e.getMessage(), e);
					textoMensajeSinFiltro = null;
				}
			}
				//addMessageError(idMsgError + ":datoBusquedaHist", "com.ibm.bbva.common.buscarBandejaHist.msgIngreseDatoBusqueda");
				//return false;
		}
		
		// Se verifican valores ingresados
		if (tipoDOISeleccionado!=null && !tipoDOISeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO) &&
           (numeroDOI==null || numeroDOI.trim().equals(""))) {		
			addMessageError(idMsgError + ":idNumDoi", 
			"com.ibm.bbva.common.buscarBandeja.msg.numero");
			return false;
		}
		if (numeroDOI!=null && !numeroDOI.trim().equals("") &&
			(tipoDOISeleccionado==null || tipoDOISeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))	) {
			addMessageError(idMsgError + ":idTipoDoi", 
			"com.ibm.bbva.common.buscarBandeja.msg.seleccion.tipoDOI");
			return false;
		}
		// Cuando los valores de los campos son ingresados se valida la consistencia entre ambos
		if (tipoDOISeleccionado!=null && !tipoDOISeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO) &&
				numeroDOI!=null && !numeroDOI.trim().equals("") ) {
			
			TipoDoi tipoDOI = tipoDoiBean.buscarPorId(Long.parseLong(tipoDOISeleccionado));			
			String codTipoDoi = tipoDOI.getCodigo();
			
			if (codTipoDoi.equals(Constantes.CODIGO_TIPO_DOI_DNI) && numeroDOI.length()!=CARACTERES_DNI) {
				addMessageError(idMsgError + ":idNumDoi",
						"com.ibm.bbva.common.buscarBandeja.msg.caracteres", "DNI", CARACTERES_DNI);
				return false;
			} else if (codTipoDoi.equals(Constantes.CODIGO_TIPO_DOI_RUC) && numeroDOI.length()!=CARACTERES_RUC) {
				addMessageError(idMsgError + ":idNumDoi",
						"com.ibm.bbva.common.buscarBandeja.msg.caracteres", "RUC", CARACTERES_RUC);
				return false;
			} else if (!codTipoDoi.equals(Constantes.CODIGO_TIPO_DOI_DNI) &&
					!codTipoDoi.equals(Constantes.CODIGO_TIPO_DOI_RUC) &&
					numeroDOI.length() > MAX_CARACTERES_OTROS){
				addMessageError(idMsgError + ":idNumDoi",
						"com.ibm.bbva.common.buscarBandeja.msg.maxCaracteres", MAX_CARACTERES_OTROS);
				return false;			
			}
		}
		return true;
	}
	
	public String limpiar() {
		this.codigoExpediente = "";
		this.numeroDOI = "";
		this.apPaterno = "";
		this.apMaterno = "";
		this.nombres = "";
		this.codigoCliente = "";
		this.fechaInicio = null;
		this.fechaFin = null;
		
		this.tipoDOISeleccionado = "";
		this.productoSeleccionado = "";
		this.subProductoSeleccionado = "";    
		return null;
	}	
	
	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getCodigoExpediente() {
		return codigoExpediente;
	}

	public void setCodigoExpediente(String codigoExpediente) {
		this.codigoExpediente = codigoExpediente;
	}

	public String getNumeroDOI() {
		return numeroDOI;
	}

	public void setNumeroDOI(String numeroDOI) {
		this.numeroDOI = numeroDOI;
	}

	public String getApPaterno() {
		return apPaterno;
	}

	public void setApPaterno(String apPaterno) {
		this.apPaterno = apPaterno;
	}

	public String getApMaterno() {
		return apMaterno;
	}

	public void setApMaterno(String apMaterno) {
		this.apMaterno = apMaterno;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public List<SelectItem> getTiposDOI() {
		return tiposDOI;
	}

	public void setTiposDOI(List<SelectItem> tiposDOI) {
		this.tiposDOI = tiposDOI;
	}

	public String getTipoDOISeleccionado() {
		return tipoDOISeleccionado;
	}

	public void setTipoDOISeleccionado(String tipoDOISeleccionado) {
		this.tipoDOISeleccionado = tipoDOISeleccionado;
	}
	
	public List<SelectItem> getProductos() {
		return productos;
	}

	public void setProductos(List<SelectItem> productos) {
		this.productos = productos;
	}

	public String getProductoSeleccionado() {
		return productoSeleccionado;
	}

	public void setProductoSeleccionado(String productoSeleccionado) {
		this.productoSeleccionado = productoSeleccionado;
	}

	public List<SelectItem> getSubProductos() {
		return subProductos;
	}

	public void setSubProductos(List<SelectItem> subProductos) {
		this.subProductos = subProductos;
	}

	public String getSubProductoSeleccionado() {
		return subProductoSeleccionado;
	}

	public void setSubProductoSeleccionado(String subProductoSeleccionado) {
		this.subProductoSeleccionado = subProductoSeleccionado;
	}

	public String getTextoMensajeSinFiltro() {
		return textoMensajeSinFiltro;
	}

	public void setTextoMensajeSinFiltro(String textoMensajeSinFiltro) {
		this.textoMensajeSinFiltro = textoMensajeSinFiltro;
	}
	
	
	
	
}