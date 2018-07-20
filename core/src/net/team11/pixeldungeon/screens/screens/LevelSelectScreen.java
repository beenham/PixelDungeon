package net.team11.pixeldungeon.screens.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.screens.AbstractScreen;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.components.levelselector.LevelInfo;
import net.team11.pixeldungeon.screens.components.levelselector.LevelSelector;
import net.team11.pixeldungeon.utils.assets.Assets;

public class LevelSelectScreen extends AbstractScreen {
    private LevelSelector selector;
    private LevelInfo info;

    @Override
    public void buildStage() {
        addActor(setupBackground());
        addActor(setupLeft());
        addActor(setupRight());
    }

    private Image setupBackground() {
        Image backgroundImage = new Image(
                Assets.getInstance().getTextureSet(Assets.BACKGROUND).
                        findRegion(ScreenEnum.LEVEL_SELECT.toString())
        );
        backgroundImage.setSize(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT);
        return backgroundImage;
    }

    private WidgetGroup setupLeft() {
        selector = new LevelSelector(PixelDungeon.V_WIDTH / 5 * 3);
        selector.setBounds(0,0,PixelDungeon.V_WIDTH/3*2, PixelDungeon.V_HEIGHT);
        return selector;
    }

    private WidgetGroup setupRight() {
        info = new LevelInfo(selector);
        info.setBounds(selector.getWidth(),0,PixelDungeon.V_WIDTH/3, PixelDungeon.V_HEIGHT);
        return info;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        info.update();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }
}
