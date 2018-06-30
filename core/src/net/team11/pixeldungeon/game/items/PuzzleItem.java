package net.team11.pixeldungeon.game.items;

import net.team11.pixeldungeon.game.puzzles.Puzzle;

public class PuzzleItem extends Item {
    private Puzzle parentPuzzle;

    protected PuzzleItem(String name) {
        super(name, true);
        super.amount = 1;
    }

    public void setParentPuzzle(Puzzle parentPuzzle) {
        this.parentPuzzle = parentPuzzle;
        parentPuzzle.addItem(this);
    }
}
