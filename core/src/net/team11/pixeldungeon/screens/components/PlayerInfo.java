package net.team11.pixeldungeon.screens.components;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionFade;
import net.team11.pixeldungeon.utils.Util;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;
import net.team11.pixeldungeon.utils.crossplatform.AndroidInterface;
import net.team11.pixeldungeon.utils.stats.GlobalStats;

import java.util.Locale;

public class PlayerInfo extends Table {
    private final String DEFAULT_LABEL = "Player Stats";

    private AndroidInterface androidInterface;
    private Label username;
    private Label attemptsVal;
    private Label completedVal;
    private Label bestTimeVal;
    private Label chestsVal;
    private Label keysVal;
    private Label deathVal;


    private float padding;

    public PlayerInfo() {
        super();
        androidInterface = PixelDungeon.getInstance().getAndroidInterface();

        padding = 25 * PixelDungeon.SCALAR;
        setSize(PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        create();
    }

    private void create() {
        Util.getInstance().saveGame();

        if (androidInterface.isSignedIn()) {
            // T, L, B, R
            add(playerLabel()).left().pad(padding*4,padding*4,padding,padding).colspan(4).expandX();
            add(exitButton()).right().pad(padding*4,padding,padding,padding*4);
            row();
            add(playerStats()).left().pad(padding,padding*4,padding,padding).colspan(1).expandY();
            add(gplayServices()).pad(padding,padding,padding,padding*4).colspan(4).right().expandX();
            row();
            add(signoutButton()).left().bottom().pad(padding,padding*4,padding*4,padding).colspan(5);
        } else {
            // T, L, B, R
            add(playerLabel()).left().pad(padding*4,padding*4,padding,padding).colspan(4).expandX();
            add(exitButton()).right().pad(padding*4,padding,padding,padding*4);
            row();
            add(playerStats()).left().pad(padding,padding*4,padding,padding).colspan(5).expandY();
            //add(gplayServices()).pad(padding,padding,padding,padding*4).colspan(4).right().expandX();
            row();
            add(signinButton()).left().bottom().pad(padding,padding*4,padding*4,padding).colspan(5);
        }
    }

    private Label playerLabel() {
        String name;

        if (androidInterface.isSignedIn()) {
            name = androidInterface.getUserName();
            if (name.length() > 16) {
                name = name.substring(0, 16) + "...";
            }
        } else {
            name = DEFAULT_LABEL;
        }
        username = new Label(name,
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        username.setFontScale(1.75f * PixelDungeon.SCALAR);
        username.setAlignment(Align.left);
        return username;
    }

    private Table playerStats() {
        float fontScale = PixelDungeon.SCALAR * 0.75f;
        GlobalStats gStats = Util.getInstance().getStatsUtil().getGlobalStats();

        Label attempts = new Label(Messages.STATS_LEVELS_ATTEMPTED, Assets.getInstance().getSkin(Assets.UI_SKIN));
        attempts.setFontScale(fontScale);
        attemptsVal = new Label(String.format(Locale.UK, "%d", gStats.getTotalAttempts()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        attemptsVal.setFontScale(fontScale);

        Label completed = new Label(Messages.STATS_LEVELS_COMPLETED, Assets.getInstance().getSkin(Assets.UI_SKIN));
        completed.setFontScale(fontScale);
        completedVal = new Label(String.format(Locale.UK, "%d", gStats.getTotalCompleted()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        completedVal.setFontScale(fontScale);

        Label bestTime = new Label(Messages.STATS_TOTAL_TIME, Assets.getInstance().getSkin(Assets.UI_SKIN));
        bestTime.setFontScale(fontScale);
        bestTimeVal = new Label(String.format(Locale.UK,"%s", gStats.getTime()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        bestTimeVal.setFontScale(fontScale);

        Label chests = new Label(Messages.STATS_CHESTS_OPENED, Assets.getInstance().getSkin(Assets.UI_SKIN));
        chests.setFontScale(fontScale);
        chestsVal = new Label(String.format(Locale.UK,"%d", gStats.getTotalChestsFound()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        chestsVal.setFontScale(fontScale);

        Label keys = new Label(Messages.STATS_KEYS_FOUND, Assets.getInstance().getSkin(Assets.UI_SKIN));
        keys.setFontScale(fontScale);
        keysVal = new Label(String.format(Locale.UK,"%d", gStats.getTotalKeysFound()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        keysVal.setFontScale(fontScale);

        Label deaths = new Label(Messages.STATS_TOTAL_DEATHS, Assets.getInstance().getSkin(Assets.UI_SKIN));
        deaths.setFontScale(fontScale);
        deathVal = new Label(String.format(Locale.UK,"%d", gStats.getTotalDeaths()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        deathVal.setFontScale(fontScale);

        Table playerStats = new Table();
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
        return playerStats;
    }

    private Table gplayServices() {
        Table gplayServices = new Table();
        TextButton achievements = new TextButton(Messages.ACHIEVEMENTS_UPPER, Assets.getInstance().getSkin(Assets.UI_SKIN));
        achievements.getLabel().setFontScale(1.25f * PixelDungeon.SCALAR);

        achievements.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                androidInterface.openAchievements();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        TextButton leaderboards = new TextButton(Messages.LEADERBOARDS_UPPER, Assets.getInstance().getSkin(Assets.UI_SKIN));
        leaderboards.getLabel().setFontScale(1.25f * PixelDungeon.SCALAR);

        leaderboards.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                androidInterface.openLeaderboards();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        gplayServices.add(achievements).top().pad(padding).fillX();
        gplayServices.row();
        gplayServices.add(leaderboards).bottom().pad(padding).fillX();

        return gplayServices;
    }

    private Button exitButton() {
        TextButton backButton = new TextButton(Messages.X, Assets.getInstance().getSkin(Assets.UI_SKIN));
        backButton.getLabel().setFontScale(1.25f * PixelDungeon.SCALAR);

        backButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().changeScreen(ScreenEnum.MAIN_MENU,
                        ScreenTransitionFade.init(.2f));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        return backButton;
    }

    private Button signoutButton() {
        TextButton signOutButton = new TextButton(Messages.SIGN_OUT, Assets.getInstance().getSkin(Assets.UI_SKIN));
        signOutButton.getLabel().setFontScale(.75f * PixelDungeon.SCALAR);

        signOutButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().getScreen().pause();
                Util.getInstance().signOut();
                ScreenManager.getInstance().getScreen().resume();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        return signOutButton;
    }

    private Button signinButton() {
        TextButton signinButton = new TextButton(Messages.SIGN_IN, Assets.getInstance().getSkin(Assets.UI_SKIN));
        signinButton.getLabel().setFontScale(.75f * PixelDungeon.SCALAR);

        signinButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                androidInterface.signIn();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        return signinButton;
    }

    public void update() {
        if (androidInterface.isSignedIn()) {
            String name = androidInterface.getUserName();
            if (name.length() > 16) {
                name = name.substring(0, 16) + "...";
            }
            username.setText(name);
        }

        GlobalStats gStats = Util.getInstance().getStatsUtil().getGlobalStats();
        attemptsVal.setText(String.format(Locale.UK, "%d", gStats.getTotalAttempts()));
        completedVal.setText(String.format(Locale.UK, "%d", gStats.getTotalCompleted()));
        bestTimeVal.setText(String.format(Locale.UK,"%s", gStats.getTime()));
        chestsVal.setText(String.format(Locale.UK, "%d", gStats.getTotalChestsFound()));
        keysVal.setText(String.format(Locale.UK, "%d", gStats.getTotalKeysFound()));
        deathVal.setText(String.format(Locale.UK, "%d", gStats.getTotalDeaths()));
    }
}
