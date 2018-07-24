package net.team11.pixeldungeon.game.entities.puzzle.invisiblefloor;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import net.team11.pixeldungeon.game.entities.puzzle.PuzzleComponent;

import java.util.HashMap;

public class Platform extends PuzzleComponent {
    public static final String TOP_LEFT = "topLeft";
    public static final String BOTTOM_LEFT = "bottomLeft";
    public static final String TOP_RIGHT = "topRight";
    public static final String BOTTOM_RIGHT = "bottomRight";
    private HashMap<String,Vector2> corners;

    private boolean start;
    private boolean end;
    private boolean checkpoint;
    private boolean avoid;

    public Platform(String name, Rectangle bounds, boolean start, boolean end, boolean checkpoint, boolean avoid) {
        super(name);
        this.start = start;
        this.end = end;
        this.checkpoint = checkpoint;
        this.avoid = avoid;

        float width = bounds.getWidth();
        float height = bounds.getHeight();
        float posX = bounds.getX() + width / 2;
        float posY = bounds.getY() + height / 2;
        corners = new HashMap<>();
        corners.put(TOP_LEFT,new Vector2(posX-width/2,posY+height/2));
        corners.put(BOTTOM_LEFT,new Vector2(posX-width/2,posY-height/2));
        corners.put(BOTTOM_RIGHT,new Vector2(posX+width/2,posY-height/2));
        corners.put(TOP_RIGHT,new Vector2(posX+width/2,posY+height/2));
    }

    public Vector2 getCorner(String string) {
        if (corners.containsKey(string)) {
            return corners.get(string);
        } else {
            return corners.get(TOP_LEFT);
        }
    }

    public boolean isStart() {
        return start;
    }

    public boolean isEnd() {
        return end;
    }

    public boolean isCheckpoint() {
        return checkpoint;
    }

    public boolean shouldAvoid() {
        return avoid;
    }
}
