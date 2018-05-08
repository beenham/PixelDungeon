package net.team11.pixeldungeon.entities.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.TorchComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.screens.PlayScreen;
import net.team11.pixeldungeon.utils.CollisionCategory;

import box2dLight.Light;
import box2dLight.PointLight;

public class Torch extends Entity {
    private boolean on;
    private Light light;

    public Torch(Rectangle bounds, boolean on, String name) {
        super(name);
        this.on = on;

        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;

        AnimationComponent animationComponent;
        this.addComponent(new TorchComponent(this));
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 0f,
                (byte)(CollisionCategory.ENTITY),
                (byte)(CollisionCategory.ENTITY | CollisionCategory.PUZZLE_AREA | CollisionCategory.BOUNDARY),
                BodyDef.BodyType.StaticBody));
        this.addComponent(animationComponent = new AnimationComponent(0));
        setupAnimations(animationComponent);
        if (on) {
            setupLight(bounds);
        }
    }

    private void setupAnimations(AnimationComponent animationComponent) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("entities/Blocks.atlas"));
        animationComponent.addAnimation(AssetName.TORCH_OFF, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.TORCH_ON, textureAtlas, 1.9f, Animation.PlayMode.LOOP_PINGPONG);
        if (on) {
            animationComponent.setAnimation(AssetName.TORCH_ON);
        } else {
            animationComponent.setAnimation(AssetName.TORCH_OFF);
        }
    }

    private void setupLight(Rectangle bounds) {
        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()*2;
        light = new PointLight(PlayScreen.rayHandler,150,Color.ORANGE,150, posX, posY);
        light.setSoftnessLength(0f);
    }

    public void setLightSize(float size) {
        light.setDistance(size);
    }
}
