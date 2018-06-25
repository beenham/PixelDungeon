package net.team11.pixeldungeon.entities.beams;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.utils.CollisionUtil;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;

public class BeamTarget extends PuzzleComponent {

    private Beam beamIn = null;

    private float yOffset = -8f;

    public BeamTarget(Rectangle bounds, String name) {
        super(name);
        float posX = bounds.getX() + bounds.getWidth() / 2;
        float posY = bounds.getY() + bounds.getHeight() / 2;

        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 0,
                (CollisionUtil.ENTITY),
                (byte) (CollisionUtil.ENTITY | CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));

        AnimationComponent animationComponent;
        this.addComponent(animationComponent = new AnimationComponent(0));
        setupAnimationComponent(animationComponent);
    }

    public Polygon getInnerBounds() {
        BodyComponent body = getComponent(BodyComponent.class);
        return CollisionUtil.createRectangle(body.getX(),body.getY()- yOffset,
                BeamGenerator.BOX_SIZE,BeamGenerator.BOX_SIZE);
    }

    public boolean hasBeamIn(){
        return this.beamIn != null;
    }

    public void setBeamIn(Beam beamIn){
        if (this.beamIn == null) {
            this.beamIn = beamIn;
            if (beamIn != null) {
                getComponent(AnimationComponent.class).setAnimation(AssetName.BEAM_TARGET_ON);
            } else {
                getComponent(AnimationComponent.class).setAnimation(AssetName.BEAM_TARGET_OFF);
            }
        } else if (beamIn == null) {
            this.beamIn = null;
            getComponent(AnimationComponent.class).setAnimation(AssetName.BEAM_TARGET_OFF);

        }
    }

    public Beam getBeamIn() {
        return beamIn;
    }

    private void setupAnimationComponent(AnimationComponent animationComponent) {
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        animationComponent.addAnimation(AssetName.BEAM_TARGET_OFF, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.BEAM_TARGET_ON, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(AssetName.BEAM_TARGET_OFF);
    }
}


