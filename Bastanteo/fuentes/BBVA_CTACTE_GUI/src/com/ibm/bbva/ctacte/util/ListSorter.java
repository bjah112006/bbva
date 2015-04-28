package com.ibm.bbva.ctacte.util;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.comparators.NullComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.vo.TareaBandejaVO;

public class ListSorter {

	private static String dateFormatInterno = "EEE MMM dd HH:mm:ss zzz yyyy";
	private static Locale locale = new Locale("en", "EN");
	private static SimpleDateFormat sdf = new SimpleDateFormat(dateFormatInterno, locale);
	private static final Logger logger = LoggerFactory.getLogger(ListSorter.class);

	public static <T> void ordenar(List<T> lista, final String nomProperty, boolean noNaturalOrder) {

		Collections.sort(lista, new Comparator<T>() {

			public int compare(T e1, T e2) {
				if (nomProperty != null && !nomProperty.trim().equals("")) {
					try {
						String valor1 = BeanUtils.getNestedProperty(e1, nomProperty);
						String valor2 = BeanUtils.getNestedProperty(e2, nomProperty);
						if (isNumeric(valor1) && isNumeric(valor2)) {
							return new BigDecimal(valor1).compareTo(new BigDecimal(valor2));
						} else {
							if (isDate(valor1) && isDate(valor2)) {
								try {
									Date v1 = sdf.parse(valor1);
									Date v2 = sdf.parse(valor2);

									return v1.compareTo(v2);
								} catch (ParseException ex) {
									logger.debug("Error en la comparacion de fechas");
									return valor1.compareTo(valor2);
								}

							} else {
								return valor1.compareTo(valor2);
							}

						}
					} catch (IllegalAccessException ex) {
						logger.error(ex.getMessage(), ex);
						return -99;
					} catch (InvocationTargetException ex) {
						logger.error(ex.getMessage(), ex);
						return -99;
					} catch (NoSuchMethodException ex) {
						logger.error(ex.getMessage(), ex);
						return -99;
					}
				} else {
					return e1.toString().compareTo(e2.toString());
				}

			}
		});
		if (noNaturalOrder) {
			Collections.reverse(lista);
		}
	}

	public static <T> void ordenar(List<T> lista, boolean noNaturalOrder, final T sortExample) {
		List<String> propertyArray = new ArrayList<String>();
		try {
			Map<String,Object> mapa = PropertyUtils.describe(sortExample);
			for(Map.Entry<String,Object> entry: mapa.entrySet() ) {  
		        if(!(entry.getValue() instanceof Class)){
		        	if(entry.getValue()!=null && (entry.getValue() instanceof String && !entry.getValue().toString().equals(""))){
		        		propertyArray.add(entry.getKey());
		        	}		        	
		        }
		    }			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		String[] arr = propertyArray.toArray(new String[propertyArray.size()]);
		ordenar(lista, noNaturalOrder, arr);
	}
	
	public static <T> void ordenar(List<T> lista, boolean noNaturalOrder, final String... sortParameters) {

		Collections.sort(lista, new Comparator<T>() {

			public int compare(T e1, T e2) {
				for (String parameter : sortParameters) {
					int comparison;
					if (parameter != null && !parameter.trim().equals("")) {
						try {
							String valor1 = BeanUtils.getNestedProperty(e1, parameter);
							String valor2 = BeanUtils.getNestedProperty(e2, parameter);
							if (isNumeric(valor1) && isNumeric(valor2)) {
								comparison = new BigDecimal(valor1).compareTo(new BigDecimal(valor2));
								//return Integer.valueOf(valor1).compareTo(Integer.valueOf(valor2));
							} else {
								if (isDate(valor1) && isDate(valor2)) {
									try {
										Date v1 = sdf.parse(valor1);
										Date v2 = sdf.parse(valor2);

										comparison = v1.compareTo(v2);
									} catch (ParseException ex) {
										logger.debug("Error en la comparacion de fechas");
										//comparison = valor1.compareTo(valor2);
										comparison = new NullComparator().compare(valor1, valor2);
									}

								} else {
									//comparison = valor1.compareTo(valor2);
									comparison = new NullComparator().compare(valor1, valor2);
								}

							}
						} catch (IllegalAccessException ex) {
							logger.error(ex.getMessage(), ex);
							comparison = -99;
						} catch (InvocationTargetException ex) {
							logger.error(ex.getMessage(), ex);
							comparison = -99;
						} catch (NoSuchMethodException ex) {
							logger.error(ex.getMessage(), ex);
							comparison = -99;
						}
					} else {
						comparison = e1.toString().compareToIgnoreCase(e2.toString());
					}
					if (comparison != 0) {
						return comparison;
					}
				}

				return 0;
			}
		});
		if (noNaturalOrder) {
			Collections.reverse(lista);
		}
	}

	
	public static void ordenarOperaciones(List<TareaBandejaVO> lista, final String sortParametro) {

		Collections.sort(lista, new Comparator<TareaBandejaVO>() {

			public int compare(TareaBandejaVO e1, TareaBandejaVO e2) {
				if (e1.getOperacion().equals(sortParametro)){
					return -1;
				}else if (e2.getOperacion().equals(sortParametro)){
					return 1;				
				}
				return 0;
			}
		});		
	}
	
	public static void ordenarSemaforo(List<TareaBandejaVO> lista, final String sortParametro) {

		Collections.sort(lista, new Comparator<TareaBandejaVO>() {

			public int compare(TareaBandejaVO e1, TareaBandejaVO e2) {
				if (e1.getCodSemaforo().equals(sortParametro)){
					return -1;
				}else if (e2.getCodSemaforo().equals(sortParametro)){
					return 1;				
				}
				return 0;
			}
		});		
	}
	
	private static boolean isNumeric(String possibleNumber) {
		boolean valid = false;
		if(possibleNumber!=null){
			if (possibleNumber.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")) {
				valid = true;
			}
		}
		
		return valid;

	}

	private static boolean isDate(String fecha) {
		boolean valid = true;
		if(fecha==null){
			valid=false;
		}else{
			try {
				sdf.parse(fecha);
			} catch (ParseException ex) {
				valid = false;
			}
		}		
		return valid;
	}
	
	public static void ordenarMotivos(List<SelectItem> lista) {

		Collections.sort(lista, new Comparator<SelectItem>() {

			public int compare(SelectItem e1, SelectItem e2) {
				return e1.getValue().toString().compareTo(e2.getValue().toString());
			}
		});		
	}	
	
	/**author:hry ps
     * ordena expedientes de la bandeja de mayor a menor desc
     *
     * @param args argumentos de la línea de comandos.
     */
	
	public static void ordenarExpedientes(List<TareaBandejaVO> lista) {

		Collections.sort(lista, new Comparator<TareaBandejaVO>() {
			public int compare(TareaBandejaVO e1, TareaBandejaVO e2) {
				if (Integer.parseInt(e1.getId())== Integer.parseInt(e2.getId())) {        //si el primer expediente es igual a la edad de la segunda retornamos 0
		            return 0;
		        } else if (Integer.parseInt(e2.getId()) > Integer.parseInt(e1.getId())) { //si el segundo expediente es mayor la edad de la primera retornamos 1
		            return 1;
		        } else {                          										  //si el segundo expediente es menor del primer expediente retornamos -1
		            return -1;
		        }
			}
		});	
	}
}
