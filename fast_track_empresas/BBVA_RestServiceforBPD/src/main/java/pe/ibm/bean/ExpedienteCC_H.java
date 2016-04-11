package pe.ibm.bean;

import java.io.Serializable;

public class ExpedienteCC_H extends ExpedienteCC implements Serializable{

	private static final long serialVersionUID = -5011514675610991861L;
	 
	
	private boolean mostrarExpediente; 
	
	
	public boolean isMostrarExpediente() {
		return mostrarExpediente;
	}
	public void setMostrarExpediente(boolean mostrarExpediente) {
		this.mostrarExpediente = mostrarExpediente;
	}
	@Override
	public String toString() {
		return "ExpedienteCC_H [mostrarExpediente="
				+ mostrarExpediente
				+ ", isMostrarExpediente()="
				+ isMostrarExpediente()
				+ ", "
				+ (getIdEstadoExpediente() != null ? "getIdEstadoExpediente()="
						+ getIdEstadoExpediente() + ", " : "")
				+ (getIdEstadoTarea() != null ? "getIdEstadoTarea()="
						+ getIdEstadoTarea() + ", " : "")
				+ (getFechaRegistro() != null ? "getFechaRegistro()="
						+ getFechaRegistro() + ", " : "")
				+ (getFechaUltimoBastanteo() != null ? "getFechaUltimoBastanteo()="
						+ getFechaUltimoBastanteo() + ", "
						: "")
				+ (getCodSemaforo() != null ? "getCodSemaforo()="
						+ getCodSemaforo() + ", " : "")
				+ (getIdOperacion() != null ? "getIdOperacion()="
						+ getIdOperacion() + ", " : "")
				+ (getTaskID() != null ? "getTaskID()=" + getTaskID() + ", "
						: "")
				+ (getActivado() != null ? "getActivado()=" + getActivado()
						+ ", " : "")
				+ (getCodigoExpediente() != null ? "getCodigoExpediente()="
						+ getCodigoExpediente() + ", " : "")
				+ (getEstadoExpediente() != null ? "getEstadoExpediente()="
						+ getEstadoExpediente() + ", " : "")
				+ (getNumeroTarea() != null ? "getNumeroTarea()="
						+ getNumeroTarea() + ", " : "")
				+ (getNombreTarea() != null ? "getNombreTarea()="
						+ getNombreTarea() + ", " : "")
				+ (getEstadoTarea() != null ? "getEstadoTarea()="
						+ getEstadoTarea() + ", " : "")
				+ (getCodUsuarioActual() != null ? "getCodUsuarioActual()="
						+ getCodUsuarioActual() + ", " : "")
				+ (getNomUsuarioActual() != null ? "getNomUsuarioActual()="
						+ getNomUsuarioActual() + ", " : "")
				+ (getCodOficina() != null ? "getCodOficina()="
						+ getCodOficina() + ", " : "")
				+ (getDesOficina() != null ? "getDesOficina()="
						+ getDesOficina() + ", " : "")
				+ (getDesTerritorio() != null ? "getDesTerritorio()="
						+ getDesTerritorio() + ", " : "")
				+ (getCodOperacion() != null ? "getCodOperacion()="
						+ getCodOperacion() + ", " : "")
				+ (getDesOperacion() != null ? "getDesOperacion()="
						+ getDesOperacion() + ", " : "")
				+ (getCodCentralCliente() != null ? "getCodCentralCliente()="
						+ getCodCentralCliente() + ", " : "")
				+ (getNumDOICliente() != null ? "getNumDOICliente()="
						+ getNumDOICliente() + ", " : "")
				+ (getRazonSocialCliente() != null ? "getRazonSocialCliente()="
						+ getRazonSocialCliente() + ", " : "")
				+ (getDatosFlujoCtaCte() != null ? "getDatosFlujoCtaCte()="
						+ getDatosFlujoCtaCte() + ", " : "")
				+ (getOperacionesCtaCte() != null ? "getOperacionesCtaCte()="
						+ getOperacionesCtaCte() + ", " : "")
				+ (getNumeroContrato() != null ? "getNumeroContrato()="
						+ getNumeroContrato() + ", " : "")
				+ (getEnvioContent() != null ? "getEnvioContent()="
						+ getEnvioContent() + ", " : "")
				+ (getFechaServidorP() != null ? "getFechaServidorP()="
						+ getFechaServidorP() : "") + "]";
	}

	
}
