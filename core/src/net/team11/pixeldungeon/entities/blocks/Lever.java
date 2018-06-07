package net.team11.pixeldungeon.entities.blocks;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.InteractionComponent;

import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.utils.CollisionCategory;

/**
 * Class to handle lever objects
 * Can be changed to generic activators in future
 * Still needs the assets to be done and added
 */
public class Lever extends Entity {

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

    }

    @Override
    public void doInteraction(boolean isPlayer){
        if (!isPlayer){
            for (final Entity entity : targetEntities){
                entity.doInteraction(isPlayer);
            }
        }
    }

}
