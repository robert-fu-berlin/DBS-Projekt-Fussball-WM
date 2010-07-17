package dbs_fussball.servlets;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import active_record.ActiveRecordMapper;
import dbs_fussball.model.Person;

/**
 * Servlet implementation class PersonServlet
 */
public class PersonServlet extends HttpServlet {
	private static final long	serialVersionUID	= 1L;

	private ActiveRecordMapper	arm;

	@Override
	public void init() throws ServletException {
		super.init();
		arm = new ActiveRecordMapper("dbs_fussball", "postgres", "vuvuzela", "dbs");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getPathInfo() == null)
			return;
		if (request.getPathInfo().matches("^/new/?$"))
			doGetNewPerson(request, response);
		else if (request.getPathInfo().matches("^/[0-9]+/?$"))
			doGetDisplayPerson(request, response);
		else if (request.getPathInfo().matches("^/[0-9]+/edit/?$"))
			doGetEditPerson(request, response);
		// TODO else render 404
	}

	private void doGetNewPerson(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		getServletContext().getRequestDispatcher("/JSP/Person.new.jsp").forward(request, response);
	}

	private void doGetEditPerson(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		assert request.getPathInfo().matches("^/[0-9]+/edit/?$");

		String[] pathComponensts = request.getPathInfo().split("/");

		try {
			Long personId = Long.parseLong(pathComponensts[1]);
			Person person = arm.findBy(Person.class, personId);

			if (person != null) {
				request.setAttribute("person", person);
				getServletContext().getRequestDispatcher("/JSP/Person.edit.jsp").forward(request, response);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void doGetDisplayPerson(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		assert request.getPathInfo().matches("^/[0-9]+/?");

		String[] pathComponensts = request.getPathInfo().split("/");

		try {
			Long personId = Long.parseLong(pathComponensts[1]);
			Person person = arm.findBy(Person.class, personId);

			if (person != null) {
				request.setAttribute("person", person);
				getServletContext().getRequestDispatcher("/JSP/Person.show.jsp").forward(request, response);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		if (request.getPathInfo().matches("^/[0-9]+/edit/?$"))
			doPostEditPerson(request, response);
		if (request.getPathInfo().matches("^/new/?$"))
			doPostNewPerson(request, response);
	}

	private void doPostNewPerson(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		try {
			Person person = new Person();

			updatePersonWithRequest(person, request);

			arm.save(person);

			if (person.getId() != null) {
				System.out.println(this.getServletContext().getContextPath() + "/person/" + person.getId());
				response.sendRedirect(response.encodeRedirectURL(this.getServletContext().getContextPath() + "/person/"
						+ person.getId()));
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void doPostEditPerson(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		assert request.getPathInfo().matches("^/[0-9]+/edit/?$");

		String[] pathComponensts = request.getPathInfo().split("/");

		try {
			Long personId = Long.parseLong(pathComponensts[1]);
			Person person = arm.findBy(Person.class, personId);

			updatePersonWithRequest(person, request);

			arm.save(person);

			if (person != null) {
				request.setAttribute("person", person);
				getServletContext().getRequestDispatcher("/JSP/Person.edit.jsp").forward(request, response);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void updatePersonWithRequest(Person person, HttpServletRequest request) {
		NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);

		if (!nullOrEmpty(request.getParameter("firstName")))
			person.setFirstName(request.getParameter("firstName"));

		if (!nullOrEmpty(request.getParameter("lastName")))
			person.setLastName(request.getParameter("lastName"));

		if (!nullOrEmpty(request.getParameter("stageName")))
			person.setStageName(request.getParameter("stageName"));

		if (!nullOrEmpty(request.getParameter("club")))
			person.setClub(request.getParameter("club"));

		if (!nullOrEmpty(request.getParameter("weight"))) {
			try {
				person.setWeight(nf.parse(request.getParameter("weight")).floatValue());
			} catch (ParseException e) {

			}
		}

		if (!nullOrEmpty(request.getParameter("height"))) {
			try {
				person.setHeight(nf.parse(request.getParameter("height")).floatValue());
			} catch (ParseException e) {
			}
		}

		// TODO Handle birth date
	}

	private static boolean nullOrEmpty(String value) {
		return value == null || value.isEmpty();
	}

}
