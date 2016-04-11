package com.ibm.bbva.ctacte.business.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.as.core.util.CommonUtils;
import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.DatosCorreo;
import com.ibm.bbva.ctacte.bean.DatosCorreoXPerfil;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.EstadoTarea;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.Historial;
import com.ibm.bbva.ctacte.bean.Perfil;
import com.ibm.bbva.ctacte.bean.PlazoSubsanacion;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.business.ExpedienteBusiness;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.dao.ClienteDAO;
import com.ibm.bbva.ctacte.dao.DatosCorreoDAO;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.EstadoExpedienteDAO;
import com.ibm.bbva.ctacte.dao.EstadoTareaDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.HistorialDAO;
import com.ibm.bbva.ctacte.dao.PlazoSubsanacionDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;
import com.ibm.bbva.ctacte.util.ParametrosSistema;

/**
 * Session Bean implementation class ExpedienteBusinessImpl
 */
@Stateless
@Local(ExpedienteBusiness.class)
public class ExpedienteBusinessImpl implements ExpedienteBusiness {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExpedienteBusinessImpl.class);
	
	@EJB
	private ClienteDAO clienteDAO;
	@EJB
	private EstadoTareaDAO estadoDAO;
	@EJB
	private TareaDAO tareaDAO;
	@EJB
	private EstadoExpedienteDAO estadoExpedienteDAO;
	@EJB
	private ExpedienteDAO expedienteDAO;
	@EJB
	private PlazoSubsanacionDAO plazoSubsanacionDAO;
	@EJB
	private DatosCorreoDAO datosCorreoDAO;
	@EJB
	private HistorialDAO historialDAO;
	@EJB
	private EmpleadoDAO empleadoDAO;

    /**
     * Default constructor. 
     */
    public ExpedienteBusinessImpl() {
    }

	@Override
	public void guardarExpediente(Expediente expediente, int idEstadoExp,
			int idTarea, int idEstadoTarea, String accion, Date fechaRegistro) {
		LOG.info("guardarExpediente (Expediente expediente, int idEstadoExp, int idTarea, int idEstadoTarea, String accion)");
		Cliente cliente = expediente.getCliente();
		if (cliente.getId()==null) {
			clienteDAO.save(cliente);
		} else {
			clienteDAO.update(cliente);
		}
		if (expediente.getId()==null) {
			expediente.setFechaRegistro(fechaRegistro);
		}
		
		expediente.setEstado(estadoExpedienteDAO.load(idEstadoExp));
		
		ExpedienteTarea tareaExpediente = new ExpedienteTarea(); 
		EstadoTarea estado = estadoDAO.load(idEstadoTarea);
		Tarea tarea = tareaDAO.load(idTarea);
		tareaExpediente.setExpediente(expediente);
		tareaExpediente.setEstadoTarea(estado);
		tareaExpediente.setTarea(tarea);
		
		HashSet<ExpedienteTarea> expTareaHst = new HashSet<ExpedienteTarea>();
		expTareaHst.add(tareaExpediente);
		
		expediente.setExpedienteTareas(expTareaHst);
		
		expediente.setAccion(accion);
		if (expediente.getId()==null) {
			expedienteDAO.save(expediente);
		} else {
			expedienteDAO.update(expediente);
		}
	}

	@Override
	public int dentroPlazoSubsanacion(Date dtFechaRegistroExpediente,
			Date dtFechaUltimoBastanteo) {
		int intDentroSubsanacion = ConstantesBusiness.FUERA_PLAZO_SUBSANACION;
		try {
			LOG.info("dentroPlazoSubsanacion - fechaRegistroExpediente: "+(dtFechaRegistroExpediente != null ? dtFechaRegistroExpediente : "null"));
			LOG.info("dentroPlazoSubsanacion - dtFechaUltimoBastanteo: "+(dtFechaUltimoBastanteo != null ? dtFechaUltimoBastanteo : "null"));
			long lngDias=0;
			long lngPlazoDias = 0;
			long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
			lngDias = (dtFechaRegistroExpediente.getTime() - dtFechaUltimoBastanteo.getTime()) / MILLSECS_PER_DAY;
			LOG.info("dentroPlazoSubsanacion - lngDias: "+lngDias);
			PlazoSubsanacion plazoSubsanacion = new PlazoSubsanacion();
			List<PlazoSubsanacion> listPlazoSubsanacion = plazoSubsanacionDAO.findAll();
			if (listPlazoSubsanacion.size()>0){
				plazoSubsanacion = listPlazoSubsanacion.get(0);
				lngPlazoDias = plazoSubsanacion.getPlazoDias();
				if (lngPlazoDias>=lngDias){
					intDentroSubsanacion = ConstantesBusiness.DENTRO_PLAZO_SUBSANACION;
				}
			}
			return intDentroSubsanacion;
		} catch (Exception ex) {
			LOG.error("", ex);
			return intDentroSubsanacion;
		}
	}
	
	@Override
	public boolean enviarCorreo(String idExpediente, String idTareaAnterior, String idTareaActual, String codUsuarioAnterior, String codUsuarioActual, String nombreAccion) {
		LOG.debug("enviarCorreo");
		LOG.debug("idExpediente: "+idExpediente);
		LOG.debug("idTareaAnterior: "+idTareaAnterior);
		LOG.debug("idTareaActual: "+idTareaActual);
		LOG.debug("codUsuarioAnterior: "+codUsuarioAnterior);
		LOG.debug("codUsuarioActual: "+codUsuarioActual);
		LOG.debug("nombreAccion: "+nombreAccion);
		
		boolean result = false;
		try {
			if (idTareaAnterior == null || idTareaAnterior.trim().equals("undefined") || idTareaAnterior.trim().equals("")) {
				idTareaAnterior = String.valueOf(ConstantesBusiness.ID_TAREA_REGISTRAR_EXPEDIENTE);
			}
			DatosCorreo datosCorreo = datosCorreoDAO.buscarPorIdTareaYNombreAccion(Integer.parseInt(idTareaAnterior), nombreAccion);
			
			if (datosCorreo != null && datosCorreo.getFlagActivo().equals("1")) {
				Expediente expediente = expedienteDAO.load(Integer.parseInt(idExpediente));
				List<Historial> lstHistorial = historialDAO.findByExpdiente(Integer.parseInt(idExpediente));
				List<Empleado> lstDestinatarios = new ArrayList<Empleado>();
				Empleado empleadoAnterior = null;
				if (codUsuarioAnterior != null && !codUsuarioAnterior.trim().equals("undefined") && !codUsuarioAnterior.trim().equals("")) {
					empleadoAnterior = empleadoDAO.findByCodigo(codUsuarioAnterior);
				}
				Empleado empleadoActual = null;
				if (codUsuarioActual != null && !codUsuarioActual.trim().equals("undefined") && !codUsuarioActual.trim().equals("")) {
					empleadoActual = empleadoDAO.findByCodigo(codUsuarioActual);
				}
				Tarea tareaAnterior = null;
				if (idTareaAnterior != null && !idTareaAnterior.trim().equals("undefined") && !idTareaAnterior.trim().equals("")) {
					tareaAnterior = tareaDAO.findById(Integer.parseInt(idTareaAnterior));
				}
				Tarea tareaActual = null;
				if (idTareaActual != null && !idTareaActual.trim().equals("undefined") && !idTareaActual.trim().equals("")) {
					tareaActual = tareaDAO.findById(Integer.parseInt(idTareaActual));
				}
				
				for (DatosCorreoXPerfil conf : datosCorreo.getDatosCorreoXPerfil()) {
					Perfil perfil = conf.getPerfil();
					if (perfil.getId().intValue() == Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_JEFE_BASTANTEO)) {
						List<Empleado> lstDestTemp = empleadoDAO.getEmpleadosPorPerfil(perfil.getId());
						lstDestinatarios.addAll(lstDestTemp);
					} else if (perfil.getId().intValue() == Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_SUPERVISOR_FIRMA)) {
						boolean enviar = false;
						if (idTareaActual != null && Integer.parseInt(idTareaActual) == ConstantesBusiness.ID_TAREA_VERIFICAR_CALIDAD_FIRMAS) {
							enviar = true;
						} else if (lstHistorial != null && lstHistorial.size() > 0) {
							for (Historial historial : lstHistorial) {
								if (historial.getTarea().getId().intValue() == ConstantesBusiness.ID_TAREA_VERIFICAR_CALIDAD_FIRMAS) {
									enviar = true;
									break;
								}
							}
						}
						if (enviar) {
							List<Empleado> lstDestTemp = empleadoDAO.getEmpleadosPorPerfil(perfil.getId());
							lstDestinatarios.addAll(lstDestTemp);
						}
					} else if (perfil.getId().intValue() == Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_SUPERVISOR_ABOGADO_BASTANTEO)) {
						// obtengo el estudio de abogado del último abogado que atendió el expediente
						Integer idEstudioAbog = null;
						if (empleadoActual != null && "1".equals(empleadoActual.getFlagAbogado()) && empleadoActual.getEstudio() != null) {
							idEstudioAbog = empleadoActual.getEstudio().getId();
						} else if (empleadoAnterior != null && "1".equals(empleadoAnterior.getFlagAbogado()) && empleadoAnterior.getEstudio() != null) {
							idEstudioAbog = empleadoAnterior.getEstudio().getId();
						} else if (lstHistorial != null && lstHistorial.size() > 0) {
							for (int i=lstHistorial.size()-1; i >= 0; i--) {
								Empleado empleadoHist = lstHistorial.get(i).getEmpleado();
								if ("1".equals(empleadoHist.getFlagAbogado()) && empleadoHist.getEstudio() != null) {
									idEstudioAbog = empleadoHist.getEstudio().getId();
									break;
								}
							}
						}
						if (idEstudioAbog != null && idEstudioAbog.intValue() > 0) {
							List<Integer> lstIdPerfil = new ArrayList<Integer>();
							lstIdPerfil.add(perfil.getId());
							List<Empleado> lstDestTemp = empleadoDAO.getEmpleadosPorPerfilesYEstudio(lstIdPerfil, idEstudioAbog);
							lstDestinatarios.addAll(lstDestTemp);
						}
					} else if (perfil.getId().intValue() == Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_GESTOR)
							|| perfil.getId().intValue() == Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_MESA_FIRMAS)
							|| perfil.getId().intValue() == Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_MESA_DOCUMENTOS)
							|| perfil.getId().intValue() == Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_ABOGADO_BASTANTEO)
							|| perfil.getId().intValue() == Integer.parseInt(ConstantesBusiness.CODIGO_PERFIL_ABOGADO_REVISION)) {
						if (empleadoActual != null && empleadoActual.getPerfil().getId().equals(perfil.getId())) {
							lstDestinatarios.add(empleadoActual);
						}
						if (empleadoAnterior != null && empleadoAnterior.getPerfil().getId().equals(perfil.getId())) {
							lstDestinatarios.add(empleadoAnterior);
						}
						if (lstHistorial != null && lstHistorial.size() > 0) {
							for (int i=lstHistorial.size()-1; i >= 0; i--) {
								Empleado empleadoHist = lstHistorial.get(i).getEmpleado();
								if (empleadoHist.getPerfil().getId().equals(perfil.getId()) && empleadoHist.getFlagActivo().equals("1")) {
									lstDestinatarios.add(empleadoHist);
									break;
								}
							}
						}
					} else {
						LOG.warn("El perfil {} no se toma en cuenta para el envío de correo.", perfil.getDescripcion());
					}
				}
				Set<String> setDestinatarios = new HashSet<String>();
				for (Empleado empleado : lstDestinatarios) {
					if (empleado.getCorreo() != null) {
						if (CommonUtils.isEmail(empleado.getCorreo().trim())) {
							setDestinatarios.add(empleado.getCorreo());
						}
					}
				}
				if (!setDestinatarios.isEmpty()) {
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					String asunto = datosCorreo.getAsunto();
					String cuerpo = new String(datosCorreo.getCuerpo());
					
					if (asunto.indexOf("NRO_EXPEDIENTE") > -1 || cuerpo.indexOf("NRO_EXPEDIENTE") > -1) {
						String nroExpediente = "";
						if (expediente.getId() != null) {
							nroExpediente = String.valueOf(expediente.getId());
						}
						asunto = asunto.replaceAll("NRO_EXPEDIENTE", nroExpediente);
						cuerpo = cuerpo.replaceAll("NRO_EXPEDIENTE", nroExpediente);
					}
					if (asunto.indexOf("CODIGO_CENTRAL") > -1 || cuerpo.indexOf("CODIGO_CENTRAL") > -1) {
						String codigoCentral = "";
						if (expediente.getCliente() != null && expediente.getCliente().getCodigoCentral() != null) {
							codigoCentral = expediente.getCliente().getCodigoCentral();
						}
						asunto = asunto.replaceAll("CODIGO_CENTRAL", codigoCentral);
						cuerpo = cuerpo.replaceAll("CODIGO_CENTRAL", codigoCentral);
					}
					if (asunto.indexOf("FECHA_CONSTITUCION") > -1 || cuerpo.indexOf("FECHA_CONSTITUCION") > -1) {
						String strFechaConstitucion = "";
						if (expediente.getCliente() != null && expediente.getCliente().getFechaConstitucion() != null) {
							strFechaConstitucion = df.format(expediente.getCliente().getFechaConstitucion());
						}
						asunto = asunto.replaceAll("FECHA_CONSTITUCION", strFechaConstitucion);
						cuerpo = cuerpo.replaceAll("FECHA_CONSTITUCION", strFechaConstitucion);
					}
					if (asunto.indexOf("TIPO_DOI") > -1 || cuerpo.indexOf("TIPO_DOI") > -1) {
						String tipoDoi = "";
						if (expediente.getCliente() != null && expediente.getCliente().getTipoDoiDes() != null) {
							tipoDoi = expediente.getCliente().getTipoDoiDes();
						}
						asunto = asunto.replaceAll("TIPO_DOI", tipoDoi);
						cuerpo = cuerpo.replaceAll("TIPO_DOI", tipoDoi);
					}
					if (asunto.indexOf("NUMERO_DOI") > -1 || cuerpo.indexOf("NUMERO_DOI") > -1) {
						String numeroDoi = "";
						if (expediente.getCliente() != null && expediente.getCliente().getNumeroDoi() != null) {
							numeroDoi = expediente.getCliente().getNumeroDoi();
						}
						asunto = asunto.replaceAll("NUMERO_DOI", numeroDoi);
						cuerpo = cuerpo.replaceAll("NUMERO_DOI", numeroDoi);
					}
					if (asunto.indexOf("RAZON_SOCIAL") > -1 || cuerpo.indexOf("RAZON_SOCIAL") > -1) {
						String razonSocial = "";
						if (expediente.getCliente() != null && expediente.getCliente().getRazonSocial() != null) {
							razonSocial = expediente.getCliente().getRazonSocial();
						}
						asunto = asunto.replaceAll("RAZON_SOCIAL", razonSocial);
						cuerpo = cuerpo.replaceAll("RAZON_SOCIAL", razonSocial);
					}
					if (asunto.indexOf("SUBPRODUCTO") > -1 || cuerpo.indexOf("SUBPRODUCTO") > -1) {
						String subproducto = "";
						if (expediente.getSubproducto() != null) {
							subproducto = expediente.getSubproducto();
						}
						asunto = asunto.replaceAll("SUBPRODUCTO", subproducto);
						cuerpo = cuerpo.replaceAll("SUBPRODUCTO", subproducto);
					}
					if (asunto.indexOf("PRODUCTO") > -1 || cuerpo.indexOf("PRODUCTO") > -1) {
						String producto = "";
						if (expediente.getProducto() != null && expediente.getProducto().getDescripcion() != null) {
							producto = expediente.getProducto().getDescripcion();
						}
						asunto = asunto.replaceAll("PRODUCTO", producto);
						cuerpo = cuerpo.replaceAll("PRODUCTO", producto);
					}
					if (asunto.indexOf("CUENTA_COBRO_COMISION") > -1 || cuerpo.indexOf("CUENTA_COBRO_COMISION") > -1) {
						String cuentaCobroComision = "";
						if (expediente.getCuentaCobroComision() != null) {
							cuentaCobroComision = expediente.getCuentaCobroComision();
						}
						asunto = asunto.replaceAll("CUENTA_COBRO_COMISION", cuentaCobroComision);
						cuerpo = cuerpo.replaceAll("CUENTA_COBRO_COMISION", cuentaCobroComision);
					}
					if (asunto.indexOf("NUMERO_CUENTA") > -1 || cuerpo.indexOf("NUMERO_CUENTA") > -1) {
						String numeroCuentaCobro = "";
						if (expediente.getNumeroCuentaCobro() != null) {
							numeroCuentaCobro = expediente.getNumeroCuentaCobro();
						}
						asunto = asunto.replaceAll("NUMERO_CUENTA", numeroCuentaCobro);
						cuerpo = cuerpo.replaceAll("NUMERO_CUENTA", numeroCuentaCobro);
					}
					if (asunto.indexOf("OPERACION") > -1 || cuerpo.indexOf("OPERACION") > -1) {
						String operacion = "";
						if (expediente.getOperacion() != null && expediente.getOperacion().getDescripcion() != null) {
							operacion = expediente.getOperacion().getDescripcion();
						}
						asunto = asunto.replaceAll("OPERACION", operacion);
						cuerpo = cuerpo.replaceAll("OPERACION", operacion);
					}
					if (asunto.indexOf("TIPO_PJ") > -1 || cuerpo.indexOf("TIPO_PJ") > -1) {
						String tipoPj = "";
						if (expediente.getDesTipoPj() != null) {
							tipoPj = expediente.getDesTipoPj();
						}
						asunto = asunto.replaceAll("TIPO_PJ", tipoPj);
						cuerpo = cuerpo.replaceAll("TIPO_PJ", tipoPj);
					}
					if (asunto.indexOf("COD_OFICINA") > -1 || cuerpo.indexOf("COD_OFICINA") > -1) {
						String codOficina = "";
						if (expediente.getOficina() != null && expediente.getOficina().getCodigo() != null) {
							codOficina = expediente.getOficina().getCodigo();
						}
						asunto = asunto.replaceAll("COD_OFICINA", codOficina);
						cuerpo = cuerpo.replaceAll("COD_OFICINA", codOficina);
					}
					if (asunto.indexOf("NOMBRE_OFICINA") > -1 || cuerpo.indexOf("NOMBRE_OFICINA") > -1) {
						String nombreOficina = "";
						if (expediente.getOficina() != null && expediente.getOficina().getDescripcion() != null) {
							nombreOficina = expediente.getOficina().getDescripcion();
						}
						asunto = asunto.replaceAll("NOMBRE_OFICINA", nombreOficina);
						cuerpo = cuerpo.replaceAll("NOMBRE_OFICINA", nombreOficina);
					}
					if (asunto.indexOf("NOMBRE_TAREA_ORIGEN") > -1 || cuerpo.indexOf("NOMBRE_TAREA_ORIGEN") > -1) {
						String nombreTareaOrigen = "";
						if (tareaAnterior != null && tareaAnterior.getDescripcion() != null) {
							nombreTareaOrigen = tareaAnterior.getDescripcion();
						}
						asunto = asunto.replaceAll("NOMBRE_TAREA_ORIGEN", nombreTareaOrigen);
						cuerpo = cuerpo.replaceAll("NOMBRE_TAREA_ORIGEN", nombreTareaOrigen);
					}
					if (asunto.indexOf("NOMBRE_TAREA_DESTINO") > -1 || cuerpo.indexOf("NOMBRE_TAREA_DESTINO") > -1) {
						String nombreTareaDestino = "";
						if (tareaActual != null && tareaActual.getDescripcion() != null) {
							nombreTareaDestino = tareaActual.getDescripcion();
						}
						asunto = asunto.replaceAll("NOMBRE_TAREA_DESTINO", nombreTareaDestino);
						cuerpo = cuerpo.replaceAll("NOMBRE_TAREA_DESTINO", nombreTareaDestino);
					}
					if (asunto.indexOf("PERFIL_USUARIO_ORIGEN") > -1 || cuerpo.indexOf("PERFIL_USUARIO_ORIGEN") > -1
							|| asunto.indexOf("USUARIO_ORIGEN") > -1 || cuerpo.indexOf("USUARIO_ORIGEN") > -1) {
						String perfilUsuarioOrigen = "";
						String usuarioOrigen = "";
						if (empleadoAnterior != null) {
							perfilUsuarioOrigen = empleadoAnterior.getPerfil().getDescripcion();
							usuarioOrigen = empleadoAnterior.getCodigo();
						}
						asunto = asunto.replaceAll("PERFIL_USUARIO_ORIGEN", perfilUsuarioOrigen);
						cuerpo = cuerpo.replaceAll("PERFIL_USUARIO_ORIGEN", perfilUsuarioOrigen);
						asunto = asunto.replaceAll("USUARIO_ORIGEN", usuarioOrigen);
						cuerpo = cuerpo.replaceAll("USUARIO_ORIGEN", usuarioOrigen);
					}
					if (asunto.indexOf("PERFIL_USUARIO_DESTINO") > -1 || cuerpo.indexOf("PERFIL_USUARIO_DESTINO") > -1
							|| asunto.indexOf("USUARIO_DESTINO") > -1 || cuerpo.indexOf("USUARIO_DESTINO") > -1) {
						String perfilUsuarioDestino = "";
						String usuarioDestino = "";
						if (empleadoActual != null) {
							perfilUsuarioDestino = empleadoActual.getPerfil().getDescripcion();
							usuarioDestino = empleadoActual.getCodigo();
						}
						asunto = asunto.replaceAll("PERFIL_USUARIO_DESTINO", perfilUsuarioDestino);
						cuerpo = cuerpo.replaceAll("PERFIL_USUARIO_DESTINO", perfilUsuarioDestino);
						asunto = asunto.replaceAll("USUARIO_DESTINO", usuarioDestino);
						cuerpo = cuerpo.replaceAll("USUARIO_DESTINO", usuarioDestino);
					}
					if (asunto.indexOf("RESULTADO_REVISION_MD") > -1 || cuerpo.indexOf("RESULTADO_REVISION_MD") > -1) {
						String observacion = "";
						if (lstHistorial != null && lstHistorial.size() > 0) {
							for (int i=lstHistorial.size()-1; i >= 0; i--) {
								Historial historial = lstHistorial.get(i);
								if (historial.getTarea().getId().intValue() == ConstantesBusiness.ID_TAREA_VERIFICAR_CALIDAD_DOCUMENTOS) {
									if (historial.getObservaciones() != null) {
										observacion = historial.getObservaciones();
									}
									break;
								}
							}
						}
						asunto = asunto.replaceAll("RESULTADO_REVISION_MD", observacion);
						cuerpo = cuerpo.replaceAll("RESULTADO_REVISION_MD", observacion);
					}
					if (asunto.indexOf("OBSERVACION_MF") > -1 || cuerpo.indexOf("OBSERVACION_MF") > -1) {
						String observacion = "";
						if (lstHistorial != null && lstHistorial.size() > 0) {
							for (int i=lstHistorial.size()-1; i >= 0; i--) {
								Historial historial = lstHistorial.get(i);
								if (historial.getTarea().getId().intValue() == ConstantesBusiness.ID_TAREA_VERIFICAR_CALIDAD_FIRMAS) {
									if (historial.getObservaciones() != null) {
										observacion = historial.getObservaciones();
									}
									break;
								}
							}
						}
						asunto = asunto.replaceAll("OBSERVACION_MF", observacion);
						cuerpo = cuerpo.replaceAll("OBSERVACION_MF", observacion);
					}
					if (asunto.indexOf("RESULTADO_VRB") > -1 || cuerpo.indexOf("RESULTADO_VRB") > -1
							|| asunto.indexOf("COMENTARIOS_VRB") > -1 || cuerpo.indexOf("COMENTARIOS_VRB") > -1
							|| asunto.indexOf("DICTAMEN_VRB") > -1 || cuerpo.indexOf("DICTAMEN_VRB") > -1
							|| asunto.indexOf("INSTRUCCIONES_VRB") > -1 || cuerpo.indexOf("INSTRUCCIONES_VRB") > -1) {
						String resultado = "";
						String observacion = "";
						String strDictamen = "";
						String strInstrucciones = "";
						if (lstHistorial != null && lstHistorial.size() > 0) {
							for (int i=lstHistorial.size()-1; i >= 0; i--) {
								Historial historial = lstHistorial.get(i);
								if (historial.getTarea().getId().intValue() == ConstantesBusiness.ID_TAREA_VERIFICAR_REALIZAR_BASTANTEO) {
									if (historial.getResultado() != null) {
										resultado = historial.getResultado();
									}
									if (historial.getObservaciones() != null) {
										observacion = historial.getObservaciones();
									}
									if (historial.getDictamenBastanteo() != null) {
										strDictamen = new String(historial.getDictamenBastanteo());	
									}
									if (historial.getInstruccionesBastanteo() != null) {
										strInstrucciones = new String(historial.getInstruccionesBastanteo());	
									}
									break;
								}
							}
						}
						asunto = asunto.replaceAll("RESULTADO_VRB", resultado);
						cuerpo = cuerpo.replaceAll("RESULTADO_VRB", resultado);
						asunto = asunto.replaceAll("COMENTARIOS_VRB", observacion);
						cuerpo = cuerpo.replaceAll("COMENTARIOS_VRB", observacion);
						asunto = asunto.replaceAll("DICTAMEN_VRB", strDictamen);
						cuerpo = cuerpo.replaceAll("DICTAMEN_VRB", strDictamen);
						asunto = asunto.replaceAll("INSTRUCCIONES_VRB", strInstrucciones);
						cuerpo = cuerpo.replaceAll("INSTRUCCIONES_VRB", strInstrucciones);
					}
					if (asunto.indexOf("RESULTADO_RER") > -1 || cuerpo.indexOf("RESULTADO_RER") > -1) {
						String resultado = "";
						if (lstHistorial != null && lstHistorial.size() > 0) {
							for (int i=lstHistorial.size()-1; i >= 0; i--) {
								Historial historial = lstHistorial.get(i);
								if (historial.getTarea().getId().intValue() == ConstantesBusiness.ID_TAREA_REVISAR_EJECUTAR_REVOCATORIA) {
									if (historial.getResultado() != null) {
										resultado = historial.getResultado();
									}
									break;
								}
							}
						}
						asunto = asunto.replaceAll("RESULTADO_RER", resultado);
						cuerpo = cuerpo.replaceAll("RESULTADO_RER", resultado);
					}
					if (asunto.indexOf("RESULTADO_RAB") > -1 || cuerpo.indexOf("RESULTADO_RAB") > -1) {
						String resultado = "";
						if (lstHistorial != null && lstHistorial.size() > 0) {
							for (int i=lstHistorial.size()-1; i >= 0; i--) {
								Historial historial = lstHistorial.get(i);
								if (historial.getTarea().getId().intValue() == ConstantesBusiness.ID_TAREA_REVISAR_APROBAR_BASTANTEO) {
									if (historial.getResultado() != null) {
										resultado = historial.getResultado();
									}
									break;
								}
							}
						}
						asunto = asunto.replaceAll("RESULTADO_RAB", resultado);
						cuerpo = cuerpo.replaceAll("RESULTADO_RAB", resultado);
					}
					if (asunto.indexOf("RESULTADO_AR") > -1 || cuerpo.indexOf("RESULTADO_AR") > -1) {
						String resultado = "";
						if (lstHistorial != null && lstHistorial.size() > 0) {
							for (int i=lstHistorial.size()-1; i >= 0; i--) {
								Historial historial = lstHistorial.get(i);
								if (historial.getTarea().getId().intValue() == ConstantesBusiness.ID_TAREA_APROBAR_REVOCATORIA) {
									if (historial.getResultado() != null) {
										resultado = historial.getResultado();
									}
									break;
								}
							}
						}
						asunto = asunto.replaceAll("RESULTADO_AR", resultado);
						cuerpo = cuerpo.replaceAll("RESULTADO_AR", resultado);
					}
					if (asunto.indexOf("IND_ADI_RE") > -1 || cuerpo.indexOf("IND_ADI_RE") > -1) {
						String observacion = "";
						if (lstHistorial != null && lstHistorial.size() > 0) {
							for (int i=lstHistorial.size()-1; i >= 0; i--) {
								Historial historial = lstHistorial.get(i);
								if (historial.getTarea().getId().intValue() == ConstantesBusiness.ID_TAREA_REGISTRAR_EXPEDIENTE) {
									if (historial.getObservaciones() != null) {
										observacion = historial.getObservaciones();
									}
									break;
								}
							}
						}
						asunto = asunto.replaceAll("IND_ADI_RE", observacion);
						cuerpo = cuerpo.replaceAll("IND_ADI_RE", observacion);
					}
					if (asunto.indexOf("IND_ADI_SF") > -1 || cuerpo.indexOf("IND_ADI_SF") > -1) {
						String observacion = "";
						if (lstHistorial != null && lstHistorial.size() > 0) {
							for (int i=lstHistorial.size()-1; i >= 0; i--) {
								Historial historial = lstHistorial.get(i);
								if (historial.getTarea().getId().intValue() == ConstantesBusiness.ID_TAREA_SUBSANAR_FIRMA) {
									if (historial.getObservaciones() != null) {
										observacion = historial.getObservaciones();
									}
									break;
								}
							}
						}
						asunto = asunto.replaceAll("IND_ADI_SF", observacion);
						cuerpo = cuerpo.replaceAll("IND_ADI_SF", observacion);
					}
					if (asunto.indexOf("IND_ADI_SD") > -1 || cuerpo.indexOf("IND_ADI_SD") > -1) {
						String observacion = "";
						if (lstHistorial != null && lstHistorial.size() > 0) {
							for (int i=lstHistorial.size()-1; i >= 0; i--) {
								Historial historial = lstHistorial.get(i);
								if (historial.getTarea().getId().intValue() == ConstantesBusiness.ID_TAREA_SUBSANAR_DOCUMENTO) {
									if (historial.getObservaciones() != null) {
										observacion = historial.getObservaciones();
									}
									break;
								}
							}
						}
						asunto = asunto.replaceAll("IND_ADI_SD", observacion);
						cuerpo = cuerpo.replaceAll("IND_ADI_SD", observacion);
					}
					
					// Envío el correo
					ParametrosSistema parametros = ParametrosSistema.getInstance();
					Properties propertiesWAS = parametros.getProperties(ParametrosSistema.CONF);
					String hostServidorCorreo = propertiesWAS.getProperty(ConstantesParametros.HOST_SERVIDOR_CORREO);;
					String puertoServidorCorreo = propertiesWAS.getProperty(ConstantesParametros.PUERTO_SERVIDOR_CORREO);
					String remitente = propertiesWAS.getProperty(ConstantesParametros.EMAIL_REMITENTE);
					
					// Get the session object
					LOG.debug("Host Servidor Correo: "+hostServidorCorreo);
					LOG.debug("Puerto Servidor Correo: "+puertoServidorCorreo);
					LOG.debug("Remitente: "+remitente);
					//Properties properties = System.getProperties();
					Properties properties = new Properties();
					properties.setProperty("mail.smtp.host", hostServidorCorreo);
					properties.setProperty("mail.smtp.port", puertoServidorCorreo);
					Session session = Session.getDefaultInstance(properties);
			
					// compose the message
					MimeMessage message = new MimeMessage(session);
					message.setFrom(new InternetAddress(remitente));
					List<InternetAddress> listAddresses = new ArrayList<InternetAddress>();
					Iterator<String> iter = setDestinatarios.iterator();
					while (iter.hasNext()) {
						String direccion = iter.next();
						listAddresses.add(new InternetAddress(direccion));
					}
					LOG.debug("Destinatarios: "+Arrays.toString(setDestinatarios.toArray(new String[0])));
					message.addRecipients(Message.RecipientType.TO, listAddresses.toArray(new InternetAddress[0]));
					message.setSubject(asunto);
					LOG.debug("Asunto del mensaje: "+asunto);
					message.setContent(cuerpo, "text/html; charset=utf-8");
					LOG.debug("Cuerpo del mensaje: "+cuerpo);

					// Send message
					Transport.send(message);
					LOG.info("Se envió el correo satisfactoriamente.");
					result = true;
				} else {
					LOG.warn("No se enviará el correo porque no se encontraron destinatarios.");
				}
			} else {
				LOG.warn("No existe configuración de correo activa para esta tarea y acción.");
			}
			
			return result;
		} catch(Exception e) {
			LOG.error("Error al enviar el correo.", e);
			return false;
		}
	}

}
