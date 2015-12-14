package com.ibm.bbva.tabla.util.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.entities.AyudaMemoria;
import com.ibm.bbva.entities.CategoriaRenta;
import com.ibm.bbva.entities.ClasifBanco;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.EstadoCivil;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.ExpedienteTC;
import com.ibm.bbva.entities.Garantia;
import com.ibm.bbva.entities.GrupoSegmento;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.Log;
import com.ibm.bbva.entities.Nivel;
import com.ibm.bbva.entities.Oficina;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.Persona;
import com.ibm.bbva.entities.Producto;
import com.ibm.bbva.entities.Segmento;
import com.ibm.bbva.entities.Subproducto;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.entities.Territorio;
import com.ibm.bbva.entities.TipoBuro;
import com.ibm.bbva.entities.TipoCategoria;
import com.ibm.bbva.entities.TipoCliente;
import com.ibm.bbva.entities.TipoDocumento;
import com.ibm.bbva.entities.TipoDoi;
import com.ibm.bbva.entities.TipoEnvio;
import com.ibm.bbva.entities.TipoMoneda;
import com.ibm.bbva.entities.TipoOferta;
import com.ibm.bbva.entities.TipoScoring;
import com.ibm.bbva.tabla.dto.DatosAyudaMemoriaIiceDTO;
import com.ibm.bbva.tabla.dto.DatosDetalleHistoricoIiceDTO;
import com.ibm.bbva.tabla.dto.DatosDetalleLogIiceDTO;
import com.ibm.bbva.tabla.dto.DatosDetalleObservacionesIiceDTO;
import com.ibm.bbva.tabla.dto.DatosDocumentosExpIiceDTO;
import com.ibm.bbva.tabla.dto.DatosHistAntiguoDTO;
import com.ibm.bbva.util.Util;

public class ConvertHistorial {
	
	private static final Logger LOG = LoggerFactory.getLogger(ConvertHistorial.class);

	public static List<Historial> convertToHistorial(List<DatosHistAntiguoDTO> listHistAntiguoDTO) {
		List<Historial> listaHistorial1=new ArrayList<Historial>();
		
		for(DatosHistAntiguoDTO histDTO: listHistAntiguoDTO){
			if(histDTO==null) continue;
			
			Historial hist = new Historial();
			hist.setExpediente(new Expediente());
			
			hist.setClienteNatural(new ClienteNatural());
			hist.getClienteNatural().setEstadoCivil(new EstadoCivil());
			CategoriaRenta categoriaRenta = new CategoriaRenta();
			List listaCategoriaRenta = new ArrayList<CategoriaRenta>();
			listaCategoriaRenta.add(categoriaRenta);
			hist.getClienteNatural().setCategoriasRenta(listaCategoriaRenta);
			hist.getClienteNatural().setPersona(new Persona());
			hist.getClienteNatural().getPersona().setProducto(new Producto());
			hist.getClienteNatural().setSegmento(new Segmento());
			hist.getClienteNatural().getSegmento().setGrupoSegmento(new GrupoSegmento());
			hist.getClienteNatural().setTipoCliente(new TipoCliente());
			hist.getClienteNatural().setTipoDoi(new TipoDoi());
		
			hist.setClienteNaturalConyuge(new ClienteNatural());
			hist.getClienteNaturalConyuge().setEstadoCivil(new EstadoCivil());
			CategoriaRenta categoriaRentaConyuge = new CategoriaRenta();
			List listaCategoriaRentaConyuge = new ArrayList<CategoriaRenta>();
			listaCategoriaRentaConyuge.add(categoriaRentaConyuge);
			hist.getClienteNaturalConyuge().setCategoriasRenta(listaCategoriaRentaConyuge);
			hist.getClienteNaturalConyuge().setPersona(new Persona());
			hist.getClienteNaturalConyuge().getPersona().setProducto(new Producto());
			hist.getClienteNaturalConyuge().setSegmento(new Segmento());
			hist.getClienteNaturalConyuge().getSegmento().setGrupoSegmento(new GrupoSegmento());
			hist.getClienteNaturalConyuge().setTipoCliente(new TipoCliente());
			hist.getClienteNaturalConyuge().setTipoDoi(new TipoDoi());
			
			hist.setEmpleado(new Empleado());
			hist.getEmpleado().setNivel(new Nivel());
			hist.getEmpleado().setOficina(new Oficina());
			hist.getEmpleado().getOficina().setOficinaPrincipal(new Oficina());
			hist.getEmpleado().getOficina().setTerritorio(new Territorio());
			hist.getEmpleado().setPerfil(new Perfil());
			hist.getEmpleado().setTipoCategoria(new TipoCategoria());
			hist.setEmpleadoResp(new Empleado());
			hist.getEmpleadoResp().setNivel(new Nivel());
			hist.getEmpleadoResp().setOficina(new Oficina());
			hist.getEmpleadoResp().getOficina().setOficinaPrincipal(new Oficina());
			hist.getEmpleadoResp().getOficina().setTerritorio(new Territorio());
			hist.getEmpleadoResp().setPerfil(new Perfil());
			hist.getEmpleadoResp().setTipoCategoria(new TipoCategoria());
			
			hist.setEstado(new Estado());
			hist.getEstado().setTarea(new Tarea());
			hist.setEstadoEnvWorkflowTc(new Estado());
			hist.getEstadoEnvWorkflowTc().setTarea(new Tarea());
			hist.setEstadoTipoResol(new Estado());
			hist.getEstadoTipoResol().setTarea(new Tarea());
			
			hist.setProducto(new Producto());
			hist.setSubproducto(new Subproducto());
			hist.getSubproducto().setProducto(new Producto());
			hist.getSubproducto().setTipoMoneda(new TipoMoneda());
			
			hist.setTipoMonedaSol(new TipoMoneda());
			hist.setTipoOferta(new TipoOferta());
			hist.setTipoScoring(new TipoScoring());
			hist.setTipoEnvio(new TipoEnvio());
			hist.setTipoBuro(new TipoBuro());
			hist.setTipoMonedaAprob(new TipoMoneda());
			hist.setBancoConyuge(new ClasifBanco());
			hist.getBancoConyuge().setProducto(new Producto());
			hist.setClasificacionBanco(new ClasifBanco());
			hist.getClasificacionBanco().setProducto(new Producto());
			hist.setOficina(new Oficina());
			hist.getOficina().setOficinaPrincipal(new Oficina());
			hist.getOficina().setTerritorio(new Territorio());
			hist.setTarea(new Tarea());
			
			//expediente
			hist.getExpediente().setOrigen(histDTO.getOrigen()!=null?histDTO.getOrigen():null);
			hist.setId(histDTO.getId()!=null?Long.parseLong(histDTO.getId()):0);
			hist.getExpediente().setId(histDTO.getNroExpediente()!=null?Long.parseLong(histDTO.getNroExpediente()):0);
			hist.getExpediente().setFecRegistro(histDTO.getFechaRegistroExp()!=null? convertStringToTimestamp(histDTO.getFechaRegistroExp()) :null );
			hist.getExpediente().setFechaEnvio(histDTO.getFechaEnvioExp()!=null? convertStringToTimestamp(histDTO.getFechaEnvioExp()) :null );
			hist.getExpediente().setFechaFin(histDTO.getFechaFinExp()!=null? convertStringToTimestamp(histDTO.getFechaFinExp()) :null);
			hist.getExpediente().setFlagActivo(histDTO.getFlagActivoExp()!=null?histDTO.getFlagActivoExp():null);
			hist.getExpediente().setFechaProgramada(histDTO.getFechaProgramadaExp()!=null? convertStringToTimestamp(histDTO.getFechaProgramadaExp()) :null);
			hist.setNumTerminal(histDTO.getNumeroTerminalExp()!=null? histDTO.getNumeroTerminalExp(): null);
			hist.getClienteNatural().setId(histDTO.getCodClienteNatural()!=null? Long.parseLong(histDTO.getCodClienteNatural()) : 0);
			hist.getEmpleado().setId(histDTO.getCodEmpleado()!=null ?Long.parseLong(histDTO.getCodEmpleado()): 0);
			hist.getEstado().setId(histDTO.getCodEstado()!=null?Long.parseLong(histDTO.getCodEstado()): 0);
			hist.getProducto().setId(histDTO.getCodProducto()!=null?Long.parseLong(histDTO.getCodProducto()): 0);
			
			//Historial
			/*hist.setOrigen(histDTO.getOrigen()!=null?histDTO.getOrigen():null);*/
			hist.setClasificacionSbs(histDTO.getClasificacionSBS()!=null?Util.convertStringToDouble(histDTO.getClasificacionSBS()):0);
			hist.setCodPreEval(histDTO.getCodPreEval()!=null?histDTO.getCodPreEval():null);
			/*hist.setComentarioAyuMem(histDTO.getComentarioAyuMem()!=null?histDTO.getComentarioAyuMem():null);
			hist.setComentarioAyuMem(histDTO.getComentarioAyuMem()!=null?histDTO.getComentarioAyuMem():null);
			hist.setComentarioAyuMem(histDTO.getComentarioAyuMem()!=null?histDTO.getComentarioAyuMem():null);
			hist.setComentarioDelegacion(histDTO.getComentarioDelegacion()!=null?histDTO.getComentarioDelegacion():null);
			hist.setComentarioRechazo(histDTO.getComentarioRechazo()!=null?histDTO.getComentarioRechazo():null);
			hist.setFecRegistro(histDTO.getFechaRegistro()!=null? convertStringToTimestamp(histDTO.getFechaRegistro()) :null );
			hist.setFechaEnvio(histDTO.getFechaEnvio()!=null? convertStringToTimestamp(histDTO.getFechaEnvio()) :null );
			hist.setFechaFin(histDTO.getFechaFin()!=null? convertStringToTimestamp(histDTO.getFechaFin()) :null );
			hist.setFechaProgramada(histDTO.getFechaProgramada()!=null? convertStringToTimestamp(histDTO.getFechaProgramada()) :null );
			hist.setFechaT1(histDTO.getFechaT1()!=null? convertStringToTimestamp(histDTO.getFechaT1()) :null );
			hist.setFechaT2(histDTO.getFechaT1()!=null? convertStringToTimestamp(histDTO.getFechaT2()) :null );
			hist.setFechaT3(histDTO.getFechaT1()!=null? convertStringToTimestamp(histDTO.getFechaT3()) :null );
			hist.setFlagComentario(histDTO.getFlagComentario()!=null?histDTO.getFlagComentario():null);
			hist.setFlagDelegacion(histDTO.getFlagDelegacion()!=null?histDTO.getFlagDelegacion():null);
			hist.setFlagDevolucion(histDTO.getFlagDevolucion()!=null?histDTO.getFlagDevolucion():null);
			hist.setFlagExcDelegacion(histDTO.getFlagExcDelegacion()!=null?histDTO.getFlagExcDelegacion():null);
			hist.setFlagModifScore(histDTO.getFlagModifScore()!=null?histDTO.getFlagModifScore():null);
			hist.setFlagRetraer(histDTO.getFlagRetraer()!=null?histDTO.getFlagRetraer():null);
			hist.setFlagSolTasaEsp(histDTO.getFlagSolTasaEsp()!=null?histDTO.getFlagSolTasaEsp():null);
			hist.setLineaConsumo(histDTO.getLineaConsumo()!=null?Double.parseDouble(histDTO.getLineaConsumo()):0);*/
			hist.setLineaCredAprob(histDTO.getLineaCredAprob()!=null?Util.convertStringToDouble(histDTO.getLineaCredAprob()):0);
			hist.setLineaCredSol(histDTO.getLineaCredSol()!=null?Util.convertStringToDouble(histDTO.getLineaCredSol()):0);
			hist.setNroContrato(histDTO.getNroContrato()!=null?histDTO.getNroContrato():null);
			/*hist.setNroCta(histDTO.getNroCuenta()!=null?histDTO.getNroCuenta():null);
			hist.setNumTerminal(histDTO.getNumTerminal()!=null?histDTO.getNumTerminal():null);
			hist.setTiempoCola(histDTO.getTiempoCola()!=null?Double.parseDouble(histDTO.getTiempoCola()):0);
			hist.setTiempoEjecucion(histDTO.getTiempoEjecucion()!=null?Double.parseDouble(histDTO.getTiempoEjecucion()):0);
			hist.setFechaT1R(histDTO.getFechaT1R()!=null? convertStringToTimestamp(histDTO.getFechaT1R()) :null );
			hist.setFechaT2R(histDTO.getFechaT2R()!=null? convertStringToTimestamp(histDTO.getFechaT2R()) :null );
			hist.setFechaT3R(histDTO.getFechaT3R()!=null? convertStringToTimestamp(histDTO.getFechaT3R()) :null );
			hist.setTiempoColar(histDTO.getTiempoColar()!=null?Double.parseDouble(histDTO.getTiempoColar()):0);
			hist.setTiempoEjecucionr(histDTO.getTiempoEjecucionR()!=null?Double.parseDouble(histDTO.getTiempoEjecucionR()):0);
			hist.setTiempoColaRetraer(histDTO.getTiempoColaRetraer()!=null?Double.parseDouble(histDTO.getTiempoColaRetraer()):0);
			hist.setPorcentajeEndeudamiento(histDTO.getPorcentajeEndeudamiento()!=null?Double.parseDouble(histDTO.getPorcentajeEndeudamiento()):0);
			hist.setRiesgoCliente(histDTO.getRiesgoCliente()!=null?Double.parseDouble(histDTO.getRiesgoCliente()):0);
			hist.setSbsConyuge(histDTO.getSbsConyugue()!=null?Double.parseDouble(histDTO.getSbsConyugue()):0);*/
			hist.setTasaEsp(histDTO.getTasaEsp()!=null?Util.convertStringToDouble(histDTO.getTasaEsp()):0);
			/*hist.setTipoResolucion(histDTO.getTipoResolucion()!=null?histDTO.getTipoResolucion():null);
			hist.setVerifDom(histDTO.getVerifDom()!=null?histDTO.getVerifDom():null);
			hist.setVerifLab(histDTO.getVerifLab()!=null?histDTO.getVerifLab():null);
			hist.setVerifDps(histDTO.getVerifDPS()!=null?histDTO.getVerifDPS():null);
			hist.setZonaPel(histDTO.getZonaPel()!=null?histDTO.getZonaPel():null);*/
			hist.setPlazoSolicitado(histDTO.getPlazoSol()!=null?histDTO.getPlazoSol():null);
			/*hist.setPlazoSolicitadoApr(histDTO.getPlazoSolAprob()!=null?histDTO.getPlazoSolAprob():null);
			hist.setTipoCambioExp(histDTO.getTipoCambioExp()!=null?new BigDecimal(histDTO.getTipoCambioExp()):null);
			hist.setFechaTipoCambioExp(histDTO.getFechaTipoCambioExp()!=null? convertStringToTimestamp(histDTO.getFechaTipoCambioExp()) :null);
			hist.setTiempoObjTC(histDTO.getTiempoObfTC()!=null?Integer.parseInt(histDTO.getTiempoObfTC()):0);
			hist.setTiempoObjTE(histDTO.getTiempoObjTE()!=null?Integer.parseInt(histDTO.getTiempoObjTE()):0);
			hist.setTiempoPreTC(histDTO.getTiempoPreTC()!=null?Integer.parseInt(histDTO.getTiempoPreTC()):0);
			hist.setTiempoPreTE(histDTO.getTiempoPreTe()!=null?Integer.parseInt(histDTO.getTiempoPreTe()):0);
			hist.setNomColumna(histDTO.getNomColumna()!=null?histDTO.getNomColumna():null);
			hist.setAns(histDTO.getAns()!=null?Integer.parseInt(histDTO.getAns()):0);*/
			hist.setRvgl(histDTO.getCodigoRVGL()!=null?histDTO.getCodigoRVGL():null);
			
			//Cliente Natural
			hist.getClienteNatural().setApePat(histDTO.getApellidoPatCliente()!=null?histDTO.getApellidoPatCliente():null);
			hist.getClienteNatural().setApeMat(histDTO.getApellidoMatCliente()!=null?histDTO.getApellidoMatCliente():null);
			hist.getClienteNatural().getCategoriasRenta().get(0).setId(histDTO.getCodCategRentaCliente()!=null?Long.parseLong(histDTO.getCodCategRentaCliente()):0);
			hist.getClienteNatural().setCodCliente(histDTO.getCodClienteNatural()!=null?histDTO.getCodClienteNatural():null);
			hist.getClienteNatural().setAval(histDTO.getAvalCliente()!=null?histDTO.getAvalCliente():null);
			hist.getClienteNatural().setCelular(histDTO.getCelularCliente()!=null?histDTO.getCelularCliente():null);
			hist.getClienteNatural().setCorreo(histDTO.getCorreoCliente()!=null?histDTO.getCorreoCliente():null);
			hist.getClienteNatural().setSubrog(histDTO.getSubrogCliente()!=null?histDTO.getSubrogCliente():null);
			hist.getClienteNatural().getEstadoCivil().setId(histDTO.getCodEstCivilCliente()!=null?Long.parseLong(histDTO.getCodEstCivilCliente()):0);
			hist.getClienteNatural().setNombre(histDTO.getNombreCliente()!=null?histDTO.getNombreCliente():null);
			hist.getClienteNatural().setIngNetoMensual(histDTO.getIngNetoMensualCliente()!=null?Util.convertStringToDouble(histDTO.getIngNetoMensualCliente()):0);
			hist.getClienteNatural().setFecVenDoi(histDTO.getFechaVenDoiCliente()!=null?convertStringToTimestamp(histDTO.getFechaVenDoiCliente()):null);
			hist.getClienteNatural().setNumDoi(histDTO.getNumDoiCliente()!=null?histDTO.getNumDoiCliente():null);
			hist.getClienteNatural().setPerExpPub(histDTO.getPerExpPubCliente()!=null? histDTO.getPerExpPubCliente(): null);
			hist.getClienteNatural().setPagoHab(histDTO.getPagoHabCliente()!=null?histDTO.getPagoHabCliente():null);
			hist.getClienteNatural().getPersona().setId(histDTO.getCodPersonaCliente()!=null?Long.parseLong(histDTO.getCodPersonaCliente()):0);
			hist.getClienteNatural().getPersona().setDescripcion(histDTO.getPersDescripCliente()!=null?histDTO.getPersDescripCliente():null);
			hist.getClienteNatural().getSegmento().setDescripcion(histDTO.getSegmentoCliente()!=null?histDTO.getSegmentoCliente():null);
			hist.getClienteNatural().getTipoCliente().setId(histDTO.getCodTipoCliente()!=null?Long.parseLong(histDTO.getCodTipoCliente()):0);
			hist.getClienteNatural().getTipoCliente().setDescripcion(histDTO.getTipoClienteDescrip()!=null?histDTO.getTipoClienteDescrip():null);
			hist.getClienteNatural().getTipoDoi().setId(histDTO.getCodTipoDoiCliente()!=null?Long.parseLong(histDTO.getCodTipoDoiCliente()):0);
			hist.getClienteNatural().getTipoDoi().setDescripcion(histDTO.getTipoDoiClienteDescrip()!=null?histDTO.getTipoDoiClienteDescrip():null);
			hist.getClienteNatural().getPersona().getProducto().setId(histDTO.getPersCodProductoCliente()!=null?Long.parseLong(histDTO.getPersCodProductoCliente()):0);
			hist.getClienteNatural().getSegmento().getGrupoSegmento().setId(histDTO.getCodGrupoSegmentoCliente()!=null?Long.parseLong(histDTO.getCodGrupoSegmentoCliente()):0);
			
			//Conyugue del Cliente
			hist.getClienteNaturalConyuge().setCodCliente(histDTO.getCodConyugue()!=null?histDTO.getCodConyugue():null);
			hist.getClienteNaturalConyuge().setApePat(histDTO.getApellidoPatConyugue()!=null?histDTO.getApellidoPatConyugue():null);
			hist.getClienteNaturalConyuge().setApeMat(histDTO.getApellidoMatConyugue()!=null?histDTO.getApellidoMatConyugue():null);
			hist.getClienteNaturalConyuge().getCategoriasRenta().get(0).setId(histDTO.getCodCategRentaConyugue()!=null?Long.parseLong(histDTO.getCodCategRentaConyugue()):0);
			hist.getClienteNaturalConyuge().setCodCliente(histDTO.getCodConyugue()!=null?histDTO.getCodConyugue():null);
			hist.getClienteNaturalConyuge().setAval(histDTO.getAvalConyugue()!=null?histDTO.getAvalConyugue():null);
			hist.getClienteNaturalConyuge().setCelular(histDTO.getCelularConyugue()!=null?histDTO.getCelularConyugue():null);
			hist.getClienteNaturalConyuge().setCorreo(histDTO.getCorreoConyugue()!=null?histDTO.getCorreoConyugue():null);
			hist.getClienteNaturalConyuge().setSubrog(histDTO.getSubrogConyugue()!=null?histDTO.getSubrogConyugue():null);
			hist.getClienteNaturalConyuge().getEstadoCivil().setId(histDTO.getCodEstCivilConyugue()!=null?Long.parseLong(histDTO.getCodEstCivilConyugue()):0);
			hist.getClienteNaturalConyuge().setNombre(histDTO.getNombreConyugue()!=null?histDTO.getNombreConyugue():null);
			hist.getClienteNaturalConyuge().setIngNetoMensual(histDTO.getIngNetoMensualConyugue()!=null?Util.convertStringToDouble(histDTO.getIngNetoMensualConyugue()):0);
			hist.getClienteNaturalConyuge().setFecVenDoi(histDTO.getFechaVenDoiConyugue()!=null?convertStringToTimestamp(histDTO.getFechaVenDoiConyugue()):null);
			hist.getClienteNaturalConyuge().setNumDoi(histDTO.getNumDoiConyugue()!=null?histDTO.getNumDoiConyugue():null);
			hist.getClienteNaturalConyuge().setPagoHab(histDTO.getPagoHabConyugue()!=null?histDTO.getPagoHabConyugue():null);
			hist.getClienteNaturalConyuge().setPerExpPub(histDTO.getPerExpPubConyugue()!=null? histDTO.getPerExpPubConyugue(): null);
			hist.getClienteNaturalConyuge().getPersona().setId(histDTO.getCodPersonaConyugue()!=null?Long.parseLong(histDTO.getCodPersonaConyugue()):0);
			hist.getClienteNaturalConyuge().getPersona().setDescripcion(histDTO.getPersDescripConyugue()!=null?histDTO.getPersDescripConyugue():null);
			hist.getClienteNaturalConyuge().getSegmento().setDescripcion(histDTO.getSegmentoConyugue()!=null?histDTO.getSegmentoConyugue():null);
			hist.getClienteNaturalConyuge().getTipoCliente().setId(histDTO.getCodTipoConyugue()!=null?Long.parseLong(histDTO.getCodTipoConyugue()):0);
			hist.getClienteNaturalConyuge().getTipoCliente().setDescripcion(histDTO.getTipoConyugueDescrip()!=null?histDTO.getTipoConyugueDescrip():null);
			hist.getClienteNaturalConyuge().getTipoDoi().setId(histDTO.getCodTipoDoiConyugue()!=null?Long.parseLong(histDTO.getCodTipoDoiConyugue()):0);
			hist.getClienteNaturalConyuge().getTipoDoi().setDescripcion(histDTO.getTipoDoiConyugueDescrip()!=null?histDTO.getTipoDoiConyugueDescrip():null);
			hist.getClienteNaturalConyuge().getPersona().getProducto().setId(histDTO.getPersCodProductoConyugue()!=null?Long.parseLong(histDTO.getPersCodProductoConyugue()):0);
			hist.getClienteNaturalConyuge().getSegmento().getGrupoSegmento().setId(histDTO.getCodGrupoSegmentoConyugue()!=null?Long.parseLong(histDTO.getCodGrupoSegmentoConyugue()):0);
			
			//Empleado
			hist.getEmpleado().setCodigo(histDTO.getCodigoEmpleado()!=null?histDTO.getCodigoEmpleado():null);
			hist.getEmpleado().setFlagActivo(histDTO.getFlagActivoEmpleado()!=null?histDTO.getFlagActivoEmpleado():null);
			hist.getEmpleado().setNombresCompletos(histDTO.getNombresCompletosEmpleado()!=null?histDTO.getNombresCompletosEmpleado():null);
			hist.getEmpleado().getNivel().setId(histDTO.getCodNivelEmpleado()!=null?Long.parseLong(histDTO.getCodNivelEmpleado()):0);
			hist.getEmpleado().getOficina().setId(histDTO.getCodOficinaEmpleado()!=null?Long.parseLong(histDTO.getCodOficinaEmpleado()):0);
			hist.getEmpleado().getPerfil().setId(histDTO.getCodPerfilEmleado()!=null?Long.parseLong(histDTO.getCodPerfilEmleado()):0);
			hist.getEmpleado().getPerfil().setDescripcion(histDTO.getPerfilEmpleado()!=null?histDTO.getPerfilEmpleado():null);
			hist.getEmpleado().getTipoCategoria().setId(histDTO.getCodTipoCategEmpleado()!=null?Long.parseLong(histDTO.getCodTipoCategEmpleado()):0);
			hist.getEmpleado().getNivel().setDescripcion(histDTO.getNivEmpDescripEmpleado()!=null?histDTO.getNivEmpDescripEmpleado():null);
			hist.getEmpleado().getOficina().setDescripcion(histDTO.getOficinaEmpleado()!=null?histDTO.getOficinaEmpleado():null);
			hist.getEmpleado().getOficina().setFlagAreaRiesgo(histDTO.getFlagAreaRiesgoOfiEmpleado()!=null?histDTO.getFlagAreaRiesgoOfiEmpleado():null);
			hist.getEmpleado().getOficina().setFlagDesplazada(histDTO.getFlagDesplezadaOfiEmpleado()!=null?histDTO.getFlagDesplezadaOfiEmpleado():null);
			hist.getEmpleado().getOficina().setFlagEscaneoWeb(histDTO.getFlagEscaneoWebOfiEmpleado()!=null?histDTO.getFlagEscaneoWebOfiEmpleado():null);
			hist.getEmpleado().getOficina().setMontoTope(histDTO.getMontoTopeOfiEmpleado()!=null?Util.convertStringToDouble(histDTO.getMontoTopeOfiEmpleado()):0);
			hist.getEmpleado().getOficina().setTasaTransf(histDTO.getTasaTransOfiEmpleado()!=null?new BigDecimal(histDTO.getTasaTransOfiEmpleado()):null);
			hist.getEmpleado().getOficina().setFlagActivo(histDTO.getFlagActivoEmpleado()!=null?histDTO.getFlagActivoEmpleado():null);
			hist.getEmpleado().getOficina().getOficinaPrincipal().setId(histDTO.getCodOfiPrincipalEmpleado()!=null?Long.parseLong(histDTO.getCodOfiPrincipalEmpleado()):0);
			hist.getEmpleado().getOficina().getTerritorio().setId(histDTO.getCodTerritorioOfiEmpleado()!=null?Long.parseLong(histDTO.getCodTerritorioOfiEmpleado()):0);
			hist.getEmpleado().getOficina().getTerritorio().setDescripcion(histDTO.getDescripTerOfiEmpleado()!=null?histDTO.getDescripTerOfiEmpleado():null);
			hist.getEmpleado().getTipoCategoria().setDescripcion(histDTO.getTipoCategEmpleado()!=null?histDTO.getTipoCategEmpleado():null);
			hist.getEmpleado().getTipoCategoria().setFlagSuperior(histDTO.getFlagSuperiorTipoCategEmpleado()!=null?histDTO.getFlagSuperiorTipoCategEmpleado():null);
			
			//Empleado Resp
			hist.getEmpleadoResp().setId(histDTO.getCodEmpleadoResp()!=null?Long.parseLong(histDTO.getCodEmpleadoResp()):0);
			hist.getEmpleadoResp().setFlagActivo(histDTO.getFlagActivoEmpleadoResp()!=null?histDTO.getFlagActivoEmpleadoResp():null);
			hist.getEmpleadoResp().setNombresCompletos(histDTO.getNombresCompletosEmpleadoResp()!=null?histDTO.getNombresCompletosEmpleadoResp():null);
			hist.getEmpleadoResp().getNivel().setId(histDTO.getCodNivelResp() !=null?Long.parseLong(histDTO.getCodNivelResp()):0);
			hist.getEmpleadoResp().getOficina().setId(histDTO.getCodOficinaEmpleadoResp()!=null?Long.parseLong(histDTO.getCodOficinaEmpleadoResp()):0);
			hist.getEmpleadoResp().getPerfil().setId(histDTO.getCodPerfilEmleadoResp()!=null?Long.parseLong(histDTO.getCodPerfilEmleadoResp()):0);
			hist.getEmpleadoResp().getPerfil().setDescripcion(histDTO.getPerfilEmpleadoResp()!=null?histDTO.getPerfilEmpleadoResp():null);
			hist.getEmpleadoResp().getTipoCategoria().setId(histDTO.getCodTipoCategEmpleadoResp()!=null?Long.parseLong(histDTO.getCodTipoCategEmpleadoResp()):0);
			hist.getEmpleadoResp().getNivel().setDescripcion(histDTO.getNivEmpDescripEmpleadoResp()!=null?histDTO.getNivEmpDescripEmpleadoResp():null);
			hist.getEmpleadoResp().getOficina().setDescripcion(histDTO.getOficinaEmpleadoResp()!=null?histDTO.getOficinaEmpleadoResp():null);
			hist.getEmpleadoResp().getOficina().setFlagAreaRiesgo(histDTO.getFlagAreaRiesgoOfiEmpleadoResp()!=null?histDTO.getFlagAreaRiesgoOfiEmpleadoResp():null);
			hist.getEmpleadoResp().getOficina().setFlagDesplazada(histDTO.getFlagDesplezadaOfiEmpleadoResp()!=null?histDTO.getFlagDesplezadaOfiEmpleadoResp():null);
			hist.getEmpleadoResp().getOficina().setFlagEscaneoWeb(histDTO.getFlagEscaneoWebOfiEmpleadoResp()!=null?histDTO.getFlagEscaneoWebOfiEmpleadoResp():null);
			hist.getEmpleadoResp().getOficina().setMontoTope(histDTO.getMontoTopeOfiEmpleadoResp()!=null?Util.convertStringToDouble(histDTO.getMontoTopeOfiEmpleadoResp()):0);
			hist.getEmpleadoResp().getOficina().setTasaTransf(histDTO.getTasaTransOfiEmpleadoResp()!=null?new BigDecimal(histDTO.getTasaTransOfiEmpleadoResp()):null);
			hist.getEmpleadoResp().getOficina().setFlagActivo(histDTO.getFlagActivoEmpleadoResp()!=null?histDTO.getFlagActivoEmpleadoResp():null);
			hist.getEmpleadoResp().getOficina().getOficinaPrincipal().setId(histDTO.getCodOfiPrincipalEmpleadoResp()!=null?Long.parseLong(histDTO.getCodOfiPrincipalEmpleadoResp()):0);
			hist.getEmpleadoResp().getOficina().getTerritorio().setId(histDTO.getCodTerritorioOfiEmpleadoResp()!=null?Long.parseLong(histDTO.getCodTerritorioOfiEmpleadoResp()):0);
			hist.getEmpleadoResp().getOficina().getTerritorio().setDescripcion(histDTO.getDescripTerOfiEmpleadoResp()!=null?histDTO.getDescripTerOfiEmpleadoResp():null);
			hist.getEmpleadoResp().getTipoCategoria().setDescripcion(histDTO.getTipoCategEmpleadoResp()!=null?histDTO.getTipoCategEmpleadoResp():null);
			hist.getEmpleadoResp().getTipoCategoria().setFlagSuperior(histDTO.getFlagSuperiorTipoCategEmpleadoResp()!=null?histDTO.getFlagSuperiorTipoCategEmpleadoResp():null);
			
			//Perfil
			//private String codPerfil;
			hist.getEmpleado().getPerfil().setFlagAdministracion(histDTO.getFlagAdminPerfil()!=null?histDTO.getFlagAdminPerfil():null);
			hist.getEmpleado().getPerfil().setFlagAsignacion(histDTO.getFlagAsignacionPerfil()!=null?histDTO.getFlagAsignacionPerfil():null);
			hist.getEmpleado().getPerfil().setFlagRegistraAyu(histDTO.getFlagRegistraAyuPerfil()!=null?histDTO.getFlagRegistraAyuPerfil():null);
			hist.getEmpleado().getPerfil().setFlagRegistraExp(histDTO.getFlagRegistraExpPerfil() !=null?histDTO.getFlagRegistraExpPerfil():null);
			hist.getEmpleado().getPerfil().setListaCorreoJefes(histDTO.getListaCorreoJefesPerfil()!=null?histDTO.getListaCorreoJefesPerfil():null);
			hist.getEmpleado().getPerfil().setFlagPendientes(histDTO.getFlagPendientesPerfil()!=null?histDTO.getFlagPendientesPerfil():null);
			hist.getEmpleado().getPerfil().setFlagProceso(histDTO.getFlagProcesoPerfil()!=null?histDTO.getFlagProcesoPerfil():null);
			hist.getEmpleado().getPerfil().setFlagMenuRegistraExpediente(histDTO.getFlagMenuRegExpPerfil()!=null?histDTO.getFlagMenuRegExpPerfil():null);
			hist.getEmpleado().getPerfil().setFlagMenuBandejaPendientes(histDTO.getFlagMenuBandPendientesPerfil()!=null?histDTO.getFlagMenuBandPendientesPerfil():null);
			hist.getEmpleado().getPerfil().setFlagMenuBusqueda(histDTO.getFlagMenuBusquedaPerfil()!=null?histDTO.getFlagMenuBusquedaPerfil():null);
			hist.getEmpleado().getPerfil().setFlagMenuBandejaHistorica(histDTO.getFlagMenuBandHistoricaPerfil()!=null?histDTO.getFlagMenuBandHistoricaPerfil():null);
			hist.getEmpleado().getPerfil().setFlagMenuBandejaAsignacion(histDTO.getFlagMenuBandAsignacionPerfil()!=null?histDTO.getFlagMenuBandAsignacionPerfil():null);
			hist.getEmpleado().getPerfil().setFlagMenuBandejaMantenimiento(histDTO.getFlagMenuBandMantenPerfil()!=null?histDTO.getFlagMenuBandMantenPerfil():null);
			hist.getEmpleado().getPerfil().setFlagMenuReporteHistorial(histDTO.getFlagMenuRepHistorialPerfil()!=null?histDTO.getFlagMenuRepHistorialPerfil():null);
			hist.getEmpleado().getPerfil().setFlagMenuReporteConsolidado(histDTO.getFlagMenuRepConsolidadoPerfil()!=null?histDTO.getFlagMenuRepConsolidadoPerfil():null);
			hist.getEmpleado().getPerfil().setFlagMenuReporteTOE(histDTO.getFlagMenuRepToePerfil()!=null?histDTO.getFlagMenuRepToePerfil():null);
			hist.getEmpleado().getPerfil().setFlagMenuHorario(histDTO.getFlagMenuHorarioPerfil()!=null?histDTO.getFlagMenuHorarioPerfil():null);
			hist.getEmpleado().getPerfil().setFlagMenuDescargaLDAP(histDTO.getFlagMenuDescargaLdapPerfil()!=null?histDTO.getFlagMenuDescargaLdapPerfil():null);
			
			//Oficina
			hist.getOficina().setId(histDTO.getCodOficina()!=null?Long.parseLong(histDTO.getCodOficina()):0);
			hist.getOficina().setDescripcion(histDTO.getOficina()!=null?histDTO.getOficina():null);
			hist.getOficina().setFlagAreaRiesgo(histDTO.getFlagAreaRiesgoOficina()!=null?histDTO.getFlagAreaRiesgoOficina():null);
			hist.getOficina().setFlagDesplazada(histDTO.getFlagDesplazadaOficina()!=null?histDTO.getFlagDesplazadaOficina():null);
			hist.getOficina().setFlagEscaneoWeb(histDTO.getFlagEscaneoWebOficina()!=null?histDTO.getFlagEscaneoWebOficina():null);
			hist.getOficina().setMontoTope(histDTO.getMontoTopeOficina()!=null?Util.convertStringToDouble(histDTO.getMontoTopeOficina()):0);
			hist.getOficina().setTasaTransf(histDTO.getTasaTransfOficina()!=null?new BigDecimal(histDTO.getTasaTransfOficina()):null);
			hist.getOficina().setFlagActivo(histDTO.getFlagActivoOficina()!=null?histDTO.getFlagActivoOficina():null);
			hist.getOficina().getOficinaPrincipal().setId(histDTO.getCodOfiPrincipalOficina()!=null?Long.parseLong(histDTO.getCodOfiPrincipalOficina()):0);
			hist.getOficina().getTerritorio().setId(histDTO.getCodTerritorioOficina()!=null?Long.parseLong(histDTO.getCodTerritorioOficina()):0);
			hist.getOficina().getTerritorio().setDescripcion(histDTO.getTerritorioOficina()!=null?histDTO.getTerritorioOficina():null);
			hist.getOficina().getTerritorio().setUbicacion(histDTO.getUbicacionOficina()!=null?histDTO.getUbicacionOficina():null);
			hist.getOficina().getTerritorio().setFlagProv(histDTO.getFlagProvOficina()!=null?histDTO.getFlagProvOficina():null);
		
			//Estado
			hist.getEstado().setCodigo(histDTO.getEstadoCodigo()!=null?histDTO.getEstadoCodigo():null);
			hist.getEstado().setDescripcion(histDTO.getEstadoDescrip()!=null?histDTO.getEstadoDescrip():null);
			hist.getEstado().getTarea().setId(histDTO.getCodTareaEstado()!=null?Long.parseLong(histDTO.getCodTareaEstado()):0);
			
			//Estado Tipo Prestamo Soles
			hist.getEstadoTipoResol().setId(histDTO.getCodEstadoTipresol()!=null?Long.parseLong(histDTO.getCodEstadoTipresol()):0);
			hist.getEstadoTipoResol().setCodigo(histDTO.getCodEstadoSol()!=null?histDTO.getCodEstadoSol():null);
			hist.getEstadoTipoResol().setDescripcion(histDTO.getDescripEstadoSol()!=null?histDTO.getDescripEstadoSol():null);
			hist.getEstadoTipoResol().getTarea().setId(histDTO.getCodTareaEstadoSol()!=null?Long.parseLong(histDTO.getCodTareaEstadoSol()):0);
			
			//Estado Envio Worckflow
			hist.getEstadoEnvWorkflowTc().setId(histDTO.getCodEstadoEnWorckflow()!=null?Long.parseLong(histDTO.getCodEstadoEnWorckflow()):0);
			hist.getEstadoEnvWorkflowTc().setCodigo(histDTO.getCodEstadoWkf() !=null?histDTO.getCodEstadoWkf():null);
			hist.getEstadoEnvWorkflowTc().setDescripcion(histDTO.getDescripEstadoWkf()!=null?histDTO.getDescripEstadoWkf():null);
			hist.getEstadoEnvWorkflowTc().getTarea().setId(histDTO.getCodTareaEstadoWkf()!=null?Long.parseLong(histDTO.getCodTareaEstadoWkf()):0);
			
			//SubProducto
			hist.getSubproducto().setId(histDTO.getCodSubProducto()!=null?Long.parseLong(histDTO.getCodSubProducto()):0);
			hist.getSubproducto().setDescripcion(histDTO.getSubProducto() !=null?histDTO.getSubProducto():null);
			hist.getSubproducto().getProducto().setId(histDTO.getCodProdSubProducto()!=null?Long.parseLong(histDTO.getCodProdSubProducto()):0);
			hist.getSubproducto().setFlagActivo(histDTO.getFlagActivoSubProducto()!=null?histDTO.getFlagActivoSubProducto():null);
			hist.getSubproducto().getTipoMoneda().setDescripcion(histDTO.getTipMonedaSubProducto()!=null?histDTO.getTipMonedaSubProducto():null);
			
			//Tarea
			hist.getTarea().setId(histDTO.getCodTarea() !=null?Long.parseLong(histDTO.getCodTarea()):0);
			hist.getTarea().setDescripcion(histDTO.getTarea()!=null?histDTO.getTarea():null);
			
			//Tipo Buro
			hist.getTipoBuro().setId(histDTO.getCodTipoBuro() !=null?Long.parseLong(histDTO.getCodTipoBuro()):0);
			hist.getTipoBuro().setCodigo(histDTO.getCodigoTipoBuro()!=null?histDTO.getCodigoTipoBuro():null);
			hist.getTipoBuro().setDescripcion(histDTO.getTipoBuro() !=null?histDTO.getTipoBuro():null);

			//Tipo Moneda
			hist.getTipoMonedaSol().setId(histDTO.getCodTipoMoneda()!=null?Long.parseLong(histDTO.getCodTipoMoneda()):0);
			hist.getTipoMonedaSol().setCodigo(histDTO.getCodigoTipoMoneda()!=null?histDTO.getCodigoTipoMoneda():null);
			hist.getTipoMonedaSol().setDescripcion(histDTO.getTipoMoneda()!=null?histDTO.getTipoMoneda():null);
						
			//Tipo Oferta
			hist.getTipoOferta().setId(histDTO.getCodTipoOferta()!=null?Long.parseLong(histDTO.getCodTipoOferta()):0);
			hist.getTipoOferta().setDescripcion(histDTO.getTipoOferta() !=null?histDTO.getTipoOferta():null);
			
			//Tipo Scoring
			hist.getTipoScoring().setId(histDTO.getCodTipoScoring()!=null?Long.parseLong(histDTO.getCodTipoScoring()):0);
			hist.getTipoScoring().setDescripcion(histDTO.getTipoScoring() !=null?histDTO.getTipoScoring():null);
		
			//Producto
			hist.getProducto().setDescripcion(histDTO.getProducto()!=null?histDTO.getProducto():null);
			
			//Tipo envio
			hist.getTipoEnvio().setId(histDTO.getCodTipoEnvio()!=null?Long.parseLong(histDTO.getCodTipoEnvio()):0);
			hist.getTipoEnvio().setCodigo(histDTO.getCodigoTipoEnvio()!=null?histDTO.getCodigoTipoEnvio():null);
			hist.getTipoEnvio().setDescripcion(histDTO.getTipoEnvio()!=null?histDTO.getTipoEnvio():null);
			
			//Tipo Moneda Prob
			hist.getTipoMonedaAprob().setId(histDTO.getCodTipoMonedaProb()!=null?Long.parseLong(histDTO.getCodTipoMonedaProb()):0);
			hist.getTipoMonedaAprob().setCodigo(histDTO.getCodigoTipoMonedaProb()!=null?histDTO.getCodigoTipoMonedaProb():null);
			hist.getTipoMonedaAprob().setDescripcion(histDTO.getTipoMonedaProb()!=null?histDTO.getTipoMonedaProb():null);
			
			//CLASIFICACION BANCO CONYUGUE
			hist.getBancoConyuge().setId(histDTO.getCodClasifBancoConyugue()!=null?Long.parseLong(histDTO.getCodClasifBancoConyugue()):0);
			hist.getBancoConyuge().setDescripcion(histDTO.getClasificacionBancoConyugue()!=null?histDTO.getClasificacionBancoConyugue():null);
			hist.getBancoConyuge().getProducto().setId(histDTO.getCodProdClasifBancoConyugue()!=null?Long.parseLong(histDTO.getCodProdClasifBancoConyugue()):0);
			
			//CLASIFICACION BANCO CLIENTE
			hist.getClasificacionBanco().setId(histDTO.getCodClasifBanco()!=null?Long.parseLong(histDTO.getCodClasifBanco()):0);
			hist.getClasificacionBanco().setDescripcion(histDTO.getClasificacionBanco());
			hist.getClasificacionBanco().getProducto().setId(histDTO.getCodProdClasifBanco()!=null?Long.parseLong(histDTO.getCodProdClasifBanco()):0);
			
			listaHistorial1.add(hist);
		}
		return listaHistorial1;
	}
		
	public static Timestamp convertStringToTimestamp(String str_date) {
	    try {
	    	LOG.info("str_date  "+str_date);
	    	DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss");
	    	//DateFormat inputFormat = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
	    	Date date = inputFormat.parse(str_date);
	    	java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
	    	
	    	return timeStampDate;
	      
	    } catch (ParseException e) {
	      System.out.println("Exception :" + e);
	      return null;
	    }
	}
	
	public static Expediente convertToExpedienteVO(DatosHistAntiguoDTO historial) {
		Expediente vo = new Expediente();
		vo.setExpedienteTC(new ExpedienteTC());
		vo.setOrigen(historial.getOrigen()!=null?historial.getOrigen():null);
		vo.setId(historial.getNroExpediente()!=null?Long.parseLong(historial.getNroExpediente()):0);
		vo.getExpedienteTC().setRiesgoCliente(historial.getRiesgoCliente() !=null?Util.convertStringToDouble(historial.getRiesgoCliente()):0);
		vo.setClienteNatural(new ClienteNatural());
		vo.getClienteNatural().setId(historial.getCodClienteNatural()!=null?Long.parseLong(historial.getCodClienteNatural()):0);
		vo.getClienteNatural().setCodCliente(historial.getCodigoCentralCliente()!=null?historial.getCodigoCentralCliente():null);
		vo.getClienteNatural().setNombre(historial.getNombreCliente()!=null?historial.getNombreCliente():null);
		vo.getClienteNatural().setApePat(historial.getApellidoPatCliente()!=null?historial.getApellidoPatCliente():null);
		vo.getClienteNatural().setApeMat(historial.getApellidoMatCliente()!=null?historial.getApellidoMatCliente():null);
		vo.getClienteNatural().setNumDoi(historial.getNumDoiCliente()!=null?historial.getNumDoiCliente():null);
		vo.getClienteNatural().setFecVenDoi(historial.getFechaVenDoiCliente()!=null?convertStringToTimestamp(historial.getFechaVenDoiCliente()):null);
		vo.getClienteNatural().setSubrog(historial.getIndicadorSubrogado()!=null?historial.getIndicadorSubrogado():null);
		
		vo.getClienteNatural().setTipoCliente(new TipoCliente());
		vo.getClienteNatural().getTipoCliente().setId(historial.getCodTipoCliente() !=null?Long.parseLong(historial.getCodTipoCliente()):0);
		vo.getClienteNatural().getTipoCliente().setDescripcion(historial.getTipoClienteDescrip()!=null?historial.getTipoClienteDescrip():null);
		vo.getClienteNatural().setPerExpPub(historial.getPerExpPubCliente()!=null?historial.getPerExpPubCliente():null);
		vo.getClienteNatural().setCorreo(historial.getCorreoCliente()!=null?historial.getCorreoCliente():null);
		vo.getClienteNatural().setCelular(historial.getCelularCliente()!=null?historial.getCelularCliente():null);
		
		vo.getClienteNatural().setTipoDoi(new TipoDoi());
		vo.getClienteNatural().getTipoDoi().setId(historial.getCodTipoDoiCliente()!=null?Long.parseLong(historial.getCodTipoDoiCliente()):0);
		vo.getClienteNatural().getTipoDoi().setDescripcion(historial.getTipoDoiClienteDescrip()!=null?historial.getTipoDoiClienteDescrip() :null);
		
		vo.getClienteNatural().setSegmento(new Segmento());
		vo.getClienteNatural().getSegmento().setCodigoSegmento(historial.getCodigoSegmentoCliente()!=null?historial.getCodigoSegmentoCliente() :null);
		vo.getClienteNatural().getSegmento().setDescripcion(historial.getSegmentoCliente()!=null?historial.getSegmentoCliente():null);
		vo.getClienteNatural().setPagoHab(historial.getPagoHabCliente() !=null?historial.getPagoHabCliente() :null);
		vo.getClienteNatural().getSegmento().setId(historial.getCodSegmentoCliente() !=null?Long.parseLong(historial.getCodSegmentoCliente()) :0);
		
		vo.getClienteNatural().setEstadoCivil(new EstadoCivil());
		vo.getClienteNatural().getEstadoCivil().setId(historial.getCodEstCivilCliente() !=null?Long.parseLong(historial.getCodEstCivilCliente()) :0);
		vo.getClienteNatural().getEstadoCivil().setDescripcion(historial.getEstadoCivilCliente() !=null?historial.getEstadoCivilCliente() :null);
		
		CategoriaRenta categoriaRenta = new CategoriaRenta();
		List listaCategoriaRenta = new ArrayList<CategoriaRenta>();
		listaCategoriaRenta.add(categoriaRenta);
		vo.getClienteNatural().setCategoriasRenta(listaCategoriaRenta);
		vo.getClienteNatural().getCategoriasRenta().get(0).setId(historial.getCodCategRentaCliente() !=null?Long.parseLong(historial.getCodCategRentaCliente()) :null);
		vo.getClienteNatural().getCategoriasRenta().get(0).setCodigo(historial.getCodCategRentaCliente() !=null?String.valueOf(Long.parseLong(historial.getCodCategRentaCliente())) :null);
		
		vo.getClienteNatural().setIngNetoMensual( historial.getIngNetoMensualCliente() !=null?Util.convertStringToDouble(historial.getIngNetoMensualCliente()):0);
		
		//Conyugue del Cliente
		//vo.getExpedienteTC().setClienteNaturalConyuge(new ClienteNatural());
		//vo.getExpedienteTC().getClienteNaturalConyuge().setCodCliente("1111");
		//vo.getExpedienteTC().getClienteNaturalConyuge().setNumDoi("11111111");
		//vo.getExpedienteTC().getClienteNaturalConyuge().set ("11111111");
		
		
		
		
		vo.setEstado(new Estado());
		vo.getEstado().setId(historial.getCodEstado()!=null?Long.parseLong(historial.getCodEstado()):0);
		vo.getEstado().setDescripcion(historial.getEstadoDescrip()!=null?historial.getEstadoDescrip():null);
		
		vo.setProducto(new Producto());
		vo.getProducto().setId(historial.getCodProducto()!=null?Long.parseLong(historial.getCodProducto()) :0);
		vo.getProducto().setDescripcion(historial.getProducto()!=null?historial.getProducto() :null);
		
		vo.setEmpleado(new Empleado());
		vo.getEmpleado().setNombres(historial.getNombreCliente()!=null?historial.getNombreCliente() :null);
		vo.getEmpleado().setApepat(historial.getApePatEmpleado() !=null?historial.getApePatEmpleado() :null);
		vo.getEmpleado().setApemat(historial.getApeMatEmpleado() !=null?historial.getApeMatEmpleado() :null);
		vo.getEmpleado().setCodigo(historial.getCodEmpleado()!=null?historial.getCodEmpleado():null);
		
		vo.getEmpleado().setPerfil(new Perfil());
		vo.getEmpleado().getPerfil().setId(historial.getCodPerfilEmleado()!=null?Long.parseLong(historial.getCodPerfilEmleado()) :0);
		vo.getEmpleado().getPerfil().setCodigo(historial.getCodigoPerfilEmpleado()!=null?historial.getCodigoPerfilEmpleado() :null);
		vo.getEmpleado().getPerfil().setDescripcion(historial.getPerfilEmpleado()!=null?historial.getPerfilEmpleado() :null);
		
		vo.getEmpleado().setOficina(new Oficina());
		vo.getEmpleado().getOficina().setId(historial.getCodOficinaEmpleado() !=null?Long.parseLong(historial.getCodOficinaEmpleado()): 0);
		vo.getEmpleado().getOficina().setDescripcion(historial.getOficinaEmpleado() !=null?historial.getOficinaEmpleado(): null);
		
		vo.getEmpleado().getOficina().setTerritorio(new Territorio());
		vo.getEmpleado().getOficina().getTerritorio().setId(historial.getCodTerritorioOfiEmpleado() !=null?Long.parseLong(historial.getCodTerritorioOfiEmpleado()): 0);
		vo.getEmpleado().getOficina().getTerritorio().setCodigo(historial.getCodigoTerritorioEmpleado()!=null?historial.getCodigoTerritorioEmpleado(): null);
		vo.getEmpleado().getOficina().getTerritorio().setDescripcion(historial.getDescripTerOfiEmpleado()!=null?historial.getDescripTerOfiEmpleado(): null);
		vo.getEmpleado().getOficina().getTerritorio().setFlagProv(historial.getFlagProvinciaTerEmpleado()!=null?historial.getFlagProvinciaTerEmpleado(): null);
		
		vo.getExpedienteTC().setTipoOferta(new TipoOferta());
		vo.getExpedienteTC().getTipoOferta().setId(historial.getCodTipoOferta()!=null?Long.parseLong(historial.getCodTipoOferta()): 0);
		vo.getExpedienteTC().getTipoOferta().setCodigo(historial.getCodigoTipoOferta() !=null?historial.getCodigoTipoOferta(): null);
		vo.getExpedienteTC().getTipoOferta().setDescripcion(historial.getNombreTipoOferta() !=null?historial.getNombreTipoOferta(): null);
		
		vo.getExpedienteTC().setCodPreEval(historial.getCodigoPreevaluadorExp() !=null?historial.getCodigoPreevaluadorExp(): null);
		vo.getExpedienteTC().setLineaCredSol(historial.getLineaCredSol()!=null?Util.convertStringToDouble(historial.getLineaCredSol()) :0);
		
		vo.getExpedienteTC().setSubproducto(new Subproducto());
		vo.getExpedienteTC().getSubproducto().setId(historial.getCodSubProducto() !=null?Long.parseLong(historial.getCodSubProducto()): 0);
		vo.getExpedienteTC().getSubproducto().setDescripcion(historial.getSubProducto() !=null?historial.getSubProducto(): null);
		
		vo.getExpedienteTC().setTipoMonedaSol(new TipoMoneda());
		vo.getExpedienteTC().getTipoMonedaSol().setDescripcion(historial.getTipoMoneda()!=null? historial.getTipoMoneda():null);
		
		vo.getExpedienteTC().setTipoEnvio(new TipoEnvio());
		
		//vo.getExpedienteTC().setSolTasaEsp(historial.getTasaEsp()!=null?historial.getTasaEsp():null);
		/*vo.getExpedienteTC().setVerifLab(historial.getVerifLab());
		vo.getExpedienteTC().setVerifDom(historial.getVerifDom());
		vo.getExpedienteTC().setZonaPel(historial.getZonaPel());
		vo.getExpedienteTC().setFlagComentario(historial.getFlagComentario());
		vo.setComentario(historial.getComentario());*/
		vo.getExpedienteTC().setLineaConsumo(historial.getLineaConsumo()!=null?Util.convertStringToDouble(historial.getLineaConsumo()):0);
		vo.getExpedienteTC().setPorcentajeEndeudamiento(historial.getPorcentajeEndeudamiento()!=null?Util.convertStringToDouble(historial.getPorcentajeEndeudamiento()):0);
		/*vo.getExpedienteTC().setRiesgoCliente(historial.getRiesgoCliente());*/
		vo.getExpedienteTC().setClasificacionSbs(historial.getClasificacionSBS()!=null?Util.convertStringToDouble(historial.getClasificacionSBS()):0);
		vo.getExpedienteTC().setSbsConyuge(historial.getClasifSbsCony() !=null?Util.convertStringToDouble(historial.getClasifSbsCony()):0);
		
		vo.getExpedienteTC().setClasificacionBanco(new ClasifBanco());
		vo.getExpedienteTC().getClasificacionBanco().setDescripcion(historial.getClasificacionBanco() !=null?historial.getClasificacionBanco():null);
		
		vo.getExpedienteTC().setBancoConyuge(new ClasifBanco());
		vo.getExpedienteTC().getBancoConyuge().setDescripcion(historial.getClasificacionBancoConyugue()!=null?historial.getClasificacionBancoConyugue():null);
		vo.getExpedienteTC().setRvgl(historial.getCodigoRVGL()!=null?historial.getCodigoRVGL():null);
		vo.getExpedienteTC().setNroCta(historial.getNroContrato()!=null?historial.getNroContrato():null);
		vo.getExpedienteTC().setNroContrato(historial.getNroContrato()!=null?historial.getNroContrato():null);
		vo.getExpedienteTC().setPlazoSolicitado(historial.getPlazoSolicitadoExp() !=null?historial.getPlazoSolicitadoExp():null);
		vo.getExpedienteTC().setPlazoSolicitadoApr(historial.getPlazoSolAprob()!=null?historial.getPlazoSolAprob():null);
		vo.getExpedienteTC().setLineaCredAprob(historial.getLineaCredAprob()!=null?Util.convertStringToDouble(historial.getLineaCredAprob()):0);
		vo.getExpedienteTC().setVerifDom(historial.getFlagVerificacionDomExp()!=null?historial.getFlagVerificacionDomExp():null);
		vo.getExpedienteTC().setVerifLab(historial.getFlagVerificacionLabExp()!=null?historial.getFlagVerificacionLabExp():null);
		vo.getExpedienteTC().setVerifDps(historial.getVerifDPS()!=null?historial.getVerifDPS():null);
		vo.getExpedienteTC().setGarantia(new Garantia());
		vo.getExpedienteTC().getGarantia().setId(historial.getCodGarantiaProducto()!=null?Long.parseLong(historial.getCodGarantiaProducto()):0);
		vo.getExpedienteTC().getGarantia().setDescripcion(historial.getGarantiaProducto()!=null?historial.getGarantiaProducto():null);
		
		/*vo.getExpedienteTC().setEstadoTipoResol(historial.getEstadoTipoResol());
		vo.getExpedienteTC().setFlagDelegacion(historial.getFlagDelegacion());
		vo.getExpedienteTC().setComentarioRechazo(historial.getComentarioRechazo());
		vo.getExpedienteTC().setComentarioDelegacion(historial.getComentarioDelegacion());*/
		
		vo.getExpedienteTC().setTipoBuro(new TipoBuro());
		vo.getExpedienteTC().getTipoBuro().setId(historial.getTipoBuro()!=null?99:0); //se pone id ficticio ya q luego se valida ese campo para mostrar en pantalla la descripcion
		vo.getExpedienteTC().getTipoBuro().setDescripcion(historial.getTipoBuro()!=null?historial.getTipoBuro():null);
		
		vo.getExpedienteTC().setTipoScoring(new TipoScoring());
		vo.getExpedienteTC().getTipoScoring().setId(historial.getCodTipoScoring()!=null?Long.parseLong(historial.getCodTipoScoring()):0);
		vo.getExpedienteTC().getTipoScoring().setDescripcion(historial.getTipoScoring()!=null?historial.getTipoScoring():null);
		
		vo.getExpedienteTC().setTipoMonedaAprob(new TipoMoneda());
		
		/*vo.getExpedienteTC().setFlagSolTasaEsp(historial.getFlagSolTasaEsp());*/
		vo.getExpedienteTC().setTasaEsp(historial.getTasaEsp()!=null?Util.convertStringToDouble(historial.getTasaEsp()):0);
		vo.getExpedienteTC().setFlagModifScore(historial.getModificarScoring()!=null?historial.getModificarScoring():null);
		/*vo.getExpedienteTC().setFlagExcDelegacion(historial.getFlagExcDelegacion());
		vo.getExpedienteTC().setEstadoEnvWorkflowTc(historial.getEstadoEnvWorkflowTc());
		vo.getExpedienteTC().setComentarioAyuMem(historial.getComentarioAyuMem());
		vo.setNumTerminal(historial.getNumTerminal());
		vo.setFechaEnvio(historial.getFechaEnvio());
		vo.setFechaProgramada(historial.getFechaProgramada());*/
		vo.setFechaFin(historial.getFechaFinExp()!=null?convertStringToTimestamp(historial.getFechaFinExp()):null);
		
		//vo.setAccion(historial.getAccion());
		
		vo.getExpedienteTC().setTarea(new Tarea());
		
		vo.getExpedienteTC().setEmpleado(new Empleado());//debe ser el empleado de creación
		vo.getExpedienteTC().getEmpleado().setNombres(historial.getNombreCliente()!=null?historial.getNombreCliente() :null);
		vo.getExpedienteTC().getEmpleado().setApepat(historial.getApePatEmpleado() !=null?historial.getApePatEmpleado() :null);
		vo.getExpedienteTC().getEmpleado().setApemat(historial.getApeMatEmpleado() !=null?historial.getApeMatEmpleado() :null);
		vo.getExpedienteTC().getEmpleado().setNombresCompletos(historial.getNombresCompletosEmpleado()!=null?historial.getNombresCompletosEmpleado() :null);
		vo.getExpedienteTC().getEmpleado().setCodigo(historial.getCodEmpleado()!=null?historial.getCodEmpleado():null);
		
		vo.getExpedienteTC().getEmpleado().setPerfil(new Perfil());
		vo.getExpedienteTC().getEmpleado().getPerfil().setId(historial.getCodPerfilEmleado()!=null?Long.parseLong(historial.getCodPerfilEmleado()) :0);
		vo.getExpedienteTC().getEmpleado().getPerfil().setCodigo(historial.getCodigoPerfilEmpleado()!=null?historial.getCodigoPerfilEmpleado() :null);
		vo.getExpedienteTC().getEmpleado().getPerfil().setDescripcion(historial.getPerfilEmpleado()!=null?historial.getPerfilEmpleado() :null);
		
		vo.getExpedienteTC().getEmpleado().setOficina(new Oficina());
		vo.getExpedienteTC().getEmpleado().getOficina().setId(historial.getCodOficinaEmpleado() !=null?Long.parseLong(historial.getCodOficinaEmpleado()): 0);
		vo.getExpedienteTC().getEmpleado().getOficina().setDescripcion(historial.getOficinaEmpleado() !=null?historial.getOficinaEmpleado(): null);
		
		vo.getExpedienteTC().getEmpleado().getOficina().setTerritorio(new Territorio());
		vo.getExpedienteTC().getEmpleado().getOficina().getTerritorio().setId(historial.getCodTerritorioOfiEmpleado() !=null?Long.parseLong(historial.getCodTerritorioOfiEmpleado()): 0);
		vo.getExpedienteTC().getEmpleado().getOficina().getTerritorio().setCodigo(historial.getCodigoTerritorioEmpleado()!=null?historial.getCodigoTerritorioEmpleado(): null);
		vo.getExpedienteTC().getEmpleado().getOficina().getTerritorio().setDescripcion(historial.getDescripTerOfiEmpleado()!=null?historial.getDescripTerOfiEmpleado(): null);
		vo.getExpedienteTC().getEmpleado().getOficina().getTerritorio().setFlagProv(historial.getFlagProvinciaTerEmpleado()!=null?historial.getFlagProvinciaTerEmpleado(): null);
		
		vo.getExpedienteTC().setOficina(new Oficina());//debe ser la oficina de creación
		vo.getExpedienteTC().getOficina().setTerritorio(new Territorio());
		vo.getExpedienteTC().getOficina().setId(historial.getCodOficina() !=null?Long.parseLong(historial.getCodOficina()) :0);
		vo.getExpedienteTC().getOficina().setCodigo(historial.getCodigoOficina()!=null?historial.getCodigoOficina() :null);
		vo.getExpedienteTC().getOficina().setDescripcion(historial.getOficina() !=null?historial.getOficina() :null);
		vo.getExpedienteTC().getOficina().setUbicacion(historial.getUbicacionOficina() !=null?historial.getUbicacionOficina() :null);
		vo.getExpedienteTC().getOficina().setMontoTope(historial.getMontoTopeOficina() !=null?Util.convertStringToDouble(historial.getMontoTopeOficina()) :0);
		vo.getExpedienteTC().getOficina().getTerritorio().setId(historial.getCodTerritorioOficina()!=null?Long.parseLong(historial.getCodTerritorioOficina()) :0);
		vo.getExpedienteTC().getOficina().getTerritorio().setDescripcion(historial.getTerritorioOficina()!=null?historial.getTerritorioOficina() :null);
		//vo.getExpedienteTC().getOficina().getTerritorio().setFlagProv(historial.getTerritorioOficina()!=null?historial.getTerritorioOficina() :null);
		//vo.getExpedienteTC().getOficina().getTerritorio().setUbicacion(historial.getu!=null?historial.getTerritorioOficina() :null);
		vo.getExpedienteTC().getOficina().setFlagEscaneoWeb (historial.getFlagEscaneoWebOficina()!=null?historial.getFlagEscaneoWebOficina() :null);
		
		/*vo.getExpedienteTC().setFechaT1(historial.getFechaT1());
		vo.getExpedienteTC().setFechaT2(historial.getFechaT2());
		vo.getExpedienteTC().setFechaT3(historial.getFechaT3());
		vo.getExpedienteTC().setNumTarjeta(expedienteBD.getExpedienteTC().getNumTarjeta());
		vo.getExpedienteTC().setClienteNaturalConyuge(historial.getClienteNaturalConyuge());
		vo.getExpedienteTC().setFlagDevolucion(historial.getFlagDevolucion());
		vo.getExpedienteTC().setFlagRetraer(historial.getFlagRetraer());
		vo.getExpedienteTC().setTipoResolucion(historial.getTipoResolucion());*/
        
		
		
		return vo;
	}
	
	public static List<AyudaMemoria> convertToAyudaMemoria(List<DatosAyudaMemoriaIiceDTO> listAyudaMemoriaDTO) {
		List<AyudaMemoria> listAyudaMemoria=new ArrayList<AyudaMemoria>();
		
		for(DatosAyudaMemoriaIiceDTO amDTO: listAyudaMemoriaDTO){
			if(amDTO==null) continue;
			
			AyudaMemoria am = new AyudaMemoria();
			am.setExpediente(new Expediente());
			am.setEmpleado(new Empleado());
			am.getEmpleado().setPerfil(new Perfil());
						
			am.getExpediente().setId(amDTO.getNroExpediente()!=null?Long.parseLong(amDTO.getNroExpediente()):0);
			am.setId(amDTO.getIdAyuda()!=null?Long.parseLong(amDTO.getIdAyuda()):0);
			am.setComentario(amDTO.getDetalleAyuda()!=null?amDTO.getDetalleAyuda():null);
			am.getEmpleado().setCodigo(amDTO.getCodUsuario()!=null?amDTO.getCodUsuario():null);
			am.getEmpleado().setNombresCompletos(amDTO.getNombreUsuario()!=null?amDTO.getNombreUsuario():null);
			am.setEstado(amDTO.getEstadoExpediente()!=null?amDTO.getEstadoExpediente():null);
			am.setFecRegistro(amDTO.getFechaHora()!=null? convertStringToTimestamp(amDTO.getFechaHora()):null);
			am.getExpediente().setId(amDTO.getNroExpediente()!=null?Long.parseLong(amDTO.getNroExpediente()):0);
			am.getEmpleado().getPerfil().setDescripcion(amDTO.getPerfilUsuario()!=null? amDTO.getPerfilUsuario():null);
						
			listAyudaMemoria.add(am);
		}
		return listAyudaMemoria;
	}
	
	public static List<Historial> convertToDetalleHistorial(List<DatosDetalleHistoricoIiceDTO> listDetalleHostorialDTO) {
		List<Historial> listHistorial = new ArrayList<Historial>();
		
		for(DatosDetalleHistoricoIiceDTO dhDTO: listDetalleHostorialDTO){
			if(dhDTO==null) continue;
			
			Historial h = new Historial();
			h.setExpediente(new Expediente());
			h.setEstado(new Estado());
			h.setEmpleado(new Empleado());
			h.getEmpleado().setPerfil(new Perfil());
			h.setPerfil(new Perfil());
						
			h.setId(dhDTO.getNroHistorial()!=null?Long.parseLong(dhDTO.getNroHistorial()):0);
			h.getEmpleado().setCodigo(dhDTO.getCodUsuario()!=null?dhDTO.getCodUsuario():null);
			h.getEmpleado().setNombresCompletos(dhDTO.getNombreUsuario()!=null?dhDTO.getNombreUsuario():null);
			h.getEstado().setDescripcion(dhDTO.getEstadoExpediente()!=null?dhDTO.getEstadoExpediente():null);
			h.setFechaT1(dhDTO.getFechaHoraEnvio() !=null?convertStringToTimestamp(dhDTO.getFechaHoraEnvio()):null);
			h.setFechaT3(dhDTO.getFechaHoraAtencion()!=null?convertStringToTimestamp(dhDTO.getFechaHoraAtencion()):null);
			h.getEmpleado().getPerfil().setDescripcion(dhDTO.getPerfilUsuario()!=null?dhDTO.getPerfilUsuario():null);
			
			h.getPerfil().setDescripcion(dhDTO.getNombrePerfil()!=null?dhDTO.getNombrePerfil():null);
			
			h.getExpediente().setId(dhDTO.getNroExpediente()!=null?Long.parseLong(dhDTO.getNroExpediente()):0);
			h.setNumTerminal(dhDTO.getTerminal()!=null?dhDTO.getTerminal():null);
			h.getEmpleado().getPerfil().setDescripcion(dhDTO.getNombrePerfil()!=null?dhDTO.getNombrePerfil():null);
			h.getEmpleado().setNombres(dhDTO.getNombrePerfil()!=null?dhDTO.getNombrePerfil():null);
			h.getEmpleado().setApepat(dhDTO.getApePatEmpleado()!=null?dhDTO.getApePatEmpleado():null);
			h.getEmpleado().setApemat(dhDTO.getApeMatEmpleado()!=null?dhDTO.getApeMatEmpleado():null);
			h.setFechaT2(dhDTO.getFechaHoraTrabajo()!=null?convertStringToTimestamp(dhDTO.getFechaHoraTrabajo()):null);
			h.setFlagDevolucion(dhDTO.getFlagDevolucion()!=null?dhDTO.getFlagDevolucion():null);
			h.getEstado().setCodigo(dhDTO.getCodEstado()!=null?dhDTO.getCodEstado():null);
						
			listHistorial.add(h);
		}
		return listHistorial;
	}
	
	public static List<Historial> convertToObservaciones(List<DatosDetalleObservacionesIiceDTO> listDetalleObservacionesDTO) {
		List<Historial> listHistorial = new ArrayList<Historial>();
		
		for(DatosDetalleObservacionesIiceDTO oDTO: listDetalleObservacionesDTO){
			if(oDTO==null) continue;
			
			Historial h = new Historial();
			h.setExpediente(new Expediente());
			h.setEstado(new Estado());
			h.setEmpleado(new Empleado());
			h.getEmpleado().setPerfil(new Perfil());
			
			//objDatosDetalleObservIiceDTO.setNroObservacion(results.getString("CORRELATIVO_OBSERVACION"));
			//objDatosDetalleObservIiceDTO.setTipo(results.getString("TIPO"));
						
			h.getExpediente().setId(oDTO.getNroExpediente()!=null?Long.parseLong(oDTO.getNroExpediente()):0);
			h.getEmpleado().setCodigo(oDTO.getCodUsuario()!=null?oDTO.getCodUsuario():null);
			h.getEmpleado().setNombresCompletos(oDTO.getNombreUsuario()!=null?oDTO.getNombreUsuario():null);
			h.getEmpleado().getPerfil().setDescripcion(oDTO.getNombrePerfil()!=null?oDTO.getNombrePerfil():null);
			h.setFecRegistro(oDTO.getFechaHora()!=null?convertStringToTimestamp(oDTO.getFechaHora()):null);
			h.getEmpleado().setNombres(oDTO.getNombreEmpleado()!=null?oDTO.getNombreEmpleado():null);
			h.getEmpleado().setApepat(oDTO.getApePatEmpleado()!=null?oDTO.getApePatEmpleado():null);
			h.getEmpleado().setApemat(oDTO.getApeMatEmpleado()!=null?oDTO.getApeMatEmpleado():null);
			h.setComentario(oDTO.getDetalleObservacion()!=null?oDTO.getDetalleObservacion():null);
			h.setNumTerminal(oDTO.getTerminal()!=null?oDTO.getTerminal():null);
			h.getEstado().setDescripcion(oDTO.getEstadoExpediente()!=null?oDTO.getEstadoExpediente():null);
						
			listHistorial.add(h);
		}
		return listHistorial;
	}
	
	public static List<Log> convertToLog(List<DatosDetalleLogIiceDTO> listDetalleLogDTO) {
		List<Log> listLog = new ArrayList<Log>();
		
		for(DatosDetalleLogIiceDTO logDTO: listDetalleLogDTO){
			if(logDTO==null) continue;
			
			Log log = new Log();
			log.setEmpleado(new Empleado());
			log.setEstado(new Estado());
			log.getEmpleado().setPerfil(new Perfil());
			log.setExpediente(new Expediente());
								
			//objDatosDetalleLogIiceDTO.setFechaHoraEnvio(results.getString("FECHA_HORA_ENVIO"));
        	//objDatosDetalleLogIiceDTO.setNomrePerfil(results.getString("NOMBRE_PERFIL"));
			
			log.setId(logDTO.getNroLog()!=null?Long.parseLong(logDTO.getNroLog()):0);
			log.getExpediente().setId(logDTO.getNroExpediente()!=null?Long.parseLong(logDTO.getNroExpediente()):0);
			log.getEmpleado().setCodigo(logDTO.getCodUsuario()!=null?logDTO.getCodUsuario():null);
			log.getEmpleado().setNombresCompletos(logDTO.getNombreUsuario()!=null?logDTO.getNombreUsuario():null);
			log.getEmpleado().getPerfil().setDescripcion(logDTO.getPerfilUsuario()!=null?logDTO.getPerfilUsuario():null);
			log.getEstado().setDescripcion(logDTO.getEstadoExpediente()!=null?logDTO.getEstadoExpediente():null);
			log.setFecRegistro(logDTO.getFechaHoraAtencion() !=null?convertStringToTimestamp(logDTO.getFechaHoraAtencion()):null);
			log.setDescripcion(logDTO.getEventoRealizado()!=null?logDTO.getEventoRealizado():null);
			log.setNumTerminal(logDTO.getTerminal()!=null?logDTO.getTerminal():null);
						
			listLog.add(log);
		}
		return listLog;
	}
	
	public static List<DocumentoExpTc> convertToDocumentoExp(List<DatosDocumentosExpIiceDTO> listDocExpDTO) {
		List<DocumentoExpTc> listDocExp = new ArrayList<DocumentoExpTc>();
		
		for(DatosDocumentosExpIiceDTO docExpDTO: listDocExpDTO){
			if(docExpDTO==null) continue;
			
			DocumentoExpTc docExp = new DocumentoExpTc();
			docExp.setTipoDocumento(new TipoDocumento());
			docExp.setPersona(new Persona());
			
			docExp.setId(docExpDTO.getCorrelativoDocExp()!=null? Long.parseLong(docExpDTO.getCorrelativoDocExp()):0);
			docExp.setIdCm(docExpDTO.getCorrelativoDoc()!=null? new BigDecimal(docExpDTO.getCorrelativoDoc()):null);
			docExp.getTipoDocumento().setId(docExpDTO.getCorrelativoTipoDoc()!=null? Long.parseLong(docExpDTO.getCorrelativoTipoDoc()):0);
			docExp.setFecReg(docExpDTO.getFechaCreacion()!=null? convertStringToTimestamp(docExpDTO.getFechaCreacion()):null);
			//fecha modificacion
			docExp.setFecVen(docExpDTO.getFechaVencimiento()!=null? convertStringToTimestamp(docExpDTO.getFechaVencimiento()):null);
			docExp.getTipoDocumento().setCodigo(docExpDTO.getTipoDocumentoCodigo()!=null? docExpDTO.getTipoDocumentoCodigo():null);
			docExp.getTipoDocumento().setDescripcion(docExpDTO.getTipoDocumentoDescripcion()!=null? docExpDTO.getTipoDocumentoDescripcion():null);
			
			docExp.setNombre(docExpDTO.getTipoDocumentoDescripcion()!=null? docExpDTO.getTipoDocumentoDescripcion():null);
			docExp.getPersona().setCodigo(docExpDTO.getUsuarioExpediente()!=null? docExpDTO.getUsuarioExpediente():null);
			//perfil
			//estado
			//flag adjunto
			
			listDocExp.add(docExp);
		}
		return listDocExp;
	}
	
		
}