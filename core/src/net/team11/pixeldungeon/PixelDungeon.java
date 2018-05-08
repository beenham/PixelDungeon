package net.team11.pixeldungeon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.team11.pixeldungeon.utils.TiledMapNames;
import net.team11.pixeldungeon.screens.PlayScreen;

public class PixelDungeon extends Game {
    public static int V_WIDTH;
    public static int V_HEIGHT;
	public SpriteBatch batch;

	/**
	 * Creation of the game.
	 * Creating SpriteBatch, setting the Width / Height of display.
	 * Creating a new viewable screen
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		V_HEIGHT = Gdx.graphics.getHeight();
		V_WIDTH = Gdx.graphics.getWidth();
		setScreen(new PlayScreen(this, TiledMapNames.TEST_LEVEL));
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
