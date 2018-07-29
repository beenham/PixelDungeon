package net.team11.pixeldungeon.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.game.entitysystem.EntityEngine;
import net.team11.pixeldungeon.game.puzzles.Puzzle;
import net.team11.pixeldungeon.utils.stats.StatsUtil;
import net.team11.pixeldungeon.utils.tiled.TiledMapLayers;
import net.team11.pixeldungeon.utils.tiled.TiledMapNames;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.tiled.TiledObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class MapManager {
    private static MapManager INSTANCE;
    private EntityEngine engine;

    private HashMap<String, Map> maps;
    private Map currentMap;
    private OrthogonalTiledMapRenderer renderer;
    private ArrayList<String> mapList;

    private MapManager() {
        currentMap = null;
        maps = new HashMap<>();
        mapList = new ArrayList<>();

        FileHandle mapFolder = Gdx.files.internal("levels");
        for (FileHandle entry : mapFolder.list()) {
            if (entry.toString().endsWith(".tmx")) {
                System.err.println("LOADING FILE: " + entry.toString());
                Map map = new Map(entry.toString());
                maps.put(map.getMapName(), map);
                mapList.add(map.getMapName());
            }
        }

        loadMap(TiledMapNames.LEVEL_TUTORIAL);
        renderer = new OrthogonalTiledMapRenderer(currentMap.getMap());
        renderer.setView(PlayScreen.gameCam);
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
            }
            if (!currentMap.isLoaded() && engine != null) {
                loadEntities();
            }
        }
    }

    private void loadEntities() {
        currentMap.setLoaded(true);
        System.out.println("Loading new entities in : " + currentMap.getMapName());

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
        return maps.get(mapList.get(0));
    }

    public Map getPrevious(String map) {
        int index = mapList.indexOf(map);
        index--;
        if (index < 0) {
            index = mapList.size() - 1;
        }
        return maps.get(mapList.get(index));
    }

    public Map getNext(String map) {
        int index = mapList.indexOf(map);
        index++;
        if (index == mapList.size()) {
            index = 0;
        }
        return maps.get(mapList.get(index));
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
