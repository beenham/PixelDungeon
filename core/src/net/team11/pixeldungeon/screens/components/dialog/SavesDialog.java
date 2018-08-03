package net.team11.pixeldungeon.screens.components.dialog;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.saves.SaveGame;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;
import net.team11.pixeldungeon.utils.stats.GlobalStats;

import java.util.Locale;

public class SavesDialog extends Dialog {
    private SaveGame saveOne;
    private SaveGame saveTwo;
    private boolean debug = false;

    public SavesDialog(SaveGame saveOne, SaveGame saveTwo) {
        super();
        this.saveOne = saveOne;
        this.saveTwo = saveTwo;
        setupContentTable();
        setupButtonTable();
        setBackground(new NinePatchDrawable(new NinePatch(Assets.getInstance()
                .getTextureSet(Assets.HUD).createPatch(AssetName.DARKEN_80))));

        setDebug(debug);
        add(setupTitleLabel()).pad(padding*2,padding,padding,padding);
        row();
        add(contentTable).pad(padding);
        row();
        add(buttonTable).pad(padding);
        setSize(PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        setPosition(PixelDungeon.V_WIDTH/2 - getWidth()/2,
                PixelDungeon.V_HEIGHT/2 - getHeight()/2);
    }

    private Table setupTitleLabel() {
        float fontScale = PixelDungeon.SCALAR;

        Label title = new Label(Messages.SAVE_CONFLICT_TITLE, Assets.getInstance().getSkin(Assets.UI_SKIN));
        title.setFontScale(fontScale);
        Label message = new Label(Messages.SAVE_CONFLICT_MESSAGE, Assets.getInstance().getSkin(Assets.UI_SKIN));
        message.setFontScale(fontScale);

        Table table = new Table();
        table.setDebug(debug);
        table.add(title);
        table.row();
        table.add(message);
        return table;
    }

    @Override
    void setupContentTable() {
        ScrollPane saveOneData = new ScrollPane(getSaveTable(saveOne),
                Assets.getInstance().getSkin(Assets.UI_SKIN),"background");
        saveOneData.setScrollingDisabled(true,false);

        ScrollPane saveTwoData = new ScrollPane(getSaveTable(saveTwo),
                Assets.getInstance().getSkin(Assets.UI_SKIN),"background");
        saveTwoData.setScrollingDisabled(true,false);

        contentTable = new Table();
        contentTable.setDebug(debug);
        contentTable.add(saveOneData).pad(padding).fillX();
        contentTable.add(saveTwoData).pad(padding).fillX();
    }

    @Override
    void setupButtonTable() {
        float buttonFontSize = PixelDungeon.SCALAR * .75f;
        float padding = 50f * PixelDungeon.SCALAR;

        TextButton saveOneButton = new TextButton(Messages.USE_SAVE_ONE,
            Assets.getInstance().getSkin(Assets.UI_SKIN));
        saveOneButton.getLabel().setFontScale(buttonFontSize);
        saveOneButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PixelDungeon.getInstance().getAndroidInterface().overwriteSave(saveOne);
                close();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        TextButton saveTwoButton = new TextButton(Messages.USE_SAVE_TWO,
            Assets.getInstance().getSkin(Assets.UI_SKIN));
        saveTwoButton.getLabel().setFontScale(buttonFontSize);
        saveTwoButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PixelDungeon.getInstance().getAndroidInterface().overwriteSave(saveTwo);
                close();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        buttonTable = new Table();
        buttonTable.setDebug(debug);
        buttonTable.add(saveOneButton).pad(padding,padding*2,padding,padding*2);
        buttonTable.add(saveTwoButton).pad(padding,padding*2,padding,padding*2);
    }

    private Table getSaveTable(SaveGame save) {
        float fontScale = PixelDungeon.SCALAR * 0.75f;
        GlobalStats gStats = save.getGlobalStats();

        Label attempts = new Label(Messages.STATS_LEVELS_ATTEMPTED, Assets.getInstance().getSkin(Assets.UI_SKIN));
        attempts.setFontScale(fontScale);
        Label attemptsVal = new Label(String.format(Locale.UK, "%d", gStats.getTotalAttempts()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        attemptsVal.setFontScale(fontScale);

        Label completed = new Label(Messages.STATS_LEVELS_COMPLETED, Assets.getInstance().getSkin(Assets.UI_SKIN));
        completed.setFontScale(fontScale);
        Label completedVal = new Label(String.format(Locale.UK, "%d", gStats.getTotalCompleted()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        completedVal.setFontScale(fontScale);

        Label bestTime = new Label(Messages.STATS_TOTAL_TIME, Assets.getInstance().getSkin(Assets.UI_SKIN));
        bestTime.setFontScale(fontScale);
        Label bestTimeVal = new Label(String.format(Locale.UK,"%s", gStats.getTime()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        bestTimeVal.setFontScale(fontScale);

        Label chests = new Label(Messages.STATS_CHESTS_OPENED, Assets.getInstance().getSkin(Assets.UI_SKIN));
        chests.setFontScale(fontScale);
        Label chestsVal = new Label(String.format(Locale.UK,"%d", gStats.getTotalChestsFound()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        chestsVal.setFontScale(fontScale);

        Label keys = new Label(Messages.STATS_KEYS_FOUND, Assets.getInstance().getSkin(Assets.UI_SKIN));
        keys.setFontScale(fontScale);
        Label keysVal = new Label(String.format(Locale.UK,"%d", gStats.getTotalKeysFound()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        keysVal.setFontScale(fontScale);

        Label deaths = new Label(Messages.STATS_TOTAL_DEATHS, Assets.getInstance().getSkin(Assets.UI_SKIN));
        deaths.setFontScale(fontScale);
        Label deathVal = new Label(String.format(Locale.UK,"%d", gStats.getTotalDeaths()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        deathVal.setFontScale(fontScale);

        Table playerStats = new Table();
        playerStats.setDebug(debug);
        playerStats.add(bestTime).left();
        playerStats.add(bestTimeVal).right().padLeft(padding);
        playerStats.row().padTop(padding);
        playerStats.add(attempts).left().expandX();
        playerStats.add(attemptsVal).right().padLeft(padding);
        playerStats.row().padTop(padding);
        playerStats.add(completed).left();
        playerStats.add(completedVal).right().padLeft(padding);
        playerStats.row().padTop(padding);
        playerStats.add(chests).left();
        playerStats.add(chestsVal).right().padLeft(padding);
        playerStats.row().padTop(padding);
        playerStats.add(keys).left();
        playerStats.add(keysVal).right().padLeft(padding);
        playerStats.row().padTop(padding);
        playerStats.add(deaths).left();
        playerStats.add(deathVal).right().padLeft(padding);
        playerStats.row().padTop(padding);
        playerStats.pad(padding*3);
        return playerStats;
    }
}
