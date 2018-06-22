package net.team11.pixeldungeon.entities.puzzle.simonsays;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.InteractionComponent;
import net.team11.pixeldungeon.puzzles.Puzzle;
import net.team11.pixeldungeon.puzzles.simonsays.SimonSays;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.CollisionUtil;

public class SimonSaysSwitch extends PuzzleComponent {
    private SimonSays parentPuzzle;
    private boolean on;
    private float timer = 1;

    public SimonSaysSwitch(Rectangle bounds, String name) {
        super(name);
        on = false;
        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;

        addComponent(new InteractionComponent(this));
        addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(),posX,posY, 0,
                (CollisionUtil.ENTITY),
                (byte)(CollisionUtil.ENTITY | CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));
        AnimationComponent animationComponent;
        addComponent(animationComponent = new AnimationComponent(0));
        setupAnimations(animationComponent);
    }

    private void setupAnimations(AnimationComponent animationComponent) {
        TextureAtlas atlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        animationComponent.addAnimation(AssetName.SS_SWITCH_ON, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.SS_SWITCH_OFF, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.SS_SWITCH_IDLE, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.SS_SWITCH_WAITING, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.SS_SWITCH_FAILED, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(AssetName.SS_SWITCH_OFF);
    }

    @Override
    public void doInteraction(boolean isPlayer) {
        if (isPlayer) {
            if (parentPuzzle.isActivated()) {
                parentPuzzle.notifyPressed(this);
            }
        } else {
            if (on) {
                timer = 1;
                on = false;
                getComponent(AnimationComponent.class).setAnimation(AssetName.SS_SWITCH_IDLE);
            } else {
                on = true;
                getComponent(AnimationComponent.class).setAnimation(AssetName.SS_SWITCH_ON);
            }
        }
    }

    public boolean isOn() {
        return on;
    }

    public float getTimer() {
        return timer;
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }

    @Override
    public void setParentPuzzle(Puzzle parentPuzzle) {
        super.setParentPuzzle(parentPuzzle);
        this.parentPuzzle = (SimonSays)parentPuzzle;
    }
}
