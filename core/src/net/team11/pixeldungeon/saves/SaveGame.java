package net.team11.pixeldungeon.saves;

import com.badlogic.gdx.utils.Json;

import net.team11.pixeldungeon.inventory.skinselect.SkinList;
import net.team11.pixeldungeon.utils.stats.GlobalStats;
import net.team11.pixeldungeon.utils.stats.LevelStats;

import java.util.HashMap;


public class SaveGame{

    public static final String SAVE_NAME = "pixelDungeonSaveGame";

    public static final String DESC = "Pixel Dungeon Save Game";

    private HashMap<String, LevelStats> levelStatsHashMap;
    private GlobalStats globalStats;
    private SkinList skinList;

    private String timeStamp;
    private int totalTime;

    public SaveGame(){}

    public SaveGame(HashMap<String, LevelStats> levelStats, GlobalStats globalStats, SkinList skinList, String timeStamp){
        this.levelStatsHashMap = levelStats;
        this.globalStats = globalStats;
        this.skinList = skinList;
        this.timeStamp = timeStamp;
        totalTime = globalStats.getTotalTime();
    }

    public byte[] getBytes(){
        Json json = new Json();
        return json.toJson(this).getBytes();
    }

    public HashMap<String, LevelStats> getLevelStatsHashMap() {
        return levelStatsHashMap;
    }

    public void setLevelStatsHashMap(HashMap<String, LevelStats> levelStatsHashMap) {
        this.levelStatsHashMap = levelStatsHashMap;
    }

    public GlobalStats getGlobalStats() {
        return globalStats;
    }

    public SkinList getSkinList() {
        return skinList;
    }

    public void setSkinList(SkinList skinList) {
        this.skinList = skinList;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public int getTotalTime() {
        return totalTime;
    }

    @Override
    public String toString(){
        return this.levelStatsHashMap + "\n" +  this.globalStats + "\n" + this.skinList.getSkinList() + "\n" + this.totalTime + "\n" + this.timeStamp;
    }
}
