package net.team11.pixeldungeon.inventory.skinselect;

import java.util.ArrayList;
import java.util.HashMap;

public class SkinList {
    private float version;
    private HashMap<String, Boolean> skins;
    private int currentSkin;

    public boolean hasSkin(String string) {
        if (skins.containsKey(string)) {
            return skins.get(string);
        }
        return false;
    }

    public void setSkin(String skin, boolean bool) {
        if (skins.containsKey(skin)) {
            skins.put(skin,bool);
        }
    }

    public void unlockSkin(String skin) {
        if (skins.containsKey(skin)) {
            skins.put(skin,true);
        }
    }

    public ArrayList<String> getSkinList() {
        return new ArrayList<>(skins.keySet());
    }

    public float getVersion() {
        return version;
    }

    public int getCurrentSkin() {
        return currentSkin;
    }

    public void setCurrentSkin(int currentSkin) {
        this.currentSkin = currentSkin;
    }
}
