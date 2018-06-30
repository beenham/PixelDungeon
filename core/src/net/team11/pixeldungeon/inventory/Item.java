package net.team11.pixeldungeon.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.team11.pixeldungeon.utils.assets.Assets;

public class Item {
    protected String name;
    protected String displayName;
    protected String description;
    protected int amount;
    protected int cost;
    protected int id;
    protected Image image;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public int getCost() {
        return cost;
    }

    public int getId() {
        return id;
    }

    public Image getImage() {
        if (image != null) {
            return image;
        } else {
            return image = new Image(Assets.getInstance()
                    .getTextureSet(Assets.ITEMS).findRegion(name));
        }
    }

    @Override
    public String toString() {
        String s = "ID: " + id + "\n" +
                "Display Name : " + name + "\n" +
                "Description: " + description + "\n" +
                "Amount: " + amount + "\n" +
                "Cost: " + cost;
        return s;
    }
}
