package net.team11.pixeldungeon.screens.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionFade;
import net.team11.pixeldungeon.statistics.GlobalStatistics;
import net.team11.pixeldungeon.statistics.StatisticsUtil;
import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.utils.Assets;
import net.team11.pixeldungeon.utils.crossplatform.AndroidInterface;

import java.util.Locale;

public class PlayerInfo extends Table {
    private float padding;
    private AndroidInterface androidInterface;
    private GlobalStatistics globalStatistics;

    public PlayerInfo() {
        super();
        androidInterface = PixelDungeon.getInstance().getAndroidInterface();
        globalStatistics = StatisticsUtil.getGlobalStatistics();

        padding = 25 * PixelDungeon.SCALAR;
        setSize(PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        //setPosition(PixelDungeon.V_WIDTH/2, PixelDungeon.V_HEIGHT/2);
        create();
    }

    public void create() {
        // T, L, B, R
        add(playerLabel()).left().pad(padding*4,padding*4,padding,padding).colspan(2).expandX();
        add(exitButton()).right().pad(padding*4,padding,padding,padding*4);
        row();
        add(playerStats()).left().pad(padding,padding*4,padding,padding).colspan(2).expandY();
        add(gplayServices()).pad(padding,padding,padding,padding*4).right();
        row();
        add(signoutButton()).left().bottom().pad(padding,padding*4,padding*4,padding);
    }

    private Label playerLabel() {
        Label username = new Label(androidInterface.getUserName(),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        username.setFontScale(1.75f * PixelDungeon.SCALAR);
        return username;
    }

    private Table playerStats() {
        Table playerStats = new Table();

        Label attempts = new Label("Attempts", Assets.getInstance().getSkin(Assets.UI_SKIN));
        Label attemptsVal = new Label(String.format(Locale.UK, "%d", globalStatistics.getAttempts()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));

        Label completed = new Label("Levels Completed", Assets.getInstance().getSkin(Assets.UI_SKIN));
        Label completedVal = new Label(String.format(Locale.UK, "%d", globalStatistics.getLevelsCompleted()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));

        Label bestTime = new Label("Total Time", Assets.getInstance().getSkin(Assets.UI_SKIN));
        Label bestTimeVal = new Label(String.format(Locale.UK,"%02d:%02d",
                globalStatistics.getTime()/60,globalStatistics.getTime()%60),
                Assets.getInstance().getSkin(Assets.UI_SKIN));

        Label chests = new Label("Chests Opened", Assets.getInstance().getSkin(Assets.UI_SKIN));
        Label chestsVal = new Label(String.format(Locale.UK,"%d",globalStatistics.getChests()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));

        Label keys = new Label("Keys Found", Assets.getInstance().getSkin(Assets.UI_SKIN));
        Label keysVal = new Label(String.format(Locale.UK,"%d",globalStatistics.getKeys()),
                Assets.getInstance().getSkin(Assets.UI_SKIN));


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
        return playerStats;
    }

    private Table gplayServices() {
        Table gplayServices = new Table();
        TextButton achievements = new TextButton("ACHIEVEMENTS", Assets.getInstance().getSkin(Assets.UI_SKIN));
        achievements.getLabel().setFontScale(1.25f * PixelDungeon.SCALAR);

        achievements.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                androidInterface.openAchievements();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        TextButton leaderboards = new TextButton("LEADERBOARDS", Assets.getInstance().getSkin(Assets.UI_SKIN));
        leaderboards.getLabel().setFontScale(1.25f * PixelDungeon.SCALAR);

        leaderboards.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                androidInterface.openAchievements();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        gplayServices.add(achievements).top().pad(padding).fillX();
        gplayServices.row();
        gplayServices.add(leaderboards).bottom().pad(padding).fillX();
        return gplayServices;
    }

    private Button exitButton() {
        TextButton backButton = new TextButton("X", Assets.getInstance().getSkin(Assets.UI_SKIN));
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
        TextButton signOutButton = new TextButton("SIGN OUT", Assets.getInstance().getSkin(Assets.UI_SKIN));
        signOutButton.getLabel().setFontScale(.75f * PixelDungeon.SCALAR);

        signOutButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().getScreen().pause();
                androidInterface.signOut();
                ScreenManager.getInstance().getScreen().resume();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        return signOutButton;
    }
}
