package net.team11.pixeldungeon.screens.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.screens.AbstractScreen;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.components.PlayerInfo;
import net.team11.pixeldungeon.utils.Util;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.crossplatform.AndroidInterface;

public class PlayerScreen extends AbstractScreen {
    private AndroidInterface androidInterface;
    private PlayerInfo playerInfo;
    private boolean signedIn = false;
    private float padding;

    @Override
    public void buildStage() {
        padding = 25 * PixelDungeon.SCALAR;
        androidInterface = PixelDungeon.getInstance().getAndroidInterface();

        addActor(setupBackground());
        signedIn = androidInterface.isSignedIn();
        addActor(playerInfo = new PlayerInfo());
    }

    private Image setupBackground() {
        Image backgroundImage = new Image(
                Assets.getInstance().getTextureSet(Assets.BACKGROUND).
                        findRegion(ScreenEnum.PLAYER_INFO.toString())
        );
        backgroundImage.setSize(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT);
        return backgroundImage;
    }

    private void recreate() {
        if (androidInterface.isSignedIn() != signedIn) {
            getActors().removeIndex(1);
            if (androidInterface.isSignedIn()) {
                Util.getInstance().signIn();
                signedIn = true;
            } else {
                signedIn = false;
            }
            addActor(playerInfo = new PlayerInfo());
        }
    }

    @Override
    public void resume() {
        super.resume();
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                recreate();
            }
        },3,0,0);
    }

    private void update() {
        if (playerInfo != null) {
            if (PixelDungeon.getInstance().getAndroidInterface().isSignedIn()) {
                playerInfo.update();
            } else {
                playerInfo = null;
            }
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }
}
