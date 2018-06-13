package net.team11.pixeldungeon.screens;

public enum ScreenEnum {
    GAME {
        public AbstractScreen getScreen(Object... params) {
            return new PlayScreen((String) params[0]);
        }
    },
    HOW_TO {
        @Override
        public String toString() {
            return "howtoScreen";
        }
        @Override
        public AbstractScreen getScreen(Object... params) {
            return new HowToScreen();
        }
    },
    LEVEL_SELECT {
        @Override
        public String toString() {
            return "levelselectScreen";
        }

        public AbstractScreen getScreen(Object... params) {
            return new LevelSelectScreen();
        }
    },
    MAIN_MENU {
        @Override
        public String toString() {
            return "mainScreen";
        }
        public AbstractScreen getScreen(Object... params) {
            return new MainMenuScreen();
        }
    },
    SKIN_SELECT {
        @Override
        public String toString() {
            return "skinSelect";
        }
        @Override
        public AbstractScreen getScreen(Object... params) {
            return new SkinSelectScreen();
        }
    };

    public abstract AbstractScreen getScreen(Object... params);
}
