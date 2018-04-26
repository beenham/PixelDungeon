package net.team11.pixeldungeon.entity.component.entitycomponent;

import net.team11.pixeldungeon.entities.blocks.Chest;
import net.team11.pixeldungeon.entitysystem.EntityComponent;

public class ChestComponent implements EntityComponent {
    private Chest chest;

    public ChestComponent(Chest chest) {
        this.chest = chest;
    }
}
