package net.team11.pixeldungeon.game.entities.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Timer;

import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.game.entities.traps.Trap;
import net.team11.pixeldungeon.game.entity.component.AnimationComponent;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entity.component.CameraComponent;
import net.team11.pixeldungeon.game.entity.component.HealthComponent;
import net.team11.pixeldungeon.game.entity.component.InteractionComponent;
import net.team11.pixeldungeon.game.entity.component.InventoryComponent;
import net.team11.pixeldungeon.game.entity.component.VelocityComponent;
import net.team11.pixeldungeon.game.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.game.map.Map;
import net.team11.pixeldungeon.game.map.MapManager;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.CollisionUtil;
import net.team11.pixeldungeon.utils.Direction;
import net.team11.pixeldungeon.utils.Util;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.utils.assets.Assets;
import net.team11.pixeldungeon.utils.inventory.InventoryUtil;

import static net.team11.pixeldungeon.game.entities.player.Player.PlayerDepth.FOUR_QUART;

public class Player extends Entity {
    public enum PlayerDepth {
        ONE_QUART {
            @Override
            public String toString() {
                return "ONE QUART";
            }
        }, TWO_QUART {
            @Override
            public String toString() {
                return "TWO QUART";
            }
        },  THREE_QUART {
            @Override
            public String toString() {
                return "THREE QUART";
            }
        },  FOUR_QUART {
            @Override
            public String toString() {
                return "FOUR QUART";
            }
        }
    }
    private float spawnX, spawnY;
    private PlayerDepth depth;
    private float scale;

    public Player(float posX, float posY) {
        super("Player");
        spawnX = posX; spawnY = posY;
        depth = FOUR_QUART;
        scale = 1f;

        VelocityComponent velocityComponent;
        AnimationComponent animationComponent;
        this.addComponent(new PlayerComponent(this));
        this.addComponent(velocityComponent = new VelocityComponent(100));
        this.addComponent(animationComponent = new AnimationComponent(0));
        this.addComponent(new HealthComponent(1,1));
        this.addComponent(new CameraComponent(PlayScreen.gameCam));
        this.addComponent(new InteractionComponent(this));
        this.addComponent(new InventoryComponent());

        InventoryUtil invenUtil = InventoryUtil.getInstance();
        TextureAtlas textureAtlas = Assets.getInstance().getPlayerTexture(
                invenUtil.getSkins().get(invenUtil.getSkinSet().getCurrentSkin()).getName());
        animationComponent.addAnimation(AssetName.PLAYER_MOVING_UP, textureAtlas, 2f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_MOVING_DOWN, textureAtlas, 2f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_MOVING_LEFT, textureAtlas, 2f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_MOVING_RIGHT, textureAtlas, 2f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_IDLE_UP, textureAtlas, 1.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_IDLE_DOWN, textureAtlas, 4f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_IDLE_LEFT, textureAtlas, 4f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_IDLE_RIGHT, textureAtlas, 4f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_INTERACTING_UP, textureAtlas, 1.75f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.PLAYER_INTERACTING_DOWN, textureAtlas, 1.75f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.PLAYER_INTERACTING_LEFT, textureAtlas, 1.75f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.PLAYER_INTERACTING_RIGHT, textureAtlas, 1.75f, Animation.PlayMode.NORMAL);
        animationComponent.addAnimation(AssetName.PLAYER_PUSHING_UP, textureAtlas, 2.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_PUSHING_DOWN, textureAtlas, 2.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_PUSHING_LEFT, textureAtlas, 2.75f, Animation.PlayMode.LOOP);
        animationComponent.addAnimation(AssetName.PLAYER_PUSHING_RIGHT, textureAtlas, 2.75f, Animation.PlayMode.LOOP);

        animationComponent.setAnimation(AssetName.PLAYER_IDLE_DOWN);
        velocityComponent.setDirection(Direction.DOWN);

        this.addComponent(new BodyComponent(14, 8, posX, posY, 1.0f,
                (CollisionUtil.ENTITY),
                (byte)(CollisionUtil.ENTITY | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.DynamicBody));
    }

    public PlayerDepth getDepth() {
        return depth;
    }

    public void setDepth(PlayerDepth depth) {
        this.depth = depth;
    }

    @Override
    public void respawn() {
        Map currentMap = MapManager.getInstance().getCurrentMap();

//        StatsUtil statsUtil = StatsUtil.getInstance();
//        CurrentStats currStats = statsUtil.getCurrentStats();
//        LevelStats levelStats = statsUtil.getLevelStats(currentMap.getMapName());

        Util.getInstance().getStatsUtil().respawn(currentMap.getMapName());
        Util.getInstance().getStatsUtil().updateAttempts(currentMap.getMapName());

        ScreenManager.getInstance().changeScreen(ScreenEnum.GAME,
                null,
                currentMap.getMapName()
                );
        PixelDungeon.getInstance().getAndroidInterface().earnLetsTryAgain();
    }

    public void startFalling(final Trap entity) {
        getComponent(BodyComponent.class).move(0,0);
        final VelocityComponent velComp = getComponent(VelocityComponent.class);
        velComp.paralyze(10);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (scale > 0) {
                    scale -= 0.05f;
                }
                if (scale < 0.3f) {
                    velComp.setyDirection(0);
                    velComp.setxDirection(0);
                }
                if (scale <= 0f) {
                    scale = 0f;
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            HealthComponent health = getComponent(HealthComponent.class);
                            health.setHealth(health.getHealth() - entity.getDamage(),entity);
                        }
                    },0.5f,0,0);
                }
            }
        },0.01f,0.01f,20);
    }

    public float getScale() {
        return scale;
    }
}
