package dbs_fussball.servlets;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import active_record.ActiveRecordMapper;

import com.google.common.collect.Lists;

import dbs_fussball.model.User;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ActiveRecordMapper	arm;

	private static final String	SALT				= "ph'nglui mglw'nafh cthulhu r'lyeh wgah'nagl fhtagn";

    public LoginServlet() {
        super();
		arm = new ActiveRecordMapper("dbs_fussball", "postgres", "vuvuzela", "dbs");
		try {
			User user = arm.find(User.class).where("email").is("robb@beer2peer.com").please();
			if (user != null)
				return;

			user = new User("robb@beer2peer.com", generateHash("cheesecake"));
			arm.save(user);
		} catch (ServletException e) {
			assert false;
		} catch (SQLException e) {
			assert false;
		}
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getServletContext().getRequestDispatcher("/JSP/Login.login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String passwordHash = generateHash(password);

		User user = arm.find(User.class).where("email").is(email).please();

		if (user != null && user.getPasswordHash().equals(passwordHash)) {
			request.getSession().setAttribute("authenticated_as", email);
			log("success");
		} else {
			request.setAttribute("email", email);
			request.setAttribute("error", Lists.newArrayList("Fehler beim einloggen"));
			getServletContext().getRequestDispatcher("/JSP/Login.login.jsp").forward(request, response);
			return;
		}
	}

	private String generateHash(String password) throws ServletException {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			BigInteger digest = new BigInteger(1, m.digest((password + SALT).getBytes()));
			return String.format("%1$032X", digest);
		} catch (NoSuchAlgorithmException e) {
			throw new ServletException(e);
		}
	}

}
