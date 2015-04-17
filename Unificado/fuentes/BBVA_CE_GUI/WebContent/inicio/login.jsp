<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title>Contratación Sencilla</title>
	
	<style type="text/css">
		label.EtiquetaAutoescalable {
			display: inline;
			float: left;
			padding-right: 5px;
			padding-top: 5px;
			vertical-align: bottom;
		}
		
		.DivPagina {
			width: 100%;
			position: relative;
			top: 50%;
			min-height: 100%;
			height: 92%;
		}
		
		.DivBotonera {
			position: absolute;
			bottom: 10px;
			right: 0px;
			width: 100%;
		}
		
		label.tituloVentanaIzq {
			float: none;
			padding-left: 12px;
			margin-left: 8px;
			margin-top: 2px;
			font-weight: bold;
			color: #70B2D2;
		}
		
		.DivContenedorVentana {
			margin: 2px auto;
			width: 50%;
			margin-left: auto;
		}
		
		table.TablaContenidoVentana {
			width: 50%;
			border-style: solid;
			border-color: #FBFCFE;
			background-color: white;
		}
		
		.BotonMargenIzq {
			margin-right: 8px;
		}
		
		.BotonMargenDer {
			border: 0px;
			background-color: #3884C2;
			color: white;
		}
		
		.alinearDer {
			text-align: right;
			padding-right: 8px;
		}
	</style>
</head>

<body bgcolor="#F2F6F9">

	<div>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			style="border-spacing: 0px;" align="center" bgcolor="white">
			<tbody>
				<tr height="70px">
					<td width="50%" valign="middle"></td>
					<td width="50%" align="center">
						<label id="tituloventana1" style="text-align: center; font-size: 30px; 
							color: #5C95B3; font-weight: bold; font-family: 'BBVA Web Light Regular';">
							Contratación Sencilla
						</label>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<form method="post" action="j_security_check">
		<br />
		<br />
		<br />
		<div class="DivPagina">
			<div>
				<center>
					<p>
						<label id="tituloventana1" style="text-align: center; font-size: 25px; color: #5C95B3; 
							font-family: 'BBVA Web Light Regular';">
							Acceso Usuario Contraseña
						</label>
					</p>
				</center>
			</div>

			<div class="DivContenedorVentana" align="center">
				<table width="100%" class="TablaContenidoVentana" border="2px;"
					style="border-color: #FBFCFE; background-color: white;"
					align="center">
					<tr>
						<td>
							<div class="DivContenedorCaja">
								<table width="100%" border="0" cellspacing="0" cellpadding="0" class="TablaContenidoVentana" align="center">
									<tr>
										<td height="15">
											<label id="lbl_17"></label>
										</td>
									</tr>
									<tr>
										<td width="90%" align="center">
											<input size="40" height="30" type="text" name="j_username" maxlength="25" value="" class="CampoSalida"></input>
										</td>
									</tr>
									<tr>
										<td height="12">
											<label id="lbl_17"></label>
										</td>
									</tr>
									<tr>
										<td width="90%" align="center">
											<input size="40" height="35" type="password" name="j_password" maxlength="25" value="" class="CampoSalida"></input>
										</td>
									</tr>
									<tr>
										<% if (request.getParameter("errorLogin") != null && request.getParameter("errorLogin").equals("true")) { %>
										<td>
											<span style="color: red;">Usuario y/o contraseña incorrecta</span>
										</td>
										<% } %>
									</tr>
								</table>
							</div>

							<div>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td height="12">
											<label id="lbl_17"></label>
										</td>
									</tr>
									<tr>
										<td width="50%" align="center">
											<!--  <label id="tituloventana1"  style="text-align: center; font-size: 14px; color: #5C95B3; font-weight: bolder; font-family: 'BBVA Web Book Regular';" >Desbloquear usuario</label>-->
										</td>
										<td width="50%" align="right">
											<input type="submit" name="action" value="Aceptar"></input>
										</td>
									</tr>
									<tr>
										<td height="15">
											<label id="lbl_17"></label>
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<br />
		<br />
		<br />
	</form>

</body>

</html>