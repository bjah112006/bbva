package com.ibm.mant.mantenimiento;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.ctacte.bean.Carterizacion;
import com.ibm.bbva.ctacte.bean.CarterizacionId;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.ProductoCE;
import com.ibm.bbva.ctacte.bean.Territorio;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.dao.CarterizacionDAO;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.ProductoCEDAO;
import com.ibm.bbva.ctacte.dao.TerritorioDAO;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.mant.tabla.vo.CarterizacionVO;

@ManagedBean(name = "actualizarCarterizacion")
@SessionScoped
public class ActualizarCarterizacionUI extends AbstractMBean {

	private static final Logger LOG = LoggerFactory
			.getLogger(ActualizarCarterizacionUI.class);
	private static final long serialVersionUID = -3074210987043104170L;

	public List<SelectItem> productos;
	public List<SelectItem> territorios;

	public List<ProductoCE> listaProductos;
	public List<Territorio> listaTerritorios;

	@EJB
	private ProductoCEDAO productoDAO;
	@EJB
	private TerritorioDAO territorioDAO;
	@EJB
	private EmpleadoDAO empleadoDAO;
	@EJB
	private CarterizacionDAO carterizacionDAO;

	private Carterizacion carterizacion;
	private CarterizacionVO carterizacionVO;
	private int idProducto;
	private int idTerritorio;
	private int idEmpleado;

	private String idCarterizacion;

	public ActualizarCarterizacionUI() {
		// super();
		LOG.info("Entra al Constructor");
	}

	@PostConstruct
	public void init() {
		productos = Util.listaVacia();
		territorios = Util.listaVacia();

		/* Carga Lista de Tipo de Productos */
		productos = Util.crearItems(productoDAO.findAll(), true, "id",
				"descripcion");
		/* Carga Lista de Territorios */
		territorios = Util.crearItems(territorioDAO.findAll(), true, "id",
				"descripcion");

		load();

	}

	/**
	 * Load
	 */
	public void load() {

		FacesContext ctx = FacesContext.getCurrentInstance();
		carterizacion = (Carterizacion) getObjectSession(Constantes.CARTERIZACION_SESION);
		carterizacionVO = new CarterizacionVO();

		if (carterizacion != null) { // inicializa cuando ya existe.

			LOG.info("Entra al init de actualizar datos existentes");
			LOG.info("Id Carterizacion " + carterizacion.getId());

			idProducto = carterizacion.getId().getIdProductoFk();
			idTerritorio = carterizacion.getId().getIdTerritorioFk();
			idEmpleado = carterizacion.getId().getIdEmpleadoFk();

			if (carterizacion != null) {

				idCarterizacion = "1";
				carterizacionVO.setIdProducto(idProducto);
				carterizacionVO.setIdTerritorio(idTerritorio);
				carterizacionVO.setIdEmpleado(idEmpleado);

				carterizacionVO
						.setCodigo((carterizacion.getCodigo() != null) ? carterizacion
								.getCodigo() : "");
				carterizacionVO
						.setDescripcion((carterizacion.getDescripcion() != null) ? carterizacion
								.getDescripcion() : "");
				carterizacionVO
						.setStrProducto((carterizacion.getProductoCE() != null) ? String
								.valueOf(carterizacion.getProductoCE().getId())
								: Constantes.CODIGO_CODIGO_CAMPO_VACIO);
				carterizacionVO
						.setStrTerritorio((carterizacion.getTerritorio() != null) ? carterizacion
								.getTerritorio().getId().toString()
								: Constantes.CODIGO_CODIGO_CAMPO_VACIO);
				carterizacionVO
						.setStrEmpleado((carterizacion.getEmpleado() != null) ? carterizacion
								.getEmpleado().getId().toString()
								: Constantes.CODIGO_CODIGO_CAMPO_VACIO);
				carterizacionVO.setCodigoEmpleado((carterizacion.getEmpleado()
						.getCodigo() != null) ? carterizacion.getEmpleado()
						.getCodigo() : "");
				carterizacionVO.setNombreEmpleado((carterizacion.getEmpleado()
						.getNombresCompletos() != null) ? carterizacion
						.getEmpleado().getNombresCompletos() : "");
			}

		} else {

			// carterizacionVO.setIdProducto();
			// carterizacionVO.setIdTerritorio();
			// carterizacionVO.setIdEmpleado();
			carterizacionVO.setIdCarterizacion(null);
			carterizacionVO.setCodigo("");
			carterizacionVO.setDescripcion("");
			carterizacionVO
					.setStrProducto(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
			carterizacionVO
					.setStrTerritorio(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
			carterizacionVO
					.setStrEmpleado(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
			carterizacionVO.setCodigoEmpleado("");
			carterizacionVO.setNombreEmpleado("");
		}

	}

	/**
	 * Clear
	 */
	public void clear() {
		removeObjectSession(Constantes.CARTERIZACION_SESION);

		carterizacionVO = new CarterizacionVO();

		idCarterizacion = null;

		idProducto = 0;
		idTerritorio = 0;
		idEmpleado = 0;

		carterizacionVO.setIdCarterizacion(null);
		carterizacionVO.setCodigo("");
		carterizacionVO.setDescripcion("");
		carterizacionVO.setStrProducto(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
		carterizacionVO.setStrTerritorio(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
		carterizacionVO.setStrEmpleado(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
		carterizacionVO.setCodigoEmpleado("");
		carterizacionVO.setNombreEmpleado("");

	}

	/**
	 * Carga una carterizaion al ser seleccionado
	 * 
	 * @param
	 * @param seleccion
	 */

	public void buscarEmpleado(AjaxBehaviorEvent event) {

		try {

			String codigo = carterizacionVO.getCodigoEmpleado();
			FacesContext ctx = FacesContext.getCurrentInstance();
			ConsultarCarterizacionUI consultarCarterizacionUI = (ConsultarCarterizacionUI) ctx
					.getApplication().getVariableResolver()
					.resolveVariable(ctx, "consultarCarterizacion");
			consultarCarterizacionUI.init();
			removeObjectSession(Constantes.CARTERIZACION_SESION);
			Empleado empleado = new Empleado();
			empleado = empleadoDAO.findByCodigo(codigo);
			if (empleado != null) {
				carterizacionVO.setNombreEmpleado(empleado
						.getNombresCompletos());
				carterizacionVO.setStrEmpleado(empleado.getId().toString());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error("Error : " + ex.getMessage());

			String idFormulario = "form";
			addComponentMessageFromPropertiesFile(idFormulario
					+ ":codigoEmpleado",
					"com.ibm.bbva.common.Carterizacion.msg.EmpleadoNoEncontrado");
		}
		// return
		// "/mantenimiento/carterizacion/actualizarCarterizacion?faces-redirect=true";
	}

	/**
	 * Cancelar edición de carterizacion.
	 * 
	 * @return
	 */
	public String cancelar() {
		LOG.debug("cancelar()");

		FacesContext ctx = FacesContext.getCurrentInstance();
		ConsultarCarterizacionUI consultarCarterizacionUI = (ConsultarCarterizacionUI) ctx
				.getApplication().getVariableResolver()
				.resolveVariable(ctx, "consultarCarterizacion");
		consultarCarterizacionUI.init();
		removeObjectSession(Constantes.CARTERIZACION_SESION);
		return "/mantenimiento/carterizacion/consultaCarterizacion?faces-redirect=true";
	}

	/**
	 * Guardad entidad de carterizacion, si existe un idCargado entonces
	 * actualizar; sino, crearlo.
	 * 
	 * @param idCabecera
	 */

	public String guardar() {

		if (!esValido())
			return "";

		LOG.debug("guardar()");

		Boolean error = false;

		if (carterizacion != null)
			LOG.debug("guardar() - : "
					+ carterizacion.getId().getIdProductoFk());

		Carterizacion carterizacion = new Carterizacion();

		try {

			CarterizacionId carterizacionId = new CarterizacionId();
			carterizacionId.setIdEmpleadoFk(Integer.valueOf(carterizacionVO
					.getStrEmpleado()));
			carterizacionId.setIdProductoFk(Integer.valueOf(carterizacionVO
					.getStrProducto()));
			carterizacionId.setIdTerritorioFk(Integer.valueOf(carterizacionVO
					.getStrTerritorio()));

			carterizacion.setId(carterizacionId);

			carterizacion
					.setCodigo((carterizacionVO.getCodigo() != null) ? carterizacionVO
							.getCodigo() : "");
			carterizacion
					.setDescripcion((carterizacionVO.getDescripcion() != null) ? carterizacionVO
							.getDescripcion() : "");

			LOG.debug("guardar() - Obtener id de producto seleccionado : "
					+ carterizacionVO.getStrTerritorio());

			if (carterizacionVO.getStrProducto() != Constantes.CODIGO_CODIGO_CAMPO_VACIO) {
				ProductoCE producto = productoDAO.load(Integer
						.parseInt(carterizacionVO.getStrProducto()));
				carterizacion.setProducto(producto);
			} else {
				carterizacion.setProducto(null);
			}

			LOG.debug("guardar() - Obtener id de territorio seleccionado : "
					+ carterizacionVO.getStrTerritorio());

			if (carterizacionVO.getStrTerritorio() != Constantes.CODIGO_CODIGO_CAMPO_VACIO) {
				Territorio territorio = territorioDAO.load(Integer
						.parseInt(carterizacionVO.getStrTerritorio()));
				carterizacion.setTerritorio(territorio);
			} else {
				carterizacion.setTerritorio(null);
			}

			if (carterizacionVO.getStrEmpleado() != Constantes.CODIGO_CODIGO_CAMPO_VACIO) {
				Empleado empleado = empleadoDAO.load(Integer
						.parseInt(carterizacionVO.getStrEmpleado()));
				carterizacion.setEmpleado(empleado);
			} else {
				carterizacion.setEmpleado(null);
			}

			LOG.debug("ID CARTERIZACION : " + idCarterizacion);

			if (idCarterizacion != null)
				carterizacionDAO.update(carterizacion);
			else
				carterizacionDAO.save(carterizacion);

		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error("Error : " + ex.getMessage());
			addErrorMessage(ex);
			error = true;
		}

		if (error) {
			return "";
		} else {
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx = FacesContext.getCurrentInstance();
			ConsultarCarterizacionUI consultarCarterizacionUI = (ConsultarCarterizacionUI) ctx
					.getApplication().getVariableResolver()
					.resolveVariable(ctx, "consultarCarterizacion");
			consultarCarterizacionUI.limpiar();
			consultarCarterizacionUI.init();

			return "/mantenimiento/carterizacion/consultaCarterizacion?faces-redirect=true";
		}

	}

	public boolean esValido() {

		// Código
		boolean bValid = true;
		String idFormulario = "form";

		// Producto
		if (carterizacionVO.getStrProducto() == null
				|| carterizacionVO.getStrProducto().equals("-1")) {
			addComponentMessageFromPropertiesFile(idFormulario + ":idProducto",
					"com.ibm.bbva.common.Carterizacion.msg.Producto.Obligatorio");
			bValid = false;
		}

		// Territorio
		if (carterizacionVO.getStrTerritorio() == null
				|| carterizacionVO.getStrTerritorio().equals("-1")) {
			addComponentMessageFromPropertiesFile(idFormulario
					+ ":idTerritorio",
					"com.ibm.bbva.common.Carterizacion.msg.Territorio.Obligatorio");
			bValid = false;
		}

		// Empleado
		if (carterizacionVO.getStrEmpleado() == null
				|| carterizacionVO.getStrEmpleado().equals("-1")) {
			addComponentMessageFromPropertiesFile(idFormulario
					+ ":codigoEmpleado",
					"com.ibm.bbva.common.Carterizacion.msg.Empleado.Obligatorio");
			bValid = false;
		}

		// Codigo de Carterizacion
		if (carterizacionVO.getCodigo() == null) {
			addComponentMessageFromPropertiesFile(idFormulario
					+ ":codigoCarterizacion",
					"com.ibm.bbva.common.Carterizacion.msg.Codigo.Obligatorio");
			bValid = false;
		} else {
			if (carterizacionVO.getCodigo().trim().length() == 0) {
				addComponentMessageFromPropertiesFile(idFormulario
						+ ":codigoCarterizacion",
						"com.ibm.bbva.common.Carterizacion.msg.Codigo.Vacio");
				bValid = false;
			}
		}

		// Descripcion
		if (carterizacionVO.getDescripcion() == null) {
			addComponentMessageFromPropertiesFile(idFormulario
					+ ":descripcionCarterizacion",
					"com.ibm.bbva.common.Carterizacion.msg.Descripcion.Obligatorio");
			bValid = false;
		} else {
			if (carterizacionVO.getDescripcion().trim().length() == 0) {
				addComponentMessageFromPropertiesFile(idFormulario
						+ ":descripcionCarterizacion",
						"com.ibm.bbva.common.Carterizacion.msg.Descripcion.Vacio");
				bValid = false;
			}
		}

		return bValid;
	}

	public List<SelectItem> getProductos() {
		return productos;
	}

	public void setProductos(List<SelectItem> productos) {
		this.productos = productos;
	}

	public List<SelectItem> getTerritorios() {
		return territorios;
	}

	public void setTerritorios(List<SelectItem> territorios) {
		this.territorios = territorios;
	}

	public List<ProductoCE> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos(List<ProductoCE> listaProductos) {
		this.listaProductos = listaProductos;
	}

	public List<Territorio> getListaTerritorios() {
		return listaTerritorios;
	}

	public void setListaTerritorios(List<Territorio> listaTerritorios) {
		this.listaTerritorios = listaTerritorios;
	}

	public CarterizacionVO getCarterizacionVO() {
		return carterizacionVO;
	}

	public void setCarterizacionVO(CarterizacionVO carterizacionVO) {
		this.carterizacionVO = carterizacionVO;
	}

	public String getIdCarterizacion() {
		return idCarterizacion;
	}

	public void setIdCarterizacion(String idCarterizacion) {
		this.idCarterizacion = idCarterizacion;
	}

}
