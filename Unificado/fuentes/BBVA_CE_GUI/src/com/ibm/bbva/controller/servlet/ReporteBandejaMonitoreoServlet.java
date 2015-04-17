package com.ibm.bbva.controller.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.Consulta;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.TablaBandejaMonitoreoMB;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.LogErrores;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.session.DocumentoExpTcBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.EstadoCEBeanLocal;
import com.ibm.bbva.session.LogErroresBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.tabla.reporte.vo.DatosGeneradosConsVO;
import com.ibm.bbva.tabla.reporte.vo.DatosGeneradosHisVO;
import com.ibm.bbva.tabla.util.vo.ListaReportePorcentajeToe;
import com.ibm.bbva.tabla.util.vo.ListaReporteToe;
import com.ibm.bbva.tabla.util.vo.ListaReporteUnidadToe;
import com.ibm.bbva.util.Util;

@WebServlet("/ReporteBandejaMonitoreoServlet")
public class ReporteBandejaMonitoreoServlet extends HttpServlet {

	private static final long serialVersionUID = -5176056731599316979L;

	private static final Logger LOG = LoggerFactory.getLogger(ReporteBandejaMonitoreoServlet.class);

	@EJB
	private PerfilBeanLocal perfilBean;@EJB
	private EmpleadoBeanLocal empleadoBean;@EJB
	private TareaBeanLocal tareaBean;@EJB
	private EstadoCEBeanLocal estadoCEBean;@EJB
	private LogErroresBeanLocal logErroresBean;@EJB
	private DocumentoExpTcBeanLocal documentoExpTcBeanLocal;

	private List < SelectItem > roles;
	private List < SelectItem > tareas;
	private List < SelectItem > estados;

	private String nombreArchivo = "Reporte Bandeja Monitoreo";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReporteBandejaMonitoreoServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		LOG.debug("Llamada al servlet ReporteBandejaMonitoreoServlet ...");

		/*Carga Lista de Estado*/
		estados = Util.crearItems(estadoCEBean.buscarTodos(), true, "id", "descripcion");

		String fechaInicio = request.getParameter("fechaInicio") == null ? "" : request.getParameter("fechaInicio");
		String fechaFin = request.getParameter("fechaFin") == null ? "" : request.getParameter("fechaFin");
		String tareaSeleccionada = request.getParameter("tarea") == null ? "" : request.getParameter("tarea");
		String rolSeleccionado = request.getParameter("rol") == null ? "" : request.getParameter("rol");
		String usuarioSeleccionado = request.getParameter("usuario") == null ? "" : request.getParameter("usuario");
		String codigoExpediente = request.getParameter("expediente") == null ? "" : request.getParameter("expediente");
		String estadoSeleccionado = request.getParameter("estado") == null ? "" : request.getParameter("estado");
		String tipoBusqueda = request.getParameter("tipoBusqueda") == null ? "1" : request.getParameter("tipoBusqueda");

		List < ExpedienteTCWPS > lista = buscar(fechaInicio, fechaFin, tareaSeleccionada, rolSeleccionado, usuarioSeleccionado, codigoExpediente, estadoSeleccionado, tipoBusqueda);


		if (tipoBusqueda.equals("3")) {
			generarReporteBandejaMonitoreo_Content(lista, response);
		} else if (tipoBusqueda.equals("2")) {
			generarReporteBandejaMonitoreo_Process(lista, response);
		} else if (tipoBusqueda.equals("1")) {
			generarReporteBandejaMonitoreo_LogErrores(lista, response);
		}
	}

	private void generarReporteBandejaMonitoreo_LogErrores(List < ExpedienteTCWPS > lista, HttpServletResponse response) {

		// Defino el Libro de Excel
		HSSFWorkbook wb = new HSSFWorkbook();

		// Creo la Hoja en Excel
		HSSFSheet sheet = wb.createSheet("Bandeja Monitoreo");

		// quito las lineas del libro para darle un mejor acabado
		sheet.setDisplayGridlines(true);

		HSSFRow myRow = null;
		HSSFCell myCell = null;

		// Cabecera
		myRow = sheet.createRow(0);

		for (int j = 0; j <= 9; j++) {
			myCell = myRow.createCell(j);

			HSSFPalette palette = wb.getCustomPalette();
			palette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 34, (byte) 102, (byte) 187);

			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			HSSFFont cellFont = wb.createFont();
			cellFont.setFontHeightInPoints((short) 10);
			cellFont.setFontName(HSSFFont.FONT_ARIAL);
			cellFont.setColor(HSSFColor.WHITE.index);
			cellStyle.setFont(cellFont);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);

			myCell.setCellStyle(cellStyle);

			switch (j) {
				case 0:
					// Expediente
					myCell.setCellValue("Expediente");
					break;
				case 1:
					// Estado anterior
					myCell.setCellValue("Estado");
					break;
				case 2:
					// Perfil actual
					myCell.setCellValue("Rol");
					break;
				case 3:
					// Tarea
					myCell.setCellValue("Tarea");
					break;
				case 4:
					// Registro
					myCell.setCellValue("Registro");
					break;
				case 5:
					// Usuario
					myCell.setCellValue("Usuario");
					break;
				case 6:
					// Fecha de incidente
					myCell.setCellValue("Fecha de Incidencia");
					break;
				case 7:
					// Tipo de Error
					myCell.setCellValue("Tipo de Error");
					break;
				case 8:
					// Fecha de restauración
					myCell.setCellValue("Fecha de Restauración");
					break;
				case 9:
					// Fecha de cancelación
					myCell.setCellValue("Fecha de Cancelación");
					break;
			}
		}


		// Cuerpo
		int i = 1;

		for (ExpedienteTCWPS expedienteTCWPS: lista) {

			myRow = sheet.createRow(i);

			for (int j = 0; j <= 9; j++) {
				myCell = myRow.createCell(j);

				switch (j) {
					case 0:
						// Expediente
						myCell.setCellValue(expedienteTCWPS.getCodigo());
						break;
					case 1:
						// Estado anterior
						myCell.setCellValue(expedienteTCWPS.getEstadoAnterior());
						break;
					case 2:
						// Perfil actual
						myCell.setCellValue(expedienteTCWPS.getPerfilUsuarioActual());
						break;
					case 3:
						// Tarea
						myCell.setCellValue(expedienteTCWPS.getDesTarea());
						break;
					case 4:
						// Registro
						myCell.setCellValue(expedienteTCWPS.getCodigoUsuarioAnterior());
						break;
					case 5:
						// Usuario
						myCell.setCellValue(expedienteTCWPS.getNombreUsuarioActual());
						break;
					case 6:
						// Fecha de incidente
						myCell.setCellValue(expedienteTCWPS.getFechaIncidencia());
						break;
					case 7:
						// Tipo de Error
						myCell.setCellValue(expedienteTCWPS.getTipoError());
						break;
					case 8:
						// Fecha de restauración
						myCell.setCellValue(expedienteTCWPS.getFechaRestauracion());
						break;
					case 9:
						// Fecha de cancelación
						myCell.setCellValue(expedienteTCWPS.getFechaCancelacion());
						break;
				}
			}

			i++;
		}

		HSSFSheet ssheet = wb.getSheetAt(0);
		ssheet.autoSizeColumn(0);
		ssheet.autoSizeColumn(1);
		ssheet.autoSizeColumn(2);
		ssheet.autoSizeColumn(3);
		ssheet.autoSizeColumn(4);
		ssheet.autoSizeColumn(5);
		ssheet.autoSizeColumn(6);
		ssheet.autoSizeColumn(7);
		ssheet.autoSizeColumn(8);
		ssheet.autoSizeColumn(9);

		response.setHeader("Cache-Control", "private");
		response.setHeader("Pragma", "private");
		response.addDateHeader("Expires", 0);
		response.setContentType("application/vnd.ms-excel");

		response.addHeader("Content-Disposition", "filename=" + nombreArchivo + ".xls");
		try {
			wb.write(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void generarReporteBandejaMonitoreo_Process(List < ExpedienteTCWPS > lista, HttpServletResponse response) {

		// Defino el Libro de Excel
		HSSFWorkbook wb = new HSSFWorkbook();

		// Creo la Hoja en Excel
		HSSFSheet sheet = wb.createSheet("Bandeja Monitoreo - Process");

		// quito las lineas del libro para darle un mejor acabado
		sheet.setDisplayGridlines(true);

		HSSFRow myRow = null;
		HSSFCell myCell = null;

		// Cabecera
		myRow = sheet.createRow(0);

		for (int j = 0; j <= 9; j++) {
			myCell = myRow.createCell(j);

			HSSFPalette palette = wb.getCustomPalette();
			palette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 34, (byte) 102, (byte) 187);

			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			HSSFFont cellFont = wb.createFont();
			cellFont.setFontHeightInPoints((short) 10);
			cellFont.setFontName(HSSFFont.FONT_ARIAL);
			cellFont.setColor(HSSFColor.WHITE.index);
			cellStyle.setFont(cellFont);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);

			myCell.setCellStyle(cellStyle);

			switch (j) {
				case 0:
					// Expediente
					myCell.setCellValue("Expediente");
					break;
				case 1:
					// Estado anterior
					myCell.setCellValue("Estado");
					break;
				case 2:
					// Perfil actual
					myCell.setCellValue("Rol");
					break;
				case 3:
					// Tarea
					myCell.setCellValue("Tarea");
					break;
				case 4:
					// Registro
					myCell.setCellValue("Registro");
					break;
				case 5:
					// Usuario
					myCell.setCellValue("Usuario");
					break;
				case 6:
					// Fecha de incidente
					myCell.setCellValue("Fecha de Incidente");
					break;
				case 7:
					// Tipo de Error
					myCell.setCellValue("# Reintentos Automáticos");
					break;
				case 8:
					// Fecha de restauración
					myCell.setCellValue("Tipo de Error");
					break;
				case 9:
					// Fecha de cancelación
					myCell.setCellValue("Descripción del Error");
					break;
			}
		}


		// Cuerpo
		int i = 1;

		for (ExpedienteTCWPS expedienteTCWPS: lista) {

			myRow = sheet.createRow(i);

			for (int j = 0; j <= 9; j++) {
				myCell = myRow.createCell(j);

				switch (j) {
					case 0:
						// Expediente
						myCell.setCellValue(expedienteTCWPS.getCodigo());
						break;
					case 1:
						// Estado anterior
						myCell.setCellValue(expedienteTCWPS.getEstadoAnterior());
						break;
					case 2:
						// Perfil actual
						myCell.setCellValue(expedienteTCWPS.getPerfilUsuarioActual());
						break;
					case 3:
						// Tarea
						myCell.setCellValue(expedienteTCWPS.getDesTarea());
						break;
					case 4:
						// Registro
						myCell.setCellValue(expedienteTCWPS.getCodigoUsuarioAnterior());
						break;
					case 5:
						// Usuario
						myCell.setCellValue(expedienteTCWPS.getNombreUsuarioActual());
						break;
					case 6:
						// Fecha de incidente
						myCell.setCellValue(expedienteTCWPS.getFechaIncidencia());
						break;
					case 7:
						// # Reintentos Automáticos
						myCell.setCellValue(expedienteTCWPS.getNroReintentos());
						break;
					case 8:
						// Tipo de Error
						myCell.setCellValue(expedienteTCWPS.getTipoError());
						break;
					case 9:
						// Descripción del Error
						myCell.setCellValue(expedienteTCWPS.getDescripcionError());
						break;
				}
			}

			i++;
		}

		HSSFSheet ssheet = wb.getSheetAt(0);
		ssheet.autoSizeColumn(0);
		ssheet.autoSizeColumn(1);
		ssheet.autoSizeColumn(2);
		ssheet.autoSizeColumn(3);
		ssheet.autoSizeColumn(4);
		ssheet.autoSizeColumn(5);
		ssheet.autoSizeColumn(6);
		ssheet.autoSizeColumn(7);
		ssheet.autoSizeColumn(8);
		ssheet.autoSizeColumn(9);

		response.setHeader("Cache-Control", "private");
		response.setHeader("Pragma", "private");
		response.addDateHeader("Expires", 0);
		response.setContentType("application/vnd.ms-excel");

		response.addHeader("Content-Disposition", "filename=" + nombreArchivo + ".xls");

		try {
			wb.write(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void generarReporteBandejaMonitoreo_Content(List < ExpedienteTCWPS > lista, HttpServletResponse response) {

		// Defino el Libro de Excel
		HSSFWorkbook wb = new HSSFWorkbook();

		// Creo la Hoja en Excel
		HSSFSheet sheet = wb.createSheet("Bandeja Monitoreo - Content");

		// quito las lineas del libro para darle un mejor acabado
		sheet.setDisplayGridlines(true);

		HSSFRow myRow = null;
		HSSFCell myCell = null;

		// Cabecera
		myRow = sheet.createRow(0);

		for (int j = 0; j <= 9; j++) {
			myCell = myRow.createCell(j);

			HSSFPalette palette = wb.getCustomPalette();
			palette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 34, (byte) 102, (byte) 187);

			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			HSSFFont cellFont = wb.createFont();
			cellFont.setFontHeightInPoints((short) 10);
			cellFont.setFontName(HSSFFont.FONT_ARIAL);
			cellFont.setColor(HSSFColor.WHITE.index);
			cellStyle.setFont(cellFont);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);

			myCell.setCellStyle(cellStyle);

			switch (j) {
				case 0:
					// Expediente
					myCell.setCellValue("Expediente");
					break;
				case 1:
					// Estado anterior
					myCell.setCellValue("Estado");
					break;
				case 2:
					// Perfil actual
					myCell.setCellValue("Rol");
					break;
				case 3:
					// Tarea
					myCell.setCellValue("Tarea");
					break;
				case 4:
					// Registro
					myCell.setCellValue("Registro");
					break;
				case 5:
					// Usuario
					myCell.setCellValue("Usuario");
					break;
				case 6:
					// Fecha de incidente
					myCell.setCellValue("Fecha de Incidente");
					break;
				case 7:
					// Tipo de Error
					myCell.setCellValue("Tipo de Error");
					break;
				case 8:
					// Cantidad de documentos subidos
					myCell.setCellValue("Cantidad de Documentos Subidos");
					break;
			}
		}


		// Cuerpo
		int i = 1;

		for (ExpedienteTCWPS expedienteTCWPS: lista) {

			myRow = sheet.createRow(i);

			for (int j = 0; j <= 9; j++) {
				myCell = myRow.createCell(j);

				switch (j) {
					case 0:
						// Expediente
						myCell.setCellValue(expedienteTCWPS.getCodigo());
						break;
					case 1:
						// Estado anterior
						myCell.setCellValue(expedienteTCWPS.getEstadoAnterior());
						break;
					case 2:
						// Perfil actual
						myCell.setCellValue(expedienteTCWPS.getPerfilUsuarioActual());
						break;
					case 3:
						// Tarea
						myCell.setCellValue(expedienteTCWPS.getDesTarea());
						break;
					case 4:
						// Registro
						myCell.setCellValue(expedienteTCWPS.getCodigoUsuarioAnterior());
						break;
					case 5:
						// Usuario
						myCell.setCellValue(expedienteTCWPS.getNombreUsuarioActual());
						break;
					case 6:
						// Fecha de incidente
						myCell.setCellValue(expedienteTCWPS.getFechaIncidencia());
						break;
					case 7:
						// Tipo de Error
						myCell.setCellValue(expedienteTCWPS.getTipoError());
						break;
					case 8:
						// Cantidad de Documentos Subidos
						myCell.setCellValue(expedienteTCWPS.getCantDocumentos());
						break;
				}
			}

			i++;
		}

		HSSFSheet ssheet = wb.getSheetAt(0);
		ssheet.autoSizeColumn(0);
		ssheet.autoSizeColumn(1);
		ssheet.autoSizeColumn(2);
		ssheet.autoSizeColumn(3);
		ssheet.autoSizeColumn(4);
		ssheet.autoSizeColumn(5);
		ssheet.autoSizeColumn(6);
		ssheet.autoSizeColumn(7);
		ssheet.autoSizeColumn(8);

		response.setHeader("Cache-Control", "private");
		response.setHeader("Pragma", "private");
		response.addDateHeader("Expires", 0);
		response.setContentType("application/vnd.ms-excel");

		response.addHeader("Content-Disposition", "filename=" + nombreArchivo + ".xls");

		try {
			wb.write(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List < ExpedienteTCWPS > buscar(String fechaInicio,
	String fechaFin,
	String tareaSeleccionada,
	String rolSeleccionado,
	String usuarioSeleccionado,
	String codigoExpediente,
	String estadoSeleccionado,
	String tipoBusqueda) {

		if (fechaInicio.equals("")) fechaInicio = null;

		if (fechaFin.equals("")) fechaFin = null;

		if (tareaSeleccionada.equals("-1")) {
			tareaSeleccionada = "";
		}
		if (rolSeleccionado.equals("-1")) {
			rolSeleccionado = "";
		}
		if (usuarioSeleccionado.equals("-1")) {
			usuarioSeleccionado = "";
		}
		if (estadoSeleccionado.equals("-1")) {
			estadoSeleccionado = "";
		}

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		Date fechaInicioDate = null;
		Date fechaFinDate = null;

		try {
			if (fechaInicio != null) fechaInicioDate = formatter.parse(fechaInicio);

			if (fechaFin != null) fechaFinDate = formatter.parse(fechaFin);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		Consulta consulta = new Consulta();
		consulta.setFechaInicio(fechaInicioDate);
		consulta.setFechaFin(fechaFinDate);
		consulta.setIdTarea(tareaSeleccionada);
		consulta.setIdPerfilUsuarioActual(rolSeleccionado);
		consulta.setCodUsuarioActual(usuarioSeleccionado);
		consulta.setCodigoExpediente(codigoExpediente);
		consulta.setEstado(Util.obtenerDescripcion(estados, estadoSeleccionado));
		consulta.setTipoConsulta(Integer.parseInt(tipoBusqueda));

		List < ExpedienteTCWPS > listTabla = new ArrayList < ExpedienteTCWPS > ();

		if ("1".equals(tipoBusqueda)) {
			// busqueda en base de datos
			List < LogErrores > listLogErrores = new ArrayList < LogErrores > ();
			listLogErrores = logErroresBean.buscarLogErrores(fechaInicioDate, fechaFinDate, rolSeleccionado, usuarioSeleccionado, tareaSeleccionada, estadoSeleccionado, codigoExpediente);
			for (LogErrores logErrores: listLogErrores) {
				ExpedienteTCWPS expedienteTCWPS = new ExpedienteTCWPS();
				//tablaMonitoreo.setCantidadDocumentos(String.valueOf(logErrores.getCantidadDocumentos())); -- no mostrar en la grilla
				expedienteTCWPS.setCodigo(String.valueOf(logErrores.getExpediente().getId()));

				if (logErrores.getEmpleado() != null) {
					expedienteTCWPS.setCodigoUsuarioAnterior(logErrores.getEmpleado().getCodigo());
					expedienteTCWPS.setNombreUsuarioActual(logErrores.getEmpleado().getNombresCompletos());
				}

				//expedienteTCWPS.setDescripcionError(logErrores.getDescripcionError()); -- no mostrar en la grilla
				expedienteTCWPS.setPerfilUsuarioActual(logErrores.getPerfil().getDescripcion());
				expedienteTCWPS.setDesTarea(logErrores.getTarea().getDescripcion());
				expedienteTCWPS.setEstadoAnterior(logErrores.getEstadoCE().getDescripcion());

				String fechaCancelacion = logErrores.getFechaCancelacion() == null ? "" : Util.parseDateString(logErrores.getFechaCancelacion(), "dd/MM/yyyy");
				expedienteTCWPS.setFechaCancelacion(fechaCancelacion);

				String fechaRestauracion = logErrores.getFechaRestauracion() == null ? "" : Util.parseDateString(logErrores.getFechaRestauracion(), "dd/MM/yyyy");
				expedienteTCWPS.setFechaRestauracion(fechaRestauracion);

				String fechaIncidencia = logErrores.getFechaIncidente() == null ? "" : Util.parseDateString(logErrores.getFechaIncidente(), "dd/MM/yyyy");
				expedienteTCWPS.setFechaIncidencia(fechaIncidencia);

				//expedienteTCWPS.setNroReintentos(String.valueOf(logErrores.getExpediente().getId())); -- no mostrar en la grilla
				expedienteTCWPS.setTipoError(logErrores.getTipoError());

				listTabla.add(expedienteTCWPS);
			}

		} else {
			RemoteUtils remoteUtils = new RemoteUtils();
			listTabla = remoteUtils.obtenerListaTareasGestion(consulta);
			for (ExpedienteTCWPS expedienteTCWPS: listTabla) {
				if ("2".equals(tipoBusqueda)) {
					expedienteTCWPS.setTipoError("PROCESS");
				} else if ("3".equals(tipoBusqueda)) {
					expedienteTCWPS.setTipoError("CONTENT");
					// se calcula la cantidad de documentos subidos
					List < DocumentoExpTc > listDocumentoExpTc = documentoExpTcBeanLocal.buscarPorExpedienteFlagEscaneado(Long.parseLong(expedienteTCWPS.getCodigo()), "1");
					int cantidadSubidos = 0;
					for (DocumentoExpTc documentoExpTc: listDocumentoExpTc) {
						if (documentoExpTc.getIdCm() != null && documentoExpTc.getIdCm().intValue() > 0) {
							cantidadSubidos++;
						}
					}
					expedienteTCWPS.setCantDocumentos("" + cantidadSubidos);
				}
				if (expedienteTCWPS.getActivado() != null) {
					try {
						Calendar activado = expedienteTCWPS.getActivado();
						SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						String formatted = format1.format(activado.getTime());
						expedienteTCWPS.setFechaIncidencia(formatted);
					} catch (Exception e) {
						expedienteTCWPS.setFechaIncidencia("");
					}
				}
				Tarea tarea = tareaBean.buscarPorId(expedienteTCWPS.getIdTareaAnterior());
				if (tarea != null) expedienteTCWPS.setDesTarea(tarea.getDescripcion());
			}
		}

		List < ExpedienteTCWPS > resultado = listTabla;

		return resultado;
	}

}