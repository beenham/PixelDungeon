package net.team11.pixeldungeon.entities.door;

import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.PositionComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.DoorComponent;
import net.team11.pixeldungeon.entitysystem.Entity;

public class Door extends Entity {
    private boolean locked;
    private Rectangle bounds;

    public Door(Rectangle rectangle, boolean locked, String name) {
        super(name);
        this.locked = locked;
        this.bounds = new Rectangle(rectangle);

        PositionComponent positionComponent;
        this.addComponent(new DoorComponent(this));
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight()));
        this.addComponent(positionComponent = new PositionComponent());

        setupPosition(positionComponent);
    }

    private void setupPosition(PositionComponent positionComponent) {
        positionComponent.setX(bounds.getX());
        positionComponent.setY(bounds.getY());
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
