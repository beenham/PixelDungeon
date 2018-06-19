package net.team11.pixeldungeon.items.keys;

import net.team11.pixeldungeon.items.Item;

public class Key extends Item {
    protected static final String chestKeyName = "Chest Key";
    protected static final String doorKeyName = "Door Key";
    protected static final String endKeyName = "End Key";
    public Key(String name) {
        super(name,true);
    }
}
