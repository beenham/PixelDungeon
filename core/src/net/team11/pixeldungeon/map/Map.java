package net.team11.pixeldungeon.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import net.team11.pixeldungeon.entities.blocks.Chest;
import net.team11.pixeldungeon.statistics.*;
import net.team11.pixeldungeon.utils.TiledMapLayers;

import java.io.File;

public class Map {
    private TiledMap map;
    private String mapName;
    private boolean loaded;

    private LevelStatistics levelStatistics;

    public Map(String mapString) {
        this.map = new TmxMapLoader().load(mapString);
        this.loaded = false;

        //  Taking the map name from after the directory, to the extension
        //  eg: levels/level_0_0.tmx -> level_0_0
        mapName = mapString.substring(7, mapString.length()-4);

        //setup statistics for the level
        setupStatistics();
    }

    private void setupStatistics(){
        String locRoot = Gdx.files.getLocalStoragePath();
        System.out.println("Local Root is: " + locRoot);

        String statsFileString = locRoot + mapName + ".json";
        System.out.println("Stats File String is: " + statsFileString);
        File statsFile = new File(statsFileString);
        if (statsFile.exists()){
            System.out.println(statsFileString + " already exists, PARSING");
            levelStatistics = Statistics.parseStatsFromFile(statsFileString);
        } else{
            System.out.println("Creating new JSON");
            levelStatistics = new LevelStatistics(mapName);
            Statistics.createNewJson(levelStatistics, statsFileString);
        }
    }

    public MapObjects getObjects(String layer) {
        return map.getLayers().get(layer).getObjects();
    }

    public RectangleMapObject getRectangleObject (String layer, String objectName) {
        return (RectangleMapObject) map.getLayers().get(layer).getObjects().get(objectName);
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public TiledMap getMap() {
        return map;
    }

    public String getMapName() {
        return mapName;
    }

    public LevelStatistics getLevelStatistics() {
        return levelStatistics;
    }
}
