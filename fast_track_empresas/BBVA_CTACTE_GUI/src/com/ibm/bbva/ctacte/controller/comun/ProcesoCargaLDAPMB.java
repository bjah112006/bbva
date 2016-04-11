package com.ibm.bbva.ctacte.controller.comun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Proceso;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.dao.ProcesoDAO;
import com.ibm.bbva.ctacte.util.ParametrosSistema;
import com.ibm.mant.tabla.dto.ProcesoDTO;

@ManagedBean (name="procesoCargaLDAP")
@ViewScoped
public class ProcesoCargaLDAPMB  implements Serializable {
	

	private static final long serialVersionUID = 2495749705347718664L;
	private static final Logger LOG = LoggerFactory.getLogger(ProcesoCargaLDAPMB.class);
	
	ParametrosSistema parametros = ParametrosSistema.getInstance();
	Properties props = parametros.getProperties(ParametrosSistema.CARGA);
	
	private List<ProcesoDTO> procesos;
	private String mensaje;
	
	@EJB
	private ProcesoDAO procesoDAO;
	
	
	@PostConstruct
	public void iniciar() {
		Integer cantidad = 0;
		try {
			cantidad = Integer.parseInt(props
					.getProperty(ConstantesParametros.CANTIDAD_REGISTROS));
		} catch (Exception e) {
			LOG.info("Erro al obtener el valor comnfigurado de cantida de registros", e);
		}
		this.procesos = cargarUltimosProcesos(cantidad);

	}


	private List<ProcesoDTO> cargarUltimosProcesos(int cantidad) {
		List<Proceso>  procesosDAO = new ArrayList<Proceso>(); 
		List<ProcesoDTO>  procesosDTO = new ArrayList<ProcesoDTO>(); 
		
		procesosDAO = procesoDAO.listarUltimosProcesos(cantidad);
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss") ;
		
		ProcesoDTO pro;
		for (Proceso p : procesosDAO) {
			try {

				pro = new ProcesoDTO();
				pro.setId(p.getId());
				pro.setNombre(p.getNombre());
				pro.setFechaInicio(df.format(p.getFechaInicio()));
				pro.setFechaFin(p.getFechaFin() != null ? df.format(p
						.getFechaFin()) : "");
				pro.setEstado(p.getDescripcion());
				procesosDTO.add(pro);
			} catch (Exception e) {
				LOG.info("Erro al transformar proceso", e);
			}
		}
		return procesosDTO;
	}
	
	public String cargarEmpleadosLDAP(){

		try {
			if(!existeProcesoPendiente()){
				String myUrl = props.getProperty(ConstantesParametros.URL_CARGA_LDAP_SERVLET) ;
				LOG.info("ConstantesParametros.URL_CARGA_LDAP_SERVLET :"+ConstantesParametros.URL_CARGA_LDAP_SERVLET+" myUrl: "+myUrl);
				String results = doHttpUrlConnectionAction(myUrl);
				LOG.info(results);
				Thread.sleep(5000);
				refrescar();
				mensaje = "Proceso de carga iniciado";
				return "proceso/formProcesoCargaLDAP?faces-redirect=true";
			}else{
				mensaje = "No se inició el proceso de carga ya que existe otro ejecutándose en este momento";
				return "proceso/formProcesoCargaLDAP?faces-redirect=true";
			}
			
		} catch (Exception e) {
			LOG.info("Erro al ejecutar el proceso", e);
		}
		return null;
	}
	
	
	private boolean existeProcesoPendiente() {
		List<Proceso>  procesosDAO = new ArrayList<Proceso>();
		procesosDAO = procesoDAO.getProcesosSinTerminar();
		return !procesosDAO.isEmpty();
	}


	private String doHttpUrlConnectionAction(String desiredUrl)
			  throws Exception
			  {
			    URL url = null;
			    BufferedReader reader = null;
			    StringBuilder stringBuilder;
			 
			    try
			    {
			      // create the HttpURLConnection
			      url = new URL(desiredUrl);
			      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			       
			      // just want to do an HTTP GET here
			      connection.setRequestMethod("GET");
			       
			      // uncomment this if you want to write output to this url
			      //connection.setDoOutput(true);
			       
			      // give it 15 seconds to respond
			      connection.setReadTimeout(15*1000);
			      connection.connect();
			 
			      // read the output from the server
			      reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			      stringBuilder = new StringBuilder();
			 
			      String line = null;
			      while ((line = reader.readLine()) != null)
			      {
			        stringBuilder.append(line + "\n");
			      }
			      return stringBuilder.toString();
			    }
			    catch (Exception e)
			    {
			      e.printStackTrace();
			      throw e;
			    }
			    finally
			    {
			      // close the reader; this can throw an exception too, so
			      // wrap it in another try/catch block.
			      if (reader != null)
			      {
			        try
			        {
			          reader.close();
			        }
			        catch (IOException ioe)
			        {
			          ioe.printStackTrace();
			        }
			      }
			    }
			  }
	
	

	public void refrescar(){
		Integer cantidad = 0;
		try {
			cantidad = Integer.parseInt(props
					.getProperty(ConstantesParametros.CANTIDAD_REGISTROS));
			mensaje = "";
		} catch (Exception e) {
			LOG.info("Erro al obtener el valor comnfigurado de cantida de registros", e);
		}
		this.procesos = cargarUltimosProcesos(cantidad);
	}

	public List<ProcesoDTO> getProcesos() {
		return procesos;
	}


	public void setProcesos(List<ProcesoDTO> procesos) {
		this.procesos = procesos;
	}


	public String getMensaje() {
		return mensaje;
	}


	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	

}
