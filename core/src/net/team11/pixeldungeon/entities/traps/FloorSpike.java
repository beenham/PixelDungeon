package net.team11.pixeldungeon.entities.traps;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.HealthComponent;
import net.team11.pixeldungeon.entity.component.InteractionComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.FloorSpikeComponent;
import net.team11.pixeldungeon.entity.component.TrapComponent;
import net.team11.pixeldungeon.utils.Assets;
import net.team11.pixeldungeon.utils.CollisionUtil;

import java.util.Timer;
import java.util.TimerTask;

public class FloorSpike extends Trap {
    public FloorSpike(Rectangle bounds, boolean enabled, String name, float timer) {
        this(bounds,enabled,name);
        this.timed = true;
        this.timerReset = timer;
        this.timer = timerReset;
    }
    public FloorSpike(Rectangle bounds, boolean enabled, String name) {
        super(name, enabled);
        this.timed = false;
        this.damage = 1;

        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;
        AnimationComponent animationComponent;
        this.addComponent(new TrapComponent(this));
        this.addComponent(new FloorSpikeComponent(this));
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 0f,
                (CollisionUtil.TRAP),
                (byte)(CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));
        this.addComponent(animationComponent = new AnimationComponent(0));
        this.addComponent(new InteractionComponent(this));
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
    public void trigger() {
        if (hasTrigger()) {
            for (final Entity entity : targetEntities) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (entity.hasComponent(TrapComponent.class)) {
                            ((Trap) entity).trigger();
                        }
                    }
                }, 125);
            }
        }
        if (enabled) {
            if (!triggered) {
                getComponent(AnimationComponent.class).setAnimation(AssetName.FLOORSPIKE_ACTIVATING);
                getComponent(AnimationComponent.class).setNextAnimation(AssetName.FLOORSPIKE_TRIGGERED);
                triggered = true;
                if (contactEntity != null) {
                    contactEntity.getComponent(HealthComponent.class)
                            .setHealth(contactEntity.getComponent(HealthComponent.class).getHealth() - damage);
                }
            } else {
                getComponent(AnimationComponent.class).setAnimation(AssetName.FLOORSPIKE_DEACTIVATING);
                getComponent(AnimationComponent.class).setNextAnimation(AssetName.FLOORSPIKE_IDLE);
                triggered = false;
            }
            if (timed) {
                resetTimer();
            }
        }
    }
}
