package net.team11.pixeldungeon.screens.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.utils.Assets;

import java.util.Locale;
public class LevelInfo extends Table {
    private LevelSelector selector;

    private Label levelName;
    private Label completedVal;
    private Label bestTimeVal;
    private Label chestsVal;
    private Label keysVal;
    private Label itemsVal;

    public LevelInfo(LevelSelector levelSelector) {
        this.selector = levelSelector;

        setupLayout();
        setBackground(new NinePatchDrawable(Assets.getInstance().getTextureSet(
                Assets.HUD).createPatch(AssetName.DARKEN_60)));
    }

    private void setupLayout() {
        setupTitle();
        row().fill();
        setupInfo();
        row().fill();
        setupPlay();
    }

    private void setupTitle() {
        levelName = new Label(selector.getMap().getMapName(), Assets.getInstance().getSkin(Assets.UI_SKIN),"title");
        levelName.setFontScale(1.25f * PixelDungeon.SCALAR);

        Table table = new Table();
        table.add(levelName).center().padTop(getHeight()/10);

        add(levelName).padTop(getHeight()/10).padBottom(getHeight()/20).expand();
    }

    private void setupInfo() {
        float padding = 30f * PixelDungeon.SCALAR;

        Label completed = new Label("Completed", Assets.getInstance().getSkin(Assets.UI_SKIN));
        completed.setFontScale(PixelDungeon.SCALAR);
        completedVal = new Label(String.format(Locale.UK,"%d",0), Assets.getInstance().getSkin(Assets.UI_SKIN));
        completedVal.setFontScale(PixelDungeon.SCALAR);
        Label bestTime = new Label("Best Time", Assets.getInstance().getSkin(Assets.UI_SKIN));
        bestTime.setFontScale(PixelDungeon.SCALAR);
        bestTimeVal = new Label(String.format(Locale.UK,"%02d:%02d",5,43), Assets.getInstance().getSkin(Assets.UI_SKIN));
        bestTimeVal.setFontScale(PixelDungeon.SCALAR);
        Label chests = new Label("Chests", Assets.getInstance().getSkin(Assets.UI_SKIN));
        chests.setFontScale(PixelDungeon.SCALAR);
        chestsVal = new Label(String.format(Locale.UK,"%d/%d",2,3), Assets.getInstance().getSkin(Assets.UI_SKIN));
        chestsVal.setFontScale(PixelDungeon.SCALAR);
        Label keys = new Label("Keys", Assets.getInstance().getSkin(Assets.UI_SKIN));
        keys.setFontScale(PixelDungeon.SCALAR);
        keysVal = new Label(String.format(Locale.UK,"%d/%d",1,1), Assets.getInstance().getSkin(Assets.UI_SKIN));
        keysVal.setFontScale(PixelDungeon.SCALAR);
        Label items = new Label("Items", Assets.getInstance().getSkin(Assets.UI_SKIN));
        items.setFontScale(PixelDungeon.SCALAR);
        itemsVal = new Label(String.format(Locale.UK,"%d/%d",0,1), Assets.getInstance().getSkin(Assets.UI_SKIN));
        itemsVal.setFontScale(PixelDungeon.SCALAR);

        Table table = new Table();

        table.add(completed).left().expandX();
        table.add(completedVal).right();
        table.row().padTop(padding);
        table.add(bestTime).left();
        table.add(bestTimeVal).right();
        table.row().padTop(padding);
        table.add(chests).left();
        table.add(chestsVal).right();
        table.row().padTop(padding);
        table.add(keys).left();
        table.add(keysVal).right();
        table.row().padTop(padding);
        table.add(items).left();
        table.add(itemsVal).right();

        add(table).pad(PixelDungeon.V_HEIGHT/5,50f * PixelDungeon.SCALAR,
                0f,50f * PixelDungeon.SCALAR).expand();
    }

    private void setupPlay() {
        Button playButton = new TextButton("PLAY", Assets.getInstance().getSkin(Assets.UI_SKIN));
        ((TextButton)playButton).getLabel().setFontScale(1f * PixelDungeon.SCALAR);
        playButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().changeScreen(ScreenEnum.GAME,
                        null,
                        selector.getMap().getMapName());
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        Table table = new Table();
        table.add(playButton).top();

        add(table).padBottom(PixelDungeon.V_HEIGHT/10).pad(20f * PixelDungeon.SCALAR).expand();
    }

    public void update() {
        levelName.setText(selector.getMap().getMapName());
        completedVal.setText(String.format(Locale.UK,"%d",0));
        bestTimeVal.setText(String.format(Locale.UK,"%02d:%02d",5,43));
        chestsVal.setText(String.format(Locale.UK,"%d/%d",2,3));
        keysVal.setText(String.format(Locale.UK,"%d/%d",1,1));
        itemsVal.setText(String.format(Locale.UK,"%d/%d",0,1));
    }
}
