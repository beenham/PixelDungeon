package net.team11.pixeldungeon.game.entity.system;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.game.entities.beams.Beam;
import net.team11.pixeldungeon.game.entities.beams.BeamGenerator;
import net.team11.pixeldungeon.game.entities.beams.BeamTarget;
import net.team11.pixeldungeon.game.entities.beams.Reflector;
import net.team11.pixeldungeon.game.entities.traps.Trap;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.game.entitysystem.EntityEngine;
import net.team11.pixeldungeon.game.entitysystem.EntitySystem;
import net.team11.pixeldungeon.utils.CollisionUtil;
import net.team11.pixeldungeon.utils.RoundTo;

import java.util.ArrayList;
import java.util.List;

public class BeamSystem extends EntitySystem {
    private List<Beam> beams;
    private List<Reflector> reflectors;
    private List<Entity> entities;
    private List<BeamTarget> beamTargets;
    public final static float yOffset = -7f;
    private final float LIGHT_SPEED = 7.5f;

    @Override
    public void init(EntityEngine entityEngine) {
        beams = new ArrayList<>();
        reflectors = new ArrayList<>();
        entities = new ArrayList<>();
        beamTargets = new ArrayList<>();
        List<Entity> allEntities = entityEngine.getEntities();
        for (Entity entity : allEntities) {
            if (entity instanceof Beam) {
                beams.add((Beam)entity);
            } else if (!(entity instanceof Trap)) {
                if (entity instanceof Reflector) {
                    reflectors.add((Reflector)entity);
                }
                if (entity instanceof BeamTarget){
                    beamTargets.add((BeamTarget)entity);
                }
                entities.add(entity);
            }
        }
    }

    @Override
    public void update(float delta) {
        for (Reflector reflector : reflectors) {
            if (reflector.hasBeamIn()) {
                if (!reflector.getBeamIn().isOn()) {
                    reflector.setBeamIn(null);
                }
            }
        }

        for (BeamTarget beamTarget : beamTargets) {
            if (beamTarget.hasBeamIn()) {
                if (!beamTarget.getBeamIn().isOn()) {
                    beamTarget.setBeamIn(null);
                }
            }
        }


        for (Beam beam : beams) {
            if (beam.isOn()) {
                Entity currClosest = null;
                if (beam.hasCurrentClosest()) {
                    currClosest = beam.getCurrentClosest();
                }

                BodyComponent beamBody = beam.getComponent(BodyComponent.class);
                Polygon beamBox = beamBody.getPolygon();

                boolean overlapping = false;
                float beamX = beamBody.getX();
                float beamY = beamBody.getY();

                BodyComponent currClosestBody;
                BodyComponent entityBody;
                Polygon entityBox;

                for (Entity entity : entities) {
                    entityBody = entity.getComponent(BodyComponent.class);
                    entityBox = CollisionUtil.createRectangle(entityBody.getX(), entityBody.getY() - yOffset,
                            entityBody.getWidth(), entityBody.getHeight());
                    float entityY = entityBody.getY() - yOffset;
                    float entityX = entityBody.getX();

                    if (entity instanceof Reflector){
                        Polygon innerBox = ((Reflector)entity).getInnerBounds();
                        if (CollisionUtil.isOverlapping(innerBox, beamBox)) {
                            overlapping = true;

                            if (currClosest != null && currClosest != entity) {
                                currClosest = beam.getCurrentClosest();
                                currClosestBody = currClosest.getComponent(BodyComponent.class);
                                float currClosestY = currClosestBody.getY() - yOffset;
                                float currClosestX = currClosestBody.getX();
                                switch (beam.getBeamDirection()) {
                                    case UP:
                                        if (entityY < currClosestY) {
                                            beam.setCurrentClosest(entity);
                                            ((Reflector) entity).setBeamIn(beam);
                                        } else {
                                            ((Reflector) entity).setBeamIn(null);
                                        }
                                        break;

                                    case DOWN:
                                        if (entityY > currClosestY) {
                                            beam.setCurrentClosest(entity);
                                            ((Reflector) entity).setBeamIn(beam);
                                        } else {
                                            ((Reflector) entity).setBeamIn(null);
                                        }
                                        break;

                                    case LEFT:
                                        if (entityX > currClosestX) {
                                            beam.setCurrentClosest(entity);
                                            ((Reflector) entity).setBeamIn(beam);
                                        } else {
                                            ((Reflector) entity).setBeamIn(null);
                                        }
                                        break;

                                    case RIGHT:
                                        if (entityX < currClosestX) {
                                            beam.setCurrentClosest(entity);
                                            ((Reflector) entity).setBeamIn(beam);
                                        } else {
                                            ((Reflector) entity).setBeamIn(null);
                                        }
                                        break;
                                }
                            } else if (currClosest != entity){
                                beam.setCurrentClosest(entity);
                                ((Reflector) entity).setBeamIn(beam);
                            } else {
                                ((Reflector) entity).setBeamIn(beam);
                            }
                        } else {
                            if (((Reflector) entity).hasBeamIn() && ((Reflector) entity).getBeamIn().equals(beam)) {
                                ((Reflector) entity).setBeamIn(null);
                            }
                        }
                    } else if (entity instanceof BeamTarget){
                        Polygon innerBox = ((BeamTarget)entity).getInnerBounds();
                        if (CollisionUtil.isOverlapping(innerBox, beamBox)) {
                            overlapping = true;

                            if (currClosest != null && currClosest != entity) {
                                float btOffset = -8f;
                                currClosest = beam.getCurrentClosest();
                                currClosestBody = currClosest.getComponent(BodyComponent.class);
                                float currClosestY = currClosestBody.getY() - btOffset;
                                float currClosestX = currClosestBody.getX();
                                switch (beam.getBeamDirection()) {
                                    case UP:
                                        if (entityY < currClosestY) {
                                            beam.setCurrentClosest(entity);
                                            ((BeamTarget) entity).setBeamIn(beam);
                                        } else {
                                            ((BeamTarget) entity).setBeamIn(null);
                                        }
                                        break;

                                    case DOWN:
                                        if (entityY > currClosestY) {
                                            beam.setCurrentClosest(entity);
                                            ((BeamTarget) entity).setBeamIn(beam);
                                        } else {
                                            ((BeamTarget) entity).setBeamIn(null);
                                        }
                                        break;

                                    case LEFT:
                                        if (entityX > currClosestX) {
                                            beam.setCurrentClosest(entity);
                                            ((BeamTarget) entity).setBeamIn(beam);
                                        } else {
                                            ((BeamTarget) entity).setBeamIn(null);
                                        }
                                        break;

                                    case RIGHT:
                                        if (entityX < currClosestX) {
                                            beam.setCurrentClosest(entity);
                                            ((BeamTarget) entity).setBeamIn(beam);
                                        } else {
                                            ((BeamTarget) entity).setBeamIn(null);
                                        }
                                        break;
                                }
                            } else if (currClosest != entity){
                                beam.setCurrentClosest(entity);
                                ((BeamTarget) entity).setBeamIn(beam);
                            } else {
                                ((BeamTarget) entity).setBeamIn(beam);
                            }
                        } else {
                            if (((BeamTarget) entity).hasBeamIn() && ((BeamTarget) entity).getBeamIn().equals(beam)) {
                                ((BeamTarget) entity).setBeamIn(null);
                            }
                        }

                    } else if (CollisionUtil.isOverlapping(entityBox, beamBox)) {
                        overlapping = true;
                        if (currClosest != null && currClosest != entity) {
                            currClosest = beam.getCurrentClosest();
                            currClosestBody = currClosest.getComponent(BodyComponent.class);
                            float currClosestY = currClosestBody.getY() - yOffset;
                            float currClosestX = currClosestBody.getX();
                            switch (beam.getBeamDirection()) {
                                case UP:
                                    if (entityY < currClosestY) {
                                        beam.setCurrentClosest(entity);
                                    }
                                    break;

                                case DOWN:
                                    if (entityY > currClosestY) {
                                        beam.setCurrentClosest(entity);
                                    }
                                    break;

                                case LEFT:
                                    if (entityX > currClosestX) {
                                        beam.setCurrentClosest(entity);
                                    }
                                    break;

                                case RIGHT:
                                    if (entityX < currClosestX) {
                                        beam.setCurrentClosest(entity);
                                    }
                                    break;
                            }
                        } else if (currClosest != entity){
                            beam.setCurrentClosest(entity);
                        }
                    }
                }

                beamBody.recreateBody(BodyDef.BodyType.StaticBody);
                if (overlapping){
                    currClosest = beam.getCurrentClosest();
                    currClosestBody = currClosest.getComponent(BodyComponent.class);
                    float currClosestX = currClosestBody.getX();
                    float currClosestY = currClosestBody.getY() - yOffset;
                    float bodyWidth;
                    float bodyHeight;
                    if (currClosest instanceof Reflector || currClosest instanceof BeamTarget) {
                        bodyWidth = BeamGenerator.BOX_SIZE;
                        bodyHeight = BeamGenerator.BOX_SIZE;
                    } else {
                        bodyWidth = currClosestBody.getWidth();
                        bodyHeight = currClosestBody.getHeight();
                    }

                    switch (beam.getBeamDirection()){
                        case UP:
                            beamY -= beamBody.getHeight()/2;
                            currClosestY -= bodyHeight/2;
                            beamBody.setHeight(distance(beamY, currClosestY) + 0.1f);
                            beamBody.setCoords(beamBody.getX() ,beamY + beamBody.getHeight()/2);
                            break;

                        case DOWN:
                            beamY += beamBody.getHeight()/2;
                            currClosestY += bodyHeight/2;
                            beamBody.setHeight(distance(beamY,currClosestY) + 0.1f);
                            beamBody.setCoords(beamBody.getX() ,beamY - beamBody.getHeight()/2);
                            break;

                        case LEFT:
                            beamX += beamBody.getWidth()/2;
                            currClosestX += bodyWidth/2;
                            beamBody.setWidth(distance(beamX, currClosestX) + 0.1f);
                            beamBody.setCoords(beamX - beamBody.getWidth()/2, beamBody.getY());
                            break;

                        case RIGHT:
                            beamX -= beamBody.getWidth()/2;
                            currClosestX -= bodyWidth/2;
                            beamBody.setWidth(distance(beamX, currClosestX) + 0.1f);
                            beamBody.setCoords(beamX + beamBody.getWidth()/2, beamBody.getY());
                            break;
                    }
                    beamBody.recreateBody(BodyDef.BodyType.StaticBody);
                } else {
                    switch (beam.getBeamDirection()){
                        case UP:
                            beamBody.setHeight(beamBody.getHeight() + LIGHT_SPEED);
                            beamBody.setCoords(beamBody.getX(), beamBody.getY() + LIGHT_SPEED/2f);
                            break;

                        case DOWN:
                            beamBody.setHeight(beamBody.getHeight() + LIGHT_SPEED);
                            beamBody.setCoords(beamBody.getX(), beamBody.getY() - LIGHT_SPEED/2f);
                            break;
                        case LEFT:
                            beamBody.setWidth(beamBody.getWidth() + LIGHT_SPEED);
                            beamBody.setCoords(beamBody.getX() - LIGHT_SPEED/2f, beamBody.getY());
                            break;

                        case RIGHT:
                            beamBody.setWidth(beamBody.getWidth() + LIGHT_SPEED);
                            beamBody.setCoords(beamBody.getX() + LIGHT_SPEED/2f, beamBody.getY());
                            break;
                    }
                    beam.setCurrentClosest(null);
                }
            }
        }
    }

    private float distance(float v1, float v2){
        return RoundTo.RoundToNearest(Math.abs(v2-v1),.1f);
    }
}
