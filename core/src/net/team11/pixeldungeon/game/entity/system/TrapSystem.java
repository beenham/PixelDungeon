package net.team11.pixeldungeon.game.entity.system;

import com.badlogic.gdx.math.Polygon;

import net.team11.pixeldungeon.game.entities.blocks.PressurePlate;
import net.team11.pixeldungeon.game.entities.player.Player;
import net.team11.pixeldungeon.game.entities.traps.Trap;
import net.team11.pixeldungeon.game.entities.traps.TrapRoom;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entity.component.HealthComponent;
import net.team11.pixeldungeon.game.entity.component.TrapComponent;
import net.team11.pixeldungeon.game.entity.component.entitycomponent.BoxComponent;
import net.team11.pixeldungeon.game.entity.component.entitycomponent.TrapRoomComponent;
import net.team11.pixeldungeon.game.entity.component.playercomponent.PlayerComponent;
import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.game.entitysystem.EntityEngine;
import net.team11.pixeldungeon.game.entitysystem.EntitySystem;
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

        for (Entity trapRoom : trapRooms) {
            Polygon trapRoomBox = trapRoom.getComponent(BodyComponent.class).getPolygon();
            boolean updateTraps = CollisionUtil.isOverlapping(trapRoomBox,playerBox)
                    && ((TrapRoom)trapRoom).isOn();
            if (trapRoom instanceof TrapRoom && updateTraps) {
                for (Entity trapEntity : ((TrapRoom) trapRoom).getTraps()) {
                    if (trapEntity instanceof PressurePlate) {
                        ((Trap) trapEntity).update(delta,entities,timer);
                    } else {
                        ((Trap) trapEntity).update(delta,player,timer);
                    }
                }
            }
        }
        if (timer <= 0) {
            timer = timerReset;
        }
    }
}
