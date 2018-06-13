package net.team11.pixeldungeon.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

public class ScreenTransitionPush implements ScreenTransition {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    private static final ScreenTransitionPush instance = new ScreenTransitionPush();
    private float duration;
    private Interpolation easing;
    private int direction;

    private float x;

    public static ScreenTransitionPush init(float duration, int direction, Interpolation easing) {
        instance.duration = duration;
        instance.easing = easing;
        instance.x = 0;
        instance.direction = direction;
        return instance;
    }

    @Override
    public float getDuration() {
        return duration;
    }

    @Override
    public void render(SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha) {
        float w = currScreen.getWidth();
        float h = currScreen.getHeight();
        if (easing != null) {
            alpha = easing.apply(alpha);
        }

        x = w * alpha;
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        switch (direction) {
            case LEFT:
                batch.draw(currScreen, x, 0, 0, 0, w, h, 1, 1,
                        0, 0, 0, currScreen.getWidth(), currScreen.getHeight(),
                        false, true);
                batch.draw(nextScreen, -nextScreen.getWidth() + x, 0, 0, 0, w, h, 1, 1,
                        0, 0, 0, currScreen.getWidth(), currScreen.getHeight(),
                        false, true);
                break;
            case RIGHT:
                batch.draw(currScreen, -x, 0, 0, 0, w, h, 1, 1,
                        0, 0, 0, currScreen.getWidth(), currScreen.getHeight(),
                        false, true);
                batch.draw(nextScreen, currScreen.getWidth() - x, 0, 0, 0, w, h, 1, 1,
                        0, 0, 0, currScreen.getWidth(), currScreen.getHeight(),
                        false, true);
                break;
        }
        batch.end();
    }
}