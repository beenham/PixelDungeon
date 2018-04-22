package net.team11.pixeldungeon.entity.component.entitycomponent;

import net.team11.pixeldungeon.entities.door.Door;
import net.team11.pixeldungeon.entitysystem.EntityComponent;

public class DoorComponent implements EntityComponent {
    private Door entity;

    public DoorComponent (Door entity) {
        this.entity = entity;
    }
}
