<%@ page import="dbs_fussball.model.*"%>
<%@ page import="java.text.NumberFormat"%>
<%@ page import="java.text.ParseException"%>
<%@ page import="java.util.Locale"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<% Person currentPerson = (Person) request.getAttribute("person"); %>
<% NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>DBS Projekt - <%= currentPerson.getDisplayName()  %></title>
</head>
<body>
<div>
<h1><%= currentPerson.getDisplayName() %></h1>
<form method="post">
<table>
	<tr>
		<td>Vorname:</td>
		<td><input name="firstName" type="text"
			<%= currentPerson.getFirstName() != null ? "value=\"" + currentPerson.getFirstName() + "\"" : "" %> />
		</td>
	</tr>
	<tr>
		<td>Nachname:</td>
		<td><input name="lastName" type="text"
			<%= currentPerson.getLastName() != null ? "value=\"" + currentPerson.getLastName() + "\"" : "" %> />
		</td>
	</tr>
	<tr>
		<td>Spielername:</td>
		<td><input name="stageName" type="text"
			<%= currentPerson.getStageName() != null ? "value=\"" + currentPerson.getStageName() + "\"" : "" %> />
		</td>
	</tr>
	<tr>
		<td>Gewicht:</td>
		<td><input name="weight" type="text"
			<%= currentPerson.getWeight() != null ? "value=\"" + nf.format(currentPerson.getWeight()) + "\"" : "" %> />
		</td>
	</tr>
	<tr>
		<td>Größe:</td>
		<td><input name="height" type="text"
			<%= currentPerson.getHeight() != null ? "value=\"" + nf.format(currentPerson.getHeight()) + "\"" : "" %> />
		</td>
	</tr>
	<tr>
		<td>Letzter Verein:</td>
		<td><input name="club" type="text"
			<%= currentPerson.getClub() != null ? "value=\"" + currentPerson.getClub() + "\"" : "" %> />
		</td>
	</tr>
</table>
<input type="submit" value="Ändern" /></form>
<form action="<%= this.getServletContext().getContextPath() + "/person/" + currentPerson.getId() + "/delete" %>" method="post"><input type="submit" value="Löschen" /></form>
</div>
</body>
</html>