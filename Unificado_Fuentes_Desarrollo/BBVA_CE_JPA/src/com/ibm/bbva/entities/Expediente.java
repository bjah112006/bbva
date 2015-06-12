package com.ibm.bbva.entities;

import static javax.persistence.CascadeType.ALL;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * The persistent class for the TBL_CE_IBM_EXPEDIENTE database table.
 * 
 */
@Entity
@Table(name="TBL_CE_IBM_EXPEDIENTE", schema = "CONELE")

@NamedQueries({	
	@NamedQuery(name = "Expediente.findAll", query = "SELECT e FROM Expediente e"),
	@NamedQuery(name = "Expediente.findById", query = "SELECT e FROM Expediente e WHERE e.id = :id"),
	@NamedQuery(name = "Expediente.findByIdCliente", query = "SELECT e FROM Expediente e WHERE e.clienteNatural.id = :idCliente"),
	@NamedQuery(name = "Expediente.findByIdExpediente", query = "SELECT e FROM Expediente e WHERE e.expedienteTC.idExpFk = :idExpediente"),
	@NamedQuery(name = "Expediente.findByIdUsuario", query = "SELECT e FROM Expediente e WHERE e.empleado.id = :idUsuario")
})

public class Expediente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CE_IBM_EXPEDIENTE_ID_GENERATOR", sequenceName="SEQ_CE_IBM_EXPEDIENTE", allocationSize=1, schema = "CONELE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CE_IBM_EXPEDIENTE_ID_GENERATOR")
	private long id;

	private String accion;

	private String comentario;

	@Column(name="FEC_REGISTRO")
	private Timestamp fecRegistro;

	@Column(name="FLAG_ACTIVO")
	private String flagActivo;
	
	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_ENVIO")
	private Timestamp fechaEnvio;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_FIN")
	private Timestamp fechaFin;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_PROGRAMADA")
	private Timestamp fechaProgramada;

	@Column(name="NUM_TERMINAL")
	private String numTerminal;

	//bi-directional many-to-one association to AyudaMemoria
	@OneToMany(mappedBy="expediente")
	private List<AyudaMemoria> ayudaMemorias;

	//bi-directional many-to-one association to DevolucionTarea
	@OneToMany(mappedBy="expediente")
	private List<DevolucionTarea> devolucionTareas;

	//bi-directional many-to-one association to DocumentoExpTc
	@OneToMany(mappedBy="expediente")
	private List<DocumentoExpTc> documentoExpTcs;

	//bi-directional many-to-one association to ClienteNatural
	@ManyToOne
	@JoinColumn(name="ID_CLIENTE_NATURAL_FK")
	private ClienteNatural clienteNatural;

	//bi-directional many-to-one association to Empleado
	@ManyToOne
	@JoinColumn(name="ID_EMPLEADO_FK")
	private Empleado empleado;

	//uni-directional many-to-one association to Estado
	@ManyToOne
	@JoinColumn(name="ID_ESTADO_FK")
	private Estado estado;

	//uni-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="ID_PRODUCTO_FK")
	private Producto producto;

	//bi-directional one-to-one association to ExpedienteTc
	@OneToOne(mappedBy="expediente", cascade = ALL)
	private ExpedienteTC expedienteTC;
	
    @Transient
    private VerificacionExp verificacionExpDom;
    
    @Transient
    private VerificacionExp verificacionExpLab;

    @Transient
    private MotivoDevolucion motivoDevolucion;    
    
	public Expediente() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccion() {
		return this.accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Timestamp getFecRegistro() {
		return this.fecRegistro;
	}

	public void setFecRegistro(Timestamp fecRegistro) {
		this.fecRegistro = fecRegistro;
	}

	public Timestamp getFechaEnvio() {
		return this.fechaEnvio;
	}

	public void setFechaEnvio(Timestamp fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public Timestamp getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Timestamp fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Timestamp getFechaProgramada() {
		return this.fechaProgramada;
	}

	public void setFechaProgramada(Timestamp fechaProgramada) {
		this.fechaProgramada = fechaProgramada;
	}

	public String getNumTerminal() {
		return this.numTerminal;
	}

	public void setNumTerminal(String numTerminal) {
		this.numTerminal = numTerminal;
	}

	public List<AyudaMemoria> getAyudaMemorias() {
		return this.ayudaMemorias;
	}

	public void setAyudaMemorias(List<AyudaMemoria> ayudaMemorias) {
		this.ayudaMemorias = ayudaMemorias;
	}

	public AyudaMemoria addAyudaMemoria(AyudaMemoria ayudaMemoria) {
		getAyudaMemorias().add(ayudaMemoria);
		ayudaMemoria.setExpediente(this);

		return ayudaMemoria;
	}

	public AyudaMemoria removeAyudaMemoria(AyudaMemoria ayudaMemoria) {
		getAyudaMemorias().remove(ayudaMemoria);
		ayudaMemoria.setExpediente(null);

		return ayudaMemoria;
	}

	public List<DevolucionTarea> getDevolucionTareas() {
		return this.devolucionTareas;
	}

	public void setDevolucionTareas(List<DevolucionTarea> devolucionTareas) {
		this.devolucionTareas = devolucionTareas;
	}

	public DevolucionTarea addDevolucionTarea(DevolucionTarea devolucionTarea) {
		getDevolucionTareas().add(devolucionTarea);
		devolucionTarea.setExpediente(this);

		return devolucionTarea;
	}

	public DevolucionTarea removeDevolucionTarea(DevolucionTarea devolucionTarea) {
		getDevolucionTareas().remove(devolucionTarea);
		devolucionTarea.setExpediente(null);

		return devolucionTarea;
	}

	public List<DocumentoExpTc> getDocumentoExpTcs() {
		return this.documentoExpTcs;
	}

	public void setDocumentoExpTcs(List<DocumentoExpTc> documentoExpTcs) {
		this.documentoExpTcs = documentoExpTcs;
	}

	public DocumentoExpTc addDocumentoExpTc(DocumentoExpTc documentoExpTc) {
		getDocumentoExpTcs().add(documentoExpTc);
		documentoExpTc.setExpediente(this);

		return documentoExpTc;
	}

	public DocumentoExpTc removeDocumentoExpTc(DocumentoExpTc documentoExpTc) {
		getDocumentoExpTcs().remove(documentoExpTc);
		documentoExpTc.setExpediente(null);

		return documentoExpTc;
	}

	public ClienteNatural getClienteNatural() {
		return this.clienteNatural;
	}

	public void setClienteNatural(ClienteNatural clienteNatural) {
		this.clienteNatural = clienteNatural;
	}

	public Empleado getEmpleado() {
		return this.empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public ExpedienteTC getExpedienteTC() {
		return this.expedienteTC;
	}

	public void setExpedienteTC(ExpedienteTC expedienteTC) {
		this.expedienteTC = expedienteTC;
		expedienteTC.setExpediente(this); // para mantener relación bidireccional
	}

	public VerificacionExp getVerificacionExpDom() {
		return verificacionExpDom;
	}

	public void setVerificacionExpDom(VerificacionExp verificacionExpDom) {
		this.verificacionExpDom = verificacionExpDom;
	}

	public VerificacionExp getVerificacionExpLab() {
		return verificacionExpLab;
	}

	public void setVerificacionExpLab(VerificacionExp verificacionExpLab) {
		this.verificacionExpLab = verificacionExpLab;
	}

	public MotivoDevolucion getMotivoDevolucion() {
		return motivoDevolucion;
	}

	public void setMotivoDevolucion(MotivoDevolucion motivoDevolucion) {
		this.motivoDevolucion = motivoDevolucion;
	}

	public String getFlagActivo() {
		return flagActivo;
	}

	public void setFlagActivo(String flagActivo) {
		this.flagActivo = flagActivo;
	}

}