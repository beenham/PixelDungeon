package net.team11.pixeldungeon.entity.system;

import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;

import java.util.ArrayList;
import java.util.List;

public class BodySystem extends EntitySystem {
    private List<Entity> entities;

    @Override
    public void init(EntityEngine entityEngine) {
        entities = new ArrayList<>();
        entities = entityEngine.getEntities(BodyComponent.class);
    }

    @Override
    public void update(float delta) {

    }
}
