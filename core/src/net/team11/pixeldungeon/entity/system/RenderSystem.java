package net.team11.pixeldungeon.entity.system;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.PositionComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.ChestComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.map.MapManager;

import java.util.ArrayList;
import java.util.List;

public class RenderSystem extends EntitySystem {
    public static float FRAME_SPEED = 14f;

    private SpriteBatch spriteBatch;
    private List<Entity> players = new ArrayList<>();
    private List<Entity> entities = new ArrayList<>();
    private List<Entity> chests = new ArrayList<>();
    private MapManager mapManager;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public RenderSystem(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void init(EntityEngine entityEngine) {
        mapManager = MapManager.getInstance();
        entities = entityEngine.getEntities(AnimationComponent.class, VelocityComponent.class, PositionComponent.class);
        players = entityEngine.getEntities(AnimationComponent.class, VelocityComponent.class, PositionComponent.class, PlayerComponent.class);
        chests = entityEngine.getEntities(AnimationComponent.class, ChestComponent.class);
    }

    @Override
    public void update(float delta) {
        mapManager.renderBackGround();
        mapManager.renderEnvironment();

        List<Entity> abovePlayer = new ArrayList<>();
        List<Entity> belowPlayer = new ArrayList<>();

        Entity player = players.get(0);
        float playerY = player.getComponent(PositionComponent.class).getY();
        for (Entity entity : chests) {
            if (entity.getComponent(PositionComponent.class).getY() > playerY) {
                abovePlayer.add(entity);
            } else {
                belowPlayer.add(entity);
            }
        }
        for (Entity entity : entities) {
            if (!entity.hasComponent(PlayerComponent.class)) {
                if (entity.getComponent(PositionComponent.class).getY() > playerY) {
                    abovePlayer.add(entity);
                } else {
                    belowPlayer.add(entity);
                }
            }
        }


        spriteBatch.begin();
        for (Entity entity : abovePlayer) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            PositionComponent positionComponent = entity.getComponent(PositionComponent.class);

            animationComponent.setStateTime(animationComponent.getStateTime() + (delta * FRAME_SPEED));
            Animation<TextureRegion> currentAnimation = animationComponent.getCurrentAnimation();
            int width = currentAnimation.getKeyFrame(0).getRegionWidth();
            int height = currentAnimation.getKeyFrame(0).getRegionHeight();

            spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                    positionComponent.getX(),
                    positionComponent.getY(),
                    width,
                    height);
        }

        for (Entity entity : players) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            PositionComponent positionComponent = entity.getComponent(PositionComponent.class);

            animationComponent.setStateTime(animationComponent.getStateTime() + (delta * FRAME_SPEED));
            Animation<TextureRegion> currentAnimation = animationComponent.getCurrentAnimation();
            int width = currentAnimation.getKeyFrame(0).getRegionWidth();
            int height = currentAnimation.getKeyFrame(0).getRegionHeight();

            spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                    positionComponent.getX() - width/2 - 4,
                    positionComponent.getY(),
                    width,
                    height);
        }

        for (Entity entity : belowPlayer) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            PositionComponent positionComponent = entity.getComponent(PositionComponent.class);

            animationComponent.setStateTime(animationComponent.getStateTime() + (delta * FRAME_SPEED));
            Animation<TextureRegion> currentAnimation = animationComponent.getCurrentAnimation();
            int width = currentAnimation.getKeyFrame(0).getRegionWidth();
            int height = currentAnimation.getKeyFrame(0).getRegionHeight();

            spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                    positionComponent.getX(),
                    positionComponent.getY(),
                    width,
                    height);
        }
        spriteBatch.end();

        mapManager.renderDecor();

    }
}
