package dbs_fussball.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import active_record.ActiveRecordMapper;
import dbs_fussball.model.Event;
import dbs_fussball.model.Match;
import dbs_fussball.model.Person;

public class MatchServlet extends HttpServlet {
	private static final long	serialVersionUID	= 1L;

	private ActiveRecordMapper	arm;

	@Override
	public void init() throws ServletException {
		super.init();
		arm = new ActiveRecordMapper("dbs_fussball", "postgres", "vuvuzela", "dbs");

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getPathInfo() == null)
			getServletContext().getRequestDispatcher("/JSP/Cup.list.jsp").forward(request, response);
		else if (request.getPathInfo().matches("^/[0-9]+/?$")) {
			doGetShowMatch(request, response);
			return;
		} else if (request.getPathInfo().matches("^/[0-9]+/edit/?$"))
			doGetEditMatch(request, response);
	}

	protected void doGetShowMatch(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String[] pathComponents = request.getPathInfo().split("/");
		Long matchId = Long.parseLong(pathComponents[1]);
		Match match = arm.find(Match.class).where("id").is(matchId).please();

		if (match == null) {
			response.sendError(404, "Could not find that match, mate!");
			return;
		} else {
			request.setAttribute("match", match);
			getServletContext().getRequestDispatcher("/JSP/Match.show.jsp").forward(request, response);
		}
	}

	protected void doGetEditMatch(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String[] pathComponents = request.getPathInfo().split("/");
		Long matchId = Long.parseLong(pathComponents[1]);
		Match match = arm.find(Match.class).where("id").is(matchId).please();

		if (match == null) {
			response.sendError(404, "Could not find that match, mate!");
			return;
		} else {
			request.setAttribute("match", match);
			getServletContext().getRequestDispatcher("/JSP/Match.edit.jsp").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		if (request.getPathInfo().matches("^/[0-9]+/edit/?$"))
			doPostEditMatch(request, response);
	}

	protected void doPostEditMatch(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		Map<String, String[]> parameters = request.getParameterMap();
		for (Entry<String, String[]> entry : parameters.entrySet())
			System.out.println(entry.getKey() + " => " + Arrays.toString(entry.getValue()));

		if (request.getParameter("action") == null) {
			getServletContext().getRequestDispatcher("/JSP/Match.edit.jsp").forward(request, response);
			return;
		}

		if (request.getParameter("action").equals("new"))
			addNewEventWithRequest(request, response);
		else if (request.getParameter("action").startsWith("remove_"))
			System.out.println("Tried to remove something");
		else if (request.getParameter("action").equals("aufstellen"))
			changeLineupWithRequest(request, response);
	}

	private void addNewEventWithRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] pathComponents = request.getPathInfo().split("/");
		Long matchId = Long.parseLong(pathComponents[1]);
		Match match = arm.find(Match.class).where("id").is(matchId).please();

		String eventType = request.getParameter("new_type");
		Event.Type type = Event.Type.valueOf(eventType);

		Float eventTime = request.getParameter("new_time").isEmpty() ? 0 : Float.parseFloat(request
				.getParameter("new_time"));
		Long primaryId = request.getParameter("new_primary").isEmpty() ? 0 : Long.parseLong(request
				.getParameter("new_primary"));
		Long secondaryId = request.getParameter("new_secondary").isEmpty() ? 0 : Long.parseLong(request
				.getParameter("new_secondary"));

		Person primary = null;
		Person secondary = null;
		try {
			primary = arm.findBy(Person.class, primaryId);
			secondary = arm.findBy(Person.class, secondaryId);
		} catch (SQLException e) {
			throw new ServletException(e);
		}

		log("Creating new event with " + type + " " + primary + " " + secondary + " " + eventTime);
		Event newEvent = new Event(type, primary, secondary, eventTime);

		match.addEvent(newEvent);

		try {
			arm.save(match);
		} catch (SQLException e) {
			throw new ServletException(e);
		}

		request.setAttribute("match", match);
		getServletContext().getRequestDispatcher("/JSP/Match.edit.jsp").forward(request, response);
	}

	private void changeLineupWithRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] pathComponents = request.getPathInfo().split("/");
		Long matchId = Long.parseLong(pathComponents[1]);
		Match match = arm.find(Match.class).where("id").is(matchId).please();

		Set<Person> playersA = new HashSet<Person>();
		for (int i = 0; i < 11; i++) {
			String playerIdString = request.getParameter("player_A_" + (i + 1));
			if (playerIdString == null || playerIdString.isEmpty())
				continue;

			Long playerId = Long.parseLong(playerIdString);
			try {
				playersA.add(arm.findBy(Person.class, playerId));
			} catch (SQLException e) {
				throw new ServletException(e);
			}
		}

		Set<Person> playersB = new HashSet<Person>();
		for (int i = 0; i < 11; i++) {
			String playerIdString = request.getParameter("player_B_" + (i + 1));
			if (playerIdString == null || playerIdString.isEmpty())
				continue;

			Long playerId = Long.parseLong(playerIdString);
			try {
				playersB.add(arm.findBy(Person.class, playerId));
			} catch (SQLException e) {
				throw new ServletException(e);
			}
		}

		Set<Person> personsToRemove = new HashSet();
		for (Person p : match.playersOfLineUpTeamA())
			personsToRemove.add(p);

		for (Person p : match.playersOfLineUpTeamB())
			personsToRemove.add(p);

		for (Person p : personsToRemove)
			match.removePlayerFromLineUpTeamA(p);

		for (Person p : playersA)
			match.addPlayerToLineUpTeamA(p);

		for (Person p : playersB)
			match.addPlayerToLineUpTeamB(p);

		try {
			arm.save(match);
		} catch (SQLException e) {
			throw new ServletException(e);
		}

		request.setAttribute("match", match);
		getServletContext().getRequestDispatcher("/JSP/Match.edit.jsp").forward(request, response);
	}

}
