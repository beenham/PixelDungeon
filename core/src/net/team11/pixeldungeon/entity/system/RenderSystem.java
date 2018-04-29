package net.team11.pixeldungeon.entity.system;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.PositionComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.map.MapManager;

import java.net.StandardSocketOptions;
import java.util.ArrayList;
import java.util.List;

public class RenderSystem extends EntitySystem {
    public static float FRAME_SPEED = 14f;

    private SpriteBatch spriteBatch;
    private List<Entity> players = new ArrayList<>();
    private List<Entity> entities = new ArrayList<>();
    private MapManager mapManager;

    public RenderSystem(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void init(EntityEngine entityEngine) {
        mapManager = MapManager.getInstance();
        entities = entityEngine.getEntities(AnimationComponent.class, PositionComponent.class);
        players = entityEngine.getEntities(AnimationComponent.class, VelocityComponent.class, PositionComponent.class, PlayerComponent.class);
    }

    @Override
    public void update(float delta) {
        mapManager.renderBackGround();
        mapManager.renderEnvironment();

        ArrayList<Entity> entityList = new ArrayList<>();

        Entity player = players.get(0);
        for (int i = 0 ; i < entities.size() ; i++) {
            if (i == 0) {
                entityList.add(entities.get(i));
            } else {
                int size = entityList.size();
                for (int j = 0 ; j < size ; j++) {
                    if (entities.get(i).getComponent(PositionComponent.class).getY() > entityList.get(j).getComponent(PositionComponent.class).getY()) {
                        entityList.add(j, entities.get(i));
                        break;
                    } else if (j == size - 1) {
                        entityList.add(j+1, entities.get(i));
                        break;
                    }
                }
            }
        }

        spriteBatch.begin();
        for (Entity entity : entityList) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
            animationComponent.setStateTime(animationComponent.getStateTime() + (delta * FRAME_SPEED));
            Animation<TextureRegion> currentAnimation = animationComponent.getCurrentAnimation();
            int width = currentAnimation.getKeyFrame(0).getRegionWidth();
            int height = currentAnimation.getKeyFrame(0).getRegionHeight();

            if (entity.equals(player)) {
                spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                        positionComponent.getX() - width/2 - 4,
                        positionComponent.getY(),
                        width,
                        height);
            } else {
                spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                        positionComponent.getX(),
                        positionComponent.getY(),
                        width,
                        height);
            }
        }
        spriteBatch.end();
        mapManager.renderDecor();
    }
}
