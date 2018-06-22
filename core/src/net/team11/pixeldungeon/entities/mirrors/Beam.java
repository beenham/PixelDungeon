package net.team11.pixeldungeon.entities.mirrors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entities.traps.Trap;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;


import net.team11.pixeldungeon.entitysystem.Entity;

import net.team11.pixeldungeon.utils.CollisionUtil;
import net.team11.pixeldungeon.utils.Direction;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;


import java.util.List;


public class Beam extends Trap {

    private Direction beamDirection;

    private final int DAMAGE = 100;
    private final float LIGHT_SPEED = 7.5f;
    private float originX;
    private float originY;

    public static final int DEPTH = 4;
    private static final int DEF_WIDTH = 4;
    private Entity currentClosest = null;

    private boolean on;

    public Beam(Rectangle bounds, String name, boolean enabled, String direction, boolean on){
        super(name, enabled);
        this.enabled = true;
        this.on = on;
        this.beamDirection = MirrorUtil.parseDirection(direction);

        this.damage = DAMAGE;

        originX = bounds.getX() + bounds.getWidth()/2;
        originY = bounds.getY() + bounds.getHeight()/2;

        //this.addComponent(new TrapComponent(this));
        this.addComponent(new BodyComponent(DEF_WIDTH, DEPTH, originX, originY, 0,
                (CollisionUtil.TRAP),
                (byte)(CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));

        AnimationComponent animationComponent;
        this.addComponent(animationComponent = new AnimationComponent(0));
        setupAnimations(animationComponent);

    }

    public void update(List<Entity> entities){
        if (on){
            BodyComponent beamBody = this.getComponent(BodyComponent.class);
            Polygon beamBox = beamBody.getPolygon();

            boolean overlapping = false;
            float beamX = beamBody.getX();
            float beamY = beamBody.getY();

            BodyComponent currClosestBody;
            BodyComponent entityBody;
            Polygon entityBox;

            for (Entity entity : entities){
                entityBody = entity.getComponent(BodyComponent.class);
                entityBox = entityBody.getPolygon();

                if (!(entity instanceof Beam || entity instanceof Trap) && CollisionUtil.isOverlapping(entityBox, beamBox)){
                    overlapping = true;
                    //Separate for readability
                    if (currentClosest != null){
                        currClosestBody = currentClosest.getComponent(BodyComponent.class);
                        switch (beamDirection) {
                            case UP:
                                if (entityBody.getY() < currClosestBody.getY()) {
                                    currentClosest = entity;
                                }
                                break;

                            case DOWN:
                                if (entityBody.getY() > currClosestBody.getY()) {
                                    currentClosest = entity;
                                }
                                break;

                            case LEFT:
                                if (entityBody.getX() > currClosestBody.getX()) {
                                    currentClosest = entity;
                                }
                                break;

                            case RIGHT:
                                if (entityBody.getX() < currClosestBody.getX()) {
                                    currentClosest = entity;
                                }
                                break;
                        }

                    } else {
                        currentClosest = entity;
                    }
                    if (entity instanceof Reflector) {
                        //((Reflector) entity).setBeamIn(this);
                    }
                } else if (entity instanceof Reflector) {
                    // set null if curr beam in is this
                }
            }
            beamBody.createBody(BodyDef.BodyType.StaticBody);

            if (overlapping){
                currClosestBody = currentClosest.getComponent(BodyComponent.class);
                float currClosestX = currClosestBody.getX();
                float currClosestY = currClosestBody.getY();

                switch (beamDirection){
                    case UP:
                        beamY -= beamBody.getHeight()/2;
                        currClosestY -= currClosestBody.getHeight()/2;
                        beamBody.setHeight(distance(beamY, currClosestY));
                        beamBody.setCoords(beamBody.getX() ,beamY + beamBody.getHeight()/2);
                        break;

                    case DOWN:
                        beamY += beamBody.getHeight()/2;
                        currClosestY += currClosestBody.getHeight()/2;
                        beamBody.setHeight(distance(beamY,currClosestY));
                        beamBody.setCoords(beamBody.getX() ,beamY - beamBody.getHeight()/2);
                        break;

                    case LEFT:
                        beamX += beamBody.getWidth()/2;
                        currClosestX += currClosestBody.getWidth()/2;
                        beamBody.setWidth(distance(beamX, currClosestX));
                        beamBody.setCoords(beamX - beamBody.getWidth()/2, beamBody.getY());
                        break;

                    case RIGHT:
                        beamX -= beamBody.getWidth()/2;
                        currClosestX -= currClosestBody.getWidth()/2;
                        beamBody.setWidth(distance(beamX, currClosestX));
                        beamBody.setCoords(beamX + beamBody.getWidth()/2, beamBody.getY());
                        break;
                }
                beamBody.createBody(BodyDef.BodyType.StaticBody);
            } else {
                switch (beamDirection){
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
                currentClosest = null;
            }
        }
    }

    public Direction getBeamDirection() {
        return beamDirection;
    }

    //Method to get the distance between two vertices
    private float distance(float v1, float v2){
        return Math.abs(v2-v1);
    }

    private void setupAnimations(AnimationComponent animationComponent){
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        if (beamDirection == Direction.RIGHT || beamDirection == Direction.LEFT) {
            animationComponent.addAnimation(AssetName.BEAM, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
            animationComponent.setAnimation(AssetName.BEAM);
        } else {
            animationComponent.addAnimation(AssetName.BEAM_UD, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
            animationComponent.setAnimation(AssetName.BEAM_UD);
        }
    }

    public boolean isOn() {
        return this.on;
    }

    @Override
    public String toString(){
        return this.getName() + " --> " + this.getBeamDirection();
    }
}
