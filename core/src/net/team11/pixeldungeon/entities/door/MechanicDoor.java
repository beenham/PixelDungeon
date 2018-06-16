package net.team11.pixeldungeon.entities.door;

import com.badlogic.gdx.math.Rectangle;

/**
 * Class to handle the mechanics behind the mechanical doors in PixelDungeon
 */
public class MechanicDoor extends Door {

    /**
     * Constructor
     * @param name : Name of the door
     * @param bounds : The bounds of the door
     * @param open : Is the door initially open or not
     */
    public MechanicDoor (String name, Rectangle bounds, boolean open) {
        super(name, bounds, Type.MECHANIC, open);
    }

    @Override
    public void doInteraction(boolean isPlayer){
        if (!isPlayer){
            setOpened(!isOpen());
        }
    }
}
