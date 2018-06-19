package net.team11.pixeldungeon.entities.puzzle;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.InteractionComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.puzzles.Puzzle;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.CollisionUtil;

import java.util.List;

public class PuzzleController extends PuzzleComponent {
    public PuzzleController(Rectangle bounds, String name) {
        super(name);
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
        animationComponent.addAnimation(AssetName.PUZZLECONTROLLER_ACTIVATED, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PUZZLECONTROLLER_COMPLETED, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PUZZLECONTROLLER_DEACTIVATED, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PUZZLECONTROLLER_WAITING, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(AssetName.PUZZLECONTROLLER_DEACTIVATED);
    }

    @Override
    public void setParentPuzzle(Puzzle parentPuzzle) {
        this.parentPuzzle = parentPuzzle;
        parentPuzzle.setController(this);
    }

    @Override
    public void doInteraction(boolean isPlayer) {
        if (isPlayer) {
            if (!parentPuzzle.isActivated() && !parentPuzzle.isCompleted() && parentPuzzle.getRemainingAttempts() > 0) {
                parentPuzzle.activate();
            } else {
                parentPuzzle.notifyPressed(this);
            }
        }
    }
}
