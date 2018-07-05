package net.team11.pixeldungeon.game.entities.beams;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.utils.CollisionUtil;

public class BeamReflectorMovable extends BeamReflector {
    private final float objectDensity = 10f;

    public BeamReflectorMovable(Rectangle bounds, String name, Beam beam){
        super(bounds,name,beam);
        setupBody(getComponent(BodyComponent.class));
    }

    @Override
    public void setBeamIn(Beam beamIn) {
        super.setBeamIn(beamIn);
        BodyComponent body = getComponent(BodyComponent.class);
        if (beamIn != null) {
            body.setDensity(0);
            body.recreateBody(BodyDef.BodyType.StaticBody);
        } else {
            body.setDensity(objectDensity);
            body.recreateBody(BodyDef.BodyType.DynamicBody);
        }
    }

    private void setupBody(BodyComponent body) {
        body.setDensity(objectDensity);
        body.setCategory(CollisionUtil.ENTITY);
        body.setCollision((byte)(CollisionUtil.ENTITY | CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY));
        body.recreateBody(BodyDef.BodyType.DynamicBody);
    }
}
