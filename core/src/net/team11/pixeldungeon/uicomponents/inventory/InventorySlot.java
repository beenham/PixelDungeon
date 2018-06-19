package net.team11.pixeldungeon.uicomponents.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.items.Item;
import net.team11.pixeldungeon.utils.assets.Assets;

public class InventorySlot extends Table {
    private static float size = 32 * 4 * PixelDungeon.SCALAR;

    public InventorySlot() {
        add().size(size);
        setBackground(new NinePatchDrawable(Assets.getInstance().getTextureSet(
                Assets.HUD).createPatch("itemSlot")));
    }

    public void setItem(Item item) {
        this.getCells().get(0).setActor(new InventoryItem(size,item));
        getCells().get(0).size(size,size);
    }

    public void setItem(Item item, float scalar) {
        float newSize = size * scalar;
        this.getCells().get(0).setActor(new InventoryItem(newSize,item));
        getCells().get(0).size(newSize,newSize);
    }
}
