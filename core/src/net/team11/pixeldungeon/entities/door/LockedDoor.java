package net.team11.pixeldungeon.entities.door;

import com.badlogic.gdx.math.Rectangle;

public class LockedDoor extends Door {
    private String key;

    public LockedDoor (String name, Rectangle bounds, boolean open, String keyName) {
        super(name, bounds, Type.LOCKED, open);
        this.key = keyName;
    }
}
