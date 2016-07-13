package onlinestore;

import java.util.List;

public interface IItemManager {
	List<Item> listItems();

	void createItem(Item newItem) throws ItemAlreadyExistException;

	void deleteItem(Item Item) throws ItemDoesNotExistException;
}
