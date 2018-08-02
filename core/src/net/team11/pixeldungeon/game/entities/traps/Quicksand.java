package net.team11.pixeldungeon.game.entities.traps;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;

import net.team11.pixeldungeon.game.entities.player.Player;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entity.component.HealthComponent;
import net.team11.pixeldungeon.game.entity.component.TrapComponent;
import net.team11.pixeldungeon.game.entity.component.VelocityComponent;
import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.utils.CollisionUtil;

public class Quicksand extends Trap {
    private float speedMod; //The speed which to set the players speed to. Make it less then 100 to slow down

    public Quicksand(ChainShape bounds, String name, float speedMod, float timeBeforeDeath) {
        super(name, false);
        this.speedMod = speedMod;
        requireSubmerged = true;
        timed = true;
        timerReset = timeBeforeDeath;
        timer = timeBeforeDeath;

        addComponent(new TrapComponent(this));
        addComponent(new BodyComponent(bounds, 0f,
                (CollisionUtil.TRAP),
                (byte) (CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));
    }

    @Override
    public void setTimer(float timer) {
        super.setTimer(timer);
        if (contactEntity instanceof Player) {
            Player player = (Player) contactEntity;
            if (timer / timerReset <= .8f) {
                contactEntity.getComponent(VelocityComponent.class).setMovementSpeed(speedMod * 3/4);
                player.setDepth(Player.PlayerDepth.THREE_QUART);
            }
            if (timer / timerReset <= .5f) {
                contactEntity.getComponent(VelocityComponent.class).setMovementSpeed(speedMod * 1/2);
                player.setDepth(Player.PlayerDepth.TWO_QUART);
            }
            if (timer / timerReset <= .25f) {
                contactEntity.getComponent(VelocityComponent.class).setMovementSpeed(speedMod * 1/4);
                player.setDepth(Player.PlayerDepth.ONE_QUART);
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            triggered = false;
            contactEntity.getComponent(VelocityComponent.class).setMovementSpeed(speedMod);
        } else {
            resetTimer();
            if (contactEntity != null) {
                ((Player)contactEntity).setDepth(Player.PlayerDepth.FOUR_QUART);
                contactEntity.getComponent(VelocityComponent.class).setMovementSpeed(100);
                contactEntity = null;
            }
        }
    }

    @Override
    public void update(float delta, Player player, float timer) {
        Polygon hitBox = getComponent(BodyComponent.class).getPolygon();
        Polygon entityBox = player.getComponent(BodyComponent.class).getPolygon();

        boolean submerged = CollisionUtil.getAmountSubmerged(hitBox, entityBox) >= .6f;

        if (!enabled) {
            if (submerged) {
                setContactingEntity(player);
                setEnabled(true);
            }
        } else {
            setTimer(super.timer - delta);
            if (!submerged) {
                setContactingEntity(null);
            }
            if (super.timer <= 0f) {
                trigger();
            }
        }
    }

    //Method called when the player enters the quicksand bounds
    @Override
    public void trigger(){
        triggered = true;
        ((Player)contactEntity).setDepth(Player.PlayerDepth.FOUR_QUART);
        HealthComponent health = contactEntity.getComponent(HealthComponent.class);
        health.setHealth(health.getHealth() - damage,this);
    }

    @Override
    public void setContactingEntity(Entity entity) {
        if (entity == null) {
            setEnabled(false);
        } else {
            super.setContactingEntity(entity);
        }
    }
}
