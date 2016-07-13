package onlinestore.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import onlinestore.CredentialsDontMatchException;
import onlinestore.User;
import onlinestore.UserAlreadyExistException;
import onlinestore.UserDoesNotExistException;

public class UserManagerTest {
	FileSystemUserManager userManager;

	@Before
	public void setup() {
		userManager = new FileSystemUserManager() {
			@Override
			Map<String, User> read() {
				Map<String, User> resultMap = new HashMap<String, User>();
				resultMap.put("mitko", new User("mitko", "123456"));
				resultMap.put("ivan", new User("ivan", "1AfS"));
				resultMap.put("gosho", new User("gosho", "parola"));
				resultMap.put("tosho", new User("tosho", "levski"));
				return resultMap;
			}

			@Override
			public List<User> listUsers() {
				return userManager.users;
			}
			@Override
			void save(){}
		};
	}

	@Test
	public void testCreateUser() throws UserAlreadyExistException {
		User newUser = new User("petur", "petur");
		userManager.createUser(newUser);
		assertEquals(newUser, userManager.listUsers().get(4));
	}

	@Test(expected = UserAlreadyExistException.class)
	public void testCreateExistingUser() throws UserAlreadyExistException {
		User newUser = new User("tosho", "levski");
		userManager.createUser(newUser);
	}

	@Test
	public void testDeleteUser() throws UserDoesNotExistException {
		List<User> users=new ArrayList<User>(userManager.read().values());
		User user=users.get(0);
		userManager.deleteUser(user);
		assertFalse(userManager.listUsers().contains(user));
	}
	@Test(expected=UserDoesNotExistException.class)
	public void testDeleteNonExistingUser() throws UserDoesNotExistException {
		User user=new User("notAnUser","password");
		userManager.deleteUser(user);
	}
	@Test(expected=CredentialsDontMatchException.class)
	public void testLogInNonExistingUser() throws CredentialsDontMatchException {
		User user=new User("notAnUser","password");
		userManager.loginUser(user);
		
	}
}
