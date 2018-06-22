package net.team11.pixeldungeon.entities.mirrors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

public class Reflector extends Entity {

    private Direction reflectDirection;
    
    private Beam beamIn = null;
    private Beam beamOut;
    
    public Reflector(Rectangle bounds, String name, String direction){
        super(name);

        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;

        //this.addComponent(new TrapComponent(this));
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 0,
                (CollisionUtil.TRAP),
                (byte)(CollisionUtil.ENTITY |  CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));    
        
        this.reflectDirection = MirrorUtil.parseDirection(direction);

        AnimationComponent animationComponent;
        this.addComponent(animationComponent = new AnimationComponent(0));
        setupAnimations(animationComponent);
    
    }

    public void setBeamIn(Beam beamIn){
        System.out.println("Beam in " + beamIn);
        this.beamIn = beamIn;
    }

    public void update(EntityEngine engine){
        if (beamIn != null && beamOut == null){
            float posx;
            float posy;

            BodyComponent bodyComponent = this.getComponent(BodyComponent.class);

            switch (reflectDirection){
                case UP:
                    System.out.println("UP");
                    posx = this.getComponent(BodyComponent.class).getX();
                    posy = this.getComponent(BodyComponent.class).getY() + this.getComponent(BodyComponent.class).getHeight()/2;
                    break;

                case DOWN:
                    System.out.println("DOWN");
                    posx = this.getComponent(BodyComponent.class).getX() ;
                    posy = this.getComponent(BodyComponent.class).getY() - this.getComponent(BodyComponent.class).getHeight()/2;
                    break;

                case LEFT:
                    System.out.println("LEFT");
                    posx = this.getComponent(BodyComponent.class).getX() - this.getComponent(BodyComponent.class).getWidth()/2;
                    posy = this.getComponent(BodyComponent.class).getY() ;
                    break;

                case RIGHT:
                    System.out.println("RIGHT");
                    posx = this.getComponent(BodyComponent.class).getX() + this.getComponent(BodyComponent.class).getWidth()/2;
                    posy = this.getComponent(BodyComponent.class).getY() ;
                    break;

                default:
                    posx = bodyComponent.getPolygon().getBoundingRectangle().getX() + bodyComponent.getPolygon().getBoundingRectangle().getWidth()/2;
                    posy = bodyComponent.getPolygon().getBoundingRectangle().getY() + bodyComponent.getPolygon().getBoundingRectangle().getHeight()/2;
                    break;
            }

            //this.beamOut = new Beam(new Rectangle(posx, posy, bodyComponent.getPolygon().getBoundingRectangle().getWidth(), Beam.DEPTH), this.name + "_beam_" + this.reflectDirection, true, this.reflectDirection.toString());
            engine.addEntity(this.beamOut);

        }
    }

    public Direction getReflectDirection() {
        return reflectDirection;
    }

    private void setupAnimations(AnimationComponent animationComponent){
        TextureAtlas textureAtlas = Assets.getInstance().getTextureSet(Assets.BLOCKS);
        animationComponent.addAnimation(AssetName.REFLECTOR, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.setAnimation(AssetName.REFLECTOR);
    }

}
