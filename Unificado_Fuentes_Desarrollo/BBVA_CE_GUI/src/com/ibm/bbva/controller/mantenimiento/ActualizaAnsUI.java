package com.ibm.bbva.controller.mantenimiento;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Ans;
import com.ibm.bbva.session.AnsBeanLocal;
import com.ibm.bbva.session.EstadoCEBeanLocal;
import com.ibm.bbva.session.GrupoSegmentoBeanLocal;
import com.ibm.bbva.session.ProductoBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.session.TipoOfertaBeanLocal;
import com.ibm.bbva.util.Util;

@ManagedBean(name = "actualizaAns")
@ViewScoped
public class ActualizaAnsUI extends AbstractMBean {
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(ActualizaAnsUI.class);
	
	private List<SelectItem> productos;
	private List<SelectItem> tareas;
	private List<SelectItem> tiposOferta;
	private List<SelectItem> gruposSegmento;
	private List<SelectItem> estados;
	private Long productoSeleccionado;
	private Long tareaSeleccionado;
	private Long tipoOfertaSeleccionado;
	private Long grupoSegmentoSeleccionado;
	private Long estadoSeleccionado;
	private BigDecimal verdeMinutos;
	private BigDecimal amarilloMinutos;
	private boolean activo;
	private boolean nuevoRegistro;
	
	private String FORM_NAME = "formManActualizaAns";
	
	@EJB
	private AnsBeanLocal ansBean;
	@EJB
	private ProductoBeanLocal productoBean;
	@EJB
	private TareaBeanLocal tareaBean;
	@EJB
	private TipoOfertaBeanLocal tipoOfertaBean;
	@EJB
	private GrupoSegmentoBeanLocal grupoSegmentoBean;
	@EJB
	private EstadoCEBeanLocal estadoCEBean;

	public ActualizaAnsUI() {
		super();
	}
	
	@PostConstruct
	public void init() {
		LOG.info("init()");
		
		Ans ans = (Ans) getObjectSession(Constantes.MANT_ANS_SESION);
		if (ans == null) {
			productoSeleccionado = Long.parseLong(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
			tareaSeleccionado = Long.parseLong(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
			tipoOfertaSeleccionado = Long.parseLong(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
			grupoSegmentoSeleccionado = Long.parseLong(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
			estadoSeleccionado = Long.parseLong(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
			verdeMinutos = null;
			amarilloMinutos = null;
			activo = true;
			nuevoRegistro = true;
		} else {
			productoSeleccionado = ans.getProducto().getId();
			tareaSeleccionado = ans.getTarea().getId();
			tipoOfertaSeleccionado = ans.getTipoOferta().getId();
			grupoSegmentoSeleccionado = ans.getGrupoSegmento().getId();
			if (ans.getEstadoCE() != null) {
				estadoSeleccionado = ans.getEstadoCE().getId();
			} else {
				estadoSeleccionado = null;
			}
			verdeMinutos = ans.getVerdeMinutos();
			amarilloMinutos = ans.getAmarilloMinutos();
			activo = (ans.getFlagActivo() != null && ans.getFlagActivo().equals(Constantes.CHECK_SELECCIONADO)) ? true : false;
			nuevoRegistro = false;
		}
		
		productos = Util.crearItems(productoBean.buscarTodos(), true, "id", "descripcion");
		tareas = Util.crearItems(tareaBean.buscarTodos(), true, "id", "descripcion");
		tiposOferta = Util.crearItems(tipoOfertaBean.buscarTodos(), true, "id", "descripcion");
		gruposSegmento = Util.crearItems(grupoSegmentoBean.buscarTodos(), true, "id", "descripcion");
		if (nuevoRegistro) {
			estados = Util.listaVacia();
		} else {
			estados = Util.crearItems(estadoCEBean.buscarPorIdTarea(tareaSeleccionado), true, "id", "descripcion");
		}
	}
	
	public void cargaEstadosXTarea(AjaxBehaviorEvent event) {
		if (tareaSeleccionado > 0) {
			estados = Util.crearItems(estadoCEBean.buscarPorIdTarea(tareaSeleccionado), true, "id", "descripcion");
		} else {
			estados = Util.listaVacia();
		}
		estadoSeleccionado = Long.parseLong(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
	}
	
	public String guardar() {
		if (esValido()) {
			try {
				if (nuevoRegistro) {
					Ans ans = new Ans();
					ans.setProducto(productoBean.buscarPorId(productoSeleccionado));
					ans.setTarea(tareaBean.buscarPorId(tareaSeleccionado));
					ans.setTipoOferta(tipoOfertaBean.buscarPorId(tipoOfertaSeleccionado));
					ans.setGrupoSegmento(grupoSegmentoBean.buscarPorId(grupoSegmentoSeleccionado));
					if (estadoSeleccionado > 0) {
						ans.setEstadoCE(estadoCEBean.buscarPorId(estadoSeleccionado));
					} else {
						ans.setEstadoCE(null);
					}
					ans.setVerdeMinutos(verdeMinutos);
					ans.setAmarilloMinutos(amarilloMinutos);
					ans.setFlagActivo(activo ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
					
					ansBean.create(ans);
				} else {
					Ans ans = (Ans) getObjectSession(Constantes.MANT_ANS_SESION);
					ans.setVerdeMinutos(verdeMinutos);
					ans.setAmarilloMinutos(amarilloMinutos);
					ans.setFlagActivo(activo ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
					
					ansBean.edit(ans);
				}
			} catch (Throwable t) {
				LOG.error(t.getMessage(), t);
				addComponentMessage(null, "Error en BBDD al intentar ejecutar la operación.");
				return null;
			}
			removeObjectSession(Constantes.MANT_ANS_SESION);
			return "/mantenimiento/formManConsultaAns?faces-redirect=true";
		} else {
			return null;
		}
	}
	
	public String cancelar() {
		removeObjectSession(Constantes.MANT_ANS_SESION);
		return "/mantenimiento/formManConsultaAns?faces-redirect=true";
	}

	private boolean esValido() {
		boolean resultado = true;
		
		if (productoSeleccionado < 0) {
			resultado = false;
			addComponentMessage(FORM_NAME+":idProducto", "El campo Producto es requerido");
		}
		if (tareaSeleccionado < 0) {
			resultado = false;
			addComponentMessage(FORM_NAME+":idTarea", "El campo Tarea es requerido");
		}
		if (tipoOfertaSeleccionado < 0) {
			resultado = false;
			addComponentMessage(FORM_NAME+":idTipoOferta", "El campo Tipo Oferta es requerido");
		}
		if (grupoSegmentoSeleccionado < 0) {
			resultado = false;
			addComponentMessage(FORM_NAME+":idGrupoSegmento", "El campo Grupo segmento es requerido");
		}
		
		return resultado;
	}
	
	public List<SelectItem> getProductos() {
		return productos;
	}

	public void setProductos(List<SelectItem> productos) {
		this.productos = productos;
	}

	public List<SelectItem> getTareas() {
		return tareas;
	}

	public void setTareas(List<SelectItem> tareas) {
		this.tareas = tareas;
	}

	public List<SelectItem> getTiposOferta() {
		return tiposOferta;
	}

	public void setTiposOferta(List<SelectItem> tiposOferta) {
		this.tiposOferta = tiposOferta;
	}

	public List<SelectItem> getGruposSegmento() {
		return gruposSegmento;
	}

	public void setGruposSegmento(List<SelectItem> gruposSegmento) {
		this.gruposSegmento = gruposSegmento;
	}

	public List<SelectItem> getEstados() {
		return estados;
	}

	public void setEstados(List<SelectItem> estados) {
		this.estados = estados;
	}

	public Long getProductoSeleccionado() {
		return productoSeleccionado;
	}

	public void setProductoSeleccionado(Long productoSeleccionado) {
		this.productoSeleccionado = productoSeleccionado;
	}

	public Long getTareaSeleccionado() {
		return tareaSeleccionado;
	}

	public void setTareaSeleccionado(Long tareaSeleccionado) {
		this.tareaSeleccionado = tareaSeleccionado;
	}

	public Long getTipoOfertaSeleccionado() {
		return tipoOfertaSeleccionado;
	}

	public void setTipoOfertaSeleccionado(Long tipoOfertaSeleccionado) {
		this.tipoOfertaSeleccionado = tipoOfertaSeleccionado;
	}

	public Long getGrupoSegmentoSeleccionado() {
		return grupoSegmentoSeleccionado;
	}

	public void setGrupoSegmentoSeleccionado(Long grupoSegmentoSeleccionado) {
		this.grupoSegmentoSeleccionado = grupoSegmentoSeleccionado;
	}

	public Long getEstadoSeleccionado() {
		return estadoSeleccionado;
	}

	public void setEstadoSeleccionado(Long estadoSeleccionado) {
		this.estadoSeleccionado = estadoSeleccionado;
	}

	public BigDecimal getVerdeMinutos() {
		return verdeMinutos;
	}

	public void setVerdeMinutos(BigDecimal verdeMinutos) {
		this.verdeMinutos = verdeMinutos;
	}

	public BigDecimal getAmarilloMinutos() {
		return amarilloMinutos;
	}

	public void setAmarilloMinutos(BigDecimal amarilloMinutos) {
		this.amarilloMinutos = amarilloMinutos;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public boolean isNuevoRegistro() {
		return nuevoRegistro;
	}

	public void setNuevoRegistro(boolean nuevoRegistro) {
		this.nuevoRegistro = nuevoRegistro;
	}

}
