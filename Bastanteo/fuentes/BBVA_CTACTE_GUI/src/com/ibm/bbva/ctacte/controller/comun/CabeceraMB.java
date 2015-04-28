package com.ibm.bbva.ctacte.controller.comun;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.grupobbva.bc.per.tele.seguridad.ServiciosSeguridadBbva;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Oficina;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="cabecera")
@SessionScoped
public class CabeceraMB extends AbstractMBean {
	
	@EJB
	private EmpleadoDAO empleadoDAO;

	private static final long serialVersionUID = 3955228332323138178L;
	
	private static final Logger LOG = LoggerFactory.getLogger(CabeceraMB.class);
	
	private Oficina oficina;
	private Empleado empleado;
	private boolean usrNoRegSistem;
	private boolean mostrarManualUsuario;

	@PostConstruct
	public void iniciar () {
		LOG.info("iniciar ()");
		empleado = cargarDatos ();
		if (empleado != null) {
			oficina = empleado.getOficina();
		} else {
			oficina = new Oficina();
			empleado = new Empleado();
		}
	}
	
	private Empleado cargarDatos () {
		LOG.info("cargarDatos ()");
		//HttpServletRequest request = (HttpServletRequest) getExternalContext().getRequest();
		//String codigo = request.getRemoteUser();
		String codigo = "";
		LOG.info("Usuario : {}",codigo);
		
		ServiciosSeguridadBbva ssBbva = new ServiciosSeguridadBbva((HttpServletRequest) getExternalContext().getRequest());
		if(ssBbva != null) {
			ssBbva.obtener_ID();
			codigo  =  ssBbva.getUsuario().toUpperCase(); 
			LOG.info("Usuario : {}",codigo);
		}else{
			LOG.warn("No se encontro el Usurio en el request : {}",codigo);
		}
		
		Empleado empleado = empleadoDAO.findByCodigo(codigo);
		if (empleado == null) {
			usrNoRegSistem = true;
			mostrarManualUsuario = false;
			LOG.warn("El empleado (usuario: {}) esta registrado en LDAP y no esta en el sistema", codigo);
		} else if (ConstantesBusiness.FLAG_EMPLEADO_INACTIVO.equals(empleado.getFlagActivo())) {
			LOG.info("El empleado {} esta inactivo", empleado.getCodigo());
			usrNoRegSistem = false;
			mostrarManualUsuario = false;
			HttpServletResponse response = (HttpServletResponse) getExternalContext().getResponse();
			try {
				response.sendRedirect("ibm_security_logout?logoutExitPage=/index.jsp");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			usrNoRegSistem = false;
			mostrarManualUsuario = empleado.getPerfil().getCodigo().equals(ConstantesBusiness.CODIGO_PERFIL_GESTOR);
		}
		Util.addObjectSession(ConstantesAdmin.EMPLEADO_SESION, empleado);
		return empleado;
	}

	public Oficina getOficina() {
		return oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public void setUsrNoRegSistem(boolean usrNoRegSistem) {
		this.usrNoRegSistem = usrNoRegSistem;
	}

	public boolean isUsrNoRegSistem() {
		return usrNoRegSistem;
	}
	
	public boolean isMostrarManualUsuario() {
		return mostrarManualUsuario;
	}

	public void setMostrarManualUsuario(boolean mostrarManualUsuario) {
		this.mostrarManualUsuario = mostrarManualUsuario;
	}
	
}
