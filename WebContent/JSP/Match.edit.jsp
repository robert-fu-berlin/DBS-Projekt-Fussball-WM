<jsp:include page="Templates/Header.jsp" />
<%@ page import="dbs_fussball.model.*"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.Set"%>
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
			Aufstellung f&uuml;r <%= match.getTeamA().getCountry() %>
		</legend>
		<% if (match.getTeamA() != null) { %>
			<% Set<Person> displayPerson = new HashSet<Person>(); %>
			<% for (Integer i = 0; i < 11; i++) { %>
				<label>Spieler <%= (i+1) %></label>
				<select name="player_A_<%= i+1 %>">
					<option value=""></option>
					<% boolean skip = false; %>
					<% for (Person p : match.getTeamA().players()) { %>
						<option value="<%= p.getId() %>" <%= match.lineUpTeamAContains(p) && displayPerson.add(p) && !skip ? "selected=\"true\"" : "" %>><%= p.getDisplayName() %></option>
						<% skip = true; %>
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
		<input type="submit" name="action" value="aufstellen" />
		<fieldset class="events">
		<table>
			<% for (Event e : match.events()) { %>
				<tr>
					<td><%= e.getType() %></td>
					<td><%= e.getPrimary() %></td>
					<td><%= e.getSecondary() %></td>
					<td><%= e.getTime() %></td>
					<td><input type="submit" name="action" value="remove_<%= e.getId() %>" /></td>
				</tr>
			<% } %>
			
			<tr>
				<td>
					<select name="new_type">
						<% for (Event.Type t : Event.Type.values()) { %>
							<option value="<%= t.name() %>"><%= t %></option>
						<% } %>
					</select>
				</td>
				<td>
					<select name="new_primary">
						<option></option>
						<% for (Person p : Sets.union(Sets.newHashSet(match.getTeamA().players()), Sets.newHashSet(match.getTeamB().players()))) { %>
							<option value="<%= p.getId() %>"><%= p.getDisplayName() %></option>
						<% } %>
					</select>
				</td>
				<td>
					<select name="new_secondary">
						<option></option>
						<% for (Person p : Sets.union(Sets.newHashSet(match.getTeamA().players()), Sets.newHashSet(match.getTeamB().players()))) { %>
							<option value="<%= p.getId() %>"><%= p.getDisplayName() %></option>
						<% } %>
					</select>
				</td>
				<td>
					<input name="new_time" type="text">
				</td>
				<td><input type="submit" name="action" value="new" /></td>
			</tr>
		</table>
	</fieldset>
	</form>
</div>
<jsp:include page="Templates/Footer.jsp" />