package net.team11.pixeldungeon.entity.system;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entities.door.Door;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.DoorComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.map.MapManager;
import net.team11.pixeldungeon.utils.TiledMapLayers;
import net.team11.pixeldungeon.utils.TiledMapObjectNames;
import net.team11.pixeldungeon.utils.TiledMapProperties;

import java.util.ArrayList;
import java.util.List;

public class VelocitySystem extends EntitySystem {

    private List<Entity> players = new ArrayList<>();
    private List<Entity> entities = new ArrayList<>();
    private MapManager mapManager;

    @Override
    public void init(EntityEngine entityEngine) {
        players = entityEngine.getEntities(VelocityComponent.class, PlayerComponent.class);
        entities = entityEngine.getEntities(BodyComponent.class);
        mapManager = MapManager.getInstance();
    }

    @Override
    public void update(float delta) {
        for (Entity entity : players) {
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

            float startX = bodyComponent.getX();
            float startY = bodyComponent.getY();

            Rectangle entityRectangle = new Rectangle(startX,startY, bodyComponent.getWidth(), bodyComponent.getHeight());

            bodyComponent.moveX(1 * velocityComponent.getxDirection() * velocityComponent.getMovementSpeed());
            bodyComponent.moveY(1 * velocityComponent.getyDirection() * velocityComponent.getMovementSpeed());

            isOverlapping(entityRectangle);
        }
    }

    private void isOverlapping(Rectangle entityRectangle) {
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
    }
}
