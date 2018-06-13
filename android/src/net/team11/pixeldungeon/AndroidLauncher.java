package net.team11.pixeldungeon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import net.team11.pixeldungeon.crossplatform.CrossPlatformSystem;
import net.team11.pixeldungeon.utils.crossplatform.AlertDialogCallback;
import net.team11.pixeldungeon.utils.crossplatform.AndroidInterface;

public class AndroidLauncher extends AndroidApplication {
	private static int RC_SIGN_IN = 9001;
	GoogleSignInClient mGoogleSignInClient = null;
	GoogleApiClient mGoogleApiClient = null;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupGoogleClient();

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new PixelDungeon(new CrossPlatformSystem(this)), config);
	}

	private void setupGoogleClient() {
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
				.requestEmail()
				.build();
		mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

		initialSignIn();
		mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
				.addApi(Games.API)
				.setViewForPopups(new View(this))
				.build();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}


	public boolean isSignedIn() {
		return GoogleSignIn.getLastSignedInAccount(this) != null;
	}

	public void initialSignIn() {
		mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
				new OnCompleteListener<GoogleSignInAccount>() {
					@Override
					public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
						if (task.isSuccessful()) {
							System.out.println("SIGNED IN ON LAUNCH");
							Games.getGamesClient(AndroidLauncher.this, task.getResult())
									.setViewForPopups(new View(AndroidLauncher.this));
						} else {
							System.out.println("COULDNT SIGN IN ON LAUNCH");
						}
					}
				});
	}

	public void signInSilently() {
		System.out.println("SIGNING IN");
		mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
				new OnCompleteListener<GoogleSignInAccount>() {
					@Override
					public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
						if (task.isSuccessful()) {
                            Games.getGamesClient(AndroidLauncher.this, task.getResult())
									.setViewForPopups(new View(AndroidLauncher.this));
						} else {
							startSignInIntent();
						}
					}
				});
	}

	private void startSignInIntent() {
		Intent intent = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(intent, RC_SIGN_IN);
	}

	public void signOut() {
		mGoogleSignInClient.signOut().addOnCompleteListener(this,
				new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						// at this point, the user is signed out.
					}
				});
	}
}
