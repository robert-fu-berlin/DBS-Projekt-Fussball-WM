<%@page import="com.google.common.collect.Lists"%>
<%@page import="dbs_fussball.model.*"%>
<%@page import="java.util.*"%>
<jsp:include page="Templates/Header.jsp" />
<% Match match = (Match) request.getAttribute("match");
   String teamA = match.getTeamA() != null ? match.getTeamA().getCountry().code : "???";
   String teamB = match.getTeamA() != null ? match.getTeamB().getCountry().code : "???"; %>
<h1 class="match-score"><%= match.getGoalsA() %>:<%= match.getGoalsB() %></h1>
<h2 class="match-teams"><%= teamA %> vs <%= teamB %></h2>

<!-- Goals -->
<h2>Tore</h2>
<% // TODO move this into match
   List<Event> goalsA = new ArrayList<Event>(), goalsB = new ArrayList<Event>(); 
   for (Event e : match.events()) {
	   switch (e.getType()) {
	   case GOAL:
	   case PENALTY_GOAL:
	   case OWN_GOAL:
		   if (match.getTeamA().containsAssociate(e.getPrimary()))
			   goalsA.add(e);
		   else
			   goalsB.add(e);
	   }
   }
%>
<table>
	<% for (int i = 0; i < Math.max(goalsA.size(), goalsA.size()); i++) { %>
		<tr>
			<% if (i < goalsA.size()) { %>
				<td><%= goalsA.get(i).getPrimary() %> (<%= goalsA.get(i).getTime() %>')</td>
			<% } else { %>
				<td></td>
			<% } %>
			<% if (i < goalsB.size()) { %>
				<td><%= goalsB.get(i).getPrimary() %> (<%= goalsB.get(i).getTime() %>')</td>
			<% } else { %>
				<td></td>
			<% } %>
		</tr>
	<% } %>
</table>

<!-- Lineups -->
<h2>Aufstellung</h2>
<% // TODO move this into match
   List<Event> exchangesA = new ArrayList<Event>(), exchangesB = new ArrayList<Event>(); 
   for (Event e : match.events()) {
	   switch (e.getType()) {
	   case EXCHANGE:
		   if (match.getTeamA().containsAssociate(e.getPrimary()))
			   exchangesA.add(e);
		   else
			   exchangesB.add(e);
	   }
   }
   List<Person> startingLineupA = Lists.newArrayList(match.playersOfLineUpTeamA());
   List<Person> startingLineupB = Lists.newArrayList(match.playersOfLineUpTeamB());
%>
<table>
	<% for (int i = 0; i < Math.max(startingLineupA.size(), startingLineupB.size()); i++) { %>
		<tr>
			<% if (i < startingLineupA.size()) { %>
				<td><%= startingLineupA.get(i) %></td>
			<% } else { %>
				<td></td>
			<% } %>
			<% if (i < startingLineupB.size()) { %>
				<td><%= startingLineupB.get(i) %></td>
			<% } else { %>
				<td></td>
			<% } %>
		</tr>
	<% } %>
	<!-- Exchanges -->
	<% for (int i = 0; i < Math.max(exchangesA.size(), exchangesB.size()); i++) { %>
		<tr>
			<% if (i < exchangesA.size()) { %>
				<td><%= exchangesA.get(i).getSecondary() %> (<%= exchangesA.get(i).getTime() %>')</td>
			<% } else { %>
				<td></td>
			<% } %>
			<% if (i < exchangesB.size()) { %>
				<td><%= exchangesB.get(i).getSecondary() %> (<%= exchangesB.get(i).getTime() %>')</td>
			<% } else { %>
				<td></td>
			<% } %>
		</tr>
	<% } %>
</table>

<!-- Annotation -->
<% if (match.getAnnotation() != null && !match.getAnnotation().isEmpty()) { %>
	<p class="annotation"><%= match.getAnnotation() %></p>
<% } %>
<jsp:include page="Templates/Footer.jsp" />