package net.team11.pixeldungeon.screens.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.utils.Assets;

public class LevelInfo extends Table {
    private ShapeRenderer shapeRenderer;
    private LevelSelector selector;

    private Label levelName;
    private Label completed;
    private Label bestTime;
    private Label chests;
    private Label keys;
    private Label items;

    public LevelInfo(LevelSelector levelSelector) {
        shapeRenderer = new ShapeRenderer();
        this.selector = levelSelector;
        setupLayout();
    }

    private void setupLayout() {
        float padding = 20f * PixelDungeon.SCALAR;
        levelName = new Label(selector.getMap().getMapName(), Assets.getInstance().getSkin(Assets.UI_SKIN),"title");
        completed = new Label("Completed: 0", Assets.getInstance().getSkin(Assets.UI_SKIN));
        bestTime = new Label("N / A", Assets.getInstance().getSkin(Assets.UI_SKIN));
        chests = new Label("Chests : 0 / 2", Assets.getInstance().getSkin(Assets.UI_SKIN));
        keys = new Label("Keys : 0 / 2", Assets.getInstance().getSkin(Assets.UI_SKIN));
        items = new Label("Items : 0 / 0", Assets.getInstance().getSkin(Assets.UI_SKIN));



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

        add(levelName);
        row().pad(padding);
        add(completed);
        row().pad(padding);
        add(bestTime);
        row().pad(padding);
        add(chests);
        row().pad(padding);
        add(keys);
        row().pad(padding);
        add(items);
        row().pad(padding);
        add(playButton);
    }

    private void update() {
        levelName.setText(selector.getMap().getMapName());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        update();
        super.draw(batch, parentAlpha);
    }
}
