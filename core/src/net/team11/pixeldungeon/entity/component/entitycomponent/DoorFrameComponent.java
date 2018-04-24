package net.team11.pixeldungeon.entity.component.entitycomponent;

import net.team11.pixeldungeon.entities.door.DoorFrame;
import net.team11.pixeldungeon.entitysystem.EntityComponent;

public class DoorFrameComponent implements EntityComponent {
    private DoorFrame doorFrame;

    public DoorFrameComponent(DoorFrame doorFrame) {
        this.doorFrame = doorFrame;
    }
}
