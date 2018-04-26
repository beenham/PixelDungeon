package net.team11.pixeldungeon.entity.system;

import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entity.animation.AnimationName;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.InteractionComponent;
import net.team11.pixeldungeon.entity.component.PositionComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.options.Direction;
import net.team11.pixeldungeon.uicomponents.Controller;

public class PlayerMovementSystem extends EntitySystem {
    private Player player;
    private EntityEngine engine;
    private Controller controller;

    @Override
    public void init(EntityEngine entityEngine) {
        this.engine = entityEngine;
        player = (Player) entityEngine.getEntities(PlayerComponent.class, PositionComponent.class,
                VelocityComponent.class, AnimationComponent.class).get(0);
    }

    @Override
    public void update(float delta) {
        if (player == null) {
            return;
        }
        VelocityComponent velocityComponent = player.getComponent(VelocityComponent.class);
        AnimationComponent animationComponent = player.getComponent(AnimationComponent.class);
        InteractionComponent interactionComponent = player.getComponent(InteractionComponent.class);
        PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);

        if (controller.isInteractPressed() && !velocityComponent.isParalyzed()) {
            velocityComponent.setxDirection(0);
            velocityComponent.setyDirection(0);
            interactionComponent.setInteracting(true);
            switch (velocityComponent.getDirection()) {
                case UP:
                    animationComponent.setAnimation(AnimationName.PLAYER_INTERACTING_UP);
                    break;
                case DOWN:
                    animationComponent.setAnimation(AnimationName.PLAYER_INTERACTING_DOWN);
                    break;
                case RIGHT:
                    animationComponent.setAnimation(AnimationName.PLAYER_INTERACTING_RIGHT);
                    break;
                case LEFT:
                    animationComponent.setAnimation(AnimationName.PLAYER_INTERACTING_LEFT);
                    break;
            }
            velocityComponent.paralyze(5);
        } else if (controller.isRightPressed() && !velocityComponent.isParalyzed()) {
            velocityComponent.setDirection(Direction.RIGHT);
            velocityComponent.setxDirection(1);
            interactionComponent.setInteracting(false);
            animationComponent.setAnimation(AnimationName.PLAYER_MOVING_RIGHT);
        } else if (controller.isUpPressed() && !velocityComponent.isParalyzed()) {
            velocityComponent.setDirection(Direction.UP);
            velocityComponent.setyDirection(1);
            interactionComponent.setInteracting(false);
            animationComponent.setAnimation(AnimationName.PLAYER_MOVING_UP);
        } else if (controller.isLeftPressed() && !velocityComponent.isParalyzed()) {
            velocityComponent.setDirection(Direction.LEFT);
            velocityComponent.setxDirection(-1);
            interactionComponent.setInteracting(false);
            animationComponent.setAnimation(AnimationName.PLAYER_MOVING_LEFT);
        } else if (controller.isDownPressed() && !velocityComponent.isParalyzed()) {
            velocityComponent.setDirection(Direction.DOWN);
            velocityComponent.setyDirection(-1);
            interactionComponent.setInteracting(false);
            animationComponent.setAnimation(AnimationName.PLAYER_MOVING_DOWN);
        } else {
            velocityComponent.setxDirection(0);
            velocityComponent.setyDirection(0);
            if (!velocityComponent.isParalyzed())
                interactionComponent.setInteracting(false);
            switch (velocityComponent.getDirection()) {
                case UP:
                    animationComponent.setAnimation(AnimationName.PLAYER_IDLE_UP);
                    break;
                case DOWN:
                    animationComponent.setAnimation(AnimationName.PLAYER_IDLE_DOWN);
                    break;
                case RIGHT:
                    animationComponent.setAnimation(AnimationName.PLAYER_IDLE_RIGHT);
                    break;
                case LEFT:
                    animationComponent.setAnimation(AnimationName.PLAYER_IDLE_LEFT);
                    break;
            }
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
