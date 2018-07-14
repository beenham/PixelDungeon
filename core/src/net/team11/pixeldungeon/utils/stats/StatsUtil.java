package net.team11.pixeldungeon.utils.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.saves.SaveGame;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class StatsUtil {
//    private static StatsUtil INSTANCE = new StatsUtil();
    private String TAG = "FILESTORAGE : ";
    private final String globalFile = "stats/globalStats.json";

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

//    public static StatsUtil getInstance() {
//        if (INSTANCE == null) {
//            INSTANCE = new StatsUtil();
//        }
//        return INSTANCE;
//    }
}
