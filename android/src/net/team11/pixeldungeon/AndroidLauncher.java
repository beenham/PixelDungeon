package net.team11.pixeldungeon;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import net.team11.pixeldungeon.crossplatform.CrossPlatformSystem;
import net.team11.pixeldungeon.playservices.AdmobClient;
import net.team11.pixeldungeon.playservices.GoogleClient;
import net.team11.pixeldungeon.playservices.SavesClient;

public class AndroidLauncher extends AndroidApplication {
	private static String TAG = "AndroidLauncher";

	private AdmobClient admobClient;
	private GoogleClient googleClient;
	private SavesClient savesClient;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		admobClient = new AdmobClient(this);
		googleClient = new GoogleClient(this);
		savesClient = new SavesClient(this);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new PixelDungeon(new CrossPlatformSystem(this)), config);
	}

	@Override
	protected void onResume() {
		super.onResume();
		googleClient.signInSilently();
	}

	public AdmobClient getAdmobClient() {
		return admobClient;
	}

	public GoogleClient getGoogleClient() {
		return googleClient;
	}

	public SavesClient getSavesClient() {
	    return savesClient;
    }
}
