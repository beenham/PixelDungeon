package net.team11.pixeldungeon.screens.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import net.team11.pixeldungeon.screens.components.dialog.RewardDialog;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionFade;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionPush;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionSplit;
import net.team11.pixeldungeon.utils.Util;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;

public class MainMenuScreen extends AbstractScreen {
    private float padding;
    private boolean rewardAdded;

    @Override
    public void buildStage() {
        padding = 25 * PixelDungeon.SCALAR;
        rewardAdded = false;
        Util.getInstance().getStatsUtil().clearCurrStats();

        addActor(setupBackground());

        addActor(setupMainTable(padding));
        addActor(setupTitleTable(padding));
        addActor(setupTopRightTable(padding));
        addActor(setupBottomRightTable(padding));
        //addActor(setupBottomLeftTable(padding));
        if (PixelDungeon.getInstance().getAndroidInterface().isRewardAvailable()) {
            addActor(setupBottomLeftTable(padding));
        }
    }

    private Table setupMainTable(float padding) {
        Table mainTable = new Table();
        mainTable.center().pad(padding);

        TextButton playButton = new TextButton(Messages.PLAY_UPPER,Assets.getInstance().getSkin(Assets.UI_SKIN));
        playButton.getLabel().setFontScale(1.25f * PixelDungeon.SCALAR);

        playButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!paused) {
                    ScreenManager.getInstance().changeScreen(ScreenEnum.LEVEL_SELECT,
                            ScreenTransitionSplit.init(1.5f, true, Interpolation.pow2));
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        TextButton skinButton = new TextButton(Messages.SKIN_SELECT, Assets.getInstance().getSkin(Assets.UI_SKIN));
        skinButton.getLabel().setFontScale(1.25f * PixelDungeon.SCALAR);

        skinButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!paused) {
                    ScreenManager.getInstance().changeScreen(ScreenEnum.SKIN_SELECT,
                            ScreenTransitionPush.init(1.5f, ScreenTransitionPush.LEFT, Interpolation.pow2));
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        mainTable.add(playButton).pad(padding);
        mainTable.row();
        //mainTable.add(skinButton).pad(padding);
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
                if (!paused) {
                    ScreenManager.getInstance().changeScreen(ScreenEnum.PLAYER_INFO,
                            ScreenTransitionFade.init(0.25f));
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        trTable.add(playerButton).size(playerButton.getWidth(),playerButton.getHeight());
        trTable.setPosition(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT);
        return trTable;
    }

    private Table setupBottomRightTable(float padding) {
        Table brTable = new Table();
        brTable.bottom().padBottom(padding*3).right().padRight(padding*3);

        Label label = new Label(PixelDungeon.VERSION,
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        label.setFontScale(0.75f * PixelDungeon.SCALAR);
        brTable.add(label);

        brTable.setPosition(PixelDungeon.V_WIDTH,0);
        return brTable;
    }

    private Table setupTopLeftTable(float padding) {
        Table blTable = new Table();
        blTable.bottom().padBottom(padding).left().padLeft(padding);

        Label label = new Label("BL",
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        label.setFontScale(1.2f * PixelDungeon.SCALAR);
        blTable.add(label);

        blTable.setDebug(true);
        return blTable;
    }

    private Table setupBottomLeftTable(float padding) {
        rewardAdded = true;
        final Table tlTable = new Table();
        tlTable.bottom().padBottom(padding*3).left().padLeft(padding*3);

        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.HUD);
        Image coinButton = new Image(textureAtlas.findRegion(AssetName.COIN_BUTTON));
        coinButton.setSize(coinButton.getWidth() * 1.5f * (PixelDungeon.SCALAR),
                coinButton.getHeight() * 1.5f * (PixelDungeon.SCALAR));
        coinButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!paused) {
                    PixelDungeon.getInstance().getAndroidInterface().showRewardAd();
                    rewardAdded = false;
                    tlTable.remove();
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        tlTable.add(coinButton).size(coinButton.getWidth(),coinButton.getHeight());

        return tlTable;
    }

    private Image setupBackground() {
        Image backgroundImage = new Image(
                Assets.getInstance().getTextureSet(Assets.BACKGROUND).findRegion(ScreenEnum.MAIN_MENU.toString())
        );
        backgroundImage.setSize(PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        return backgroundImage;
    }

    private void update() {
        if (!rewardAdded && PixelDungeon.getInstance().getAndroidInterface().isRewardAvailable()) {
            addActor(setupBottomLeftTable(padding));
        }
    }

    @Override
    public void resume() {
        for (Actor actor : getActors()) {
            if (actor instanceof RewardDialog) {
                return;
            }
        }
        super.resume();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }
}
