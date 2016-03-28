package com.ibm.bbva.controller.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractSortPagDataTableMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.CategoriaRenta;
import com.ibm.bbva.entities.GuiaDocumentaria;
import com.ibm.bbva.entities.Persona;
import com.ibm.bbva.entities.Producto;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.entities.TipoDocumento;
import com.ibm.bbva.entities.TipoOferta;
import com.ibm.bbva.session.CategoriaRentaBeanLocal;
import com.ibm.bbva.session.GuiaDocumentariaBeanLocal;
import com.ibm.bbva.session.PersonaBeanLocal;
import com.ibm.bbva.session.ProductoBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.session.TipoDocumentoBeanLocal;
import com.ibm.bbva.session.TipoOfertaBeanLocal;
import com.ibm.bbva.tabla.util.vo.GuiaDocumentariaVO;
import com.ibm.bbva.util.Util;

@ManagedBean(name = "buscarGuia")
@SessionScoped
public class buscarGuiaDocumentaria extends AbstractSortPagDataTableMBean {

	private List<GuiaDocumentaria> listaDatosGuiaDocumentaria;
	private List<GuiaDocumentariaVO> listaGuiaDocumentaria;

	@EJB
	private GuiaDocumentariaBeanLocal guiaDocumentariaBean;
	@EJB
	private TipoDocumentoBeanLocal tipoDocumentoBean;
	@EJB
	private ProductoBeanLocal productoBean;
	@EJB
	private TipoOfertaBeanLocal tipoOfertaBean;
	@EJB
	private CategoriaRentaBeanLocal categoriaRentaBean;
	@EJB
	private TareaBeanLocal tareaBean;
	@EJB
	private PersonaBeanLocal personaBean;

	private GuiaDocumentaria guiaDocumentaria;
	private List<SelectItem> documentos;
	private String documentosSeleccionado;
	private List<SelectItem> tiposProducto;
	private String productoSeleccionado;
	private List<SelectItem> tiposOferta;
	private String tipoOfertaSeleccionado;
	private List<SelectItem> categoriaRenta;
	private String categoriaRentaSeleccionado;
	private List<SelectItem> persona;
	private String personaSeleccionado;
	private List<SelectItem> tarea;
	private String tareaSeleccionado;
	private List<GuiaDocumentaria> listaAuxGuia;

	private HtmlDataTable tablaBinding;
	private String numRegistros;

	private boolean itemSeleccionado;

	private static final Logger LOG = LoggerFactory.getLogger(buscarGuiaDocumentaria.class);

	@PostConstruct
	public void init() {
		tiposProducto = Util.listaVacia();
		tiposOferta = Util.listaVacia();
		categoriaRenta = Util.listaVacia();
		persona = Util.listaVacia();
		tarea = Util.listaVacia();
		documentos = Util.listaVacia();
		/* Carga Lista de Tipo de Productos */
		tiposProducto = Util.crearItems(productoBean.buscarTodos(), true, "id",
				"descripcion");
		/* Carga Lista de Tipo de Ofertas */
		tiposOferta = Util.crearItems(tipoOfertaBean.buscarTodos(), true, "id",
				"descripcion");
		/* Carga Lista de Categoria Rentas */
		categoriaRenta = Util.crearItems(categoriaRentaBean.buscarTodos(),
				true, "id", "descripcion");
		/* Carga Lista de Persona */
		persona = Util.crearItems(personaBean.buscarTodos(), true, "id",
				"descripcion");
		
		List<Tarea> tareas = tareaBean.buscarTodos();
		
		/*
		 * 16/12/14
		 * 
		 * Quitar la tarea 26 de la lista de tareas.
		 * 
		 * "La tarea 26 ya no participa del flujo y no debe ser referenciada en ninguna parte 
		 * del sistema (ejemplo filtros de búsqueda de tareas y reportes), actualmente aparece en varias
		 * opciones del sistema, ver evidencias. Verificar que no sea visible en todo el sistema."
		 *  
		 */
		
		List<Tarea> auxTareas = new ArrayList<Tarea>();
		
		for(Tarea tarea : tareas){
			if(tarea.getCodigo() != null){
				if(!tarea.getCodigo().equals("26")){
					auxTareas.add(tarea);
				}
			}
		}
		
		/* Carga Lista de Tareas */
		tarea = Util.crearItems(auxTareas, true, "id",
				"descripcion");
		
		
		/* Carga Lista de Tipo de Documentos */
		documentos = Util.crearItems(tipoDocumentoBean.buscar(), true, "id",
				"descripcion");
	}

	public void inicio() {
		this.tareaSeleccionado = "";
		this.documentosSeleccionado = "";
		this.productoSeleccionado = "";
		this.tipoOfertaSeleccionado = "";
		this.categoriaRentaSeleccionado = "";
		this.personaSeleccionado = "";
		listaDatosGuiaDocumentaria = guiaDocumentariaBean.buscarTodos();
		mostrarTabla(listaDatosGuiaDocumentaria);
		if(super.dataTable != null){
			ordenarPorDefecto();
		}
	}

	public String cancelar() {
		return "/mantenimiento/formManTablasMaestras?faces-redirect=true";
	}

	public void cargarGuiaDocumentaria(String idGuiaDocumentaria,boolean seleccion) {
		LOG.info("seleccion " + seleccion);
		LOG.info("idGuiaDocumentaria " + idGuiaDocumentaria);
		itemSeleccionado=seleccion;
		if (seleccion){
			LOG.info("Entro al metodo Ajax");
			GuiaDocumentaria guiaDocumentaria = new GuiaDocumentaria();
			guiaDocumentaria = guiaDocumentariaBean.buscarPorId(Long.valueOf(idGuiaDocumentaria));
			LOG.info("Id GuiaDocumentaria " + guiaDocumentaria.getId());
			addObjectSession(Constantes.GUIA_DOCUMENTARIA_SESION, guiaDocumentaria);
		}
		else{
			removeObjectSession(Constantes.GUIA_DOCUMENTARIA_SESION);
		}
	}
	
	public String agregar() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx = FacesContext.getCurrentInstance(); 
		actualizaGuiaDocumentariaUI actualizaGuiaDocumentaria = (actualizaGuiaDocumentariaUI) ctx
				.getApplication().getVariableResolver().resolveVariable(ctx, "actualizaGuiaDocumentaria");
		actualizaGuiaDocumentaria.init();
		return "/mantenimiento/formManActualizaGuiaDocumentaria?faces-redirect=true";
	}

	public void mostrarTabla(List<GuiaDocumentaria> listaDatosGuiaDocumentaria) {
 		itemSeleccionado=false;
		if (listaDatosGuiaDocumentaria.size() > 0) {
			listaGuiaDocumentaria = new ArrayList<GuiaDocumentariaVO>();
			for (int i = 0; i < listaDatosGuiaDocumentaria.size(); i++) {
				GuiaDocumentaria detalle = listaDatosGuiaDocumentaria.get(i);
				GuiaDocumentariaVO guiaDocumentaria = new GuiaDocumentariaVO();
				if (detalle != null) {
					guiaDocumentaria.setSeleccion(false);
					guiaDocumentaria.setId(String.valueOf(detalle.getId()));
					if (detalle.getTipoDocumento() != null) {
						guiaDocumentaria.setTipoDocumento(detalle.getTipoDocumento().getDescripcion().toString());
					}
					
					if (detalle.getObligatorio().equals(Constantes.CHECK_SELECCIONADO)) {
						guiaDocumentaria.setObligatorio(true);
					} else {
						guiaDocumentaria.setObligatorio(false);
					}
					
					if (detalle.getProducto() != null) {
						guiaDocumentaria.setProducto(detalle.getProducto().getDescripcion().toString());
					}
					
					if (detalle.getSubproducto() != null) {
						guiaDocumentaria.setSubproducto(detalle.getSubproducto().getDescripcion().toString());
					}else{
						guiaDocumentaria.setSubproducto(Constantes.DEFECTO_COMBO_TODOS);
					}
					
					if (detalle.getTipoOferta() != null) {
						guiaDocumentaria.setTipoOferta(detalle.getTipoOferta().getDescripcion().toString());
					} else{
						guiaDocumentaria.setTipoOferta(Constantes.DEFECTO_COMBO_TODOS);
					}

					/*if (detalle.getFlagCliente().equals(Constantes.CHECK_SELECCIONADO)) {
						guiaDocumentaria.setCliente(true);
					} else {
						guiaDocumentaria.setCliente(false);
					}*/
					if (detalle.getFlagCliente() != null){
						guiaDocumentaria.setCliente(detalle.getFlagCliente().equals("1") ? "SI" : "NO");
					} else{
						guiaDocumentaria.setCliente(Constantes.DEFECTO_COMBO_TODOS);
					}
					
					/*if (detalle.getFlagPagoHab().equals(Constantes.CHECK_SELECCIONADO)) {
						guiaDocumentaria.setPagoHabiente(true);
					} else {
						guiaDocumentaria.setPagoHabiente(false);
					}*/
					if (detalle.getFlagPagoHab() != null){
						guiaDocumentaria.setPagoHabiente(detalle.getFlagPagoHab().equals("1") ? "SI" : "NO");
					} else{
						guiaDocumentaria.setPagoHabiente(Constantes.DEFECTO_COMBO_TODOS);
					}
					
					if (detalle.getCategoriaRenta() != null) {
						guiaDocumentaria.setCategoriaRenta(detalle.getCategoriaRenta().getDescripcion().toString());
					} else{
						guiaDocumentaria.setCategoriaRenta(Constantes.DEFECTO_COMBO_TODOS);
					}
					
					if (detalle.getPersona() != null) {
						guiaDocumentaria.setPersona(detalle.getPersona().getDescripcion().toString());
					} else{
						guiaDocumentaria.setPersona(Constantes.DEFECTO_COMBO_TODOS);
					}
					
					if (detalle.getFlagTasaEsp() != null){
						guiaDocumentaria.setTasaEspecial(detalle.getFlagTasaEsp().equals("1") ? "SI" : "NO");
					} else{
						guiaDocumentaria.setTasaEspecial(Constantes.DEFECTO_COMBO_TODOS);
					}
					
					if (detalle.getTarea()!=null){
						guiaDocumentaria.setTarea(detalle.getTarea().getDescripcion().toString());
					}

					/*if (detalle.getFlagVerifDom().equals(Constantes.CHECK_SELECCIONADO)) {
						guiaDocumentaria.setVerificacionDomiciliaria(true);
					} else {
						guiaDocumentaria.setVerificacionDomiciliaria(false);
					}*/
					if (detalle.getFlagVerifDom() != null){
						guiaDocumentaria.setVerificacionDomiciliaria(detalle.getFlagVerifDom().equals("1") ? "SI" : "NO");
					} else{
						guiaDocumentaria.setVerificacionDomiciliaria(Constantes.DEFECTO_COMBO_TODOS);
					}
					
					/*if (detalle.getFlagVerifLab().equals(Constantes.CHECK_SELECCIONADO)) {
						guiaDocumentaria.setVerificacionLaboral(true);
					} else {
						guiaDocumentaria.setVerificacionLaboral(false);
					}*/
					if (detalle.getFlagVerifLab() != null){
						guiaDocumentaria.setVerificacionLaboral(detalle.getFlagVerifLab().equals("1") ? "SI" : "NO");
					} else{
						guiaDocumentaria.setVerificacionLaboral(Constantes.DEFECTO_COMBO_TODOS);
					}
					
					/*if (detalle.getFlagDps().equals(Constantes.CHECK_SELECCIONADO)) {
						guiaDocumentaria.setFlagDps(true);
					} else {
						guiaDocumentaria.setFlagDps(false);
					}*/
					if (detalle.getFlagDps() != null){
						guiaDocumentaria.setFlagDps(detalle.getFlagDps().equals("1") ? "SI" : "NO");
					} else{
						guiaDocumentaria.setFlagDps(Constantes.DEFECTO_COMBO_TODOS);
					}
					
					if (detalle.getFlagComiteRiesgo().equals(Constantes.CHECK_SELECCIONADO)) {
						guiaDocumentaria.setFlagComiteRiesgo(true);
					} else {
						guiaDocumentaria.setFlagComiteRiesgo(false);
					}
					
					if (detalle.getDescripcion()!=null){
						guiaDocumentaria.setDescripcion(detalle.getDescripcion().toString());
					} else{
						guiaDocumentaria.setDescripcion("");
					}
					
					guiaDocumentaria.setId(String.valueOf(detalle.getId()));
					
					if (detalle.getFlagActivo().equals(Constantes.CHECK_SELECCIONADO)) {
						guiaDocumentaria.setFlagActivo(true);
					} else {
						guiaDocumentaria.setFlagActivo(false);
					}

				}
				listaGuiaDocumentaria.add(guiaDocumentaria);
			}
		} else {
			listaGuiaDocumentaria = new ArrayList<GuiaDocumentariaVO>();
			setListaGuiaDocumentaria(listaGuiaDocumentaria);
		}
	}
	
	public String actualizar(){
		String idComponente="formManGuiaDocumentaria";
		if (itemSeleccionado){
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx = FacesContext.getCurrentInstance();
			actualizaGuiaDocumentariaUI actualizaGuiaDocumentaria = (actualizaGuiaDocumentariaUI)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "actualizaGuiaDocumentaria");
			actualizaGuiaDocumentaria.obtenerDatos();
			return "/mantenimiento/formManActualizaGuiaDocumentaria?faces-redirect=true";
		} else{	
			addMessageError(idComponente + ":idTablaGuiaDocumentaria",
					"com.ibm.bbva.common.GuiaDocumentaria.msg.FailActualizar");
			return null;
		}
	}

	public void actualizarLista(){
		listaAuxGuia = guiaDocumentariaBean.buscarTodos();
		limpiarControlesBusqueda();
		mostrarTabla(listaAuxGuia);
		if(super.dataTable != null){
			ordenarPorDefecto();
		}
	}
	
	public void limpiarControlesBusqueda(){
		documentosSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		productoSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		tipoOfertaSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		categoriaRentaSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		personaSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		tareaSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
	}
	
	public String buscar() {
		listaDatosGuiaDocumentaria = new ArrayList<GuiaDocumentaria>();
		listaGuiaDocumentaria = new ArrayList<GuiaDocumentariaVO>();
		GuiaDocumentaria guiaDocumentaria = new GuiaDocumentaria();

		TipoDocumento tipoDocumento = new TipoDocumento();
		if (documentosSeleccionado != null && !documentosSeleccionado.equals("") && !documentosSeleccionado.equals("-1")) {
			tipoDocumento.setId(Util.validarCampoLong(documentosSeleccionado));
			guiaDocumentaria.setTipoDocumento(tipoDocumento);
		}

		Producto producto = new Producto();
		if (productoSeleccionado != null && !productoSeleccionado.equals("") && !productoSeleccionado.equals("-1")) {
			producto.setId(Util.validarCampoLong(productoSeleccionado));
			guiaDocumentaria.setProducto(producto);
		}

		TipoOferta tipoOferta = new TipoOferta();
		if (tipoOfertaSeleccionado != null && !tipoOfertaSeleccionado.equals("") && !tipoOfertaSeleccionado.equals("-1")) {
			tipoOferta.setId(Util.validarCampoLong(tipoOfertaSeleccionado));
			guiaDocumentaria.setTipoOferta(tipoOferta);
		}

		CategoriaRenta categoriaRenta = new CategoriaRenta();
		if (categoriaRentaSeleccionado != null && !categoriaRentaSeleccionado.equals("") && !categoriaRentaSeleccionado.equals("-1")) {
			categoriaRenta.setId(Util
					.validarCampoLong(categoriaRentaSeleccionado));
			guiaDocumentaria.setCategoriaRenta(categoriaRenta);
		}

		Persona persona = new Persona();
		if (personaSeleccionado != null && !personaSeleccionado.equals("") && !personaSeleccionado.equals("-1")) {
			persona.setId(Util.validarCampoLong(personaSeleccionado));
			guiaDocumentaria.setPersona(persona);
		}

		Tarea tarea = new Tarea();
		if (tareaSeleccionado != null && !tareaSeleccionado.equals("") && !tareaSeleccionado.equals("-1")) {
			tarea.setId(Util.validarCampoLong(tareaSeleccionado));
			guiaDocumentaria.setTarea(tarea);
		}

		if (guiaDocumentaria != null) {
			listaDatosGuiaDocumentaria = guiaDocumentariaBean.resultadoBusqueda(guiaDocumentaria);
			mostrarTabla(listaDatosGuiaDocumentaria);
			if(super.dataTable != null){
				ordenarPorDefecto();
			}
		}
		return "/mantenimiento/formManGuiaDocumentaria?faces-redirect=true";
	}

	public void limpiar() {
		this.tareaSeleccionado = "";
		this.documentosSeleccionado = "";
		this.productoSeleccionado = "";
		this.tipoOfertaSeleccionado = "";
		this.categoriaRentaSeleccionado = "";
		this.personaSeleccionado = "";
		/*for (GuiaDocumentariaVO item : listaGuiaDocumentaria){
			item.setSeleccion(false);
		}*/
		listaDatosGuiaDocumentaria = guiaDocumentariaBean.buscarTodos();
		mostrarTabla(listaDatosGuiaDocumentaria);		
		if(super.dataTable != null){
			ordenarPorDefecto();
		}
	}

	@Override
	public void ordenar(ActionEvent event) {
		LOG.info("ordenar::::");
		String sortFieldAttribute = getAttribute(event, "sortField");
		if (sortFieldAttribute != null) {
			if (sortField != null && sortField.equals(sortFieldAttribute)) {
				sortAscending = !sortAscending;
			} else {
				sortField = sortFieldAttribute;
				sortAscending = true;
			}
			// regresa a la primera página si cambia el orden
			dataTable.setFirst(0);
		} // si no viene el atributo sortField no cambia el ordenamiento

		String columna = this.sortField;
	}
	
	public List<GuiaDocumentaria> getListaDatosGuiaDocumentaria() {
		return listaDatosGuiaDocumentaria;
	}

	public void setListaDatosGuiaDocumentaria(
			List<GuiaDocumentaria> listaDatosGuiaDocumentaria) {
		this.listaDatosGuiaDocumentaria = listaDatosGuiaDocumentaria;
	}

	public List<GuiaDocumentariaVO> getListaGuiaDocumentaria() {
		return listaGuiaDocumentaria;
	}

	public void setListaGuiaDocumentaria(
			List<GuiaDocumentariaVO> listaGuiaDocumentaria) {
		this.listaGuiaDocumentaria = listaGuiaDocumentaria;
	}

	public HtmlDataTable getTablaBinding() {
		return tablaBinding;
	}

	public void setTablaBinding(HtmlDataTable tablaBinding) {
		this.tablaBinding = tablaBinding;
	}

	public String getNumRegistros() {
		return numRegistros;
	}

	public void setNumRegistros(String numRegistros) {
		this.numRegistros = numRegistros;
	}

	public List<SelectItem> getTiposProducto() {
		return tiposProducto;
	}

	public void setTiposProducto(List<SelectItem> tiposProducto) {
		this.tiposProducto = tiposProducto;
	}

	public String getProductoSeleccionado() {
		return productoSeleccionado;
	}

	public void setProductoSeleccionado(String productoSeleccionado) {
		this.productoSeleccionado = productoSeleccionado;
	}

	public List<SelectItem> getTiposOferta() {
		return tiposOferta;
	}

	public void setTiposOferta(List<SelectItem> tiposOferta) {
		this.tiposOferta = tiposOferta;
	}

	public String getTipoOfertaSeleccionado() {
		return tipoOfertaSeleccionado;
	}

	public void setTipoOfertaSeleccionado(String tipoOfertaSeleccionado) {
		this.tipoOfertaSeleccionado = tipoOfertaSeleccionado;
	}

	public List<SelectItem> getCategoriaRenta() {
		return categoriaRenta;
	}

	public void setCategoriaRenta(List<SelectItem> categoriaRenta) {
		this.categoriaRenta = categoriaRenta;
	}

	public String getCategoriaRentaSeleccionado() {
		return categoriaRentaSeleccionado;
	}

	public void setCategoriaRentaSeleccionado(String categoriaRentaSeleccionado) {
		this.categoriaRentaSeleccionado = categoriaRentaSeleccionado;
	}

	public List<SelectItem> getPersona() {
		return persona;
	}

	public void setPersona(List<SelectItem> persona) {
		this.persona = persona;
	}

	public String getPersonaSeleccionado() {
		return personaSeleccionado;
	}

	public void setPersonaSeleccionado(String personaSeleccionado) {
		this.personaSeleccionado = personaSeleccionado;
	}

	public List<SelectItem> getTarea() {
		return tarea;
	}

	public void setTarea(List<SelectItem> tarea) {
		this.tarea = tarea;
	}

	public String getTareaSeleccionado() {
		return tareaSeleccionado;
	}

	public void setTareaSeleccionado(String tareaSeleccionado) {
		this.tareaSeleccionado = tareaSeleccionado;
	}

	public List<SelectItem> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(List<SelectItem> documentos) {
		this.documentos = documentos;
	}

	public String getDocumentosSeleccionado() {
		return documentosSeleccionado;
	}

	public void setDocumentosSeleccionado(String documentosSeleccionado) {
		this.documentosSeleccionado = documentosSeleccionado;
	}

	public List<GuiaDocumentaria> getListaAuxGuia() {
		return listaAuxGuia;
	}

	public void setListaAuxGuia(List<GuiaDocumentaria> listaAuxGuia) {
		this.listaAuxGuia = listaAuxGuia;
	}

	@Override
	public void actualiarAyudaHorario(ActionEvent event) {
		// TODO Apéndice de método generado automáticamente
		
	}

}
