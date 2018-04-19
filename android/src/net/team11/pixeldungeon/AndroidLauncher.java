package net.team11.pixeldungeon;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import net.team11.pixeldungeon.PixelDungeon;

public class AndroidLauncher extends AndroidApplication {
	PixelDungeon pixelDungeon;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(pixelDungeon = new PixelDungeon(), config);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}
}
