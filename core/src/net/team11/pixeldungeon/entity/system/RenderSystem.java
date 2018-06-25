package net.team11.pixeldungeon.entity.system;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;

import net.team11.pixeldungeon.entities.mirrors.Beam;

import net.team11.pixeldungeon.entities.blocks.PressurePlate;

import net.team11.pixeldungeon.entities.blocks.Torch;
import net.team11.pixeldungeon.entities.mirrors.Reflector;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;

import net.team11.pixeldungeon.entity.component.entitycomponent.BoxComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.DoorComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.TorchComponent;

import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.map.MapManager;

import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.CollisionUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RenderSystem extends EntitySystem {
    public static float FRAME_SPEED = 15;

    private SpriteBatch spriteBatch;
    private List<Entity> entities = null;
    private MapManager mapManager;

    public RenderSystem(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void init(EntityEngine entityEngine) {
        mapManager = MapManager.getInstance();
        entities = entityEngine.getEntities(AnimationComponent.class);
    }

    @Override
    public void update(float delta) {
        mapManager.renderBackGround();
        mapManager.renderEnvironment();

        ArrayList<Entity> entityList = new ArrayList<>(entities.size());

        float bleed = 64;

        Polygon cameraBox = CollisionUtil.createRectangle(
                PlayScreen.gameCam.position.x,
                PlayScreen.gameCam.position.y,
                PlayScreen.gameCam.viewportWidth*0.1f+bleed*2,
                PlayScreen.gameCam.viewportHeight*0.1f+bleed*2);

        ArrayList<Entity> alwaysBottom = new ArrayList<>();
        for (int i = 0 ; i < entities.size() ; i++) {
            Polygon entityBox = entities.get(i).getComponent(BodyComponent.class).getPolygon();
            if (CollisionUtil.isOverlapping(cameraBox,entityBox)) {
                if (i == 0) {
                    entityList.add(entities.get(i));
                } else {
                    int size = entityList.size();
                    for (int j = 0; j < size; j++) {
                        if (entities.get(i) instanceof PressurePlate) {
                            alwaysBottom.add(entities.get(i));
                        } else if (entities.get(i).getComponent(BodyComponent.class).getY() > entityList.get(j).getComponent(BodyComponent.class).getY()) {
                            entityList.add(j, entities.get(i));
                            break;
                        } else if (j == size - 1) {
                            entityList.add(j + 1, entities.get(i));
                            break;
                        }
                    }
                }
            }
        }
        for (Entity entity : alwaysBottom) {
            entityList.add(0,entity);
        }

        spriteBatch.begin();
        for (Entity entity : entityList) {
            if (entity.hasComponent(TorchComponent.class) && ((int)(delta*100000))%6 == 0) {
                ((Torch) entity).setLightSize(new Random().nextInt(10)+40f);
            }
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);
            Animation<TextureRegion> currentAnimation = animationComponent.getCurrentAnimation();
            float width = currentAnimation.getKeyFrame(0).getRegionWidth();
            int height = currentAnimation.getKeyFrame(0).getRegionHeight();


            if (entity instanceof  Reflector){
                ((Reflector)entity).update();
                spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                        bodyComponent.getX() - bodyComponent.getWidth()/2,
                        bodyComponent.getY() - bodyComponent.getHeight()/2,
                        width,
                        height);
            }  else if (!(entity instanceof Beam)) {
                spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                        bodyComponent.getX() - bodyComponent.getWidth()/2,
                        bodyComponent.getY() - bodyComponent.getHeight()/2,
                        width,
                        height);
            } else {
                if (((Beam) entity).isOn()) {
                    spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                            bodyComponent.getX() - bodyComponent.getWidth()/2,
                            bodyComponent.getY() - bodyComponent.getHeight()/2,
                            bodyComponent.getWidth(),
                            bodyComponent.getHeight());
                }
            }

            animationComponent.setStateTime(animationComponent.getStateTime() + (delta * FRAME_SPEED));
        }
        spriteBatch.end();
        mapManager.renderDecor();
    }

    public void updatePaused() {
        mapManager.renderBackGround();
        mapManager.renderEnvironment();
        ArrayList<Entity> entityList = new ArrayList<>(entities.size());
        float bleed = 64;

        Polygon cameraBox = CollisionUtil.createRectangle(
                PlayScreen.gameCam.position.x,
                PlayScreen.gameCam.position.y,
                PlayScreen.gameCam.viewportWidth*0.1f+bleed*2,
                PlayScreen.gameCam.viewportHeight*0.1f+bleed*2);

        for (int i = 0 ; i < entities.size() ; i++) {
            Polygon entityBox = entities.get(i).getComponent(BodyComponent.class).getPolygon();
            if (CollisionUtil.isOverlapping(cameraBox,entityBox)) {
                if (i == 0) {
                    entityList.add(entities.get(i));
                } else {
                    int size = entityList.size();
                    for (int j = 0; j < size; j++) {
                        if (entities.get(i).getComponent(BodyComponent.class).getY() > entityList.get(j).getComponent(BodyComponent.class).getY()) {
                            entityList.add(j, entities.get(i));
                            break;
                        } else if (j == size - 1) {
                            entityList.add(j + 1, entities.get(i));
                            break;
                        }
                    }
                }
            }
        }
        spriteBatch.begin();
        for (Entity entity : entityList) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);
            Animation<TextureRegion> currentAnimation = animationComponent.getCurrentAnimation();
            float width = currentAnimation.getKeyFrame(0).getRegionWidth();
            int height = currentAnimation.getKeyFrame(0).getRegionHeight();
            spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                    bodyComponent.getX() - width/2,
                    bodyComponent.getY() - bodyComponent.getHeight()/2,
                    width,
                    height);
        }
        spriteBatch.end();
        mapManager.renderDecor();
    }
}
