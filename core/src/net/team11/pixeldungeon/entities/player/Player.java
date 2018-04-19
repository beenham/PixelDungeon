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
import net.team11.pixeldungeon.entity.component.PositionComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.screens.PlayScreen;

public class Player extends Entity {
    public Player() {
        this.addComponent(new PlayerComponent(this));
        this.addComponent(new PositionComponent(PixelDungeon.V_WIDTH/2, PixelDungeon.V_HEIGHT/2));
        this.addComponent(new VelocityComponent(100));
        this.addComponent(new AnimationComponent(0));
        this.addComponent(new HealthComponent(1, 1));
        this.addComponent(new CameraComponent(PlayScreen.gameCam));

        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("entities/Player.atlas"));
        AnimationComponent animationComponent = getComponent(AnimationComponent.class);
        animationComponent.addAnimation(AnimationName.PLAYER_DOWN, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AnimationName.PLAYER_UP, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AnimationName.PLAYER_RIGHT, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AnimationName.PLAYER_LEFT, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(AnimationName.PLAYER_DOWN);
        int width = animationComponent.getAnimationList().get(AnimationName.PLAYER_LEFT).getKeyFrame(0).getRegionWidth();
        this.addComponent(new BodyComponent(width, 8));
    }
}
