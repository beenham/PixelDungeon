package net.team11.pixeldungeon.game.entitysystem;

import net.team11.pixeldungeon.game.entity.component.playercomponent.PlayerComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityManager {
    private Entity player;
    private List<Entity> entities = new ArrayList<>();
    private HashMap<String, List<Entity>> loadedEntities = new HashMap<>();

    public List<Entity> getEntities(Class<? extends EntityComponent> entityComponent) {
        List<Entity> entityList = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.hasComponent(entityComponent)) {
                entityList.add(entity);
            }
        }
        return entityList;
    }

    @SafeVarargs
    public final List<Entity> getEntities(Class<? extends EntityComponent>... entityComponent) {
        List<Entity> entityList = new ArrayList<>();

        for (Entity entity : entities) {
            boolean hasAllComponent = true;
            for (Class<? extends EntityComponent> anEntityComponent : entityComponent) {
                if (!entity.hasComponent(anEntityComponent)) {
                    hasAllComponent = false;
                }
            }
            if (hasAllComponent) {
                entityList.add(entity);
            }
        }

        return entityList;
    }

    public void addEntity(Entity entity) {
        if (entity.hasComponent(PlayerComponent.class)) {
            player = entity;
        }
        if (!entities.contains(entity)) {
            this.entities.add(entity);
        }
    }

    public void storeEntities(String name) {
        System.out.println("Storing @ " + name);
        for (Entity entity :entities) {
            System.out.println(entity.toString());
        }
        loadedEntities.put(name, entities);
        entities = new ArrayList<>();
        entities.add(player);
    }

    public void loadEntities(String name) {
        entities = new ArrayList<>();
        System.out.println("Loading @ " + name);
        entities = loadedEntities.get(name);
        for (Entity entity :entities) {
            System.out.println(entity.toString());
        }
    }

    public boolean isEmpty() {
        return entities.isEmpty();
    }
}
