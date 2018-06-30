package net.team11.pixeldungeon.inventory.skinselect;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.team11.pixeldungeon.inventory.Item;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;

public class Skin extends Item {
    private boolean buyable;
    private String unlock;

    @Override
    public Image getImage() {
        if (image == null) {
            image = new Image(
                    Assets.getInstance().getPlayerTexture(Assets.PLAYER_DEFAULT)
                    .findRegion(AssetName.PLAYER_IDLE_DOWN));
        }
        return image;
    }

    public boolean isBuyable() {
        return buyable;
    }

    public String getUnlock() {
        return unlock;
    }
}
