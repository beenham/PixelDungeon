package net.team11.pixeldungeon.items;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.utils.Assets;

public class Key extends Item {

    private String keyName;

    public Key(String id, String name) {
        super(id, true);
        amount = 1;
        this.keyName = name;
        this.image = new Image(Assets.getInstance().getTextureSet(Assets.ITEMS)
                .findRegion(AssetName.SMALL_KEY));
    }

    public void setKeyName(String name){
        this.keyName = name;
    }

    public String getKeyName(){
        return this.keyName;
    }
}
