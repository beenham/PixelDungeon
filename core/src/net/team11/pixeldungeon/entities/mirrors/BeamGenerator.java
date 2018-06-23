package net.team11.pixeldungeon.entities.mirrors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.utils.CollisionUtil;
import net.team11.pixeldungeon.utils.Direction;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;

public class BeamGenerator extends Entity {
    private Direction beamDirection;
    public BeamGenerator(Rectangle bounds, String name, String direction){
        super(name);

        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;

        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 0,
                (CollisionUtil.ENTITY),
                (byte)(CollisionUtil.ENTITY |  CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));

        this.beamDirection = MirrorUtil.parseDirection(direction);

        AnimationComponent animationComponent;
        this.addComponent(animationComponent = new AnimationComponent(0));
        setupAnimations(animationComponent);
    }

    private void setupAnimations(AnimationComponent animationComponent){
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        animationComponent.addAnimation(AssetName.BEAM_GENERATOR_ON, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.BEAM_GENERATOR_OFF, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(AssetName.BEAM_GENERATOR_ON);
    }
}
