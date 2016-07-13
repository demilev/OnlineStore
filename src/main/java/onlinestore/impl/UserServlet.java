package onlinestore.impl;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import onlinestore.User;
import onlinestore.UserAlreadyExistException;

public class UserServlet extends HttpServlet {
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

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BufferedReader reader = request.getReader();
		Gson gson = new Gson();
		User user = gson.fromJson(reader, User.class);
		try {
			userManager.createUser(user);
		} catch (UserAlreadyExistException e) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			response.getWriter().println("This user already exists!");
		}
	}

}
