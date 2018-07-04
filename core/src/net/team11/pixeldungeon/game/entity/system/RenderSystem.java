package net.team11.pixeldungeon.game.entity.system;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.game.entities.beams.Beam;

import net.team11.pixeldungeon.game.entities.blocks.Box;
import net.team11.pixeldungeon.game.entities.blocks.PressurePlate;

import net.team11.pixeldungeon.game.entities.blocks.Torch;
import net.team11.pixeldungeon.game.entities.player.Player;
import net.team11.pixeldungeon.game.entity.component.AnimationComponent;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;

import net.team11.pixeldungeon.game.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.game.entitysystem.EntityEngine;
import net.team11.pixeldungeon.game.entitysystem.EntitySystem;
import net.team11.pixeldungeon.game.map.MapManager;

import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.CollisionUtil;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RenderSystem extends EntitySystem {
    public static float FRAME_SPEED = 15;

    private SpriteBatch spriteBatch;
    private Player player;
    private List<Entity> entities;
    private List<Entity> alwaysBottom;
    private List<Entity> movableEntities;

    private List<Entity> drawList;
    private MapManager mapManager;

    private int loopIterations;
    private boolean init;

    public RenderSystem(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void init(EntityEngine entityEngine) {
        mapManager = MapManager.getInstance();

        player = (Player) entityEngine.getEntities(PlayerComponent.class).get(0);

        movableEntities = new ArrayList<>();
        alwaysBottom = new ArrayList<>();
        entities = new ArrayList<>();

        List<Entity> entityList = entityEngine.getEntities(AnimationComponent.class);
        for (Entity entity : entityList) {
            if (entity instanceof Player
                    || entity instanceof Box) {
                movableEntities.add(entity);
                entities.add(entity);
            } else if (entity instanceof PressurePlate) {
                alwaysBottom.add(entity);
            } else {
                entities.add(entity);
            }
        }

        init = true;

        insertionSort(entities);
    }

    @Override
    public void update(float delta) {
        System.out.println("FRAMERATE : " + 1 / delta);
        if (init) {
            insertionSort(entities);
            init = false;
        }
        loopIterations = 0;

        mapManager.renderBackGround();
        mapManager.renderEnvironment();

        drawList = new ArrayList<>(sortList());
        for (Entity entity : alwaysBottom) {
            drawList.add(0,entity);
        }

        spriteBatch.begin();
        for (Entity entity : sortList()) {
            loopIterations++;
            if (entity instanceof Torch && ((int) (delta * 100000)) % 6 == 0) {
                ((Torch) entity).setLightSize(new Random().nextInt(10) + 40f);
            }
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);
            //System.out.println("Entity : " + entity + " y: " + bodyComponent.getY());
            Animation<TextureRegion> currentAnimation = animationComponent.getCurrentAnimation();


            float bleed = 64 * PixelDungeon.SCALAR;
            Polygon cameraBox = CollisionUtil.createRectangle(
                    PlayScreen.gameCam.position.x,
                    PlayScreen.gameCam.position.y,
                    PlayScreen.gameCam.viewportWidth*0.1f+bleed*2,
                    PlayScreen.gameCam.viewportHeight*0.1f+bleed*2);
            Polygon entityBox = bodyComponent.getPolygon();

            if (CollisionUtil.isOverlapping(cameraBox,entityBox)) {
                float width = currentAnimation.getKeyFrame(0).getRegionWidth();
                int height = currentAnimation.getKeyFrame(0).getRegionHeight();

                if (!(entity instanceof Beam)) {
                    spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                            bodyComponent.getX() - width / 2,
                            bodyComponent.getY() - bodyComponent.getHeight() / 2,
                            width,
                            height);
                } else {
                    if (((Beam) entity).isOn()) {
                        spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                                bodyComponent.getX() - bodyComponent.getWidth() / 2,
                                bodyComponent.getY() - bodyComponent.getHeight() / 2,
                                bodyComponent.getWidth(),
                                bodyComponent.getHeight());
                    }
                }
            }

            animationComponent.setStateTime(animationComponent.getStateTime() + (delta * RenderSystem.FRAME_SPEED));
            if (animationComponent.getCurrentAnimation().getPlayMode() == Animation.PlayMode.NORMAL) {
                if (animationComponent.getCurrentAnimation().isAnimationFinished(animationComponent.getStateTime())) {
                    animationComponent.setAnimation(animationComponent.getPreviousAnimation());
                }
            }
        }
        spriteBatch.end();

        mapManager.renderWallTop();
    }

    public void updatePaused() {
        mapManager.renderBackGround();
        mapManager.renderEnvironment();
        spriteBatch.begin();
        for (Entity entity : drawList) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);
            Animation<TextureRegion> currentAnimation = animationComponent.getCurrentAnimation();
            float width = currentAnimation.getKeyFrame(0).getRegionWidth();
            int height = currentAnimation.getKeyFrame(0).getRegionHeight();

            if (!(entity instanceof Beam)) {
                spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                        bodyComponent.getX() - width/2,
                        bodyComponent.getY() - bodyComponent.getHeight()/2,
                        width,
                        height);
            } else if (((Beam) entity).isOn()) {
                spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                        bodyComponent.getX() - bodyComponent.getWidth() / 2,
                        bodyComponent.getY() - bodyComponent.getHeight() / 2,
                        bodyComponent.getWidth(),
                        bodyComponent.getHeight());
            }
        }
        spriteBatch.end();
        mapManager.renderWallTop();
    }

    private ArrayList<Entity> sortDrawList() {
        float bleed = 64 * PixelDungeon.SCALAR;
        Polygon cameraBox = CollisionUtil.createRectangle(
                PlayScreen.gameCam.position.x,
                PlayScreen.gameCam.position.y,
                PlayScreen.gameCam.viewportWidth*0.1f+bleed*2,
                PlayScreen.gameCam.viewportHeight*0.1f+bleed*2);

        ArrayList<Entity> restOfList = new ArrayList<>();
        ArrayList<Entity> alwaysBottom = new ArrayList<>();
        drawList.add(player);

        for (int i = 0 ; i < entities.size() ; i++) {
            Polygon entityBox = entities.get(i).getComponent(BodyComponent.class).getPolygon();
            if (CollisionUtil.isOverlapping(cameraBox,entityBox)) {
                if (entities.get(i).equals(player)) {
                    continue;
                }
                if (entities.get(i) instanceof PressurePlate) {
                    alwaysBottom.add(entities.get(i));
                } else if (entities.get(i) instanceof Beam){
                    float beamY = entities.get(i).getComponent(BodyComponent.class).getY();
                    float playerY = player.getComponent(BodyComponent.class).getY() - BeamSystem.yOffset;
                    if (beamY > playerY){
                        boolean added = false;
                        int j = drawList.indexOf(player);
                        while (j > 0){
                            if (beamY <= drawList.get(j).getComponent(BodyComponent.class).getY() - BeamSystem.yOffset){
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
                            if (beamY <= drawList.get(j).getComponent(BodyComponent.class).getY() - BeamSystem.yOffset){
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
            drawList.add(0,entity);
        }
        return restOfList;
    }

    private List<Entity> sortList() {
        List<Entity> drawList = entities;
        for (Entity entity : movableEntities) {
            // 5 - 4 - 3 - 2 - 1

            loopIterations++;
            int currIndex = drawList.indexOf(entity);

            float currY = entity.getComponent(BodyComponent.class).getY();
            float prevY;
            float nextY;

            if (currIndex > 0 && currIndex < drawList.size()-1) {
                prevY = drawList.get(currIndex - 1).getComponent(BodyComponent.class).getY();
                nextY = drawList.get(currIndex + 1).getComponent(BodyComponent.class).getY();

                while (!(prevY > currY && currY >= nextY)) {
                    loopIterations++;
                    if (currY >= prevY) {
                        Collections.swap(drawList, currIndex, currIndex - 1);
                        currIndex--;
                    } else if (nextY > currY) {
                        Collections.swap(drawList, currIndex, currIndex + 1);
                        currIndex++;
                    }
                    if (currIndex < drawList.size()-1 && currIndex > 0) {
                        prevY = drawList.get(currIndex - 1).getComponent(BodyComponent.class).getY();
                        nextY = drawList.get(currIndex + 1).getComponent(BodyComponent.class).getY();
                    } else {
                        break;
                    }
                }
            } else if (currIndex == 0) {
                nextY = drawList.get(currIndex + 1).getComponent(BodyComponent.class).getY();

                while (!(currY >= nextY)) {
                    loopIterations++;
                    if (nextY > currY) {
                        Collections.swap(drawList, currIndex, currIndex + 1);
                        currIndex++;
                    }
                    if (currIndex < drawList.size()-1 && currIndex > 0) {
                        nextY = drawList.get(currIndex + 1).getComponent(BodyComponent.class).getY();
                    } else {
                        break;
                    }
                }
            } else if (currIndex == drawList.size() - 1) {
                prevY = drawList.get(currIndex - 1).getComponent(BodyComponent.class).getY();
                while (!(prevY > currY)) {
                    loopIterations++;
                    if (currY >= prevY) {
                        Collections.swap(drawList, currIndex, currIndex - 1);
                        currIndex--;
                    }
                    if (currIndex < drawList.size()-1 && currIndex > 0) {
                        prevY = drawList.get(currIndex - 1).getComponent(BodyComponent.class).getY();
                    } else {
                        break;
                    }
                }
            }
        }
        return drawList;
    }

    private void insertionSort(List<Entity> list) {
        for (int i=1; i < list.size(); i++) {
            Entity key = list.get(i);
            int j = i;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while (j > 0 && list.get(j - 1).getComponent(BodyComponent.class).getY() <=
                    key.getComponent(BodyComponent.class).getY()) {
                list.set(j,list.get(j-1));
                j--;
            }
            list.set(j,key);
        }
    }
}
