package net.team11.pixeldungeon.utils.stats;

import java.util.ArrayList;

public class CurrentStats {
    private int chestsFound;
    private int keysFound;
    private int itemsFound;
    private int deaths;
    private ArrayList<String> chests = new ArrayList<>();
    private ArrayList<String> keys = new ArrayList<>();
    private ArrayList<String> items = new ArrayList<>();

    public CurrentStats() {
        chestsFound = 0;
        keysFound = 0;
        itemsFound = 0;
        deaths = 0;
    }

    public void incrementChests() {
        chestsFound++;
    }
    public void incrementKeys() {
        keysFound++;
    }
    public void incrementItems() {
        itemsFound++;
    }

    public void incrementDeaths() {
        deaths++;
    }

    public void addChest(String chest) {
        chests.add(chest);
    }

    public void addKey(String key) {
        keys.add(key);
    }

    public void addItem(String item) {
        items.add(item);
    }

    public int getChestsFound() {
        return chestsFound;
    }

    public int getKeysFound() {
        return keysFound;
    }

    public int getItemsFound() {
        return itemsFound;
    }

    public int getDeaths() {
        return deaths;
    }

    public ArrayList<String> getChests() {
        return chests;
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    public ArrayList<String> getItems() {
        return items;
    }
}
