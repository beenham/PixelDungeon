package net.team11.pixeldungeon.game.entity.system;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.game.entities.player.Player;
import net.team11.pixeldungeon.game.entity.component.HealthComponent;
import net.team11.pixeldungeon.game.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.game.entitysystem.EntityEngine;
import net.team11.pixeldungeon.game.entitysystem.EntitySystem;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.Util;
import net.team11.pixeldungeon.utils.stats.StatsUtil;

public class HealthSystem extends EntitySystem {
    private boolean deathDisplayed;
    private HealthComponent playerHealthComponent;

    @Override
    public void init(EntityEngine engine) {
        deathDisplayed = false;
        Player player = (Player) engine.getEntities(PlayerComponent.class, HealthComponent.class).get(0);
        this.playerHealthComponent = player.getComponent(HealthComponent.class);

    }

    @Override
    public void update(float delta) {
        if (playerHealthComponent.getHealth() <= 0 && !deathDisplayed) {
            PixelDungeon.getInstance().getAndroidInterface().earnLetsTryAgain();
            StatsUtil statsUtil = Util.getInstance().getStatsUtil();
            statsUtil.getGlobalStats().incrementDeaths();
            statsUtil.saveTimer();
            PlayScreen.uiManager.showDeathMenu(playerHealthComponent.getKillingAnimation());
            deathDisplayed = true;
        }
    }
}
