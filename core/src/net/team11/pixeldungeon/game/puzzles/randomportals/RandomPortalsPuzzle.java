package net.team11.pixeldungeon.game.puzzles.randomportals;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import net.team11.pixeldungeon.game.entities.player.Player;
import net.team11.pixeldungeon.game.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.game.entities.puzzle.randomportals.Portal;
import net.team11.pixeldungeon.game.entities.puzzle.randomportals.PortalExit;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entity.component.VelocityComponent;
import net.team11.pixeldungeon.game.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.game.entitysystem.EntityEngine;
import net.team11.pixeldungeon.game.puzzles.Puzzle;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.CollisionUtil;
import net.team11.pixeldungeon.utils.Direction;
import net.team11.pixeldungeon.utils.assets.Messages;

import java.util.HashMap;
import java.util.Random;

public class RandomPortalsPuzzle extends Puzzle {
    private Player player;

    private int stages;
    private HashMap<Integer, HashMap<Integer, Portal>> portals;
    private PortalExit portalExit;

    public RandomPortalsPuzzle(String name, int stages) {
        super(name);
        this.stages = stages;

        portals = new HashMap<>();
        for (int i = 0 ; i < stages ; i++) {
            portals.put(i, new HashMap<Integer, Portal>());
        }
    }

    @Override
    protected void init() {
        super.init();
        HashMap<Integer, Portal> forwardPortals = new HashMap<>();
        HashMap<Integer, Portal> backPortals = new HashMap<>();
        for (int i = 0 ; i < stages ; i++) {
            Random rand = new Random();
            HashMap<Integer,Portal> currPortals = portals.get(i);
            while (!forwardPortals.containsKey(i)) {
                Portal portal = currPortals.get(rand.nextInt(currPortals.size()));
                if (portal.isLinkable()) {
                    forwardPortals.put(i, portal);
                    if (i < stages - 1) {
                        forwardPortals.get(i)
                                .setTargetCoords(portals.get(i + 1).get(rand.nextInt(portals.get(i + 1).size()))
                                        .getComponent(BodyComponent.class).getCoords());
                    } else if (i == stages - 1) {
                        forwardPortals.get(i)
                                .setTargetCoords(portalExit.getComponent(BodyComponent.class).getCoords());
                    }
                }
            }
            if (i > 0) {
                while (!backPortals.containsKey(i)) {
                    Portal portal = portals.get(i).get(rand.nextInt(portals.get(i).size()));
                    if (!forwardPortals.containsValue(portal) && portal.isLinkable()) {
                        backPortals.put(i,portal);
                        if (i < stages) {
                            backPortals.get(i)
                                    .setTargetCoords(portals.get(i-1).get(rand.nextInt(portals.get(i-1).size()))
                                            .getComponent(BodyComponent.class).getCoords());
                        }
                    }
                }
            }

            for (int j = 0 ; j < currPortals.size() ; j++) {
                Random random = new Random();
                Portal currPortal = currPortals.get(j);
                if (!(backPortals.containsValue(currPortal)
                        || forwardPortals.containsValue(currPortal))
                        && currPortal.isLinkable()) {
                    int randomPortalSet = 0;
                    if (i > 0) {
                        randomPortalSet = random.nextInt(i);
                    }
                    currPortal.setTargetCoords(portals.get(randomPortalSet)
                            .get(random.nextInt(portals.get(randomPortalSet).size()))
                                    .getComponent(BodyComponent.class).getCoords());
                }
            }
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        BodyComponent playerBody = player.getComponent(BodyComponent.class);
        VelocityComponent playerVel = player.getComponent(VelocityComponent.class);
        Vector2 exitCoords = portalExit.getComponent(BodyComponent.class).getCoords();

        for (int i = 0 ; i < stages ; i++)  {
            HashMap<Integer,Portal> currPortals = portals.get(i);
            for (int j = 0 ; j < currPortals.size() ; j++) {
                Portal currPortal = currPortals.get(j);
                if (currPortal.isLinkable() && !playerVel.isParalyzed()) {
                    Polygon portalBox = currPortal.getComponent(BodyComponent.class).getPolygon();
                    if (CollisionUtil.isSubmerged(portalBox,playerBody.getPolygon())) {
                        playerBody.setCoords(currPortal.getTargetCoords());
                        if (currPortal.getTargetCoords().equals(exitCoords)) {
                            playerVel.setDirection(Direction.UP);
                            playerVel.setyDirection(1);
                            onComplete();
                        } else {
                            playerVel.setDirection(Direction.DOWN);
                            playerVel.setyDirection(-1);
                        }
                        playerBody.move(0, 2);
                        playerVel.paralyze(4f);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void addComponent(PuzzleComponent puzzleComponent) {
        super.addComponent(puzzleComponent);
        if (puzzleComponent instanceof Portal) {
            Portal portal = (Portal) puzzleComponent;
            HashMap<Integer,Portal> map = portals.get(portal.getStage());
            map.put(map.size(),portal);
        } else if (puzzleComponent instanceof PortalExit) {
            portalExit = (PortalExit) puzzleComponent;
        }
    }

    @Override
    public void setupEntities(EntityEngine engine) {
        player = (Player) engine.getEntities(PlayerComponent.class).get(0);
        activate();
    }

    @Override
    protected void onComplete() {
        super.onComplete();
        completed = true;
        activated = false;
        attempts++;
        PlayScreen.uiManager.initTextBox(Messages.PORTAL_COMPLETE);
    }
}