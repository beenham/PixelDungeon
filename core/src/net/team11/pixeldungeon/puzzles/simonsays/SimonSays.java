package net.team11.pixeldungeon.puzzles.simonsays;

import net.team11.pixeldungeon.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.entities.puzzle.simonsays.SimonSaysSwitch;
import net.team11.pixeldungeon.entities.traps.Trap;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.puzzles.Puzzle;
import net.team11.pixeldungeon.utils.AssetName;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Puzzle: Simon Says
 * The classic memory puzzle based off of the game 'Simon'
 */
public class SimonSays extends Puzzle{
    private float difficulty;
    private float numStages;

    private int currStage;
    private int currStep;
    private int loggedStep;
    private int flashAmount;
    private float pauseTime;

    private boolean waiting;
    private boolean paused;
    private boolean initialised;
    private boolean failed;

    private List<List<Integer>> sequences = new ArrayList<>();

    public SimonSays(String name, float difficulty, float maxAttempts, float numStages) {
        super(name);
        super.maxAttempts = maxAttempts;
        this.difficulty = difficulty;
        this.numStages = numStages;

        pauseTime = 0.5f;
        attempts = 0;
        activated = false;
        completed = false;
        timerReset = 15 / difficulty;
        timer = timerReset;
    }

    @Override
    protected void init() {
        if (attempts < maxAttempts) {
            updateAssets(AssetName.SS_SWITCH_IDLE,AssetName.PUZZLECONTROLLER_ACTIVATED);
            generateSequence();
            resetTimer();

            failed = false;
            paused = false;
            waiting = false;
            initialised = false;

            currStep = 0;
            currStage = 0;
            loggedStep = 0;
            flashAmount = 1;
            pauseTime *= 3;

            attempts++;
        }
    }

    @Override
    public void update(float delta) {
        if (!waiting && initialised) {
            if (!paused) {
                for (int i = 0; i < puzzleComponents.size(); i++) {
                    if (i == sequences.get(currStage).get(currStep)) {
                        SimonSaysSwitch saysSwitch = (SimonSaysSwitch) puzzleComponents.get(i);
                        if (saysSwitch.isOn()) {
                            saysSwitch.setTimer(saysSwitch.getTimer() - delta);
                            resetTimer();
                            if (saysSwitch.getTimer() <= 0) {
                                saysSwitch.doInteraction(false);
                                currStep++;
                                paused = true;
                            }
                            break;
                        } else {
                            saysSwitch.doInteraction(false);
                        }
                    }
                }
            } else {
                if (timerReset - timer >= pauseTime) {
                    paused = false;
                    if (currStep == sequences.get(currStage).size()) {
                        currStep = 0;
                        waiting = true;
                        updateAssets(AssetName.SS_SWITCH_WAITING,AssetName.PUZZLECONTROLLER_WAITING);
                    }
                    else if (currStep == 0 && currStage > 0) {
                        pauseTime /= 2;
                    }
                }
            }
        } else if (failed) {
            if (timerReset - timer >= pauseTime) {
                if (flashAmount > 0) {
                    if (paused) {
                        updateAssets(AssetName.SS_SWITCH_OFF,AssetName.PUZZLECONTROLLER_DEACTIVATED);
                        paused = false;
                        flashAmount--;
                        resetTimer();
                    } else {
                        resetTimer();
                        paused = true;
                        updateAssets(AssetName.SS_SWITCH_FAILED,AssetName.PUZZLECONTROLLER_DEACTIVATED);
                    }
                } else {
                    activated = false;
                    if (attempts == maxAttempts) {
                        updateAssets(AssetName.SS_SWITCH_FAILED,AssetName.PUZZLECONTROLLER_ACTIVATED);
                    }
                }
            }
        } else if (completed) {
            if (timerReset - timer >= pauseTime) {
                if (flashAmount > 0) {
                    if (paused) {
                        updateAssets(AssetName.SS_SWITCH_ON,AssetName.PUZZLECONTROLLER_COMPLETED);
                        paused = false;
                        flashAmount--;
                        resetTimer();
                    } else {
                        resetTimer();
                        paused = true;
                        updateAssets(AssetName.SS_SWITCH_IDLE,AssetName.PUZZLECONTROLLER_COMPLETED);
                    }
                } else {
                    activated = false;
                }
            }
        } else if (!initialised) {
            if (timerReset - timer >= pauseTime) {
                if (flashAmount > 0) {
                    if (paused) {
                        updateAssets(AssetName.SS_SWITCH_IDLE,AssetName.PUZZLECONTROLLER_ACTIVATED);
                        paused = false;
                        flashAmount--;
                        resetTimer();
                    } else {
                        resetTimer();
                        paused = true;
                        updateAssets(AssetName.SS_SWITCH_ON,AssetName.PUZZLECONTROLLER_ACTIVATED);
                    }
                } else {
                    initialised = true;

                    pauseTime /= 3;
                    flashAmount = 3;
                }
            }
        } else if (paused) {
            if (timerReset - timer >= pauseTime) {
                if (loggedStep == 0) {
                    updateAssets(AssetName.SS_SWITCH_IDLE,AssetName.PUZZLECONTROLLER_ACTIVATED);
                    waiting = false;
                    resetTimer();
                } else {
                    resetTimer();
                    paused = false;
                    updateAssets(AssetName.SS_SWITCH_WAITING,AssetName.PUZZLECONTROLLER_WAITING);
                }
            }
        }
    }

    @Override
    public void deactivate() {
        failed = true;
        waiting = true;
        resetTimer();
        updateAssets(AssetName.SS_SWITCH_FAILED,AssetName.PUZZLECONTROLLER_DEACTIVATED);
        if (attempts == maxAttempts) {
            trigger();
        }
    }

    public void notifyPressed(PuzzleComponent saysSwitch) {
        if (waiting && !completed && !failed) {
            if (!paused && saysSwitch instanceof SimonSaysSwitch) {
                if (saysSwitch.equals(puzzleComponents.get(sequences.get(currStage).get(loggedStep)))) {
                    saysSwitch.getComponent(AnimationComponent.class).setAnimation(AssetName.SS_SWITCH_ON);
                    puzzleController.getComponent(AnimationComponent.class).setAnimation(AssetName.PUZZLECONTROLLER_COMPLETED);
                    loggedStep++;
                    paused = true;
                    resetTimer();
                } else {
                    deactivate();
                    return;
                }
                if (loggedStep >= sequences.get(currStage).size()) {
                    resetTimer();
                    currStage++;
                    loggedStep = 0;
                    pauseTime *= 2;
                    updateAssets(AssetName.SS_SWITCH_ON,AssetName.PUZZLECONTROLLER_COMPLETED);
                    if (currStage == numStages) {
                        complete();
                    }
                }
            }
        }
    }

    private void generateSequence() {
        sequences = new ArrayList<>();
        for (int i = 0 ; i < numStages ; i++) {
            ArrayList<Integer> sequence = new ArrayList<>();
            for (int j = 0 ; j < i*difficulty + 3 ; j++) {
                sequence.add(j, new Random().nextInt(puzzleComponents.size()));
            }
            sequences.add(sequence);
        }
    }

    private void complete() {
        completed = true;
        pauseTime /= 2;
        paused = true;
        resetTimer();
        trigger();
        updateAssets(AssetName.SS_SWITCH_IDLE,AssetName.PUZZLECONTROLLER_COMPLETED);
    }

    private void trigger() {
        if (completed) {
            for (Entity entity : onCompleteEntities) {
                entity.doInteraction(false);
            }
        } else {
            for (Entity entity : onFailEntities) {
                if (entity instanceof Trap) {
                    ((Trap) entity).trigger();
                } else {
                    entity.doInteraction();
                }
            }
        }
    }

    private void updateAssets(String puzzleComp, String puzzleCont) {
        for (int i = 0 ; i < puzzleComponents.size() ; i++) {
            puzzleComponents.get(i).getComponent(AnimationComponent.class)
                    .setAnimation(puzzleComp);
        }
        puzzleController.getComponent(AnimationComponent.class).setAnimation(puzzleCont);
    }
}
