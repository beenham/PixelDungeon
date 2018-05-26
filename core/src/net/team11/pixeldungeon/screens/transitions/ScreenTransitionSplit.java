package net.team11.pixeldungeon.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Array;

import net.team11.pixeldungeon.PixelDungeon;

public class ScreenTransitionSplit implements ScreenTransition {
    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;
    private static final ScreenTransitionSplit instance = new ScreenTransitionSplit();
    private float duration;
    private int direction;
    private Interpolation easing;
    private Array<Integer> sliceIndex = new Array<Integer>();
    private boolean top;

    private float x;

    public static ScreenTransitionSplit init(float duration, int direction, boolean top,Interpolation easing) {
        instance.duration = duration;
        instance.direction = direction;
        instance.easing = easing;
        // create shuffled list of slice indices which determines
        // the order of slice animation
        instance.sliceIndex.clear();
        instance.sliceIndex.add(0,1);
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
        float y = 0;
        if (easing != null) {
            alpha = easing.apply(alpha);
        }
        switch (direction) {
            case HORIZONTAL:
                x = w/2 * alpha;
                System.out.println(x);
                break;
            case VERTICAL:

                break;
        }
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (top) {
            batch.draw(nextScreen, 0, 0, 0, 0, w, h, 1, 1,
                    0, 0, 0, currScreen.getWidth(), currScreen.getHeight(),
                    false, true);

            ///*
            batch.draw(currScreen, w/2 + x, 0, 0,0, w/2, h,
                    1,1,0,currScreen.getWidth()/2,0,
                    currScreen.getWidth()/2, currScreen.getHeight(),false,true);
            //*/
            batch.draw(currScreen, 0 - x, 0, 0,0, w/2, h,
                    1,1,0,0,0,
                    currScreen.getWidth()/2, currScreen.getHeight(),false,true);
        } else {

            batch.draw(currScreen, 0, 0, 0, 0, w, h, 1, 1,
                    0, 0, 0, currScreen.getWidth(), currScreen.getHeight(),
                    false, true);

            ///*
            batch.draw(nextScreen, w - x, 0, 0,0, w/2, h,
                    1,1,0,currScreen.getWidth()/2,0,
                    currScreen.getWidth()/2, currScreen.getHeight(),false,true);
            //*/
            batch.draw(nextScreen, -w/2 + x, 0, 0,0, w/2, h,
                    1,1,0,0,0,
                    currScreen.getWidth()/2, currScreen.getHeight(),false,true);
        }
        batch.end();
    }
}