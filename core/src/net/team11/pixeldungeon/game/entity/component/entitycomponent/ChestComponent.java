package net.team11.pixeldungeon.game.entity.component.entitycomponent;

import net.team11.pixeldungeon.game.entities.blocks.Chest;
import net.team11.pixeldungeon.game.entitysystem.EntityComponent;

public class ChestComponent implements EntityComponent {
    private Chest chest;

    public ChestComponent(Chest chest) {
        this.chest = chest;
    }
}
