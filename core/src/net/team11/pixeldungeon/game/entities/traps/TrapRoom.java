package net.team11.pixeldungeon.game.entities.traps;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;

import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entity.component.entitycomponent.TrapRoomComponent;
import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.utils.CollisionUtil;

import java.util.ArrayList;
import java.util.List;

public class TrapRoom extends Entity {
    private List<Trap> traps;

    public TrapRoom(ChainShape bounds, String name) {
        super(name);
        traps = new ArrayList<>();

        addComponent(new TrapRoomComponent(this));
        addComponent(new BodyComponent(bounds, 0f,
                (CollisionUtil.TRAP),
                (byte) (CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));
    }

    public List<Trap> getTraps() {
        return traps;
    }

    public void addTrap(Trap trap) {
        traps.add(trap);
    }
}
