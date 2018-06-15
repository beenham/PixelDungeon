package net.team11.pixeldungeon.utils.tiled;

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
import net.team11.pixeldungeon.entities.blocks.Lever;
import net.team11.pixeldungeon.entities.blocks.Pillar;
import net.team11.pixeldungeon.entities.blocks.Torch;
import net.team11.pixeldungeon.entities.door.ButtonDoor;
import net.team11.pixeldungeon.entities.door.DoorFrame;
import net.team11.pixeldungeon.entities.door.LockedDoor;
import net.team11.pixeldungeon.entities.door.MechanicDoor;
import net.team11.pixeldungeon.entities.traps.FloorSpike;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.items.Coin;
import net.team11.pixeldungeon.items.Item;
import net.team11.pixeldungeon.items.keys.ChestKey;
import net.team11.pixeldungeon.items.keys.DoorKey;
import net.team11.pixeldungeon.items.keys.EndKey;
import net.team11.pixeldungeon.utils.CollisionCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TiledObjectUtil {
    /**
     * Used to add entities of the map objects into the engine
     * @param engine Game engine that holds systems, and entities
     * @param mapObjects The Objects taken from the Tiled Map file
     */
    public static void parseTiledEntityLayer (EntityEngine engine, MapObjects mapObjects) {
        if (mapObjects != null) {
            for (MapObject mapObject : mapObjects) {
                
                //  What is used to get the information from the entity objects on the map
                RectangleMapObject object = (RectangleMapObject) mapObject;

                //  Retrieves the type of entity specified in the tiled map
                String type = (String) object.getProperties().get(TiledMapProperties.ENTITY_TYPE);

                //  If the entity has any targets to 'trigger'
                List<String> targets = new ArrayList<>();
                if (mapObject.getProperties().containsKey(TiledMapProperties.TARGET)) {
                    targets = parseTargets(mapObject);
                }
                boolean trigger = false;
                if (mapObject.getProperties().containsKey(TiledMapProperties.TRIGGER)) {
                    trigger = (boolean) mapObject.getProperties().get(TiledMapProperties.TRIGGER);
                }


                switch (type) {
                    case TiledMapObjectNames.BOX:   //  Box entity
                        if (object.getProperties().containsKey(TiledMapProperties.BOX_PUSHABLE)) {
                            boolean pushable = (boolean) object.getProperties().get(TiledMapProperties.BOX_PUSHABLE);
                            engine.addEntity(new Box(object.getRectangle(), pushable, object.getName()));
                        } else {
                            System.err.println("BOX: " + object.getName() + " was not setup correctly!");
                        }
                        break;

                    case TiledMapObjectNames.CHEST: //  Chest entity
                        if (object.getProperties().containsKey(TiledMapProperties.OPENED)) {
                            boolean opened = (boolean) object.getProperties().get(TiledMapProperties.OPENED);
                            boolean locked = (boolean) object.getProperties().get(TiledMapProperties.LOCKED);
                            String itemName = (String) object.getProperties().get(TiledMapProperties.ITEM_NAME);
                            int amount = (int) object.getProperties().get(TiledMapProperties.AMOUNT);
                            Item item = null;
                            System.out.println((String)object.getProperties().get(TiledMapProperties.ITEM));
                            switch ((String)object.getProperties().get(TiledMapProperties.ITEM)) {
                                case TiledMapObjectNames.COIN:
                                    item = new Coin(amount);
                                    break;
                                case TiledMapObjectNames.DOOR_KEY:
                                    item = new DoorKey(itemName);
                                    break;
                                case TiledMapObjectNames.CHEST_KEY:
                                    item = new ChestKey(itemName);
                                    break;
                                case TiledMapObjectNames.BOSS_KEY:
                                    item = new EndKey(itemName);
                                    break;

                            }

                            Chest chest = new Chest(object.getRectangle(), opened,locked, object.getName(), item);
                            if (chest.isLocked()){
                                    chest.setChestKey(new ChestKey((String) object.getProperties().get(TiledMapProperties.KEY_NAME)));
                            }
                            engine.addEntity(chest);
                        } else {
                            System.err.println("CHEST: " + object.getName() + " was not setup correctly!");
                        }
                        break;

                    case TiledMapObjectNames.DOOR:  //  Door entity
                        if (object.getProperties().containsKey(TiledMapProperties.OPENED)) {
                            String doorType = (String) object.getProperties().get(TiledMapProperties.DOOR_TYPE);
                            boolean open = (boolean) object.getProperties().get(TiledMapProperties.OPENED);
                            switch (doorType) {
                                case "BUTTON":
                                    engine.addEntity(new ButtonDoor(object.getName(), object.getRectangle(), open));
                                    break;
                                case "LOCKED":
                                    String keyName = (String) object.getProperties().get(TiledMapProperties.KEY_NAME);
                                    engine.addEntity(new LockedDoor(object.getName(), object.getRectangle(), open, keyName));
                                    //System.out.println("//\n"+keyID+"\n"+keyName+"\\");
                                    break;
                                case "MECHANIC":
                                    engine.addEntity(new MechanicDoor(object.getName(), object.getRectangle(), open));
                                    break;
                                default:
                                    System.err.println("DOOR: " + object.getName() + " was not setup correctly!");
                            }
                        } else {
                            System.err.println("DOOR: " + object.getName() + " was not setup correctly!");
                        }
                        break;

                    case TiledMapObjectNames.DOOR_PILLAR:   //  Door Pillar entity (For edge of doors)
                        String texture = (String) object.getProperties().get(TiledMapProperties.TEXTURE);
                        engine.addEntity(new DoorFrame(object.getRectangle(), object.getName(), texture));
                        break;

                    case TiledMapObjectNames.FLOOR_SPIKE:   //  Floor spike entity
                        //  Mandatory keys / properties
                        if (object.getProperties().containsKey(TiledMapProperties.ENABLED)
                                && object.getProperties().containsKey(TiledMapProperties.TIMED)
                                && object.getProperties().containsKey(TiledMapProperties.TIMER)) {
                            FloorSpike floorSpike;
                            boolean enabled = (boolean) object.getProperties().get(TiledMapProperties.ENABLED);
                            boolean timed = (boolean) object.getProperties().get(TiledMapProperties.TIMED);
                            if (timed) {
                                float timer = (float) object.getProperties().get(TiledMapProperties.TIMER);
                                engine.addEntity(floorSpike = new FloorSpike(object.getRectangle(), enabled,
                                        object.getName(), timer));
                            } else {
                                engine.addEntity(floorSpike = new FloorSpike(object.getRectangle(), enabled, object.getName()));
                            }

                            if (mapObject.getProperties().containsKey(TiledMapProperties.TARGET)) {
                                floorSpike.setTargets(targets);
                                floorSpike.setTrigger(trigger);
                            }
                        } else {
                            System.out.println("FLOORSPIKE: " + object.getName() + " was not setup correctly!");
                        }
                        break;

                    case TiledMapObjectNames.TORCH: //  Torch entity
                        if (object.getProperties().containsKey(TiledMapProperties.TORCH_ON)) {
                            boolean on = (boolean) object.getProperties().get(TiledMapProperties.TORCH_ON);
                            Torch torch = new Torch(object.getRectangle(), on, object.getName());
                            engine.addEntity(torch);
                        } else {
                            System.err.println("TORCH: " + object.getName() + " was not setup correctly!");
                        }
                        break;

                    case TiledMapObjectNames.PILLAR:    //  Pillar entity
                        engine.addEntity(new Pillar(object.getRectangle(), object.getName()));
                        break;

                    case TiledMapObjectNames.LEVER:
                        if (object.getProperties().containsKey(TiledMapProperties.TARGET)){
                            Lever lever = new Lever(object.getRectangle(), object.getName());
                            engine.addEntity(lever);
                            lever.setTrigger(trigger);
                            lever.setTargets(targets);
                        }
                        break;

                    default:
                        throw new IllegalArgumentException("This isn't a valid entity! " + type);
                }
            }
        }
    }

    /**
     * Used to add the collision walls into the game
     * @param world Game World that holds body entities for collision
     * @param objects The Objects taken from the Tiled Map file
     */
    public static void parseTiledObjectLayer(World world, MapObjects objects) {
        for (MapObject mapObject : objects) {
            Shape shape;

            //  If object is not polyline object, end loop
            if (mapObject instanceof PolylineMapObject) {
                shape = createPolyLine((PolylineMapObject) mapObject);
            } else {
                continue;
            }

            //  Defining the body
            Body body;
            BodyDef bdef = new BodyDef();
            //  Unmovable / unpassable
            bdef.type = BodyDef.BodyType.KinematicBody;
            body = world.createBody(bdef);

            //  Creating fixture of the def
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 0f;    //  Setting density to 0 for collision / pushing animation

            /*
             *  Defines what bodies the collide with this body
             *  This body is of the BOUNDARY TYPE
             *  This body will collide with ENTITY and PUZZLE AREA types
             */
            fixtureDef.filter.categoryBits = CollisionCategory.BOUNDARY;
            fixtureDef.filter.maskBits = (byte) (CollisionCategory.ENTITY | CollisionCategory.PUZZLE_AREA);

            body.createFixture(fixtureDef);
            shape.dispose();
        }
    }

    /**
     * Used to add the collision walls into the game
     * @param world Game World that holds body entities for collision
     * @param objects The Objects taken from the Tiled Map file
     */
    public static void parseTiledPuzzleLayer(World world, MapObjects objects) {
        for (MapObject mapObject : objects) {
            Shape shape;

            //  If object is not polyline object, end loop
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
            fixtureDef.density = 0f;    //  Setting density to 0 for collision / pushing animation

            /*
             *  Defines what bodies the collide with this body
             *  This body is of the PUZZLE AREA TYPE
             *  This body will collide with ENTITY and BOUNDARY types
             */
            fixtureDef.filter.categoryBits = CollisionCategory.PUZZLE_AREA;
            fixtureDef.filter.maskBits = (byte) (CollisionCategory.ENTITY | CollisionCategory.BOUNDARY);

            body.createFixture(fixtureDef);
            shape.dispose();
        }
    }

    /**
     * Used to create a body shape for the collision boundary
     * @param polyline The line from the tiled map file
     * @return cs the Shape of the polygon
     */
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

    /**
     * Used to find the targets of objects within the map file
     * @param mapObject Object to retrieve targets
     * @return List of String of target names
     */
    public static ArrayList<String> parseTargets(MapObject mapObject) {
        String targetString = (String)mapObject.getProperties().get(TiledMapProperties.TARGET);
        List<String> parsedTargets = Arrays.asList(targetString.split(","));
        return new ArrayList<>(parsedTargets);
    }

    /**
     * Used to add the target entities into the current entity.
     * @param engine Engine to go through all entities
     * @param currEntity Current entity
     */
    public static void parseTargets(EntityEngine engine, Entity currEntity) {
        List<Entity> targetEntities = new ArrayList<>();
        for (String target : currEntity.getTargets()) {
            for (Entity entity : engine.getEntities()) {
                if (entity.getName().equals(target)) {
                    targetEntities.add(entity);
                }
            }
        }
        currEntity.setTargetEntities(targetEntities);
    }
}
