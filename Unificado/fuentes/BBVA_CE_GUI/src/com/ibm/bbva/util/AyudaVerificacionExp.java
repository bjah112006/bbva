package com.ibm.bbva.util;

import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.form.RegistrarDatosMB;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.LineaMaxima;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.TblCeIbmDelegacionOficina;
import com.ibm.bbva.entities.VerificacionExp;
import com.ibm.bbva.service.business.client.ExpedienteDTO;
import com.ibm.bbva.session.LineaMaximaBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.TareaPerfilBeanLocal;
import com.ibm.bbva.session.TblCeIbmDelegacionOficinaLocal;
import com.ibm.bbva.session.VerificacionExpBeanLocal;

public class AyudaVerificacionExp {

	private VerificacionExpBeanLocal verificacionExpBean;
	private LineaMaximaBeanLocal lineaMaximaBeanLocalBean;
	private PerfilBeanLocal perfilBeanLocalBean;
	private TblCeIbmDelegacionOficinaLocal delegacionOficinaLocalBean;
	
	private static final Logger LOG = LoggerFactory.getLogger(RegistrarDatosMB.class);
	
	public AyudaVerificacionExp() {
		super();
		try {
			verificacionExpBean = (VerificacionExpBeanLocal) new InitialContext()
					.lookup("ejblocal:com.ibm.bbva.session.VerificacionExpBeanLocal");
			lineaMaximaBeanLocalBean = (LineaMaximaBeanLocal) new InitialContext()
					.lookup("ejblocal:com.ibm.bbva.session.LineaMaximaBeanLocal");		
			perfilBeanLocalBean = (PerfilBeanLocal) new InitialContext()
					.lookup("ejblocal:com.ibm.bbva.session.PerfilBeanLocal");	
			delegacionOficinaLocalBean = (TblCeIbmDelegacionOficinaLocal) new InitialContext()
					.lookup("ejblocal:com.ibm.bbva.session.TblCeIbmDelegacionOficinaLocal");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public boolean existeTresObsDomLab(Expediente expediente) {
		int i =0, j=0;
		List<VerificacionExp> listVerificacionExp;
		/*Obtener Datos de Verificacion expediente*/
		 listVerificacionExp = verificacionExpBean.buscarPorExpediente(expediente.getId());	
		 for (VerificacionExp list :listVerificacionExp) {
			 if (list.getFlagVerif()!=null && list.getFlagVerif().equals(Constantes.RESULTADO_TIPO_VERIFICACION_OBSERVADO_ID)) {
				 if(String.valueOf(list.getTipoVerificacion().getId()).equals(Constantes.CODIGO_VERIFICACION_LABORAL )) {
					i++;				
				 }
	             if(String.valueOf(list.getTipoVerificacion().getId()).equals(Constantes.CODIGO_VERIFICACION_DOMICILIARIA )) {
	            	 j++;
	             }
			 }
		 }			
		 if (i==3 || j==3) {		
			 return true;
		 }		
		 return false;
	}
	
	public Perfil lineaMaximaValidacion(ExpedienteDTO objExpedienteDTO){
		
		Perfil objPerfil=null;
		boolean enviado=false;
		String delegacionExpediente="";
		LOG.info("metodo lineaMaximaValidacion");
		if(objExpedienteDTO!=null){
			try{
					List<LineaMaxima> listLineaMaxima=lineaMaximaBeanLocalBean.buscarTodos();

					if(listLineaMaxima.size()>0){
						for(LineaMaxima lineaMax : listLineaMaxima)
							if(lineaMax!=null)
								if(lineaMax.getProducto()!=null && objExpedienteDTO.getCodigoProducto()!=null && 
										lineaMax.getProducto().getId()==objExpedienteDTO.getCodigoProducto()){
										LOG.info("Subrogado = "+objExpedienteDTO.getFlagSubRogado());
										LOG.info("Producto = "+lineaMax.getProducto().getId());
										LOG.info("lineaMax.getFlagSubrogado() = "+lineaMax.getFlagSubrogado());
									if(lineaMax.getProducto().getId()==Constantes.ID_APLICATIVO_PLD &&
											objExpedienteDTO.getFlagSubRogado()!=null && !lineaMax.getFlagSubrogado().trim().equals("") &&
											objExpedienteDTO.getFlagSubRogado().trim().equals(Constantes.CODIGO_FLAG_SUBROGADO_ACTIVO) && 
											objExpedienteDTO.getFlagSubRogado().trim().equals(lineaMax.getFlagSubrogado().trim())){
												LOG.info("lineaMax.getFlagSubrogado() ="+lineaMax.getFlagSubrogado());
												objPerfil=lineaMax.getPerfil();	
												enviado=true;
												LOG.info("enviado=true;=");
												break;										
									}else{
										if(lineaMax.getTipoMoneda()!=null && objExpedienteDTO.getCodigoTipoMonedaSol()!=null && 
												//lineaMax.getTipoMoneda().getId()==objExpedienteDTO.getCodigoTipoMonedaSol())
												lineaMax.getTipoMoneda().getId()==Long.valueOf(Constantes.CODIGO_TIPO_CAMBIO_SOLES))
											if(lineaMax.getTipoScoring()!=null && objExpedienteDTO.getCodigoTipoScoring()!=null 
												&& lineaMax.getTipoScoring().getId()==objExpedienteDTO.getCodigoTipoScoring()){
													boolean condicion=comparacionSegunSimbolo(objExpedienteDTO.getLineaCredSol(),
															lineaMax.getSimbolo().trim(),lineaMax.getMonto());
													LOG.info("condicion="+condicion);
													if(condicion){
														LOG.info("ntro a condicion");
														objPerfil=lineaMax.getPerfil();	
														enviado=true;
														LOG.info("enviado=true;=");
														break;
													}												
												
											}										
									}
								}
						
						if(!enviado)
							objPerfil=perfilBeanLocalBean.buscarPorId(Constantes.PERFIL_DEFECTO);
						
					}else
						LOG.info("listLineaMaxima es vacío");

			}catch(Exception e){
				objPerfil=null;
				e.printStackTrace();
			}
			
			if(objPerfil!=null)
				return objPerfil;			
		}else
			LOG.info("Objeto parametro objExpedienteDTO es nulo");
		
		return null;
	}
	
	public boolean comparacionSegunSimbolo(double primerValor,String simbolo, double segundoValor){
		if(simbolo!=null && !simbolo.trim().equals("") && isDouble(""+primerValor) && isDouble(""+segundoValor)){
			if(simbolo.trim().equals(Constantes.IGUAL))
				if(primerValor==segundoValor)
					return true;
				
				if(simbolo.trim().equals(Constantes.MAYOR))
					if(primerValor>segundoValor)
						return true;
					
					if(simbolo.trim().equals(Constantes.MAYOR_QUE))
						if(primerValor>=segundoValor)
							return true;
						
						if(simbolo.trim().equals(Constantes.MENOR))
							if(primerValor<segundoValor)
								return true;
							
							if(simbolo.trim().equals(Constantes.MENOR_QUE))
								if(primerValor<=segundoValor)
									return true;			
							
								if(simbolo.trim().equals(Constantes.DIFERENTE))
									if(primerValor!=segundoValor)
										return true;			
		}
		
		return false;
	}
	
	 public boolean isDouble(String cad)
	 {
		 try
		 {
		   Double.parseDouble(cad.trim());
		   return true;
		 }
		 catch(NumberFormatException nfe)
		 {
		   return false;
		 }
	 }	
	 
		public boolean delegacionOficinaValidacion(ExpedienteDTO objExpedienteDTO){
			LOG.info("metodo delegacionOficinaValidacion ");
			if(objExpedienteDTO!=null){
				LOG.info("CodigoNivel = "+objExpedienteDTO.getCodigoNivel());
				LOG.info("CodigoTipoMonedaSol = "+objExpedienteDTO.getCodigoTipoMonedaSol());
				LOG.info("CodigoProducto = "+objExpedienteDTO.getCodigoProducto());
				if(objExpedienteDTO.getCodigoNivel()>0 && objExpedienteDTO.getCodigoTipoMonedaSol()>0 && objExpedienteDTO.getCodigoProducto()>0){
					TblCeIbmDelegacionOficina objDelegacionOficina=delegacionOficinaLocalBean.buscarPorIdNivelIdProducto(objExpedienteDTO.getCodigoNivel(), objExpedienteDTO.getCodigoProducto());

					if(objDelegacionOficina!=null){
						LOG.info("Nivel = "+objDelegacionOficina.getNivel());
						return true;
					}else
						LOG.info("objDelegacionOficina es nulo");			
				}else
					LOG.info("Valor parametro es nulo");			
			}else
				LOG.info("Valor objExpedienteDTO es nulo");

			return false;
		}		 
}