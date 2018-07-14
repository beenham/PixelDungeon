package net.team11.pixeldungeon.saves;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

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
    private String totalTime;

    public SaveGame(){}

    public SaveGame(HashMap<String, LevelStats> levelStats, GlobalStats globalStats, SkinList skinList, String timeStamp){
        this.levelStatsHashMap = levelStats;
        this.globalStats = globalStats;
        this.skinList = skinList;
        this.timeStamp = timeStamp;
        System.out.println("Finished Creating Save Game");
        System.out.println(this);
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

    public void setGlobalStats(GlobalStats globalStats) {
        this.globalStats = globalStats;
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

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    @Override
    public String toString(){
        return this.levelStatsHashMap + "\n" +  this.globalStats + "\n" + this.skinList.getSkinList() + "\n" + this.totalTime + "\n" + this.timeStamp;
    }
}
