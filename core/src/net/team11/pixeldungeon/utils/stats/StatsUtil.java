package net.team11.pixeldungeon.utils.stats;

import com.badlogic.gdx.Gdx;

import net.team11.pixeldungeon.game.entities.blocks.Chest;
import net.team11.pixeldungeon.game.items.Item;
import net.team11.pixeldungeon.game.items.keys.Key;

import java.util.HashMap;
import java.util.Locale;
import net.team11.pixeldungeon.utils.Util;

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

    public void setLevelStats(HashMap<String, LevelStats> map){
        this.levelStats = map;
    }

    public void setGlobalStats(GlobalStats globalStats){
        this.globalStats = globalStats;
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

    private void clearLocal() {
        Gdx.files.local("stats").deleteDirectory();
    }

    public LevelStats getLevelStats(String name) {
        return levelStats.get(name);
    }

    public GlobalStats getGlobalStats() {
        return globalStats;
    }

    public void updateLevelStats(String mapName, LevelStats stats) {
        this.getLevelStats().put(mapName, stats);
    }

    public void updateChests(Chest chest) {
        this.getCurrentStats().incrementChests();
        this.getCurrentStats().addChest(chest.getName());
        this.getGlobalStats().incrementChestsFound();
        Util.getInstance().saveGame();
    }

    //
    public void updateKeys(Key key) {
        System.out.println("Updating key stats");
        this.getCurrentStats().incrementKeys();
        this.getGlobalStats().incrementKeysFound();
        this.getCurrentStats().addKey(key.getName());
        System.out.println(this.getCurrentStats());
        Util.getInstance().saveGame();
    }

    //
    public void updateItems(Item item) {
        this.getCurrentStats().incrementItems();
        this.getGlobalStats().incrementItemsFound();
        this.getCurrentStats().addItem(item.getName());
        Util.getInstance().saveGame();
    }

    //
    public void updateAttempts(String mapName) {
        this.getLevelStats(mapName).incrementAttempts();
        this.getGlobalStats().incrementAttempts();
        Util.getInstance().saveGame();
    }

    //
    public void updateDeaths() {
        this.getCurrentStats().incrementDeaths();
        this.getGlobalStats().incrementDeaths();
        Util.getInstance().saveGame();
    }

    public void updatePuzzleAttempts() {
        this.getGlobalStats().incrementPuzzleAttempted();
        Util.getInstance().saveGame();
    }

    public void updatePuzzleCompleted() {
        this.getGlobalStats().incrementPuzzleCompleted();
        Util.getInstance().saveGame();
    }


    public void updateGlobal() {
        saveTimer();
    }

    public void respawn(String mapName) {
        this.getCurrentStats().respawn();
        updateAttempts(mapName);
        Util.getInstance().saveGame();
    }
}
