package net.team11.pixeldungeon.entity.system;

import com.badlogic.gdx.math.Polygon;

import net.team11.pixeldungeon.entities.blocks.PressurePlate;
import net.team11.pixeldungeon.entities.player.Player;
import net.team11.pixeldungeon.entities.traps.Trap;
import net.team11.pixeldungeon.entities.traps.TrapRoom;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.entity.component.HealthComponent;
import net.team11.pixeldungeon.entity.component.TrapComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.BoxComponent;
import net.team11.pixeldungeon.entity.component.entitycomponent.TrapRoomComponent;
import net.team11.pixeldungeon.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.entitysystem.Entity;
import net.team11.pixeldungeon.entitysystem.EntityEngine;
import net.team11.pixeldungeon.entitysystem.EntitySystem;
import net.team11.pixeldungeon.utils.CollisionUtil;

import java.util.ArrayList;
import java.util.List;

public class TrapSystem extends EntitySystem {
    private final float timerReset = 50;
    private float timer = timerReset;
    private Player player;
    private List<Entity> trapRooms;
    private List<Entity> entities;


    @Override
    public void init(EntityEngine entityEngine) {
        player = (Player) entityEngine.getEntities(PlayerComponent.class).get(0);
        trapRooms = new ArrayList<>();
        trapRooms = entityEngine.getEntities(TrapRoomComponent.class);
        entities = new ArrayList<>();
        entities.addAll(entityEngine.getEntities(BoxComponent.class));
        entities.addAll(entityEngine.getEntities(PlayerComponent.class));
    }

    @Override
    public void update(float delta) {
        timer -= delta * RenderSystem.FRAME_SPEED;
        Polygon playerBox = player.getComponent(BodyComponent.class).getPolygon();

        for (Entity trapRoomEntity : trapRooms) {
            Polygon trapRoomBox = trapRoomEntity.getComponent(BodyComponent.class).getPolygon();
            boolean updateTraps = CollisionUtil.isOverlapping(trapRoomBox,playerBox);
            if (trapRoomEntity instanceof TrapRoom && updateTraps) {
                for (Entity trapEntity : ((TrapRoom) trapRoomEntity).getTraps()) {
                    if (trapEntity instanceof PressurePlate) {
                        PressurePlate plate = (PressurePlate) trapEntity;
                        Polygon plateBox = plate.getComponent(BodyComponent.class).getPolygon();

                        for (Entity entity : entities) {
                            Polygon entityBox = entity.getComponent(BodyComponent.class).getPolygon();
                            boolean overLapping = CollisionUtil.isOverlapping(plateBox, entityBox);

                            if (plate.isContacting()) {
                                if (entity == plate.getContactEntity()) {
                                    if (overLapping) {
                                        break;
                                    } else {
                                        plate.setContactingEntity(null);
                                    }
                                }
                            } else if (overLapping) {
                                plate.setContactingEntity(entity);
                            } else if (plate.getTimer() >= 0) {
                                plate.trigger();
                            }
                        }
                        plate.setTimer(plate.getTimer() - delta);
                    } else {
                        Trap trap = (Trap) trapEntity;
                        TrapComponent trapComponent = trap.getComponent(TrapComponent.class);
                        Polygon trapBox = trap.getComponent(BodyComponent.class).getPolygon();
                        boolean overLapping = CollisionUtil.isOverlapping(trapBox, playerBox);
                        boolean submerged = CollisionUtil.isSubmerged(trapBox, playerBox);

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
                                } else if (overLapping) {
                                    trap.setContactingEntity(player);
                                    if (trap.isTriggered()) {
                                        HealthComponent health = player.getComponent(HealthComponent.class);
                                        health.setHealth(health.getHealth() - trap.getDamage(),trap);
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
                                        health.setHealth(health.getHealth() - trap.getDamage(),trap);
                                    }
                                } else {
                                    trap.setContactingEntity(null);
                                }
                            } else {
                                if (overLapping) {
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
                }

                for (Entity trapEntity : trapRooms) {
                    if (trapEntity instanceof PressurePlate && ((Trap) trapEntity).isEnabled()) {
                        PressurePlate plate = (PressurePlate) trapEntity;
                        for (Entity entity : entities) {
                            TrapComponent trapComponent = plate.getComponent(TrapComponent.class);
                            Polygon trapBox = plate.getComponent(BodyComponent.class).getPolygon();
                            Polygon entityBox = entity.getComponent(BodyComponent.class).getPolygon();
                            boolean overLapping = CollisionUtil.isOverlapping(trapBox, entityBox);

                            if (overLapping) {
                                plate.setContactingEntity(entity);
                                if (!plate.isTriggered() && !trapComponent.isInteracting()) {
                                    trapComponent.trigger();
                                }
                            }
                        }
                    }
                }
                if (timer <= 0) {
                    timer = timerReset;
                }
            }
        }
    }
}
