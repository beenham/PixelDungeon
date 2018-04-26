package net.team11.pixeldungeon.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.entity.animation.AnimationName;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.CameraComponent;
import net.team11.pixeldungeon.entity.component.HealthComponent;
import net.team11.pixeldungeon.entity.component.InteractionComponent;
import net.team11.pixeldungeon.entity.component.PositionComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.options.Direction;
import net.team11.pixeldungeon.screens.PlayScreen;

public class Player extends Entity {
    public Player() {
        VelocityComponent velocityComponent;
        AnimationComponent animationComponent;
        this.addComponent(new PlayerComponent(this));
        this.addComponent(new PositionComponent(PixelDungeon.V_WIDTH/2, PixelDungeon.V_HEIGHT/2));
        this.addComponent(velocityComponent = new VelocityComponent(100));
        this.addComponent(animationComponent = new AnimationComponent(0));
        this.addComponent(new HealthComponent(1, 1));
        this.addComponent(new CameraComponent(PlayScreen.gameCam));
        this.addComponent(new InteractionComponent(this));

        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("entities/Player.atlas"));
        animationComponent.addAnimation(AnimationName.PLAYER_MOVING_DOWN, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AnimationName.PLAYER_MOVING_UP, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AnimationName.PLAYER_MOVING_RIGHT, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AnimationName.PLAYER_MOVING_LEFT, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AnimationName.PLAYER_IDLE_DOWN, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AnimationName.PLAYER_IDLE_UP, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AnimationName.PLAYER_IDLE_RIGHT, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AnimationName.PLAYER_IDLE_LEFT, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AnimationName.PLAYER_INTERACTING_UP, textureAtlas, 1.75f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AnimationName.PLAYER_INTERACTING_DOWN, textureAtlas, 1.75f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AnimationName.PLAYER_INTERACTING_RIGHT, textureAtlas, 1.75f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AnimationName.PLAYER_INTERACTING_LEFT, textureAtlas, 1.75f, Animation.PlayMode.NORMAL);

        animationComponent.setAnimation(AnimationName.PLAYER_IDLE_DOWN);
        velocityComponent.setDirection(Direction.DOWN);

        int width = animationComponent.getAnimationList().get(AnimationName.PLAYER_IDLE_LEFT).getKeyFrame(0).getRegionWidth();
        System.out.println("WIDTH : " + width);
        this.addComponent(new BodyComponent(width-3, 8));
    }
}
