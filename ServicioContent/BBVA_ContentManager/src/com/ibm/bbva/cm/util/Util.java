package com.ibm.bbva.cm.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.CalendarConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.bean.Documento;
import com.ibm.bbva.cm.bean.DocumentoPid;
import com.ibm.bbva.cm.exception.ICMException;

public class Util {
	private static final Logger logger = LoggerFactory.getLogger(Util.class);
	
	public static String IntegerCharToString(Integer integer) {
		int i = integer.intValue();
		char c = (char) i;
		Character character = new Character(c);
		return character.toString();
	}

	public static boolean nullStringAsDefaultBoolean(String o) {
		boolean retorno = false;
		if (o != null) {
			retorno = (o.trim().equals("1") ? Boolean.TRUE : Boolean.FALSE).booleanValue();
		}
		return retorno;
	}

	public static boolean nullCharacterAsDefaultBoolean(Character o) {
		boolean retorno = false;
		if (o != null) {
			retorno = (o.equals("1") ? Boolean.TRUE : Boolean.FALSE).booleanValue();
		}
		return retorno;
	}

	public static boolean nullIntegerAsDefaultBoolean(Integer o) {
		boolean retorno = false;
		if (o != null) {
			retorno = ((((char) o.intValue()) == '1') ? Boolean.TRUE
					: Boolean.FALSE).booleanValue();
		}
		return retorno;
	}

	public static String leerPropiedad(String nombreProp) {
		return ParametrosConfUtil.getInstance().getValue(nombreProp);
	}

	public static DocumentoPid translateToDocPid(Documento doc) throws ICMException {
		Date defaultValue = null;
		Calendar defaultCalendar = null;
		DateConverter converter = new DateConverter(defaultValue);
		CalendarConverter calendConv = new CalendarConverter(defaultCalendar);
		ConvertUtils.register(converter, java.util.Date.class);
		ConvertUtils.register(calendConv, Calendar.class);
		DocumentoPid docPid = new DocumentoPid();
		try {
			PropertyUtils.copyProperties(docPid, doc);
		} catch (IllegalAccessException e) {
			throw new ICMException(e);
		} catch (InvocationTargetException e) {
			throw new ICMException(e);
		} catch (NoSuchMethodException e) {
			throw new ICMException(e);
		}
		return docPid;
	}

	public static Documento translateToDoc(DocumentoPid docPid) {
		Documento doc = new Documento();
		try {
			PropertyUtils.copyProperties(doc, docPid);
		} catch (IllegalAccessException e) {
			logger.error("Error translateToDoc()", e);
		} catch (InvocationTargetException e) {
			logger.error("Error translateToDoc()", e);
		} catch (NoSuchMethodException e) {
			logger.error("Error translateToDoc()", e);
		}
		return doc;
	}

	public static Documento[] translateToDocs(DocumentoPid[] docspid) {
		Documento[] docs = null;
		if (docspid != null) {
			docs = new Documento[docspid.length];
			for (int i = 0; i < docspid.length; i++) {
				docs[i] = translateToDoc(docspid[i]);
			}
		}
		return docs;
	}

	public static DocumentoPid[] translateToPid(Documento[] docs) {
		DocumentoPid[] pids = null;
		if (docs != null) {
			pids = new DocumentoPid[docs.length];
			for (int i = 0; i < docs.length; i++) {
				pids[i] = translateToDocPid(docs[i]);
			}
		}
		return pids;
	}

	public static List<DocumentoPid> translateToPid(List<Documento> docs) {
		List<DocumentoPid> pids = null;
		if (docs != null) {
			pids = new ArrayList<DocumentoPid>();
			for (Documento doc : docs) {
				pids.add(translateToDocPid(doc));
			}
		}
		return pids;
	}
	
	public static List<Documento> translateToDocumentoList(
			List<DocumentoPid> pids) {
		List<Documento> docs = new ArrayList<Documento>();
		for (DocumentoPid pid : pids) {
			docs.add(translateToDoc(pid));
		}
		return docs;
	}
	
    public static String mostrarMensajeHTML(Exception e) {
        StackTraceElement[] stackTraceElement = null;
        StringBuilder trace = new StringBuilder();

        try {
            trace.append("Error: <b>");
            trace.append(e.getMessage());
            trace.append("</b><br/>Traza:<br/>");
            stackTraceElement = e.getStackTrace();

            if (stackTraceElement != null && stackTraceElement.length > 0) {
                for (StackTraceElement elem : stackTraceElement) {
                    trace.append(elem);
                    trace.append("<br/>");
                }
            }
        } catch (Exception ex) {
            trace = new StringBuilder(e.getMessage());
            logger.error("", e);
        }

        return trace.toString();
    }
}