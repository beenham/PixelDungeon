package net.team11.pixeldungeon.puzzles;

import net.team11.pixeldungeon.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.entities.puzzle.PuzzleController;
import net.team11.pixeldungeon.entitysystem.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Puzzle {
    protected String name;
    private UUID uuid;

    protected boolean completed;
    protected boolean activated;
    protected float attempts;
    protected float maxAttempts;
    protected float timer;
    protected float timerReset;

    protected PuzzleController puzzleController;
    protected HashMap<Integer, PuzzleComponent> puzzleComponents = new HashMap<>();
    protected List<Entity> onCompleteEntities = new ArrayList<>();
    protected List<Entity> onFailEntities = new ArrayList<>();
    private List<String> completeTargets = new ArrayList<>();
    private List<String> failTargets = new ArrayList<>();

    protected Puzzle(String name) {
        this.name = name;
        uuid = UUID.randomUUID();
    }

    public void activate() {
        this.activated = true;
        init();
    }

    protected void init() {}

    public boolean isActivated() {
        return this.activated;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void addComponent(PuzzleComponent puzzleComponent) {
        puzzleComponents.put(puzzleComponents.size(),puzzleComponent);
    }

    public void setController(PuzzleController controller) {
        this.puzzleController = controller;
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

    public String getName() {
        return name;
    }

    public float getTimer() {
        return timer;
    }

    public void setTimer(float timer) {
        this.timer = timer;
        //System.out.println("Time: " + timer);
    }

    protected void resetTimer() {
        timer = timerReset;
    }

    public void deactivate() {
        activated = false;
    }

    public void update(float delta) {

    }

    public void notifyPressed(PuzzleComponent puzzleComponent) {

    }

    @Override
    public String toString() {
        return name + " : " + uuid;
    }
}
