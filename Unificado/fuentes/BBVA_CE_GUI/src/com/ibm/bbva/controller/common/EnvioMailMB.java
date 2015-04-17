package com.ibm.bbva.controller.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Accion;
import com.ibm.bbva.entities.CategoriaRenta;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.DatosCorreo;
import com.ibm.bbva.entities.DatosCorreoDestin;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.TipoMoneda;
import com.ibm.bbva.session.AccionBeanLocal;
import com.ibm.bbva.session.ClienteNaturalBeanLocal;
import com.ibm.bbva.session.DatosCorreoDestinBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.EstadoBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.DatosCorreoBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.TipoMonedaBeanLocal;
import com.ibm.bbva.util.EnvioMail;

@SuppressWarnings("serial")
@ManagedBean(name = "envioMail")
@RequestScoped
public class EnvioMailMB extends AbstractMBean {
	
	private static final Logger LOG = LoggerFactory.getLogger(EnvioMailMB.class);
	
	@EJB
	HistorialBeanLocal historialBeanLocalBean;
	@EJB
	EmpleadoBeanLocal empleadoBeanLocal;
	@EJB
	DatosCorreoBeanLocal datosCorreoBeanLocal;
	@EJB
	DatosCorreoDestinBeanLocal datosCorreoDestinBeanLocal;
	@EJB
	AccionBeanLocal accionBeanLocal;
	@EJB
	PerfilBeanLocal perfilBeanLocal;
	@EJB
	ClienteNaturalBeanLocal clienteNaturalBeanLocalBean;
	@EJB
	EmpleadoBeanLocal empleadoBeanLocalBean;
	@EJB 
	EstadoBeanLocal estadoBeanLocal;
	@EJB
	TipoMonedaBeanLocal tipoMonedaBeanLocal;
	
	private String accion="";
	private String asunto="";
	private String destinatario="";
	private String strCuerpo="";
	private long idPerfil=0;
	private long idResponsable=0;
	private List<DatosCorreoDestin> detalle= new ArrayList();
	
	public EnvioMailMB(){
		
	}
	@SuppressWarnings("unused")
	public void processEnvioCorreo(Expediente expediente){
		LOG.info("Entro al EnvioCorreo");
		Empleado empleado= new Empleado();
		EnvioMail envioMail = new EnvioMail();
		DatosCorreo datosCorreo= new DatosCorreo();
		long idTarea=0;
		String nuevoAsunto="";
		String nuevoCuerpo="";
		String perfilActual="";
		String perfilSiguiente="";
		long idPerfil=0;
		long idResponsable=0;
		long idProducto=0;
		long idAccion=0;
		String destinatarioCopia="";
		String strGestorExpediente="";
		try{
			List<Historial> listHistorial=null;
			idProducto=expediente.getProducto().getId();
			idTarea=expediente.getExpedienteTC().getTarea().getId();
			accion=expediente.getAccion();
			LOG.info("accion : "+accion);
			idResponsable=expediente.getEmpleado().getId();
			//idAccion=obtenerUltimaAccion(expediente.getId());
			idAccion=obtenerUltimaAccion(expediente);
			LOG.info("idResponsable : "+idResponsable);
			datosCorreo=datosCorreoBeanLocal.buscarPorTareaAccion(idTarea,idAccion,idProducto);
			if (datosCorreo!=null){
				long idOficinaExpediente=0;
				String strUsuarioSgteTarea="";
				String strGestorActual=""; // Usuario ( gestor ) que posee el expediente en la tarea actual. 
				List<InternetAddress> listAddresses = new ArrayList<InternetAddress> (); // To
				List<InternetAddress> listAddressesCopia= new ArrayList<InternetAddress>(); //CC
				long idCabecera=datosCorreo.getId();
				detalle=datosCorreoDestinBeanLocal.buscarPorIdDatosCorreo(idCabecera);
				LOG.info("datosCorreo : "+datosCorreo);
				asunto=datosCorreo.getAsunto();
				LOG.info("asunto : "+asunto);
				perfilActual="FORMALIZADOR";
				perfilSiguiente="FORMALIZADOR";
				listHistorial = historialBeanLocalBean.buscarPerfilRecientePorId(expediente.getId());
				nuevoAsunto=modificarTexto(asunto, expediente,listHistorial,perfilActual, perfilSiguiente);
				LOG.info("nuevoAsunto"+nuevoAsunto);// modifica el asunto de acuerdo a la tarea
				byte[] byteCuerpo = datosCorreo.getCuerpo();
				strCuerpo = byteCuerpo==null ? null : new String (byteCuerpo);
				LOG.info("cuerpo"+strCuerpo);
				nuevoCuerpo=modificarTexto(strCuerpo, expediente,listHistorial,perfilActual, perfilSiguiente);// modifica el cuerpo de acuerdo a la tarea
				LOG.info("nuevoCuerpo"+nuevoCuerpo);
				//Oficina donde se creo el expediente
				if (expediente!=null && expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getOficina()!=null){
					idOficinaExpediente=expediente.getExpedienteTC().getOficina().getId(); // Oficina donde se creo el expediente
				}
				List<Empleado> lstEmpleados = null;
				for (int i=0; i<detalle.size();i++){
					Perfil objPerfil=null;
					objPerfil=detalle.get(i).getPerfil();
					if(objPerfil.getId() == Constantes.ID_PERFIL_GERENTE || objPerfil.getId() == Constantes.ID_PERFIL_SUB_GERENTE){
						lstEmpleados = empleadoBeanLocalBean.buscarPorOficinaPerfil(idOficinaExpediente, Long.valueOf(objPerfil.getId()));
						if (lstEmpleados.size()>0){
							for (int j=0; j<lstEmpleados.size();j++){
								if (lstEmpleados.get(j).getCorreo()!=null){
									destinatario=lstEmpleados.get(j).getCorreo();
									if (destinatario!=null){
										LOG.info("destinatario no es nulo");
										listAddresses.add(new InternetAddress(destinatario));
									}
								}
							}
						}
					}
					else if(objPerfil.getId() == Constantes.PERFIL_JEFE_EQUIPO_CPM || objPerfil.getId() == Constantes.ID_PERFIL_SUPERIOR_RIESGOS){
						//lstEmpleados = empleadoBeanLocalBean.buscarPorIdPerfil(Long.valueOf(objPerfil.getId()));
						lstEmpleados=empleadoBeanLocalBean.buscarPorPerfilActivo(Long.valueOf(objPerfil.getId()));
						if (lstEmpleados.size()>0){
							for (int j=0; j<lstEmpleados.size();j++){
								if (lstEmpleados.get(j).getCorreo()!=null){
									destinatario=lstEmpleados.get(j).getCorreo();
									if (destinatario!=null){
										listAddresses.add(new InternetAddress(destinatario));
									}
								}
							}
						}
					}
					else{
						List<Historial> lstHistExpediente = new ArrayList<Historial>();
						lstHistExpediente=historialBeanLocalBean.buscarXCriterioExpedienteXPerfil(expediente.getId(), objPerfil.getId());
						String strUltimoParticipante="";
						if (lstHistExpediente.size()>0){
							for (Historial item :lstHistExpediente ){
								strUltimoParticipante=item.getEmpleado().getCorreo();
								if (strUltimoParticipante!=null){
									listAddresses.add(new InternetAddress(strUltimoParticipante));
								}
							}
						}
					}
				}
				// validacion del usuario actual y el siguiente
				for (int j=0; j<detalle.size();j++){
					Perfil objPerfil=null;
					objPerfil=detalle.get(j).getPerfil();
					if (objPerfil!=null){
						if (objPerfil.getId()==expediente.getEmpleado().getPerfil().getId()){
							strGestorActual=expediente.getEmpleado().getCorreo(); // CORREO DEL USUARIO QUIEN LO TIENE EN ESE MOMENTO.
							if (strGestorActual!=null){
								listAddresses.add(new InternetAddress(strGestorActual));
							}
						}
						if (objPerfil.getId()==expediente.getEmpleado().getPerfil().getId()){
							strUsuarioSgteTarea=expediente.getEmpleado().getCorreo();  // CORREO DEL USUARIO A QUIEN VA , DE LA SIGUIENTE TAREA
							if (strUsuarioSgteTarea!=null){
								listAddresses.add(new InternetAddress(strUsuarioSgteTarea));
							}
						}
					}
				}
				InternetAddress[] arrAddresses = listAddresses.toArray(new InternetAddress[0]);
				LOG.info("Entrara al Util Enviar Correo");
				envioMail.enviarMail(nuevoAsunto, arrAddresses, destinatario, nuevoCuerpo);
			}
		}catch(Exception e){
			e.printStackTrace();
			LOG.error("Error al enviar el Mail..en EnvioMailMB", e);
		}
		
	}
	
	public long obtenerUltimaAccion(Expediente objExpediente){
		long idAccion=0L;
		String strAccion="";
		strAccion=objExpediente.getAccion();
		if (strAccion!=null){
				LOG.info("obtenerUltimaAccion - strAccion "+ strAccion);
				LOG.info(" antes de instanciar accion");
				Accion accion =null;
				LOG.info("Accion instanciada");
				accion=accionBeanLocal.buscarPorAccion(strAccion);
				if (accion!=null){
					idAccion=accion.getId();
				}
			}
		return idAccion;	
	}

	public String obtenerDestinatario(long idResponsable){
		String destinatario;
		Empleado empleado= new Empleado();
		empleado=empleadoBeanLocal.buscarPorId(idResponsable);
		destinatario=empleado.getCorreo();
		return destinatario;
	}
	
	public String modificarTexto(String original, Expediente expediente, List<Historial> listHistorial,String perfilActual, 
								String perfilSiguiente){
		String newOriginal="";
		
		if (expediente.getId()!=0){
			original=original.replaceAll("NRO_EXPEDIENTE",String.valueOf(expediente.getId()));
		}
		
		if (expediente.getEstado()!=null){
			long idEstado= expediente.getEstado().getId();
			String strEstado="";
			Estado estadoTmp= new Estado();
			estadoTmp=estadoBeanLocal.buscarPorId(idEstado);
			if (estadoTmp!=null){
				strEstado=estadoTmp.getDescripcion();
				//original=original.replaceAll("ESTADO_EXPEDIENTE",String.valueOf(expediente.getEstado().getDescripcion()));
				original=original.replaceAll("ESTADO_EXPEDIENTE",strEstado);
			}
		}
		
		if (expediente.getClienteNatural()!=null){
			original=original.replaceAll("NOMBRE_CLIENTE",String.valueOf(expediente.getClienteNatural().getNombre()));
		}
		if (expediente.getClienteNatural()!=null){
			original=original.replaceAll("APEPAT_CLIENTE",String.valueOf(expediente.getClienteNatural().getApePat()));
			original=original.replaceAll("APEMAT_CLIENTE",String.valueOf(expediente.getClienteNatural().getApeMat()));
		}	
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoMonedaSol()!=null){
			long idTipoMonedaSol=expediente.getExpedienteTC().getTipoMonedaSol().getId();
			TipoMoneda tipoMoneda = new TipoMoneda();
			tipoMoneda = tipoMonedaBeanLocal.buscarPorId(idTipoMonedaSol);
			
			String tipoMonedaDescripcion = tipoMoneda.getDescripcion();
			
			if(tipoMonedaDescripcion != null){
				if(tipoMonedaDescripcion.equals("$")){
					original=original.replaceAll("TIPO_MONEDA_SOLICITADO","\\" + tipoMonedaDescripcion);
				}else{
					original=original.replaceAll("TIPO_MONEDA_SOLICITADO", tipoMonedaDescripcion);
				}
			}
			
		}

		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoMonedaAprob()!=null){
			long idTipoMonedaSol=expediente.getExpedienteTC().getTipoMonedaAprob().getId();
			TipoMoneda tipoMoneda = new TipoMoneda();
			tipoMoneda = tipoMonedaBeanLocal.buscarPorId(idTipoMonedaSol);
			
			String tipoMonedaDescripcion = tipoMoneda.getDescripcion();
			
			if(tipoMonedaDescripcion != null){
				if(tipoMonedaDescripcion.equals("$")){
					original=original.replaceAll("TIPO_MONEDA_APROBADO","\\" + tipoMonedaDescripcion);
				}else{
					original=original.replaceAll("TIPO_MONEDA_APROBADO", tipoMonedaDescripcion);
				}
			}
			
		}
		
		
		
		
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getLineaCredAprob()>=0){
			original=original.replaceAll("LINEA_CREDITO_APROBADA",String.valueOf(expediente.getExpedienteTC().getLineaCredAprob()));
		}
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getLineaCredSol()>=0){
			original=original.replaceAll("LINEA_CREDITO_SOLICITADA",String.valueOf(expediente.getExpedienteTC().getLineaCredSol()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getLineaCredSol()>=0){
			original=original.replaceAll("LINEA_CREDITO",String.valueOf(expediente.getExpedienteTC().getLineaCredSol()));
		}
		
//		if (listHistorial.size()>0 || listHistorial!=null){
//				original=original.replaceAll("ULTIMO_EJECUT_HISTORIAL",String.valueOf(listHistorial.get(0).getEmpleado().getCodigo()));
//		}
		
		/*INICIO INCIDENCIA 85 06.01.2015*/
		if (expediente!=null && expediente.getEmpleado()!=null){ 
			List<Historial> lstHistExpediente = new ArrayList<Historial>();
			long idPerfilEjecutivo=6;
			//lstHistExpediente=historialBeanLocalBean.buscarXCriterioExpedienteXPerfil(expediente.getId(),expediente.getEmpleado().getPerfil().getId());
			lstHistExpediente=historialBeanLocalBean.buscarXCriterioExpedienteXPerfil(expediente.getId(),idPerfilEjecutivo);  // busca el ultimo EJECUTIVO que reviso el expediente
			String strCodigoUltimoEjecutivo="";
			String strUltimoEjecutivo="";
			String strUltimoEjec="";
			if (lstHistExpediente.size()>0){
				for (Historial item :lstHistExpediente ){
					strCodigoUltimoEjecutivo=item.getEmpleado().getCodigo();
					strUltimoEjecutivo=item.getEmpleado().getNombresCompletos();
					strUltimoEjec=strCodigoUltimoEjecutivo+ " - " +strUltimoEjecutivo;
					if (strUltimoEjecutivo!=null){
						original=original.replaceAll("ULTIMO_EJECUT_HISTORIAL",strUltimoEjec);
						
					}
				}
			}
		}

		/*FIN INCIDENCIA 85 06.01.2015*/
		
		
		if (perfilActual!=null){
				original=original.replaceAll("PERFIL_USUARIO_ORIGEN",perfilActual);
		}
		if (perfilSiguiente!=null){
			original=original.replaceAll("PERFIL_USUARIO_DESTINO",perfilSiguiente);
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getOficina()!=null){
			original=original.replaceAll("COD_OFICINA",String.valueOf(expediente.getExpedienteTC().getOficina().getCodigo()));
		}
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getOficina()!=null){
			original=original.replaceAll("NOMBRE_OFICINA",String.valueOf(expediente.getExpedienteTC().getOficina().getDescripcion()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getNroCta()!=null){
			original=original.replaceAll("NRO_CUENTA",String.valueOf(expediente.getExpedienteTC().getNroCta()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getNroContrato()!=null){
			original=original.replaceAll("NRO_CONTRATO",String.valueOf(expediente.getExpedienteTC().getNroContrato()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getNumTarjeta()!=null){
			original=original.replaceAll("NUM_TARJETA",String.valueOf(expediente.getExpedienteTC().getNumTarjeta()));
		}
	
		if (expediente.getComentario()!=null){
			original=original.replaceAll("COMENTARIO",String.valueOf(expediente.getComentario()));
		}
		
		if (expediente.getClienteNatural()!=null && expediente.getClienteNatural().getCodCliente()!=null){
			original=original.replaceAll("COD_CLIENTE",String.valueOf(expediente.getClienteNatural().getCodCliente()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getClienteNaturalConyuge()!=null){
			long idConyuge=expediente.getExpedienteTC().getClienteNaturalConyuge().getId();
			ClienteNatural conyuge = new ClienteNatural();
			conyuge=clienteNaturalBeanLocalBean.buscarPorId(idConyuge);
			String tipoDoiConyuge=conyuge.getTipoDoi().getDescripcion();
			String doiConyuge=conyuge.getNumDoi();
			String nombreConyuge=conyuge.getNombre();
			String apePatConyuge=conyuge.getApePat();
			String apeMatConyuge=conyuge.getApeMat();
			
			String nombreCompletoConyuge=nombreConyuge+" "+apePatConyuge+" "+apeMatConyuge;
			
			if (tipoDoiConyuge!=null){
				//if (expediente.getClienteNatural().getPersona().getCodigo().equals("2")){
				original=original.replaceAll("TIPO_DOI_CONYUGE",tipoDoiConyuge);
				//}
			}
			
			if (doiConyuge!=null){
				//if (expediente.getClienteNatural().getPersona().getCodigo().equals("2")){
					original=original.replaceAll("NRO_DOI_CONYUGE",doiConyuge);
				//}
			}
				
			if (nombreCompletoConyuge!=null){
				//if (expediente.getClienteNatural().getPersona().getCodigo().equals("2")){
					//String nombreConyuge = expediente.getClienteNatural().getNombre()+" "+expediente.getClienteNatural().getApePat()+" "+expediente.getClienteNatural().getApeMat();
					original=original.replaceAll("NOMBRE_COMPLETO_CONYUGE",nombreCompletoConyuge);
				//}
			}
			
		}else{
			original=original.replaceAll("TIPO_DOI_CONYUGE"," ");
			original=original.replaceAll("NRO_DOI_CONYUGE"," ");
			original=original.replaceAll("NOMBRE_COMPLETO_CONYUGE"," ");
		}
		
		if (expediente.getClienteNatural()!=null && expediente.getClienteNatural().getTipoDoi()!=null){	
			original=original.replaceAll("TIPO_DOI",String.valueOf(expediente.getClienteNatural().getTipoDoi().getDescripcion()));
		}
		
		if (expediente.getClienteNatural()!=null && expediente.getClienteNatural().getNumDoi()!=null){	
			original=original.replaceAll("NRO_DOI",String.valueOf(expediente.getClienteNatural().getNumDoi()));
		}
		
		if (expediente.getClienteNatural()!=null && expediente.getClienteNatural().getFecVenDoi()!=null){	
			SimpleDateFormat dateformat= new SimpleDateFormat("dd-MM-yyyy");
			String fechaVenDoi=dateformat.format(expediente.getClienteNatural().getFecVenDoi());
			//original=original.replaceAll("FECHA_VEN_DOI",String.valueOf(expediente.getClienteNatural().getFecVenDoi()));
			original=original.replaceAll("FECHA_VEN_DOI",fechaVenDoi);
		}
		
		if (expediente.getClienteNatural()!=null && expediente.getClienteNatural().getEstadoCivil()!=null){	
			original=original.replaceAll("ESTADO_CIVIL",String.valueOf(expediente.getClienteNatural().getEstadoCivil().getDescripcion()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getSubproducto()!=null){	
			original=original.replaceAll("SUB_PRODUCTO",String.valueOf(expediente.getExpedienteTC().getSubproducto().getDescripcion()));
		}
		
		if (expediente.getProducto()!=null){
			original=original.replaceAll("PRODUCTO",String.valueOf(expediente.getProducto().getDescripcion()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getTipoOferta()!=null){	
			original=original.replaceAll("TIPO_OFERTA",String.valueOf(expediente.getExpedienteTC().getTipoOferta().getDescripcion()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getPlazoSolicitado()!=null){
			original=original.replaceAll("PLAZO_SOLICITADO",String.valueOf(expediente.getExpedienteTC().getPlazoSolicitado()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getPlazoSolicitadoApr()!=null){
			original=original.replaceAll("PLAZO_APROBADO",String.valueOf(expediente.getExpedienteTC().getPlazoSolicitadoApr()));
		}
		
		if (listHistorial.size()>0 || listHistorial!=null){
			original=original.replaceAll("USUARIO_ORIGEN",String.valueOf(listHistorial.get(0).getEmpleado().getCodigo()));
		}
		
		if (expediente!=null && expediente.getEmpleado()!=null){
			original=original.replaceAll("USUARIO_DESTINO",String.valueOf(expediente.getEmpleado().getCodigo()));
		}
		
		if (expediente.getClienteNatural()!=null && expediente.getClienteNatural().getSegmento()!=null){
			original=original.replaceAll("SEGMENTO",String.valueOf(expediente.getClienteNatural().getSegmento().getDescripcion()));
		}
		
		List<CategoriaRenta> listCategorias=null;
		if (expediente.getClienteNatural().getCategoriasRenta()!=null){
			listCategorias=expediente.getClienteNatural().getCategoriasRenta();
			String categorias="";
				for (int i=0; i<listCategorias.size();i++){
					categorias+=listCategorias.get(i).getDescripcion()+",";
				}
				original=original.replaceAll("CATEGORIA_RENTA",categorias);
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getCodPreEval()!=null){
			original=original.replaceAll("COD_PRE_EVALUADOR",String.valueOf(expediente.getExpedienteTC().getCodPreEval()));
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getVerifDom()!=null){
			if (expediente.getExpedienteTC().getVerifDom().equals("1")){
				original=original.replaceAll("VERIF_DOMICILIARIA","VERIFICACION DOMICILIARIA ACTIVA");
			}else{
				original=original.replaceAll("VERIF_DOMICILIARIA","VERIFICACION DOMICILIARIA NO ACTIVA");
			}
			
		}
		
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getVerifLab()!=null){
			//original=original.replaceAll("VERIF_LABORAL",String.valueOf(expediente.getExpedienteTC().getVerifLab()));
			if (expediente.getExpedienteTC().getVerifDom().equals("1")){
				original=original.replaceAll("VERIF_LABORAL","VERIFICACION LABORAL ACTIVA");
			}else{
				original=original.replaceAll("VERIF_LABORAL","VERIFICACION LABORAL NO ACTIVA");
			}
		}
			
		if (expediente.getExpedienteTC()!=null && expediente.getExpedienteTC().getVerifDps()!=null){
			//original=original.replaceAll("VERIF_DPS",String.valueOf(expediente.getExpedienteTC().getVerifDps()));
			if (expediente.getExpedienteTC().getVerifDom().equals("1")){
				original=original.replaceAll("VERIF_DPS","VERIFICACION DPS ACTIVA");
			}else{
				original=original.replaceAll("VERIF_DPS","VERIFICACION DPS NO ACTIVA");
			}
		}	
		original=original.replaceAll("PERFIL_SUPERIOR_RIESGOS","SUPERVISOR RIESGOS");
		
		newOriginal=original;
		return newOriginal;
	}
	
	public String obtenerPerfilActual(Long idTarea){
		String perfilActual="";
		long idPerfilActual=0;
		if (idTarea==1 || idTarea==10 || idTarea==15 ||idTarea== 16 || idTarea==20){
			idPerfilActual=6;
		}else if (idTarea==2 ){
			idPerfilActual=5;
		}else if (idTarea==3 || idTarea==14 || idTarea==18 ){
			idPerfilActual=3;
		}else if (idTarea==4){
			idPerfilActual=13;
		}else if (idTarea==5 || idTarea==6 || idTarea==13 ||idTarea== 17 || idTarea==26){
			idPerfilActual=10;
		}else if (idTarea==7 || idTarea==11 || idTarea==27){
			idPerfilActual=2;
		}else if (idTarea==12){
			idPerfilActual=8;
		}else if (idTarea==19){
			idPerfilActual=4;
			}
		Perfil perfilAux = new Perfil();
		perfilAux=perfilBeanLocal.buscarPorId(idPerfilActual);
		perfilActual=perfilAux.getDescripcion();
		return perfilActual;
	}
	
	public String obtenerPerfilSiguiente(Long idTarea , Long idAccion){
		String perfilSiguiente="";
		long idPerfilSiguiente=0;
		if (idTarea==1){
			if (idAccion==20){
				idPerfilSiguiente=5;
			}else if (idAccion==16){
				idPerfilSiguiente=6;
			}
		}else if (idTarea==2){
			if (idAccion==9){
				idPerfilSiguiente=3;
			}else if (idAccion==10){
				idPerfilSiguiente=6;
			}
		}else if (idTarea==3){
			if (idAccion==14){
				idPerfilSiguiente=10;
			}else if (idAccion==10){
				idPerfilSiguiente=5;
			}
		}else if (idTarea==4){
			if (idAccion==6){
				idPerfilSiguiente=10;
			}else if (idAccion==5){
				idPerfilSiguiente=6;
			}else if (idAccion==11){
				idPerfilSiguiente=8;
			}else if (idAccion==10){
				idPerfilSiguiente=6;
			}else if (idAccion==19){
				idPerfilSiguiente=2;
			}else if (idAccion==22){
				idPerfilSiguiente=10;
			}
		}else if (idTarea==5){
			if (idAccion==21){
				idPerfilSiguiente=2;
			}else if (idAccion==13){
				idPerfilSiguiente=10;
			}else if (idAccion==10){
				idPerfilSiguiente=6;
			}else if (idAccion==18){
				idPerfilSiguiente=6;
			}else if (idAccion==19){
				idPerfilSiguiente=2;
			}
		}else if (idTarea==6){
			if (idAccion==21){
				idPerfilSiguiente=2;
			}else if (idAccion==10){
				idPerfilSiguiente=6;
			}
		}else if (idTarea==7){
			if (idAccion==7){
				idPerfilSiguiente=2;
			}else if (idAccion==10){
				idPerfilSiguiente=10;
			}
		}else if (idTarea==10){
			if (idAccion==23){
				idPerfilSiguiente=10;
			}else if (idAccion==15){
				idPerfilSiguiente=10;
			}else if (idAccion==19){
				idPerfilSiguiente=2;
			}
		}else if (idTarea==11){
			idPerfilSiguiente=0;
		}else if (idTarea==12){
			if (idAccion==6){
				idPerfilSiguiente=10;
			}else if (idAccion==5){
				idPerfilSiguiente=6;
			}else if (idAccion==22){
				idPerfilSiguiente=10;
			}else if (idAccion==12){
				idPerfilSiguiente=4;
			}else if (idAccion==10){
				idPerfilSiguiente=6;
			}else if (idAccion==19){
				idPerfilSiguiente=2;
			}
		}else if (idTarea==13){
			if (idAccion==17){
				idPerfilSiguiente=3;
			}
		}else if (idTarea==14){
			if (idAccion==9){
				idPerfilSiguiente=8;
			}
		}else if (idTarea==15){
			if (idAccion==22){
				idPerfilSiguiente=10;
			}else if (idAccion==19){
				idPerfilSiguiente=2;
			}
		}else if (idTarea==16){
			if (idAccion==3){
				idPerfilSiguiente=10;
			}else if (idAccion==15){
				idPerfilSiguiente=10;
			}
		}else if (idTarea==17){
			if (idAccion==9){
				idPerfilSiguiente=3;
			}
		}else if (idTarea==18){
			if (idAccion==14){
				idPerfilSiguiente=8;
			}
		}else if (idTarea==19){
			if (idAccion==4){
				idPerfilSiguiente=10;
			}else if (idAccion==5){
				idPerfilSiguiente=6;
			}else if (idAccion==10){
				idPerfilSiguiente=6;
			}else if (idAccion==19){
				idPerfilSiguiente=2;
			}
		}else if (idTarea==20){
			if (idAccion==20){
				idPerfilSiguiente=5;
			}else if (idAccion==19){
				idPerfilSiguiente=2;
			}
		}else if (idTarea==26){
			if (idAccion==13){
				idPerfilSiguiente=10;
			}
		}else if (idTarea==27){
			idPerfilSiguiente=0;
		}
		
		if (idPerfilSiguiente==0){
			perfilSiguiente="";
		}else{
			Perfil perfilAux = new Perfil();
			perfilAux=perfilBeanLocal.buscarPorId(idPerfilSiguiente);
			perfilSiguiente=perfilAux.getDescripcion();
		}
		return perfilSiguiente;
	}
	
	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	

	public String getStrCuerpo() {
		return strCuerpo;
	}
	public void setStrCuerpo(String strCuerpo) {
		this.strCuerpo = strCuerpo;
	}
	public long getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(long idPerfil) {
		this.idPerfil = idPerfil;
	}

	public long getIdResponsable() {
		return idResponsable;
	}

	public void setIdResponsable(long idResponsable) {
		this.idResponsable = idResponsable;
	}	
	
}
