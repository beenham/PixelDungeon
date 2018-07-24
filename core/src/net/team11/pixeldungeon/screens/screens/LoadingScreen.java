package net.team11.pixeldungeon.screens.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.game.map.MapManager;
import net.team11.pixeldungeon.screens.AbstractScreen;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionFade;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;
import net.team11.pixeldungeon.utils.inventory.InventoryUtil;
import net.team11.pixeldungeon.utils.stats.StatsUtil;

import java.util.Locale;
import java.util.Random;

public class LoadingScreen extends AbstractScreen {
    private boolean load;
    private Label test;

    @Override
    public void buildStage() {
        load = false;

        addActor(setupBackground());
        setupTable();
    }

    private Image setupBackground() {
        Image backgroundImage = new Image(
                Assets.getInstance().getTextureSet(Assets.BACKGROUND).findRegion(ScreenEnum.LOADING.toString())
        );
        backgroundImage.setSize(PixelDungeon.V_WIDTH,PixelDungeon.V_HEIGHT);
        return backgroundImage;
    }

    private Table setupTable() {
        Label label = new Label(Messages.TITLE_UPPER, Assets.getInstance().getSkin(Assets.UI_SKIN),"title");
        label.setFontScale(PixelDungeon.SCALAR);

        Table table = new Table();
        table.add(label);
        table.row();
        table.add(test = new Label("",Assets.getInstance().getSkin(Assets.UI_SKIN)));
        table.setPosition(PixelDungeon.V_WIDTH/2, PixelDungeon.V_HEIGHT/2);
        return table;
    }

    private void update() {
        test.setText(String.format(Locale.UK,"%d",new Random().nextInt(100)));
        if (!load) {
            load = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    StatsUtil.getInstance();
                    InventoryUtil.getInstance();
                    MapManager.getInstance();
                    new Timer();
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            ScreenManager.getInstance().changeScreen(ScreenEnum.MAIN_MENU,
                                    ScreenTransitionFade.init(2f));
                        }
                    },5,0,0);
                }
            }).run();
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
