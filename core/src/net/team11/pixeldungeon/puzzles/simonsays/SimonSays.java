package net.team11.pixeldungeon.puzzles.simonsays;

import net.team11.pixeldungeon.puzzles.Puzzle;

public class SimonSays extends Puzzle{
    public SimonSays(String name, float difficulty, float maxAttempts, float numStages) {
        super(name);
        super.maxAttempts = maxAttempts;
        attempts = 0;
        activated = false;
        completed = false;
    }
}
