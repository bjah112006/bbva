package iist.ws;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ServiceWFTarjetas {
	
	/*private StringBuffer QUERY =new StringBuffer()
									.append(" select ") 
									.append(" s.id_estado id_estado,") 
									.append(" e.descripcion descripcion")
									.append(" from IIST.tiist_tarjetas t,IIST.tiist_solicitud s,IIST.tiist_estado e")
									.append(" where t.id_tarjetas=s.id_tarjetas")
									.append(" and s.id_estado=e.id_estado");*/
	
	private StringBuffer QUERY2 =new StringBuffer()
	.append(" SELECT t.id_tarjetas, t.nro_tarjeta, t.num_contrato_tarjeta, t.num_documento,")
	.append(" e.descripcion ||' - '||s.descripcion as descripcion,t.id_situacion ,t.id_estado,s.id_estado as idestadoSitu ")
	.append(" FROM IIST.tiist_tarjetas t ,IIST.tiist_situacion s,IIST.tiist_estado e ")
	.append(" WHERE t.id_situacion = s.id_situacion ")
	.append(" AND t.id_estado = s.id_estado ")
	.append(" AND s.id_estado = e.id_estado ");
									
	private String error;
	private String descError;
	private String codEstado = "";
	private String descEstado = "";
	
	private DataSource fuenteDatos = null;

    public String devolverEstadoEntregaTC(String numeroContrato,String numeroTarjeta) {
        Connection conn = null;
        System.out.println("===== devolverEstadoEntregaTC() =======");
        try{
        	 
        	 System.out.println(" numeroContrato: "+numeroContrato);
        	 System.out.println(" numeroTarjeta: "+numeroTarjeta);        	 
        	 error = "00";
        	 descError = "Exitoso";
        	 codEstado = padRight(codEstado,2 ," ");
        	 descEstado = padRight(descEstado,45 ," ");
        	 Context contextoInicial = new InitialContext(); 
    		 fuenteDatos = (DataSource) contextoInicial.lookup("jdbc/IIST");
    		 conn = fuenteDatos.getConnection();
    		 if(conn != null) {
    	        QUERY2.append(" AND ").
    	        append(" substr(t.nro_tarjeta,13,4)=?").append(" AND ").
    	        append(" t.num_contrato_tarjeta=?");   
    	        
    	        System.out.println("Query : "+QUERY2.toString());
    	        
    	        PreparedStatement statement = conn.prepareStatement(QUERY2.toString());
    	        statement.setString(1, numeroTarjeta);
    	        statement.setString(2, numeroContrato);
    	        ResultSet result = statement.executeQuery();
    	        if (result.next()) {
    	        	codEstado = result.getString("id_estado");
    	        	descEstado = result.getString("descripcion");
    	        }   
    	        codEstado = padRight(codEstado,2 ," ");
	        	descEstado = padRight(descEstado,45 ," ");
	        	
	        	System.out.println("[RESULTADO]--->  codEstado: "+codEstado);
	        	System.out.println("[RESULTADO]--->  descEstado: "+descEstado);
    	     }
        } catch (NamingException e) {
        	descError = "No se encontro el JNDI";
        	error="01";
			e.printStackTrace();
		} catch (SQLException e) {
			error="01";
			descError = "Se encontro un error en la base de datos";
			e.printStackTrace();
		}catch (Exception e) {
			descError = "Se encontro un problema";
			error="01";
			e.printStackTrace();
		}
        return error+"|"+descError+"|"+codEstado+"|"+descEstado;
    }

    public static String padLeft(String cadena, int size, String filler) {
		String strOutput = "";
		String strTempFiller = "";
		for (int i = 0; i < size; i++) {
			strTempFiller = strTempFiller.concat(filler);
		}
		strOutput = strTempFiller.concat(cadena);
		strOutput = strOutput.substring(strOutput.length() - size, strOutput.length());
		return strOutput;
	}

	public static String padRight(String cadena, int size, String filler) {
		String strOutput = "";
		String strTempFiller = "";
		for (int i = 0; i < size; i++) {
			strTempFiller = strTempFiller.concat(filler);
		}
		strOutput = cadena.concat(strTempFiller);
		strOutput = strOutput.substring(0, size);
		return strOutput;
	}
}
