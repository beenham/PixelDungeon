package net.team11.pixeldungeon.game.entity.system;

import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entity.component.CameraComponent;
import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.game.entitysystem.EntityEngine;
import net.team11.pixeldungeon.game.entitysystem.EntitySystem;

public class CameraSystem extends EntitySystem {
    private Entity player;

    @Override
    public void init(EntityEngine entityEngine) {
        this.player = entityEngine.getEntities(CameraComponent.class, BodyComponent.class).get(0);
    }

    @Override
    public void update(float delta) {
        BodyComponent bodyComponent = this.player.getComponent(BodyComponent.class);
        CameraComponent cameraComponent = this.player.getComponent(CameraComponent.class);
        cameraComponent.setPosition(bodyComponent.getX(),bodyComponent.getY());
    }
}
