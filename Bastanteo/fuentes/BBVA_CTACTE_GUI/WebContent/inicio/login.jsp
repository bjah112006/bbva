<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=EDGE"/>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<title>Contratación Electrónica</title>
		<style type="text/css">
			#formulario {
        		border: 2px solid;
        		width: 290px;
			    position: absolute;
			    left: 50%;
			    margin-left: -145px;
			    top: 50%;
			    margin-top: -41px;
        	}
		</style>
	</head>
	<body>
		<form method="post" action="j_security_check">
			<table id="formulario">
				<tbody>
					<tr>
						<td valign="middle">Usuario:</td>
						<td valign="top">
							<input size="30" type="text" name="j_username" maxlength="25" value=""></input>
						</td>
					</tr>
					<tr>
						<td valign="top">Contraseña:</td>
						<td valign="top">
							<input size="30" type="password" name="j_password" maxlength="25" value=""></input>
						</td>
					</tr>
					<% if (request.getParameter("errorLogin") != null && request.getParameter("errorLogin").equals("true")) { %>
					<tr>
						<td colspan="2">
							<span style="color:red;">Usuario y/o contraseña incorrecto</span>
						</td>
					</tr>
					<% } %>
					<tr>
						<td colspan="2" align="right">
							<input type="submit" name="action" value="Ingresar"></input>
							<input type="reset" name="reset" value="Limpiar"></input>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</body>
</html>