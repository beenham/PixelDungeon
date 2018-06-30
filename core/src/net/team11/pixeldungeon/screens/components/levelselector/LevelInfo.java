package net.team11.pixeldungeon.screens.components.levelselector;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.utils.assets.Messages;
import net.team11.pixeldungeon.utils.stats.LevelStats;
import net.team11.pixeldungeon.utils.stats.StatsUtil;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;

import java.util.Locale;

public class LevelInfo extends Table {
    private LevelSelector selector;
    private StatsUtil statsUtil;

    private Label levelName;
    private Label attemptsVal;
    private Label completedVal;
    private Label bestTimeVal;
    private Label chestsVal;
    private Label keysVal;
    private Label itemsVal;

    public LevelInfo(LevelSelector levelSelector) {
        this.selector = levelSelector;
        this.statsUtil = StatsUtil.getInstance();
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
        levelName = new Label(statsUtil.getLevelStats(selector.getMap().getMapName()).getLevelName(), Assets.getInstance().getSkin(Assets.UI_SKIN),"title");
        levelName.setFontScale(1.25f * PixelDungeon.SCALAR);

        Table table = new Table();
        table.add(levelName).bottom().padTop(getHeight()/5);

        add(levelName).expand();
    }

    private void setupInfo() {
        float padding = 30f * PixelDungeon.SCALAR;
        float fontScale = PixelDungeon.SCALAR;

        Label attempts = new Label(Messages.STATS_ATTEMPTED, Assets.getInstance().getSkin(Assets.UI_SKIN));
        attemptsVal = new Label(String.format(Locale.UK, "%d", statsUtil.getLevelStats(selector.getMap().getMapName()).getAttempts()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));

        Label completed = new Label(Messages.STATS_COMPLETED, Assets.getInstance().getSkin(Assets.UI_SKIN));
        completedVal = new Label(String.format(Locale.UK, "%s", statsUtil.getLevelStats(selector.getMap().getMapName()).getCompleted()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));

        Label bestTime = new Label(Messages.STATS_BEST_TIME, Assets.getInstance().getSkin(Assets.UI_SKIN));
        bestTimeVal = new Label(String.format(Locale.UK,"%s",statsUtil.getLevelStats(selector.getMap().getMapName()).getBestTime()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));

        Label chests = new Label(Messages.STATS_CHESTS, Assets.getInstance().getSkin(Assets.UI_SKIN));
        chestsVal = new Label(String.format(Locale.UK,"%d/%d",statsUtil.getLevelStats(selector.getMap().getMapName()).getFoundChests(),
                statsUtil.getLevelStats(selector.getMap().getMapName()).getTotalChests()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));

        Label keys = new Label(Messages.STATS_KEYS, Assets.getInstance().getSkin(Assets.UI_SKIN));
        keysVal = new Label(String.format(Locale.UK,"%d/%d",statsUtil.getLevelStats(selector.getMap().getMapName()).getFoundKeys(),
                statsUtil.getLevelStats(selector.getMap().getMapName()).getTotalKeys()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));

        Label items = new Label(Messages.STATS_ITEMS, Assets.getInstance().getSkin(Assets.UI_SKIN));
        itemsVal = new Label(String.format(Locale.UK,"%d/%d",statsUtil.getLevelStats(selector.getMap().getMapName()).getFoundItems(),
                statsUtil.getLevelStats(selector.getMap().getMapName()).getTotalItems()),
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
        Button playButton = new TextButton(Messages.PLAY_UPPER, Assets.getInstance().getSkin(Assets.UI_SKIN));
        ((TextButton)playButton).getLabel().setFontScale(1f * PixelDungeon.SCALAR);
        playButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                statsUtil.getLevelStats(selector.getMap().getMapName()).incrementAttempts();
                statsUtil.writeLevelStats(selector.getMap().getMapName());
                statsUtil.getGlobalStats().incrementAttempts();
                statsUtil.writeGlobalStats();
                ScreenManager.getInstance().changeScreen(ScreenEnum.GAME,
                        null,
                        selector.getMap().getMapName());
                
                PixelDungeon.getInstance().getAndroidInterface().earnNewAdventurer();
                PixelDungeon.getInstance().getAndroidInterface().earn10Attempts();
                PixelDungeon.getInstance().getAndroidInterface().earn100Attempts();
                PixelDungeon.getInstance().getAndroidInterface().earn500Attempts();
                PixelDungeon.getInstance().getAndroidInterface().earn1000Attempts();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        Table table = new Table();
        table.add(playButton).top();

        add(table).padBottom(PixelDungeon.V_HEIGHT/10).pad(20f * PixelDungeon.SCALAR).expand();
    }

    public void update() {
        LevelStats stats = statsUtil.getLevelStats(selector.getMap().getMapName());

        levelName.setText(stats.getLevelName());
        attemptsVal.setText(String.format(Locale.UK,"%d",stats.getAttempts()));
        completedVal.setText(String.format(Locale.UK,"%s",stats.getCompleted()));
        bestTimeVal.setText(String.format(Locale.UK,"%s",stats.getBestTime()));
        chestsVal.setText(String.format(Locale.UK,"%d/%d",stats.getFoundChests(),stats.getTotalChests()));
        keysVal.setText(String.format(Locale.UK,"%d/%d",stats.getFoundKeys(),stats.getTotalKeys()));
        itemsVal.setText(String.format(Locale.UK,"%d/%d",stats.getFoundItems(),stats.getTotalItems()));
    }
}
