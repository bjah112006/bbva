package com.ibm.bbva.ctacte.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Cuenta;
import com.ibm.bbva.ctacte.bean.Documento;
import com.ibm.bbva.ctacte.bean.DocumentoCM;
import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteCuenta;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.GuiaDocument;
import com.ibm.bbva.ctacte.bean.Historial;
import com.ibm.bbva.ctacte.bean.HorarioCC;
import com.ibm.bbva.ctacte.bean.Participe;
import com.ibm.bbva.ctacte.bean.RangoCC;
import com.ibm.bbva.ctacte.bean.servicio.core.ClientePJCore;
import com.ibm.bbva.ctacte.bean.servicio.core.CuentaCore;
import com.ibm.bbva.ctacte.bean.servicio.core.ParticipeCore;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.dao.CuentaDAO;
import com.ibm.bbva.ctacte.dao.DocumentoDAO;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteCuentaDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.HistorialDAO;
import com.ibm.bbva.ctacte.dao.HorarioCCDAO;
import com.ibm.bbva.ctacte.dao.RangoCCDAO;
import com.ibm.bbva.ctacte.vo.HorarioVO;

public class Util {
	
	
	
	private static final Logger LOG = LoggerFactory.getLogger(Util.class);
	
	private static final String PATRON_EMAIL = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * @return Retorna una lista vacia
	 */
	public static List<SelectItem> listaVacia () {
		ArrayList<SelectItem> items = new ArrayList<SelectItem>(1);
		SelectItem selectItem = new SelectItem();
        selectItem.setValue(ConstantesAdmin.CODIGO_CAMPO_VACIO);
        selectItem.setLabel("");
        items.add(selectItem);
        return items;
    }
	
	/**
     * Crea una lista con elementos SelectItem a partir de una lista
     * @param lista Lista del cual se va obtener los datos
     * @param propValor Es la propiedad que se asignara a SelectItem.value
     * @param propNombre Es la propiedad que se agisnara a SelectItem.label
     * @return Lista con elementos de tipo SelectItem
     */
    public static List<SelectItem> crearItems (List<?> lista, String propValor, String propNombre) {
        return crearItems(lista, false, propValor, propNombre);
    }
    
    /**
     * Crea una lista con elementos SelectItem a partir de una lista
     * @param lista Lista del cual se va obtener los datos
     * @param propValor Es la propiedad que se asignara a SelectItem.value
     * @param propNombre Es la propiedad que se agisnara a SelectItem.label
     * @param habilitado Si los items son habilitados
     * @return Lista con elementos de tipo SelectItem
     */
    public static List<SelectItem> crearItems (List<?> lista, String propValor, String propNombre, boolean habilitado) {
        return crearItems(lista, false, propValor, propNombre, habilitado);
    }
    
    /**
     * Crea una lista con elementos SelectItem a partir de una lista
     * @param lista Lista del cual se va obtener los datos
     * @param blaco Se es true muestra un item en blanco al inicio con id=null
     * @param propValor Es la propiedad que se asignara a SelectItem.value
     * @param propNombre Es la propiedad que se agisnara a SelectItem.label
     * @return Lista con elementos de tipo SelectItem
     */
    public static List<SelectItem> crearItems (List<?> lista, boolean blaco, String propValor, String propNombre) {
    	return crearItems(lista, blaco, propValor, propNombre, true);
    }
    
    public static SelectItem crearItem (String valor, String texto) {
    	SelectItem selectItem = new SelectItem();
		selectItem.setValue(valor);
        selectItem.setLabel(texto);
        return selectItem;
    }
    
    /**
     * Crea una lista con elementos SelectItem a partir de una lista
     * @param lista Lista del cual se va obtener los datos
     * @param blanco Se es true muestra un item en blanco al inicio con id=null
     * @param propValor Es la propiedad que se asignara a SelectItem.value
     * @param propNombre Es la propiedad que se agisnara a SelectItem.label
     * @param habilitado Si los items son habilitados
     * @return Lista con elementos de tipo SelectItem
     */
    public static List<SelectItem> crearItems (List<?> lista, boolean blanco, String propValor, String propNombre, boolean habilitado) {
        ArrayList<SelectItem> items = new ArrayList<SelectItem> ();
        agregarBlanco(blanco, items);
        try {
        	if (lista!= null && !lista.isEmpty()) {
        		PropertyUtilsBean propBean = new PropertyUtilsBean ();
        		for (Object obj : lista) {
	        		Object objVal = propBean.getProperty(obj, propValor);
	        		Object objNom = propBean.getProperty(obj, propNombre);
	        		SelectItem selectItem = new SelectItem();
	        		selectItem.setValue(objVal.toString());
	                selectItem.setLabel(objNom.toString());
	                if (!habilitado) {
	                	selectItem.setDisabled(true);
	                }
	                items.add(selectItem);
        		}
        	}
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return items;
    }
    
    /**
     * Crea una lista de select items a partir de un arreglo
     * de Strings
     * 
     * @param lista
     * @param blanco
     * @param propValor
     * @param propNombre
     * @param habilitado
     * @return
     */
    public static List<SelectItem> crearSimpleItems (List<String> lista, boolean blanco, boolean habilitado) {
        ArrayList<SelectItem> items = new ArrayList<SelectItem> ();
        agregarBlanco(blanco, items);
        try {
        	if (lista!= null && !lista.isEmpty()) {
        		for (String str : lista) {
	        		SelectItem selectItem = new SelectItem();
	        		selectItem.setValue(str);
	                selectItem.setLabel(str);
	                if (!habilitado) {
	                	selectItem.setDisabled(true);
	                }
	                items.add(selectItem);
        		}
        	}
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return items;
    }
    
    public static List<SelectItem> crearItems (List<?> lista, boolean blanco, String propValor, String propNombre, String formato, boolean habilitado) {
        ArrayList<SelectItem> items = new ArrayList<SelectItem> ();
        agregarBlanco(blanco, items);
        try {
        	if (lista!= null && !lista.isEmpty()) {
        		PropertyUtilsBean propBean = new PropertyUtilsBean ();
        		for (Object obj : lista) {
	        		Object objVal = propBean.getProperty(obj, propValor);
	        		StringTokenizer st = new StringTokenizer (propNombre, ",");
	        		Object[] objs = new Object[st.countTokens()];
	        		int cont = 0;
	        		while (st.hasMoreTokens()) {
	        			objs[cont++] = propBean.getProperty(obj, st.nextToken().trim());
	        		}
	        		String nombre = MessageFormat.format(formato, objs);
	        		SelectItem selectItem = new SelectItem();
	        		selectItem.setValue(objVal.toString());
	                selectItem.setLabel(nombre);
	                if (!habilitado) {
	                	selectItem.setDisabled(true);
	                }
	                items.add(selectItem);
        		}
        	} else {
        		agregarBlanco(!blanco, items);
        	}
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return items;
    }

	private static void agregarBlanco(boolean blanco,
			ArrayList<SelectItem> items) {
		if (blanco) {
        	SelectItem selectItem = new SelectItem();
            selectItem.setValue(ConstantesAdmin.CODIGO_CAMPO_VACIO);
            selectItem.setLabel("--seleccione--");
            items.add(selectItem);
        }
	}
    
    public static double convertStringToDouble(String nombreCampo) {
    	return Double.parseDouble(nombreCampo.replace(",", ""));
    }
    
    public static String convertDoubleToString(double nombreCampo, String pattern) {
		NumberFormat formatter = new DecimalFormat();
		formatter = new DecimalFormat(pattern);
		return formatter.format(nombreCampo);
    }
    
    public static boolean esNuloVacio(String nombreCampo) {
    	return nombreCampo==null || nombreCampo.trim().equals("");      	
    }

	public static class SecletItemComparator implements Comparator<SelectItem> {
		
		boolean order = false;

		/**
		 * @param Ordena una Lista true = Desc, false = Asc
		 */
		public SecletItemComparator(boolean order) {		
			this.order = order;
		}
		
	    public int compare(SelectItem s1, SelectItem s2) {  
	    	if (order) {
                return s2.getLabel().compareTo(s1.getLabel());                
	    	}else{
	    		return s1.getLabel().compareTo(s2.getLabel());
	    	}
	    }  
	}
	
	public static String obtenerIp() {
		String returnIp = "";		
		HttpServletRequest objeto = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		returnIp = objeto.getRemoteAddr();		
		return returnIp;
	}
	
	public static String validarCampo (String campo) {
		if (campo != null) {
			campo = campo.trim();
			if (campo.equals("")) {
				return null;
			} else {
				return campo;
			}
		}
		return null;
	}
	
	public static Long validarCampoLong (String campo) {
		if (campo != null) {
			campo = campo.trim();
			if (campo.equals("")) {
				return null;
			} else {
				return new Long(campo);
			}
		}
		return null;
	}
	
	public static String validarId (String id) {
		return (id==null || ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(id)) ? null : id;
	}
	
	public static Integer validarIdInteger (String id) {
		return (id==null || ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(id)) ? null : new Integer(id);
	}
	
	public static String obtenerDescripcion (List<SelectItem> lista, String valor) {
		if (valor != null && !ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(valor)) {
			for (SelectItem si : lista) {
				if (valor.equals(si.getValue())) {
					return si.getLabel();
				}
			}
		}
		return null;
	}
	
	/**
	 * Obtiene un objeto del ambito Session
	 * @param nombreMB Nombre del objeto
	 * @return
	 */
	public static Object getObjectSession (String nombreMB) {
		Object objeto = null;
		if (nombreMB != null) {
			objeto = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(nombreMB);
		}
		return objeto;
	}
	
	/**
	 * Alamacena un objeto del ambito Session
	 * @param nombreMB Nombre del objeto
	 * @param objeto Objeto que se almacenara en la sesion
	 */
	public static void addObjectSession (String nombreMB, Object objeto) {
		//+POR SOLICITUD BBVA+LOG.info("addObjectSession (String nombreMB, Object objeto)");
		//+POR SOLICITUD BBVA+LOG.info("nombreMB : "+nombreMB + "  --  Object : " + objeto);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(nombreMB, objeto);
	}
	
	/**
	 * Elimina un objeto del ambito Session
	 * @param nombreMB Nombre del objeto
	 */
	public static void removeObjectSession (String nombreMB) {
		FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().remove(nombreMB);
	}

	/**
	 * Obtiene un objeto del ambito Request
	 * @param nombreMB Nombre del objeto
	 * @return
	 */
	public static Object getObjectRequest (String nombreMB) {
		Object objeto = null;
		if (nombreMB != null) {
			objeto = FacesContext.getCurrentInstance().getExternalContext()
            		.getRequestMap().get(nombreMB);
		}
		return objeto;
	}
	
	/**
	 * Asigna un objeto al ambito Request
	 * @param nombreMB Nombre del objeto 
	 * @param objeto Objeto que se agregara a la sesion
	 */
	public static void addObjectRequest (String nombreMB, Object objeto) {
		FacesContext.getCurrentInstance().getExternalContext()
        		.getRequestMap().put(nombreMB, objeto);
	}
	
	/**
	 * @return Retorna el Objeto ExternalContext del FacesContext
	 */
	public static ExternalContext getExternalContext () {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
	
	// TODO ver si es necesario usarlo
	public static Blob stringToBlob (String texto) {
		/*Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		LobCreator lobCreator = Hibernate.getLobCreator(session);
		Blob blob = lobCreator.createBlob(texto.getBytes());
		transaction.commit();
		return blob;*/
		return null;
	}
	
	public static String blobToString (Blob blob) {
		StringBuilder builder = new StringBuilder ();
		try {
			InputStreamReader isr = new InputStreamReader (blob.getBinaryStream());
			BufferedReader br = new BufferedReader(isr);
			String txt = null;
			while ((txt=br.readLine()) !=null) {
				builder.append(txt);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return builder.toString();
	}
	public static Date parseStringToDate(String sfecha, String formato){
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat(formato);
		String strFecha = sfecha;
		Date fecha = null;
		try {

			fecha = formatoDelTexto.parse(strFecha);

		} catch (ParseException ex) {

			LOG.error(ex.getMessage(), ex);

		}
		return fecha;
	}
	
	public static void generarRegistroHistExp(Expediente expediente) {
		Empleado empleado = (Empleado) getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		generarRegistroHistExp(expediente, empleado);
	}
	
	public static void generarRegistroHistExp(Expediente expediente, Empleado empleado){
		expediente.setFechaFin(new Date());
		HistorialDAO historialDAO = EJBLocator.getHistorialDAO();
		LOG.info("********generarRegistroHistExp(Expediente expediente)***********");
		for (ExpedienteTarea expedienteTarea : expediente.getExpedienteTareas()) {		
			Historial historial = crearHistorial(expedienteTarea, empleado);
			historialDAO.save(historial);
			if (expedienteTarea.getTarea().getId() == ConstantesBusiness.ID_TAREA_PRE_REGISTRO) {
				break;
			}
		}
	}
	
	public static void generarRegistroHistExpCompletado(Expediente expediente, Integer idTarea, Empleado empleado){
		expediente.setFechaFin(new Date());
		HistorialDAO historialDAO = EJBLocator.getHistorialDAO();
		for (ExpedienteTarea expedienteTarea : expediente.getExpedienteTareas()) {
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("************************expedienteTarea.getTarea().getId(): " +expedienteTarea.getTarea().getId());
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("************************idTarea: " +idTarea);
			if (expedienteTarea.getTarea().getId() ==idTarea) {
				Historial historial = crearHistorial(expedienteTarea, empleado);
				historialDAO.save(historial);
				//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("************************ TAREAS-SIZE*************************************"+expediente.getExpedienteTareas().size());
				//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("************************ SE COMPLETO EL REGISTRO *************************************");
				break;
			}
		}
	}
	
	public static DocumentoExp crearDocumentoExp (GuiaDocument guiaDocument) {
		DocumentoExp documentoExp = new DocumentoExp ();
		Documento documento = guiaDocument.getDocumento();
		documentoExp.setDocumento(documento);
		documentoExp.setFlagObligatorio(guiaDocument.getFlagObligatorio());
		documentoExp.setFlagReqEscaneo(guiaDocument.getFlagReqEscaneo());
		documentoExp.setDocPesoPromKB(documento.getPesoPromHojaKB());
		Integer diasVigencia = documento.getVigenciaDias();
		if (ConstantesBusiness.FLAG_VIGENCIA_DOCUMENTO.equals(documento.getFlagVigencia())
				&& diasVigencia != null) {
			DateTime fechaHoy = new DateTime (new Date());
			Date fechaVigencia = fechaHoy.plusDays(diasVigencia).toDate();
			documentoExp.setFechaVigencia(fechaVigencia);
		}
		return documentoExp;
	}
	
	public static boolean insertarDocumentoExp (String codigo) {
		Expediente expediente = (Expediente) getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		DocumentoDAO documentoDAO = EJBLocator.getDocumentoDAO();
		Documento documento = documentoDAO.findByCodigo(codigo);
		DocumentoExpDAO documentoExpDAO = EJBLocator.getDocumentoExpDAO();
		DocumentoExp documentoExpOrig = documentoExpDAO.findByCodDocExp(codigo, expediente.getId());
		if (documentoExpOrig == null) {
			DocumentoExp documentoExp = new DocumentoExp ();
			documentoExp.setDocumento(documento);
			documentoExp.setExpediente(expediente);
			documentoExp.setDocPesoPromKB(documento.getPesoPromHojaKB());
			documentoExp.setFlagObligatorio(ConstantesAdmin.FLAG_FALSO);
			documentoExpDAO.save(documentoExp);
			LOG.info("Se creó documento {} en guía documentaria.", codigo);
			return true;
		} else if (ConstantesAdmin.FLAG_VERDADERO.equals(documentoExpOrig.getFlagCm())) {
			LOG.warn("Documento {} ya existe en BD y en Content.", codigo);
			return false;
		} else {
			LOG.warn("Documento {} ya existe en BD pero no en Content.", codigo);
			return true;
		}
	}
	
	public static Expediente crearExpedienteCU1 () {
		Expediente expediente = new Expediente ();
		expediente.setEmpleado((Empleado) getObjectSession(ConstantesAdmin.EMPLEADO_SESION));
		expediente.setOficina(expediente.getEmpleado().getOficina());
		expediente.setNumTerminal(Util.obtenerIp());
		return expediente;
	}
	
	public static void copiarExpediente (Expediente fuente, Expediente destino) {
		copiarExpediente (fuente, destino, true);
	}
	
	public static void copiarExpediente (Expediente fuente, Expediente destino, boolean incluirID) {
		if (incluirID) {
			destino.setId(fuente.getId());
		}
		destino.setAccion(fuente.getAccion());
		destino.setCampania(fuente.getCampania());
		destino.setCliente(fuente.getCliente());
		destino.setCodTipoPj(fuente.getCodTipoPj());
		destino.setCuentaCobroComision(fuente.getCuentaCobroComision());
		destino.setDesTipoPj(fuente.getDesTipoPj());
		destino.setDictamenBastanteo(fuente.getDictamenBastanteo());
		destino.setEmpleado(fuente.getEmpleado());
		destino.setEstado(fuente.getEstado());
		destino.setEstadoCuenta(fuente.getEstadoCuenta());
		destino.setExpedienteTareas(fuente.getExpedienteTareas());
		destino.setFechaEnvio(fuente.getFechaEnvio());
		destino.setFechaFin(fuente.getFechaFin());
		destino.setFechaProgramada(fuente.getFechaProgramada());
		destino.setFechaRegistro(fuente.getFechaRegistro());
		destino.setFlagConformidadBastanteo(fuente.getFlagConformidadBastanteo());
		destino.setFlagCorreo(fuente.getFlagCorreo());
		destino.setFlagEjecutoCobroComision(fuente.getFlagEjecutoCobroComision());
		destino.setFlagExoneracionComision(fuente.getFlagExoneracionComision());
		destino.setFlagFacultadesEspeciales(fuente.getFlagFacultadesEspeciales());
		destino.setFlagModificatoria(fuente.getFlagModificatoria());
		destino.setFlagSmstexto(fuente.getFlagSmstexto());
		destino.setInstruccionesBastanteo(fuente.getInstruccionesBastanteo());
		destino.setMotivo(fuente.getMotivo());
		destino.setNumeroCuentaCobro(fuente.getNumeroCuentaCobro());
		destino.setObservaciones(fuente.getObservaciones());
		destino.setOficina(fuente.getOficina());
		destino.setOperacion(fuente.getOperacion());
		destino.setProducto(fuente.getProducto());
		destino.setResultado(fuente.getResultado());
		destino.setResultadoBastanteo(fuente.getResultadoBastanteo());
		destino.setResultadoRevocatoria(fuente.getResultadoRevocatoria());
		destino.setSubproducto(fuente.getSubproducto());
	}

	private static Historial crearHistorial(ExpedienteTarea expedienteTarea, Empleado empleado) {
		Expediente expediente = expedienteTarea.getExpediente();
		Historial historial = new Historial();
		LOG.info("********crearHistorial(ExpedienteTarea expedienteTarea)***********");
		LOG.info("********expediente.getEstado().getDescripcion()***********"+expediente.getEstado().getDescripcion());
		
		historial.setEstado(expediente.getEstado());
		historial.setTarea(expedienteTarea.getTarea());
		historial.setCliente(expediente.getCliente());
		historial.setCampania(expediente.getCampania());
		historial.setCodTipoPj(expediente.getCodTipoPj());
		historial.setCuentaCobroComision(expediente.getCuentaCobroComision());
		historial.setDesTipoPj(expediente.getDesTipoPj());
		historial.setDictamenBastanteo(expediente.getDictamenBastanteo());
		historial.setEmpleado(empleado);
		historial.setEstadoCuenta(expediente.getEstadoCuenta());
		historial.setExpediente(expediente);
		historial.setFechaEnvio(expediente.getFechaEnvio());
		historial.setFechaFin(expediente.getFechaFin());
		historial.setFechaProgramada(expediente.getFechaProgramada());
		historial.setFechaRegistro(new Date());
		historial.setFlagConformidadBastanteo(expediente.getFlagConformidadBastanteo());
		historial.setFlagCorreo(expediente.getFlagCorreo());
		historial.setFlagEjecutoCobroComision(expediente.getFlagEjecutoCobroComision());
		historial.setFlagExoneracionComision(expediente.getFlagExoneracionComision());
		historial.setFlagFacultadesEspeciales(expediente.getFlagFacultadesEspeciales());
		historial.setFlagModificatoria(expediente.getFlagModificatoria());
		historial.setFlagSmstexto(expediente.getFlagSmstexto());
		historial.setInstruccionesBastanteo(expediente.getInstruccionesBastanteo());
		historial.setMotivo(expediente.getMotivo());
		historial.setNumeroCuentaCobro(expediente.getNumeroCuentaCobro());
		historial.setObservaciones(expediente.getObservaciones());
		historial.setObservacionesBastanteo(expediente.getObservacionesBastanteo());
		historial.setOficina(expediente.getOficina());
		historial.setProducto(expediente.getProducto());
		historial.setResultado(expediente.getResultado());
		historial.setResultadoBastanteo(expediente.getResultadoBastanteo());
		historial.setResultadoRevocatoria(expediente.getResultadoRevocatoria());
		historial.setSubproducto(expediente.getSubproducto());
		historial.setOperacion(expediente.getOperacion());
		historial.setFechaFin(new Date());
		historial.setNumTerminal(expediente.getNumTerminal());
//		historial.setAnsReal(obtenerAnsTarea(historial.getFechaEnvio(), historial.getFechaFin(),empleado));
				
		//obtener expediente
		//FECHA_MESA_DOCUMENTO
		ExpedienteDAO expedienteDAO = EJBLocator.getExpedienteDAO();
		Expediente exp = expedienteDAO.load(expediente.getId());
		if(expedienteTarea.getTarea().getId() == ConstantesBusiness.ID_TAREA_VERIFICAR_CALIDAD_DOCUMENTOS){
			if(historial.getResultado()!= null && historial.getResultado().equalsIgnoreCase("obserbado")){
				exp.setFlagObsMesaDocumento(historial.getResultado());
			}
			exp.setUltimaFechaMesaDocumento(historial.getFechaFin());
			//FECHA_MESA_FIRMAS
		}else if (expedienteTarea.getTarea().getId() == ConstantesBusiness.ID_TAREA_VERIFICAR_CALIDAD_FIRMAS){
			if(historial.getResultado()!= null && historial.getResultado().equalsIgnoreCase("obserbado")){
				exp.setFlagObsMesaFirmas(historial.getResultado());
			}
			exp.setUltimaFechaMesaFirmas(historial.getFechaFin());
			//FECHA_ABOGADO_BAST
		}else if (expedienteTarea.getTarea().getId() == ConstantesBusiness.ID_TAREA_VERIFICAR_REALIZAR_BASTANTEO){
			exp.setUltimaFechaRealizarBastanteo(historial.getFechaFin());
			//guardar resultado y dictamen bastanteo de tarea 12
			exp.setResultadoVerRealizarBastanteo(historial.getResultado());
			exp.setDictamenVerRealizarBastanteo(historial.getDictamenBastanteo());
			//FECHA_VERIF_RESUL_TRAMITE
		}else if (expedienteTarea.getTarea().getId() == ConstantesBusiness.ID_TAREA_VERIFICAR_RESULTADO_TRAMITE){
			exp.setUltimaFechaVerifResultTra(historial.getFechaFin());
			
		}
		expedienteDAO.update(exp);
		try {
			if(expedienteTarea.getTarea().getId() == ConstantesBusiness.ID_TAREA_REGISTRAR_EXPEDIENTE){
				historial.setAnsReal(0);
				historial.setAnsEsperado(0);
				historial.setCumpleAns("1");
			}else{
				int ansTiempoReal =  obtenerAnsTienpoReal(historial, empleado);
				LOG.info("ansTiempoReal :",ansTiempoReal);
				
				historial.setAnsReal(ansTiempoReal);
				historial.setAnsEsperado(expedienteTarea.getTarea().getAmarilloMinutos()+expedienteTarea.getTarea().getVerdeMinutos());
				historial.setCumpleAns(historial.getAnsReal()> historial.getAnsEsperado() ? "0" : "1");	
				LOG.info("historial :",historial);
			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return historial;
	}
	
	/**TODO: DEBE IMPLEMENTARCE EN LA VISTA  LA VALIDACION PARA 
	 * QUE LOS RANGOS INSERTADOS NO SE CRICEN ENTRE ELLOS**/
	public static int obtenerAnsTienpoReal(Historial historial,
			Empleado empleado) {
		int ans = 0 ;
		HorarioCCDAO horarioCCDAO = EJBLocator.getHorarioCCDAO();
		RangoCCDAO rangoCCDAO = EJBLocator.getRangoCCDAO();
		HorarioVO horarioVO = new HorarioVO();
		horarioVO.setIdPerfil(empleado.getPerfil().getId());
		horarioVO.setIdOficina(empleado.getOficina().getId());
		try {

			List<HorarioCC> feriados = horarioCCDAO.obtenerFeriadosPorPeriodo(historial.getFechaEnvio(),historial.getFechaFin(), 
					empleado.getPerfil().getId(), empleado.getOficina().getId());

			List<RangoCC> rangos = rangoCCDAO.obtenerRangos(horarioVO);

			List<Date> diasEntreFecha = diasEntreFecha(historial.getFechaEnvio(), historial.getFechaFin());
			
			
			if(diasEntreFecha.size() == 0){
			//TODO: LA TAREA INICIO Y TERMINO EL MISMO DIA.
				List<RangoCC> rangosPorDia = ordenarRangos(historial.getFechaEnvio(),rangos, diasEntreFecha.size(), feriados );
				
				ans = ans + obtenerAnsPorDia(rangosPorDia, historial.getFechaEnvio(), historial.getFechaFin(), historial.getFechaEnvio() );
				
			}else if (diasEntreFecha.size() > 0){
				//TODO: LA TAREA INICIO Y TERMINO EN DIAS DIFERNTES CON UNO O MAS DIAS DE DIFERENCIA.
				for(Date dia : diasEntreFecha){
					List<RangoCC> rangosPorDia = ordenarRangos(dia,rangos, diasEntreFecha.size(), feriados );
					
					ans = ans + obtenerAnsPorDia(rangosPorDia, historial.getFechaEnvio(), historial.getFechaFin(), dia);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			ans = 0;
		}
		return ans;
	}

	private static int obtenerAnsPorDia(List<RangoCC> rangosPorDia, Date fechaInicial, Date fechaFinal, Date fechaEvaluar) {
		int preAns = 0;
		
		try {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			
			Date truncatedDateInicial = DateUtils.truncate(fechaInicial,
					Calendar.DATE);

			Date truncatedDateFinal = DateUtils.truncate(fechaFinal,
					Calendar.DATE);

			Date truncatedDateEvaluar = DateUtils.truncate(fechaEvaluar,
					Calendar.DATE);

			
			if(truncatedDateInicial.compareTo(truncatedDateEvaluar) == 0 && truncatedDateFinal.compareTo(truncatedDateEvaluar) == 0 ){
				//evaluar las horas inicio y fin
				Date tiempoEvaluarInicial = df.parse(df.format(fechaInicial));
				Date tiempoEvaluarFinal = df.parse(df.format(fechaFinal));
				for (RangoCC r : rangosPorDia) {
					Date rangoInicial = df.parse(r.getRangoInicial());
					Date rangoFinal = df.parse(r.getRangoFinal());
					if( tiempoEvaluarInicial.after(rangoInicial) && tiempoEvaluarInicial.before(rangoFinal) && 
							tiempoEvaluarFinal.after(rangoInicial) && tiempoEvaluarFinal.before(rangoFinal)   ){
					//1. terminana en el mismo rango
						long diff = tiempoEvaluarFinal.getTime() - tiempoEvaluarInicial.getTime();
						long diffMinutes = diff / (60 * 1000) ;
						preAns = preAns + (int) diffMinutes;
								break;
					}
					
					//2. hora inicial antes del rango a y hora fin dentro del rango.
					if(tiempoEvaluarInicial.before(rangoInicial) && tiempoEvaluarFinal.after(rangoInicial) && tiempoEvaluarFinal.before(rangoFinal)){
						long diff = tiempoEvaluarFinal.getTime() - rangoInicial.getTime();
						long diffMinutes = diff / (60 * 1000) ;
						preAns = preAns + (int) diffMinutes;
						break;
								
					}
					
					if(tiempoEvaluarInicial.before(rangoInicial) && tiempoEvaluarFinal.after(rangoInicial) && tiempoEvaluarFinal.after(rangoFinal)){
						long diff = rangoFinal.getTime() - rangoInicial.getTime();
						long diffMinutes = diff / (60 * 1000) ;
						preAns = preAns + (int) diffMinutes;
								
					}
					
					//3. si la hora iniial eta dentro del rangoa pero hora final no esta en el ranto.
					if(tiempoEvaluarInicial.after(rangoInicial) && tiempoEvaluarFinal.after(rangoFinal) && tiempoEvaluarInicial.before(rangoFinal)){
						long diff = rangoFinal.getTime() - tiempoEvaluarInicial.getTime();
						long diffMinutes = diff / (60 * 1000) ;
						preAns = preAns + (int) diffMinutes;
							
					}
					
					
				}
				
			} else if (truncatedDateInicial.compareTo(truncatedDateEvaluar) == 0) {
				//evaluar horai inicio
				Date tiempoEvaluar = df.parse(df.format(fechaInicial));
				for (RangoCC r : rangosPorDia) {
					Date rangoInicial = df.parse(r.getRangoInicial());
					Date rangoFinal = df.parse(r.getRangoFinal());
					
					if(tiempoEvaluar.after(rangoInicial) && tiempoEvaluar.before(rangoFinal) ){
						long diff = rangoFinal.getTime() - tiempoEvaluar.getTime();
						long diffMinutes = diff / (60 * 1000) ;
						preAns = preAns + (int) diffMinutes;
								break;
					}
					
					if(tiempoEvaluar.before(rangoInicial)){
						long diff = rangoFinal.getTime() - rangoInicial.getTime();
						long diffMinutes = diff / (60 * 1000) ;
						preAns = preAns + (int) diffMinutes;
					}
					
					
				}
				
			} else if (truncatedDateFinal.compareTo(truncatedDateEvaluar) == 0) {
				//evaluar hora fin
				Date tiempoEvaluar = df.parse(df.format(fechaFinal));
				for (RangoCC r : rangosPorDia) {
					Date rangoInicial = df.parse(r.getRangoInicial());
					Date rangoFinal = df.parse(r.getRangoFinal());
					//despues-antes
					if(tiempoEvaluar.after(rangoInicial) && tiempoEvaluar.before(rangoFinal) ){
						long diff =  tiempoEvaluar.getTime() - rangoInicial.getTime() ;
						long diffMinutes = diff / (60 * 1000) ;
						preAns = preAns + (int) diffMinutes;
								break;
					}
					
					if(tiempoEvaluar.after(rangoFinal)){
						long diff = rangoFinal.getTime() - rangoInicial.getTime();
						long diffMinutes = diff / (60 * 1000) ;
						preAns = preAns + (int) diffMinutes;
								
					}
				}
				
			} else {
				//evaluar solo rangos
				Date tiempoEvaluar  = df.parse("00:00");
				for (RangoCC r : rangosPorDia) {
					Date rangoA = df.parse(r.getRangoInicial());
					Date rangoB = df.parse(r.getRangoFinal());
					long diff = rangoB.getTime() - rangoA.getTime();
					long diffMinutes = diff / (60 * 1000) ;
					preAns = preAns + (int) diffMinutes;
							
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return preAns;
	}

	private static int getAns(String rangoInicial, String rangoFinal,
			Date tiempoEvaluar) {
		
		
		return 0;
	}

	private static List<RangoCC> ordenarRangos(Date fecha,
			List<RangoCC> rangos, int cantidadDias, List<HorarioCC> feriados) {
		
		RangoCCDAO rangoCCDAO = EJBLocator.getRangoCCDAO();
		List<RangoCC> rangosOrdenados = new ArrayList<RangoCC>();
		List<RangoCC> rangosTmp = new ArrayList<RangoCC>();
		try {
			Calendar calFecha = Calendar.getInstance();
			calFecha.setTime(fecha);
			int diaDeLaSemana = calFecha.get(Calendar.DAY_OF_WEEK);
			
			HorarioCC feriado = isHolyday(fecha, feriados);
			if(feriado != null){
				rangosTmp =rangoCCDAO.findByHorarioCC(feriado.getId());
			}else{
				for(RangoCC r : rangos){
					if(r.getHorario().getDia() == diaDeLaSemana){
						rangosTmp.add(r);
					}
				}
			}
			
			Date dateMenorRango = new SimpleDateFormat("HH:mm").parse("23:59");
			RangoCC rango;
			int conteo = rangosTmp.size();
			
			List<RangoCC> rangosToRemove=new ArrayList<RangoCC>();
			for (int i=1 ; i <= conteo ; i++ ){
				rango = new RangoCC();
				for(RangoCC r : rangosTmp){
					if(new SimpleDateFormat("HH:mm").parse(r.getRangoInicial()).before(dateMenorRango)){
						dateMenorRango =new SimpleDateFormat("HH:mm").parse(r.getRangoInicial());
						rango = r;
					}
				}
//				rangosTmp.remove(rango);
				rangosToRemove.add(rango);
				rangosOrdenados.add(rango);
				dateMenorRango = new SimpleDateFormat("HH:mm").parse("23:59");
			}
			rangosTmp.removeAll(rangosToRemove);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rangosOrdenados;
	}

	private static List<Date> diasEntreFecha(Date fechaEnvio, Date fechaFin) {
		List<Date> diasEntreFechas = new ArrayList<Date>();

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		Date truncatedDateInicial = DateUtils.truncate(fechaEnvio,
				Calendar.DATE);
		Date truncatedDateFinal = DateUtils.truncate(fechaFin, Calendar.DATE);

		Calendar calFechaInicial = Calendar.getInstance();
		calFechaInicial.setTime(truncatedDateInicial);

		Calendar calFechaFinal = Calendar.getInstance();
		calFechaFinal.setTime(truncatedDateFinal);

		System.out.println("Fecha inicial: " + df.format(truncatedDateInicial));
		System.out.println("Fecha Final: " + df.format(truncatedDateFinal));
		diasEntreFechas.add(calFechaInicial.getTime());
		while (calFechaInicial.getTime().before(calFechaFinal.getTime())) {
			calFechaInicial.add(Calendar.DATE, 1);
			System.out.println("Dia: " + df.format(calFechaInicial.getTime()));
			diasEntreFechas.add(calFechaInicial.getTime());
		}

		return diasEntreFechas;
	}

	private static HorarioCC isHolyday(Date evaluateDay, List<HorarioCC> feriados) {
		HorarioCC f = null;
		for(HorarioCC feriado : feriados ){
			if(feriado.getFechaExcepcion().equals(evaluateDay) ){ //verificar la comparacion
				f = feriado;
				break;
			}
		}
		return f;
	}

	public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }
	
	public static String parseDateString(Date fecha,SimpleDateFormat formatoFecha){
		 
		String fechaTexto=null;
		
		if (fecha !=null){
			Format formatter;
			formatter = formatoFecha;  // 2002
			fechaTexto = formatter.format(fecha);
		}
		return fechaTexto;
	}
	
	public static List<Cuenta> copiarListaCuentas (List<CuentaCore> cuentas) {
		ArrayList<Cuenta> lista = new ArrayList<Cuenta> ();
		if (cuentas!=null && !cuentas.isEmpty()) {
			for (CuentaCore cc : cuentas) {
				lista.add(Util.crearCuenta(cc));
			}
		}
		return lista;
	}

	private static Cuenta crearCuenta(CuentaCore cuentaCore) {
		Cuenta cuenta = new Cuenta ();
		cuenta.setFechaCreacion(cuentaCore.getFechCreacionCtaCte());
		cuenta.setMonedaCod(cuentaCore.getMonedaCodigo());
		cuenta.setMonedaDes(cuentaCore.getMonedaDescripcion());
		cuenta.setNumeroContrato(cuentaCore.getNumeroContrato());
		cuenta.setProductoCod(cuentaCore.getCodigoProducto());
		cuenta.setProductoDes(cuentaCore.getDescProducto());
		cuenta.setSituacionCuenta(cuentaCore.getSituacionCuenta());
		cuenta.setSubproductoCod(cuentaCore.getCodigoSubProducto());
		cuenta.setSubproductoDes(cuentaCore.getDescSubProducto());
		cuenta.setParticipes(copiarParticipes(cuentaCore.getParticipes(), cuenta));
		return cuenta;
	}
	
	private static Set<Participe> copiarParticipes(List<ParticipeCore> lista, Cuenta cuenta) {
		Set<Participe> participes = new HashSet<Participe>();
		for (ParticipeCore p : lista) {
			participes.add(crearParticipe(p, cuenta));
		}
		return participes;
	}

	private static Participe crearParticipe(ParticipeCore psfp, Cuenta cuenta) {
		Participe participe = new Participe ();
		participe.setApellidoMaterno(psfp.getApellidoMaterno());
		participe.setApellidoPaterno(psfp.getApellidoPaterno());
		//participe.setCliente(cliente);
		participe.setCodigoCentral(psfp.getCodigoCentral());
		participe.setCuenta(cuenta);
		participe.setDepartamentoCod(psfp.getCodigoDepartamento());
		participe.setDepartamentoDes(psfp.getDescDepartamento());
		participe.setDireccion(psfp.getDireccion());
		participe.setDistritoCod(psfp.getCodigoDistrito());
		participe.setDistritoDes(psfp.getDescDistrito());
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			participe.setFechaSerializacion(dateFormat.parse(psfp.getFechaSerialFirma()));
		} catch (Exception e) {
			try {
				SimpleDateFormat dateFormatEx = new SimpleDateFormat("dd/MM/yyyy");
				participe.setFechaSerializacion(dateFormatEx.parse(psfp.getFechaSerialFirma()));
			} catch (Exception ex) {
				participe.setFechaSerializacion(null);
			}
		}
		//participe.setFechaSerializacion(psfp.getFechaSerialFirma());
		//participe.setFlagFirmaActiva(psfp.getIndFirma());
		String indFirm = psfp.getIndFirmaAsociada();
		participe.setFlagFirmaAsociada(indFirm==null ? "0" : indFirm.substring(0, 1));
		//participe.setFlagIndicadorFirma(psfp.get);
		participe.setIndicadorFirma(psfp.getIndFirma());
		participe.setNivelIntervencion(psfp.getNivelIntervencion());
		participe.setNombre(psfp.getNombres());
		//participe.setNumeroCuenta(psfp.get);
		participe.setNumeroDoi(psfp.getNumeroDOI());
		participe.setProvinciaCod(psfp.getCodigoProvincia());
		participe.setProvinciaDes(psfp.getDescProvincia());
		participe.setSecuenciaIntervencion(psfp.getSecIntervencion());
		participe.setTipoDoi(psfp.getTipoDOI());
		participe.setTipoDoiDes(psfp.getDescTipoDOI());
		//participe.setUbicacion(psfp.get);
		return participe;
	}
	
	public static boolean validoEmail (String mail) {
		Pattern pattern = Pattern.compile(PATRON_EMAIL);
		Matcher matcher = pattern.matcher(mail);
		return matcher.matches();
	}
	
	public static void copiarDocumentoExp (DocumentoExp fuente, DocumentoExp destino) {
		destino.setDocPesoPromKB(fuente.getDocPesoPromKB());
		destino.setDocumento(fuente.getDocumento());
		destino.setExpediente(fuente.getExpediente());
		destino.setFechaEscaneo(fuente.getFechaEscaneo());
		destino.setFechaVigencia(fuente.getFechaVigencia());
		destino.setFlagAlterno(fuente.getFlagAlterno());
		destino.setFlagCm(fuente.getFlagCm());
		destino.setFlagEscaneado(fuente.getFlagEscaneado());
		destino.setFlagObligatorio(fuente.getFlagObligatorio());
		destino.setFlagRechazado(fuente.getFlagRechazado());
		destino.setFlagReqEscaneo(fuente.getFlagReqEscaneo());
		destino.setIdCm(fuente.getIdCm());
		destino.setIdCmCopia(fuente.getIdCmCopia());
		destino.setMotivo(fuente.getMotivo());
		destino.setNombreArchivo(fuente.getNombreArchivo());
		destino.setPidCM(fuente.getPidCM());
	}
	
	// TODO migrar treenode
	/*public static TreeNode getTreeNodeFromDocumentos(int tipoConsulta, String groupByProperty ,List<DocumentoCM> lista, boolean noNaturalOrder, String... orderByProperties){
        TreeNode root = new DefaultTreeNode("root", null);  
        try {                   
                Documento documento = null;
        	    Map<String , List<DocumentoCM>> mapa = com.ibm.bbva.ctacte.servicio.util.ListSorter.groupByOrderBy(groupByProperty, lista, noNaturalOrder, orderByProperties);
        	    Map<Documento , List<DocumentoCM>> mapaDescripciones = new TreeMap<Documento, List<DocumentoCM>>();
        	    
        	    ParametrosVistaUnicaDAO parametrosVistaUnicaDAO = DAOFactory.getInstance().getParametrosVistaUnicaDAO();
        	    boolean flag = parametrosVistaUnicaDAO.findParametroVistaUnica(1);        	    
        	    //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("flag : "+flag);
        	    
        	    for(Map.Entry<String, List<DocumentoCM>> grupo : mapa.entrySet()){        	    	                    
                    DocumentoDAO documentoDAO = DAOFactory.getInstance().getDocumentoDAO();
                    //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("grupo.getKey() for "+grupo.getKey());
                    documento = documentoDAO.findByCodigo(grupo.getKey());
                    //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("documento for "+documento);
                   
                    mapaDescripciones.put(documento, grupo.getValue());
        	    }
        	    
        	    for(Map.Entry<Documento, List<DocumentoCM>> grupo : mapaDescripciones.entrySet()){
                	//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("algo : "+grupo.getKey()+" : "+grupo.getValue().size());
                	DocumentoCM d = new DocumentoCM(); 
                    d.setNombreArchivo(grupo.getKey().getDescripcion().toUpperCase());
                    TreeNode nodo = new DefaultTreeNode(d, root); 
                    for(DocumentoCM doc : grupo.getValue()){ 
                    		if(doc.getTipoArchivo() !=null){
                    			if(doc.getTipoArchivo().equals("TIF")){				
                    				doc.setRutaCM(ConstantesBusiness.RUTA_CM_VISOR);
                    			}else{
                    				doc.setRutaCM(doc.getUrlContent());
                    			}
                    		}
                    		if(tipoConsulta == 2){
                    			if(grupo.getKey().getTipoVisor()!= null && grupo.getKey().getTipoVisor().equals("A")){
                        			if(doc.getTipoArchivo().equals("TIF")){
                        				new DefaultTreeNode(doc, nodo);
                        			}                    			
                        		}else{
                        			new DefaultTreeNode(doc, nodo);
                        		}      
                    		}else{
                    			new DefaultTreeNode(doc, nodo);
                    		}
                    		
                    		if(grupo.getKey().getCodigoDocumento().equals(ConstantesBusiness.CODIGO_DOCUMENTO_COPIA_LITERAL) && flag) {
                    			break;
                    		}
                    }
                }
        } catch (Exception e) {
                e.printStackTrace();
        } 
        return root;
	}*/

	public static DocumentoCM parseDocumentoToDocumentoCM(com.ibm.bbva.cm.servicepid.DocumentoPid doc ){
		DocumentoCM docCM = new DocumentoCM();
		Date defaultValue = null;         
		DateConverter converter = new DateConverter (defaultValue);         
		ConvertUtils.register (converter, java.util.Date.class);
		try {				
			//inicio - mod Manuel
			//org.apache.commons.beanutils.BeanUtils.copyProperties(docCM, doc);
			docCM.setCodCliente(doc.getCodCliente());			
			docCM.setContenido(doc.getContenido());
			docCM.setFechaCreacion(doc.getFechaCreacion()==null?null:doc.getFechaCreacion().toGregorianCalendar());
			docCM.setNombreArchivo(doc.getNombreArchivo());
			docCM.setNroDocumento(doc.getNumDoi());
			docCM.setOrigen(doc.getOrigen());
			docCM.setPid(doc.getPid());
			docCM.setRutaCM(doc.getUrlContent());
			docCM.setTipo(doc.getTipo());
			docCM.setTipoArchivo(docCM.getTipoArchivo());
			docCM.setCodigoDocumento(docCM.getCodigoDocumento());
			docCM.setTipoDoi(doc.getTipoDoi());
			docCM.setUrlContent(doc.getUrlContent());			
			//fin - mod Manuel			
			docCM.setFechaExpiracion(doc.getFechaExpiracion()==null?null:doc.getFechaExpiracion().toGregorianCalendar().getTime());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		return docCM;
	}
	
	public static List<DocumentoCM> parseCollectionDocumentToDocumentoCM(List<com.ibm.bbva.cm.servicepid.DocumentoPid> listaDoc){
		List<DocumentoCM> listaR = new ArrayList<DocumentoCM>();
		for(com.ibm.bbva.cm.servicepid.DocumentoPid d : listaDoc){
			listaR.add(parseDocumentoToDocumentoCM(d));
		}
		return listaR;
	}
	
	public static Date truncateDate(Date fecha) {
		if (fecha != null) {
			DateTime dateTime = new DateTime(fecha);
			return dateTime.withTimeAtStartOfDay().toDate();
		} else {
			return null;
		}
	}
	
	public static boolean actualizarParticipes(Expediente exp, ClientePJCore clientePJCore) {
		try {
			LOG.info("expediente.getId(): "+exp.getId());
			CuentaDAO cuentaDAO = EJBLocator.getCuentaDAO();
			ExpedienteCuentaDAO expedienteCuentaDAO = EJBLocator.getExpedienteCuentaDAO();
			
			List<Cuenta> cuentasBD = cuentaDAO.findByCliente(exp.getCliente());
			List<Cuenta> cuentasSFP = copiarListaCuentas(clientePJCore.getDatosCore().getCuentas());
			
			// borro Expediente X Cuenta antiguos
			List<ExpedienteCuenta> listaEC = expedienteCuentaDAO.findListaExpedienteCuenta(exp.getId());
			for (ExpedienteCuenta ece : listaEC) {
				expedienteCuentaDAO.deleteById(ece.getId());
			}
			
			// borro Cuentas y Partícipes antiguos
			for (Cuenta ccBD : cuentasBD) {
				cuentaDAO.deleteById(ccBD.getId());
			}
			
			for (Cuenta ccSFP : cuentasSFP) {
				Cuenta ccAntigua = null;
				for (Cuenta ccBD : cuentasBD) {
					if (ccBD.getNumeroContrato().equals(ccSFP.getNumeroContrato())) {
						ccAntigua = ccBD;
						break;
					}
				}
				LOG.info("cuenta.getNumeroContrato(): "+ccSFP.getNumeroContrato());
				LOG.info("cuenta.getParticipes(): "+ccSFP.getParticipes().size());
				for (Participe pSFP : ccSFP.getParticipes()) {
					pSFP.setCliente(exp.getCliente());
					pSFP.setCuenta(ccSFP);
					
					if (ccAntigua != null) {
						Participe pAntiguo = null;
						for (Participe pBD : ccAntigua.getParticipes()) {
							if (pBD.getCodigoCentral().equals(pSFP.getCodigoCentral())) {
								pAntiguo = pBD;
								break;
							}
						}
						if (pAntiguo != null) {
							pSFP.setFlagFirmaAsociada(pAntiguo.getFlagFirmaAsociada());
						}
					}
				}
				ccSFP.setCliente(exp.getCliente());
				cuentaDAO.save(ccSFP);
				
				LOG.info("cuenta.getId(): "+ccSFP.getId());
				
				ExpedienteCuenta expedienteCuenta = new ExpedienteCuenta();
				expedienteCuenta.setIdCuenta(ccSFP.getId());
				expedienteCuenta.setIdExpediente(exp.getId());
				expedienteCuentaDAO.save(expedienteCuenta);
			}
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		}
		
		return true;
	}
	
}
