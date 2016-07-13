package onlinestore;

public class ItemDoesNotExistException extends Exception {

	private static final long serialVersionUID = 1L;

	public ItemDoesNotExistException() {

		super();
	}
	public ItemDoesNotExistException(String exceptionMessage){
		super(exceptionMessage);
	}
	
}
