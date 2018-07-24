package net.team11.pixeldungeon.game.entities.traps.floorspike;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.game.entities.traps.Trap;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entity.component.HealthComponent;
import net.team11.pixeldungeon.game.entity.component.InteractionComponent;
import net.team11.pixeldungeon.game.entity.component.TrapComponent;
import net.team11.pixeldungeon.game.entitysystem.Entity;
import net.team11.pixeldungeon.utils.CollisionUtil;

import java.util.Timer;
import java.util.TimerTask;

public class FloorSpike extends Trap {
    public enum SpikeSize {
        oneXone {
            @Override
            public String toString() {
                return "1x1";
            }
        },
        oneXtwo {
            @Override
            public String toString() {
                return "1x2";
            }
        },
        twoXone {
            @Override
            public String toString() {
                return "2x1";
            }
        },
        twoXtwo {
            @Override
            public String toString() {
                return "2x2";
            }
        },
        twoXthree {
            @Override
            public String toString() {
                return "2x3";
            }
        },
        threeXtwo {
            @Override
            public String toString() {
                return "3x2";
            }
        }
    }

    protected SpikeSize size;

    public FloorSpike(Rectangle bounds, boolean enabled, String name) {
        super(name, enabled);

        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;
        this.addComponent(new TrapComponent(this));
        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 0f,
                (CollisionUtil.TRAP),
                (byte)(CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));
        this.addComponent(new InteractionComponent(this));
    }

    @Override
    public void trigger() {
        if (hasTrigger()) {
            for (final Entity entity : targetEntities) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (entity.hasComponent(TrapComponent.class)) {
                            ((Trap) entity).trigger();
                        }
                    }
                }, 125);
            }
        }
        if (enabled) {
            if (!triggered) {
                setActivatedAnim();
                triggered = true;
                if (contactEntity != null) {
                    contactEntity.getComponent(HealthComponent.class)
                            .setHealth(contactEntity.getComponent(HealthComponent.class).getHealth() - damage,this);
                }
            } else {
                setDeactivatedAnim();
                triggered = false;
            }
            if (timed) {
                resetTimer();
            }
        }
    }

    protected void setActivatedAnim() {}

    protected void setDeactivatedAnim() {}
}
