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

import net.team11.pixeldungeon.entities.beams.BeamTarget;
import net.team11.pixeldungeon.entities.blocks.Box;
import net.team11.pixeldungeon.entities.blocks.Chest;
import net.team11.pixeldungeon.entities.blocks.FloorPiston;
import net.team11.pixeldungeon.entities.blocks.Lever;
import net.team11.pixeldungeon.entities.blocks.Pillar;
import net.team11.pixeldungeon.entities.blocks.PressurePlate;
import net.team11.pixeldungeon.entities.blocks.Torch;
import net.team11.pixeldungeon.entities.door.ButtonDoor;
import net.team11.pixeldungeon.entities.door.DoorFrame;
import net.team11.pixeldungeon.entities.door.DungeonDoor;
import net.team11.pixeldungeon.entities.door.LockedDoor;
import net.team11.pixeldungeon.entities.door.MechanicDoor;
import net.team11.pixeldungeon.entities.beams.Beam;
import net.team11.pixeldungeon.entities.beams.BeamGenerator;
import net.team11.pixeldungeon.entities.beams.Reflector;
import net.team11.pixeldungeon.entities.puzzle.CompletedIndicator;
import net.team11.pixeldungeon.entities.puzzle.PuzzleController;
import net.team11.pixeldungeon.entities.puzzle.colouredgems.GemPillar;
import net.team11.pixeldungeon.entities.puzzle.colouredgems.WallScribe;
import net.team11.pixeldungeon.entities.puzzle.simonsays.SimonSaysSwitch;
import net.team11.pixeldungeon.entities.traps.FloorSpike;
import net.team11.pixeldungeon.entities.traps.Quicksand;
import net.team11.pixeldungeon.entities.traps.TrapRoom;
import net.team11.pixeldungeon.entity.component.entitycomponent.TrapRoomComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.items.Coin;
import net.team11.pixeldungeon.items.Item;
import net.team11.pixeldungeon.items.keys.ChestKey;
import net.team11.pixeldungeon.items.keys.DoorKey;
import net.team11.pixeldungeon.items.keys.DungeonKey;
import net.team11.pixeldungeon.puzzles.Puzzle;
import net.team11.pixeldungeon.puzzles.beamPuzzle.BeamPuzzle;
import net.team11.pixeldungeon.puzzles.boxpuzzle.BoxPuzzle;
import net.team11.pixeldungeon.puzzles.levelpuzzle.LevelPuzzle;
import net.team11.pixeldungeon.puzzles.simonsays.SimonSays;
import net.team11.pixeldungeon.puzzles.colouredgems.ColouredGemsPuzzle;
import net.team11.pixeldungeon.utils.CollisionUtil;

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
                RectangleMapObject rectObject = new RectangleMapObject();
                if (mapObject instanceof RectangleMapObject) {
                    rectObject = (RectangleMapObject)mapObject;
                }

                //  Retrieves the type of entity specified in the tiled map
                String type = (String) mapObject.getProperties().get(TiledMapProperties.ENTITY_TYPE);

                //  If the entity has any targets to 'trigger'
                List<String> targets = new ArrayList<>();
                if (mapObject.getProperties().containsKey(TiledMapProperties.TARGET)) {
                    targets = parseTargets(mapObject, TiledMapProperties.TARGET);
                }

                boolean trigger = false;
                if (mapObject.getProperties().containsKey(TiledMapProperties.TRIGGER)) {
                    trigger = (boolean) mapObject.getProperties().get(TiledMapProperties.TRIGGER);
                }

                String puzzleName;


                switch (type) {
                    case TiledMapObjectNames.BOX:   //  Box entity
                        if (rectObject.getProperties().containsKey(TiledMapProperties.BOX_PUSHABLE)) {
                            boolean pushable = (boolean) rectObject.getProperties().get(TiledMapProperties.BOX_PUSHABLE);
                            engine.addEntity(new Box(rectObject.getRectangle(), pushable, rectObject.getName()));
                        } else {
                            System.err.println("BOX: " + rectObject.getName() + " was not setup correctly!");
                        }
                        break;

                    case TiledMapObjectNames.CHEST: //  Chest entity
                        if (rectObject.getProperties().containsKey(TiledMapProperties.OPENED)) {
                            boolean opened = (boolean) rectObject.getProperties().get(TiledMapProperties.OPENED);
                            boolean locked = (boolean) rectObject.getProperties().get(TiledMapProperties.LOCKED);
                            boolean dungeonKey = false;
                            int amount = (int) rectObject.getProperties().get(TiledMapProperties.AMOUNT);
                            Item item = null;
                            switch ((String)rectObject.getProperties().get(TiledMapProperties.ITEM)) {
                                case TiledMapObjectNames.COIN:
                                    item = new Coin(amount);
                                    break;
                                case TiledMapObjectNames.DOOR_KEY:
                                    item = new DoorKey();
                                    break;
                                case TiledMapObjectNames.CHEST_KEY:
                                    item = new ChestKey();
                                    break;
                                case TiledMapObjectNames.DUNGEON_KEY:
                                    item = new DungeonKey();
                                    dungeonKey = true;
                                    break;
                            }
                            Chest chest = new Chest(rectObject.getRectangle(), opened, locked, dungeonKey, rectObject.getName(), item);
                            if (chest.isLocked()) {
                                chest.setChestKey(new ChestKey());
                            }
                            engine.addEntity(chest);
                        } else {
                            System.err.println("CHEST: " + rectObject.getName() + " was not setup correctly!");
                        }
                        break;

                    case TiledMapObjectNames.DOOR:  //  Door entity
                        if (rectObject.getProperties().containsKey(TiledMapProperties.OPENED)) {
                            String doorType = (String) rectObject.getProperties().get(TiledMapProperties.DOOR_TYPE);
                            boolean open = (boolean) rectObject.getProperties().get(TiledMapProperties.OPENED);
                            switch (doorType) {
                                case "BUTTON":
                                    engine.addEntity(new ButtonDoor(rectObject.getName(), rectObject.getRectangle(), open));
                                    break;
                                case "DUNGEON":
                                    engine.addEntity(new DungeonDoor(rectObject.getName(), rectObject.getRectangle()));
                                    break;
                                case "LOCKED":
                                    engine.addEntity(new LockedDoor(rectObject.getName(), rectObject.getRectangle(), open));
                                    //System.out.println("//\n"+keyID+"\n"+keyName+"\\");
                                    break;
                                case "MECHANIC":
                                    engine.addEntity(new MechanicDoor(rectObject.getName(), rectObject.getRectangle(), open));
                                    break;
                                default:
                                    System.err.println("DOOR: " + rectObject.getName() + " was not setup correctly!");
                            }
                        } else {
                            System.err.println("DOOR: " + rectObject.getName() + " was not setup correctly!");
                        }
                        break;

                    case TiledMapObjectNames.DOOR_PILLAR:   //  Door Pillar entity (For edge of doors)
                        String texture = (String) rectObject.getProperties().get(TiledMapProperties.TEXTURE);
                        engine.addEntity(new DoorFrame(rectObject.getRectangle(), rectObject.getName(), texture));
                        break;

                    case TiledMapObjectNames.FLOOR_SPIKE:   //  Floor spike entity
                        //  Mandatory keys / properties
                        if (rectObject.getProperties().containsKey(TiledMapProperties.ENABLED)
                                && rectObject.getProperties().containsKey(TiledMapProperties.TIMED)
                                && rectObject.getProperties().containsKey(TiledMapProperties.TIMER)) {
                            FloorSpike floorSpike;
                            boolean enabled = (boolean) rectObject.getProperties().get(TiledMapProperties.ENABLED);
                            boolean timed = (boolean) rectObject.getProperties().get(TiledMapProperties.TIMED);
                            String room = (String) rectObject.getProperties().get(TiledMapProperties.ROOM);

                            if (timed) {
                                float timer = (float) rectObject.getProperties().get(TiledMapProperties.TIMER);
                                floorSpike = new FloorSpike(rectObject.getRectangle(), enabled,
                                        rectObject.getName(), timer);
                            } else {
                                floorSpike = new FloorSpike(rectObject.getRectangle(), enabled, rectObject.getName());
                            }
                            List<Entity> trapRooms = engine.getEntities(TrapRoomComponent.class);
                            for (Entity entity : trapRooms) {
                                if (entity instanceof TrapRoom && entity.getName().equals(room)) {
                                    ((TrapRoom) entity).addTrap(floorSpike);
                                    engine.addEntity(floorSpike);
                                }
                            }

                            if (mapObject.getProperties().containsKey(TiledMapProperties.TARGET)) {
                                floorSpike.setTargets(targets);
                                floorSpike.setTrigger(trigger);
                            }
                        } else {
                            System.out.println("FLOORSPIKE: " + rectObject.getName() + " was not setup correctly!");
                        }
                        break;

                    case TiledMapObjectNames.GEM_PILLAR:
                        puzzleName = (String) mapObject.getProperties().get(TiledMapProperties.PUZZLE_NAME);
                        int id = (int) mapObject.getProperties().get(TiledMapProperties.ID);

                        GemPillar gemPillar = new GemPillar(
                                rectObject.getRectangle(),mapObject.getName(), id);
                        gemPillar.setParentPuzzle(engine.getPuzzle(puzzleName));
                        engine.addEntity(gemPillar);
                        break;

                    case TiledMapObjectNames.TORCH: //  Torch entity
                        if (rectObject.getProperties().containsKey(TiledMapProperties.TORCH_ON)) {
                            boolean on = (boolean) rectObject.getProperties().get(TiledMapProperties.TORCH_ON);
                            Torch torch = new Torch(rectObject.getRectangle(), on, rectObject.getName());
                            engine.addEntity(torch);
                        } else {
                            System.err.println("TORCH: " + rectObject.getName() + " was not setup correctly!");
                        }
                        break;

                    case TiledMapObjectNames.PILLAR:    //  Pillar entity
                        engine.addEntity(new Pillar(rectObject.getRectangle(), rectObject.getName()));
                        break;

                    case TiledMapObjectNames.PUZZLE_CONTROLLER:
                        puzzleName = (String) mapObject.getProperties().get(TiledMapProperties.PUZZLE_NAME);

                        PuzzleController pController = new PuzzleController(
                                rectObject.getRectangle(),mapObject.getName());
                        pController.setParentPuzzle(engine.getPuzzle(puzzleName));
                        engine.addEntity(pController);
                        break;

                    case TiledMapObjectNames.PUZZLE_SS_SWITCH:
                        puzzleName = (String) mapObject.getProperties().get(TiledMapProperties.PUZZLE_NAME);

                        SimonSaysSwitch saysSwitch = new SimonSaysSwitch(
                                rectObject.getRectangle(),mapObject.getName());
                        saysSwitch.setParentPuzzle(engine.getPuzzle(puzzleName));
                        engine.addEntity(saysSwitch);
                        break;

                    case TiledMapObjectNames.LEVER:
                        if (rectObject.getProperties().containsKey(TiledMapProperties.TARGET)){
                            Lever lever = new Lever(rectObject.getRectangle(), rectObject.getName());
                            engine.addEntity(lever);
                            lever.setTrigger(trigger);
                            lever.setTargets(targets);
                        } else {
                            System.err.println("LEVER: " + rectObject.getName() + " was not setup correctly!");
                        }
                        break;

                    case TiledMapObjectNames.QUICKSAND:
                        if (mapObject.getProperties().containsKey(TiledMapProperties.SMOD)){
                            String room = (String) mapObject.getProperties().get(TiledMapProperties.ROOM);
                            ChainShape shape = createPolyLine(((PolylineMapObject)mapObject));
                            Quicksand quicksand = new Quicksand(shape, mapObject.getName(),
                                    (float)mapObject.getProperties().get(TiledMapProperties.SMOD),
                                    (float) mapObject.getProperties().get(TiledMapProperties.TIMER));
                            List<Entity> trapRooms = engine.getEntities(TrapRoomComponent.class);

                            for (Entity entity : trapRooms) {
                                if (entity instanceof TrapRoom && entity.getName().equals(room)) {
                                    ((TrapRoom) entity).addTrap(quicksand);
                                    engine.addEntity(quicksand);
                                    engine.addEntity(quicksand);
                                }
                            }
                        } else {
                            System.err.println("QUICKSAND: " + rectObject.getName() + " was not setup correctly!");
                        }
                        break;

                    case TiledMapObjectNames.WALL_SCRIBE:
                        puzzleName = (String) mapObject.getProperties().get(TiledMapProperties.PUZZLE_NAME);

                        WallScribe wallScribe = new WallScribe(
                                rectObject.getRectangle(),mapObject.getName());
                        wallScribe.setParentPuzzle(engine.getPuzzle(puzzleName));
                        engine.addEntity(wallScribe);
                        break;

                    case TiledMapObjectNames.PRESSURE_PLATE:
                        if (rectObject.getProperties().containsKey(TiledMapProperties.ACTIVETIME) &&
                                rectObject.getProperties().containsKey(TiledMapProperties.AUTOCLOSE)){
                            String room = (String) mapObject.getProperties().get(TiledMapProperties.ROOM);
                            PressurePlate pressurePlate = new PressurePlate(rectObject.getRectangle(), rectObject.getName(),
                                    (float)mapObject.getProperties().get(TiledMapProperties.ACTIVETIME),
                                    (boolean)rectObject.getProperties().get(TiledMapProperties.AUTOCLOSE));
                            List<Entity> trapRooms = engine.getEntities(TrapRoomComponent.class);

                            for (Entity entity : trapRooms) {
                                if (entity instanceof TrapRoom && entity.getName().equals(room)) {
                                    ((TrapRoom) entity).addTrap(pressurePlate);
                                    engine.addEntity(pressurePlate);
                                }
                            }
                            pressurePlate.setTrigger(trigger);
                            pressurePlate.setTargets(targets);
                        } else {
                            System.err.println("PRESSURE_PLATE PLATE: " + rectObject.getName() + " was not setup correctly!");
                        }
                        break;

                    case TiledMapObjectNames.COMPLETED_INDICATOR:
                        puzzleName = (String) mapObject.getProperties().get(TiledMapProperties.PUZZLE_NAME);
                        CompletedIndicator indicator = new CompletedIndicator(rectObject.getRectangle(), rectObject.getName());
                        engine.addEntity(indicator);
                        indicator.setParentPuzzle(engine.getPuzzle(puzzleName));
                        break;


                    case TiledMapObjectNames.BEAM:
                        if (rectObject.getProperties().containsKey(TiledMapProperties.DIRECTION)){
                            Beam beam = new Beam(rectObject.getRectangle(), rectObject.getName(),
                                    true, (String) rectObject.getProperties().get(TiledMapProperties.DIRECTION),true);
                            engine.addEntity(beam);
                        } else {
                            System.err.println("BEAM_HORIZONTAL: " + rectObject.getName() +  " was not setup correctly!");
                        }
                        break;

                    case TiledMapObjectNames.REFLECTOR:
                        if (rectObject.getProperties().containsKey(TiledMapProperties.DIRECTION)){
                            Beam beamOut = new Beam(rectObject.getRectangle(), mapObject.getName(),
                                    true,(String)mapObject.getProperties().get(TiledMapProperties.DIRECTION),true);
                            Reflector reflector = new Reflector(rectObject.getRectangle(), rectObject.getName(),
                                    (String) rectObject.getProperties().get(TiledMapProperties.DIRECTION), beamOut);
                            engine.addEntity(reflector);
                            engine.addEntity(beamOut);
                        } else {
                            System.err.println("BEAM_REFLECTOR_OFF: " + rectObject.getName() +  " was not setup correctly!");
                        }
                        break;

                    case TiledMapObjectNames.BEAM_GENERATOR:
                        if (rectObject.getProperties().containsKey(TiledMapProperties.DIRECTION)){
                            Beam beamOut = new Beam(rectObject.getRectangle(), mapObject.getName(),
                                    true,(String)mapObject.getProperties().get(TiledMapProperties.DIRECTION),true);
                            BeamGenerator generator = new BeamGenerator(rectObject.getRectangle(), rectObject.getName(),
                                    (String) rectObject.getProperties().get(TiledMapProperties.DIRECTION),beamOut);
                            engine.addEntity(generator);
                            engine.addEntity(beamOut);
                        } else {
                            System.err.println("BEAM_REFLECTOR_OFF: " + rectObject.getName() +  " was not setup correctly!");
                        }
                        break;

                    case TiledMapObjectNames.BEAM_TARGET:
                        puzzleName = (String)mapObject.getProperties().get(TiledMapProperties.PUZZLE_NAME);

                        BeamTarget beamTarget = new BeamTarget(rectObject.getRectangle(), rectObject.getName());
                        beamTarget.setParentPuzzle(engine.getPuzzle(puzzleName));
                        engine.addEntity(beamTarget);
                        break;

                    case  TiledMapObjectNames.FLOOR_PISTON:
                        boolean activated = (boolean) rectObject.getProperties().get(TiledMapProperties.ACTIVATED);
                        engine.addEntity(new FloorPiston(rectObject.getRectangle(), activated, rectObject.getName()));
                        break;

                    default:
                        //throw new IllegalArgumentException("This isn't a valid entity! " + type);
                }
            }
        }
    }

    public static void parseTiledRoomLayer (EntityEngine engine, MapObjects mapObjects) {
        if (mapObjects != null) {
            for (MapObject mapObject : mapObjects) {
                if (mapObject instanceof PolylineMapObject) {
                    String type = (String) mapObject.getProperties().get(TiledMapProperties.ENTITY_TYPE);

                    switch (type) {
                        case TiledMapObjectNames.TRAP_ROOM:
                            ChainShape shape = createPolyLine(((PolylineMapObject)mapObject));
                            TrapRoom room = new TrapRoom(shape, mapObject.getName());
                            engine.addEntity(room);
                    }
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
            fixtureDef.filter.categoryBits = CollisionUtil.BOUNDARY;
            fixtureDef.filter.maskBits = (byte) (CollisionUtil.ENTITY | CollisionUtil.PUZZLE_AREA);

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
            fixtureDef.filter.categoryBits = CollisionUtil.PUZZLE_AREA;
            fixtureDef.filter.maskBits = (byte) (CollisionUtil.ENTITY | CollisionUtil.BOUNDARY);
            body.createFixture(fixtureDef);
            shape.dispose();
        }
    }

    public static void parseTiledPuzzleLayer (EntityEngine engine, MapObjects mapObjects) {
        if (mapObjects != null) {
            for (MapObject mapObject : mapObjects) {
                if (!mapObject.getProperties().containsKey(TiledMapProperties.PUZZLE_TYPE)) {
                    continue;
                }
                String type = (String) mapObject.getProperties().get(TiledMapProperties.PUZZLE_TYPE);

                //  If the entity has any targets to 'trigger'
                List<String> activateTargets = new ArrayList<>();
                List<String> completeTargets = new ArrayList<>();
                List<String> failTargets = new ArrayList<>();
                if (mapObject.getProperties().containsKey(TiledMapProperties.TARGET_ACTIVATE)) {
                    activateTargets = parseTargets(mapObject, TiledMapProperties.TARGET_ACTIVATE);
                }
                if (mapObject.getProperties().containsKey(TiledMapProperties.TARGET_COMPLETE)) {
                    completeTargets = parseTargets(mapObject, TiledMapProperties.TARGET_COMPLETE);
                }
                if (mapObject.getProperties().containsKey(TiledMapProperties.TARGET_FAIL)) {
                    failTargets = parseTargets(mapObject, TiledMapProperties.TARGET_FAIL);
                }

                //  Retrieves the type of entity specified in the tiled map
                switch (type) {
                    case TiledMapPuzzleNames.LEVEL_PUZZLE:
                        LevelPuzzle levelPuzzle = new LevelPuzzle(mapObject.getName());
                        levelPuzzle.setActivateTargets(activateTargets);
                        levelPuzzle.setCompleteTargets(completeTargets);
                        levelPuzzle.setFailTargets(failTargets);
                        engine.addPuzzle(levelPuzzle);
                        break;

                    case TiledMapPuzzleNames.SIMON_SAYS:
                        try {
                            String name = mapObject.getName();
                            int difficulty = (int) mapObject.getProperties().get(TiledMapProperties.DIFFICULTY);
                            int maxAttempts = (int) mapObject.getProperties().get(TiledMapProperties.MAX_ATTEMPTS);
                            int numStages = (int) mapObject.getProperties().get(TiledMapProperties.STAGES);

                            SimonSays simonSays = new SimonSays(name, difficulty, maxAttempts, numStages);
                            simonSays.setActivateTargets(activateTargets);
                            simonSays.setCompleteTargets(completeTargets);
                            simonSays.setFailTargets(failTargets);
                            engine.addPuzzle(simonSays);
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                        break;
                    case TiledMapPuzzleNames.COLOUR_GEMS:
                        String name = mapObject.getName();
                        int maxAttempts = (int) mapObject.getProperties().get(TiledMapProperties.MAX_ATTEMPTS);
                        int numGems = (int) mapObject.getProperties().get(TiledMapProperties.NUM_GEMS);

                        ArrayList<String> chests;
                        chests = parseTargets(mapObject, TiledMapProperties.CHESTS);

                        ColouredGemsPuzzle colourGems = new ColouredGemsPuzzle(name, numGems, maxAttempts);
                        colourGems.setActivateTargets(activateTargets);
                        colourGems.setCompleteTargets(completeTargets);
                        colourGems.setFailTargets(failTargets);
                        colourGems.setChests(chests);
                        engine.addPuzzle(colourGems);
                        break;
                    case TiledMapPuzzleNames.BOX_PUZZLE:
                        ArrayList<String> boxes;
                        boxes = parseTargets(mapObject, TiledMapProperties.BOXES);

                        BoxPuzzle boxPuzzle = new BoxPuzzle(mapObject.getName());
                        boxPuzzle.setActivateTargets(activateTargets);
                        boxPuzzle.setCompleteTargets(completeTargets);
                        boxPuzzle.setFailTargets(failTargets);
                        boxPuzzle.setBoxNames(boxes);
                        engine.addPuzzle(boxPuzzle);
                        break;

                    case TiledMapPuzzleNames.BEAM_PUZZLE:
                        BeamPuzzle beamPuzzle = new BeamPuzzle(mapObject.getName());

                        beamPuzzle.setCompleteTargets(completeTargets);
                        engine.addPuzzle(beamPuzzle);
                }
            }
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
    public static ArrayList<String> parseTargets(MapObject mapObject, String property) {
        String targetString = (String)mapObject.getProperties().get(property);
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

    /**
     * Used to add the target entities into the current entity.
     * @param engine Engine to go through all entities
     * @param puzzle Current entity
     */
    public static void parseTargets(EntityEngine engine, Puzzle puzzle) {
        List<Entity> activateEntities = new ArrayList<>();
        for (String target : puzzle.getActivateTargets()) {
            for (Entity entity : engine.getEntities()) {
                if (entity.getName().equals(target)) {
                    activateEntities.add(entity);
                }
            }
        }
        puzzle.setOnActivateEntities(activateEntities);

        List<Entity> completeEntities = new ArrayList<>();
        for (String target : puzzle.getCompleteTargets()) {
            for (Entity entity : engine.getEntities()) {
                if (entity.getName().equals(target)) {
                    completeEntities.add(entity);
                }
            }
        }
        puzzle.setOnCompleteEntities(completeEntities);

        List<Entity> failEntities = new ArrayList<>();
        for (String target : puzzle.getFailTargets()) {
            for (Entity entity : engine.getEntities()) {
                if (entity.getName().equals(target)) {
                    failEntities.add(entity);
                }
            }
        }
        puzzle.setOnFailEntities(failEntities);
    }
}