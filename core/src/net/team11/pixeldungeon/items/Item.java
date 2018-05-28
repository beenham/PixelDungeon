package net.team11.pixeldungeon.items;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.UUID;

public class Item {
    public enum Type {
        COIN,
        DOOR_KEY,
        CHEST_KEY,
        END_KEY
    }

    protected String name;
    private Type type;
    protected int amount = 0;
    private boolean dungeonOnly;
    protected Image image;
    private UUID uuid;

    public Item(String name, Type type, boolean dungeonOnly){
        this.name = name;
        this.type = type;
        this.dungeonOnly = dungeonOnly;
        this.uuid = UUID.randomUUID();
    }

    public Image getIcon() {
        return image;
    }

    public String getName() {
        return this.name;
    }

    public Type getType() {
        return this.type;
    }

    public boolean isDungeonOnly() {
        return dungeonOnly;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return name + " : " + uuid;
    }
}
