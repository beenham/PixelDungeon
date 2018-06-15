package net.team11.pixeldungeon.entity.system;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.InventoryComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.map.MapManager;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionFade;
import net.team11.pixeldungeon.utils.stats.StatsUtil;
import net.team11.pixeldungeon.utils.tiled.TiledMapLayers;
import net.team11.pixeldungeon.utils.tiled.TiledMapObjectNames;
import net.team11.pixeldungeon.utils.tiled.TiledMapProperties;

import java.util.ArrayList;
import java.util.List;

public class VelocitySystem extends EntitySystem {
    private List<Entity> players = new ArrayList<>();
    private MapManager mapManager;
    private EntityEngine engine;

    @Override
    public void init(EntityEngine entityEngine) {
        engine = entityEngine;
        players = entityEngine.getEntities(VelocityComponent.class, PlayerComponent.class);
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
            bodyComponent.moveX(1 * velocityComponent.getxDirection() * velocityComponent.getMovementSpeed());
            bodyComponent.moveY(1 * velocityComponent.getyDirection() * velocityComponent.getMovementSpeed());

            isOverlapping(bodyComponent.getRectangle());
        }
    }

    private void isOverlapping(Rectangle entityRectangle) {
        try {
            RectangleMapObject mapObject = mapManager.getCurrentMap().getRectangleObject(TiledMapLayers.POINTS_LAYER, TiledMapObjectNames.LAYER_EXIT);
            Rectangle collision = new Rectangle(mapObject.getRectangle());
            if (entityRectangle.overlaps(collision)) {
                if (mapObject.getProperties().containsKey(TiledMapProperties.MAP)) {
                    ScreenManager.getInstance().changeScreen(ScreenEnum.GAME,
                            null,
                            mapObject.getProperties().get(TiledMapProperties.MAP));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            RectangleMapObject mapObject = mapManager.getCurrentMap().getRectangleObject(TiledMapLayers.POINTS_LAYER, TiledMapObjectNames.MAP_EXIT);
            Rectangle collision = new Rectangle(mapObject.getRectangle());
            if (entityRectangle.overlaps(collision)) {
                engine.finish();
                ScreenManager.getInstance().changeScreen(ScreenEnum.LEVEL_COMPLETE,
                        ScreenTransitionFade.init(1f),
                        players.get(0).getComponent(InventoryComponent.class));
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
}
