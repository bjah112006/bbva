package com.ibm.mant.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractSortPagDataTableMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.ctacte.bean.PerfilXNivel;
import com.ibm.bbva.ctacte.bean.PerfilXNivelPK;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.dao.PerfilXNivelDAO;
import com.ibm.mant.tabla.vo.PerfilXNivelVO;

@ManagedBean(name="busquedaAsignacionPerfiles")
@SessionScoped
public class BusquedaAsignacionPerfilesUI extends AbstractSortPagDataTableMBean {
	
	private static final long serialVersionUID = 1310823112814534353L;
	
	private static final Logger LOG = LoggerFactory.getLogger(BusquedaAsignacionPerfilesUI.class);
	
	private List<SelectItem> lstNiveles;
	private String nivelSeleccionado;
	private String valor;
	private List<PerfilXNivel> listaPerfilXNivel;
	private List<PerfilXNivelVO> listaPerfilXNivelVO;
	private boolean itemSeleccionado;
	
	@EJB
	private PerfilXNivelDAO perfilXNivelDAO;
	
	@PostConstruct
	public void init() {
		LOG.info("init()");
		
		lstNiveles = new ArrayList<SelectItem>();
		lstNiveles.add(new SelectItem(ConstantesAdmin.CODIGO_CAMPO_VACIO, "--seleccione--"));
		lstNiveles.add(new SelectItem("1", "Usuario"));
		lstNiveles.add(new SelectItem("2", "Puesto"));
		lstNiveles.add(new SelectItem("3", "Oficina"));
		
		load();
		limpiar();
	}
	
	public void load() {
		listaPerfilXNivel = perfilXNivelDAO.search(null);
		mostrarTabla(listaPerfilXNivel);
	}
	
	public String limpiar() {
		this.nivelSeleccionado = ConstantesAdmin.CODIGO_CAMPO_VACIO;
		this.valor = "";
		
		for(PerfilXNivelVO item : listaPerfilXNivelVO){
			item.setSeleccion(false);
		}
		
		removeObjectSession(Constantes.PERFIL_X_NIVEL_SESION);
		
		return null;
	}
	
	public String buscar() {
		PerfilXNivel filtro = new PerfilXNivel();
		filtro.setId(new PerfilXNivelPK());
		
		if (!nivelSeleccionado.equals(ConstantesAdmin.CODIGO_CAMPO_VACIO)) {
			filtro.getId().setTipoNivel(Long.parseLong(nivelSeleccionado));
		}
		if (valor != null && !valor.trim().equals("")) {
			filtro.getId().setValor(valor);
		}
		
		listaPerfilXNivel = perfilXNivelDAO.search(filtro);
		mostrarTabla(listaPerfilXNivel);
		
		return null;
	}
	
	public String agregar() {
		removeObjectSession(Constantes.PERFIL_X_NIVEL_SESION);
		
		return "/mantenimiento/asignacionPerfiles/actualizacion?faces-redirect=true";
	}
	
	public String editar() {
		if (itemSeleccionado) {
			return "/mantenimiento/asignacionPerfiles/actualizacion?faces-redirect=true";
		} else {
			removeObjectSession(Constantes.PERFIL_X_NIVEL_SESION);
			
			return null;
		}
	}
	
	public String eliminar() {
		LOG.info("eliminar()");
		if (itemSeleccionado){
			PerfilXNivel item = (PerfilXNivel) getObjectSession(Constantes.PERFIL_X_NIVEL_SESION);
			try {
				perfilXNivelDAO.delete(item);
				perfilXNivelDAO.actualizarPerfilEmpleados(item, true);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				mensajeError(null, "Error en BBDD al intentar ejecutar la operación.");
			}
			
			if (listaPerfilXNivelVO != null) {
				int i = 0;
				for (PerfilXNivelVO o : listaPerfilXNivelVO){
					if (o.isSeleccion()) {
						o.setSeleccion(false);
						break;
					}
					i++;
				}
				listaPerfilXNivelVO.remove(i);
			}
			
			removeObjectSession(Constantes.CARTERIZACION_SESION);
			itemSeleccionado = false;
		}
		
		return null;
	}
	
	public String cancelar() {
		return "/mantenimiento/seleccionarTabla?faces-redirect=true";
	}
	
	public void mostrarTabla(List<PerfilXNivel> listaPerfilXNivel) {

		itemSeleccionado = false;
		removeObjectSession(Constantes.PERFIL_X_NIVEL_SESION);

		if (listaPerfilXNivel.size() > 0) {
			listaPerfilXNivelVO = new ArrayList<PerfilXNivelVO>();

			for (int i = 0; i < listaPerfilXNivel.size(); i++) {

				PerfilXNivel perfilXNivel = listaPerfilXNivel.get(i);
				PerfilXNivelVO perfilXNivelVO = new PerfilXNivelVO();

				if (perfilXNivel != null) {

					if (perfilXNivel.getId().getTipoNivel() == 1) {
						perfilXNivelVO.setNivelDesc("Usuario");
					} else if (perfilXNivel.getId().getTipoNivel() == 2) {
						perfilXNivelVO.setNivelDesc("Puesto");
					} else if (perfilXNivel.getId().getTipoNivel() == 3) {
						perfilXNivelVO.setNivelDesc("Oficina");
					}

					perfilXNivelVO.setTipoNivel(perfilXNivel.getId().getTipoNivel());
					perfilXNivelVO.setValor(perfilXNivel.getId().getValor());

					if (perfilXNivel.getPerfil() != null)
						perfilXNivelVO.setPerfilDesc(perfilXNivel.getPerfil().getDescripcion());
					
					perfilXNivelVO.setFlagActivo(perfilXNivel.getFlagActivo().equals(Constantes.CHECK_SELECCIONADO));
					
				}
				
				listaPerfilXNivelVO.add(perfilXNivelVO);
			}
		} else {
			listaPerfilXNivelVO = new ArrayList<PerfilXNivelVO>();
		}
	}
	
	public void cargarSeleccion(AjaxBehaviorEvent event) {
		LOG.info("cargarSeleccion(AjaxBehaviorEvent event)");
		FacesContext ctx = FacesContext.getCurrentInstance();
		long tipoNivel = ctx.getApplication().evaluateExpressionGet(ctx, "#{item.tipoNivel}", Long.class).longValue();
		String valor = ctx.getApplication().evaluateExpressionGet(ctx, "#{item.valor}", String.class);
		boolean seleccion = ctx.getApplication().evaluateExpressionGet(ctx, "#{item.seleccion}", Boolean.class).booleanValue();
		LOG.info("tipoNivel: " + tipoNivel);
		LOG.info("valor: " + valor);
		LOG.info("seleccion: " + seleccion);
		
		itemSeleccionado = seleccion;
		
		if (seleccion) {
			PerfilXNivel item;
			PerfilXNivelPK id = new PerfilXNivelPK();
			
			id.setTipoNivel(tipoNivel);
			id.setValor(valor);
			
			item = perfilXNivelDAO.load(id);
			
			addObjectSession(Constantes.PERFIL_X_NIVEL_SESION, item);
		} else {
			removeObjectSession(Constantes.PERFIL_X_NIVEL_SESION);
		}
	}

	@Override
	public void ordenar(ActionEvent event) {
	}
	
	public List<SelectItem> getLstNiveles() {
		return lstNiveles;
	}

	public void setLstNiveles(List<SelectItem> lstNiveles) {
		this.lstNiveles = lstNiveles;
	}

	public String getNivelSeleccionado() {
		return nivelSeleccionado;
	}

	public void setNivelSeleccionado(String nivelSeleccionado) {
		this.nivelSeleccionado = nivelSeleccionado;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public List<PerfilXNivel> getListaPerfilXNivel() {
		return listaPerfilXNivel;
	}

	public void setListaPerfilXNivel(List<PerfilXNivel> listaPerfilXNivel) {
		this.listaPerfilXNivel = listaPerfilXNivel;
	}

	public List<PerfilXNivelVO> getListaPerfilXNivelVO() {
		return listaPerfilXNivelVO;
	}

	public void setListaPerfilXNivelVO(List<PerfilXNivelVO> listaPerfilXNivelVO) {
		this.listaPerfilXNivelVO = listaPerfilXNivelVO;
	}

	public boolean isItemSeleccionado() {
		return itemSeleccionado;
	}

	public void setItemSeleccionado(boolean itemSeleccionado) {
		this.itemSeleccionado = itemSeleccionado;
	}

}
