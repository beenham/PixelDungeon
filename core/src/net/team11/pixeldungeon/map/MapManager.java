package net.team11.pixeldungeon.map;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import net.team11.pixeldungeon.options.TiledMapLayers;
import net.team11.pixeldungeon.options.TiledMapNames;
import net.team11.pixeldungeon.screens.PlayScreen;

import java.util.HashMap;

public class MapManager {
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
        renderer.render(new int[]{TiledMapLayers.BACKGROUND_LAYER});
    }

    public void renderEnvironment() {
        renderer.setView(PlayScreen.gameCam);
        renderer.render(new int[]{TiledMapLayers.FLOOR_LAYER});
        renderer.render(new int[]{TiledMapLayers.WALL_LAYER});
    }

    public void renderDecor() {
        renderer.setView(PlayScreen.gameCam);
        renderer.render(new int[]{TiledMapLayers.DECOR_LAYER});
    }

    public void loadMap(String mapName) {
        if (maps.containsKey(mapName)) {
            currentMap = maps.get(mapName);
            if (renderer != null) {
                renderer.setMap(currentMap.getMap());
            }
        }
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
}
