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
            skins.remove(skin);
            skins.put(skin,bool);
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
}
