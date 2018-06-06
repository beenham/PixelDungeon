package net.team11.pixeldungeon.entitysystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.map.Map;
import net.team11.pixeldungeon.map.MapManager;
import net.team11.pixeldungeon.screens.PlayScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Entity {
    protected String name;
    private UUID uuid;
    private List<EntityComponent> entityComponents = new ArrayList<EntityComponent>();
    private Map parentMap;

    protected List<Entity> targetEntities = new ArrayList<>();
    private List<String> targets = new ArrayList<>();
    private boolean trigger = false;

    public Entity() {
        this.uuid = UUID.randomUUID();
    }

    public Entity(String name, Map parentMap) {
        this.parentMap = parentMap;
        this.name = name;
        this.uuid = UUID.randomUUID();
    }

    public Entity(String name){
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

    //  Void ; to be overridden by child if necessary
    public void doInteraction() {}

    //  Void ; to be overridden by child if necessary
    public void respawn() {}

    public boolean hasTrigger() {
        return this.trigger;
    }

    public void setTrigger(boolean trigger) {
        this.trigger = trigger;
    }

    public void setTargets(List<String> entities) {
        while (!entities.isEmpty()) {
            if (!targets.contains(entities.get(0))) {
                targets.add(entities.remove(0));
            } else {
                entities.remove(0);
            }
        }
    }

    public List<String> getTargets() {
        return targets;
    }

    public void setTargetEntities(List<Entity> entities) {
        while (!entities.isEmpty()) {
            if (!targetEntities.contains(entities.get(0))) {
                targetEntities.add(entities.remove(0));
            } else {
                entities.remove(0);
            }
        }
    }

    public Map getParentMap(){
        return this.parentMap;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return name + " : " + uuid;
    }
}
