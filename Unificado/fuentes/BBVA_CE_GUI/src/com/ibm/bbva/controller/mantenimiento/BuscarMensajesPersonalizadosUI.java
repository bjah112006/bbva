package com.ibm.bbva.controller.mantenimiento;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractSortPagDataTableMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.GuiaDocumentaria;
import com.ibm.bbva.entities.Mensajes;
import com.ibm.bbva.session.MensajesBeanLocal;
import com.ibm.bbva.session.ProductoBeanLocal;
import com.ibm.bbva.tabla.util.vo.GuiaDocumentariaVO;
import com.ibm.bbva.tabla.util.vo.MensajesVO;

@ManagedBean(name = "buscarMensajes")
@SessionScoped
public class BuscarMensajesPersonalizadosUI extends AbstractSortPagDataTableMBean{
	
	private static final Logger LOG = LoggerFactory.getLogger(BuscarMensajesPersonalizadosUI.class);

	@EJB
	private MensajesBeanLocal mensajesBean;
	
	private HtmlDataTable tablaBinding;
	private String numRegistros;
	
	
	private List<Mensajes> listaMensajes;
	private List<MensajesVO> listaMensajesVO;
	private boolean itemSeleccionado;
	
	@PostConstruct
	public void init() {
	
	}
	
	public void inicio(){
		listaMensajes = mensajesBean.buscarTodos();
		mostrarTabla(listaMensajes);
		if(super.dataTable != null){
			ordenarPorDefecto();
		}
	}
	
	public String cancelar(){
		return "/mantenimiento/formManTablasMaestras?faces-redirect=true"; 
	}
	
	public void actualizarLista(){
		listaMensajes = mensajesBean.buscarTodos();
		mostrarTabla(listaMensajes);
		if(super.dataTable != null){
			ordenarPorDefecto();
		}
	}
	
	public void mostrarTabla(List<Mensajes> listaMensajes) {
		itemSeleccionado=false;
		String strContenidoUTF="";
 		if (listaMensajes.size() > 0) {
			listaMensajesVO = new ArrayList<MensajesVO>();
			for (int i = 0; i < listaMensajes.size(); i++) {
				Mensajes detalle = listaMensajes.get(i);
				MensajesVO mensaje = new MensajesVO();
				if (detalle != null) {
					mensaje.setSeleccion(false);
					mensaje.setIdMensajes(String.valueOf(detalle.getId()));
					
					if (detalle.getDescripcion()!=null){
						mensaje.setStrDescripcion(detalle.getDescripcion());
					}
					
					if (detalle.getContenido()!=null){
						try{
							strContenidoUTF= new String(detalle.getContenido());
							mensaje.setStrContenido(strContenidoUTF);
						}catch(Exception e){
							LOG.error(e.getMessage(), e);
							strContenidoUTF = null;
						}
						
					}

				}
				listaMensajesVO.add(mensaje);
			}
		} else {
			listaMensajesVO = new ArrayList<MensajesVO>();
			setListaMensajesVO(listaMensajesVO);
		}
	}
	
	public String agregar() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx = FacesContext.getCurrentInstance(); 
		ActualizaMensajesUI actualizaMensajes = (ActualizaMensajesUI) ctx
				.getApplication().getVariableResolver().resolveVariable(ctx, "actualizaMensajes");
		actualizaMensajes.inicio();
		return "/mantenimiento/formManActualizaMensajes?faces-redirect=true";
	}
	
	public void cargarMensajes(String idMensajes,boolean seleccion) {
		LOG.info("seleccion " + seleccion);
		LOG.info("idGuiaDocumentaria " + idMensajes);
		itemSeleccionado=seleccion;
		if (seleccion){
			LOG.info("Entro al metodo Ajax");
			Mensajes mensajes = new Mensajes();
			mensajes = mensajesBean.buscarPorId(Long.valueOf(idMensajes));
			LOG.info("Id GuiaDocumentaria " + mensajes.getId());
			addObjectSession(Constantes.MENSAJES_SESION, mensajes);
		}
		else{
			removeObjectSession(Constantes.GUIA_DOCUMENTARIA_SESION);
		}
	}
	
	public String actualizar(){
		String idComponente="formManMensajes";
		if (itemSeleccionado){
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx = FacesContext.getCurrentInstance();
			ActualizaMensajesUI actualizaMensajesUI = (ActualizaMensajesUI)
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "actualizaMensajes");
			actualizaMensajesUI.obtenerDatos();
			return "/mantenimiento/formManActualizaMensajes?faces-redirect=true";
		}else{
			addMessageError(idComponente + ":idTablaMensajes",
					"com.ibm.bbva.common.GuiaDocumentaria.msg.FailActualizar");
			return null;
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

	public List<Mensajes> getListaMensajes() {
		return listaMensajes;
	}

	public void setListaMensajes(List<Mensajes> listaMensajes) {
		this.listaMensajes = listaMensajes;
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

	public List<MensajesVO> getListaMensajesVO() {
		return listaMensajesVO;
	}

	public void setListaMensajesVO(List<MensajesVO> listaMensajesVO) {
		this.listaMensajesVO = listaMensajesVO;
	}

	public boolean isItemSeleccionado() {
		return itemSeleccionado;
	}

	public void setItemSeleccionado(boolean itemSeleccionado) {
		this.itemSeleccionado = itemSeleccionado;
	}

	@Override
	public void actualiarAyudaHorario(ActionEvent event) {
		// TODO Apéndice de método generado automáticamente
		
	}
	
	
	
	
}
