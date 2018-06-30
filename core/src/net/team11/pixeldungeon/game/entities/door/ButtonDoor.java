package net.team11.pixeldungeon.game.entities.door;

import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.game.entity.component.InteractionComponent;

public class ButtonDoor extends Door {
    public ButtonDoor (String name, Rectangle bounds, boolean open) {
        super(name, bounds, Type.BUTTON, open);
        this.addComponent(new InteractionComponent(this));
    }

    @Override
    public void doInteraction(boolean isPlayer) {
        if (isPlayer) {
            if (type == Type.BUTTON) {
                if (!open) {
                    setOpened(true);
                }
            }
        }
    }
}
