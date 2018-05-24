package net.team11.pixeldungeon.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entity.component.InventoryComponent;
import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.CameraComponent;
import net.team11.pixeldungeon.entity.component.HealthComponent;
import net.team11.pixeldungeon.entity.component.InteractionComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.utils.Assets;
import net.team11.pixeldungeon.utils.CollisionCategory;
import net.team11.pixeldungeon.utils.Direction;
import net.team11.pixeldungeon.screens.PlayScreen;

public class Player extends Entity {
    private float spawnX, spawnY;

    public Player(float posX, float posY) {
        super("Player");
        spawnX = posX; spawnY = posY;

        VelocityComponent velocityComponent;
        AnimationComponent animationComponent;
        this.addComponent(new PlayerComponent(this));
        this.addComponent(velocityComponent = new VelocityComponent(100));
        this.addComponent(animationComponent = new AnimationComponent(0));
        this.addComponent(new HealthComponent(3, 3));
        this.addComponent(new CameraComponent(PlayScreen.gameCam));
        this.addComponent(new InteractionComponent(this));
        this.addComponent(new InventoryComponent());

        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.PLAYER);
        animationComponent.addAnimation(AssetName.PLAYER_MOVING_UP, textureAtlas, 2f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_MOVING_DOWN, textureAtlas, 2f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_MOVING_LEFT, textureAtlas, 2f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_MOVING_RIGHT, textureAtlas, 2f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_IDLE_UP, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_IDLE_DOWN, textureAtlas, 4f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_IDLE_LEFT, textureAtlas, 4f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_IDLE_RIGHT, textureAtlas, 4f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_INTERACTING_UP, textureAtlas, 1.75f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.PLAYER_INTERACTING_DOWN, textureAtlas, 1.75f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.PLAYER_INTERACTING_LEFT, textureAtlas, 1.75f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.PLAYER_INTERACTING_RIGHT, textureAtlas, 1.75f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.PLAYER_PUSHING_UP, textureAtlas, 2.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_PUSHING_DOWN, textureAtlas, 2.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_PUSHING_LEFT, textureAtlas, 2.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_PUSHING_RIGHT, textureAtlas, 2.75f, Animation.PlayMode.LOOP);

        animationComponent.setAnimation(AssetName.PLAYER_IDLE_DOWN);
        velocityComponent.setDirection(Direction.DOWN);

        int width = animationComponent.getAnimationList().get(AssetName.PLAYER_IDLE_LEFT).getKeyFrame(0).getRegionWidth();
        this.addComponent(new BodyComponent(width, 8, posX, posY, 1.0f,
                (CollisionCategory.ENTITY),
                (byte)(CollisionCategory.ENTITY | CollisionCategory.BOUNDARY),
                BodyDef.BodyType.DynamicBody));
    }

    @Override
    public void respawn() {
        getComponent(BodyComponent.class).setCoords(spawnX, spawnY);
        getComponent(HealthComponent.class).setHealth(getComponent(HealthComponent.class).getMaxHealth());
    }
}
