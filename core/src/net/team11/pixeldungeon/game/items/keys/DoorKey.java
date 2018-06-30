package net.team11.pixeldungeon.game.items.keys;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;

public class DoorKey extends Key{
    public DoorKey() {
        super(doorKeyName);
        amount = 1;
        this.image = new Image(Assets.getInstance().getTextureSet(Assets.ITEMS)
                .findRegion(AssetName.DOOR_KEY));
    }
}
