package net.team11.pixeldungeon.puzzles.simonsays;

import net.team11.pixeldungeon.entities.puzzle.simonsays.SimonSaysSwitch;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.puzzles.Puzzle;
import net.team11.pixeldungeon.utils.AssetName;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimonSays extends Puzzle{
    private float difficulty;
    private float numStages;

    private int currStage = 0;
    private int currStep = 0;
    private int loggedStep = 0;
    private boolean wait;

    private List<List<Integer>> sequences = new ArrayList<>();

    public SimonSays(String name, float difficulty, float maxAttempts, float numStages) {
        super(name);
        super.maxAttempts = maxAttempts;
        this.difficulty = difficulty;
        this.numStages = numStages;
        attempts = 0;
        activated = false;
        completed = false;
        wait = false;
        timerReset = 15 / difficulty;
        timer = timerReset;
    }

    @Override
    protected void init() {
        if (attempts < maxAttempts) {
            puzzleController.getComponent(AnimationComponent.class).setAnimation(AssetName.PUZZLECONTROLLER_ACTIVATED);
            generateSequence();
            resetTimer();
            attempts++;
            for (Entity target : targetEntities) {
                System.out.println(target);
                target.doInteraction(false);
            }
        }
    }

    private void generateSequence() {
        sequences = new ArrayList<>();
        for (int i = 0 ; i < numStages ; i++) {
            ArrayList<Integer> sequence = new ArrayList<>();
            for (int j = 0 ; j < i * difficulty + difficulty * 3 ; j++) {
                sequence.add(j, new Random().nextInt(puzzleComponents.size()));
            }
            System.out.println("SIMONSAYS : " + sequence);
            sequences.add(sequence);
        }
    }

    @Override
    public void update(float delta) {
        if (!wait) {
            for (int i = 0; i < puzzleComponents.size(); i++) {
                if (i == sequences.get(currStage).get(currStep)) {
                    SimonSaysSwitch saysSwitch = (SimonSaysSwitch) puzzleComponents.get(i);
                    if (saysSwitch.isOn()) {
                        saysSwitch.setTimer(saysSwitch.getTimer() - delta);
                        resetTimer();
                        if (saysSwitch.getTimer() <= 0) {
                            saysSwitch.doInteraction(false);
                            currStep++;
                        }
                        break;
                    } else {
                        saysSwitch.doInteraction(false);
                    }
                }
            }
            if (currStep == sequences.get(currStage).size()) {
                currStep = 0;
                wait = true;
            }
        }
        timer -= delta;
    }

    @Override
    public void deactivate() {
        System.out.println("SIMONSAYS : FAILED!");
        super.deactivate();
        wait = false;
        currStep = 0;
        currStage = 0;
        loggedStep = 0;
        resetTimer();
        puzzleController.getComponent(AnimationComponent.class).setAnimation(AssetName.PUZZLECONTROLLER_DEACTIVATED);
        for (Entity target : targetEntities) {
            System.out.println(target);
            target.doInteraction(false);
        }
    }

    public void notifyPressed(SimonSaysSwitch saysSwitch) {
        if (wait) {
            if (saysSwitch.equals(puzzleComponents.get(sequences.get(currStage).get(loggedStep)))) {
                System.out.println("SIMONSAYS : CORRECT!");
                loggedStep++;
                resetTimer();
            } else {
                deactivate();
                return;
            }
            if (loggedStep >= sequences.get(currStage).size()) {
                System.out.println("SIMONSAYS : NEXT STAGE! " + (currStage+1) + " / " + numStages);
                resetTimer();
                wait = false;
                currStage++;
                loggedStep = 0;
                if (currStage == numStages) {
                    complete();
                }
            }
        }
    }

    private void complete() {
        System.out.println("SIMONSAYS : Completed!");
        this.activated = false;
        this.completed = true;
        puzzleController.getComponent(AnimationComponent.class).setAnimation(AssetName.PUZZLECONTROLLER_COMPLETED);
        for (int i = 0 ; i < puzzleComponents.size() ; i++) {
            puzzleComponents.get(i).getComponent(AnimationComponent.class)
                    .setAnimation(AssetName.SS_SWITCH_ON);
        }
    }

    private void resetTimer() {
        timer = timerReset;
    }
}
