package onlinestore.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import onlinestore.Item;
import onlinestore.ItemAlreadyExistException;
import onlinestore.ItemDoesNotExistException;

public class ItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private volatile FileSystemItemManager itemManager;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		itemManager = new FileSystemItemManager();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Gson gson = new Gson();
		String itemJson = gson.toJson(itemManager.listItems());
		response.getWriter().write(itemJson);

	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestURI = req.getRequestURI();
		Pattern pattern = Pattern.compile("\\/items\\/([^\\/&?]+)");
		Matcher matcher = pattern.matcher(requestURI);
		matcher.find();
		String itemName = matcher.group(1);
		String description = new String();
		double price = 0;
		for (Item item : itemManager.listItems()) {
			if (item.getItemName().equals(itemName)) {
				description = item.getDescription();
				price = item.getPrice();
				break;
			}
		}
		Item item = new Item(itemName, description, price);
		try {
			itemManager.deleteItem(item);
		} catch (ItemDoesNotExistException e) {
			resp.setStatus(HttpServletResponse.SC_CONFLICT);
			resp.getWriter().println("This item does not exists!");
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		BufferedReader reader = request.getReader();
		Gson gson = new Gson();
		Item item = gson.fromJson(reader, Item.class);
		try {
			itemManager.createItem(item);
		} catch (ItemAlreadyExistException e) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			response.getWriter().println("This item already exists!");

		}

	}

}
