package onlinestore;

public class CredentialsDontMatchException extends Exception {
	private static final long serialVersionUID = 1L;
	public CredentialsDontMatchException() {
		super();
	}
	public CredentialsDontMatchException(String exceptionMessage) {
		super(exceptionMessage);
	}
}
