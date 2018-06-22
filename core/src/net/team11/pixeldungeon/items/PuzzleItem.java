package net.team11.pixeldungeon.items;

import net.team11.pixeldungeon.puzzles.Puzzle;

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
