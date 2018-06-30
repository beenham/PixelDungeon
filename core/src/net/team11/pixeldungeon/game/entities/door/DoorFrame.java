package net.team11.pixeldungeon.game.entities.door;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.game.entity.component.AnimationComponent;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.CollisionUtil;

public class DoorFrame extends Entity {

    public DoorFrame(Rectangle bounds, String name, String animation) {
        super(name);

        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;

        AnimationComponent animationComponent;
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 0f,
                (CollisionUtil.ENTITY),
                (byte)(CollisionUtil.ENTITY | CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));
        this.addComponent(animationComponent = new AnimationComponent(0));

        setupAnimations(animationComponent, animation);
    }

    private void setupAnimations(AnimationComponent animationComponent, String animation) {
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        animationComponent.addAnimation(animation, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(animation);
    }
}
