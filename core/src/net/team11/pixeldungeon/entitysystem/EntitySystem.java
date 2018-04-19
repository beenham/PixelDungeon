package net.team11.pixeldungeon.entitysystem;

public abstract class EntitySystem {
    public abstract void init(EntityEngine entityEngine);
    public abstract void update(float delta);
}
