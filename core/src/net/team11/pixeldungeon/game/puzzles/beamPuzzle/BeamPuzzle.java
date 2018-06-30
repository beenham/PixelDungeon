package net.team11.pixeldungeon.game.puzzles.beamPuzzle;

import net.team11.pixeldungeon.game.entities.beams.BeamTarget;
import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.game.puzzles.Puzzle;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.assets.Messages;


/**
 *  Beam Puzzle : Hit all the targets and watch the magic happen!
 */
public class BeamPuzzle extends Puzzle {

    public BeamPuzzle(String name){
        super(name);
        maxAttempts = 1;
        attempts = 0;
        activate();
    }

    @Override
    public void update(float delta){
        int targetCount = 0;
        for (Entity entity : puzzleComponents) {
            if (entity instanceof BeamTarget && ((BeamTarget) entity).hasBeamIn()) {
                targetCount++;
            }
        }
        if (targetCount == puzzleComponents.size()) {
            onComplete();
        }
    }

    @Override
    public void onComplete(){
        super.onComplete();
        activated = false;
        this.completed = true;
        String message = Messages.BEAM_COMPLETE_01 + ".\n";
        PlayScreen.uiManager.initTextBox(message);
        completed = true;
        trigger();
    }
}
