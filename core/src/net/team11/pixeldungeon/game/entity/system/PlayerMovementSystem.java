package net.team11.pixeldungeon.game.entity.system;

import net.team11.pixeldungeon.game.entities.player.Player;
import net.team11.pixeldungeon.game.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.game.uicomponents.UIManager;
import net.team11.pixeldungeon.utils.assets.AssetName;
import net.team11.pixeldungeon.game.entity.component.AnimationComponent;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entity.component.InteractionComponent;
import net.team11.pixeldungeon.game.entity.component.VelocityComponent;
import net.team11.pixeldungeon.game.entitysystem.EntityEngine;
import net.team11.pixeldungeon.game.entitysystem.EntitySystem;
import net.team11.pixeldungeon.utils.Direction;
import net.team11.pixeldungeon.game.uicomponents.Hud;

public class PlayerMovementSystem extends EntitySystem {
    private Player player;
    private UIManager uiManager;
    private Hud hud;

    @Override
    public void init(EntityEngine entityEngine) {
        player = (Player) entityEngine.getEntities(PlayerComponent.class,
                VelocityComponent.class, AnimationComponent.class).get(0);
    }

    @Override
    public void update(float delta) {
        if (player == null) {
            return;
        }
        Player.PlayerDepth depth = player.getDepth();
        BodyComponent bodyComponent = player.getComponent(BodyComponent.class);
        VelocityComponent velocityComponent = player.getComponent(VelocityComponent.class);
        AnimationComponent animationComponent = player.getComponent(AnimationComponent.class);
        InteractionComponent interactionComponent = player.getComponent(InteractionComponent.class);

        if (hud.isInteractPressed() && !velocityComponent.isParalyzed() && depth == Player.PlayerDepth.FOUR_QUART) {
            velocityComponent.setxDirection(0);
            velocityComponent.setyDirection(0);
            interactionComponent.setInteracting(true);
            switch (velocityComponent.getDirection()) {
                case UP:
                    animationComponent.setAnimation(AssetName.PLAYER_INTERACTING_UP);
                    break;
                case DOWN:
                    animationComponent.setAnimation(AssetName.PLAYER_INTERACTING_DOWN);
                    break;
                case RIGHT:
                    animationComponent.setAnimation(AssetName.PLAYER_INTERACTING_RIGHT);
                    break;
                case LEFT:
                    animationComponent.setAnimation(AssetName.PLAYER_INTERACTING_LEFT);
                    break;
            }
            velocityComponent.paralyze(5);
        } else if (hud.isRightPressed() && !velocityComponent.isParalyzed()) {
            velocityComponent.setDirection(Direction.RIGHT);
            velocityComponent.setxDirection(1);
            interactionComponent.setInteracting(false);
            if (bodyComponent.isPushing()) {
                animationComponent.setAnimation(AssetName.PLAYER_PUSHING_RIGHT);
            } else {
                animationComponent.setAnimation(AssetName.PLAYER_MOVING_RIGHT);
            }
        } else if (hud.isUpPressed() && !velocityComponent.isParalyzed()) {
            velocityComponent.setDirection(Direction.UP);
            velocityComponent.setyDirection(1);
            interactionComponent.setInteracting(false);
            if (bodyComponent.isPushing()) {
                animationComponent.setAnimation(AssetName.PLAYER_PUSHING_UP);
            } else {
                animationComponent.setAnimation(AssetName.PLAYER_MOVING_UP);
            }
        } else if (hud.isLeftPressed() && !velocityComponent.isParalyzed()) {
            velocityComponent.setDirection(Direction.LEFT);
            velocityComponent.setxDirection(-1);
            interactionComponent.setInteracting(false);
            if (bodyComponent.isPushing()) {
                animationComponent.setAnimation(AssetName.PLAYER_PUSHING_LEFT);
            } else {
                animationComponent.setAnimation(AssetName.PLAYER_MOVING_LEFT);
            }
        } else if (hud.isDownPressed() && !velocityComponent.isParalyzed()) {
            velocityComponent.setDirection(Direction.DOWN);
            velocityComponent.setyDirection(-1);
            interactionComponent.setInteracting(false);
            if (bodyComponent.isPushing()) {
                animationComponent.setAnimation(AssetName.PLAYER_PUSHING_DOWN);
            } else {
                animationComponent.setAnimation(AssetName.PLAYER_MOVING_DOWN);
            }
        } else if (hud.isInventoryPressed()) {
            velocityComponent.setxDirection(0);
            velocityComponent.setyDirection(0);
            uiManager.showInventory();
        } else if (hud.isPausePressed()) {
            velocityComponent.setxDirection(0);
            velocityComponent.setyDirection(0);
            bodyComponent.moveX(0);
            bodyComponent.moveY(0);
            uiManager.showPauseMenu(true);
        } else {
            if (!velocityComponent.isParalyzed()) {
                interactionComponent.setInteracting(false);
                velocityComponent.setxDirection(0);
                velocityComponent.setyDirection(0);
                bodyComponent.moveX(0);
                bodyComponent.moveY(0);
            }
            if (!velocityComponent.isParalyzed()) {
                switch (velocityComponent.getDirection()) {
                    case UP:
                        animationComponent.setAnimation(AssetName.PLAYER_IDLE_UP);
                        break;
                    case DOWN:
                        animationComponent.setAnimation(AssetName.PLAYER_IDLE_DOWN);
                        break;
                    case RIGHT:
                        animationComponent.setAnimation(AssetName.PLAYER_IDLE_RIGHT);
                        break;
                    case LEFT:
                        animationComponent.setAnimation(AssetName.PLAYER_IDLE_LEFT);
                        break;
                }
            } else {
                switch (velocityComponent.getDirection()) {
                    case UP:
                        animationComponent.setAnimation(AssetName.PLAYER_MOVING_UP);
                        break;
                    case DOWN:
                        animationComponent.setAnimation(AssetName.PLAYER_MOVING_DOWN);
                        break;
                    case LEFT:
                        animationComponent.setAnimation(AssetName.PLAYER_MOVING_LEFT);
                        break;
                    case RIGHT:
                        animationComponent.setAnimation(AssetName.PLAYER_MOVING_RIGHT);
                        break;
                }
            }
        }
    }

    public void setUIManager(UIManager uiManager) {
        this.uiManager = uiManager;
        this.hud = uiManager.getHud();
    }
}
