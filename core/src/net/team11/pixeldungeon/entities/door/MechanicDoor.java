package net.team11.pixeldungeon.entities.door;

import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entity.component.InteractionComponent;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.assets.Messages;

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
        this.addComponent(new InteractionComponent(this));
    }

    @Override
    public void doInteraction(boolean isPlayer){
        if (isPlayer) {
            if (!isOpen()) {
                String message = Messages.DOOR_CANNOT_OPEN;
                PlayScreen.uiManager.initTextBox(message);
            }
        } else {
            setOpened(!isOpen());
        }
    }
}
