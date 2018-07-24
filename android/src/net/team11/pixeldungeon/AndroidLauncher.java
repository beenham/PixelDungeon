package net.team11.pixeldungeon;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.games.snapshot.Snapshot;

import net.team11.pixeldungeon.crossplatform.CrossPlatformSystem;
import net.team11.pixeldungeon.playservices.AdmobClient;
import net.team11.pixeldungeon.playservices.GoogleClient;

public class AndroidLauncher extends AndroidApplication {
	private static String TAG = "AndroidLauncher";

	private AdmobClient admobClient;
	private GoogleClient googleClient;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		admobClient = new AdmobClient(this);
		googleClient = new GoogleClient(this);

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
}
