package net.team11.pixeldungeon.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

public class ScreenTransitionSplit implements ScreenTransition {
    private static final ScreenTransitionSplit instance = new ScreenTransitionSplit();
    private float duration;
    private Interpolation easing;
    private boolean top;

    private float x;

    public static ScreenTransitionSplit init(float duration, boolean top,Interpolation easing) {
        instance.duration = duration;
        instance.easing = easing;
        instance.x = 0;
        instance.top = top;
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

        x = w/2 * alpha;
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (top) {
            batch.draw(nextScreen, 0, 0, 0, 0, w, h, 1, 1,
                    0, 0, 0, currScreen.getWidth(), currScreen.getHeight(),
                    false, true);
            batch.draw(currScreen, w/2 + x, 0, 0,0, w/2, h,
                    1,1,0,currScreen.getWidth()/2,0,
                    currScreen.getWidth()/2, currScreen.getHeight(),false,true);
            batch.draw(currScreen, 0 - x, 0, 0,0, w/2, h,
                    1,1,0,0,0,
                    currScreen.getWidth()/2, currScreen.getHeight(),false,true);
        } else {

            batch.draw(currScreen, 0, 0, 0, 0, w, h, 1, 1,
                    0, 0, 0, currScreen.getWidth(), currScreen.getHeight(),
                    false, true);
            batch.draw(nextScreen, w - x, 0, 0,0, w/2, h,
                    1,1,0,currScreen.getWidth()/2,0,
                    currScreen.getWidth()/2, currScreen.getHeight(),false,true);
            batch.draw(nextScreen, -w/2 + x, 0, 0,0, w/2, h,
                    1,1,0,0,0,
                    currScreen.getWidth()/2, currScreen.getHeight(),false,true);
        }
        batch.end();
    }
}