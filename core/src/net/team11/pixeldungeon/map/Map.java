package net.team11.pixeldungeon.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Map {
    private TiledMap map;
    private String mapName;

    public Map(String mapString) {
        this.map = new TmxMapLoader().load(mapString);

        //  Taking the map name from after the directory, to the extension
        //  eg: levels/level_0_0.tmx -> level_0_0
        mapName = mapString.substring(7, mapString.length()-4);
    }

    public TextureMapObject getTextureObject(String layer, String objectName) {
        return (TextureMapObject) map.getLayers().get(layer).getObjects().get(objectName);
    }

    public TiledMap getMap() {
        return map;
    }

    public String getMapName() {
        return mapName;
    }
}
