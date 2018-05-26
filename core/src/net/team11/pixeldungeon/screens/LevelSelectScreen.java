package net.team11.pixeldungeon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.screens.components.LevelInfo;
import net.team11.pixeldungeon.screens.components.LevelSelector;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionSplit;
import net.team11.pixeldungeon.utils.Assets;

public class LevelSelectScreen extends AbstractScreen {
    private LevelSelector selector;
    private LevelInfo info;

    @Override
    public void buildStage() {
        addActor(setupLayout());
    }

    private Table setupLayout() {
        Table layout = new Table();
        layout.setBounds(0,0,PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        layout.setDebug(true);
        layout.add(setupLeft());
        layout.add(setupRight());
        return layout;
    }

    private Table setupLeft() {
        Table table = new Table();
        Button backButton = new TextButton("BACK", Assets.getInstance().getSkin(Assets.UI_SKIN));
        ((TextButton)backButton).getLabel().setFontScale(1f * PixelDungeon.SCALAR);
        backButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().changeScreen(ScreenEnum.MAIN_MENU,
                        ScreenTransitionSplit.init(1f,ScreenTransitionSplit.HORIZONTAL, false,Interpolation.pow2));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        table.add(backButton).left().pad(20 * PixelDungeon.SCALAR);
        table.row().pad(20 * PixelDungeon.SCALAR);
        table.add(selector = new LevelSelector(PixelDungeon.V_WIDTH / 5 * 3)).pad(20 * PixelDungeon.SCALAR);
        return table;
    }

    private Table setupRight() {
        Table table = new Table();
        table.add(info = new LevelInfo(selector));
        return table;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0,0,0,1);

        //update();
        draw();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }
}
