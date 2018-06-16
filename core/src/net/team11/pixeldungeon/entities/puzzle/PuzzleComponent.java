package net.team11.pixeldungeon.entities.puzzle;

import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.puzzles.Puzzle;

public class PuzzleComponent extends Entity {
    protected Puzzle parentPuzzle;

    protected PuzzleComponent(String name) {
        super(name);
    }

    public void setParentPuzzle(Puzzle parentPuzzle) {
        this.parentPuzzle = parentPuzzle;
        parentPuzzle.addComponent(this);
    }
}
