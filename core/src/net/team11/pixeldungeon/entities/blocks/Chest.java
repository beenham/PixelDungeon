package net.team11.pixeldungeon.entities.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entity.animation.AnimationName;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.InteractionComponent;
import net.team11.pixeldungeon.entity.component.PositionComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.ChestComponent;
import net.team11.pixeldungeon.entitysystem.Entity;

public class Chest extends Entity {
    private boolean opened;
    private Rectangle bounds;

    public Chest(Rectangle rectangle, boolean opened, String name) {
        super(name);
        this.opened = opened;
        this.bounds = new Rectangle(rectangle);
        AnimationComponent animationComponent;
        PositionComponent positionComponent;
        this.addComponent(new ChestComponent(this));
        this.addComponent(new BodyComponent(bounds.getWidth(),bounds.getHeight()));
        this.addComponent(animationComponent = new AnimationComponent(0));
        this.addComponent(positionComponent = new PositionComponent());
        this.addComponent(new InteractionComponent(this));

        setupAnimations(animationComponent);
        setupPosition(positionComponent);
    }

    private void setupAnimations(AnimationComponent animationComponent) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("entities/Blocks.atlas"));
        animationComponent.addAnimation(AnimationName.CHEST_CLOSED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AnimationName.CHEST_OPENED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        if (opened) {
            animationComponent.setAnimation(AnimationName.CHEST_OPENED);
        } else {
            animationComponent.setAnimation(AnimationName.CHEST_CLOSED);
        }
    }

    private void setupPosition(PositionComponent positionComponent) {
        positionComponent.setX(bounds.getX());
        positionComponent.setY(bounds.getY());
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isOpened() {
        return opened;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void doInteraction() {
        if (opened) {
            getComponent(AnimationComponent.class).setAnimation(AnimationName.CHEST_CLOSED);
            opened = false;
        } else {
            getComponent(AnimationComponent.class).setAnimation(AnimationName.CHEST_OPENED);
            opened = true;
        }
    }
}
