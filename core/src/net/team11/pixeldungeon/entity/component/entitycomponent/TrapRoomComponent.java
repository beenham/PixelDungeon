package net.team11.pixeldungeon.entity.component.entitycomponent;

import net.team11.pixeldungeon.entities.traps.TrapRoom;
import net.team11.pixeldungeon.entitysystem.EntityComponent;

public class TrapRoomComponent implements EntityComponent {
    private TrapRoom trapRoom;

    public TrapRoomComponent(TrapRoom trapRoom) {
        this.trapRoom = trapRoom;
    }
}
