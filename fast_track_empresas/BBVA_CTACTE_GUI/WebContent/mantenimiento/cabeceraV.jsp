<%
    String msg = "";
    String sql=request.getParameter("sql");
    if(sql==null)sql="";
    sql=sql.trim();

	response.getWriter().println("<form method='post'>");
	response.getWriter().println("<textarea name='sql' cols='90' rows='10' style='border:1px solid #98bf21;'>");
	response.getWriter().println(sql);
	response.getWriter().println("</textarea>");
	response.getWriter().println("<br><input type='submit' value='Enviar' style='font-size:11px;font-weight:normal;background-color:#A7C942; color:#ffffff;border:1px solid green;'>");
	response.getWriter().println("</form>");
	response.getWriter().println("<hr>");

    if(sql.length()>0){
      java.sql.Connection cn=null;
       try {
			javax.naming.InitialContext ic = new javax.naming.InitialContext();
			javax.sql.DataSource ds = (javax.sql.DataSource) ic.lookup("jdbc/VISPOD");
			cn=ds.getConnection();
			
			/*
				Class.forName("oracle.jdbc.driver.OracleDriver");
				String url = "jdbc:oracle:thin:@localhost:1521:xe";
				cn=java.sql.DriverManager.getConnection(url,"scott","tiger");
			*/
			
			String[] asql=sql.split(";");
			for(int i=0;i<asql.length;i++){
				response.getWriter().println("<hr>");
				response.getWriter().println("<font color='#0000FF'>Consulta: ["+i+"] --> "+asql[i]+" </font><br>");
				asql[i]=asql[i].trim();

				if(asql[i].indexOf("select")==0 || asql[i].indexOf("SELECT")==0 ){
					java.sql.ResultSet rs = cn.createStatement().executeQuery(asql[i]);
					response.getWriter().println("<table  border='1' style='border-color:#A7C942; border:1px solid #98bf21;' >");					
						response.getWriter().println("<tr>");
						response.getWriter().println("<th></th>");					
						for(int j=1;j<=rs.getMetaData().getColumnCount();j++){
							response.getWriter().println("<td style='font-size:11px;'>");
							response.getWriter().println(rs.getMetaData().getColumnTypeName(j));
							if(!rs.getMetaData().getColumnTypeName(j).equals("BLOB")){
								response.getWriter().println("("+rs.getMetaData().getPrecision(j)+","+rs.getMetaData().getScale(j)+")");
							}
							response.getWriter().println("</td >");							
						}
						response.getWriter().println("</tr>");
						
						response.getWriter().println("<tr>");
						response.getWriter().println("<th style='font-size:10px;'> Fila </th>");
						for(int j=1;j<=rs.getMetaData().getColumnCount();j++){
							response.getWriter().println("<th style='background-color:#A7C942; color:#ffffff;font-size:11px;border:1px solid #98bf21;padding:3px 7px 2px 7px'>");
							response.getWriter().println(rs.getMetaData().getColumnName(j));
							response.getWriter().println("</th>");
						}
						response.getWriter().println("</tr>");

						while (rs.next()) {
						
						response.getWriter().println("<tr>");
						response.getWriter().println("<td style='font-size:10px;'>"+rs.getRow()+"</td>");
						for(int j=1;j<=rs.getMetaData().getColumnCount();j++){
								if(!rs.getMetaData().getColumnTypeName(j).equals("BLOB")){
									response.getWriter().println("<td style='color:#000000;background-color:#EAF2D3;font-size:12px;'>"+rs.getString(j)+"</td>");
								}else{
									response.getWriter().println("<td style='color:#000000;background-color:#EAF2D3;font-size:12px;'>...</td>");
								}
							}
						response.getWriter().println("</tr>");
						}
					
					response.getWriter().println("</table>");
			  
          }else{
              cn.createStatement().execute(asql[i]);
              response.getWriter().println("Se ha ejecutado correctamente <br>");
          }


        }

		} catch (Exception e) {
			response.getWriter().println("<h3>Error BD </h3>");
			response.getWriter().println("<hr>");
			response.getWriter().println("<font color='#FF0000'>"+e.getMessage()+"</font>");
		}finally{
			if (cn!=null) cn.close();
		}

    }else{
          response.getWriter().println("Instruccion vacio, intente nuevamente.<br>");
    }

%>
