package net.team11.pixeldungeon.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.utils.TiledMapLayers;
import net.team11.pixeldungeon.utils.TiledMapNames;
import net.team11.pixeldungeon.screens.PlayScreen;
import net.team11.pixeldungeon.utils.TiledObjectUtil;

import java.util.HashMap;

public class MapManager {
    private EntityEngine engine;

    private HashMap<String, Map> maps = new HashMap<>();
    private Map currentMap = null;
    private OrthogonalTiledMapRenderer renderer;

    private MapManager() {
        FileHandle mapFolder = Gdx.files.internal("levels");
        for (FileHandle entry : mapFolder.list()) {
            Map map = new Map(entry.toString());
            maps.put(map.getMapName(), map);
        }

        loadMap(TiledMapNames.LEVEL_0_0);
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
                currentMap.getMap().getLayers().getIndex(TiledMapLayers.WALL_LAYER)};
        renderer.render(layers);
    }

    public void renderDecor() {
        renderer.setView(PlayScreen.gameCam);
        int[] layers = {currentMap.getMap().getLayers().getIndex(TiledMapLayers.DECOR_LAYER)};

        renderer.render(layers);
    }

    public void loadMap(String mapName) {
        if (maps.containsKey(mapName)) {
            if (engine != null && engine.hasEntities()) {
                engine.storeEntities(currentMap.getMapName());
            }
            currentMap = maps.get(mapName);
            if (renderer != null) {
                renderer.setMap(currentMap.getMap());
            }
            if (!currentMap.isLoaded() && engine != null) {
                loadEntities();
            } else if (engine != null) {
                engine.loadEntities(currentMap.getMapName());
            }
        }
    }

    private void loadEntities() {
        currentMap.setLoaded(true);
        System.out.println("Loading new entities in : " + currentMap.getMapName());

        TiledObjectUtil.parseTiledEntityLayer(engine, currentMap.getObjects(TiledMapLayers.BLOCKS_LAYER));
        TiledObjectUtil.parseTiledEntityLayer(engine, currentMap.getObjects(TiledMapLayers.DOOR_LAYER));
        TiledObjectUtil.parseTiledObjectLayer(PlayScreen.world,currentMap.getObjects(TiledMapLayers.COLLISION_LAYER));
        TiledObjectUtil.parseTiledPuzzleLayer(PlayScreen.world,currentMap.getObjects(TiledMapLayers.PUZZLE_LAYER));
    }

    /////////////////////////////
    //   Getters and Setters   //
    /////////////////////////////

    public Map getCurrentMap() {
        return currentMap;
    }

    private static MapManager INSTANCE = new MapManager();

    public static MapManager getInstance() {
        return INSTANCE;
    }

    public void setEngine(EntityEngine engine) {
        this.engine = engine;
    }
}
