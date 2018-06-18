package net.team11.pixeldungeon.items.keys;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.utils.Assets;

public class DoorKey extends Key{
    public DoorKey(String name) {
        super(name);
        amount = 1;
        this.image = new Image(Assets.getInstance().getTextureSet(Assets.ITEMS)
                .findRegion(AssetName.SMALL_KEY));
    }
}
