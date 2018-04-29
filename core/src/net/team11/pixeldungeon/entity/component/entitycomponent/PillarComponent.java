package net.team11.pixeldungeon.entity.component.entitycomponent;

import net.team11.pixeldungeon.entities.blocks.Pillar;
import net.team11.pixeldungeon.entitysystem.EntityComponent;

public class PillarComponent implements EntityComponent {
    private Pillar pillar;

    public PillarComponent(Pillar pillar) {
        this.pillar = pillar;
    }
}
