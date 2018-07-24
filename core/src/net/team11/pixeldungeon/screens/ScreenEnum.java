package net.team11.pixeldungeon.screens;

import net.team11.pixeldungeon.game.entity.component.InventoryComponent;
import net.team11.pixeldungeon.screens.screens.LevelCompleteScreen;
import net.team11.pixeldungeon.screens.screens.LevelSelectScreen;
import net.team11.pixeldungeon.screens.screens.LoadingScreen;
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
    LEVEL_SELECT {
        @Override
        public String toString() {
            return "levelSelectScreen";
        }

        public AbstractScreen getScreen(Object... params) {
            return new LevelSelectScreen();
        }
    },
    LEVEL_COMPLETE {
        @Override
        public String toString() {
            return "levelCompleteScreen";
        }

        public AbstractScreen getScreen(Object... params) {
            return new LevelCompleteScreen((InventoryComponent)params[0]);
        }
    },
    LOADING {
        @Override
        public String toString() {
            return "loadingScreen";
        }

        public AbstractScreen getScreen(Object... params) {
            return new LoadingScreen();
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
