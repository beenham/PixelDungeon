package net.team11.pixeldungeon.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.system.AnimationSystem;
import net.team11.pixeldungeon.entity.system.CameraSystem;
import net.team11.pixeldungeon.entity.system.HealthSystem;
import net.team11.pixeldungeon.entity.system.InteractionSystem;
import net.team11.pixeldungeon.entity.system.PlayerMovementSystem;
import net.team11.pixeldungeon.entity.system.RenderSystem;
import net.team11.pixeldungeon.entity.system.TrapSystem;
import net.team11.pixeldungeon.entity.system.VelocitySystem;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.utils.TiledMapLayers;
import net.team11.pixeldungeon.utils.TiledMapObjectNames;
import net.team11.pixeldungeon.uicomponents.Controller;
import net.team11.pixeldungeon.map.MapManager;

import box2dLight.RayHandler;


public class PlayScreen implements Screen {
    public static PixelDungeon game;
    public static OrthographicCamera gameCam;
    public static RayHandler rayHandler;
    private FitViewport gamePort;
    private Controller controller;
    private MapManager mapManager;

    private EntityEngine engine;
    private PlayerMovementSystem playerMovementSystem;

    public static World world;
    private Box2DDebugRenderer b2dr;
    private Player player;

    public PlayScreen(PixelDungeon game, String mapName) {
        this.game = game;
        this.controller = new Controller(game.batch);
        setupCamera();
        setupViewport();
        setupEngine();
        setupLight();
        setupPlayer(mapName);
        mapManager.loadMap(mapName);
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
        this.mapManager.reset();
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
        engine.addSystem(new HealthSystem());
        engine.addSystem(new InteractionSystem());
        engine.addSystem(new TrapSystem());

        mapManager.setEngine(engine);
        playerMovementSystem.setController(controller);
    }

    private void setupPlayer(String mapName) {
        RectangleMapObject mapObject = mapManager.getMap(mapName).getRectangleObject(TiledMapLayers.POINTS_LAYER, TiledMapObjectNames.SPAWN_POINT);
        float posX = mapObject.getRectangle().getX();
        float posY = mapObject.getRectangle().getY();
        engine.addEntity(player = new Player(posX, posY));
    }

    private void setupLight() {
        world = new World(new Vector2(0, 0), false);
        b2dr = new Box2DDebugRenderer();
        rayHandler = new RayHandler(world, PixelDungeon.V_WIDTH/32, PixelDungeon.V_HEIGHT/32);
        rayHandler.setCombinedMatrix(gameCam);
        rayHandler.setShadows(true);
        rayHandler.setAmbientLight(0.75f);
        rayHandler.getLightMapTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    private void handleInput(float deltaTime){
        playerMovementSystem.update(deltaTime);
        player.getComponent(VelocityComponent.class).setMovementSpeed(100);
    }

    public void update(float deltaTime) {
        handleInput(deltaTime);
        gameCam.update();
        rayHandler.update();
        rayHandler.setCombinedMatrix(gameCam);
        game.batch.setProjectionMatrix(gameCam.combined);
        world.step(1 / 60f, 6, 2);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(88,61,52,0);
        engine.update(delta);

        b2dr.render(world, gameCam.combined);
        rayHandler.render();

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
        b2dr.dispose();
        world.dispose();
        rayHandler.dispose();
    }
}
