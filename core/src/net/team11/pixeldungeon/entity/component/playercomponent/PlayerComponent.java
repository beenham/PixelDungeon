package net.team11.pixeldungeon.entity.component.playercomponent;

import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entitysystem.EntityComponent;
import net.team11.pixeldungeon.entitysystem.EntityEngine;

import java.util.LinkedList;

public class PlayerComponent implements EntityComponent {
    private Player entity;

    public PlayerComponent(Player entity) {
        this.entity = entity;
    }
}
