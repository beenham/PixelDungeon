package net.team11.pixeldungeon.game.entities.puzzle.randomportals;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.game.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.utils.CollisionUtil;

public class PortalExit extends PuzzleComponent {
    public PortalExit(Rectangle bounds,String name) {
        super(name);
        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;

        addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(),posX,posY, 0,
                (CollisionUtil.PUZZLE_AREA),
                (byte)(CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));
    }
}
