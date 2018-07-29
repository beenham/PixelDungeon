package net.team11.pixeldungeon.game.entities.traps.floorspike;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.game.entity.component.AnimationComponent;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;

public class FloorSpike1x1 extends FloorSpike{
    public FloorSpike1x1(Rectangle bounds, boolean enabled, String name, float timer) {
        this(bounds,enabled,name);
        super.timed = true;
        super.timerReset = timer;
        super.timer = timerReset;
    }

    public FloorSpike1x1(Rectangle bounds, boolean enabled, String name) {
        super(bounds,enabled,name);

        AnimationComponent animationComponent;
        this.addComponent(animationComponent = new AnimationComponent(0));
        setupAnimations(animationComponent);
    }

    private void setupAnimations(AnimationComponent animationComponent) {
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.TRAPS);
        animationComponent.addAnimation(AssetName.FLOORSPIKE_IDLE, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.FLOORSPIKE_ACTIVATING, textureAtlas, 0.3f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.FLOORSPIKE_DEACTIVATING, textureAtlas, 1f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.FLOORSPIKE_TRIGGERED, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(AssetName.FLOORSPIKE_IDLE);
    }

    @Override
    protected void setActivatedAnim() {
        getComponent(AnimationComponent.class).setAnimation(AssetName.FLOORSPIKE_ACTIVATING);
        getComponent(AnimationComponent.class).setNextAnimation(AssetName.FLOORSPIKE_TRIGGERED);
    }

    @Override
    protected void setDeactivatedAnim() {
        getComponent(AnimationComponent.class).setAnimation(AssetName.FLOORSPIKE_DEACTIVATING);
        getComponent(AnimationComponent.class).setNextAnimation(AssetName.FLOORSPIKE_IDLE);
    }
}
