package net.team11.pixeldungeon.entities.mirrors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entities.traps.Trap;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;


import net.team11.pixeldungeon.entitysystem.Entity;

import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.utils.Assets;
import net.team11.pixeldungeon.utils.CollisionCategory;
import net.team11.pixeldungeon.utils.Direction;



import java.util.List;


public class Beam extends Trap {

    private Direction beamDirection;

    private final int DAMAGE = 100;
    private final float LIGHT_SPEED = (float)0.75;

    public static final int DEPTH = 4;
    public static final int DEF_WIDTH = 4;
    private Entity currentClosest = null;

    public Beam(Rectangle bounds, String name, boolean enabled, String direction){
        super(name, enabled);
        this.enabled = true;

        this.beamDirection = MirrorUtil.parseDirection(direction);

        this.damage = DAMAGE;


        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;

        //this.addComponent(new TrapComponent(this));
        this.addComponent(new BodyComponent(DEF_WIDTH, DEPTH, posX, posY, 0,
                (CollisionCategory.TRAP),
                (byte)(CollisionCategory.PUZZLE_AREA | CollisionCategory.BOUNDARY),
                BodyDef.BodyType.StaticBody));

        AnimationComponent animationComponent;
        this.addComponent(animationComponent = new AnimationComponent(0));
        setupAnimations(animationComponent);

    }

    public void update(List<Entity> entities){
        BodyComponent beamComponent = this.getComponent(BodyComponent.class);
        Rectangle beamRectangle = beamComponent.getRectangle();
        boolean overlapping = false;

        for (Entity entity : entities){
            Rectangle rectangle = entity.getComponent(BodyComponent.class).getRectangle();
            if (!(entity instanceof Beam) && beamRectangle.overlaps(rectangle)){
                overlapping = true;
                currentClosest = entity;
            }
        }
        if (overlapping){

            if (currentClosest instanceof Reflector){
                ((Reflector)currentClosest).setBeamIn(this);
            }

            //Directional Conditions
            switch (beamDirection){
                case UP:

                    break;

                case DOWN:
                    System.out.println("Overlapping down");
                    beamComponent.setWidth(distance(beamRectangle.getY(), currentClosest.getComponent(BodyComponent.class).getRectangle().getY()));
                    beamComponent.setCoords(beamRectangle.getX(), beamComponent.getY() + beamComponent.getHeight()/2);
                    break;

                case LEFT:
                    beamComponent.setWidth(distance(beamRectangle.getX(), currentClosest.getComponent(BodyComponent.class).getRectangle().getX()));
                    beamComponent.setCoords(beamRectangle.getX() + beamComponent.getWidth()/2, beamComponent.getY());
                    break;

                case RIGHT:
                    beamComponent.setWidth(distance(beamRectangle.getX(), currentClosest.getComponent(BodyComponent.class).getRectangle().getX()));
                    beamComponent.setCoords(beamRectangle.getX() + beamComponent.getWidth()/2, beamComponent.getY());
                    break;
            }

        }

        else {
            beamComponent.createBody(BodyDef.BodyType.StaticBody);
            switch (beamDirection){
                case UP:

                    break;

                case DOWN:
                    beamComponent.setWidth(DEF_WIDTH);
                    beamComponent.setHeight(beamComponent.getHeight() - LIGHT_SPEED);
                    beamComponent.setCoords((beamComponent.getX()), (float) (beamComponent.getY() - LIGHT_SPEED /2.0));
                    break;

                case LEFT:
                    beamComponent.setWidth(beamComponent.getWidth() + LIGHT_SPEED);
                    beamComponent.setCoords((float) (beamComponent.getX() - LIGHT_SPEED /2.0), beamComponent.getY());
                    break;

                case RIGHT:
                    beamComponent.setWidth(beamComponent.getWidth()+ LIGHT_SPEED);
                    beamComponent.setCoords((float) (beamComponent.getX() + LIGHT_SPEED /2.0), beamComponent.getY());
                    break;
            }

        }

    }

    public Direction getBeamDirection() {
        return beamDirection;
    }

    private float distance(float x1, float x2){
        return Math.abs(x2-x1);
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

    @Override
    public String toString(){
        return this.getName() + " --> " + this.getBeamDirection();
    }
}
