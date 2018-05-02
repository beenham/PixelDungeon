package net.team11.pixeldungeon.entities.door;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.DoorComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.utils.CollisionCategory;

public class Door extends Entity {
    private boolean locked;

    public Door(Rectangle bounds, boolean locked, String name) {
        super(name);
        this.locked = locked;

        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;

        this.addComponent(new DoorComponent(this));
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 1.0f,
                (byte)(CollisionCategory.ENTITY),
                (byte)(CollisionCategory.ENTITY | CollisionCategory.PUZZLE_AREA | CollisionCategory.BOUNDARY),
                BodyDef.BodyType.StaticBody));
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
        if (locked) {
            getComponent(BodyComponent.class).createBody(BodyDef.BodyType.KinematicBody);
        } else {
            getComponent(BodyComponent.class).removeBody();
        }
    }

    public boolean isLocked() {
        return locked;
    }
}
