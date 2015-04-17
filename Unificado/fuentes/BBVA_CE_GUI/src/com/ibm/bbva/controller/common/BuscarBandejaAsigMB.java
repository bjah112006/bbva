package com.ibm.bbva.controller.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Comparator;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.Consulta;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.EstadoCE;
import com.ibm.bbva.entities.EstadoTareaCE;
import com.ibm.bbva.entities.Mensajes;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.entities.Mensajes;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.entities.Territorio;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.EstadoCEBeanLocal;
import com.ibm.bbva.session.EstadoTareaCEBeanLocal;
import com.ibm.bbva.session.MensajesBeanLocal;
import com.ibm.bbva.session.OficinaBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.PosibleValorBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.session.TerritorioBeanLocal;
import com.ibm.bbva.util.ExpedienteTCWrapper;
import com.ibm.bbva.util.Util;

/**
 * Esta clase ha sido refactorizada
 * Antes era un @RequestScoped donde el método init() anotado
 * con @PostConstruct se llamaba cada vez que se invocaba algún método
 * por medio de AJAX.
 * 
 * @author devniel
 *
 */
@SuppressWarnings("serial")
@ManagedBean(name = "buscarBandejaAsig")

@RequestScoped
public class BuscarBandejaAsigMB extends AbstractMBean {
	
	@EJB
	private EmpleadoBeanLocal empleadoBean;
	@EJB
	private PerfilBeanLocal perfilBean;	
	@EJB
	private TerritorioBeanLocal territorioBean;
	@EJB
	private TareaBeanLocal tareaBean;
	@EJB
	private OficinaBeanLocal oficinaBean;
	@EJB
	private EstadoCEBeanLocal estadoCEBean;	
	@EJB
	private EstadoTareaCEBeanLocal estadoTareaCEBean;	
	@EJB
	private PosibleValorBeanLocal posibleValorBean;	
	@EJB
	private MensajesBeanLocal mensajeBean;
	
	private Empleado empleado;
	private List<SelectItem> tiposFechas;
	private List<SelectItem> usuarios;
	private List<SelectItem> usuariosAsig;
	private List<SelectItem> roles;
	private List<SelectItem> tareas;
	private List<SelectItem> estados;
	private List<SelectItem> oficinas;
	private String tipoFechaSeleccionado;
	private String usuarioSeleccionado;
	private String usuarioSeleccionado2;
	private String usuarioAsigSeleccionado;
	private String rolSeleccionado;
	private String estadoSeleccionado;
	private String tareaSeleccionada;
	private String oficinaSeleccionada;
	
	private String textoMensajeSinFiltro;
	
	private String codigoExpediente;
	private String oficina;
	private Date fechaInicio;
	private Date fechaFin;

	private boolean mostrarXusuario;
	private static final Logger LOG = LoggerFactory.getLogger(BuscarBandejaAsigMB.class);

	public BuscarBandejaAsigMB() {

	}
	
	@PostConstruct
	public void init() {
		empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		LOG.info("empleado.getPerfil().getId(): "+empleado.getPerfil().getId());
		LOG.info("empleado.getOficina().getId(): "+empleado.getOficina().getId());
		LOG.info("empleado.getPerfil().getFlagAdministracion(): "+empleado.getPerfil().getFlagAdministracion());
		
		tiposFechas = Util.listaVacia();
		oficina=Constantes.CODIGO_OFICINA;		
		tareas = Util.listaVacia();
		estados = Util.listaVacia();
		usuarios=Util.listaVacia();
		usuariosAsig=Util.listaVacia();
		oficinas = Util.listaVacia();

		usuarioAsigSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		oficinaSeleccionada = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		rolSeleccionado = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		estadoSeleccionado= Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		
		usuarioSeleccionado2=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		usuarioSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		tareaSeleccionada=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		/*Carga Lista de Roles*/
		if(roles==null || roles.size()<=0){
			if(empleado.getPerfil().getFlagAdministracion().equals("1")){
				roles = Util.crearItems(perfilBean.buscarPorProceso(), true, "id", "descripcion");
			}else{
				roles = Util.crearItems(perfilBean.buscarPorIdJefe(empleado.getPerfil().getId()), true, "id", "descripcion");
			}
		}
		
		if(Util.isListaVacia(tiposFechas)){
			//PARA QUITAR FECHA DE REGISTRO DE COMBO TIPO_OFERTA - 26_FEB_2015
			/*tiposFechas=Util.crearItems(posibleValorBean.buscarPorIdColumna(Long.parseLong(Constantes.ID_POSIBLE_VALOR_PERFIL_TIPO_FECHA)), 
					true, "valor", "etiqueta");*/			
			//SOLO PARA MOSTRAR FECHA DE LLEGADA COMO OPCION EN COMBO TIPO_OFERTA - 26_FEB_2015
			tiposFechas=Util.crearItems(posibleValorBean.buscarPorIdColumnaIdValor(Long.parseLong(Constantes.ID_POSIBLE_VALOR_PERFIL_TIPO_FECHA), "2"), 
					true, "valor", "etiqueta");
			//tipoFechaSeleccionado="2";
		}

		
		/* Cargar lista de tareas */
		tareas = (List<SelectItem>) getObjectSession(Constantes.LISTA_USUARIOS_TAREA_ASIG);
		oficinas=(List<SelectItem>) getObjectSession(Constantes.LISTA_OFICINAS_CARGADAS);	
		usuarios=(List<SelectItem>) getObjectSession(Constantes.LISTA_USUARIOS_ROL_ASIG);
		
		LOG.info("tareaSeleccionada .... "+tareaSeleccionada);

		if(tareas!=null && tareas.size()>1){
			tareaSeleccionada= (String) getObjectSession(Constantes.CODIGO_TAREA_ASIGNADO);
			if(tareaSeleccionada!=null && !tareaSeleccionada.equals("-1")){
				estados = (List<SelectItem>) getObjectSession(Constantes.LISTA_TAREA_ESTADO_ASIG);
				if(Util.isListaVacia(estados)){
					buscarEstado(null);
					LOG.info("estados ="+estados.size());
				}else
					LOG.info("estados nula o vacia");
				
				LOG.info("tarea ="+tareas.size());				
			}

		}else
			LOG.info("tarea nula o vacia");

		Consulta objCconsulta = (Consulta) getObjectSession(Constantes.CONSULTA_BANDASIGN);
		boolean op = (Boolean) getObjectSession(Constantes.CONSULTA_BANDASIGN_EXP);

		if(op && objCconsulta!=null){
			LOG.info("entro objConsulta");
			addObjectSession(Constantes.CONSULTA_BANDASIGN_EXP, false);
			codigoExpediente=objCconsulta.getCodigoExpediente()==null?null:Util.validarCampo (objCconsulta.getCodigoExpediente());
			fechaInicio=objCconsulta.getFechaInicio();
			fechaFin=objCconsulta.getFechaFin();
			tipoFechaSeleccionado=objCconsulta.getCodTipoFecha()==null?Constantes.CODIGO_CODIGO_CAMPO_VACIO:objCconsulta.getCodTipoFecha();
			rolSeleccionado=objCconsulta.getIdPerfilUsuarioActual();
			oficina=objCconsulta.getFlagProvincia();
			LOG.info("oficina init:::"+oficina);
			oficinaSeleccionada=objCconsulta.getIdOficina();
			
			List<String> usr = objCconsulta.getUsuarios();
			if(usr!=null && usr.size()>0)
				usuarioSeleccionado=usr.get(0);
			
			usuarioSeleccionado2=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			estadoSeleccionado=Util.obtenerValor(estados,objCconsulta.getEstado())==null?Constantes.CODIGO_CODIGO_CAMPO_VACIO:Util.obtenerValor(estados,objCconsulta.getEstado());
			tareaSeleccionada=objCconsulta.getIdTarea();
		}
		
		String nombJSP = getNombreJSPPrincipal();     
		LOG.info("nombJSP = "+nombJSP);
		
		if (Constantes.PERFIL_GERENTE_OFICINA.longValue() == empleado.getPerfil().getId() || Constantes.PERFIL_SUB_GERENTE_OFICINA.longValue() == empleado.getPerfil().getId()) {
			mostrarXusuario=true;
		}
		
		LOG.info("oficinaSeleccionada->:"+oficinaSeleccionada);
		LOG.info("usuarioAsigSeleccionado->:"+usuarioAsigSeleccionado);
		LOG.info("usuarioSeleccionado2->:"+usuarioSeleccionado2);
		LOG.info("usuarioSeleccionado->:"+usuarioSeleccionado);

		/*if(usuarios!=null && usuarios.size()>0)
		for(SelectItem obj: usuarios){
			LOG.info("usuarios obj->:"+obj.getValue());
			LOG.info("usuarios obj->:"+obj.getLabel()+"-----------");
		}
		else
			LOG.info("usuarios- es nulo o vacio");
		
		if(usuariosAsig!=null && usuariosAsig.size()>0)
		for(SelectItem obj: usuariosAsig){
			LOG.info("usuariosAsig obj->:"+obj.getValue());
			LOG.info("usuariosAsig obj->:"+obj.getLabel()+"-----------");
		}
		else
			LOG.info("usuariosAsig- es nulo o vacio");		*/
	}	
	
	public void obtenerObjetoConsulta(){
		Consulta objCconsulta = (Consulta) getObjectSession(Constantes.CONSULTA_BANDASIGN);
		if(objCconsulta!=null){
			codigoExpediente=objCconsulta.getCodigoExpediente()==null?null:Util.validarCampo (objCconsulta.getCodigoExpediente());
			fechaInicio=objCconsulta.getFechaInicio();
			fechaFin=objCconsulta.getFechaFin();
			tipoFechaSeleccionado=objCconsulta.getCodTipoFecha()==null?Constantes.CODIGO_CODIGO_CAMPO_VACIO:objCconsulta.getCodTipoFecha();
			rolSeleccionado=objCconsulta.getIdPerfilUsuarioActual();
			LOG.info("rolSeleccionado"+rolSeleccionado);
			oficina=objCconsulta.getFlagProvincia();
			LOG.info("oficina obtenerObjetoConsulta:::"+oficina);
			oficinaSeleccionada = objCconsulta.getIdOficina();
			tareaSeleccionada = objCconsulta.getIdTarea();
			
			List<String> usr = objCconsulta.getUsuarios();
			if(usr!=null && usr.size()>0 && usr.size()<=1){
				LOG.info("usr tamaño:::"+usr.size());
				usuarioSeleccionado=usr.get(0);
				usuarioSeleccionado2=usr.get(0);
				LOG.info("usr.get(0):::"+usr.get(0));
			}else{
				LOG.info("usr tamaño:::TODOS");
				usuarioSeleccionado2="";
				usuarioSeleccionado="";
			}	
			if(estados!=null && estados.size()>0){
				LOG.info("estados :::"+estados.size());
				estadoSeleccionado=Util.obtenerValor(estados,objCconsulta.getEstado())==null?Constantes.CODIGO_CODIGO_CAMPO_VACIO:Util.obtenerValor(estados,objCconsulta.getEstado());
			}else{
				LOG.info("estados vacio");
			}
				
		}		
	}
	
	public boolean valida(String parm) {
		String idMsgError = "frmBandejaReasigTareas";
		LOG.info("Metodo valida");
		if(parm.equals("2")){
			obtenerObjetoConsulta();
			LOG.info("rolSeleccionado::::"+rolSeleccionado);			
		}
		if((rolSeleccionado==null || rolSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) &&  
		   (codigoExpediente==null || codigoExpediente.equals(Constantes.VALOR_VACIO))){
			LOG.info("mensaje de error");
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
				//addMessageError(idMsgError + ":datoBusqueda", "com.ibm.bbva.common.buscarBandejaHist.msgIngreseDatoBusqueda");
				//return false;
		}else{
			LOG.info("rol y num exp vacios");
			if((this.fechaInicio!=null && this.fechaFin!=null) && (tipoFechaSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO) || tipoFechaSeleccionado==null)){
				LOG.info("mensaje de error fechas");
				addMessageError(idMsgError + ":datoBusqueda", "com.ibm.bbva.common.buscarBandejaAsig.msgIngreseDatoBusquedaFecha");
				return false;
			}			
		}
		
		return true;
	}	
	
	public boolean validaCargaSeleccionada() {
		LOG.info("Metodo validaCargaSeleccionada");
		//Verificar que ingrese al menos un valor de busqueda
		if(
				(tipoFechaSeleccionado==null || tipoFechaSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))&&				
				(codigoExpediente==null || codigoExpediente.equals(Constantes.VALOR_VACIO)) &&
				fechaInicio == null && 
				fechaFin == null &&				
				( rolSeleccionado==null || rolSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) && 
				(tareaSeleccionada==null || tareaSeleccionada.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) && 
				(estadoSeleccionado==null || estadoSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) && 
				(usuarioSeleccionado2==null || usuarioSeleccionado2.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))){
				LOG.info("mensaje de error 1111");
				return false;
		}

		return true;
	}	
		
	public String getCodigoExpediente() {
		return codigoExpediente;
	}

	public void setCodigoExpediente(String codigoExpediente) {
		this.codigoExpediente = codigoExpediente;
	}
	
	/**
	 * 15/12/14
	 * 
	 * @author Daniel Flores
	 * Comentando lo incomentable.
	 * 
	 * Este método crea un objeto que será aquel contenedor de filtros
	 * para realizar la búsqueda.
	 * 
	 * @return
	 */
	 public Consulta crearObjetoConsulta(){
		Consulta consulta = new Consulta();
		
		consulta.setCodigoExpediente(Util.validarCampo (codigoExpediente)==null?null:Util.validarCampo (codigoExpediente).trim());
		LOG.info("codigo expediente = "+consulta.getCodigoExpediente());
		consulta.setFechaInicio(fechaInicio);
		LOG.info("Fecha inicio = "+consulta.getFechaInicio());
		consulta.setFechaFin(fechaFin);
		LOG.info("Fecha fin = "+consulta.getFechaFin());
		LOG.info("tipoFechaSeleccionado = "+tipoFechaSeleccionado);
		consulta.setCodTipoFecha(tipoFechaSeleccionado);
		
		LOG.info("rolSeleccionado = "+rolSeleccionado);
		//consulta.setIdPerfilUsuarioActual(Util.obtenerDescripcion(roles,rolSeleccionado));
		if(rolSeleccionado!=null && !rolSeleccionado.trim().equals("")){
			Perfil objPerfil=perfilBean.buscarPorId(Long.parseLong(rolSeleccionado));
			
			consulta.setIdPerfilUsuarioActual(objPerfil==null?null:objPerfil.getCodigo());
				
			LOG.info("IdPerfilUsuarioActual = "+consulta.getIdPerfilUsuarioActual());			
		}
		
		/*
		 * 15/12/14
		 * Filtro Oficina : Aplicable a todos.
		 * Un perfil de Administrador usará adicionalmente el filtro oficinaSeleccionada.
		 * 
		 * Este filtro de oficina solo abarca las oficinas de acuerdo a 3 criterios :
		 * - Oficinas en Lima
		 * - Oficinas en Provincia
		 * - Todas las oficinas
		 * 
		 */
		oficina=(String) getObjectSession(Constantes.PROV_BAND_ASIG);
		if (oficina!=null && !oficina.trim().equals("") && (oficina.equals(Constantes.FLAG_TERRITORIO_LIMA) || oficina.equals(Constantes.FLAG_TERRITORIO_PROVINCIA))) {
            consulta.setFlagProvincia(oficina);
            LOG.info("oficina = "+consulta.getFlagProvincia());
		}
		LOG.info("oficina = "+consulta.getFlagProvincia());
		
		LOG.info("oficina crearObjetoConsulta:::"+oficina);

		if (usuarioSeleccionado!=null && !Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(usuarioSeleccionado) && !usuarioSeleccionado.equals("")) {	
			LOG.info("usuarioSeleccionado "+usuarioSeleccionado);
			ArrayList<String> usr = new ArrayList<String> ();
			//usr.add(Util.obtenerDescripcion(usuarios,usuarioSeleccionado));
			usr.add(usuarioSeleccionado);
			consulta.setUsuarios(usr);
			//AGREGADO EL 26 FEBRERO 2015
			consulta.setCodUsuarioActual(usuarioSeleccionado);
		}
		
		/*
		 * 15/12/14
		 * Filtro Oficina Seleccionada : Aplicable solo a Administrador
		 * 
		 * Este filtro permite filtrar por un ID de oficina en específico.
		 */
		if (oficinaSeleccionada!=null && !Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(oficinaSeleccionada) && !oficinaSeleccionada.equals("")) {
			LOG.info("oficinaSeleccionada : " + oficinaSeleccionada);
			consulta.setIdOficina(oficinaSeleccionada);
		}
		
		ArrayList<String> usu= new ArrayList<String> ();
		
		if (usuarioSeleccionado2!=null && !Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(usuarioSeleccionado2) && !usuarioSeleccionado2.equals("")) {
			LOG.info("usuarioSeleccionado2 "+usuarioSeleccionado2);
			usu.add(usuarioSeleccionado2);	
			//AGREGADO EL 26 FEBRERO 2015
			consulta.setCodUsuarioActual(usuarioSeleccionado2);
			consulta.setUsuarios(usu);
		}else{
			if(usuarios!=null && usuarios.size()>1){
				LOG.info("usuarios:::"+usuarios.size());
				for (SelectItem si : usuarios){
					LOG.info("item::: "+si.getValue().toString());
					usu.add(si.getValue().toString());
				}
				consulta.setUsuarios(usu);
			}else
				LOG.info("usuarios es vacio");
		}
		
		if(consulta.getUsuarios()!=null && consulta.getUsuarios().size()>0){
			consulta.setConsiderarUsuarios(true);
			LOG.info("cantidad usu = "+consulta.getUsuarios().size());
		}else{
			consulta.setConsiderarUsuarios(false);
			LOG.info("usu es nulo o vacio ");
		}
		
		consulta.setCodEstado(estadoSeleccionado);
		consulta.setEstado(Util.obtenerDescripcion(estados, estadoSeleccionado));	
		LOG.info("Crear objeto CONSULTA con ID_TAREA:::"+tareaSeleccionada);
		if(tareaSeleccionada!=null && !tareaSeleccionada.equals("-1"))
			consulta.setIdTarea(tareaSeleccionada);
		if(consulta!=null && (consulta.getCodigoExpediente()!=null && !consulta.getCodigoExpediente().equals("")) || 
				(consulta.getIdPerfilUsuarioActual()!=null && !consulta.getIdPerfilUsuarioActual().equals(""))){

			/* El anterior desarrollador tenía que agregar el objeto a sesión para poder
			 * usar este en el método init() que siempre se invocaba cada vez que se utilizaba
			 * AJAX. La solución era usar un ViewScoped o al menos SessionScoped.
			 * 
			 * Lo dejamos así, porque a lo largo de la clase, se obtiene el método por medio de sesión.
			 */
			addObjectSession(Constantes.CONSULTA_BANDASIGN, consulta);
			LOG.info("almacenando objeto consulta");
		}
		
		return consulta;
	}
	
	/**
	 * 15/12/14
	 * 
	 * Este método se invoca cuando el usuario
	 * presiona el botón de "Buscar"
	 */
	 public void buscar(){
		/*
		 * Realizar búsqueda
		 */
		Consulta consulta=crearObjetoConsulta();
		
		if (valida("2")){
			
			consulta = (Consulta) getObjectSession(Constantes.CONSULTA_BANDASIGN);
			
			RemoteUtils tareasBDelegate = new RemoteUtils();
			LOG.info("Iniciando Metodo" + "[tareasBDelegate.obtenerInstanciasTareasPorUsuario]");
			//addObjectSession(Constantes.CONSULTA_BANDASIGN, consulta);
			consulta.setTipoConsulta(4);
			List<ExpedienteTCWPS> lista = tareasBDelegate.listarTareasBandejaReasignacion(consulta);
			
			List<ExpedienteTCWPS> listaXProvincia=new ArrayList<ExpedienteTCWPS>();
			oficina=(String) getObjectSession(Constantes.PROV_BAND_ASIG);
			LOG.info("oficina buscar = "+oficina);
			if (oficina!=null && !oficina.trim().equals("") && (oficina.equals(Constantes.FLAG_TERRITORIO_LIMA) || 
					oficina.equals(Constantes.FLAG_TERRITORIO_PROVINCIA))) {
				List<Territorio> listTerritorio= territorioBean.buscarPorFlagProvincia(oficina);
				
				for(ExpedienteTCWPS exp: lista){
					LOG.info("----------------------------------------");
					LOG.info("exp codigo:::"+exp.getCodigo());
					LOG.info("exp territorio:::"+exp.getIdTerritorio());
					for(Territorio terr: listTerritorio){
						if(exp.getIdTerritorio()!=null && !exp.getIdTerritorio().equals("") && 
								Long.parseLong(exp.getIdTerritorio())==terr.getId()){
							LOG.info("OK territorio:::"+exp.getIdTerritorio());
							listaXProvincia.add(exp);
							break;
						}
					}
					
				}
				listTerritorio=null;
				addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, listaXProvincia);
			}else{
				addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, lista);
			}		
			
			if(lista!=null && lista.size()>=0)
				LOG.info("lista obtenida::::"+lista.size());
			else
				LOG.info("lista obtenida::::nula");
			
			if(listaXProvincia!=null && listaXProvincia.size()>=0)
				LOG.info("listaXProvincia obtenida::::"+listaXProvincia.size());
			else
				LOG.info("listaXProvincia obtenida::::nula");			
			
			FacesContext ctx = FacesContext.getCurrentInstance();
			TablaBandejaAsigMB tablaBandejaAsig = (TablaBandejaAsigMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "tablaBandejaAsig");
			tablaBandejaAsig.actualizarLista();
			tablaBandejaAsig.ordenarPorAccionCombo();
			LOG.info("numeroRegistro::::");

			/* 
			 * Carga Lista de Usuarios Asignar 
			 */
			usuariosAsig = (List<SelectItem>) getObjectSession(Constantes.LISTA_USUARIOS_ASIG);
		/*	
			if (Util.isListaVacia(usuariosAsig)) {
				if (rolSeleccionado!=null && !rolSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
					
					List<Empleado> listaEmpxPerfil = new ArrayList<Empleado>();
					
					if(empleado.getPerfil().getFlagAdministracion().equals("1")){
						listaEmpxPerfil.addAll(empleadoBean.buscarPorOficinaPerfil(Long.valueOf(oficinaSeleccionada), Long.valueOf(rolSeleccionado)));			
					}else{
						listaEmpxPerfil=empleadoBean.buscarPorPerfilEmpleadoActivo(empleado.getPerfil().getId(), empleado.getOficina().getId());
					}
					
					List<Empleado> listaEmp = new ArrayList<Empleado>();
					
					for (Empleado emp : listaEmpxPerfil) {				
						if (emp.getId() != empleado.getId()) {
							listaEmp.add(emp);
						}
					}
					usuariosAsig = Util.crearItems(listaEmp, true, "codigo", "nombresCompletos");
					
					for(SelectItem lis: usuariosAsig){
						System.out.println("usuario Asignado. label: "+ lis.getLabel()+" valor: "+lis.getValue());
					}
					addObjectSession(Constantes.LISTA_USUARIOS_ASIG, usuariosAsig);
				}else{
					usuariosAsig=Util.listaVacia();
				}
			}
		*/
			}else{
				addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, null);
		}
	}
	
	public String limpiar() {
		this.codigoExpediente = "";
		this.tipoFechaSeleccionado = "";
		this.usuarioSeleccionado = "";
		this.usuarioSeleccionado2 = "";
		this.usuarioAsigSeleccionado = "";
		this.rolSeleccionado = "";   
		this.tareaSeleccionada="";
		this.oficinaSeleccionada = "";
		
		this.estadoSeleccionado = "";
		this.fechaInicio = null;
		this.fechaFin = null;
		usuarioSeleccionado2=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		List<ExpedienteTCWPS> lista=new ArrayList<ExpedienteTCWPS>();
		addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, lista);
		usuariosAsig=Util.listaVacia();
		
		addObjectSession(Constantes.CONSULTA_BANDASIGN_EXP, false);
		addObjectSession(Constantes.BOTON_BANDASIGN_ASIGNA, false);
		addObjectSession(Constantes.CONSULTA_BANDASIGN_ASIGNA, true);
		addObjectSession(Constantes.LISTA_USUARIOS_ASIG, usuariosAsig);
		addObjectSession(Constantes.CODIGO_USU_ASIGNADO_EXP, null);
		removeObjectSession(Constantes.LIS_BANDASIGN_ASIGNA);
		removeObjectSession(Constantes.MSJ_BANDASIGN_ASIGNA);
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		return null;
	}

	public void cargarListaAsignar(List<Empleado> lista) {
		/*Carga Lista de Usuarios Asignar*/
		if(usuariosAsig!=null && usuariosAsig.size()>0)
			LOG.info("usuariosAsig tamaño:::"+usuariosAsig.size());
		else
			LOG.info("usuariosAsig es nulo");
		usuariosAsig = Util.crearItems(lista, true, "codigo", "nombresCompletos");	
		addObjectSession(Constantes.LISTA_USUARIOS_ASIG, usuariosAsig);
	}
	
	public void limpiarListaAsignar() {
		/*Carga Lista de Usuarios Asignar*/
		usuariosAsig=Util.listaVacia();	
		addObjectSession(Constantes.LISTA_USUARIOS_ASIG, usuariosAsig);
	}	

	/**
	 * 15/12/14
	 * 
	 * Este método se ejecuta cuando el usuario selecciona
	 * una tarea.
	 * 
	 * @param event
	 */
	public void buscarEstado(AjaxBehaviorEvent event) {		
		estadoSeleccionado=Constantes.CODIGO_CODIGO_CAMPO_VACIO;

		if (tareaSeleccionada!=null && !tareaSeleccionada.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			List<EstadoTareaCE> listaEmp =  new ArrayList<EstadoTareaCE>();
			List<EstadoCE> listaEstado =  new ArrayList<EstadoCE>();
			LOG.info("Busqueda por 1 sola tarea ... "+tareaSeleccionada);		
			listaEmp.addAll(estadoTareaCEBean.buscarPorIdTarea(Long.parseLong(tareaSeleccionada)));	
			listaEstado=listaEstadosUnicos(listaEmp);
			estados = Util.crearItems(listaEstado,true, "id", "descripcion");			
		}else
			estados=Util.listaVacia();	
		
		addObjectSession(Constantes.LISTA_TAREA_ESTADO_ASIG, estados);
		addObjectSession(Constantes.CODIGO_TAREA_ASIGNADO, tareaSeleccionada);
		
		if(estados!=null && estados.size()>0)
			LOG.info("tamaño estados = "+estados.size());
		else
			LOG.info("estados es nulo o vacío");
	}
	
	/**
	 * Método que se invoca cuando un usuario selecciona
	 * un elemento de la lista de Oficinas
	 * 
	 * @param event
	 */
	public void buscarOficina(AjaxBehaviorEvent event){
		List<Empleado> listaEmp = new ArrayList<Empleado>();
		//List<Tarea> listaTarea = new ArrayList<Tarea>();
		
		usuarioSeleccionado2=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		List<ExpedienteTCWPS> lista=new ArrayList<ExpedienteTCWPS>();
		addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, lista);
		
		usuariosAsig=Util.listaVacia();
		usuarioAsigSeleccionado="";
		
		addObjectSession(Constantes.CONSULTA_BANDASIGN_EXP, false);
		addObjectSession(Constantes.BOTON_BANDASIGN_ASIGNA, false);
		addObjectSession(Constantes.CONSULTA_BANDASIGN_ASIGNA, true);
		
		removeObjectSession(Constantes.LIS_BANDASIGN_ASIGNA);
		removeObjectSession(Constantes.MSJ_BANDASIGN_ASIGNA);
		
		LOG.info("Parametros de  buscarPorOficina : ");
		LOG.info("oficinaSeleccionada = "+oficinaSeleccionada);
		LOG.info("oficina = "+ oficinaSeleccionada);
		rolSeleccionado=(String)getObjectSession(Constantes.ROL_SELECCIONADO);
		LOG.info("rolSeleccionado = "+ rolSeleccionado);
		//removeObjectSession(Constantes.LISTA_USUARIOS_TAREA_ASIG);
		removeObjectSession(Constantes.LISTA_USUARIOS_ROL_ASIG);
		removeObjectSession(Constantes.LISTA_USUARIOS_ASIG);
		
		if (oficinaSeleccionada!=null && !oficinaSeleccionada.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			LOG.info("Busqueda por 1 solo oficina ... "+oficinaSeleccionada);
			
			/**
			 * Si es un administrador,
			 * obtener todas los usuarios activos
			 * por oficina y
			 * rol seleccionados
			 */
			
			if(empleado.getPerfil().getFlagAdministracion().equals("1")){
				listaEmp.addAll(empleadoBean.buscarPorPerfilEmpleadoActivo(Long.valueOf(rolSeleccionado), Long.valueOf(oficinaSeleccionada)));			
			}
			
			//listaTarea.addAll(tareaBean.buscarPorPerfil(Long.valueOf(rolSeleccionado)));
			
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
			/*for(Tarea tarea : listaTarea){
				if(tarea.getCodigo() != null){
					if(tarea.getCodigo().equals("26")){
						listaTarea.remove(tarea);
						break;
					}
				}
			}*/
			
			//estados = Util.listaVacia();
		} else {
			LOG.info("Busqueda por todos los roles ... ");
//			for (SelectItem p : roles) {
//				listaEmp.addAll(empleadoBean.buscarPorOficinaPerfil(empleado.getOficina().getId(), Long.valueOf(p.getValue().toString())));
//			}
			usuarios=Util.listaVacia();
			tareas = Util.listaVacia();
			//estados =Util.listaVacia();
			usuariosAsig=Util.listaVacia();
			
			// Cuando cambia de perfil, codigoPerfil=-1 ( en la lista) se limpia la grilla del resultado
			limpiarTablaAsignacion();
		}
			
		LOG.info("listaEmp.size: " + listaEmp.size());
		usuarios = Util.crearItems(listaEmp,true, "codigo", "nombresCompletos");
	
		/*Carga Lista de Tareas*/
		//tareas = Util.crearItems(listaTarea, true, "id", "descripcion");
		
		//usuariosAsig = Util.listaVacia();
		//usuariosAsig = new ArrayList<SelectItem>(usuarios);
		
		//addObjectSession(Constantes.LISTA_USUARIOS_TAREA_ASIG, tareas);
		addObjectSession(Constantes.LISTA_USUARIOS_CARGADOS, listaEmp);
		addObjectSession(Constantes.LISTA_USUARIOS_ROL_ASIG, usuarios);
		addObjectSession(Constantes.LISTA_USUARIOS_ASIG, usuariosAsig);
		
		if(usuarios!=null && usuarios.size()>0)
			LOG.info("tamaño usuarios = "+usuarios.size());
		else
			LOG.info("usuarios list es nulo o vacío");
		
		/*if(tareas!=null && tareas.size()>0){
			LOG.info("tamaño tareas = "+listaTarea.size());
		}else
			LOG.info("tareas list es nulo o vacio ");	*/
		
		/*if(listaTarea!=null && listaTarea.size()>0){
			LOG.info("tamaño listaTarea = "+listaTarea.size());
		}else
			LOG.info("tareas list es nulo o vacio ");*/	
		
	}

	public void buscarRol(AjaxBehaviorEvent event) {
		List<Empleado> listaEmp = new ArrayList<Empleado>();
		List<Tarea> listaTarea = new ArrayList<Tarea>();
		
		List<Oficina> listaOficina = new ArrayList<Oficina>();
		
		usuarioSeleccionado2=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		oficinaSeleccionada=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		
		List<ExpedienteTCWPS> lista=new ArrayList<ExpedienteTCWPS>();
		addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, lista);
		usuariosAsig=Util.listaVacia();
		usuarioAsigSeleccionado="";
		
		addObjectSession(Constantes.CONSULTA_BANDASIGN_EXP, false);
		addObjectSession(Constantes.BOTON_BANDASIGN_ASIGNA, false);
		addObjectSession(Constantes.CONSULTA_BANDASIGN_ASIGNA, true);
		removeObjectSession(Constantes.LIS_BANDASIGN_ASIGNA);
		removeObjectSession(Constantes.MSJ_BANDASIGN_ASIGNA);
		
		LOG.info("Parametros de  buscarPorOficinaPerfil : ");
		LOG.info("rolSeleccionado = "+rolSeleccionado);
		LOG.info("oficina = "+empleado.getOficina().getId());
		
		removeObjectSession(Constantes.LISTA_USUARIOS_TAREA_ASIG);
		removeObjectSession(Constantes.LISTA_USUARIOS_ROL_ASIG);
		removeObjectSession(Constantes.LISTA_USUARIOS_ASIG);
		removeObjectSession(Constantes.LISTA_OFICINAS_CARGADAS);
		
		if (rolSeleccionado!=null && !rolSeleccionado.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			LOG.info("Busqueda por 1 solo rol ... "+rolSeleccionado);

			/**
			 * Si es un administrador,
			 * obtener todas los usuarios por oficina de 
			 * administrador
			 */
			if(empleado.getPerfil().getFlagAdministracion().equals("1")){
				
				//listaEmp.addAll(empleadoBean.buscarPorOficinaPerfil(empleado.getOficina().getId(), Long.valueOf(rolSeleccionado)));			
				listaOficina.addAll(oficinaBean.buscarPorIdPerfilEmpleados(Long.valueOf(rolSeleccionado)));
			
			}else{
				
				/**
				 * Si no es administrador,
				 * obtener todos los usuarios por oficina del
				 * jefe
				 */
				//listaEmp.addAll(empleadoBean.buscarPorOficinaPerfil(empleado.getOficina().getId(), Long.valueOf(rolSeleccionado)));			
				listaEmp.addAll(empleadoBean.buscarPorPerfilEmpleadoActivo(Long.valueOf(rolSeleccionado),empleado.getOficina().getId()));
				
			}
			
			listaTarea.addAll(tareaBean.buscarPorPerfil(Long.valueOf(rolSeleccionado)));
			
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
			for(Tarea tarea : listaTarea){
				if(tarea.getCodigo() != null){
					if(tarea.getCodigo().equals("26")){
						listaTarea.remove(tarea);
						break;
					}
				}
			}	
			estados = Util.listaVacia();
		} else {
			LOG.info("Busqueda por todos los roles ... ");
//			for (SelectItem p : roles) {
//				listaEmp.addAll(empleadoBean.buscarPorOficinaPerfil(empleado.getOficina().getId(), Long.valueOf(p.getValue().toString())));
//			}
			usuarios=Util.listaVacia();
			tareas = Util.listaVacia();
			estados =Util.listaVacia();
			usuariosAsig=Util.listaVacia();
			
			oficinas=Util.listaVacia();
			// Cuando cambia de perfil, codigoPerfil=-1 ( en la lista) se limpia la grilla del resultado
			limpiarTablaAsignacion();
		}
			
		LOG.info("listaEmp.size: " + listaEmp.size());
		usuarios = Util.crearItems(listaEmp,true, "codigo", "nombresCompletos");

		oficinas = Util.crearItemsConcat(listaOficina,true,"id","codigo", "descripcion");

		/*Carga Lista de Tareas*/
		tareas = Util.crearItems(listaTarea, true, "id", "descripcion");	

		//usuariosAsig = Util.listaVacia();
		//usuariosAsig = new ArrayList<SelectItem>(usuarios);
		
		addObjectSession(Constantes.LISTA_USUARIOS_TAREA_ASIG, tareas);
		
		if(empleado.getPerfil().getFlagAdministracion().equals("0")){
			addObjectSession(Constantes.LISTA_USUARIOS_CARGADOS, listaEmp);
		}
		
		if(empleado.getPerfil().getFlagAdministracion().equals("1")){
			addObjectSession(Constantes.LISTA_OFICINAS_CARGADAS, oficinas);
		}
		
		addObjectSession(Constantes.LISTA_USUARIOS_ROL_ASIG, usuarios);
		addObjectSession(Constantes.LISTA_USUARIOS_ASIG, usuariosAsig);
		addObjectSession(Constantes.ROL_SELECCIONADO, rolSeleccionado);
		
		if(usuarios!=null && usuarios.size()>0)
			LOG.info("tamaño usuarios = "+usuarios.size());
		else
			LOG.info("usuarios list es nulo o vacío");
		
		if(tareas!=null && tareas.size()>0){
			LOG.info("tamaño tareas = "+listaTarea.size());
		}else
			LOG.info("tareas list es nulo o vacio ");	
		
		if(listaTarea!=null && listaTarea.size()>0){
			LOG.info("tamaño listaTarea = "+listaTarea.size());
		}else
			LOG.info("tareas list es nulo o vacio ");	
		
	}
	
	public void limpiarTablaAsignacion(){
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		TablaBandejaAsigMB tablaBandejaAsig = (TablaBandejaAsigMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "tablaBandejaAsig");
		tablaBandejaAsig.limpiarLista();
	}
	
	
	public String getEstadoSeleccionado() {
		estadoSeleccionado=(String)getObjectSession(Constantes.CODIGO_ESTADO_ASIGNADO);
		if(estadoSeleccionado!=null)
			return estadoSeleccionado;
		else
			return Constantes.CODIGO_CODIGO_CAMPO_VACIO;
	}

	public void setEstadoSeleccionado(String estadoSeleccionado) {
		this.estadoSeleccionado = estadoSeleccionado;
		addObjectSession(Constantes.CODIGO_ESTADO_ASIGNADO, estadoSeleccionado);
	}

	public List<SelectItem> getEstados() {
		estados=(List<SelectItem>) getObjectSession(Constantes.LISTA_TAREA_ESTADO_ASIG);
		if(estados!=null && estados.size()>0)
			LOG.info("estados tamaño::"+estados.size());
		else{
			estados=Util.listaVacia();
			LOG.info("estados es nulo");
		}			
		return estados;
	}

	public void setEstados(List<SelectItem> estados) {
		this.estados = estados;
	}

	public boolean isMostrarXusuario() {
		return mostrarXusuario;
	}

	public void setMostrarXusuario(boolean mostrarXusuario) {
		this.mostrarXusuario = mostrarXusuario;
	}

	public List<SelectItem> getTiposFechas() {
		return tiposFechas;
	}

	public void setTiposFechas(List<SelectItem> tiposFechas) {
		this.tiposFechas = tiposFechas;
	}

	public List<SelectItem> getUsuarios() {
		List<SelectItem> usuarios =(List<SelectItem>) getObjectSession(Constantes.LISTA_USUARIOS_ROL_ASIG);
		if(usuarios!=null && usuarios.size()>0)
			LOG.info("usuarios es tamaño::"+usuarios.size());
		else{
			usuarios=Util.listaVacia();
			LOG.info("usuarios es nulo");
		}
		
		return usuarios;
	}

	public void setUsuarios(List<SelectItem> usuarios) {
		this.usuarios = usuarios;
	}

	public List<SelectItem> getRoles() {
		return roles;
	}

	public void setRoles(List<SelectItem> roles) {
		this.roles = roles;
	}

	public String getTipoFechaSeleccionado() {
		return tipoFechaSeleccionado;
	}

	public void setTipoFechaSeleccionado(String tipoFechaSeleccionado) {
		this.tipoFechaSeleccionado = tipoFechaSeleccionado;
	}

	public String getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}

	public void setUsuarioSeleccionado(String usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}
	
	public String getUsuarioSeleccionado2() {
		return usuarioSeleccionado2;
	}

	public void setUsuarioSeleccionado2(String usuarioSeleccionado2) {
		this.usuarioSeleccionado2 = usuarioSeleccionado2;
		LOG.info("seteo usuarioSeleccionado2... "+this.usuarioSeleccionado2);
	}

	public String getRolSeleccionado() {
		return rolSeleccionado;
	}

	public void setRolSeleccionado(String rolSeleccionado) {
		this.rolSeleccionado = rolSeleccionado;
	}

	public String getOficina() {
		return oficina;
	}

	public void setOficina(String oficina) {
		this.oficina = oficina;
		addObjectSession(Constantes.PROV_BAND_ASIG, oficina);
	}

	public List<SelectItem> getUsuariosAsig() {
		List<SelectItem> usuariosAsig = (List<SelectItem>) getObjectSession(Constantes.LISTA_USUARIOS_ASIG);
		if(usuariosAsig!=null && usuariosAsig.size()>0)
			LOG.info("usu asig es tamaño::"+usuariosAsig.size());
		else{
			usuariosAsig=Util.listaVacia();
			LOG.info("usu asig es nulo");
		}
		return usuariosAsig;
	}

	public void setUsuariosAsig(List<SelectItem> usuariosAsig) {
		this.usuariosAsig = usuariosAsig;
	}

	public String getUsuarioAsigSeleccionado() {
		LOG.info("getUsuarioAsigSeleccionado");
		if(usuariosAsig!=null && usuariosAsig.size()>0) {
			for(SelectItem obj: usuariosAsig){
				LOG.info("usuariosAsig obj->:"+obj.getValue());
				LOG.info("usuariosAsig obj->:"+obj.getLabel()+"-----------");
			}
		} else {
			usuariosAsig=Util.listaVacia();
			LOG.info("usuariosAsig- es nulo o vacio");	
		}
		LOG.info("usuarioAsigSeleccionado->:"+usuarioAsigSeleccionado);
		usuarioAsigSeleccionado=(String)getObjectSession(Constantes.CODIGO_USU_ASIGNADO_EXP);
		return usuarioAsigSeleccionado;
	}

	public void setUsuarioAsigSeleccionado(String usuarioAsigSeleccionado) {
		LOG.info("setUsuarioAsigSeleccionado():"+usuarioAsigSeleccionado);
		this.usuarioAsigSeleccionado = usuarioAsigSeleccionado;
		addObjectSession(Constantes.CODIGO_USU_ASIGNADO_EXP, usuarioAsigSeleccionado);
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

	public List<SelectItem> getTareas() {
		tareas=(List<SelectItem>) getObjectSession(Constantes.LISTA_USUARIOS_TAREA_ASIG);
		if(tareas!=null && tareas.size()>0)
			LOG.info("tareases tamaño::"+tareas.size());
		else{
			tareas=Util.listaVacia();
			LOG.info("tareas es nulo");
		}		
		return tareas;
	}

	public void setTareas(List<SelectItem> tareas) {
		this.tareas = tareas;
	}

	public String getTareaSeleccionada() {
		return tareaSeleccionada;
	}

	public void setTareaSeleccionada(String tareaSeleccionada) {
		this.tareaSeleccionada = tareaSeleccionada;
	}	
	
	private List<EstadoCE> listaEstadosUnicos (List<EstadoTareaCE> idEstadoPorIdTarea) {
	List<EstadoCE> listaEstado = new ArrayList<EstadoCE>();
	for(EstadoTareaCE estados: idEstadoPorIdTarea){
		listaEstado.addAll(estadoCEBean.buscarPorIdEstado(estados.getEstadoCE().getId()));
	} 
	Set<EstadoCE> set = new TreeSet<EstadoCE> (new Comparator<EstadoCE>(){
		public int compare(EstadoCE o1, EstadoCE o2) {
			return o1.getDescripcion().compareToIgnoreCase(o2.getDescripcion());			
		}
	});	
		for (EstadoCE estado : listaEstado) {
			set.add(estado);
		}
	return new ArrayList<EstadoCE>(set);
	}

	
	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public List<SelectItem> getOficinas() {
		oficinas=(List<SelectItem>) getObjectSession(Constantes.LISTA_OFICINAS_CARGADAS);
		if(oficinas!=null && oficinas.size()>0){
			LOG.info("oficina tamaño :::"+oficinas.size());
			for(SelectItem item : oficinas){
				LOG.info("oficina value::"+item.getValue()+" :::: item:::"+item.getDescription());
			}
		}else
			LOG.info("oficina es nulo o vacio");
		return oficinas;
	}

	public void setOficinas(List<SelectItem> oficinas) {
		this.oficinas = oficinas;
	}

	public String getOficinaSeleccionada() {
		LOG.info("oficinaSeleccionada:::"+oficinaSeleccionada);
		if(oficinaSeleccionada==null)
			oficinaSeleccionada="-1";
		return oficinaSeleccionada;
	}
	
	public void setOficinaSeleccionada(String oficinaSeleccionada) {
		this.oficinaSeleccionada = oficinaSeleccionada;
	}

	public String getTextoMensajeSinFiltro() {
		return textoMensajeSinFiltro;
	}

	public void setTextoMensajeSinFiltro(String textoMensajeSinFiltro) {
		this.textoMensajeSinFiltro = textoMensajeSinFiltro;
	}
	
	
	

}