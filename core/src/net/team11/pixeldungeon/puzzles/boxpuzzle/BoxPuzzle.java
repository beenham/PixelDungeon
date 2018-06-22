package net.team11.pixeldungeon.puzzles.boxpuzzle;

import net.team11.pixeldungeon.entities.blocks.Box;
import net.team11.pixeldungeon.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.BoxComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.ChestComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.puzzles.Puzzle;
import net.team11.pixeldungeon.utils.CollisionUtil;

import java.util.ArrayList;

public class BoxPuzzle extends Puzzle {
    private ArrayList<Box> boxes;
    private ArrayList<String> boxNames;

    public BoxPuzzle(String name) {
        super(name);
        timed = false;
        maxAttempts = 100;
        attempts = 0;

        boxNames = new ArrayList<>();
        boxes = new ArrayList<>();

        activate();
    }

    @Override
    protected void init() {

    }

    @Override
    public void update(float delta) {
        /*
        for (PuzzleComponent component : puzzleComponents) {
            if (component instanceof BoxDock) {
                if (!((BoxDock)component).isDocked()) {
                    for (Box box : boxes) {
                        if (CollisionUtil.isSubmerged(component.getComponent(BodyComponent.class).getPolygon(),
                                box.getComponent(BodyComponent.class).getPolygon())) {
                            System.out.print("BOX SUBMERGED");
                            ((BoxDock) component).setBox(box);
                            component.doInteraction(false);
                        }
                    }
                }
            }
        }
        */
    }

    @Override
    public void setupEntities(EntityEngine engine) {
        while (!boxNames.isEmpty()) {
            String boxName = boxNames.get(0);
            for (Entity entity : engine.getEntities(BoxComponent.class)) {
                if (entity instanceof Box) {
                    if (entity.getName().equals(boxName)) {
                        boxes.add((Box)entity);
                        System.out.println("BOX ADDED TO PUZZLE");
                        boxNames.remove(boxName);
                        break;
                    }
                }
            }
            boxNames.remove(boxName);
        }
    }

    public void setBoxNames(ArrayList<String> boxNames) {
        this.boxNames = boxNames;
    }
}
