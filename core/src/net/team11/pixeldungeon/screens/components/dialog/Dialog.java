package net.team11.pixeldungeon.screens.components.dialog;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;

public abstract class Dialog extends Table {
    protected Table buttonTable;
    protected Table contentTable;

    protected float padding;

    protected Dialog() {
        padding = 25 * PixelDungeon.SCALAR;
        setBackground(new NinePatchDrawable(new NinePatch(Assets.getInstance()
                .getTextureSet(Assets.HUD).createPatch(AssetName.UI_SCROLLPANE))));
    }

    abstract void setupContentTable();
    abstract void setupButtonTable();

    protected void close() {
        remove();
        ScreenManager.getInstance().getScreen().resume();
    }

    public void show(Stage stage) {
        stage.addActor(this);
        ScreenManager.getInstance().getScreen().pause();
    }

}
