package net.team11.pixeldungeon.game.entity.component.playercomponent;

import net.team11.pixeldungeon.game.entity.component.VelocityComponent;
import net.team11.pixeldungeon.game.entities.player.Player;
import net.team11.pixeldungeon.game.entitysystem.EntityComponent;

public class PlayerComponent implements EntityComponent {
    private Player entity;

    public PlayerComponent(Player entity) {
        this.entity = entity;
    }

    public String getMoveAnimation() {
        VelocityComponent velocityComponent = entity.getComponent(VelocityComponent.class);
        switch (velocityComponent.getDirection()) {
            case UP:
                return "this";
            case DOWN:
                return "this";
            case RIGHT:
                return "this";
            case LEFT:
                return "this";
        }
        return null;
    }
}
