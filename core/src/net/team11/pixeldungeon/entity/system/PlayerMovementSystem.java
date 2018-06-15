package net.team11.pixeldungeon.entity.system;

import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.uicomponents.PauseMenu;
import net.team11.pixeldungeon.uicomponents.inventory.InventoryUI;
import net.team11.pixeldungeon.utils.AssetName;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.InteractionComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.utils.Direction;
import net.team11.pixeldungeon.uicomponents.Hud;

public class PlayerMovementSystem extends EntitySystem {
    private Player player;
    private EntityEngine engine;
    private Hud hud;
    private InventoryUI inventoryUI;
    private PauseMenu pauseMenu;

    @Override
    public void init(EntityEngine entityEngine) {
        this.engine = entityEngine;
        player = (Player) entityEngine.getEntities(PlayerComponent.class,
                VelocityComponent.class, AnimationComponent.class).get(0);
    }

    @Override
    public void update(float delta) {
        if (player == null) {
            return;
        }
        BodyComponent bodyComponent = player.getComponent(BodyComponent.class);
        VelocityComponent velocityComponent = player.getComponent(VelocityComponent.class);
        AnimationComponent animationComponent = player.getComponent(AnimationComponent.class);
        InteractionComponent interactionComponent = player.getComponent(InteractionComponent.class);

        if (hud.isVisible()) {
            if (hud.isInteractPressed() && !velocityComponent.isParalyzed()) {
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
                hud.setPressed(false);
                hud.setVisible(false);
                inventoryUI.setVisible(true);
            } else if (hud.isPausePressed()) {
                velocityComponent.setxDirection(0);
                velocityComponent.setyDirection(0);
                hud.setPressed(false);
                hud.setVisible(false);
                pauseMenu.setVisible(true);
                ScreenManager.getInstance().getScreen().pause();
            } else {
                velocityComponent.setxDirection(0);
                velocityComponent.setyDirection(0);
                bodyComponent.moveX(0);
                bodyComponent.moveY(0);

                if (!velocityComponent.isParalyzed())
                    interactionComponent.setInteracting(false);
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
            }
        }
    }

    public void setHud(Hud hud) {
        this.hud = hud;
    }

    public void setInventoryUI(InventoryUI inventoryUI) {
        this.inventoryUI = inventoryUI;
    }

    public void setPauseMenu(PauseMenu pauseMenu) {
        this.pauseMenu = pauseMenu;
    }
}
