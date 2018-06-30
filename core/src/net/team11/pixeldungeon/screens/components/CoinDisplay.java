package net.team11.pixeldungeon.screens.components;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;

import java.util.Locale;

public class CoinDisplay extends Table {
    public CoinDisplay(float size, int amount) {
        setupCoinDisplay(size,amount);
    }

    private void setupCoinDisplay(float size, int amount) {
        Image coin = new Image(Assets.getInstance().getTextureSet(Assets.ITEMS)
                .findRegion(AssetName.COIN));
        Label label = new Label(
                numberToString(amount),
                Assets.getInstance().getSkin(Assets.UI_SKIN));
        label.setFontScale(PixelDungeon.SCALAR);

        add(coin).size(size).padRight(size/2);
        add(label);
    }

    private String numberToString(int value) {
        if (value / 1000000 > 0) {
            return String.format(Locale.UK,
                    "%d,%03d,%03d", value / 1000000, value % 1000000 / 1000, value % 1000);
        } else if (value / 1000 > 0) {
            return String.format(Locale.UK,
                    "%d,%03d", value / 1000, value % 1000);
        } else {
            return String.format(Locale.UK,
                    "%d", value);
        }
    }
}
