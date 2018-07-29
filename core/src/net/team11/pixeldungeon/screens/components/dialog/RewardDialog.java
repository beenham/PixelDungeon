package net.team11.pixeldungeon.screens.components.dialog;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.components.coin.CoinDisplay;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;

public class RewardDialog extends Table {
    private boolean debug = false;

    private Table buttonTable;
    private Table contentTable;
    private TextButton watchAnother;

    private int amount;

    public RewardDialog(int amount) {
        super();
        this.amount = amount;
        setDebug(debug);
        setBackground(new NinePatchDrawable(new NinePatch(Assets.getInstance().getTextureSet(Assets.HUD).createPatch(AssetName.UI_SCROLLPANE))));
        float padding = 25 * PixelDungeon.SCALAR;

        setupButtonTable();
        add(setupContentTable()).pad(padding);
        add(buttonTable).pad(padding);
        setSize(PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        pack();
        setPosition(PixelDungeon.V_WIDTH/2 - getWidth()/2,
                PixelDungeon.V_HEIGHT/2 - getHeight()/2);
    }

    private Table setupContentTable() {
        Image image = new Image(Assets.getInstance().getTextureSet(Assets.BLOCKS)
                .findRegion(AssetName.LOCKED_CHEST_OPENED));

        contentTable = new Table();
        contentTable.add(image).size(buttonTable.getPrefHeight()).fill();
        contentTable.setDebug(debug);
        return contentTable;
    }

    private void setupButtonTable() {
        float fontSize = PixelDungeon.SCALAR * 1.25f;
        float buttonFontSize = PixelDungeon.SCALAR * .75f;
        float padding = 50f * PixelDungeon.SCALAR;

        Label title = new Label(Messages.AD_CONGRATS,
                Assets.getInstance().getSkin(Assets.UI_SKIN),"title");
        title.setFontScale(fontSize);

        Label text = new Label(Messages.AD_THANKS,
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        text.setFontScale(buttonFontSize);
        text.setWrap(true);

        TextButton doneButton = new TextButton(Messages.AD_DONE,
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        doneButton.getLabel().setFontScale(buttonFontSize);
        doneButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                close();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        watchAnother = new TextButton(Messages.AD_REWATCH,
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        watchAnother.getLabel().setFontScale(buttonFontSize);
        watchAnother.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PixelDungeon.getInstance().getAndroidInterface().showRewardAd();
                close();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        buttonTable = new Table();
        buttonTable.add(title).colspan(2).left();
        buttonTable.row();
        buttonTable.add(text).colspan(2).left().fill().pad(padding,0,padding,padding);
        buttonTable.row();
        buttonTable.add(new CoinDisplay(title.getPrefHeight(),amount)).left().padLeft(padding);
        buttonTable.row();
        buttonTable.add(doneButton).pad(padding,0,padding,padding).left().expandX();
        buttonTable.add(watchAnother).pad(padding).right().fill();
        buttonTable.setDebug(debug);
    }

    private void close() {
        remove();
        ScreenManager.getInstance().getScreen().resume();
    }

    public void show(Stage stage) {
        stage.addActor(this);
        ScreenManager.getInstance().getScreen().pause();
    }
}
