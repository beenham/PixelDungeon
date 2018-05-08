package net.team11.pixeldungeon.entity.system;

import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entity.component.HealthComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;

import java.util.ArrayList;
import java.util.List;

public class HealthSystem extends EntitySystem {
    private final float timerReset = 25;
    private float timer = timerReset;

    private List<Entity> entities = new ArrayList<>();
    private Player player;
    private HealthComponent playerHealthComponent;

    @Override
    public void init(EntityEngine engine) {
        entities = engine.getEntities(HealthComponent.class);
        this.player = (Player) engine.getEntities(PlayerComponent.class, HealthComponent.class).get(0);
        this.playerHealthComponent = player.getComponent(HealthComponent.class);

    }

    @Override
    public void update(float delta) {
        for (Entity entity : entities) {
            HealthComponent healthComponent = entity.getComponent(HealthComponent.class);

            if (healthComponent.getHealth() <= 0) {
                entity.respawn();
            }
        }
    }

    public int getPlayerHealth() {
        return playerHealthComponent.getHealth();
    }

    public int getPlayerMaxHealth() {
        return playerHealthComponent.getMaxHealth();
    }
}
