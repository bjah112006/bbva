package com.ibm.bbva.controller.common;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Horario;
import com.ibm.bbva.entities.HorarioOficina;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.entities.Territorio;
import com.ibm.bbva.messages.Mensajes;
import com.ibm.bbva.session.HorarioBeanLocal;
import com.ibm.bbva.session.HorarioOficinaBeanLocal;
import com.ibm.bbva.session.OficinaBeanLocal;
import com.ibm.bbva.session.TerritorioBeanLocal;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "horarioSemanal")
@SessionScoped
public class HorarioSemanalMB extends AbstractMBean {

	@EJB
	private OficinaBeanLocal oficinaBean;
	@EJB
	private HorarioBeanLocal horarioBean;
	@EJB
	private HorarioOficinaBeanLocal horarioOficinaBean;
	@EJB
	private TerritorioBeanLocal territorioBean; 
	
	private String idHorario;
	private Date fechaInicio;
	private Date fechaFin;
	private String horaInicio;
	private String horaFin;
	private List<String> diasSeleccionados;
	private List<SelectItem> dias;
	private List<SelectItem> oficinas;
	private String oficinaSeleccionada;
	private boolean activo;
	private boolean excepcion;
	private boolean todoDia;
	private List<Horario> listaHorario;
	private boolean habExepciones;
	private List<SelectItem> territorios;
	private String territorioSeleccionada;
	
	private static final Logger LOG = LoggerFactory.getLogger(HorarioSemanalMB.class);
	
	@PostConstruct
	public void init() {
		
		dias = new ArrayList<SelectItem> (7);
		dias.add(crearItem(String.valueOf(Calendar.SUNDAY), 
				Mensajes.getMensaje("com.ibm.bbva.horario.formHorario.dom")));
		dias.add(crearItem(String.valueOf(Calendar.MONDAY), 
				Mensajes.getMensaje("com.ibm.bbva.horario.formHorario.lun")));
		dias.add(crearItem(String.valueOf(Calendar.TUESDAY), 
				Mensajes.getMensaje("com.ibm.bbva.horario.formHorario.mar")));
		dias.add(crearItem(String.valueOf(Calendar.WEDNESDAY), 
				Mensajes.getMensaje("com.ibm.bbva.horario.formHorario.mie")));
		dias.add(crearItem(String.valueOf(Calendar.THURSDAY), 
				Mensajes.getMensaje("com.ibm.bbva.horario.formHorario.jue")));
		dias.add(crearItem(String.valueOf(Calendar.FRIDAY), 
				Mensajes.getMensaje("com.ibm.bbva.horario.formHorario.vie")));
		dias.add(crearItem(String.valueOf(Calendar.SATURDAY), 
				Mensajes.getMensaje("com.ibm.bbva.horario.formHorario.sab")));
		diasSeleccionados = new ArrayList<String> (7);

		List<Oficina> listaOficina = oficinaBean.buscarTodos();

		oficinas = Util.crearItemsConcat(listaOficina, true, "id", "codigo", "descripcion");
		//oficinas = Util.crearItems(new ArrayList<Oficina>(), true, "id", "descripcion");
		
		List<Territorio> listaTerritorio = territorioBean.buscarTodos();
		territorios = Util.crearItemsE(listaTerritorio, true, "id", "descripcion", "Todos");
		
		listaHorario = new ArrayList<Horario>();
	}
	
	private SelectItem crearItem (String value, String label) {
		SelectItem si = new SelectItem ();
		si.setValue(value);
		si.setLabel(label);
		return si;
	}

	public String getIdHorario() {
		return idHorario;
	}

	public void setIdHorario(String idHorario) {
		this.idHorario = idHorario;
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

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	public List<String> getDiasSeleccionados() {
		return diasSeleccionados;
	}

	public void setDiasSeleccionados(List<String> diasSeleccionados) {
		this.diasSeleccionados = diasSeleccionados;
	}

	public List<SelectItem> getDias() {
		return dias;
	}

	public void setDias(List<SelectItem> dias) {
		this.dias = dias;
	}
	
	public List<SelectItem> getOficinas() {
		return oficinas;
	}

	public void setOficinas(List<SelectItem> oficinas) {
		this.oficinas = oficinas;
	}

	public String getOficinaSeleccionada() {
		return oficinaSeleccionada;
	}

	public void setOficinaSeleccionada(String oficinaSeleccionada) {
		this.oficinaSeleccionada = oficinaSeleccionada;
	}

	public boolean getActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public List<Horario> getListaHorario() {
		return listaHorario;
	}

	public void setListaHorario(List<Horario> listaHorario) {
		this.listaHorario = listaHorario;
	}

	public boolean isExcepcion() {
		return excepcion;
	}

	public void setExcepcion(boolean excepcion) {
		this.excepcion = excepcion;
	}

	public boolean isTodoDia() {
		return todoDia;
	}

	public void setTodoDia(boolean todoDia) {
		this.todoDia = todoDia;
	}

	public boolean isHabExepciones() {
		return habExepciones;
	}

	public void setHabExepciones(boolean habExepciones) {
		this.habExepciones = habExepciones;
	}
	
	public void nuevo (ActionEvent ae) {
		limpiar();
		refrescarJSP();
	}

	public void aceptar (ActionEvent ae) {
		if (esValido()) {
			if (oficinaSeleccionada!=null && !Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(oficinaSeleccionada)) {
				guardar(oficinaSeleccionada);	
			}else{
			    for (SelectItem o :oficinas) {
			    	if (!Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(o.getValue().toString())) {			    
			    	   guardar(o.getValue().toString());
			    	}
			    }
			}
			limpiar();
			obtenerHorario ();
			refrescarJSP();
		}
	}

	private void guardar(String oficinaParam) {
		Horario horario = new Horario();
		horario.setActivo(activo ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
		horario.setExcepcion(excepcion ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);

		horario.setDiaInicio(fechaInicio);
		horario.setTodoDia(todoDia ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
		
		if (!todoDia) {
			horario.setHoraInicio(horaInicio);
			horario.setHoraFin(horaFin);
			horario.setDiaFin(fechaFin);
			DateTime dti = null;
			DateTime dtf = null;
			try {
				dti = new DateTime (Constantes.FORMATO_HORA.parse(horaInicio));
				dtf = new DateTime (Constantes.FORMATO_HORA.parse(horaFin));
			} catch (ParseException e) {}
			
			BigDecimal minutos = BigDecimal.valueOf(Long.valueOf(String.valueOf(Minutes.minutesBetween(dti, dtf).getMinutes())));

			horario.setDom(diasSeleccionados.contains(String.valueOf(Calendar.SUNDAY)) ? minutos:new BigDecimal(0));
			horario.setLun(diasSeleccionados.contains(String.valueOf(Calendar.MONDAY)) ? minutos:new BigDecimal(0));
			horario.setMar(diasSeleccionados.contains(String.valueOf(Calendar.TUESDAY)) ? minutos:new BigDecimal(0));
			horario.setMie(diasSeleccionados.contains(String.valueOf(Calendar.WEDNESDAY)) ? minutos:new BigDecimal(0));
			horario.setJue(diasSeleccionados.contains(String.valueOf(Calendar.THURSDAY)) ? minutos:new BigDecimal(0));
			horario.setVie(diasSeleccionados.contains(String.valueOf(Calendar.FRIDAY)) ? minutos:new BigDecimal(0));
			horario.setSab(diasSeleccionados.contains(String.valueOf(Calendar.SATURDAY)) ? minutos:new BigDecimal(0));
		}
		if (idHorario == null || idHorario.equals("")) {
			Horario hor = horarioBean.create(horario);
			
			HorarioOficina horarioOficina = new HorarioOficina();				
			horarioOficina.setHorario(new Horario());
			horarioOficina.setOficina(new Oficina());
			
			horarioOficina.getHorario().setId(hor.getId());				
			long idOficina = Long.parseLong(oficinaParam);				
			horarioOficina.getOficina().setId(idOficina);
			horarioOficinaBean.create(horarioOficina);
		} else {
			horario.setId(Long.parseLong(idHorario));				
			horarioBean.edit(horario);
		}
	}
	
	public void cambiarTerritorio (ValueChangeEvent vce) {
		Object codigo = vce.getNewValue();
		territorioSeleccionada = (String)codigo;
		if (territorioSeleccionada!=null && 
				!Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(territorioSeleccionada)) {
			
			List<Oficina> lista = oficinaBean.buscarPorIdTerritorio(Long.valueOf(territorioSeleccionada));			
			for (Oficina o : lista) {
				o.setDescripcion(o.getCodigo()+" - "+o.getDescripcion());
			}			
			oficinas.clear();
			oficinas = Util.crearItems(lista, true, "id", "descripcion");
		}else{
			oficinas.clear();
			this.oficinaSeleccionada=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
			List<Oficina> listaOficina = oficinaBean.buscarTodos();
			oficinas = Util.crearItemsConcat(listaOficina, true, "id", "codigo", "descripcion");
		}
		listaHorario.clear();
		limpiar();
		refrescarJSP();
	}

	public void cambiarOficina (ValueChangeEvent vce) {
		Object codigo = vce.getNewValue();
		oficinaSeleccionada = (String)codigo;
		if (oficinaSeleccionada!=null && 
				!Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(oficinaSeleccionada)) {
			obtenerHorario ();
		}
		limpiar();
		refrescarJSP();
	}
	
	private void obtenerHorario () {
		if (oficinaSeleccionada!=null && !Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(oficinaSeleccionada)) {
			listaHorario.clear();
			
			List<HorarioOficina> lista = horarioOficinaBean.buscarPorIdOficina(Long.parseLong(oficinaSeleccionada));

			for (HorarioOficina ho : lista) {
				listaHorario.add(horarioBean.buscarPorId(ho.getHorario().getId()));
			}
		}
	}
	
	private boolean esValido () {
		boolean existeErrores = false;

		String formulario = "frmHorario";
		
		/*if (Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(oficinaSeleccionada)) {
			addMessageError(formulario + ":idOficinaSeleccionada","com.ibm.bbva.common.horarioSemanal.msg.oficina");
			existeErrores = true;
		}*/
		if (fechaInicio == null) {
			if (todoDia) {
				addMessageError(formulario + ":fecIni","com.ibm.bbva.common.horarioSemanal.msg.fechaInicioTodoDia");
			} else {
				addMessageError(formulario + ":fecIni","com.ibm.bbva.common.horarioSemanal.msg.fechaInicio");
			}
			
			existeErrores = true;
		}
		if (todoDia) {
			if (/*erroresHorarioNormal()*/erroresTodoDia()) {
				addMessageError(formulario + ":fecIni","com.ibm.bbva.common.horarioSemanal.msg.todoDia");
				existeErrores = true;
			}
		} else {
			if (fechaFin == null) {
				addMessageError(formulario + ":fecFin","com.ibm.bbva.common.horarioSemanal.msg.fechaFin");
				existeErrores = true;
			}
			if (fechaInicio!=null && fechaFin!=null){
				if (!fechaInicio.before(fechaFin)){
					addMessageError(formulario + ":fecIni","com.ibm.bbva.common.horarioSemanal.msg.fechaInicioMenorFin");
					existeErrores = true;
				} else {
					if (erroresHorarioNormal() /*&& (idHorario==null || idHorario.equals(""))*/) {
						addMessageError(formulario + ":horarioNormal","com.ibm.bbva.common.horarioSemanal.msg.horarioNormal");
						existeErrores = true;
					}
				}
			}
			if (horaInicio==null || (horaInicio=horaInicio.trim()).equals("")) {
				addMessageError(formulario + ":horaInicio","com.ibm.bbva.common.horarioSemanal.msg.horaInicio");
				existeErrores = true;
			}
			if (horaFin==null || (horaFin=horaFin.trim()).equals("")) {
				addMessageError(formulario + ":horaFin","com.ibm.bbva.common.horarioSemanal.msg.horaFin");
				existeErrores = true;
			}
			Date hi = null;
			try {
				hi = Constantes.FORMATO_HORA.parse(horaInicio);
			} catch (ParseException e) {
				existeErrores = true;
			}
			Date hf = null;
			try {
				hf = Constantes.FORMATO_HORA.parse(horaFin);
			} catch (ParseException e) {
				existeErrores = true;
			}
			if (hi!=null && hf!=null && !hi.before(hf)){
				addMessageError(formulario + ":horaInicioMenorFin","com.ibm.bbva.common.horarioSemanal.msg.horaInicio");
				existeErrores = true;
			}
			if (diasSeleccionados.isEmpty()) {
				addMessageError(formulario + ":idSeleccionadoDias","com.ibm.bbva.common.horarioSemanal.msg.dias");
				existeErrores = true;
			}
		}
		return !existeErrores;
	}
	
	private boolean erroresTodoDia () {
		List<HorarioOficina> lista = horarioOficinaBean.buscarPorIdOficina(Long.parseLong(oficinaSeleccionada));
		Horario filtHor = new Horario ();
		
		DateTime dfi = new DateTime (fechaInicio);
		for (HorarioOficina ho : lista) {
			filtHor.setId(ho.getHorario().getId());
			filtHor.setActivo(Constantes.CHECK_SELECCIONADO);
			filtHor.setExcepcion(Constantes.CHECK_SELECCIONADO);
			filtHor.setTodoDia(Constantes.CHECK_SELECCIONADO);
			List<Horario> listaHorario = horarioBean.buscarXcriterios(filtHor);
			if (!listaHorario.isEmpty()) {
				Horario horario = listaHorario.get(0);
				DateTime ddi = new DateTime (horario.getDiaInicio());
				if (dfi.isEqual(ddi)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean erroresHorarioNormal () {
		List<HorarioOficina> lista = horarioOficinaBean.buscarPorIdOficina(Long.parseLong(oficinaSeleccionada));
		//Horario filtHor = new Horario();
		
		String strFechaFin = "31/12/9999";
		
		if(todoDia){
			fechaFin = Util.parseStringToDate(strFechaFin, "dd/MM/yyyy");
		}
		
		Interval intervalo = new Interval (fechaInicio.getTime(), fechaFin.getTime());
		/*LOG.info("inicio pantalla::::"+fechaInicio.getTime());
		LOG.info("fin pantalla::::"+fechaFin.getTime());*/
		
		/*Date horaInicioPantalla = Util.parseStringToDate(horaInicio+":00", "hh:mm:ss");
		Date horaFinPantalla = Util.parseStringToDate(horaFin+":00", "hh:mm:ss");
		Interval intervaloHoras = new Interval (horaInicioPantalla.getTime(), horaFinPantalla.getTime());*/
		
		List<HorarioOficina> listaTmp = new ArrayList<HorarioOficina>(lista);
		
		if (idHorario != null && !idHorario.equals("")) {
			int count = -1;
			for (int i = 0; i< listaTmp.size(); i++) {
				if(listaTmp.get(i).getHorario().getId() == Long.valueOf(idHorario)){
					count = i;
					break;
				}
			}
			if(count >= 0){
				listaTmp.remove(count);
			}
		}

		for (HorarioOficina ho : listaTmp) {
			/*LOG.info("idHorario:::::"+ho.getHorario().getId());
			LOG.info("activo:::::"+ho.getHorario().getActivo());
			LOG.info("excepcion:::::"+ho.getHorario().getExcepcion());
			LOG.info("todoDia:::::"+ho.getHorario().getTodoDia());
			LOG.info("activoPantalla::::"+activo);
			LOG.info("excepcionPantalla::::"+excepcion);*/
			//filtHor.setId(ho.getHorario().getId());
			//filtHor.setActivo(Constantes.CHECK_SELECCIONADO);
			//filtHor.setExcepcion(excepcion ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
			//filtHor.setTodoDia(Constantes.CHECK_NO_SELECCIONADO);
			if (ho.getHorario().getActivo().equals(activo ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO) && 
					ho.getHorario().getExcepcion().equals(excepcion ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO)	/*&&
					ho.getHorario().getTodoDia().equals(todoDia ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO)*/){
				//LOG.info("si cumple");
				if(todoDia && ho.getHorario().getTodoDia().equals("1")){
					return true;
				}
				else{
					Date diaFinReg = ho.getHorario().getDiaFin();
					if(ho.getHorario().getTodoDia().equals("1")){
						diaFinReg = Util.parseStringToDate(strFechaFin, "dd/MM/yyyy");
					}
					//LOG.info("inicio Reg:::"+ho.getHorario().getDiaInicio().getTime());
					//LOG.info("fin Reg:::"+diaFinReg.getTime());
					if(intervalo.contains(ho.getHorario().getDiaInicio().getTime())
						|| intervalo.contains(diaFinReg.getTime())) {
						//Date horaInicioReg = Util.parseStringToDate(ho.getHorario().getHoraInicio() + ":00", "hh:mm:ss");
						//Date horaFinReg = Util.parseStringToDate(ho.getHorario().getHoraFin()  + ":00", "hh:mm:ss");
						//if (intervaloHoras.contains(horaInicioReg.getTime())
						//		|| intervaloHoras.contains(horaFinReg.getTime())) {
						if(todoDia || ho.getHorario().getTodoDia().equals("1")){
							return true;
						}
						else{
							if ((ho.getHorario().getDom().intValue() > 0 && diasSeleccionados.contains(String.valueOf(Calendar.SUNDAY))) ||
									(ho.getHorario().getLun().intValue() > 0 && diasSeleccionados.contains(String.valueOf(Calendar.MONDAY))) ||
									(ho.getHorario().getMar().intValue() > 0 && diasSeleccionados.contains(String.valueOf(Calendar.TUESDAY))) ||
									(ho.getHorario().getMie().intValue() > 0 && diasSeleccionados.contains(String.valueOf(Calendar.WEDNESDAY))) ||
									(ho.getHorario().getJue().intValue() > 0 && diasSeleccionados.contains(String.valueOf(Calendar.THURSDAY))) ||
									(ho.getHorario().getVie().intValue() > 0 && diasSeleccionados.contains(String.valueOf(Calendar.FRIDAY))) ||
									(ho.getHorario().getSab().intValue() > 0 && diasSeleccionados.contains(String.valueOf(Calendar.SATURDAY)))) {
								//LOG.info("entro true");
								return true;
							}
						}
					}
					//}
				}
			}
			
			/*List<Horario> listaHorario = horarioBean.buscarXcriterios(filtHor);
			LOG.info("listaHorario:::"+listaHorario.size());
			if (!listaHorario.isEmpty()) {
				Horario horario = listaHorario.get(0);
				LOG.info("inicio::::"+horario.getDiaInicio().getTime());
				LOG.info("fin::::"+horario.getDiaFin().getTime());
				if (intervalo.contains(horario.getDiaInicio().getTime())
						|| intervalo.contains(horario.getDiaFin().getTime())) {
					LOG.info("s contiene");
					return true;
				}
			}*/
		}
		return false;
	}
	
	public void editar (ActionEvent ae) {		
		obtenerHorario ();
		long idHorario = Long.parseLong(getRequestParameter("idHorario"));
		seleccionarHorario(idHorario);
		refrescarJSP();
		habExepciones = true;
	}

	private void seleccionarHorario(long horarioParm) {
		for (Horario horario : listaHorario) {
			if (horario.getId() == horarioParm) {
				idHorario = String.valueOf(horarioParm);
				fechaInicio = horario.getDiaInicio();
				fechaFin = horario.getDiaFin();
				horaInicio = horario.getHoraInicio();
				horaFin = horario.getHoraFin();
				
				diasSeleccionados.clear();
				agregarLista (horario.getDom(), Calendar.SUNDAY);
				agregarLista (horario.getLun(), Calendar.MONDAY);
				agregarLista (horario.getMar(), Calendar.TUESDAY);
				agregarLista (horario.getMie(), Calendar.WEDNESDAY);
				agregarLista (horario.getJue(), Calendar.THURSDAY);
				agregarLista (horario.getVie(), Calendar.FRIDAY);
				agregarLista (horario.getSab(), Calendar.SATURDAY);

				long oficina = horarioOficinaBean.buscarPorIdHorario(horarioParm).get(0).getOficina().getId();
				oficinaSeleccionada = String.valueOf(oficina);
				activo = Constantes.CHECK_SELECCIONADO.equals(horario.getActivo());
				excepcion = Constantes.CHECK_SELECCIONADO.equals(horario.getExcepcion());
				todoDia = Constantes.CHECK_SELECCIONADO.equals(horario.getTodoDia());
				break;
			}
		}
	}
	
	private void limpiar () {
		idHorario = null;
		fechaInicio = null;
		fechaFin = null;
		horaInicio = null;
		horaFin = null;
		diasSeleccionados.clear();
		activo = false;
		excepcion = false;
		todoDia = false;
		habExepciones = false;
	}
	
	private void agregarLista (BigDecimal minutos, int dia) {
		if (minutos != BigDecimal.ZERO) {
			diasSeleccionados.add(String.valueOf(dia));
		}
	}

	public List<SelectItem> getTerritorios() {
		return territorios;
	}

	public void setTerritorios(List<SelectItem> territorios) {
		this.territorios = territorios;
	}

	public String getTerritorioSeleccionada() {
		return territorioSeleccionada;
	}

	public void setTerritorioSeleccionada(String territorioSeleccionada) {
		this.territorioSeleccionada = territorioSeleccionada;
	}
}
