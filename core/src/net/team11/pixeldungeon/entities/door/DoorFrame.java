package net.team11.pixeldungeon.entities.door;

import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entity.component.entitycomponent.DoorFrameComponent;
import net.team11.pixeldungeon.entitysystem.Entity;

public class DoorFrame extends Entity {
    private Rectangle bounds;
    private String name;

    public DoorFrame(Rectangle rectangle, String name) {
        this.addComponent(new DoorFrameComponent(this));
        this.bounds = new Rectangle(rectangle);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
