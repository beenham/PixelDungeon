package net.team11.pixeldungeon.game.entity.component.entitycomponent;

import net.team11.pixeldungeon.game.entities.traps.TrapRoom;
import net.team11.pixeldungeon.game.entitysystem.EntityComponent;

public class TrapRoomComponent implements EntityComponent {
    private TrapRoom trapRoom;

    public TrapRoomComponent(TrapRoom trapRoom) {
        this.trapRoom = trapRoom;
    }
}
