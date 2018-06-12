package net.team11.pixeldungeon.entities.door;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import net.team11.pixeldungeon.entity.component.InteractionComponent;
import net.team11.pixeldungeon.statistics.GlobalStatistics;

public class ButtonDoor extends Door {
    public ButtonDoor (String name, Rectangle bounds, boolean open) {
        super(name, bounds, Type.BUTTON, open);

        this.addComponent(new InteractionComponent(this));
    }

    @Override
    public void doInteraction() {
        if (type == Type.BUTTON) {
                setOpened(!open);
        }
    }
}
