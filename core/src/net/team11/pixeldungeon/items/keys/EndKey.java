package net.team11.pixeldungeon.items.keys;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.utils.Assets;

public class EndKey extends Key {
    public EndKey(String name) {
        super(name, Type.END_KEY);
        amount = 1;
        this.image = new Image(Assets.getInstance().getTextureSet(Assets.ITEMS)
                .findRegion(AssetName.SMALL_KEY));
    }
}
