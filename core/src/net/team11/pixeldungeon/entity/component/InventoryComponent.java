package net.team11.pixeldungeon.entity.component;

import net.team11.pixeldungeon.entitysystem.EntityComponent;
import net.team11.pixeldungeon.items.GhostItem;
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

    public boolean removeItem(Item item){
//        System.out.println("Item is : " + item);
//        System.out.println("List of items: " +items);
        for (Item listItems : items){
//            System.out.println("Comparing " + item + " and " + listItems);
            if (item.getName().matches(listItems.getName())){
//                System.out.println("Match " + item.getName() + " and " + listItems);
                items.remove(listItems);
//                System.out.println("List of Items: " + items);
                return true;
            }
        }
        return false;
    }

    public boolean hasItem(String name, Class itemClass) {
        for (Item item : items) {
//            System.out.println(item.getClass().toString());
//            System.out.println(item.getName());
            if (item.getClass().equals(itemClass) && item.getName().equals(name)) {
//                System.out.println("Item is " + item);
                return true;
            }
        }
        return false;
    }
}
