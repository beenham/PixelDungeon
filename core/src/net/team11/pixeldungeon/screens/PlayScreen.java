package net.team11.pixeldungeon.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.entities.door.Door;
import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entity.component.PositionComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.system.AnimationSystem;
import net.team11.pixeldungeon.entity.system.CameraSystem;
import net.team11.pixeldungeon.entity.system.PlayerMovementSystem;
import net.team11.pixeldungeon.entity.system.RenderSystem;
import net.team11.pixeldungeon.entity.system.VelocitySystem;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.options.TiledMapLayers;
import net.team11.pixeldungeon.options.TiledMapObjectNames;
import net.team11.pixeldungeon.options.TiledMapProperties;
import net.team11.pixeldungeon.uicomponents.Controller;
import net.team11.pixeldungeon.map.MapManager;

public class PlayScreen implements Screen {
    private PixelDungeon game;

    public static OrthographicCamera gameCam;
    private FitViewport gamePort;
    private Controller controller;
    private MapManager mapManager;

    private EntityEngine engine;
    private PlayerMovementSystem playerMovementSystem;

    private Player player;

    public PlayScreen(PixelDungeon game) {
        this.game = game;
        this.controller = new Controller(game.batch);
        setupCamera();
        setupViewport();
        setupEngine();
        setupPlayer();
        setupEntities();
        engine.resume();
    }

    @Override
    public void show() {
        engine.resume();
    }

    private void setupCamera() {
        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false, PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT);
        gameCam.zoom = 0.1f;
        this.mapManager = MapManager.getInstance();
        gameCam.update();
    }

    private void setupViewport() {
        gamePort = new FitViewport(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT, gameCam);
        gamePort.setScreenPosition(PixelDungeon.V_WIDTH/2, PixelDungeon.V_HEIGHT/2);
    }

    private void setupEngine() {
        engine = new EntityEngine();
        engine.pause();
        engine.addSystem(new RenderSystem(game.batch));
        engine.addSystem(playerMovementSystem = new PlayerMovementSystem());
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new VelocitySystem());
        engine.addSystem(new CameraSystem());

        playerMovementSystem.setController(controller);
    }

    private void setupPlayer() {
        engine.addEntity(player = new Player());
        PositionComponent positionComponent = this.player.getComponent(PositionComponent.class);
        TextureMapObject mapObject = mapManager.getCurrentMap().getTextureObject(TiledMapLayers.POINTS_LAYER, TiledMapObjectNames.SPAWN_POINT);
        positionComponent.setY(mapObject.getY());
        positionComponent.setX(mapObject.getX());
    }

    private void setupEntities() {
        MapObjects mapObjects = mapManager.getCurrentMap().getObjects(TiledMapLayers.DOOR_LAYER);
        for (MapObject mapObject : mapObjects) {
            if (mapObject.getName().equals(TiledMapObjectNames.DOOR)) {
                RectangleMapObject door = (RectangleMapObject) mapObject;
                if (door.getProperties().containsKey(TiledMapProperties.DOOR_LOCKED)) {
                    boolean locked = (boolean) door.getProperties().get(TiledMapProperties.DOOR_LOCKED);
                    engine.addEntity(new Door(door.getRectangle(), locked, door.getName()));
                }
            }
            if (mapObject.getName().equals(TiledMapObjectNames.DOOR_PILLAR)) {
                RectangleMapObject door = (RectangleMapObject) mapObject;
                engine.addEntity(new Door(door.getRectangle(), true, door.getName()));
            }
        }
    }

    private void handleInput(float deltaTime){
        playerMovementSystem.update(deltaTime);
        player.getComponent(VelocityComponent.class).setMovementSpeed(100);
    }

    public void update(float deltaTime) {
        handleInput(deltaTime);
        gameCam.update();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,255);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gameCam.combined);
        engine.update(delta);

        if(Gdx.app.getType() == Application.ApplicationType.Android) {
            controller.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        controller.resize(width, height);
    }

    @Override
    public void pause() {
        engine.pause();
    }

    @Override
    public void resume() {
        engine.resume();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
