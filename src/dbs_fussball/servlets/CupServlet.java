package dbs_fussball.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import active_record.ActiveRecordMapper;
import dbs_fussball.model.Cup;
import dbs_fussball.model.FifaCountry;
import dbs_fussball.model.Cup.CupBuilder;

public class CupServlet extends HttpServlet {
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
		else if (request.getPathInfo().matches("^/new/?$"))
			getServletContext().getRequestDispatcher("/JSP/Cup.new.jsp").forward(request, response);
		else if (request.getPathInfo().matches("^/[0-9]+/?$")) {
			doGetShowCup(request, response);
			return;
		} else if (request.getPathInfo().matches("^/[0-9]+/edit/?$")) {

		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		if (request.getPathInfo().matches("^/new/?$"))
			doPostNewCup(request, response);
	}

	private void doGetShowCup(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		assert request.getPathInfo().matches("^/[0-9]+/?$");

		String[] pathComponents = request.getPathInfo().split("/");
		Long cupId = Long.parseLong(pathComponents[1]);
		Cup cup = null;

		cup = arm.find(Cup.class).where("id").is(cupId).please();

		log("Found cup with id " + (cup != null ? cup.getId() : "null!"));

		request.setAttribute("cup", cup);

		getServletContext().getRequestDispatcher("/JSP/Cup.show.jsp").forward(request, response);
	}

	private void doPostNewCup(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		Map<String, String[]> parameters = request.getParameterMap();

		CupBuilder builder = new CupBuilder();
		EnumSet<FifaCountry> countries = EnumSet.noneOf(FifaCountry.class);

		List<String> errors = new ArrayList<String>();
		/*
		 * Iterate over all expected parameters, add an error if a parameters is
		 * bogus, missing or duplicate
		 */
		for (char group = 'A'; group <= 'H'; group++)
			for (int pos = 0; pos < 4; pos++) {
				String countryCode = request.getParameter("group_" + group + "_" + pos);
				if (countryCode == null || FifaCountry.countryForCode(countryCode) == null)
					errors.add("Feld " + (pos + 1) + " in Gruppe " + group + " ist unbesetzt.");
				else {
					FifaCountry country = FifaCountry.countryForCode(countryCode);
					builder.addCountryToGroup(country, group);
					if (!countries.add(country))
						errors.add(country + " wurde mehrfach eingetragen.");
				}
			}

		if (!errors.isEmpty()) {
			request.setAttribute("error", errors);

			// We add the parameters to the attributes map so we can display what the user entered
			for (Entry<String, String[]> entry : parameters.entrySet())
				request.setAttribute(entry.getKey(), entry.getValue()[0]);
			getServletContext().getRequestDispatcher("/JSP/Cup.new.jsp").forward(request, response);
			return;
		}

		Cup newCup = builder.build();
		try {
			arm.save(newCup);
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}
}