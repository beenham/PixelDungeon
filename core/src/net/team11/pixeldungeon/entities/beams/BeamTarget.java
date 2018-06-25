package net.team11.pixeldungeon.entities.beams;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.entity.component.AnimationComponent;
import net.team11.pixeldungeon.entity.component.BodyComponent;
import net.team11.pixeldungeon.utils.CollisionUtil;

public class BeamTarget extends PuzzleComponent {

    private boolean beamIn = false;

    public BeamTarget(Rectangle bounds, String name) {
        super(name);
        float posX = bounds.getX() + bounds.getWidth() / 2;
        float posY = bounds.getY() + bounds.getHeight() / 2;

        this.addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(), posX, posY, 0,
                (CollisionUtil.ENTITY),
                (byte) (CollisionUtil.ENTITY | CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));

//        AnimationComponent animationComponent;
//        this.addComponent(animationComponent = new AnimationComponent(0));
//        setupAnimationComponent(animationComponent);
    }

    public boolean hasBeamIn(){
        return this.beamIn;
    }

    public void setBeamIn(boolean beamIn){
        this.beamIn = beamIn;
    }

    private void setupAnimationComponent(AnimationComponent animationComponent) {

    }
}


