package net.team11.pixeldungeon.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import net.team11.pixeldungeon.entities.blocks.Box;
import net.team11.pixeldungeon.entities.blocks.Chest;
import net.team11.pixeldungeon.entities.blocks.Pillar;
import net.team11.pixeldungeon.entities.blocks.Torch;
import net.team11.pixeldungeon.entities.door.Door;
import net.team11.pixeldungeon.entities.door.DoorFrame;
import net.team11.pixeldungeon.entitysystem.EntityEngine;

public class TiledObjectUtil {
    public static void parseTiledEntityLayer (EntityEngine engine, MapObjects mapObjects) {

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
                case TiledMapObjectNames.TORCH:
                    if (object.getProperties().containsKey(TiledMapProperties.TORCH_ON)) {
                        boolean on = (boolean) object.getProperties().get(TiledMapProperties.TORCH_ON);
                        Torch torch = new Torch(object.getRectangle(), on, object.getName());
                        engine.addEntity(torch);
                    } else {
                        System.err.println("TORCH: " + object.getName() + " was not setup correctly!");
                    }
                    break;
                case TiledMapObjectNames.PILLAR:
                    engine.addEntity(new Pillar(object.getRectangle(), object.getName()));
                    break;
            }
        }

    }

    public static void parseTiledObjectLayer(World world, MapObjects objects) {
        for (MapObject mapObject : objects) {
            Shape shape;

            if (mapObject instanceof PolylineMapObject) {
                shape = createPolyLine((PolylineMapObject) mapObject);
            } else {
                continue;
            }

            Body body;
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.KinematicBody;
            body = world.createBody(bdef);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1f;
            byte test = (byte) (CollisionCategory.ENTITY | CollisionCategory.PUZZLE_AREA);
            fixtureDef.filter.categoryBits = CollisionCategory.BOUNDARY;
            fixtureDef.filter.maskBits = test;

            body.createFixture(fixtureDef);
            shape.dispose();
        }
    }

    public static void parseTiledPuzzleLayer(World world, MapObjects objects) {
        for (MapObject mapObject : objects) {
            Shape shape;

            if (mapObject instanceof PolylineMapObject) {
                shape = createPolyLine((PolylineMapObject) mapObject);
            } else {
                continue;
            }

            Body body;
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            body = world.createBody(bdef);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1f;
            fixtureDef.filter.categoryBits = CollisionCategory.PUZZLE_AREA;
            fixtureDef.filter.maskBits = (byte) (CollisionCategory.ENTITY | CollisionCategory.BOUNDARY);

            body.createFixture(fixtureDef);
            shape.dispose();
        }
    }

    private static ChainShape createPolyLine(PolylineMapObject polyline) {
        float[] vertices = polyline.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length/2];

        for (int i = 0 ; i < worldVertices.length ; i++) {
            worldVertices[i] = new Vector2(vertices[i*2], vertices[i * 2 + 1]);
        }
        ChainShape cs = new ChainShape();
        cs.createChain(worldVertices);
        return cs;
    }
}
