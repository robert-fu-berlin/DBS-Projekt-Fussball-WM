package dbs_fussball.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import active_record.ActiveRecordMapper;
import dbs_fussball.model.Match;

public class MatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
