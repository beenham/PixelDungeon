package net.team11.pixeldungeon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.team11.pixeldungeon.PixelDungeon;

public abstract class AbstractScreen extends Stage implements Screen {
    protected PixelDungeon game = PixelDungeon.getInstance();
    protected static Viewport viewport;
    public static OrthographicCamera gameCam;

    protected boolean gameCall = false;
    protected boolean paused;

    protected AbstractScreen() {
        super(viewport = new FitViewport(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT, gameCam = new OrthographicCamera()));
        paused = true;
    }

    public abstract void buildStage();

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0,0,0,0);
        super.act(delta);
        super.draw();
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width,height,true);
        PixelDungeon.V_WIDTH = width;
        PixelDungeon.V_HEIGHT = height;
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {}

    public void setGameCall(boolean gameCall) {
        this.gameCall = gameCall;
    }

    public abstract InputProcessor getInputProcessor();
}
