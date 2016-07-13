package onlinestore.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import onlinestore.CredentialsDontMatchException;
import onlinestore.IUserManager;
import onlinestore.User;
import onlinestore.UserAlreadyExistException;
import onlinestore.UserDoesNotExistException;

public class FileSystemUserManager implements IUserManager {
	private static final String USERS_FILE = "C:\\Users\\Mitko\\workspace\\eduproject\\Users.bin";
	List<User> users;

	@Override
	public void createUser(User newUser) throws UserAlreadyExistException {
		users = new ArrayList<User>(read().values());
		if (users.contains(newUser) == true) {
			throw new UserAlreadyExistException("Someone already has that username.");
		} else
			users.add(newUser);
		save();

	}

	@Override
	public List<User> listUsers() {
		users = new ArrayList<User>(read().values());
		return users;
	}

	@Override
	public void deleteUser(User user) throws UserDoesNotExistException {
		users = new ArrayList<User>(read().values());
		Map<String, User> mUsers = read();

		if (mUsers.containsKey(user.getName()) == false) {
			throw new UserDoesNotExistException("This user does not exists.");
		}

		mUsers.remove(user.getName());
		users = new ArrayList<User>(mUsers.values());
		save();

	}

	@Override
	public void loginUser(User user) throws CredentialsDontMatchException {
		users = new ArrayList<User>(read().values());
		if (users.contains(user) == false) {
			throw new CredentialsDontMatchException("Invalid name or password.");
		}

	}

	@SuppressWarnings("unchecked")
	Map<String, User> read() {
		Map<String, User> fileContent = new HashMap<String, User>();

		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
			Object obj = in.readObject();
			ArrayList<User> list = new ArrayList<User>();
			list = (ArrayList<User>) obj;

			for (User user : list) {
				fileContent.put(user.getName(), user);
			}

		} catch (IOException e) {
			throw new RuntimeException("Couldn't read users", e);
		} catch (ClassNotFoundException e) {

			throw new RuntimeException("Class not found", e);
		}
		return fileContent;

	}

	void save() {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
			out.writeObject(users);
		} catch (IOException e) {

			throw new RuntimeException("Couldn't persist users", e);
		}
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		FileSystemUserManager um = new FileSystemUserManager();
		List<User> content = new ArrayList<User>(um.read().values());
		for (int i = 0; i < content.size(); i++) {
			System.out.println(
					content.get(i).getName() + " " + content.get(i).getPassword());
		}
		User newUser = new User("peshotoo", "239241");
		try {
			um.createUser(newUser);
		} catch (UserAlreadyExistException e) {

			e.printStackTrace();
		}
		try {
			um.loginUser(newUser);
		} catch (CredentialsDontMatchException e1) {

			e1.printStackTrace();
		}
		/*
		try {
			um.deleteUser(newUser);
		} catch (UserDoesNotExistException e) {

			e.printStackTrace();
		}
		*/
		long end = System.currentTimeMillis();
		System.out.println("Finished for " + ((end - start)) + " mili seconds");
	}
}
