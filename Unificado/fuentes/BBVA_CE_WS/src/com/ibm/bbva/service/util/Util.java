package com.ibm.bbva.service.util;



import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.MontoPeso;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.TipoDocumento;
import com.ibm.bbva.service.bean.DocumentoCM;
import com.ibm.bbva.service.bean.EmpleadoDTO;
import com.ibm.bbva.service.bean.EmpleadoVO;
import com.ibm.bbva.service.bean.PerfilDTO;
import com.ibm.bbva.session.MontoPesoBeanLocal;
import com.ibm.bbva.ws.service.Constantes;

public class Util {
	
	public static List<DocumentoCM> mapearDocumentosBDADocumentoCM(List<DocumentoExpTc> objDocumentoExpTcs){
		
		List<DocumentoCM> docs = new ArrayList<DocumentoCM>();
		
		for(DocumentoExpTc doc : objDocumentoExpTcs){
			docs.add(mapearDocumentoBDADocumentoCM(doc));
		}
		
		return docs;
	}

	public static DocumentoCM mapearDocumentoBDADocumentoCM(DocumentoExpTc objDocumentoExpTc){
		//System.out.println("Metodo mapearDocumentoBDaDocumentoCM");		
		DocumentoCM objDocumentoCM=new DocumentoCM();
		Calendar fechExpiracion = Calendar.getInstance();
		Calendar fechRegistro = Calendar.getInstance();
		
		objDocumentoCM.setId(objDocumentoExpTc.getId()); // nuevo id para content
		
		if(objDocumentoExpTc.getObligatorio()!=null && !objDocumentoExpTc.getObligatorio().trim().equals("")){
			Character idExpediente=new Character((char)objDocumentoExpTc.getObligatorio().toString().charAt(0));
			objDocumentoCM.setMandatorio(idExpediente);
		}
		if(objDocumentoExpTc.getFecVen()!=null){
			fechExpiracion.setTime(objDocumentoExpTc.getFecVen());
			objDocumentoCM.setFechaExpiracion(fechExpiracion);
		}
		if(objDocumentoExpTc.getCodCliente()!=null)
			objDocumentoCM.setCodCliente(objDocumentoExpTc.getCodCliente());
		
		if(objDocumentoExpTc.getExpediente()!=null)
			objDocumentoCM.setIdExpediente(objDocumentoExpTc.getExpediente().getId());
		
		if(objDocumentoExpTc.getNroDoi()!=null)
			objDocumentoCM.setNumDocumento(objDocumentoExpTc.getNroDoi());
		
		if(objDocumentoExpTc.getTipoDoi()!=null)
			objDocumentoCM.setTipoDoi(objDocumentoExpTc.getTipoDoi().getCodigo());
		
		if(objDocumentoExpTc.getPidCm()!=null)
			objDocumentoCM.setPidCm(objDocumentoExpTc.getPidCm());
		
		if(objDocumentoExpTc.getIdCm()!=null)
			objDocumentoCM.setIdCm(objDocumentoExpTc.getIdCm());
		
		if(objDocumentoExpTc.getFecReg()!=null){
			fechRegistro.setTime(objDocumentoExpTc.getFecReg());
			objDocumentoCM.setFechaRegistro(fechRegistro);
		}
		
		if(objDocumentoExpTc.getFlagCm()!=null)
			objDocumentoCM.setFlagCm(objDocumentoExpTc.getFlagCm());
		
		if(objDocumentoExpTc.getNombreArchivo()!=null)
			objDocumentoCM.setNombreArchivo(objDocumentoExpTc.getNombreArchivo());
		
		if(objDocumentoExpTc.getTipoDocumento()!=null)
			objDocumentoCM.setIdTipoDocumento(objDocumentoExpTc.getTipoDocumento().getId());
		
		return objDocumentoCM;
	}
	
	public static DocumentoExpTc mapearDocumentoCMADocumentoBD(DocumentoCM objDocumentoCM){
		//System.out.println("Metodo mapearDocumentoCMaDocumentoBD");
		DocumentoExpTc objDocumentoExpTc=new DocumentoExpTc();

		if(objDocumentoCM!=null){
			objDocumentoExpTc.setId(objDocumentoCM.getId());
			objDocumentoExpTc.setExpediente(new Expediente());
			objDocumentoExpTc.getExpediente().setId(objDocumentoCM.getIdExpediente());
			objDocumentoExpTc.setTipoDocumento(new TipoDocumento());
			if(objDocumentoCM.getIdTipoDocumento()>0)
				objDocumentoExpTc.getTipoDocumento().setId(objDocumentoCM.getIdTipoDocumento());
			objDocumentoExpTc.setIdCm(objDocumentoCM.getIdCm());
			objDocumentoExpTc.setPidCm(objDocumentoCM.getPidCm());
			
			if(objDocumentoCM.getFechaRegistro()!=null && objDocumentoCM.getFechaRegistro().getTimeInMillis()>0){
				Timestamp fecha =new Timestamp(objDocumentoCM.getFechaRegistro().getTimeInMillis());		
				objDocumentoExpTc.setFecReg(fecha);				
			}

			objDocumentoExpTc.setFlagCm(objDocumentoCM.getFlagCm());
			objDocumentoExpTc.setNombreArchivo(objDocumentoCM.getNombreArchivo());			
		}


		return objDocumentoExpTc;
	}
	
	public static EmpleadoDTO mapearEmpleadoAEmpleadoDTO(Empleado objEmpleado){
		System.out.println("Metodo mapearEmpleadotoEmpleadoDTO");
		EmpleadoDTO objEmpleadoDTO=new EmpleadoDTO();
		
		objEmpleadoDTO.setId(objEmpleado.getId());
		objEmpleadoDTO.setApepat(objEmpleado.getApepat());
		objEmpleadoDTO.setApemat(objEmpleado.getApemat());
		objEmpleadoDTO.setCodigo(objEmpleado.getCodigo());
		objEmpleadoDTO.setCorreo(objEmpleado.getCorreo());
		
		if(objEmpleado.getFecegr()!=null)
			objEmpleadoDTO.setFecegr(objEmpleado.getFecegr());
		if(objEmpleado.getFecing()!=null)
			objEmpleadoDTO.setFecing(objEmpleado.getFecing());
		
		
		objEmpleadoDTO.setFlagActivo(objEmpleado.getFlagActivo());
				
		if(objEmpleado.getTipoCategoria()!=null)
			objEmpleadoDTO.setIdCategoria(objEmpleado.getTipoCategoria().getId());
		
		if(objEmpleado.getOficina()!=null)
			objEmpleadoDTO.setIdOficina(objEmpleado.getOficina().getId());
		if(objEmpleado.getPerfil()!=null)
			objEmpleadoDTO.setIdPerfil(objEmpleado.getPerfil().getId());
		
		objEmpleadoDTO.setNombreCategoria(objEmpleado.getTipoCategoria().getDescripcion());		
		objEmpleadoDTO.setNombreOficina(objEmpleado.getOficina().getDescripcion());
		objEmpleadoDTO.setNombrePerfil(objEmpleado.getPerfil().getDescripcion());
		objEmpleadoDTO.setNombres(objEmpleado.getNombres());
		objEmpleadoDTO.setNombresCompletos(objEmpleado.getNombresCompletos());

		return objEmpleadoDTO;
	}	
	
	public static PerfilDTO mapearPerfilAPerfilDTO(Perfil objPerfil){
		System.out.println("Metodo mapearPerfiltoPerfilDTO");
		if(objPerfil!=null){		
			if(objPerfil.getPerfilJefe()!=null)
				return new PerfilDTO(objPerfil.getId(), objPerfil.getCodigo(), objPerfil.getDescripcion(), 
					objPerfil.getFlagAdministracion(), objPerfil.getFlagAsignacion(), objPerfil.getFlagRegistraAyu(), 
					objPerfil.getFlagRegistraExp(), objPerfil.getListaCorreoJefes(), objPerfil.getPerfilJefe().getId(), 
					objPerfil.getPerfilJefe().getDescripcion());
			else
				return new PerfilDTO(objPerfil.getId(), objPerfil.getCodigo(), objPerfil.getDescripcion(), 
						objPerfil.getFlagAdministracion(), objPerfil.getFlagAsignacion(), objPerfil.getFlagRegistraAyu(), 
						objPerfil.getFlagRegistraExp(), objPerfil.getListaCorreoJefes());				
		}
		
		return null;
	}
	
	public static boolean comparacionSegunSimbolo(double primerValor,String simbolo, double segundoValor){
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
	
	 public static boolean isDouble(String cad)
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
	 
	 public static boolean isLong(String cad)
	 {
	        try {
	        	Long.parseLong(cad.trim());
	        	return true;
	        } catch (NumberFormatException nfe){
	        	return false;
	        }
	  }
	 
	 public static boolean isInteger(String cad)
	 {
		 try
		 {
			 Integer.parseInt(cad.trim());
		   return true;
		 }
		 catch(NumberFormatException nfe)
		 {
		   return false;
		 }
	 }
	 
	public static EmpleadoVO mapearEmpleadoBDAEmpleadoVO(Empleado objEmpleado){
			System.out.println("Metodo mapearEmpleadoBDAEmpleadoVO");
			
			EmpleadoVO objEmpleadoVO=new EmpleadoVO();
			
			objEmpleadoVO.setId(objEmpleado.getId());
			
			if(objEmpleado.getFecegr()!=null)
				objEmpleadoVO.setFecegr(objEmpleado.getFecegr());
				
			if(objEmpleado.getFecing()!=null)
			objEmpleadoVO.setFecing(objEmpleado.getFecing());
			
			if(objEmpleado.getNivel()!=null)
			objEmpleadoVO.setIdNivelFk(objEmpleado.getNivel().getId());
			
			if(objEmpleado.getOficina()!=null)
				objEmpleadoVO.setIdOficinaFk(objEmpleado.getOficina().getId());
			
			if(objEmpleado.getPerfil()!=null)
				objEmpleadoVO.setIdPerfilFk(objEmpleado.getPerfil().getId());
			
			if(objEmpleado.getTipoCategoria()!=null)
				objEmpleadoVO.setIdTipoCategoriaFk(objEmpleado.getTipoCategoria().getId());
			
			objEmpleadoVO.setFlagActivo(objEmpleado.getFlagActivo()==null?"":objEmpleado.getFlagActivo());
			objEmpleadoVO.setNombres(objEmpleado.getNombres()==null?"":objEmpleado.getNombres());
			objEmpleadoVO.setNombresCompletos(objEmpleado.getNombresCompletos()==null?"":objEmpleado.getNombresCompletos());
			objEmpleadoVO.setApepat(objEmpleado.getApepat()==null?"":objEmpleado.getApepat());
			objEmpleadoVO.setApemat(objEmpleado.getApemat()==null?"":objEmpleado.getApemat());
			objEmpleadoVO.setCodigo(objEmpleado.getCodigo()==null?"":objEmpleado.getCodigo());
			objEmpleadoVO.setCorreo(objEmpleado.getCorreo()==null?"":objEmpleado.getCorreo());
			
			return objEmpleadoVO;
		}
	
	public static int pesoPorUsuario(List<Expediente> listExpedienteTemp, MontoPesoBeanLocal montoPesoBeanLocalBean){
		
		int cargaUsuario=0;
		if(listExpedienteTemp!=null && montoPesoBeanLocalBean!=null){
			for(Expediente objExpediente : listExpedienteTemp){
				System.out.println("objExpediente "+objExpediente.getId());
				int Peso=Integer.parseInt(objExpediente.getProducto().getPeso().toString());
				MontoPeso objMontoPeso=montoPesoBeanLocalBean.buscarPorIdProductoIdTipoOfertaMonto(objExpediente.getProducto().getId(),
						objExpediente.getExpedienteTC().getLineaCredSol(), objExpediente.getExpedienteTC().getTipoMonedaSol().getId());
				if(objMontoPeso!=null && objMontoPeso.getPeso()!=null)
					cargaUsuario+=Peso*Integer.parseInt(objMontoPeso.getPeso().toString());			
			}
		}

		System.out.println("cargaUsuario "+cargaUsuario);
		return cargaUsuario;
	}
	
	/**
	 * Metodo para obtener una fecha con el formato deseado
	 * @param fecha
	 * @param formato
	 * @return
	 */
	public static Date formatearFecha(Date fecha, String formato){
		
		try {
			SimpleDateFormat dateFormat =  new SimpleDateFormat(formato);
			String strFecha = dateFormat.format(fecha);
			return dateFormat.parse(strFecha);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
				
	}
	
	/**
	 * Metodo para obtener la fecha segun formato
	 * @param formato
	 * @param fecha
	 * @return
	 */
	public static String formatDateObject(String formato, java.util.Date fecha) {
        return new java.text.SimpleDateFormat(formato, Locale.getDefault()).format(fecha);
    }
	
	/**
	 * Metodo para parsear una cadena a fecha.
	 * @param sfecha
	 * @param formato
	 * @return
	 */
	public static Date parseStringToDate(String sfecha, String formato){
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat(formato);
		String strFecha = sfecha;
		Date fecha = null;
		try {

			fecha = formatoDelTexto.parse(strFecha);

		} catch (ParseException ex) {

			ex.printStackTrace();

		}
		return fecha;
	}
	
	public static double convertStringToDouble(String source, char decimalSeparator, char groupingSeparator){
    	DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(decimalSeparator);
        symbols.setGroupingSeparator(groupingSeparator);
        df.setDecimalFormatSymbols(symbols);        
		try {
			return df.parse(source).doubleValue();
		} catch (ParseException e) {
			return 0.0;
		}        
    }

}
