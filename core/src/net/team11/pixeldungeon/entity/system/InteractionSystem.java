package net.team11.pixeldungeon.entity.system;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entities.blocks.Chest;
import net.team11.pixeldungeon.entities.blocks.Lever;
import net.team11.pixeldungeon.entities.door.DungeonDoor;
import net.team11.pixeldungeon.entities.door.LockedDoor;
import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.InteractionComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.utils.CollisionUtil;

import java.util.ArrayList;
import java.util.List;

public class InteractionSystem extends EntitySystem {

    private List<Entity> entities = null;
    private Player player;

    @Override
    public void init(EntityEngine entityEngine) {
        entities = new ArrayList<>(entityEngine.getEntities(InteractionComponent.class, BodyComponent.class).size());
        entities = entityEngine.getEntities(InteractionComponent.class, BodyComponent.class);
        player = (Player) entityEngine.getEntities(PlayerComponent.class).get(0);
    }

    @Override
    public void update(float delta) {
        BodyComponent body = player.getComponent(BodyComponent.class);
        Polygon playerBox = body.getPolygon();

        float distance = 1;
        switch (player.getComponent(VelocityComponent.class).getDirection()) {
            case UP:
                playerBox = CollisionUtil.createRectangle(body.getX(),body.getY()+distance,
                        body.getWidth(),body.getHeight());
                break;
            case DOWN:
                playerBox = CollisionUtil.createRectangle(body.getX(),body.getY()-distance,
                        body.getWidth(),body.getHeight());
                break;
            case RIGHT:
                playerBox = CollisionUtil.createRectangle(body.getX() + distance,body.getY(),
                        body.getWidth(),body.getHeight());
                break;
            case LEFT:
                playerBox = CollisionUtil.createRectangle(body.getX() - distance,body.getY(),
                        body.getWidth(),body.getHeight());
                break;
        }

        for (Entity entity : entities) {
            if (!(entity instanceof Player)) {
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
                Polygon entityBox = bodyComponent.getPolygon();
                if (CollisionUtil.isOverlapping(entityBox,playerBox)) {
                    if (player.getComponent(InteractionComponent.class).isInteracting()) {
                        interactionComponent.doInteraction();
                        if (entity instanceof Chest) {
                            Chest chest = (Chest) entity;
                            chest.doInteraction(player);
                        } else if (entity instanceof LockedDoor){
                            LockedDoor lockedDoor = (LockedDoor) entity;
                            lockedDoor.doInteraction(player);
                        } else if (entity instanceof DungeonDoor){
                            DungeonDoor dungeonDoor = (DungeonDoor) entity;
                            dungeonDoor.doInteraction(player);
                        }
                    }
                }
            }
        }
    }
}
