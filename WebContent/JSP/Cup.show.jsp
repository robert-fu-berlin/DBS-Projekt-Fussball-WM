<%@ page import="dbs_fussball.model.*"%>
<%@ page import="java.util.Set"%>
<jsp:include page="Templates/Header.jsp" />
<% Cup cup = (Cup) request.getAttribute("cup"); %>
<h1>Weltmeisterschaft <%= cup.getId() %></h1>
<div class="ko-tree">
	<!-- Finale -->
	<jsp:include page="Partials/MatchNode.jsp">
		<jsp:param value="<%= cup.getFinal().getId() %>" name="match-id"/>
		<jsp:param value="final" name="match-type"/>
		<jsp:param value="<%= cup.getFinal().getTeamA() != null ? cup.getFinal().getTeamA().getCountry().code : "???" %>" name="team-a"/>
		<jsp:param value="<%= cup.getFinal().getTeamB() != null ? cup.getFinal().getTeamB().getCountry().code : "???" %>" name="team-b"/>
		<jsp:param value="<%= cup.getFinal().getGoalsA() %>" name="goals-a"/>
		<jsp:param value="<%= cup.getFinal().getGoalsB() %>" name="goals-b"/>
	</jsp:include>

	<!-- Third place -->
	<jsp:include page="Partials/MatchNode.jsp">
		<jsp:param value="<%= cup.getThirdPlace().getId() %>" name="match-id"/>
		<jsp:param value="third-place" name="match-type"/>
		<jsp:param value="<%= cup.getThirdPlace().getTeamA() != null ? cup.getThirdPlace().getTeamA().getCountry().code : "???" %>" name="team-a"/>
		<jsp:param value="<%= cup.getThirdPlace().getTeamB() != null ? cup.getThirdPlace().getTeamB().getCountry().code : "???" %>" name="team-b"/>
		<jsp:param value="<%= cup.getThirdPlace().getGoalsA() %>" name="goals-a"/>
		<jsp:param value="<%= cup.getThirdPlace().getGoalsB() %>" name="goals-b"/>
	</jsp:include>

	<!-- Semi finals -->
	<% for (int i = 1; i <= 2; i++) { %>
		<jsp:include page="Partials/MatchNode.jsp">
			<jsp:param value="<%= cup.getSemiFinal(i).getId() %>" name="match-id"/>	
			<jsp:param value="semi-final" name="match-type"/>
			<jsp:param value="<%= cup.getSemiFinal(i).getTeamA() != null ? cup.getSemiFinal(i).getTeamA().getCountry().code : "???" %>" name="team-a"/>
			<jsp:param value="<%= cup.getSemiFinal(i).getTeamB() != null ? cup.getSemiFinal(i).getTeamB().getCountry().code : "???" %>" name="team-b"/>
			<jsp:param value="<%= cup.getSemiFinal(i).getGoalsA() %>" name="goals-a"/>
			<jsp:param value="<%= cup.getSemiFinal(i).getGoalsB() %>" name="goals-b"/>
		</jsp:include>
	<% } %>

	<!-- Quarter finals -->
	<% for (int i = 1; i <= 4; i++) { %>
		<jsp:include page="Partials/MatchNode.jsp">
			<jsp:param value="<%= cup.getQuarterFinal(i).getId() %>" name="match-id"/>
			<jsp:param value="quarter-final" name="match-type"/>
			<jsp:param value="<%= cup.getQuarterFinal(i).getTeamA() != null ? cup.getQuarterFinal(i).getTeamA().getCountry().code : "???" %>" name="team-a"/>
			<jsp:param value="<%= cup.getQuarterFinal(i).getTeamB() != null ? cup.getQuarterFinal(i).getTeamB().getCountry().code : "???" %>" name="team-b"/>
			<jsp:param value="<%= cup.getQuarterFinal(i).getGoalsA() %>" name="goals-a"/>
			<jsp:param value="<%= cup.getQuarterFinal(i).getGoalsB() %>" name="goals-b"/>
		</jsp:include>
	<% } %>

	<!-- Round of sixteen -->
	<% for (int i = 1; i <= 8; i++) { %>
		<jsp:include page="Partials/MatchNode.jsp">
			<jsp:param value="<%= cup.getRoundOfSixteen(i).getId() %>" name="match-id"/>
			<jsp:param value="round-of-sixteen" name="match-type"/>
			<jsp:param value="<%= cup.getRoundOfSixteen(i).getTeamA() != null ? cup.getRoundOfSixteen(i).getTeamA().getCountry().code : "???" %>" name="team-a"/>
			<jsp:param value="<%= cup.getRoundOfSixteen(i).getTeamB() != null ? cup.getRoundOfSixteen(i).getTeamB().getCountry().code : "???" %>" name="team-b"/>
			<jsp:param value="<%= cup.getRoundOfSixteen(i).getGoalsA() %>" name="goals-a"/>
			<jsp:param value="<%= cup.getRoundOfSixteen(i).getGoalsB() %>" name="goals-b"/>
		</jsp:include>
	<% } %>

</div>
<div class="groups-table">
	<% for (char group = 'A'; group <= 'H'; group++) {
		// TODO sort by time
		Set<Match> matches = cup.getMatches(group); %>
		<p>Gruppe <%= group %></p>
		<table>
		<% for (Match m : matches) { %>
			<tr>
				<td>
					<!-- Quick and dirty -->
					<a href="<%= request.getContextPath() %>/match/<%= m.getId() %>">
						<%= m.getTeamA().getCountry() %> vs <%= m.getTeamB().getCountry() %>
					</a>
				</td>
				<td><%= m.getGoalsA() %> : <%= m.getGoalsB() %></td>
			</tr>
		<% } %>
		</table>
	<% } %>
</div>
<jsp:include page="Templates/Footer.jsp" />