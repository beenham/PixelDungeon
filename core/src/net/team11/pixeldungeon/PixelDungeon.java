package net.team11.pixeldungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.team11.pixeldungeon.game.map.MapManager;
import net.team11.pixeldungeon.screens.ScreenEnum;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.game.DirectedGame;
import net.team11.pixeldungeon.utils.inventory.InventoryUtil;
import net.team11.pixeldungeon.utils.stats.StatsUtil;
import net.team11.pixeldungeon.utils.crossplatform.AndroidInterface;

public class PixelDungeon extends DirectedGame {
    public static int V_WIDTH;
    public static int V_HEIGHT;
    public static float SCALAR;
    public static final String VERSION = "Beta 0.1";
	public SpriteBatch batch;

	private static PixelDungeon INSTANCE;
	private AndroidInterface androidInterface;

	public PixelDungeon(AndroidInterface androidInterface) {
		this.androidInterface = androidInterface;
	}

	@Override
	public void create () {
		INSTANCE = this;
		batch = new SpriteBatch();
		V_HEIGHT = Gdx.graphics.getHeight();
		V_WIDTH = Gdx.graphics.getWidth();
		SCALAR = (V_HEIGHT / 1080f);
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
		StatsUtil.getInstance();
		InventoryUtil.getInstance();
		MapManager.getInstance();
	}

	public AndroidInterface getAndroidInterface() {
		return androidInterface;
	}

	public static PixelDungeon getInstance() {
		return INSTANCE;
	}
}
