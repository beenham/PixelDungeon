package net.team11.pixeldungeon.game.entity.component.entitycomponent;

import net.team11.pixeldungeon.game.entities.blocks.Box;
import net.team11.pixeldungeon.game.entitysystem.EntityComponent;

public class BoxComponent implements EntityComponent{
    private Box box;

    public BoxComponent(Box box) {
        this.box = box;
    }
}
