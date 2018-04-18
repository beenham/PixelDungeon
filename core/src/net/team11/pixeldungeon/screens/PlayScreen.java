package net.team11.pixeldungeon.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.components.Controller;
import net.team11.pixeldungeon.scenes.Hud;

public class PlayScreen implements Screen {
    private PixelDungeon game;

    private OrthographicCamera gameCam;
    private ScreenViewport gamePort;
    private Hud hud;
    private Controller controller;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private Vector3 touchPoint = new Vector3();

    private float viewportWidth;
    private float viewportHeight;

    public PlayScreen(PixelDungeon game) {
        this.game = game;

        gameCam = new OrthographicCamera();
        gamePort = new ScreenViewport(gameCam);
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("openMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        controller = new Controller(game.batch);

        viewportWidth = gamePort.getWorldWidth();
        viewportHeight = gamePort.getWorldHeight();
        gameCam.position.set(gamePort.getWorldWidth()/2 + 50, gamePort.getWorldHeight()/2 + 50, 0);
        gameCam.zoom = 0.1f;
    }

    @Override
    public void show() {

    }

    public void handleInput(float deltaTime){
        /*
        if (Gdx.input.isTouched()) {
            if (Gdx.input.getGyroscopeX() > viewportWidth/2)
                gameCam.position.x += 5 * deltaTime;
            else
                gameCam.position.x += 5 * deltaTime;
                gameCam.position.y += 5 * deltaTime;
        }
        */
        int moveAmount = 50;
        if (controller.isRightPressed())
            gameCam.position.x += moveAmount * deltaTime;
        if (controller.isUpPressed())
            gameCam.position.y += moveAmount * deltaTime;
        if (controller.isLeftPressed())
            gameCam.position.x -= moveAmount * deltaTime;
        if (controller.isDownPressed())
            gameCam.position.y -= moveAmount * deltaTime;
    }

    public void update(float deltaTime) {
        handleInput(deltaTime);
        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,255);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        //hud.stage.draw();

        if(Gdx.app.getType() == Application.ApplicationType.Android)
            controller.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
