package net.team11.pixeldungeon.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Map {
    private TiledMap map;

    public Map(String mapString) {
        this.map = new TmxMapLoader().load(mapString);
    }

    public TextureMapObject getTextureObject(String layer, String objectName) {
        return (TextureMapObject) map.getLayers().get(layer).getObjects().get(objectName);
    }

    public TiledMap getMap() {
        return map;
    }
}
