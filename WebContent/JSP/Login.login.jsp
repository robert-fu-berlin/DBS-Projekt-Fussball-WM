<jsp:include page="Templates/Header.jsp" />
<h1>Einloggen</h1>
<jsp:include page="Partials/Error.jsp" />
<form method="post">
	<fieldset class="worldcup-group">
		<legend>Einloggen</legend>
		<label>E-Mail</label>
		<% if (request.getAttribute("email") == null)  {  %>
		<input name="email" type="text" />
		<% } else { %>
		<input name="email" type="text" value="<%= request.getAttribute("email") %>" />
		<% } %>
		<label>Passwort</label>
		<input name="password" type="password" />
		<input class="button" type="submit" value="Einloggen!" />
	</fieldset>
</form>
<jsp:include page="Templates/Footer.jsp" />