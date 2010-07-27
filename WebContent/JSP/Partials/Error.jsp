<%@page import="java.util.List"%>
<% if (request.getAttribute("error") != null && request.getAttribute("error") instanceof List) { %>
	<p class="error-message">
		Es sind Fehler aufgetreten:
		<ul>
		<% for (String error : (List<String>) request.getAttribute("error")) { %>
			<li><%= error %></li>	
		<% } %>
		</ul>
	</p>
<% } %>
