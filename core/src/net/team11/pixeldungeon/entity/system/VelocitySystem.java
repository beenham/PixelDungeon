package net.team11.pixeldungeon.entity.system;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.PositionComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.map.MapManager;

import java.util.ArrayList;
import java.util.List;

public class VelocitySystem extends EntitySystem {

    private List<Entity> entities = new ArrayList<>();
    private MapManager mapManager;

    private enum Axis {
        X,
        Y
    }

    @Override
    public void init(EntityEngine entityEngine) {
        entities = entityEngine.getEntities(PositionComponent.class, VelocityComponent.class);
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

                    //Draw rectangle
                    //drawRectangle(cellRec);

                    if (entityRectangle.overlaps(cellRec)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
