package net.team11.pixeldungeon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.screens.components.LevelInfo;
import net.team11.pixeldungeon.screens.components.LevelSelector;
import net.team11.pixeldungeon.utils.Assets;

public class LevelSelectScreen extends AbstractScreen {
    private LevelSelector selector;
    private LevelInfo info;

    @Override
    public void buildStage() {
        addActor(setupBackground());
        addActor(setupLeft());
        addActor(setupRight());
    }

    private Image setupBackground() {
        Image backgroundImage = new Image(
                Assets.getInstance().getTextureSet(Assets.BACKGROUND).
                        findRegion(ScreenEnum.LEVEL_SELECT.toString())
        );
        backgroundImage.setSize(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT);
        return backgroundImage;
    }

    private Table setupLeft() {
        selector = new LevelSelector(PixelDungeon.V_WIDTH / 5 * 3);
        selector.setBounds(0,0,PixelDungeon.V_WIDTH/3*2, PixelDungeon.V_HEIGHT);
        return selector;
    }

    private WidgetGroup setupRight() {
        info = new LevelInfo(selector);
        info.setBounds(selector.getWidth(),0,PixelDungeon.V_WIDTH/3, PixelDungeon.V_HEIGHT);
        return info;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0,0,0,1);
        info.update();
        draw();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }
}
