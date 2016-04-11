package com.ibm.bbva.ctacte.business.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.bbva.ws.ldap.servidor.Usuario;
import pe.com.bbva.ws.ldap.servidor.WSLDAPServiceImplService;

import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.Oficina;
import com.ibm.bbva.ctacte.bean.auxiliares.EmpleadoAuxiliar;
import com.ibm.bbva.ctacte.business.UploadWorkersBusiness;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.OficinaDAO;

@Stateless
@Local(UploadWorkersBusiness.class)
public class UploadWorkersBusinessImpl implements UploadWorkersBusiness {

	private static final Logger LOG = LoggerFactory.getLogger(UploadWorkersBusinessImpl.class);
	
	@EJB
	private EmpleadoDAO empleadoDAO;
	
	@EJB
	private OficinaDAO oficinaDAO;
	
	
	
	public UploadWorkersBusinessImpl() {
		
	}

	@Override
	public List<EmpleadoAuxiliar> updateWorkerData() {
	
		List<EmpleadoAuxiliar> empleadosAuxiliares =  new ArrayList<EmpleadoAuxiliar>();
		try {
			
		List<Oficina> oficinasBD = oficinaDAO.findAllOrderedByCodigo(false);
		LOG.info("Cantidad oficinas: " + oficinasBD.size());
		
		List<Empleado> empeladosBD = new ArrayList<Empleado>();
		for(Oficina oficina : oficinasBD){
			LOG.info("oficina: " + oficina.getCodigo() + " - " + oficina.getDescripcion());
			empeladosBD = empleadoDAO.getEmpleadosPorOficina(oficina.getId());	
			LOG.info("Cantidad empleados de la oficina " + oficina.getCodigo() + " - " + oficina.getDescripcion() + ": " + empeladosBD.size());
			for(Empleado empleado : empeladosBD){
				empleado.setFlagActivo("0");
				empleadoDAO.save(empleado);
				LOG.info("Se actualizo el empleado: " + empleado.getCodigo() + " a inactivo");
			}
		}
		
//		for(EmpleadoAuxiliar aux : empleadosAuxiliares){
//			reasigneFile(aux);
//		}
		
		
		// TODO: Implementar la carga de empleados LDAP
		
		URL url = new URL("http://118.180.34.112:9080/ws-ldap3/wService?wsdl");
        WSLDAPServiceImplService proxy = new WSLDAPServiceImplService(url);
        List<Usuario> empleadosLDAP = new ArrayList<Usuario>();
        empleadosLDAP = proxy.getWSLDAPServiceImplPort().obtenerUsuarios(new ArrayList<String>());
        LOG.info("Cantidad empleados del SW: " + empleadosLDAP.size());
		
		Set<String> codigos = new HashSet<String>();
		for (Usuario empleadoLDAP : empleadosLDAP) {
			if (empleadoLDAP != null && empleadoLDAP.getUsuario() != null
					&& !(empleadoLDAP.getUsuario().length() > 0)) {
				Empleado empleado = empleadoDAO.getByUserCode(empleadoLDAP
						.getUsuario());
				if (empleado != null) {
					workerProcess(empleado, empleadoLDAP, empleadosAuxiliares);
				}
			}
		}
		} catch (Exception e) {
			LOG.info("Error", e);
		}
		return empleadosAuxiliares;
		
	}

	

	private void reasigneFile(EmpleadoAuxiliar aux) {
		if(aux.isEsActivo()==false && aux.isEsOficinaNueva()==true){
			//reaisgnarExpediente dentro de la otra fociina.
		}else if (aux.isEsActivo()==false && aux.isEsOficinaNueva()==false){
			//reaisgnarExpediente dentro de la misma fociina.
		}
	}

	private void workerProcess(Empleado empleadoBD, Usuario empleadoLDAP, List<EmpleadoAuxiliar> empleadosAuxiliares ) {
		empleadoBD.setFlagActivo(empleadoLDAP.isInactivo() ? "0" : "1");
		
		Oficina oficina = empleadoBD.getOficina();
		
		if(!empleadoBD.getOficina().getCodigo().equalsIgnoreCase(empleadoLDAP.getCodigoCentro())){
			empleadoBD.setOficina(oficinaDAO.findByCodigo(empleadoLDAP.getCodigoCentro()));
		}
		
		empleadoBD.setNomcargo(empleadoLDAP.getNombreCargoLocal());
		empleadoBD.setNombres(empleadoLDAP.getNombres());
		empleadoBD.setApepat(empleadoLDAP.getPrimerApellido());
		empleadoBD.setApemat(empleadoLDAP.getSegundoApellido());
		empleadoBD.setNombresCompletos(empleadoLDAP.getNombres() + " " +  empleadoLDAP.getPrimerApellido() + " " +empleadoLDAP.getSegundoApellido() );
	
		EmpleadoAuxiliar empleadoAuxiliar  =  new EmpleadoAuxiliar();
		empleadoAuxiliar.setEmpleado(empleadoBD);
		empleadoAuxiliar.setEsActivo(empleadoBD.getFlagActivo().equalsIgnoreCase("0") ? false : true);
		empleadoAuxiliar.setEsOficinaNueva(!empleadoBD.getOficina().getCodigo().equalsIgnoreCase(empleadoLDAP.getCodigoCentro()));
		empleadoAuxiliar.setOficinaAntigua(oficina);
		empleadoAuxiliar.setOficinaNueva(empleadoBD.getOficina());
		empleadosAuxiliares.add(empleadoAuxiliar);
		empleadoDAO.save(empleadoBD);
	}

}
