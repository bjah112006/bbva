package com.ibm.bbva.cm.daemon;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimerTask;

import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.bean.Documento;
import com.ibm.bbva.cm.service.ContentService;
import com.ibm.bbva.cm.service.impl.ContentServiceImpl;
import com.ibm.bbva.cm.support.FileFTPService;
import com.ibm.bbva.cm.util.Constantes;
import com.ibm.bbva.cm.util.Util;
import com.ibm.bbva.convert.ConvertirArchivos;
import com.ibm.bbva.ctacte.servicio.cm.CEContentManagerServicePortProxy;
import com.ibm.bbva.ctacte.servicio.cm.DocumentoCM;
import com.ibm.bbva.ctacte.servicio.cm.DocumentoExpediente;
import com.ibm.bbva.service.business.IbmBbvaBusinessPortProxy;

public class DaemonTask extends TimerTask{
	private static final Logger logger = LoggerFactory.getLogger(DaemonTask.class);
	private ContentService service;

	@Override
	public void run() {
//		logger.info("Verificando si hay archivos por trasladar al CM..."+new Date());
//		FTPFile[] files = FileFTPService.listarArchivosEnRepo();
//		service = new ContentServiceImpl();
//		//Inicio IF Existen files FTP
//		if(files != null && files.length > 0){
//			logger.info("Numero de archivos encontrados: "+files.length);
//			for(FTPFile f : files){
//				logger.info("Archivo: "+f.getName());
//				boolean success=false;
//				try {
//					String[] partes = getPartesNomArchivo(f.getName());
//					
//					logger.info("*****************try-RUN*************");
//					if (f.getName().indexOf("unavailable") >= 0) {
//						logger.info("Archivo "+f.getName()+" está siendo usado por otro hilo.");
//					//Inicio IF Para Tarjeta de Credito
//					} else if(partes.length==5 && partes[3].trim().equals("available") && partes[4].trim().equals("UN")){
//						String fromName = f.getName();
//						String toName = f.getName().replaceAll("available", "unavailable");
//						boolean resultadoRename = FileFTPService.renombrarFTPFile(fromName, toName);
//						if (!resultadoRename) {
//							logger.warn("No se pudo renombrar el archivo "+fromName+" a "+toName+". Debe estar siendo usado por otro hilo.");
//							continue;
//						} else {
//							f.setName(toName);
//							logger.info("Se renombró el archivo a "+f.getName());
//						}
//						
//						Date ini = new Date();
//						Calendar fechaCreacion = Calendar.getInstance();
//						fechaCreacion.setTime(ini);
//						Documento doc = new Documento();
//						logger.info("Creando documento : "+partes[1]+" - "+partes[2]+" - "+partes[0]);
//						logger.debug("Creando documento : "+partes[1]+" - "+partes[2]+" - "+partes[0]);
//						com.ibm.bbva.service.business.DocumentoCM docVO = consultarDocumentoExpTC(Long.parseLong(partes[2]), partes[1]);
//						if(docVO!=null){
//							logger.info("*****************docVO.getCodCliente() *************"+docVO.getCodCliente());
//							logger.info("docVO.getId() : "+docVO.getId());
//							logger.info("docVO.getNroDocumento() : "+docVO.getNumDocumento());
//							logger.debug("docVO.getCodCliente() : "+docVO.getCodCliente());
//							logger.debug("docVO.getNroDocumento() : "+docVO.getNumDocumento());
//							logger.debug("docVO.getTipoDoi() : "+docVO.getTipoDoi());
//							logger.debug("docVO.getFechaExpiracion() : "+docVO.getFechaExpiracion());
//							logger.debug("docVO.getMandatorio() : "+docVO.getMandatorio());
//							
//							logger.info("!docVO.getCodCliente() : "+docVO.getCodCliente());
//							if (doc.getCodCliente() == null) {
//								doc.setCodCliente("");
//							} else {
//								doc.setCodCliente(docVO.getCodCliente());
//							}
//							doc.setTipo(partes[1]);
//							doc.setNumDoi(docVO.getNumDocumento());
//							doc.setTipoDoi(docVO.getTipoDoi());
//							doc.setId(new Integer((int)docVO.getId()));
//							//doc.setId(new Integer(partes[0]));
//							if (docVO.getFechaExpiracion() == null) {
//								doc.setFechaExpiracion(null);
//							} else {
//								doc.setFechaExpiracion(docVO.getFechaExpiracion().toGregorianCalendar());
//							}
//							doc.setMandatorio(Util.nullStringAsDefaultBoolean(Util.IntegerCharToString(docVO.getMandatorio())));
//							byte[] bytes = FileFTPService.getContentFTPFile(f.getName());
//							doc.setContenido(bytes);
//							doc.setFechaCreacion(fechaCreacion);
//							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//							doc.setNombreArchivo(sdf.format(fechaCreacion.getTime()));
//							doc.setOrigen("PTC");
//							String strPID = null;
//							if (bytes != null) {
//								strPID = service.insert_PID(doc);
//								logger.info("strPID: " + strPID);
//								if(strPID!=null){
//									success = true;
//									docVO.setIdCm(new BigDecimal(docVO.getId()));
//									docVO.setPidCm(strPID);
//									docVO.setFechaRegistro(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
//									docVO.setFlagCm("1");
//									docVO.setNombreArchivo(sdf.format(fechaCreacion.getTime()));
//									actualizarDocumentoExpTC(docVO);
//									FileFTPService.deleteFTPFile(f.getName());
//								}else{
//									docVO.setIdCm(new BigDecimal(0));
//									docVO.setPidCm("");
//									docVO.setFechaRegistro(null);
//									docVO.setFlagCm("0");
//									docVO.setNombreArchivo("");
//									actualizarDocumentoExpTC(docVO);
//								}
//							}
//						}
//						Date fin = new Date();
//						if(success){
//							logger.debug("Tiempo tomado en grabar el documento : "+partes[1]+" - "+partes[0]+" : "+(fin.getTime()-ini.getTime())+"ms");
//						}else{
//							logger.error("Ocurrio un error al grabar el documento : "+partes[1]+" - "+partes[0]+" el documento no ha sido eliminado de la carpeta FTP, se intentara grabar nuevamente");
//							if (f.getName().indexOf("unavailable") >= 0) {
//								boolean resultado = FileFTPService.renombrarFTPFile(f.getName(), f.getName().replaceAll("unavailable", "available"));
//								if (!resultado) {
//									logger.error("Ocurrió un error inesperado al renombrar el archivo "+f.getName()+". Se tendrá que renombrar manualmente.");
//								}
//							}
//						}	
//					//Fin IF Para Tarjeta de Credito
//					}
//					
//					//********************************************************************************+
//					//+++++++++++++++++++++++INICIO IF PARA CUENTA CORRIENTE+++++++++++++++++++++++++++
//					//********************************************************************************+
//					else if(partes.length>=4 && partes[2].trim().equals("available") && partes[3].trim().equals("CC")){
//						logger.info("***********INICIO CUENTA CORRIENTE**************"+new Date());
//						partes = getPartesNomArchivo_CC(f.getName());
//						String fromName = f.getName();
//						String toName = f.getName().replaceAll("available", "unavailable");
//						boolean resultadoRename = FileFTPService.renombrarFTPFile(fromName, toName);
//						if (!resultadoRename) {
//							logger.warn("No se pudo renombrar el archivo "+fromName+" a "+toName+". Debe estar siendo usado por otro hilo.");
//							continue;
//						} else {
//							f.setName(toName);
//							logger.info("Se renombró el archivo a "+f.getName());
//						}
//						Date ini = new Date();
//						Calendar fechaCreacion = Calendar.getInstance();
//						fechaCreacion.setTime(ini);
//						Documento doc = new Documento();						
//						logger.debug("Creando documento CC : "+partes[1]+" - "+partes[0]);
//						logger.info("Creando documento CC : "+partes[1]+" - "+partes[0]);
//						int idExpediente = Integer.parseInt(partes[0].trim());
//						String codigoDocumento = partes[1].substring(0, partes[1].indexOf("."));
//						//int idDocumento = Integer.parseInt(partes[1].substring(0, partes[1].indexOf(".")));
//						
//						logger.info("docVO.getCodCliente() : "+idExpediente);
//						logger.info("docVO.getNroDocumento() : "+codigoDocumento);
//						
//						logger.info("obtenerDocumentoxCodigo_CC(idExpediente,codigoDocumento)");
//						
//						DocumentoCM docVO = obtenerDocumentoxCodigo_CC(idExpediente,codigoDocumento);
//						logger.info("***************DaemonTask-docVO**********"+docVO);
//						if(docVO!=null){
//							logger.debug("docVO.getCodCliente() : "+docVO.getCodCliente());
//							logger.debug("docVO.getNroDocumento() : "+docVO.getNroDocumento());
//							logger.debug("docVO.getTipoDoi() : "+docVO.getTipoDoi());
//							logger.debug("docVO.getTipo() : "+partes[1]);
//							logger.debug("docVO.getId() : "+docVO.getId());
//							logger.debug("docVO.getFechaExpiracion() : "+docVO.getFechaExpiracion());
//							logger.debug("docVO.getMandatorio() : "+docVO.getMandatorio());
//							
//							logger.info("docVO.getCodCliente() : "+docVO.getCodCliente());
//							logger.info("docVO.getNroDocumento() : "+docVO.getNroDocumento());
//							logger.info("docVO.getTipoDoi() : "+docVO.getTipoDoi());
//							
//							logger.info("docVO.getTipo() : "+partes[1]);
//							logger.info("docVO.getId() : "+docVO.getId());
//							logger.info("docVO.getFechaExpiracion() : "+docVO.getFechaExpiracion());
//							
//							if(!docVO.getCodCliente().equals("-1")){
//								doc.setCodCliente(docVO.getCodCliente());
//								doc.setTipo(partes[1]);
//								doc.setNumDoi(docVO.getNroDocumento());
//								doc.setTipoDoi(docVO.getTipoDoi());
//								doc.setId(docVO.getId().intValue());
//								if (docVO.getFechaExpiracion() != null) {
//									doc.setFechaExpiracion(docVO.getFechaExpiracion().toGregorianCalendar());
//								} else {
//									doc.setFechaExpiracion(null);
//								}
//								doc.setMandatorio(Util.nullIntegerAsDefaultBoolean(docVO.getMandatorio()));
//								byte[] bytes = FileFTPService.getContentFTPFile(f.getName());
//								doc.setContenido(bytes);
//								doc.setOrigen(Constantes.ORIGEN_ARCHIVO_CC);
//								//20120715180000
//
//								doc.setFechaCreacion(fechaCreacion);
//								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//								doc.setNombreArchivo(sdf.format(fechaCreacion.getTime()));
//								
//															
//								logger.debug("doc.getNombreArchivo() : "+doc.getNombreArchivo());
//								logger.debug("partes.length : "+partes.length);
//								
//								logger.info("doc.getNombreArchivo() : "+doc.getNombreArchivo());
//								logger.info("partes.length : "+partes.length);
//								for (int i=0; i<partes.length; i++) {
//									logger.debug(i+" : "+partes[i]);
//								}
//								boolean flagHistorico = false;
//								if (partes.length==6 || partes.length==7){ 
//									if(partes[4].trim().equals("HIST")){
//										doc.setNombreArchivo(partes[5]);
//										logger.info("****INICIO - Historico**** : "+partes[5]);
//										Calendar c = Calendar.getInstance();
//										c.setTime(sdf.parse(partes[5]));										
//										doc.setFechaCreacion(c);
//										logger.info("*******FIN - Historico****");	
//										flagHistorico = true;
//									}
//								}
//								logger.info("flagHistorico : "+flagHistorico);
//								
//								boolean flagVisorAvanzado = false;
//								boolean flagPdfAnotado = false;
//								if (partes.length==5){ 
//									if(partes[4].trim().equals("VA")){
//										String strTipo = doc.getTipo();
//										logger.info("strTipo: " + strTipo);
//										if (strTipo.indexOf(".")>0){
//											if (strTipo.toUpperCase().indexOf("PDF")>0){
//												logger.info("f.getName(): " + f.getName());
//												Integer intTIF = generarArchivoTIFF(f);
//												logger.info("intTIF: " + intTIF);
//											}
//											if (strTipo.toUpperCase().indexOf("TIF")>0){
//												flagVisorAvanzado = true;
//											}
//										}
//									}
//									if(partes[4].trim().equals("AN")){
//										flagPdfAnotado = true;
//									}
//								}
//								logger.info("flagVisorAvanzado: " + flagVisorAvanzado);
//								
//								if (bytes != null) {
//									/****Consultar la tabla Doc Exp para saber si ya se adjunto un documento anteriormente y proceder a eliminar la version anterior****/
//									Integer idCM = 0;
//									Integer idCMCopia = 0;
//									DocumentoExpediente documentoExpediente = obtenerDocumentoExpediente_CC(idExpediente, codigoDocumento);
//									logger.info("documentoExpediente: " + documentoExpediente);
//									if (documentoExpediente!=null){
//										logger.info("documentoExpediente.getIdCm(): " + documentoExpediente.getIdCm());
//										logger.info("documentoExpediente.getIdCmCopia(): " + documentoExpediente.getIdCmCopia());
//										if (documentoExpediente.getIdCm()!=null)
//											idCM = documentoExpediente.getIdCm();
//										if (documentoExpediente.getIdCmCopia()!=null)
//											idCMCopia = documentoExpediente.getIdCmCopia();
//									}
//									Documento docDelete = new Documento();
//									Documento docUpdate = new Documento();
//									
//									if (!flagPdfAnotado) {
//										if (idCM!=0 && !flagHistorico){
//											if (!flagVisorAvanzado) {
//												docDelete.setId(idCM);
//												service.delete(docDelete);
//											}
//										}
//										if (idCMCopia!=0 && !flagHistorico){
//											if (flagVisorAvanzado) {
//												docDelete.setId(idCMCopia);
//												service.delete(docDelete);
//											}
//										}
//									} else {
//										docUpdate.setId(idCM);
//										docUpdate.setContenido(bytes);
//										docUpdate.setOrigen(Constantes.ORIGEN_ARCHIVO_CC);
//									}
//									
//									logger.info("1-doc.getId(): " + doc.getId());
//
//									/****Se procede a Guardar el documento en Content Manager***/
//									if (!flagPdfAnotado) {
//										String strPID = service.insert_PID(doc);
//										logger.info("strPID: " + strPID);
//										if (strPID!=null) {
//											success = true;
//											logger.debug("flagHistorico : "+flagHistorico);
//											if (!flagHistorico){
//												logger.debug("flagVisorAvanzado : "+flagVisorAvanzado);
//												if (!flagVisorAvanzado)
//													actualizarEstadoDocumentoxCodigo_CC(idExpediente, codigoDocumento,"1",doc.getId(),strPID);
//												else
//													actualizarDocumentoExp_IdCMCopia(idExpediente, codigoDocumento,doc.getId(),strPID);
//											}
//											FileFTPService.deleteFTPFile(f.getName());
//										} else {	
//											logger.debug("flagHistorico : "+flagHistorico);
//											if (!flagHistorico){
//												actualizarEstadoDocumentoxCodigo_CC(idExpediente, codigoDocumento,"0",doc.getId(),"");
//											}
//										}
//									} else {
//										String strPID = service.update(docUpdate);
//										if (!strPID.equals("KO")) {
//											success = true;
//											FileFTPService.deleteFTPFile(f.getName());
//										}
//									}
//								}
//							}else{
//								Documento d = new Documento();
//								logger.debug("docVO.getId().intValue() : "+docVO.getId().intValue());
//								d.setId(docVO.getId().intValue());
//								logger.info("service.find(d)---docVO.getId().intValue()"+docVO.getId().intValue());
//								Documento docu =service.find(d);
//								logger.debug("f.getName() : "+f.getName());
//								byte[] bytes = FileFTPService.getContentFTPFile(f.getName());
//								docu.setContenido(bytes);
//								if (bytes != null) {
//									if(success=service.update(docu).equals("OK")){
//										FileFTPService.deleteFTPFile(f.getName());
//									}
//								}
//							}
//						}
//						Date fin = new Date();
//						if(success){
//							logger.debug("Tiempo tomado en grabar el documento : "+partes[1]+" - "+partes[0]+" : "+(fin.getTime()-ini.getTime())+"ms");
//						}else{
//							logger.error("Ocurrio un error al grabar el documento : "+partes[1]+" - "+partes[0]+" el documento no ha sido eliminado de la carpeta FTP, se intentara grabar nuevamente");
//							if (f.getName().indexOf("unavailable") >= 0) {
//								boolean resultado = FileFTPService.renombrarFTPFile(f.getName(), f.getName().replaceAll("unavailable", "available"));
//								if (!resultado) {
//									logger.error("Ocurrió un error inesperado al renombrar el archivo "+f.getName()+". Se tendrá que renombrar manualmente.");
//								}
//							}
//						}	
//						
//					}
//					//Fin IF Para Cuenta Corriente
//				} catch (Exception e) {
//					logger.error("Error", e);
//					if (f.getName().indexOf("unavailable") >= 0) {
//						boolean resultado = FileFTPService.renombrarFTPFile(f.getName(), f.getName().replaceAll("unavailable", "available"));
//						if (!resultado) {
//							logger.error("Ocurrió un error inesperado al renombrar el archivo "+f.getName()+". Se tendrá que renombrar manualmente.");
//						}
//					}
//				}
//				logger.debug("************************************");
//			}
//		}
//		else{
//			logger.info("No se encontraron archivos para trasladar");
//		}
		//Fin IF Existen files FTP
	}
	
	
	private static String EnvioDocumento_CC(){
		String strEnvioDocumento_CC = "OK";
		
		return strEnvioDocumento_CC;
	}
	
	private static String EnvioDocumento_TC(){
		String strEnvioDocumento_TC = "OK";
		
		return strEnvioDocumento_TC;
	}
			
	public static Calendar getCalendar(String str_date){
		DateFormat formatter ; 
		Date date = null ; 
		if(str_date!=null){
			formatter = new SimpleDateFormat(FileFTPService.leerPropiedad(Constantes.DATE_FORMAT));
			try {
				date = (Date)formatter.parse(str_date);
			} catch (ParseException e) {
				date = new Date();
				logger.error("Error al parsear fecha", e);
			}
		}else{
			date = new Date();
		}
		
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static String[] getPartesNomArchivo(String nomFileFTP){
		String[] partes = nomFileFTP.split("-");
		partes[1] = partes[1].substring(0, partes[1].indexOf("."));
		return partes;
	}
	
	public static String[] getPartesNomArchivo_CC(String nomFileFTP){
		String[] partes = nomFileFTP.split("-");
		//partes[1] = partes[1].substring(0, partes[1].indexOf("."));
		return partes;
	}
	
	private static com.ibm.bbva.service.business.DocumentoCM consultarDocumentoExpTC(Long idDocExp, String codigoTipoDoc) {
		IbmBbvaBusinessPortProxy servicio = new IbmBbvaBusinessPortProxy();
		servicio._getDescriptor().setEndpoint(FileFTPService.leerPropiedad(Constantes.WS_PATH_PROPERTY_NAME));
		com.ibm.bbva.service.business.DocumentoCM doc = servicio.consultarDocumentoExpediente(idDocExp, codigoTipoDoc);
		return doc;
	}
	
	private static boolean actualizarDocumentoExpTC(com.ibm.bbva.service.business.DocumentoCM objDocumentoCM) {
		IbmBbvaBusinessPortProxy servicio = new IbmBbvaBusinessPortProxy();
		servicio._getDescriptor().setEndpoint(FileFTPService.leerPropiedad(Constantes.WS_PATH_PROPERTY_NAME));
		return servicio.actualizarDocumentoExpTC(objDocumentoCM);
	}	
	
	private static DocumentoCM obtenerDocumento_CC(int idexpediente, int idDocumento) {
		DocumentoCM doc = null;
		try { // This code block invokes the ServiceIBMBBVA:crearDocumento operation on web service
			logger.info("***************TRY-DaemonTask-DocumentoCM -obtenerDocumento_CC**********");
			CEContentManagerServicePortProxy servicio = new CEContentManagerServicePortProxy();
			servicio._getDescriptor().setEndpoint(FileFTPService.leerPropiedad(Constantes.WS_PATH_PROPERTY_NAME_CC));
			doc = servicio.obtenerDocumentoCM(idexpediente, idDocumento);
		} catch (Exception ex) {
			logger.error("***************CATCH-Exception-DaemonTask-obtenerDocumento_CC**********", ex);
		}

		return doc;
	}
	
	private static DocumentoCM obtenerDocumentoxCodigo_CC(int idexpediente, String codigoDocumento) {
		DocumentoCM doc = null;
		try { // This code block invokes the ServiceIBMBBVA:crearDocumento operation on web service
			logger.info("***************TRY-DaemonTask-DocumentoCM obtenerDocumentoxCodigo_CC**********");
			CEContentManagerServicePortProxy servicio = new CEContentManagerServicePortProxy();
			servicio._getDescriptor().setEndpoint(FileFTPService.leerPropiedad(Constantes.WS_PATH_PROPERTY_NAME_CC));
			doc = servicio.obtenerDocumentoCMxCodigo(idexpediente, codigoDocumento);
		} catch (Exception ex) {
			logger.error("Error obtenerDocumentoxCodigo_CC", ex);
		}

		return doc;
	}
	
	private static String actualizarEstadoDocumento_CC(int idExpediente, int idDocumento, String flagCM, int idCM, String pidCM){
		String result = "OK";
		try { // This code block invokes the ServiceIBMBBVA:crearDocumento operation on web service
			CEContentManagerServicePortProxy servicio = new CEContentManagerServicePortProxy();
			servicio._getDescriptor().setEndpoint(FileFTPService.leerPropiedad(Constantes.WS_PATH_PROPERTY_NAME_CC));
			result = servicio.actualizarEstadoDocumentoCC(idExpediente, idDocumento, flagCM, idCM, pidCM);
		} catch (Exception ex) {
			result = "KO";
			logger.error("Error actualizarEstadoDocumento_CC", ex);
		} 
		
		return result;
	}
	
	private static String actualizarEstadoDocumentoxCodigo_CC(int idExpediente, String codigoDocumento, String flagCM, int idCM, String pidCM){
		String result = "OK";
		try { // This code block invokes the ServiceIBMBBVA:crearDocumento operation on web service
			logger.info("actualizarEstadoDocumentoxCodigo_CC("+idExpediente+","+codigoDocumento+","+flagCM+","+idCM+","+pidCM+")");
			CEContentManagerServicePortProxy servicio = new CEContentManagerServicePortProxy();
			servicio._getDescriptor().setEndpoint(FileFTPService.leerPropiedad(Constantes.WS_PATH_PROPERTY_NAME_CC));
			result = servicio.actualizarEstadoDocumentoxCodigoCC(idExpediente, codigoDocumento, flagCM, idCM, pidCM);			
		} catch (Exception ex) {
			result = "KO";
			logger.error("Error actualizarEstadoDocumentoxCodigo_CC", ex);
		} 
		
		return result;
	}
	
	public String actualizarDocumentoExp_IdCMCopia (int idExpediente, String codigoDocumento, int idCMCopia, String pidCM){
		String result = "OK";
		try { // This code block invokes the ServiceIBMBBVA:crearDocumento operation on web service
			CEContentManagerServicePortProxy servicio = new CEContentManagerServicePortProxy();
			servicio._getDescriptor().setEndpoint(FileFTPService.leerPropiedad(Constantes.WS_PATH_PROPERTY_NAME_CC));
			result = servicio.actualizarDocumentoExpIdCMCopia(idExpediente, codigoDocumento, idCMCopia, pidCM);			
		} catch (Exception ex) {
			result = "KO";
			logger.error("Error actualizarDocumentoExp_IdCMCopia", ex);
		} 
		
		return result;
	}
	
	private static Integer obtenerDocumento_CC(int idExpediente, String codigoDocumento){
		Integer idCM;
		try { // This code block invokes the ServiceIBMBBVA:crearDocumento operation on web service
			logger.info("***************DaemonTask-Integer-obtenerDocumento_CC**********");
			CEContentManagerServicePortProxy servicio = new CEContentManagerServicePortProxy();
			servicio._getDescriptor().setEndpoint(FileFTPService.leerPropiedad(Constantes.WS_PATH_PROPERTY_NAME_CC));
			idCM = servicio.obtenerDocumentoxCodigoCC(idExpediente, codigoDocumento);			
		} catch (Exception ex) {	
			logger.error("***************CATCH-Exception-DaemonTask-Integer-obtenerDocumento_CC**********");
			logger.error("***************CATCH-Exception-DaemonTask-idExpediente**********"+idExpediente);
			logger.error("***************CATCH-Exception-DaemonTask-codigoDocumento**********"+codigoDocumento);
			idCM = 0;
			logger.error("Error obtenerDocumento_CC", ex);
		} 		
		return idCM;
	}	
	
	public static DocumentoExpediente obtenerDocumentoExpediente_CC (int idExpediente, String codigoDocumento){
		DocumentoExpediente documentoExpediente = new DocumentoExpediente();
		try { // This code block invokes the ServiceIBMBBVA:crearDocumento operation on web service
			logger.info("***************DocumentoExpediente obtenerDocumentoExpediente_CC**********");
			CEContentManagerServicePortProxy servicio = new CEContentManagerServicePortProxy();
			servicio._getDescriptor().setEndpoint(FileFTPService.leerPropiedad(Constantes.WS_PATH_PROPERTY_NAME_CC));
			documentoExpediente = servicio.obtenerDocumentoExpedienteCC(idExpediente, codigoDocumento);			
		} catch (Exception ex) {
			logger.error("***************CATCH-Exception-DaemonTask-Integer-DocumentoExpediente obtenerDocumentoExpediente_CC**********");
			logger.error("***************CATCH-Exception-DaemonTask-idExpediente**********"+idExpediente);
			logger.error("***************CATCH-Exception-DaemonTask-codigoDocumento**********"+codigoDocumento);
			documentoExpediente = null;
			logger.error("Error obtenerDocumentoExpediente_CC", ex);
		} 		
		return documentoExpediente;
	}
	
	private static Integer generarArchivoTIFF(FTPFile f){
		Integer idCM = 0;
		try { // This code block invokes the ServiceIBMBBVA:crearDocumento operation on web service
//			CEContentManagerServiceService servicio = new CEContentManagerServiceServiceLocator();
//			CEContentManagerService ceContentManagerService = servicio.getCEContentManagerService(new java.net.URL(FileFTPService.leerPropiedad(Constantes.WS_PATH_PROPERTY_NAME_CC)));
//			ceContentManagerService.crearArchivoTif(nomArchivo);
			
			String outArchFTP = f.getName().replace(".pdf", ".tif").replaceAll("unavailable", "available");
			
			/*Recuperar de FTP*/
			byte[] data = FileFTPService.getContentFTPFile(f.getName());
			
			/*Convertir de pdf a tiff*/
			if (data != null) {
				ConvertirArchivos convertirArchivos = new ConvertirArchivos();
				byte[] bytes = convertirArchivos.convertirTIFF(data);
				ByteArrayInputStream fin = new ByteArrayInputStream(bytes);			
				boolean resultado = FileFTPService.storeFTPFile(outArchFTP, fin);
				idCM = (resultado ? 1 : 0);
			} else {
				idCM = 0;
				logger.error("No se pudo leer el archivo para generar el archivo TIFF");
			}
		} catch (Exception ex) {
			idCM = 0;
			logger.error("Error generarArchivoTIF()", ex);
		} 		
		return idCM;
	}
}
