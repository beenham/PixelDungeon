package net.team11.pixeldungeon;

import android.os.Bundle;
import android.view.MotionEvent;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

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
