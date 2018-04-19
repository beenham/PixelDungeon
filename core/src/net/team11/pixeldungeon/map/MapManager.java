package net.team11.pixeldungeon.map;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import net.team11.pixeldungeon.options.TiledMapLayers;
import net.team11.pixeldungeon.screens.PlayScreen;

import java.util.ArrayList;
import java.util.List;

public class MapManager {
    private List<Map> maps = new ArrayList<Map>();
    private Map currentMap = null;
    private OrthogonalTiledMapRenderer renderer;

    private MapManager() {
        FileHandle mapFolder = Gdx.files.internal("levels");
        for (FileHandle entry : mapFolder.list()) {
            maps.add(new Map(entry.toString()));
        }

        loadMap(maps.get(0));

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

    private void loadMap(Map map) {
        currentMap = map;
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
