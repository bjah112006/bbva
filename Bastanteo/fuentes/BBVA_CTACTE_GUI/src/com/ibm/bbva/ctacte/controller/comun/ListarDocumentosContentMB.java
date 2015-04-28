package com.ibm.bbva.ctacte.controller.comun;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clientecontent.ClienteContentCC;

import com.ibm.bbva.cm.service.Documento;
import com.ibm.bbva.cm.servicepid.DocumentoPid;
import com.ibm.bbva.ctacte.bean.DocumentoCM;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.dao.DocumentoDAO;
import com.ibm.bbva.ctacte.util.DateRange;
import com.ibm.bbva.ctacte.util.GenericPredicate;
import com.ibm.bbva.ctacte.util.ListSorter;
import com.ibm.bbva.ctacte.util.ParametrosSistema;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="listarDocumentosContentMB")
@ViewScoped
public class ListarDocumentosContentMB extends AbstractMBean {

	private static final long serialVersionUID = -6399120168925416691L;
	private static final Logger LOG = LoggerFactory.getLogger(ListarDocumentosContentMB.class);
	
	private String selFecha;
	private DateRange strFecha;
	private boolean disFiltrar;
	private String codCentral;
	private String numeroDoi;
	private String razonSocial;	
	private List<Documento> listaDocumentos;
	//private TreeNode root;
    //private TreeNode selectedNode;
    private int filtro;
    private String cadenaJson; //man1
    
	private List<DocumentoCM> listaCM;
    private List<DocumentoCM> lista2CM;
    
    private List<DocumentoPid> lista;
    private List<DocumentoPid> lista2;
    private int rowNum;
    
    @EJB
    private DocumentoDAO documentoDAO;
	
    
	 @PostConstruct
     public void iniciar () {
             LOG.info("**************ListarDocumentosContentMB----public void iniciar ()************");                
             Expediente exp = (Expediente) Util.getObjectSession(ConstantesAdmin.DATOS_CLIENTE_SESION);
             LOG.info("**************ListarDocumetosContentMB-iniciar()-expediente***********:"+exp);  
             String parametros = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("parametros");             
             //WEBCEService service = new WEBCEService();
             LOG.info("parametros : "+parametros);
             
             strFecha = new DateRange(null);
             if (parametros==null) {
            	if(exp!=null)
                {
            		this.setCodCentral(exp.getCliente().getCodigoCentral());
        	        this.setNumeroDoi(exp.getCliente().getNumeroDoi());
        	        this.setRazonSocial(exp.getCliente().getRazonSocial());
                 } else {
                	 //mensajeErrorPrincipal("idTablaDocumentos", "El cliente no existe en el HOST");
                	 mensajeErrorPrincipal("idTablaDocumentos", "No se ha podido obtener información del cliente");
                	 return;
                 }
    	             
              }else{ 
            	  LOG.info("**************ListarDocumetosContentMB-parametros***********:"+parametros.split(";")[0]); 
            	  LOG.info("**************ListarDocumetosContentMB-parametros***********:"+parametros.split(";")[1]); 
            	  LOG.info("**************ListarDocumetosContentMB-parametros***********:"+parametros.split(";")[2]); 
                  this.setCodCentral(parametros.split(";")[0]);
                  this.setNumeroDoi(parametros.split(";")[1]);
                  this.setRazonSocial(parametros.split(";")[2]);
               }
             
             try
             {
            	 LOG.info("**************TRY  -listarDocumentosContentMB- lista = service.CM_buscarVistaUnica(this.getCodCentral()) --**************");
            	 lista = CM_buscarVistaUnica(this.getCodCentral());
                 listaCM = Util.parseCollectionDocumentToDocumentoCM(lista);   
                 
                 Empleado empleadoSesion = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
                 /* Ya no es necesario que los gestores vean el visor avanzado porque ahora se 
                  * actualiza el PDF con las anotaciones al grabar el TIFF */
//         		 if(empleadoSesion.getPerfil().getCodigo().equalsIgnoreCase(ConstantesBusiness.CODIGO_PERFIL_GESTOR) ||
//         			empleadoSesion.getPerfil().getCodigo().equalsIgnoreCase(ConstantesBusiness.CODIGO_PERFIL_MESA_DOCUMENTOS) ||
//         			empleadoSesion.getPerfil().getCodigo().equalsIgnoreCase(ConstantesBusiness.CODIGO_PERFIL_MESA_FIRMAS) ||
//         			empleadoSesion.getPerfil().getCodigo().equalsIgnoreCase(ConstantesBusiness.CODIGO_PERFIL_ABOGADO_BASTANTEO) ||
//         			empleadoSesion.getPerfil().getCodigo().equalsIgnoreCase(ConstantesBusiness.CODIGO_PERFIL_ABOGADO_REVISION) ||
//         			empleadoSesion.getPerfil().getCodigo().equalsIgnoreCase(ConstantesBusiness.CODIGO_PERFIL_MIGRADOR)) {
         			filtro = 1;
         			final DocumentoCM nuevoFiltro = new DocumentoCM();
         			nuevoFiltro.setTipoArchivo("PDF");
         			CollectionUtils.filter(listaCM,	new GenericPredicate<DocumentoCM>(nuevoFiltro));
         			
         			//inicio - mod man1
         			this.setCadenaJson(toJsonJqGrid(listaCM, filtro));
         			//fin - mod man1
         			//root = com.ibm.bbva.ctacte.util.Util.getTreeNodeFromDocumentos(filtro,"codigoDocumento", listaCM, true,"fechaCreacion");
//                 }else{
//                	 filtro=2;
//               	    //root = com.ibm.bbva.ctacte.util.Util.getTreeNodeFromDocumentos(filtro,"codigoDocumento", listaCM, true,"fechaCreacion");
//                	 this.setCadenaJson(toJsonJqGrid(listaCM, filtro));
//                 }
         		this.selFecha = "1";
             }catch(Exception e){
            	 //redirectAction("../vistaUnica/vistaUnicaLista");
            	 LOG.error("**************CATCH -- listarDocumentosContentMB -iniciar() --**************", e);
            	 mensajeErrorPrincipal("idTablaDocumentos", "Ocurrió un error en la comunicación del servicio de Content...");

             }
     }

	public void filtrar (ActionEvent ae) {
		
		Date fechaDesde = strFecha.getLimInf();
		Date fechaHasta = null;
		Date fechaBusq = new Date();		
		
		if (strFecha.getLimSup()!=null) {
			Calendar fechaHastaSupC = Calendar.getInstance();				
			fechaHastaSupC.setTime(strFecha.getLimSup());
			fechaHastaSupC.set(Calendar.HOUR_OF_DAY, 23);
			fechaHastaSupC.set(Calendar.MINUTE, 59);
			fechaHastaSupC.set(Calendar.SECOND, 59);
			fechaHasta = fechaHastaSupC.getTime();
		}
		
		if (strFecha.getLimSup().compareTo(strFecha.getLimInf())<0) {
			mensajeErrorPrincipal("idFechas", "La fecha Desde debe ser menor que la fecha Hasta");
			return;
		}
		
		lista2CM = new ArrayList<DocumentoCM>();
		
		for (DocumentoCM doc : listaCM) {
			if (this.getSelFecha().equals("1")) {
				//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Fecha Creación");
				fechaBusq = doc.getFechaCreacion()==null?null:doc.getFechaCreacion().getTime();
			} else {
				//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Fecha Vigencia");				
				fechaBusq = doc.getFechaExpiracion()==null?null:doc.getFechaExpiracion();	
			}
			
			if (fechaBusq!=null &&
		       ((fechaDesde!=null && fechaHasta!=null && (fechaDesde.before(fechaBusq) || fechaDesde.equals(fechaBusq)) && (fechaHasta.after(fechaBusq) || fechaHasta.equals(fechaBusq))) ||
		        (fechaDesde!=null && fechaHasta!=null && fechaDesde.equals(fechaHasta) && fechaDesde.equals(fechaBusq)) ||
		        (fechaDesde!=null && fechaHasta==null && fechaDesde.before(fechaBusq)) ||
		        (fechaDesde==null && fechaHasta!=null && fechaHasta.after(fechaBusq)))	              
		       ) {
		        	//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Entroxxxxx : "+fechaBusq);	        	
		        	lista2CM.add(doc);
		    }			
		}
		//inicio - mod man1
		this.setCadenaJson(toJsonJqGrid(lista2CM, filtro));
		//fin - mod man1
		
		//root = com.ibm.bbva.ctacte.util.Util.getTreeNodeFromDocumentos(filtro,"codigoDocumento", lista2CM, true,"fechaCreacion");
	}
	
	private List<DocumentoPid> CM_buscarVistaUnica(String codCliente){
        try{               
    		LOG.info("***********TRY -WEBCEService-CM_buscarVistaUnica(String codCliente)***************"+codCliente);    
    		DocumentoPid documento = new DocumentoPid(); 
    		String url	= (String) ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).get(ConstantesParametros.SERVICIO_CONTENT_PID);
            ClienteContentCC clienteContent = new ClienteContentCC(url);
            documento.setCodCliente(codCliente);
            LOG.debug("Documento a buscar !!! : " + documento);
            
            DocumentoPid[] listTemporal = clienteContent.historialDocumentoxCliente(documento);
            LOG.info("listTemporal VISTA UNICA: " + listTemporal);
            List<DocumentoPid> listaDocs = Arrays.asList(listTemporal);
            ListSorter.ordenar(listaDocs, false, "tipo","fechaCreacion");
            return listaDocs;
                
        } catch (Exception ex) {
            LOG.error("***********CATCH  CM_buscarVistaUnica(String codCliente)************", ex);
            //throw new ServiceLocationException(e);
            return null;
        }
	}
	
	public String getSelFecha() {
		return selFecha;
	}

	public void setSelFecha(String selFecha) {
		this.selFecha = selFecha;
	}

	public DateRange getStrFecha() {
		return strFecha;
	}

	public void setStrFecha(DateRange strFecha) {
		this.strFecha = strFecha;
	}

	public boolean isDisFiltrar() {
		return disFiltrar;
	}

	public void setDisFiltrar(boolean disFiltrar) {
		this.disFiltrar = disFiltrar;
	}

	public String getCodCentral() {
		return codCentral;
	}

	public void setCodCentral(String codCentral) {
		this.codCentral = codCentral;
	}

	public String getNumeroDoi() {
		return numeroDoi;
	}

	public void setNumeroDoi(String numeroDoi) {
		this.numeroDoi = numeroDoi;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public List<Documento> getListaDocumentos() {
		return listaDocumentos;
	}

	public void setListaDocumentos(List<Documento> listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}

	public List<DocumentoCM> getListaCM() {
		return listaCM;
	}

	public void setListaCM(List<DocumentoCM> listaCM) {
		this.listaCM = listaCM;
	}

	public List<DocumentoCM> getLista2CM() {
		return lista2CM;
	}

	public void setLista2CM(List<DocumentoCM> lista2CM) {
		this.lista2CM = lista2CM;
	}

	public List<DocumentoPid> getLista() {
		return lista;
	}

	public void setLista(List<DocumentoPid> lista) {
		this.lista = lista;
	}

	public List<DocumentoPid> getLista2() {
		return lista2;
	}

	public void setLista2(List<DocumentoPid> lista2) {
		this.lista2 = lista2;
	}
	
    public String getCadenaJson() {
		return cadenaJson;
	}
	public void setCadenaJson(String cadenaJson) {
		this.cadenaJson = cadenaJson;
	}
	
	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public String toJsonJqGrid(List<DocumentoCM> lista, int tipoConsulta){		
		int contador = 0;
		String jsonJqGrid = "[";
		LOG.info("lista.size(): "+lista.size());
		for(int i=0; i<lista.size(); i++){
			com.ibm.bbva.ctacte.bean.Documento documento = documentoDAO.findByCodigo(lista.get(i).getCodigoDocumento());
			LOG.info("CODIGO DOCUMENTO:"+lista.get(i).getCodigoDocumento());
			LOG.info("TIPO ARCHIVO:"+lista.get(i).getTipoArchivo());
			LOG.info("TIPO VISOR:"+documento.getTipoVisor());
			if (tipoConsulta == 2) {
				if (documento.getTipoVisor() != null
						&& documento.getTipoVisor().equals("A")) {
					if (!lista.get(i).getTipoArchivo().equals("TIF")) {
						continue;
					}
				}
			}
			String rutaCM = lista.get(i).getRutaCM();
			if (lista.get(i).getTipoArchivo() != null) {
				if (lista.get(i).getTipoArchivo().equals("TIF")) {
					rutaCM = ConstantesBusiness.RUTA_CM_VISOR + lista.get(i).getPid() + "&type=" + lista.get(i).getCodigoDocumento();
				}
			}
			if (!jsonJqGrid.equals("[")) {
				jsonJqGrid = jsonJqGrid + ",";
			}
    		
			jsonJqGrid = jsonJqGrid + "[" + '"'+ documento.getDescripcion() + '"' + "," + 
			'"'+lista.get(i).getNombreArchivo() + '"' + "," 
			+ '"'+FechaToString(lista.get(i).getFechaCreacion()) + '"'  + "," 
			+ '"'+FechaToString(lista.get(i).getFechaExpiracion()) + '"' + "," 
			+ '"'+rutaCM + '"' + "," 
			+ '"'+lista.get(i).getTipoArchivo() + '"'
			+ "]";
			
			contador++;
		}
		jsonJqGrid = jsonJqGrid + "]";
		this.rowNum = contador;
		
		return jsonJqGrid;
	}
	
	public String FechaToString(Object fecha){
		DateFormat df = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
		String strFecha = " ";
		if(fecha != null){
			if(fecha instanceof Date){
				strFecha = df.format(((Date) fecha).getTime());
			}else if(fecha instanceof Calendar){
				strFecha = df.format(((Calendar) fecha).getTime());
			}
		}
		return strFecha;		
	}
	
}
