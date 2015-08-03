package com.ibm.bbva.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="TBL_CE_IBM_MULTITABLA", schema = "CONELE")

@NamedQueries({
	@NamedQuery(name="Multitabla.findByPadre", query="SELECT c FROM Multitabla c WHERE c.parametro = :idPadre"),
	@NamedQuery(name="Multitabla.findById", query="SELECT c FROM Multitabla c WHERE c.id = :id")
})
public class Multitabla implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long parametro;
	private Long tipo;
	private String nombre;
	private String descripcion;
	private String estado;
	private String codigo;
	private String codigoEti;
	private String codigoHabil;
	private Long entero;
	private String enteroEti;
	private String enteroHabil;
	private BigDecimal decimales;
	private String decimalesEti;
	private String decimalesHabil;
	private String texto;
	private String textoEti;
	private String textoHabil;
	private Date fecha;
	private String fechaEti;
	private String fechaHabil;
	private String hora;
	private String horaEti;
	private String horaHabil;
	private String booleano;
	private String booleanoEti;
	private String booleanoHabil;
	private String funcion;
	private String funcionEti;
	private String funcionHabil;
	private String funcionMsg;
	private String permiteHijo;
	private Date fechaModifica;
	private Date fechaCrea;
	private String usuCrea;
	private String usuModifica;
	private Long parametroTipo;

	public Multitabla() {}

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ID_PARAMETRIA", precision = 10, scale = 0)
	public Long getParametro() {
		return this.parametro;
	}

	public void setParametro(Long parametro) {
		this.parametro = parametro;
	}

	@Column(name = "TIPO", nullable = false, precision = 10, scale = 0)
	public Long getTipo() {
		return this.tipo;
	}

	public void setTipo(Long tipo) {
		this.tipo = tipo;
	}

	@Column(name = "NOMBRE", nullable = false, length = 400)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "DESCRIPCION", length = 2000)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "ESTADO", nullable = false, length = 4)
	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Column(name = "CODIGO", length = 20)
	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Column(name = "CODIGO_ETI", length = 100)
	public String getCodigoEti() {
		return this.codigoEti;
	}

	public void setCodigoEti(String codigoEti) {
		this.codigoEti = codigoEti;
	}

	@Column(name = "CODIGO_HABIL", nullable = false, length = 1)
	public String getCodigoHabil() {
		return this.codigoHabil;
	}

	public void setCodigoHabil(String codigoHabil) {
		this.codigoHabil = codigoHabil;
	}

	@Column(name = "ENTERO", precision = 20, scale = 0)
	public Long getEntero() {
		return this.entero;
	}

	public void setEntero(Long entero) {
		this.entero = entero;
	}

	@Column(name = "ENTERO_ETI", length = 400)
	public String getEnteroEti() {
		return this.enteroEti;
	}

	public void setEnteroEti(String enteroEti) {
		this.enteroEti = enteroEti;
	}

	@Column(name = "ENTERO_HABIL", nullable = false, length = 1)
	public String getEnteroHabil() {
		return this.enteroHabil;
	}

	public void setEnteroHabil(String enteroHabil) {
		this.enteroHabil = enteroHabil;
	}

	@Column(name = "DECIMALES", precision = 22, scale = 5)
	public BigDecimal getDecimales() {
		return this.decimales;
	}

	public void setDecimales(BigDecimal decimales) {
		this.decimales = decimales;
	}

	@Column(name = "DECIMALES_ETI", length = 400)
	public String getDecimalesEti() {
		return this.decimalesEti;
	}

	public void setDecimalesEti(String decimalesEti) {
		this.decimalesEti = decimalesEti;
	}

	@Column(name = "DECIMALES_HABIL", nullable = false, length = 1)
	public String getDecimalesHabil() {
		return this.decimalesHabil;
	}

	public void setDecimalesHabil(String decimalesHabil) {
		this.decimalesHabil = decimalesHabil;
	}

	@Column(name = "TEXTO", length = 2000)
	public String getTexto() {
		return this.texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	@Column(name = "TEXTO_ETI", length = 400)
	public String getTextoEti() {
		return this.textoEti;
	}

	public void setTextoEti(String textoEti) {
		this.textoEti = textoEti;
	}

	@Column(name = "TEXTO_HABIL", nullable = false, length = 1)
	public String getTextoHabil() {
		return this.textoHabil;
	}

	public void setTextoHabil(String textoHabil) {
		this.textoHabil = textoHabil;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA", length = 7)
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Column(name = "FECHA_ETI", length = 400)
	public String getFechaEti() {
		return this.fechaEti;
	}

	public void setFechaEti(String fechaEti) {
		this.fechaEti = fechaEti;
	}

	@Column(name = "FECHA_HABIL", nullable = false, length = 1)
	public String getFechaHabil() {
		return this.fechaHabil;
	}

	public void setFechaHabil(String fechaHabil) {
		this.fechaHabil = fechaHabil;
	}

	@Column(name = "HORA", length = 20)
	public String getHora() {
		return this.hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	@Column(name = "HORA_ETI", length = 100)
	public String getHoraEti() {
		return this.horaEti;
	}

	public void setHoraEti(String horaEti) {
		this.horaEti = horaEti;
	}

	@Column(name = "HORA_HABIL", nullable = false, length = 1)
	public String getHoraHabil() {
		return this.horaHabil;
	}

	public void setHoraHabil(String horaHabil) {
		this.horaHabil = horaHabil;
	}

	@Column(name = "BOOLEANO", length = 1)
	public String getBooleano() {
		return this.booleano;
	}

	public void setBooleano(String booleano) {
		this.booleano = booleano;
	}

	@Column(name = "BOOLEANO_ETI", length = 400)
	public String getBooleanoEti() {
		return this.booleanoEti;
	}

	public void setBooleanoEti(String booleanoEti) {
		this.booleanoEti = booleanoEti;
	}

	@Column(name = "BOOLEANO_HABIL", nullable = false, length = 1)
	public String getBooleanoHabil() {
		return this.booleanoHabil;
	}

	public void setBooleanoHabil(String booleanoHabil) {
		this.booleanoHabil = booleanoHabil;
	}

	@Column(name = "FUNCION", length = 400)
	public String getFuncion() {
		return this.funcion;
	}

	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}

	@Column(name = "FUNCION_ETI", length = 100)
	public String getFuncionEti() {
		return this.funcionEti;
	}

	public void setFuncionEti(String funcionEti) {
		this.funcionEti = funcionEti;
	}

	@Column(name = "FUNCION_HABIL", nullable = false, length = 1)
	public String getFuncionHabil() {
		return this.funcionHabil;
	}

	public void setFuncionHabil(String funcionHabil) {
		this.funcionHabil = funcionHabil;
	}

	@Column(name = "FUNCION_MSG", length = 800)
	public String getFuncionMsg() {
		return this.funcionMsg;
	}

	public void setFuncionMsg(String funcionMsg) {
		this.funcionMsg = funcionMsg;
	}

	@Column(name = "PERMITE_HIJO", length = 1)
	public String getPermiteHijo() {
		return this.permiteHijo;
	}

	public void setPermiteHijo(String permiteHijo) {
		this.permiteHijo = permiteHijo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_MODIFICA", length = 7)
	public Date getFechaModifica() {
		return this.fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_CREA", nullable = false, length = 7)
	public Date getFechaCrea() {
		return this.fechaCrea;
	}

	public void setFechaCrea(Date fechaCrea) {
		this.fechaCrea = fechaCrea;
	}

	@Column(name = "USU_CREA", nullable = false, length = 28)
	public String getUsuCrea() {
		return this.usuCrea;
	}

	public void setUsuCrea(String usuCrea) {
		this.usuCrea = usuCrea;
	}

	@Column(name = "USU_MODIFICA", length = 28)
	public String getUsuModifica() {
		return this.usuModifica;
	}

	public void setUsuModifica(String usuModifica) {
		this.usuModifica = usuModifica;
	}

	@Column(name = "TIPO", precision = 10, scale = 0)
	public Long getParametroTipo() {
		return parametroTipo;
	}

	public void setParametroTipo(Long parametroTipo) {
		this.parametroTipo = parametroTipo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MultiTabla [id=");
		builder.append(id);
		builder.append(", ");
		if (parametro != null) {
			builder.append("parametro=");
			builder.append(parametro);
			builder.append(", ");
		}
		builder.append("tipo=");
		builder.append(tipo);
		builder.append(", ");
		if (nombre != null) {
			builder.append("nombre=");
			builder.append(nombre);
			builder.append(", ");
		}
		if (descripcion != null) {
			builder.append("descripcion=");
			builder.append(descripcion);
			builder.append(", ");
		}
		if (estado != null) {
			builder.append("estado=");
			builder.append(estado);
			builder.append(", ");
		}
		if (codigo != null) {
			builder.append("codigo=");
			builder.append(codigo);
			builder.append(", ");
		}
		if (codigoEti != null) {
			builder.append("codigoEti=");
			builder.append(codigoEti);
			builder.append(", ");
		}
		builder.append("codigoHabil=");
		builder.append(codigoHabil);
		builder.append(", ");
		if (entero != null) {
			builder.append("entero=");
			builder.append(entero);
			builder.append(", ");
		}
		if (enteroEti != null) {
			builder.append("enteroEti=");
			builder.append(enteroEti);
			builder.append(", ");
		}
		builder.append("enteroHabil=");
		builder.append(enteroHabil);
		builder.append(", ");
		if (decimales != null) {
			builder.append("decimales=");
			builder.append(decimales);
			builder.append(", ");
		}
		if (decimalesEti != null) {
			builder.append("decimalesEti=");
			builder.append(decimalesEti);
			builder.append(", ");
		}
		builder.append("decimalesHabil=");
		builder.append(decimalesHabil);
		builder.append(", ");
		if (texto != null) {
			builder.append("texto=");
			builder.append(texto);
			builder.append(", ");
		}
		if (textoEti != null) {
			builder.append("textoEti=");
			builder.append(textoEti);
			builder.append(", ");
		}
		builder.append("textoHabil=");
		builder.append(textoHabil);
		builder.append(", ");
		if (fecha != null) {
			builder.append("fecha=");
			builder.append(fecha);
			builder.append(", ");
		}
		if (fechaEti != null) {
			builder.append("fechaEti=");
			builder.append(fechaEti);
			builder.append(", ");
		}
		builder.append("fechaHabil=");
		builder.append(fechaHabil);
		builder.append(", ");
		if (hora != null) {
			builder.append("hora=");
			builder.append(hora);
			builder.append(", ");
		}
		if (horaEti != null) {
			builder.append("horaEti=");
			builder.append(horaEti);
			builder.append(", ");
		}
		builder.append("horaHabil=");
		builder.append(horaHabil);
		builder.append(", ");
		if (booleano != null) {
			builder.append("booleano=");
			builder.append(booleano);
			builder.append(", ");
		}
		if (booleanoEti != null) {
			builder.append("booleanoEti=");
			builder.append(booleanoEti);
			builder.append(", ");
		}
		builder.append("booleanoHabil=");
		builder.append(booleanoHabil);
		builder.append(", ");
		if (funcion != null) {
			builder.append("funcion=");
			builder.append(funcion);
			builder.append(", ");
		}
		if (funcionEti != null) {
			builder.append("funcionEti=");
			builder.append(funcionEti);
			builder.append(", ");
		}
		builder.append("funcionHabil=");
		builder.append(funcionHabil);
		builder.append(", ");
		if (funcionMsg != null) {
			builder.append("funcionMsg=");
			builder.append(funcionMsg);
			builder.append(", ");
		}
		if (permiteHijo != null) {
			builder.append("permiteHijo=");
			builder.append(permiteHijo);
			builder.append(", ");
		}
		if (fechaModifica != null) {
			builder.append("fechaModifica=");
			builder.append(fechaModifica);
			builder.append(", ");
		}
		if (fechaCrea != null) {
			builder.append("fechaCrea=");
			builder.append(fechaCrea);
			builder.append(", ");
		}
		if (usuCrea != null) {
			builder.append("usuCrea=");
			builder.append(usuCrea);
			builder.append(", ");
		}
		if (usuModifica != null) {
			builder.append("usuModifica=");
			builder.append(usuModifica);
			builder.append(", ");
		}
		if (parametroTipo != null) {
			builder.append("parametroTipo=");
			builder.append(parametroTipo);
		}
		builder.append("]");
		return builder.toString();
	}
	
}
