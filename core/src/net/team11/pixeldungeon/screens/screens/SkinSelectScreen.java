package net.team11.pixeldungeon.screens.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.screens.AbstractScreen;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.components.skinselector.SkinInfo;
import net.team11.pixeldungeon.screens.components.skinselector.SkinSelector;
import net.team11.pixeldungeon.utils.assets.Assets;

public class SkinSelectScreen extends AbstractScreen {
    private SkinSelector selector;
    private SkinInfo info;

    @Override
    public void buildStage() {
        addActor(setupBackground());
        addActor(setupLeft());
        addActor(setupRight());
    }

    private Image setupBackground() {
        Image backgroundImage = new Image(
                Assets.getInstance().getTextureSet(Assets.BACKGROUND).findRegion(ScreenEnum.SKIN_SELECT.toString())
        );
        backgroundImage.setSize(PixelDungeon.V_WIDTH * 1.f, PixelDungeon.V_HEIGHT * 1.f);
        return backgroundImage;
    }

    private WidgetGroup setupRight() {
        selector = new SkinSelector(info,PixelDungeon.V_WIDTH / 5 * 3);
        selector.setBounds(PixelDungeon.V_WIDTH/5*2,0,PixelDungeon.V_WIDTH/5*3, PixelDungeon.V_HEIGHT);
        return selector;
    }

    private WidgetGroup setupLeft() {
        info = new SkinInfo(PixelDungeon.V_WIDTH / 5 * 2);
        info.setBounds(0,0,PixelDungeon.V_WIDTH/5*2, PixelDungeon.V_HEIGHT);
        return info;
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }
}

