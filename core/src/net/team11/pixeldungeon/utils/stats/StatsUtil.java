package net.team11.pixeldungeon.utils.stats;

import net.team11.pixeldungeon.game.entities.blocks.Chest;
import net.team11.pixeldungeon.game.items.Item;
import net.team11.pixeldungeon.game.items.keys.Key;
import net.team11.pixeldungeon.utils.Util;

import java.util.HashMap;
import java.util.Locale;

public class StatsUtil {
    private HashMap<String, LevelStats> levelStats;
    private GlobalStats globalStats;
    private CurrentStats currentStats;
    private int timer = 0;
    private int flaggedTimer = 0;

    public StatsUtil(HashMap<String, LevelStats> levelStats, GlobalStats globalStats) {
        this.levelStats = levelStats;
        this.globalStats = globalStats;
    }

    public HashMap<String, LevelStats> getLevelStats() {
        return this.levelStats;
    }

    public void initialiseCurrStats() {
        if (currentStats == null) {
            currentStats = new CurrentStats();
        }
    }

    public void clearCurrStats () {
        currentStats = null;
    }

    public CurrentStats getCurrentStats() {
        return currentStats;
    }

    public void startTimer() {
        timer = 0;
        flaggedTimer = 0;
    }

    public void incrementTimer() {
        timer++;
    }

    public void saveTimer() {
        globalStats.addTime(timer-flaggedTimer);
        flaggedTimer = timer;
    }

    public int getTimer() {
        return timer;
    }

    public String getTimerString() {
        return String.format(Locale.UK,"%02d:%02d",timer/60,timer%60);
    }

<<<<<<< HEAD
=======
    public static void clearLocal() {
        Gdx.files.local("saves").deleteDirectory();
    }

>>>>>>> release-branch
    public LevelStats getLevelStats(String name) {
        return levelStats.get(name);
    }

    public GlobalStats getGlobalStats() {
        return globalStats;
    }

    public void updateChests(Chest chest) {
        currentStats.incrementChests();
        currentStats.addChest(chest.getName());
        globalStats.incrementChestsFound();
        Util.getInstance().saveGame();
    }

    //
    public void updateKeys(Key key) {
        System.out.println("Updating key stats");
        currentStats.incrementKeys();
        currentStats.addKey(key.getName());
        globalStats.incrementKeysFound();
        Util.getInstance().saveGame();
    }

    //
    public void updateItems(Item item) {
        currentStats.incrementItems();
        currentStats.addItem(item.getName());
        globalStats.incrementItemsFound();
        Util.getInstance().saveGame();
    }

    //
    public void updateAttempts(String mapName) {
        if (!levelStats.get(mapName).isTutorial()) {
            getLevelStats(mapName).incrementAttempts();
            globalStats.incrementAttempts();
            AchivementStats.incrementAttempts();
            Util.getInstance().saveGame();
        }
    }

    public void updatePuzzleAttempts() {
        globalStats.incrementPuzzleAttempted();
        Util.getInstance().saveGame();
    }

    public void updatePuzzleCompleted() {
        this.getGlobalStats().incrementPuzzleCompleted();
        Util.getInstance().saveGame();
    }

    public void respawn(String mapName) {
        currentStats.respawn();
        globalStats.incrementDeaths();
        updateAttempts(mapName);
        Util.getInstance().saveGame();
    }
}
