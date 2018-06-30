package net.team11.pixeldungeon.game.entity.component;


import net.team11.pixeldungeon.game.entitysystem.EntityComponent;
import net.team11.pixeldungeon.utils.Direction;

public class VelocityComponent implements EntityComponent {

    private float xDirection;
    private float yDirection;
    private float movementSpeed;

    private boolean paralyzed;
    private float paralyzedTime;

    private Direction direction;

    public VelocityComponent(float movementSpeed) {
        this.xDirection = 0f;
        this.yDirection = 0f;
        this.movementSpeed = movementSpeed;
        this.direction = Direction.RIGHT;
        this.paralyzed = false;
    }

    public void paralyze(float seconds) {
        this.paralyzed = true;
        this.paralyzedTime = seconds;
    }

    public float getxDirection() {
        return xDirection;
    }

    public void setxDirection(float xDirection) {
        this.xDirection = xDirection;
    }

    public float getyDirection() {
        return yDirection;
    }

    public void setyDirection(float yDirection) {
        this.yDirection = yDirection;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isParalyzed() {
        return paralyzed;
    }

    public void setParalyzed(boolean paralyzed) {
        this.paralyzed = paralyzed;
    }

    public float getParalyzedTime() {
        return paralyzedTime;
    }

    public void setParalyzedTime(float paralyzedTime) {
        this.paralyzedTime = paralyzedTime;
    }
}
