package net.team11.pixeldungeon.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import net.team11.pixeldungeon.screens.game.DirectedGame;
import net.team11.pixeldungeon.screens.transitions.ScreenTransition;

public class ScreenManager {
    private static ScreenManager INSTANCE;
    private DirectedGame game;

    private ScreenManager() {
        super();
    }

    public static ScreenManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ScreenManager();
        }
        return INSTANCE;
    }

    public void initialize(DirectedGame game) {
        this.game = game;
    }

    public void showScreen(ScreenEnum screenEnum, Object... params) {
        Screen currentScreen = game.getScreen();
        AbstractScreen newScreen = screenEnum.getScreen(params);
        newScreen.buildStage();
        game.setScreen(newScreen);

        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }

    public void changeScreen(ScreenEnum screenEnum, ScreenTransition screenTransition, Object... params) {
        AbstractScreen newScreen = screenEnum.getScreen(params);
        newScreen.buildStage();
        game.setScreen(newScreen, screenTransition);
    }

    public Screen getScreen() {
        return game.getScreen();
    }
}
