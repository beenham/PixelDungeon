package net.team11.pixeldungeon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.game.DirectedGame;
import net.team11.pixeldungeon.screens.transitions.ScreenTransitionFade;
import net.team11.pixeldungeon.utils.TiledMapNames;
import net.team11.pixeldungeon.screens.PlayScreen;

public class PixelDungeon extends DirectedGame {
    public static int V_WIDTH;
    public static int V_HEIGHT;
    public static float SCALAR;
	public SpriteBatch batch;

	private static PixelDungeon INSTANCE;

	@Override
	public void create () {
		INSTANCE = this;
		batch = new SpriteBatch();
		V_HEIGHT = Gdx.graphics.getHeight();
		V_WIDTH = Gdx.graphics.getWidth();
		SCALAR = (V_HEIGHT / 1080f);

		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().showScreen(ScreenEnum.GAME,
				//ScreenTransitionFade.init(0.5f),
				TiledMapNames.TEST_LEVEL);
	}

	public static PixelDungeon getInstance() {
		return INSTANCE;
	}
}
