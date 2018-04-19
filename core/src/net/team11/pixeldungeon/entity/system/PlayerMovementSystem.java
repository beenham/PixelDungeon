package net.team11.pixeldungeon.entity.system;

import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entity.animation.AnimationName;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
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
        PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);

        if (controller.isRightPressed()) {
            velocityComponent.setDirection(Direction.RIGHT);
            velocityComponent.setxDirection(1);
            animationComponent.setAnimation(AnimationName.PLAYER_RIGHT);
        } else if (controller.isUpPressed()) {
            velocityComponent.setDirection(Direction.UP);
            velocityComponent.setyDirection(1);
            animationComponent.setAnimation(AnimationName.PLAYER_UP);
        } else if (controller.isLeftPressed()) {
            velocityComponent.setDirection(Direction.LEFT);
            velocityComponent.setxDirection(-1);
            animationComponent.setAnimation(AnimationName.PLAYER_LEFT);
        } else if (controller.isDownPressed()) {
            velocityComponent.setDirection(Direction.DOWN);
            velocityComponent.setyDirection(-1);
            animationComponent.setAnimation(AnimationName.PLAYER_DOWN);
        } else {
            velocityComponent.setxDirection(0);
            velocityComponent.setyDirection(0);
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
