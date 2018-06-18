package net.team11.pixeldungeon.entities.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.BoxComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.utils.CollisionUtil;

public class Box extends Entity {
    private boolean pushable;

    public Box(Rectangle bounds, boolean pushable, String name) {
        super(name);
        this.pushable = pushable;

        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;

        AnimationComponent animationComponent;
        this.addComponent(new BoxComponent(this));
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 7f,
                (CollisionUtil.ENTITY),
                (byte)(CollisionUtil.ENTITY | CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.DynamicBody));
        this.addComponent(new VelocityComponent(60));
        this.addComponent(animationComponent = new AnimationComponent(0));

        setupAnimations(animationComponent);
    }

    private void setupAnimations(AnimationComponent animationComponent) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("entities/Blocks.atlas"));
        animationComponent.addAnimation(AssetName.BOX_IDLE, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(AssetName.BOX_IDLE);
    }

    public boolean isPushable() {
        return pushable;
    }
}
