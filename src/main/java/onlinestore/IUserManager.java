package onlinestore;

import java.util.List;

public interface IUserManager {
	List<User> listUsers();

	void createUser(User newUser) throws UserAlreadyExistException;

	void deleteUser(User user) throws UserDoesNotExistException;

	void loginUser(User user) throws CredentialsDontMatchException;
}
