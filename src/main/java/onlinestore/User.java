package onlinestore;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String password;

	public User() {
		name = "";
		password = "";

	}

	public User(String name, String password) {
		this.name = name;
		this.password = password;

	}

	public boolean validate(User user) {

		return name.equals(user.name) && password.equals(user.password);

	}

	public void changePassword(String newPassword) {
		password = newPassword;
	}

	public boolean hasEqualName(User newUser) {

		return name.equals(newUser.name);
	}

	public String getName() {

		return name;
	}

	public String getPassword() {

		return password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}

}
