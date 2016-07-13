package onlinestore.impl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import onlinestore.CredentialsDontMatchException;
import onlinestore.User;

@WebServlet("/eduproject/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private volatile FileSystemUserManager userManager;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		userManager = new FileSystemUserManager();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null
				|| session.getAttribute("user").toString().isEmpty()) {
			response.getWriter().append("No user is logged in.");
		} else {
			response.getWriter().append("Logged in user: " + session.getAttribute("user"));
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("user") != null) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			response.getWriter().println("An user has already logged in.");

		} else {
			String name = request.getParameter("username");
			String password = request.getParameter("password");
			if (name == null || password == null || name.isEmpty() || password.isEmpty()) {
				out.println("<HTML><HEAD><TITLE>Access Denied</TITLE></HEAD>");
				out.println("<BODY>Your username and password are invalid.<BR>");
				out.println("You may want to <A HREF=\"login.html\">try again</A>");
				out.println("<BR>Or go back to <A HREF=\"index.html\">main page</A>");
				out.println("</BODY></HTML>");
			} else {
				User user = new User(name, password);
				try {
					userManager.loginUser(user);
					session.setAttribute("user", name);
					response.getWriter().println("You successfully loged in.");
					response.sendRedirect("system.html");
				} catch (CredentialsDontMatchException e) {
					response.setStatus(HttpServletResponse.SC_CONFLICT);
					out.println("<HTML><HEAD><TITLE>Access Denied</TITLE></HEAD>");
					out.println("<BODY>Your username and password don't match.<BR>");
					out.println("You may want to <A HREF=\"login.html\">try again</A>");
					out.println("<BR>Or go back to <A HREF=\"index.html\">main page</A>");
					out.println("</BODY></HTML>");

				}
			}
		}
	}

}
