package net.team11.pixeldungeon.screens.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.screens.AbstractScreen;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionFade;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionSplit;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;

public class MainMenuScreen extends AbstractScreen {
    private Image backgroundImage;
    private float panH = .2f * PixelDungeon.SCALAR;
    private float panV = .15f * PixelDungeon.SCALAR;

    @Override
    public void buildStage() {
        float padding = 25 * PixelDungeon.SCALAR;

        addActor(setupBackground());

        addActor(setupMainTable(padding));
        addActor(setupTitleTable(padding));
        addActor(setupTopRightTable(padding));
        //addActor(setupBottomRightTable(padding));
        //addActor(setupBottomLeftTable(padding));
        //addActor(setupTopLeftTable(padding));
    }

    private Table setupMainTable(float padding) {
        Table mainTable = new Table();
        mainTable.center().pad(padding);

        TextButton playButton = new TextButton(Messages.PLAY_UPPER,Assets.getInstance().getSkin(Assets.UI_SKIN));
        playButton.getLabel().setFontScale(1.25f * PixelDungeon.SCALAR);

        playButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().changeScreen(ScreenEnum.LEVEL_SELECT,
                        ScreenTransitionSplit.init(1.5f,true,Interpolation.pow2));
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        TextButton skinButton = new TextButton(Messages.SKIN_SELECT, Assets.getInstance().getSkin(Assets.UI_SKIN));
        skinButton.getLabel().setFontScale(1.25f * PixelDungeon.SCALAR);
        skinButton.setDisabled(true);

        /*
        skinButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().changeScreen(ScreenEnum.SKIN_SELECT,
                        ScreenTransitionPush.init(1.5f,ScreenTransitionPush.LEFT,Interpolation.pow2));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        //*/

        TextButton helpButton = new TextButton(Messages.HOW_TO_PLAY, Assets.getInstance().getSkin(Assets.UI_SKIN));
        helpButton.getLabel().setFontScale(1.25f * PixelDungeon.SCALAR);
        helpButton.setDisabled(true);

        /*
        helpButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().changeScreen(ScreenEnum.HOW_TO,
                        ScreenTransitionPush.init(1.5f,ScreenTransitionPush.RIGHT,Interpolation.pow2));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        //*/

        mainTable.add(playButton).pad(padding);
        mainTable.row();
        mainTable.add(skinButton).pad(padding);
        mainTable.row();
        mainTable.add(helpButton).pad(padding);
        mainTable.setPosition(PixelDungeon.V_WIDTH/2, PixelDungeon.V_HEIGHT/2);
        return mainTable;
    }

    private Table setupTitleTable(float padding) {
        Table titleTable = new Table();
        titleTable.top().padTop(padding);

        Label label = new Label(Messages.TITLE,Assets.getInstance().getSkin(Assets.UI_SKIN), "title");
        label.setFontScale(2f * PixelDungeon.SCALAR);
        titleTable.add(label);

        titleTable.setPosition(PixelDungeon.V_WIDTH/2,
                PixelDungeon.V_HEIGHT);
        return titleTable;
    }


    private Table setupTopRightTable(float padding) {
        Table trTable = new Table();
        trTable.top().padTop(padding*3).right().padRight(padding*3);

        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.HUD);
        Image playerButton = new Image(textureAtlas.findRegion(AssetName.PLAYER_ICON));
        playerButton.setSize(playerButton.getWidth()*10*PixelDungeon.SCALAR,
                playerButton.getHeight()*10*PixelDungeon.SCALAR);
        playerButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().changeScreen(ScreenEnum.PLAYER_INFO,
                        ScreenTransitionFade.init(0.25f));
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        trTable.add(playerButton).size(playerButton.getWidth(),playerButton.getHeight());
        trTable.setPosition(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT);
        return trTable;
    }

    private Table setupBottomRightTable(float padding) {
        Table brTable = new Table();
        brTable.bottom().padBottom(padding).right().padRight(padding);

        Label label = new Label("BR", new Label.LabelStyle(Assets.getInstance().getFont(Assets.P_FONT), Color.WHITE));
        label.setFontScale(1.2f * PixelDungeon.SCALAR);
        brTable.add(label);

        brTable.setPosition(PixelDungeon.V_WIDTH,0);
        brTable.setDebug(true);
        return brTable;
    }

    private Table setupBottomLeftTable(float padding) {
        Table blTable = new Table();
        blTable.bottom().padBottom(padding).left().padLeft(padding);

        Label label = new Label("BL", new Label.LabelStyle(Assets.getInstance().getFont(Assets.P_FONT), Color.WHITE));
        label.setFontScale(1.2f * PixelDungeon.SCALAR);
        blTable.add(label);

        blTable.setDebug(true);
        return blTable;
    }

    private Table setupTopLeftTable(float padding) {
        Table tlTable = new Table();
        tlTable.top().padTop(padding).left().padLeft(padding);

        Label label = new Label("TL", new Label.LabelStyle(Assets.getInstance().getFont(Assets.P_FONT), Color.WHITE));
        label.setFontScale(1.2f * PixelDungeon.SCALAR);
        tlTable.add(label);

        tlTable.setPosition(0,PixelDungeon.V_HEIGHT);
        tlTable.setDebug(true);
        return tlTable;
    }

    private Image setupBackground() {
        backgroundImage = new Image(
                Assets.getInstance().getTextureSet(Assets.BACKGROUND).findRegion(ScreenEnum.MAIN_MENU.toString())
        );
        backgroundImage.setSize(PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        return backgroundImage;
    }

    private void update() {
        if (backgroundImage.getX() > 0 || backgroundImage.getX() + backgroundImage.getWidth() < PixelDungeon.V_WIDTH) {
            panH *= -1;
        }
        if (backgroundImage.getY() > 0 || backgroundImage.getY() + backgroundImage.getHeight() < PixelDungeon.V_HEIGHT) {
            panV *= -1;
        }
        backgroundImage.setPosition(backgroundImage.getX() + panH, backgroundImage.getY() + panV);
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
