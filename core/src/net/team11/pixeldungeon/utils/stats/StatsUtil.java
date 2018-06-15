package net.team11.pixeldungeon.utils.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.HashMap;
import java.util.Locale;

public class StatsUtil {
    private static StatsUtil INSTANCE = new StatsUtil();

    private String TAG = "FILESTORAGE : ";
    private final String globalFile = "stats/globalStats.json";

    private HashMap<String, LevelStats> levelStats = new HashMap<>();
    private GlobalStats globalStats;
    private CurrentStats currentStats;
    private int timer = 0;
    private int flaggedTimer = 0;

    private StatsUtil() {
        clearLocal();
        for (FileHandle file : Gdx.files.local("stats").list()) {
            System.out.println(TAG + " file : " + file.name() + " dir:" + file.isDirectory());
        }
        for (FileHandle file : Gdx.files.local("stats/levels").list()) {
            System.out.println(TAG + " file : " + file.name() + " dir:" + file.isDirectory());
        }

        if (!Gdx.files.local("stats/").exists()) {
            readInternalLevelFiles();
        } else {
            for (FileHandle file : Gdx.files.local("stats/levels").list()) {
                if (file.toString().endsWith(".json")) {
                    Json json = new Json();
                    LevelStats stats = json.fromJson(LevelStats.class,file);
                    System.out.println(TAG + "LOCAL " + file.name() + "\n" + stats.toString());
                    levelStats.put(stats.getLevelName(), stats);
                }
            }
            readInternalLevelFiles();
        }
    }

    private void readInternalLevelFiles() {
        Json json = new Json();
        for (FileHandle file : Gdx.files.internal("stats/levels").list()) {
            if (file.toString().endsWith(".json") && !levelStats.containsKey(file.nameWithoutExtension())) {
                LevelStats stats = json.fromJson(LevelStats.class,file);
                System.out.println(TAG + "INTERNAL " + file.name() + "\n" + stats.toString());
                levelStats.put(stats.getLevelName(), stats);
                String filePath = "stats/levels/" + stats.getLevelName() + ".json";
                Gdx.files.local(filePath).writeString(json.toJson(stats),false);
            }
        }
        globalStats = json.fromJson(GlobalStats.class,Gdx.files.internal(globalFile));
        writeGlobalStats();
    }

    public void writeLevelStats(String file) {
        Json json = new Json();
        LevelStats stats = levelStats.get(file);
        String filePath = "stats/levels/" + stats.getLevelName() + ".json";
        Gdx.files.local(filePath).writeString(json.toJson(stats),false);
    }

    public void writeGlobalStats() {
        Json json = new Json();
        Gdx.files.local(globalFile).writeString(json.toJson(globalStats),false);
    }

    public void initialiseCurrStats() {
        currentStats = new CurrentStats();
    }

    public CurrentStats getCurrentStats() {
        return currentStats;
    }

    public void startTimer() {
        timer = 20;
        flaggedTimer = 0;
    }

    public void incrementTimer() {
        timer++;
    }

    public void saveTimer() {
        globalStats.addTime(timer-flaggedTimer);
        flaggedTimer = timer;
        writeGlobalStats();
    }

    public int getTimer() {
        return timer;
    }

    public String getTimerString() {
        return String.format(Locale.UK,"%02d:%02d",timer/60,timer%60);
    }

    public void clearLocal() {
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
