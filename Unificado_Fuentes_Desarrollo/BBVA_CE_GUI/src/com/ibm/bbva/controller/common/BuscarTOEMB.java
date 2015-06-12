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
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.form.BandejaReasigTareasMB;
import com.ibm.bbva.controller.form.ReporteTOE;
import com.ibm.bbva.entities.EstadoCE;
import com.ibm.bbva.entities.EstadoTareaCE;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.session.EstadoBeanLocal;
import com.ibm.bbva.session.EstadoCEBeanLocal;
import com.ibm.bbva.session.EstadoTareaCEBeanLocal;
import com.ibm.bbva.session.OficinaBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.PosibleValorBeanLocal;
import com.ibm.bbva.session.ProductoBeanLocal;
import com.ibm.bbva.session.TerritorioBeanLocal;
import com.ibm.bbva.session.TipoOfertaBeanLocal;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "buscarTOE")
@RequestScoped
public class BuscarTOEMB extends AbstractMBean {
	
	@EJB
	private TipoOfertaBeanLocal tipoOfertaobean;
	@EJB
	private PerfilBeanLocal perfilBean;
//	@EJB
//	private EstadoBeanLocal estadoBean;	
	@EJB
	private EstadoCEBeanLocal estadoCEBean;	
	@EJB
	private EstadoTareaCEBeanLocal estadoTareaCeBean;	
	@EJB
	private PosibleValorBeanLocal posibleValorBean;	
	@EJB
	private ProductoBeanLocal productoBean;	
	@EJB
	private OficinaBeanLocal oficinaBean;	
	@EJB
	private TerritorioBeanLocal territorioBean;		
	
	private static final Logger LOG = LoggerFactory.getLogger(BandejaReasigTareasMB.class);
	private Date fechaInicio;
	private Date fechaFin;

	private List<SelectItem> listaTipoOferta;
	public String codTipoOferta;
	private List<SelectItem> listaPerfil;
	private String codPerfil;
	private List<SelectItem> listaEstado;
	private String codEstado;	
	private List<SelectItem> listaPosibleValor;
	public String codPosibleValor;	
	private List<SelectItem> listaProducto;
	public String codProducto;	
	private List<SelectItem> listaTerritorio;
	public String codTerritorio;
	private List<SelectItem> listaOficina;
	public String codOficina;	
	private boolean existeMsj;
	private boolean existePerfil;
	private boolean existeFlujo;
	public String paramTipoReporteApplet="";
	
	private Expediente expediente;
	
	@PostConstruct
    public void init() {		
		
		listaProducto = Util.listaVacia ();
		listaEstado = Util.listaVacia ();
		listaTerritorio = Util.listaVacia ();
		listaOficina = Util.listaVacia ();
		codEstado = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		codProducto = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		
		if(codTerritorio==null)
			codTerritorio=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		
		if(codOficina==null)
			codOficina=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		//codOficina=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		inicializarDatosTOE();
		
	}
	
	public String limpiar(){
		codEstado = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		codPerfil = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		codTipoOferta = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		codProducto = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		codTerritorio=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		codOficina=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		fechaInicio=null;
		fechaFin=null;
		//listaEstado = Util.listaVacia ();
		listaEstado=Util.crearItemsE(new ArrayList<String>(), true, "id", "descripcion", ">>Todos<<");
		listaOficina= Util.listaVacia ();
		
		return null;
	}
	
	public void inicializarDatosTOE(){

			listaTipoOferta = Util.crearItemsE(tipoOfertaobean.buscarTodos(), true,"id", "descripcion", ">>Todos<<");	
			
			/***Inicio - Requerimiento 281***/
			//listaPerfil=Util.crearItems(perfilBean.buscarTodos(), true, "id", "descripcion");
			listaPerfil=Util.crearItemsE(perfilBean.buscarPorProceso(), true, "id", "descripcion", ">>Todos<<");
			Collections.sort(listaPerfil, new Util.SecletItemComparator(false));
			/***Fin - Requerimiento 281***/
			
			listaPosibleValor=Util.crearItems(posibleValorBean.buscarPorIdColumna(Long.parseLong(Constantes.ID_POSIBLE_VALOR_PERFIL_ESTADO)), true, "valor", "etiqueta");
			listaProducto=Util.crearItemsE(productoBean.buscarTodos(), true, "id", "descripcion", ">>Todos<<");
			listaTerritorio=Util.crearItems(territorioBean.buscarTodos(), true, "id", "descripcion");
			Collections.sort(listaTerritorio, new Util.SecletItemComparator(false, true));
		//	listaOficina=Util.crearItems(oficinabean.buscarTodos(), true, "id", "descripcion");
			
			String perfilSeleccionado = (String) getObjectSession(Constantes.REPORTE_PERFIL);
			
			if(perfilSeleccionado!=null){
				this.setCodPerfil(perfilSeleccionado);
				cargarListaEstados (Long.parseLong(codPerfil));
			}else{
				listaEstado=Util.crearItemsE(new ArrayList<String>(), true, "id", "descripcion", ">>Todos<<");
			}
			
			String territorioSeleccionado = (String) getObjectSession(Constantes.REPORTE_TERRITORIO);
			if(territorioSeleccionado!=null){
				this.setCodTerritorio(territorioSeleccionado);
				cargarListaOficinas(Long.parseLong(territorioSeleccionado));
			}
			
	}
	
	public void listarEstados(AjaxBehaviorEvent event) {	
		ReporteTOE reporteTOEMB=null;		
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		reporteTOEMB = (ReporteTOE)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "reporteTOE");	
		
		reporteTOEMB.setViewGenerar(false);
		
		listaEstado = Util.listaVacia ();
		codEstado = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		codPosibleValor = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		
			if(codPerfil!=null){
				addObjectSession(Constantes.REPORTE_PERFIL, codPerfil);
				cargarListaEstados (Long.parseLong(codPerfil));
				//buscar();
			}
		
	}
	
	public void listarOficinas(AjaxBehaviorEvent event) {	
//		ReporteTOE reporteTOEMB=null;		
		
		//FacesContext ctx = FacesContext.getCurrentInstance();
		
	//	reporteTOEMB = (ReporteTOE)  
//				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "reporteTOE");	
		
		//reporteTOEMB.setViewGenerar(false);
		
		listaOficina = Util.listaVacia ();
		codOficina = Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		
			if(codTerritorio!=null){
				addObjectSession(Constantes.REPORTE_TERRITORIO, codTerritorio);
				cargarListaOficinas (Long.parseLong(codTerritorio));
				//buscar();
			}
		
	}
	
	public void cargarListaOficinas(long codTerritorio){	
		List<Oficina> listTemp_oficina=oficinaBean.buscarPorIdTerritorio(codTerritorio);
		for(Oficina of : listTemp_oficina){
			if(of!=null){
				of.setDescripcion(of.getCodigo()+" - "+of.getDescripcion());
			}
		}
		listaOficina=Util.crearItems(listTemp_oficina, true, "id", "descripcion");
		
	}	
	
	public void actVistaGenerar(AjaxBehaviorEvent event){
		ReporteTOE reporteTOEMB=null;		
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		reporteTOEMB = (ReporteTOE)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "reporteTOE");	
		
		reporteTOEMB.setViewGenerar(false);
	}
	
	public void defReporteGeneral(AjaxBehaviorEvent event){
		ReporteTOE reporteTOEMB=null;		
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		reporteTOEMB = (ReporteTOE)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "reporteTOE");	
		
		reporteTOEMB.setViewGenerar(false);
		
		listaEstado = Util.listaVacia ();
		codEstado = Constantes.CODIGO_CODIGO_CAMPO_VACIO;		
		codPerfil=Constantes.CODIGO_CODIGO_CAMPO_VACIO;
		buscar();
	} 
	
	public void seteoEstado(AjaxBehaviorEvent event) {
		ReporteTOE reporteTOEMB=null;		
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		reporteTOEMB = (ReporteTOE)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "reporteTOE");	
		
		reporteTOEMB.setViewGenerar(false);
		
		addObjectSession(Constantes.REPORTE_ESTADO, codEstado);
	}	
	
	
	public void cargarListaEstados(long idPerfil){	
		List<EstadoTareaCE> listaIdEstado =  new ArrayList<EstadoTareaCE>();
		LOG.info("idPerfil:::"+idPerfil);
		listaIdEstado.addAll(estadoTareaCeBean.buscarPorIdPerfilMenosEnDesuso(idPerfil)) ;
		//LOG.info("listaIdEstado:::"+listaIdEstado.size());
		listaEstado=Util.crearItemsE(listaEstadosUnicos(listaIdEstado), true, "id", "descripcion", ">>Todos<<");
	}
	
	public String buscar(){
		this.obtenerParametros();
		return null;
		
	}
	
	public ArrayList obtenerParametros(){		
		ArrayList arrayList=null;
		boolean existeError=true;

		if(!esValidoFechas()){

						arrayList=new ArrayList();
						arrayList.add(this.fechaInicio);									//FECHA INICIO 	(0)
						arrayList.add(this.fechaFin);										//FECHA FIN		(1)
	
						if(codProducto!=null)
							arrayList.add(this.codProducto);								//PRODUCTO 		(2)
						
						arrayList.add(Long.parseLong(this.getCodPerfil()));					//PERFIL		(3)
						
						if(codTipoOferta!=null)
							arrayList.add(this.codTipoOferta);								//TIPO DE OFERTA(4)	
						
						if(codEstado!=null)
							arrayList.add(Long.parseLong(this.getCodEstado()));				//ESTADO		(5)
						
						if(codTerritorio!=null)
							arrayList.add(Long.parseLong(this.getCodTerritorio()));			//TERRITORIO	(6)
						
						if(codOficina!=null)
							arrayList.add(Long.parseLong(this.getCodOficina()));			//OFICINA		(7)
						
						
			// Si perfil o estado están seleccionados, entonces
			// usar el reporte detallado.
						
			boolean usarReporteDetallado = false;
						
			if(this.getCodPerfil() != null && !this.getCodPerfil().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))
				usarReporteDetallado = true;
						
			if(this.getCodEstado() != null && !this.getCodEstado().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))
				usarReporteDetallado = true;
						
			// Si no, usar el reporte específico
						
			if(usarReporteDetallado){
				arrayList.add(Integer.parseInt(Constantes.ID_TOE_ESPECIFICO));		//TIPO DE REPORTE (8)
				paramTipoReporteApplet = Constantes.ID_TOE_ESPECIFICO;
			}else{
				arrayList.add(Integer.parseInt(Constantes.ID_TOE_GENERAL));
						paramTipoReporteApplet=Constantes.ID_TOE_GENERAL;
					}
				
			existeError=false;
		}

		return arrayList;
	}
	
	public boolean esValidoFechas(){
		
		String jspPrinc = getNombreJSPPrincipal();
		String formulario = "";
		if (jspPrinc.equals("formGeneraReporteTOE")) {
			formulario = "formGeneraReporteTOE";
		}		
		
		Date fechaActual = new Date();

		boolean existeError = false;

		if (fechaInicio == null) {
			addMessageError(formulario + ":fecInicial", 
					"com.ibm.bbva.common.buscarTOE.msg.fechaInicio.vacio");
			existeError = true;
		} 

		if (fechaFin == null) {
			addMessageError(formulario + ":fecFinal", 
					"com.ibm.bbva.common.buscarTOE.msg.fechaFin.vacio");
			existeError = true;
		} 
		
		if (fechaInicio != null && fechaFin != null) {
			if (fechaFin.before(fechaInicio)) {
				addMessageError(formulario + ":fecFinal", 
						"com.ibm.bbva.common.buscarTC.msg.fechaFin.menor");
				existeError = true;
			}
		}
		
		if (fechaInicio!=null && fechaFin!=null){
			if (fechaInicio.after(fechaActual) || fechaFin.after(fechaActual)){
				addMessageError(formulario + ":fecInicial", 
						"com.ibm.bbva.common.buscarTC.msg.fechaFin.fechaInvalida");
				existeError = true;
			}
		}
		
		return existeError;		
	}
	
	public boolean esValidoGeneral() {
		
		String jspPrinc = getNombreJSPPrincipal();
		String formulario = "";
		boolean existeError = false;
		
		if (jspPrinc.equals("formGeneraReporteTOE")) {
			formulario = "formGeneraReporteTOE";
		}		
		if (getCodPosibleValor()==null || getCodPosibleValor().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			this.existeFlujo=false;
			existeError = true;
		}else{
			this.existeFlujo=true;
			if (getCodTipoOferta()==null || getCodTipoOferta().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
				addMessageError(formulario + ":selTipoOferta", 
						"com.ibm.bbva.common.buscarTOE.msg.tipoOferta.vacio");
				existeError = true;
			}			
		}			
		return existeError;
	}	
	
	public boolean esValidoEspecifico (){
		String jspPrinc = getNombreJSPPrincipal();
		String formulario = "";
		if (jspPrinc.equals("formGeneraReporteTOE")) {
			formulario = "formGeneraReporteTOE";
		}
		
		boolean existeError = false;		
		
		if (getCodPerfil()==null || getCodPerfil().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
			//addMessageError(formulario + ":selPerfil", 
				//	"com.ibm.bbva.common.buscarTOE.msg.perfil.vacio");
			existeError = true;
		}
		

		
		if(!existeError){
				if (getCodEstado()==null || getCodEstado().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
					addMessageError(formulario + ":selEstado", 
							"com.ibm.bbva.common.buscarTOE.msg.estado.vacio");
					existeError = true;
				}			
				if (getCodTipoOferta()==null || getCodTipoOferta().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
					addMessageError(formulario + ":selTipoOferta", 
							"com.ibm.bbva.common.buscarTOE.msg.tipoOferta.vacio");
					existeError = true;
				}
				if (getCodProducto()==null || getCodProducto().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
					addMessageError(formulario + ":selProducto", 
							"com.ibm.bbva.common.buscarTOE.msg.producto.vacio");
					existeError = true;
				}	
				if (getCodTerritorio()==null || getCodTerritorio().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
					addMessageError(formulario + ":selTerritorio", 
							"com.ibm.bbva.common.buscarTOE.msg.territorio.vacio");
					existeError = true;
				}	
				if (getCodOficina()==null || getCodOficina().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
					addMessageError(formulario + ":selOficina", 
							"com.ibm.bbva.common.buscarTOE.msg.oficina.vacio");
					existeError = true;
				}					
		}

		return existeError;		
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

	public String getCodPerfil() {
		return codPerfil;
	}

	public void setCodPerfil(String codPerfil) {
		this.codPerfil = codPerfil;
	}

	public List<SelectItem> getListaEstado() {
		return listaEstado;
	}

	public void setListaEstado(List<SelectItem> listaEstado) {
		this.listaEstado = listaEstado;
	}

	public String getCodEstado() {
		return codEstado;
	}

	public void setCodEstado(String codEstado) {
		this.codEstado = codEstado;
	}

	public List<SelectItem> getListaPosibleValor() {
		return listaPosibleValor;
	}

	public void setListaPosibleValor(List<SelectItem> listaPosibleValor) {
		this.listaPosibleValor = listaPosibleValor;
	}

	public String getCodPosibleValor() {
		return codPosibleValor;
	}

	public void setCodPosibleValor(String codPosibleValor) {
		this.codPosibleValor = codPosibleValor;
	}

	public boolean isExisteMsj() {
		return existeMsj;
	}

	public void setExisteMsj(boolean existeMsj) {
		this.existeMsj = existeMsj;
	}


	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public boolean isExistePerfil() {
		return existePerfil;
	}

	public void setExistePerfil(boolean existePerfil) {
		this.existePerfil = existePerfil;
	}

	public boolean isExisteFlujo() {
		return existeFlujo;
	}

	public void setExisteFlujo(boolean existeFlujo) {
		this.existeFlujo = existeFlujo;
	}

	public String getParamTipoReporteApplet() {
		return paramTipoReporteApplet;
	}

	public void setParamTipoReporteApplet(String paramTipoReporteApplet) {
		this.paramTipoReporteApplet = paramTipoReporteApplet;
	}

	public List<SelectItem> getListaProducto() {
		return listaProducto;
	}

	public void setListaProducto(List<SelectItem> listaProducto) {
		this.listaProducto = listaProducto;
	}

	public String getCodProducto() {
		return codProducto;
	}

	public void setCodProducto(String codProducto) {
		this.codProducto = codProducto;
	}

	public List<SelectItem> getListaTerritorio() {
		return listaTerritorio;
	}

	public void setListaTerritorio(List<SelectItem> listaTerritorio) {
		this.listaTerritorio = listaTerritorio;
	}

	public String getCodTerritorio() {
		return codTerritorio;
	}

	public void setCodTerritorio(String codTerritorio) {
		this.codTerritorio = codTerritorio;
	}

	public List<SelectItem> getListaOficina() {
		return listaOficina;
	}

	public void setListaOficina(List<SelectItem> listaOficina) {
		this.listaOficina = listaOficina;
	}

	public String getCodOficina() {
		return codOficina;
	}

	public void setCodOficina(String codOficina) {
		this.codOficina = codOficina;
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