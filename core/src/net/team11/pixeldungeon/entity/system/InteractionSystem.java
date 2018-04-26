package net.team11.pixeldungeon.entity.system;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.InteractionComponent;
import net.team11.pixeldungeon.entity.component.PositionComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.options.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InteractionSystem extends EntitySystem {

    private List<Entity> entities = new ArrayList<>();
    private Player player;

    @Override
    public void init(EntityEngine entityEngine) {
        entities = entityEngine.getEntities(PositionComponent.class, InteractionComponent.class, BodyComponent.class);
        player = (Player) entityEngine.getEntities(PlayerComponent.class).get(0);
    }

    @Override
    public void update(float delta) {
        PositionComponent position = player.getComponent(PositionComponent.class);
        BodyComponent body = player.getComponent(BodyComponent.class);
        Direction direction = player.getComponent(VelocityComponent.class).getDirection();
        Rectangle playerRect = new Rectangle(position.getX()-body.getWidth()/2, position.getY(),
                        body.getWidth(), body.getHeight());

        float distance = 5;
        switch (direction) {
            case UP:
                playerRect.y = playerRect.y + distance;
                break;
            case DOWN:
                playerRect.y = playerRect.y - distance;
                break;
            case RIGHT:
                playerRect.x = playerRect.x + distance;
                break;
            case LEFT:
                playerRect.x = playerRect.x - distance;
                break;
        }

        for (Entity entity : entities) {
            if (!entity.hasComponent(PlayerComponent.class)) {
                PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
                BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);
                InteractionComponent interactionComponent = entity.getComponent(InteractionComponent.class);

                if (interactionComponent.isInteracting()) {
                    interactionComponent.setInteractTime(interactionComponent.getInteractTime() - delta * RenderSystem.FRAME_SPEED);
                    if (interactionComponent.getInteractTime() <= 0f) {
                        interactionComponent.setInteracting(false);
                        interactionComponent.setInteractTime(0);
                    }
                    continue;
                }

                Rectangle entityRect = new Rectangle(positionComponent.getX(), positionComponent.getY(),
                        bodyComponent.getWidth(), bodyComponent.getHeight());

                if (playerRect.overlaps(entityRect)) {
                    if (player.getComponent(InteractionComponent.class).isInteracting()) {
                        interactionComponent.doInteraction();
                    }
                }
            }
        }
    }
}
