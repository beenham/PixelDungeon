package net.team11.pixeldungeon.entity.system;

import net.team11.pixeldungeon.entity.component.CameraComponent;
import net.team11.pixeldungeon.entity.component.PositionComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.map.MapManager;
import net.team11.pixeldungeon.screens.PlayScreen;

public class CameraSystem extends EntitySystem {

    private Entity player;

    @Override
    public void init(EntityEngine entityEngine) {
        this.player = entityEngine.getEntities(CameraComponent.class, PositionComponent.class).get(0);
    }

    @Override
    public void update(float delta) {
        PositionComponent positionComponent = this.player.getComponent(PositionComponent.class);
        CameraComponent cameraComponent = this.player.getComponent(CameraComponent.class);

        cameraComponent.getCamera().position.x = positionComponent.getX();
        cameraComponent.getCamera().position.y = positionComponent.getY();
    }
}
