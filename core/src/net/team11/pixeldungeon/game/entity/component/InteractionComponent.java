package net.team11.pixeldungeon.game.entity.component;

import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.game.entitysystem.EntityComponent;

public class InteractionComponent implements EntityComponent {
    private Entity entity;
    private boolean interacting;
    private float interactTime;


    public InteractionComponent(Entity entity) {
        this.entity = entity;
        this.interacting = false;
    }
    public void doInteraction() {
        entity.doInteraction(true);
        this.interacting = true;
        this.interactTime = 10;
    }

    /////////////////////////////////
    public float getInteractTime() {
        return interactTime;
    }

    public void setInteractTime(float interactTime) {
        this.interactTime = interactTime;
    }

    public void setInteracting(boolean interacting) {
        this.interacting = interacting;
    }

    public boolean isInteracting() {
        return interacting;
    }
}
