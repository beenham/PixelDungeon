package net.team11.pixeldungeon.uicomponents.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.items.Item;
import net.team11.pixeldungeon.utils.Assets;

import java.util.Locale;

public class InventoryItem extends Stack {
    private float size;
    private Item item;

    public InventoryItem(float size, Item item) {
        this.size = size;
        this.item = item;

        if (item != null) {
            setupItemSlot();
        }
    }

    private void setupItemSlot() {
        Image icon = item.getIcon();
        icon.setSize(size,size);
        Table iconTable = new Table();
        iconTable.add(icon).size(size,size);

        Table numberTable = new Table();
        numberTable.bottom().right();
        Label numberLabel = new Label(String.format(Locale.UK,"%d",item.getAmount()),
                new Label.LabelStyle(Assets.getInstance().getFont(Assets.P_FONT), Color.WHITE));
        numberLabel.setFontScale(0.75f * PixelDungeon.SCALAR);
        numberTable.add(numberLabel);

        add(iconTable);
        add(numberTable);
    }
}
