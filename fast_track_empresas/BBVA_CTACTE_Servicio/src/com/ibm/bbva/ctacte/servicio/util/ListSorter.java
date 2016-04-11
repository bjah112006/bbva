package com.ibm.bbva.ctacte.servicio.util;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ajbullon
 */
public class ListSorter {

    private static String dateFormatInterno = "EEE MMM dd HH:mm:ss zzz yyyy";
    private static Locale locale = new Locale("en", "EN");
    private static SimpleDateFormat sdf = new SimpleDateFormat(dateFormatInterno, locale);
    private static final Logger LOG = LoggerFactory.getLogger(ListSorter.class);

    public static <T> void ordenar(List<T> lista, final String nomProperty, boolean noNaturalOrder) {

        Collections.sort(lista, new Comparator<T>() {

            public int compare(T e1, T e2) {
                if (nomProperty != null && !nomProperty.trim().equals("")) {
                    try {
                        String valor1 = BeanUtils.getNestedProperty(e1, nomProperty);
                        String valor2 = BeanUtils.getNestedProperty(e2, nomProperty);
                        if (isNumeric(valor1) && isNumeric(valor2)) {
                            return new BigDecimal(valor1).compareTo(new BigDecimal(valor2));
                            //return Integer.valueOf(valor1).compareTo(Integer.valueOf(valor2));
                            } else {
                            if (isDate(valor1) && isDate(valor2)) {
                                try {
                                    Date v1 = sdf.parse(valor1);
                                    Date v2 = sdf.parse(valor2);

                                    return v1.compareTo(v2);
                                } catch (ParseException ex) {
                                	LOG.debug("Error en la comparacion de fechas");
                                    return valor1.compareTo(valor2);
                                }

                            } else {
                                return valor1.compareTo(valor2);
                            }

                        }
                    } catch (IllegalAccessException ex) {
                    	LOG.error(ex.getMessage(), ex);
                        return -99;
                    } catch (InvocationTargetException ex) {
                    	LOG.error(ex.getMessage(), ex);
                        return -99;
                    } catch (NoSuchMethodException ex) {
                    	LOG.error(ex.getMessage(), ex);
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

    public static <T> void ordenar(List<T> lista, boolean noNaturalOrder, final String... sortParameters) {

        Collections.sort(lista, new Comparator<T>() {

            public int compare(T e1, T e2) {
                for (String parameter : sortParameters) {
                    int comparison =0;
                    if (parameter != null && !parameter.trim().equals("")) {
                        try {                            
                            Object valorObj1 = PropertyUtils.getNestedProperty(e1, parameter);
                            Object valorObj2 = PropertyUtils.getNestedProperty(e2, parameter);
                            
                            if(valorObj1 == null){
                                comparison = -1;
                            }                            
                            if(valorObj2 == null){
                                comparison = 1;
                            }
                            if(valorObj1 == null && valorObj2 == null){
                                comparison = 0;
                            }
                            if(valorObj1 != null && valorObj2 != null){
                                if(valorObj1 instanceof Comparable && valorObj2 instanceof Comparable){
                                    comparison = ListSorter.compararA((Comparable)valorObj1, (Comparable)valorObj2);
                                }
                            }                            
                        } catch (Exception ex) {
                        	LOG.error(ex.getMessage(), ex);
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
    
    public static <T,K> Map<K , List<T>> groupByOrderBy(String groupByProperty ,List<T> lista, boolean noNaturalOrder, String... orderByProperties) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        Set<K> groups = new LinkedHashSet<K>() ;
        for(T t : lista){
            K valorObj1 = (K) PropertyUtils.getNestedProperty(t, groupByProperty);
            groups.add(valorObj1);
        }
//        List<K> listaSet = new ArrayList<K>(groups);
//        ordenar(listaSet, "", false);
//        groups = new LinkedHashSet<K>(listaSet);
        Map<K , List<T>> mapReturn = new TreeMap<K, List<T>>();
        for(K k : groups){
            List<T> listaOrd = new ArrayList<T>();
            for(T t : lista){
                if(k.equals(PropertyUtils.getNestedProperty(t, groupByProperty))){
                    listaOrd.add(t);
                }
            }
            ordenar(listaOrd, noNaturalOrder, orderByProperties);
            mapReturn.put(k, listaOrd);
        }
        
        printMap(mapReturn);
        return mapReturn;
    }
    
    public static <T,K> void printMap(Map<T, K> map) {
		for (Map.Entry entry : map.entrySet()) {
			LOG.info("Key : " + entry.getKey() + " Value : "				+ entry.getValue());
		}
	}


    public static <T extends Comparable> int compararA(T o1 , T o2){
        return o1.compareTo(o2);
    }
    
    private static boolean isNumeric(String possibleNumber) {
        boolean valid = false;
        if (possibleNumber.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")) {
            valid = true;
        }
        return valid;

    }

    private static boolean isDate(String fecha) {
        boolean valid = true;
        try {
            sdf.parse(fecha);
        } catch (ParseException ex) {
            valid = false;
        }
        return valid;
    }
    
    public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
      List<T> list = new ArrayList<T>(c);
      java.util.Collections.sort(list);
      return list;
    }
}
