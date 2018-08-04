package net.team11.pixeldungeon.game.entity.system;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.game.entities.beams.Beam;
import net.team11.pixeldungeon.game.entities.beams.BeamReflectorMovable;
import net.team11.pixeldungeon.game.entities.blocks.Box;
import net.team11.pixeldungeon.game.entities.blocks.PressurePlate;
import net.team11.pixeldungeon.game.entities.blocks.Torch;
import net.team11.pixeldungeon.game.entities.player.Player;
import net.team11.pixeldungeon.game.entity.component.AnimationComponent;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;
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
    private List<Entity> entities;
    private List<Entity> alwaysBottom;
    private List<Entity> movableEntities;

    private List<Entity> drawList;
    private MapManager mapManager;

    private boolean init;

    public RenderSystem(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void init(EntityEngine entityEngine) {
        mapManager = MapManager.getInstance();

        movableEntities = new ArrayList<>();
        alwaysBottom = new ArrayList<>();
        entities = new ArrayList<>();

        List<Entity> entityList = entityEngine.getEntities(AnimationComponent.class);
        for (Entity entity : entityList) {
            if (entity instanceof Player
                    || entity instanceof Box
                    || entity instanceof Beam
                    || entity instanceof BeamReflectorMovable) {
                movableEntities.add(entity);
                entities.add(entity);
            } else if (entity instanceof PressurePlate) {
                alwaysBottom.add(entity);
            } else {
                entities.add(entity);
            }
        }

        init = true;
    }

    @Override
    public void update(float delta) {
        if (init) {
            insertionSort(entities);
            init = false;
        }

        mapManager.renderBackGround();
        mapManager.renderEnvironment();

        drawList = new ArrayList<>(sortList());
        for (Entity entity : alwaysBottom) {
            drawList.add(0,entity);
        }

        float bleed = 64 * PixelDungeon.SCALAR;
        Polygon cameraBox = CollisionUtil.createRectangle(
                PlayScreen.gameCam.position.x,
                PlayScreen.gameCam.position.y,
                PlayScreen.gameCam.viewportWidth*0.1f+bleed*2,
                PlayScreen.gameCam.viewportHeight*0.1f+bleed*2);

        spriteBatch.begin();
        for (Entity entity : drawList) {
            if (entity instanceof Torch && ((int) (delta * 100000)) % 6 == 0) {
                ((Torch) entity).setLightSize(new Random().nextInt(10) + 40f);
            }
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);
            Animation<TextureRegion> currentAnimation = animationComponent.getCurrentAnimation();

            Polygon entityBox = bodyComponent.getPolygon();

            if (CollisionUtil.isOverlapping(cameraBox,entityBox)) {
                float width = currentAnimation.getKeyFrame(animationComponent.getStateTime()).getRegionWidth();
                float height = currentAnimation.getKeyFrame(animationComponent.getStateTime()).getRegionHeight();

                if (!(entity instanceof Beam)) {
                    if (entity instanceof Player) {
                        if (((Player) entity).getScale() > 0f) {
                            spriteBatch.setColor(1, 1, 1, ((Player) entity).getScale());
                            TextureRegion texture = new TextureRegion(currentAnimation.getKeyFrame(animationComponent.getStateTime(),true));
                            switch (((Player)entity).getDepth()) {
                                case ONE_QUART:
                                    texture.setRegionHeight((int)(height * 1/3));
                                    break;
                                case TWO_QUART:
                                    texture.setRegionHeight((int)(height * 2/5));
                                    break;
                                case THREE_QUART:
                                    texture.setRegionHeight((int)(height * 3/4));
                                    break;
                                case FOUR_QUART:
                                    texture.setRegionHeight((int)(height));
                                    break;
                            }
                            spriteBatch.draw(texture,
                                    bodyComponent.getX() - ((width * ((Player) entity).getScale()) / 2),
                                    bodyComponent.getY() - ((bodyComponent.getHeight() * ((Player) entity).getScale()) / 2),
                                    width * ((Player) entity).getScale(),
                                    height * ((Player) entity).getScale() - (height-texture.getRegionHeight()));
                            spriteBatch.setColor(1, 1, 1, 1);
                        }
                    } else {
                        spriteBatch.draw(currentAnimation.getKeyFrame(animationComponent.getStateTime(), true),
                                bodyComponent.getX() - width / 2,
                                bodyComponent.getY() - bodyComponent.getHeight() / 2,
                                width,
                                height);
                    }
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
            float width = currentAnimation.getKeyFrame(animationComponent.getStateTime()).getRegionWidth();
            int height = currentAnimation.getKeyFrame(animationComponent.getStateTime()).getRegionHeight();

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

    private List<Entity> sortList() {
        List<Entity> drawList = entities;
        for (Entity entity : movableEntities) {
            // 5 - 4 - 3 - 2 - 1
            int currIndex = drawList.indexOf(entity);

            float currY;
            float prevY;
            float nextY;

            if (currIndex > 0 && currIndex < drawList.size()-1 && entities.size() > 1) {
                prevY = getPrevY(entity);
                currY = getCurrY(entity,true);

                if (currY >= prevY) {
                    while (!(prevY > currY)) {
                        Collections.swap(drawList, currIndex, currIndex - 1);
                        currIndex--;
                        if (currIndex < drawList.size() - 1 && currIndex > 0) {
                            prevY = getPrevY(entity);
                            currY = getCurrY(entity, true);
                        } else {
                            break;
                        }
                    }
                }

                currY = getCurrY(entity,false);
                nextY = getNextY(entity);


                if (nextY > currY) {
                    while (!(currY >= nextY)) {
                        Collections.swap(drawList, currIndex, currIndex + 1);
                        currIndex++;
                        if (currIndex < drawList.size() - 1 && currIndex > 0) {
                            nextY = getNextY(entity);
                            currY = getCurrY(entity, false);
                        } else {
                            break;
                        }
                    }
                }
            } else if (currIndex == 0 && entities.size() > 1) {
                nextY = getNextY(entity);
                currY = getCurrY(entity,false);

                while (!(currY >= nextY)) {
                    if (nextY > currY) {
                        Collections.swap(drawList, currIndex, currIndex + 1);
                        currIndex++;
                    }
                    if (currIndex < drawList.size()-1 && currIndex > 0) {
                        nextY = getNextY(entity);
                        currY = getCurrY(entity,false);
                    } else {
                        break;
                    }
                }
            } else if (currIndex == drawList.size() - 1 && entities.size() > 1) {
                prevY = getPrevY(entity);
                currY = getCurrY(entity,true);
                while (!(prevY > currY)) {
                    if (currY >= prevY) {
                        Collections.swap(drawList, currIndex, currIndex - 1);
                        currIndex--;
                    }
                    if (currIndex < drawList.size()-1 && currIndex > 0) {
                        prevY = getPrevY(entity);
                        currY = getCurrY(entity,true);
                    } else {
                        break;
                    }
                }
            }
        }
        return drawList;
    }

    private float getCurrY(Entity currEntity, boolean prev) {
        BodyComponent currBody = currEntity.getComponent(BodyComponent.class);
        float currY = currBody.getY();
        int currIndex = entities.indexOf(currEntity);

        if (prev) {
            if (!(currEntity instanceof Beam)) {
                if (entities.get(currIndex-1) instanceof Beam) {
                    currY -= BeamSystem.yOffset;
                }
            }
        } else {
            if (!(currEntity instanceof Beam)) {
                if (entities.get(currIndex+1) instanceof Beam) {
                    currY -= BeamSystem.yOffset;
                }
            }
        }

        return currY;
    }

    private float getNextY(Entity currEntity) {
        int currIndex = entities.indexOf(currEntity);
        BodyComponent nextBody = entities.get(currIndex+1).getComponent(BodyComponent.class);
        float nextY = nextBody.getY();

        if (currEntity instanceof Beam && !(entities.get(currIndex+1) instanceof Beam)) {
            nextY -= BeamSystem.yOffset;
        }

        return nextY;
    }

    private float getPrevY(Entity currEntity) {
        int currIndex = entities.indexOf(currEntity);
        BodyComponent prevBody = entities.get(currIndex-1).getComponent(BodyComponent.class);
        float prevY = prevBody.getY();

        if (currEntity instanceof Beam && !(entities.get(currIndex-1) instanceof Beam)) {
            prevY -= BeamSystem.yOffset;
        }

        return prevY;
    }


    private void insertionSort(List<Entity> list) {
        for (int i = 1; i < list.size(); i++) {
            Entity key = list.get(i);
            int j = i;

            while (j > 0 && list.get(j - 1).getComponent(BodyComponent.class).getY() <=
                    key.getComponent(BodyComponent.class).getY()) {
                list.set(j,list.get(j-1));
                j--;
            }
            list.set(j,key);
        }
    }
}
