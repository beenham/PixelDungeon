package net.team11.pixeldungeon.game.entities.puzzle.colouredgems;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.game.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.game.entity.component.AnimationComponent;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entity.component.InteractionComponent;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.CollisionUtil;

import java.util.Random;

public class WallScribe extends PuzzleComponent {
    private String text = null;

    public WallScribe (Rectangle bounds, String name) {
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
        animationComponent.addAnimation(AssetName.WALL_SCRIBE_ONE, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.WALL_SCRIBE_TWO, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.WALL_SCRIBE_THREE, atlas,1f, Animation.PlayMode.LOOP);
        int rand = Math.abs(new Random().nextInt() % 3);
        switch (rand) {
            case 0:
                animationComponent.setAnimation(AssetName.WALL_SCRIBE_ONE);
                break;
            case 1:
                animationComponent.setAnimation(AssetName.WALL_SCRIBE_TWO);
                break;
            case 2:
                animationComponent.setAnimation(AssetName.WALL_SCRIBE_THREE);
                break;
            default:
                animationComponent.setAnimation(AssetName.WALL_SCRIBE_ONE);

        }
    }

    public void setText(String text) {
        this.text = text;
        System.out.println(text);
    }

    @Override
    public void doInteraction(boolean isPlayer) {
        if (isPlayer) {
            PlayScreen.uiManager.initTextBox(text);
        }
    }
}
