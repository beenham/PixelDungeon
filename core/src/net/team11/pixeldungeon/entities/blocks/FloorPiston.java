package net.team11.pixeldungeon.entities.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.utils.CollisionUtil;
import net.team11.pixeldungeon.utils.assets.AssetName;

public class FloorPiston extends Entity {
    private boolean activated;

    public FloorPiston(Rectangle bounds, boolean activated, String name) {
        super(name);
        this.activated = activated;

        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;

        AnimationComponent animationComponent;
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 0,
                (CollisionUtil.ENTITY),
                (byte)(CollisionUtil.ENTITY | CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));
        this.addComponent(animationComponent = new AnimationComponent(0));

        setupAnimations(animationComponent);
    }

    private void setupAnimations(AnimationComponent animationComponent) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("entities/Blocks.atlas"));
        animationComponent.addAnimation(AssetName.FLOORPISTON_ACTIVATED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.FLOORPISTON_DEACTIVATED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.FLOORPISTON_ACTIVATING, textureAtlas, 0.5f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.FLOORPISTON_DEACTIVATING, textureAtlas, 0.5f, Animation.PlayMode.NORMAL);

        if (activated) {
            animationComponent.setAnimation(AssetName.FLOORPISTON_ACTIVATED);
        } else {
            animationComponent.setAnimation(AssetName.FLOORPISTON_DEACTIVATED);
            getComponent(BodyComponent.class).removeBody();
        }
    }

    @Override
    public void doInteraction(boolean isPlayer) {
        if (!isPlayer) {
            activated = !activated;
            BodyComponent bodyComponent = getComponent(BodyComponent.class);
            if (activated) {
                bodyComponent.recreateBody(BodyDef.BodyType.StaticBody);
                getComponent(AnimationComponent.class).setAnimation(AssetName.FLOORPISTON_ACTIVATING);
                getComponent(AnimationComponent.class).setNextAnimation(AssetName.FLOORPISTON_ACTIVATED);
            } else {
                bodyComponent.removeBody();
                getComponent(AnimationComponent.class).setAnimation(AssetName.FLOORPISTON_DEACTIVATING);
                getComponent(AnimationComponent.class).setNextAnimation(AssetName.FLOORPISTON_DEACTIVATED);
            }
        }
    }
}