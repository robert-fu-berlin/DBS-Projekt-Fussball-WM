<%@page import="dbs_fussball.model.*"%>
<% String matchType = request.getParameter("match-type"); 
   String teamA = request.getParameter("team-a"); 
   String teamB = request.getParameter("team-b");
   String goalsA = request.getParameter("goals-a");
   String goalsB = request.getParameter("goals-b");
   String matchId = request.getParameter("match-id");
   String humanreadableMatchType = "Match";
   if (matchType.equals("final"))
	   humanreadableMatchType = "Finale";
   else if (matchType.equals("third-place"))
	   humanreadableMatchType = "Spiel um den 3. Platz";
   else if (matchType.equals("semi-final"))
	   humanreadableMatchType = "Halbfinale";
   else if (matchType.equals("quarter-final"))
	   humanreadableMatchType = "Viertelfinale";
   else if (matchType.equals("round-of-sixteen"))
	   humanreadableMatchType = "Achtelfinale";
   %>
<div class="tree-node <%= matchType != null ? matchType : "" %>">
	<a href="<%= request.getContextPath() %>/match/<%= matchId %>">
		<span class="type-line">
			<%= humanreadableMatchType %>
		</span>
		<span class="goal-line">
			<%= goalsA %> : <%= goalsB %>
		</span>
		<span class="versus-line">
			<%= teamA %> vs. <%= teamB %>
		</span>
	</a>
</div>