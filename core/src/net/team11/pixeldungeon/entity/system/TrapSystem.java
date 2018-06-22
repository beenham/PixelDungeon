package net.team11.pixeldungeon.entity.system;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import net.team11.pixeldungeon.entities.blocks.PressurePlate;
import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entities.traps.Quicksand;
import net.team11.pixeldungeon.entities.traps.Trap;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.HealthComponent;
import net.team11.pixeldungeon.entity.component.TrapComponent;
import net.team11.pixeldungeon.entity.component.VelocityComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.CollisionUtil;

import java.util.ArrayList;
import java.util.List;

public class TrapSystem extends EntitySystem {
    private final float timerReset = 50;
    private float timer = timerReset;
    private Player player;
    private List<Entity> traps = null;

    @Override
    public void init(EntityEngine entityEngine) {
        player = (Player) entityEngine.getEntities(PlayerComponent.class).get(0);
        traps = new ArrayList<>();
        traps = entityEngine.getEntities(TrapComponent.class);
    }

    @Override
    public void update(float delta) {
        timer = timer - delta * RenderSystem.FRAME_SPEED;

        Polygon playerBox = player.getComponent(BodyComponent.class).getPolygon();
        for (Entity entity : traps) {
            Trap trap = (Trap) entity;
            TrapComponent trapComponent = trap.getComponent(TrapComponent.class);
            Polygon trapBox = trap.getComponent(BodyComponent.class).getPolygon();
            boolean overLapping = CollisionUtil.isOverlapping(trapBox,playerBox);
            boolean submerged = CollisionUtil.isSubmerged(trapBox,playerBox);

            if (!trap.isEnabled()) {
                if (submerged) {
                    trap.setContactingEntity(player);
                    trap.setEnabled(true);
                }
            }

            if (trap.isEnabled()) {
                if (trapComponent.isInteracting()) {
                    trapComponent.setInteractTime(trapComponent.getInteractTime() - delta);
                    if (trapComponent.getInteractTime() <= 0f) {
                        trapComponent.setInteracting(false);
                        trapComponent.setInteractTime(0);
                    }
                    continue;
                }
                if (trap.isTimed()) {
                    trap.setTimer(trap.getTimer() - delta);
                    if (!overLapping && !submerged) {
                        trap.setContactingEntity(null);
                    } else if (trap.requireSubmerged() && !submerged) {
                        trap.setContactingEntity(null);
                    } else if (overLapping){
                        trap.setContactingEntity(player);
                        if (trap.isTriggered()) {
                            HealthComponent health = player.getComponent(HealthComponent.class);
                            health.setHealth(health.getHealth()-trap.getDamage());
                        }
                    }
                    if (trap.getTimer() <= 0f) {
                        trap.trigger();
                    }
                } else if (trap.hasTrigger()) {
                    if (overLapping) {
                        trap.setContactingEntity(player);
                        if (trap.isTriggered()) {
                            HealthComponent health = player.getComponent(HealthComponent.class);
                            health.setHealth(health.getHealth()-trap.getDamage());
                        }
                    } else {
                        trap.setContactingEntity(null);
                    }
                } else {
                    if (overLapping){
                        trap.setContactingEntity(player);
                        if (!trap.isTriggered() && !trapComponent.isInteracting()) {
                            trapComponent.trigger();
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
