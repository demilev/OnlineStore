package onlinestore.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import onlinestore.IItemManager;
import onlinestore.Item;
import onlinestore.ItemAlreadyExistException;
import onlinestore.ItemDoesNotExistException;

public class FileSystemItemManager implements IItemManager {
	private static final String ITEMS_FILE = "C:\\Users\\Mitko\\workspace\\eduproject\\Items.bin";
	List<Item> items;

	@Override
	public List<Item> listItems() {
		items = new ArrayList<Item>(read().values());
		return items;
	}

	@Override
	public void createItem(Item newItem) throws ItemAlreadyExistException {
		items = new ArrayList<Item>(read().values());

		if (items.contains(newItem)) {
			throw new ItemAlreadyExistException("This item already exists.");
		}
		items.add(newItem);

		save();
	}

	@Override
	public void deleteItem(Item item) throws ItemDoesNotExistException {
		items = new ArrayList<Item>(read().values());
		Map<String, Item> mItems = read();

		if (mItems.containsKey(item.getItemName()) == false) {
			throw new ItemDoesNotExistException("This item does not exists.");
		}
		mItems.remove(item.getItemName());
		items = new ArrayList<Item>(mItems.values());
		save();
	}


	@SuppressWarnings("unchecked")
	Map<String, Item> read() {
		Map<String, Item> fileContent = new HashMap<String, Item>();

		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ITEMS_FILE))) {
			Object obj = in.readObject();
			ArrayList<Item> list = new ArrayList<Item>();
			list = (ArrayList<Item>) obj;

			for (Item item : list) {
				fileContent.put(item.getItemName(), item);
			}

		} catch (IOException e) {
			throw new RuntimeException("Couldn't read items", e);
		} catch (ClassNotFoundException e) {

			throw new RuntimeException("Class not found", e);
		}
		return fileContent;

	}

	void save() {

		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ITEMS_FILE))) {
			out.writeObject(items);
		} catch (IOException e) {

			throw new RuntimeException("Couldn't persist items", e);
		}
	}

	public static void main(String[] args) {
		FileSystemItemManager im = new FileSystemItemManager();
		List<Item> content = new ArrayList<Item>(im.read().values());
		for (int i = 0; i < content.size(); i++) {
			System.out.println(content.get(i).getItemName() + " "
					+ content.get(i).getDescription() + " " + content.get(i).getPrice());
		}
		Gson gson=new Gson();
		String json=gson.toJson(content.get(0));
		System.out.println(json);

	}
}
