package net.team11.pixeldungeon.game.puzzles;

import net.team11.pixeldungeon.game.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.game.entities.puzzle.PuzzleController;
import net.team11.pixeldungeon.game.entities.traps.Trap;
import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.game.entitysystem.EntityEngine;
import net.team11.pixeldungeon.game.items.PuzzleItem;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.Util;
import net.team11.pixeldungeon.utils.assets.Messages;
import net.team11.pixeldungeon.utils.stats.StatsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Puzzle {
    protected String name;
    private UUID uuid;

    protected boolean completed;
    protected boolean activated;
    protected boolean timed;
    protected int attempts;
    protected int maxAttempts;
    protected float timer;
    protected float timerReset;

    protected PuzzleController puzzleController;
    protected ArrayList<PuzzleComponent> puzzleComponents = new ArrayList<>();
    protected ArrayList<PuzzleItem> puzzleItems = new ArrayList<>();

    private List<Entity> onActivateEntities = new ArrayList<>();
    private List<Entity> onCompleteEntities = new ArrayList<>();
    private List<Entity> onFailEntities = new ArrayList<>();
    private List<String> activateTargets = new ArrayList<>();
    private List<String> completeTargets = new ArrayList<>();
    private List<String> failTargets = new ArrayList<>();

    protected Puzzle(String name) {
        this.name = name;
        uuid = UUID.randomUUID();
    }

    public void activate() {
        System.out.println("Activated ");
        this.activated = true;
        init();
    }

    protected void init() {
        Util.getInstance().getStatsUtil().updatePuzzleAttempts();
    }

    public boolean isActivated() {
        return this.activated;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void addComponent(PuzzleComponent puzzleComponent) {
        puzzleComponents.add(puzzleComponents.size(),puzzleComponent);
    }

    public void addItem(PuzzleItem puzzleItem) {
        puzzleItems.add(puzzleItems.size(),puzzleItem);
    }

    public void setController(PuzzleController controller) {
        this.puzzleController = controller;
    }

    public void setActivateTargets(List<String> entities) {
        while (!entities.isEmpty()) {
            if (!activateTargets.contains(entities.get(0))) {
                activateTargets.add(entities.remove(0));
            } else {
                entities.remove(0);
            }
        }
    }

    public void setOnActivateEntities(List<Entity> entities) {
        while (!entities.isEmpty()) {
            if (!onActivateEntities.contains(entities.get(0))) {
                onActivateEntities.add(entities.remove(0));
            } else {
                entities.remove(0);
            }
        }
    }

    public void setCompleteTargets(List<String> entities) {
        while (!entities.isEmpty()) {
            if (!completeTargets.contains(entities.get(0))) {
                completeTargets.add(entities.remove(0));
            } else {
                entities.remove(0);
            }
        }
    }

    public void setOnCompleteEntities(List<Entity> entities) {
        while (!entities.isEmpty()) {
            if (!onCompleteEntities.contains(entities.get(0))) {
                onCompleteEntities.add(entities.remove(0));
            } else {
                entities.remove(0);
            }
        }
    }

    public void setFailTargets(List<String> entities) {
        while (!entities.isEmpty()) {
            if (!failTargets.contains(entities.get(0))) {
                failTargets.add(entities.remove(0));
            } else {
                entities.remove(0);
            }
        }
    }

    public void setOnFailEntities(List<Entity> entities) {
        while (!entities.isEmpty()) {
            if (!onFailEntities.contains(entities.get(0))) {
                onFailEntities.add(entities.remove(0));
            } else {
                entities.remove(0);
            }
        }
    }

    public List<String> getCompleteTargets() {
        return completeTargets;
    }

    public List<String> getFailTargets() {
        return failTargets;
    }

    public List<String> getActivateTargets() {
        return activateTargets;
    }

    public String getName() {
        return name;
    }

    public float getTimer() {
        return timer;
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }

    protected void resetTimer() {
        timer = timerReset;
    }

    public void deactivate() {
        activated = false;
    }

    public void outOfTime() {
        deactivate();
        String message = Messages.PUZZLE_OUT_OF_TIME + ".\n";
        if (getRemainingAttempts() == 0) {
            message += Messages.PUZZLE_FAILED;
        } else {
            message += String.format(Locale.UK,Messages.PUZZLE_ATTEMPTS_REMAINING,getRemainingAttempts());
        }
        PlayScreen.uiManager.initTextBox(message);
    }

    public void update(float delta) {}

    public boolean isTimed() {
        return timed;
    }

    protected void trigger() {
        if (completed) {
            for (Entity entity : onCompleteEntities) {
                if (entity instanceof Trap) {
                    ((Trap) entity).trigger();
                } else {
                    entity.doInteraction(false);
                }
            }
            for (Entity entity : onActivateEntities) {
                if (entity instanceof Trap) {
                    ((Trap) entity).trigger();
                } else {
                    entity.doInteraction(false);
                }
            }
        } else if (getRemainingAttempts() > 0) {
            for (Entity entity : onActivateEntities) {
                if (entity instanceof Trap) {
                    ((Trap) entity).trigger();
                } else {
                    entity.doInteraction(false);
                }
            }
        } else {
            for (Entity entity : onFailEntities) {
                if (entity instanceof Trap) {
                    ((Trap) entity).trigger();
                } else {
                    entity.doInteraction(false);
                }
            }
        }
    }

    public void notifyPressed(PuzzleComponent puzzleComponent) {}

    public ArrayList<PuzzleComponent> getPuzzleComponents() {
        return puzzleComponents;
    }

    public int getRemainingAttempts() {
        return maxAttempts - attempts;
    }

    public void setupEntities(EntityEngine engine){}

    protected void onComplete() {
        Util.getInstance().getStatsUtil().updatePuzzleCompleted();
    }

    @Override
    public String toString() {
        return name + " : " + uuid;
    }
}
