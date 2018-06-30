package net.team11.pixeldungeon.game.entity.component;

import net.team11.pixeldungeon.game.entities.traps.Trap;
import net.team11.pixeldungeon.game.entitysystem.EntityComponent;

public class TrapComponent implements EntityComponent {
    private Trap trap;
    private boolean interacting;
    private float interactTime;

    public TrapComponent(Trap trap) {
        this.trap = trap;
    }

    public void trigger() {
        trap.trigger();
        this.interacting = true;
        this.interactTime = 5;
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
