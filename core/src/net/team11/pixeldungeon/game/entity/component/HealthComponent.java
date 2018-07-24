package net.team11.pixeldungeon.game.entity.component;

import net.team11.pixeldungeon.game.entities.traps.floorhole.FloorHole;
import net.team11.pixeldungeon.game.entities.traps.floorspike.FloorSpike;
import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.game.entitysystem.EntityComponent;
import net.team11.pixeldungeon.utils.assets.AssetName;

public class HealthComponent implements EntityComponent {
    private int health, maxHealth;
    private Entity latestDamageEntity;

    public HealthComponent(int health, int maxHealth) {
        this.health = health;
        this.maxHealth = maxHealth;
        latestDamageEntity = null;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(int health, Entity damageEntity) {
        this.health = health;
        this.latestDamageEntity = damageEntity;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public String getKillingAnimation() {
        if (latestDamageEntity instanceof FloorSpike) {
            return AssetName.DEATH_FLOOR_SPIKE;
        } else if (latestDamageEntity instanceof FloorHole) {
            return AssetName.DEATH_DEFAULT;
        }

        return AssetName.DEATH_FIRE;
    }
}
