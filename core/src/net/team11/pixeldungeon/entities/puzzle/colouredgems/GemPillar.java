package net.team11.pixeldungeon.entities.puzzle.colouredgems;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.InteractionComponent;
import net.team11.pixeldungeon.items.puzzleitems.ColouredGem;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.CollisionUtil;

public class GemPillar extends PuzzleComponent {
    private int id;
    private ColouredGem gem;

    public GemPillar(Rectangle bounds, String name, int id) {
        super(name);
        this.id = id;
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
        animationComponent.addAnimation(AssetName.GEMPILLAR_EMPTY, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.GEMPILLAR_BLUE, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.GEMPILLAR_PURPLE, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.GEMPILLAR_RED, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.GEMPILLAR_YELLOW, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.GEMPILLAR_GREEN, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.GEMPILLAR_WHITE, atlas,1f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(AssetName.GEMPILLAR_EMPTY);
    }


    @Override
    public void doInteraction(boolean isPlayer) {
        if (isPlayer) {
            if (!parentPuzzle.isCompleted() && parentPuzzle.getRemainingAttempts() > 0) {
                if (this.gem != null) {
                    PlayScreen.uiManager.initItemSelector(gem, this);
                } else {
                    PlayScreen.uiManager.initItemSelector(ColouredGem.class, this);
                }
            }
        }
    }

    public void setGem(ColouredGem gem) {
        this.gem = gem;
        switch (gem.getColour()) {
            case BLUE:
                getComponent(AnimationComponent.class).setAnimation(AssetName.GEMPILLAR_BLUE);
                break;
            case PURPLE:
                getComponent(AnimationComponent.class).setAnimation(AssetName.GEMPILLAR_PURPLE);
                break;
            case RED:
                getComponent(AnimationComponent.class).setAnimation(AssetName.GEMPILLAR_RED);
                break;
            case WHITE:
                getComponent(AnimationComponent.class).setAnimation(AssetName.GEMPILLAR_WHITE);
                break;
            case YELLOW:
                getComponent(AnimationComponent.class).setAnimation(AssetName.GEMPILLAR_YELLOW);
                break;
            case GREEN:
                getComponent(AnimationComponent.class).setAnimation(AssetName.GEMPILLAR_GREEN);
                break;
            default:
                getComponent(AnimationComponent.class).setAnimation(AssetName.GEMPILLAR_EMPTY);
        }
        parentPuzzle.notifyPressed(this);
    }

    public ColouredGem takeGem() {
        ColouredGem tempGem = gem;
        gem = null;
        getComponent(AnimationComponent.class).setAnimation(AssetName.GEMPILLAR_EMPTY);
        return tempGem;
    }

    public boolean hasGem() {
        return gem != null;
    }

    public ColouredGem getGem() {
        return gem;
    }

    public int getId() {
        return id;
    }
}