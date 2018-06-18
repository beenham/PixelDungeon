package net.team11.pixeldungeon.items.puzzleitems;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.team11.pixeldungeon.entities.puzzle.colouredgems.GemPillar;
import net.team11.pixeldungeon.items.PuzzleItem;
import net.team11.pixeldungeon.puzzles.colouredgems.Colour;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;

public class ColouredGem extends PuzzleItem {
    private Colour colour;
    private GemPillar gemPillar;

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

    public void setGemPillar(GemPillar gemPillar) {
        this.gemPillar = gemPillar;
    }

    public Colour getColour() {
        return colour;
    }
}
