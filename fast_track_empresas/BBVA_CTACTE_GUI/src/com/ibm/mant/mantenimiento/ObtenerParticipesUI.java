package com.ibm.mant.mantenimiento;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.as.core.helpers.TipoDato;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.core.DatosClientePJCore;
import com.ibm.bbva.ctacte.business.ClienteBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="obtenerParticipes")
@ViewScoped
public class ObtenerParticipesUI extends AbstractMBean {
	
	private static final long serialVersionUID = 5084101406594844337L;

	private static final Logger LOG = LoggerFactory.getLogger(ObtenerParticipesUI.class);
	
	private String idExpediente;
	private Empleado empleado;
	
	@EJB
	private ExpedienteDAO expedienteDAO;
	@EJB
	private ClienteBusiness clienteBusiness;
	
	@PostConstruct
	public void init() {
		LOG.info("init()");
		empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
	}
	
	public void actualizarParticipes(AjaxBehaviorEvent event) {
		Integer id = null;
		try {
			id = Integer.parseInt(idExpediente);
		} catch(Exception e) {
			String mensaje = "El campo Nro. Expediente debe ser de tipo "
					+ TipoDato.INTEGER.getNombreMostrar();
			mensajeErrorPrincipal("idExpediente", mensaje);
			return;
		}
		
		Expediente expediente = expedienteDAO.load(id);
		if (expediente != null) {
			try {
				List<ClientePJCore> listaCliente = clienteBusiness.buscarCodigoCentral(expediente.getCliente().getCodigoCentral(), empleado.getCodigo());
				if (listaCliente.isEmpty()) {
					mensajeErrorPrincipal("guardar", "El cliente no existe.");
				} else if (listaCliente.size()==1) {
					DatosClientePJCore datos = clienteBusiness.obtenerDatosClientePJ(listaCliente.get(0).getCodigoCentral(), empleado.getCodigo());
					listaCliente.get(0).setDatosCore(datos);
					
					boolean resultado = Util.actualizarParticipes(expediente, listaCliente.get(0));
					if (resultado) {
						mensajeInfoPrincipal("guardar", "Los partícipes fueron actualizados.");
					} else {
						mensajeErrorPrincipal("guardar", "Ocurrió un error al actualizar los partícipes.");
					}
				} else {
					mensajeErrorPrincipal("guardar", "Ocurrió un error al actualizar los partícipes.");
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				mensajeErrorPrincipal("guardar", "Ocurrió un error al actualizar los partícipes.");
			}
		} else {
			mensajeErrorPrincipal("idExpediente", "No existe el expediente");
		}
	}
	
	public String cancelar(){
		return "/mantenimiento/seleccionarTabla?faces-redirect=true";
	}

	public String getIdExpediente() {
		return idExpediente;
	}

	public void setIdExpediente(String idExpediente) {
		this.idExpediente = idExpediente;
	}

}
