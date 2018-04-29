package net.team11.pixeldungeon.entities.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entity.animation.AnimationName;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.PositionComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.PillarComponent;
import net.team11.pixeldungeon.entitysystem.Entity;


public class Pillar extends Entity {
    public Pillar (Rectangle bounds, String name) {
        super(name);

        AnimationComponent animationComponent;
        PositionComponent positionComponent;
        this.addComponent(new PillarComponent(this));
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight()));
        this.addComponent(animationComponent = new AnimationComponent(0));
        this.addComponent(positionComponent = new PositionComponent());

        setupAnimations(animationComponent);
        setupPosition(positionComponent, bounds.getX(), bounds.getY());
    }

    private void setupAnimations(AnimationComponent animationComponent) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("entities/Blocks.atlas"));
        animationComponent.addAnimation(AnimationName.PILLAR, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(AnimationName.PILLAR);
    }

    private void setupPosition(PositionComponent positionComponent, float posX, float posY) {
        positionComponent.setX(posX);
        positionComponent.setY(posY);
    }
}
