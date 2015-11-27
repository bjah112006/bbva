package com.ibm.bbva.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.servlet.ReporteTCCSVServlet;
import com.ibm.bbva.entities.EstadoCE;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.PosibleValor;
import com.ibm.bbva.entities.Producto;
import com.ibm.bbva.entities.TipoOferta;
import com.ibm.bbva.session.EstadoCEBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.ProductoBeanLocal;
import com.ibm.bbva.session.TipoOfertaBeanLocal;
import com.ibm.bbva.session.PosibleValorBeanLocal;
import com.ibm.bbva.tabla.ejb.impl.TablaFacadeBean;
import com.ibm.bbva.tabla.reporte.vo.DatosGeneradosVO;
import com.ibm.bbva.tabla.reporte.vo.DatosPorcentajeVO;
import com.ibm.bbva.tabla.reporte.vo.DatosUnidadVO;
import com.ibm.bbva.tabla.util.vo.ListaReportePorcentajeToe;
import com.ibm.bbva.tabla.util.vo.ListaReporteToe;
import com.ibm.bbva.tabla.util.vo.ListaReporteUnidadToe;

public class AyudaDatosReporte {


	
	private TablaFacadeBean tablaFacadeBean = null;

	private PosibleValorBeanLocal posibleValorBean;	
	private ProductoBeanLocal productoBean;	
	private TipoOfertaBeanLocal tipoOfertaobean;	
	private PerfilBeanLocal perfilBean;
	private EstadoCEBeanLocal estadoCEBean;	
	
	/**
	 * Vista de Tablas segun Filtros
	 * */
	public boolean viewTablaGeneral;
	public boolean viewTablaEspecifica;
	
	public boolean viewProductoTC;
	public boolean viewProductoPLD;
	public boolean viewTipoOfertaAprobado;
	public boolean viewTipoOfertaRegular;
	
	private  ArrayList<String> generar_listaRoles;
	private  ArrayList<String> generar_listaPerfiles;	
	private ArrayList<String> listaTiempo_tc_te;
	
	private List<DatosGeneradosVO> listDatosGeneradosVO;
	private List<DatosGeneradosVO> listDatosGeneradosVO_reproceso;	
	private List<DatosPorcentajeVO> listDatosPorcentajeVO;
	private List<PosibleValor> listPosibleValor;
	private List<DatosUnidadVO> listDatosUnidadVO;
	
	
	public Map<Integer, Map<String, Map<String, ArrayList<DatosGeneradosVO>>>> mapaDatosTipoOfertaRol;
	public Map<Integer,Map<Integer, Map<String, Map<String, ArrayList<DatosGeneradosVO>>>>> mapaDatosProductoTipoOferta;
	
	public Map<Integer, Map<String, ArrayList<DatosPorcentajeVO>>> mapaPorcentajeTipoOfertaRol;
	public Map<Integer,Map<Integer, Map<String, ArrayList<DatosPorcentajeVO>>>> mapaPorcentajeProductoTipoOferta;
	
	public Map<Integer, Map<String, ArrayList<DatosUnidadVO>>> mapaUnidadTipoOfertaRol;
	public Map<Integer,Map<Integer, Map<String, ArrayList<DatosUnidadVO>>>> mapaUnidadProductoTipoOferta;
	
	ListaReporteToe objListaReporteToe;
	private List<ListaReporteToe> listReport_Prod_1_TipOferta_1_Flujo_1;
	private List<ListaReporteToe> listReport_Prod_1_TipOferta_1_Flujo_2;
	private List<ListaReporteToe> listReport_Prod_1_TipOferta_2_Flujo_1;
	private List<ListaReporteToe> listReport_Prod_1_TipOferta_2_Flujo_2;
	private List<ListaReporteToe> listReport_Prod_2_TipOferta_1_Flujo_1;
	private List<ListaReporteToe> listReport_Prod_2_TipOferta_1_Flujo_2;
	private List<ListaReporteToe> listReport_Prod_2_TipOferta_2_Flujo_1;
	private List<ListaReporteToe> listReport_Prod_2_TipOferta_2_Flujo_2;
	private List<List<ListaReporteToe>> listaReporte_Prod_TipoOferta_Flujo;
	
	ListaReportePorcentajeToe objListaReportePorcentajeToe;
	private List<ListaReportePorcentajeToe> listPorc_Prod_1_TipOferta_1_Flujo_1;
	private List<ListaReportePorcentajeToe> listPorc_Prod_1_TipOferta_1_Flujo_2;
	private List<ListaReportePorcentajeToe> listPorc_Prod_1_TipOferta_2_Flujo_1;
	private List<ListaReportePorcentajeToe> listPorc_Prod_1_TipOferta_2_Flujo_2;
	private List<ListaReportePorcentajeToe> listPorc_Prod_2_TipOferta_1_Flujo_1;
	private List<ListaReportePorcentajeToe> listPorc_Prod_2_TipOferta_1_Flujo_2;
	private List<ListaReportePorcentajeToe> listPorc_Prod_2_TipOferta_2_Flujo_1;
	private List<ListaReportePorcentajeToe> listPorc_Prod_2_TipOferta_2_Flujo_2;
	private List<List<ListaReportePorcentajeToe>> listPorc_Prod_TipOferta_Flujo;
	
	ListaReporteUnidadToe objListaReporteUnidadToe;
	private List<ListaReporteUnidadToe> listUnid_Prod_1_TipOferta_1_Flujo_1;
	private List<ListaReporteUnidadToe> listUnid_Prod_1_TipOferta_1_Flujo_2;
	private List<ListaReporteUnidadToe> listUnid_Prod_1_TipOferta_2_Flujo_1;
	private List<ListaReporteUnidadToe> listUnid_Prod_1_TipOferta_2_Flujo_2;
	private List<ListaReporteUnidadToe> listUnid_Prod_2_TipOferta_1_Flujo_1;
	private List<ListaReporteUnidadToe> listUnid_Prod_2_TipOferta_1_Flujo_2;
	private List<ListaReporteUnidadToe> listUnid_Prod_2_TipOferta_2_Flujo_1;
	private List<ListaReporteUnidadToe> listUnid_Prod_2_TipOferta_2_Flujo_2;
	private List<List<ListaReporteUnidadToe>> listUnid_Prod_TipOferta_Flujo;
	
	//valores generales
	private static String valorColum="";
	private static String valorColumPerfil="";
	private static String valorColumPerfilLista="";
	private static String valorColumObjPerfil="";
	
	//valores especificos
	private static String valorColumEsp="";
	private static String valorColumPerfilEsp="";
	private static String valorColumObjPerfilEsp="";
	
	private String tituloEspecifico;
	private String msjSinResultado;
	
	private boolean tabla1;
	private boolean tabla2;
	private boolean tabla3;
	private boolean tabla4;
	private boolean tabla5;
	private boolean tabla6;
	private boolean tabla7;
	private boolean tabla8;
	
	private String tituloTabla1;
	private String tituloTabla2;
	private String tituloTabla3;
	private String tituloTabla4;
	private String tituloTabla5;
	private String tituloTabla6;
	private String tituloTabla7;
	private String tituloTabla8;
	
	
	private String PerfilEspecifico;
	private String EstadoEspecifico;
	private String UnidadEspecifico;
	private String ProductoEspecifico;
	private String TipoOfertaEspecifico;	
	
	
	private static final Logger LOG = LoggerFactory.getLogger(ReporteTCCSVServlet.class);
	
	/**
	 * TEMPORAL - ELIMINAR
	 * */
	/*
	public String tramaDatosGeneradosVO_Prod_1_TipOferta_1;
	public String tramaDatosGeneradosVO_Prod_1_TipOferta_2;
	public String tramaDatosGeneradosVO_Prod_2_TipOferta_1;
	public String tramaDatosGeneradosVO_Prod_2_TipOferta_2;
	*/
	public AyudaDatosReporte(){
		try {
			productoBean = (ProductoBeanLocal) new InitialContext()
			.lookup("ejblocal:com.ibm.bbva.session.ProductoBeanLocal");
			
			tipoOfertaobean = (TipoOfertaBeanLocal) new InitialContext()
			.lookup("ejblocal:com.ibm.bbva.session.TipoOfertaBeanLocal");	

			posibleValorBean = (PosibleValorBeanLocal) new InitialContext()
			.lookup("ejblocal:com.ibm.bbva.session.PosibleValorBeanLocal");
			
			perfilBean = (PerfilBeanLocal) new InitialContext()
			.lookup("ejblocal:com.ibm.bbva.session.PerfilBeanLocal");
			
			estadoCEBean = (EstadoCEBeanLocal) new InitialContext()
			.lookup("ejblocal:com.ibm.bbva.session.EstadoCEBeanLocal");
			
		} catch (NamingException e) {
			// TODO Bloque catch generado automáticamente
			LOG.error(e.getMessage(), e);
		}		
	}

	public String buscarGenerarTOE(ArrayList arrayList){

		String sTipoReport=arrayList.get(8).toString(); // TIPO REPORTE
		String sTipoProduct=arrayList.get(2).toString();	// PRODUCTO
		String sTipoOferta=arrayList.get(4).toString();	// TIPO OFERTA
		
			if(sTipoReport!=null && sTipoReport.trim().length()>0){
				Integer iTipoReport=Integer.parseInt(sTipoReport.trim());
						
				if(iTipoReport==Integer.parseInt(Constantes.ID_TOE_GENERAL)){
					
					if(sTipoProduct!=null && sTipoProduct.trim().length()>0){
						
						List<Producto> listProducto=new ArrayList<Producto>();
						String tmpSTipoProduct=sTipoProduct;
						
						if (sTipoProduct.trim().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){// TODOS LOS PRODUCTOS		
							listProducto=productoBean.buscarTodos();
							this.viewProductoPLD=true;
							this.viewProductoTC=true;
						}else{
							LOG.info("sTipoProduct="+sTipoProduct);
							if(productoBean!=null)
								LOG.info("productoBean no es null");
							else
								LOG.info("productoBean es null");
							
							listProducto.add(productoBean.buscarPorId(Long.parseLong(sTipoProduct)));
							
							if(Integer.parseInt(sTipoProduct)==(Constantes.ID_APLICATIVO_TC)){
								this.viewProductoTC=true;
								this.viewProductoPLD=false;
							}else{
								this.viewProductoTC=false;
								this.viewProductoPLD=true;
							}
								
						}
						
						mapaDatosProductoTipoOferta= new TreeMap<Integer, Map<Integer,Map<String,Map<String,ArrayList<DatosGeneradosVO>>>>>();
						mapaPorcentajeProductoTipoOferta= new TreeMap<Integer, Map<Integer,Map<String,ArrayList<DatosPorcentajeVO>>>>();
						mapaUnidadProductoTipoOferta= new TreeMap<Integer, Map<Integer,Map<String,ArrayList<DatosUnidadVO>>>>();
							
						for(Producto prod: listProducto){
							arrayList.set(2, String.valueOf(prod.getId()));
								
							mapaDatosTipoOfertaRol=agregarMapaProductoTipoOferta(mapaDatosProductoTipoOferta, Integer.parseInt(arrayList.get(2).toString()));
							mapaPorcentajeTipoOfertaRol=agregarMapaPorcenProductoTipoOferta(mapaPorcentajeProductoTipoOferta, Integer.parseInt(arrayList.get(2).toString()));
							mapaUnidadTipoOfertaRol=agregarMapaUnidProductoTipoOferta(mapaUnidadProductoTipoOferta, Integer.parseInt(arrayList.get(2).toString()));

							if(sTipoOferta!=null && sTipoOferta.trim().length()>0){
								List<TipoOferta> lisTipoOferta=new ArrayList<TipoOferta>();
								String tmpSTipoOferta=sTipoOferta;
									
								if (sTipoOferta.trim().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){ // TODOS
									lisTipoOferta = tipoOfertaobean.buscarTodos();				
									this.viewTipoOfertaAprobado=true;
									this.viewTipoOfertaRegular=true;	
								}else{
									lisTipoOferta.add(tipoOfertaobean.buscarPorId(Long.parseLong(sTipoOferta)));
									
									if(sTipoOferta.equals(Constantes.CODIGO_OFERTA_APROBADO)){
											this.viewTipoOfertaAprobado=true;
											this.viewTipoOfertaRegular=false;
									}else{
											this.viewTipoOfertaAprobado=false;
											this.viewTipoOfertaRegular=true;
									}									
								}

								for(TipoOferta tipoOferta: lisTipoOferta){									
									arrayList.set(4, String.valueOf(tipoOferta.getId()));
		
									listDatosGeneradosVO=this.actualizarListaTiemposTOE(arrayList, "1"); 
									listDatosGeneradosVO_reproceso=this.actualizarListaTiemposTOE(arrayList, "2");	
									if(listDatosGeneradosVO_reproceso!=null && listDatosGeneradosVO_reproceso.size()>0){
										listDatosGeneradosVO.addAll(listDatosGeneradosVO_reproceso);	
									}									
										
									agregarMapaTipoOferta(mapaDatosTipoOfertaRol, Integer.parseInt(arrayList.get(4).toString()), listDatosGeneradosVO);
											
											
									listDatosPorcentajeVO=actualizarListaPorcentajeTOE(arrayList);
											
									agregarMapaPorcentajeTipoOferta(mapaPorcentajeTipoOfertaRol, Integer.parseInt(arrayList.get(4).toString()), listDatosPorcentajeVO);
											
									listDatosUnidadVO=actualizarListaUnidadTOE(arrayList);
											
									agregarMapaUnidadTipoOferta(mapaUnidadTipoOfertaRol, Integer.parseInt(arrayList.get(4).toString()), listDatosUnidadVO);

									}
								
									arrayList.set(4, tmpSTipoOferta);								
								}							
							}
							arrayList.set(2, tmpSTipoProduct);										
					}
					
					listPosibleValor = posibleValorBean.buscarPorIdColumna(Constantes.ID_POSIBLE_VALOR_TOE);

					this.viewTablaGeneral=true;
					this.viewTablaEspecifica=false;
					
					generar_cargarListaRolPerfil(listPosibleValor);
					cargarListaTiempo_tc_Te(listPosibleValor);
					cargarLista(arrayList);
					
					//setearValores();
					
				}else{
					//Reporte Especifico
					
					listaReporte_Prod_TipoOferta_Flujo=new ArrayList<List<ListaReporteToe>>();
					listDatosGeneradosVO=this.actualizarListaTiemposTOE(arrayList, "0");
					
					if(listDatosGeneradosVO!=null && listDatosGeneradosVO.size()>0){
						listPosibleValor = posibleValorBean.buscarPorIdColumnaIdValor(Constantes.ID_POSIBLE_VALOR_TOE, 
								listDatosGeneradosVO.get(0).getValor().trim());
						listDatosPorcentajeVO=this.actualizarListaPorcentajeTOE(arrayList);						
						inicializaValoresEspecificos();
						cargarValoresFiltroEspecifico(arrayList);
						cargarLista(arrayList);
						this.viewTablaEspecifica=false;
						this.viewTablaGeneral=false;
						this.tituloEspecifico=crearTituloEspecifico(arrayList);

					}else{
						this.setMsjSinResultado("No se encontró resultado con los filtros seleccionados");
						this.viewTablaEspecifica=false;
						return "1";
					}
	
				}			
			}			

		return null;
	}
	
	public void cargarValoresFiltroEspecifico(ArrayList arrayList){
		//TablaGenerarTOEMB.valorColumEsp=Constantes.VALOR_VACIO;
		//TablaGenerarTOEMB.valorColumPerfilEsp=Constantes.VALOR_VACIO;
		//TablaGenerarTOEMB.valorColumObjPerfilEsp=Constantes.VALOR_VACIO;

		if(arrayList.get(3)!=null && !(arrayList.get(3).toString()).equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			Perfil objPerfilTemp=perfilBean.buscarPorId(Long.parseLong(arrayList.get(3).toString()));
			if(objPerfilTemp!=null){
				PerfilEspecifico=objPerfilTemp.getDescripcion();
				if(Integer.parseInt(String.valueOf(objPerfilTemp.getId()))==Constantes.ID_PERFIL_ANALISIS_DE_RIESGOS || 
						Integer.parseInt(String.valueOf(objPerfilTemp.getId()))==Constantes.ID_PERFIL_RIESGOS_SUPERIOR){
					UnidadEspecifico="RIESGO";
				}else{
					UnidadEspecifico="CPM";
				}
			}			
		}
		
		if(arrayList.get(2)!=null && !(arrayList.get(2).toString()).equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			Producto objProdTemp=productoBean.buscarPorId(Long.parseLong(arrayList.get(2).toString()));
			if(objProdTemp!=null){
				this.setProductoEspecifico(objProdTemp.getDescripcion());
			}			
		}	
		
		if(arrayList.get(4)!=null && !(arrayList.get(4).toString()).equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			TipoOferta objTipoOfertaTemp=tipoOfertaobean.buscarPorId(Long.parseLong(arrayList.get(4).toString()));
			if(objTipoOfertaTemp!=null){
				this.setTipoOfertaEspecifico(objTipoOfertaTemp.getDescripcion());
			}			
		}		

		if(arrayList.get(5)!=null  && !(arrayList.get(5).toString()).equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			EstadoCE objEstadoTemp= estadoCEBean.buscarPorId(Long.parseLong(arrayList.get(5).toString()));
			if(objEstadoTemp!=null){
				EstadoEspecifico=objEstadoTemp.getDescripcion();
			}
		}	
		
		
	}


	public ArrayList<String> obtListaEspecifica() {
		ArrayList<String> result=new ArrayList<String>();
		result.add(UnidadEspecifico);
		return result;
	}
	
	
	public ArrayList<String> obtListaPerfilTxt() {
		ArrayList<String> result=new ArrayList<String>();
		String rpta=PerfilEspecifico;
		
		if(EstadoEspecifico!=null)
			rpta=rpta +" \n "+EstadoEspecifico ;
		

		result.add(rpta);
		
		return result;

	}


	public ArrayList<String> objListaPorcentaje(){
		ArrayList<String> result=new ArrayList<String>();
		LOG.info("objListaPorcentaje");
		if(listDatosPorcentajeVO!=null && listDatosPorcentajeVO.size()>0){
			LOG.info("listDatosPorcentajeVO tamaño "+listDatosPorcentajeVO.size());
			String resultado=listDatosPorcentajeVO.get(0).getValorPorcentaje();
			LOG.info("resultado:::"+resultado);
			result.add(resultado);
			return result;
		}
		return null;
	}


	public String objListaObjTxt(String cad, String tipo){

		String cadResult=this.devolverObjetivo(cad, tipo);
			
			return cadResult;

			
	}

	public String devolverObjetivo(String cad, String tip){
		
		if(listDatosGeneradosVO!=null && listDatosGeneradosVO.size()>0){

			for(DatosGeneradosVO obj: listDatosGeneradosVO){
				if(cad.equals(Constantes.ID_TIEMPO_OBJ)){
					if(obj.getTipoConcepto().trim().equals(Constantes.DESCRIPCION_TIEMPO_OBJETIVO.toUpperCase()))
						if(tip.equals(Constantes.ID_TIEMPO_TE))
							return obj.getTe();
						else
							return obj.getTc();									 
							 						
					}else
						if(cad.equals(Constantes.ID_TIEMPO_PRE)){
							if(obj.getTipoConcepto().trim().equals(Constantes.DESCRIPCION_TIEMPO_PRELIMINAR.toUpperCase()))
								if(tip.equals(Constantes.ID_TIEMPO_TE))
									return obj.getTe();
								else
									return obj.getTc();									 
								 								
						}else
							if(obj.getTipoConcepto().trim().equals(Constantes.DESCRIPCION_TIEMPO_REAL.toUpperCase()))
								if(tip.equals(Constantes.ID_TIEMPO_TE))
									return obj.getTe();
								else
									return obj.getTc();										
			}			
		}
		return null;
	}	

	public String crearTituloEspecifico(ArrayList arrayList){
		Producto prod= productoBean.buscarPorId(Long.parseLong(arrayList.get(2).toString()));
		TipoOferta tipOferta= tipoOfertaobean.buscarPorId(Long.parseLong(arrayList.get(4).toString()));
		
		if(prod!=null && tipOferta!=null)
			return obtTituloEncabezadoTablas(viewTablaGeneral, prod.getDescripcion(), tipOferta.getDescripcion(), "");
		return "";
	}
	
	public String obtTituloEncabezadoTablas(boolean tipoTitGeneral, String tipoProducto, String tipoOferta, String tipoFlujo){
		String subTitulo="";

	        //subTitulo=Constantes.DESCRIPCION_SUBTITULO_1; //+this.obtRangoFechas();
	        subTitulo=subTitulo+ Constantes.VALOR_SEPARADOR_TITULO+Constantes.DESCRIPCION_SUBTITULO_1_1+ tipoProducto;
	        subTitulo=subTitulo+ Constantes.VALOR_SEPARADOR_TITULO+Constantes.DESCRIPCION_SUBTITULO_2+ tipoOferta;
	        
	        if(tipoTitGeneral)
	        	subTitulo=subTitulo+ Constantes.VALOR_SEPARADOR_TITULO+Constantes.DESCRIPCION_SUBTITULO_3+ tipoFlujo;
	        
	      return subTitulo;	
			
	}
	
	public List<DatosGeneradosVO> actualizarListaTiemposTOE (ArrayList arrayList , String opcion) {

		List<DatosGeneradosVO> listDatosGeneradosVO=null;
		
		if (this.tablaFacadeBean == null) {
			this.tablaFacadeBean = new TablaFacadeBean();
		}	

		if(arrayList!=null){
			
			if(arrayList.size()==10)
				arrayList.set(9, String.valueOf(opcion));
			else
				arrayList.add(String.valueOf(opcion));
			
			
			listDatosGeneradosVO=tablaFacadeBean.getGenerarDatosGenericoTOE(arrayList);
		}

		return listDatosGeneradosVO;
	}		
	
	public void setearBoolTabla(boolean v){
		this.setTabla1(v);
		this.setTabla2(v);
		this.setTabla3(v);
		this.setTabla4(v);
		this.setTabla5(v);
		this.setTabla6(v);
		this.setTabla7(v);
		this.setTabla8(v);
	}
	
	public void cargarLista(ArrayList arrayList){

		setearBoolTabla(false);
		LOG.info("cargarLista");
		listaReporte_Prod_TipoOferta_Flujo=new ArrayList<List<ListaReporteToe>>();
		listPorc_Prod_TipOferta_Flujo=new ArrayList<List<ListaReportePorcentajeToe>>();
		listUnid_Prod_TipOferta_Flujo = new ArrayList<List<ListaReporteUnidadToe>>();
		if(this.viewTablaGeneral){					//REPORTE - GENERICO
			LOG.info("Tabla general");
			if(this.viewProductoTC){				// PRODUCTO - TC
				LOG.info("tabla prod tc");
				if(this.viewTipoOfertaAprobado){	//TIPO OFERTA - APROBADO
					
					listPosibleValor = posibleValorBean.buscarPorIdColumna(Constantes.ID_POSIBLE_VALOR_TOE);
					
					LOG.info("oferta aprobado");
					listReport_Prod_1_TipOferta_1_Flujo_1 =cargarLista_Generico_Filtros(Constantes.ID_APLICATIVO_TC, Constantes.CODIGO_OFERTA_APROBADO, 
							Constantes.VALOR_FLUJO_REGULAR);
					listPorc_Prod_1_TipOferta_1_Flujo_1=cargarListaPorcentaje_Generico_Filtros(Constantes.ID_APLICATIVO_TC, Constantes.CODIGO_OFERTA_APROBADO, 
							Constantes.VALOR_FLUJO_REGULAR);
					listUnid_Prod_1_TipOferta_1_Flujo_1=cargarListaUnidad_Generico_Filtros(Constantes.ID_APLICATIVO_TC, Constantes.CODIGO_OFERTA_APROBADO, 
							Constantes.VALOR_FLUJO_REGULAR);					
					
					listReport_Prod_1_TipOferta_1_Flujo_2 = cargarLista_Generico_Filtros(Constantes.ID_APLICATIVO_TC, Constantes.CODIGO_OFERTA_APROBADO, 
							Constantes.VALOR_FLUJO_REPROCESO);	
					listPorc_Prod_1_TipOferta_1_Flujo_2= cargarListaPorcentaje_Generico_Filtros(Constantes.ID_APLICATIVO_TC, Constantes.CODIGO_OFERTA_APROBADO, 
							Constantes.VALOR_FLUJO_REPROCESO);
					listUnid_Prod_1_TipOferta_1_Flujo_2=cargarListaUnidad_Generico_Filtros(Constantes.ID_APLICATIVO_TC, Constantes.CODIGO_OFERTA_APROBADO, 
							Constantes.VALOR_FLUJO_REPROCESO);	
					this.setTabla1(true);
					this.setTabla2(true);
					
					this.setTituloTabla1(obtTituloEncabezadoTablas(this.viewTablaGeneral,Constantes.DESCRIPCION_TIPO_PRODUCTO_TC, 
							Constantes.DESCRIPCION_TIPO_OFERTA_APROBADO, Constantes.DESCRIPCION_TIPO_FLUJO_REGULAR));
					this.setTituloTabla2(obtTituloEncabezadoTablas(this.viewTablaGeneral,Constantes.DESCRIPCION_TIPO_PRODUCTO_TC, 
							Constantes.DESCRIPCION_TIPO_OFERTA_APROBADO, Constantes.DESCRIPCION_TIPO_FLUJO_REPROCESO));	
					
					listaReporte_Prod_TipoOferta_Flujo.add(listReport_Prod_1_TipOferta_1_Flujo_1);
					listaReporte_Prod_TipoOferta_Flujo.add(listReport_Prod_1_TipOferta_1_Flujo_2);
					
					listPorc_Prod_TipOferta_Flujo.add(listPorc_Prod_1_TipOferta_1_Flujo_1);
					listPorc_Prod_TipOferta_Flujo.add(listPorc_Prod_1_TipOferta_1_Flujo_2);
					
					listUnid_Prod_TipOferta_Flujo.add(listUnid_Prod_1_TipOferta_1_Flujo_1);
					listUnid_Prod_TipOferta_Flujo.add(listUnid_Prod_1_TipOferta_1_Flujo_2);
					
					LOG.info("tamaño listaReporte_Prod_TipoOferta_Flujo = "+listaReporte_Prod_TipoOferta_Flujo.size());
				}
				
				if(this.viewTipoOfertaRegular){	//TIPO OFERTA - REGULAR
					LOG.info("tamaño oferta regular ");
					listReport_Prod_1_TipOferta_2_Flujo_1 = cargarLista_Generico_Filtros(Constantes.ID_APLICATIVO_TC, Constantes.CODIGO_OFERTA_REGULAR, 
							Constantes.VALOR_FLUJO_REGULAR);
					listPorc_Prod_1_TipOferta_2_Flujo_1 = cargarListaPorcentaje_Generico_Filtros(Constantes.ID_APLICATIVO_TC, Constantes.CODIGO_OFERTA_REGULAR, 
							Constantes.VALOR_FLUJO_REGULAR);
					listUnid_Prod_1_TipOferta_2_Flujo_1=cargarListaUnidad_Generico_Filtros(Constantes.ID_APLICATIVO_TC, Constantes.CODIGO_OFERTA_REGULAR, 
							Constantes.VALOR_FLUJO_REGULAR);	
					
					listReport_Prod_1_TipOferta_2_Flujo_2 = cargarLista_Generico_Filtros(Constantes.ID_APLICATIVO_TC, Constantes.CODIGO_OFERTA_REGULAR, 
							Constantes.VALOR_FLUJO_REPROCESO);	
					listPorc_Prod_1_TipOferta_2_Flujo_2 = cargarListaPorcentaje_Generico_Filtros(Constantes.ID_APLICATIVO_TC, Constantes.CODIGO_OFERTA_REGULAR, 
							Constantes.VALOR_FLUJO_REPROCESO);
					listUnid_Prod_1_TipOferta_2_Flujo_2 = cargarListaUnidad_Generico_Filtros(Constantes.ID_APLICATIVO_TC, Constantes.CODIGO_OFERTA_REGULAR, 
							Constantes.VALOR_FLUJO_REPROCESO);
					
					this.setTabla3(true);
					this.setTabla4(true);
					
					this.setTituloTabla3(obtTituloEncabezadoTablas(this.viewTablaGeneral,Constantes.DESCRIPCION_TIPO_PRODUCTO_TC, 
							Constantes.DESCRIPCION_TIPO_OFERTA_REGULAR, Constantes.DESCRIPCION_TIPO_FLUJO_REGULAR));
					this.setTituloTabla4(obtTituloEncabezadoTablas(this.viewTablaGeneral,Constantes.DESCRIPCION_TIPO_PRODUCTO_TC, 
							Constantes.DESCRIPCION_TIPO_OFERTA_REGULAR, Constantes.DESCRIPCION_TIPO_FLUJO_REPROCESO));	
					
					listaReporte_Prod_TipoOferta_Flujo.add(listReport_Prod_1_TipOferta_2_Flujo_1);
					listaReporte_Prod_TipoOferta_Flujo.add(listReport_Prod_1_TipOferta_2_Flujo_2);
					
					listPorc_Prod_TipOferta_Flujo.add(listPorc_Prod_1_TipOferta_2_Flujo_1);
					listPorc_Prod_TipOferta_Flujo.add(listPorc_Prod_1_TipOferta_2_Flujo_2);
					
					listUnid_Prod_TipOferta_Flujo.add(listUnid_Prod_1_TipOferta_2_Flujo_1);
					listUnid_Prod_TipOferta_Flujo.add(listUnid_Prod_1_TipOferta_2_Flujo_2);	
					
					LOG.info("tamaño listaReporte_Prod_TipoOferta_Flujo = "+listaReporte_Prod_TipoOferta_Flujo.size());
				}				
				
			}
			
			if(this.viewProductoPLD){				// PRODUCTO - PLD
				
				if(this.viewTipoOfertaAprobado){	//TIPO OFERTA - APROBADO
					
					listReport_Prod_2_TipOferta_1_Flujo_1 = cargarLista_Generico_Filtros(Constantes.ID_APLICATIVO_PLD, Constantes.CODIGO_OFERTA_APROBADO, 
							Constantes.VALOR_FLUJO_REGULAR);
					listPorc_Prod_2_TipOferta_1_Flujo_1 = cargarListaPorcentaje_Generico_Filtros(Constantes.ID_APLICATIVO_PLD, Constantes.CODIGO_OFERTA_APROBADO, 
							Constantes.VALOR_FLUJO_REGULAR);		
					listUnid_Prod_2_TipOferta_1_Flujo_1 = cargarListaUnidad_Generico_Filtros(Constantes.ID_APLICATIVO_PLD, Constantes.CODIGO_OFERTA_APROBADO, 
							Constantes.VALOR_FLUJO_REGULAR);					
					
					listReport_Prod_2_TipOferta_1_Flujo_2 = cargarLista_Generico_Filtros(Constantes.ID_APLICATIVO_PLD, Constantes.CODIGO_OFERTA_APROBADO, 
							Constantes.VALOR_FLUJO_REPROCESO);
					listPorc_Prod_2_TipOferta_1_Flujo_2 = cargarListaPorcentaje_Generico_Filtros(Constantes.ID_APLICATIVO_PLD, Constantes.CODIGO_OFERTA_APROBADO, 
							Constantes.VALOR_FLUJO_REPROCESO);		
					listUnid_Prod_2_TipOferta_1_Flujo_2 = cargarListaUnidad_Generico_Filtros(Constantes.ID_APLICATIVO_PLD, Constantes.CODIGO_OFERTA_APROBADO, 
							Constantes.VALOR_FLUJO_REPROCESO);	
					
					this.setTabla5(true);
					this.setTabla6(true);
					
					this.setTituloTabla5(obtTituloEncabezadoTablas(this.viewTablaGeneral,Constantes.DESCRIPCION_TIPO_PRODUCTO_PLD, 
							Constantes.DESCRIPCION_TIPO_OFERTA_APROBADO, Constantes.DESCRIPCION_TIPO_FLUJO_REGULAR));	
					this.setTituloTabla6(obtTituloEncabezadoTablas(this.viewTablaGeneral,Constantes.DESCRIPCION_TIPO_PRODUCTO_PLD, 
							Constantes.DESCRIPCION_TIPO_OFERTA_APROBADO, Constantes.DESCRIPCION_TIPO_FLUJO_REPROCESO));
					
					listaReporte_Prod_TipoOferta_Flujo.add(listReport_Prod_2_TipOferta_1_Flujo_1);
					listaReporte_Prod_TipoOferta_Flujo.add(listReport_Prod_2_TipOferta_1_Flujo_2);
					
					listPorc_Prod_TipOferta_Flujo.add(listPorc_Prod_2_TipOferta_1_Flujo_1);
					listPorc_Prod_TipOferta_Flujo.add(listPorc_Prod_2_TipOferta_1_Flujo_2);
					
					listUnid_Prod_TipOferta_Flujo.add(listUnid_Prod_2_TipOferta_1_Flujo_1);
					listUnid_Prod_TipOferta_Flujo.add(listUnid_Prod_2_TipOferta_1_Flujo_2);
					
				}
				
				if(this.viewTipoOfertaRegular){	//TIPO OFERTA - REGULAR
						
					listReport_Prod_2_TipOferta_2_Flujo_1 = cargarLista_Generico_Filtros(Constantes.ID_APLICATIVO_PLD, Constantes.CODIGO_OFERTA_REGULAR, 
							Constantes.VALOR_FLUJO_REGULAR);
					listPorc_Prod_2_TipOferta_2_Flujo_1 = cargarListaPorcentaje_Generico_Filtros(Constantes.ID_APLICATIVO_PLD, Constantes.CODIGO_OFERTA_REGULAR, 
							Constantes.VALOR_FLUJO_REGULAR);	
					listUnid_Prod_2_TipOferta_2_Flujo_1 = cargarListaUnidad_Generico_Filtros(Constantes.ID_APLICATIVO_PLD, Constantes.CODIGO_OFERTA_REGULAR, 
							Constantes.VALOR_FLUJO_REGULAR);
					
					listReport_Prod_2_TipOferta_2_Flujo_2 = cargarLista_Generico_Filtros(Constantes.ID_APLICATIVO_PLD, Constantes.CODIGO_OFERTA_REGULAR, 
							Constantes.VALOR_FLUJO_REPROCESO);
					listPorc_Prod_2_TipOferta_2_Flujo_2 = cargarListaPorcentaje_Generico_Filtros(Constantes.ID_APLICATIVO_PLD, Constantes.CODIGO_OFERTA_REGULAR, 
							Constantes.VALOR_FLUJO_REPROCESO);	
					listUnid_Prod_2_TipOferta_2_Flujo_2 = cargarListaUnidad_Generico_Filtros(Constantes.ID_APLICATIVO_PLD, Constantes.CODIGO_OFERTA_REGULAR, 
							Constantes.VALOR_FLUJO_REPROCESO);
					
					this.setTabla7(true);
					this.setTabla8(true);
					
					this.setTituloTabla7(obtTituloEncabezadoTablas(this.viewTablaGeneral,Constantes.DESCRIPCION_TIPO_PRODUCTO_PLD, 
							Constantes.DESCRIPCION_TIPO_OFERTA_REGULAR, Constantes.DESCRIPCION_TIPO_FLUJO_REGULAR));
					this.setTituloTabla8(obtTituloEncabezadoTablas(this.viewTablaGeneral,Constantes.DESCRIPCION_TIPO_PRODUCTO_PLD, 
							Constantes.DESCRIPCION_TIPO_OFERTA_REGULAR, Constantes.DESCRIPCION_TIPO_FLUJO_REPROCESO));		
					
					listaReporte_Prod_TipoOferta_Flujo.add(listReport_Prod_2_TipOferta_2_Flujo_1);
					listaReporte_Prod_TipoOferta_Flujo.add(listReport_Prod_2_TipOferta_2_Flujo_2);
					
					listPorc_Prod_TipOferta_Flujo.add(listPorc_Prod_2_TipOferta_2_Flujo_1);
					listPorc_Prod_TipOferta_Flujo.add(listPorc_Prod_2_TipOferta_2_Flujo_2);
					
					listUnid_Prod_TipOferta_Flujo.add(listUnid_Prod_2_TipOferta_2_Flujo_1);
					listUnid_Prod_TipOferta_Flujo.add(listUnid_Prod_2_TipOferta_2_Flujo_2);
				}				
				
			}			
		}else{
			LOG.info("Tabla especifica");

			listReport_Prod_1_TipOferta_1_Flujo_1 =cargarLista_Especificos_Filtros();
			listPorc_Prod_1_TipOferta_1_Flujo_1=cargarListaPorcentaje_Especificos_Filtros();
			listaReporte_Prod_TipoOferta_Flujo.add(listReport_Prod_1_TipOferta_1_Flujo_1);
			listPorc_Prod_TipOferta_Flujo.add(listPorc_Prod_1_TipOferta_1_Flujo_1);
			LOG.info("Tamaño de listaReporte_Prod_TipoOferta_Flujo:::"+listaReporte_Prod_TipoOferta_Flujo.size());
			LOG.info("Tamaño de listPorc_Prod_TipOferta_Flujo:::"+listPorc_Prod_TipOferta_Flujo.size());
		}		
	}
	
	public List<ListaReporteToe> cargarLista_Generico_Filtros(int id_producto, String id_tipo_oferta, String id_flujo){
		ArrayList arrayListTiempo=new ArrayList();
		List<ListaReporteToe> listTemp=new ArrayList<ListaReporteToe>();
		
		arrayListTiempo.add(Constantes.DESCRIPCION_TIEMPO_OBJETIVO.toUpperCase().trim());
		arrayListTiempo.add(Constantes.DESCRIPCION_TIEMPO_PRELIMINAR.toUpperCase().trim());
		arrayListTiempo.add(Constantes.DESCRIPCION_TIEMPO_REAL.toUpperCase().trim());
		
	//	listTemp.add(cargarCabeceraTiempo());
		
		for (int i = 0; i < arrayListTiempo.size(); i++){
			listTemp.add(cargarLista_Generico_Filtros(id_producto, id_tipo_oferta, 
					id_flujo, (String) arrayListTiempo.get(i)));
		}	
		
		return listTemp;
	}
	
	public ListaReporteToe cargarLista_Generico_Filtros(int id_producto, String id_tipo_oferta, String id_flujo, String tiempo){
		objListaReporteToe=new ListaReporteToe();
		
		if(mapaDatosProductoTipoOferta!=null && id_producto>0){
			Map<Integer, Map<String, Map<String, ArrayList<DatosGeneradosVO>>>> mapTipoOferta = 
					mapaDatosProductoTipoOferta.get(id_producto);
			
			if(id_producto>0 && id_tipo_oferta!=null && id_flujo!=null && tiempo!=null)
			if(mapTipoOferta!=null){
				
				Map<String, Map<String, ArrayList<DatosGeneradosVO>>>  mapTipoFlujo = 
						mapTipoOferta.get(Integer.parseInt(id_tipo_oferta));
				
				if(mapTipoFlujo!=null){
					
					Map<String, ArrayList<DatosGeneradosVO>> mapTipoConcepto = 
							mapTipoFlujo.get(id_flujo);
					
					if(mapTipoConcepto!=null){
						
						ArrayList<DatosGeneradosVO> listDatosGeneradosVO = 
								mapTipoConcepto.get(tiempo);
						
						inicializaValoresGenerales();
						
						objListaReporteToe.setColumn0(tiempo);
						
						if(listDatosGeneradosVO!=null){
							
							for(DatosGeneradosVO vo: listDatosGeneradosVO){
								boolean bol;
								
								do{
									PosibleValor pv =obtenerRolLista();
									if(pv.getValor().trim().equals(vo.getValor()))
										bol=true;
									else
										bol=false;
									
									setearColumnas(vo, pv);
								}while(!bol);
							
							}			
						}else{
							boolean bol;
							
							do{
								PosibleValor pv =obtenerRolLista();				
								setearColumnas(new DatosGeneradosVO(), pv);
								
								if(pv.getValor().trim().equals("CEE"))
									bol=true;
								else
									bol=false;
								
							}while(!bol);			
						}			
					}				
				}					
			}			
		}


		return objListaReporteToe;
	}
	public List<ListaReporteToe> cargarLista_Especificos_Filtros(){
		ArrayList arrayListTiempo=new ArrayList();
		List<ListaReporteToe> listTemp=new ArrayList<ListaReporteToe>();
		
		arrayListTiempo.add(Constantes.DESCRIPCION_TIEMPO_OBJETIVO.toUpperCase().trim());
		arrayListTiempo.add(Constantes.DESCRIPCION_TIEMPO_PRELIMINAR.toUpperCase().trim());
		arrayListTiempo.add(Constantes.DESCRIPCION_TIEMPO_REAL.toUpperCase().trim());
		
	//	listTemp.add(cargarCabeceraTiempo());
		
		for (int i = 0; i < arrayListTiempo.size(); i++){
			listTemp.add(cargarLista_Espeficicos_Filtros((String) arrayListTiempo.get(i)));
		}	
		
		return listTemp;
	}
	public ListaReporteToe cargarLista_Espeficicos_Filtros(String tiempo){
		objListaReporteToe=new ListaReporteToe();

						objListaReporteToe.setColumn0(tiempo);
						
						if(listDatosGeneradosVO!=null){
							
							for(DatosGeneradosVO vo: listDatosGeneradosVO){
								LOG.info("Tiempo::::"+tiempo);
									if(vo.getTipoConcepto().trim().equals(tiempo)){
										setearColumnas(vo);;									 
									}		 						
							}			
						}			

		return objListaReporteToe;
	}	
	
	public void setearColumnas(DatosGeneradosVO vo){
		if(objListaReporteToe.getColumn1()==null){
			LOG.info("Seteando valores::::"+vo.getTc()+":::"+vo.getTe());
			objListaReporteToe.setColumn1(vo.getTc()==null?"":vo.getTc());
			objListaReporteToe.setColumn2(vo.getTe()==null?"":vo.getTe());				
		}
	}
	
	public void setearColumnas(DatosGeneradosVO vo, PosibleValor pv){

		if(pv.getValor().trim().equals(vo.getValor())){
			if(objListaReporteToe.getColumn1()==null){				
				objListaReporteToe.setColumn1(vo.getTe()==null?"":vo.getTe());					
			}else					
				if(objListaReporteToe.getColumn2()==null){
					objListaReporteToe.setColumn2(vo.getTc()==null?"":vo.getTc());
					objListaReporteToe.setColumn3(vo.getTe()==null?"":vo.getTe());					
				}else
					if(objListaReporteToe.getColumn4()==null){
						objListaReporteToe.setColumn4(vo.getTc()==null?"":vo.getTc());
						objListaReporteToe.setColumn5(vo.getTe()==null?"":vo.getTe());						
					}else						
						if(objListaReporteToe.getColumn6()==null){
							objListaReporteToe.setColumn6(vo.getTc()==null?"":vo.getTc());
							objListaReporteToe.setColumn7(vo.getTe()==null?"":vo.getTe());								
						}else								
							if(objListaReporteToe.getColumn8()==null){
								objListaReporteToe.setColumn8(vo.getTc()==null?"":vo.getTc());
								objListaReporteToe.setColumn9(vo.getTe()==null?"":vo.getTe());								
							}else								
								if(objListaReporteToe.getColumn10()==null){
									objListaReporteToe.setColumn10(vo.getTc()==null?"":vo.getTc());
									objListaReporteToe.setColumn11(vo.getTe()==null?"":vo.getTe());
								}else								
									if(objListaReporteToe.getColumn12()==null){
										objListaReporteToe.setColumn12(vo.getTc()==null?"":vo.getTc());
										objListaReporteToe.setColumn13(vo.getTe()==null?"":vo.getTe());
									}else								
										if(objListaReporteToe.getColumn14()==null){
											objListaReporteToe.setColumn14(vo.getTc()==null?"":vo.getTc());
											objListaReporteToe.setColumn15(vo.getTe()==null?"":vo.getTe());
										}else								
											if(objListaReporteToe.getColumn16()==null){
												objListaReporteToe.setColumn16(vo.getTc()==null?"":vo.getTc());
												objListaReporteToe.setColumn17(vo.getTe()==null?"":vo.getTe());
											}
		}else{
			
			if(objListaReporteToe.getColumn1()==null){
				objListaReporteToe.setColumn1("");			
			}else
				if(objListaReporteToe.getColumn2()==null){
					objListaReporteToe.setColumn2("");
					objListaReporteToe.setColumn3("");
				}else
					if(objListaReporteToe.getColumn4()==null){
						objListaReporteToe.setColumn4("");
						objListaReporteToe.setColumn5("");
					}else
						if(objListaReporteToe.getColumn6()==null){
							objListaReporteToe.setColumn6("");
							objListaReporteToe.setColumn7("");
						}else
							if(objListaReporteToe.getColumn8()==null){
								objListaReporteToe.setColumn8("");
								objListaReporteToe.setColumn9("");
							}else
								if(objListaReporteToe.getColumn10()==null){
									objListaReporteToe.setColumn10("");
									objListaReporteToe.setColumn11("");
								}else
									if(objListaReporteToe.getColumn12()==null){
										objListaReporteToe.setColumn12("");
										objListaReporteToe.setColumn13("");
									}else
										if(objListaReporteToe.getColumn14()==null){
											objListaReporteToe.setColumn14("");
											objListaReporteToe.setColumn15("");
										}else
											if(objListaReporteToe.getColumn16()==null){
												objListaReporteToe.setColumn16("");
												objListaReporteToe.setColumn17("");
											}		
							
		}		
	}
	
	public PosibleValor obtenerRolLista(){
		PosibleValor objPosibleValor=null;

		int j=0;
		if(listPosibleValor!=null){	
			for(int i=0;i<listPosibleValor.size()-1;i++){
				objPosibleValor=new PosibleValor();
				if(valorColumPerfilLista.equals(Constantes.VALOR_VACIO)){
					valorColumPerfilLista=obtDetallePerfil(listPosibleValor.get(i).getEtiqueta());
					objPosibleValor=listPosibleValor.get(i);
					break;
				}else
				{
					if(valorColumPerfilLista.equals(obtDetallePerfil(listPosibleValor.get(i).getEtiqueta()))){
						j=i+1;
						if(!valorColumPerfilLista.equals(obtDetallePerfil(listPosibleValor.get(j).getEtiqueta()))){
							valorColumPerfilLista=obtDetallePerfil(listPosibleValor.get(j).getEtiqueta());
							objPosibleValor=listPosibleValor.get(j);						
							break;							
						}
					}
				}
			}
			

		}
		
		return objPosibleValor;
	}
	
	public List<ListaReportePorcentajeToe> cargarListaPorcentaje_Generico_Filtros(int id_producto, String id_tipo_oferta, String id_flujo){
		List<ListaReportePorcentajeToe> listTemp=new ArrayList<ListaReportePorcentajeToe>();

			listTemp.add(cargarListaPorcentaje_Filtros(id_producto, id_tipo_oferta, 
					id_flujo));

		return listTemp;
	}
	
	public List<ListaReportePorcentajeToe> cargarListaPorcentaje_Especificos_Filtros(){
		List<ListaReportePorcentajeToe> listTemp=new ArrayList<ListaReportePorcentajeToe>();

			listTemp.add(cargarListaPorcentaje_Filtros());

		return listTemp;
	}
	
	public ListaReportePorcentajeToe cargarListaPorcentaje_Filtros(){
		objListaReportePorcentajeToe=new ListaReportePorcentajeToe();
		
	//	if(mapaPorcentajeProductoTipoOferta!=null){
		//	Map<Integer, Map<String, ArrayList<DatosPorcentajeVO>>> mapTipoOferta = 
			//		mapaPorcentajeProductoTipoOferta.get(id_producto);
			
			
		//	if(mapTipoOferta!=null){
				
			//	Map<String, ArrayList<DatosPorcentajeVO>>  mapTipoFlujo = 
				//		mapTipoOferta.get(Integer.parseInt(id_tipo_oferta));
				
				//if(mapTipoFlujo!=null){
					
					//ArrayList<DatosPorcentajeVO> listDatosPorcentajeVO = 
						//	mapTipoFlujo.get(id_flujo);

					//inicializaValoresGenerales();
					
					objListaReportePorcentajeToe.setColumn0("TOE");
					
					if(listDatosPorcentajeVO!=null){
						
						for(DatosPorcentajeVO vo: listDatosPorcentajeVO){
							//boolean bol;
							
							//do{
								//PosibleValor pv =obtenerRolLista();
								//if(pv.getValor().trim().equals(vo.getValorRol()))
									//bol=true;
								//else
									//bol=false;
								
								setearPorcentajeColumnas(vo);
							//}while(!bol);
						
						}			
					}/*else{
						boolean bol;
						
						do{
							PosibleValor pv =obtenerRolLista();
							setearPorcentajeColumnas(new DatosPorcentajeVO(), pv);
							
							if(pv.getValor().trim().equals("CEE"))
								bol=true;
							else
								bol=false;
							
						}while(!bol);			
					}*/				
				//}
				
			//}			
	//	}

		return objListaReportePorcentajeToe;
	}
	
	public ListaReportePorcentajeToe cargarListaPorcentaje_Filtros(int id_producto, String id_tipo_oferta, String id_flujo){
		objListaReportePorcentajeToe=new ListaReportePorcentajeToe();
		
		if(mapaPorcentajeProductoTipoOferta!=null){
			Map<Integer, Map<String, ArrayList<DatosPorcentajeVO>>> mapTipoOferta = 
					mapaPorcentajeProductoTipoOferta.get(id_producto);
			
			
			if(mapTipoOferta!=null){
				
				Map<String, ArrayList<DatosPorcentajeVO>>  mapTipoFlujo = 
						mapTipoOferta.get(Integer.parseInt(id_tipo_oferta));
				
				if(mapTipoFlujo!=null){
					
					ArrayList<DatosPorcentajeVO> listDatosPorcentajeVO = 
							mapTipoFlujo.get(id_flujo);

					inicializaValoresGenerales();
					
					objListaReportePorcentajeToe.setColumn0("TOE");
					
					if(listDatosPorcentajeVO!=null){
						
						for(DatosPorcentajeVO vo: listDatosPorcentajeVO){
							boolean bol;
							
							do{
								PosibleValor pv =obtenerRolLista();
								if(pv.getValor().trim().equals(vo.getValorRol()))
									bol=true;
								else
									bol=false;
								
								setearPorcentajeColumnas(vo, pv);
							}while(!bol);
						
						}			
					}else{
						boolean bol;
						
						do{
							PosibleValor pv =obtenerRolLista();
							setearPorcentajeColumnas(new DatosPorcentajeVO(), pv);
							
							if(pv.getValor().trim().equals("CEE"))
								bol=true;
							else
								bol=false;
							
						}while(!bol);			
					}				
				}
				
			}			
		}

		return objListaReportePorcentajeToe;
	}
	
	public void setearPorcentajeColumnas(DatosPorcentajeVO vo){
		if(objListaReportePorcentajeToe.getColumn1()==null){				
			objListaReportePorcentajeToe.setColumn1(vo.getValorPorcentaje()==null?"":vo.getValorPorcentaje());			
		}
	}
	
	public void setearPorcentajeColumnas(DatosPorcentajeVO vo, PosibleValor pv){
		if(pv.getValor().trim().equals(vo.getValorRol())){
			if(objListaReportePorcentajeToe.getColumn1()==null){				
				objListaReportePorcentajeToe.setColumn1(vo.getValorPorcentaje()==null?"":vo.getValorPorcentaje());			
			}else					
				if(objListaReportePorcentajeToe.getColumn2()==null){
					objListaReportePorcentajeToe.setColumn2(vo.getValorPorcentaje()==null?"":vo.getValorPorcentaje());				
				}else
					if(objListaReportePorcentajeToe.getColumn3()==null){
						objListaReportePorcentajeToe.setColumn3(vo.getValorPorcentaje()==null?"":vo.getValorPorcentaje());				
					}else						
						if(objListaReportePorcentajeToe.getColumn4()==null){
							objListaReportePorcentajeToe.setColumn4(vo.getValorPorcentaje()==null?"":vo.getValorPorcentaje());							
						}else								
							if(objListaReportePorcentajeToe.getColumn5()==null){
								objListaReportePorcentajeToe.setColumn5(vo.getValorPorcentaje()==null?"":vo.getValorPorcentaje());		
							}else								
								if(objListaReportePorcentajeToe.getColumn6()==null){
									objListaReportePorcentajeToe.setColumn6(vo.getValorPorcentaje()==null?"":vo.getValorPorcentaje());								
									
								}else								
									if(objListaReportePorcentajeToe.getColumn7()==null){
										objListaReportePorcentajeToe.setColumn7(vo.getValorPorcentaje()==null?"":vo.getValorPorcentaje());								
										
									}else								
										if(objListaReportePorcentajeToe.getColumn8()==null){
											objListaReportePorcentajeToe.setColumn8(vo.getValorPorcentaje()==null?"":vo.getValorPorcentaje());								
											
										}else								
											if(objListaReportePorcentajeToe.getColumn9()==null){
												objListaReportePorcentajeToe.setColumn9(vo.getValorPorcentaje()==null?"":vo.getValorPorcentaje());								
												
											}

		}else{
			
			if(objListaReportePorcentajeToe.getColumn1()==null){
				objListaReportePorcentajeToe.setColumn1("");				
			}else
				if(objListaReportePorcentajeToe.getColumn2()==null){
					objListaReportePorcentajeToe.setColumn2("");
				}else
					if(objListaReportePorcentajeToe.getColumn3()==null){
						objListaReportePorcentajeToe.setColumn3("");
					}else
						if(objListaReportePorcentajeToe.getColumn4()==null){
							objListaReportePorcentajeToe.setColumn4("");
						}else
							if(objListaReportePorcentajeToe.getColumn5()==null){
								objListaReportePorcentajeToe.setColumn5("");
							}else
								if(objListaReportePorcentajeToe.getColumn6()==null){
									objListaReportePorcentajeToe.setColumn6("");
								}else
									if(objListaReportePorcentajeToe.getColumn7()==null){
										objListaReportePorcentajeToe.setColumn7("");
									}else
										if(objListaReportePorcentajeToe.getColumn8()==null){
											objListaReportePorcentajeToe.setColumn8("");
										}else
											if(objListaReportePorcentajeToe.getColumn9()==null){
												objListaReportePorcentajeToe.setColumn9("");
											}		
							
		}		
	}
	
	
	public List<ListaReporteUnidadToe>  cargarListaUnidad_Generico_Filtros(int id_producto, String id_tipo_oferta, String id_flujo){
		List<ListaReporteUnidadToe> listTemp=new ArrayList<ListaReporteUnidadToe>();

		listTemp=cargarListaUnidad_Filtros(id_producto, id_tipo_oferta, 
					id_flujo);

		return listTemp;
	}
	
	public List<ListaReporteUnidadToe> cargarListaUnidad_Filtros(int id_producto, String id_tipo_oferta, String id_flujo){
		List<ListaReporteUnidadToe> listResult=new ArrayList<ListaReporteUnidadToe>();
		
		ArrayList<String> listConceptos=new ArrayList<String>();
		listConceptos.add("CPM");
		listConceptos.add("RIESGO");
		
		if(mapaUnidadProductoTipoOferta!=null){
			
			Map<Integer, Map<String, ArrayList<DatosUnidadVO>>> mapTipoOferta = 
					mapaUnidadProductoTipoOferta.get(id_producto);
			
			if(mapTipoOferta!=null){
				
				Map<String, ArrayList<DatosUnidadVO>>  mapTipoFlujo = 
						mapTipoOferta.get(Integer.parseInt(id_tipo_oferta));
				
				if(mapTipoFlujo!=null){
					ArrayList<DatosUnidadVO> listDatosUnidadVO = 
							mapTipoFlujo.get(id_flujo);

							for(int i=0; i<listConceptos.size(); i++){
								
								objListaReporteUnidadToe=new ListaReporteUnidadToe();
								
								float totalAceptados=0;
								float totalRegistros=0;
								double totalPorcentaje=0;
								
								setearUnidadColumnas(listConceptos.get(i).toString());
								
								if(listDatosUnidadVO!=null){
									for(DatosUnidadVO vo: listDatosUnidadVO){
										if(vo.getTipo().trim().equals(listConceptos.get(i).toString())){
											totalAceptados=totalAceptados+vo.getAceptado();
											totalRegistros=totalRegistros+vo.getTotal();
										}							
									}
									
									totalPorcentaje=(totalAceptados*100)/totalRegistros;
									totalPorcentaje=((double)Math.round(totalPorcentaje * 10) / 10);
								
								}else
									totalPorcentaje=0.0;
								
								setearUnidadColumnas(String.valueOf(totalPorcentaje));	
								
								listResult.add(objListaReporteUnidadToe);
							}					
				}
				
			}
			
		}


		
		return listResult;
	}
	
	public void setearUnidadColumnas(String cad){
		if(objListaReporteUnidadToe.getColumn1()==null){				
			objListaReporteUnidadToe.setColumn1(cad==null?"":cad);			
		}else					
			if(objListaReporteUnidadToe.getColumn2()==null){
					objListaReporteUnidadToe.setColumn2(cad==null?"":cad);				
			}
	}

	public ListaReporteToe cargarCabeceraTiempo(){
		objListaReporteToe=new ListaReporteToe();
		objListaReporteToe.setColumn1(Constantes.VALOR_TIEMPO_TE);
		objListaReporteToe.setColumn2(Constantes.VALOR_TIEMPO_TC);
		objListaReporteToe.setColumn3(Constantes.VALOR_TIEMPO_TE);
		objListaReporteToe.setColumn4(Constantes.VALOR_TIEMPO_TC);
		objListaReporteToe.setColumn5(Constantes.VALOR_TIEMPO_TE);
		objListaReporteToe.setColumn6(Constantes.VALOR_TIEMPO_TC);
		objListaReporteToe.setColumn7(Constantes.VALOR_TIEMPO_TE);
		objListaReporteToe.setColumn8(Constantes.VALOR_TIEMPO_TC);
		objListaReporteToe.setColumn9(Constantes.VALOR_TIEMPO_TE);
		objListaReporteToe.setColumn10(Constantes.VALOR_TIEMPO_TC);
		objListaReporteToe.setColumn11(Constantes.VALOR_TIEMPO_TE);
		objListaReporteToe.setColumn12(Constantes.VALOR_TIEMPO_TC);
		objListaReporteToe.setColumn13(Constantes.VALOR_TIEMPO_TE);
		objListaReporteToe.setColumn14(Constantes.VALOR_TIEMPO_TC);
		objListaReporteToe.setColumn15(Constantes.VALOR_TIEMPO_TE);
		objListaReporteToe.setColumn16(Constantes.VALOR_TIEMPO_TC);
		objListaReporteToe.setColumn17(Constantes.VALOR_TIEMPO_TE);
		
		return objListaReporteToe;
				
	}

	public void generar_cargarListaRolPerfil(List<PosibleValor> listPosibleValor) {
		
    	Map<String, ArrayList<String>> mapPerfilRol = new HashMap<String, ArrayList<String>>();
    	
    	if(listPosibleValor!=null){

    		generar_listaRoles=new ArrayList<String>();
    		generar_listaPerfiles=new ArrayList<String>();
    		
	    		for(PosibleValor objPosibleValor : listPosibleValor){
	        		
	    			String rol=obtDetalle(objPosibleValor.getEtiqueta());
	    			ArrayList<String> list= mapPerfilRol.get(rol);
	    			if(list==null){
	    				list=new ArrayList<String>();
	    				mapPerfilRol.put(rol, list);
	    				generar_listaRoles.add(rol);
	    				
	    			}
	    			
	    			String perfil=obtDetallePerfil(objPosibleValor.getEtiqueta());
	    			generar_listaPerfiles.add(perfil);
	    		}
    	}

    }	
	
	public String obtDetalle (String cad){
		if(cad!=null && !cad.equals(Constantes.VALOR_VACIO)){
			int pos=cad.trim().indexOf(Constantes.VALOR_SEPARADOR);
			cad=cad.substring(0, pos);
			return cad;
		}
		return null;
		
	}
	
	public String obtDetallePerfil (String cad){
		int pos=cad.trim().indexOf(Constantes.VALOR_SEPARADOR);	
		cad=cad.substring(pos+2, cad.length());
		return cad;
	}
	
	public void inicializaValoresGenerales(){
		AyudaDatosReporte.valorColum=Constantes.VALOR_VACIO;
		AyudaDatosReporte.valorColumPerfil=Constantes.VALOR_VACIO;
		AyudaDatosReporte.valorColumPerfilLista=Constantes.VALOR_VACIO;
		AyudaDatosReporte.valorColumObjPerfil=Constantes.VALOR_VACIO;		
	}
	
	public void inicializaValoresEspecificos(){
		AyudaDatosReporte.valorColumEsp=Constantes.VALOR_VACIO;
		AyudaDatosReporte.valorColumPerfilEsp=Constantes.VALOR_VACIO;
		AyudaDatosReporte.valorColumObjPerfilEsp=Constantes.VALOR_VACIO;
	
	}	

	public void agregarMapaUnidadTipoOferta (Map<Integer, Map<String, ArrayList<DatosUnidadVO>>>  mapa, 
			Integer codTipoOferta, List<DatosUnidadVO> listDatosUnidadVO) {
		
		Map<String, ArrayList<DatosUnidadVO>> mapaUnidadTipoFlujo= mapa.get(codTipoOferta);
		
		if(mapaUnidadTipoFlujo==null){
			mapaUnidadTipoFlujo=new TreeMap<String, ArrayList<DatosUnidadVO>>();
			mapa.put(codTipoOferta, mapaUnidadTipoFlujo);
		}
		
		mapaUnidadTipoFlujo(mapaUnidadTipoFlujo, listDatosUnidadVO);
	}	
	
	public void mapaUnidadTipoFlujo(Map<String, ArrayList<DatosUnidadVO>> mapa, List<DatosUnidadVO> listDatosUnidadVO){
		if(listDatosUnidadVO!=null)
		for (DatosUnidadVO objVo : listDatosUnidadVO) {
						
			ArrayList<DatosUnidadVO> lista = mapa.get(objVo.getFlujo());
			if (lista==null) {
				lista = new ArrayList<DatosUnidadVO>();
				mapa.put(objVo.getFlujo(), lista);
			}
			
			lista.add(objVo);
		}	
	}		
	
	public List<DatosUnidadVO> actualizarListaUnidadTOE (ArrayList arrayList)  {
		List<DatosUnidadVO> listDatosUnidadVO2=null;
		
		if (this.tablaFacadeBean == null) {
			this.tablaFacadeBean = new TablaFacadeBean();
		}	
	
		if(arrayList!=null){
			listDatosUnidadVO2=tablaFacadeBean.getGenerarValorUnidadTOE(arrayList);
		}

		return listDatosUnidadVO2;
	}	
	
	public void agregarMapaPorcentajeTipoOferta (Map<Integer, Map<String, ArrayList<DatosPorcentajeVO>>>  mapa, 
			Integer codTipoOferta, List<DatosPorcentajeVO> listDatosPorcentajeVO) {
		
		Map<String, ArrayList<DatosPorcentajeVO>> mapaPorcentajeTipoFlujo= mapa.get(codTipoOferta);
		
		if(mapaPorcentajeTipoFlujo==null){
			mapaPorcentajeTipoFlujo=new TreeMap<String, ArrayList<DatosPorcentajeVO>>();
			mapa.put(codTipoOferta, mapaPorcentajeTipoFlujo);
		}
		mapaPorcentajeTipoFlujo(mapaPorcentajeTipoFlujo, listDatosPorcentajeVO);

	}
	
	public void mapaPorcentajeTipoFlujo(Map<String, ArrayList<DatosPorcentajeVO>> mapa, List<DatosPorcentajeVO> listDatosPorcentajeVO){
		if(listDatosPorcentajeVO!=null)
		for (DatosPorcentajeVO objVo : listDatosPorcentajeVO) {
						
			ArrayList<DatosPorcentajeVO> lista = mapa.get(objVo.getValorFlujo());
			if (lista==null) {
				lista = new ArrayList<DatosPorcentajeVO>();
				mapa.put(objVo.getValorFlujo(), lista);
			}
			
			lista.add(objVo);
		}	
	}
	
	public List<DatosPorcentajeVO> actualizarListaPorcentajeTOE (ArrayList arrayList) {

		List<DatosPorcentajeVO> listDatosPorcentajeVO2=null;
		
		if (this.tablaFacadeBean == null) {
			this.tablaFacadeBean = new TablaFacadeBean();
		}	

		if(arrayList!=null){
			listDatosPorcentajeVO2=tablaFacadeBean.getGenerarDatosPorcentajeTOE(arrayList);
		}

		return listDatosPorcentajeVO2;
	} 	
	
	public Map<Integer, Map<String, ArrayList<DatosUnidadVO>>> agregarMapaUnidProductoTipoOferta (
			Map<Integer, Map<Integer, Map<String, ArrayList<DatosUnidadVO>>>>  mapa, 
			Integer codProducto) {
		
		Map<Integer, Map<String, ArrayList<DatosUnidadVO>>> mapaUnidadTipoOferta= mapa.get(codProducto);
		
		if(mapaUnidadTipoOferta==null){
			mapaUnidadTipoOferta=new TreeMap<Integer, Map<String,ArrayList<DatosUnidadVO>>>();
			mapa.put(codProducto, mapaUnidadTipoOferta);
		}
		
		return mapaUnidadTipoOferta;

	}	
	
	public Map<Integer, Map<String, ArrayList<DatosPorcentajeVO>>> agregarMapaPorcenProductoTipoOferta (
			Map<Integer, Map<Integer, Map<String, ArrayList<DatosPorcentajeVO>>>>  mapa, 
			Integer codProducto) {
		
		Map<Integer, Map<String, ArrayList<DatosPorcentajeVO>>> mapaPorcentajeTipoOferta= mapa.get(codProducto);
		
		if(mapaPorcentajeTipoOferta==null){
			mapaPorcentajeTipoOferta=new TreeMap<Integer, Map<String,ArrayList<DatosPorcentajeVO>>>();
			mapa.put(codProducto, mapaPorcentajeTipoOferta);
		}
		
		return mapaPorcentajeTipoOferta;
	}	
	
	public void agregarMapaTipoOferta (Map<Integer, Map<String, Map<String, ArrayList<DatosGeneradosVO>>>>  mapa, 
			Integer codTipoOferta, List<DatosGeneradosVO> listDatosGenerados) {
		
		Map<String, Map<String, ArrayList<DatosGeneradosVO>>> mapaTipoFlujo= mapa.get(codTipoOferta);
		
		if(mapaTipoFlujo==null){
			mapaTipoFlujo=new TreeMap<String, Map<String,ArrayList<DatosGeneradosVO>>>();
			mapa.put(codTipoOferta, mapaTipoFlujo);
		}

		mapaTipoFlujo(mapaTipoFlujo, listDatosGenerados);
	}
	
	public void mapaTipoFlujo(Map<String, Map<String, ArrayList<DatosGeneradosVO>>> mapa, List<DatosGeneradosVO> listDatosGenerados){
		
		if(listDatosGenerados!=null)
		for (DatosGeneradosVO objVo : listDatosGenerados) {
			
			Map<String, ArrayList<DatosGeneradosVO>> lista = mapa.get(objVo.getFlujo());
			if (lista==null) {
				lista = new TreeMap<String, ArrayList<DatosGeneradosVO>>();
				mapa.put(objVo.getFlujo(), lista);
			}
			mapaTipoConcepto(lista, objVo.getTipoConcepto().toUpperCase().trim(),objVo);
		}	
	}
	
	public void mapaTipoConcepto(Map<String, ArrayList<DatosGeneradosVO>> mapa, String Concepto, DatosGeneradosVO objDatosGeneradosVO){
		
		ArrayList<DatosGeneradosVO> lista = mapa.get(Concepto);
		
		if (lista==null) {
			lista = new ArrayList<DatosGeneradosVO>();
			mapa.put(Concepto, lista);
		}
		lista.add(objDatosGeneradosVO);
		
	}	
	
	/**
	 * ELIMINAR METODO TEMPORAL
	 * */
	public List<DatosGeneradosVO> actualizarListasDatosgenerados(String trama){
		List<DatosGeneradosVO> listDatosGeneradosVO=new ArrayList<DatosGeneradosVO>(); 
		
    	if (trama!=null) {
    		StringTokenizer tokenizer = new StringTokenizer(trama, ",");
    		while (tokenizer.hasMoreTokens()) {
    			DatosGeneradosVO obj = new DatosGeneradosVO();
    			obj.setValor(tokenizer.nextToken());
    			obj.setTipoConcepto(tokenizer.nextToken());
    			obj.setTc(tokenizer.nextToken());
            	obj.setTe(tokenizer.nextToken());
            	obj.setFlujo(tokenizer.nextToken());    			
            	listDatosGeneradosVO.add(obj);
    		}
    	}
    	
    	return listDatosGeneradosVO;
	}
	
	public Map<Integer, Map<String, Map<String, ArrayList<DatosGeneradosVO>>>> agregarMapaProductoTipoOferta (Map<Integer, Map<Integer, Map<String, Map<String, ArrayList<DatosGeneradosVO>>>>>  mapa, 
			Integer codProducto) {
		
		Map<Integer, Map<String, Map<String, ArrayList<DatosGeneradosVO>>>> mapaTipoOferta= mapa.get(codProducto);
		
		if(mapaTipoOferta==null){
			mapaTipoOferta=new TreeMap<Integer, Map<String,Map<String,ArrayList<DatosGeneradosVO>>>>();
			mapa.put(codProducto, mapaTipoOferta);
		}

		return mapaTipoOferta;

	}	
	
	public ArrayList<String> cargarListaTiempo_tc_Te(List<PosibleValor> listPosibleValor) {
		//ArrayList<String> listaTiempo_tc_te=null;
    	int i=1;
    	
    	if(listPosibleValor!=null){
    		listaTiempo_tc_te=new ArrayList<String>();
    		
	    		for(PosibleValor objPosibleValor : listPosibleValor){
	    			if(i==1)
	    				listaTiempo_tc_te.add("TE");
	    			else{
	    				listaTiempo_tc_te.add("TC");
	    				listaTiempo_tc_te.add("TE");
	    			}
	    				
	    			i++;
	    			
	    		}
    	}
    	
    	return listaTiempo_tc_te;
    }	
	
	public ArrayList<String> cargarListaTiempo_tc_Te_Especificos() {
		//ArrayList<String> listaTiempo_tc_te=null;
    	int i=1;

    		listaTiempo_tc_te=new ArrayList<String>();

	    				listaTiempo_tc_te.add("TC");
	    				listaTiempo_tc_te.add("TE");

    	return listaTiempo_tc_te;
    }

	
	/***********************************************************************************************************************
	 ************************************************************************************************************************
	 ***********************************************************************************************************************
	 * AYUDA GENERACION EXCEL
	 * REPORTE TOE
	 * ABRIL 2014
	 ***********************************************************************************************************************
	 ************************************************************************************************************************
	 ************************************************************************************************************************
	 * **/
	
	public void pintarTitulos(Sheet sheet, int firsRow, int lastRow, int firstCol, int lastCol){
        sheet.addMergedRegion(new CellRangeAddress(lastRow, lastRow, firstCol, lastCol));
	}
	
	public void crearContenido_Tiempo(HSSFWorkbook wb, Sheet sheet, int firstCol,  
			 int encab, int formato, List<ListaReporteToe> listReporteToe, int numFilaTitulo1){

		LOG.info("listReporteToe = "+listReporteToe.size());

		int cont=0;
		
		if(listReporteToe!=null && listReporteToe.size()>0)
		for(int i=0; i<listReporteToe.size(); i++){
			
			Row titTiempo = crearFila(sheet, wb, numFilaTitulo1+6+ cont);
			//	for(int j=0; j<=17; j++){
				//	crearContenido_Tiempo(wb, titTiempo, firstCol+j,    encab, formato, listReporteToe.get(i).getColumn0());
				//
				if(i <= 2)
					crearContenido_Tiempo(wb, titTiempo, firstCol,    encab, 200, listReporteToe.get(i).getColumn0());
				else
					crearContenido_Tiempo(wb, titTiempo, firstCol,    encab, formato, listReporteToe.get(i).getColumn0());
				
				crearContenido_Tiempo(wb, titTiempo, firstCol+1,  encab, formato, listReporteToe.get(i).getColumn1());
				crearContenido_Tiempo(wb, titTiempo, firstCol+2,  encab, formato, listReporteToe.get(i).getColumn2());
				crearContenido_Tiempo(wb, titTiempo, firstCol+3,  encab, formato, listReporteToe.get(i).getColumn3());
				crearContenido_Tiempo(wb, titTiempo, firstCol+4,  encab, formato, listReporteToe.get(i).getColumn4());
				crearContenido_Tiempo(wb, titTiempo, firstCol+5,  encab, formato, listReporteToe.get(i).getColumn5());
				crearContenido_Tiempo(wb, titTiempo, firstCol+6,  encab, formato, listReporteToe.get(i).getColumn6());
				crearContenido_Tiempo(wb, titTiempo, firstCol+7,  encab, formato, listReporteToe.get(i).getColumn7());
				crearContenido_Tiempo(wb, titTiempo, firstCol+8,  encab, formato, listReporteToe.get(i).getColumn8());
				crearContenido_Tiempo(wb, titTiempo, firstCol+9,  encab, formato, listReporteToe.get(i).getColumn9());
				crearContenido_Tiempo(wb, titTiempo, firstCol+10,  encab, formato, listReporteToe.get(i).getColumn10());
				crearContenido_Tiempo(wb, titTiempo, firstCol+11,  encab, formato, listReporteToe.get(i).getColumn11());
				crearContenido_Tiempo(wb, titTiempo, firstCol+12,  encab, formato, listReporteToe.get(i).getColumn12());
				crearContenido_Tiempo(wb, titTiempo, firstCol+13,  encab, formato, listReporteToe.get(i).getColumn13());
				crearContenido_Tiempo(wb, titTiempo, firstCol+14,  encab, formato, listReporteToe.get(i).getColumn14());
				crearContenido_Tiempo(wb, titTiempo, firstCol+15,  encab, formato, listReporteToe.get(i).getColumn15());
				crearContenido_Tiempo(wb, titTiempo, firstCol+16,  encab, formato, listReporteToe.get(i).getColumn16());
				crearContenido_Tiempo(wb, titTiempo, firstCol+17,  encab, formato, listReporteToe.get(i).getColumn17());				
				
				cont++;
		}
	
	}
	
	public void crearContenido_TiempoEspecifico(HSSFWorkbook wb, Sheet sheet, int firstCol,  
			 int encab, int formato, List<ListaReporteToe> listReporteToe, int numFilaTitulo1){

		LOG.info("listReporteToe = "+listReporteToe.size());

		int cont=0;
		
		if(listReporteToe!=null && listReporteToe.size()>0)
		for(int i=0; i<listReporteToe.size(); i++){
			
			Row titTiempo = crearFila(sheet, wb, numFilaTitulo1+6+ cont);

				//if(i <= 2)
					//crearContenido_Tiempo(wb, titTiempo, firstCol,    encab, 200, listReporteToe.get(i).getColumn0());
				//else
				//	crearContenido_Tiempo(wb, titTiempo, firstCol,    encab, formato, listReporteToe.get(i).getColumn0());
				
				crearContenido_Tiempo(wb, titTiempo, firstCol,  encab, formato, listReporteToe.get(i).getColumn0());
				crearContenido_Tiempo(wb, titTiempo, firstCol+1,  encab, formato, listReporteToe.get(i).getColumn1());
				crearContenido_Tiempo(wb, titTiempo, firstCol+2,  encab, formato, listReporteToe.get(i).getColumn2());
				/*crearContenido_Tiempo(wb, titTiempo, firstCol+3,  encab, formato, listReporteToe.get(i).getColumn3());
				crearContenido_Tiempo(wb, titTiempo, firstCol+4,  encab, formato, listReporteToe.get(i).getColumn4());
				crearContenido_Tiempo(wb, titTiempo, firstCol+5,  encab, formato, listReporteToe.get(i).getColumn5());
				crearContenido_Tiempo(wb, titTiempo, firstCol+6,  encab, formato, listReporteToe.get(i).getColumn6());
				crearContenido_Tiempo(wb, titTiempo, firstCol+7,  encab, formato, listReporteToe.get(i).getColumn7());
				crearContenido_Tiempo(wb, titTiempo, firstCol+8,  encab, formato, listReporteToe.get(i).getColumn8());
				crearContenido_Tiempo(wb, titTiempo, firstCol+9,  encab, formato, listReporteToe.get(i).getColumn9());
				crearContenido_Tiempo(wb, titTiempo, firstCol+10,  encab, formato, listReporteToe.get(i).getColumn10());
				crearContenido_Tiempo(wb, titTiempo, firstCol+11,  encab, formato, listReporteToe.get(i).getColumn11());
				crearContenido_Tiempo(wb, titTiempo, firstCol+12,  encab, formato, listReporteToe.get(i).getColumn12());
				crearContenido_Tiempo(wb, titTiempo, firstCol+13,  encab, formato, listReporteToe.get(i).getColumn13());
				crearContenido_Tiempo(wb, titTiempo, firstCol+14,  encab, formato, listReporteToe.get(i).getColumn14());
				crearContenido_Tiempo(wb, titTiempo, firstCol+15,  encab, formato, listReporteToe.get(i).getColumn15());
				crearContenido_Tiempo(wb, titTiempo, firstCol+16,  encab, formato, listReporteToe.get(i).getColumn16());
				crearContenido_Tiempo(wb, titTiempo, firstCol+17,  encab, formato, listReporteToe.get(i).getColumn17());*/				
				
				cont++;
		}
	
	}	
	
	/**
	 * tipoCabecera
	 * 0=Porcentaje
	 * */
	public void crearPorcentajeTOE(HSSFWorkbook wb, Sheet sheet, Row trow, int firstCol, int lastCol, 
			int firstRow, int encab, int formato, int tipoCabecera, List<ListaReportePorcentajeToe> lista){
		
		if(lista!=null && lista.size()>0)
		for(int i=0; i<lista.size(); i++){

			
			for(int j=firstCol; j<=lastCol; j++){
				//crearCeldaTitulo(wb, trow, j, CellStyle.ALIGN_CENTER,
			   	//        CellStyle.VERTICAL_CENTER, lista.get(i).getColumn0(), true, true, encab, formato);			
				switch (j) {
				case 1:
					crearContenido_Tiempo(wb, trow, j,    encab, 200, lista.get(i).getColumn0());
					break;
				case 2:
					crearContenido_Tiempo(wb, trow, j,    encab, formato, lista.get(i).getColumn1());
					break;
				case 3:
					crearContenido_Tiempo(wb, trow, j,    encab, formato, lista.get(i).getColumn2());
					crearContenido_Tiempo(wb, trow, j+1,    encab, formato, "");
					pintarTitulos(sheet, firstRow, firstRow, j, j+1);
					break;
				case 5:
					crearContenido_Tiempo(wb, trow, j,    encab, formato, lista.get(i).getColumn3());
					crearContenido_Tiempo(wb, trow, j+1,    encab, formato, "");
					pintarTitulos(sheet, firstRow, firstRow, j, j+1);
					break;
				case 7:
					crearContenido_Tiempo(wb, trow, j,    encab, formato, lista.get(i).getColumn4());
					crearContenido_Tiempo(wb, trow, j+1,    encab, formato, "");
					pintarTitulos(sheet, firstRow, firstRow, j, j+1);
					break;
				case 9:
					crearContenido_Tiempo(wb, trow, j,    encab, formato, lista.get(i).getColumn5());
					crearContenido_Tiempo(wb, trow, j+1,    encab, formato, "");
					pintarTitulos(sheet, firstRow, firstRow, j, j+1);
					break;
				case 11:
					crearContenido_Tiempo(wb, trow, j,    encab, formato, lista.get(i).getColumn6());
					crearContenido_Tiempo(wb, trow, j+1,    encab, formato, "");
					pintarTitulos(sheet, firstRow, firstRow, j, j+1);
					break;
				case 13:
					crearContenido_Tiempo(wb, trow, j,    encab, formato, lista.get(i).getColumn7());
					crearContenido_Tiempo(wb, trow, j+1,    encab, formato, "");
					pintarTitulos(sheet, firstRow, firstRow, j, j+1);
					break;
				case 15:
					crearContenido_Tiempo(wb, trow, j,    encab, formato, lista.get(i).getColumn8());
					crearContenido_Tiempo(wb, trow, j+1,    encab, formato, "");
					pintarTitulos(sheet, firstRow, firstRow, j, j+1);
					break;
				case 17:
					crearContenido_Tiempo(wb, trow, j,    encab, formato, lista.get(i).getColumn9());
					crearContenido_Tiempo(wb, trow, j+1,    encab, formato, "");
					pintarTitulos(sheet, firstRow, firstRow, j, j+1);
					break;
				}			
			}			
		}
	}	
	
	public void crearPorcentajeTOE_especifico(HSSFWorkbook wb, Sheet sheet, Row trow, int firstCol, int lastCol, 
			int firstRow, int encab, int formato, int tipoCabecera, List<ListaReportePorcentajeToe> lista){
		
		if(lista!=null && lista.size()>0)
		for(int i=0; i<lista.size(); i++){

			
			for(int j=firstCol; j<=lastCol; j++){
				//crearCeldaTitulo(wb, trow, j, CellStyle.ALIGN_CENTER,
			   	//        CellStyle.VERTICAL_CENTER, lista.get(i).getColumn0(), true, true, encab, formato);			
				switch (j) {
				case 1:
					crearContenido_Tiempo(wb, trow, j,    encab, 200, lista.get(i).getColumn0());
					break;
				case 2:
					crearContenido_Tiempo(wb, trow, j,    encab, formato, lista.get(i).getColumn1());
					break;
				case 3:
					crearContenido_Tiempo(wb, trow, j,    encab, formato, lista.get(i).getColumn2());
					break;
				}			
			}			
		}
	}	
	
	public void crearContenido_Tiempo(HSSFWorkbook wb, Row trow, int firstCol, 
			 int encab, int formato, String valor ){
		
		crearCeldaTitulo(wb, trow, firstCol, CellStyle.ALIGN_CENTER,
			   	CellStyle.VERTICAL_CENTER, valor, true, true, encab, formato);						
		
	}	
	
	public void createTituloCell(HSSFWorkbook wb, Row row, int column, 
			short halign, short valign,String strContenido) {

	    CreationHelper ch = wb.getCreationHelper();
	        Cell cell = row.createCell(column);
	        cell.setCellValue(ch.createRichTextString(strContenido));
	       
	        HSSFFont cellFont = wb.createFont();
	        cellFont.setFontHeightInPoints((short)9);
	        cellFont.setFontName(HSSFFont.FONT_ARIAL);
	        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	       
	        CellStyle cellStyle = wb.createCellStyle();
	        cellStyle.setAlignment(halign);
	        cellStyle.setVerticalAlignment(valign);
	        cellStyle.setFont(cellFont);
	        cell.setCellStyle(cellStyle);
	    }
	
	/**
	 * tipoCabecera
	 * 0=Perfil
	 * 1=Rol
	 * 2=Tiempo TC Y TE
	 * */
	public void crearTituloCabecera_Rol_Perfil_Tiempo(HSSFWorkbook wb, Sheet sheet, Row trow, int firstCol, int lastCol, 
			int firstRow, int encab, int formato, int tipoCabecera, ArrayList<String> lista){
		
		int cont=1;
		
		if(lista!=null && lista.size()>0)
		for(int i=0; i<lista.size(); i++){

			pintarTitulos(sheet, firstRow, firstRow, firstCol, lastCol);
			
			for(int j=firstCol; j<=lastCol; j++){
				crearCeldaTitulo(wb, trow, j, CellStyle.ALIGN_CENTER,
			   	        CellStyle.VERTICAL_CENTER, lista.get(i).toString(), true, true, encab, formato);						
			}	
			
			firstCol=lastCol+1;
			if(tipoCabecera==0){
				lastCol=firstCol+1;
			}else
				if(tipoCabecera==1){
					if(cont<3){
			            lastCol=firstCol+3;
					}else{
						if(cont==3)
							lastCol=firstCol+5;
						else
							lastCol=firstCol+1;
					}
				}else
					if(tipoCabecera==2){
						lastCol=firstCol;
					}

			cont++;
		}
	
	}	
	
	public Row crearFila(Sheet sheet, HSSFWorkbook wb, int numFila){
		Row tmpRow =sheet.createRow((short) numFila);
		return tmpRow;		
	}
	
	public Row crearFila(SXSSFSheet sheet, SXSSFWorkbook wb, int numFila){
		Row tmpRow =sheet.createRow(numFila);
		return tmpRow;		
	}
	
	public void crearCeldaTitulo(HSSFWorkbook wb, Row row, int column, short halign, short valign, 
			String strContenido, boolean booBorde,boolean booCabecera, int encab, int Formato) {
		
			CreationHelper ch = wb.getCreationHelper();
	        Cell cell = row.createCell(column);
	        if(strContenido==null)
	        	strContenido=Constantes.VALOR_VACIO;
	        
	        if(Formato==100){
	        	if(!strContenido.equals(Constantes.VALOR_VACIO))
	        		cell.setCellValue(ch.createRichTextString(strContenido+" %"));
	        	else
	        		cell.setCellValue(ch.createRichTextString(strContenido));
	        }else
	        	cell.setCellValue(ch.createRichTextString(strContenido));
	        
	        CellStyle cellStyle = wb.createCellStyle();
	        cellStyle.setAlignment(halign);
	        cellStyle.setVerticalAlignment(valign);
	        cellStyle.setWrapText(true);
	        /*
		    cell1.setCellValue("This is Row 1 \n column 1.");
		    CellStyle style = wb.createCellStyle();
		    style.setWrapText(true);
		    cell1.setCellStyle(style);*/
		    
	        if(booBorde){
	        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);
	        cellStyle.setBottomBorderColor((short)8);
	        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_DOTTED);
	        cellStyle.setLeftBorderColor((short)8);
	        cellStyle.setBorderRight(HSSFCellStyle.BORDER_DOTTED);
	        cellStyle.setRightBorderColor((short)8);
	        cellStyle.setBorderTop(HSSFCellStyle.BORDER_DOTTED);
	        cellStyle.setTopBorderColor((short)8);
	        }
	        if(booCabecera){
	        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	        cellStyle.setBottomBorderColor((short)8);
	        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
	        cellStyle.setLeftBorderColor((short)8);
	        cellStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
	        cellStyle.setRightBorderColor((short)8);
	        cellStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
	        cellStyle.setTopBorderColor((short)8);
	        }	       
	        
	        if(Formato==200){
	        	
	        	HSSFPalette palette = wb.getCustomPalette();
	        	palette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 34, (byte) 102, (byte) 187);
	        	
		        cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		        HSSFFont cellFont = wb.createFont();
		        cellFont.setFontHeightInPoints((short)10);
		        cellFont.setFontName(HSSFFont.FONT_ARIAL);
		        cellFont.setColor(HSSFColor.WHITE.index);
		        cellStyle.setFont(cellFont);
	        }
	        cell.setCellStyle(cellStyle);

	}
	
	public void crearCeldaTitulo(SXSSFWorkbook wb, Row row, int column, short halign, short valign, 
			String strContenido, boolean booBorde,boolean booCabecera, int encab, int Formato) {
		
			CreationHelper ch = wb.getCreationHelper();
	        Cell cell = row.createCell(column);
	        if(strContenido==null)
	        	strContenido=Constantes.VALOR_VACIO;
	        
	        if(Formato==100){
	        	if(!strContenido.equals(Constantes.VALOR_VACIO))
	        		cell.setCellValue(ch.createRichTextString(strContenido+" %"));
	        	else
	        		cell.setCellValue(ch.createRichTextString(strContenido));
	        }else
	        	cell.setCellValue(ch.createRichTextString(strContenido));
	        
	        XSSFCellStyle cellStyle = (XSSFCellStyle)wb.createCellStyle();
	        cellStyle.setAlignment(halign);
	        //cellStyle.setAlignment((short)0);
	        cellStyle.setVerticalAlignment(valign);
	        //cellStyle.setVerticalAlignment((short) 3);
	        cellStyle.setWrapText(true);
	       		    
	        if(booBorde){
	        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);
	        cellStyle.setBottomBorderColor((short)8);
	        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_DOTTED);
	        cellStyle.setLeftBorderColor((short)8);
	        cellStyle.setBorderRight(HSSFCellStyle.BORDER_DOTTED);
	        cellStyle.setRightBorderColor((short)8);
	        cellStyle.setBorderTop(HSSFCellStyle.BORDER_DOTTED);
	        cellStyle.setTopBorderColor((short)8);
	        }
	        if(booCabecera){
	        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	        cellStyle.setBottomBorderColor((short)8);
	        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
	        cellStyle.setLeftBorderColor((short)8);
	        cellStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
	        cellStyle.setRightBorderColor((short)8);
	        cellStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
	        cellStyle.setTopBorderColor((short)8);
	        }	       
	        
	        if(Formato==200){
	        	
	        	//HSSFPalette palette = wb.getCustomPalette();
	        	//palette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 34, (byte) 102, (byte) 187);
	        	
		        //cellStyle.setWrapText(true);
	        	cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		        Font cellFont = wb.createFont();
		        cellFont.setFontHeightInPoints((short)10);
		        cellFont.setFontName(HSSFFont.FONT_ARIAL);
		        cellFont.setColor(HSSFColor.WHITE.index);
		        cellStyle.setFont(cellFont);
	        }
	        cell.setCellStyle(cellStyle);

	}
	
	public void crearCeldaData(HSSFFont cellFont, HSSFWorkbook wb, Row row, int column, 
			short halign, short valign,String strContenido) {

	    CreationHelper ch = wb.getCreationHelper();
	        Cell cell = row.createCell(column);
	        if(strContenido==null){
	        	strContenido=Constantes.VALOR_VACIO;
	        }
	        cell.setCellValue(ch.createRichTextString(strContenido));
	       	        
	        //HSSFFont cellFont = wb.createFont();
	        cellFont.setFontHeightInPoints((short)9);
	        cellFont.setFontName(HSSFFont.FONT_ARIAL);
	        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	        /*
	        CellStyle cellStyle = wb.createCellStyle();
	        cellStyle.setAlignment(halign);
	        cellStyle.setVerticalAlignment(valign);
	        cellStyle.setFont(cellFont);
	        cell.setCellStyle(cellStyle);
	        */
	    }
	
	public void crearCeldaData(Font cellFont, SXSSFWorkbook wb, Row row, int column, 
			short halign, short valign,String strContenido) {

	    CreationHelper ch = wb.getCreationHelper();
	        Cell cell = row.createCell(column);
	        if(strContenido==null){
	        	strContenido=Constantes.VALOR_VACIO;
	        }
	        cell.setCellValue(ch.createRichTextString(strContenido));
	       
	        cellFont.setFontHeightInPoints((short)9);
	        cellFont.setFontName(HSSFFont.FONT_ARIAL);
	        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	       
	    }
	
	/**
	 * FIN DE AYUDA GENERACION EXCEL
	 * */
	public String getMsjSinResultado() {
		return msjSinResultado;
	}

	public void setMsjSinResultado(String msjSinResultado) {
		this.msjSinResultado = msjSinResultado;
	}

	public boolean isTabla1() {
		return tabla1;
	}

	public void setTabla1(boolean tabla1) {
		this.tabla1 = tabla1;
	}

	public boolean isTabla2() {
		return tabla2;
	}

	public void setTabla2(boolean tabla2) {
		this.tabla2 = tabla2;
	}

	public boolean isTabla3() {
		return tabla3;
	}

	public void setTabla3(boolean tabla3) {
		this.tabla3 = tabla3;
	}

	public boolean isTabla4() {
		return tabla4;
	}

	public void setTabla4(boolean tabla4) {
		this.tabla4 = tabla4;
	}

	public boolean isTabla5() {
		return tabla5;
	}

	public void setTabla5(boolean tabla5) {
		this.tabla5 = tabla5;
	}

	public boolean isTabla6() {
		return tabla6;
	}

	public void setTabla6(boolean tabla6) {
		this.tabla6 = tabla6;
	}

	public boolean isTabla7() {
		return tabla7;
	}

	public void setTabla7(boolean tabla7) {
		this.tabla7 = tabla7;
	}

	public boolean isTabla8() {
		return tabla8;
	}

	public void setTabla8(boolean tabla8) {
		this.tabla8 = tabla8;
	}

	public String getTituloTabla1() {
		return tituloTabla1;
	}

	public void setTituloTabla1(String tituloTabla1) {
		this.tituloTabla1 = tituloTabla1;
	}

	public String getTituloTabla2() {
		return tituloTabla2;
	}

	public void setTituloTabla2(String tituloTabla2) {
		this.tituloTabla2 = tituloTabla2;
	}

	public String getTituloTabla3() {
		return tituloTabla3;
	}

	public void setTituloTabla3(String tituloTabla3) {
		this.tituloTabla3 = tituloTabla3;
	}

	public String getTituloTabla4() {
		return tituloTabla4;
	}

	public void setTituloTabla4(String tituloTabla4) {
		this.tituloTabla4 = tituloTabla4;
	}

	public String getTituloTabla5() {
		return tituloTabla5;
	}

	public void setTituloTabla5(String tituloTabla5) {
		this.tituloTabla5 = tituloTabla5;
	}

	public String getTituloTabla6() {
		return tituloTabla6;
	}

	public void setTituloTabla6(String tituloTabla6) {
		this.tituloTabla6 = tituloTabla6;
	}

	public String getTituloTabla7() {
		return tituloTabla7;
	}

	public void setTituloTabla7(String tituloTabla7) {
		this.tituloTabla7 = tituloTabla7;
	}

	public String getTituloTabla8() {
		return tituloTabla8;
	}

	public void setTituloTabla8(String tituloTabla8) {
		this.tituloTabla8 = tituloTabla8;
	}
	public boolean isViewTablaGeneral() {
		return viewTablaGeneral;
	}
	public void setViewTablaGeneral(boolean viewTablaGeneral) {
		this.viewTablaGeneral = viewTablaGeneral;
	}
	public boolean isViewProductoTC() {
		return viewProductoTC;
	}
	public void setViewProductoTC(boolean viewProductoTC) {
		this.viewProductoTC = viewProductoTC;
	}
	public boolean isViewProductoPLD() {
		return viewProductoPLD;
	}
	public void setViewProductoPLD(boolean viewProductoPLD) {
		this.viewProductoPLD = viewProductoPLD;
	}
	public boolean isViewTipoOfertaAprobado() {
		return viewTipoOfertaAprobado;
	}
	public void setViewTipoOfertaAprobado(boolean viewTipoOfertaAprobado) {
		this.viewTipoOfertaAprobado = viewTipoOfertaAprobado;
	}
	public boolean isViewTipoOfertaRegular() {
		return viewTipoOfertaRegular;
	}
	public void setViewTipoOfertaRegular(boolean viewTipoOfertaRegular) {
		this.viewTipoOfertaRegular = viewTipoOfertaRegular;
	}

	public ArrayList<String> getGenerar_listaRoles() {
		return generar_listaRoles;
	}

	public void setGenerar_listaRoles(ArrayList<String> generar_listaRoles) {
		this.generar_listaRoles = generar_listaRoles;
	}

	public ArrayList<String> getGenerar_listaPerfiles() {
		return generar_listaPerfiles;
	}

	public void setGenerar_listaPerfiles(ArrayList<String> generar_listaPerfiles) {
		this.generar_listaPerfiles = generar_listaPerfiles;
	}

	public List<List<ListaReporteToe>> getListaReporte_Prod_TipoOferta_Flujo() {
		if(listaReporte_Prod_TipoOferta_Flujo!=null)
			LOG.info("listaReporte_Prod_TipoOferta_Flujo::: tamaño:::"+listaReporte_Prod_TipoOferta_Flujo.size());
		else
			LOG.info("listaReporte_Prod_TipoOferta_Flujo null");
		return listaReporte_Prod_TipoOferta_Flujo;
	}

	public void setListaReporte_Prod_TipoOferta_Flujo(
			List<List<ListaReporteToe>> listaReporte_Prod_TipoOferta_Flujo) {
		this.listaReporte_Prod_TipoOferta_Flujo = listaReporte_Prod_TipoOferta_Flujo;
	}

	public ArrayList<String> getListaTiempo_tc_te() {
		return listaTiempo_tc_te;
	}

	public void setListaTiempo_tc_te(ArrayList<String> listaTiempo_tc_te) {
		this.listaTiempo_tc_te = listaTiempo_tc_te;
	}

	public List<List<ListaReportePorcentajeToe>> getListPorc_Prod_TipOferta_Flujo() {
		return listPorc_Prod_TipOferta_Flujo;
	}

	public void setListPorc_Prod_TipOferta_Flujo(
			List<List<ListaReportePorcentajeToe>> listPorc_Prod_TipOferta_Flujo) {
		this.listPorc_Prod_TipOferta_Flujo = listPorc_Prod_TipOferta_Flujo;
	}

	public List<List<ListaReporteUnidadToe>> getListUnid_Prod_TipOferta_Flujo() {
		return listUnid_Prod_TipOferta_Flujo;
	}

	public void setListUnid_Prod_TipOferta_Flujo(
			List<List<ListaReporteUnidadToe>> listUnid_Prod_TipOferta_Flujo) {
		this.listUnid_Prod_TipOferta_Flujo = listUnid_Prod_TipOferta_Flujo;
	}

	public String getProductoEspecifico() {
		return ProductoEspecifico;
	}

	public void setProductoEspecifico(String productoEspecifico) {
		ProductoEspecifico = productoEspecifico;
	}

	public String getTipoOfertaEspecifico() {
		return TipoOfertaEspecifico;
	}

	public void setTipoOfertaEspecifico(String tipoOfertaEspecifico) {
		TipoOfertaEspecifico = tipoOfertaEspecifico;
	}	

}
