package net.team11.pixeldungeon.entities.blocks;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.InteractionComponent;

import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.utils.Assets;
import net.team11.pixeldungeon.utils.CollisionCategory;

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
                (CollisionCategory.ENTITY),
                (byte)(CollisionCategory.ENTITY | CollisionCategory.PUZZLE_AREA | CollisionCategory.BOUNDARY),
                BodyDef.BodyType.StaticBody));
        this.addComponent(animationComponent = new AnimationComponent(0));
        this.addComponent(new InteractionComponent(this));

        setupAnimations(animationComponent);
    }

    public void setupAnimations(AnimationComponent animationComponent){
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        animationComponent.addAnimation(AssetName.TMP_OFF, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.TMP_ON, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        if (activated) {
            animationComponent.setAnimation(AssetName.TMP_OFF);
        } else {
            animationComponent.setAnimation(AssetName.TMP_ON);
        }
    }

    public void setActivated(boolean activated){
        this.activated = activated;
        if (!activated){
            getComponent(AnimationComponent.class).setAnimation(AssetName.TMP_ON);
        } else {
            getComponent(AnimationComponent.class).setAnimation(AssetName.TMP_OFF);
        }
    }

    @Override
    public void doInteraction(boolean isPlayer){
        if (!isPlayer){
            setActivated(!activated);
            for (Entity entity : targetEntities){
                entity.doInteraction(isPlayer);
            }
        }
    }

}
