package com.ibm.bbva.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.DateTime;

import com.grupobbva.bc.per.tele.ldap.directorio.IILDPeUsuario;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Nivel;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.TipoCategoria;
import com.ibm.bbva.entities.ToePerfilEstado;
import com.ibm.bbva.entities.VistaExpedienteCantidad;
import com.ibm.bbva.session.NivelBeanLocal;
import com.ibm.bbva.session.OficinaBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.TipoCategoriaBeanLocal;
import com.ibm.bbva.session.ToePerfilEstadoBeanLocal;
import com.ibm.bbva.session.VistaExpedienteCantidadBeanLocal;
import com.ibm.bbva.tabla.util.vo.EmpleadoVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Util {
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	@EJB
	private ToePerfilEstadoBeanLocal toePerfilEstadoBeanLocal;
	/*public static String iniciarInstanciaProceso(String nombreInstanciaProceso, ExpedienteTCWPS expedienteTCWPS){
try {
			
			Object ref =  refBeanRemoto();
	        LOG.info("linea 5 ref:" + ref);
	        BusinessTaskManagerWrapperBeanRemote interfaz = (BusinessTaskManagerWrapperBeanRemote) PortableRemoteObject.narrow(ref, BusinessTaskManagerWrapperBeanRemote.class);
	        LOG.info("linea 6");
	        String mensaje = interfaz.iniciarInstanciaProceso(nombreInstanciaProceso, expedienteTCWPS);
	        LOG.info("linea 7");
	        return mensaje;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}*/
	
	private static final Logger LOG = LoggerFactory.getLogger(Util.class);
	
	public static Object refBeanRemoto(){
		try {
			Properties props = new Properties();  
			//LOG.info("linea 1");
	        props.put(Context.INITIAL_CONTEXT_FACTORY,"com.ibm.websphere.naming.WsnInitialContextFactory");
	        //LOG.info("linea 2");
	        props.put(Context.PROVIDER_URL,"corbaloc:iiop:9.6.104.213:9810");
	        //LOG.info("linea 3");
	        Context ctx = new InitialContext(props);
	        //LOG.info("linea 4");
	        Object ref = ctx.lookup("com.ibm.process.BusinessTaskManagerWrapperBeanRemote");
	        //LOG.info("linea 5 ref:" + ref);
	        return ref;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	} 
	/**
	 * @return Retorna una lista vacia
	 */
	public static List<SelectItem> listaVacia () {
		ArrayList<SelectItem> items = new ArrayList<SelectItem>(1);
		SelectItem selectItem = new SelectItem();
        selectItem.setValue(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
        selectItem.setLabel("");
        items.add(selectItem);
        return items;
    }
	
	public static Collection<SelectItem> listaVaciaC () {
		Collection<SelectItem> items = new ArrayList<SelectItem>();		
		SelectItem si = new SelectItem();
		si.setValue(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
		si.setLabel(" ");
		items.add(si);
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
    
    public static List<SelectItem> setEtiqueta(List<SelectItem> lista, String id, String etiqueta) {
		for(int i=0; i <= lista.size(); i++){
			SelectItem selectItem = new SelectItem();
			selectItem = lista.get(i);
			if(selectItem.getValue().toString().equals(id) ) {
				selectItem.setLabel(etiqueta.trim());
				break;
			}
		}
		return lista;
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

    public static List<SelectItem> crearItemsE (List<?> lista, boolean blaco, String propValor, String propNombre, String etiqueta) {
    	return crearItemsE(lista, blaco, propValor, propNombre, true, etiqueta);
    }
    
    /**
     * Crea una lista con elementos SelectItem a partir de una lista
     * @param lista Lista del cual se va obtener los datos
     * @param blaco Se es true muestra un item en blanco al inicio con id=null
     * @param propValor Es la propiedad que se asignara a SelectItem.value
     * @param propNombre Es la propiedad que se agisnara a SelectItem.label
     * @return Lista con elementos de tipo SelectItem
     */
    public static List<SelectItem> crearItemsConcat (List<?> lista, boolean blaco, String propValor, String propNombre1, String propNombre2) {
    	return crearItemsConcat(lista, blaco, propValor, propNombre1, propNombre2, true);
    }
    
    public static List<SelectItem> crearItemsConcatE (List<?> lista, boolean blaco, String propValor, String propNombre1, String propNombre2, String etiqueta) {
    	return crearItemsConcatE(lista, blaco, propValor, propNombre1, propNombre2, true, etiqueta);
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
        if (blanco) {
        	SelectItem selectItem = new SelectItem();
            selectItem.setValue(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
            selectItem.setLabel("");
            items.add(selectItem);
        }
        try {
        	if (lista!= null && !lista.isEmpty()) {
        		Class<?> clase = lista.get(0).getClass();
	        	Method metVal = metodoGet (clase, propValor);
	        	Method metNomb = metodoGet (clase, propNombre);
	        	Object[] params = new Object[0];
	            for (Object obj : lista) {
	                SelectItem selectItem = new SelectItem();
	                selectItem.setValue(metVal.invoke(obj, params).toString());
	                selectItem.setLabel(metNomb.invoke(obj, params).toString());
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
    
    public static List<SelectItem> eliminarCamposRepetidos(List<SelectItem> lista) {
		ArrayList<SelectItem> items = new ArrayList<SelectItem> ();
		ArrayList<String> labels = new ArrayList<String>();
		for (SelectItem selectItem : lista) {
			if (!labels.contains(selectItem.getLabel())) {
				labels.add(selectItem.getLabel());
				items.add(selectItem);
			}
		}
		return items;
	}
    
    public static List<SelectItem> crearItemsE (List<?> lista, boolean blanco, String propValor, String propNombre, boolean habilitado, String etiqueta) {
        ArrayList<SelectItem> items = new ArrayList<SelectItem> ();
        if (blanco) {
        	SelectItem selectItem = new SelectItem();
            selectItem.setValue(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
            selectItem.setLabel(etiqueta);
            items.add(selectItem);
        }
        try {
        	if (lista!= null && !lista.isEmpty()) {
        		Class<?> clase = lista.get(0).getClass();
	        	Method metVal = metodoGet (clase, propValor);
	        	Method metNomb = metodoGet (clase, propNombre);
	        	Object[] params = new Object[0];
	            for (Object obj : lista) {
	                SelectItem selectItem = new SelectItem();
	                selectItem.setValue(metVal.invoke(obj, params).toString());
	                selectItem.setLabel(metNomb.invoke(obj, params).toString());
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
    
    public static List<SelectItem> crearItemsConcat (List<?> lista, boolean blanco, String propValor, String propNombre1, String propNombre2, boolean habilitado) {
        ArrayList<SelectItem> items = new ArrayList<SelectItem> ();
        if (blanco) {
        	SelectItem selectItem = new SelectItem();
            selectItem.setValue(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
            selectItem.setLabel("");
            items.add(selectItem);
        }
        try {
        	if (lista!= null && !lista.isEmpty()) {
        		Class<?> clase = lista.get(0).getClass();
	        	Method metVal = metodoGet (clase, propValor);
	        	Method metNomb1 = metodoGet (clase, propNombre1);
	        	Method metNomb2 = metodoGet (clase, propNombre2);
	        	Object[] params = new Object[0];
	            for (Object obj : lista) {
	                SelectItem selectItem = new SelectItem();
	                selectItem.setValue(metVal.invoke(obj, params).toString());
	                selectItem.setLabel(metNomb1.invoke(obj, params).toString() + " - " + metNomb2.invoke(obj, params).toString());
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
    
    public static List<SelectItem> crearItemsConcatE (List<?> lista, boolean blanco, String propValor, String propNombre1, String propNombre2, boolean habilitado, String etiqueta) {
        ArrayList<SelectItem> items = new ArrayList<SelectItem> ();
        if (blanco) {
        	SelectItem selectItem = new SelectItem();
            selectItem.setValue(Constantes.CODIGO_CODIGO_CAMPO_VACIO);
            selectItem.setLabel(etiqueta);
            items.add(selectItem);
        }
        try {
        	if (lista!= null && !lista.isEmpty()) {
        		Class<?> clase = lista.get(0).getClass();
	        	Method metVal = metodoGet (clase, propValor);
	        	Method metNomb1 = metodoGet (clase, propNombre1);
	        	Method metNomb2 = metodoGet (clase, propNombre2);
	        	Object[] params = new Object[0];
	            for (Object obj : lista) {
	                SelectItem selectItem = new SelectItem();
	                selectItem.setValue(metVal.invoke(obj, params).toString());
	                selectItem.setLabel(metNomb1.invoke(obj, params).toString() + " - " + metNomb2.invoke(obj, params).toString());
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
    
    private static Method metodoGet (Class<?> clase, String propiedad) throws NoSuchMethodException {
		char anterior = propiedad.charAt(0);
		char nuevo = Character.toUpperCase(anterior);
    	String nombMetodo = "get" + propiedad.replaceFirst(String.valueOf(anterior), String.valueOf(nuevo));
	    return clase.getMethod(nombMetodo);
    }
    
    public static double convertStringToDouble(String nombreCampo) {   	
    	return Double.parseDouble(nombreCampo.replace(",", ""));
    }
   
    public static double convertStringToDouble(String source, char decimalSeparator, char groupingSeparator){
    	DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(decimalSeparator);
        symbols.setGroupingSeparator(groupingSeparator);
        df.setDecimalFormatSymbols(symbols);        
		try {
			return df.parse(source).doubleValue();
		} catch (ParseException e) {
			return 0.0;
		}         
    }
    
    public static String convertDoubleToString(double nombreCampo, String pattern) {
		NumberFormat formatter = new DecimalFormat();
		formatter = new DecimalFormat(pattern);
		return formatter.format(nombreCampo);
    }
    
    public static boolean esNuloVacio(String nombreCampo) {
    	return nombreCampo==null || nombreCampo.trim().equals("");      	
    }

    /*
	public static class SecletItemComparator implements Comparator<SelectItem> {
		
		boolean order = false;
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
	*/
    
    public static class SecletItemComparator implements Comparator<SelectItem> {
		
		boolean order = false;
		boolean concat = false;

		/**
		 * @param Ordena una Lista true = Desc, false = Asc
		 */
		public SecletItemComparator(boolean order) {		
			this.order = order;
		}
		
		public SecletItemComparator(boolean order, boolean concat) {
			this.order = order;
			this.concat = concat;
		}
		
	    public int compare(SelectItem s1, SelectItem s2) {  
	    	if(concat == false){
	    		if (order) {
	                return s2.getLabel().compareTo(s1.getLabel());                
		    	}else{
		    		return s1.getLabel().compareTo(s2.getLabel());
		    	}
	    	} else {
	    		if(s1.getLabel().trim() != "" && s2.getLabel().trim() != "") {
	    			String label1[] = s1.getLabel().split("-");
		    		String label2[] = s2.getLabel().split("-");
		    		if (order) {
		                return label2[1].compareTo(label1[1]);                
			    	}else{
			    		return label1[1].compareTo(label2[1]);
			    	}
	    		} else {
	    			if (order) {
		                return s2.getLabel().compareTo(s1.getLabel());                
			    	}else{
			    		return s1.getLabel().compareTo(s2.getLabel());
			    	}
	    		}
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
	
	public static boolean validarEmail(String correo){
		try{
		    // Compiles the given regular expression into a pattern.
			if (correo!=null){
				correo=correo.replaceAll("^\\s*","");
				correo=correo.trim();
			}
		    Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		    // Match the given input against this pattern
		    Matcher matcher = pattern.matcher(correo);
		    return matcher.matches();
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
		}
		return false;
		
		
	}
	
	
	
	public static String validarId (String id) {
		return (id==null || Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(id)) ? null : id;
	}
	
	public static Integer validarIdInteger (String id) {
		return (id==null || Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(id)) ? null : new Integer(id);
	}
	
	public static String obtenerDescripcion (List<SelectItem> lista, String valor) {
		if (valor != null && !Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(valor)) {
			//LOG.info("Tamaño de usuario="+lista.size());
			//LOG.info("valor="+valor);
			for (SelectItem si : lista) {
				//LOG.info("si="+si.getValue().toString());
				if (valor.equals(si.getValue())) {
					//LOG.info("OK");
					return si.getLabel();
				}
			}
		}
		return null;
	}
	
	public static void initEntityFK(Object o, List<Class<?>> clazz) {
		//LOG.info("Iniciando "+o);
		if(o != null){

			Map<String, Object> m;
			try {
				m = PropertyUtils.describe(o);
				Set<Map.Entry<String, Object>> s = m.entrySet();
				//LOG.info("Tamaño mapa : "+s.size());
				/*for(Map.Entry<String, Object> e : s){
					LOG.info("key : "+e.getKey()+" - value : "+e.getValue());
				}*/
				Field[] campos = o.getClass().getDeclaredFields();
				//LOG.info("Tamaño campos : "+campos.length);
				for(Field f : campos){
					if(m.containsKey(f.getName())){
						ManyToOne rel = f.getAnnotation(ManyToOne.class);
						OneToOne rel2 = f.getAnnotation(OneToOne.class);
						//LOG.info("Nomcampo : "+f.getName()+" - "+rel+":"+rel2);
						if(rel != null || rel2 != null){
							Object veri = PropertyUtils.getProperty(o, f.getName());
							if(veri == null){
								//Object obj = f.getType().newInstance();
								Constructor[] ctors = f.getType().getDeclaredConstructors();
								Constructor ctor = null;
								for (int i = 0; i < ctors.length; i++) {
								    ctor = ctors[i];
								    if (ctor.getGenericParameterTypes().length == 0)
									break;
								}
								ctor.setAccessible(true);
								Object obj2 = ctor.newInstance();
								//LOG.info("Iniciando : "+f.getName() + " de la clase : "+o.getClass().getSimpleName()+" con valor: "+obj2);
								BeanUtils.setProperty(o, f.getName(), obj2);
								
								if(!clazz.contains(f.getType())){									
									if(!f.getType().equals(o.getClass())){
										clazz.add(f.getType());
										initEntityFK(PropertyUtils.getProperty(o, f.getName()),clazz);
									}
								}

							}
						}
					}
				}
			} catch (Exception e1) {
				LOG.error(e1.getMessage(), e1);
			}

		}		
	}

	public static String parseDateString(Date fecha,String formato){		
		SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);		
		String fechaTexto=null;
		
		if (fecha !=null){
			Format formatter;
			formatter = formatoFecha;  // 2002
			fechaTexto = formatter.format(fecha);
		}
		return fechaTexto;
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
	
	public static Date addDaysToDate(Date fecha, int nroDias) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha );
		cal.add( Calendar.DATE, nroDias);		
	
		String strFecha = parseDateString(cal.getTime(),"dd/MM/yyyy");
		fecha = parseStringToDate(strFecha, "dd/MM/yyyy");
		return fecha;
	}
	
	public static void actualizaDevoluciones(Expediente expediente) {
	   long nroDevoluciones;	
	   expediente.getExpedienteTC().setFlagDevolucion(Constantes.FLAG_DEVOLUCION);
	   LOG.info("FLAG_DEVOLUCION="+Constantes.FLAG_DEVOLUCION);
	   
	   if (expediente.getExpedienteTC().getNroDevoluciones() == null) {
		   nroDevoluciones=1;
	   }else{		
		   nroDevoluciones = expediente.getExpedienteTC().getNroDevoluciones().intValue() + 1;
	   }
	   expediente.getExpedienteTC().setNroDevoluciones(BigDecimal.valueOf(nroDevoluciones));
	   LOG.info("expediente.getExpedienteTC() devoluciones="+expediente.getExpedienteTC().getNroDevoluciones());
	}
	
	public static double minutesBetween(DateTime date1, DateTime date2) {		
		return Math.abs((date1.getSecondOfDay() - date2.getSecondOfDay())/Constantes.UNMINUTO);
	}

	public static double minutesBetweenEntero(DateTime date1, DateTime date2) {		
		return Math.abs(date1.getMinuteOfDay() - date2.getMinuteOfDay());
	}
	
	public static BigDecimal round(double d,int scale) {
		BigDecimal minuto = new BigDecimal(d+"");       
		return minuto.setScale(scale, RoundingMode.HALF_UP);
	}
	
    public static File guardarArchivoEnRutaReturnFile(String pRutaArchivo, ArrayList pNombreColumnas,
		ArrayList pListaDatos) {
	            File fileChoosen = null;
		try {
			fileChoosen = new File(pRutaArchivo);
			FileWriter outFile = new FileWriter(fileChoosen);
			String linea = "";
			for (int i = 0; i < pNombreColumnas.size(); i++) {
				String lsCabecera = (String)pNombreColumnas.get(i);
				linea += lsCabecera + "\t\t";
			}
			
			outFile.write(linea + "\n");
			for (int i = 0; i < pListaDatos.size(); i++) {
				linea = "";
				for (int j = 0; j < ((ArrayList)pListaDatos.get(i)).size(); j++) {
					String lscadena = getValorCampoArrayList(pListaDatos,i,j);
					linea += lscadena + "\t";
				}
				outFile.write(linea + "\n");
			}
			outFile.close();
		} catch (IOException ioerr) {
			ioerr.printStackTrace();
		}
	            return fileChoosen;
	}	
    
	/**
	  * @param pArrayList - Objeto ArrayList, que contiene objetos ArrayList, donde se busca el valor del campo.
	  * @param pFila  -  Fila donde se busca el valor del campo.
	  * @param pColumna - Columna donde se busca el valor del campo.
	  *
	  * @return String devuelve el valor del metodo
	  */	
	public static String getValorCampoArrayList(ArrayList pArrayList, int pFila, int pColumna)
	{
		String value = "";
		if(pArrayList == null || pFila < 0 || pArrayList.size() <= pFila || 
			pColumna < 0 || ((ArrayList)pArrayList.get(pFila)).size() <= pColumna)
			return value;
		if( ((ArrayList)pArrayList.get(pFila)).get(pColumna) == null ) return value;
		value = ((String)((ArrayList)pArrayList.get(pFila)).get(pColumna)).trim();
		return value;
	}
	
	//Emi
	public static String fechaTimeStamp() {
		 
		 String fechaActual = "";
		 java.util.Date utilDate = new java.util.Date(); //fecha actual
		 long lnMilisegundos = utilDate.getTime();	
		 java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(lnMilisegundos);
		  
		 fechaActual = String.valueOf(sqlTimestamp);
		  
		 return fechaActual;
	}
	
    public static String getIdTx(String codApp, String codUsu) {
        
        String resultado = generatetxId();
        resultado = resultado+codApp+codUsu;
        
        return resultado;   
    }
    
    public static String generatetxId() {
        String txId = getFechyHoraFormatBatch().toString().concat((String.valueOf(Math.random()).substring(2)));

        if (txId.length() > 20)
            txId = txId.substring(0, 20);

        return txId;
    }
    
    public static String getFechyHoraFormatBatch() {
        return getFechayHoraActualByFormato("yyyyMMddHHmmss");
    }
    
    public static String getFechayHoraActualByFormato(String formato) {
        return formatDateObject(formato, GregorianCalendar.getInstance().getTime());
    }
    
    public static String formatDateObject(String formato, java.util.Date fecha) {
        return new java.text.SimpleDateFormat(formato, Locale.getDefault()).format(fecha);
    }
	
    public static boolean validaCuenta(String nroCuentaOrigen){
		boolean resultado=false;
		LOG.info("nroCuentaOrigen: " + nroCuentaOrigen);
		if (nroCuentaOrigen.length()==23){
			//String nroCuenta = nroCuentaOrigen.substring(0, 8) + nroCuentaOrigen.substring(10, nroCuentaOrigen.length());
			String nroCuenta = nroCuentaOrigen.substring(0, 4) + nroCuentaOrigen.substring(5, 9) + nroCuentaOrigen.substring(13, nroCuentaOrigen.length());
			LOG.info("nroCuenta 1: " + nroCuenta);
			//nroCuenta = nroCuentaOrigen;
			LOG.info("nroCuenta 2: " + nroCuenta);
			String digitoChequeo = nroCuentaOrigen.substring(10, 12);
			LOG.info("digitoChequeo: " + digitoChequeo);
			String cad1 = "";
			cad1 = operacionProducto(nroCuenta,4,5,1);
			cad1 = cad1 + operacionProducto(nroCuenta,5,6,2);
			cad1 = cad1 + operacionProducto(nroCuenta,6,7,1);
			cad1 = cad1 + operacionProducto(nroCuenta,7,8,2);
			LOG.info("cad1: " + cad1);
			
			Integer valor = 3;
			for(int i=0;i<cad1.length();i++){
				valor = valor + Integer.parseInt(cad1.substring(i, i+1));
			}
			LOG.info("valor: " + valor);
			
			String dig1 = "";
			dig1 = operacionCociente(valor);
			LOG.info("dig1: " + dig1);
			dig1 = (dig1.length()<=1) ? dig1 : dig1.substring(dig1.length()-1);
			LOG.info("dig1: " + dig1);
			
			String cad2 = "";
			for(int i=8;i<nroCuenta.length();i=i+2){
				cad2 = cad2 + operacionProducto(nroCuenta,i,i+1,1);
				cad2 = cad2 + operacionProducto(nroCuenta,i+1,i+2,2);
			}
			LOG.info("cad2: " + cad2);
			
			valor = 0;
			for(int i=0;i<cad2.length();i++){
				valor = valor + Integer.parseInt(cad2.substring(i, i+1));
			}
			LOG.info("valor: " + valor);
			
			String dig2 = "";
			dig2 = operacionCociente(valor);
			LOG.info("dig2: " + dig2);
			dig2 = (dig2.length()<=1) ? dig2 : dig2.substring(dig2.length()-1);
			LOG.info("dig2: " + dig2);
			
			if (digitoChequeo.equals(dig1+dig2))
				resultado = true;
    	}
		return resultado;
	}
	
	//Función que extrae un caracter del parametro "cadena" en base a "posIni" y "posFin" 
	//y lo multiplica con el parámetro "valor", devolviendo el "resultado" como String
	public static String operacionProducto(String cadena, Integer posIni, Integer posFin, Integer valor){
		String resultado = "";
		Integer producto =0;
		String caracter = cadena.substring(posIni, posFin);
		Integer digito = Integer.parseInt(caracter.trim());
		producto = digito * valor;
		resultado = producto.toString();
		return resultado;
	}
	public static String operacionCociente(Integer valor){
		String resultado = "";
		Integer cociente = (((valor / 10) + 1) * 10) - valor;
		resultado = cociente.toString();
		return resultado;
	}
		
	public static EmpleadoVO mapearEmpleadoBDAEmpleadoVO(Empleado objEmpleado){
		LOG.info("Metodo mapearEmpleadoBDAEmpleadoVO");
		
		EmpleadoVO objEmpleadoVO=new EmpleadoVO();
		
		objEmpleadoVO.setId(objEmpleado.getId());
		
		if(objEmpleado.getFecegr()!=null)
		objEmpleadoVO.setFecegr(objEmpleado.getFecegr());
			
		if(objEmpleado.getFecing()!=null)
		objEmpleadoVO.setFecing(objEmpleado.getFecing());
		
		if(objEmpleado.getNivel()!=null)
		objEmpleadoVO.setIdNivelFk(objEmpleado.getNivel().getId());
		
		if(objEmpleado.getOficina()!=null)
			objEmpleadoVO.setIdOficinaFk(objEmpleado.getOficina().getId());
		
		if(objEmpleado.getPerfil()!=null)
			objEmpleadoVO.setIdPerfilFk(objEmpleado.getPerfil().getId());
		
		if(objEmpleado.getTipoCategoria()!=null)
			objEmpleadoVO.setIdTipoCategoriaFk(objEmpleado.getTipoCategoria().getId());
		
		if (objEmpleado.getFlagActivo()==null || objEmpleado.getFlagActivo().equals("0"))
			objEmpleadoVO.setFlagActivo(false);
		else
			objEmpleadoVO.setFlagActivo(true);
		objEmpleadoVO.setNombres(objEmpleado.getNombres()==null?"":objEmpleado.getNombres());
		objEmpleadoVO.setNombresCompletos(objEmpleado.getNombresCompletos()==null?"":objEmpleado.getNombresCompletos());
		objEmpleadoVO.setApepat(objEmpleado.getApepat()==null?"":objEmpleado.getApepat());
		objEmpleadoVO.setApemat(objEmpleado.getApemat()==null?"":objEmpleado.getApemat());
		objEmpleadoVO.setCodigo(objEmpleado.getCodigo()==null?"":objEmpleado.getCodigo());
		objEmpleadoVO.setCorreo(objEmpleado.getCorreo()==null?"":objEmpleado.getCorreo());
		
		return objEmpleadoVO;
	}
	
	public static Empleado mapearEmpleadoVOAEmpleadoBD(EmpleadoVO objEmpleadoVO, OficinaBeanLocal oficinabean, PerfilBeanLocal perfilbean, NivelBeanLocal nivelbean, TipoCategoriaBeanLocal tipocategoriabean){
		LOG.info("Metodo mapearEmpleadoVOAEmpleadoBD");
		LOG.info("objEmpleadoVO.getIdPerfilFk(): " + objEmpleadoVO.getIdPerfilFk());
		LOG.info("objEmpleadoVO.getIdNivelFk(): " + objEmpleadoVO.getIdNivelFk());
		LOG.info("objEmpleadoVO.getIdOficinaFk(): " + objEmpleadoVO.getIdOficinaFk());
		LOG.info("objEmpleadoVO.getIdTipoCategoriaFk: " + objEmpleadoVO.getIdTipoCategoriaFk());
		
		Empleado empleado=new Empleado();
		
		if (objEmpleadoVO.getId()!=null)
			empleado.setId(objEmpleadoVO.getId());
		empleado.setCodigo(objEmpleadoVO.getCodigo());
		empleado.setNombres(objEmpleadoVO.getNombres());
		empleado.setApemat(objEmpleadoVO.getApemat());
		empleado.setApepat(objEmpleadoVO.getApepat());
		Perfil perfil = perfilbean.buscarPorId(objEmpleadoVO.getIdPerfilFk());
		empleado.setPerfil(perfil);
		empleado.setCorreo(objEmpleadoVO.getCorreo());
		empleado.setFlagActivo(objEmpleadoVO.getFlagActivo()?"1":"0");
		Nivel nivel = nivelbean.buscarPorId(objEmpleadoVO.getIdNivelFk());
		empleado.setNivel(nivel);
		empleado.setNombresCompletos(objEmpleadoVO.getNombresCompletos());
		Oficina oficina = oficinabean.buscarPorId(objEmpleadoVO.getIdOficinaFk());
		empleado.setOficina(oficina);
		TipoCategoria tipocategoria = tipocategoriabean.buscarPorId(objEmpleadoVO.getIdTipoCategoriaFk());
		empleado.setTipoCategoria(tipocategoria);
		
		return empleado;
	}
	
	
	public static EmpleadoVO consultarEmpleadoLDAP(String codigoEmpleado, OficinaBeanLocal oficinabean){
		LOG.info("Metodo consultarEmpleadoLDAP");
		LOG.info("codigoEmpleado: " + codigoEmpleado);
		IILDPeUsuario userLDAP =null;
		EmpleadoVO objEmpleadoVO=new EmpleadoVO();
		try{
			userLDAP = IILDPeUsuario.recuperarUsuario(codigoEmpleado);
			String codOficina ="";
			//objEmpleadoVO.setId(objEmpleado.getId());
			objEmpleadoVO.setCodigo(codigoEmpleado);
			if (userLDAP!=null){
				objEmpleadoVO.setApepat(userLDAP.getApellido1()==null?"":userLDAP.getApellido1());
				objEmpleadoVO.setApemat(userLDAP.getApellido2()==null?"":userLDAP.getApellido2());
				objEmpleadoVO.setNombres(userLDAP.getNombre()==null?"":userLDAP.getNombre());
				objEmpleadoVO.setNombresCompletos(objEmpleadoVO.getApepat() + " " + objEmpleadoVO.getApemat() + " " + objEmpleadoVO.getNombres());
				objEmpleadoVO.setCorreo(userLDAP.getEmail()==null?"":userLDAP.getEmail());
				if(userLDAP.getBancoOficina()!=null && userLDAP.getBancoOficina().getCodigo()!=null){
					if (userLDAP.getBancoOficina().getCodigo()!=null)
						codOficina = userLDAP.getBancoOficina().getCodigo();
					objEmpleadoVO.setCodOficina(codOficina);
					Oficina oficina = oficinabean.buscarPorCodigo(codOficina);
					if (oficina!=null){
						objEmpleadoVO.setIdOficinaFk(oficina.getId());
					}
				}
			}else{
				LOG.debug("userLDAP es nulo");
//				objEmpleadoVO.setCodigo(codigoEmpleado);
//				objEmpleadoVO.setApepat("apepat userTEMP");
//				objEmpleadoVO.setApemat("apemat userTEMP");
//				objEmpleadoVO.setNombres("nombre userTEMP");
//				objEmpleadoVO.setNombresCompletos(objEmpleadoVO.getApepat() + " " + objEmpleadoVO.getApemat() + " " + objEmpleadoVO.getNombres());
//				objEmpleadoVO.setCorreo("correo@bbva.com");
//				codOficina = null;
//					Oficina oficina = oficinabean.buscarPorCodigo(codOficina);
//					objEmpleadoVO.setCodOficina(codOficina);
//					if (oficina!=null){
//						objEmpleadoVO.setIdOficinaFk(oficina.getId());
//					}
			}
		}
		catch(Exception e){
			LOG.debug("Error consultarEmpleadoLDAP: " + e.getMessage());
		}
		return objEmpleadoVO;
	}
	
	public static long cantidadExpedientesEmpleado(Integer idEmpleado,VistaExpedienteCantidadBeanLocal vistaexpedientecantidadbean){
		long cantidad = 0;
		
		VistaExpedienteCantidad vistaExpedienteCantidad = vistaexpedientecantidadbean.cantidadExpPorIdEmpleado(idEmpleado);
		if (vistaExpedienteCantidad!=null){
			cantidad = vistaExpedienteCantidad.getNumExpedientes();
			LOG.info("CantidadExpedientesEmpleado-cantidad: " + cantidad);
		}
		return cantidad;
	}
	public static Map<Integer,Object> cantidadExpPorEmpleado(VistaExpedienteCantidadBeanLocal vistaexpedientecantidadbean){
		Map<Integer,Object> mapCantidadExpPorEmpleado = new TreeMap<Integer, Object>();
		mapCantidadExpPorEmpleado = vistaexpedientecantidadbean.cantidadExpPorEmpleado();
		return mapCantidadExpPorEmpleado;
	}
	public static boolean validarEmpleado(EmpleadoVO empleadoVO){
		boolean result = true;
		if (empleadoVO.getNombres()==null || empleadoVO.getNombres().trim().equals(""))
			result = false;
		if (empleadoVO.getNombresCompletos()==null || empleadoVO.getNombresCompletos().trim().equals(""))
			result = false;
		if (empleadoVO.getApemat()==null || empleadoVO.getApemat().trim().equals(""))
			result = false;
		if (empleadoVO.getApepat()==null || empleadoVO.getApepat().trim().equals(""))
			result = false;
		if (empleadoVO.getCodigo()==null || empleadoVO.getCodigo().trim().equals(""))
			result = false;
//		if (empleadoVO.getCorreo()==null || empleadoVO.getCorreo().trim().equals(""))
//			result = false;
		if (empleadoVO.getIdNivelFk()==null || empleadoVO.getIdNivelFk()<1)
			result = false;
		if (empleadoVO.getIdOficinaFk()==null || empleadoVO.getIdOficinaFk()<1)
			result = false;
		if (empleadoVO.getIdPerfilFk()==null || empleadoVO.getIdPerfilFk()<1)
			result = false;
		if (empleadoVO.getIdTipoCategoriaFk()==null || empleadoVO.getIdTipoCategoriaFk()<1)
			result = false;
		return result;
	}
	
	 public static boolean isDouble(String cad)
	 {
		 try
		 {
		   Double.parseDouble(cad.trim());
		   return true;
		 }
		 catch(NumberFormatException nfe)
		 {
		   return false;
		 }
	 }
	 
	 public static String obtenerValor (List<SelectItem> lista, String label) {
		if (label != null && !label.equals("")) {
			LOG.info("Tamaño de usuario="+lista.size());
			LOG.info("label="+label);
			for (SelectItem si : lista) {
				LOG.info("si="+si.getLabel());
				if (label.equals(si.getLabel())) {
					LOG.info("OK");
					return si.getValue().toString();
				}
			}
		}
		return null;
	 }	 
	
	 public static int restarFechas(Date fechaIn, Date fechaFinal ){
		 GregorianCalendar fechaInicio= new GregorianCalendar();
		 fechaInicio.setTime(fechaIn);
		 GregorianCalendar fechaFin= new GregorianCalendar();
		 fechaFin.setTime(fechaFinal);
		 int dias = 0;
		 if(fechaFin.get(Calendar.YEAR)==fechaInicio.get(Calendar.YEAR)){
			 dias =(fechaFin.get(Calendar.DAY_OF_YEAR)- fechaInicio.get(Calendar.DAY_OF_YEAR))+1;
		 }else{
			 int rangoAnyos = fechaFin.get(Calendar.YEAR) - fechaInicio.get(Calendar.YEAR);

			 for(int i=0;i<=rangoAnyos;i++){
				 int diasAnio = fechaInicio.isLeapYear(fechaInicio.get(Calendar.YEAR)) ? 366 : 365;
				 if(i==0){
					 dias=1+dias +(diasAnio- fechaInicio.get(Calendar.DAY_OF_YEAR));
				 }else if(i==rangoAnyos){
					 dias=dias +fechaFin.get(Calendar.DAY_OF_YEAR);
				 }else{
					 dias=dias+diasAnio;
				 }
			 }
		 }

		 return dias;
	 }
	 
	 public static List<ToePerfilEstado> recuperaValoresTOEPerfilEstado(Expediente expediente , ToePerfilEstadoBeanLocal toePerfilEstadoBeanLocal){
			List<ToePerfilEstado> lstToePerfilEstado = new ArrayList<ToePerfilEstado>();
			try{
				String strTipoFlujo="";
				String tipoFlujo="";
				if (expediente.getExpedienteTC().getFlagDevolucion()==null || expediente.getExpedienteTC().getFlagDevolucion().equals(Constantes.FLAG_REGULAR)){
					strTipoFlujo=Constantes.DESCRIPCION_TIPO_FLUJO_REGULAR;
				}else if (expediente.getExpedienteTC().getFlagDevolucion().equals(Constantes.FLAG_DEVOLUCION)){
					strTipoFlujo=Constantes.DESCRIPCION_TIPO_FLUJO_REPROCESO;
				}
				
				if (strTipoFlujo.equals(Constantes.DESCRIPCION_TIPO_FLUJO_REGULAR)){
					tipoFlujo="R";
				}else if (strTipoFlujo.equals(Constantes.DESCRIPCION_TIPO_FLUJO_REPROCESO)){
					tipoFlujo="P";
				}
				if (expediente.getExpedienteTC().getTarea().getId()==Long.valueOf(Constantes.REGISTRAR_EXPEDIENTE_TAREA_1) ||
					expediente.getExpedienteTC().getTarea().getId()==Long.valueOf(Constantes.VERIFICACION_RESULTADO_VERIF_DOMICILIARIA)||
					expediente.getExpedienteTC().getTarea().getId()==Long.valueOf(Constantes.ID_TAREA_APROBAR_EXPEDIENTE)||
					expediente.getExpedienteTC().getTarea().getId()==Long.valueOf(Constantes.ID_TAREA_EJECUTAR_EVALUACION_CREDITICIA)||
					expediente.getExpedienteTC().getTarea().getId()==Long.valueOf(Constantes.ID_TAREA_REVISAR_REGISTRAR_DICTAMEN)||
					expediente.getExpedienteTC().getTarea().getId()==Long.valueOf(Constantes.ID_TAREA_CAMBIAR_SITUACION_EXPEDIENTE)||
					expediente.getExpedienteTC().getTarea().getId()==Long.valueOf(Constantes.ID_TAREA_ANULAR_MODIFICACION_14)||
					expediente.getExpedienteTC().getTarea().getId()==Long.valueOf(Constantes.ID_TAREA_ANULAR_MODIFICACION_18)||
					expediente.getExpedienteTC().getTarea().getId()==Long.valueOf(Constantes.ID_TAREA_REGISTRAR_APROBACION)||
					expediente.getExpedienteTC().getTarea().getId()==Long.valueOf(Constantes.ID_TAREA_REALIZAR_ALTA)){
					lstToePerfilEstado=toePerfilEstadoBeanLocal.buscarRegistroToePerfilEstadoTarea1(expediente.getEstado().getId(),
																									expediente.getEmpleado().getPerfil().getId(),
																					   				expediente.getExpedienteTC().getTipoOferta().getId(), 
																									tipoFlujo, expediente.getProducto().getId(), 
																									expediente.getExpedienteTC().getTarea().getId());
				}else{
					lstToePerfilEstado=toePerfilEstadoBeanLocal.buscarRegistroToePerfilEstado(expediente.getEstado().getId(),
																							  expediente.getEmpleado().getPerfil().getId(),
																					  		  expediente.getExpedienteTC().getTipoOferta().getId(), 
																							  tipoFlujo, expediente.getProducto().getId(), 
																							  expediente.getExpedienteTC().getTarea().getId());
				}
			}catch(Exception ex){
				LOG.error(ex.getMessage(), ex);
			}
																	
			
			return lstToePerfilEstado;
		}


	public static boolean isListaVacia(List<SelectItem> lista) {
					
		if(lista==null) return true;
		if(lista.size()<=0) return true;
		
		if(lista.get(0).getValue().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			return true;
		}else{
			return false;
		}

	}


		
}