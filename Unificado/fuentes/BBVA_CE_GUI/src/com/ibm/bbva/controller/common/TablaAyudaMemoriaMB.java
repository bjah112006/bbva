package com.ibm.bbva.controller.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractTablaMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.ConstantesAdmin;
import com.ibm.bbva.entities.AyudaMemoria;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.MotivoDevolucion;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.entities.TareaPerfil;
import com.ibm.bbva.session.AyudaMemoriaBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.tabla.util.vo.TablaAyudaMemoria;
import com.ibm.bbva.util.Util;
import com.ibm.ws.webservices.xml.waswebservices.parameter;
import com.ibm.xtq.ast.nodes.ValueOf;

@SuppressWarnings("serial")
@ManagedBean(name="tablaAyudaMemoria")
@RequestScoped
public class TablaAyudaMemoriaMB extends AbstractTablaMBean {
	
	@EJB
	private AyudaMemoriaBeanLocal ayudaMemoriaBean;
	@EJB
	private TareaBeanLocal tareaBean;
	//private EmpleadoBeanLocal empleadoBean;
	private Empleado empleado;
	private Expediente expediente;
	private Historial historial;
	private HtmlDataTable tablaBinding;
	private List<TablaAyudaMemoria> listTabla;
	private AyudaMemoria ayudaMemoria;
	private List<AyudaMemoria> listAyudaMemoria;
	private List<AyudaMemoria> listAyudaMemoriaSort;
	private boolean mostrarPaneles;
	private HtmlInputText htmlCodigoAyudaMemoriaEliminar;
	private String idAyudaEliminar;
	private String strCodigoAyudaMemoriaEliminar;
	private HtmlCommandButton btnEliminarAyudaMemoria;
	private HtmlCommandButton btnEditarAyudaMemoria;
	private HtmlCommandButton btnDesasociarEditarAyudaMemoria;
	private String strCodigoAyudaMemoriaEditar;
	private HtmlInputText htmlCodigoAyudaMemoriaEditar;
	private String flagEditar;
	private HtmlInputHidden inputFlagEditar;
	private String comandoSesion;
	private boolean renderedEliminarAyudaMemoria;
	private boolean renderedEditarAyudaMemoria;
	private String renderedTablaAyudaMemoria;
	
	private static final Logger LOG = LoggerFactory.getLogger(TablaAyudaMemoriaMB.class);

	public TablaAyudaMemoriaMB() {
	}
	
	@PostConstruct
	public void init() {
		String nombJSP = getNombreJSPPrincipal();
		LOG.info("nombJSP : "+nombJSP);
		String frmJSP=nombJSP.substring(4, nombJSP.length());
		LOG.info("frmJSP : "+frmJSP);
		renderedTablaAyudaMemoria="frm"+frmJSP+":"+"tablaAyuMem";
		LOG.info("renderedTablaAyudaMemoria : "+renderedTablaAyudaMemoria);
		empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION); 
		/*String idPerfil = String.valueOf(empleado.getPerfil().getId()); 			
		if (idPerfil.equals(String.valueOf(Constantes.ID_PERFIL_EJECUTIVO)) && !nombJSP.equals("formVisualizarExpediente")  ) {
			renderedEliminarAyudaMemoria=true;
			renderedEditarAyudaMemoria=true;
		}else if (nombJSP.equals("formVisualizarExpediente") || !idPerfil.equals(Constantes.ID_PERFIL_EJECUTIVO) ) {
			renderedEliminarAyudaMemoria=false;
			renderedEditarAyudaMemoria=false;
		}*/
			
		obtenerDatos();
        habilitarAyudaMemoriaRol();
	}
	
	public void obtenerDatos() {

		/*Obtiene Datos de Expediente*/
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		ayudaMemoria = new AyudaMemoria();
		listAyudaMemoriaSort = new ArrayList<AyudaMemoria>();

		//Util.initEntityFK(ayudaMemoria,new ArrayList());		
		if (expediente != null && expediente.getId() > 0) {
			listAyudaMemoria = ayudaMemoriaBean.buscarPorIdExpediente(expediente.getId());				
			listAyudaMemoriaSort.addAll(listAyudaMemoria);
			listAyudaMemoria = new ArrayList<AyudaMemoria> ();
			//Collections.sort(listAyudaMemoriaSort, new ComparadorAyudaMemoria());			
			listAyudaMemoria.addAll(listAyudaMemoriaSort);
			List<AyudaMemoria> listAyudaMemoriaBD = (List<AyudaMemoria>)getObjectSession("listaBD");
			if(listAyudaMemoriaBD == null){
				addObjectSession("listaBD", listAyudaMemoria);
			}
		} else {
			listAyudaMemoria = (List<AyudaMemoria>)getObjectSession(Constantes.AYUDA_MEMORIA_SESION);
			if (listAyudaMemoria==null) {
				listAyudaMemoria = new ArrayList<AyudaMemoria> ();
				
				addObjectSession(Constantes.AYUDA_MEMORIA_SESION, listAyudaMemoria);
			}
		}		
		iniciar(Constantes.PAGINA_ACTUAL_AYUDA_MEMORIA, listAyudaMemoria.size(), 
				Constantes.FILAS_TABLA);
		
	}
	
	protected void mostrarTabla(int indiceInicioFila, int filas) { 
		if (getFilas()>0) {
			listTabla=(List<TablaAyudaMemoria>)getObjectSession(Constantes.AYUDA_MEMORIA_SESION3);
			listTabla = new ArrayList<TablaAyudaMemoria> ();
			int indiceFilaFinal = indiceInicioFila + filas;
			for(int i=indiceInicioFila, n=listAyudaMemoria.size(); i<n && i<indiceFilaFinal; i++){
				TablaAyudaMemoria taMemoria = crearTablaAyudaMemoria (listAyudaMemoria.get(i));
				if (listAyudaMemoria.get(i).getEstado()==null){
					listTabla.add(taMemoria);
				}
			}
		}
	}

	private TablaAyudaMemoria crearTablaAyudaMemoria(AyudaMemoria ayuMemVO) {
		TablaAyudaMemoria tabla = new TablaAyudaMemoria();
		tabla.setCodigoUsuario(ayuMemVO.getEmpleado().getCodigo());
		tabla.setPerfilUsuario(ayuMemVO.getEmpleado().getPerfil().getDescripcion());
		SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy hh:mm:ss a"); 
		tabla.setFechaHora(sdf.format(ayuMemVO.getFecRegistro()));
		tabla.setComentario(ayuMemVO.getComentario()); 
		tabla.setIdAyudaMemoria(String.valueOf(ayuMemVO.getId()));
		tabla.setColEliminar("");
		// validacion de la edicion del item
		if(ayuMemVO.getFecRegistro() != null){
			tabla.setVisOpElimina(true);
			tabla.setVisOpModificar(true);
		}
		boolean editable = true;
		boolean expedienteGrabar = false;
		if(expediente != null && expediente.getId() > 0){
			String accion = expediente.getAccion() == null ? "" : expediente.getAccion().toUpperCase().trim();
			if(accion.equals("GRABAR")){
				expedienteGrabar = true;
			}
		}
		
		List<AyudaMemoria> listAyudaMemoriaBD = (List<AyudaMemoria>)getObjectSession("listaBD");
		if(listAyudaMemoriaBD != null){
			for(AyudaMemoria ayudaMemoriaBD : listAyudaMemoriaBD){
				if(ayudaMemoriaBD.getId() == ayuMemVO.getId() && !expedienteGrabar){
					editable = false;
					break;
				}
			}
		}
		if(!editable){
			tabla.setVisOpElimina(true);
			tabla.setVisOpModificar(true);
		}
		else{
			tabla.setVisOpElimina(false);
			tabla.setVisOpModificar(false);
		}
		return tabla;
	}
	
	public void borrarElemento(TablaAyudaMemoria item){
		int indiceListTabla=0;
		int indiceListAyudaMemoria=0;
		indiceListTabla=listTabla.indexOf(item);
		LOG.info("INDEX : " + indiceListTabla);
		item.setColEliminar(Constantes.CONSTANTE_ELIMINACION);
		if (expediente!=null && expediente.getId()>0){
			ayudaMemoria.setId(Long.parseLong(item.getIdAyudaMemoria()));
			ayudaMemoria.setComentario(item.getComentario());
			ayudaMemoria.setEstado(item.getColEliminar());
			listAyudaMemoria = ayudaMemoriaBean.buscarPorIdExpediente(expediente.getId());
			List<AyudaMemoria> listaAyudaMemoria = new ArrayList<AyudaMemoria>(listAyudaMemoria);
			listTabla.remove(indiceListTabla);
			indiceListAyudaMemoria=indiceListTabla;
			LOG.info("listTabla : " +listAyudaMemoria.get(indiceListAyudaMemoria).getComentario().toString());
			listaAyudaMemoria.remove(indiceListAyudaMemoria);
			listAyudaMemoria=listaAyudaMemoria;
			//ayudaMemoriaBean.update(ayudaMemoria); 
			ayudaMemoriaBean.delete(ayudaMemoria);
			mostrarTabla(0, Constantes.FILAS_TABLA);
			
			//String idAyudaMemoria = String.valueOf(ayudaMemoria.getId());	
			//addObjectSession(ConstantesAdmin.FLAG_AYUDA_MEMORIA_SESION, "editarAyudaMemoria");
			//addObjectSession(ConstantesAdmin.AYUDA_MEMORIA_SESION2, idAyudaMemoria);
			
		}else{
			ayudaMemoria.setId(Long.parseLong(item.getIdAyudaMemoria()));
			ayudaMemoria.setComentario(item.getComentario());
			ayudaMemoria.setEstado(item.getColEliminar());
			listTabla.remove(indiceListTabla);
			indiceListAyudaMemoria=indiceListTabla;
			LOG.info("listTabla : " +listAyudaMemoria.get(indiceListAyudaMemoria).getComentario().toString());
			listAyudaMemoria.remove(indiceListAyudaMemoria);
			
		}
		//String idAyudaMemoria = String.valueOf(ayudaMemoria.getId());	
		//addObjectSession(ConstantesAdmin.FLAG_AYUDA_MEMORIA_SESION, "editarAyudaMemoria");
		//addObjectSession(ConstantesAdmin.AYUDA_MEMORIA_SESION2, idAyudaMemoria);
	}
	
	public AyudaMemoria obtenerDatoEditarAyudaMemoria(AjaxBehaviorEvent event) {
		/*Obtiene Datos de AyudaMemoria*/			
		String nuevoAyudaMemoria= (String)getObjectSession(Constantes.AYUDA_MEMORIA_NUEVO);	
		AyudaMemoria tabla = new AyudaMemoria();
		if(nuevoAyudaMemoria!=null){			
			ayudaMemoria.setId(Long.valueOf(strCodigoAyudaMemoriaEditar.toString()));
			for(AyudaMemoria aMemoria: listAyudaMemoria){
				if(aMemoria.getId()==Long.parseLong(strCodigoAyudaMemoriaEditar) ){
					tabla.setId(aMemoria.getId());
					tabla.setComentario(aMemoria.getComentario());
					tabla.setFecRegistro(aMemoria.getFecRegistro());
					ayudaMemoria.setId(tabla.getId());
					ayudaMemoria.setComentario(tabla.getComentario()); 
				}
			}
			
		}else{
			ayudaMemoria.setId(Long.valueOf(strCodigoAyudaMemoriaEditar.toString()));
			tabla=ayudaMemoriaBean.buscarPorId(ayudaMemoria.getId());
			ayudaMemoria.setId(tabla.getId());
			ayudaMemoria.setComentario(tabla.getComentario()); 
		}
		String idAyudaMemoria = String.valueOf(ayudaMemoria.getId());	
		addObjectSession(ConstantesAdmin.FLAG_AYUDA_MEMORIA_SESION, "editarAyudaMemoria");
		addObjectSession(ConstantesAdmin.AYUDA_MEMORIA_SESION2, idAyudaMemoria);
 
		return tabla;
	}
	
	public String desasociarEditarAyudaMemoria(){
		//System.out.println("----entro a desasociarEditarAyudaMemoria----");
		removeObjectSession(ConstantesAdmin.FLAG_AYUDA_MEMORIA_SESION);
		removeObjectSession(ConstantesAdmin.AYUDA_MEMORIA_SESION2);
		return null;
	}
 
	public void ingresarComentario (ActionEvent ae) {
		String FlatEditar= (String)getObjectSession(ConstantesAdmin.FLAG_AYUDA_MEMORIA_SESION);
		String idMemoria= (String)getObjectSession(ConstantesAdmin.AYUDA_MEMORIA_SESION2);
		Map<String, String> paramMap = getExternalContext().getRequestParameterMap();
		// se insertan los datos
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
	
		ayudaMemoria.setEmpleado(empleado);		
		ayudaMemoria.setFecRegistro(new Timestamp(new Date().getTime())); 
		ayudaMemoria.setComentario(paramMap.get(buscarId(paramMap)).toString().trim());
		if (expediente != null && expediente.getId() > 0) {
			// se inserta el objeto
			ayudaMemoria.setExpediente(expediente);			
			if(FlatEditar==null && ayudaMemoria.getComentario() != null ){
				ayudaMemoriaBean.create(ayudaMemoria);				
			}else{
				ayudaMemoria.setId(Long.valueOf(idMemoria.toString()));
				ayudaMemoriaBean.update(ayudaMemoria); 
				removeObjectSession(ConstantesAdmin.FLAG_AYUDA_MEMORIA_SESION);
				removeObjectSession(ConstantesAdmin.AYUDA_MEMORIA_SESION2);
			}
	
		} else {
			TablaAyudaMemoria tabla = new TablaAyudaMemoria();

			/* Modificar Dato de Lista en  Memoria*/
			if(FlatEditar!=null){
				int idAyudaMemoria=0;  
		 		idAyudaMemoria=Integer.parseInt(idMemoria); 					 						  
				ayudaMemoria.setId(idAyudaMemoria);
				tabla.setComentario(ayudaMemoria.getComentario()); 
				for( AyudaMemoria ayuda:listAyudaMemoria){
					if(ayuda.getId()==idAyudaMemoria){
						ayuda.setComentario(ayudaMemoria.getComentario());
						break;
					}									
				}
				removeObjectSession(ConstantesAdmin.FLAG_AYUDA_MEMORIA_SESION);
				removeObjectSession(ConstantesAdmin.AYUDA_MEMORIA_SESION2);
			}else{	
				/* Realiza esto, cuando entra a  formRegistrarExpediente*/
				long max = 0;
				for(AyudaMemoria ayudaMemoria : listAyudaMemoria){
					if(-ayudaMemoria.getId() > max){
						max = -ayudaMemoria.getId();
					}
				}
				
				ayudaMemoria.setId(-(max+1));
				// agregar nuevos item a la lista tabla ayuda memoria	
				if(!ayudaMemoria.getComentario().equals("")){						
					listAyudaMemoria.add(listAyudaMemoria.size(), ayudaMemoria);
					addObjectSession(Constantes.AYUDA_MEMORIA_NUEVO,"nuevo");
				}else{
					LOG.info("NO agrega a la lista XQ COMENTARIO es vacio ");
				}
			}
			
		}
		 
		// se recupera la lista 
		if (expediente != null && expediente.getId() > 0) {
			// se recupera la lista
			listAyudaMemoria = ayudaMemoriaBean.buscarPorIdExpediente(expediente.getId());
			mostrarTabla(0, Constantes.FILAS_TABLA);
		} else {
			mostrarTabla(0, Constantes.FILAS_TABLA);
		} 
 
	}
	
	private String buscarId (Map<String, String> map) {
		LOG.info("TablaAyudaMemoriaMB:::Inicio de metodo buscarId");
		for (String key : map.keySet()) {
			if (key.endsWith("txtComentAyuMem")) {
				return key;
			}
		}
		return null;
	}
	
	public void habilitarAyudaMemoriaRol() {
		/* Obtener los datos del empleado */
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		expediente.setEmpleado(empleado);

		/* Obtener los datos del perfil */
		Perfil perfil = empleado.getPerfil();
		
        /*Permite el Ingreso de comentarios en Ayuda Memoria*/
		this.mostrarPaneles = false;
		/*if (perfil.getFlagRegistraAyu().equals(Constantes.FLAG_REGISTRA_AYUDA_MEMORIA)) {
			this.mostrarPaneles = true;
		}
		
		Oficina oficina = (Oficina) getObjectSession(Constantes.OFICINA_SESION);
		
		if((perfil.getId() - Constantes.PERFIL_SUB_GERENTE_OFICINA.longValue()) == Constantes.OFICINA_DESPLAZADA
				&& oficina.getFlagDesplazada().equalsIgnoreCase(Constantes.ID_OFICINA_DESPLAZADA)){
			this.mostrarPaneles = true;
		}*/
		
		String tipoVisualizacion = obtenerTipoVisualizacion();
		boolean perfilTarea = false;
		long idTarea = -1;
		if(expediente != null && expediente.getId() > 0){
			idTarea = expediente.getExpedienteTC().getTarea().getId();
		}
		else{
			idTarea = Constantes.ID_TAREA_1.longValue();
		}
		Tarea tarea = tareaBean.buscarPorId(idTarea);
		List<TareaPerfil> listTareaPerfil = tarea.getTareaPerfiles();
		for(TareaPerfil tareaPerfil : listTareaPerfil){
			if(tareaPerfil.getPerfil() != null && perfil != null){
				if(tareaPerfil.getPerfil().getId() == perfil.getId()){
					perfilTarea = true;
					break;
				}
			}
		}
		//System.out.println("flagRegistraAyu:::::"+perfil.getFlagRegistraAyu());
		if(perfilTarea && perfil.getFlagRegistraAyu().equals(Constantes.FLAG_REGISTRA_AYUDA_MEMORIA) && !"3".equals(tipoVisualizacion)){
			this.mostrarPaneles = true;
		}
		
		//System.out.println("mostrarPaneles::::::"+this.mostrarPaneles);
		
	}
	
	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public void setHistorial(Historial historial) {
		this.historial = historial;
	}

	public Historial getHistorial() {
		return historial;
	}

	public void setTablaBinding(HtmlDataTable tablaBinding) {
		this.tablaBinding = tablaBinding;
	}

	public HtmlDataTable getTablaBinding() {
		return tablaBinding;
	}

	public void setListAyudaMemoria(List<AyudaMemoria> listAyudaMemoria) {
		this.listAyudaMemoria = listAyudaMemoria;
	}

	public List<AyudaMemoria> getListAyudaMemoria() {
		return listAyudaMemoria;
	}

	public void setListTabla(List<TablaAyudaMemoria> listTabla) {
		this.listTabla = listTabla;
	}

	public List<TablaAyudaMemoria> getListTabla() {
		return listTabla;
	}

	public AyudaMemoria getAyudaMemoria() {
		return ayudaMemoria;
	}

	public void setAyudaMemoria(AyudaMemoria ayudaMemoria) {
		this.ayudaMemoria = ayudaMemoria;
	}
	
	private class ComparadorAyudaMemoria implements Comparator<AyudaMemoria> {

		public int compare(AyudaMemoria p1, AyudaMemoria p2) {
			return new Long( p2.getId()).compareTo(new Long(p1.getId()));
			//	return (int)(o2.getId()-o1.getId());
		}
		
	}
	
	public String obtenerTipoVisualizacion() {
		String nombJSP = getNombreJSPPrincipal();
/*		if (nombJSP.equals("formAprobarExpediente") ||
			nombJSP.equals("formConsultarClienteModificaciones") ||
			nombJSP.equals("formCoordinarClienteSubsanar") ||
			nombJSP.equals("formEjecutarEvalCrediticia") ||
			nombJSP.equals("formEvaluarDevolucionRiesgos") ||
//			nombJSP.equals("formEvaluarFactibilidadOp") ||
//			nombJSP.equals("formGestionarSubsanarOperacion") ||
			nombJSP.equals("formRegistrarExpediente") ||
//			nombJSP.equals("formRegistrarExpedienteCu23") ||
//			nombJSP.equals("formRegistrarExpedienteCu25") ||
			nombJSP.equals("formRegularizarEscanearDocumentacion") ||
			nombJSP.equals("formRevisarRegistrarDictamen") ||
			nombJSP.equals("formSolicitarVerificacionDomiciliaria") ||
			nombJSP.equals("formVerificarResultadoDomiciliaria")) {
			return Constantes.ESCANEADO_TIPO_DOCUMENTOS_CASO1;
		}else if (nombJSP.equals("formAnularModificarOpCu14") ||
				  nombJSP.equals("formAnularModificarOpCu18") ||
				  nombJSP.equals("formRegistrarDatos") ||
//				  nombJSP.equals("formRegistrarEstado") ||
				  nombJSP.equals("formVerificarConformidadExpediente")){
			return Constantes.ESCANEADO_TIPO_DOCUMENTOS_CASO2;
		}else if (nombJSP.equals("formArchivarExpediente") ||
				  nombJSP.equals("formCambiarSituacionExp") ||
				  nombJSP.equals("formCambiarSituacionTramite") ||
				  nombJSP.equals("formRealizarAltaTarjeta") ||
				  nombJSP.equals("formRegistrarAprobResolucion") ||
//				  nombJSP.equals("formVerificarConformidadCierreExp") ||
				  nombJSP.equals("formVerificarExpDesestimados") ||
				  nombJSP.equals("formVisualizarExpediente")){
			return Constantes.ESCANEADO_TIPO_DOCUMENTOS_CASO3;
		}*/
		
		if (nombJSP.equals("formVisualizarExpediente")){ 
			return Constantes.ESCANEADO_TIPO_DOCUMENTOS_CASO3;
		}else if (nombJSP.equals("formVerificarConformidadExpediente")){
			return Constantes.ESCANEADO_TIPO_DOCUMENTOS_CASO2;
		}else{
			return Constantes.ESCANEADO_TIPO_DOCUMENTOS_CASO1;
		}
	}

	public boolean isMostrarPaneles() {
		return mostrarPaneles;
	}

	public void setMostrarPaneles(boolean mostrarPaneles) {
		this.mostrarPaneles = mostrarPaneles;
	}

	public String getIdAyudaEliminar() {
		return idAyudaEliminar;
	}

	public void setIdAyudaEliminar(String idAyudaEliminar) {
		this.idAyudaEliminar = idAyudaEliminar;
	}

	public HtmlInputText getHtmlCodigoAyudaMemoriaEliminar() {
		return htmlCodigoAyudaMemoriaEliminar;
	}

	public void setHtmlCodigoAyudaMemoriaEliminar(HtmlInputText htmlCodigoAyudaMemoriaEliminar) {
		this.htmlCodigoAyudaMemoriaEliminar = htmlCodigoAyudaMemoriaEliminar;
	}

	public String getStrCodigoAyudaMemoriaEliminar() {
		return strCodigoAyudaMemoriaEliminar;
	}

	public void setStrCodigoAyudaMemoriaEliminar(String strCodigoAyudaMemoriaEliminar) {
		this.strCodigoAyudaMemoriaEliminar = strCodigoAyudaMemoriaEliminar;
	}

	public HtmlCommandButton getBtnEliminarAyudaMemoria() {
		return btnEliminarAyudaMemoria;
	}

	public void setBtnEliminarAyudaMemoria(HtmlCommandButton btnEliminarAyudaMemoria) {
		this.btnEliminarAyudaMemoria = btnEliminarAyudaMemoria;
	}

	public HtmlCommandButton getBtnEditarAyudaMemoria() {
		return btnEditarAyudaMemoria;
	}

	public void setBtnEditarAyudaMemoria(HtmlCommandButton btnEditarAyudaMemoria) {
		this.btnEditarAyudaMemoria = btnEditarAyudaMemoria;
	}
	
	public HtmlCommandButton getBtnDesasociarEditarAyudaMemoria() {
		return btnDesasociarEditarAyudaMemoria;
	}

	public void setBtnDesasociarEditarAyudaMemoria(HtmlCommandButton btnDesasociarEditarAyudaMemoria) {
		this.btnDesasociarEditarAyudaMemoria = btnDesasociarEditarAyudaMemoria;
	}

	public String getStrCodigoAyudaMemoriaEditar() {
		return strCodigoAyudaMemoriaEditar;
	}

	public void setStrCodigoAyudaMemoriaEditar(String strCodigoAyudaMemoriaEditar) {
		this.strCodigoAyudaMemoriaEditar = strCodigoAyudaMemoriaEditar;
	}

	public HtmlInputText getHtmlCodigoAyudaMemoriaEditar() {
		return htmlCodigoAyudaMemoriaEditar;
	}

	public void setHtmlCodigoAyudaMemoriaEditar(HtmlInputText htmlCodigoAyudaMemoriaEditar) {
		this.htmlCodigoAyudaMemoriaEditar = htmlCodigoAyudaMemoriaEditar;
	}

	public String getFlagEditar() {
		return flagEditar;
	}

	public void setFlagEditar(String flagEditar) {
		this.flagEditar = flagEditar;
	}

	public HtmlInputHidden getInputFlagEditar() {
		return inputFlagEditar;
	}

	public void setInputFlagEditar(HtmlInputHidden inputFlagEditar) {
		this.inputFlagEditar = inputFlagEditar;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public String getComandoSesion() {
		return comandoSesion;
	}

	public void setComandoSesion(String comandoSesion) {
		this.comandoSesion = comandoSesion;
	}

	public boolean isRenderedEliminarAyudaMemoria() {
		return renderedEliminarAyudaMemoria;
	}

	public void setRenderedEliminarAyudaMemoria(boolean renderedEliminarAyudaMemoria) {
		this.renderedEliminarAyudaMemoria = renderedEliminarAyudaMemoria;
	}

	public boolean isRenderedEditarAyudaMemoria() {
		return renderedEditarAyudaMemoria;
	}

	public void setRenderedEditarAyudaMemoria(boolean renderedEditarAyudaMemoria) {
		this.renderedEditarAyudaMemoria = renderedEditarAyudaMemoria;
	}

	public String getRenderedTablaAyudaMemoria() {
		return renderedTablaAyudaMemoria;
	}

	public void setRenderedTablaAyudaMemoria(String renderedTablaAyudaMemoria) {
		this.renderedTablaAyudaMemoria = renderedTablaAyudaMemoria;
	}
	

}