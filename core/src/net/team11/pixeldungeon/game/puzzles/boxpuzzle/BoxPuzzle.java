package net.team11.pixeldungeon.game.puzzles.boxpuzzle;

import net.team11.pixeldungeon.game.entities.puzzle.CompletedIndicator;
import net.team11.pixeldungeon.game.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.game.puzzles.Puzzle;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.assets.Messages;

public class BoxPuzzle extends Puzzle {
    private int completedParts;
    private int maxParts;

    public BoxPuzzle(String name) {
        super(name);
        completedParts = 0;
        maxParts = 0;
        timed = false;
        maxAttempts = 100;
        attempts = 0;

        activate();
    }

    @Override
    public void notifyPressed(PuzzleComponent puzzleComponent) {
        if (!completed && activated) {
            if (puzzleComponent instanceof CompletedIndicator) {
                if (((CompletedIndicator) puzzleComponent).isOn()) {
                    completedParts++;
                    if (completedParts == maxParts) {
                        onComplete();
                    }
                } else {
                    completedParts--;
                }
            }
        }
    }

    @Override
    public void addComponent(PuzzleComponent puzzleComponent) {
        super.addComponent(puzzleComponent);
        if (puzzleComponent instanceof CompletedIndicator) {
            maxParts++;
        }
    }

    @Override
    protected void onComplete() {
        super.onComplete();
        completed = true;
        activated = false;
        trigger();
        String message = Messages.PUZZLE_COMPLETE;
        PlayScreen.uiManager.initTextBox(message);
    }
}
