package pe.ibm.bean;

import java.io.Serializable;
import java.util.Date;

public class ConsultaServicio implements Serializable {

	private static final long serialVersionUID = 1L;
	private String codigoExpediente;
	private String idTarea;
	private String codUsuarioActual;
	private String codCentralCliente;
	private String idOperacion;
	private String numDOICliente;
	private String razonSocialCliente;
	private String idTerritorio;
	private String codUsuarioAbogado;
	private String codEstudioAbogado;
	private String codUsuarioResponsable;
	private Date fechaRegistroIni;
	private Date fechaRegistroFin;
	private Date fechaActivacionIni;
	private Date fechaActivacionFin;
	private String codOficina;

	public ConsultaServicio() {
		this.codigoExpediente = "";
		this.idTarea = "";
		this.codUsuarioActual = "";
		this.codCentralCliente = "";
		this.idOperacion = "";
		this.numDOICliente = "";
		this.razonSocialCliente = "";
		this.idTerritorio = "";
		this.codUsuarioAbogado = "";
		this.codEstudioAbogado = "";
		this.codUsuarioResponsable = "";
		this.fechaRegistroIni = null;
		this.fechaRegistroFin = null;
		this.fechaActivacionIni = null;
		this.fechaActivacionFin = null;
		this.codOficina = "";
	}

	public String getCodigoExpediente() {
		return codigoExpediente;
	}

	public void setCodigoExpediente(String codigoExpediente) {
		this.codigoExpediente = codigoExpediente;
	}

	public String getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(String idTarea) {
		this.idTarea = idTarea;
	}

	public String getCodUsuarioActual() {
		return codUsuarioActual;
	}

	public void setCodUsuarioActual(String codUsuarioActual) {
		this.codUsuarioActual = codUsuarioActual;
	}

	public String getCodCentralCliente() {
		return codCentralCliente;
	}

	public void setCodCentralCliente(String codCentralCliente) {
		this.codCentralCliente = codCentralCliente;
	}

	public String getIdOperacion() {
		return idOperacion;
	}

	public void setIdOperacion(String idOperacion) {
		this.idOperacion = idOperacion;
	}

	public String getNumDOICliente() {
		return numDOICliente;
	}

	public void setNumDOICliente(String numDOICliente) {
		this.numDOICliente = numDOICliente;
	}

	public String getRazonSocialCliente() {
		return razonSocialCliente;
	}

	public void setRazonSocialCliente(String razonSocialCliente) {
		this.razonSocialCliente = razonSocialCliente;
	}

	public String getIdTerritorio() {
		return idTerritorio;
	}

	public void setIdTerritorio(String idTerritorio) {
		this.idTerritorio = idTerritorio;
	}

	public String getCodUsuarioAbogado() {
		return codUsuarioAbogado;
	}

	public void setCodUsuarioAbogado(String codUsuarioAbogado) {
		this.codUsuarioAbogado = codUsuarioAbogado;
	}

	public String getCodEstudioAbogado() {
		return codEstudioAbogado;
	}

	public void setCodEstudioAbogado(String codEstudioAbogado) {
		this.codEstudioAbogado = codEstudioAbogado;
	}

	public String getCodUsuarioResponsable() {
		return codUsuarioResponsable;
	}

	public void setCodUsuarioResponsable(String codUsuarioResponsable) {
		this.codUsuarioResponsable = codUsuarioResponsable;
	}

	public Date getFechaRegistroIni() {
		return fechaRegistroIni;
	}

	public void setFechaRegistroIni(Date fechaRegistroIni) {
		this.fechaRegistroIni = fechaRegistroIni;
	}

	public Date getFechaRegistroFin() {
		return fechaRegistroFin;
	}

	public void setFechaRegistroFin(Date fechaRegistroFin) {
		this.fechaRegistroFin = fechaRegistroFin;
	}

	public Date getFechaActivacionIni() {
		return fechaActivacionIni;
	}

	public void setFechaActivacionIni(Date fechaActivacionIni) {
		this.fechaActivacionIni = fechaActivacionIni;
	}

	public Date getFechaActivacionFin() {
		return fechaActivacionFin;
	}

	public void setFechaActivacionFin(Date fechaActivacionFin) {
		this.fechaActivacionFin = fechaActivacionFin;
	}

	public String getCodOficina() {
		return codOficina;
	}

	public void setCodOficina(String codOficina) {
		this.codOficina = codOficina;
	}

}
