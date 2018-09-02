package net.team11.pixeldungeon.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.game.entitysystem.EntityEngine;
import net.team11.pixeldungeon.game.puzzles.Puzzle;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.T11Log;
import net.team11.pixeldungeon.utils.Util;
import net.team11.pixeldungeon.utils.stats.LevelStats;
import net.team11.pixeldungeon.utils.tiled.TiledMapLayers;
import net.team11.pixeldungeon.utils.tiled.TiledMapNames;
import net.team11.pixeldungeon.utils.tiled.TiledObjectUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MapManager {
    private static final String TAG = "MapManager";
    private static MapManager INSTANCE;
    private EntityEngine engine;

    private HashMap<String, Map> maps;
    private Map currentMap;
    private OrthogonalTiledMapRenderer renderer;
    private HashMap<Integer,ArrayList<String>> mapList;

    private MapManager() {
        currentMap = null;
        maps = new HashMap<>();
        HashMap<Integer, HashMap<Integer,String>> mapIndex = new HashMap<>();

        FileHandle mapFolder = Gdx.files.internal("levels");
        for (FileHandle entry : mapFolder.list()) {
            if (entry.toString().endsWith(".tmx")) {
                T11Log.error(TAG, "LOADING FILE: " + entry.toString());
                Map map = new Map(entry.toString());
                String mapName = map.getMapName();
                int set = Integer.valueOf(mapName.substring(mapName.indexOf("_") + 1,mapName.lastIndexOf("_")));
                int index = Integer.valueOf(mapName.substring(mapName.lastIndexOf("_") + 1,mapName.length()));
                if (mapIndex.containsKey(set)) {
                    mapIndex.get(set).put(index,mapName);
                } else {
                    mapIndex.put(set,new HashMap<Integer, String>());
                    mapIndex.get(set).put(index,mapName);
                }
                maps.put(map.getMapName(), map);
            }
        }

        mapList = new HashMap<>();
        ArrayList<Integer> mapOrder = new ArrayList<>();
        for (HashMap.Entry<Integer,HashMap<Integer,String>> mEntry : mapIndex.entrySet()) {
            for (HashMap.Entry<Integer,String> mEntry1 : mEntry.getValue().entrySet()) {
                mapOrder.add(mEntry1.getKey());
            }
            Collections.sort(mapOrder);
            ArrayList<String> list = new ArrayList<>();
            for (int index : mapOrder) {
                list.add(mEntry.getValue().get(index));
            }
            mapList.put(mEntry.getKey(),list);
            mapOrder = new ArrayList<>();
        }
        T11Log.error(TAG,"MapList : " + mapList);

        loadMap(TiledMapNames.LEVEL_TUTORIAL);
    }

    public void renderBackGround() {
        renderer.setView(PlayScreen.gameCam);
        int[] layers = {currentMap.getMap().getLayers().getIndex(TiledMapLayers.BACKGROUND_LAYER)};
        renderer.render(layers);
    }

    public void renderEnvironment() {
        renderer.setView(PlayScreen.gameCam);
        int[] layers = {currentMap.getMap().getLayers().getIndex(TiledMapLayers.FLOOR_LAYER),
                currentMap.getMap().getLayers().getIndex(TiledMapLayers.TRAP_FLOOR_LAYER),
                currentMap.getMap().getLayers().getIndex(TiledMapLayers.WALL_LAYER)};
        renderer.render(layers);
    }

    public void renderWallTop() {
        renderer.setView(PlayScreen.gameCam);
        int[] layers = {
                currentMap.getMap().getLayers().getIndex(TiledMapLayers.WALLTOP1_LAYER),
                currentMap.getMap().getLayers().getIndex(TiledMapLayers.WALLTOP2_LAYER),
                currentMap.getMap().getLayers().getIndex(TiledMapLayers.WALLTOP3_LAYER),
                currentMap.getMap().getLayers().getIndex(TiledMapLayers.DECOR_LAYER)
        };

        renderer.render(layers);
    }

    public void loadMap(String mapName) {
        if (maps.containsKey(mapName)) {
            currentMap = maps.get(mapName);
            if (renderer != null) {
                renderer.setMap(currentMap.getMap());
                renderer.setView(PlayScreen.gameCam);
            } else {
                renderer = new OrthogonalTiledMapRenderer(currentMap.getMap());
                renderer.setView(PlayScreen.gameCam);
            }
            if (!currentMap.isLoaded() && engine != null) {
                loadEntities();
            }
        }
    }

    private void loadEntities() {
        currentMap.setLoaded(true);
        T11Log.info(TAG,"Loading new entities in : " + currentMap.getMapName());

        TiledObjectUtil.parseTiledPuzzleLayer(engine,PlayScreen.world,currentMap.getObjects(TiledMapLayers.PUZZLE_LAYER));
        TiledObjectUtil.parseTiledRoomLayer(engine, currentMap.getObjects(TiledMapLayers.TRAP_LAYER));
        TiledObjectUtil.parseTiledEntityLayer(engine, currentMap.getObjects(TiledMapLayers.TRAP_LAYER));
        TiledObjectUtil.parseTiledEntityLayer(engine, currentMap.getObjects(TiledMapLayers.BLOCKS_LAYER));
        TiledObjectUtil.parseTiledEntityLayer(engine, currentMap.getObjects(TiledMapLayers.DOOR_LAYER));
        TiledObjectUtil.parseTiledObjectLayer(PlayScreen.world,currentMap.getObjects(TiledMapLayers.COLLISION_LAYER));
        if (currentMap.hasLayer(TiledMapLayers.TUTORIAL_LAYER)) {
            TiledObjectUtil.parseTiledRoomLayer(engine, currentMap.getObjects(TiledMapLayers.TUTORIAL_LAYER));
        }

        for (Entity entity : engine.getEntities()) {
            if (entity.hasTrigger()) {
                TiledObjectUtil.parseTargets(engine, entity);
            }
        }

        for (Puzzle puzzle : engine.getPuzzles()) {
            TiledObjectUtil.parseTargets(engine,puzzle);
            puzzle.setupEntities(engine);
        }
    }

    /////////////////////////////
    //   Getters and Setters   //
    /////////////////////////////

    public Map getCurrentMap() {
        return currentMap;
    }

    public Map getMap(String map) {
        return maps.get(map);
    }

    public Map getFirstMap() {
        return maps.get(mapList.get(0).get(0));
    }

    public Map getPrevious(String map) {
        int set = 0;
        for (int i : mapList.keySet()) {
            if (mapList.get(i).contains(map)) {
                set = i;
                break;
            }
        }

        HashMap<String,LevelStats> stats = Util.getInstance().getStatsUtil().getLevelStats();
        int index = mapList.get(set).indexOf(map);
        index--;
        if (index < 0) {
            set--;
            if (set < 0) {
                set = mapList.size()-1;
            }
            index = mapList.get(set).size()-1;
            if (mapList.get(set).size() > 1) {
                LevelStats lStats = stats.get(mapList.get(set).get(index - 1));
                while (index > 0 && lStats.getCompleted() == 0) {
                    index--;
                    lStats = stats.get(mapList.get(set).get(index - 1));
                }
            }
        }
        return maps.get(mapList.get(set).get(index));
    }

    public Map getNext(String map) {
        int set = 0;
        for (int i : mapList.keySet()) {
            if (mapList.get(i).contains(map)) {
                set = i;
                break;
            }
        }

        HashMap<String,LevelStats> stats = Util.getInstance().getStatsUtil().getLevelStats();
        int index = mapList.get(set).indexOf(map);
        LevelStats lStats = stats.get(mapList.get(set).get(index));
        if (!lStats.isTutorial()) {
            if (lStats.getCompleted() > 0) {
                index++;
            } else {
                index = mapList.get(set).size();
            }
        } else {
            index++;
        }
        if (index == mapList.get(set).size()) {
            set++;
            if (set == mapList.size()) {
                set = 0;
            }
            index = 0;
        }
        return maps.get(mapList.get(set).get(index));
    }

    public void reset() {
        for (String map : maps.keySet()) {
            maps.get(map).setLoaded(false);
        }
    }

    public static MapManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MapManager();
        }
        return INSTANCE;
    }

    public void setEngine(EntityEngine engine) {
        this.engine = engine;
    }
}
