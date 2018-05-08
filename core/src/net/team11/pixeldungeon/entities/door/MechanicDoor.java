package net.team11.pixeldungeon.entities.door;

import com.badlogic.gdx.math.Rectangle;

public class MechanicDoor extends Door {
    public MechanicDoor (String name, Rectangle bounds, boolean open) {
        super(name, bounds, Type.MECHANIC, open);
    }
}
