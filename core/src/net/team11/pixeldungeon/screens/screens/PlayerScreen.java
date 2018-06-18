package net.team11.pixeldungeon.screens.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.screens.AbstractScreen;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.components.PlayerInfo;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionFade;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.crossplatform.AndroidInterface;

public class PlayerScreen extends AbstractScreen {
    private AndroidInterface androidInterface;
    private boolean signedIn = false;
    private float padding;

    @Override
    public void buildStage() {
        padding = 25 * PixelDungeon.SCALAR;
        androidInterface = PixelDungeon.getInstance().getAndroidInterface();

        addActor(setupBackground());
        if (androidInterface.isSignedIn()) {
            signedIn = true;
            addActor(new PlayerInfo());
        } else {
            signedIn = false;
            addActor(buildLogin());
        }
    }

    private Image setupBackground() {
        Image backgroundImage = new Image(
                Assets.getInstance().getTextureSet(Assets.BACKGROUND).
                        findRegion(ScreenEnum.PLAYER_INFO.toString())
        );
        backgroundImage.setSize(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT);
        return backgroundImage;
    }

    private Table buildLogin() {
        Table mainTable = new Table();
        mainTable.center().pad(padding);

        TextButton signInButton = new TextButton("SIGN IN", Assets.getInstance().getSkin(Assets.UI_SKIN));
        signInButton.getLabel().setFontScale(1.25f * PixelDungeon.SCALAR);

        signInButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                androidInterface.signIn();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        TextButton backButton = new TextButton("BACK", Assets.getInstance().getSkin(Assets.UI_SKIN));
        backButton.getLabel().setFontScale(1.25f * PixelDungeon.SCALAR);

        backButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().changeScreen(ScreenEnum.MAIN_MENU,
                        ScreenTransitionFade.init(0.25f));
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        mainTable.add(signInButton).pad(padding);
        mainTable.row();
        mainTable.add(backButton).pad(padding);

        mainTable.setPosition(PixelDungeon.V_WIDTH/2, PixelDungeon.V_HEIGHT/2);
        return mainTable;
    }

    private void recreate() {
        if (androidInterface.isSignedIn() != signedIn) {
            getActors().removeIndex(1);
            if (androidInterface.isSignedIn()) {
                signedIn = true;
                addActor(new PlayerInfo());
            } else {
                signedIn = false;
                addActor(buildLogin());
            }
        }
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0,0,0,1);
        draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recreate();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }
}
