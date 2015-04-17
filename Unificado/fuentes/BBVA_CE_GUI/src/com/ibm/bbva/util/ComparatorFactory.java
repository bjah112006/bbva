package com.ibm.bbva.util;

import java.util.Comparator;

import pe.ibm.bean.ExpedienteTCWPS;

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
				return comparator.compare(o1.getPerfilUsuarioAnterior(), o2.getPerfilUsuarioAnterior());
			}
		};
	}
	
	public static Comparator<ExpedienteTCWPS> usuario (String orden) {
		return new ComparatorBase<String, ExpedienteTCWPS> (orden){
			public int compare(ExpedienteTCWPS o1, ExpedienteTCWPS o2) {
				return comparator.compare(o1.getNombreUsuarioAnterior(), o2.getNombreUsuarioAnterior());
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
	
}
