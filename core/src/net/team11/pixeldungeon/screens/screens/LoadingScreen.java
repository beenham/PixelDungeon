package net.team11.pixeldungeon.screens.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.game.map.MapManager;
import net.team11.pixeldungeon.screens.AbstractScreen;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionFade;
import net.team11.pixeldungeon.utils.Util;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.inventory.InventoryUtil;

public class LoadingScreen extends AbstractScreen {
    private boolean load;

    @Override
    public void buildStage() {
        load = false;
        addActor(setupBackground());
    }

    private Image setupBackground() {
        Image backgroundImage = new Image(
                Assets.getInstance().getTextureSet(Assets.BACKGROUND).findRegion(ScreenEnum.LOADING.toString())
        );
        backgroundImage.setSize(PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        return backgroundImage;
    }

    private void update() {
        if (!load) {
            load = true;
            new Timer().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    Util.getInstance();
                    PixelDungeon.getInstance().getAndroidInterface().loadSaveGame();
                    Assets.getInstance().init();
                    InventoryUtil.getInstance();
                    MapManager.getInstance();
                    new Timer();
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            ScreenManager.getInstance().changeScreen(ScreenEnum.MAIN_MENU,
                                    ScreenTransitionFade.init(2f));
                        }
                    },3,0,0);
                }
            },1,0,0);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return null;
    }
}
