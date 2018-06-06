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
import net.team11.pixeldungeon.utils.TiledObjectUtil;

import java.util.Locale;
public class LevelInfo extends Table {
    private LevelSelector selector;

    private Label levelName;
    private Label attemptsVal;
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
        table.add(levelName).bottom().padTop(getHeight()/5);

        add(levelName).expand();
    }

    private void setupInfo() {
        float padding = 30f * PixelDungeon.SCALAR;
        float fontScale = PixelDungeon.SCALAR;

        Label attempts = new Label("Attempts", Assets.getInstance().getSkin(Assets.UI_SKIN));
        //attemptsVal = new Label(String.format(Locale.UK,"%d",0), Assets.getInstance().getSkin(Assets.UI_SKIN));
        attemptsVal = new Label(String.format(Locale.UK, "%d", selector.getMap().getLevelStatistics().getAttempts()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));

        Label completed = new Label("Completed", Assets.getInstance().getSkin(Assets.UI_SKIN));
        //completedVal = new Label(String.format(Locale.UK,"%d",0), Assets.getInstance().getSkin(Assets.UI_SKIN));
        completedVal = new Label(String.format(Locale.UK, "%s", selector.getMap().getLevelStatistics().isCompleted()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));

        Label bestTime = new Label("Best Time", Assets.getInstance().getSkin(Assets.UI_SKIN));
        //bestTimeVal = new Label(String.format(Locale.UK,"%02d:%02d",5,43), Assets.getInstance().getSkin(Assets.UI_SKIN));
        bestTimeVal = new Label(String.format(Locale.UK,"%s",selector.getMap().getLevelStatistics().getBestTime()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));

        Label chests = new Label("Chests", Assets.getInstance().getSkin(Assets.UI_SKIN));
        //chestsVal = new Label(String.format(Locale.UK,"%d/%d",2,3), Assets.getInstance().getSkin(Assets.UI_SKIN));
        chestsVal = new Label(String.format(Locale.UK,"%d/%d",selector.getMap().getLevelStatistics().getNumChests(),selector.getMap().getLevelStatistics().getTotalChests()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));

        Label keys = new Label("Keys", Assets.getInstance().getSkin(Assets.UI_SKIN));
        //keysVal = new Label(String.format(Locale.UK,"%d/%d",1,1), Assets.getInstance().getSkin(Assets.UI_SKIN));
        keysVal = new Label(String.format(Locale.UK,"%d/%d",selector.getMap().getLevelStatistics().getNumKeys(),selector.getMap().getLevelStatistics().getTotalKeys()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));

        Label items = new Label("Items", Assets.getInstance().getSkin(Assets.UI_SKIN));
        //itemsVal = new Label(String.format(Locale.UK,"%d/%d",0,1), Assets.getInstance().getSkin(Assets.UI_SKIN));
        itemsVal = new Label(String.format(Locale.UK,"%d/%d",selector.getMap().getLevelStatistics().getNumItems(),selector.getMap().getLevelStatistics().getTotalItems()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));

        attempts.setFontScale(fontScale);
        attemptsVal.setFontScale(fontScale);
        completed.setFontScale(fontScale);
        completedVal.setFontScale(fontScale);
        bestTime.setFontScale(fontScale);
        bestTimeVal.setFontScale(fontScale);
        chests.setFontScale(fontScale);
        chestsVal.setFontScale(fontScale);
        keys.setFontScale(fontScale);
        keysVal.setFontScale(fontScale);
        items.setFontScale(fontScale);
        itemsVal.setFontScale(fontScale);

        Table table = new Table();

        table.add(attempts).left().expandX();
        table.add(attemptsVal).right();
        table.row().padTop(padding);
        table.add(completed).left();
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

        add(table).pad(PixelDungeon.V_HEIGHT/10,padding,padding,padding).expand();
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
        attemptsVal.setText(String.format(Locale.UK,"%d",selector.getMap().getLevelStatistics().getAttempts()));
        completedVal.setText(String.format(Locale.UK,"%s",selector.getMap().getLevelStatistics().isCompleted()));
        bestTimeVal.setText(String.format(Locale.UK,"%02d:%02d",selector.getMap().getLevelStatistics().getNumChests(),selector.getMap().getLevelStatistics().getTotalChests()));
        chestsVal.setText(String.format(Locale.UK,"%d/%d",selector.getMap().getLevelStatistics().getNumChests(),selector.getMap().getLevelStatistics().getTotalChests()));
        keysVal.setText(String.format(Locale.UK,"%d/%d",selector.getMap().getLevelStatistics().getNumKeys(),selector.getMap().getLevelStatistics().getTotalKeys()));
        itemsVal.setText(String.format(Locale.UK,"%d/%d",selector.getMap().getLevelStatistics().getNumItems(),selector.getMap().getLevelStatistics().getTotalItems()));
    }
}
