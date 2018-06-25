package net.team11.pixeldungeon.entities.mirrors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entitysystem.Entity;

import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.utils.CollisionUtil;
import net.team11.pixeldungeon.utils.Direction;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;

import java.util.ArrayList;

public class Reflector extends Entity {

    private Direction reflectDirection;
    private Beam beamIn = null;

    private final float yOffset = -7;

    //UP, DOWN, LEFT, RIGHT
    //x,y,w,h
    private ArrayList<Beam> beamsOut = new ArrayList<>();

    private EntityEngine engine;

    private Polygon innerBounds;
    
    public Reflector(Rectangle bounds, String name, String direction, EntityEngine engine){
        super(name);

        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;
        this.engine = engine;

        this.innerBounds = CollisionUtil.createRectangle(posX, posY + 7, 6, 6);
        float[] points = innerBounds.getVertices();
        System.out.println("Inner bounds: " + points[0] +  " " + points[8] + " " + points[16] + " " + points[24]);
        System.out.println("Inner Width/Height: " + (points[8] - points[0]) + " " + (points[25] - points[1] ));


        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 0,
                (CollisionUtil.ENTITY),
                (byte)(CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));

        System.out.println("Reflector bounds " + this.getComponent(BodyComponent.class).getX() + " " + (this.getComponent(BodyComponent.class).getX() - this.getComponent(BodyComponent.class).getWidth()/2));

        this.reflectDirection = MirrorUtil.parseDirection(direction);

        AnimationComponent animationComponent;
        this.addComponent(animationComponent = new AnimationComponent(0));
        setupAnimations(animationComponent);

        setupBeams();
    }

    public Polygon getInnerBounds() {
        return this.innerBounds;
    }

    private void setupBeams(){
        BodyComponent bodyComponent = this.getComponent(BodyComponent.class);
        float[] vertices = this.innerBounds.getVertices();
        float rightX = vertices[0]+4 ;

        for (int i = 0 ; i < vertices.length; i++){
            System.out.println(vertices[i]);
        }

        this.beamsOut.add(new Beam(
                new Rectangle(bodyComponent.getX(), bodyComponent.getY() + bodyComponent.getHeight()/2 - yOffset, Beam.WIDTH, Beam.DEPTH),
                this.getName() +  "_BEAM_UP", true, "up", false));

        this.beamsOut.add(new Beam(
                new Rectangle(bodyComponent.getX() - 2, bodyComponent.getY() - bodyComponent.getHeight()/2 - 3, Beam.WIDTH, Beam.DEPTH),
                this.getName() +  "_BEAM_DOWN", true, "down", false));

        this.beamsOut.add(new Beam(
                new Rectangle(bodyComponent.getX()  - bodyComponent.getWidth()/2 - 4.5f, bodyComponent.getY(), Beam.WIDTH, Beam.DEPTH),
                this.getName() +  "_BEAM_LEFT", true, "left", false));

        this.beamsOut.add(new Beam(
                new Rectangle(rightX, bodyComponent.getY() + 4, Beam.WIDTH, Beam.DEPTH),
                this.getName() +  "_BEAM_RIGHT", true, "right", false));

    }

    private boolean hasBeamIn(){
        return (this.beamIn != null);
    }

    public void setBeamIn(Beam beamIn){
        if (!hasBeamIn()){
            this.beamIn = beamIn;
        }
    }

    private void enableBeams(){
        switch (reflectDirection){
            case UP:
                if (!engine.hasEntity(beamsOut.get(0))){
                    engine.addEntity(beamsOut.get(0));

                }
                this.beamsOut.get(0).setOn(true);
                break;
            case DOWN:
                if (!engine.hasEntity(beamsOut.get(1))){
                    engine.addEntity(beamsOut.get(1));

                }
                this.beamsOut.get(1).setOn(true);
                break;
            case LEFT:
                if (!engine.hasEntity(beamsOut.get(2))){
                    engine.addEntity(beamsOut.get(2));

                }
                this.beamsOut.get(2).setOn(true);
                break;
            case RIGHT:
                if (!engine.hasEntity(beamsOut.get(3))){
                    engine.addEntity(beamsOut.get(3));
                }
                this.beamsOut.get(3).setOn(true);
                break;
        }
    }

    public void removeBeamIn(){
        this.beamIn = null;
    }

    public void disableBeam(){
        switch (reflectDirection){
            case UP:
                this.beamsOut.get(0).setOn(false);
                break;
            case DOWN:
                this.beamsOut.get(1).setOn(false);
                break;
            case LEFT:
                this.beamsOut.get(2).setOn(false);
                break;
            case RIGHT:
                this.beamsOut.get(3).setOn(false);
                break;
        }
    }

    private boolean isOutOn(){
        switch (reflectDirection){
            case UP:
                return beamsOut.get(0).isOn();

            case DOWN:
                return beamsOut.get(1).isOn();

            case LEFT:
                return beamsOut.get(2).isOn();

            case RIGHT:
                return beamsOut.get(3).isOn();
        }

        return false;
    }

    public void update(){
                if (beamIn != null && !isOutOn()){
            enableBeams();
        } else if (beamIn != null && isOutOn()){
        } else { disableBeam();
        }
    }

    public Direction getReflectDirection() {
        return reflectDirection;
    }

    private void setupAnimations(AnimationComponent animationComponent){
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        animationComponent.addAnimation(AssetName.BEAM_REFLECTOR_OFF, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.BEAM_REFLECTOR_ON, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(AssetName.BEAM_REFLECTOR_OFF);
    }

}
