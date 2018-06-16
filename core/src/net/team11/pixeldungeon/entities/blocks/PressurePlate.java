package net.team11.pixeldungeon.entities.blocks;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entities.traps.Trap;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.TrapComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.utils.Assets;
import net.team11.pixeldungeon.utils.CollisionUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Class to handle pressure plates in game
 * When making in Tiled set the autoClose boolean to true for entity to re-activate after a
 * specified amount of time, activeTime.
 */
public class PressurePlate extends Trap {

    private long activeTime;
    private boolean active;
    private Timer timer = new Timer();
    private boolean autoClose;

    public PressurePlate(Rectangle bounds, String name, long activeTime, boolean autoClose) {
        super(name, true);
        this.timed = true;
        this.activeTime = activeTime;
        this.autoClose = autoClose;

        float posX = bounds.getX() + bounds.getWidth() / 2;
        float posY = bounds.getY() + bounds.getHeight() / 2;

        this.addComponent(new TrapComponent(this));
        this.active = false;
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 0f,
                (CollisionUtil.TRAP),
                (byte)(CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));
        AnimationComponent animationComponent;
        this.addComponent(animationComponent = new AnimationComponent(0));
        setupAnimations(animationComponent);
    }

    private void setupAnimations(AnimationComponent animationComponent) {
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        animationComponent.addAnimation(AssetName.PRESSUREPLATE_DOWN,textureAtlas,1, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PRESSUREPLATE_UP,textureAtlas,1, Animation.PlayMode.LOOP);
        if (active) {
            animationComponent.setAnimation(AssetName.PRESSUREPLATE_DOWN);
        } else {
            animationComponent.setAnimation(AssetName.PRESSUREPLATE_UP);
        }
    }

    public boolean isActive(){
        return this.active;
    }

    public boolean doesAutoClose(){
        return this.autoClose;
    }

    public void stepOn(){
        active = true;
        getComponent(AnimationComponent.class).setAnimation(AssetName.PRESSUREPLATE_DOWN);
        for (Entity entity : targetEntities){
            System.out.println("\t\t" + entity.getName());
            entity.doInteraction(false);
        }
    }

    public void stepOff(){
        if (autoClose){
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    for (Entity entity : targetEntities) {
                        entity.doInteraction(false);
                    }
                    active = false;
                    timer.cancel();
                    timer.purge();
                    timer = new Timer();
                    getComponent(AnimationComponent.class).setAnimation(AssetName.PRESSUREPLATE_UP);

                }
            }, activeTime * 1000);
        } else {
            getComponent(AnimationComponent.class).setAnimation(AssetName.PRESSUREPLATE_UP);
            active = false;
        }
    }
}