package net.team11.pixeldungeon.game.entity.component;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.game.entitysystem.EntityComponent;
import net.team11.pixeldungeon.game.items.Item;

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
                if (isFull()) {
                    PixelDungeon.getInstance().getAndroidInterface().earnFullInventory();
                }
                return true;
            } else {
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
        return null;
    }

    public void removeItem(Item item){
        for (Item myItem : items){
            System.out.println("Comparing " + item.toString() + " and " + myItem.toString());
            if (myItem.getClass().equals(item.getClass()) && myItem.getName().equals(item.getName())) {
                System.out.println("Match " + item + " and " + myItem);
                items.remove(myItem);
                break;
            }
        }
    }

    public boolean hasItem(Item item) {
        for (Item myItem : items) {
            if (myItem.getClass().equals(item.getClass()) && myItem.getName().equals(item.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isFull() {
        return items.size() == MAX_SIZE;
    }
}
