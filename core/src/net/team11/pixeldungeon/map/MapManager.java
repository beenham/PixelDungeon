package net.team11.pixeldungeon.map;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import net.team11.pixeldungeon.entities.blocks.Box;
import net.team11.pixeldungeon.entities.blocks.Chest;
import net.team11.pixeldungeon.entities.blocks.Pillar;
import net.team11.pixeldungeon.entities.door.Door;
import net.team11.pixeldungeon.entities.door.DoorFrame;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.options.TiledMapLayers;
import net.team11.pixeldungeon.options.TiledMapNames;
import net.team11.pixeldungeon.options.TiledMapObjectNames;
import net.team11.pixeldungeon.options.TiledMapProperties;
import net.team11.pixeldungeon.screens.PlayScreen;

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
            currentMap = maps.get(mapName);
            if (renderer != null) {
                renderer.setMap(currentMap.getMap());
            }
            if (!currentMap.isLoaded() && engine != null) {
                setupEntities();
            }
        }
    }

    private void setupEntities() {
        currentMap.setLoaded(true);

        MapObjects mapObjects = currentMap.getObjects(TiledMapLayers.BLOCKS_LAYER);
        MapObjects doorObjects = currentMap.getObjects(TiledMapLayers.DOOR_LAYER);
        for (MapObject object : doorObjects) {
            mapObjects.add(object);
        }

        for (MapObject mapObject : mapObjects) {
            RectangleMapObject object = (RectangleMapObject) mapObject;
            String type = (String) object.getProperties().get(TiledMapProperties.ENTITY_TYPE);
            switch (type) {
                case TiledMapObjectNames.BOX:
                    if (object.getProperties().containsKey(TiledMapProperties.BOX_PUSHABLE)) {
                        boolean pushable = (boolean) object.getProperties().get(TiledMapProperties.BOX_PUSHABLE);
                        engine.addEntity(new Box(object.getRectangle(), pushable, object.getName()));
                    } else {
                        System.err.println("BOX: " + object.getName() + " was not setup correctly!");
                    }
                    break;
                case TiledMapObjectNames.CHEST:
                    if (object.getProperties().containsKey(TiledMapProperties.CHEST_OPENED)) {
                        boolean opened = (boolean) object.getProperties().get(TiledMapProperties.CHEST_OPENED);
                        engine.addEntity(new Chest(object.getRectangle(), opened, object.getName()));
                    } else {
                        System.err.println("CHEST: " + object.getName() + " was not setup correctly!");
                    }
                    break;
                case TiledMapObjectNames.DOOR:
                    if (object.getProperties().containsKey(TiledMapProperties.DOOR_LOCKED)) {
                        boolean locked = (boolean) object.getProperties().get(TiledMapProperties.DOOR_LOCKED);
                        engine.addEntity(new Door(object.getRectangle(), locked, object.getName()));
                    } else {
                        System.err.println("DOOR: " + object.getName() + " was not setup correctly!");
                    }
                    break;
                case TiledMapObjectNames.DOOR_PILLAR:
                    String texture = (String) object.getProperties().get(TiledMapProperties.TEXTURE);
                    engine.addEntity(new DoorFrame(object.getRectangle(), object.getName(), texture));
                    break;
                case TiledMapObjectNames.PILLAR:
                    engine.addEntity(new Pillar(object.getRectangle(), object.getName()));
                    break;
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

    public void setEngine(EntityEngine engine) {
        this.engine = engine;
    }
}
