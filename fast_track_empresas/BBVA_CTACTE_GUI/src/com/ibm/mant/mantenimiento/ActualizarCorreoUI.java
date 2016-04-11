package com.ibm.mant.mantenimiento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.ctacte.bean.DatosCorreo;
import com.ibm.bbva.ctacte.bean.DatosCorreoXPerfil;
import com.ibm.bbva.ctacte.bean.Perfil;
import com.ibm.bbva.ctacte.bean.TareaPerfil;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.dao.AccionDAO;
import com.ibm.bbva.ctacte.dao.DatosCorreoDAO;
import com.ibm.bbva.ctacte.dao.PerfilDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;
import com.ibm.bbva.ctacte.dao.TareaPerfilDAO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean(name="actualizarCorreo")
@ViewScoped
public class ActualizarCorreoUI extends AbstractMBean {

	private static final long serialVersionUID = -7971627233122457181L;
	
	private static final Logger LOG = LoggerFactory.getLogger(ActualizarCorreoUI.class);
	
	private List<SelectItem> perfiles;
	private List<SelectItem> tareas;
	private List<SelectItem> acciones;
	private List<SelectItem> estados;
	private List<SelectItem> perfilesA;
	private List<SelectItem> perfilesB;
	private Integer perfilSeleccionado;
	private Integer tareaSeleccionada;
	private Integer accionSeleccionada;
	private String estadoSeleccionado;
	private String asunto;
	private String cuerpo;
	private List<Integer> perfilesSeleccionadosA;
	private List<Integer> perfilesSeleccionadosB;
	private boolean nuevoRegistro;
	
	@EJB
	private PerfilDAO perfilDAO;
	@EJB
	private TareaDAO tareaDAO;
	@EJB
	private AccionDAO accionDAO;
	@EJB
	private DatosCorreoDAO datosCorreoDAO;
	@EJB
	private TareaPerfilDAO tareaPerfilDAO;
	
	@PostConstruct
	public void init() {
		LOG.info("init()");
		DatosCorreo datosCorreo = (DatosCorreo) getObjectSession(Constantes.MANT_CORREO_SESION);
		if (datosCorreo == null) {
			perfilSeleccionado = Integer.parseInt(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
			tareaSeleccionada = Integer.parseInt(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
			accionSeleccionada = Integer.parseInt(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
			estadoSeleccionado = Constantes.CHECK_SELECCIONADO; // Activo por defecto
			asunto = null;
			cuerpo = null;
			nuevoRegistro = true;
		} else {
			TareaPerfil tareaPerfil = tareaPerfilDAO.findTareaPerfilxTarea(datosCorreo.getAccion().getTarea().getId());
			perfilSeleccionado = tareaPerfil.getPerfil().getId();
			tareaSeleccionada = datosCorreo.getAccion().getTarea().getId();
			accionSeleccionada = datosCorreo.getAccion().getId();
			estadoSeleccionado = Constantes.CHECK_SELECCIONADO.equals(datosCorreo.getFlagActivo()) ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO;
			asunto = datosCorreo.getAsunto();
			if (datosCorreo.getCuerpo() != null) {
				cuerpo = new String(datosCorreo.getCuerpo());
			}
			nuevoRegistro = false;
		}
		
		perfiles = Util.crearItems(perfilDAO.buscarPerfilesConTareaAsignada(), true, "id", "descripcion");
		estados = new ArrayList<SelectItem>();
		estados.add(new SelectItem(Constantes.CHECK_SELECCIONADO, "SI"));
		estados.add(new SelectItem(Constantes.CHECK_NO_SELECCIONADO, "NO"));
		perfilesSeleccionadosA = new ArrayList<Integer>();
		perfilesSeleccionadosB = new ArrayList<Integer>();
		// IDs de los perfiles que se pueden configurar como destinatarios
		List<Integer> listaIdPerfiles = new ArrayList<Integer>(Arrays.asList(
				Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_JEFE_BASTANTEO),
				Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_SUPERVISOR_FIRMA),
				Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_SUPERVISOR_ABOGADO_BASTANTEO),
				Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_GESTOR),
				Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_MESA_FIRMAS),
				Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_MESA_DOCUMENTOS),
				Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_ABOGADO_BASTANTEO),
				Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_ABOGADO_REVISION)));
		if (nuevoRegistro) {
			tareas = Util.crearItems(null, true, "id", "descripcion");
			acciones = Util.crearItems(null, true, "id", "descripcion");
			perfilesA = Util.crearItems(perfilDAO.buscarPorListaIDs(listaIdPerfiles), false, "id", "descripcion");
			perfilesB = new ArrayList<SelectItem>();
		} else {
			tareas = Util.crearItems(tareaDAO.findByPerfil(perfilSeleccionado), true, "id", "descripcion");
			acciones = Util.crearItems(accionDAO.findByTarea(tareaSeleccionada), true, "id", "descripcion");
			perfilesA = new ArrayList<SelectItem>();
			perfilesB = new ArrayList<SelectItem>();
			List<Perfil> lstPerfilesA = perfilDAO.buscarPorListaIDs(listaIdPerfiles);
			List<Perfil> lstPerfilesB = new ArrayList<Perfil>();
			if (datosCorreo.getDatosCorreoXPerfil() != null && !datosCorreo.getDatosCorreoXPerfil().isEmpty()) {
				for (DatosCorreoXPerfil destinatario : datosCorreo.getDatosCorreoXPerfil()) {
					lstPerfilesB.add(destinatario.getPerfil());
					for (Perfil perfil : lstPerfilesA) {
						if (perfil.getId().equals(destinatario.getPerfil().getId())) {
							lstPerfilesA.remove(perfil);
							break;
						}
					}
				}
			}
			perfilesA = Util.crearItems(lstPerfilesA, false, "id", "descripcion");
			perfilesB = Util.crearItems(lstPerfilesB, false, "id", "descripcion");
			ordenarSelecItem(perfilesB);
		}
	}
	
	public void seleccionarPerfil(AjaxBehaviorEvent event) {
		if (!ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO.equals(perfilSeleccionado)) {
			tareas = Util.crearItems(tareaDAO.findByPerfil(perfilSeleccionado), true, "id", "descripcion");
		} else {
			tareas = Util.crearItems(null, true, "id", "descripcion");
		}
		tareaSeleccionada = Integer.parseInt(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
		accionSeleccionada = Integer.parseInt(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
		acciones = Util.crearItems(null, true, "id", "descripcion");
	}
	
	public void seleccionarTarea(AjaxBehaviorEvent event) {
		if (!ConstantesAdmin.CODIGO_CODIGO_CAMPO_VACIO.equals(tareaSeleccionada)) {
			acciones = Util.crearItems(accionDAO.findByTarea(tareaSeleccionada), true, "id", "descripcion");
		} else {
			acciones = Util.crearItems(null, true, "id", "descripcion");
		}
		accionSeleccionada = Integer.parseInt(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
	}
	
	public void leftToRight(AjaxBehaviorEvent event) {
		for (Integer idPerfil : perfilesSeleccionadosA) {
			for (SelectItem item : perfilesA) {
				if (idPerfil.equals(Integer.parseInt((String) item.getValue()))) {
					perfilesA.remove(item);
					perfilesB.add(item);
					break;
				}
			}
		}
		ordenarSelecItem(perfilesB);
		perfilesSeleccionadosA = new ArrayList<Integer>();
		perfilesSeleccionadosB = new ArrayList<Integer>();
	}
	
	public void rightToLeft(AjaxBehaviorEvent event) {
		for (Integer idPerfil : perfilesSeleccionadosB) {
			for (SelectItem item : perfilesB) {
				if (idPerfil.equals(Integer.parseInt((String) item.getValue()))) {
					perfilesB.remove(item);
					perfilesA.add(item);
					break;
				}
			}
		}
		ordenarSelecItem(perfilesA);
		perfilesSeleccionadosA = new ArrayList<Integer>();
		perfilesSeleccionadosB = new ArrayList<Integer>();
	}
	
	private void ordenarSelecItem(List<SelectItem> list) {
		Collections.sort(list, new Comparator<SelectItem>() {
			public int compare(SelectItem s1, SelectItem s2) {
				return s1.getLabel().compareToIgnoreCase(s2.getLabel());
			}
		});
	}
	
	public String guardar() {
		if (esValido()) {
			try {
				if (nuevoRegistro) {
					DatosCorreo obj = new DatosCorreo();
					obj.setAccion(accionDAO.load(accionSeleccionada));
					obj.setAsunto(asunto);
					obj.setCuerpo(cuerpo.getBytes());
					obj.setFlagActivo(estadoSeleccionado);
					obj.setDatosCorreoXPerfil(new ArrayList<DatosCorreoXPerfil>());
					for (SelectItem item : perfilesB) {
						DatosCorreoXPerfil destinatario = new DatosCorreoXPerfil();
						destinatario.setDatosCorreo(obj);
						destinatario.setPerfil(perfilDAO.load(Integer.parseInt((String)item.getValue())));
						obj.getDatosCorreoXPerfil().add(destinatario);
					}
					datosCorreoDAO.save(obj);
				} else {
					DatosCorreo obj = (DatosCorreo) getObjectSession(Constantes.MANT_CORREO_SESION);
					obj.setAccion(accionDAO.load(accionSeleccionada));
					obj.setAsunto(asunto);
					obj.setCuerpo(cuerpo.getBytes());
					obj.setFlagActivo(estadoSeleccionado);					
					if (obj.getDatosCorreoXPerfil() != null && !obj.getDatosCorreoXPerfil().isEmpty()) {
						Iterator<DatosCorreoXPerfil> iter = obj.getDatosCorreoXPerfil().iterator();
						while (iter.hasNext()) {
							DatosCorreoXPerfil destinatario = iter.next();
							boolean encontrado = false;
							for (SelectItem item : perfilesB) {
								if (destinatario.getPerfil().getId().equals(Integer.parseInt((String)item.getValue()))) {
									perfilesB.remove(item);
									encontrado = true;
									break;
								}
							}
							if (!encontrado) {
								iter.remove();
							}
						}
					} else {
						obj.setDatosCorreoXPerfil(new ArrayList<DatosCorreoXPerfil>());
					}
					for (SelectItem item : perfilesB) {
						DatosCorreoXPerfil destinatario = new DatosCorreoXPerfil();
						destinatario.setDatosCorreo(obj);
						destinatario.setPerfil(perfilDAO.load(Integer.parseInt((String)item.getValue())));
						obj.getDatosCorreoXPerfil().add(destinatario);
					}
					datosCorreoDAO.update(obj);
				}
			} catch (Throwable t) {
				LOG.error(t.getMessage(), t);
				addComponentMessage(null, "Error en BBDD al intentar ejecutar la operación.");
				return null;
			}
			removeObjectSession(Constantes.MANT_CORREO_SESION);
			return "/mantenimiento/correo/busqueda?faces-redirect=true";
		} else {
			return null;
		}
	}
	
	public String cancelar() {
		removeObjectSession(Constantes.MANT_CORREO_SESION);
		
		return "/mantenimiento/correo/busqueda?faces-redirect=true";
	}
	
	private boolean esValido() {
		boolean resultado = true;
		
		if (perfilSeleccionado < 0) {
			resultado = false;
			addComponentMessage("form:idPerfil", "El campo Perfil es requerido");
		}
		if (tareaSeleccionada < 0) {
			resultado = false;
			addComponentMessage("form:idTarea", "El campo Tarea es requerido");
		}
		if (accionSeleccionada < 0) {
			resultado = false;
			addComponentMessage("form:idAccion", "El campo Acción es requerido");
		}
		if (asunto == null || asunto.trim().equals("")) {
			resultado = false;
			addComponentMessage("form:asunto", "El campo Asunto es requerido");
		}
		if (cuerpo == null || cuerpo.trim().equals("")) {
			resultado = false;
			addComponentMessage("form:cuerpo", "El campo Cuerpo es requerido");
		}
		if (perfilesB == null || perfilesB.isEmpty()) {
			resultado = false;
			addComponentMessage("form:origen", "Debe elegir al menos un destinatario");
		}
		
		return resultado;
	}

	public List<SelectItem> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(List<SelectItem> perfiles) {
		this.perfiles = perfiles;
	}

	public List<SelectItem> getTareas() {
		return tareas;
	}

	public void setTareas(List<SelectItem> tareas) {
		this.tareas = tareas;
	}

	public List<SelectItem> getAcciones() {
		return acciones;
	}

	public void setAcciones(List<SelectItem> acciones) {
		this.acciones = acciones;
	}

	public List<SelectItem> getEstados() {
		return estados;
	}

	public void setEstados(List<SelectItem> estados) {
		this.estados = estados;
	}

	public List<SelectItem> getPerfilesA() {
		return perfilesA;
	}

	public void setPerfilesA(List<SelectItem> perfilesA) {
		this.perfilesA = perfilesA;
	}

	public List<SelectItem> getPerfilesB() {
		return perfilesB;
	}

	public void setPerfilesB(List<SelectItem> perfilesB) {
		this.perfilesB = perfilesB;
	}

	public Integer getPerfilSeleccionado() {
		return perfilSeleccionado;
	}

	public void setPerfilSeleccionado(Integer perfilSeleccionado) {
		this.perfilSeleccionado = perfilSeleccionado;
	}

	public Integer getTareaSeleccionada() {
		return tareaSeleccionada;
	}

	public void setTareaSeleccionada(Integer tareaSeleccionada) {
		this.tareaSeleccionada = tareaSeleccionada;
	}

	public Integer getAccionSeleccionada() {
		return accionSeleccionada;
	}

	public void setAccionSeleccionada(Integer accionSeleccionada) {
		this.accionSeleccionada = accionSeleccionada;
	}

	public String getEstadoSeleccionado() {
		return estadoSeleccionado;
	}

	public void setEstadoSeleccionado(String estadoSeleccionado) {
		this.estadoSeleccionado = estadoSeleccionado;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}

	public List<Integer> getPerfilesSeleccionadosA() {
		return perfilesSeleccionadosA;
	}

	public void setPerfilesSeleccionadosA(List<Integer> perfilesSeleccionadosA) {
		this.perfilesSeleccionadosA = perfilesSeleccionadosA;
	}

	public List<Integer> getPerfilesSeleccionadosB() {
		return perfilesSeleccionadosB;
	}

	public void setPerfilesSeleccionadosB(List<Integer> perfilesSeleccionadosB) {
		this.perfilesSeleccionadosB = perfilesSeleccionadosB;
	}

	public boolean isNuevoRegistro() {
		return nuevoRegistro;
	}

	public void setNuevoRegistro(boolean nuevoRegistro) {
		this.nuevoRegistro = nuevoRegistro;
	}

}
