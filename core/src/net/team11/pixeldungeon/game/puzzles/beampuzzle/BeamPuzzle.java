package net.team11.pixeldungeon.game.puzzles.beampuzzle;

import com.badlogic.gdx.math.Polygon;

import net.team11.pixeldungeon.game.entities.beams.BeamTarget;
import net.team11.pixeldungeon.game.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.game.puzzles.Puzzle;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.assets.Messages;

import java.util.ArrayList;
import java.util.List;


/**
 *  Beam Puzzle : Hit all the targets and watch the magic happen!
 */
public class BeamPuzzle extends Puzzle {
    private Polygon bounds;
    private List<BeamTarget> targets;

    public BeamPuzzle(String name, Polygon bounds){
        super(name);
        targets = new ArrayList<>();
        this.bounds = bounds;
        activate();
    }

    @Override
    public void addComponent(PuzzleComponent puzzleComponent) {
        super.addComponent(puzzleComponent);
        System.out.println("ADDED : " + puzzleComponent);
        if (puzzleComponent instanceof BeamTarget) {
            targets.add((BeamTarget)puzzleComponent);
        }
    }

    @Override
    public void update(float delta){
        int targetCount = 0;
        for (BeamTarget entity : targets) {
            if (entity.hasBeamIn()) {
                targetCount++;
            }
        }
        if (targetCount == targets.size()) {
            onComplete();
        }
    }

    @Override
    public void onComplete(){
        super.onComplete();
        activated = false;
        completed = true;
        PlayScreen.uiManager.initTextBox(Messages.BEAM_COMPLETE_01);
        trigger();
    }

    public Polygon getBounds() {
        return bounds;
    }
}
