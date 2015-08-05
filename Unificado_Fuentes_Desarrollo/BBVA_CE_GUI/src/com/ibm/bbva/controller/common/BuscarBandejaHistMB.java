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
import com.ibm.bbva.tabla.dto.DatosAyudaMemoriaIiceDTO;
import com.ibm.bbva.tabla.dto.DatosGeneradosHisDTO;
import com.ibm.bbva.tabla.dto.DatosHistAntiguoDTO;
import com.ibm.bbva.tabla.util.vo.ConvertHistorial;
import com.ibm.bbva.tabla.util.vo.HistorialDetalle;
import com.ibm.bbva.util.Util;

import com.ibm.bbva.tabla.ejb.impl.TablaFacadeBean;

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
	
	/*FIX ERIKA ABREGU 27/06/2015 */
	private TablaFacadeBean tablaFacadeBean = null;
	
	
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
	//FIX ERIKA ABREGU 19/06/2015
	private boolean habAntiguaCSPLD;
	
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
		/*Estado estado = new Estado();
		estado.setId(Constantes.ESTADO_CERRADO_TAREA_27);
		historial.setEstado(estado);
		*/
		List<Long> listIdsEstados= new ArrayList<Long>();
		listIdsEstados.add(Constantes.ESTADO_CERRADO_TAREA_27.longValue());
		listIdsEstados.add(Constantes.ESTADO_CERRADO_TAREA_11.longValue());
		
		if (fechaFin!=null) {
			fechaFin = Util.addDaysToDate(fechaFin, 1);
		}
		
		
		/*FIX ERIKA ABREGU 24/06/2015
		 * */
		List<Historial> listaHistorial1=null;
		
		if(habAntiguaCSPLD){
			long idExpediente =-1;
			long idProducto =-1;
			long idSubProducto =-1;
			String apePatCliente = "";
			String apeMatCliente ="";
			String nombreCliente ="";
			String numDoi="";
			long tipDoi=-1;
			String estado ="";
			Date fecInicio = new Date();
			Date fecFin = new Date();
			String idsEstados ="";
			ArrayList<Object> listaParametros=null;
			
			if(historial.getExpediente() != null && historial.getExpediente().getId()>0){
				idExpediente = historial.getExpediente().getId();
			}
			if(historial.getProducto() != null && historial.getProducto().getId()>0){
				idProducto = historial.getProducto().getId();
			}
			if(historial.getSubproducto() != null && historial.getSubproducto().getId()>0){
				idSubProducto = historial.getSubproducto().getId();
			}
			if(historial.getClienteNatural() != null && historial.getClienteNatural().getApePat()!=null){
				apePatCliente = String.valueOf(historial.getClienteNatural().getApePat());
			}
			if(historial.getClienteNatural() != null && historial.getClienteNatural().getApeMat()!=null){
				apeMatCliente = String.valueOf(historial.getClienteNatural().getApeMat());
			}
			if(historial.getClienteNatural() != null && historial.getClienteNatural().getNombre()!=null){
				nombreCliente = String.valueOf(historial.getClienteNatural().getNombre());
			}
			if(historial.getClienteNatural() != null && historial.getClienteNatural().getNumDoi()!=null){
				numDoi = String.valueOf(historial.getClienteNatural().getNumDoi());
			}
			if(historial.getClienteNatural() != null && historial.getClienteNatural().getTipoDoi()!=null 
					&& historial.getClienteNatural().getTipoDoi().getId() > 0){
				tipDoi = historial.getClienteNatural().getTipoDoi().getId();
			}
			if(historial.getEstado()!=null && historial.getEstado().getId() > 0 ){
				estado = String.valueOf(historial.getEstado().getId() );
			}
			if(fechaInicio!=null ){
				fecInicio = fechaInicio;
			}else{
				fecInicio = null;
			}
			if(fechaFin!=null ){
				fecFin = fechaFin;
			}else{
				fecFin = null;
			}
			
			//Obtener relacion de estados para el filtro
			int contador=0;
			for ( Long id:listIdsEstados) {
				if(id.equals(null)) continue;
				
				if(contador==0){
					idsEstados += id.toString() + "' ";
				}else{
					idsEstados += ", '" + id.toString();
				}
				contador++;
			}
								
			//preparando parametros
			listaParametros = new ArrayList<Object>();
			listaParametros.add(new Long(idExpediente));
			listaParametros.add(new Long(idProducto));
			listaParametros.add(new Long(idSubProducto));
			listaParametros.add(new String(apePatCliente));
			listaParametros.add(new String(apeMatCliente));
			listaParametros.add(new String(nombreCliente));
			listaParametros.add(new String(numDoi));
			listaParametros.add(new Long(tipDoi));
			listaParametros.add(new String(estado));
			
			//SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
			listaParametros.add(fecInicio);
			listaParametros.add(fecFin);
			listaParametros.add(new String(idsEstados));
			
			if (this.tablaFacadeBean == null) {
				this.tablaFacadeBean = new TablaFacadeBean();
			}
			List<DatosHistAntiguoDTO> listHistAntiguoDTO=tablaFacadeBean.getGenerarDatosHistAntiguo(listaParametros);
			if (listHistAntiguoDTO == null) {
				listHistAntiguoDTO = new ArrayList<DatosHistAntiguoDTO> ();
			}
			//pasar datos de listHistAntiguoDTO a listaHistorial1
			listaHistorial1 = ConvertHistorial.convertToHistorial(listHistAntiguoDTO);
		}else{
			listaHistorial1 = historialBean.buscarXcriterios(historial, fechaInicio, fechaFin, listIdsEstados);
			
		}
		
		/*Estado*/
		/*Estado estado2 = new Estado();
		estado2.setId(Constantes.ESTADO_CERRADO_TAREA_11);
		historial.setEstado(estado2);*/
		
		//List<Historial> listaHistorial2 = historialBean.buscarXcriterios(historial, fechaInicio, fechaFin);
		List<Historial> listaHistorial = new ArrayList<Historial>();		
		listaHistorial.addAll(listaHistorial1);
		//listaHistorial.addAll(listaHistorial2);
		for (Historial hist: listaHistorial) {
			if(hist.getExpediente() == null) continue;
			
			historialDetalle = new HistorialDetalle();
			historialDetalle.setId(hist.getId());
			historialDetalle.setIdExpediente(hist.getExpediente() == null?null:hist.getExpediente().getId());
			historialDetalle.setEstado(hist.getEstado() == null?null:hist.getEstado().getDescripcion()); 
			historialDetalle.setTipoOferta(hist.getTipoOferta()==null?"":hist.getTipoOferta().getDescripcion());
			//LOG.info("hist.getTipoMonedaSol().getDescripcion()  "+hist.getTipoMonedaSol().getDescripcion());

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
			//FIX ERIKA ABREGU
			if(Constantes.EXPEDIENTE_ANTIGUO.equals(hist.getExpediente().getOrigen())){
				historialDetalle.setTerritorio(hist.getOficina().getTerritorio().getDescripcion()==null?"":hist.getOficina().getTerritorio().getDescripcion());
			}else{
				historialDetalle.setTerritorio(hist.getEmpleadoResp()==null?"":hist.getEmpleadoResp().getOficina().getTerritorio().getDescripcion());
			}
			
			historialDetalle.setNumeroContrato(hist.getNroContrato());
			if(hist.getComentario()==null || !hist.getComentario().trim().equals("")){
				historialDetalle.setObservacion(Constantes.OBSERVACION_NO_REGISTRADA);
			}else{
				historialDetalle.setObservacion(Constantes.OBSERVACION_REGISTRADA);
			}
			//FIX ERIKA ABREGU
			if(hist.getExpediente().getOrigen()!=null && (Constantes.EXPEDIENTE_ANTIGUO).equals(hist.getExpediente().getOrigen())){
				SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy"); 
				LOG.info("hist.getExpediente().getFecRegistro()  "+hist.getExpediente().getFecRegistro());
				historialDetalle.setFechaRegistro(hist.getExpediente().getFecRegistro()!=null? sdf.format(hist.getExpediente().getFecRegistro()) :null );
			}else{
				SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy hh:mm:ss a"); 
				historialDetalle.setFechaRegistro(hist.getExpediente().getFecRegistro()!=null? sdf.format(hist.getExpediente().getFecRegistro()) :null );
			}
			//FIN DE FIX
			historialDetalle.setCorreo(hist.getClienteNatural()==null?"":hist.getClienteNatural().getCorreo());		 
			historialDetalle.setCelular(hist.getClienteNatural()==null?"":hist.getClienteNatural().getCelular());
			
			/**FIX ERIKA ABREGU 05/07/2015
			 */
			historialDetalle.setOrigen(hist.getExpediente().getOrigen()==null?"Nueva CS Unificado":hist.getExpediente().getOrigen());
		    listaDet.add(historialDetalle);
		}
		return listaDet;
	}
	public boolean valida() {
		boolean resultado;
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
					return false;
				}
			}else{
				addMessageError(idMsgError + ":datoBusquedaHist", "com.ibm.bbva.common.buscarBandejaHist.msgIngreseDatoBusqueda");
				return false;
			}
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
	//FIX ERIKA ABREGU 19/06/2015
	public boolean isHabAntiguaCSPLD() {
		return habAntiguaCSPLD;
	}
	//FIX ERIKA ABREGU 19/06/2015
	public void setHabAntiguaCSPLD(boolean habAntiguaCSPLD) {
		this.habAntiguaCSPLD = habAntiguaCSPLD;
	}
	
	
	
	
}