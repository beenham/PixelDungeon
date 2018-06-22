package net.team11.pixeldungeon.screens;

import net.team11.pixeldungeon.entity.component.InventoryComponent;
import net.team11.pixeldungeon.screens.screens.HowToScreen;
import net.team11.pixeldungeon.screens.screens.LevelCompleteScreen;
import net.team11.pixeldungeon.screens.screens.LevelSelectScreen;
import net.team11.pixeldungeon.screens.screens.MainMenuScreen;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.screens.screens.PlayerScreen;
import net.team11.pixeldungeon.screens.screens.SkinSelectScreen;

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
    LEVEL_COMPLETE {
        @Override
        public String toString() {
            return "levelcompleteScreen";
        }

        public AbstractScreen getScreen(Object... params) {
            return new LevelCompleteScreen((InventoryComponent)params[0]);
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
    PLAYER_INFO {
        @Override
        public String toString() {
            return "playerInfoScreen";
        }
        @Override
        public AbstractScreen getScreen(Object... params) {
            return new PlayerScreen();
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
