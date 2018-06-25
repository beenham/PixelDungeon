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

import net.team11.pixeldungeon.entities.player.Player;

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
    private Player player;
    private List<Entity> entities = null;
    private MapManager mapManager;

    public RenderSystem(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void init(EntityEngine entityEngine) {
        mapManager = MapManager.getInstance();

        player = (Player) entityEngine.getEntities(PlayerComponent.class).get(0);
        entities = new ArrayList<>();

        entities = entityEngine.getEntities(AnimationComponent.class);
    }

    @Override
    public void update(float delta) {
        mapManager.renderBackGround();
        mapManager.renderEnvironment();

        ArrayList<Entity> drawList = new ArrayList<>();
        ArrayList<Entity> restOfList = new ArrayList<>();

        float bleed = 64;
        Polygon cameraBox = CollisionUtil.createRectangle(
                PlayScreen.gameCam.position.x,
                PlayScreen.gameCam.position.y,
                PlayScreen.gameCam.viewportWidth*0.1f+bleed*2,
                PlayScreen.gameCam.viewportHeight*0.1f+bleed*2);

        ArrayList<Entity> alwaysBottom = new ArrayList<>();
        drawList.add(player);

        int loopIterations = 0;

        for (int i = 0 ; i < entities.size() ; i++) {
            loopIterations++;
            Polygon entityBox = entities.get(i).getComponent(BodyComponent.class).getPolygon();
            if (CollisionUtil.isOverlapping(cameraBox,entityBox)) {
                if (entities.get(i).equals(player)) {
                    continue;
                }
                if (entities.get(i) instanceof PressurePlate) {
                    alwaysBottom.add(entities.get(i));
                } else if (entities.get(i) instanceof Beam){
                    float beamY = entities.get(i).getComponent(BodyComponent.class).getY();
                    float playerY = player.getComponent(BodyComponent.class).getY() + BeamSystem.yOffset;
                    if (beamY > playerY){
                        boolean added = false;
                        int j = drawList.indexOf(player);
                        while (j > 0){
                            loopIterations++;
                            if (beamY <= drawList.get(j).getComponent(BodyComponent.class).getY() + BeamSystem.yOffset){
                                drawList.add(j+1, entities.get(i));
                                added = true;
                                break;
                            } else{
                                j--;
                            }
                        }

                        if (!added){
                            drawList.add(0, entities.get(i));
                        }
                    } else {
                        boolean added = false;
                        int j = drawList.size()-1;
                        while (j > drawList.indexOf(player)){
                            loopIterations++;
                            if (beamY <= drawList.get(j).getComponent(BodyComponent.class).getY() + BeamSystem.yOffset){
                                drawList.add(j+1, entities.get(i));
                                added = true;
                                break;
                            } else {
                                j--;
                            }
                        }
                        if (!added){
                            drawList.add(j+1, entities.get(i));
                        }
                    }
                } else {
                    float entityY = entities.get(i).getComponent(BodyComponent.class).getY();
                    if (entityY > player.getComponent(BodyComponent.class).getY()) {
                        // FILTER LEFT SIDE OF ARRAY
                        boolean added = false;
                        int j = drawList.indexOf(player) - 1;
                        while (j > 0) {
                            loopIterations++;
                            if (entityY <= drawList.get(j).getComponent(BodyComponent.class).getY()) {
                                drawList.add(j+1,entities.get(i));
                                added = true;
                                break;
                            } else {
                                j--;
                            }
                        }
                        if (!added) {
                            drawList.add(0, entities.get(i));
                        }
                    } else {
                        // FILTER RIGHT SIDE OF ARRAY
                        boolean added = false;
                        int j = drawList.size()-1;
                        while (j > drawList.indexOf(player)) {
                            loopIterations++;
                            if (entityY <= drawList.get(j).getComponent(BodyComponent.class).getY()) {
                                drawList.add(j+1,entities.get(i));
                                added = true;
                                break;
                            } else {
                                j--;
                            }
                        }
                        if (!added) {
                            drawList.add(j+1, entities.get(i));
                        }
                    }
                }
            } else {
                restOfList.add(entities.get(i));
            }
        }
        for (Entity entity : alwaysBottom) {
            loopIterations++;
            drawList.add(0,entity);
        }

        //System.out.println("List: " + drawList.size() +"/"+entities.size());
        //System.out.println("Loop iterations : " + loopIterations);

        spriteBatch.begin();
        for (Entity entity : drawList) {
//            System.out.println(entity.getName());
            if (entity instanceof Torch && ((int)(delta*100000))%6 == 0) {
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

        for (Entity entity : restOfList) {
            AnimationComponent animComp = entity.getComponent(AnimationComponent.class);
            animComp.setStateTime(animComp.getStateTime() + (delta * FRAME_SPEED));
        }

        mapManager.renderWallTop();
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
        mapManager.renderWallTop();
    }
}
