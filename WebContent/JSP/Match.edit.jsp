<jsp:include page="Templates/Header.jsp" />
<%@ page import="dbs_fussball.model.*"%>
<%@ page import="com.google.common.collect.Sets" %>
<% Match match = (Match) request.getAttribute("match");
   String teamA = match.getTeamA() != null ? match.getTeamA().getCountry().code : "???";
   String teamB = match.getTeamA() != null ? match.getTeamB().getCountry().code : "???"; %>
<h1 class="match-score"><%= match.getGoalsA() %>:<%= match.getGoalsB() %></h1>
<h2 class="match-teams"><%= teamA %> vs <%= teamB %></h2>
<div>
	<form method="post">
		<fieldset>
		<legend>
			Aufstellung für <%= match.getTeamA().getCountry() %>
		</legend>
		<% if (match.getTeamA() != null) { %>
			<% for (Integer i = 0; i < 11; i++) { %>
				<label>Spieler <%= (i+1) %></label>
				<select>
					<option value=""></option>
					<% for (Person p : match.getTeamA().players()) { %>
						<option value="<%= p.getId() %>"><%= p.getDisplayName() %></option>
					<% } %>
				</select>
			<% } %>
		<% } %>
		</fieldset>
		
		<fieldset>
		<legend>
			Aufstellung für <%= match.getTeamB().getCountry() %>
		</legend>
		<% if (match.getTeamB() != null) { %>
			<% for (Integer i = 0; i < 11; i++) { %>
				<label>Spieler <%= (i+1) %></label>
				<select>
					<option value=""></option>
					<% for (Person p : match.getTeamB().players()) { %>
						<option value="<%= p.getId() %>"><%= p.getDisplayName() %></option>
					<% } %>
				</select>
			<% } %>
		<% } %>
		</fieldset>
		
		<fieldset class="events">
		<table>
			<% for (Event e : match.events()) { %>
				<tr>
					<td><%= e.getType() %></td>
					<td><%= e.getTime() %></td>
					<td><%= e.getPrimary() %></td>
					<td><%= e.getSecondary() %></td>
					<td><input type="button" /></td>
				</tr>
			<% } %>
			<tr>
				<td>
					<select>
						<% for (Event.Type t : Event.Type.values()) { %>
							<option><%= t %></option>
						<% } %>
					</select>
				</td>
				<td>
					<select>
						<% for (Person p : Sets.union(Sets.newHashSet(match.getTeamA().players()), Sets.newHashSet(match.getTeamB().players()))) { %>
							<option><%= p.getDisplayName() %></option>
						<% } %>
					</select>
				</td>
				<td>
					<select>
						<% for (Person p : Sets.union(Sets.newHashSet(match.getTeamA().players()), Sets.newHashSet(match.getTeamB().players()))) { %>
							<option><%= p.getDisplayName() %></option>
						<% } %>
					</select>
				</td>
			</tr>
		</table>
		</fieldset>
	</form>
</div>
<jsp:include page="Templates/Footer.jsp" />