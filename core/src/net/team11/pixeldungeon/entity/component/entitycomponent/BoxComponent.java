package net.team11.pixeldungeon.entity.component.entitycomponent;

import net.team11.pixeldungeon.entities.blocks.Box;
import net.team11.pixeldungeon.entitysystem.EntityComponent;

public class BoxComponent implements EntityComponent{
    private Box box;

    public BoxComponent(Box box) {
        this.box = box;
    }
}
