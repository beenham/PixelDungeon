package net.team11.pixeldungeon.game.entities.beams;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.game.entity.component.AnimationComponent;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entity.system.BeamSystem;

import net.team11.pixeldungeon.utils.CollisionUtil;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;

public class Reflector extends BeamGenerator {
    private Beam beamIn = null;
    
    public Reflector(Rectangle bounds, String name, String direction, Beam beam){
        super(bounds,name,direction,beam);
        super.beamOut.setOn(false);
        setupAnimations(getComponent(AnimationComponent.class));
    }

    public Polygon getInnerBounds() {
        BodyComponent body = getComponent(BodyComponent.class);
        return CollisionUtil.createRectangle(body.getX(),body.getY()- BeamSystem.yOffset,
                BeamGenerator.BOX_SIZE,BeamGenerator.BOX_SIZE);
    }

    public void setBeamIn(Beam beamIn){
        if (this.beamIn == null) {
            this.beamIn = beamIn;
            if (beamIn != null) {
                setupBeam(getComponent(BodyComponent.class).getX(),getComponent(BodyComponent.class).getY()-BeamSystem.yOffset);
                getComponent(AnimationComponent.class).setAnimation(AssetName.BEAM_REFLECTOR_ON);
                beamOut.setOn(true);
            } else {
                getComponent(AnimationComponent.class).setAnimation(AssetName.BEAM_REFLECTOR_OFF);
                beamOut.setOn(false);
            }
        } else if (beamIn == null) {
            this.beamIn = null;
            getComponent(AnimationComponent.class).setAnimation(AssetName.BEAM_REFLECTOR_OFF);
            beamOut.setOn(false);
        }
    }

    public Beam getBeamIn() {
        return beamIn;
    }

    public boolean hasBeamIn() {
        return beamIn != null;
    }

    @Override
    public void doInteraction(boolean isPlayer) {}

    private void setupAnimations(AnimationComponent animationComponent){
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        animationComponent.addAnimation(AssetName.BEAM_REFLECTOR_OFF, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.BEAM_REFLECTOR_ON, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(AssetName.BEAM_REFLECTOR_OFF);
    }
}
