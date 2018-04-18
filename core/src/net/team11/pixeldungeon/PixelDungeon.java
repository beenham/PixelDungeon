package net.team11.pixeldungeon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.team11.pixeldungeon.screens.PlayScreen;

public class PixelDungeon extends Game {
    public static int V_WIDTH;
    public static int V_HEIGHT;
	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		V_HEIGHT = Gdx.graphics.getHeight();
		V_WIDTH = Gdx.graphics.getWidth();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
