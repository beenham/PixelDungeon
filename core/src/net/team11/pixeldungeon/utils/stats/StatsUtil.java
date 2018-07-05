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
    private static StatsUtil INSTANCE = new StatsUtil();
    private String TAG = "FILESTORAGE : ";
    private final String globalFile = "stats/globalStats.json";

    private HashMap<String, LevelStats> levelStats;
    private GlobalStats globalStats;
    private CurrentStats currentStats;
    private int timer = 0;
    private int flaggedTimer = 0;

    private SaveGame currentSave;

    private StatsUtil() {
//        clearLocal();
        currentSave = PixelDungeon.getInstance().getAndroidInterface().loadSaveGame();
        this.levelStats = currentSave.getLevelStatsHashMap();
        this.globalStats = currentSave.getGlobalStats();
    }

    private void readInternalLevelFiles() {
        Json json = new Json();
        for (FileHandle file : Gdx.files.internal("stats/levels").list()) {
            if (file.toString().endsWith(".json") && !levelStats.containsKey(file.nameWithoutExtension())) {
                LevelStats stats = json.fromJson(LevelStats.class,file);
                System.out.println(json.toJson(stats));
                levelStats.put(stats.getFileName(), stats);
                String filePath = "stats/levels/" + stats.getFileName() + ".json";
                Gdx.files.local(filePath).writeString(json.toJson(stats),false);
            }
        }
        readGlobalStats();
        saveGlobalStats();
    }

    private void readGlobalStats() {
        try {
            globalStats = new Json().fromJson(GlobalStats.class,Gdx.files.local(globalFile));
        } catch (Exception e) {
            System.out.println(TAG + " generating new global");
            globalStats = new Json().fromJson(GlobalStats.class,Gdx.files.internal(globalFile));
        }
    }

    public void writeLevelStats(String file) {
        Json json = new Json();
        LevelStats stats = levelStats.get(file);
        String filePath = "stats/levels/" + stats.getFileName() + ".json";
        Gdx.files.local(filePath).writeString(json.toJson(stats),false);
        currentSave.setLevelStatsHashMap(levelStats);
        PixelDungeon.getInstance().getAndroidInterface().saveGame(currentSave);
    }


    public void saveGlobalStats() {
        Json json = new Json();
        Gdx.files.local(globalFile).writeString(json.toJson(globalStats),false);
        currentSave.setGlobalStats(globalStats);
        PixelDungeon.getInstance().getAndroidInterface().saveGame(currentSave);
    }

    public void setLevelStats(HashMap<String, LevelStats> map){
        this.levelStats = map;
    }

    public void setGlobalStats(GlobalStats globalStats){
        this.globalStats = globalStats;
    }

    public SaveGame getCurrentSave() {
        return currentSave;
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
        saveGlobalStats();
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

    public static StatsUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StatsUtil();
        }
        return INSTANCE;
    }
}
