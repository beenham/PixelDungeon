package net.team11.pixeldungeon.entity.component;

import net.team11.pixeldungeon.entitysystem.EntityComponent;
import net.team11.pixeldungeon.items.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class InventoryComponent implements EntityComponent {
    public static final int MAX_SIZE = 10;
    private List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return items;
    }

    public boolean addItem(Item item) {
        if (item != null) {
            if (items.size() < MAX_SIZE) {
                items.add(item);
                return true;
            } else {
                System.out.println("Not enough space!");
                return false;
            }
        }
        return false;
    }

    public Item getItem(String name, Class itemClass) {
        for (Item item : items) {
            if (item.getClass() == itemClass) {
                return item;
            }
        }
        throw new NoSuchElementException("You don't have this item");
    }

    public boolean hasItem(String name, Class itemClass) {
        for (Item item : items) {
            if (item.getClass().equals(itemClass) && item.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
