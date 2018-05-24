package net.team11.pixeldungeon.uicomponents.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.items.Item;
import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.utils.Assets;

import java.util.Locale;

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
