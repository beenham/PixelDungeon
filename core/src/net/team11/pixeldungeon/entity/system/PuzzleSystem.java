package net.team11.pixeldungeon.entity.system;

import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.puzzles.Puzzle;

import java.util.ArrayList;
import java.util.List;

public class PuzzleSystem extends EntitySystem {
    private List<Puzzle> puzzles = new ArrayList<>();

    @Override
    public void init(EntityEngine entityEngine) {
        puzzles = entityEngine.getPuzzles();
    }

    @Override
    public void update(float delta) {
        for (Puzzle puzzle : puzzles) {
            if (puzzle.isActivated()) {
                if (puzzle.isTimed()) {
                    puzzle.setTimer(puzzle.getTimer() - delta);
                    puzzle.update(delta);
                    if (puzzle.getTimer() <= 0) {
                        puzzle.outOfTime();
                    }
                } else if (puzzle.getRemainingAttempts() > 0) {
                    puzzle.update(delta);
                }
            }
        }
    }
}
