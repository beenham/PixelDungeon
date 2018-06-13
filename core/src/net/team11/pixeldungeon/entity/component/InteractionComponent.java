package net.team11.pixeldungeon.entity.component;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityComponent;

public class InteractionComponent implements EntityComponent {
    private Entity entity;
    private boolean interacting;
    private float interactTime;


    public InteractionComponent(Entity entity) {
        this.entity = entity;
        this.interacting = false;
    }
    public void doInteraction() {
        entity.doInteraction();
        this.interacting = true;
        this.interactTime = 8;
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
