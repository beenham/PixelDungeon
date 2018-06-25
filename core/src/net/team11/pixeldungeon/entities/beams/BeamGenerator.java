package net.team11.pixeldungeon.entities.beams;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.system.BeamSystem;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.utils.CollisionUtil;
import net.team11.pixeldungeon.utils.Direction;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;

public class BeamGenerator extends Entity {
    protected Direction beamDirection;
    protected Beam beamOut;
    public static float BOX_SIZE = 6f;

    public BeamGenerator(Rectangle bounds, String name, String direction, Beam beamOut){
        super(name);
        this.beamOut = beamOut;
        this.beamDirection = MirrorUtil.parseDirection(direction);

        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;

        setupBeam(posX,posY- BeamSystem.yOffset);

        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 0,
                (CollisionUtil.ENTITY),
                (byte)(CollisionUtil.ENTITY |  CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));

        AnimationComponent animationComponent;
        this.addComponent(animationComponent = new AnimationComponent(0));
        setupAnimations(animationComponent);
    }

    protected void setupBeam(float x, float y) {
        float offset = 0.1f;
        switch (beamDirection) {
            case UP:
                y += (BOX_SIZE/2 + Beam.DEPTH/2 + offset);
                break;
            case DOWN:
                y -= (BOX_SIZE/2 + Beam.DEPTH/2 + offset);
                break;
            case LEFT:
                x -= (BOX_SIZE/2 + Beam.DEPTH/2 + offset);
                break;
            case RIGHT:
                x += (BOX_SIZE/2 + Beam.DEPTH/2 + offset);
                break;
        }
        beamOut.getComponent(BodyComponent.class).setCoords(x,y);
    }

    @Override
    public void doInteraction(boolean isPlayer) {
        if (!isPlayer) {
            if (beamOut.isOn()) {
                beamOut.setOn(false);
                getComponent(AnimationComponent.class).setAnimation(AssetName.BEAM_GENERATOR_OFF);
            } else {
                setupBeam(getComponent(BodyComponent.class).getX(),getComponent(BodyComponent.class).getY()-BeamSystem.yOffset);
                getComponent(AnimationComponent.class).setAnimation(AssetName.BEAM_GENERATOR_ON);
                beamOut.setOn(true);
            }
        }
    }

    private void setupAnimations(AnimationComponent animationComponent){
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        animationComponent.addAnimation(AssetName.BEAM_GENERATOR_ON, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.BEAM_GENERATOR_OFF, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(AssetName.BEAM_GENERATOR_ON);
    }
}
