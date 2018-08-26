package net.team11.pixeldungeon.game.entities.traps.floorhole;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;

import net.team11.pixeldungeon.game.entities.player.Player;
import net.team11.pixeldungeon.game.entities.traps.Trap;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entity.component.TrapComponent;
import net.team11.pixeldungeon.utils.CollisionUtil;

public class FloorHole extends Trap {
    public FloorHole(String name, ChainShape bounds) {
        super(name, true);
        requireSubmerged = true;

        addComponent(new TrapComponent(this));
        addComponent(new BodyComponent(bounds, 0f,
                (CollisionUtil.TRAP),
                (byte) (CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));
    }

    @Override
    public void update(float delta, Player player, float timer) {
        Polygon hitBox = getComponent(BodyComponent.class).getPolygon();
        Polygon entityBox = player.getComponent(BodyComponent.class).getPolygon();

        if (CollisionUtil.getAmountSubmerged(hitBox, entityBox) >= .5f) {
            setContactingEntity(player);
            if (!triggered) {
                trigger(false);
            }
        }
    }

    //Method called when the player enters the quicksand bounds
    @Override
    public void trigger(boolean trapRoom){
        if (!triggered && contactEntity instanceof Player) {
            ((Player) contactEntity).startFalling(this);
        }
        triggered = true;
    }
}
