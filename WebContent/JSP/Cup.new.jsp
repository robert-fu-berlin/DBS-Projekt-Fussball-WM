<%@page import="com.google.common.collect.ImmutableList"%>
<%@page import="dbs_fussball.model.FifaCountry"%>
<jsp:include page="Templates/Header.jsp" />
<h1>Neue Weltmeisterschaft erstellen</h1>
<p>Welche 32 Mannschaften haben sich für die Weltmeisterschaft qualifiziert?</p>
<jsp:include page="Partials/Error.jsp" />
<form method="post">
	<% for (char group = 'A'; group <= 'H'; group++) { %>
	<fieldset class="worldcup-group">
		<legend>Gruppe <%= group %></legend>
			<% for (int pos = 0; pos < 4; pos++) { %>
			<select name="<%= "group_" + group + "_" + pos %>">
				<option> </option>
				<jsp:include page="Partials/Forms/CountryOptions.jsp">
					<jsp:param value="<%= request.getAttribute("group_" + group + "_" + pos) %>" name="selected"/>
				</jsp:include>
			</select>
			<% } %>
	</fieldset>
	<% } %>
	<input type="submit" value="Weiter!" />
</form>
<jsp:include page="Templates/Footer.jsp" />