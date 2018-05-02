package net.team11.pixeldungeon.entity.component.entitycomponent;

import net.team11.pixeldungeon.entities.blocks.Torch;
import net.team11.pixeldungeon.entitysystem.EntityComponent;

public class TorchComponent implements EntityComponent {
    private Torch torch;

    public TorchComponent(Torch torch) {
        this.torch = torch;
    }
}
