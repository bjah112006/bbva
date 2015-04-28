package com.ibm.bbva.ctacte.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.ibm.bbva.ctacte.business.ExpedienteBusiness;
import com.ibm.bbva.ctacte.dao.AuditoriaBastanteoDAO;
import com.ibm.bbva.ctacte.dao.AuditoriaClienteDAO;
import com.ibm.bbva.ctacte.dao.AuditoriaCriteriosSupervisionDAO;
import com.ibm.bbva.ctacte.dao.AyudaMemoriaDAO;
import com.ibm.bbva.ctacte.dao.CampaniaDAO;
import com.ibm.bbva.ctacte.dao.CarterizacionDAO;
import com.ibm.bbva.ctacte.dao.ClienteDAO;
import com.ibm.bbva.ctacte.dao.CobroComisionDAO;
import com.ibm.bbva.ctacte.dao.CuentaDAO;
import com.ibm.bbva.ctacte.dao.DocumentoCMDAO;
import com.ibm.bbva.ctacte.dao.DocumentoDAO;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.DocumentoHistDAO;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.EscaneoWebDAO;
import com.ibm.bbva.ctacte.dao.EstadoExpedienteDAO;
import com.ibm.bbva.ctacte.dao.EstadoTareaDAO;
import com.ibm.bbva.ctacte.dao.EstudioAbogadoDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteCuentaDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteTareaDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteTareaProcesoDAO;
import com.ibm.bbva.ctacte.dao.GuiaDocumentDAO;
import com.ibm.bbva.ctacte.dao.HistorialDAO;
import com.ibm.bbva.ctacte.dao.HorarioDAO;
import com.ibm.bbva.ctacte.dao.HorarioOficinaDAO;
import com.ibm.bbva.ctacte.dao.LDAPUsuarioDAO;
import com.ibm.bbva.ctacte.dao.ListaCerradaDAO;
import com.ibm.bbva.ctacte.dao.LogDAO;
import com.ibm.bbva.ctacte.dao.MotivoDAO;
import com.ibm.bbva.ctacte.dao.OficinaDAO;
import com.ibm.bbva.ctacte.dao.OperacionDAO;
import com.ibm.bbva.ctacte.dao.ParametrosConfDAO;
import com.ibm.bbva.ctacte.dao.ParametrosVistaUnicaDAO;
import com.ibm.bbva.ctacte.dao.ParticipeDAO;
import com.ibm.bbva.ctacte.dao.PerfilBalanceoDAO;
import com.ibm.bbva.ctacte.dao.PerfilDAO;
import com.ibm.bbva.ctacte.dao.PerfilXNivelDAO;
import com.ibm.bbva.ctacte.dao.PlazoSubsanacionDAO;
import com.ibm.bbva.ctacte.dao.ProductoDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;
import com.ibm.bbva.ctacte.dao.TareaExpedienteDAO;
import com.ibm.bbva.ctacte.dao.TareaPerfilDAO;
import com.ibm.bbva.ctacte.dao.TareasReasigDAO;
import com.ibm.bbva.ctacte.dao.TerritorioDAO;
import com.ibm.bbva.ctacte.dao.ViewNumeroExpedientesEmpleadoDAO;
import com.ibm.bbva.ctacte.dao.ViewPesoDocumentoExpedienteDAO;
import com.ibm.bbva.ctacte.dao.ViewPesoParticipeExpedienteDAO;
import com.ibm.bbva.ctacte.dao.servicio.CoreDAO;
import com.ibm.bbva.ctacte.dao.servicio.SistemaFirmasPoderesDAO;

public class EJBLocator {
	
	public static Object lookupEJBLocal(Class o) {
		try {
			return new InitialContext().lookup("ejblocal:" + o.getCanonicalName());
		} catch (NamingException e) {
			return null;
		}
	}
	
	public static EstadoExpedienteDAO getEstadoExpedienteDAO() {
		return (EstadoExpedienteDAO) lookupEJBLocal(EstadoExpedienteDAO.class);
	}

	public static CampaniaDAO getCampaniaDAO() {
		return (CampaniaDAO) lookupEJBLocal(CampaniaDAO.class);
	}

	public static ClienteDAO getClienteDAO() {
		return (ClienteDAO) lookupEJBLocal(ClienteDAO.class);
	}

	public static DocumentoDAO getDocumentoDAO() {
		return (DocumentoDAO) lookupEJBLocal(DocumentoDAO.class);
	}

	public static DocumentoExpDAO getDocumentoExpDAO() {
		return (DocumentoExpDAO) lookupEJBLocal(DocumentoExpDAO.class);
	}

	public static DocumentoHistDAO getDocumentoHistDAO() {
		return (DocumentoHistDAO) lookupEJBLocal(DocumentoHistDAO.class);
	}

	public static EmpleadoDAO getEmpleadoDAO() {
		return (EmpleadoDAO) lookupEJBLocal(EmpleadoDAO.class);
	}

	public static EstadoTareaDAO getEstadoTareaDAO() {
		return (EstadoTareaDAO) lookupEJBLocal(EstadoTareaDAO.class);
	}

	public static ExpedienteDAO getExpedienteDAO() {
		return (ExpedienteDAO) lookupEJBLocal(ExpedienteDAO.class);
	}

	public static GuiaDocumentDAO getGuiaDocumentDAO() {
		return (GuiaDocumentDAO) lookupEJBLocal(GuiaDocumentDAO.class);
	}

	public static HistorialDAO getHistorialDAO() {
		return (HistorialDAO) lookupEJBLocal(HistorialDAO.class);
	}

	public static ListaCerradaDAO getListaCerradaDAO() {
		return (ListaCerradaDAO) lookupEJBLocal(ListaCerradaDAO.class);
	}

	public static MotivoDAO getMotivoDAO() {
		return (MotivoDAO) lookupEJBLocal(MotivoDAO.class);
	}

	public static OficinaDAO getOficinaDAO() {
		return (OficinaDAO) lookupEJBLocal(OficinaDAO.class);
	}

	public static OperacionDAO getOperacionDAO() {
		return (OperacionDAO) lookupEJBLocal(OperacionDAO.class);
	}

	public static ParticipeDAO getParticipeDAO() {
		return (ParticipeDAO) lookupEJBLocal(ParticipeDAO.class);
	}

	public static PerfilDAO getPerfilDAO() {
		return (PerfilDAO) lookupEJBLocal(PerfilDAO.class);
	}

	public static ProductoDAO getProductoDAO() {
		return (ProductoDAO) lookupEJBLocal(ProductoDAO.class);
	}

	public static TareaDAO getTareaDAO() {
		return (TareaDAO) lookupEJBLocal(TareaDAO.class);
	}

	public static TareaPerfilDAO getTareaPerfilDAO() {
		return (TareaPerfilDAO) lookupEJBLocal(TareaPerfilDAO.class);
	}

	public static TerritorioDAO getTerritorioDAO() {
		return (TerritorioDAO) lookupEJBLocal(TerritorioDAO.class);
	}

	public static SistemaFirmasPoderesDAO getSistemaFirmasPoderesDAO() {
		return (SistemaFirmasPoderesDAO) lookupEJBLocal(SistemaFirmasPoderesDAO.class);
	}

	public static CoreDAO getCoreDAO() {
		return (CoreDAO) lookupEJBLocal(CoreDAO.class);
	}
	public static PlazoSubsanacionDAO getPlazoSubsanacionDAO() {
		return (PlazoSubsanacionDAO) lookupEJBLocal(PlazoSubsanacionDAO.class);
	}
	
	public static ViewPesoParticipeExpedienteDAO getViewPesoParticipeExpedienteDAO() {
		return (ViewPesoParticipeExpedienteDAO) lookupEJBLocal(ViewPesoParticipeExpedienteDAO.class);
	}
	
	public static ViewPesoDocumentoExpedienteDAO getViewPesoDocumentoExpedienteDAO() {
		return (ViewPesoDocumentoExpedienteDAO) lookupEJBLocal(ViewPesoDocumentoExpedienteDAO.class);
	}
	
	public static ViewNumeroExpedientesEmpleadoDAO getViewNumeroExpedientesEmpleadoDAO() {
		return (ViewNumeroExpedientesEmpleadoDAO) lookupEJBLocal(ViewNumeroExpedientesEmpleadoDAO.class);
	}

	public static TareaExpedienteDAO getTareaExpedienteDAO() {
		return (TareaExpedienteDAO) lookupEJBLocal(TareaExpedienteDAO.class);
	}
	
	public static AuditoriaClienteDAO getAuditoriaClienteDAO() {
		return (AuditoriaClienteDAO) lookupEJBLocal(AuditoriaClienteDAO.class);
	}
	
	public static AuditoriaBastanteoDAO getAuditoriaBastanteoDAO() {
		return (AuditoriaBastanteoDAO) lookupEJBLocal(AuditoriaBastanteoDAO.class);
	}
	
	public static AuditoriaCriteriosSupervisionDAO getAuditoriaCriteriosSupervisionDAO() {
		return (AuditoriaCriteriosSupervisionDAO) lookupEJBLocal(AuditoriaCriteriosSupervisionDAO.class);
	}
	
	public static PerfilBalanceoDAO getPerfilBalanceoDAO() {
		return (PerfilBalanceoDAO) lookupEJBLocal(PerfilBalanceoDAO.class);
	}

	public static CuentaDAO getCuentaDAO() {
		return (CuentaDAO) lookupEJBLocal(CuentaDAO.class);
	}

	public static ExpedienteTareaDAO getExpedienteTareaDAO() {
		return (ExpedienteTareaDAO) lookupEJBLocal(ExpedienteTareaDAO.class);
	}
	
	public static ExpedienteCuentaDAO getExpedienteCuentaDAO() {
		return (ExpedienteCuentaDAO) lookupEJBLocal(ExpedienteCuentaDAO.class);
	}
	
	public static DocumentoCMDAO getDocumentoCMDAO() {
		return (DocumentoCMDAO) lookupEJBLocal(DocumentoCMDAO.class);
	}

	public static AyudaMemoriaDAO getAyudaMemoriaDAO() {
		return (AyudaMemoriaDAO) lookupEJBLocal(AyudaMemoriaDAO.class);
	}

	public static LogDAO getLogDAO () {
		return (LogDAO) lookupEJBLocal(LogDAO.class);
	}
	
	public static CobroComisionDAO getCobroComisionDAO () {
		return (CobroComisionDAO) lookupEJBLocal(CobroComisionDAO.class);
	}
	
	public static EstudioAbogadoDAO getEstudioAbogadoDAO () {
		return (EstudioAbogadoDAO) lookupEJBLocal(EstudioAbogadoDAO.class);
	}

	public static HorarioDAO getHorarioDAO() {
		return (HorarioDAO) lookupEJBLocal(HorarioDAO.class);
	}

	public static HorarioOficinaDAO getHorarioOficinaDAO() {
		return (HorarioOficinaDAO) lookupEJBLocal(HorarioOficinaDAO.class);
	}

	public static TareasReasigDAO getTareasReasigDAO() {
		return (TareasReasigDAO) lookupEJBLocal(TareasReasigDAO.class);
	}
	
	public static ExpedienteTareaProcesoDAO getExpedienteTareaProcesoDAO() {
		return (ExpedienteTareaProcesoDAO) lookupEJBLocal(ExpedienteTareaProcesoDAO.class);
	}
	
	public static CarterizacionDAO getCarterizacionDAO () {
		return (CarterizacionDAO) lookupEJBLocal(CarterizacionDAO.class);
	}
	
	public static ParametrosVistaUnicaDAO getParametrosVistaUnicaDAO () {
		return (ParametrosVistaUnicaDAO) lookupEJBLocal(ParametrosVistaUnicaDAO.class);
	}

	public static EscaneoWebDAO getEscaneoWebDAO() {
		return (EscaneoWebDAO) lookupEJBLocal(EscaneoWebDAO.class);
	}
	
	public static ExpedienteBusiness getExpedienteBusiness() {
		return (ExpedienteBusiness) lookupEJBLocal(ExpedienteBusiness.class);
	}
	
	public static ParametrosConfDAO getParametrosConfDAO() {
		return (ParametrosConfDAO) lookupEJBLocal(ParametrosConfDAO.class);
	}
	
	public static LDAPUsuarioDAO getLDAPUsuarioDAO() {
		return (LDAPUsuarioDAO) lookupEJBLocal(LDAPUsuarioDAO.class);
	}
	
	public static PerfilXNivelDAO getPerfilXNivelDAO() {
		return (PerfilXNivelDAO) lookupEJBLocal(PerfilXNivelDAO.class);
	}

}
