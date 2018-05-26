package net.team11.pixeldungeon.uicomponents.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.items.Item;
import net.team11.pixeldungeon.utils.Assets;

public class InventorySlot extends Table {
    private static float size = 32 * 4 * PixelDungeon.SCALAR;

    public InventorySlot() {
        setBackground(new NinePatchDrawable(Assets.getInstance().getTextureSet(
                Assets.HUD).createPatch("itemSlot")));
        add().size(size);
    }

    public void setItem(Item item) {
        this.getCells().get(0).setActor(new InventoryItem(size,item));
    }
}
