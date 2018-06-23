package net.team11.pixeldungeon.uicomponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionFade;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;

public class PauseMenu extends Stage {
    private ShapeRenderer shapeRenderer;
    private EntityEngine engine;

    private boolean visible = false;
    private boolean resumePressed;

    private Table pauseTable;

    public PauseMenu(SpriteBatch spriteBatch, EntityEngine engine) {
        super(new FitViewport(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT, new OrthographicCamera()), spriteBatch);
        shapeRenderer = new ShapeRenderer();
        this.engine = engine;
        setupTable();
        addActor(pauseTable);
    }

    private void setupTable() {
        float padding = 25 * PixelDungeon.SCALAR;

        Label titleLabel = new Label(Messages.GAME_PAUSED_UPPER, new Label.LabelStyle(Assets.getInstance().getFont(Assets.P_FONT), Color.WHITE));
        titleLabel.setFontScale(1.5f * PixelDungeon.SCALAR);

        TextButton resumeButton = new TextButton(Messages.RESUME, Assets.getInstance().getSkin(Assets.UI_SKIN));
        resumeButton.getLabel().setFontScale(PixelDungeon.SCALAR);
        resumeButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return resumePressed = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                resumePressed = false;
            }
        });

        TextButton mainMenuButton = new TextButton(Messages.QUIT_TO_MAIN, Assets.getInstance().getSkin(Assets.UI_SKIN));
        mainMenuButton.getLabel().setFontScale(PixelDungeon.SCALAR);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getActors().removeIndex(0);
                setupDialog();
                addActor(pauseTable);
                return true;
            }
        });

        pauseTable = new Table();
        pauseTable.add(titleLabel).pad(padding * 3);
        pauseTable.row().pad(padding);
        pauseTable.add(resumeButton).pad(padding);
        pauseTable.row().pad(padding);
        pauseTable.add(mainMenuButton).pad(padding);
        pauseTable.pack();

        pauseTable.setPosition(PixelDungeon.V_WIDTH/2 - pauseTable.getWidth()/2,PixelDungeon.V_HEIGHT/2 - pauseTable.getHeight()/2);
    }

    private void setupDialog() {
        float padding = 25 * PixelDungeon.SCALAR;

        Label titleLabel = new Label(Messages.ARE_YOU_SURE, new Label.LabelStyle(Assets.getInstance().getFont(Assets.P_FONT), Color.WHITE));
        titleLabel.setFontScale(1.5f * PixelDungeon.SCALAR);

        Label infoLabel = new Label(Messages.LEVEL_LEAVING, Assets.getInstance().getSkin(Assets.UI_SKIN));
        infoLabel.setWrap(true);
        infoLabel.setFontScale(PixelDungeon.SCALAR);
        infoLabel.setAlignment(Align.center);

        TextButton yesButton = new TextButton(Messages.YES, Assets.getInstance().getSkin(Assets.UI_SKIN));
        yesButton.getLabel().setFontScale(PixelDungeon.SCALAR);
        yesButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                engine.finish();
                ScreenManager.getInstance().changeScreen(ScreenEnum.MAIN_MENU,
                        ScreenTransitionFade.init(1f));
                return true;
            }
        });

        TextButton noButton = new TextButton(Messages.NO, Assets.getInstance().getSkin(Assets.UI_SKIN));
        noButton.getLabel().setFontScale(PixelDungeon.SCALAR);
        noButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getActors().removeIndex(0);
                setupTable();
                addActor(pauseTable);
                return true;
            }
        });

        pauseTable = new Table();
        pauseTable.add(titleLabel).colspan(5).pad(padding,padding*7,padding,padding*7);
        pauseTable.row().pad(padding);
        pauseTable.add(infoLabel).colspan(5).padBottom(padding*4).fillX().expandX();
        pauseTable.row();
        pauseTable.add().pad(padding);
        pauseTable.add(yesButton).pad(padding).size(yesButton.getPrefWidth()*2,yesButton.getPrefHeight()).right();
        pauseTable.add().pad(padding/2);
        pauseTable.add(noButton).pad(padding).size(yesButton.getPrefWidth()*2,yesButton.getPrefHeight()).left();
        pauseTable.add().pad(padding);
        pauseTable.setSize(PixelDungeon.V_WIDTH*4/5,PixelDungeon.V_HEIGHT*4/5);

        pauseTable.setPosition(PixelDungeon.V_WIDTH/2 - pauseTable.getWidth()/2,PixelDungeon.V_HEIGHT/2 - pauseTable.getHeight()/2);
    }

    public void setVisible(boolean visible) {
        resumePressed = false;
        this.visible = visible;
        if (visible) {
            Gdx.input.setInputProcessor(this);
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isResumePressed() {
        return resumePressed;
    }

    @Override
    public void draw() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0,0,0,0.60f));
        shapeRenderer.rect(0,0,PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        super.draw();
    }
}
