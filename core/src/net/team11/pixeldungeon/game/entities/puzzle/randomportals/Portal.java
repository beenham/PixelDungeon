package net.team11.pixeldungeon.game.entities.puzzle.randomportals;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

import net.team11.pixeldungeon.game.entities.puzzle.PuzzleComponent;
import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.utils.CollisionUtil;

public class Portal extends PuzzleComponent {
    private int stage;
    private boolean linkable;
    private Vector2 targetCoords;

    public Portal(Rectangle bounds, String name, int stage, boolean linkable) {
        super(name);
        this.stage = stage;
        this.linkable = linkable;
        float posX = bounds.getX() + bounds.getWidth()/2;
        float posY = bounds.getY() + bounds.getHeight()/2;

        addComponent(new BodyComponent(bounds.getWidth(), bounds.getHeight(),posX,posY, 0,
                (CollisionUtil.PUZZLE_AREA),
                (byte)(CollisionUtil.PUZZLE_AREA | CollisionUtil.BOUNDARY),
                BodyDef.BodyType.StaticBody));
    }

    public Vector2 getTargetCoords() {
        return targetCoords;
    }

    public void setTargetCoords(Vector2 targetCoords) {
        this.targetCoords = targetCoords;
    }

    public int getStage() {
        return stage;
    }

    public boolean isLinkable() {
        return linkable;
    }
}
