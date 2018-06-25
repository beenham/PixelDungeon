package net.team11.pixeldungeon.entity.system;

import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entity.component.HealthComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.map.MapManager;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.stats.LevelStats;
import net.team11.pixeldungeon.utils.stats.StatsUtil;

import java.util.ArrayList;
import java.util.List;

public class HealthSystem extends EntitySystem {
    private final float timerReset = 25;
    private float timer;
    private boolean deathDisplayed;

    private List<Entity> entities = new ArrayList<>();
    private HealthComponent playerHealthComponent;

    @Override
    public void init(EntityEngine engine) {
        timer = timerReset;
        deathDisplayed = false;
        entities = engine.getEntities(HealthComponent.class);
        Player player = (Player) engine.getEntities(PlayerComponent.class, HealthComponent.class).get(0);
        this.playerHealthComponent = player.getComponent(HealthComponent.class);

    }

    @Override
    public void update(float delta) {
        if (playerHealthComponent.getHealth() <= 0 && !deathDisplayed) {
            StatsUtil statsUtil = StatsUtil.getInstance();
            statsUtil.getGlobalStats().incrementDeaths();
            statsUtil.saveTimer();
            PlayScreen.uiManager.showDeathMenu(playerHealthComponent.getKillingAnimation());
            deathDisplayed = true;
        }
    }

    public int getPlayerHealth() {
        return playerHealthComponent.getHealth();
    }

    public int getPlayerMaxHealth() {
        return playerHealthComponent.getMaxHealth();
    }
}
