<%@page import="dbs_fussball.model.FifaCountry"%>
<% for (FifaCountry country : FifaCountry.values()) { 
	if (request.getParameter("selected") != null && request.getParameter("selected").equals(country.code)) { %>
		<option value="<%= country.code %>" selected="selected"><%= country %></option>
	<% } else { %>
		<option value="<%= country.code %>"><%= country %></option>
	<% } 
} %>