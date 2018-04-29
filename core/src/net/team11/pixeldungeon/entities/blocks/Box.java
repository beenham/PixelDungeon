package net.team11.pixeldungeon.entities.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entity.animation.AnimationName;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.PositionComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.BoxComponent;
import net.team11.pixeldungeon.entitysystem.Entity;

public class Box extends Entity {
    private boolean pushable;
    private Rectangle bounds;

    public Box(Rectangle bounds, boolean pushable, String name) {
        super(name);
        this.pushable = pushable;
        this.bounds = bounds;

        AnimationComponent animationComponent;
        PositionComponent positionComponent;
        this.addComponent(new BoxComponent(this));
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight()));
        this.addComponent(new VelocityComponent(60));
        this.addComponent(animationComponent = new AnimationComponent(0));
        this.addComponent(positionComponent = new PositionComponent());

        setupAnimations(animationComponent);
        setupPosition(positionComponent);
    }

    private void setupAnimations(AnimationComponent animationComponent) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("entities/Blocks.atlas"));
        animationComponent.addAnimation(AnimationName.BOX_IDLE, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(AnimationName.BOX_IDLE);
    }

    private void setupPosition(PositionComponent positionComponent) {
        positionComponent.setX(bounds.getX());
        positionComponent.setY(bounds.getY());
    }

    public boolean isPushable() {
        return pushable;
    }
}
