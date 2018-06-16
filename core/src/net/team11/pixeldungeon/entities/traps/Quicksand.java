package net.team11.pixeldungeon.entities.traps;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;

import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.HealthComponent;
import net.team11.pixeldungeon.entity.component.TrapComponent;

import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.utils.Assets;
import net.team11.pixeldungeon.utils.CollisionUtil;

import java.util.Timer;
import java.util.TimerTask;

public class Quicksand extends Trap {

    private float speedMod; //The speed which to set the players speed to. Make it less then 100 to slow down
    private float timeBeforeDeath;  //The time that the player can spend in the quicksand before they die

    private Timer timer = new Timer();

    private boolean active = false;

    public Quicksand(ChainShape bounds, String name, boolean enabled, float speedMod, float timeBeforeDeath){
        super(name, enabled);

        this.speedMod = speedMod;
        this.timeBeforeDeath = timeBeforeDeath;

        this.damage = 100;
        this.timed = true;
        this.addComponent(new TrapComponent(this));
        this.addComponent(new BodyComponent(bounds,0f,
                (CollisionUtil.TRAP),
                (byte)(CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));

//        AnimationComponent animationComponent;
//        this.addComponent(animationComponent = new AnimationComponent(0));
//        setupAnimations(animationComponent);
    }

    //Method called when the player enters the quicksand bounds
    public void enter(){
        contactEntity.getComponent(VelocityComponent.class).setMovementSpeed(speedMod);
        this.active = true;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (active){
                    //Kill the player
                    HealthComponent health = contactEntity.getComponent(HealthComponent.class);
                    health.setHealth(health.getHealth() - damage);

                    timer.cancel();
                    timer.purge();
                } else {
                    timer.cancel();
                    timer.purge();
                }
                timer = new Timer();
            }
        }, (long)timeBeforeDeath*1000);
    }

    //Method when the player leaves the quicksand bounds
    public void leave(){
        this.active = false;
        timer.cancel();
        timer = new Timer();
    }

    public boolean isActive(){
        return active;
    }

    private void setupAnimations(AnimationComponent animationComponent) {
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        animationComponent.addAnimation(AssetName.TMP_OFF, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(AssetName.TMP_OFF);
    }


}
