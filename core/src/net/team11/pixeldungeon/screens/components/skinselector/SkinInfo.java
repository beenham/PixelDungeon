package net.team11.pixeldungeon.screens.components.skinselector;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;

public class SkinInfo extends Table {
    public SkinInfo() {
        setupLayout();
        setBackground(new NinePatchDrawable(Assets.getInstance().getTextureSet(
                Assets.HUD).createPatch(AssetName.DARKEN_60)));
    }

    private void setupLayout() {

    }
}
