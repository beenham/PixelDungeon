package net.team11.pixeldungeon.entities.door;

import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entity.component.entitycomponent.DoorComponent;
import net.team11.pixeldungeon.entitysystem.Entity;

public class Door extends Entity {
    private boolean locked;
    private Rectangle bounds;
    private String doorType;

    public Door(Rectangle rectangle, boolean locked, String doorType) {
        this.addComponent(new DoorComponent(this));
        this.locked = locked;
        this.bounds = new Rectangle(rectangle);
        this.doorType = doorType;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getDoorType() {
        return doorType;
    }

    public boolean isLocked() {
        return locked;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
