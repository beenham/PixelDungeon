package net.team11.pixeldungeon.entitysystem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Entity {
    protected String name;
    private UUID uuid;
    private List<EntityComponent> entityComponents = new ArrayList<EntityComponent>();

    public Entity() {
        this.uuid = UUID.randomUUID();
    }

    public Entity(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID();
    }

    protected void addComponent(EntityComponent component) {
        this.entityComponents.add(component);
    }

    public boolean hasComponent(Class<? extends EntityComponent> component) {
        for (EntityComponent entityComponent : entityComponents) {
            if (component.isInstance(entityComponent)) {
                return true;
            }
        }
        return false;
    }

    public <T extends EntityComponent> T getComponent(Class<T> component) {
        for (EntityComponent entityComponent : entityComponents) {
            if (component.isInstance(entityComponent)) {
                return component.cast(entityComponent);
            }
        }
        return null;
    }

    public void doInteraction() {

    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return name + " : " + uuid;
    }
}
