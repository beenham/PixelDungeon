package net.team11.pixeldungeon.entity.system;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.PositionComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.map.MapManager;
import net.team11.pixeldungeon.options.Direction;
import net.team11.pixeldungeon.screens.PlayScreen;

import java.util.ArrayList;
import java.util.List;

public class RenderSystem extends EntitySystem {
    public static float FRAME_SPEED = 14f;

    private SpriteBatch spriteBatch;
    private List<Entity> entities = new ArrayList<>();
    private MapManager mapManager;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public RenderSystem(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void init(EntityEngine entityEngine) {
        mapManager = MapManager.getInstance();
        entities = entityEngine.getEntities(AnimationComponent.class, VelocityComponent.class, PositionComponent.class, PlayerComponent.class);
    }

    @Override
    public void update(float delta) {
        mapManager.renderBackGround();
        mapManager.renderEnvironment();

        spriteBatch.begin();
        for (Entity entity : entities) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
            VelocityComponent velocityComponent = entity.getComponent(VelocityComponent.class);
            BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);

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
        spriteBatch.end();

        mapManager.renderDecor();

    }
}
