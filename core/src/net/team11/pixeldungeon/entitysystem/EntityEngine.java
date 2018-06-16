package net.team11.pixeldungeon.entitysystem;

import net.team11.pixeldungeon.entity.system.RenderSystem;

import java.util.LinkedList;
import java.util.List;

public class EntityEngine {
    private EntityManager entityManager;
    private LinkedList<EntitySystem> systems;

    private boolean finished;
    private boolean paused;

    public EntityEngine() {
        this.entityManager = new EntityManager();
        systems = new LinkedList<>();
    }

    public void update(float delta) {
        if (finished) {
            return;
        }
        if (paused) {
            for (EntitySystem entitySystem : systems) {
                if (entitySystem.getClass().equals(RenderSystem.class)) {
                    ((RenderSystem)entitySystem).updatePaused();
                    return;
                }
            }
        }

        for (EntitySystem entitySystem : systems) {
            entitySystem.update(delta);
        }
    }

    public void addEntity(Entity entity) {
        entityManager.addEntity(entity);
        updateSystems();
    }

    public List<Entity> getEntities(Class<? extends EntityComponent> componentType) {
        return this.entityManager.getEntities(componentType);
    }

    @SafeVarargs
    public final List<Entity> getEntities(Class<? extends EntityComponent>... componentType) {
        return this.entityManager.getEntities(componentType);
    }

    public void storeEntities(String name) {
        entityManager.storeEntities(name);
    }

    public void loadEntities (String name) {
        entityManager.loadEntities(name);
    }

    public boolean hasEntities() {
        return !entityManager.isEmpty();
    }

    public void hasSystem(Class<?> systemType) {
        for (EntitySystem entitySystem : systems) {
            systemType.isInstance(entitySystem);
        }
    }

    public void addSystem(EntitySystem system) {
        this.systems.add(system);
    }

    public <T extends EntitySystem> T getSystem(Class<T> system) {
        for (EntitySystem eComponent : this.systems) {
            if (system.isInstance(eComponent)) {
                return system.cast(eComponent);
            }
        }

        return null;
    }

    private void updateSystems() {
        for (EntitySystem system : this.systems) {
            system.init(this);
        }
    }

    public void pause() {
        this.paused = true;
    }

    public void resume() {
        System.out.println("RESUME: resumed called in engine.");
        this.paused = false;
    }

    public void finish() {
        this.finished = true;
    }
}
