package net.team11.pixeldungeon.game.entity.component;

import com.badlogic.gdx.graphics.OrthographicCamera;

import net.team11.pixeldungeon.game.entitysystem.EntityComponent;

public class CameraComponent implements EntityComponent {

    private OrthographicCamera camera;

    public CameraComponent(OrthographicCamera camera) {
        this.camera = camera;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setPosition(float x, float y) {
        camera.position.x = x;
        camera.position.y = y;
    }
}
