package net.team11.pixeldungeon.utils.inventory;

import net.team11.pixeldungeon.game.entity.component.InventoryComponent;
import net.team11.pixeldungeon.game.items.Coin;
import net.team11.pixeldungeon.game.map.MapManager;
import net.team11.pixeldungeon.utils.Util;
import net.team11.pixeldungeon.utils.stats.CurrentStats;
import net.team11.pixeldungeon.utils.stats.LevelStats;
import net.team11.pixeldungeon.utils.stats.StatsUtil;

public class CoinAwarder {
    private static CoinAwarder INSTANCE;

    private final int firstTimeCoins = 500;
    private final int completeCoins = 50;

    private int timeCoins;
    private int foundCoins;
    private int noDeathValue;
    private boolean noDeaths;
    private boolean firstTime;

    private int totalCoinCount;

    public void init(InventoryComponent inventory) {
        timeCoins = completeCoins;
        foundCoins = 0;
        noDeaths = false;
        firstTime = false;

        StatsUtil statsUtil = Util.getStatsUtil();
        LevelStats levelStats = statsUtil.getLevelStats(MapManager.getInstance()
                .getCurrentMap().getMapName());
        CurrentStats currStats = statsUtil.getCurrentStats();

        setNoDeaths(currStats);
        setTimeCoins(statsUtil,levelStats);
        setFoundCoins(inventory);
        setFirstTime(levelStats);

        setTotalCoins();
    }

    public static CoinAwarder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CoinAwarder();
        }
        return INSTANCE;
    }

    private void setFoundCoins(InventoryComponent inventory) {
        Coin coin = (Coin)inventory.getItem("coin", Coin.class);
        if (coin != null) {
            foundCoins = coin.getAmount();
        }
    }

    private void setTimeCoins(StatsUtil statsUtil, LevelStats levelStats) {
        int currTime = statsUtil.getTimer();

        if (currTime <= levelStats.getTargetTime(LevelStats.TARGET_TIME_1)) {
            timeCoins *= Math.pow(2,3);
        } else if (currTime <= levelStats.getTargetTime(LevelStats.TARGET_TIME_2)) {
            timeCoins *= Math.pow(2,2);
        } else if (currTime <= levelStats.getTargetTime(LevelStats.TARGET_TIME_3)) {
            timeCoins *= Math.pow(2,1);
        }

        if (noDeaths) {
            noDeathValue = timeCoins;
        }
    }

    private void setNoDeaths(CurrentStats currentStats) {
        if (currentStats.getDeaths() <= 0) {
            noDeaths = true;
        }
    }

    private void setFirstTime(LevelStats levelStats) {
        if (levelStats.getCompleted() == 1) {
            firstTime = true;
        }
    }

    private void setTotalCoins() {
        totalCoinCount = 0;
        totalCoinCount += foundCoins;
        totalCoinCount += timeCoins;
        if (noDeaths) {
            totalCoinCount += noDeathValue;
        }
        if (firstTime) {
            totalCoinCount += firstTimeCoins;
        }
    }

    public int getTotalCoinCount() {
        return totalCoinCount;
    }

    public int getFirstTimeCoins() {
        return firstTimeCoins;
    }

    public int getTimeCoins() {
        return timeCoins;
    }

    public int getNoDeathValue() {
        return noDeathValue;
    }

    public int getFoundCoins() {
        return foundCoins;
    }

    public boolean isFirstTime() {
        return firstTime;
    }
}
