package net.team11.pixeldungeon.saves;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import net.team11.pixeldungeon.inventory.skinselect.SkinList;
import net.team11.pixeldungeon.utils.stats.GlobalStats;
import net.team11.pixeldungeon.utils.stats.LevelStats;

import java.util.HashMap;


public class SaveGame{

    public static final String TAG = "PixelDungeon";

    public static final String DESC = "Pixel Dungeon Save Game";

    private HashMap<String, LevelStats> levelStatsHashMap;
    private GlobalStats globalStats;
    private SkinList skinList;

    private String timeStamp;
    private String totalTime;

    public SaveGame(){}

    public SaveGame(String levelData, String globalData/*, byte[] skinData*/, String timeStamp){
        loadFromJson(levelData, globalData/*, new String(skinData)*/);
        this.timeStamp = timeStamp;
        System.out.println("Finished Creating Save Game");
        System.out.println(this);
    }


    @SuppressWarnings("unchecked")
    private void loadFromJson(String levelData, String globalData/*, String skinData*/){
        Json json = new Json();

        JsonValue levelValue = new JsonReader().parse(levelData);
        this.levelStatsHashMap = json.readValue(HashMap.class, levelValue);

        JsonValue globalValue = new JsonReader().parse(globalData);
        this.globalStats = json.readValue(GlobalStats.class, globalValue);

//        this.skinList = json.fromJson(SkinList.class, skinData);
        this.totalTime = this.globalStats.getTime();

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
        return this.levelStatsHashMap + "\n" +  this.globalStats + "\n" + this.totalTime + "\n" + this.timeStamp;
    }
}
