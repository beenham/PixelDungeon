package net.team11.pixeldungeon.screens.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.screens.AbstractScreen;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionPush;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.assets.Messages;

public class HowToScreen extends AbstractScreen {
    Image backgroundImage;

    @Override
    public void buildStage() {
        float padding = 25 * PixelDungeon.SCALAR;
        addActor(setupBackground());
        addActor(setupMainTable(padding));
    }

    private Image setupBackground() {
        backgroundImage = new Image(
                Assets.getInstance().getTextureSet(Assets.BACKGROUND).findRegion(ScreenEnum.HOW_TO.toString())
        );
        backgroundImage.setSize(PixelDungeon.V_WIDTH * 1.f, PixelDungeon.V_HEIGHT * 1.f);
        //image.setPosition(0 - image.getWidth()/3, 0 - image.getHeight() / 3);
        return backgroundImage;
    }

    private Table setupMainTable(float padding) {
        Table mainTable = new Table();
        mainTable.center().pad(padding);

        TextButton backButton = new TextButton(Messages.BACK_UPPER,Assets.getInstance().getSkin(Assets.UI_SKIN));
        backButton.getLabel().setFontScale(1.25f * PixelDungeon.SCALAR);

        backButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().changeScreen(ScreenEnum.MAIN_MENU,
                        ScreenTransitionPush.init(1.5f,ScreenTransitionPush.LEFT,Interpolation.pow2));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        mainTable.add(backButton).pad(padding);

        mainTable.setPosition(PixelDungeon.V_WIDTH/2, PixelDungeon.V_HEIGHT/2);
        mainTable.setDebug(true);
        return mainTable;
    }


    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }
}