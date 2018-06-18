package net.team11.pixeldungeon.entities.blocks;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.InteractionComponent;

import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.CollisionUtil;

/**
 * Class to handle lever objects
 * Can be changed to generic activators in future
 * Still needs the assets to be done and added
 */
public class Lever extends Entity {

    private boolean activated = false;

    public Lever(Rectangle bounds,String name){
        super(name);
        //do bounds stuff
        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;

        AnimationComponent animationComponent;
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 0,
                (CollisionUtil.ENTITY),
                (byte)(CollisionUtil.ENTITY | CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));
        this.addComponent(animationComponent = new AnimationComponent(0));
        this.addComponent(new InteractionComponent(this));

        setupAnimations(animationComponent);
    }

    private void setupAnimations(AnimationComponent animationComponent){
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        animationComponent.addAnimation(AssetName.LEVER_LEFT, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.LEVER_SWITCHING_LEFT, textureAtlas, 1.25f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.LEVER_RIGHT, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.LEVER_SWITCHING_RIGHT, textureAtlas, .75f, Animation.PlayMode.NORMAL);
        if (activated) {
            animationComponent.setAnimation(AssetName.LEVER_LEFT);
        } else {
            animationComponent.setAnimation(AssetName.LEVER_RIGHT);
        }
    }

    public void setActivated(boolean activated){
        this.activated = activated;
        if (!activated){
            getComponent(AnimationComponent.class).setAnimation(AssetName.LEVER_SWITCHING_RIGHT);
            getComponent(AnimationComponent.class).setNextAnimation(AssetName.LEVER_RIGHT);
        } else {
            getComponent(AnimationComponent.class).setAnimation(AssetName.LEVER_SWITCHING_LEFT);
            getComponent(AnimationComponent.class).setNextAnimation(AssetName.LEVER_LEFT);
        }
    }

    @Override
    public void doInteraction(){
        setActivated(!activated);
        for (Entity entity : targetEntities){
            entity.doInteraction(false);
        }
    }
}
