package net.team11.pixeldungeon.game.entities.traps;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;

import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entity.component.entitycomponent.TrapRoomComponent;
import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.CollisionUtil;
import net.team11.pixeldungeon.utils.assets.Messages;

import java.util.ArrayList;
import java.util.List;

public class TrapRoom extends Entity {
    private List<Trap> traps;
    private boolean on;


    public TrapRoom(ChainShape bounds, String name) {
        super(name);
        traps = new ArrayList<>();
        on = true;

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

    @Override
    public void doInteraction(boolean isPlayer) {
        if (!isPlayer) {
            on = !on;
            if (!on) {
                for (Trap trap : traps) {
                    if (trap.isTriggered()) {
                        trap.trigger();
                    }
                }
                PlayScreen.uiManager.initTextBox(Messages.TRAP_ROOM_DEACTIVATED);
            } else {
                PlayScreen.uiManager.initTextBox(Messages.TRAP_ROOM_ACTIVATED);
            }
        }
    }

    public boolean isOn() {
        return on;
    }
}
