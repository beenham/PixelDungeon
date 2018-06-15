package net.team11.pixeldungeon.entity.system;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.map.MapManager;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.utils.stats.StatsUtil;
import net.team11.pixeldungeon.utils.tiled.TiledMapLayers;
import net.team11.pixeldungeon.utils.tiled.TiledMapObjectNames;
import net.team11.pixeldungeon.utils.tiled.TiledMapProperties;

import java.util.ArrayList;
import java.util.List;

public class VelocitySystem extends EntitySystem {

    private List<Entity> players = new ArrayList<>();
    private MapManager mapManager;

    @Override
    public void init(EntityEngine entityEngine) {
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
                            //ScreenTransitionSlide.init(1.5f,ScreenTransitionSlide.RIGHT, true, Interpolation.fade),
                            //ScreenTransitionSlice.init(0.5f,ScreenTransitionSlice.UP_DOWN,2,Interpolation.pow2),
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
                    StatsUtil.getInstance().getLevelStats(MapManager.getInstance().getCurrentMap().getMapName()).incrementCompleted();
                    StatsUtil.getInstance().writeLevelStats(MapManager.getInstance().getCurrentMap().getMapName());
                    ScreenManager.getInstance().changeScreen(ScreenEnum.MAIN_MENU,
                            //ScreenTransitionSlide.init(1.5f,ScreenTransitionSlide.RIGHT, true, Interpolation.fade),
                            //ScreenTransitionSlice.init(0.5f,ScreenTransitionSlice.UP_DOWN,2,Interpolation.pow2),
                            null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
