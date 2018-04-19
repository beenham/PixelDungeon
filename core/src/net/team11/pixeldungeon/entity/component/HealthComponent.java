package net.team11.pixeldungeon.entity.component;

import net.team11.pixeldungeon.entitysystem.EntityComponent;

public class HealthComponent implements EntityComponent {

    private int health, maxHealth;

    public HealthComponent(int health, int maxHealth) {
        this.health = health;
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }
}
