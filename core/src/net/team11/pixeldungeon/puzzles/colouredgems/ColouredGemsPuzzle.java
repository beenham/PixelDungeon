package net.team11.pixeldungeon.puzzles.colouredgems;

import net.team11.pixeldungeon.entities.blocks.Chest;
import net.team11.pixeldungeon.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.ChestComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.items.puzzleitems.ColouredGem;
import net.team11.pixeldungeon.puzzles.Puzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ColouredGemsPuzzle extends Puzzle {
    private HashMap<Integer,ColouredGem> gems = new HashMap<>();
    private ColouredGemsHints hints;

    private List<String> chests;

    private int numGems;
    public ColouredGemsPuzzle(String name, int numGems, float maxAttempts) {
        super(name);
        this.maxAttempts = maxAttempts;
        this.numGems = numGems;
        this.attempts = 1;
        activate();
    }

    @Override
    protected void init() {
        ArrayList<Colour> colours = new ArrayList<>();
        colours.add(Colour.BLUE);
        colours.add(Colour.PURPLE);
        colours.add(Colour.RED);
        colours.add(Colour.WHITE);
        colours.add(Colour.YELLOW);
        colours.add(Colour.GREEN);
        while (gems.size() < numGems) {
            int rand = new Random().nextInt(colours.size());
            gems.put(gems.size(),new ColouredGem(colours.get(rand) + " Gem",colours.remove(rand)));
        }

        hints = new ColouredGemsHints(gems);
        System.out.println(this);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void notifyPressed(PuzzleComponent puzzleComponent) {
        super.notifyPressed(puzzleComponent);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("ColourGems: ").append(name).append('\n');
        for (int i = 0 ; i < gems.size() ; i++) {
            s.append(gems.get(i).getColour()).append(" ");
        }
        s.append('\n').append(hints.toString());
        return s.toString();
    }

    public void setChests(List<String> chests) {
        this.chests = chests;
    }

    public void fillChests(EntityEngine engine) {
        Collections.shuffle(chests);
        int item = 0;
        while (!chests.isEmpty()) {
            String chestName = chests.get(0);
            for (Entity entity : engine.getEntities(ChestComponent.class)) {
                if (entity instanceof Chest) {
                    if (entity.getName().equals(chestName)) {
                        chests.remove(chestName);
                        ((Chest) entity).setItem(gems.get(item++));
                        break;
                    }
                }
            }
        }
    }
}
