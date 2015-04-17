package com.ibm.bbva.controller.common;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.EstadoCE;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.PosibleValor;
import com.ibm.bbva.entities.Producto;
import com.ibm.bbva.entities.TipoOferta;
import com.ibm.bbva.session.EstadoCEBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.PosibleValorBeanLocal;
import com.ibm.bbva.session.ProductoBeanLocal;
import com.ibm.bbva.session.TipoOfertaBeanLocal;
import com.ibm.bbva.tabla.ejb.impl.TablaFacadeBean;
import com.ibm.bbva.tabla.reporte.vo.DatosGeneradosVO;
import com.ibm.bbva.tabla.reporte.vo.DatosPorcentajeVO;
import com.ibm.bbva.tabla.reporte.vo.DatosUnidadVO;
import com.ibm.bbva.tabla.util.vo.ListaReportePorcentajeToe;
import com.ibm.bbva.tabla.util.vo.ListaReporteToe;
import com.ibm.bbva.tabla.util.vo.ListaReporteToePerfil;
import com.ibm.bbva.tabla.util.vo.ListaReporteToeRol;
import com.ibm.bbva.tabla.util.vo.ListaReporteUnidadToe;
import java.text.DecimalFormat;

/*
 * 100 = CON PORCENTAJE SIN COLOR
200 = SIN PORCENTAJE CON COLOR
300 = SIN PORCENTAJE SIN COLOR

*/
@SuppressWarnings("serial")
@ManagedBean(name = "tablaGenerarTOE")
@RequestScoped
public class TablaGenerarTOEMB {

	@EJB
	private PosibleValorBeanLocal posibleValorBean;
	@EJB
	private ProductoBeanLocal productoBean;	
	@EJB
	private TipoOfertaBeanLocal tipoOfertaobean;	
	@EJB
	private PerfilBeanLocal perfilBean;
	@EJB
	private EstadoCEBeanLocal estadoCEBean;	
	
	private List<PosibleValor> listPosibleValor;
	private List<DatosGeneradosVO> listDatosGeneradosVO;
	private List<DatosGeneradosVO> listDatosGeneradosVO_reproceso;
	private List<DatosPorcentajeVO> listDatosPorcentajeVO;
	private List<DatosUnidadVO> listDatosUnidadVO;
	
	private TablaFacadeBean tablaFacadeBean = null;
	//valores generales
	private static String valorColum="";
	private static String valorColumPerfil="";
	private static String valorColumPerfilLista="";
	private static String valorColumObjPerfil="";
	
	//valores especificos
	private static String valorColumEsp="";
	private static String valorColumPerfilEsp="";
	private static String valorColumObjPerfilEsp="";
	
	
	private int contTiempo=0;
	private int linea=0; 
	private String rangoFechas;
	private String rangoFechasFormato;
	
	private String PerfilEspecifico;
	private String EstadoEspecifico;
	private String UnidadEspecifico;
	private String TipoOfertaEspecifico;
	private String ProductoEspecifico;
	
	/**
	 * Vista de Tablas segun Filtros
	 * */
	private boolean viewTablaEspecifica;
	private boolean viewTablaGeneral;
		
	private boolean viewProductoTC;
	private boolean viewProductoPLD;
	private boolean viewTipoOfertaAprobado;
	private boolean viewTipoOfertaRegular;
	private boolean viewFlujoRegular;
	private boolean viewFlujoReproceso;
	
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
	private String tituloTabla9;
	private String tituloTabla10;
	
	
	private static String valorColumPorcentaje="";

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
	
	ListaReportePorcentajeToe objListaReportePorcentajeToe;
	private List<ListaReportePorcentajeToe> listPorc_Prod_1_TipOferta_1_Flujo_1;
	private List<ListaReportePorcentajeToe> listPorc_Prod_1_TipOferta_1_Flujo_2;
	private List<ListaReportePorcentajeToe> listPorc_Prod_1_TipOferta_2_Flujo_1;
	private List<ListaReportePorcentajeToe> listPorc_Prod_1_TipOferta_2_Flujo_2;
	private List<ListaReportePorcentajeToe> listPorc_Prod_2_TipOferta_1_Flujo_1;
	private List<ListaReportePorcentajeToe> listPorc_Prod_2_TipOferta_1_Flujo_2;
	private List<ListaReportePorcentajeToe> listPorc_Prod_2_TipOferta_2_Flujo_1;
	private List<ListaReportePorcentajeToe> listPorc_Prod_2_TipOferta_2_Flujo_2;
	
	ListaReporteUnidadToe objListaReporteUnidadToe;
	private List<ListaReporteUnidadToe> listUnid_Prod_1_TipOferta_1_Flujo_1;
	private List<ListaReporteUnidadToe> listUnid_Prod_1_TipOferta_1_Flujo_2;
	private List<ListaReporteUnidadToe> listUnid_Prod_1_TipOferta_2_Flujo_1;
	private List<ListaReporteUnidadToe> listUnid_Prod_1_TipOferta_2_Flujo_2;
	private List<ListaReporteUnidadToe> listUnid_Prod_2_TipOferta_1_Flujo_1;
	private List<ListaReporteUnidadToe> listUnid_Prod_2_TipOferta_1_Flujo_2;
	private List<ListaReporteUnidadToe> listUnid_Prod_2_TipOferta_2_Flujo_1;
	private List<ListaReporteUnidadToe> listUnid_Prod_2_TipOferta_2_Flujo_2;
	
	ListaReporteToeRol objlistaRoles;
	ListaReporteToePerfil objlistaPerfiles;
	private  ArrayList<ListaReporteToeRol> listaRoles;
	private  ArrayList<ListaReporteToePerfil> listaPerfiles;
	
	private String msjSinResultado;
	private String tituloEspecifico;
	
	private static final Logger LOG = LoggerFactory.getLogger(TablaGenerarTOEMB.class);
	
	public TablaGenerarTOEMB() {
	}
	
	@PostConstruct
    public void init() {
		
	}	
	
	public String obtPorcentaje(){
		
		if(listDatosPorcentajeVO!=null && listDatosPorcentajeVO.size()>0){
			for(int i=0;i<listDatosPorcentajeVO.size()-1;i++)
			{
				if(!valorColumPorcentaje.equals(Constantes.VALOR_VACIO)){
					if(TablaGenerarTOEMB.valorColumPorcentaje.equals(listDatosPorcentajeVO.get(i).getValorRol())){
						TablaGenerarTOEMB.valorColumPorcentaje=listDatosPorcentajeVO.get(i+1).getValorRol();
							return listDatosPorcentajeVO.get(i+1).getValorPorcentaje();
					}
				}else{
					TablaGenerarTOEMB.valorColumPorcentaje=listDatosPorcentajeVO.get(i).getValorRol();
					return listDatosPorcentajeVO.get(i).getValorPorcentaje();
				}			
			}
		}
		
		return null;
	}	
	
	public boolean validarReporteEspecificoTOE(ArrayList arrayList){
		
		String sTipoReport=arrayList.get(8).toString(); // TIPO REPORTE		

		if(sTipoReport!=null && sTipoReport.trim().length()>0){
			Integer iTipoReport=Integer.parseInt(sTipoReport.trim());
			System.out.println("tipoReport = "+iTipoReport);
								
			if(iTipoReport==Integer.parseInt(Constantes.ID_TOE_ESPECIFICO)){
			//REPORTE ESPECIFICO
				
				listDatosGeneradosVO=this.actualizarListaTiemposTOE(arrayList, "0");
				
				if(listDatosGeneradosVO!=null && listDatosGeneradosVO.size()>0){
					return true;//SI HAY DATA DE DEVOLUCION
				}else{
					this.setMsjSinResultado("No se encontró resultado con los filtros seleccionados");
					return false;//NO HAY DATA DE DEVOLUCION
				}				
			}

		
		}
		
		return false;
	}
	
	public String buscarTOE(ArrayList arrayList){
		
		String sTipoReport=arrayList.get(8).toString(); // TIPO REPORTE
		String sTipoProduct=arrayList.get(2).toString();	// PRODUCTO
		String sTipoOferta=arrayList.get(4).toString();	// TIPO OFERTA

		if(sTipoReport!=null && sTipoReport.trim().length()>0){
			Integer iTipoReport=Integer.parseInt(sTipoReport.trim());
			System.out.println("tipoReport = "+iTipoReport);
					
			if(iTipoReport==Integer.parseInt(Constantes.ID_TOE_GENERAL)){
				
				if(sTipoProduct!=null && sTipoProduct.trim().length()>0){
					
					List<Producto> listProducto=new ArrayList<Producto>();
					String tmpSTipoProduct=sTipoProduct;
					System.out.println("tmpSTipoProduct = "+tmpSTipoProduct);
					
					if (sTipoProduct.trim().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){// TODOS LOS PRODUCTOS		
						listProducto=productoBean.buscarTodos();
						this.viewProductoPLD=true;
						this.viewProductoTC=true;
					}else{
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
										
										
								listDatosPorcentajeVO=this.actualizarListaPorcentajeTOE(arrayList);

										
										agregarMapaPorcentajeTipoOferta(mapaPorcentajeTipoOfertaRol, Integer.parseInt(arrayList.get(4).toString()), listDatosPorcentajeVO);
										
										listDatosUnidadVO=this.actualizarListaUnidadTOE(arrayList);

										
										agregarMapaUnidadTipoOferta(mapaUnidadTipoOfertaRol, Integer.parseInt(arrayList.get(4).toString()), listDatosUnidadVO);

									}
									arrayList.set(4, tmpSTipoOferta);								
							}							
						}
						arrayList.set(2, tmpSTipoProduct);										
				}
				
				listPosibleValor = posibleValorBean.buscarPorIdColumna(Constantes.ID_POSIBLE_VALOR_TOE);
								
				inicializaValoresGenerales();
				
				this.viewTablaGeneral=true;
				this.viewTablaEspecifica=false;
				cargarListaRolPerfil();
				cargarLista(arrayList);
				
			}else{
				//REPORTE ESPECIFICO
				
				listDatosGeneradosVO=this.actualizarListaTiemposTOE(arrayList, "0");
				
				if(listDatosGeneradosVO!=null && listDatosGeneradosVO.size()>0){
					listPosibleValor = posibleValorBean.buscarPorIdColumnaIdValor(Constantes.ID_POSIBLE_VALOR_TOE, 
							listDatosGeneradosVO.get(0).getValor().trim());
					listDatosPorcentajeVO=this.actualizarListaPorcentajeTOE(arrayList);						
					inicializaValoresEspecificos();
					cargarValoresFiltroEspecifico(arrayList);
					this.viewTablaEspecifica=true;
					this.viewTablaGeneral=false;
					this.tituloEspecifico=crearTituloEspecifico(arrayList);
					
				}else{
					this.setMsjSinResultado("No se encontró resultado con los filtros seleccionados");
					this.viewTablaEspecifica=false;
				}
					
			}			
		}
		
		return null;
	}
	
	public void cargarValoresFiltroEspecifico(ArrayList arrayList){
		TablaGenerarTOEMB.valorColumEsp=Constantes.VALOR_VACIO;
		TablaGenerarTOEMB.valorColumPerfilEsp=Constantes.VALOR_VACIO;
		TablaGenerarTOEMB.valorColumObjPerfilEsp=Constantes.VALOR_VACIO;
		this.contTiempo=0;
		this.linea=0;
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

		if(arrayList.get(5)!=null && !(arrayList.get(5).toString()).equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){
			EstadoCE objEstadoTemp= estadoCEBean.buscarPorId(Long.parseLong(arrayList.get(5).toString()));
			if(objEstadoTemp!=null){
				EstadoEspecifico=objEstadoTemp.getDescripcion();
			}
		}	
		
		if(arrayList.get(4)!=null){
			TipoOferta objTipoOfertaTemp=tipoOfertaobean.buscarPorId(Long.parseLong(arrayList.get(4).toString()));
			if(objTipoOfertaTemp!=null){
				TipoOfertaEspecifico=objTipoOfertaTemp.getDescripcion();
			}else{
				TipoOfertaEspecifico="APROBADO / REGULAR ";
			}		
		}
		
		if(arrayList.get(2)!=null){
			Producto objProductoTemp=productoBean.buscarPorId(Long.parseLong(arrayList.get(2).toString()));
			if(objProductoTemp!=null){
				ProductoEspecifico=objProductoTemp.getDescripcion();
			}else{
				ProductoEspecifico="TC/PLD";
			}
		}
		
		tituloTabla9= "<span style='color:red;'>" +Constantes.DESCRIPCION_SUBTITULO_1_1+ProductoEspecifico+"</span>";
		tituloTabla10=Constantes.DESCRIPCION_SUBTITULO_2 + TipoOfertaEspecifico;
		
		
	}


	public String obtListaEspecifica() {
		return UnidadEspecifico;
	}
	
	
	public String obtListaPerfilTxt() {
		String rpta=PerfilEspecifico;	
		return rpta;
	}
	public String obtListaEstadoTxt() {
		String rpta="";
		
		if(EstadoEspecifico!=null)
			rpta=EstadoEspecifico ;
		
		return rpta;
	}

	public String objListaPorcentaje(){
		LOG.info("objListaPorcentaje");
		if(listDatosPorcentajeVO!=null && listDatosPorcentajeVO.size()>0){
			LOG.info("listDatosPorcentajeVO tamaño "+listDatosPorcentajeVO.size());
			String resultado=listDatosPorcentajeVO.get(0).getValorPorcentaje();
			LOG.info("resultado:::"+resultado);
			resultado=resultado+" % ";
			return resultado;
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
	
	public String getConsultaResultado(){
		if(this.msjSinResultado!=null)
			if(this.getMsjSinResultado().trim().length()>0)
				return this.getMsjSinResultado();
		return null;
	}
	
	public String crearTituloEspecifico(ArrayList arrayList){
		Producto prod= productoBean.buscarPorId(Long.parseLong(arrayList.get(2).toString()));
		TipoOferta tipOferta= tipoOfertaobean.buscarPorId(Long.parseLong(arrayList.get(4).toString()));
		
		if(prod!=null && tipOferta!=null)
			return obtTituloEncabezadoTablas(viewTablaGeneral, prod.getDescripcion(), tipOferta.getDescripcion(), "");
		return "";
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
					}else{
						listProducto.add(productoBean.buscarPorId(Long.parseLong(sTipoProduct)));	
					}		
					
					for(Producto prod: listProducto){
							arrayList.set(2, String.valueOf(prod.getId()));

							if(sTipoOferta!=null && sTipoOferta.trim().length()>0){
								List<TipoOferta> lisTipoOferta=new ArrayList<TipoOferta>();
								String tmpSTipoOferta=sTipoOferta;
								
								if (sTipoOferta.trim().equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)){ // TODOS
									lisTipoOferta = tipoOfertaobean.buscarTodos();													
								}else{
									lisTipoOferta.add(tipoOfertaobean.buscarPorId(Long.parseLong(sTipoOferta)));									
								}

								for(TipoOferta tipoOferta: lisTipoOferta){									
									arrayList.set(4, String.valueOf(tipoOferta.getId()));
									listDatosGeneradosVO=this.actualizarListaTiemposTOE(arrayList,"1");
									listDatosGeneradosVO_reproceso=this.actualizarListaTiemposTOE(arrayList,"2");
									listDatosGeneradosVO.addAll(listDatosGeneradosVO_reproceso);
								}
								arrayList.set(4, tmpSTipoOferta);
							}							
						}
						arrayList.set(2, tmpSTipoProduct);
				}
				
				listPosibleValor = posibleValorBean.buscarPorIdColumna(Constantes.ID_POSIBLE_VALOR_TOE);
				listDatosPorcentajeVO=this.actualizarListaPorcentajeTOE(arrayList);
				listDatosUnidadVO=this.actualizarListaUnidadTOE(arrayList);
				
			}else{
				listDatosGeneradosVO=this.actualizarListaTiemposTOE(arrayList,"0");
				
				if(listDatosGeneradosVO!=null && listDatosGeneradosVO.size()>0){
					listPosibleValor = posibleValorBean.buscarPorIdColumnaIdValor(Constantes.ID_POSIBLE_VALOR_TOE, 
							listDatosGeneradosVO.get(0).getValor().trim());
					listDatosPorcentajeVO=this.actualizarListaPorcentajeTOE(arrayList);						
					inicializaValoresEspecificos();
					this.viewTablaEspecifica=true;
					this.viewTablaGeneral=false;
				}				
			}			
		}
		
		return null;
	}	

	public void cargarListaRolPerfil() {
		
    	Map<String, ArrayList<String>> mapPerfilRol = new HashMap<String, ArrayList<String>>();
    	
    	if(listPosibleValor!=null){
    		objlistaRoles=new ListaReporteToeRol();
    		objlistaPerfiles=new ListaReporteToePerfil();
    		listaRoles=new ArrayList<ListaReporteToeRol>();
    		listaPerfiles=new ArrayList<ListaReporteToePerfil>();
    		
	    		for(PosibleValor objPosibleValor : listPosibleValor){
	    			String rol=obtDetalle(objPosibleValor.getEtiqueta());
	    			ArrayList<String> list= mapPerfilRol.get(rol);
	    			
	    			if(list==null){
	    				list=new ArrayList<String>();
	    				mapPerfilRol.put(rol, list);
	    				setearColumnaRol(rol);
	    			}
	    			String perfil=obtDetallePerfil(objPosibleValor.getEtiqueta());
	    			setearColumnaPerfil(perfil);
	    		}
	    		
	    		if(objlistaRoles!=null)
	    			listaRoles.add(objlistaRoles);	
	    		
	    		if(objlistaPerfiles!=null)
	    			listaPerfiles.add(objlistaPerfiles);
    	}

    }
	
	public void setearColumnaRol(String rol){
		if(objlistaRoles.getColumn1()==null || objlistaRoles.getColumn1().length()<1){				
			objlistaRoles.setColumn1(rol);					
		}else					
			if(objlistaRoles.getColumn2()==null || objlistaRoles.getColumn2().length()<1){
				objlistaRoles.setColumn2(rol);						
			}else
				if(objlistaRoles.getColumn3()==null || objlistaRoles.getColumn3().length()<1){
					objlistaRoles.setColumn3(rol);							
				}else						
					if(objlistaRoles.getColumn4()==null || objlistaRoles.getColumn4().length()<1){
						objlistaRoles.setColumn4(rol);									
					}else						
						if(objlistaRoles.getColumn5()==null || objlistaRoles.getColumn5().length()<1){
							objlistaRoles.setColumn5(rol);								
						}		
	}
	
	public void setearColumnaPerfil(String perfil){
		if(objlistaPerfiles.getColumn1()==null || objlistaPerfiles.getColumn1().length()<1){				
			objlistaPerfiles.setColumn1(perfil);				
		}else					
			if(objlistaPerfiles.getColumn2()==null || objlistaPerfiles.getColumn2().length()<1){
				objlistaPerfiles.setColumn2(perfil);						
			}else
				if(objlistaPerfiles.getColumn3()==null || objlistaPerfiles.getColumn3().length()<1){
					objlistaPerfiles.setColumn3(perfil);					
				}else						
					if(objlistaPerfiles.getColumn4()==null || objlistaPerfiles.getColumn4().length()<1){
						objlistaPerfiles.setColumn4(perfil);								
					}else						
						if(objlistaPerfiles.getColumn5()==null || objlistaPerfiles.getColumn5().length()<1){
							objlistaPerfiles.setColumn5(perfil);								
						}else					
							if(objlistaPerfiles.getColumn6()==null || objlistaPerfiles.getColumn6().length()<1){
								objlistaPerfiles.setColumn6(perfil);					
							}else
								if(objlistaPerfiles.getColumn7()==null || objlistaPerfiles.getColumn7().length()<1){
									objlistaPerfiles.setColumn7(perfil);						
								}else						
									if(objlistaPerfiles.getColumn8()==null || objlistaPerfiles.getColumn8().length()<1){
										objlistaPerfiles.setColumn8(perfil);
									}else						
										if(objlistaPerfiles.getColumn9()==null || objlistaPerfiles.getColumn9().length()<1){
											objlistaPerfiles.setColumn9(perfil);								
										}		
	}	

	public PosibleValor obtenerRol(){
		PosibleValor objPosibleValor=null;

		int j=0;
		if(listPosibleValor!=null)
			for(int i=0;i<listPosibleValor.size()-1;i++){
				objPosibleValor=new PosibleValor();
				if(valorColumPerfil.equals(Constantes.VALOR_VACIO)){
					valorColumPerfil=obtDetallePerfil(listPosibleValor.get(i).getEtiqueta());
					objPosibleValor=listPosibleValor.get(i);
					break;
				}else
				{
					if(valorColumPerfil.equals(obtDetallePerfil(listPosibleValor.get(i).getEtiqueta()))){
						j=i+1;
						if(!valorColumPerfil.equals(obtDetallePerfil(listPosibleValor.get(j).getEtiqueta()))){
							valorColumPerfil=obtDetallePerfil(listPosibleValor.get(j).getEtiqueta());
							objPosibleValor=listPosibleValor.get(j);						
							break;							
						}
					}
				}
			}

		return objPosibleValor;
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
	
	public String obtRangoFechas(){
		BuscarTOEMB buscarTOEMB=null;	
		String fechaRango=null;
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		buscarTOEMB = (BuscarTOEMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarTOE");
		
		DateFormat df =  DateFormat.getDateInstance();
		if(buscarTOEMB!=null && buscarTOEMB.getFechaInicio()!=null && buscarTOEMB.getFechaFin()!=null){
			fechaRango= df.format(buscarTOEMB.getFechaInicio())+ Constantes.DESCRIPCION_SUBTITULO_4+ df.format(buscarTOEMB.getFechaFin());
		}
		
		this.setRangoFechas(fechaRango);
		//rangoFechasFormato=obtRangoFechasFormato(fechaRango);
		return fechaRango;
	}
	
	public String obtRangoFechasFormato(String fechaRango){
		String fechaRangoFormato="";
		String diaInicio ="";
		String mesInicio="";
		String diaFin="";
		String mesFin="";
		String mes;
		diaInicio=fechaRango.substring(0,2);
		mesInicio=fechaRango.substring(3,5);
		diaFin=fechaRango.substring(14,16);
		mesFin=fechaRango.substring(17,19);
		
		mes=obtenerMes(mesFin);
		
		fechaRangoFormato=diaInicio + " AL "  + diaFin + " " + mes;
		
		return fechaRangoFormato;
		
	}
	
	public String obtenerMes(String mes){
		int intMes=0;
		String mesResultado="";
		intMes=Integer.parseInt(mes);
		switch(intMes){
			case 1  : mesResultado="ENERO" ; break;
			case 2  : mesResultado="FEBRERO" ; break;
			case 3  : mesResultado="MARZO" ; break;
			case 4  : mesResultado="ABRIL" ; break;
			case 5  : mesResultado="MAYO" ; break;
			case 6  : mesResultado="JUNIO" ; break;
			case 7  : mesResultado="JULIO" ; break;
			case 8  : mesResultado="AGOSTO" ; break;
			case 9  : mesResultado="SEPTIEMBRE" ; break;
			case 10 : mesResultado="OCTUBRE" ; break;
			case 11 : mesResultado="NOVIEMBRE" ; break;
			case 12 : mesResultado="DICIEMBRE" ; break;
		}
		
		return mesResultado;
		
	}
	
	public String obtTituloEncabezadoTablas(boolean tipoTitGeneral, String tipoProducto, String tipoOferta, String tipoFlujo){
		String subTitulo=null;

	        subTitulo=Constantes.DESCRIPCION_SUBTITULO_1 +this.obtRangoFechas();
	        //subTitulo=subTitulo+ Constantes.VALOR_SEPARADOR_TITULO+Constantes.DESCRIPCION_SUBTITULO_1_1+ tipoProducto;
	        subTitulo=subTitulo+ Constantes.VALOR_SEPARADOR_TITULO+"<span style='color:red;'>"+ Constantes.DESCRIPCION_SUBTITULO_1_1+tipoProducto+"</span>";
	        subTitulo=subTitulo+ Constantes.VALOR_SEPARADOR_TITULO+Constantes.DESCRIPCION_SUBTITULO_2+ tipoOferta;
	        
	        if(tipoTitGeneral)
	        	subTitulo=subTitulo+ Constantes.VALOR_SEPARADOR_TITULO+Constantes.DESCRIPCION_SUBTITULO_3+ tipoFlujo;
	        
	      return subTitulo;	
			
	}
	public void cargarLista(ArrayList arrayList){

		setearBoolTabla(false);
				
		if(this.viewTablaGeneral){					//REPORTE - GENERICO
			
			if(this.viewProductoTC){				// PRODUCTO - TC
				
				if(this.viewTipoOfertaAprobado){	//TIPO OFERTA - APROBADO
					
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
				}
				
				if(this.viewTipoOfertaRegular){	//TIPO OFERTA - REGULAR
						
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
					listPorc_Prod_2_TipOferta_1_Flujo_2 =cargarListaPorcentaje_Generico_Filtros(Constantes.ID_APLICATIVO_PLD, Constantes.CODIGO_OFERTA_APROBADO, 
							Constantes.VALOR_FLUJO_REPROCESO);			
					listUnid_Prod_2_TipOferta_1_Flujo_2 = cargarListaUnidad_Generico_Filtros(Constantes.ID_APLICATIVO_PLD, Constantes.CODIGO_OFERTA_APROBADO, 
							Constantes.VALOR_FLUJO_REPROCESO);	
					
					this.setTabla5(true);
					this.setTabla6(true);
					
					this.setTituloTabla5(obtTituloEncabezadoTablas(this.viewTablaGeneral,Constantes.DESCRIPCION_TIPO_PRODUCTO_PLD, 
							Constantes.DESCRIPCION_TIPO_OFERTA_APROBADO, Constantes.DESCRIPCION_TIPO_FLUJO_REGULAR));	
					this.setTituloTabla6(obtTituloEncabezadoTablas(this.viewTablaGeneral,Constantes.DESCRIPCION_TIPO_PRODUCTO_PLD, 
							Constantes.DESCRIPCION_TIPO_OFERTA_APROBADO, Constantes.DESCRIPCION_TIPO_FLUJO_REPROCESO));					
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
				}				
				
			}			
		}		
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
	
	public List<ListaReporteToe> cargarLista_Generico_Filtros(int id_producto, String id_tipo_oferta, String id_flujo){
		ArrayList arrayListTiempo=new ArrayList();
		List<ListaReporteToe> listTemp=new ArrayList<ListaReporteToe>();
		
		arrayListTiempo.add(Constantes.DESCRIPCION_TIEMPO_OBJETIVO.toUpperCase().trim());
		arrayListTiempo.add(Constantes.DESCRIPCION_TIEMPO_PRELIMINAR.toUpperCase().trim());
		arrayListTiempo.add(Constantes.DESCRIPCION_TIEMPO_REAL.toUpperCase().trim());
		
		//listTemp.add(cargarCabeceraTiempo());
		
		for (int i = 0; i < arrayListTiempo.size(); i++){
			listTemp.add(cargarLista_Generico_Filtros(id_producto, id_tipo_oferta, 
					id_flujo, (String) arrayListTiempo.get(i)));
		}	
		
		return listTemp;
	}
	
	public List<ListaReportePorcentajeToe> cargarListaPorcentaje_Generico_Filtros(int id_producto, String id_tipo_oferta, String id_flujo){
		List<ListaReportePorcentajeToe> listTemp=new ArrayList<ListaReportePorcentajeToe>();

			listTemp.add(cargarListaPorcentaje_Filtros(id_producto, id_tipo_oferta, 
					id_flujo));

		return listTemp;
	}
	
	public List<ListaReporteUnidadToe>  cargarListaUnidad_Generico_Filtros(int id_producto, String id_tipo_oferta, String id_flujo){
		List<ListaReporteUnidadToe> listTemp=new ArrayList<ListaReporteUnidadToe>();

		listTemp=cargarListaUnidad_Filtros(id_producto, id_tipo_oferta, 
					id_flujo);

		return listTemp;
	}	
	
	
	public List<ListaReporteToe> getCargarCabeceraTiempo(){
		List<ListaReporteToe> listresult=new ArrayList<ListaReporteToe>();
		ListaReporteToe obj=new ListaReporteToe();
		obj.setColumn1(Constantes.VALOR_TIEMPO_TE);
		obj.setColumn2(Constantes.VALOR_TIEMPO_TC);
		obj.setColumn3(Constantes.VALOR_TIEMPO_TE);
		obj.setColumn4(Constantes.VALOR_TIEMPO_TC);
		obj.setColumn5(Constantes.VALOR_TIEMPO_TE);
		obj.setColumn6(Constantes.VALOR_TIEMPO_TC);
		obj.setColumn7(Constantes.VALOR_TIEMPO_TE);
		obj.setColumn8(Constantes.VALOR_TIEMPO_TC);
		obj.setColumn9(Constantes.VALOR_TIEMPO_TE);
		obj.setColumn10(Constantes.VALOR_TIEMPO_TC);
		obj.setColumn11(Constantes.VALOR_TIEMPO_TE);
		obj.setColumn12(Constantes.VALOR_TIEMPO_TC);
		obj.setColumn13(Constantes.VALOR_TIEMPO_TE);
		obj.setColumn14(Constantes.VALOR_TIEMPO_TC);
		obj.setColumn15(Constantes.VALOR_TIEMPO_TE);
		obj.setColumn16(Constantes.VALOR_TIEMPO_TC);
		obj.setColumn17(Constantes.VALOR_TIEMPO_TE);
		listresult.add(obj);
		return listresult;
				
	}
	public ListaReporteToe cargarLista_Generico_Filtros(int id_producto, String id_tipo_oferta, String id_flujo, String tiempo){
		objListaReporteToe=new ListaReporteToe();

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
					LOG.info("Tiempo::::::"+tiempo);
					inicializaValoresGenerales();
					
					objListaReporteToe.setColumn0(tiempo);
					int valorTiempo=0;
					
					//if(tiempo!=null && tiempo.trim().equals("T. REAL")){
						valorTiempo=1;
					//}
					if(listDatosGeneradosVO!=null){
						LOG.info("listDatosGeneradosVO tamaño ::: "+listDatosGeneradosVO.size());
						for(DatosGeneradosVO vo: listDatosGeneradosVO){
							boolean bol;
							
							do{
								PosibleValor pv =obtenerRolLista();
								if(pv.getValor().trim().equals(vo.getValor()))
									bol=true;
								else
									bol=false;
								
								setearColumnas(vo, pv, valorTiempo);
							}while(!bol);
						
						}			
					}else{
						boolean bol;
						LOG.info("listDatosGeneradosVO es nulo");
						do{
							PosibleValor pv =obtenerRolLista();				
							setearColumnas(new DatosGeneradosVO(), pv, valorTiempo);
							
							if(pv.getValor().trim().equals("CEE"))
								bol=true;
							else
								bol=false;
							
						}while(!bol);			
					}			
				}				
			}					
		}

		return objListaReporteToe;
	}
	
	public ListaReportePorcentajeToe cargarListaPorcentaje_Filtros(int id_producto, String id_tipo_oferta, String id_flujo){
		objListaReportePorcentajeToe=new ListaReportePorcentajeToe();
		
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

		
		return objListaReportePorcentajeToe;
	}	
	
	public List<ListaReporteUnidadToe> cargarListaUnidad_Filtros(int id_producto, String id_tipo_oferta, String id_flujo){
		List<ListaReporteUnidadToe> listResult=new ArrayList<ListaReporteUnidadToe>();
		
		ArrayList<String> listConceptos=new ArrayList<String>();
		listConceptos.add("CPM");
		listConceptos.add("RIESGO");
		
		// // system.out.println("id_producto="+id_producto);
		// // system.out.println("id_tipo_oferta="+id_tipo_oferta);
		// // system.out.println("id_flujo="+id_flujo);
		
		Map<Integer, Map<String, ArrayList<DatosUnidadVO>>> mapTipoOferta = 
				mapaUnidadProductoTipoOferta.get(id_producto);
		// // system.out.println("UNIDAD mapTipoOferta="+mapTipoOferta.size());
		
		Map<String, ArrayList<DatosUnidadVO>>  mapTipoFlujo = 
				mapTipoOferta.get(Integer.parseInt(id_tipo_oferta));
		// // system.out.println("UNIDAD mapTipoFlujo="+mapTipoFlujo.size());
		
		ArrayList<DatosUnidadVO> listDatosUnidadVO = 
				mapTipoFlujo.get(id_flujo);
		// // system.out.println("UNIDAD mapTipoConcepto="+listDatosUnidadVO.size());
		
		// // system.out.println("UNIDAD listConceptos.size="+listConceptos.size());
				for(int i=0; i<listConceptos.size(); i++){
					
					objListaReporteUnidadToe=new ListaReporteUnidadToe();
					
					float totalAceptados=0;
					float totalRegistros=0;
					double totalPorcentaje=0;
					
					// // system.out.println("UNIDAD listConceptos.get(0).toString() = "+listConceptos.get(0).toString());
					setearUnidadColumnas(listConceptos.get(i).toString());
					
					if(listDatosUnidadVO!=null){
						for(DatosUnidadVO vo: listDatosUnidadVO){
							LOG.info("vo.getTipo().trim() :: "+vo.getTipo().trim());
							LOG.info("listConceptos.get(i).toString():: "+listConceptos.get(i).toString());
							if(vo.getTipo().trim().equals(listConceptos.get(i).toString())){
								totalAceptados=totalAceptados+vo.getAceptado();
								totalRegistros=totalRegistros+vo.getTotal();
							}							
						}
						
						totalPorcentaje=(totalAceptados*100)/totalRegistros;
						totalPorcentaje=((double)Math.round(totalPorcentaje * 10) / 10);
						LOG.info("totalPorcentaje :: "+totalPorcentaje);
					}else
						totalPorcentaje=0.0;
					
					setearUnidadColumnas(String.valueOf(totalPorcentaje));	
					
					listResult.add(objListaReporteUnidadToe);
				}

		
		return listResult;
	}
	
	public double obtPorcentajeCPM_Riesgos(String tipo){
		float totalAceptados=0;
		float totalRegistros=0;
		float totalPorcentaje=0;
		
	 if(listDatosUnidadVO!=null && listDatosUnidadVO.size()>0)	
		for(int i=0; i<listDatosUnidadVO.size();i++){
			if(listDatosUnidadVO.get(i)!=null){
				if(listDatosUnidadVO.get(i).getTipo().trim().equals(tipo)){
					totalAceptados=totalAceptados+listDatosUnidadVO.get(i).getAceptado();
					totalRegistros=totalRegistros+listDatosUnidadVO.get(i).getTotal();
				}
			}
		}

	 	totalPorcentaje=(totalAceptados*100)/totalRegistros;
		return (double)Math.round(totalPorcentaje * 10) / 10;
	}
	
	public void setearColumnas(DatosGeneradosVO vo, PosibleValor pv, int valorTiempo){
		DecimalFormat format = new DecimalFormat("0.00");
		  
		if(pv.getValor().trim().equals(vo.getValor())){
			if(objListaReporteToe.getColumn1()==null){				
				objListaReporteToe.setColumn1(vo.getTe()==null?"":(valorTiempo==0?vo.getTe():format.format(Double.parseDouble(vo.getTe())).toString()).replace(',', '.'));
			}else					
				if(objListaReporteToe.getColumn2()==null){
					objListaReporteToe.setColumn2(vo.getTc()==null?"":(valorTiempo==0?vo.getTc():format.format(Double.parseDouble(vo.getTc())).toString()).replace(',', '.'));
					objListaReporteToe.setColumn3(vo.getTe()==null?"":(valorTiempo==0?vo.getTe():format.format(Double.parseDouble(vo.getTe())).toString()).replace(',', '.'));
				}else
					if(objListaReporteToe.getColumn4()==null){
						objListaReporteToe.setColumn4(vo.getTc()==null?"":(valorTiempo==0?vo.getTc():format.format(Double.parseDouble(vo.getTc())).toString()).replace(',', '.'));
						objListaReporteToe.setColumn5(vo.getTe()==null?"":(valorTiempo==0?vo.getTe():format.format(Double.parseDouble(vo.getTe())).toString()).replace(',', '.'));						
					}else						
						if(objListaReporteToe.getColumn6()==null){
							objListaReporteToe.setColumn6(vo.getTc()==null?"":(valorTiempo==0?vo.getTc():format.format(Double.parseDouble(vo.getTc())).toString()).replace(',', '.'));
							objListaReporteToe.setColumn7(vo.getTe()==null?"":(valorTiempo==0?vo.getTe():format.format(Double.parseDouble(vo.getTe())).toString()).replace(',', '.'));								
						}else								
							if(objListaReporteToe.getColumn8()==null){
								objListaReporteToe.setColumn8(vo.getTc()==null?"":(valorTiempo==0?vo.getTc():format.format(Double.parseDouble(vo.getTc())).toString()).replace(',', '.'));
								objListaReporteToe.setColumn9(vo.getTe()==null?"":(valorTiempo==0?vo.getTe():format.format(Double.parseDouble(vo.getTe())).toString()).replace(',', '.'));							
							}else								
								if(objListaReporteToe.getColumn10()==null){
									objListaReporteToe.setColumn10(vo.getTc()==null?"":(valorTiempo==0?vo.getTc():format.format(Double.parseDouble(vo.getTc())).toString()).replace(',', '.'));
									objListaReporteToe.setColumn11(vo.getTe()==null?"":(valorTiempo==0?vo.getTe():format.format(Double.parseDouble(vo.getTe())).toString()).replace(',', '.'));								
								}else								
									if(objListaReporteToe.getColumn12()==null){
										objListaReporteToe.setColumn12(vo.getTc()==null?"":(valorTiempo==0?vo.getTc():format.format(Double.parseDouble(vo.getTc())).toString()).replace(',', '.'));
										objListaReporteToe.setColumn13(vo.getTe()==null?"":(valorTiempo==0?vo.getTe():format.format(Double.parseDouble(vo.getTe())).toString()).replace(',', '.'));										
									}else								
										if(objListaReporteToe.getColumn14()==null){
											objListaReporteToe.setColumn14(vo.getTc()==null?"":(valorTiempo==0?vo.getTc():format.format(Double.parseDouble(vo.getTc())).toString()).replace(',', '.'));
											objListaReporteToe.setColumn15(vo.getTe()==null?"":(valorTiempo==0?vo.getTe():format.format(Double.parseDouble(vo.getTe())).toString()).replace(',', '.'));												
										}else								
											if(objListaReporteToe.getColumn16()==null){
												objListaReporteToe.setColumn16(vo.getTc()==null?"":(valorTiempo==0?vo.getTc():format.format(Double.parseDouble(vo.getTc())).toString()).replace(',', '.'));
												objListaReporteToe.setColumn17(vo.getTe()==null?"":(valorTiempo==0?vo.getTe():format.format(Double.parseDouble(vo.getTe())).toString()).replace(',', '.'));												
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
	
	public void setearUnidadColumnas(String cad){

			if(objListaReporteUnidadToe.getColumn1()==null){				
				objListaReporteUnidadToe.setColumn1(cad==null?"":cad);				
			}else					
				if(objListaReporteUnidadToe.getColumn2()==null){
					objListaReporteUnidadToe.setColumn2(cad==null?"":cad);				
				}
	
	}		
	
	private Map<Integer, Map<String, Map<String, ArrayList<DatosGeneradosVO>>>> agregarMapaProductoTipoOferta (Map<Integer, Map<Integer, Map<String, Map<String, ArrayList<DatosGeneradosVO>>>>>  mapa, 
			Integer codProducto) {
		
		Map<Integer, Map<String, Map<String, ArrayList<DatosGeneradosVO>>>> mapaTipoOferta= mapa.get(codProducto);
		
		if(mapaTipoOferta==null){
			mapaTipoOferta=new TreeMap<Integer, Map<String,Map<String,ArrayList<DatosGeneradosVO>>>>();
			mapa.put(codProducto, mapaTipoOferta);
		}

		return mapaTipoOferta;

	}
	
	private Map<Integer, Map<String, ArrayList<DatosPorcentajeVO>>> agregarMapaPorcenProductoTipoOferta (
			Map<Integer, Map<Integer, Map<String, ArrayList<DatosPorcentajeVO>>>>  mapa, 
			Integer codProducto) {
		
		Map<Integer, Map<String, ArrayList<DatosPorcentajeVO>>> mapaPorcentajeTipoOferta= mapa.get(codProducto);
		
		if(mapaPorcentajeTipoOferta==null){
			mapaPorcentajeTipoOferta=new TreeMap<Integer, Map<String,ArrayList<DatosPorcentajeVO>>>();
			mapa.put(codProducto, mapaPorcentajeTipoOferta);
		}
		
		return mapaPorcentajeTipoOferta;
	}	
	
	private Map<Integer, Map<String, ArrayList<DatosUnidadVO>>> agregarMapaUnidProductoTipoOferta (
			Map<Integer, Map<Integer, Map<String, ArrayList<DatosUnidadVO>>>>  mapa, 
			Integer codProducto) {
		
		Map<Integer, Map<String, ArrayList<DatosUnidadVO>>> mapaUnidadTipoOferta= mapa.get(codProducto);
		
		if(mapaUnidadTipoOferta==null){
			mapaUnidadTipoOferta=new TreeMap<Integer, Map<String,ArrayList<DatosUnidadVO>>>();
			mapa.put(codProducto, mapaUnidadTipoOferta);
			// // system.out.println("se creo UNIDAD con tipo producto = "+ codProducto);
		}
		
		return mapaUnidadTipoOferta;

	}	
	
	private void agregarMapaTipoOferta (Map<Integer, Map<String, Map<String, ArrayList<DatosGeneradosVO>>>>  mapa, 
			Integer codTipoOferta, List<DatosGeneradosVO> listDatosGenerados) {
		
		Map<String, Map<String, ArrayList<DatosGeneradosVO>>> mapaTipoFlujo= mapa.get(codTipoOferta);
		
		if(mapaTipoFlujo==null){
			//mapaTipoFlujo=new TreeMap<String, ArrayList<DatosGeneradosVO>>();
			mapaTipoFlujo=new TreeMap<String, Map<String,ArrayList<DatosGeneradosVO>>>();
			mapa.put(codTipoOferta, mapaTipoFlujo);
			//// system.out.println("se creo uno con tipo oferta = "+ codTipoOferta);
		}
		//// system.out.println("ya fue creado map tipo oferta = "+ codTipoOferta);
		mapaTipoFlujo(mapaTipoFlujo, listDatosGenerados);
		//lista.add(objDatosGeneradosVO);
	}
	
	private void agregarMapaPorcentajeTipoOferta (Map<Integer, Map<String, ArrayList<DatosPorcentajeVO>>>  mapa, 
			Integer codTipoOferta, List<DatosPorcentajeVO> listDatosPorcentajeVO) {
		
		Map<String, ArrayList<DatosPorcentajeVO>> mapaPorcentajeTipoFlujo= mapa.get(codTipoOferta);
		
		if(mapaPorcentajeTipoFlujo==null){
			mapaPorcentajeTipoFlujo=new TreeMap<String, ArrayList<DatosPorcentajeVO>>();
			mapa.put(codTipoOferta, mapaPorcentajeTipoFlujo);
		}
		mapaPorcentajeTipoFlujo(mapaPorcentajeTipoFlujo, listDatosPorcentajeVO);

	}	
	
	private void agregarMapaUnidadTipoOferta (Map<Integer, Map<String, ArrayList<DatosUnidadVO>>>  mapa, 
			Integer codTipoOferta, List<DatosUnidadVO> listDatosUnidadVO) {
		
		Map<String, ArrayList<DatosUnidadVO>> mapaUnidadTipoFlujo= mapa.get(codTipoOferta);
		
		if(mapaUnidadTipoFlujo==null){
			//mapaTipoFlujo=new TreeMap<String, ArrayList<DatosGeneradosVO>>();
			mapaUnidadTipoFlujo=new TreeMap<String, ArrayList<DatosUnidadVO>>();
			mapa.put(codTipoOferta, mapaUnidadTipoFlujo);
			// // system.out.println("se creo UNIDAD con tipo oferta = "+ codTipoOferta);
		}
		// // system.out.println("ya fue creado map UNIDAD tipo oferta = "+ codTipoOferta);
		mapaUnidadTipoFlujo(mapaUnidadTipoFlujo, listDatosUnidadVO);

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
	
	public void mapaPorcentajeTipoFlujo(Map<String, ArrayList<DatosPorcentajeVO>> mapa, List<DatosPorcentajeVO> listDatosPorcentajeVO){
		 // system.out.println("entro mapaPorcentajeTipoFlujo");
		
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
	
	public void mapaUnidadTipoFlujo(Map<String, ArrayList<DatosUnidadVO>> mapa, List<DatosUnidadVO> listDatosUnidadVO){
		// system.out.println("entro mapaUnidadTipoFlujo");
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
	
	public void mapaTipoConcepto(Map<String, ArrayList<DatosGeneradosVO>> mapa, String Concepto, DatosGeneradosVO objDatosGeneradosVO){
		
		ArrayList<DatosGeneradosVO> lista = mapa.get(Concepto);
		
		if (lista==null) {
			lista = new ArrayList<DatosGeneradosVO>();
			mapa.put(Concepto, lista);
		}
		lista.add(objDatosGeneradosVO);
		
	}
	
	public void mapaPorcentajeTipoConcepto(Map<String, ArrayList<DatosPorcentajeVO>> mapa, String rol, DatosPorcentajeVO objDatosPorcentajeVO){
		
		ArrayList<DatosPorcentajeVO> lista = mapa.get(rol);
		
		if (lista==null) {
			lista = new ArrayList<DatosPorcentajeVO>();
			mapa.put(rol, lista);
		}
		lista.add(objDatosPorcentajeVO);
		
	}
	
/*	private void agregarMapaTipoRol (Map<String, ArrayList<DatosGeneradosVO>> mapa, String Flujo, DatosGeneradosVO objDatosGeneradosVO) {
		// system.out.println("Agregando agregarMapaTipoFlujo");
		ArrayList<DatosGeneradosVO> lista = mapa.get(Flujo);
		if (lista==null) {
			lista = new ArrayList<DatosGeneradosVO> ();
			mapa.put(Flujo, lista);
			// system.out.println("se creo uno con tipo flujo = "+ Flujo);
		}
		// system.out.println("se agrego flujo ");
		lista.add(objDatosGeneradosVO);
	}
	*/
	public List<DatosGeneradosVO> actualizarListaTiemposTOE (ArrayList arrayList, String opcion) {

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
	
	public String actualizarTramaDatosGenerados(List<DatosGeneradosVO> listDatosGeneradosVO){
		StringBuffer sb = new StringBuffer();
		String resultado=null;
		for (DatosGeneradosVO obj : listDatosGeneradosVO) {
			if (obj != null) {
				sb.append(obj.getValor());
				sb.append(",");
				sb.append(obj.getTipoConcepto());
				sb.append(",");
				sb.append(obj.getTc());
				sb.append(",");
				sb.append(obj.getTe());
				sb.append(",");	
				sb.append(obj.getFlujo());
				sb.append(",");				
			}
		}
		
		if (sb.length() > 0) 
			resultado = sb.substring(0, sb.length() - 1).toString();

		return resultado;
	} 
		
	public void inicializaValoresGenerales(){
		TablaGenerarTOEMB.valorColum=Constantes.VALOR_VACIO;
		TablaGenerarTOEMB.valorColumPerfil=Constantes.VALOR_VACIO;
		TablaGenerarTOEMB.valorColumPerfilLista=Constantes.VALOR_VACIO;
		TablaGenerarTOEMB.valorColumObjPerfil=Constantes.VALOR_VACIO;
		TablaGenerarTOEMB.valorColumPorcentaje=Constantes.VALOR_VACIO;
		this.contTiempo=0;
		this.linea=0;		
	}
	
	public void inicializaValoresEspecificos(){
		TablaGenerarTOEMB.valorColumEsp=Constantes.VALOR_VACIO;
		TablaGenerarTOEMB.valorColumPerfilEsp=Constantes.VALOR_VACIO;
		TablaGenerarTOEMB.valorColumObjPerfilEsp=Constantes.VALOR_VACIO;
		this.contTiempo=0;
		this.linea=0;		
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
/*
	public List<PosibleValor> getObtLista() {
		List<PosibleValor> result=new ArrayList<PosibleValor>();
		PosibleValor objPosibleValor=new PosibleValor();
		int j=0;
		
		
		if(listPosibleValor!=null){			
				for(int i=0;i<listPosibleValor.size()-1;i++){
					objPosibleValor=new PosibleValor();
					if(valorColum.equals(Constantes.VALOR_VACIO)){
						valorColum=obtDetalle(listPosibleValor.get(i).getEtiqueta());
						objPosibleValor=listPosibleValor.get(i);
						break;
					}else
					{						
						if(valorColum.equals(obtDetalle(listPosibleValor.get(i).getEtiqueta()))){
							j=i+1;
							if(!valorColum.equals(obtDetalle(listPosibleValor.get(j).getEtiqueta()))){
								valorColum=obtDetalle(listPosibleValor.get(j).getEtiqueta());
								
								objPosibleValor=listPosibleValor.get(j);
								break;							
							}
						}
					}
				}				

			result.add(objPosibleValor);	
			return result;
		}
		return null;
	}*/
	/*
	public String obtListaEspecifica() {
		PosibleValor objPosibleValor=null;
		
		if(listPosibleValor!=null){
			if(listPosibleValor.size()==1){
				if(valorColumEsp.equals(Constantes.VALOR_VACIO)){
					valorColumEsp=obtDetalle(listPosibleValor.get(0).getEtiqueta());
					objPosibleValor=listPosibleValor.get(0);
				}				
			}
			String cad=Constantes.VALOR_VACIO;
			if(objPosibleValor!=null)
				cad=this.obtDetalle(objPosibleValor.getEtiqueta());	

			return cad;
		}
		
		return null;
	}
	
	
	public String obtListaPerfilTxt(String cad) {

		PosibleValor objPosibleValor=null;

		int j=0;
		if(listPosibleValor!=null){
			if(listPosibleValor.size()==1 && cad.equals(Constantes.ID_TIEMPO_TE)){
				if(valorColumPerfilEsp.equals(Constantes.VALOR_VACIO)){
					valorColumPerfilEsp=obtDetallePerfil(listPosibleValor.get(0).getEtiqueta());
					objPosibleValor=listPosibleValor.get(0);
				}				
			}else{
				for(int i=0;i<listPosibleValor.size()-1;i++){
					objPosibleValor=new PosibleValor();
					if(valorColumPerfil.equals(Constantes.VALOR_VACIO)){
						valorColumPerfil=obtDetallePerfil(listPosibleValor.get(i).getEtiqueta());
						objPosibleValor=listPosibleValor.get(i);
						break;
					}else
					{
						if(valorColumPerfil.equals(obtDetallePerfil(listPosibleValor.get(i).getEtiqueta()))){
							j=i+1;
							if(!valorColumPerfil.equals(obtDetallePerfil(listPosibleValor.get(j).getEtiqueta()))){
								valorColumPerfil=obtDetallePerfil(listPosibleValor.get(j).getEtiqueta());
								objPosibleValor=listPosibleValor.get(j);						
								break;							
							}
						}
					}
				}				
			}

			if(objPosibleValor!=null && objPosibleValor.getEtiqueta()!=null && objPosibleValor.getEtiqueta().trim().length()>0)
				return obtDetallePerfil(objPosibleValor.getEtiqueta());
		}
		
		return null;
	}	
	
	public String objListaPorcentaje(){
		LOG.info("objListaPorcentaje");
		if(listDatosPorcentajeVO!=null && listDatosPorcentajeVO.size()>0){
			LOG.info("listDatosPorcentajeVO tamaño "+listDatosPorcentajeVO.size());
			String resultado=listDatosPorcentajeVO.get(listDatosPorcentajeVO.size()-1).getValorPorcentaje();
			LOG.info("resultado:::"+resultado);
			return resultado;
		}
		return null;
	}
	
	public String objListaObjTxt(String cad, String tipo){
		PosibleValor objPosibleValor=null;
		String cadResult=null;
		
		if(cad.equals(Constantes.ID_TIEMPO_PRE) && linea==0){
			linea=1;//T.Preliminar
			valorColumObjPerfil=Constantes.VALOR_VACIO;
			valorColumObjPerfilEsp=Constantes.VALOR_VACIO;
			contTiempo=0;
		}
		
		if(cad.equals(Constantes.ID_TIEMPO_REAL) && linea==1){
			linea=2;//T.Real
			valorColumObjPerfil=Constantes.VALOR_VACIO;
			valorColumObjPerfilEsp=Constantes.VALOR_VACIO;
			contTiempo=0;
		}
			
		int j=0;
		
		if(listPosibleValor!=null){
			if(listPosibleValor.size()==1){
				if(valorColumObjPerfilEsp.equals(Constantes.VALOR_VACIO)){
					valorColumObjPerfilEsp=obtDetallePerfil(listPosibleValor.get(0).getEtiqueta());
					objPosibleValor=listPosibleValor.get(0);
				}else{
					objPosibleValor=listPosibleValor.get(0);
							
					if(contTiempo==1){
						valorColumObjPerfilEsp=obtDetallePerfil(listPosibleValor.get(0).getEtiqueta());
						contTiempo=0;
					}else
						contTiempo++;			
				}				
			}else{
				for(int i=0;i<listPosibleValor.size()-1;i++){
					objPosibleValor=new PosibleValor();
					if(valorColumObjPerfil.equals(Constantes.VALOR_VACIO)){
						valorColumObjPerfil=obtDetallePerfil(listPosibleValor.get(i).getEtiqueta());
						objPosibleValor=listPosibleValor.get(i);
						break;
					}else
					{
						if(valorColumObjPerfil.equals(obtDetallePerfil(listPosibleValor.get(i).getEtiqueta()))){
							j=i+1;
							if(!valorColumObjPerfil.equals(obtDetallePerfil(listPosibleValor.get(j).getEtiqueta()))){
								objPosibleValor=listPosibleValor.get(j);
								
								if(contTiempo==1){
									valorColumObjPerfil=obtDetallePerfil(listPosibleValor.get(j).getEtiqueta());
									contTiempo=0;
								}else
									contTiempo++;

								break;							
							}
						}
					}
				}				
			}

			
			if(objPosibleValor!=null)
				cadResult=this.devolverObjetivo(objPosibleValor, cad, tipo);
			
			return cadResult;
		}
		
		return null;	
	}*/
	
	/*
	public List<DatosGeneradosVO> devolverListaObj(PosibleValor ps){
		List<DatosGeneradosVO> result=null;
		if(listDatosGeneradosVO!=null && listDatosGeneradosVO.size()>0){
			result=new ArrayList<DatosGeneradosVO>();
							
			for(DatosGeneradosVO obj: listDatosGeneradosVO){
				if(obj!=null){
					if(ps.getValor().trim().equals(obj.getValor().trim())){
						result.add(obj);
					}					
				}
			}			
		}
		return result;
	}*/
	
	public String devolverObjetivo(PosibleValor ps, String cad, String tip){
		
		if(listDatosGeneradosVO!=null && listDatosGeneradosVO.size()>0){

			for(DatosGeneradosVO obj: listDatosGeneradosVO){
				if(obj!=null){
					if(ps!=null && ps.getValor()!=null && obj.getValor()!=null &&ps.getValor().trim().equals(obj.getValor().trim())){
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
			}			
		}
		return null;
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
	
	public List<PosibleValor> getListPosibleValor() {
		return listPosibleValor;
	}

	public void setListPosibleValor(List<PosibleValor> listPosibleValor) {
		this.listPosibleValor = listPosibleValor;
	}

	public List<DatosGeneradosVO> getListDatosGeneradosVO() {
		return listDatosGeneradosVO;
	}

	public void setListDatosGeneradosVO(List<DatosGeneradosVO> listDatosGeneradosVO) {
		this.listDatosGeneradosVO = listDatosGeneradosVO;
	}

	public static String getValorColum() {
		return valorColum;
	}

	public static void setValorColum(String valorColum) {
		TablaGenerarTOEMB.valorColum = valorColum;
	}

	public static String getValorColumPerfil() {
		return valorColumPerfil;
	}

	public static void setValorColumPerfil(String valorColumPerfil) {
		TablaGenerarTOEMB.valorColumPerfil = valorColumPerfil;
	}

	public boolean isViewTablaEspecifica() {
		return viewTablaEspecifica;
	}

	public void setViewTablaEspecifica(boolean viewTablaEspecifica) {
		this.viewTablaEspecifica = viewTablaEspecifica;
	}

	public boolean isViewTablaGeneral() {
		return viewTablaGeneral;
	}

	public void setViewTablaGeneral(boolean viewTablaGeneral) {
		this.viewTablaGeneral = viewTablaGeneral;
	}

	public List<DatosPorcentajeVO> getListDatosPorcentajeVO() {
		return listDatosPorcentajeVO;
	}

	public void setListDatosPorcentajeVO(List<DatosPorcentajeVO> listDatosPorcentajeVO) {
		this.listDatosPorcentajeVO = listDatosPorcentajeVO;
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

	public boolean isViewFlujoRegular() {
		return viewFlujoRegular;
	}

	public void setViewFlujoRegular(boolean viewFlujoRegular) {
		this.viewFlujoRegular = viewFlujoRegular;
	}

	public boolean isViewFlujoReproceso() {
		return viewFlujoReproceso;
	}

	public void setViewFlujoReproceso(boolean viewFlujoReproceso) {
		this.viewFlujoReproceso = viewFlujoReproceso;
	}

	public ListaReporteToe getObjListaReporteToe() {
		return objListaReporteToe;
	}

	public void setObjListaReporteToe(ListaReporteToe objListaReporteToe) {
		this.objListaReporteToe = objListaReporteToe;
	}

	public List<ListaReporteToe> getListReport_Prod_1_TipOferta_1_Flujo_1() {
		return listReport_Prod_1_TipOferta_1_Flujo_1;
	}

	public void setListReport_Prod_1_TipOferta_1_Flujo_1(
			List<ListaReporteToe> listReport_Prod_1_TipOferta_1_Flujo_1) {
		this.listReport_Prod_1_TipOferta_1_Flujo_1 = listReport_Prod_1_TipOferta_1_Flujo_1;
	}

	public List<ListaReporteToe> getListReport_Prod_1_TipOferta_1_Flujo_2() {
		return listReport_Prod_1_TipOferta_1_Flujo_2;
	}

	public void setListReport_Prod_1_TipOferta_1_Flujo_2(
			List<ListaReporteToe> listReport_Prod_1_TipOferta_1_Flujo_2) {
		this.listReport_Prod_1_TipOferta_1_Flujo_2 = listReport_Prod_1_TipOferta_1_Flujo_2;
	}

	public List<ListaReporteToe> getListReport_Prod_1_TipOferta_2_Flujo_1() {
		return listReport_Prod_1_TipOferta_2_Flujo_1;
	}

	public void setListReport_Prod_1_TipOferta_2_Flujo_1(
			List<ListaReporteToe> listReport_Prod_1_TipOferta_2_Flujo_1) {
		this.listReport_Prod_1_TipOferta_2_Flujo_1 = listReport_Prod_1_TipOferta_2_Flujo_1;
	}

	public List<ListaReporteToe> getListReport_Prod_1_TipOferta_2_Flujo_2() {
		return listReport_Prod_1_TipOferta_2_Flujo_2;
	}

	public void setListReport_Prod_1_TipOferta_2_Flujo_2(
			List<ListaReporteToe> listReport_Prod_1_TipOferta_2_Flujo_2) {
		this.listReport_Prod_1_TipOferta_2_Flujo_2 = listReport_Prod_1_TipOferta_2_Flujo_2;
	}

	public ListaReporteToeRol getObjlistaRoles() {
		return objlistaRoles;
	}

	public void setObjlistaRoles(ListaReporteToeRol objlistaRoles) {
		this.objlistaRoles = objlistaRoles;
	}

	public ListaReporteToePerfil getObjlistaPerfiles() {
		return objlistaPerfiles;
	}

	public void setObjlistaPerfiles(ListaReporteToePerfil objlistaPerfiles) {
		this.objlistaPerfiles = objlistaPerfiles;
	}

	public ArrayList<ListaReporteToeRol> getListaRoles() {
		return listaRoles;
	}

	public void setListaRoles(ArrayList<ListaReporteToeRol> listaRoles) {
		this.listaRoles = listaRoles;
	}

	public ArrayList<ListaReporteToePerfil> getListaPerfiles() {
		return listaPerfiles;
	}

	public void setListaPerfiles(ArrayList<ListaReporteToePerfil> listaPerfiles) {
		this.listaPerfiles = listaPerfiles;
	}

	public List<ListaReporteToe> getListReport_Prod_2_TipOferta_1_Flujo_1() {
		return listReport_Prod_2_TipOferta_1_Flujo_1;
	}

	public void setListReport_Prod_2_TipOferta_1_Flujo_1(
			List<ListaReporteToe> listReport_Prod_2_TipOferta_1_Flujo_1) {
		this.listReport_Prod_2_TipOferta_1_Flujo_1 = listReport_Prod_2_TipOferta_1_Flujo_1;
	}

	public List<ListaReporteToe> getListReport_Prod_2_TipOferta_1_Flujo_2() {
		return listReport_Prod_2_TipOferta_1_Flujo_2;
	}

	public void setListReport_Prod_2_TipOferta_1_Flujo_2(
			List<ListaReporteToe> listReport_Prod_2_TipOferta_1_Flujo_2) {
		this.listReport_Prod_2_TipOferta_1_Flujo_2 = listReport_Prod_2_TipOferta_1_Flujo_2;
	}

	public List<ListaReporteToe> getListReport_Prod_2_TipOferta_2_Flujo_1() {
		return listReport_Prod_2_TipOferta_2_Flujo_1;
	}

	public void setListReport_Prod_2_TipOferta_2_Flujo_1(
			List<ListaReporteToe> listReport_Prod_2_TipOferta_2_Flujo_1) {
		this.listReport_Prod_2_TipOferta_2_Flujo_1 = listReport_Prod_2_TipOferta_2_Flujo_1;
	}

	public List<ListaReporteToe> getListReport_Prod_2_TipOferta_2_Flujo_2() {
		return listReport_Prod_2_TipOferta_2_Flujo_2;
	}

	public void setListReport_Prod_2_TipOferta_2_Flujo_2(
			List<ListaReporteToe> listReport_Prod_2_TipOferta_2_Flujo_2) {
		this.listReport_Prod_2_TipOferta_2_Flujo_2 = listReport_Prod_2_TipOferta_2_Flujo_2;
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

	public List<ListaReportePorcentajeToe> getListPorc_Prod_1_TipOferta_1_Flujo_1() {
		return listPorc_Prod_1_TipOferta_1_Flujo_1;
	}

	public void setListPorc_Prod_1_TipOferta_1_Flujo_1(
			List<ListaReportePorcentajeToe> listPorc_Prod_1_TipOferta_1_Flujo_1) {
		this.listPorc_Prod_1_TipOferta_1_Flujo_1 = listPorc_Prod_1_TipOferta_1_Flujo_1;
	}

	public List<ListaReportePorcentajeToe> getListPorc_Prod_1_TipOferta_1_Flujo_2() {
		return listPorc_Prod_1_TipOferta_1_Flujo_2;
	}

	public void setListPorc_Prod_1_TipOferta_1_Flujo_2(
			List<ListaReportePorcentajeToe> listPorc_Prod_1_TipOferta_1_Flujo_2) {
		this.listPorc_Prod_1_TipOferta_1_Flujo_2 = listPorc_Prod_1_TipOferta_1_Flujo_2;
	}

	public List<ListaReportePorcentajeToe> getListPorc_Prod_1_TipOferta_2_Flujo_1() {
		return listPorc_Prod_1_TipOferta_2_Flujo_1;
	}

	public void setListPorc_Prod_1_TipOferta_2_Flujo_1(
			List<ListaReportePorcentajeToe> listPorc_Prod_1_TipOferta_2_Flujo_1) {
		this.listPorc_Prod_1_TipOferta_2_Flujo_1 = listPorc_Prod_1_TipOferta_2_Flujo_1;
	}

	public List<ListaReportePorcentajeToe> getListPorc_Prod_1_TipOferta_2_Flujo_2() {
		return listPorc_Prod_1_TipOferta_2_Flujo_2;
	}

	public void setListPorc_Prod_1_TipOferta_2_Flujo_2(
			List<ListaReportePorcentajeToe> listPorc_Prod_1_TipOferta_2_Flujo_2) {
		this.listPorc_Prod_1_TipOferta_2_Flujo_2 = listPorc_Prod_1_TipOferta_2_Flujo_2;
	}

	public List<ListaReportePorcentajeToe> getListPorc_Prod_2_TipOferta_1_Flujo_1() {
		return listPorc_Prod_2_TipOferta_1_Flujo_1;
	}

	public void setListPorc_Prod_2_TipOferta_1_Flujo_1(
			List<ListaReportePorcentajeToe> listPorc_Prod_2_TipOferta_1_Flujo_1) {
		this.listPorc_Prod_2_TipOferta_1_Flujo_1 = listPorc_Prod_2_TipOferta_1_Flujo_1;
	}

	public List<ListaReportePorcentajeToe> getListPorc_Prod_2_TipOferta_1_Flujo_2() {
		return listPorc_Prod_2_TipOferta_1_Flujo_2;
	}

	public void setListPorc_Prod_2_TipOferta_1_Flujo_2(
			List<ListaReportePorcentajeToe> listPorc_Prod_2_TipOferta_1_Flujo_2) {
		this.listPorc_Prod_2_TipOferta_1_Flujo_2 = listPorc_Prod_2_TipOferta_1_Flujo_2;
	}

	public List<ListaReportePorcentajeToe> getListPorc_Prod_2_TipOferta_2_Flujo_1() {
		return listPorc_Prod_2_TipOferta_2_Flujo_1;
	}

	public void setListPorc_Prod_2_TipOferta_2_Flujo_1(
			List<ListaReportePorcentajeToe> listPorc_Prod_2_TipOferta_2_Flujo_1) {
		this.listPorc_Prod_2_TipOferta_2_Flujo_1 = listPorc_Prod_2_TipOferta_2_Flujo_1;
	}

	public List<ListaReportePorcentajeToe> getListPorc_Prod_2_TipOferta_2_Flujo_2() {
		return listPorc_Prod_2_TipOferta_2_Flujo_2;
	}

	public void setListPorc_Prod_2_TipOferta_2_Flujo_2(
			List<ListaReportePorcentajeToe> listPorc_Prod_2_TipOferta_2_Flujo_2) {
		this.listPorc_Prod_2_TipOferta_2_Flujo_2 = listPorc_Prod_2_TipOferta_2_Flujo_2;
	}

	public String getMsjSinResultado() {
		return msjSinResultado;
	}

	public void setMsjSinResultado(String msjSinResultado) {
		this.msjSinResultado = msjSinResultado;
	}

	public String getTituloEspecifico() {
		return tituloEspecifico;
	}

	public void setTituloEspecifico(String tituloEspecifico) {
		this.tituloEspecifico = tituloEspecifico;
	}

	public List<DatosUnidadVO> getListDatosUnidadVO() {
		return listDatosUnidadVO;
	}

	public void setListDatosUnidadVO(List<DatosUnidadVO> listDatosUnidadVO) {
		this.listDatosUnidadVO = listDatosUnidadVO;
	}

	public List<ListaReporteUnidadToe> getListUnid_Prod_1_TipOferta_1_Flujo_1() {
		return listUnid_Prod_1_TipOferta_1_Flujo_1;
	}

	public void setListUnid_Prod_1_TipOferta_1_Flujo_1(
			List<ListaReporteUnidadToe> listUnid_Prod_1_TipOferta_1_Flujo_1) {
		this.listUnid_Prod_1_TipOferta_1_Flujo_1 = listUnid_Prod_1_TipOferta_1_Flujo_1;
	}

	public List<ListaReporteUnidadToe> getListUnid_Prod_1_TipOferta_1_Flujo_2() {
		return listUnid_Prod_1_TipOferta_1_Flujo_2;
	}

	public void setListUnid_Prod_1_TipOferta_1_Flujo_2(
			List<ListaReporteUnidadToe> listUnid_Prod_1_TipOferta_1_Flujo_2) {
		this.listUnid_Prod_1_TipOferta_1_Flujo_2 = listUnid_Prod_1_TipOferta_1_Flujo_2;
	}

	public List<ListaReporteUnidadToe> getListUnid_Prod_1_TipOferta_2_Flujo_1() {
		return listUnid_Prod_1_TipOferta_2_Flujo_1;
	}

	public void setListUnid_Prod_1_TipOferta_2_Flujo_1(
			List<ListaReporteUnidadToe> listUnid_Prod_1_TipOferta_2_Flujo_1) {
		this.listUnid_Prod_1_TipOferta_2_Flujo_1 = listUnid_Prod_1_TipOferta_2_Flujo_1;
	}

	public List<ListaReporteUnidadToe> getListUnid_Prod_1_TipOferta_2_Flujo_2() {
		return listUnid_Prod_1_TipOferta_2_Flujo_2;
	}

	public void setListUnid_Prod_1_TipOferta_2_Flujo_2(
			List<ListaReporteUnidadToe> listUnid_Prod_1_TipOferta_2_Flujo_2) {
		this.listUnid_Prod_1_TipOferta_2_Flujo_2 = listUnid_Prod_1_TipOferta_2_Flujo_2;
	}

	public List<ListaReporteUnidadToe> getListUnid_Prod_2_TipOferta_1_Flujo_1() {
		return listUnid_Prod_2_TipOferta_1_Flujo_1;
	}

	public void setListUnid_Prod_2_TipOferta_1_Flujo_1(
			List<ListaReporteUnidadToe> listUnid_Prod_2_TipOferta_1_Flujo_1) {
		this.listUnid_Prod_2_TipOferta_1_Flujo_1 = listUnid_Prod_2_TipOferta_1_Flujo_1;
	}

	public List<ListaReporteUnidadToe> getListUnid_Prod_2_TipOferta_1_Flujo_2() {
		return listUnid_Prod_2_TipOferta_1_Flujo_2;
	}

	public void setListUnid_Prod_2_TipOferta_1_Flujo_2(
			List<ListaReporteUnidadToe> listUnid_Prod_2_TipOferta_1_Flujo_2) {
		this.listUnid_Prod_2_TipOferta_1_Flujo_2 = listUnid_Prod_2_TipOferta_1_Flujo_2;
	}

	public List<ListaReporteUnidadToe> getListUnid_Prod_2_TipOferta_2_Flujo_1() {
		return listUnid_Prod_2_TipOferta_2_Flujo_1;
	}

	public void setListUnid_Prod_2_TipOferta_2_Flujo_1(
			List<ListaReporteUnidadToe> listUnid_Prod_2_TipOferta_2_Flujo_1) {
		this.listUnid_Prod_2_TipOferta_2_Flujo_1 = listUnid_Prod_2_TipOferta_2_Flujo_1;
	}

	public List<ListaReporteUnidadToe> getListUnid_Prod_2_TipOferta_2_Flujo_2() {
		return listUnid_Prod_2_TipOferta_2_Flujo_2;
	}

	public void setListUnid_Prod_2_TipOferta_2_Flujo_2(
			List<ListaReporteUnidadToe> listUnid_Prod_2_TipOferta_2_Flujo_2) {
		this.listUnid_Prod_2_TipOferta_2_Flujo_2 = listUnid_Prod_2_TipOferta_2_Flujo_2;
	}

	public String getRangoFechas() {
		return rangoFechas;
	}

	public void setRangoFechas(String rangoFechas) {
		this.rangoFechas = rangoFechas;
	}

	public String getPerfilEspecifico() {
		return PerfilEspecifico;
	}

	public void setPerfilEspecifico(String perfilEspecifico) {
		PerfilEspecifico = perfilEspecifico;
	}

	public String getTipoOfertaEspecifico() {
		return TipoOfertaEspecifico;
	}

	public void setTipoOfertaEspecifico(String tipoOfertaEspecifico) {
		TipoOfertaEspecifico = tipoOfertaEspecifico;
	}

	public String getProductoEspecifico() {
		return ProductoEspecifico;
	}

	public void setProductoEspecifico(String productoEspecifico) {
		ProductoEspecifico = productoEspecifico;
	}

	public String getTituloTabla9() {
		return tituloTabla9;
	}

	public void setTituloTabla9(String tituloTabla9) {
		this.tituloTabla9 = tituloTabla9;
	}

	public String getTituloTabla10() {
		return tituloTabla10;
	}

	public void setTituloTabla10(String tituloTabla10) {
		this.tituloTabla10 = tituloTabla10;
	}

	public String getRangoFechasFormato() {
		return rangoFechasFormato;
	}

	public void setRangoFechasFormato(String rangoFechasFormato) {
		this.rangoFechasFormato = rangoFechasFormato;
	}
	
	
	
	
	
}