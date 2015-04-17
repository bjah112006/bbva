package com.ibm.bbva.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Minutes;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Horario;
import com.ibm.bbva.entities.HorarioOficina;
import com.ibm.bbva.session.HorarioBeanLocal;
import com.ibm.bbva.session.HorarioOficinaBeanLocal;
import com.ibm.bbva.tabla.reporte.vo.DatosGeneradosVO;
import com.ibm.bbva.tabla.reporte.vo.DatosUnidadVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AyudaHorario extends AbstractMBean {
	
	private HorarioBeanLocal horarioBean;
	private HorarioOficinaBeanLocal horarioOficinaBean;
	
	private Secuencia secuencia;
	private TreeSet<Calendar> listaTodoDia;
	private Map<Long, ArrayList<Horario>> mapaDatosHorarioOficina;
	
	private static final Logger LOG = LoggerFactory.getLogger(AyudaHorario.class);
	
	public AyudaHorario(long idOficina) throws NamingException {
		horarioBean = (HorarioBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.HorarioBeanLocal");
		horarioOficinaBean = (HorarioOficinaBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.HorarioOficinaBeanLocal");

		actualizarDatos (idOficina);
	}
	
	private void actualizarDatos (long idOficina) {	
		LOG.info("Inicio actualizarDatos AyudaHorario");
		
		//Comparator<Horario> comparator = new ComparadorFecha();
		Comparator<Horario> comparator = new ComparadorId();
		TreeSet<Horario> horarioNormal = new TreeSet<Horario> (comparator);
		TreeSet<Horario> horarioExcepcion = new TreeSet<Horario> (comparator);
		listaTodoDia = new TreeSet<Calendar> (new Comparador());
		//List<HorarioOficina> lista = horarioOficinaBean.buscarPorIdOficinaActivosHorarios(idOficina);
		
		//crearMapHorario(mapaDatosHorarioOficina);
		mapaDatosHorarioOficina = (Map<Long, ArrayList<Horario>>) getObjectSession(Constantes.LISTA_HORARIO_OFICINA);
		
		ArrayList<Horario> lista = mapaDatosHorarioOficina.get(idOficina);
				
		//Horario h = new Horario();
		//Horario horario = new Horario();
		if(lista!=null && lista.size()>0){
			LOG.info("Tamaño de horario :::"+lista.size()+", para Oficina "+idOficina);
			for (Horario objHO : lista) {
				//h.setId(ho.getHorario().getId());
				//h.setActivo(Constantes.CHECK_SELECCIONADO);
				//List<Horario> listaHO = horarioBean.buscarXcriterios(h);
				//if(ho!=null && ho.getHorario()!=null){
					//Horario objHO = ho.getHorario();
						//horario = listaHO.get(0);
						if (Constantes.CHECK_SELECCIONADO.equals(objHO.getExcepcion())) {
							if (Constantes.CHECK_SELECCIONADO.equals(objHO.getTodoDia())) {
								Calendar calendar = Calendar.getInstance();
								calendar.setTimeInMillis(objHO.getDiaInicio().getTime());
								listaTodoDia.add(calendar);
							} else {
								horarioExcepcion.add(objHO);
							}
						} else {
							horarioNormal.add(objHO);
						}
									
				//}
				
				/*if (!listaHO.isEmpty()) {
					horario = listaHO.get(0);
					if (Constantes.CHECK_SELECCIONADO.equals(horario.getExcepcion())) {
						if (Constantes.CHECK_SELECCIONADO.equals(horario.getTodoDia())) {
							Calendar calendar = Calendar.getInstance();
							calendar.setTimeInMillis(horario.getDiaInicio().getTime());
							listaTodoDia.add(calendar);
						} else {
							horarioExcepcion.add(horario);
						}
					} else {
						horarioNormal.add(horario);
					}
				}*/
			}
			secuencia = new Secuencia (horarioNormal, horarioExcepcion);			
		}else{
			LOG.info("No existe horario de Oficina "+idOficina+" en map");
			secuencia=null;
		}
	}

	public int calcularMinutos (Calendar inicioTarea, Calendar ahora) {
		int minutos = 0;
		
		estandarizar (inicioTarea);
		estandarizar (ahora);

		Calendar tareaDia = obtenerDia (inicioTarea);
		Calendar hoy = obtenerDia (ahora);
		
		if(secuencia!=null){
			LOG.info("secuencia no es null");
			if (tareaDia.equals(hoy) && (listaTodoDia.size()==0 || !listaTodoDia.contains(tareaDia))) {	
				minutos = secuencia.minutosMismoDia(inicioTarea, ahora);
			} else {			
				boolean esPrimero = true;
				while (!tareaDia.after(hoy)) {
					if (!listaTodoDia.contains(tareaDia)) {
						int min = 0;
						if (esPrimero) {
							min += secuencia.minutosInicio(inicioTarea);
							esPrimero = false;
						} else if (tareaDia.equals(hoy)){
							min += secuencia.minutosFin(ahora);
						} else {
							min += secuencia.minutos(tareaDia);
						}
						if (min == Constantes.NO_TIENE_MINUTOS) {
							//return Constantes.NO_TIENE_MINUTOS;
							min=0;
						} //else {
							minutos += min;
						//}
					}
					tareaDia.add(Calendar.DAY_OF_MONTH, 1);
				}
			}
			
			secuencia.reiniciar();
		}else
			LOG.info("secuencia es null");

		
		return minutos;
	}
	
	private void estandarizar (Calendar calendar) {
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}
	
	private Calendar obtenerDia (Calendar calendar) {
		Calendar dia = Calendar.getInstance();
		dia.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 
				calendar.get(Calendar.DAY_OF_MONTH), 0, 0);
		estandarizar (dia);
		return dia;
	}

	private class ComparadorFecha implements Comparator<Horario> {
		public int compare(Horario o1, Horario o2) {
			return o1.getDiaInicio().compareTo(o2.getDiaInicio());
		}
	}

	private class ComparadorId implements Comparator<Horario> {
		public int compare(Horario o1, Horario o2) {
			return String.valueOf(o1.getId()).compareTo(String.valueOf(o2.getId()));
		}
	}
	
	private class Comparador implements Comparator<Calendar> {
		public int compare(Calendar o1, Calendar o2) {
			return o1.compareTo(o2);
		}
	}
	
	private class RangoFecha {
		private RangoFecha siguiente;
        private Calendar inicio;
        private Calendar fin;
        private Calendar horaInicio;
        private Calendar horaFin;
        private int[] dias;
        
        private RangoFecha (Horario horario) {
        	inicio = Calendar.getInstance();
        	inicio.setTimeInMillis(horario.getDiaInicio().getTime());
        	fin = Calendar.getInstance();
        	fin.setTimeInMillis(horario.getDiaFin().getTime());
        	dias = new int[7];
        	dias[Calendar.SUNDAY-1] = minutosDia(horario.getDom()!=null?horario.getDom().intValue():null);
        	dias[Calendar.MONDAY-1] = minutosDia(horario.getLun()!=null?horario.getLun().intValue():null);
        	dias[Calendar.TUESDAY-1] = minutosDia(horario.getMar()!=null?horario.getMar().intValue():null);
        	dias[Calendar.WEDNESDAY-1] = minutosDia(horario.getMie()!=null?horario.getMie().intValue():null);
        	dias[Calendar.THURSDAY-1] = minutosDia(horario.getJue()!=null?horario.getJue().intValue():null);
        	dias[Calendar.FRIDAY-1] = minutosDia(horario.getVie()!=null?horario.getVie().intValue():null);
        	dias[Calendar.SATURDAY-1] = minutosDia(horario.getSab()!=null?horario.getSab().intValue():null);
        	try {
	        	horaInicio = Calendar.getInstance(); 
	        	horaInicio.setTime(Constantes.FORMATO_HORA.parse(horario.getHoraInicio()));
	        	horaFin = Calendar.getInstance();
	        	horaFin.setTime(Constantes.FORMATO_HORA.parse(horario.getHoraFin()));
        	} catch (Exception e) {}
        }
        
        public void setSiguiente (RangoFecha siguiente) {
        	this.siguiente = siguiente;
        }
        
        public RangoFecha getSiguiente() {
			return siguiente;
		}

		public DateTime crearHoraInicio(Calendar fechaBase) {
			horaInicio.set(fechaBase.get(Calendar.YEAR), 
					fechaBase.get(Calendar.MONTH), 
					fechaBase.get(Calendar.DAY_OF_MONTH));
			return new DateTime (horaInicio);
		}

		public DateTime crearHoraFin(Calendar fechaBase) {
			horaFin.set(fechaBase.get(Calendar.YEAR), 
					fechaBase.get(Calendar.MONTH), 
					fechaBase.get(Calendar.DAY_OF_MONTH));
			return new DateTime (horaFin);
		}

		private int minutosDia (Integer dia) {
        	return dia==null ? 0 : dia;
        }

        public int minutos (Calendar fecha) {
        	if (!inicio.after(fecha) && !fin.before(fecha)) {
                return dias[fecha.get(Calendar.DAY_OF_WEEK)-1];
            } else {
            	return Constantes.NO_TIENE_MINUTOS;
            }
        }
	}
	
	private class Secuencia {
        private RangoFecha rfNorInicio;
        private RangoFecha rfNorActual;
        private RangoFecha rfExInicio;
        private RangoFecha rfExActual;
        
        public Secuencia (TreeSet<Horario> horarioNorm, TreeSet<Horario> horarioEx) {
        	rfNorInicio = crearRangoFecha (horarioNorm);
        	rfExInicio = crearRangoFecha (horarioEx);        	
        	reiniciar ();
        }
        
        public void reiniciar () {
        	rfNorActual = rfNorInicio;
        	rfExActual = rfExInicio;
        }
        
        private RangoFecha crearRangoFecha (TreeSet<Horario> listaHorario) {
        	RangoFecha inicio = null;
        	RangoFecha actual = null;
        	for (Horario hvo : listaHorario) {        		
        		if (inicio==null) {
        			inicio = new RangoFecha (hvo);
        			actual = inicio;
        		} else {
        			RangoFecha rangoFecha = new RangoFecha (hvo);
        			actual.setSiguiente(rangoFecha);
        			//actual = rangoFecha;
        		}
        	}
        	//return inicio;
        	return actual;
        }
        
        public int minutos (Calendar fecha) {
        	int minutosNor = obtenerMinutos (rfNorActual, fecha);
        	if (minutosNor==Constantes.NO_TIENE_MINUTOS) {
        		return Constantes.NO_TIENE_MINUTOS;
        	}
        	int minutosEx = obtenerMinutos(rfExActual, fecha);
            return minutosNor - (minutosEx==Constantes.NO_TIENE_MINUTOS ? 0 : minutosEx);
        }
        
        public int minutosMismoDia (Calendar fechaInicio, Calendar fechaFin) {
        	int minutosNor = obtenerMinutos (rfNorActual, fechaInicio);
        	if (minutosNor==Constantes.NO_TIENE_MINUTOS) {
        		return Constantes.NO_TIENE_MINUTOS;
        	}
        	int minutosEx = obtenerMinutos(rfExActual, fechaInicio);
        	
            return minutosNor - (minutosEx==Constantes.NO_TIENE_MINUTOS ? 0 : minutosEx) - 
            		minutosInicio (fechaFin) - minutosFin(fechaInicio);
        }
        
        public int minutosInicio (Calendar fecha) {
        	int minutosNor = obtenerMinutos (rfNorActual, fecha);
        	if (minutosNor==Constantes.NO_TIENE_MINUTOS) {
        		return Constantes.NO_TIENE_MINUTOS;
        	}
        	
        	int minutos = 0;
        	int minutosEx = obtenerMinutos(rfExActual, fecha);
        	minutosEx = (minutosEx==Constantes.NO_TIENE_MINUTOS ? 0 : minutosEx);
        	DateTime dtHora = new DateTime (fecha);

        	RangoFecha rfNorActualTemp = obtenerRangoFecha(rfNorActual, fecha);
        	
        	DateTime hiNorm = rfNorActualTemp.crearHoraInicio(fecha);
        	DateTime hfNorm = rfNorActualTemp.crearHoraFin(fecha);
        	
        	if (rfExActual==null) {
        		Interval interval = new Interval (hiNorm, hfNorm);
        		if (interval.contains(dtHora)) {
        			minutos = Minutes.minutesBetween(dtHora, hfNorm).getMinutes();
        		} else if (dtHora.isBefore(hiNorm)) {
        			minutos = minutosNor;
        		} else {
        			minutos = 0;
        		}
        	} else {
        		
        		RangoFecha rfExActualTemp = obtenerRangoFecha(rfExActual, fecha);
        		
	        	if (rfExActualTemp!=null){
	        		DateTime hiEx = rfExActualTemp.crearHoraInicio(fecha);
	        		DateTime hfEx = rfExActualTemp.crearHoraFin(fecha);
	        	
	        	
		        	Interval primInterv = new Interval (hiNorm, hiEx);
		        	Interval interInterv = new Interval (hiEx, hfEx);
		        	Interval segInterval = new Interval (hfEx, hfNorm);
	        	
		        	if (primInterv.contains(dtHora)){
		        		minutos = minutosNor - minutosEx - 
		        				Minutes.minutesBetween(hiNorm, dtHora).getMinutes();
		        	} else if (segInterval.contains(dtHora)) {
		        		minutos = minutosNor - Minutes.minutesBetween(hiNorm, dtHora).getMinutes();
		        	} else if (interInterv.contains(dtHora)) {
		        		minutos = minutosNor - Minutes.minutesBetween(hiNorm, hfEx).getMinutes();
		        	} else if (dtHora.isBefore(hiNorm)) {
		        		minutos = minutosNor - minutosEx;
		        	} // else  minutos = 0
	        	}
        	}
            return minutos;
        }
        
        public int minutosFin (Calendar fecha) {
        	int minutosNor = obtenerMinutos (rfNorActual, fecha);
        	if (minutosNor==Constantes.NO_TIENE_MINUTOS) {
        		return Constantes.NO_TIENE_MINUTOS;
        	}
        	
        	int minutos = 0;
        	int minutosEx = obtenerMinutos(rfExActual, fecha);
        	minutosEx = (minutosEx==Constantes.NO_TIENE_MINUTOS ? 0 : minutosEx);
        	DateTime dtHora = new DateTime (fecha);
        	
        	RangoFecha rfNorActualTemp = obtenerRangoFecha(rfNorActual, fecha);
        	
        	DateTime hiNorm = rfNorActualTemp.crearHoraInicio(fecha);
        	DateTime hfNorm = rfNorActualTemp.crearHoraFin(fecha);
        	
        	if (rfExActual==null) {
        		Interval interval = new Interval (hiNorm, hfNorm);
        		if (interval.contains(dtHora)) {
        			minutos = Minutes.minutesBetween(hiNorm, dtHora).getMinutes();
        		} else if (dtHora.isAfter(hfNorm)) {
        			minutos = minutosNor;
        		} else {
        			minutos = 0;
        		}
        	} else {
        		
        		RangoFecha rfExActualTemp = obtenerRangoFecha(rfExActual, fecha);
        		
        		if (rfExActualTemp!=null){
		        	DateTime hiEx = rfExActualTemp.crearHoraInicio(fecha);
		        	DateTime hfEx = rfExActualTemp.crearHoraFin(fecha);
		        	
		        	Interval primInterv = new Interval (hiNorm, hiEx);
		        	Interval interInterv = new Interval (hiEx, hfEx);
		        	Interval segInterval = new Interval (hfEx, hfNorm);
		        	if (primInterv.contains(dtHora)){
		        		minutos = minutosNor - Minutes.minutesBetween(dtHora, hfNorm).getMinutes();
		        	} else if (segInterval.contains(dtHora)) {
		        		minutos = minutosNor - minutosEx - 
		        				Minutes.minutesBetween(dtHora, hfNorm).getMinutes();
		        	} else if (interInterv.contains(dtHora)) {
		        		minutos = minutosNor - Minutes.minutesBetween(hiEx, hfNorm).getMinutes();
		        	} else if (dtHora.isAfter(hfNorm)) {
		        		minutos = minutosNor - minutosEx;
		        	} // else  minutos = 0
        		}
        	}
        	
            return minutos;
        }
        
        private int obtenerMinutos (RangoFecha rangoFecha, Calendar fecha) {
        	int minutos = Constantes.NO_TIENE_MINUTOS;
        	while (minutos==Constantes.NO_TIENE_MINUTOS && rangoFecha!=null) {
        		minutos = rangoFecha.minutos(fecha);
        		if (minutos==0)
        			minutos = Constantes.NO_TIENE_MINUTOS;
        		
        		if (minutos==Constantes.NO_TIENE_MINUTOS) {
        			rangoFecha = rangoFecha.getSiguiente();
        		}
        	}
        	return minutos;
        }
        
        private RangoFecha obtenerRangoFecha (RangoFecha rangoFecha, Calendar fecha) {
        	int minutos = Constantes.NO_TIENE_MINUTOS;
        	while (minutos==Constantes.NO_TIENE_MINUTOS && rangoFecha!=null) {
        		minutos = rangoFecha.minutos(fecha);
        		if (minutos==0)
        			minutos = Constantes.NO_TIENE_MINUTOS;
        		
        		if (minutos==Constantes.NO_TIENE_MINUTOS) {
        			rangoFecha = rangoFecha.getSiguiente();
        		}
        	}
        	return rangoFecha;
        }
        
    }
	
	public double calculoTiempo(Timestamp tInicial, Timestamp tFinal) {
		double minutos=0;
				
		Calendar calT1 = Calendar.getInstance();
		calT1.setTime(tInicial);
		
		Calendar calT2 = Calendar.getInstance();
		calT2.setTime(tFinal);
		
		minutos = calcularMinutos(calT1, calT2);
		
		/*Si devuelve -1 es que no esta configurado el horario para la oficina*/
		/*o el rango de tiempo no esta registrado para las fechas y horas*/
		/*entonces cambiamos el retorno a cero*/
		if (minutos == Constantes.NO_TIENE_MINUTOS) {
			minutos = Constantes.CEROMINUTO;
		}
		
		return minutos;
	}	
}
