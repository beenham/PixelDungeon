package net.team11.pixeldungeon.entity.system;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entities.blocks.Chest;
import net.team11.pixeldungeon.entities.door.Door;
import net.team11.pixeldungeon.entities.door.DoorFrame;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.PositionComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.ChestComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.DoorComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.DoorFrameComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.map.MapManager;
import net.team11.pixeldungeon.options.TiledMapLayers;
import net.team11.pixeldungeon.options.TiledMapObjectNames;
import net.team11.pixeldungeon.options.TiledMapProperties;

import java.util.ArrayList;
import java.util.List;

public class VelocitySystem extends EntitySystem {

    private List<Entity> entities = new ArrayList<>();
    private List<Entity> chests = new ArrayList<>();
    private List<Entity> doors = new ArrayList<>();
    private List<Entity> doorFrames = new ArrayList<>();
    private MapManager mapManager;

    private enum Axis {
        X,
        Y
    }

    @Override
    public void init(EntityEngine entityEngine) {
        entities = entityEngine.getEntities(PositionComponent.class, VelocityComponent.class);
        doors = entityEngine.getEntities(DoorComponent.class);
        doorFrames = entityEngine.getEntities(DoorFrameComponent.class);
        chests = entityEngine.getEntities(ChestComponent.class);
        mapManager = MapManager.getInstance();
    }

    @Override
    public void update(float delta) {
        for (Entity entity : entities) {
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

            //Draw rectaangle
            //drawRectangle(entityRectangle);

            //Collision
            if (startX != endX) {
                entityRectangle.x = endX - 10;
                if (!isOverlapping(entityRectangle, entity, Axis.X)) {
                    positionComponent.setX(endX);
                }
                entityRectangle.x = startX - 10;
            }

            if (startY != endY) {
                entityRectangle.y = endY;
                if (!isOverlapping(entityRectangle, entity, Axis.Y)) {
                    positionComponent.setY(endY);
                }
                entityRectangle.y = startY;
            }
        }
    }

    private boolean isOverlapping(Rectangle entityRectangle, Entity entity, Axis axis) {
        VelocityComponent velocityComponent = entity.getComponent(VelocityComponent.class);
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

                try {
                    RectangleMapObject mapObject = mapManager.getCurrentMap().getRectangleObject(TiledMapLayers.POINTS_LAYER, TiledMapObjectNames.LAYER_EXIT);
                    Rectangle collison = new Rectangle(mapObject.getRectangle());
                    if (entityRectangle.overlaps(collison)) {
                        if (mapObject.getProperties().containsKey(TiledMapProperties.MAP)) {
                            for (Entity entity1 : doors) {
                                Door door = (Door) entity1;
                                if (door.isLocked()) {
                                    door.setLocked(false);
                                }
                            }
                            mapManager.loadMap((String) mapObject.getProperties().get(TiledMapProperties.MAP));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (Entity entity1 : doorFrames) {
                    DoorFrame door = (DoorFrame) entity1;
                    if (entityRectangle.overlaps(door.getBounds())) {
                        return true;
                    }
                }

                for (Entity entity1 : doors) {
                    Door door = (Door) entity1;
                    if (entityRectangle.overlaps(door.getBounds()) && door.isLocked()) {
                        return true;
                    }
                }

                for (Entity entity1 : chests) {
                    Chest chest = (Chest) entity1;
                    if (entityRectangle.overlaps(chest.getBounds())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
