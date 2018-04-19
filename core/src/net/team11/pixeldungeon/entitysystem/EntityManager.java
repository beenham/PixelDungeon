package net.team11.pixeldungeon.entitysystem;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    private List<Entity> entities = new ArrayList<>();

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
        this.entities.add(entity);
    }
}
