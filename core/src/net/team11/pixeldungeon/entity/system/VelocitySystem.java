package net.team11.pixeldungeon.entity.system;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entities.blocks.Box;
import net.team11.pixeldungeon.entities.blocks.Chest;
import net.team11.pixeldungeon.entities.door.Door;
import net.team11.pixeldungeon.entities.door.DoorFrame;
import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.PositionComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.BoxComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.ChestComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.DoorComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.DoorFrameComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.map.MapManager;
import net.team11.pixeldungeon.options.Direction;
import net.team11.pixeldungeon.options.TiledMapLayers;
import net.team11.pixeldungeon.options.TiledMapObjectNames;
import net.team11.pixeldungeon.options.TiledMapProperties;

import java.util.ArrayList;
import java.util.List;

public class VelocitySystem extends EntitySystem {

    private List<Entity> players = new ArrayList<>();
    private List<Entity> entities = new ArrayList<>();
    private MapManager mapManager;

    @Override
    public void init(EntityEngine entityEngine) {
        players = entityEngine.getEntities(PositionComponent.class, VelocityComponent.class, PlayerComponent.class);
        entities = entityEngine.getEntities(PositionComponent.class, BodyComponent.class);
        mapManager = MapManager.getInstance();
    }

    @Override
    public void update(float delta) {
        for (Entity entity : players) {
            PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
            VelocityComponent velocityComponent = entity.getComponent(VelocityComponent.class);
            BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);

            //Paralyzing handling
            if (velocityComponent.isParalyzed()) {
                velocityComponent.setParalyzedTime(velocityComponent.getParalyzedTime() - delta * RenderSystem.FRAME_SPEED);
                if (velocityComponent.getParalyzedTime() <= 0f) {
                    velocityComponent.setParalyzed(false);
                    velocityComponent.setParalyzedTime(0);
                }
                continue;
            }

            float startX = positionComponent.getX();
            float endX = startX + (velocityComponent.getxDirection() * velocityComponent.getMovementSpeed() * delta);
            float startY = positionComponent.getY();
            float endY = startY + (velocityComponent.getyDirection() * velocityComponent.getMovementSpeed() * delta);

            Rectangle entityRectangle = new Rectangle(startX - 10, startY, bodyComponent.getWidth(), bodyComponent.getHeight());

            //Collision
            if (startX != endX) {
                entityRectangle.x = endX - 10;
                if (!isOverlapping(entityRectangle, entity, delta)) {
                    positionComponent.setX(startX + (velocityComponent.getxDirection() * velocityComponent.getMovementSpeed() * delta));
                }
                entityRectangle.x = startX - 10;
            }

            if (startY != endY) {
                entityRectangle.y = endY;
                if (!isOverlapping(entityRectangle, entity, delta)) {
                    positionComponent.setY(startY + (velocityComponent.getyDirection() * velocityComponent.getMovementSpeed() * delta));
                }
                entityRectangle.y = startY;
            }
        }
    }

    private boolean isOverlapping(Rectangle entityRectangle, Entity entity, float delta) {
        List<TiledMapTileLayer> layers = new ArrayList<>();
        layers.add((TiledMapTileLayer) mapManager.getCurrentMap().getMap().getLayers().get("walls"));
        for (TiledMapTileLayer layer : layers) {
            float rectangleMiddleX = entityRectangle.x + 16;
            float rectangleMiddleY = entityRectangle.y + 16;

            for (int i = -3; i < 4; i++) {
                for (int j = -3; j < 4; j++) {
                    float cellX = rectangleMiddleX + (i * 16);
                    float cellY = rectangleMiddleY + (j * 16);

                    TiledMapTileLayer.Cell cell = layer.getCell((int) (cellX / 16), (int) (cellY / 16));

                    if (cell == null || cell.getTile() == null) {
                        continue;
                    }

                    float moduloX = cellX % 16;
                    float moduloY = cellY % 16;

                    float recX = cellX - moduloX;
                    float recY = cellY - moduloY;

                    Rectangle cellRec = new Rectangle(recX, recY, 16, 16);

                    if (entityRectangle.overlaps(cellRec)) {
                        return true;
                    }
                }
            }
            try {
                RectangleMapObject mapObject = mapManager.getCurrentMap().getRectangleObject(TiledMapLayers.POINTS_LAYER, TiledMapObjectNames.LAYER_EXIT);
                Rectangle collison = new Rectangle(mapObject.getRectangle());
                if (entityRectangle.overlaps(collison)) {
                    if (mapObject.getProperties().containsKey(TiledMapProperties.MAP)) {
                        for (Entity entity1 : entities) {
                            if (entity1.hasComponent(DoorComponent.class)) {
                                Door door = (Door) entity1;
                                if (mapObject.getProperties().containsKey(TiledMapProperties.TARGET)) {
                                    if (door.getName().equals(mapObject.getProperties().get(TiledMapProperties.TARGET))) {
                                        if (door.isLocked()) {
                                            door.setLocked(false);
                                        }
                                    }
                                }
                            }
                        }
                        mapManager.loadMap((String) mapObject.getProperties().get(TiledMapProperties.MAP));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (Entity entity1 : entities) {
                if (entity1.hasComponent(BoxComponent.class) && !entity1.equals(entity)) {
                    BodyComponent body = entity1.getComponent(BodyComponent.class);
                    PositionComponent position = entity1.getComponent(PositionComponent.class);
                    Rectangle entityRect = new Rectangle(position.getX(), position.getY(), body.getWidth(), body.getHeight());
                    if (entityRectangle.overlaps(entityRect) && ((Box)entity1).isPushable()) {
                        if (entity.hasComponent(BoxComponent.class)) {
                            return true;
                        }

                        VelocityComponent velocityComponent = entity1.getComponent(VelocityComponent.class);
                        PositionComponent positionComponent = entity1.getComponent(PositionComponent.class);
                        Direction direction = entity.getComponent(VelocityComponent.class).getDirection();

                        entity.getComponent(VelocityComponent.class).setMovementSpeed(velocityComponent.getMovementSpeed());
                        float startX = positionComponent.getX();
                        float endX;
                        float startY = positionComponent.getY();
                        float endY;
                        switch (direction) {
                            case UP:
                                velocityComponent.setyDirection(1);
                                endY = startY + (velocityComponent.getyDirection() * velocityComponent.getMovementSpeed() * delta);
                                entityRect.y = endY;
                                if (!isOverlapping(entityRect, entity1, delta)) {
                                    positionComponent.setY(endY);
                                    return false;
                                } else {
                                    return true;
                                }
                            case DOWN:
                                velocityComponent.setyDirection(-1);
                                endY = startY + (velocityComponent.getyDirection() * velocityComponent.getMovementSpeed() * delta);
                                entityRect.y = endY;
                                if (!isOverlapping(entityRect, entity1, delta)) {
                                    positionComponent.setY(endY);
                                    return false;
                                } else {
                                    return true;
                                }
                            case RIGHT:
                                velocityComponent.setxDirection(1);
                                endX = startX + (velocityComponent.getxDirection() * velocityComponent.getMovementSpeed() * delta);
                                entityRect.x = endX;
                                if (!isOverlapping(entityRect, entity1, delta)) {
                                    positionComponent.setX(endX);
                                    return false;
                                } else {
                                    return true;
                                }
                            case LEFT:
                                velocityComponent.setxDirection(-1);
                                endX = startX + (velocityComponent.getxDirection() * velocityComponent.getMovementSpeed() * delta);
                                entityRect.x = endX;
                                if (!isOverlapping(entityRect, entity1, delta)) {
                                    positionComponent.setX(endX);
                                    return false;
                                } else {
                                    return true;
                                }
                        }
                    }
                } else if (!entity1.hasComponent(PlayerComponent.class) && !entity1.equals(entity)) {
                    if (entity1.hasComponent(DoorComponent.class)) {
                        Door door = (Door) entity1;
                        BodyComponent body = entity1.getComponent(BodyComponent.class);
                        PositionComponent position = entity1.getComponent(PositionComponent.class);
                        Rectangle entityRect = new Rectangle(position.getX(), position.getY(), body.getWidth(), body.getHeight());
                        if (door.isLocked() && entityRectangle.overlaps(entityRect)) {
                            return true;
                        }
                    } else {
                        BodyComponent body = entity1.getComponent(BodyComponent.class);
                        PositionComponent position = entity1.getComponent(PositionComponent.class);
                        Rectangle entityRect = new Rectangle(position.getX(), position.getY(), body.getWidth(), body.getHeight());
                        if (entityRectangle.overlaps(entityRect)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
