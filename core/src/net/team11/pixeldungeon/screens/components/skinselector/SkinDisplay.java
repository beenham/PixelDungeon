package net.team11.pixeldungeon.screens.components.skinselector;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import net.team11.pixeldungeon.inventory.skinselect.Skin;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.inventory.InventoryUtil;

public class SkinDisplay extends Table {
    private float size;
    private Skin skin;
    private Stack skinStack;

    public SkinDisplay(float size, Skin skin) {
        this.size = size;
        this.skin = skin;

        add(skinStack = setupSkinSlot()).size(size);
        setBackground(new NinePatchDrawable(Assets.getInstance().getTextureSet(
                Assets.HUD).createPatch("itemSlot")));
    }

    private Stack setupSkinSlot() {
        Image icon = skin.getImage();
        float width = icon.getWidth() / (icon.getHeight()/size);
        Table iconTable = new Table();
        iconTable.add(icon).size(width,size);
        Stack skinStack = new Stack();
        skinStack.add(iconTable);
        if (!InventoryUtil.getInstance().getSkinSet().hasSkin(skin.getName())) {
            Image lock = new Image(Assets.getInstance().getTextureSet(Assets.HUD).findRegion("itemLock"));
            Table lockTable = new Table();
            lockTable.add(lock).size(size/2).expand().fill().bottom().right();
            skinStack.add(lockTable);
        }
        return skinStack;
    }

    public void update() {
        getCell(skinStack).setActor(skinStack = setupSkinSlot());
    }
}
