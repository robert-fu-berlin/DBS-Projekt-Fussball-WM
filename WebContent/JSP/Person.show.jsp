<%@ page import="dbs_fussball.model.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<% Person currentPerson = (Person) request.getAttribute("person"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>DBS Projekt - <%= currentPerson.getDisplayName()  %></title>
</head>
<body>
	<div>
		<h1><%= currentPerson.getDisplayName() %></h1>
		<table>
			<% if (currentPerson.getBirthDate() != null) { %>
			<tr>
				<td>Geboren am:</td>
				<td><%= currentPerson.getBirthDate() %></td>
			</tr>
			<% } %>
			<% if (currentPerson.getWeight() != null) { %>
			<tr>
				<td>Gewicht:</td>
				<td><%= currentPerson.getWeight() %>kg</td>
			</tr>
			<% } %>
			<% if (currentPerson.getHeight() != null) { %>
			<tr>
				<td>Größe:</td>
				<td><%= currentPerson.getHeight() %>m</td>
			</tr>
			<% } %>
			<% if (currentPerson.getClub() != null) { %>
			<tr>
				<td>Letzter Verein:</td>
				<td><%= currentPerson.getClub() %></td>
			</tr>
			<% } %>
		</table>
	</div>
</body>
</html>