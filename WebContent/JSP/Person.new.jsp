<%@ page import="dbs_fussball.model.*" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="java.util.Locale" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<% NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>DBS Projekt - Neuer Spieler</title>
</head>
<body>
	<div>
		<h1>Neuer Spieler</h1>
		<form method="post">
		<table>
			<tr>
				<td>Vorname:</td>
				<td>
					<input name="firstName" type="text" />
				</td>
			</tr>
			<tr>
				<td>Nachname:</td>
				<td>
					<input name="lastName" type="text" />
				</td>
			</tr>
			<tr>
				<td>Spielername:</td>
				<td>
					<input name="stageName" type="text" />
				</td>
			</tr>
			<tr>
				<td>Gewicht:</td>
				<td>
					<input name="weight" type="text" />
				</td>
			</tr>
			<tr>
				<td>Größe:</td>
				<td>
					<input name="height" type="text" />
				</td>
			</tr>
			<tr>
				<td>Letzter Verein:</td>
				<td>
					<input name="club" type="text" />
				</td>
			</tr>
		</table>
		<input type="submit" value="Submit" />
		</form>
	</div>
</body>
</html>