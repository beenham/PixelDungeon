package net.team11.pixeldungeon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import net.team11.pixeldungeon.crossplatform.CrossPlatformSystem;

public class AndroidLauncher extends AndroidApplication {
	private static String TAG = "PixelDungeon";
	public static int RC_SIGN_IN = 9001;
	public static int RC_ACHIEVEMENT_UI = 9003;
	public static int RC_LEADERBOARD_UI = 9004;
	private GoogleSignInClient mGoogleSignInClient = null;
	private GoogleSignInAccount mSignedInAccount = null;
	private Player mGooglePlayer = null;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupGoogleClient();

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new PixelDungeon(new CrossPlatformSystem(this)), config);
	}

	@Override
	protected void onResume() {
		super.onResume();
		signInSilently();
	}

	private void setupGoogleClient() {
		mGoogleSignInClient = GoogleSignIn.getClient(this,GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
		signInSilently();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}


	public boolean isSignedIn() {
		return GoogleSignIn.getLastSignedInAccount(this) != null;
	}

	public void signInSilently() {
		mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
				new OnCompleteListener<GoogleSignInAccount>() {
					@Override
					public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
						if (task.isSuccessful()) {
							onConnected(task.getResult());
						}
					}
				});
	}

	public void startSignInIntent() {
		Intent intent = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(intent, RC_SIGN_IN);
	}

	public void signOut() {
		mGoogleSignInClient.signOut().addOnCompleteListener(this,
				new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						mSignedInAccount = null;
						mGooglePlayer = null;
					}
				});
	}

	public String getUserName() {
	    if (mGooglePlayer != null) {
	        return mGooglePlayer.getDisplayName();
        }
        return null;
	}

	private void onConnected(GoogleSignInAccount googleSignInAccount) {
		Log.d(TAG, "onConnected(): connected to Google APIs");
		if (mSignedInAccount != googleSignInAccount) {
			mSignedInAccount = googleSignInAccount;

			// get the playerId from the PlayersClient
			PlayersClient playersClient = Games.getPlayersClient(this, googleSignInAccount);
			playersClient.getCurrentPlayer()
					.addOnSuccessListener(new OnSuccessListener<Player>() {
						@Override
						public void onSuccess(Player player) {
							mGooglePlayer = player;
						}
					});
			Games.getGamesClient(this, googleSignInAccount).setViewForPopups(findViewById(android.R.id.content));
			Games.getGamesClient(this, googleSignInAccount).setGravityForPopups(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
		}
	}

	public GoogleSignInAccount getmSignedInAccount() {
		return mSignedInAccount;
	}

	private boolean isSaving = false;
	public void openSave(boolean saving){

	}
}
