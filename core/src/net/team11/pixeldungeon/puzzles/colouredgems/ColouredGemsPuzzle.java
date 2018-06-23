package net.team11.pixeldungeon.puzzles.colouredgems;

import net.team11.pixeldungeon.entities.blocks.Chest;
import net.team11.pixeldungeon.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.entities.puzzle.PuzzleController;
import net.team11.pixeldungeon.entities.puzzle.colouredgems.GemPillar;
import net.team11.pixeldungeon.entities.puzzle.colouredgems.WallScribe;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.ChestComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.items.puzzleitems.ColouredGem;
import net.team11.pixeldungeon.puzzles.Puzzle;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Messages;
import net.team11.pixeldungeon.utils.stats.StatsUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ColouredGemsPuzzle extends Puzzle {
    private HashMap<Integer,ColouredGem> gems;
    private HashMap<Integer, Boolean> placed;
    private ColouredGemsHints hints;

    private List<String> chests;

    private int numGems;
    private boolean ready;
    public ColouredGemsPuzzle(String name, int numGems, int maxAttempts) {
        super(name);
        super.timed = false;
        gems = new HashMap<>();
        placed = new HashMap<>();
        this.ready = false;
        this.maxAttempts = maxAttempts;
        this.numGems = numGems;
        this.attempts = 0;
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
        boolean ready = true;
        for (int i = 0 ; i < placed.size() ; i++) {
            if (!placed.get(i)) {
                ready = false;
            }
        }
        if (ready) {
            this.ready = true;
            puzzleController.getComponent(AnimationComponent.class).setAnimation(AssetName.PUZZLECONTROLLER_WAITING);
        } else {
            puzzleController.getComponent(AnimationComponent.class).setAnimation(AssetName.PUZZLECONTROLLER_DEACTIVATED);
        }
    }

    @Override
    public void addComponent(PuzzleComponent puzzleComponent) {
        super.addComponent(puzzleComponent);
        if (puzzleComponent instanceof GemPillar) {
            placed.put(((GemPillar) puzzleComponent).getId(),false);
        }
    }

    @Override
    public void notifyPressed(PuzzleComponent puzzleComponent) {
        super.notifyPressed(puzzleComponent);
        if (puzzleComponent instanceof GemPillar) {
            if (((GemPillar) puzzleComponent).hasGem()) {
                placed.put(((GemPillar) puzzleComponent).getId(),true);
            } else {
                placed.put(((GemPillar) puzzleComponent).getId(),false);
            }
        } else if (puzzleComponent instanceof PuzzleController) {
            if (completed) {
                String message = Messages.PUZZLE_COMPLETE;
                PlayScreen.uiManager.initTextBox(message);
            } else if (attempts < maxAttempts) {
                if (ready) {
                    boolean correct = true;
                    for (PuzzleComponent component : puzzleComponents) {
                        if (component instanceof GemPillar) {
                            if (!((GemPillar) component)
                                    .getGem().equals(gems.get(((GemPillar) component).getId()))) {
                                correct = false;
                            }
                        }
                    }
                    if (correct) {
                        onComplete();
                    } else {
                        incrementAttempts();
                        String message = Messages.GEMS_INCORRECT_ORDER + ".\n";
                        if (attempts == maxAttempts) {
                            trigger();
                            message += Messages.PUZZLE_FAILED;
                            puzzleController.getComponent(AnimationComponent.class).setAnimation(AssetName.PUZZLECONTROLLER_ACTIVATED);
                        } else {
                            message += String.format(Locale.UK, Messages.PUZZLE_ATTEMPTS_REMAINING,
                                    (maxAttempts - attempts));
                        }
                        PlayScreen.uiManager.initTextBox(message);
                    }
                } else {
                    String message = Messages.GEMS_NOT_READY + "\n" + Messages.GEMS_CLUES;
                    PlayScreen.uiManager.initTextBox(message);
                }
            } else {
                String message = Messages.PUZZLE_NO_ATTEMPTS;
                PlayScreen.uiManager.initTextBox(message);
            }
        }
    }

    @Override
    protected void onComplete() {
        super.onComplete();
        this.completed = true;
        this.activated = false;
        String message = Messages.GEMS_COMPLETE + ".\n" + Messages.PUZZLE_COMPLETE + ".";
        PlayScreen.uiManager.initTextBox(message);
        completed = true;
        puzzleController.getComponent(AnimationComponent.class).setAnimation(AssetName.PUZZLECONTROLLER_COMPLETED);
        trigger();
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

    @Override
    public void setupEntities(EntityEngine engine) {
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

        ArrayList<WallScribe> scribes = new ArrayList<>();
        for (int i = 0 ; i < puzzleComponents.size() ; i++) {
            if (puzzleComponents.get(i) instanceof WallScribe) {
                scribes.add((WallScribe) puzzleComponents.get(i));
            }
        }

        Collections.shuffle(scribes);
        for (WallScribe wallScribe : scribes) {
            wallScribe.setText(hints.getHint());
        }
    }

    private void incrementAttempts() {
        attempts++;
        StatsUtil.getInstance().getGlobalStats().incrementPuzzleAttempted();
        StatsUtil.getInstance().writeGlobalStats();
        StatsUtil.getInstance().saveTimer();
    }
}
