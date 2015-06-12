package com.ibm.bbva.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.Cliente;
import pe.ibm.bean.Consulta;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bean.ExpedienteTCWPSWeb;
import pe.ibm.bean.Producto;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.PanelDocumentosMB;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.entities.TipoCliente;
import com.ibm.bbva.entities.TipoDoi;
import com.ibm.bbva.entities.TipoOferta;
import com.ibm.bbva.entities.TipoScoring;
import com.ibm.bbva.session.ClienteNaturalBeanLocal;
import com.ibm.bbva.session.EstadoBeanLocal;
import com.ibm.bbva.session.TipoDoiBeanLocal;
import com.ibm.bbva.session.TipoScoringBeanLocal;

public class AyudaExpedienteTC extends AbstractMBean{

	private static final Logger LOG = LoggerFactory.getLogger(AyudaExpedienteTC.class);

	private EstadoBeanLocal estadoBeanLocalBean;
	private TipoDoiBeanLocal tipoDoiBeanLocalBean;
	private ClienteNaturalBeanLocal clienteNaturalBean;
	private TipoScoringBeanLocal tipoScoringBean;
	
	public AyudaExpedienteTC()  {
		super();
		try {
			estadoBeanLocalBean=(EstadoBeanLocal) new InitialContext()
					.lookup("ejblocal:com.ibm.bbva.session.EstadoBeanLocal");
			tipoDoiBeanLocalBean=(TipoDoiBeanLocal) new InitialContext()
					.lookup("ejblocal:com.ibm.bbva.session.TipoDoiBeanLocal");
			clienteNaturalBean = (ClienteNaturalBeanLocal) new InitialContext()
					.lookup("ejblocal:com.ibm.bbva.session.ClienteNaturalBeanLocal");
			tipoScoringBean = (TipoScoringBeanLocal) new InitialContext()
					.lookup("ejblocal:com.ibm.bbva.session.TipoScoringBeanLocal");			
		} catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}	
	}
	
	/**
	 * Verifica si se han subido nuevos
	 * documentos para el expediente en curso, en base a eso
	 * se estable un Flag resultando un expediente modificado.
	 * 
	 * @param exp - Expediente a transformar
	 * @param panelDocumento - Panel de documentos (ManagedBean)
	 * @return
	 */
	public ExpedienteTCWPS setFlagEnvioContent(ExpedienteTCWPS expedienteTCWPS, PanelDocumentosMB panelDocumentos){
				
		if(panelDocumentos != null){
			if(panelDocumentos.isExistenNuevosArchivos()){
				expedienteTCWPS.setFlagEnvioContent("1");
			}else{
				expedienteTCWPS.setFlagEnvioContent("0");
			}
		}
		
		return expedienteTCWPS;
				
	}
	
	/**
	 * Existen valores que se deben asignar segun el caso de uso:<BR>
	 * codigoRVGL: CU 14 <BR>
	 * devueltoPor: CU 15 <BR>
	 * modificacionScoring: CU 16, 21, 24 <BR>
	 * scoringAprobado: CU 3 <BR>
	 * verificacionDomiciliaria: CU 4 <BR>
	 */
	public ExpedienteTCWPS copiarDatos (AbstractMBean abstractMBean, 
			ClienteNatural clienteNatural, String accion, Integer estado){
		Expediente expediente = (Expediente) abstractMBean.getObjectSession(Constantes.EXPEDIENTE_SESION);
		return copiarDatos(abstractMBean, expediente, clienteNatural, accion, estado);
	}

	/**
	 * Existen valores que se deben asignar segun el caso de uso:<BR>
	 * codigoRVGL: CU 14 <BR>
	 * devueltoPor: CU 15 estado anterior<BR>
	 * modificacionScoring: CU 16, 21, 24 <BR>
	 * scoringAprobado: CU 3 <BR>
	 * verificacionDomiciliaria: CU 4 <BR>
	 */
	public ExpedienteTCWPS copiarDatos (AbstractMBean abstractMBean, String accion, Integer estado) {
		Expediente expediente = (Expediente) abstractMBean.getObjectSession(Constantes.EXPEDIENTE_SESION);
		ClienteNatural clienteNatural = clienteNaturalBean.buscarPorId(expediente.getClienteNatural().getId());
		
		return copiarDatos(abstractMBean, expediente, clienteNatural, accion, estado);
	}
	
	private ExpedienteTCWPS copiarDatos (AbstractMBean abstractMBean, Expediente expediente,
			ClienteNatural clienteNatural, String accion, Integer estado) {
		
		
		
		Empleado empleado = (Empleado) abstractMBean.getObjectSession(Constantes.USUARIO_SESION);
		
		ExpedienteTCWPSWeb objTempWeb= (ExpedienteTCWPSWeb) abstractMBean.getObjectSession(Constantes.EXPEDIENTE_PROCESO_SESION);
		ExpedienteTCWPS expedienteTCWPS = fromExpedienteTCWPSWebToExpedienteTCWPS(objTempWeb);
		
		/*if (Constantes.ACCION_BOTON_RECHAZAR_EXPEDIENTE.equals(accion)){
			if(estadoBeanLocalBean!=null){
				Estado objEstado=estadoBeanLocalBean.buscarPorId(estado);
				if(objEstado!=null)
					expedienteTCWPS.setEstado(objEstado.getDescripcion()==null ? "":objEstado.getDescripcion());
			}

			if(accion!=null )
				expedienteTCWPS.setAccion(accion);
			
			return expedienteTCWPS;
		}*/
		
		//+++++++++++++++++++++
		expedienteTCWPS.setDevueltoPor("");
		expedienteTCWPS.setModificacionScoring("");
		expedienteTCWPS.setScoringAprobado("");
		expedienteTCWPS.setCodigoRVGL("");
		expedienteTCWPS.setFlagRetraer("0");
		//expedienteTCWPS.setCodigoUsuarioActual("");	
		expedienteTCWPS.setPerfilUsuarioActual("");
		expedienteTCWPS.setFlagSubrogacion(expediente.getClienteNatural().getSubrog());
		//expedienteTCWPS.setNombreUsuarioActual("");
		
		//+++++++++++++++++++++
		
		//VERIFICA SI EXPEDIENTE Y EXPEDIENTETC NO ES NULO
		if(expediente!=null && expediente.getExpedienteTC()!=null){
			
			if(expediente.getExpedienteTC().getTipoMonedaSol()!=null){
				expedienteTCWPS.setMoneda(expediente.getExpedienteTC().getTipoMonedaSol().getDescripcion());
			}
			
			expedienteTCWPS.setIdTarea(expediente.getExpedienteTC().getTarea()==null?"":expediente.getExpedienteTC().getTarea().getCodigo());
			
			expedienteTCWPS.setLineaCredito(expediente.getExpedienteTC().getLineaCredSol());
			expedienteTCWPS.setMontoAprobado(expediente.getExpedienteTC().getLineaCredAprob());
			expedienteTCWPS.setCodigoPreEvaluador(expediente.getExpedienteTC().getCodPreEval()==null?"":expediente.getExpedienteTC().getCodPreEval());
			expedienteTCWPS.setNumeroContrato(expediente.getExpedienteTC().getNroContrato()==null?"":expediente.getExpedienteTC().getNroContrato());
			expedienteTCWPS.setObservacion(expediente.getComentario());
			LOG.info("Observacion -> "+expediente.getComentario());
			
			Calendar c=Calendar.getInstance();
			LOG.info("Tiempo T3-> "+expediente.getExpedienteTC().getFechaT3());
			if(expediente.getExpedienteTC().getFechaT3()!=null){
				c.setTimeInMillis(expediente.getExpedienteTC().getFechaT3().getTime());
				/*c.set(expediente.getExpedienteTC().getFechaT3().getYear(), expediente.getExpedienteTC().getFechaT3().getMonth(), 
						expediente.getExpedienteTC().getFechaT3().getDate(), expediente.getExpedienteTC().getFechaT3().getHours(),
						expediente.getExpedienteTC().getFechaT3().getMinutes(), expediente.getExpedienteTC().getFechaT3().getSeconds());*/
				LOG.info("Calendar c -> "+c.toString());
				expedienteTCWPS.setActivado(c);				
			}else
				LOG.info("expediente.getFecRegistro() es nulo");
			
			if(expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getFechaT1()!=null && expediente.getExpedienteTC().getFechaT2()!=null){
				LOG.info("Fecha y Hora fecha 1:::"+expediente.getExpedienteTC().getFechaT1().toString());
				LOG.info("Fecha y Hora fecha 2:::"+expediente.getExpedienteTC().getFechaT2().toString());
			}else
				LOG.info("f1 y f2 nulos");

			LOG.info("Fecha y Hora fecha 3:::"+expedienteTCWPS.getActivado().toString());
			
			//expedienteTC.setScoringAprobado(scoringAprobado);// 3
			//expedienteTC.setModificacionScoring(modificacionScoring);// 16, 21, 24
			//expedienteTC.setCodigoRVGL(codigoRVGL);// esta en cu 14
			

			//if(estado!=null && estado.equals(Constantes.ESTADO_APROBADO_TAREA_3)){
				expedienteTCWPS.setCodigoRVGL(expediente.getExpedienteTC().getRvgl()==null?"":expediente.getExpedienteTC().getRvgl());
				
				expedienteTCWPS.setModificacionScoring(expediente.getExpedienteTC().getFlagModifScore()==null?"":expediente.getExpedienteTC().getFlagModifScore());
				
				if(expediente.getExpedienteTC().getTipoScoring()!=null && tipoScoringBean!=null){
						TipoScoring objTipoScoring=tipoScoringBean.buscarPorId(expediente.getExpedienteTC().getTipoScoring().getId());
						expedienteTCWPS.setScoringAprobado(objTipoScoring.getDescripcion()==null?"":objTipoScoring.getDescripcion());
				}else
					expedienteTCWPS.setScoringAprobado("");
			//}	
			
			if(expediente.getExpedienteTC().getOficina()!=null){
				LOG.info("Seteando Oficina:::"+expediente.getExpedienteTC().getOficina().getId());
				Oficina objOficina=expediente.getExpedienteTC().getOficina();
				if(objOficina!=null && expedienteTCWPS!=null){
					expedienteTCWPS.setIdOficina(Util.validarCampoLong(""+objOficina.getId())==null?"":""+objOficina.getId());
					expedienteTCWPS.setDesOficina(objOficina.getId()>0?objOficina.getDescripcion():"");
				}

				if(objOficina!=null && objOficina.getTerritorio()!=null){
					expedienteTCWPS.setIdTerritorio(String.valueOf(objOficina.getTerritorio().getId()));
					expedienteTCWPS.setDesTerritorio(objOficina.getTerritorio().getId()>0?objOficina.getTerritorio().getDescripcion():"");
				}
								
			}
			
			if(expediente.getExpedienteTC().getTipoOferta()!=null){
				TipoOferta objTipoOferta=expediente.getExpedienteTC().getTipoOferta();
				expedienteTCWPS.setTipoOferta(objTipoOferta.getDescripcion()==null?"":objTipoOferta.getDescripcion());
				expedienteTCWPS.setIdTipoOferta(Util.validarCampoLong(""+objTipoOferta.getId())==null?"":""+objTipoOferta.getId());
			}
			
			if(expediente.getVerificacionExpDom()!=null || expediente.getVerificacionExpLab()!=null){
				boolean verif = Constantes.CHECK_SELECCIONADO.equals(expediente.getVerificacionExpDom()) 
						|| Constantes.CHECK_SELECCIONADO.equals(expediente.getVerificacionExpLab());
				
				expedienteTCWPS.setVerificacionDomiciliaria(verif ? Constantes.CHECK_SELECCIONADO : 
					Constantes.CHECK_NO_SELECCIONADO);	
			}
			LOG.info("");
			if(expediente.getProducto()!=null){
				expedienteTCWPS.setProducto(new Producto());
				expedienteTCWPS.getProducto().setProducto(expediente.getProducto().getDescripcion()==null?"":expediente.getProducto().getDescripcion());
				expedienteTCWPS.getProducto().setIdProducto(String.valueOf(expediente.getProducto().getId()));
			}
				
			if(expediente.getExpedienteTC().getSubproducto()!=null)
				expedienteTCWPS.getProducto().setSubProducto(expediente.getExpedienteTC().getSubproducto().getDescripcion()==null?"":expediente.getExpedienteTC().getSubproducto().getDescripcion());
					
			expediente.getExpedienteTC().setVerifDom(expediente.getExpedienteTC().getVerifDom()==null?Constantes.CHECK_NO_SELECCIONADO:expediente.getExpedienteTC().getVerifDom());
			expediente.getExpedienteTC().setVerifLab(expediente.getExpedienteTC().getVerifLab()==null?Constantes.CHECK_NO_SELECCIONADO:expediente.getExpedienteTC().getVerifLab());
			expediente.getExpedienteTC().setVerifDps(expediente.getExpedienteTC().getVerifDps()==null?Constantes.CHECK_NO_SELECCIONADO:expediente.getExpedienteTC().getVerifDps());
			//Para pruebas, de lo contrario manda NULL, cuando deberia ser cero '0'
			/*if(expediente.getExpedienteTC().getVerifDom()!=null)
				expedienteTCWPS.setVerificacionDomiciliaria(expediente.getExpedienteTC().getVerifDom());
			else
				expedienteTCWPS.setVerificacionDomiciliaria(Constantes.CHECK_NO_SELECCIONADO);*/
			if(expediente.getExpedienteTC().getVerifDom().equals(Constantes.CHECK_SELECCIONADO) || 
					expediente.getExpedienteTC().getVerifLab().equals(Constantes.CHECK_SELECCIONADO) || 
					expediente.getExpedienteTC().getVerifDps().equals(Constantes.CHECK_SELECCIONADO)){
				expedienteTCWPS.setVerificacionDomiciliaria(Constantes.CHECK_SELECCIONADO);
				LOG.info("Verificacion Domiciliaria TRUE");
			}else{
				expedienteTCWPS.setVerificacionDomiciliaria(Constantes.CHECK_NO_SELECCIONADO);
				LOG.info("Verificacion Domiciliaria TRUE");
			}	
		}
		
		//expedienteTC.setDelegacion(delegacion);// 12
		//expedienteTC.setDevueltoPor(devueltoPor);// 15
		//expedienteTCWPS.setVerificacionConforme(0);// 5	
		
		if(clienteNatural!=null){
			Cliente cliente	=new Cliente();
			cliente.setApMaterno(clienteNatural.getApeMat()==null?"":clienteNatural.getApeMat());
			cliente.setApPaterno(clienteNatural.getApePat()==null?"":clienteNatural.getApePat());
			cliente.setNombre(clienteNatural.getNombre()==null?"":clienteNatural.getNombre());

			cliente.setNumeroDOI(clienteNatural.getNumDoi()==null?"":clienteNatural.getNumDoi());
			cliente.setCodigoCliente(clienteNatural.getCodCliente()==null?"":clienteNatural.getCodCliente());
			
			if(clienteNatural.getTipoCliente()!=null){
				TipoCliente objTipoCliente=clienteNatural.getTipoCliente();
				cliente.setCodigoTipoCliente(objTipoCliente.getCodigo()==null?"":objTipoCliente.getCodigo());
				cliente.setDescripcionTipoCliente(objTipoCliente.getDescripcion()==null?"":objTipoCliente.getDescripcion());				
			}
			
			if(clienteNatural.getCorreo()!=null){		
				cliente.setCorreo(clienteNatural.getCorreo());
			}
			if(clienteNatural.getCelular()!=null){	
				cliente.setNroCelular(clienteNatural.getCelular());
			}
			 
			if(clienteNatural.getTipoDoi().getDescripcion()!=null){				
				cliente.setTipoDOI(clienteNatural.getTipoDoi().getDescripcion()==null?"":clienteNatural.getTipoDoi().getDescripcion());	
			}else
				if(tipoDoiBeanLocalBean!=null){
					
					TipoDoi objTipoDoi=tipoDoiBeanLocalBean.buscarPorId(clienteNatural.getTipoDoi().getId());
					cliente.setTipoDOI(objTipoDoi.getDescripcion()==null?"":objTipoDoi.getDescripcion());
				}else
					cliente.setTipoDOI("");
			
			expedienteTCWPS.setCliente(cliente);// AGREGADOOO	
			
			if(clienteNatural.getSegmento()!=null){
				expedienteTCWPS.setSegmento(clienteNatural.getSegmento().getDescripcion()==null?"":clienteNatural.getSegmento().getDescripcion());
				LOG.info("clienteNatural.getSegmento().getDescripcion() : "+clienteNatural.getSegmento().getDescripcion());
				if (clienteNatural.getSegmento().getGrupoSegmento()!=null) {
					LOG.info("clienteNatural-getSegmento-getGrupoSegmento-id : "+clienteNatural.getSegmento().getGrupoSegmento().getId());
				    expedienteTCWPS.setIdGrupoSegmento(Util.validarCampoLong(""+clienteNatural.getSegmento().getGrupoSegmento().getId())==null?"":""+clienteNatural.getSegmento().getGrupoSegmento().getId());
				    LOG.info("IdGrupoSegmento ::::: "+expedienteTCWPS.getIdGrupoSegmento());
				}				
			}				

		}
		
		String existenTransferencias =  (String) FacesContext.getCurrentInstance().
		getExternalContext().getSessionMap().get(Constantes.EXISTEN_TRANSFERENCIAS);

		
		if (existenTransferencias == null || existenTransferencias.trim().equals("")) {
			existenTransferencias = Constantes.FLAG_EXISTE_TRANSFERENCIAS_CERO;
		}
		
		// expedienteTCWPS.setEnvioContent(existenTransferencias.equals(Constantes.FLAG_EXISTE_TRANSFERENCIAS_CERO)?Constantes.FLAG_EXISTE_TRANSFERENCIAS_UNO:Constantes.FLAG_EXISTE_TRANSFERENCIAS_CERO);

		//expedienteTC.setEnvioContent(Constantes.FLAG_EXISTE_TRANSFERENCIAS_UNO);
		
		if(estado !=null && estadoBeanLocalBean!=null){
			Estado objEstado=estadoBeanLocalBean.buscarPorId(estado);
			if(objEstado!=null)
				expedienteTCWPS.setEstado(objEstado.getDescripcion()==null ? "":objEstado.getDescripcion());
		}

		if(accion!=null )
			expedienteTCWPS.setAccion(accion);
		
		expedienteTCWPS.setCodigo(String.valueOf(expediente.getId()));
		
		if(empleado!=null){
			LOG.info("CODIGO ="+empleado.getCodigo());
			LOG.info("ID ="+empleado.getId());
			
			String codigoUsuario = empleado.getCodigo();
			
			expedienteTCWPS.setNombreUsuarioAnterior(empleado.getNombresCompletos()==null?"":empleado.getNombresCompletos());
			expedienteTCWPS.setCodigoUsuarioAnterior(codigoUsuario);
			

			if(empleado.getPerfil()!=null){
				String idPerfilUsuarioAnt=""+empleado.getPerfil().getId();
				
				expedienteTCWPS.setIdPerfilUsuarioAnterior(idPerfilUsuarioAnt);				
				expedienteTCWPS.setPerfilUsuarioAnterior(empleado.getPerfil().getDescripcion()==null?"":empleado.getPerfil().getDescripcion());
				
				expedienteTCWPS.setIdPerfilUsuarioActual(""+empleado.getPerfil().getId());
				expedienteTCWPS.setPerfilUsuarioActual(empleado.getPerfil().getDescripcion());
				//Opcion GRABAR >> Constantes.ACCION_BOTON_GRABAR, 	Constantes.ESTADO_EN_REGISTRO_TAREA_1
				if(accion.equals(Constantes.ACCION_BOTON_GRABAR) && estado==Constantes.ESTADO_EN_REGISTRO_TAREA_1){
					expedienteTCWPS.setCodigoUsuarioActual(empleado.getCodigo());
					expedienteTCWPS.setNombreUsuarioActual(empleado.getNombresCompletos()==null?"":empleado.getNombresCompletos());					
				}
				LOG.info("PerfilUsuarioActual : "+expedienteTCWPS.getPerfilUsuarioActual());
				expedienteTCWPS.setFlagProvincia(empleado.getOficina().getTerritorio().getFlagProv());
			}
				
			if(empleado.getOficina()!=null && empleado.getOficina().getTerritorio()!=null)
				expedienteTCWPS.setFlagProvincia(empleado.getOficina().getTerritorio().getFlagProv()==null?"":empleado.getOficina().getTerritorio().getFlagProv());
			
			if (expedienteTCWPS.getCodigoEmpleadoResponsable()==null) {
				expedienteTCWPS.setCodigoEmpleadoResponsable(codigoUsuario);
				
			}		
						
		}
		
		return expedienteTCWPS;
	}

	/**
	 * Actualiza la lista con elementos de tipo ExpedienteTC y lo almacena en sesion
	 * para que luego sea utilizada en la bandeja de pendientes
	 * @param consulta
	 */
	public void actualizarListaExpedienteTC(Consulta consulta) {
		RemoteUtils tareasBDelegate = new RemoteUtils();
		
		consulta=obtenerUsuario(consulta);		
		//List<ExpedienteTCWPS> lista = tareasBDelegate.obtenerInstanciasTareasPorUsuario(consulta);
		List<ExpedienteTCWPSWeb> lista = tareasBDelegate.obtenerListaTareasBandPendiente(consulta);
		
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
				Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, lista);
		tareasBDelegate=null;
		lista=null;
	}
	
	public Consulta obtenerUsuario(Consulta consulta){
		
		List<String> listUsuarios=new ArrayList<String>();
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		listUsuarios.add(empleado.getCodigo()); 
		consulta.setUsuarios(listUsuarios);
		
		//Empleado empleado = (Empleado) FacesContext.getCurrentInstance().getExternalContext()
        		//.getSessionMap().get(Constantes.USUARIO_SESION);		
		//List<String> listUsuarios=new ArrayList<String>();		
		//listUsuarios.add(empleado.getCodigo()); 
		//consulta.setUsuarios(listUsuarios);
		consulta.setCodUsuarioActual(empleado.getCodigo());
		//consulta.setConsiderarUsuarios(true);
		return consulta;
	}
	
	public ExpedienteTCWPS fromExpedienteTCWPSWebToExpedienteTCWPS(ExpedienteTCWPSWeb obj) {
		ExpedienteTCWPS objTemp = new ExpedienteTCWPS();
		
			objTemp.setAccion(obj.getAccion());
			objTemp.setActivado(obj.getActivado());
			objTemp.setCantDocumentos(obj.getCantDocumentos());
			objTemp.setCliente(obj.getCliente());
			objTemp.setCodigo(obj.getCodigo());
			objTemp.setCodigoEmpleadoResponsable(obj.getCodigoEmpleadoResponsable());
			objTemp.setCodigoPreEvaluador(obj.getCodigoPreEvaluador());
			objTemp.setCodigoRVGL(obj.getCodigoRVGL());
			objTemp.setCodigoUsuarioActual(obj.getCodigoUsuarioActual());
			objTemp.setCodigoUsuarioAnterior(obj.getCodigoUsuarioAnterior());
			objTemp.setDescripcionError(obj.getDescripcionError());
			objTemp.setDesOficina(obj.getDesOficina());
			objTemp.setDesTarea(obj.getDesTarea());
			objTemp.setDesTareaAnterior(obj.getDesTareaAnterior());
			objTemp.setDesTerritorio(obj.getDesTerritorio());
			objTemp.setDevueltoPor(obj.getDevueltoPor());
			objTemp.setEstado(obj.getEstado());
			objTemp.setEstadoAnterior(obj.getEstadoAnterior());
			objTemp.setFechaCancelacion(obj.getFechaCancelacion());
			objTemp.setFechaDocumento(obj.getFechaDocumento());
			objTemp.setFechaIncidencia(obj.getFechaIncidencia());
			objTemp.setFechaRestauracion(obj.getFechaRestauracion());
			objTemp.setFlagEnProcesoTimer(obj.getFlagEnProcesoTimer());
			objTemp.setFlagEnvioContent(obj.getFlagEnvioContent());
			objTemp.setFlagProvincia(obj.getFlagProvincia());
			objTemp.setFlagRetraer(obj.getFlagRetraer());
			objTemp.setFlagSubrogacion(obj.getFlagSubrogacion());
			objTemp.setIdGrupoSegmento(obj.getIdGrupoSegmento());
			objTemp.setIdOficina(obj.getIdOficina());
			objTemp.setIdPerfilUsuarioActual(obj.getIdPerfilUsuarioActual());
			objTemp.setIdPerfilUsuarioAnterior(obj.getIdPerfilUsuarioAnterior());
			objTemp.setIdTarea(obj.getIdTarea());
			objTemp.setIdTareaAnterior(obj.getIdTareaAnterior());
			objTemp.setIdTerritorio(obj.getIdTerritorio());
			objTemp.setIdTipoOferta(obj.getIdTipoOferta());
			objTemp.setLineaCredito(obj.getLineaCredito()==null?0.0:obj.getLineaCredito());
			objTemp.setModificacionScoring(obj.getModificacionScoring());
			objTemp.setMoneda(obj.getMoneda());
			objTemp.setMontoAprobado(obj.getMontoAprobado());
			objTemp.setNombreNavegacionWeb(obj.getNombreNavegacionWeb());
			objTemp.setNombreUsuarioActual(obj.getNombreUsuarioActual());
			objTemp.setNombreUsuarioAnterior(obj.getNombreUsuarioAnterior());
			objTemp.setNroReintentos(obj.getNroReintentos());
			objTemp.setNumeroContrato(obj.getNumeroContrato());
			objTemp.setNumeroDevoluciones(obj.getNumeroDevoluciones());
			objTemp.setObservacion(obj.getObservacion());
			objTemp.setPerfilUsuarioActual(obj.getPerfilUsuarioActual());
			objTemp.setPerfilUsuarioAnterior(obj.getPerfilUsuarioAnterior());
			objTemp.setProducto(obj.getProducto());
			objTemp.setScoringAprobado(obj.getScoringAprobado());
			objTemp.setSegmento(obj.getSegmento());
			objTemp.setTaskID(obj.getTaskID());
			objTemp.setTipoError(obj.getTipoError());
			objTemp.setTipoOferta(obj.getTipoOferta());
			objTemp.setVerificacionDomiciliaria(obj.getVerificacionDomiciliaria());
			
			return objTemp;
		}	
	
}
