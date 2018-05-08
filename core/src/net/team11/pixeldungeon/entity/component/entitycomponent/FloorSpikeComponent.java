package net.team11.pixeldungeon.entity.component.entitycomponent;

import net.team11.pixeldungeon.entities.traps.FloorSpike;
import net.team11.pixeldungeon.entitysystem.EntityComponent;

public class FloorSpikeComponent implements EntityComponent {
    private FloorSpike floorSpike;

    public FloorSpikeComponent(FloorSpike floorSpike) {
        this.floorSpike = floorSpike;
    }
}
