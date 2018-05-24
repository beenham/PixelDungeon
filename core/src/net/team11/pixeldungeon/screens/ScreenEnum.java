package net.team11.pixeldungeon.screens;

public enum ScreenEnum {
    /*MAIN_MENU {
        public AbstractScreen getScreen(Object... params) {
            return new MainMenuScreen();
        }
    },
    LEVEL_SELECT {
        public AbstractScreen getScreen(Object... params) {
            return new MainMenuScreen();
        }
    },*/
    GAME {
        public AbstractScreen getScreen(Object... params) {
            return new PlayScreen((String) params[0]);
        }
    };

    public abstract AbstractScreen getScreen(Object... params);
}
