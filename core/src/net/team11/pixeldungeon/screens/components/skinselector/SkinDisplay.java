package net.team11.pixeldungeon.screens.components.skinselector;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import net.team11.pixeldungeon.inventory.skinselect.Skin;
import net.team11.pixeldungeon.utils.assets.Assets;

public class SkinDisplay extends Table {
    private float size;
    private Skin skin;

    public SkinDisplay(float size, Skin skin) {
        this.size = size;
        this.skin = skin;

        add(setupSkinSlot()).size(size);

        setBackground(new NinePatchDrawable(Assets.getInstance().getTextureSet(
                Assets.HUD).createPatch("itemSlot")));
    }

    private Stack setupSkinSlot() {
        Image icon = skin.getImage();
        float width = icon.getWidth() / (icon.getHeight()/size);
        System.out.println("w: " + width);
        System.out.println("h: " + size);
        icon.setSize(width,size);
        Table iconTable = new Table();
        iconTable.add(icon).size(width,size);

        Stack skinStack = new Stack();
        skinStack.add(iconTable);
        return skinStack;
    }
}
