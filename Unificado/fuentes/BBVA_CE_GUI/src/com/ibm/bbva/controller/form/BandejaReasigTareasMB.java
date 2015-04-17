package com.ibm.bbva.controller.form;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.Consulta;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.controller.AbstractLinksMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.BuscarBandejaAsigMB;
import com.ibm.bbva.controller.common.MenuMB;
import com.ibm.bbva.controller.common.TablaBandejaAsigMB;
import com.ibm.bbva.controller.common.TablaBandejaPendMB;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.session.DevolucionTareaBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.util.ExpedienteTCWrapper;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "bandejaReasigTareas")
@RequestScoped
public class BandejaReasigTareasMB extends AbstractLinksMBean {
	
	@EJB
	EmpleadoBeanLocal empleadoBean;
	@EJB
	PerfilBeanLocal perfilBean;
	@EJB
	ExpedienteBeanLocal expedienteBeanLocalBean;
	
	private boolean desAsignar;
	private String codUsuario;
	private List<String> listaExpedientes;
	private static final Logger LOG = LoggerFactory.getLogger(BandejaReasigTareasMB.class);
	private String strExpedientes;
	private boolean bolEstadoExp;
	private boolean actEstadoExp;
	private boolean deshabilitar;
	private String validacionBuscarOK;
	private String strResultado;
	
	public BandejaReasigTareasMB() {
		desAsignar=true;
		LOG.info("desAsignar:::"+desAsignar);
	}
	
	
	public void buscar(AjaxBehaviorEvent event) {	
		String idMsgError = "frmBandejaReasigTareas";
		FacesContext ctx = FacesContext.getCurrentInstance();
		BuscarBandejaAsigMB busqueda = (BuscarBandejaAsigMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaAsig");
		LOG.info("this.deshabilitar:::"+this.deshabilitar);
		LOG.info("this.isActEstadoExp():::"+this.isActEstadoExp());
		if(this.isActEstadoExp() || this.deshabilitar){
			this.setActEstadoExp(false);
			//LOG.info("mensaje de información");
			//addMessageError(idMsgError + ":datoAsignacion", "com.ibm.bbva.common.buscarBandejaHist.msgAsignacionExpediente");
		}
		if (busqueda.valida("2")){
			busqueda.buscar();
		}
		
		this.setValidacionBuscarOK("2");
		this.deshabilitar=false;
		addObjectSession(Constantes.CONSULTA_BANDASIGN_ASIGNA, true);
		
		//return null;
	}
	
	public String limpiar() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		BuscarBandejaAsigMB buscarBandejaMB = (BuscarBandejaAsigMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaAsig");					
		buscarBandejaMB.limpiar();
		addObjectSession(Constantes.CONSULTA_BANDASIGN_ASIGNA, true);
		
		return null;
	}
	
	public String validarBusqueda(){
		FacesContext ctx = FacesContext.getCurrentInstance();
		BuscarBandejaAsigMB busqueda = (BuscarBandejaAsigMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaAsig");
		
		if (busqueda.valida("1")){
			this.setValidacionBuscarOK("1");
			Consulta consulta=busqueda.crearObjetoConsulta();
			addObjectSession(Constantes.CONSULTA_BANDASIGN, consulta);
		}else
			this.setValidacionBuscarOK("2");
		
		LOG.info("busqueda::::"+this.getValidacionBuscarOK());
		
		return null;
	}
	
	public String validar(){
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		TablaBandejaAsigMB tablaBandejaAsig = (TablaBandejaAsigMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "tablaBandejaAsig");
		tablaBandejaAsig.setListTabla((List<ExpedienteTCWrapper>)getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION));
		
		BuscarBandejaAsigMB busqueda = (BuscarBandejaAsigMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaAsig");
		LOG.info("busqueda.getUsuarioAsigSeleccionado(): " + busqueda.getUsuarioAsigSeleccionado());
		addObjectSession(Constantes.USUARIO_ASIGNADO, busqueda.getUsuarioAsigSeleccionado());
		
		strExpedientes="";
		listaExpedientes=new ArrayList<String>();
		
		if(tablaBandejaAsig.getListTabla()!=null && tablaBandejaAsig.getListTabla().size()>0){
			
			for (ExpedienteTCWrapper expTC : tablaBandejaAsig.getListTabla()){
				if (expTC.isSeleccion()){
					if(expTC.getCodigo()!=null && !expTC.getCodigo().trim().equals("")){
						Expediente objExp=expedienteBeanLocalBean.buscarPorIdExpediente(Long.parseLong(expTC.getCodigo()));
						
						if(objExp!=null && objExp.getFlagActivo()!=null && objExp.getFlagActivo().trim().equals(Constantes.FLAG_ESTADO_ACTIVO_EXPEDIENTE)){
							//Se agregan los expedientes que se estan trabajando
							LOG.info("expTC.getCodigo()::::"+expTC.getCodigo());
							listaExpedientes.add(expTC.getCodigo());
						}
					}
				}
			}
			addObjectSession(Constantes.BOTON_BANDASIGN_ASIGNA, true);
			
			if(listaExpedientes.size()>0){
				addObjectSession(Constantes.LIS_BANDASIGN_ASIGNA, listaExpedientes);
				for(String str : listaExpedientes)
					strExpedientes=strExpedientes+str+" - ";
				
				strExpedientes=strExpedientes.substring(0, strExpedientes.length()-3);	
			}
			
			addObjectSession(Constantes.MSJ_BANDASIGN_ASIGNA, strExpedientes);
			LOG.info("strExpedientes::::"+strExpedientes);
			LOG.info("bolEstadoExp::::"+bolEstadoExp);	
			this.setDeshabilitar(false);
			
		}

		return null;
	}
	
	public void inhabilitarActivosExp(){
		listaExpedientes = (List<String>) getObjectSession(Constantes.LIS_BANDASIGN_ASIGNA);
		LOG.info("strExpedientes:::"+strExpedientes);	
		
		if(listaExpedientes!=null && listaExpedientes.size()>0){
			for(String item: listaExpedientes){
				String idExpediente = item;
				LOG.info("idExpediente ::: "+idExpediente);
				
				if(idExpediente!=null && !idExpediente.trim().equals("")){
					//Actualiza estado activo de Expediente para el momento de la reasignación			
					expedienteBeanLocalBean.actualizarEstadoExpediente(Constantes.FLAG_ESTADO_INACTIVO_EXPEDIENTE, Long.parseLong(idExpediente));
					LOG.info("Inactivado ::: "+idExpediente);
				}			
			}			
		}else{
			LOG.info("listaExpedientes vacio");
		}

	}
	
	public void asignar(AjaxBehaviorEvent event) {
		LOG.info("Entro!!");

		String idMsgError = "frmBandejaReasigTareas";
		addObjectSession(Constantes.BOTON_BANDASIGN_ASIGNA, false);
		addObjectSession(Constantes.CONSULTA_BANDASIGN_ASIGNA, true);
		inhabilitarActivosExp();
		// validacion de carterizacion
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		MenuMB menu = (MenuMB)getObjectRequest("menu");
		String carterizacion = menu.verificarExisteCarterizacion(empleado.getOficina().getTerritorio().getId(), empleado.getId());		
		if (carterizacion.equals(Constantes.NO_EXISTE_CARTERIZACION) ) {			 
			addMessageError(idMsgError + ":datoReasignar", "com.ibm.bbva.bandejaReasigTareas.formBandejaReasigTareas.mensajeError");		
			//return null;
		}else{
			FacesContext ctx = FacesContext.getCurrentInstance();
			TablaBandejaAsigMB tablaBandejaAsig = (TablaBandejaAsigMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "tablaBandejaAsig");
			tablaBandejaAsig.setListTabla((List<ExpedienteTCWrapper>)getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION));
			eliminarObjetosSession();
			codUsuario=(String)getObjectSession(Constantes.COD_USUARIO_ASIGNAR);
			String usuarioAsignado = "";
			

			for (ExpedienteTCWrapper expTC : tablaBandejaAsig.getListTabla()) {
				LOG.info("expTC.isSeleccion: " + expTC.isSeleccion());
				if (expTC.isSeleccion()) {	
					//BuscarBandejaAsigMB busqueda = (BuscarBandejaAsigMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaAsig");
					String usuSeleccionado=(String)getObjectSession(Constantes.USUARIO_ASIGNADO);					
					LOG.info("busqueda.getUsuarioAsigSeleccionado(): " + usuSeleccionado);
					if(!Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(usuSeleccionado)){
						//usuarioAsignado = Util.obtenerDescripcion(busqueda.getUsuariosAsig(), busqueda.getUsuarioAsigSeleccionado());
						usuarioAsignado=usuSeleccionado;
						if (!Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(usuarioAsignado)) {
							RemoteUtils tareasBDelegate = new RemoteUtils();
							//expTC.getExpedienteTC().setCodigoUsuarioActual(expTC.getCodigoUsuarioActual());
							LOG.info("Expediente: " + expTC.getCodigo() + " Task: " + expTC.getTaskID());
							LOG.info("USUARIO ASIGNADO = "+usuarioAsignado);
							Empleado empl=empleadoBean.buscarPorCodigo(usuarioAsignado);
							
							LOG.info("ID PERFIL = "+empl.getPerfil().getId());
							Perfil perfil=perfilBean.buscarPorId(empl.getPerfil().getId());
							tareasBDelegate.transferirTarea(expTC.getCodigoUsuarioActual(),	usuarioAsignado, expTC.getTaskID());
							LOG.info("Transferencia exitosa para expediente:" + expTC.getCodigo());
							expTC.setIdPerfilUsuarioActual(String.valueOf(empl.getPerfil().getId()));
							expTC.setPerfilUsuarioActual(perfil.getDescripcion());
							expTC.setCodigoUsuarioActual(usuarioAsignado);
							
							expTC.getExpedienteTC().setNombreUsuarioActual(empl.getNombresCompletos());
							expTC.getExpedienteTC().setIdPerfilUsuarioActual(String.valueOf(empl.getPerfil().getId()));
							expTC.getExpedienteTC().setPerfilUsuarioActual(perfil.getDescripcion());
							expTC.getExpedienteTC().setCodigoUsuarioActual(usuarioAsignado);						
							tareasBDelegate.enviarExpedienteTC(expTC.getTaskID(), expTC.getExpedienteTC());
							LOG.info("Envio exitoso para expediente:" + expTC.getCodigo());
							
							LOG.info("Actualizacion de Empleaso Asignacion");
							LOG.info("Empleado Asignado: " + empl.getId());
							actualizaEmpleadoExpediente(empl.getId(), Long.valueOf(expTC.getCodigo()));
							expTC.setSeleccion(false);
							/*
							 * Actualizar parametros en la clase ExpedienteTCWPS con el codigo actual expTC.getCodigoUsuarioActual() de usuario 
							 *  y pasarlo al metodo enviarExpedienteTC del RemoteUtils
							 * **/						
							//BuscarBandejaAsigMB busquedas = (BuscarBandejaAsigMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaAsig");
							//busquedas.buscar();
							//return null;
						}
					}else{
						//BuscarBandejaAsigMB busquedas = (BuscarBandejaAsigMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaAsig");
						//busquedas.buscar();
						//return null;				
					}
				}		
		    }
			//LOG.info("mensaje de información");
			//addMessageError(idMsgError + ":datoAsignacion", "com.ibm.bbva.common.buscarBandejaHist.msgAsignacionExpediente");
			
			ctx = FacesContext.getCurrentInstance();
			BuscarBandejaAsigMB busqueda = (BuscarBandejaAsigMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaAsig");

			addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, tablaBandejaAsig.getListTabla());
			//removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
			busqueda.buscar();
			this.setStrResultado("Asignación satisfactoria");
			this.setActEstadoExp(false);
			this.setDeshabilitar(false);
			List<Empleado> listaEmp = new ArrayList<Empleado>();
			busqueda.cargarListaAsignar(listaEmp);			
		}
		
	}
	
	public boolean actualizaEmpleadoExpediente(Long idEmpleado, Long idExpediente){
		LOG.info("actualizaEmpleadoExpediente >>>> idEmpleado : "+idEmpleado+" idExpediente : "+idExpediente);	

		Expediente objExpediente=expedienteBeanLocalBean.buscarPorId(idExpediente);
		if(objExpediente!=null){
			objExpediente.setEmpleado(new Empleado());
			objExpediente.getEmpleado().setId(idEmpleado);
			expedienteBeanLocalBean.edit(objExpediente);	
			return true;
		}
		return false;		
    }
	
	public String cancelar() {		
		return "/index?faces-redirect=true";
	}
	public boolean isDesAsignar() {
		desAsignar = (Boolean) getObjectSession(Constantes.CONSULTA_BANDASIGN_ASIGNA);
		LOG.info("DesAsignar1:::"+desAsignar);
		return desAsignar;
	}
	public void setDesAsignar(boolean desAsignar) {
		this.desAsignar = desAsignar;
	}


	public List<String> getListaExpedientes() {
		return listaExpedientes;
	}


	public void setListaExpedientes(List<String> listaExpedientes) {
		this.listaExpedientes = listaExpedientes;
	}


	public String getStrExpedientes() {
		strExpedientes = (String) getObjectSession(Constantes.MSJ_BANDASIGN_ASIGNA);
		LOG.info("strExpedientes:::"+strExpedientes);			
		return strExpedientes==null?"":strExpedientes;
	}


	public void setStrExpedientes(String strExpedientes) {
		this.strExpedientes = strExpedientes;
	}


	public boolean isBolEstadoExp() {
		bolEstadoExp = (Boolean) getObjectSession(Constantes.BOTON_BANDASIGN_ASIGNA);
		LOG.info("isBolEstadoExp:::"+bolEstadoExp);	
		return bolEstadoExp;
	}


	public void setBolEstadoExp(boolean bolEstadoExp) {
		this.bolEstadoExp = bolEstadoExp;
	}


	public boolean isActEstadoExp() {
		//actEstadoExp = (Boolean) getObjectSession(Constantes.ACT_BANDASIGN_ASIGNA);
		LOG.info("actEstadoExp:::"+actEstadoExp);			
		return actEstadoExp;
	}


	public void setActEstadoExp(boolean actEstadoExp) {
		this.actEstadoExp = actEstadoExp;
	}


	public boolean isDeshabilitar() {
		//deshabilitar = (Boolean) getObjectSession(Constantes.CONSULTA_BANDASIGN_INAH);
		LOG.info("deshabilitar get:::"+deshabilitar);		
		return deshabilitar;
	}


	public void setDeshabilitar(boolean deshabilitar) {
		LOG.info("deshabilitar seteo:::"+deshabilitar);
		this.deshabilitar = deshabilitar;
	}


	public String getValidacionBuscarOK() {
		return validacionBuscarOK;
	}


	public void setValidacionBuscarOK(String validacionBuscarOK) {
		this.validacionBuscarOK = validacionBuscarOK;
	}


	public String getStrResultado() {
		return strResultado;
	}


	public void setStrResultado(String strResultado) {
		this.strResultado = strResultado;
	}

}