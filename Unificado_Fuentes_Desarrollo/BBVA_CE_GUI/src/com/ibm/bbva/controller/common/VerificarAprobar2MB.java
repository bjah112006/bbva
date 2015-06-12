package com.ibm.bbva.controller.common;

import java.util.ArrayList;
import java.util.List;

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
import com.ibm.bbva.controller.form.RegistrarExpedienteMB;
import com.ibm.bbva.controller.form.VerificarResultadoDomiciliariaMB;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.TipoVerificacion;
import com.ibm.bbva.entities.VerificacionExp;
import com.ibm.bbva.session.VerificacionExpBeanLocal;

@SuppressWarnings("serial")
@ManagedBean(name = "verificarAprobar2")
@RequestScoped
public class VerificarAprobar2MB extends AbstractMBean {

	@EJB
	VerificacionExpBeanLocal verificacionExpBean; 
	
	private VerificacionExp verificacionExp;
	private Expediente expediente;
	
	private List<String> selectedItems;
	private List<SelectItem> listaResultadoVerificacion;
	private List<VerificacionExp> listVerificacionExp;
		
	private String selectedItemsResultVl1;
	private String selectedItemsResultVl2;
	private String selectedItemsResultVl3;
	private boolean selectedItemVl1;
	private boolean selectedItemVl2;
	private boolean selectedItemVl3;

	private String selectedItemsResultVd1;
	private String selectedItemsResultVd2;
	private String selectedItemsResultVd3;
	private boolean selectedItemVd1;
	private boolean selectedItemVd2;
	private boolean selectedItemVd3;
	
	private String selectedItemsResultVdps1;
	private String selectedItemsResultVdps2;
	private String selectedItemsResultVdps3;
	private boolean selectedItemVdps1;
	private boolean selectedItemVdps2;
	private boolean selectedItemVdps3;
	
	private boolean selectedItemZp;
	
	private boolean habCheckVl1;
	private boolean habCheckVl2;
	private boolean habCheckVl3;
	private boolean habCheckVd1;
	private boolean habCheckVd2;
	private boolean habCheckVd3;
	private boolean habCheckVdps1;
	private boolean habCheckVdps2;
	private boolean habCheckVdps3;	
	
	private boolean habSelVl1;
	private boolean habSelVl2;
	private boolean habSelVl3;
	private boolean habSelVd1;
	private boolean habSelVd2;
	private boolean habSelVd3;
	private boolean habSelVdps1;
	private boolean habSelVdps2;
	private boolean habSelVdps3;	
	
	private boolean habZonaPl;
	
	private String veriResObs;   
	
	private Long idVl1;
	private Long idVl2;
	private Long idVl3;
	private Long idVd1;
	private Long idVd2;
	private Long idVd3;
	private Long idVdps1;
	private Long idVdps2;
	private Long idVdps3;	
	private Long idZp;
	
	private static final Logger LOG = LoggerFactory.getLogger(VerificarAprobar2MB.class);
	
	public VerificarAprobar2MB() {	
		
	}	
	
	@PostConstruct
    public void init() {
		/*Obtiene Datos de Expediente*/
		expediente = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		obtenerDatos();		
		this.setVeriResObs(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID);
		
		ocultaBotones(null);
	}
	
	private void obtenerDatos() {
		 
		 int i =1, j=1, k=1;
		 SelectItem selectItem;
		 listaResultadoVerificacion = new ArrayList();
		 selectItem = new SelectItem(Constantes.RESULTADO_TIPO_VERIFICACION_SELECCIONE_ID, Constantes.RESULTADO_TIPO_VERIFICACION_SELECCIONE);
		 listaResultadoVerificacion.add(selectItem);
		 selectItem = new SelectItem(Constantes.RESULTADO_TIPO_VERIFICACION_APROBADO_ID, Constantes.RESULTADO_TIPO_VERIFICACION_APROBADO);
		 listaResultadoVerificacion.add(selectItem);
		 selectItem = new SelectItem(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID,Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO);		
		 listaResultadoVerificacion.add(selectItem);
		 
		 /*Obtener Datos de Verificacion expediente*/
		 listVerificacionExp = verificacionExpBean.buscarPorExpediente(expediente.getId());
		 
		 
		 for (VerificacionExp list :listVerificacionExp) {			 
			 if(String.valueOf(list.getTipoVerificacion().getId()).equals(Constantes.CODIGO_VERIFICACION_LABORAL )) {				 
				 if (i == 1) {
					selectedItemVl1 = true;
					selectedItemsResultVl1 = list.getFlagVerif();
					setIdVl1(list.getId());
					
				} else if (i == 2) {
					selectedItemVl2 = true;
					selectedItemsResultVl2 = list.getFlagVerif();
					setIdVl2(list.getId());
					
				} else if (i == 3) {
					selectedItemVl3 = true;
					selectedItemsResultVl3 = list.getFlagVerif();
					setIdVl3(list.getId());
				}
				i++;				
			 }
             if(String.valueOf(list.getTipoVerificacion().getId()).equals(Constantes.CODIGO_VERIFICACION_DOMICILIARIA )) {            	 
				 if (j == 1) {
						selectedItemVd1 = true;
						selectedItemsResultVd1 = list.getFlagVerif();
						setIdVd1(list.getId());
						
					} else if (j == 2) {
						selectedItemVd2 = true;
						selectedItemsResultVd2 = list.getFlagVerif();
						setIdVd2(list.getId());
						
					} else if (j == 3) {
						selectedItemVd3 = true;
						selectedItemsResultVd3 = list.getFlagVerif();
						setIdVd3(list.getId());
						
					}
					j++;					
			 }
             if(String.valueOf(list.getTipoVerificacion().getId()).equals(Constantes.CODIGO_ZONA_PELIGROSA )) {            	 
            	 selectedItemZp = true;
         		setIdZp(list.getId());
			 }
             if(String.valueOf(list.getTipoVerificacion().getId()).equals(Constantes.CODIGO_VERIFICACION_DPS) ){
            	 if (k == 1) {
            		 selectedItemVdps1 = true;
            		 selectedItemsResultVdps1 = list.getFlagVerif();
            		 setIdVdps1(list.getId());
            	 } else if(k == 2) {
            		 selectedItemVdps2 = true;
            		 selectedItemsResultVdps2 = list.getFlagVerif();
            		 setIdVdps2(list.getId());
            	 } else if(k == 3) {
            		 selectedItemVdps3 = true;
            		 selectedItemsResultVdps3 = list.getFlagVerif();
            		 setIdVdps3(list.getId());
            	 }
            	 k++;
             }             
		 }
		 
		 if (String.valueOf(expediente.getEstado().getId()).equals(Constantes.CODIGO_EXPEDIENTE_SUBSANADO)){
			 deshabilitaCampos();
		 }else if(String.valueOf(expediente.getEstado().getId()).equals(Constantes.CODIGO_EN_ESPERA_RESPUESTA_TAREA5) ||
				 String.valueOf(expediente.getEstado().getId()).equals(Constantes.CODIGO_EN_ESPERA_RESPUESTA_TAREA26)) {
			 habilitaEnEspera();			 
		 }else{			
			 habilitaOtrosEstados();			 
		 }
	}
	
	public boolean esValido () {
		String jspPrinc = getNombreJSPPrincipal();
		String idComponente = "";
		if (jspPrinc.equals("formVerificarResultadoDomiciliaria")) {
			idComponente = "frmVerificarResultadoDomiciliaria";
		} else if (jspPrinc.equals("formSolicitarVerificacionDomiciliaria")) {
			idComponente = "frmSolicitarVerificacionDomiciliaria";
		}
		
		boolean existeError = false;
		
		LOG.info("expediente.getEstado().getId():::"+expediente.getEstado().getId());
		if(String.valueOf(expediente.getEstado().getId()).equals(Constantes.CODIGO_EN_ESPERA_RESPUESTA_TAREA5) ||
				 String.valueOf(expediente.getEstado().getId()).equals(Constantes.CODIGO_EN_ESPERA_RESPUESTA_TAREA26)) {
		
			/*Verificacion Laboral */
			if (this.isHabCheckVl1() && selectedItemVl1 && (selectedItemsResultVl1==null || selectedItemsResultVl1.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))) {
				addMessageError(idComponente + ":selectedItemVl1", 
						"com.ibm.bbva.common.verificarAprobar2.msg.laboral");
				existeError = true;
			}
			
			if (this.isHabCheckVl2() && selectedItemVl2 && (selectedItemsResultVl2==null || selectedItemsResultVl2.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))) {
				addMessageError(idComponente + ":selectedItemVl2", 
						"com.ibm.bbva.common.verificarAprobar2.msg.laboral");
				existeError = true;
			}
			
			if (this.isHabCheckVl3() && selectedItemVl3 && (selectedItemsResultVl3==null || selectedItemsResultVl3.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))) {
				addMessageError(idComponente + ":selectedItemVl3", 
						"com.ibm.bbva.common.verificarAprobar2.msg.laboral");
				existeError = true;
			}
			
			/*Verificacion Domiciliaria */
			if (this.isHabCheckVd1() && selectedItemVd1 && (selectedItemsResultVd1==null || selectedItemsResultVd1.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))) {
				addMessageError(idComponente + ":selectedItemVd1", 
						"com.ibm.bbva.common.verificarAprobar2.msg.domiciliaria");
				existeError = true;
			}
			
			if (this.isHabCheckVd2() && selectedItemVd2 && (selectedItemsResultVd2==null || selectedItemsResultVd2.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))) {
				addMessageError(idComponente + ":selectedItemVd2", 
						"com.ibm.bbva.common.verificarAprobar2.msg.domiciliaria");
				existeError = true;
			}
			
			if (this.isHabCheckVd3() && selectedItemVd3 && (selectedItemsResultVd3==null || selectedItemsResultVd3.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))) {
				addMessageError(idComponente + ":selectedItemVd3", 
						"com.ibm.bbva.common.verificarAprobar2.msg.domiciliaria");
				existeError = true;
			}
			
			/*verificacion DPS*/
			if (isProductoPld() == true) {
				
				if (this.isHabCheckVdps1() && selectedItemVdps1 && (selectedItemsResultVdps1==null || selectedItemsResultVdps1.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))) {
					addMessageError(idComponente + ":selectedItemVdps1",
						"com.ibm.bbva.common.verificarAprobar2.msg.dps");
					existeError = true;
				}
				
				if (this.isHabCheckVdps2() && selectedItemVdps2 && (selectedItemsResultVdps2==null || selectedItemsResultVdps2.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))) {
					addMessageError(idComponente + ":selectedItemVdps2",
						"com.ibm.bbva.common.verificarAprobar2.msg.dps");
					existeError = true;
				}
				
				if (this.isHabCheckVdps3() && selectedItemVdps3 && (selectedItemsResultVdps3==null || selectedItemsResultVdps3.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))) {
					addMessageError(idComponente + ":selectedItemVdps3",
						"com.ibm.bbva.common.verificarAprobar2.msg.dps");
					existeError = true;
				}
			}			
		}		
		return !existeError;
	}
	
	public boolean isProductoPld() {
		String lsGetIdProd="", lsAplPld="";
		lsGetIdProd = String.valueOf(expediente.getProducto().getId());
		lsAplPld = String.valueOf(Constantes.ID_APLICATIVO_PLD);
		
		if (lsGetIdProd.equals(lsAplPld)) {
			return true;
		}else {
			return false;
		}
	}	
	
	public List<VerificacionExp> cargarListaVerificaciones() {
		VerificacionExp verificacionExp;
		List<VerificacionExp> lista = new ArrayList<VerificacionExp>();
		
		/*Verificacion Laboral*/		
    	if (selectedItemVl1) {
    		verificacionExp = new VerificacionExp();
    		verificacionExp.setExpediente(new Expediente());
    		verificacionExp.setTipoVerificacion(new TipoVerificacion());
   		    verificacionExp.setId(getIdVl1()!=null?getIdVl1():0);
   		 //LOG.info("getIdVl1()");
   		 //LOG.info("getIdVl1()="+getIdVl1()==null?0:getIdVl1());
    		verificacionExp.setFlagVerif(!validaSeleccion(selectedItemsResultVl1)?null:selectedItemsResultVl1);
    		verificacionExp.setExpediente(expediente);
    		verificacionExp.getTipoVerificacion().setId(Long.parseLong(Constantes.CODIGO_VERIFICACION_LABORAL ));
			lista.add(verificacionExp);
		}		
		if (selectedItemVl2) {
			verificacionExp = new VerificacionExp();
    		verificacionExp.setExpediente(new Expediente());
    		verificacionExp.setTipoVerificacion(new TipoVerificacion());			
			verificacionExp.setId(getIdVl2()!=null?getIdVl2():0);
	   	//LOG.info("getIdVl2()");
	   	//LOG.info("getIdVl2()="+getIdVl2()==null?0:getIdVl2());
			verificacionExp.setFlagVerif(!validaSeleccion(selectedItemsResultVl2)?null:selectedItemsResultVl2);
			verificacionExp.setExpediente(expediente);
			verificacionExp.getTipoVerificacion().setId(Long.parseLong(Constantes.CODIGO_VERIFICACION_LABORAL ));
			lista.add(verificacionExp);
		}		
		if (selectedItemVl3) {
			verificacionExp = new VerificacionExp();
    		verificacionExp.setExpediente(new Expediente());
    		verificacionExp.setTipoVerificacion(new TipoVerificacion());			
			verificacionExp.setId(getIdVl3()!=null?getIdVl3():0);
		   	//LOG.info("getIdVl3()");
		   	//LOG.info("getIdVl3()="+getIdVl3()==null?0:getIdVl3());			
			verificacionExp.setFlagVerif(!validaSeleccion(selectedItemsResultVl3)?null:selectedItemsResultVl3);
			verificacionExp.setExpediente(expediente);
			verificacionExp.getTipoVerificacion().setId(Long.parseLong(Constantes.CODIGO_VERIFICACION_LABORAL ));
			lista.add(verificacionExp);
		}
		
		/*Verificacion Domiciliaria*/
		if (selectedItemVd1) {
			verificacionExp = new VerificacionExp();
    		verificacionExp.setExpediente(new Expediente());
    		verificacionExp.setTipoVerificacion(new TipoVerificacion());			
			verificacionExp.setId(getIdVd1()!=null?getIdVd1():0);
		   	LOG.info("getIdVd1()");
		   //	LOG.info("getIdVd1()="+getIdVd1()!=null?getIdVd1():0);			
			verificacionExp.setFlagVerif(!validaSeleccion(selectedItemsResultVd1)?null:selectedItemsResultVd1);
			verificacionExp.setExpediente(expediente);
			verificacionExp.getTipoVerificacion().setId(Long.parseLong(Constantes.CODIGO_VERIFICACION_DOMICILIARIA ));
			lista.add(verificacionExp);
		}
		
		if (selectedItemVd2) {
			verificacionExp = new VerificacionExp();
    		verificacionExp.setExpediente(new Expediente());
    		verificacionExp.setTipoVerificacion(new TipoVerificacion());			
			verificacionExp.setId(getIdVd2()!=null?getIdVd2():0);
		   	LOG.info("getIdVd2()");
		   	LOG.info("getIdVd2()="+(getIdVd2()==null?0:getIdVd2()));			
			verificacionExp.setFlagVerif(!validaSeleccion(selectedItemsResultVd2)?null:selectedItemsResultVd2);
			verificacionExp.setExpediente(expediente);
			verificacionExp.getTipoVerificacion().setId(Long.parseLong(Constantes.CODIGO_VERIFICACION_DOMICILIARIA ));
			lista.add(verificacionExp);
		}
		
		if (selectedItemVd3) {
			verificacionExp = new VerificacionExp();
    		verificacionExp.setExpediente(new Expediente());
    		verificacionExp.setTipoVerificacion(new TipoVerificacion());			
			verificacionExp.setId(getIdVd3()!=null?getIdVd3():0);
		   	LOG.info("getIdVd3()");
		   	LOG.info("getIdVd3()="+(getIdVd3()==null?0:getIdVd3()));			
			verificacionExp.setFlagVerif(!validaSeleccion(selectedItemsResultVd3)?null:selectedItemsResultVd3);
			verificacionExp.setExpediente(expediente);
			verificacionExp.getTipoVerificacion().setId(Long.parseLong(Constantes.CODIGO_VERIFICACION_DOMICILIARIA ));
			lista.add(verificacionExp);
		}
		
		/*Verificacion Zona Peligrosa*/
		if(selectedItemZp) {
			verificacionExp = new VerificacionExp();
    		verificacionExp.setExpediente(new Expediente());
    		verificacionExp.setTipoVerificacion(new TipoVerificacion());			
			verificacionExp.setId(getIdZp()!=null?getIdZp():0);
		   	LOG.info("getIdZp()");
		   	LOG.info("getIdZp()="+(getIdZp()==null?0:getIdZp()));			
			verificacionExp.setFlagVerif("0");
			verificacionExp.setExpediente(expediente);
			verificacionExp.getTipoVerificacion().setId(Long.parseLong(Constantes.CODIGO_ZONA_PELIGROSA ));
		  lista.add(verificacionExp);
		}
		
		/*Verificación Dps*/
		if (isProductoPld() == true) {
			if(selectedItemVdps1) {
				   verificacionExp = new VerificacionExp();
				   verificacionExp.setExpediente(new Expediente());
				   verificacionExp.setTipoVerificacion(new TipoVerificacion());
				   verificacionExp.setId(getIdVdps1()!=null?getIdVdps1():0);
				   LOG.info("getIdVdps1()");
				   LOG.info("getIdVdps1()="+(getIdVdps1()==null?0:getIdVdps1()));
				   verificacionExp.setFlagVerif(!validaSeleccion(selectedItemsResultVdps1)?null:selectedItemsResultVdps1);
				   verificacionExp.setExpediente(expediente);
				   verificacionExp.getTipoVerificacion().setId(Long.parseLong(Constantes.CODIGO_VERIFICACION_DPS));
				   lista.add(verificacionExp);
				}

			if(selectedItemVdps2) {
				verificacionExp = new VerificacionExp();
				verificacionExp.setExpediente(new Expediente());
				verificacionExp.setTipoVerificacion(new TipoVerificacion());
				verificacionExp.setId(getIdVdps2()!=null?getIdVdps2():0);
				LOG.info("getIdVdps2()");
				LOG.info("getIdVdps2()="+(getIdVdps2()==null?0:getIdVdps2()));
				verificacionExp.setFlagVerif(!validaSeleccion(selectedItemsResultVdps2)?null:selectedItemsResultVdps2);
				verificacionExp.setExpediente(expediente);
				verificacionExp.getTipoVerificacion().setId(Long.parseLong(Constantes.CODIGO_VERIFICACION_DPS));
				lista.add(verificacionExp);
			}
			
			if(selectedItemVdps3) {
				verificacionExp = new VerificacionExp();
				verificacionExp.setExpediente(new Expediente());
				verificacionExp.setTipoVerificacion(new TipoVerificacion());
				verificacionExp.setId(getIdVdps3()!=null?getIdVdps3():0);
				LOG.info("getIdVdps3()");
				LOG.info("getIdVdps3()="+(getIdVdps3()==null?0:getIdVdps3()));
				verificacionExp.setFlagVerif(!validaSeleccion(selectedItemsResultVdps3)?null:selectedItemsResultVdps3);
				verificacionExp.setExpediente(expediente);
				verificacionExp.getTipoVerificacion().setId(Long.parseLong(Constantes.CODIGO_VERIFICACION_DPS));
				lista.add(verificacionExp);
			}
		}
		
		return lista;		
	}
	
	public void deshabilitaCampos() {
		this.setHabCheckVl1(true);
		this.setHabCheckVl2(true);
		this.setHabCheckVl3(true);
		this.setHabCheckVd1(true);
		this.setHabCheckVd2(true);
		this.setHabCheckVd3(true);
		this.setHabCheckVdps1(true);
		this.setHabCheckVdps2(true);
		this.setHabCheckVdps3(true);		
		this.setHabSelVl1(true);
		this.setHabSelVl2(true);
		this.setHabSelVl3(true);
		this.setHabSelVd1(true);
		this.setHabSelVd2(true);
		this.setHabSelVd3(true);
		this.setHabZonaPl(true);
		this.setHabSelVdps1(true);
		this.setHabSelVdps2(true);
		this.setHabSelVdps3(true);		
	}
	
	public void habilitaEnEspera() {
		 deshabilitaCampos();
		 if (selectedItemVl1) {
			 if(selectedItemsResultVl1==null || selectedItemsResultVl1.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {		 
		        this.setHabSelVl1(false);
			 }
		 } else{
			 this.setHabCheckVl1(false);
		 }
		 if (selectedItemVl2 && (selectedItemsResultVl2==null || selectedItemsResultVl2.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))){
			 this.setHabSelVl2(false);			 
	     }
		 if (selectedItemVl3 && (selectedItemsResultVl3==null || selectedItemsResultVl3.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))){
		     this.setHabSelVl3(false);		     
	     }
		 
		 if (selectedItemVd1){
			 if (selectedItemsResultVd1==null || selectedItemsResultVd1.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {		 
		        this.setHabSelVd1(false);
			 }
		 }else{
			 this.setHabCheckVd1(false);	
		 }
		 if (selectedItemVd2 && (selectedItemsResultVd2==null || selectedItemsResultVd2.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))){
			 this.setHabSelVd2(false);						
	     }
		 if (selectedItemVd3 && (selectedItemsResultVd3==null || selectedItemsResultVd3.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))){
		     this.setHabSelVd3(false);		     
	     }											
		 this.setHabZonaPl(false);
		 
		 if (selectedItemVdps1) {
			 if (selectedItemsResultVdps1==null || selectedItemsResultVdps1.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO)) {
				 this.setHabSelVdps1(false);
			 }
		 }else {
			 this.setHabCheckVdps1(false);
		 }
		 if (selectedItemVdps2 && (selectedItemsResultVdps2==null || selectedItemsResultVdps2.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))) {
			 this.setHabSelVdps2(false);
		 }
		 if (selectedItemVdps3 && (selectedItemsResultVdps3==null || selectedItemsResultVdps3.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO))) {
			 this.setHabSelVdps3(false);
		 }		 
	}
	
	public void habilitaOtrosEstados() {
		 deshabilitaCampos();
		 //LOG.info("selectedItemVl1: "+selectedItemVl1+" selectedItemVl2: "+selectedItemVl2+" selectedItemVl3: "+selectedItemVl3);
		 //LOG.info("selectedItemVd1: "+selectedItemVd1+" selectedItemVd2: "+selectedItemVd2+" selectedItemVd3: "+selectedItemVd3);
		 if (!selectedItemVl1){
		     this.setHabCheckVl1(false);
		     this.selectedItemVl1 = expediente.getExpedienteTC().getVerifLab().equals(Constantes.CHECK_SELECCIONADO) ? true : false;
		 }
		 /*if (!selectedItemVl2 && selectedItemVl1){
			 this.setHabCheckVl2(false);
	     }
		 if (!selectedItemVl3 && selectedItemVl2){
			 this.setHabCheckVl3(false);	     
	     }*/
		 
		 if (!selectedItemVd1){
			 this.setHabCheckVd1(false);	     
		     this.selectedItemVd1 = expediente.getExpedienteTC().getVerifDom().equals(Constantes.CHECK_SELECCIONADO) ? true : false;
		 }
		 /*if (!selectedItemVd2 && selectedItemVd1){
			 this.setHabCheckVd2(false);						
	     }
		 if (!selectedItemVd3 && selectedItemVd2){
			 this.setHabCheckVd3(false);	     
	     }*/							
		 this.setHabZonaPl(false);
		 
		 if (!selectedItemVdps1) {
			 this.setHabCheckVdps1(false);
			 this.selectedItemVdps1 = expediente.getExpedienteTC().getVerifDps().equals(Constantes.CHECK_SELECCIONADO) ? true : false;
		 }		 
	}
		
	public boolean validaSeleccion(String seleccion) {
		return seleccion==null || seleccion.equals(Constantes.CODIGO_CODIGO_CAMPO_VACIO) ? false : true;
	}
	
	public void copiarDatosExpediente () {
		LOG.info("habCheckVl1::::::"+habCheckVl1);
		LOG.info("habCheckVl2::::::"+habCheckVl2);
		LOG.info("habCheckVl3::::::"+habCheckVl3);
		LOG.info("habCheckVd1::::::"+habCheckVd1);
		LOG.info("habCheckVd2::::::"+habCheckVd2);
		LOG.info("habCheckVd3::::::"+habCheckVd3);
		LOG.info("habCheckVdps1::::::"+habCheckVdps1);
		LOG.info("habCheckVdps2::::::"+habCheckVdps2);
		LOG.info("habCheckVdps3::::::"+habCheckVdps3);
		
		if(!habCheckVl1 || !habCheckVd1 || !habCheckVdps1){
			expediente.getExpedienteTC().setVerifLab(this.selectedItemVl1 ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
			expediente.getExpedienteTC().setVerifDom(this.selectedItemVd1 ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
			expediente.getExpedienteTC().setVerifDps(this.selectedItemVdps1 ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
		}
		if(!habCheckVl2 || !habCheckVd2 || !habCheckVdps2){
			expediente.getExpedienteTC().setVerifLab(this.selectedItemVl2 ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
			expediente.getExpedienteTC().setVerifDom(this.selectedItemVd2 ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
			expediente.getExpedienteTC().setVerifDps(this.selectedItemVdps2 ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
		}
		if(!habCheckVl3 || !habCheckVd3 || !habCheckVdps3){
			expediente.getExpedienteTC().setVerifLab(this.selectedItemVl3 ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
			expediente.getExpedienteTC().setVerifDom(this.selectedItemVd3 ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
			expediente.getExpedienteTC().setVerifDps(this.selectedItemVdps3 ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
		}

		
		expediente.getExpedienteTC().setZonaPel(this.selectedItemZp ? Constantes.CHECK_SELECCIONADO : Constantes.CHECK_NO_SELECCIONADO);
	}
	
	
	public void ocultaBotones(AjaxBehaviorEvent event) {
        String jspPrinc = getNombreJSPPrincipal();		
		if (jspPrinc.equals("formVerificarResultadoDomiciliaria")) {
			FacesContext ctx = FacesContext.getCurrentInstance();  
			VerificarResultadoDomiciliariaMB verificarResultadoDomiciliaria = (VerificarResultadoDomiciliariaMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarResultadoDomiciliaria");
			
			LOG.info("VERIFICACION_DOMICILIARIA: "+getObjectSession(Constantes.VERIFICACION_DOMICILIARIA));
			LOG.info("VERIFICACION_LABORAL: "+getObjectSession(Constantes.VERIFICACION_LABORAL));

			if(getObjectSession(Constantes.VERIFICACION_DOMICILIARIA)!=null ){
				if(getObjectSession(Constantes.VERIFICACION_DOMICILIARIA).equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID)){
					selectedItemsResultVd3=(String)getObjectSession(Constantes.VERIFICACION_DOMICILIARIA);	
					removeObjectSession(Constantes.VERIFICACION_DOMICILIARIA);
					LOG.info("VERIFICACION_DOMICILIARIA.EXISTE VALOR PARA ESTABLECEER CANCELAR de VENTANA RECHAZO: "+selectedItemsResultVd3);
				}
				
			}			
			if(getObjectSession(Constantes.VERIFICACION_LABORAL)!=null ){
				if(getObjectSession(Constantes.VERIFICACION_LABORAL).equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID)){
				selectedItemsResultVl3=(String)getObjectSession(Constantes.VERIFICACION_LABORAL);
				removeObjectSession(Constantes.VERIFICACION_LABORAL);

				LOG.info("VERIFICACION_LABORAL.EXISTE VALOR PARA ESTABLECEER CANCELAR deVENTANA RECHAZO: "+selectedItemsResultVl3);
				}
			}
			if ((this.selectedItemsResultVl1!=null && this.selectedItemsResultVl1.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID) &&
				 this.selectedItemsResultVl2!=null && this.selectedItemsResultVl2.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID) &&
				 this.selectedItemsResultVl3!=null && this.selectedItemsResultVl3.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID)) ||
				
				(this.selectedItemsResultVd1!=null && this.selectedItemsResultVd1.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID) &&
				 this.selectedItemsResultVd2!=null && this.selectedItemsResultVd2.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID) &&
				 this.selectedItemsResultVd3!=null && this.selectedItemsResultVd3.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID)) ||
				 
				 (this.selectedItemsResultVdps1!=null && this.selectedItemsResultVdps1.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID) &&
				  this.selectedItemsResultVdps2!=null && this.selectedItemsResultVdps2.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID) &&
				  this.selectedItemsResultVdps3!=null && this.selectedItemsResultVdps3.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID)) ) {
				
				verificarResultadoDomiciliaria.setRenderedResolver(true);
				verificarResultadoDomiciliaria.setRenderedDevolver(true);
				verificarResultadoDomiciliaria.setRenderedEnEspera(true);
				verificarResultadoDomiciliaria.setRenderedObs(true);
				verificarResultadoDomiciliaria.setRenderedRechazar(false);
				verificarResultadoDomiciliaria.setRenderedNoConforme(true);	
				verificarResultadoDomiciliaria.obtenerOpcionBotonesNoConforme();
			}else{
				verificarResultadoDomiciliaria.setRenderedResolver(false);
				verificarResultadoDomiciliaria.setRenderedDevolver(false);
				verificarResultadoDomiciliaria.setRenderedEnEspera(false);
				verificarResultadoDomiciliaria.setRenderedObs(false);
				verificarResultadoDomiciliaria.setRenderedRechazar(true);
				verificarResultadoDomiciliaria.setRenderedNoConforme(false);
				verificarResultadoDomiciliaria.obtenerOpcionBotones(expediente.getId());				
			}
		}
	}	
	
		
	
	public void ocultaBotones2(AjaxBehaviorEvent event) {
      	
			FacesContext ctx = FacesContext.getCurrentInstance();  
			VerificarResultadoDomiciliariaMB verificarResultadoDomiciliaria = (VerificarResultadoDomiciliariaMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarResultadoDomiciliaria");			
			LOG.info("ocultaBotones2::VERIFICACION_DOMICILIARIA: "+getObjectSession(Constantes.VERIFICACION_DOMICILIARIA));
			LOG.info("ocultaBotones2::VERIFICACION_LABORAL: "+getObjectSession(Constantes.VERIFICACION_LABORAL));
			
			if(getObjectSession(Constantes.VERIFICACION_DOMICILIARIA)!=null ){
				if(getObjectSession(Constantes.VERIFICACION_DOMICILIARIA).equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID)){
					selectedItemsResultVd3=(String)getObjectSession(Constantes.VERIFICACION_DOMICILIARIA);	
					LOG.info("VERIFICACION_DOMICILIARIA.EXISTE VALOR PARA ESTABLECEER CANCELAR de VENTANA RECHAZO: "+selectedItemsResultVd3);
				}
			}			
			if(getObjectSession(Constantes.VERIFICACION_LABORAL)!=null ){
				if(getObjectSession(Constantes.VERIFICACION_LABORAL).equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID)){
				selectedItemsResultVl3=(String)getObjectSession(Constantes.VERIFICACION_LABORAL);
				LOG.info("VERIFICACION_LABORAL.EXISTE VALOR PARA ESTABLECEER CANCELAR deVENTANA RECHAZO: "+selectedItemsResultVl3);
				}
			}
			if ((this.selectedItemsResultVl1!=null && this.selectedItemsResultVl1.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID) &&
				 this.selectedItemsResultVl2!=null && this.selectedItemsResultVl2.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID) &&
				 this.selectedItemsResultVl3!=null && this.selectedItemsResultVl3.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID)) ||
				
				(this.selectedItemsResultVd1!=null && this.selectedItemsResultVd1.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID) &&
				 this.selectedItemsResultVd2!=null && this.selectedItemsResultVd2.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID) &&
				 this.selectedItemsResultVd3!=null && this.selectedItemsResultVd3.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID)) ||
				 
				 (this.selectedItemsResultVdps1!=null && this.selectedItemsResultVdps1.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID) &&
				  this.selectedItemsResultVdps2!=null && this.selectedItemsResultVdps2.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID) &&
				  this.selectedItemsResultVdps3!=null && this.selectedItemsResultVdps3.equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID)) ) {
				
				verificarResultadoDomiciliaria.setRenderedResolver(true);
				verificarResultadoDomiciliaria.setRenderedDevolver(true);
				verificarResultadoDomiciliaria.setRenderedEnEspera(true);
				verificarResultadoDomiciliaria.setRenderedObs(true);
				verificarResultadoDomiciliaria.setRenderedRechazar(false);
				verificarResultadoDomiciliaria.setRenderedNoConforme(true);		
				verificarResultadoDomiciliaria.obtenerOpcionBotonesNoConforme();
			} 
		
	}
	
	public void ocultaBotones3(AjaxBehaviorEvent event) {
        String jspPrinc = getNombreJSPPrincipal();		
		if (jspPrinc.equals("formVerificarResultadoDomiciliaria")) {
			FacesContext ctx = FacesContext.getCurrentInstance();  
			VerificarResultadoDomiciliariaMB verificarResultadoDomiciliaria = (VerificarResultadoDomiciliariaMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "verificarResultadoDomiciliaria"); 
				verificarResultadoDomiciliaria.setRenderedResolver(true);
				verificarResultadoDomiciliaria.setRenderedDevolver(true);
				verificarResultadoDomiciliaria.setRenderedEnEspera(true);
				verificarResultadoDomiciliaria.setRenderedObs(true);
				verificarResultadoDomiciliaria.setRenderedRechazar(false);
				verificarResultadoDomiciliaria.setRenderedNoConforme(true);
				verificarResultadoDomiciliaria.obtenerOpcionBotonesNoConforme();
		}
	}
	
	public void cambiarVerLaboral(AjaxBehaviorEvent event) {
		FacesContext ctx = FacesContext.getCurrentInstance();		
		PanelDocumentosMB panelDocumento = (PanelDocumentosMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");		
		
		/*Guia Documentaria*/
		panelDocumento.cargarDocumentosPanel(event);
	}

	public void cambiarVerDomiciliaria(AjaxBehaviorEvent event) {
		FacesContext ctx = FacesContext.getCurrentInstance();		
		PanelDocumentosMB panelDocumento = (PanelDocumentosMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");		
		
		/*Guia Documentaria*/
		panelDocumento.cargarDocumentosPanel(event);
	}
	
	public void cambiarVerDps(AjaxBehaviorEvent event) {
		FacesContext ctx = FacesContext.getCurrentInstance();		
		PanelDocumentosMB panelDocumento = (PanelDocumentosMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "paneldocumentos");		
		
		/*Guia Documentaria*/
		panelDocumento.cargarDocumentosPanel(event);
	}
	
	public List<String> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<String> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public List<SelectItem> getListaResultadoVerificacion() {
		return listaResultadoVerificacion;
	}

	public void setListaResultadoVerificacion(
			List<SelectItem> listaResultadoVerificacion) {
		this.listaResultadoVerificacion = listaResultadoVerificacion;
	}

	public String getSelectedItemsResultVl1() {
		return selectedItemsResultVl1;
	}

	public void setSelectedItemsResultVl1(String selectedItemsResultVl1) {
		this.selectedItemsResultVl1 = selectedItemsResultVl1;
	}

	public String getSelectedItemsResultVl2() {
		return selectedItemsResultVl2;
	}

	public void setSelectedItemsResultVl2(String selectedItemsResultVl2) {
		this.selectedItemsResultVl2 = selectedItemsResultVl2;
	}

	public String getSelectedItemsResultVl3() {
		return selectedItemsResultVl3;
	}

	public void setSelectedItemsResultVl3(String selectedItemsResultVl3) {
		this.selectedItemsResultVl3 = selectedItemsResultVl3;
	}

	public boolean isSelectedItemVl1() {
		return selectedItemVl1;
	}

	public void setSelectedItemVl1(boolean selectedItemVl1) {
		this.selectedItemVl1 = selectedItemVl1;
	}

	public boolean isSelectedItemVl2() {
		return selectedItemVl2;
	}

	public void setSelectedItemVl2(boolean selectedItemVl2) {
		this.selectedItemVl2 = selectedItemVl2;
	}

	public boolean isSelectedItemVl3() {
		return selectedItemVl3;
	}

	public void setSelectedItemVl3(boolean selectedItemVl3) {
		this.selectedItemVl3 = selectedItemVl3;
	}

	public String getSelectedItemsResultVd1() {
		return selectedItemsResultVd1;
	}

	public void setSelectedItemsResultVd1(String selectedItemsResultVd1) {
		this.selectedItemsResultVd1 = selectedItemsResultVd1;
	}

	public String getSelectedItemsResultVd2() {
		return selectedItemsResultVd2;
	}

	public void setSelectedItemsResultVd2(String selectedItemsResultVd2) {
		this.selectedItemsResultVd2 = selectedItemsResultVd2;
	}

	public String getSelectedItemsResultVd3() {
		return selectedItemsResultVd3;
	}

	public void setSelectedItemsResultVd3(String selectedItemsResultVd3) {
		this.selectedItemsResultVd3 = selectedItemsResultVd3;
	}

	public boolean isSelectedItemVd1() {
		return selectedItemVd1;
	}

	public void setSelectedItemVd1(boolean selectedItemVd1) {
		this.selectedItemVd1 = selectedItemVd1;
	}

	public boolean isSelectedItemVd2() {
		return selectedItemVd2;
	}

	public void setSelectedItemVd2(boolean selectedItemVd2) {
		this.selectedItemVd2 = selectedItemVd2;
	}

	public boolean isSelectedItemVd3() {
		return selectedItemVd3;
	}

	public void setSelectedItemVd3(boolean selectedItemVd3) {
		this.selectedItemVd3 = selectedItemVd3;
	}

	public boolean isSelectedItemZp() {
		return selectedItemZp;
	}

	public void setSelectedItemZp(boolean selectedItemZp) {
		this.selectedItemZp = selectedItemZp;
	}

	public VerificacionExp getVerificacionExpteVO() {
		return verificacionExp;
	}

	public void setVerificacionExp(
			VerificacionExp verificacionExp) {
		this.verificacionExp = verificacionExp;
	}

	public List<VerificacionExp> getListVerificacionExp() {
		return listVerificacionExp;
	}

	public void setListVerificacionExpediente(
			List<VerificacionExp> listVerificacionExp) {
		this.listVerificacionExp = listVerificacionExp;
	}

	public boolean isHabCheckVl1() {
		return habCheckVl1;
	}

	public void setHabCheckVl1(boolean habCheckVl1) {
		this.habCheckVl1 = habCheckVl1;
	}

	public boolean isHabCheckVl2() {
		return habCheckVl2;
	}

	public void setHabCheckVl2(boolean habCheckVl2) {
		this.habCheckVl2 = habCheckVl2;
	}

	public boolean isHabCheckVl3() {
		return habCheckVl3;
	}

	public void setHabCheckVl3(boolean habCheckVl3) {
		this.habCheckVl3 = habCheckVl3;
	}

	public boolean isHabCheckVd1() {
		return habCheckVd1;
	}

	public void setHabCheckVd1(boolean habCheckVd1) {
		this.habCheckVd1 = habCheckVd1;
	}

	public boolean isHabCheckVd2() {
		return habCheckVd2;
	}

	public void setHabCheckVd2(boolean habCheckVd2) {
		this.habCheckVd2 = habCheckVd2;
	}

	public boolean isHabCheckVd3() {
		return habCheckVd3;
	}

	public void setHabCheckVd3(boolean habCheckVd3) {
		this.habCheckVd3 = habCheckVd3;
	}

	public boolean isHabSelVl1() {
		return habSelVl1;
	}

	public void setHabSelVl1(boolean habSelVl1) {
		this.habSelVl1 = habSelVl1;
	}

	public boolean isHabSelVl2() {
		return habSelVl2;
	}

	public void setHabSelVl2(boolean habSelVl2) {
		this.habSelVl2 = habSelVl2;
	}

	public boolean isHabSelVl3() {
		return habSelVl3;
	}

	public void setHabSelVl3(boolean habSelVl3) {
		this.habSelVl3 = habSelVl3;
	}

	public boolean isHabSelVd1() {
		return habSelVd1;
	}

	public void setHabSelVd1(boolean habSelVd1) {
		this.habSelVd1 = habSelVd1;
	}

	public boolean isHabSelVd2() {
		return habSelVd2;
	}

	public void setHabSelVd2(boolean habSelVd2) {
		this.habSelVd2 = habSelVd2;
	}

	public boolean isHabSelVd3() {
		return habSelVd3;
	}

	public void setHabSelVd3(boolean habSelVd3) {
		this.habSelVd3 = habSelVd3;
	}

	public boolean isHabZonaPl() {
		return habZonaPl;
	}

	public void setHabZonaPl(boolean habZonaPl) {
		this.habZonaPl = habZonaPl;
	}

	public String getVeriResObs() {
		return veriResObs;
	}

	public void setVeriResObs(String veriResObs) {
		this.veriResObs = veriResObs;
	}

	public Long getIdVl1() {
		return idVl1;
	}

	public void setIdVl1(Long idVl1) {
		this.idVl1 = idVl1;
	}

	public Long getIdVl2() {
		return idVl2;
	}

	public void setIdVl2(Long idVl2) {
		this.idVl2 = idVl2;
	}

	public Long getIdVl3() {
		return idVl3;
	}

	public void setIdVl3(Long idVl3) {
		this.idVl3 = idVl3;
	}

	public Long getIdVd1() {
		return idVd1;
	}

	public void setIdVd1(Long idVd1) {
		this.idVd1 = idVd1;
	}

	public Long getIdVd2() {
		return idVd2;
	}

	public void setIdVd2(Long idVd2) {
		this.idVd2 = idVd2;
	}

	public Long getIdVd3() {
		return idVd3;
	}

	public void setIdVd3(Long idVd3) {
		this.idVd3 = idVd3;
	}

	public Long getIdZp() {
		return idZp;
	}

	public void setIdZp(Long idZp) {
		this.idZp = idZp;
	}

	public String getSelectedItemsResultVdps1() {
		return selectedItemsResultVdps1;
	}

	public void setSelectedItemsResultVdps1(String selectedItemsResultVdps1) {
		this.selectedItemsResultVdps1 = selectedItemsResultVdps1;
	}

	public String getSelectedItemsResultVdps2() {
		return selectedItemsResultVdps2;
	}

	public void setSelectedItemsResultVdps2(String selectedItemsResultVdps2) {
		this.selectedItemsResultVdps2 = selectedItemsResultVdps2;
	}

	public String getSelectedItemsResultVdps3() {
		return selectedItemsResultVdps3;
	}

	public void setSelectedItemsResultVdps3(String selectedItemsResultVdps3) {
		this.selectedItemsResultVdps3 = selectedItemsResultVdps3;
	}

	public boolean isSelectedItemVdps1() {
		return selectedItemVdps1;
	}

	public void setSelectedItemVdps1(boolean selectedItemVdps1) {
		this.selectedItemVdps1 = selectedItemVdps1;
	}

	public boolean isSelectedItemVdps2() {
		return selectedItemVdps2;
	}

	public void setSelectedItemVdps2(boolean selectedItemVdps2) {
		this.selectedItemVdps2 = selectedItemVdps2;
	}

	public boolean isSelectedItemVdps3() {
		return selectedItemVdps3;
	}

	public void setSelectedItemVdps3(boolean selectedItemVdps3) {
		this.selectedItemVdps3 = selectedItemVdps3;
	}

	public boolean isHabCheckVdps1() {
		return habCheckVdps1;
	}

	public void setHabCheckVdps1(boolean habCheckVdps1) {
		this.habCheckVdps1 = habCheckVdps1;
	}

	public boolean isHabCheckVdps2() {
		return habCheckVdps2;
	}

	public void setHabCheckVdps2(boolean habCheckVdps2) {
		this.habCheckVdps2 = habCheckVdps2;
	}

	public boolean isHabCheckVdps3() {
		return habCheckVdps3;
	}

	public void setHabCheckVdps3(boolean habCheckVdps3) {
		this.habCheckVdps3 = habCheckVdps3;
	}

	public boolean isHabSelVdps1() {
		return habSelVdps1;
	}

	public void setHabSelVdps1(boolean habSelVdps1) {
		this.habSelVdps1 = habSelVdps1;
	}

	public boolean isHabSelVdps2() {
		return habSelVdps2;
	}

	public void setHabSelVdps2(boolean habSelVdps2) {
		this.habSelVdps2 = habSelVdps2;
	}

	public boolean isHabSelVdps3() {
		return habSelVdps3;
	}

	public void setHabSelVdps3(boolean habSelVdps3) {
		this.habSelVdps3 = habSelVdps3;
	}

	public Long getIdVdps1() {
		return idVdps1;
	}

	public void setIdVdps1(Long idVdps1) {
		this.idVdps1 = idVdps1;
	}

	public Long getIdVdps2() {
		return idVdps2;
	}

	public void setIdVdps2(Long idVdps2) {
		this.idVdps2 = idVdps2;
	}

	public Long getIdVdps3() {
		return idVdps3;
	}

	public void setIdVdps3(Long idVdps3) {
		this.idVdps3 = idVdps3;
	}
	
	
}
