package net.team11.pixeldungeon.game.puzzles.invisiblefloor;

import net.team11.pixeldungeon.game.entities.player.Player;
import net.team11.pixeldungeon.game.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.game.entities.puzzle.invisiblefloor.Platform;
import net.team11.pixeldungeon.game.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.game.entitysystem.EntityEngine;
import net.team11.pixeldungeon.game.puzzles.Puzzle;

import java.util.ArrayList;

public class InvisibleFloorPuzzle extends Puzzle {
    private Player player;
    private ArrayList<ArrayList<Platform>> platforms;

    public InvisibleFloorPuzzle(String name) {
        super(name);
    }

    @Override
    public void setupEntities(EntityEngine engine) {
        super.setupEntities(engine);
        player = (Player)engine.getEntities(PlayerComponent.class).get(0);
    }

    @Override
    public void addComponent(PuzzleComponent puzzleComponent) {
        super.addComponent(puzzleComponent);
        if (puzzleComponent instanceof Platform) {
        }
    }
}
