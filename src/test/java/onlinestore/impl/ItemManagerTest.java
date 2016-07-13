package onlinestore.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import onlinestore.Item;
import onlinestore.ItemAlreadyExistException;
import onlinestore.ItemDoesNotExistException;

public class ItemManagerTest {
	private FileSystemItemManager itemManager;

	@Before
	public void setup() {
		itemManager = new FileSystemItemManager() {
			@Override
			Map<String, Item> read() {
				Map<String, Item> resultMap = new HashMap<String, Item>();
				resultMap.put("mlqko-vr", new Item("mlqko-vr", "vereia", 1.1));
				resultMap.put("cheren hlqb", new Item("cheren hlqb", "sus zurna", 1));
				resultMap.put("domati-bg", new Item("domati-bg", "bulgarski", 2));
				resultMap.put("pileshko_meso-g", new Item("pileshko_meso-g", "gurdi", 7.5));
				return resultMap;
			}
			@Override
			public List<Item> listItems(){
				return items;
			}
			@Override
			void save(){}
		};
	}

	@Test
	public void testCreateItem() throws ItemAlreadyExistException {
		Item newItem = new Item("mlqko", "bor chvor", 1.5);
		itemManager.createItem(newItem);
		assertEquals(newItem, itemManager.listItems().get(4));
	}

	@Test(expected = ItemAlreadyExistException.class)
	public void testCreateExistingItem() throws ItemAlreadyExistException {
		Item newItem = new Item("pileshko_meso-g", "gurdi", 7.5);
		itemManager.createItem(newItem);
	}

	@Test
	public void testDeleteItem() throws ItemDoesNotExistException {
		List<Item> items = new ArrayList<Item>(itemManager.read().values());
		Item item = items.get(0);
		itemManager.deleteItem(item);
		assertFalse(itemManager.listItems().contains(item));
	}
	@Test(expected=ItemDoesNotExistException.class)
	public void testDeleteNonExistingItem() throws ItemDoesNotExistException {
		Item item =new Item("ITEM","THIS IS NOT AN ITEM",0);
		itemManager.deleteItem(item);
		
	}
}
