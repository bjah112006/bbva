package com.ibm.bbva.ctacte.controller.form;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import pe.ibm.bean.ExpedienteCC;

import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="bandejaTemporal")
@RequestScoped
public class BandejaTemporalMB extends AbstractMBean {
	
	@EJB
	private ExpedienteDAO dao;

	private static final long serialVersionUID = 8860449969260544736L;

	public String nuevoBastanteo () {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("nuevoBastanteo ()");
		cargarDatos ("formRegistrarNuevoBastanteo");
		return redirect("/registrarNuevoBastanteo/formRegistrarNuevoBastanteo");
	}
	
	private void cargarDatos (String nombre) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("cargarDatos (String nombre)");
		Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		if (Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION)==null) {
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("expediente nulo");
			Expediente expediente;
			if ("formRegistrarNuevoBastanteo".equals(nombre)) {
				expediente = Util.crearExpedienteCU1();
			} else {
				expediente = dao.load(123);
				expediente.setEmpleado(empleado);
			}
			Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_SESION, expediente);
		}
		if (Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_PROCESO_SESION)==null) {
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("expedienteCC nulo");
			ExpedienteCC expedienteCC = new ExpedienteCC ();
			Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_PROCESO_SESION, expedienteCC);
		}
	}
}
