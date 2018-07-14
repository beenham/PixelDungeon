package net.team11.pixeldungeon.screens.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.game.entity.component.InventoryComponent;
import net.team11.pixeldungeon.game.items.Coin;
import net.team11.pixeldungeon.game.map.MapManager;
import net.team11.pixeldungeon.saves.SaveGame;
import net.team11.pixeldungeon.screens.AbstractScreen;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.components.coin.CoinAwarderDisplay;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionFade;
import net.team11.pixeldungeon.utils.Util;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;
import net.team11.pixeldungeon.utils.stats.AchivementStats;
import net.team11.pixeldungeon.utils.stats.GlobalStats;
import net.team11.pixeldungeon.utils.stats.LevelStats;
import net.team11.pixeldungeon.utils.stats.StatsUtil;

import java.util.Locale;

public class LevelCompleteScreen extends AbstractScreen {
    private StatsUtil statsUtil = Util.getStatsUtil();
    private LevelStats levelStats;
    private CoinAwarderDisplay coinAwarder;

    private float padding;
    private Label yourTime;
    private Label bestTime;
    private Label chestsVal;
    private Label keysVal;
    private Label itemsVal;
    private Label deathsVal;
    private int timeVal;
    private int chestValue;
    private int itemsValue;
    private int keysValue;
    private int deathsValue;

    private float timer;
    private float speed;
    private boolean paused;

    public LevelCompleteScreen(InventoryComponent inventory) {
        chestValue = 0;
        itemsValue = 0;
        keysValue = 0;
        deathsValue = 0;
        timer = 0;
        speed = 0.02f;

        padding = 25 * PixelDungeon.SCALAR;
        updateLevelStats();
        statsUtil.saveTimer();
        coinAwarder = new CoinAwarderDisplay(inventory);
    }

    private void updateLevelStats() {
        levelStats = statsUtil.getLevelStats(MapManager.getInstance().getCurrentMap().getMapName());
        timeVal = 0;

        levelStats.incrementCompleted();
        statsUtil.getGlobalStats().incrementCompleted();
        for (String chest : statsUtil.getCurrentStats().getChests()) {
            levelStats.setChestFound(chest);
        }
        for (String key : statsUtil.getCurrentStats().getKeys()) {
            levelStats.setKeyFound(key);
        }
        for (String item : statsUtil.getCurrentStats().getItems()) {
            levelStats.setItemFound(item);
        }
        levelStats.submitBestTime(Util.getStatsUtil().getTimer());
        Util.saveGame();

        AchivementStats.updateStats(statsUtil.getCurrentStats(),levelStats);
    }

    @Override
    public void buildStage() {
        addActor(setupBackground());
        addActor(buildScreen());
    }

    private Image setupBackground() {
        Image backgroundImage = new Image(
                Assets.getInstance().getTextureSet(Assets.BACKGROUND).
                        findRegion(ScreenEnum.PLAYER_INFO.toString())
        );
        backgroundImage.setSize(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT);
        return backgroundImage;
    }

    private Table buildScreen() {
        Table mainTable = new Table();

        TextButton doneButton = new TextButton(Messages.DONE_CAMELCASE,Assets.getInstance().getSkin(Assets.UI_SKIN));
        doneButton.getLabel().setFontScale(1.2f * PixelDungeon.SCALAR);
        doneButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().changeScreen(ScreenEnum.MAIN_MENU,
                        ScreenTransitionFade.init(1f));
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        mainTable.add(createLabel(levelStats.getLevelName(), 1.5f))
                .pad(padding*4,padding*4,padding,padding).left().expandX();
        mainTable.add(createLabel(Messages.STATS_COMPLETED, 1.25f))
                .pad(padding*4,padding,padding,padding*4).right();
        mainTable.row();
        mainTable.add(yourTime = createLabel(Messages.STATS_YOUR_TIME+":  " + "00:00", 1.25f))
                .pad(padding,padding*4,padding,padding).left();
        mainTable.add(bestTime = createLabel(Messages.STATS_BEST_TIME+":  " + "00:00", 1.25f))
                .pad(padding,padding,padding,padding*4).right();
        mainTable.row();
        mainTable.add(coinAwarder)
                .pad(padding,padding*4,padding,padding).top().left().expand();
        mainTable.add(playerStats())
                .pad(padding,padding,padding,padding*4).top().right().expandY().fillY();
        mainTable.row();
        mainTable.add(doneButton).colspan(2)
                .pad(padding,padding,padding*4,padding*4).bottom().right();

        mainTable.setSize(PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        return mainTable;
    }

    private Label createLabel(String label, float scale) {
        Label username = new Label(label,
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        username.setFontScale(scale * PixelDungeon.SCALAR);
        return username;
    }

    private Table playerStats() {
        LevelStats stats = statsUtil.getLevelStats(MapManager.getInstance().getCurrentMap().getMapName());
        float fontSize = PixelDungeon.SCALAR * 0.75f;

        Table playerStats = new Table();

        Label chests = new Label(Messages.STATS_CHESTS_OPENED+":  ", Assets.getInstance().getSkin(Assets.UI_SKIN));
        chestsVal = new Label(String.format(Locale.UK,"%d/%d",
                chestValue,
                stats.getTotalChests()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        chests.setFontScale(fontSize);
        chestsVal.setFontScale(fontSize);

        Label keys = new Label(Messages.STATS_KEYS_FOUND+":  ", Assets.getInstance().getSkin(Assets.UI_SKIN));
        keysVal = new Label(String.format(Locale.UK,"%d/%d",
                keysValue,
                stats.getTotalKeys()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        keys.setFontScale(fontSize);
        keysVal.setFontScale(fontSize);

        Label items = new Label(Messages.STATS_ITEMS_FOUND+":  ", Assets.getInstance().getSkin(Assets.UI_SKIN));
        itemsVal = new Label(String.format(Locale.UK,"%d/%d",
                itemsValue,
                stats.getTotalItems()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        items.setFontScale(fontSize);
        itemsVal.setFontScale(fontSize);

        Label deaths = new Label(Messages.STATS_DEATHS+":  ", Assets.getInstance().getSkin(Assets.UI_SKIN));
        deathsVal = new Label(String.format(Locale.UK,"%d",
                deathsValue),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        deaths.setFontScale(fontSize);
        deathsVal.setFontScale(fontSize);

        playerStats.add(chests).left();
        playerStats.add(chestsVal).right().padLeft(padding);
        playerStats.row().padTop(padding);
        playerStats.add(keys).left();
        playerStats.add(keysVal).right().padLeft(padding);
        playerStats.row().padTop(padding);
        playerStats.add(items).left();
        playerStats.add(itemsVal).right().padLeft(padding);
        playerStats.row().padTop(padding);
        playerStats.add(deaths).left();
        playerStats.add(deathsVal).right().padLeft(padding);
        playerStats.row().padTop(padding);
        return playerStats;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
    }

    private void update(float delta) {
        if (!paused) {
            if (timeVal < statsUtil.getTimer()) {
                timer += delta;
                if (timer >= speed) {
                    timeVal++;
                    yourTime.setText(String.
                            format(Locale.UK, Messages.STATS_YOUR_TIME + ":  %02d:%02d", timeVal / 60, timeVal % 60));
                    if (timeVal <= levelStats.getBestTimeVal()) {
                        bestTime.setText(String.
                                format(Locale.UK, Messages.STATS_BEST_TIME + ":  %02d:%02d", timeVal / 60, timeVal % 60));
                    }
                    if (statsUtil.getTimer() - timeVal <= 10) {
                        speed += 0.025f;
                    }
                    timer = 0;
                }
            } else {
                coinAwarder.update();

                if (chestValue < statsUtil.getCurrentStats().getChestsFound()) {
                    speed = 0.3f;
                    timer += delta;
                    if (timer >= speed) {
                        chestValue++;
                        chestsVal.setText(String.format(Locale.UK, "%d/%d",
                                chestValue,
                                levelStats.getTotalChests()));
                        timer = 0;
                    }
                    if (itemsValue == statsUtil.getCurrentStats().getItemsFound()) {
                        speed = 0.05f;
                    }
                }
                if (keysValue < statsUtil.getCurrentStats().getKeysFound()) {
                    speed = 0.3f;
                    timer += delta;
                    if (timer >= speed) {
                        keysValue++;
                        keysVal.setText(String.format(Locale.UK, "%d/%d",
                                keysValue,
                                levelStats.getTotalKeys()));
                        timer = 0;
                    }
                    if (itemsValue == statsUtil.getCurrentStats().getItemsFound()) {
                        speed = 0.05f;
                    }
                }
                if (itemsValue < statsUtil.getCurrentStats().getItemsFound()) {
                    speed = 0.3f;
                    timer += delta;
                    if (timer >= speed) {
                        itemsValue++;
                        itemsVal.setText(String.format(Locale.UK, "%d/%d",
                                itemsValue,
                                levelStats.getTotalItems()));
                        timer = 0;
                    }
                    if (itemsValue == statsUtil.getCurrentStats().getItemsFound()) {
                        speed = 0.05f;
                    }
                }
                if (deathsValue < statsUtil.getCurrentStats().getDeaths()) {
                    timer += delta;
                    if (timer >= speed) {
                        deathsValue++;
                        deathsVal.setText(String.format(Locale.UK, "%d", deathsValue));
                        timer = 0;
                        if (statsUtil.getTimer() - timeVal <= 10) {
                            speed += 0.025f;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        this.paused = true;
        super.pause();
    }

    @Override
    public void resume() {
        this.paused = false;
        super.resume();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }
}
