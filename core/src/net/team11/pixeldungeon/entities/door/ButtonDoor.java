package net.team11.pixeldungeon.entities.door;

import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entity.component.InteractionComponent;

public class ButtonDoor extends Door {
    public ButtonDoor (String name, Rectangle bounds, boolean open) {
        super(name, bounds, Type.BUTTON, open);
        this.addComponent(new InteractionComponent(this));
    }

    @Override
    public void doInteraction() {
        if (type == Type.BUTTON) {
            if (!open) {
                setOpened(true);
            }
        }
    }
}
