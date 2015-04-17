package pagecode;



import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import java.io.IOException;
import java.lang.String;

/**
 * Provides a common base class for all generated code behind files.
 */
public abstract class PageCodeBase {

	/**
	 * Agrega un mensaje de error al contexto de la pantalla.
	 * 
	 * @param e
	 *            - Excepción cuyo mensaje se va a agregar.
	 */
	public void addErrorMessage(Exception e) {
		if (e != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, e.getCause() != null ? e
							.getCause().getMessage() : e.getMessage(), ""));
		}
	}

	

	/**
	 * Agrega un mensaje de error al contexto de la pantalla.
	 * 
	 * @param e
	 *            -Mensaje a agregar.
	 */
	public void addErrorMessage(String mensaje) {
		if (mensaje != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, mensaje, ""));
		}
	}

	/**
	 * Agrega un mensaje de error a un componente gráfico de la pantalla.
	 * 
	 * @param idComponente
	 *            Id del componente al cual se le va a asociar el mensaje de
	 *            error.
	 * @param mensaje
	 *            Mensaje de error a mostrar para el componente.
	 */
	public void addComponentMessage(String idComponente, String mensaje) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(idComponente, new FacesMessage(mensaje));
	}



	/**
	 * Return the managed bean with the given name
	 * 
	 * @param mgdBeanName   the name of the managed bean to retrieve
	 * @return
	 */
	/*protected Object getManagedBean(String mgdBeanName) {
		String expression = "#{" + mgdBeanName + "}";
		return resolveExpression(expression);
	}*/

	protected ExternalContext getExternalContext () {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
	
	protected void redirectAction (String pagina, String[]... parametros) {
		StringBuilder sb = new StringBuilder(pagina).append(".jsf");
		if (parametros != null) {
			boolean esPrimero =  true;
			for (String[] param : parametros) {
				if (esPrimero) {
					sb.append('?');
				} else {
					sb.append('&');
				}
				sb.append(param[0]).append('=').append(param[1]);
				esPrimero = false;
			}
		}
		
		try {
			getExternalContext().redirect(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}