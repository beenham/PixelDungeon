package net.team11.pixeldungeon.game.entities.traps;

import net.team11.pixeldungeon.game.entitysystem.Entity;

import java.util.List;

public class Trap extends Entity {
    protected boolean enabled;
    protected boolean timed;
    protected boolean triggered;
    protected boolean requireSubmerged;

    protected int damage;
    protected float timerReset;
    protected float timer;

    protected Entity contactEntity;

    protected Trap(String name, Boolean enabled) {
        super(name);
        this.enabled = enabled;
        triggered = false;
        timed = false;
        requireSubmerged = false;
        damage = 5;
        timer = 0;
        timerReset = 0;
    }

    public void setContactingEntity(Entity entity) {
        this.contactEntity = entity;
    }

    public void trigger() {

    }

    public boolean requireSubmerged() {
        return requireSubmerged;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public boolean isTimed() {
        return timed;
    }

    public boolean isContacting() {
        return contactEntity != null;
    }

    public Entity getContactEntity() {
        return contactEntity;
    }

    public float getTimer() {
        return timer;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }

    public void resetTimer() {
        timer = timerReset;
    }

    public int getDamage() {
        return damage;
    }

    public void update (float delta, List<Entity> entityList) {}
}
