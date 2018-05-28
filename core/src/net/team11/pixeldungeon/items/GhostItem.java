package net.team11.pixeldungeon.items;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GhostItem extends Item {

    public GhostItem(String name){
        super(name, true);
        this.image = new Image();
    }

}
