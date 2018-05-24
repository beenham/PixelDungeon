package net.team11.pixeldungeon.entity.system;

import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entities.traps.Trap;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.HealthComponent;
import net.team11.pixeldungeon.entity.component.TrapComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.screens.PlayScreen;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SingleSelectionModel;

public class TrapSystem extends EntitySystem {
    private final float timerReset = 50;
    private float timer = timerReset;
    private Player player;
    private List<Entity> traps = null;

    @Override
    public void init(EntityEngine entityEngine) {
        player = (Player) entityEngine.getEntities(PlayerComponent.class).get(0);
        traps = new ArrayList<>(entityEngine.getEntities(TrapComponent.class).size());
        traps = entityEngine.getEntities(TrapComponent.class);
    }

    @Override
    public void update(float delta) {
        timer = timer - delta * RenderSystem.FRAME_SPEED;

        Rectangle playerRect = player.getComponent(BodyComponent.class).getRectangle();
        for (Entity entity : traps) {
            Trap trap = (Trap) entity;
            if (trap.isEnabled()) {
                TrapComponent trapComponent = trap.getComponent(TrapComponent.class);
                if (trapComponent.isInteracting()) {
                    trapComponent.setInteractTime(trapComponent.getInteractTime() - delta * RenderSystem.FRAME_SPEED);
                    if (trapComponent.getInteractTime() <= 0f) {
                        trapComponent.setInteracting(false);
                        trapComponent.setInteractTime(0);
                    }
                    continue;
                }
                Rectangle trapRect = trap.getComponent(BodyComponent.class).getRectangle();
                if (trap.isTimed()) {
                    trap.setTimer(trap.getTimer() - delta * RenderSystem.FRAME_SPEED);
                    if (playerRect.overlaps(trapRect)) {
                        trap.setContactingEntity(player);
                        if (trap.isTriggered()) {
                            HealthComponent health = player.getComponent(HealthComponent.class);
                            health.setHealth(health.getHealth()-trap.getDamage());
                        }
                    } else {
                        trap.setContactingEntity(null);
                    }
                    if (trap.getTimer() <= 0f) {
                        trap.trigger();
                        trap.resetTimer();
                    }
                } else if (trap.hasTrigger()) {
                    if (playerRect.overlaps(trapRect)) {
                        trap.setContactingEntity(player);
                        if (trap.isTriggered()) {
                            HealthComponent health = player.getComponent(HealthComponent.class);
                            health.setHealth(health.getHealth()-trap.getDamage());
                        }
                    } else {
                        trap.setContactingEntity(null);
                    }
                } else {
                    if (playerRect.overlaps(trapRect)){
                        trap.setContactingEntity(player);
                        if (!trap.isTriggered() && !trapComponent.isInteracting()) {
                            trap.trigger();
                        }
                    } else if (trap.isTriggered() && timer <= 0) {
                        trap.setContactingEntity(null);
                        trap.trigger();
                    }
                }
            }
        }
        if (timer <= 0) {
            timer = timerReset;
        }
    }
}
