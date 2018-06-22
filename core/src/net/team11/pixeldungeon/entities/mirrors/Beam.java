package net.team11.pixeldungeon.entities.mirrors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

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
    private final float LIGHT_SPEED = (float)0.75;

    public static final int DEPTH = 4;
    public static final int DEF_WIDTH = 4;
    private Entity currentClosest = null;

    private boolean on;

    public Beam(Rectangle bounds, String name, boolean enabled, String direction, boolean on){
        super(name, enabled);
        this.enabled = true;
        this.on = on;
        this.beamDirection = MirrorUtil.parseDirection(direction);

        this.damage = DAMAGE;

        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;

        //this.addComponent(new TrapComponent(this));
        this.addComponent(new BodyComponent(DEF_WIDTH, DEPTH, posX, posY, 0,
                (CollisionUtil.TRAP),
                (byte)(CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));

        AnimationComponent animationComponent;
        this.addComponent(animationComponent = new AnimationComponent(0));
        setupAnimations(animationComponent);

    }

    public void update(List<Entity> entities){

        if (on){
            BodyComponent beamComponent = this.getComponent(BodyComponent.class);
            Polygon beamRectangle = beamComponent.getPolygon();
            boolean overlapping = false;

            float beamVertices[] = beamRectangle.getVertices();
            float beamX;
            float entityVertices[];
            float entityX, entityY;

            for (Entity entity : entities){
                Polygon rectangle = entity.getComponent(BodyComponent.class).getPolygon();

                if (!(entity instanceof Beam) && CollisionUtil.isOverlapping(rectangle, beamRectangle)){
                    overlapping = true;

                    //Separate for readability
                    if (currentClosest != null){
                        float tmpVertices[] = entity.getComponent(BodyComponent.class).getPolygon().getVertices();
                        float currClosestVertices[] = currentClosest.getComponent(BodyComponent.class).getPolygon().getVertices();
                        switch (beamDirection){

                            case UP:
                                if (tmpVertices[1] < currClosestVertices[1]){
                                    currentClosest = entity;
                                }
                                break;

                            case DOWN:
                                if (tmpVertices[1] > currClosestVertices[1]){
                                    currentClosest = entity;
                                }
                                break;

                            case LEFT:
                                if (tmpVertices[0] > currClosestVertices[0]){
                                    currentClosest = entity;
                                }
                                break;

                            case RIGHT:
                                if (tmpVertices[0] < currClosestVertices[0]){
                                    currentClosest = entity;
                                }
                                break;
                        }

                    } else {
                        currentClosest = entity;
                    }

                }
            }
            beamComponent.createBody(BodyDef.BodyType.StaticBody);

            if (overlapping){
                entityVertices = currentClosest.getComponent(BodyComponent.class).getPolygon().getVertices();
                entityX = entityVertices[0];

                switch (beamDirection){
                    case UP:
                        float beamY = beamVertices[1];
                        entityY = entityVertices[1];
                        System.out.println("Overlapping with: " + currentClosest);
                        beamComponent.setHeight(distance(beamY, entityY));
                        beamComponent.setCoords(beamComponent.getX() ,beamY + beamComponent.getHeight()/2);
                        break;

                    case DOWN:
                        float beamYD = beamVertices[25];
                        entityY = entityVertices[25];
                        System.out.println("Overlapping with: " + currentClosest);
                        beamComponent.setHeight(distance(beamYD, entityY));
                        beamComponent.setCoords(beamComponent.getX() ,beamYD - beamComponent.getHeight()/2);
                        break;

                    case LEFT:
                        beamX = beamVertices[8];
                        beamComponent.setWidth(distance(beamX, entityX + currentClosest.getComponent(BodyComponent.class).getWidth()));
                        beamComponent.setCoords(beamX - beamComponent.getWidth()/2, beamComponent.getY());
                        break;

                    case RIGHT:
                        beamX = beamVertices[0];
                        beamComponent.setWidth(distance(beamX, entityX));
                        beamComponent.setCoords(beamX + beamComponent.getWidth()/2, beamComponent.getY());
                        break;
                }
            } else {

                switch (beamDirection){
                    case UP:
                        beamComponent.setHeight(beamComponent.getHeight() + LIGHT_SPEED);
                        beamComponent.setCoords( (beamComponent.getX()), (float) (beamComponent.getY() + LIGHT_SPEED /2.0));
                        break;

                    case DOWN:
                        beamComponent.setHeight(beamComponent.getHeight() + LIGHT_SPEED);
                        beamComponent.setCoords( (beamComponent.getX()), (float) (beamComponent.getY() - LIGHT_SPEED /2.0));
                        break;
                    case LEFT:
                        beamComponent.setWidth(beamComponent.getWidth() + LIGHT_SPEED);
                        beamComponent.setCoords((float) (beamComponent.getX() - LIGHT_SPEED /2.0), beamComponent.getY());
                        break;

                    case RIGHT:
                        beamComponent.setWidth(beamComponent.getWidth() + LIGHT_SPEED);
                        beamComponent.setCoords((float) (beamComponent.getX() + LIGHT_SPEED /2.0), beamComponent.getY());
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
