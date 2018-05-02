package net.team11.pixeldungeon.entity.system;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.InteractionComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.screens.PlayScreen;
import net.team11.pixeldungeon.utils.Direction;

import java.util.ArrayList;
import java.util.List;

public class InteractionSystem extends EntitySystem {

    private List<Entity> entities = new ArrayList<>();
    private Player player;

    @Override
    public void init(EntityEngine entityEngine) {
        entities = entityEngine.getEntities(InteractionComponent.class, BodyComponent.class);
        player = (Player) entityEngine.getEntities(PlayerComponent.class).get(0);
    }

    @Override
    public void update(float delta) {
        BodyComponent body = player.getComponent(BodyComponent.class);
        Body playerBody = body.getBody();
        Direction direction = player.getComponent(VelocityComponent.class).getDirection();
        Rectangle playerRect = new Rectangle(body.getX()-body.getWidth()/2, body.getY(),
                        body.getWidth(), body.getHeight());

        float distance = 1;
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
                BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);
                InteractionComponent interactionComponent = entity.getComponent(InteractionComponent.class);
                Body entityBody = entity.getComponent(BodyComponent.class).getBody();

                if (interactionComponent.isInteracting()) {
                    interactionComponent.setInteractTime(interactionComponent.getInteractTime() - delta * RenderSystem.FRAME_SPEED);
                    if (interactionComponent.getInteractTime() <= 0f) {
                        interactionComponent.setInteracting(false);
                        interactionComponent.setInteractTime(0);
                    }
                    continue;
                }

                Rectangle entityRect = new Rectangle(bodyComponent.getX(), bodyComponent.getY(),
                        bodyComponent.getWidth(), bodyComponent.getHeight());

                PlayScreen.world.getContactList();
                if (playerRect.overlaps(entityRect)) {
                    if (player.getComponent(InteractionComponent.class).isInteracting()) {
                        interactionComponent.doInteraction();
                    }
                }
            }
        }
    }
}
