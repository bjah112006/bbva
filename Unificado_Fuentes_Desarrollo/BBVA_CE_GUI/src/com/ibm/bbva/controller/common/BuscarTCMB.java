package com.ibm.bbva.controller.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.form.ReporteTC;
import com.ibm.bbva.entities.EstadoCE;
import com.ibm.bbva.entities.EstadoTareaCE;
import com.ibm.bbva.entities.ParametrosConf;
import com.ibm.bbva.session.EstadoCEBeanLocal;
import com.ibm.bbva.session.EstadoTareaCEBeanLocal;
import com.ibm.bbva.session.OficinaBeanLocal;
import com.ibm.bbva.session.ParametrosConfBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.ProductoBeanLocal;
import com.ibm.bbva.session.TerritorioBeanLocal;
import com.ibm.bbva.session.TipoOfertaBeanLocal;
import com.ibm.bbva.util.Util;


@SuppressWarnings("serial")
@ManagedBean(name = "buscarTC")
@RequestScoped
public class BuscarTCMB extends AbstractMBean {
	
	private static final Logger LOG = LoggerFactory.getLogger(BuscarTCMB.class);
	
	@EJB
	private TipoOfertaBeanLocal tipoOfertaobean;
	@EJB
	private PerfilBeanLocal perfilBean;
 	@EJB
	private EstadoCEBeanLocal estadoCEBean;	
	@EJB
	private EstadoTareaCEBeanLocal estadoTareaCeBean;	
	@EJB
	private ProductoBeanLocal productoBean;
	@EJB
	private TerritorioBeanLocal territorioBean;
	@EJB
	private OficinaBeanLocal oficinaBean;
	@EJB
	private ParametrosConfBeanLocal parametrosConfBean;
	
	List<ParametrosConf> listaParametros;
	
	private Date fechaInicio;
	private Date fechaFin;
	private String tipoReporteTC;
	
	private List<SelectItem> listaTipoOferta;
	public String codTipoOferta;
	private List<SelectItem> listaPerfil;
	private String codPerfil;
	private List<SelectItem> listaProducto;
	private String codProducto;
	private List<SelectItem> listaTerritorio;
	private String codTerritorio;
	private List<SelectItem> listaOficina;
	private String codOficina;
	private List<SelectItem> listaEstado;
	private String codEstado;
	private boolean existeMsj;
	private String tituloReporte;
	private boolean existeError=true;
	
	@PostConstruct
    public void init() {
		listaEstado = Util.listaVacia ();
		listaEstado = Util.setEtiqueta(listaEstado, "-1", ">>Todos<<");
		
		listaOficina = Util.listaVacia();
		listaOficina = Util.setEtiqueta(listaOficina, "-1", ">>Todos<<");
		
		codEstado = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		codOficina = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		inicializarDatosTC();
	}
	
	public void inicializarDatosTC(){

			listaTipoOferta = Util.crearItems(tipoOfertaobean.buscarTodos(), true,"id", "descripcion");
			listaTipoOferta = Util.setEtiqueta(listaTipoOferta, "-1", ">>Todos<<");
			
			/***Inicio - Requerimiento 281***/
			//listaPerfil=Util.crearItems(perfilBean.buscarTodos(), true, "id", "descripcion");
			listaPerfil=Util.crearItems(perfilBean.buscarPorProceso(), true, "id", "descripcion");
			Collections.sort(listaPerfil, new Util.SecletItemComparator(false));
			/***Fin - Requerimiento 281***/
			
			listaPerfil=Util.setEtiqueta(listaPerfil, "-1", ">>Todos<<");
					
			listaProducto=Util.crearItems(productoBean.buscarTodos(), true, "id", "descripcion" );
			listaProducto=Util.setEtiqueta(listaProducto, "-1", ">>Todos<<");
			
			listaTerritorio=Util.crearItems(territorioBean.buscarTodos(), true, "id", "descripcion");
			Collections.sort(listaTerritorio, new Util.SecletItemComparator(false, true));
			listaTerritorio=Util.setEtiqueta(listaTerritorio,"-1",">>Todos<<");
			
			String perfilSeleccionado = (String) getObjectSession(Constantes.REPORTE_PERFIL);
			String territorioSeleccionado = (String) getObjectSession(Constantes.REPORTE_TERRITORIO);
			tipoReporteTC = (String) getObjectSession(Constantes.TIPO_REPORTE_TC); 
			
			if(tipoReporteTC.trim().equals(Constantes.ID_TC_HISTORICO)){
				tituloReporte=Constantes.TITULO_REPORTE_TC_HISTORICO;
			}else{
				if(tipoReporteTC.trim().equals(Constantes.ID_TC_CONSOLIDADO))
					tituloReporte=Constantes.TITULO_REPORTE_TC_CONSOLIDADO;
			}
			if(perfilSeleccionado!=null){
				this.setCodPerfil(perfilSeleccionado);
				cargarListaEstados (Long.parseLong(codPerfil));
			}
			if(territorioSeleccionado!=null){
				this.setCodTerritorio(territorioSeleccionado);
				cargarListaOficinas (Long.parseLong(codTerritorio));
			}
	}
	
	public void listarEstados(AjaxBehaviorEvent event) {	
		ReporteTC reporteTCMB=null;		
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		reporteTCMB = (ReporteTC)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "reporteTC");	
		
		reporteTCMB.setViewGenerar(false);
		
		listaEstado = Util.listaVacia ();
		codEstado = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		
		if(codPerfil!=null){
			addObjectSession(Constantes.REPORTE_PERFIL, codPerfil);
			cargarListaEstados (Long.parseLong(codPerfil));
			buscar();
		}
	}

	public void listarOficinas(AjaxBehaviorEvent event) {	
		ReporteTC reporteTCMB=null;		
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		reporteTCMB = (ReporteTC)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "reporteTC");	
		
		reporteTCMB.setViewGenerar(false);
		
		listaOficina = Util.listaVacia ();
		codOficina = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		
		if(codTerritorio!=null){
			addObjectSession(Constantes.REPORTE_TERRITORIO, codTerritorio);
			cargarListaOficinas (Long.parseLong(codTerritorio));
			buscar();
		}
	}
	public void actVistaGenerar(AjaxBehaviorEvent event){
		ReporteTC reporteTCMB=null;		
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		reporteTCMB = (ReporteTC)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "reporteTC");	
		
		reporteTCMB.setViewGenerar(false);
	}
	
	public void defReporteGeneral(AjaxBehaviorEvent event){
		ReporteTC reporteTCMB=null;		
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		reporteTCMB = (ReporteTC)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "reporteTC");	
		
		reporteTCMB.setViewGenerar(false);
		
		listaEstado = Util.listaVacia ();
		listaOficina = Util.listaVacia();
		codEstado = Constantes.CODIGO_CODIGO_CAMPO_VACIO;		
		codPerfil=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		codOficina=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		codTerritorio=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		buscar();
	} 

	public void cargarListaEstados(long idPerfil){	
		List<EstadoTareaCE> listaIdEstado =  new ArrayList<EstadoTareaCE>();
		listaIdEstado.addAll(estadoTareaCeBean.buscarPorIdPerfilMenosEnDesuso(idPerfil)) ;
		listaEstado=Util.crearItems(listaEstadosUnicos(listaIdEstado), true, "id", "descripcion");
//		listaEstado=Util.crearItems(estadoBean.buscarPorIdPerfil(idPerfil), true, "id", "descripcion");

		listaEstado=Util.setEtiqueta(listaEstado, "-1", ">>Todos<<");
	}	
	public void cargarListaOficinas(long idTerritorio){	
		listaOficina=Util.crearItems(oficinaBean.buscarPorIdTerritorio(idTerritorio), true, "id", "descripcion");
		listaOficina=Util.setEtiqueta(listaOficina, "-1", ">>Todos<<");
	}	
	public String buscar(){
		this.obtenerParametros();
		return null;		
	}
	
	public ArrayList obtenerParametros(){		
		ArrayList arrayList=null;
		existeError=true;
		LOG.info("MAYDELINE: obtenerParametros: getCodEstado: "+codEstado+" codPerfil: "+codPerfil);
		if(!esValidoFechas()){
			arrayList=new ArrayList();
			arrayList.add(this.fechaInicio);
			arrayList.add(this.fechaFin);
			arrayList.add(Long.parseLong(this.getCodProducto()));
			arrayList.add(Long.parseLong(this.getCodPerfil()));
			arrayList.add(Long.parseLong(this.getCodTipoOferta()));
			arrayList.add(Long.parseLong(this.getCodEstado()));
			arrayList.add(Long.parseLong(this.getCodTerritorio()));
			arrayList.add(Long.parseLong(this.getCodOficina()));
			arrayList.add(Integer.parseInt(this.getTipoReporteTC()));
			existeError=false;		
		}
		return arrayList;
	}
	
	public boolean esValidoFechas(){
		
		String jspPrinc = getNombreJSPPrincipal();
		String formulario = "";
		if (jspPrinc.equals("formGeneraReporteTC")) {
			formulario = "formGeneraReporteTC";
		}		

		boolean existeErrores = false;
		
		Date fechaActual = new Date();

		if (fechaInicio == null) {
			addMessageError(formulario + ":fecInicial", 
					"com.ibm.bbva.common.buscarTC.msg.fechaInicio.vacio");
			existeErrores = true;
		} 

		if (fechaFin == null) {
			addMessageError(formulario + ":fecFinal", 
					"com.ibm.bbva.common.buscarTC.msg.fechaFin.vacio");
			existeErrores = true;
		}
		
		if (fechaInicio != null && fechaFin != null) {
			if (fechaFin.before(fechaInicio)) {
				addMessageError(formulario + ":fecFinal", 
						"com.ibm.bbva.common.buscarTC.msg.fechaFin.menor");
				existeErrores = true;
			}
		}
		
		if (fechaInicio!=null && fechaFin!=null){
			if (fechaInicio.after(fechaActual) || fechaFin.after(fechaActual)){
				addMessageError(formulario + ":fecInicial", 
						"com.ibm.bbva.common.buscarTC.msg.fechaFin.fechaInvalida");
				existeErrores = true;
			}
		}
				
		if (fechaInicio != null && fechaFin != null) {			
			int diasConfig = 0;
			
			if(parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "DIAS_FILTRO_REPORTES") != null){
				diasConfig = Integer.parseInt(parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, "DIAS_FILTRO_REPORTES").getValorVariable());
			}
						
			int diasFiltro = Util.restarFechas(fechaInicio, fechaFin);
			
			if (diasFiltro > 0 && diasConfig > 0) {
				if (diasFiltro > diasConfig) {
					String sDias = String.valueOf(diasConfig);										
					addMessageError(formulario + ":fecInicial", "com.ibm.bbva.common.buscarTC.msg.diasEntreFechasFiltro.mayor", sDias);
					
					existeErrores = true;
				}
			}
		}
		
		return existeErrores;
	}
	
	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public List<SelectItem> getListaTipoOferta() {
		return listaTipoOferta;
	}

	public void setListaTipoOferta(List<SelectItem> listaTipoOferta) {
		this.listaTipoOferta = listaTipoOferta;
	}

	public String getCodTipoOferta() {
		return codTipoOferta;
	}

	public void setCodTipoOferta(String codTipoOferta) {
		this.codTipoOferta = codTipoOferta;
	}

	public List<SelectItem> getListaPerfil() {
		return listaPerfil;
	}

	public void setListaPerfil(List<SelectItem> listaPerfil) {
		this.listaPerfil = listaPerfil;
	}
	public List<SelectItem> getListaProducto() {
		return listaProducto;
	}
	
	public void setListaProducto(List<SelectItem> listaProducto) {
		this.listaProducto = listaProducto;
	}
	public List<SelectItem> getListaTerritorio() {
		return listaTerritorio;
	}
	
	public void setListaTerritorio(List<SelectItem> listaTerritorio) {
		this.listaTerritorio = listaTerritorio;
	}
	public String getCodPerfil() {
		return codPerfil;
	}

	public void setCodPerfil(String codPerfil) {
		this.codPerfil = codPerfil;
	}

	public String getCodProducto() {
		return codProducto;
	}
	
	public void setCodProducto(String codProducto) {
		this.codProducto = codProducto;
	}
	public String getCodTerritorio() {
		return codTerritorio;
	}
	
	public void setCodTerritorio(String codTerritorio) {
		this.codTerritorio = codTerritorio;
	}
	public String getCodOficina() {
		return codOficina;
	}
	
	public void setCodOficina(String codOficina) {
		this.codOficina = codOficina;
	}
	public List<SelectItem> getListaEstado() {
		return listaEstado;
	}

	public void setListaEstado(List<SelectItem> listaEstado) {
		this.listaEstado = listaEstado;
	}
	public List<SelectItem> getListaOficina() {
		return listaOficina;
	}
	
	public void setListaOficina(List<SelectItem> listaOficina) {
		this.listaOficina = listaOficina;
	}
	public String getCodEstado() {
		return codEstado;
	}

	public void setCodEstado(String codEstado) {
		this.codEstado = codEstado;
	}
	
	public String getTipoReporteTC() {
		return this.tipoReporteTC;
	}
	
	public void setTipoReporteTC(String tipoReporte) {
		this.tipoReporteTC = tipoReporte;
	}
	
	public boolean isExisteMsj() {
		return existeMsj;
	}

	public void setExisteMsj(boolean existeMsj) {
		this.existeMsj = existeMsj;
	}
	
	public String getTituloReporte()
	{
		return this.tituloReporte;
	}
	
	public void setTituloReporte(String tituloReporte)
	{
		this.tituloReporte = tituloReporte;
	}
	
	public boolean isExisteError() {
		return this.existeError;
	}
	
	public void setExisteError(boolean existeError) {
		this.existeError = existeError;
	}
	
	private List<EstadoCE> listaEstadosUnicos (List<EstadoTareaCE> idEstadoPorIdPerfil) {
		List<EstadoCE> listaEstado = new ArrayList<EstadoCE>();
		for(EstadoTareaCE estados: idEstadoPorIdPerfil){
			listaEstado.addAll( estadoCEBean.buscarPorIdEstado(estados.getEstadoCE().getId()));
		} 
		Set<EstadoCE> set = new TreeSet<EstadoCE> (new Comparator<EstadoCE>(){
			public int compare(EstadoCE o1, EstadoCE o2) {
				return o1.getDescripcion().compareToIgnoreCase(o2.getDescripcion());			
			}
		});	
			for (EstadoCE estado : listaEstado) {
				set.add(estado);
			}
		return new ArrayList<EstadoCE>(set);
		}
}