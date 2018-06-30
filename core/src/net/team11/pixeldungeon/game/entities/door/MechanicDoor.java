package net.team11.pixeldungeon.game.entities.door;

import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.game.entity.component.InteractionComponent;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.assets.Messages;

public class MechanicDoor extends Door {
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
