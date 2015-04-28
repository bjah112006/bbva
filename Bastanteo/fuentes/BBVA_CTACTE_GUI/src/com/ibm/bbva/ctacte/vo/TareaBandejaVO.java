package com.ibm.bbva.ctacte.vo;

import java.io.Serializable;
import java.util.Date;

import pe.ibm.bean.ExpedienteCC;

import com.ibm.bbva.ctacte.util.AyudaHorario;
import com.ibm.bbva.ctacte.util.DateRange;

public class TareaBandejaVO implements Serializable{

	private static final long serialVersionUID = -3063520974547027399L;
	//private static final String ROJO = "rojo.png";
	//private static final String VERDE = "verde.png";
	//private static final String AMARILLO = "amarillo.png";
	private String id;
	private String codCentral;
	private String codTarea;
	private String nomTarea;
	private DateRange strFechaAsignacion;
	private String codEstadoTarea; //de la tarea
	private String dscEstadoTarea; //de la tarea
	private String codSemaforo;
	private String codSemaforoT;
	private DateRange strFechaAtencion;
	private String responsable;
	private String oficinaTarea; // de la tarea
	private String territorioTarea; // de la tarea
	private String expediente; // codigo exp
	private String operacion;
	private String dscCliente; // razon social
	private DateRange strFechaInicio;
	private String codEstadoExp; // del exp
	private String dscEstadoExp; // del exp
	private DateRange strFechaTermino;
	private String abogadoEstudio;
	private String numDoi;
	private String gestor;
	private String razSoc;
	private String oficinaExp; // del Exp
	private String territorioExp; // del Exp
	private ExpedienteCC expedienteCC;
	private String estudio;
	private AyudaHorario ayudaHorario;
	private Integer minutos;
	private Integer verdeMinutos;
	private String codResponsable;
		
	public String getCodResponsable() {
		return codResponsable;
	}
	public void setCodResponsable(String codResponsable) {
		this.codResponsable = codResponsable;
	}
	public TareaBandejaVO() {
		strFechaAsignacion = new DateRange(new Date());
		strFechaAtencion = new DateRange(new Date());
		strFechaInicio = new DateRange(new Date());
		strFechaTermino = new DateRange(new Date());
	}
	public String getOficinaExp() {
		return oficinaExp;
	}

	public void setOficinaExp(String oficinaExp) {
		this.oficinaExp = oficinaExp;
	}

	public String getTerritorioExp() {
		return territorioExp;
	}

	public void setTerritorioExp(String territorioExp) {
		this.territorioExp = territorioExp;
	}
	
	public String getCodTarea() {
		return codTarea;
	}

	public void setCodTarea(String codTarea) {
		this.codTarea = codTarea;
	}

	public String getNomTarea() {
		return nomTarea;
	}

	public void setNomTarea(String nomTarea) {
		this.nomTarea = nomTarea;
	}

	public DateRange getStrFechaAsignacion() {
		return strFechaAsignacion;
	}

	public void setStrFechaAsignacion(DateRange strFechaAsignacion) {
		this.strFechaAsignacion = strFechaAsignacion;
	}

	
	public String getCodEstadoTarea() {
		return codEstadoTarea;
	}

	public void setCodEstadoTarea(String codEstadoTarea) {
		this.codEstadoTarea = codEstadoTarea;
	}

	public String getDscEstadoTarea() {
		return dscEstadoTarea;
	}

	public void setDscEstadoTarea(String dscEstadoTarea) {
		this.dscEstadoTarea = dscEstadoTarea;
	}

	public String getCodSemaforo() {				
		/*if(true){
			//Logica
			if (getExpedienteCC()==null) {			
				return "";	
			}
			ayudaHorario = new AyudaHorario(Integer.valueOf(expedienteCC.getCodOficina()));
			TareaDAO tareaDAO = DAOFactory.getInstance().getTareaDAO();
			List<Tarea> lisTar = tareaDAO.findById(Integer.valueOf(expedienteCC.getNumeroTarea()));
			
			setVerdeMinutos(lisTar.get(0).getVerdeMinutos());
			
			if (lisTar==null || lisTar.isEmpty()){
				return "";
			}
			setMinutos(ayudaHorario.calcularMinutos(expedienteCC.getActivado(), Calendar.getInstance()));
			if(lisTar.get(0).getVerdeMinutos()>=getMinutos()){
				codSemaforo = VERDE;			
			}else{
				if(lisTar.get(0).getAmarilloMinutos()>=getMinutos()){
					codSemaforo = AMARILLO;
				}else{			
					codSemaforo = ROJO;				
				}
			}
			expedienteCC.setCodSemaforo(codSemaforo);
			
			Calendar calendar = Calendar.getInstance();		
			calendar.setTime(expedienteCC.getActivado().getTime());			
			calendar.add(Calendar.MINUTE,getVerdeMinutos());
			strFechaAtencion = new DateRange(calendar.getTime());			
		}*/
		return codSemaforo;
	}

	public void setCodSemaforo(String codSemaforo) {
		this.codSemaforo = codSemaforo;
	}

	public DateRange getStrFechaAtencion() {
		return strFechaAtencion;
	}

	public void setStrFechaAtencion(DateRange strFechaAtencion) {
		this.strFechaAtencion = strFechaAtencion;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public String getOficinaTarea() {
		return oficinaTarea;
	}

	public void setOficinaTarea(String oficinaTarea) {
		this.oficinaTarea = oficinaTarea;
	}

	public String getTerritorioTarea() {
		return territorioTarea;
	}

	public void setTerritorioTarea(String territorioTarea) {
		this.territorioTarea = territorioTarea;
	}

	public String getExpediente() {
		return expediente;
	}

	public void setExpediente(String expediente) {
		this.expediente = expediente;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public String getDscCliente() {
		return dscCliente;
	}

	public void setDscCliente(String dscCliente) {
		this.dscCliente = dscCliente;
	}

	public DateRange getStrFechaInicio() {
		return strFechaInicio;
	}

	public void setStrFechaInicio(DateRange strFechaInicio) {
		this.strFechaInicio = strFechaInicio;
	}

	

	public String getCodEstadoExp() {
		return codEstadoExp;
	}

	public void setCodEstadoExp(String codEstadoExp) {
		this.codEstadoExp = codEstadoExp;
	}

	public String getDscEstadoExp() {
		return dscEstadoExp;
	}

	public void setDscEstadoExp(String dscEstadoExp) {
		this.dscEstadoExp = dscEstadoExp;
	}

	public DateRange getStrFechaTermino() {
		return strFechaTermino;
	}

	public void setStrFechaTermino(DateRange strFechaTermino) {
		this.strFechaTermino = strFechaTermino;
	}

	public String getAbogadoEstudio() {
		return abogadoEstudio;
	}

	public void setAbogadoEstudio(String abogadoEstudio) {
		this.abogadoEstudio = abogadoEstudio;
	}

	public String getCodCentral() {
		return codCentral;
	}

	public void setCodCentral(String codCentral) {
		this.codCentral = codCentral;
	}

	public String getNumDoi() {
		return numDoi;
	}

	public void setNumDoi(String numDoi) {
		this.numDoi = numDoi;
	}

	public String getGestor() {
		return gestor;
	}

	public void setGestor(String gestor) {
		this.gestor = gestor;
	}

	public String getRazSoc() {
		return razSoc;
	}

	public void setRazSoc(String razSoc) {
		this.razSoc = razSoc;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setExpedienteCC(ExpedienteCC expedienteCC) {
		this.expedienteCC = expedienteCC;
	}

	public ExpedienteCC getExpedienteCC() {
		return expedienteCC;
	}

	public String getEstudio() {
		return estudio;
	}

	public void setEstudio(String estudio) {
		this.estudio = estudio;
	}

	public String getCodSemaforoT() {
		return codSemaforo;
	}

	public void setCodSemaforoT(String codSemaforoT) {
		this.codSemaforo = codSemaforoT;
	}

	public Integer getMinutos() {
		return minutos;
	}

	public void setMinutos(Integer minutos) {
		this.minutos = minutos;
	}

	public Integer getVerdeMinutos() {
		return verdeMinutos;
	}

	public void setVerdeMinutos(Integer verdeMinutos) {
		this.verdeMinutos = verdeMinutos;
	}	
}
