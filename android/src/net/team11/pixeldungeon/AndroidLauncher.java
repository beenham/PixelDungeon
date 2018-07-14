package net.team11.pixeldungeon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Json;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadata;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import net.team11.pixeldungeon.crossplatform.CrossPlatformSystem;
import net.team11.pixeldungeon.saves.SaveGame;
import net.team11.pixeldungeon.utils.Util;

import java.io.IOException;

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
			GoogleSignInOptions signInOption =
					new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
							.requestScopes(Drive.SCOPE_APPFOLDER)
							.build();

			GoogleSignInClient signInClient = GoogleSignIn.getClient(this, signInOption);
			signInClient.silentSignIn().addOnCompleteListener(this,
					new OnCompleteListener<GoogleSignInAccount>() {
						@Override
						public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
							if (task.isSuccessful()) {
								onConnected(task.getResult());
								System.out.println("Successfully signed in");
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

	private static final int RC_SAVED_GAMES = 9009;

	public void showSavedGamesUI() {
		SnapshotsClient snapshotsClient =
				Games.getSnapshotsClient(this, GoogleSignIn.getLastSignedInAccount(this));
		int maxNumberOfSavedGamesToShow = 5;

		Task<Intent> intentTask = snapshotsClient.getSelectSnapshotIntent(
				"See My Saves", true, true, maxNumberOfSavedGamesToShow);

		intentTask.addOnSuccessListener(new OnSuccessListener<Intent>() {
			@Override
			public void onSuccess(Intent intent) {
				startActivityForResult(intent, RC_SAVED_GAMES);
			}
		});
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode,
									Intent intent) {
		if (intent != null) {
			if (intent.hasExtra(SnapshotsClient.EXTRA_SNAPSHOT_METADATA)) {
				System.out.println("Loading");

				loadSnapshot().addOnSuccessListener(new OnSuccessListener<byte[]>() {
					@Override
					public void onSuccess(byte[] bytes) {
						System.out.println("Successfully loaded data");
						Json json = new Json();
						SaveGame saveGame = json.fromJson(SaveGame.class, new String(bytes));
						System.out.println(saveGame);
						//Write to local and load
						Util.saveGame(saveGame);
						Util.loadGame();
						System.out.println("Finished loading");
					}
				});

			} else if (intent.hasExtra(SnapshotsClient.EXTRA_SNAPSHOT_NEW)) {
				System.out.println("Saving");
				SnapshotsClient snapshotsClient = Games.getSnapshotsClient(this, GoogleSignIn.getLastSignedInAccount(this));
				snapshotsClient.open(SaveGame.SAVE_NAME, true).addOnSuccessListener(new OnSuccessListener<SnapshotsClient.DataOrConflict<Snapshot>>() {
					@Override
					public void onSuccess(SnapshotsClient.DataOrConflict<Snapshot> snapshotDataOrConflict) {
						System.out.println("Success Opening Snapshot");
						Json json = new Json();
						SaveGame saveGame = json.fromJson(SaveGame.class, Gdx.files.local("saves/null/saveGame.json"));
						writeSnapshot(snapshotDataOrConflict.getData(), saveGame.getBytes());
					}
				});

			}
		}
	}

	private void writeSnapshot(Snapshot snapshot, byte[] data) {

		// Set the data payload for the snapshot
		snapshot.getSnapshotContents().writeBytes(data);

		Bitmap coverImage = Bitmap.createBitmap(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(coverImage);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawText("Pixel Dungeon", 0F, 0F, paint);
		paint.setColor(Color.BLACK);
		canvas.drawRect(0F, 0F, (float) PixelDungeon.V_WIDTH, (float) PixelDungeon.V_HEIGHT, paint);


		// Create the change operation
		SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
				.setCoverImage(coverImage)
				.setDescription(SaveGame.DESC)
				.build();

		SnapshotsClient snapshotsClient =
				Games.getSnapshotsClient(this, GoogleSignIn.getLastSignedInAccount(this));

		// Commit the operation
		snapshotsClient.commitAndClose(snapshot, metadataChange);
	}

	private Task<byte[]> loadSnapshot() {
		// Get the SnapshotsClient from the signed in account.
		SnapshotsClient snapshotsClient =
				Games.getSnapshotsClient(this, GoogleSignIn.getLastSignedInAccount(this));

		// In the case of a conflict, the most recently modified version of this snapshot will be used.
		int conflictResolutionPolicy = SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED;

		// Open the saved game using its name.
		return snapshotsClient.open(SaveGame.SAVE_NAME, true, conflictResolutionPolicy)
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.e(TAG, "Error while opening Snapshot.", e);
					}
				}).continueWith(new Continuation<SnapshotsClient.DataOrConflict<Snapshot>, byte[]>() {
					@Override
					public byte[] then(@NonNull Task<SnapshotsClient.DataOrConflict<Snapshot>> task) throws Exception {
						Snapshot snapshot = task.getResult().getData();

						// Opening the snapshot was a success and any conflicts have been resolved.
						try {
							// Extract the raw data from the snapshot.
							return snapshot.getSnapshotContents().readFully();
						} catch (IOException e) {
							Log.e(TAG, "Error while reading Snapshot.", e);
						}

						return null;
					}
				});
	}

}
