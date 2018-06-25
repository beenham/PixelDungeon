package net.team11.pixeldungeon.puzzles.beamPuzzle;

import net.team11.pixeldungeon.entities.beams.BeamTarget;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.puzzles.Puzzle;


public class BeamPuzzle extends Puzzle {

    public BeamPuzzle(String name){
        super(name);
        System.out.println("Success creating Beam Puzzle");
    }

    @Override
    public void init(){

    }

    @Override
    public void update(float delta){
        int targetCount = 0;
        for (Entity entity : puzzleComponents){
            if (entity instanceof BeamTarget && ((BeamTarget)entity).hasBeamIn()){
                targetCount++;
            }
        }

        if (targetCount == puzzleComponents.size()){
            System.out.println("All targets hit, completing puzzle");
        }
    }

    @Override
    public void onComplete(){

    }
}
