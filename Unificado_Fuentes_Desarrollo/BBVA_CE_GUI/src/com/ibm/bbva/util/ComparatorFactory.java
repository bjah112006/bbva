package com.ibm.bbva.util;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import pe.ibm.bean.ExpedienteTCWPS;

import com.ibm.bbva.messages.Mensajes;
import com.ibm.bbva.tabla.util.vo.HistorialDetalle;
import com.ibm.bbva.util.comparators.ComparatorBase;

public class ComparatorFactory {
	
	public static Comparator<ExpedienteTCWPS> codigo (String orden) {
		return new ComparatorBase<Integer, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(new Integer(o1.getCodigo()), new Integer (o2.getCodigo()));
			}
		};
	}
	
	public static Comparator<HistorialDetalle> codigoHistorial (String orden) {
		return new ComparatorBase<Long, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getIdExpediente(), o2.getIdExpediente());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> estado (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getEstado(), o2.getEstado());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> estadoHistorial (String orden) {
		return new ComparatorBase<String, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getEstado(), o2.getEstado());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> rol (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getPerfilUsuarioActual(), o2.getPerfilUsuarioActual());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> usuario (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getCodigoUsuarioActual(), o2.getCodigoUsuarioActual());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> tarea (String orden) {
		return new ComparatorBase<Integer, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				String idTarea1 = o1.getIdTarea();
				String idTarea2 = o2.getIdTarea();
				idTarea1 = (idTarea1.trim().equals("")) ? "0" : idTarea1;
				idTarea2 = (idTarea2.trim().equals("")) ? "0" : idTarea2;
				return comparator.compare(new Integer(idTarea1), new Integer (idTarea2));
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> segmento (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getSegmento(), o2.getSegmento());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> segmentoHistorial (String orden) {
		return new ComparatorBase<String, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getSegmento(), o2.getSegmento());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> tipoOferta (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getTipoOferta(), o2.getTipoOferta());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> tipoOfertaHistorial (String orden) {
		return new ComparatorBase<String, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getTipoOferta(), o2.getTipoOferta());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> tipoDOI (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getCliente().getTipoDOI(), o2.getCliente().getTipoDOI());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> tipoDOIHistorial (String orden) {
		return new ComparatorBase<String, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getTipoDOI(), o2.getTipoDOI());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> numeroDOI (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getCliente().getNumeroDOI(), o2.getCliente().getNumeroDOI());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> numeroDOIHistorial (String orden) {
		return new ComparatorBase<String, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getNumeroDOI(), o2.getNumeroDOI());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> apPaterno (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getCliente().getApPaterno(), o2.getCliente().getApPaterno());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> apPaternoHistorial (String orden) {
		return new ComparatorBase<String, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getApPaterno(), o2.getApPaterno());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> apMaterno (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getCliente().getApMaterno(), o2.getCliente().getApMaterno());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> apMaternoHistorial (String orden) {
		return new ComparatorBase<String, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getApMaterno(), o2.getApMaterno());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> nombre (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getCliente().getNombre(), o2.getCliente().getNombre());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> nombreHistorial (String orden) {
		return new ComparatorBase<String, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getNombre(), o2.getNombre());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> producto (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getProducto().getProducto(), o2.getProducto().getProducto());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> productoHistorial (String orden) {
		return new ComparatorBase<String, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getProducto(), o2.getProducto());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> subProducto (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getProducto().getSubProducto(), o2.getProducto().getSubProducto());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> subProductoHistorial (String orden) {
		return new ComparatorBase<String, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getSubProducto(), o2.getSubProducto());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> moneda (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getMoneda(), o2.getMoneda());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> monedaHistorial (String orden) {
		return new ComparatorBase<String, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getTipoMonedaSolicitada(), o2.getTipoMonedaSolicitada());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> fechaRegistro (String orden) {
		return new ComparatorBase<Date, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(((ExpedienteTCWrapper)o1).getFechaActivado(), ((ExpedienteTCWrapper)o2).getFechaActivado());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> nomUsuarioAsig (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(((ExpedienteTCWrapper)o1).getNombreCompletoUsuarioActual(), ((ExpedienteTCWrapper)o2).getNombreCompletoUsuarioActual());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> nomUsuario (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(((ExpedienteTCWrapper)o1).getNombreUsuarioActual(), ((ExpedienteTCWrapper)o2).getNombreUsuarioActual());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> lineaCredito (String orden) {
		return new ComparatorBase<Double, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getLineaCredito(), o2.getLineaCredito());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> montoAprobado (String orden) {
		return new ComparatorBase<Double, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getMontoAprobado(), o2.getMontoAprobado());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> oficina (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(((ExpedienteTCWrapper)o1).getOficina(), ((ExpedienteTCWrapper)o2).getOficina());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> territorio (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(((ExpedienteTCWrapper)o1).getTerritorio(), ((ExpedienteTCWrapper)o2).getTerritorio());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> codigoRVGL (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getCodigoRVGL(), o2.getCodigoRVGL());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> contrato (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getNumeroContrato(), o2.getNumeroContrato());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> observacion (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getObservacion(), o2.getObservacion());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> lineaCreditoSolicitadoHistorial (String orden) {
		return new ComparatorBase<Double, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getLineaCreditoSolicitado(), o2.getLineaCreditoSolicitado());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> lineaCreditoAprobadoHistorial (String orden) {
		return new ComparatorBase<Double, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getLineaCreditoAprobado(), o2.getLineaCreditoAprobado());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> oficinaHistorial (String orden) {
		return new ComparatorBase<String, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getOficina(), o2.getOficina());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> territorioHistorial (String orden) {
		return new ComparatorBase<String, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getTerritorio(), o2.getTerritorio());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> codigoRGVLHistorial (String orden) {
		return new ComparatorBase<String, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getCodigoRGVL(), o2.getCodigoRGVL());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> numeroContratoHistorial (String orden) {
		return new ComparatorBase<String, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getNumeroContrato(), o2.getNumeroContrato());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> observacionHistorial (String orden) {
		return new ComparatorBase<String, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				return comparator.compare(o1.getObservacion(), o2.getObservacion());
			}
		};
	}
	
	public static Comparator<HistorialDetalle> fechaRegistroHistorial (String orden) {
		return new ComparatorBase<Date, HistorialDetalle> (orden){
			public int compare(HistorialDetalle o1, HistorialDetalle o2) {
				SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy hh:mm:ss a");
				Date date1 = null;
				Date date2 = null;
				try {
					date1 = sdf.parse(o1.getFechaRegistro());
				} catch (Exception e) {
					date1 = null;
				}
				try {
					date2 = sdf.parse(o2.getFechaRegistro());
				} catch (Exception e) {
					date2 = null;
				}
				return comparator.compare(date1, date2);
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> alerta (String orden) {
		return new ComparatorBase<Integer, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				String color1 = ((ExpedienteTCWrapper)o1).getUrlColor();
				String color2 = ((ExpedienteTCWrapper)o2).getUrlColor();
				
				return comparator.compare(colorToInt(color1), colorToInt(color2));
			}
		};
	}
	
	private static Integer colorToInt(String color) {
		if (color == null || color.trim().equals("")) {
			return 0;
		} else if (color.indexOf("rojo") >= 0) {
			return 1;
		} else if (color.indexOf("amarillo") >= 0) {
			return 2;
		} else if (color.indexOf("verde") >= 0) {
			return 3;
		}
		return 0;
	}
	
	public static Comparator<ExpedienteTCWPS> estadoTarjeta (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				String estadoTarjeta1 = null;
				String estadoTarjeta2 = null;
				if (((ExpedienteTCWrapper) o1).isRenderEstadoTarea()) {
					estadoTarjeta1 = ((ExpedienteTCWrapper) o1).getEstadoTarjeta();
				}
				if (((ExpedienteTCWrapper) o2).isRenderEstadoTarea()) {
					estadoTarjeta2 = ((ExpedienteTCWrapper) o2).getEstadoTarjeta();
				}
				return comparator.compare(estadoTarjeta1, estadoTarjeta2);
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> estadoArchivos (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				String estadoArchivos1 = null;
				String estadoArchivos2 = null;
				if (((ExpedienteTCWrapper) o1).isTarea28()) {
					estadoArchivos1 = Mensajes.getMensaje("com.ibm.bbva.common.tablaBandejaAsig.errorArchivos");
				}
				if (((ExpedienteTCWrapper) o2).isTarea28()) {
					estadoArchivos2 = Mensajes.getMensaje("com.ibm.bbva.common.tablaBandejaAsig.errorArchivos");
				}
				return comparator.compare(estadoArchivos1, estadoArchivos2);
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> devoluciones (String orden) {
		return new ComparatorBase<Integer, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(((ExpedienteTCWrapper) o1).getNroDevoluciones(), ((ExpedienteTCWrapper) o2).getNroDevoluciones());
			}
		};
	}
	
}
