package net.team11.pixeldungeon.game.entities.puzzle;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.game.entity.component.AnimationComponent;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entity.component.InteractionComponent;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.CollisionUtil;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;

public class CompletedIndicator extends PuzzleComponent {
    private boolean on;

    public CompletedIndicator(Rectangle bounds, String name) {
        super(name);
        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;
        on = false;

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
        animationComponent.addAnimation(AssetName.COMPLETED_INDICATOR_ON, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.COMPLETED_INDICATOR_OFF, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(AssetName.COMPLETED_INDICATOR_OFF);
    }

    @Override
    public void doInteraction(boolean isPlayer) {
        if (isPlayer) {
            if (on) {
                String message = Messages.INDICATOR_IS_ON;
                PlayScreen.uiManager.initTextBox(message);
            } else {
                String message = Messages.INDICATOR_CANT_DO;
                PlayScreen.uiManager.initTextBox(message);
            }
        } else {
            if (!on) {
                parentPuzzle.notifyPressed(this);
                getComponent(AnimationComponent.class).setAnimation(AssetName.COMPLETED_INDICATOR_ON);
            }
        }
    }

    public boolean isOn() {
        return on;
    }
}
