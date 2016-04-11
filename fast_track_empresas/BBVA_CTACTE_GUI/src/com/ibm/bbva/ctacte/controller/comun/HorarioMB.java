package com.ibm.bbva.ctacte.controller.comun;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.as.core.exception.DataAccessException;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Historial;
import com.ibm.bbva.ctacte.bean.HorarioCC;
import com.ibm.bbva.ctacte.bean.Oficina;
import com.ibm.bbva.ctacte.bean.Perfil;
import com.ibm.bbva.ctacte.bean.RangoCC;
import com.ibm.bbva.ctacte.bean.Territorio;
import com.ibm.bbva.ctacte.controller.AbstractTablaMBean;
import com.ibm.bbva.ctacte.dao.HorarioCCDAO;
import com.ibm.bbva.ctacte.dao.OficinaDAO;
import com.ibm.bbva.ctacte.dao.PerfilDAO;
import com.ibm.bbva.ctacte.dao.RangoCCDAO;
import com.ibm.bbva.ctacte.dao.TerritorioDAO;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.bbva.ctacte.vo.HorarioVO;
import com.ibm.mant.tabla.dto.HorarioDTO;

@ManagedBean(name = "horario")
@SessionScoped
public class HorarioMB extends AbstractTablaMBean {
	private static final long serialVersionUID = -5542744867434188121L;
	private static final Logger LOG = LoggerFactory.getLogger(HorarioMB.class);
	private static final HorarioDTO HorarioDTO = null;
	private HorarioVO horarioVO;
	private HorarioVO feriadoVO;
	private HorarioVO rangoVO;

	private List<Oficina> oficinas;
	private List<Territorio> territorios;
	private List<Perfil> perfiles;
	private List<SelectItem> oficinaItems;
	private List<HorarioDTO> semana;

	private List<RangoCC> rangosLunes;
	private List<RangoCC> rangosMartes;
	private List<RangoCC> rangosMiercoles;
	private List<RangoCC> rangosJueves;
	private List<RangoCC> rangosViernes;
	private List<RangoCC> rangosSabado;
	private List<RangoCC> rangosDomingo;
	private List<HorarioCC> excepciones;
	private List<HorarioCC> excepcionesGenerales;

	private boolean mostrarRango;
	private boolean mostrarAgregarFeriado;
	private boolean mostrarAgregarRangoFeriado;
	private boolean preAgregarDesabilitado;
	private String mensaje;

	private String tituloPanelDiaSemana;

	@EJB
	private TerritorioDAO TerritorioDAO;

	@EJB
	private PerfilDAO perfilDAO;

	@EJB
	private OficinaDAO oficinaDAO;

	@EJB
	private HorarioCCDAO horarioCCDAO;

	@EJB
	private RangoCCDAO rangoCCDAO;

	@PostConstruct
	public void iniciar() {
		LOG.info("{iniciar}");
		inicializarFiltros();
	}

	public void testAns() {
		Empleado empleado = new Empleado();
		empleado.setPerfil(new Perfil());
		empleado.setOficina(new Oficina());
		try {

			empleado.getPerfil().setId(1);
			empleado.getOficina().setId(1);

			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

			Historial historial = new Historial();

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

			Calendar c = Calendar.getInstance();  
			c.add(Calendar.DATE, 3);
			Date fechaFin = c.getTime();

			String stringFechaA = "30/07/2015 10:20:56";
			String stringFechaB = "30/07/2015 18:10:56";
			Date fechaA = new Date();
			Date fechaB = fechaFin;

			historial.setFechaEnvio(dateFormat.parse(stringFechaA));
			historial.setFechaFin(dateFormat.parse(stringFechaB));

			System.out.println("Fecha inicial: " + stringFechaA);
			System.out.println("Fecha Final: " + stringFechaB);
			Util.obtenerAnsTienpoReal(historial, empleado);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void inicializarFiltros() {

		horarioVO = new HorarioVO();
		rangoVO =  new HorarioVO();
		oficinas = oficinaDAO.findByIdTerritorio(0);
		territorios = TerritorioDAO.findAllOrderedByCodigo();
		perfiles = perfilDAO.buscarTodos();
		this.oficinaItems = Util.crearItems(oficinas, false, "id",
				"codigo,descripcion", "{0} {1}", true);
		inicializarHorario();
		preAgregarDesabilitado = true;
		excepciones = new ArrayList<HorarioCC>();
		excepcionesGenerales =  new ArrayList<HorarioCC>();
		tituloPanelDiaSemana = "";
		mostrarAgregarFeriado=false;
	}

	private void inicializarHorario() {
		mostrarRango=false;
		rangosLunes = new ArrayList<RangoCC>();
		rangosMartes = new ArrayList<RangoCC>();
		rangosMiercoles = new ArrayList<RangoCC>();
		rangosJueves = new ArrayList<RangoCC>();
		rangosViernes = new ArrayList<RangoCC>();
		rangosSabado = new ArrayList<RangoCC>();
		rangosDomingo = new ArrayList<RangoCC>();

		excepciones = new ArrayList<HorarioCC>();
		excepcionesGenerales =  new ArrayList<HorarioCC>();

	}

	//rendered="#{horario.mostrarAgregarFeriado}"

	public void obtenerHorarioGenerico(AjaxBehaviorEvent event) {
		inicializarHorario();
		try {
			List<RangoCC> rangos = rangoCCDAO.obtenerRangos(horarioVO);
			excepciones = horarioCCDAO.obtenerFeriados(horarioVO);

			for (RangoCC rango : rangos) {
				if(rango.getHorario().getExcepcion()==null||rango.getHorario().getExcepcion().equalsIgnoreCase("")){
					if (rango.getHorario().getDia() == 2) {
						rangosLunes.add(rango);
					} else if (rango.getHorario().getDia() == 3) {
						rangosMartes.add(rango);
					} else if (rango.getHorario().getDia() == 4) {
						rangosMiercoles.add(rango);
					} else if (rango.getHorario().getDia() == 5) {
						rangosJueves.add(rango);
					} else if (rango.getHorario().getDia() == 6) {
						rangosViernes.add(rango);
					} else if (rango.getHorario().getDia() == 7) {
						rangosSabado.add(rango);
					} else if (rango.getHorario().getDia() == 1) {
						rangosDomingo.add(rango);
					}
				}
			}


			Integer[] arrayRangos = new Integer[] {rangosLunes.size(),rangosMartes.size(), rangosMiercoles.size(),
					rangosJueves.size(),rangosViernes.size(), rangosSabado.size(), rangosDomingo.size()};       
			Integer mayorSize = Collections.max(Arrays.asList(arrayRangos));

			completarEspacios(rangosLunes, mayorSize);
			completarEspacios(rangosMartes, mayorSize);
			completarEspacios(rangosMiercoles, mayorSize);
			completarEspacios(rangosJueves, mayorSize);
			completarEspacios(rangosViernes, mayorSize);
			completarEspacios(rangosSabado, mayorSize);
			completarEspacios(rangosDomingo, mayorSize);




			for(HorarioCC feriado : excepciones){
				List<RangoCC> rangosCC  = rangoCCDAO.findByHorarioCC(feriado.getId());
				feriado.setRangos(rangosCC);
			}


			if(horarioVO.getIdPerfil()!=0 && horarioVO.getIdOficina()!=0){
				preAgregarDesabilitado=false;
			}else{
				preAgregarDesabilitado=true;
			}


			mostrarAgregarFeriado = false;
			mostrarAgregarRangoFeriado = false;

		} catch (DataAccessException e) {
			LOG.info("", e);
		}

	}

	public void obtenerHorarioExcepcion() {
		try {
			excepciones = horarioCCDAO.obtenerFeriados(horarioVO);

			for (HorarioCC feriado : excepciones) {
				List<RangoCC> rangosCC = rangoCCDAO.findByHorarioCC(feriado
						.getId());
				feriado.setRangos(rangosCC);
			}
		} catch (DataAccessException e) {
			LOG.info("",e);
		}
	}

	public void obtenerHorarioGenerico() {
		mostrarRango=false;
		rangosLunes = new ArrayList<RangoCC>();
		rangosMartes = new ArrayList<RangoCC>();
		rangosMiercoles = new ArrayList<RangoCC>();
		rangosJueves = new ArrayList<RangoCC>();
		rangosViernes = new ArrayList<RangoCC>();
		rangosSabado = new ArrayList<RangoCC>();
		rangosDomingo = new ArrayList<RangoCC>();
		try {
			List<RangoCC> rangos = rangoCCDAO.obtenerRangos(horarioVO);


			for (RangoCC rango : rangos) {
				if(rango.getHorario().getExcepcion()==null||rango.getHorario().getExcepcion().equalsIgnoreCase("")){

					if (rango.getHorario().getDia() == 2) {
						rangosLunes.add(rango);
					} else if (rango.getHorario().getDia() == 3) {
						rangosMartes.add(rango);
					} else if (rango.getHorario().getDia() == 4) {
						rangosMiercoles.add(rango);
					} else if (rango.getHorario().getDia() == 5) {
						rangosJueves.add(rango);
					} else if (rango.getHorario().getDia() == 6) {
						rangosViernes.add(rango);
					} else if (rango.getHorario().getDia() == 7) {
						rangosSabado.add(rango);
					} else if (rango.getHorario().getDia() == 1) {
						rangosDomingo.add(rango);
					}
				}
			}


			Integer[] arrayRangos = new Integer[] {rangosLunes.size(),rangosMartes.size(), rangosMiercoles.size(),
					rangosJueves.size(),rangosViernes.size(), rangosSabado.size(), rangosDomingo.size()};       
			Integer mayorSize = Collections.max(Arrays.asList(arrayRangos));

			completarEspacios(rangosLunes, mayorSize);
			completarEspacios(rangosMartes, mayorSize);
			completarEspacios(rangosMiercoles, mayorSize);
			completarEspacios(rangosJueves, mayorSize);
			completarEspacios(rangosViernes, mayorSize);
			completarEspacios(rangosSabado, mayorSize);
			completarEspacios(rangosDomingo, mayorSize);


			preAgregarDesabilitado=false;
		} catch (DataAccessException e) {
			LOG.info("", e);
		}

	}


	private void completarEspacios(List<RangoCC> rangos, Integer mayor) {
		if(rangos.size()<mayor){
			int tamanio = mayor - rangos.size();
			for(int i=1; i<=tamanio; i++){
				RangoCC r =  new RangoCC();
				r.setEstado("0");
				r.setRangoInicial("");
				r.setRangoFinal("");
				rangos.add(r);
			}
		}

	}







	private boolean validateFormatoHHMM(String rangoMinutoFinal) {
		boolean error = false;
		String theTime = rangoMinutoFinal;

		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm"); //HH = 24h format
		dateFormat.setLenient(false); //this will not enable 25:67 for example
		try {
			dateFormat.parse(theTime);

		} catch (ParseException e) {
			error = true;
			LOG.info("Error de formato");
		}
		return error;
	}


	/** Mantenimiento rango generico 
	 * preAgregarRangoSemana()
	 * preModificarRangoSemana()
	 * 
	 */

	public void preAgregarRangoSemana(){
		FacesContext context = FacesContext.getCurrentInstance();
		Map requestMap = context.getExternalContext().getRequestParameterMap();
		Integer dayOfWeek = Integer.parseInt((String) requestMap.get("diaSeleccionado"));
		horarioVO.setIdDiaSeleccionado(dayOfWeek.intValue());
		horarioVO.setRangoHoraFinal("");
		horarioVO.setRangoHoraInicial("");
		horarioVO.setRangoMinutoFinal("");
		horarioVO.setRangoMinutoInicial("");
		horarioVO.setEstado(true);
		mostrarRango = true;
		horarioVO.idRango=0;
	}

	public void preModificarRangoSemana(){
		tituloPanelDiaSemana = "Modificar rango";
		FacesContext context = FacesContext.getCurrentInstance();
		Map requestMap = context.getExternalContext().getRequestParameterMap();
		String rangoId = (String)requestMap.get("rangoSeleccionado");
		String diaId = (String)requestMap.get("dia");

		LOG.info("rangoId: " + rangoId);
		LOG.info("diaId: " + diaId);

		if("".equalsIgnoreCase(rangoId)){
			horarioVO.setIdDiaSeleccionado(Integer.valueOf(diaId).intValue());
			horarioVO.setRangoHoraFinal("");
			horarioVO.setRangoHoraInicial("");
			horarioVO.setRangoMinutoFinal("");
			horarioVO.setRangoMinutoInicial("");
			horarioVO.setIdRango(0);
			horarioVO.setEstado(true);
		}else{
			RangoCC rango = rangoCCDAO.load(Integer.valueOf(rangoId).intValue());
			horarioVO.setIdDiaSeleccionado(rango.getHorario().getDia());
			horarioVO.setEstado(rango.getEstado().equalsIgnoreCase("1") ? true : false);
			String[] horaInicial = rango.getRangoInicial().split(":");
			String[] horaFinal = rango.getRangoFinal().split(":");
			horarioVO.setRangoHoraInicial(horaInicial[0]);
			horarioVO.setIdRango(rango.getId());
			horarioVO.setIdHorario(rango.getHorario().getId());
			horarioVO.setRangoMinutoInicial(horaInicial[1]);
			horarioVO.setRangoHoraFinal(horaFinal[0]);
			horarioVO.setRangoMinutoFinal(horaFinal[1]);
		}
		mostrarRango = true;
	}

	public void cerrarGuardarRangoSemana(){
		mostrarRango = false;
		horarioVO.idRango=0;
	}

	public void guardarRangoSemana(){
		RangoCC rango;
		if (!validarRango()){

			if(horarioVO.idRango != 0){
				rango = rangoCCDAO.load(horarioVO.idRango);
				rango.setEstado(horarioVO.isEstado() == true ? "1" : "0");
				rango.setId(horarioVO.getIdRango());
				rango.setRangoInicial(horarioVO.getRangoHoraInicial() + ":" + horarioVO.getRangoMinutoInicial());
				rango.setRangoFinal(horarioVO.getRangoHoraFinal() + ":" + horarioVO.getRangoMinutoFinal());
				rangoCCDAO.update(rango);
			}else{
				HorarioCC horario = null;
				try {
					horario = horarioCCDAO.getDayOFTheWeek(horarioVO.getIdPerfil(), horarioVO.getIdOficina(), horarioVO.getIdDiaSeleccionado());
				} catch (DataAccessException e) {
					LOG.info("horario: " + horario);
				}
				if(horario==null){
					horario =  new HorarioCC();
					horario.setDia(horarioVO.getIdDiaSeleccionado());
					horario.setEstado("1");
					horario.setOficina(oficinaDAO.load(horarioVO.getIdOficina()));
					horario.setPerfil(perfilDAO.load(horarioVO.getIdPerfil()));
					horario.setDescripcion(getNameOFdAY(horarioVO.getIdDiaSeleccionado()));
					horarioCCDAO.save(horario);

					rango = new RangoCC();
					rango.setHorario(horario);
					rango.setEstado(horarioVO.isEstado() == true ? "1" : "0");
					rango.setRangoInicial(horarioVO.getRangoHoraInicial() + ":" + horarioVO.getRangoMinutoInicial());
					rango.setRangoFinal(horarioVO.getRangoHoraFinal() + ":" + horarioVO.getRangoMinutoFinal());
					rangoCCDAO.save(rango);

					//insertarDia y rango
				}else{
					rango = new RangoCC();
					rango.setHorario(horario);
					rango.setEstado(horarioVO.isEstado() == true ? "1" : "0");
					rango.setRangoInicial(horarioVO.getRangoHoraInicial() + ":" + horarioVO.getRangoMinutoInicial());
					rango.setRangoFinal(horarioVO.getRangoHoraFinal() + ":" + horarioVO.getRangoMinutoFinal());
					rangoCCDAO.update(rango);
				}
			}
			obtenerHorarioGenerico();
			mostrarRango = false;
			horarioVO.idRango=0;
		}

	}

	/** -------Mantenimiento Feriado------
	 * method preAgregarFeriado()
	 * method preModificarFeriado()
	 * method guardarFeriado()
	 * method eliminarFeriado()
	 * 
	 * **/

	public void preAgregarFeriado() {

		feriadoVO = new HorarioVO();

		feriadoVO.setEstadoHorario(true);
		feriadoVO.setIdPerfil(horarioVO.getIdPerfil());
		feriadoVO.setIdOficina(horarioVO.getIdOficina());
		feriadoVO.setAplicaTodo(false);
		feriadoVO.setAplicaOficina(false);

		mostrarAgregarFeriado = true;
		mostrarAgregarRangoFeriado = false;
		rangoVO = new HorarioVO();
	}

	public void preModificarFeriado(){
		FacesContext context = FacesContext.getCurrentInstance();
		Map requestMap = context.getExternalContext().getRequestParameterMap();
		Integer idFeriado = Integer.parseInt((String) requestMap.get("idFeriado"));
		HorarioCC horarioCC = horarioCCDAO.load(idFeriado);

		feriadoVO = new HorarioVO();

		feriadoVO.setIdPerfil(horarioCC.getPerfil().getId());
		feriadoVO.setIdOficina(horarioCC.getOficina().getId());
		feriadoVO.setIdHorario(horarioCC.getId());
		feriadoVO.setFecha(horarioCC.getFechaExcepcion());
		feriadoVO.setEstadoHorario("1".equalsIgnoreCase(horarioCC.getEstado()) ? true : false);
		feriadoVO.setAplicaTodo("1".equalsIgnoreCase(horarioCC.getAplicaTodo()) ? true : false);
		feriadoVO.setAplicaOficina("1".equalsIgnoreCase(horarioCC.getAplicaOficina()) ? true : false);
		feriadoVO.setDescripcion(horarioCC.getDescripcion());

		Calendar c = Calendar.getInstance();
		c.setTime(horarioCC.getFechaExcepcion());
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		feriadoVO.setIdDiaSeleccionado(dayOfWeek);
		mostrarAgregarFeriado = true;
		mostrarAgregarRangoFeriado = false;
		rangoVO = new HorarioVO();
	}

	public void cerrarGuardarFeriado(){
		mostrarAgregarFeriado = false;

		mostrarAgregarRangoFeriado = false;
		rangoVO = new HorarioVO();
	}

	public void cerrarGuardarRangoFeriado(){
		mostrarAgregarFeriado = false;

		mostrarAgregarRangoFeriado = false;
		rangoVO = new HorarioVO();
	}

	public void guardarFeriado(){
		if (!validarModificarFeriado()) {

			HorarioCC horario;
			if (feriadoVO.idHorario == 0) {

				horario = new HorarioCC();
				horario.setExcepcion("1");
				horario.setDescripcion(feriadoVO.getDescripcion());
				horario.setFechaExcepcion(feriadoVO.getFecha());
				horario.setEstado(feriadoVO.isEstadoHorario() == true ? "1"
						: "0");
				horario.setOficina(oficinaDAO.load(horarioVO.getIdOficina()));
				horario.setPerfil(perfilDAO.load(horarioVO.getIdPerfil()));
				horario.setAplicaToto(feriadoVO.isAplicaTodo() == true ? "1"
						: "0");
				horario.setAplicaOficina(feriadoVO.isAplicaOficina() == true ? "1"
						: "0");
				Calendar c = Calendar.getInstance();
				c.setTime(feriadoVO.getFecha());
				int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
				horario.setDia(dayOfWeek);
				horarioCCDAO.save(horario);

			} else if (feriadoVO.idHorario != 0) {
				horario = horarioCCDAO.load(feriadoVO.getIdHorario());
				horario.setExcepcion("1");
				horario.setDescripcion(feriadoVO.getDescripcion());
				horario.setFechaExcepcion(feriadoVO.getFecha());
				horario.setEstado(feriadoVO.isEstadoHorario() == true ? "1"
						: "0");
				horario.setOficina(oficinaDAO.load(feriadoVO.getIdOficina()));
				horario.setPerfil(perfilDAO.load(feriadoVO.getIdPerfil()));
				horario.setAplicaToto(feriadoVO.isAplicaTodo() == true ? "1"
						: "0");
				horario.setAplicaOficina(feriadoVO.isAplicaOficina() == true ? "1"
						: "0");
				Calendar c = Calendar.getInstance();
				c.setTime(feriadoVO.getFecha());
				int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
				horario.setDia(dayOfWeek);
				horarioCCDAO.update(horario);
			}

			mostrarAgregarFeriado = false;

			mostrarAgregarRangoFeriado = false;
			rangoVO = new HorarioVO();
			obtenerHorarioExcepcion();
		}
	}


	public void eliminarFeriado(){
		FacesContext context = FacesContext.getCurrentInstance();
		Map requestMap = context.getExternalContext().getRequestParameterMap();
		Integer idFeriado = Integer.parseInt((String) requestMap
				.get("idFeriado"));

		try {

			HorarioCC horarioCC = horarioCCDAO.load(idFeriado);
			List<RangoCC> rangos = rangoCCDAO
					.findByHorarioCC(horarioCC.getId());
			for (RangoCC r : rangos) {
				rangoCCDAO.delete(r);
			}
			horarioCCDAO.delete(horarioCC);
			obtenerHorarioExcepcion();
		} catch (Exception e) {
			LOG.info("",e);
		}
	}



	/** -------Mantenimiento Rango Feriado------
	 * method preAgregarRangoExepcion()
	 * method preModificarRangoFeriado()
	 * method guardarRangoFeriado()
	 * method eliminarRango()
	 * 
	 * **/

	public void preAgregarRangoExepcion(){
		FacesContext context = FacesContext.getCurrentInstance();
		Map requestMap = context.getExternalContext().getRequestParameterMap();
		Integer idFeriado = Integer.parseInt((String) requestMap.get("idFeriado"));
		HorarioCC feriado = horarioCCDAO.load(idFeriado);

		rangoVO = new HorarioVO();
		rangoVO.setTitulo(feriado.getDescripcion() + " " + feriado.getFechaExcepcion());
		rangoVO.setEstado(true);
		rangoVO.setIdRango(0);
		rangoVO.setRangoHoraInicial("");
		rangoVO.setRangoMinutoInicial("");
		rangoVO.setRangoHoraFinal("");
		rangoVO.setRangoMinutoFinal("");
		rangoVO.setIdHorario(feriado.getId());


		mostrarAgregarFeriado = false;

		mostrarAgregarRangoFeriado = true;

	}

	public void preModificarRangoFeriado(){
		FacesContext context = FacesContext.getCurrentInstance();
		Map requestMap = context.getExternalContext().getRequestParameterMap();
		Integer idRangoFeriado = Integer.parseInt((String) requestMap.get("idRangoSeleccionado"));
		RangoCC rango = rangoCCDAO.load(idRangoFeriado);


		rangoVO = new HorarioVO();
		rangoVO.setIdRango(rango.getId());

		rangoVO.setEstado("1".equalsIgnoreCase(rango.getEstado()) ? true : false);

		String[] horaInicial = rango.getRangoInicial().split(":");
		String[] horaFinal = rango.getRangoFinal().split(":");

		rangoVO.setRangoHoraInicial(horaInicial[0]);
		rangoVO.setRangoMinutoInicial(horaInicial[1]);
		rangoVO.setRangoHoraFinal(horaFinal[0]);
		rangoVO.setRangoMinutoFinal(horaFinal[1]);
		rangoVO.setIdHorario(rango.getHorario().getId());

		mostrarAgregarFeriado = false;

		mostrarAgregarRangoFeriado = true;

	}




	public void guardarRangoFeriado(){
		RangoCC rango;
		if (!validarModificarRangoFeriado()) {
			if (rangoVO.idRango == 0) {
				rango = new RangoCC();
				rango.setEstado(rangoVO.isEstado() == true ? "1" : "0");
				rango.setRangoInicial(rangoVO.getRangoHoraInicial() + ":"
						+ rangoVO.getRangoMinutoInicial());
				rango.setRangoFinal(rangoVO.getRangoHoraFinal() + ":"
						+ rangoVO.getRangoMinutoFinal());
				rango.setHorario(horarioCCDAO.load(rangoVO.getIdHorario()));
				rangoCCDAO.save(rango);
			}else if (rangoVO.idRango != 0){
				rango = rangoCCDAO.load(rangoVO.getIdRango());				
				rango.setEstado(rangoVO.isEstado() == true ? "1" : "0");
				rango.setRangoInicial(rangoVO.getRangoHoraInicial() + ":"
						+ rangoVO.getRangoMinutoInicial());
				rango.setRangoFinal(rangoVO.getRangoHoraFinal() + ":"
						+ rangoVO.getRangoMinutoFinal());
				rango.setHorario(horarioCCDAO.load(rangoVO.getIdHorario()));
				rangoCCDAO.update(rango);	
			}
			obtenerHorarioExcepcion();
			mostrarAgregarFeriado = false;

			mostrarAgregarRangoFeriado = false;
			rangoVO = new HorarioVO();
		}
	}

	public void eliminarRango(){
		FacesContext context = FacesContext.getCurrentInstance();
		Map requestMap = context.getExternalContext().getRequestParameterMap();
		Integer idRango = Integer.parseInt((String) requestMap.get("idRango"));
		RangoCC rangoCC = rangoCCDAO.load(idRango);
		rangoCCDAO.delete(rangoCC);
		obtenerHorarioGenerico();
		obtenerHorarioExcepcion();

	}

	private boolean validarModificarFeriado() {
		boolean error = false;

		if (feriadoVO.getFecha() == null) {
			mensaje = "Debe seleccionar un día de la semana";
			return error = true;
		} else if (feriadoVO.getDescripcion().equalsIgnoreCase("")) {
			mensaje = "Debe introducir nombre del feriado";
			return error = true;
		} else {
			return error;
		}
	}

	private boolean validarModificarRangoFeriado() {
		boolean error = false;

		if(rangoVO.getRangoHoraInicial().equalsIgnoreCase("")){
			mensaje = "Debe introducir la hora del rango inicial";
			return error = true;
		}else if(rangoVO.getRangoMinutoInicial().equalsIgnoreCase("")){
			mensaje = "Debe introducir los minutos del rango inicial";
			return error = true;
		}else if(rangoVO.getRangoHoraFinal().equalsIgnoreCase("")){
			mensaje = "Debe introducir la hora del rango final";
			return error = true;
		}else if(rangoVO.getRangoMinutoFinal().equalsIgnoreCase("")){
			mensaje = "Debe introducir los minutos del rango final";
			return error = true;
		}else if(validateFormatoHHMM(rangoVO.getRangoHoraInicial()+":" +rangoVO.getRangoMinutoInicial())){
			mensaje = "Debe introducir un rango incial correcto";
			return error = true;
		}else if(validateFormatoHHMM(rangoVO.getRangoHoraFinal()+":" +rangoVO.getRangoMinutoFinal())){
			mensaje = "Debe introducir un rango final correcto";
			return error = true;
		}else {
			return error;
		}
	}

	private String getNameOFdAY(int idDiaSeleccionado) {
		String nameDay="";
		if (idDiaSeleccionado == 2) {
			nameDay = "LUNES";
		} else if (idDiaSeleccionado == 3) {
			nameDay = "MARTES";
		} else if (idDiaSeleccionado == 4) {
			nameDay = "MIERCOLES";
		} else if (idDiaSeleccionado == 5) {
			nameDay = "JUEVES";
		} else if (idDiaSeleccionado == 6) {
			nameDay = "VIERNES";
		} else if (idDiaSeleccionado == 7) {
			nameDay = "SABADO";
		} else if (idDiaSeleccionado == 1) {
			nameDay = "DOMINGO";
		}
		return nameDay;
	}

	private boolean validarRango() {
		boolean error = false;
		if(horarioVO.getIdDiaSeleccionado()==0){
			mensaje = "Debe seleccionar un día de la semana";
			return error = true;
		}else if(horarioVO.getRangoHoraInicial().equalsIgnoreCase("")){
			mensaje = "Debe introducir la hora del rango inicial";
			return error = true;
		}else if(horarioVO.getRangoMinutoInicial().equalsIgnoreCase("")){
			mensaje = "Debe introducir los minutos del rango inicial";
			return error = true;
		}else if(horarioVO.getRangoHoraFinal().equalsIgnoreCase("")){
			mensaje = "Debe introducir la hora del rango final";
			return error = true;
		}else if(horarioVO.getRangoMinutoFinal().equalsIgnoreCase("")){
			mensaje = "Debe introducir los minutos del rango final";
			return error = true;
		}else if(validateFormatoHHMM(horarioVO.getRangoHoraInicial()+":" +horarioVO.getRangoMinutoInicial())){
			mensaje = "Debe introducir un rango incial correcto";
			return error = true;
		}else if(validateFormatoHHMM(horarioVO.getRangoHoraFinal()+":" +horarioVO.getRangoMinutoFinal())){
			mensaje = "Debe introducir un rango final correcto";
			return error = true;
		}else {
			return error;
		}
	}

	private void crearListaOficinasExp(Integer idTerritorio) {
		LOG.info("idTerritorio: "+idTerritorio);
		this.oficinaItems = Util.crearItems(
				oficinaDAO.findByIdTerritorio(idTerritorio), false, "id",
				"codigo,descripcion", "{0} {1}", true);
		LOG.info("oficinaItems :" +oficinaItems.size());
	}

	public List<SelectItem> getTerritoriosSelectValue() {
		return Util.crearItems(territorios, false, "id", "codigo,descripcion",
				"{0} {1}", true);
	}

	public List<SelectItem> getPerfilesSelectValue() {
		return Util.crearItems(perfiles, "id", "descripcion");
	}

	public void obtenerOficinaPorTerritorio(AjaxBehaviorEvent event) {
		LOG.info("obtenerOficinaPorTerritorio");
		//		if (horarioVO.getIdTerritorio() == 0) {
		//			LOG.info("horarioVO.getIdTerritorio() == 0");
		//			this.oficinaItems = Util.listaVacia();
		//		} else {
		crearListaOficinasExp(Integer.parseInt(String.valueOf(horarioVO
				.getIdTerritorio())));
		//		}
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Oficina> getOficinas() {
		return oficinas;
	}

	public void setOficinas(List<Oficina> oficinas) {
		this.oficinas = oficinas;
	}

	public List<Territorio> getTerritorios() {
		return territorios;
	}

	public void setTerritorios(List<Territorio> territorios) {
		this.territorios = territorios;
	}

	public List<SelectItem> getOficinaItems() {
		return oficinaItems;
	}

	public void setOficinaItems(List<SelectItem> oficinaItems) {
		this.oficinaItems = oficinaItems;
	}

	public HorarioVO getHorarioVO() {
		return horarioVO;
	}

	public void setHorarioVO(HorarioVO horarioVO) {
		this.horarioVO = horarioVO;
	}

	public List<Perfil> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(List<Perfil> perfiles) {
		this.perfiles = perfiles;
	}

	public List<HorarioDTO> getSemana() {
		return semana;
	}

	public void setSemana(List<HorarioDTO> semana) {
		this.semana = semana;
	}

	public List<RangoCC> getRangosLunes() {
		return rangosLunes;
	}

	public void setRangosLunes(List<RangoCC> rangosLunes) {
		this.rangosLunes = rangosLunes;
	}

	public List<RangoCC> getRangosMartes() {
		return rangosMartes;
	}

	public void setRangosMartes(List<RangoCC> rangosMartes) {
		this.rangosMartes = rangosMartes;
	}

	public List<RangoCC> getRangosMiercoles() {
		return rangosMiercoles;
	}

	public void setRangosMiercoles(List<RangoCC> rangosMiercoles) {
		this.rangosMiercoles = rangosMiercoles;
	}

	public List<RangoCC> getRangosJueves() {
		return rangosJueves;
	}

	public void setRangosJueves(List<RangoCC> rangosJueves) {
		this.rangosJueves = rangosJueves;
	}

	public List<RangoCC> getRangosViernes() {
		return rangosViernes;
	}

	public void setRangosViernes(List<RangoCC> rangosViernes) {
		this.rangosViernes = rangosViernes;
	}

	public List<RangoCC> getRangosSabado() {
		return rangosSabado;
	}

	public void setRangosSabado(List<RangoCC> rangosSabado) {
		this.rangosSabado = rangosSabado;
	}

	public List<RangoCC> getRangosDomingo() {
		return rangosDomingo;
	}

	public void setRangosDomingo(List<RangoCC> rangosDomingo) {
		this.rangosDomingo = rangosDomingo;
	}

	public boolean isMostrarRango() {
		return mostrarRango;
	}

	public void setMostrarRango(boolean mostrarRango) {
		this.mostrarRango = mostrarRango;
	}

	public boolean isPreAgregarDesabilitado() {
		return preAgregarDesabilitado;
	}

	public void setPreAgregarDesabilitado(boolean preAgregarDesabilitado) {
		this.preAgregarDesabilitado = preAgregarDesabilitado;
	}

	public List<HorarioCC> getExcepciones() {
		return excepciones;
	}

	public void setExcepciones(List<HorarioCC> excepciones) {
		this.excepciones = excepciones;
	}

	public List<HorarioCC> getExcepcionesGenerales() {
		return excepcionesGenerales;
	}

	public void setExcepcionesGenerales(List<HorarioCC> excepcionesGenerales) {
		this.excepcionesGenerales = excepcionesGenerales;
	}

	public String getTituloPanelDiaSemana() {
		return tituloPanelDiaSemana;
	}

	public void setTituloPanelDiaSemana(String tituloPanelDiaSemana) {
		this.tituloPanelDiaSemana = tituloPanelDiaSemana;
	}

	public HorarioVO getFeriadoVO() {
		return feriadoVO;
	}

	public void setFeriadoVO(HorarioVO feriadoVO) {
		this.feriadoVO = feriadoVO;
	}

	public boolean isMostrarAgregarFeriado() {
		return mostrarAgregarFeriado;
	}

	public void setMostrarAgregarFeriado(boolean mostrarAgregarFeriado) {
		this.mostrarAgregarFeriado = mostrarAgregarFeriado;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public HorarioVO getRangoVO() {
		return rangoVO;
	}

	public void setRangoVO(HorarioVO rangoVO) {
		this.rangoVO = rangoVO;
	}

	public boolean isMostrarAgregarRangoFeriado() {
		return mostrarAgregarRangoFeriado;
	}

	public void setMostrarAgregarRangoFeriado(boolean mostrarAgregarRangoFeriado) {
		this.mostrarAgregarRangoFeriado = mostrarAgregarRangoFeriado;
	}

}
