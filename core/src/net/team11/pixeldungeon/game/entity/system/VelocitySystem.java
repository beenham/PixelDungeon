package net.team11.pixeldungeon.game.entity.system;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entity.component.InventoryComponent;
import net.team11.pixeldungeon.game.entity.component.VelocityComponent;
import net.team11.pixeldungeon.game.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.game.entitysystem.EntityEngine;
import net.team11.pixeldungeon.game.entitysystem.EntitySystem;
import net.team11.pixeldungeon.game.map.MapManager;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionFade;
import net.team11.pixeldungeon.game.tutorial.TutorialZone;
import net.team11.pixeldungeon.utils.CollisionUtil;
import net.team11.pixeldungeon.utils.Util;
import net.team11.pixeldungeon.utils.stats.StatsUtil;
import net.team11.pixeldungeon.utils.tiled.TiledMapLayers;
import net.team11.pixeldungeon.utils.tiled.TiledMapObjectNames;

import java.util.List;

public class VelocitySystem extends EntitySystem {
    private List<Entity> players;
    private List<TutorialZone> tutorials;
    private MapManager mapManager;
    private EntityEngine engine;

    @Override
    public void init(EntityEngine entityEngine) {
        engine = entityEngine;
        players = entityEngine.getEntities(VelocityComponent.class, PlayerComponent.class);
        tutorials = entityEngine.getTutorials();
        mapManager = MapManager.getInstance();

    }

    @Override
    public void update(float delta) {
        for (Entity entity : players) {
            VelocityComponent velocityComponent = entity.getComponent(VelocityComponent.class);
            BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);

            //Paralyzing handling
            if (velocityComponent.isParalyzed()) {
                velocityComponent.setParalyzedTime(velocityComponent.getParalyzedTime() - (delta * RenderSystem.FRAME_SPEED));
                if (velocityComponent.getParalyzedTime() <= 0f) {
                    velocityComponent.setParalyzed(false);
                    velocityComponent.setParalyzedTime(0);
                }
                continue;
            }
            bodyComponent.moveX(1 * velocityComponent.getxDirection() * velocityComponent.getMovementSpeed());
            bodyComponent.moveY(1 * velocityComponent.getyDirection() * velocityComponent.getMovementSpeed());

            isOverlapping(bodyComponent.getPolygon());
            for (TutorialZone zone : tutorials) {
                if (zone.isVisible() && !CollisionUtil.isOverlapping(bodyComponent.getPolygon(),zone.getZone())) {
                    zone.exitZone();
                } else if (!zone.isVisible() && CollisionUtil.isOverlapping(bodyComponent.getPolygon(),zone.getZone())) {
                    zone.initZone();
                }
            }
        }
    }

    private void isOverlapping(Polygon entityBox) {
        try {
            RectangleMapObject mapObject = mapManager.getCurrentMap().getRectangleObject(TiledMapLayers.POINTS_LAYER, TiledMapObjectNames.MAP_EXIT);
            Rectangle collision = new Rectangle(mapObject.getRectangle());
            Polygon collisionBox = CollisionUtil.createRectangle(
                    collision.x+collision.width/2,collision.y+collision.height/2,
                    collision.width,collision.height);
            if (CollisionUtil.isOverlapping(collisionBox,entityBox)) {
                engine.finish();
                if (Util.getStatsUtil()
                        .getLevelStats(MapManager.getInstance().getCurrentMap().getMapName())
                        .isTutorial()) {
                    ScreenManager.getInstance().changeScreen(ScreenEnum.MAIN_MENU,
                            ScreenTransitionFade.init(1f));
                }
                ScreenManager.getInstance().changeScreen(ScreenEnum.LEVEL_COMPLETE,
                        ScreenTransitionFade.init(1f),
                        players.get(0).getComponent(InventoryComponent.class));
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
}
