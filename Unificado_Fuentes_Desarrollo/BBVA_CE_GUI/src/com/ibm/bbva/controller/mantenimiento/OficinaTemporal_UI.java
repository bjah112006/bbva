package com.ibm.bbva.controller.mantenimiento;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.controller.*;
import com.ibm.bbva.entities.DescargaLDAP;
import com.ibm.bbva.entities.DescargaLDAPCarteriz;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Multitabla;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.entities.OficinaTemporal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.MutitablaBeanLocal;
import com.ibm.bbva.session.OficinaBeanLocal;
import com.ibm.bbva.session.OficinaTemporalBeanLocal;
import com.ibm.bbva.tabla.util.vo.DescargaLDAPVO;
import com.ibm.bbva.tabla.util.vo.OficinaTemporalVO;
import com.ibm.bbva.util.EnvioMail;
import com.ibm.bbva.util.Util;

@ManagedBean(name = "oficinaTemporal")
@SessionScoped
public class OficinaTemporal_UI extends AbstractSortPagDataTableMBean
{

	private static final Logger LOG = LoggerFactory.getLogger(OficinaTemporal_UI.class);

	@EJB
	private OficinaBeanLocal oficinaBeanLocal;
	
	@EJB
	private MutitablaBeanLocal multitablaBeanLocal;
	
	@EJB
	private OficinaTemporalBeanLocal oficinaTemporalBeanLocal;
	
	@EJB
	private HistorialBeanLocal historialBeanLocal;
	
	@EJB
	private EmpleadoBeanLocal empleadoBeanLocal;
			
	private String filtroSeleccionadoOficina;
	private Date filtroSeleccionadoFechaInicio;
	private Date filtroSeleccionadoFechaFin;
	private String filtroSeleccionadoHoraInicio;
	private String filtroSeleccionadoHoraFin;
	private String filtroSeleccionadoEstado;
	
	private String edicionId;
	private String edicionOficina;
	private Date edicionFechaInicio;
	private Date edicionFechaFin;
	private String edicionHoraInicio;
	private String edicionHoraFin;
	private String edicionEstado;
		
	private List<SelectItem> listaOficina;
	private List<SelectItem> listaEstado;
	
	private List<OficinaTemporalVO> listaOficinaTemporal;
		
	private boolean edicionOficinaDeshabilitado;
	private boolean edicionHoraInicioDeshabilitado;
	private boolean edicionFechaInicioDeshabilitado;
	
	public OficinaTemporal_UI() 
	{
		super();
	}

	public String guardarRegistro() 
	{
		try
		{
			Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
							
			if(this.getEdicionOficina().equals("-1"))
			{
				super.addComponentMessage(null, "Seleccione una Oficina");
				return null;
			}
			else if(this.getEdicionFechaInicio() == null)
			{
				super.addComponentMessage(null, "Ingrese una fecha de inicio");
				return null;
			}
			else if(this.getEdicionFechaFin() == null)
			{
				super.addComponentMessage(null, "Ingrese una fecha de fin");
				return null;
			}
			else if(this.getEdicionHoraInicio() == null || this.getEdicionHoraInicio().length() == 0)
			{
				super.addComponentMessage(null, "Ingrese una hora de inicio");
				return null;
			}
			else if(this.getEdicionHoraFin() == null || this.getEdicionHoraFin().length() == 0)
			{
				super.addComponentMessage(null, "Ingrese una hora de fin");
				return null;
			}
			else if(this.getEdicionEstado().equals("-1"))
			{
				super.addComponentMessage(null, "Seleccione un estado");
				return null;
			}
			else if(empleado.getOficinaBackup() == null && Long.valueOf(this.getEdicionOficina().split("_")[0]).equals(empleado.getOficina().getId()))
			{
				super.addComponentMessage(null, "La oficina seleccionada no puede ser establecida como temporal");
				return null;
			}
			else if((this.getEdicionId() == null || this.getEdicionId().length() == 0) && empleado.getOficinaBackup() != null && Long.valueOf(this.getEdicionOficina().split("_")[0]).equals(empleado.getOficinaBackup().getId()))
			{
				super.addComponentMessage(null, "La oficina seleccionada no puede ser establecida como temporal");
				return null;
			}
			else if((this.getEdicionId() == null || this.getEdicionId().length() == 0) && empleado.getOficinaBackup() != null && Long.valueOf(this.getEdicionOficina().split("_")[0]).equals(empleado.getOficina().getId()))
			{
				super.addComponentMessage(null, "La oficina seleccionada ya se encuentra establecida como temporal");
				return null;
			}
			else
			{		
				SimpleDateFormat dateformat= new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat datetimeformat= new SimpleDateFormat("dd/MM/yyyy HH:mm");
				DateFormat timeformat= new SimpleDateFormat("HH:mm");
				
				Date fechaHoraInicio = datetimeformat.parse(dateformat.format(this.getEdicionFechaInicio()) + " " + this.getEdicionHoraInicio());
				Date fechaHoraFin = datetimeformat.parse(dateformat.format(this.getEdicionFechaFin()) + " " + this.getEdicionHoraFin());
				Date fechaActual = dateformat.parse(dateformat.format(new Date()));
				Date horaActual = timeformat.parse(timeformat.format(new Date()));
				
				if(fechaHoraInicio.compareTo(fechaHoraFin) >= 0)
				{
					super.addComponentMessage(null, "La fecha y hora de inicio debe ser menor a la fecha y hora de fin");
					return null;
				}
				else if(!this.isEdicionFechaInicioDeshabilitado() && fechaActual.compareTo(this.getEdicionFechaInicio()) > 0)
				{
					super.addComponentMessage(null, "La fecha de inicio debe ser igual o mayor a le fecha actual");
					return null;
				}
				else if(fechaActual.compareTo(this.getEdicionFechaFin()) > 0)
				{
					super.addComponentMessage(null, "La fecha de fin debe ser igual o mayor a le fecha actual");
					return null;
				}
				else if(!this.isEdicionHoraInicioDeshabilitado() && fechaActual.compareTo(this.getEdicionFechaInicio()) == 0 && timeformat.parse(this.getEdicionHoraInicio()).compareTo(horaActual) < 0)
				{
					super.addComponentMessage(null, "Si la fecha de inicio es igual a la fecha actual, la hora de inicio deber ser igual o mayor a la hora actual");
					return null;
				}
				else if(fechaActual.compareTo(this.getEdicionFechaFin()) == 0 && timeformat.parse(this.getEdicionHoraFin()).compareTo(horaActual) < 0)
				{
					super.addComponentMessage(null, "Si la fecha de fin es igual a la fecha actual, la hora de fin deber ser igual o mayor a la hora actual");
					return null;
				}
				else
				{
					List<OficinaTemporal> listaOficinaTemporal = null;
					
					if(this.getEdicionEstado().equals("A"))
					{
						listaOficinaTemporal = this.oficinaTemporalBeanLocal.buscarTraslape((this.getEdicionId() == null || this.getEdicionId().length() == 0) ? -1L : Long.parseLong(this.getEdicionId()),
																													empleado.getId(),
																													this.getEdicionFechaInicio(),
																													this.getEdicionFechaFin(),
																													this.getEdicionHoraInicio(),
																													this.getEdicionHoraFin(),
																													"A");

					}
					
					if(listaOficinaTemporal != null && listaOficinaTemporal.size() > 0)
					{
						super.addComponentMessage(null, "Existe un cruce entre el rango de fecha y horas seleccionado y una oficina temporal ya establecida");
						return null;
					}
										
					if(!this.getEdicionOficina().equals(empleado.getOficina()))
					{
						RemoteUtils remoteUtils = new RemoteUtils();
						long cantexp = remoteUtils.countConsultaListaTareasTC(empleado.getCodigo());					
						if(cantexp > 0)
						{
							super.addComponentMessage(null, "No se pueden registrar/actualizar oficinas temporales, usted cuenta con expediente pendientes");
							return null;
						}
					}
																					
				}
			}
			
			OficinaTemporal objOficinaTemporal = null;
			
			if(this.getEdicionId() == null || this.getEdicionId().length() == 0)
			{
										
				objOficinaTemporal = new OficinaTemporal();
				objOficinaTemporal.setEmpleado(empleado);
				objOficinaTemporal.setOficina(this.oficinaBeanLocal.buscarPorId(Long.valueOf(this.getEdicionOficina().split("_")[0])));
				objOficinaTemporal.setFechaInicio(this.getEdicionFechaInicio());
				objOficinaTemporal.setFechaFin(this.getEdicionFechaFin());
				objOficinaTemporal.setHoraInicio(this.getEdicionHoraInicio());
				objOficinaTemporal.setHoraFin(this.getEdicionHoraFin());
				objOficinaTemporal.setEstado(this.getEdicionEstado());
				
				oficinaTemporalBeanLocal.create(objOficinaTemporal);
											
			}
			else
			{		
				
				objOficinaTemporal = this.oficinaTemporalBeanLocal.buscarPorId(Long.valueOf(this.getEdicionId()));
				objOficinaTemporal.setOficina(this.oficinaBeanLocal.buscarPorId(Long.valueOf(this.getEdicionOficina().split("_")[0])));
				objOficinaTemporal.setFechaInicio(this.getEdicionFechaInicio());
				objOficinaTemporal.setFechaFin(this.getEdicionFechaFin());
				objOficinaTemporal.setHoraInicio(this.getEdicionHoraInicio());
				objOficinaTemporal.setHoraFin(this.getEdicionHoraFin());
				objOficinaTemporal.setEstado(this.getEdicionEstado());
				
				this.oficinaTemporalBeanLocal.edit(objOficinaTemporal);
						
			}
			
			EnvioMail envioMail = new EnvioMail();
			List<InternetAddress> listAddresses = new ArrayList<InternetAddress> ();
			List<Empleado> listaEmpleado = null;
			if(empleado.getOficinaBackup() != null)
			{
				listaEmpleado = this.empleadoBeanLocal.buscarGerenteActivoPorOficinaPerfil(empleado.getOficinaBackup().getId(), Constantes.PERFIL_GERENTE_OFICINA);
				for(Empleado objEmpleado : listaEmpleado)
				{
					listAddresses.add(new InternetAddress(objEmpleado.getCorreo()));
				}
				listaEmpleado = this.empleadoBeanLocal.buscarGerenteActivoPorOficinaPerfil(empleado.getOficinaBackup().getId(), Constantes.PERFIL_SUB_GERENTE_OFICINA);
				for(Empleado objEmpleado : listaEmpleado)
				{
					listAddresses.add(new InternetAddress(objEmpleado.getCorreo()));
				}
			}
			else
			{
				listaEmpleado = this.empleadoBeanLocal.buscarGerenteActivoPorOficinaPerfil(empleado.getOficina().getId(), Constantes.PERFIL_GERENTE_OFICINA);
				for(Empleado objEmpleado : listaEmpleado)
				{
					listAddresses.add(new InternetAddress(objEmpleado.getCorreo()));
				}
				listaEmpleado = this.empleadoBeanLocal.buscarGerenteActivoPorOficinaPerfil(empleado.getOficina().getId(), Constantes.PERFIL_SUB_GERENTE_OFICINA);
				for(Empleado objEmpleado : listaEmpleado)
				{
					listAddresses.add(new InternetAddress(objEmpleado.getCorreo()));
				}
			}
			
			listaEmpleado = this.empleadoBeanLocal.buscarGerenteActivoPorOficinaPerfil(objOficinaTemporal.getOficina().getId(), Constantes.PERFIL_GERENTE_OFICINA);
			for(Empleado objEmpleado : listaEmpleado)
			{
				listAddresses.add(new InternetAddress(objEmpleado.getCorreo()));
			}
			listaEmpleado = this.empleadoBeanLocal.buscarGerenteActivoPorOficinaPerfil(objOficinaTemporal.getOficina().getId(), Constantes.PERFIL_SUB_GERENTE_OFICINA);
			for(Empleado objEmpleado : listaEmpleado)
			{
				listAddresses.add(new InternetAddress(objEmpleado.getCorreo()));
			}
						
			InternetAddress[] arrAddresses = listAddresses.toArray(new InternetAddress[0]);
			Multitabla objMultitabla = this.multitablaBeanLocal.buscarPorId(Constantes.PARAMETRO_CORREO_OFICINA_TEMPORAL);
			
			String cuerpoCorreo = objMultitabla.getTexto();
			cuerpoCorreo = cuerpoCorreo.replace("[EMPLEADO]", empleado.getCodigo() + " - " + empleado.getNombresCompletos());
			cuerpoCorreo = cuerpoCorreo.replace("[OFICINA]", objOficinaTemporal.getOficina().getCodigo() + " - " + objOficinaTemporal.getOficina().getDescripcion());
			
			SimpleDateFormat dateformat= new SimpleDateFormat("dd/MM/yyyy");
			cuerpoCorreo = cuerpoCorreo.replace("[PERIODO]", dateformat.format(objOficinaTemporal.getFechaInicio()) + " " + objOficinaTemporal.getHoraInicio() + 
                    								" - " + dateformat.format(objOficinaTemporal.getFechaFin()) + " " + objOficinaTemporal.getHoraFin());
						
			envioMail.enviarMail(objMultitabla.getDescripcion(), arrAddresses, empleado.getCorreo(), cuerpoCorreo);
			
			this.buscarOficinaTemporal();
			return "/oficinaTemporal/formManConsultaOficinaTemporal?faces-redirect=true";
		}
		catch(Exception ex)
		{
			super.addComponentMessage(null,
					"Error al registrar en base de datos");
			return null;		
		}		
							
	}
	
	public String agregar() 
	{			
		this.setEdicionId(null);
		this.setEdicionOficina(null);
		this.setEdicionFechaInicio(null);
		this.setEdicionFechaFin(null);
		this.setEdicionHoraInicio(null);
		this.setEdicionHoraFin(null);
		this.setEdicionEstado("A");
		this.setEdicionOficinaDeshabilitado(false);
		this.setEdicionHoraInicioDeshabilitado(false);
		this.setEdicionFechaInicioDeshabilitado(false);
			
		return "/oficinaTemporal/formManActualizaOficinaTemporal?faces-redirect=true";
	}
	
	public String buscar() 
	{
		try
		{
			this.buscarOficinaTemporal();
		}
		catch(Exception ex)
		{
			super.addComponentMessage(null,
					"Error al realizar unas búsqueda");
		}
			
		return null;
							
	}
	
	public String cancelar() 
	{
		return "/oficinaTemporal/formManConsultaOficinaTemporal?faces-redirect=true";
	}
	
	public String editar() 
	{
		try
		{					
			if(this.obtenerElementoSeleccionado() != null)
			{
				OficinaTemporal objOficinaTemporal = oficinaTemporalBeanLocal.buscarPorId(Long.valueOf(this.obtenerElementoSeleccionado().getId()));
				this.setEdicionId(String.valueOf(objOficinaTemporal.getId()));
				this.setEdicionOficina(String.valueOf(objOficinaTemporal.getOficina().getId()) + "_" + objOficinaTemporal.getOficina().getTerritorio().getDescripcion());
				this.setEdicionFechaInicio(objOficinaTemporal.getFechaInicio());
				this.setEdicionFechaFin(objOficinaTemporal.getFechaFin());
				this.setEdicionHoraInicio(objOficinaTemporal.getHoraInicio());
				this.setEdicionHoraFin(objOficinaTemporal.getHoraFin());
				this.setEdicionEstado(objOficinaTemporal.getEstado());
				this.setEdicionOficinaDeshabilitado(true);
				
				SimpleDateFormat dateformat= new SimpleDateFormat("dd/MM/yyyy");				
				DateFormat timeformat= new SimpleDateFormat("HH:mm");
										
				Date fechaActual = dateformat.parse(dateformat.format(new Date()));
				Date horaActual = timeformat.parse(timeformat.format(new Date()));
				
				this.setEdicionFechaInicioDeshabilitado(false);
				this.setEdicionHoraInicioDeshabilitado(false);
				
				if(fechaActual.compareTo(this.getEdicionFechaInicio()) > 0)
				{
					this.setEdicionFechaInicioDeshabilitado(true);
					this.setEdicionHoraInicioDeshabilitado(true);
				}
				else if(fechaActual.compareTo(this.getEdicionFechaInicio()) == 0)
				{
					this.setEdicionFechaInicioDeshabilitado(true);
					if(fechaActual.compareTo(this.getEdicionFechaInicio()) == 0 && timeformat.parse(this.getEdicionHoraInicio()).compareTo(horaActual) < 0)
					{
						this.setEdicionHoraInicioDeshabilitado(true);
					}
				}
								
				
				return "/oficinaTemporal/formManActualizaOficinaTemporal?faces-redirect=true";
			}
			else
			{
				return null;
			}
		}
		catch(Exception ex)
		{
			super.addComponentMessage(null,
					"Error al consultar en base de datos");
			return null;		
		}
							
	}

	public String limpiar()
	{
		this.setListaOficinaTemporal(null);
		this.setFiltroSeleccionadoOficina(null);
		this.setFiltroSeleccionadoFechaInicio(null);
		this.setFiltroSeleccionadoFechaFin(null);
		this.setFiltroSeleccionadoHoraInicio(null);
		this.setFiltroSeleccionadoHoraFin(null);
		this.setFiltroSeleccionadoEstado(null);		
		return null;
	}
	
	public void buscarOficinaTemporal() throws ParseException
	{
		SimpleDateFormat dateformat= new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat datetimeformat= new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		
		List<OficinaTemporal> listaOficinaTemporal = this.oficinaTemporalBeanLocal.buscar(empleado.getId(),
																							this.getFiltroSeleccionadoFechaInicio(),
																							this.getFiltroSeleccionadoFechaFin(),
																							this.getFiltroSeleccionadoHoraInicio(),
																							this.getFiltroSeleccionadoHoraFin(),
																							this.getFiltroSeleccionadoOficina().split("_")[0],
																							this.getFiltroSeleccionadoEstado());
		
		this.listaOficinaTemporal = new ArrayList<OficinaTemporalVO>();
		OficinaTemporalVO objOficinaTemporalVO = null;
		Date fechaHoraInicio = null;
		Date fechaHoraFin = null;
		for(OficinaTemporal objOficinaTemporal : listaOficinaTemporal)
		{
			objOficinaTemporalVO = new OficinaTemporalVO();
			objOficinaTemporalVO.setId(String.valueOf(objOficinaTemporal.getId()));
			objOficinaTemporalVO.setOficina(objOficinaTemporal.getOficina().getCodigo() + " - " + objOficinaTemporal.getOficina().getDescripcion());
			objOficinaTemporalVO.setTerritorio(objOficinaTemporal.getOficina().getTerritorio().getDescripcion());
			objOficinaTemporalVO.setRangoFechas(dateformat.format(objOficinaTemporal.getFechaInicio()) + " " + objOficinaTemporal.getHoraInicio() + 
					                             " - " + dateformat.format(objOficinaTemporal.getFechaFin()) + " " + objOficinaTemporal.getHoraFin());
			
			fechaHoraInicio = datetimeformat.parse(dateformat.format(objOficinaTemporal.getFechaInicio()) + " " + objOficinaTemporal.getHoraInicio());
			fechaHoraFin = datetimeformat.parse(dateformat.format(objOficinaTemporal.getFechaFin()) + " " + objOficinaTemporal.getHoraFin());
			objOficinaTemporalVO.setExpedientes(this.historialBeanLocal.obtenerNumeroRegistradosXTemporalidad(objOficinaTemporal.getEmpleado().getId(), 
																											  objOficinaTemporal.getOficina().getId(), 
																											  fechaHoraInicio, fechaHoraFin));
			objOficinaTemporalVO.setFlagActivo(objOficinaTemporal.getEstado().equals("A"));
		
			this.listaOficinaTemporal.add(objOficinaTemporalVO);
		}				
	}
	
	public void cargarEstado()
	{
		this.listaEstado = Util.crearItems(multitablaBeanLocal.buscarPorPadre(Constantes.PARAMETRO_ESTADO_OFICINA_TEMPORAL), true, "codigo", "nombre");
	}
	
	public void cargarOficina()
	{		
		List<Oficina> lstOficina = oficinaBeanLocal.buscarTodos();		
		ArrayList<SelectItem> items = new ArrayList<SelectItem> ();
		
		SelectItem selectItem = new SelectItem();
        selectItem.setValue(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
        selectItem.setLabel("");
        items.add(selectItem);
        
        for(Oficina objOficina : lstOficina)
        {
        	if(objOficina.getTerritorio() != null)
        	{
        		selectItem = new SelectItem();
            	selectItem.setValue(objOficina.getId() + "_" + objOficina.getTerritorio().getDescripcion());
                selectItem.setLabel(objOficina.getCodigo() + " - " + objOficina.getDescripcion());
                items.add(selectItem);
        	}        	
        }
                      
		this.listaOficina = items; 		
	}
	
	public OficinaTemporalVO obtenerElementoSeleccionado()
	{
		if(this.getListaOficinaTemporal() != null && this.getListaOficinaTemporal().size() > 0)
		{
			for(OficinaTemporalVO objOficinaTemporalVO : this.getListaOficinaTemporal())
			{
				if(objOficinaTemporalVO.isSeleccion())
				{
					return objOficinaTemporalVO;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public void ordenar(ActionEvent event) {
		// TODO Apéndice de método generado automáticamente
		
	}

	@Override
	public void actualiarAyudaHorario(ActionEvent event) {
		// TODO Apéndice de método generado automáticamente
		
	}

	public String getFiltroSeleccionadoOficina() {
		return filtroSeleccionadoOficina;
	}

	public void setFiltroSeleccionadoOficina(String filtroSeleccionadoOficina) {
		this.filtroSeleccionadoOficina = filtroSeleccionadoOficina;
	}

	public List<SelectItem> getListaOficina() {
		return listaOficina;
	}

	public void setListaOficina(List<SelectItem> listaOficina) {
		this.listaOficina = listaOficina;
	}

	public Date getFiltroSeleccionadoFechaInicio() {
		return filtroSeleccionadoFechaInicio;
	}

	public void setFiltroSeleccionadoFechaInicio(Date filtroSeleccionadoFechaInicio) {
		this.filtroSeleccionadoFechaInicio = filtroSeleccionadoFechaInicio;
	}

	public Date getFiltroSeleccionadoFechaFin() {
		return filtroSeleccionadoFechaFin;
	}

	public void setFiltroSeleccionadoFechaFin(Date filtroSeleccionadoFechaFin) {
		this.filtroSeleccionadoFechaFin = filtroSeleccionadoFechaFin;
	}

	public String getFiltroSeleccionadoHoraInicio() {
		return filtroSeleccionadoHoraInicio;
	}

	public void setFiltroSeleccionadoHoraInicio(String filtroSeleccionadoHoraInicio) {
		this.filtroSeleccionadoHoraInicio = filtroSeleccionadoHoraInicio;
	}

	public String getFiltroSeleccionadoHoraFin() {
		return filtroSeleccionadoHoraFin;
	}

	public void setFiltroSeleccionadoHoraFin(String filtroSeleccionadoHoraFin) {
		this.filtroSeleccionadoHoraFin = filtroSeleccionadoHoraFin;
	}

	public String getFiltroSeleccionadoEstado() {
		return filtroSeleccionadoEstado;
	}

	public void setFiltroSeleccionadoEstado(String filtroSeleccionadoEstado) {
		this.filtroSeleccionadoEstado = filtroSeleccionadoEstado;
	}

	public List<SelectItem> getListaEstado() {
		return listaEstado;
	}

	public void setListaEstado(List<SelectItem> listaEstado) {
		this.listaEstado = listaEstado;
	}

	public String getEdicionId() {
		return edicionId;
	}

	public void setEdicionId(String edicionId) {
		this.edicionId = edicionId;
	}

	public String getEdicionOficina() {
		return edicionOficina;
	}

	public void setEdicionOficina(String edicionOficina) {
		this.edicionOficina = edicionOficina;
	}

	public Date getEdicionFechaInicio() {
		return edicionFechaInicio;
	}

	public void setEdicionFechaInicio(Date edicionFechaInicio) {
		this.edicionFechaInicio = edicionFechaInicio;
	}

	public Date getEdicionFechaFin() {
		return edicionFechaFin;
	}

	public void setEdicionFechaFin(Date edicionFechaFin) {
		this.edicionFechaFin = edicionFechaFin;
	}

	public String getEdicionHoraInicio() {
		return edicionHoraInicio;
	}

	public void setEdicionHoraInicio(String edicionHoraInicio) {
		this.edicionHoraInicio = edicionHoraInicio;
	}

	public String getEdicionHoraFin() {
		return edicionHoraFin;
	}

	public void setEdicionHoraFin(String edicionHoraFin) {
		this.edicionHoraFin = edicionHoraFin;
	}

	public String getEdicionEstado() {
		return edicionEstado;
	}

	public void setEdicionEstado(String edicionEstado) {
		this.edicionEstado = edicionEstado;
	}

	public List<OficinaTemporalVO> getListaOficinaTemporal() {
		return listaOficinaTemporal;
	}

	public void setListaOficinaTemporal(List<OficinaTemporalVO> listaOficinaTemporal) {
		this.listaOficinaTemporal = listaOficinaTemporal;
	}

	public boolean isEdicionOficinaDeshabilitado() {
		return edicionOficinaDeshabilitado;
	}

	public void setEdicionOficinaDeshabilitado(boolean edicionOficinaDeshabilitado) {
		this.edicionOficinaDeshabilitado = edicionOficinaDeshabilitado;
	}

	public boolean isEdicionHoraInicioDeshabilitado() {
		return edicionHoraInicioDeshabilitado;
	}

	public void setEdicionHoraInicioDeshabilitado(
			boolean edicionHoraInicioDeshabilitado) {
		this.edicionHoraInicioDeshabilitado = edicionHoraInicioDeshabilitado;
	}

	public boolean isEdicionFechaInicioDeshabilitado() {
		return edicionFechaInicioDeshabilitado;
	}

	public void setEdicionFechaInicioDeshabilitado(
			boolean edicionFechaInicioDeshabilitado) {
		this.edicionFechaInicioDeshabilitado = edicionFechaInicioDeshabilitado;
	}
	
}
