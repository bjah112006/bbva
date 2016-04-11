package com.ibm.bbva.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Minutes;

import com.ibm.bbva.constantes.ConstantesAdmin;
import com.ibm.bbva.ctacte.bean.Horario;
import com.ibm.bbva.ctacte.bean.HorarioOficina;
import com.ibm.bbva.ctacte.dao.HorarioDAO;
import com.ibm.bbva.ctacte.dao.HorarioOficinaDAO;
import com.ibm.bbva.ctacte.util.EJBLocator;

public class AyudaHorario implements java.io.Serializable{
	
	private Secuencia secuencia;
	private TreeSet<Calendar> listaTodoDia;
	
	public AyudaHorario(Integer idOficina) {
		actualizarDatos (idOficina);
	}
	
	private void actualizarDatos (Integer idOficina) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("idOficina : "+idOficina);
		Comparator<Horario> comparator = new ComparadorFecha();
		TreeSet<Horario> horarioNormal = new TreeSet<Horario> (comparator);
		TreeSet<Horario> horarioExcepcion = new TreeSet<Horario> (comparator);
		listaTodoDia = new TreeSet<Calendar> (new Comparador());
		
		HorarioOficinaDAO horarioOficinaDAO = EJBLocator.getHorarioOficinaDAO();
		HorarioDAO horarioDAO = EJBLocator.getHorarioDAO(); 
		
		List<HorarioOficina> lista = horarioOficinaDAO.findByOficina(idOficina);		
		for (HorarioOficina ho : lista) {
			List<Horario> listaHO =  horarioDAO.findByIdActivo(ho.getId(), ConstantesAdmin.HORARIO_ACTIVO);
			if (!listaHO.isEmpty()) {
				Horario horarioVO = listaHO.get(0);
				if (ConstantesAdmin.HORARIO_ACTIVO.equals(horarioVO.getExcepcion())) {
					if (ConstantesAdmin.HORARIO_ACTIVO.equals(horarioVO.getTodoDia())) {
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(horarioVO.getDiaInicio().getTime());
						listaTodoDia.add(calendar);
					} else {
						horarioExcepcion.add(horarioVO);
					}
				} else {
					horarioNormal.add(horarioVO);
				}
			}
		}
		secuencia = new Secuencia (horarioNormal, horarioExcepcion);
	}

	public int calcularMinutos (Calendar inicioTarea, Calendar ahora) {		
		int minutos = 0;
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("++calcularMinutos++");
		estandarizar (inicioTarea);
		estandarizar (ahora);

		Calendar tareaDia = obtenerDia (inicioTarea);
		Calendar hoy = obtenerDia (ahora);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("tareaDia : "+tareaDia.getTime());
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("hoy : "+hoy.getTime());
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("inicioTarea : "+inicioTarea.getTime());
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("ahora : "+ahora.getTime());		
		if (tareaDia.equals(hoy) && !listaTodoDia.contains(tareaDia)) {
			minutos = secuencia.minutosMismoDia2(inicioTarea, ahora);
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("minutos 1 : "+minutos);
		} else {
			boolean esPrimero = true;
			while (!tareaDia.after(hoy)) {
				if (!listaTodoDia.contains(tareaDia)) {
					int min = 0;
					if (esPrimero) {
						min += secuencia.minutosInicio(inicioTarea);
						esPrimero = false;
						//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("minutos 2 : "+min);
					} else if (tareaDia.equals(hoy)){
						min += secuencia.minutosFin(ahora);
						//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("minutos 3 : "+min);
					} else {
						min += secuencia.minutos(tareaDia);
						//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("minutos 4 : "+min);
					}
					if (min == ConstantesAdmin.NO_TIENE_MINUTOS) {
						//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("minutos 5 : "+min);
						return ConstantesAdmin.NO_TIENE_MINUTOS;
					} else {
						minutos += min;
						//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("minutos 6 : "+min);
					}
				}
				tareaDia.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		secuencia.reiniciar();
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

	private class Comparador implements Comparator<Calendar>,Serializable {
		public int compare(Calendar o1, Calendar o2) {
			return o1.compareTo(o2);
		}
	}
	
	private class RangoFecha implements Serializable {
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
        	dias[Calendar.SUNDAY-1] = minutosDia(horario.getDom());
        	dias[Calendar.MONDAY-1] = minutosDia(horario.getLun());
        	dias[Calendar.TUESDAY-1] = minutosDia(horario.getMar());
        	dias[Calendar.WEDNESDAY-1] = minutosDia(horario.getMie());
        	dias[Calendar.THURSDAY-1] = minutosDia(horario.getJue());
        	dias[Calendar.FRIDAY-1] = minutosDia(horario.getVie());
        	dias[Calendar.SATURDAY-1] = minutosDia(horario.getSab());
        	try {
	        	horaInicio = Calendar.getInstance(); 
	        	horaInicio.setTime(ConstantesAdmin.FORMATO_HORA.parse(horario.getHoraInicio()));
	        	horaFin = Calendar.getInstance();
	        	horaFin.setTime(ConstantesAdmin.FORMATO_HORA.parse(horario.getHoraFin()));
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
            	return ConstantesAdmin.NO_TIENE_MINUTOS;
            }
        }
	}
	
	private class Secuencia implements Serializable {
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
        			actual = rangoFecha;
        		}
        	}
        	return inicio;
        }
        
        public int minutos (Calendar fecha) {
        	int minutosNor = obtenerMinutos (rfNorActual, fecha);
        	if (minutosNor==ConstantesAdmin.NO_TIENE_MINUTOS) {
        		return ConstantesAdmin.NO_TIENE_MINUTOS;
        	}
        	int minutosEx = obtenerMinutos(rfExActual, fecha);
            return minutosNor - (minutosEx==ConstantesAdmin.NO_TIENE_MINUTOS ? 0 : minutosEx);
        }
        
        public int minutosMismoDia (Calendar fechaInicio, Calendar fechaFin) {
        	int minutosNor = obtenerMinutos (rfNorActual, fechaInicio);        	
        	if (minutosNor==ConstantesAdmin.NO_TIENE_MINUTOS) {
        		return ConstantesAdmin.NO_TIENE_MINUTOS;
        	}        	
        	int minutosEx = obtenerMinutos(rfExActual, fechaInicio);        	
            return minutosNor - (minutosEx==ConstantesAdmin.NO_TIENE_MINUTOS ? 0 : minutosEx) -                   
            		minutosInicio (fechaFin) - minutosFin(fechaInicio);
        }
        
        public int minutosMismoDia2 (Calendar fechaInicio, Calendar fechaFin) {
        	long milis1 = fechaInicio.getTimeInMillis();
        	long milis2 = fechaFin.getTimeInMillis();        	
        	long diff = milis2 - milis1;        	
        	Long diffMinutes = diff / (60 * 1000); 
        	return Integer.parseInt(diffMinutes.toString());
        }
        
        
        public int minutosInicio (Calendar fecha) {
        	int minutosNor = obtenerMinutos (rfNorActual, fecha);        	
        	if (minutosNor==ConstantesAdmin.NO_TIENE_MINUTOS) {
        		return ConstantesAdmin.NO_TIENE_MINUTOS;
        	}
        	
        	int minutos = 0;
        	int minutosEx = obtenerMinutos(rfExActual, fecha);
        	minutosEx = (minutosEx==ConstantesAdmin.NO_TIENE_MINUTOS ? 0 : minutosEx);
        	DateTime dtHora = new DateTime (fecha);

        	DateTime hiNorm = rfNorActual.crearHoraInicio(fecha);
        	DateTime hfNorm = rfNorActual.crearHoraFin(fecha);
        	
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
	        	DateTime hiEx = rfExActual.crearHoraInicio(fecha);
	        	DateTime hfEx = rfExActual.crearHoraFin(fecha);
	        	
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
            return minutos;
        }
        
        public int minutosFin (Calendar fecha) {
        	int minutosNor = obtenerMinutos (rfNorActual, fecha);        	
        	if (minutosNor==ConstantesAdmin.NO_TIENE_MINUTOS) {
        		return ConstantesAdmin.NO_TIENE_MINUTOS;
        	}
        	
        	int minutos = 0;
        	int minutosEx = obtenerMinutos(rfExActual, fecha);
        	minutosEx = (minutosEx==ConstantesAdmin.NO_TIENE_MINUTOS ? 0 : minutosEx);
        	DateTime dtHora = new DateTime (fecha);
        	
        	DateTime hiNorm = rfNorActual.crearHoraInicio(fecha);
        	DateTime hfNorm = rfNorActual.crearHoraFin(fecha);
        	
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
	        	DateTime hiEx = rfExActual.crearHoraInicio(fecha);
	        	DateTime hfEx = rfExActual.crearHoraFin(fecha);
	        	
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
        	
            return minutos;
        }
        
        private int obtenerMinutos (RangoFecha rangoFecha, Calendar fecha) {
        	int minutos = ConstantesAdmin.NO_TIENE_MINUTOS;
        	while (minutos==ConstantesAdmin.NO_TIENE_MINUTOS && rangoFecha!=null) {
        		minutos = rangoFecha.minutos(fecha);        		
        		if (minutos==ConstantesAdmin.NO_TIENE_MINUTOS) {
        			rangoFecha = rangoFecha.getSiguiente();
        		}
        	}
        	return minutos;
        }
    }
	
}
