package com.ibm.bbva.controller.mantenimiento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.component.UIComponent;
import javax.faces.convert.ConverterException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.DatosCorreo;
import com.ibm.bbva.entities.DatosCorreoDestin;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.session.DatosCorreoDestinBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;


@ManagedBean (name="tablaDestinatarios")
@SessionScoped
public class tablaDestinatariosUI extends AbstractMBean{
	
	private static final Logger LOG = LoggerFactory.getLogger(tablaDestinatariosUI.class);
	
	@EJB
	private DatosCorreoDestinBeanLocal datosCorreoDestinBean;
	@EJB
	private PerfilBeanLocal perfilBean;
	
	private DatosCorreo datosCorreo;
	private List<Perfil> rol;
	private List<Perfil> rolesEliminar;
	private List<Perfil> roles;
	private List<Perfil> perfilAux;
	private List<Perfil> rolesAux;
	private List<SelectItem> items;  
	private List<String> source;
	private List<SelectItem> destination;
	private List<String> selected;
	private List<Perfil> rolesDestino;
	private List<Perfil> rolesDevolver; // roles que son devueltos en la accion del boton eliminar
	private List<Perfil> listaDestino;
	private List<Perfil> rolesAgregado;
	private boolean ocultarEliminar=true;
	
	@PostConstruct
	public void init(){
		roles=perfilBean.buscarTodos();
		datosCorreo=(DatosCorreo)getObjectSession(Constantes.DATOS_CORREO_SESION);
	} 
	
	public void inicio(DatosCorreo cabecera){
		if (cabecera.getId()==0){
			//roles=perfilBean.buscarTodos();
			roles=perfilBean.buscarPorProceso();
			items=new ArrayList<SelectItem>(roles.size());  
			for(Perfil role : roles)  
			 items.add(new SelectItem(role.getId(), role.getDescripcion()));  
			destination= new ArrayList<SelectItem>();
			ocultarEliminar=true;
		}else{
			recuperarListas(cabecera);
			ocultarEliminar=true;
		}
		 
	}
	
	public void recuperarListas(DatosCorreo cabecera){
		FacesContext ctx =  FacesContext.getCurrentInstance();
		
		destination=new ArrayList<SelectItem>(); 
		List<DatosCorreoDestin> listaDestinatarios=new ArrayList<DatosCorreoDestin>();
		listaDestinatarios=datosCorreoDestinBean.buscarPorIdDatosCorreo(cabecera.getId());
		/*RECUPERARA LA LISTA DE LA DERECHA*/
		listaDestino=new ArrayList<Perfil>();
		if (listaDestinatarios.size()>0){
			for (int i=0 ; i<listaDestinatarios.size();i++){
				Perfil perfil = new Perfil();
				if (listaDestinatarios.get(i).getPerfil()!=null){
					perfil.setId(Long.valueOf(listaDestinatarios.get(i).getPerfil().getCodigo()));
					perfil.setCodigo(listaDestinatarios.get(i).getPerfil().getCodigo());
					perfil.setDescripcion(listaDestinatarios.get(i).getPerfil().getDescripcion());
					listaDestino.add(perfil);
				}
			}
			rolesEliminar=listaDestino;
			for(Perfil role : listaDestino)  
				 destination.add(new SelectItem(role.getId(), role.getDescripcion()));
			/*AHORA RECUPERARA LA LISTA DE LA IZQUIERDA*/
			perfilAux=perfilBean.buscarTodos();
			
			List<Perfil> rolesOrigenTemp=new ArrayList<Perfil>();
			boolean eliminar;
			for (Perfil item1 : perfilAux){
				eliminar=false;
				for (Perfil item2:listaDestino){
					if (item1.getCodigo().equals(item2.getCodigo())){
						eliminar=true;
					}
				}
				if (!eliminar){
					rolesOrigenTemp.add(item1);
				}
			}
			roles=rolesOrigenTemp;
			items=new ArrayList<SelectItem>(rolesOrigenTemp.size());  
			 for(Perfil role : rolesOrigenTemp) 
				 items.add(new SelectItem(role.getId(), role.getDescripcion()));
			 LOG.info("tamaño despues de borrar" + items.size());
		}
		
		ctx = FacesContext.getCurrentInstance();
		actualizarCorreoUI actualizarCorreo = (actualizarCorreoUI)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "actualizarCorreo");
		//destinatario.recuperarListas(datosCorreo);
		actualizarCorreo.setLstAuxDestinatarios(destination);
	}
	
	public void cargarDestino(AjaxBehaviorEvent event){
		LOG.info("destination size");
	}
	
	public List<Perfil> getRoles() {
		return roles;
	}
	
	public void setRoles(List<Perfil> roles) {
		this.roles = roles;
	}
	
	public List<SelectItem> getItems() {
		return items;
	}
	
	public void setItems(List<SelectItem> items) {
		this.items = items;
	}
	
	public List<SelectItem> getDestination() {
		return destination;
	}
	
	public void setDestination(List<SelectItem> destination) {
		this.destination = destination;
	}
	
	public List<Perfil> getRolesDestino() {
		return rolesDestino;
	}
	
	public void setRolesDestino(List<Perfil> rolesDestino) {
		this.rolesDestino = rolesDestino;
	}
	
	public PerfilBeanLocal getPerfilBean() {
		return perfilBean;
	}

	public void setPerfilBean(PerfilBeanLocal perfilBean) {
		this.perfilBean = perfilBean;
	}

	public List<Perfil> getRolesAux() {
		return rolesAux;
	}

	public void setRolesAux(List<Perfil> rolesAux) {
		this.rolesAux = rolesAux;
	}

	public List<Perfil> getRolesDevolver() {
		return rolesDevolver;
	}

	public void setRolesDevolver(List<Perfil> rolesDevolver) {
		this.rolesDevolver = rolesDevolver;
	}

	public List<Perfil> getPerfilAux() {
		return perfilAux;
	}

	public void setPerfilAux(List<Perfil> perfilAux) {
		this.perfilAux = perfilAux;
	}

	public List<Perfil> getListaDestino() {
		return listaDestino;
	}

	public void setListaDestino(List<Perfil> listaDestino) {
		this.listaDestino = listaDestino;
	}

	public List<Perfil> getRol() {
		return rol;
	}

	public void setRol(List<Perfil> rol) {
		this.rol = rol;
	}

	public DatosCorreo getDatosCorreo() {
		return datosCorreo;
	}

	public void setDatosCorreo(DatosCorreo datosCorreo) {
		this.datosCorreo = datosCorreo;
	}

	public List<Perfil> getRolesAgregado() {
		return rolesAgregado;
	}

	public void setRolesAgregado(List<Perfil> rolesAgregado) {
		this.rolesAgregado = rolesAgregado;
	}

	public List<Perfil> getRolesEliminar() {
		return rolesEliminar;
	}

	public void setRolesEliminar(List<Perfil> rolesEliminar) {
		this.rolesEliminar = rolesEliminar;
	}

	public boolean isOcultarEliminar() {
		return ocultarEliminar;
	}

	public void setOcultarEliminar(boolean ocultarEliminar) {
		this.ocultarEliminar = ocultarEliminar;
	}

	public List<String> getSource() {
		return source;
	}

	public void setSource(List<String> source) {
		this.source = source;
	}

	public List<String> getSelected() {
		return selected;
	}

	public void setSelected(List<String> selected) {
		this.selected = selected;
	}
	
	
}
