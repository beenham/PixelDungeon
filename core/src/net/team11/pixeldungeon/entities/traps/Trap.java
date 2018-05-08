package net.team11.pixeldungeon.entities.traps;

import net.team11.pixeldungeon.entitysystem.Entity;

public class Trap extends Entity {
    protected boolean enabled;
    protected boolean timed;
    protected boolean triggered;

    protected int damage;
    protected float timerReset;
    protected float timer;

    protected Entity contactEntity;

    public Trap(String name, Boolean enabled) {
        super(name);
        this.enabled = enabled;
    }

    public void setContactingEntity(Entity entity) {
        this.contactEntity = entity;
    }

    public void trigger() {

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

    public float getTimer() {
        return timer;
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
}
