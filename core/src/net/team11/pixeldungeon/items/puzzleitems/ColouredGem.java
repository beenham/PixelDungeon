package net.team11.pixeldungeon.items.puzzleitems;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.team11.pixeldungeon.items.PuzzleItem;
import net.team11.pixeldungeon.puzzles.colouredgems.Colour;
import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.utils.Assets;

public class ColouredGem extends PuzzleItem {
    private Colour colour;

    public ColouredGem(String name, Colour colour) {
        super(name);
        this.colour = colour;
        switch (colour) {
            case BLUE:
                this.image = new Image(Assets.getInstance().getTextureSet(Assets.ITEMS)
                        .findRegion(AssetName.BLUE_GEM));
                break;
            case PURPLE:
                this.image = new Image(Assets.getInstance().getTextureSet(Assets.ITEMS)
                        .findRegion(AssetName.PURPLE_GEM));
                break;
            case RED:
                this.image = new Image(Assets.getInstance().getTextureSet(Assets.ITEMS)
                        .findRegion(AssetName.RED_GEM));
                break;
            case WHITE:
                this.image = new Image(Assets.getInstance().getTextureSet(Assets.ITEMS)
                        .findRegion(AssetName.WHITE_GEM));
                break;
            case YELLOW:
                this.image = new Image(Assets.getInstance().getTextureSet(Assets.ITEMS)
                        .findRegion(AssetName.YELLOW_GEM));
                break;
            case GREEN:
                this.image = new Image(Assets.getInstance().getTextureSet(Assets.ITEMS)
                        .findRegion(AssetName.GREEN_GEM));
                break;
        }
    }

    public Colour getColour() {
        return colour;
    }
}
