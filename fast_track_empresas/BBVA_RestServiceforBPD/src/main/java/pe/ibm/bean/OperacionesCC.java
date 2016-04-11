package pe.ibm.bean;

public class OperacionesCC {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String tipoFlujoOperacion;
	private String flagCriterioAuditoriaInterna;
	private String flagEstadoBastanteo;
	private String flagPlazoSubsanacion;
	private String flagCobroComision;
	private Integer idEmpleadoActual;
	
	public String getTipoFlujoOperacion() {
		return tipoFlujoOperacion;
	}
	public void setTipoFlujoOperacion(String tipoFlujoOperacion) {
		this.tipoFlujoOperacion = tipoFlujoOperacion;
	}
	public String getFlagCriterioAuditoriaInterna() {
		return flagCriterioAuditoriaInterna;
	}
	public void setFlagCriterioAuditoriaInterna(String flagCriterioAuditoriaInterna) {
		this.flagCriterioAuditoriaInterna = flagCriterioAuditoriaInterna;
	}
	public String getFlagEstadoBastanteo() {
		return flagEstadoBastanteo;
	}
	public void setFlagEstadoBastanteo(String flagEstadoBastanteo) {
		this.flagEstadoBastanteo = flagEstadoBastanteo;
	}
	public String getFlagPlazoSubsanacion() {
		return flagPlazoSubsanacion;
	}
	public void setFlagPlazoSubsanacion(String flagPlazoSubsanacion) {
		this.flagPlazoSubsanacion = flagPlazoSubsanacion;
	}
	public String getFlagCobroComision() {
		return flagCobroComision;
	}
	public void setFlagCobroComision(String flagCobroComision) {
		this.flagCobroComision = flagCobroComision;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public Integer getIdEmpleadoActual() {
		return idEmpleadoActual;
	}
	public void setIdEmpleadoActual(Integer idEmpleadoActual) {
		this.idEmpleadoActual = idEmpleadoActual;
	}

}
