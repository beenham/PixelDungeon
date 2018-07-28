package net.team11.pixeldungeon;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import net.team11.pixeldungeon.crossplatform.CrossPlatformSystem;
import net.team11.pixeldungeon.saves.SaveGame;

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
    protected void onCreate(Bundle savedInstanceState) {
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
        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
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
            Games.getGamesClient(this, googleSignInAccount).setGravityForPopups(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        }
    }

    public GoogleSignInAccount getmSignedInAccount() {
        return mSignedInAccount;
    }

    public void saveGame() {
        SnapshotsClient snapshotsClient = Games.getSnapshotsClient(this, GoogleSignIn.getLastSignedInAccount(this));
        snapshotsClient.open(SaveGame.SAVE_NAME, true).addOnSuccessListener(new OnSuccessListener<SnapshotsClient.DataOrConflict<Snapshot>>() {
            @Override
            public void onSuccess(SnapshotsClient.DataOrConflict<Snapshot> snapshotDataOrConflict) {
                System.out.println("Success Opening Snapshot");
                Json json = new Json();
                final SaveGame saveGame = json.fromJson(SaveGame.class, Gdx.files.local("saves/saveGame.json"));

                System.out.println("Current local version has more play time");
                processSnapshotOpenResult(snapshotDataOrConflict, 5).addOnSuccessListener(new OnSuccessListener<Snapshot>() {
                    @Override
                    public void onSuccess(Snapshot snapshot) {
                        if (getPlayTime(saveGame.getBytes()) > snapshot.getMetadata().getPlayedTime()) {
                            writeSnapshot(snapshot, saveGame.getBytes());
                        } else {
                            System.out.println("Current local version has less play time");
                        }
                    }
                });


            }
        });

    }

    private void writeSnapshot(Snapshot snapshot, byte[] data) {
        snapshot.getSnapshotContents().writeBytes(data);

        Bitmap coverImage = Bitmap.createBitmap(PixelDungeon.V_WIDTH, PixelDungeon.V_HEIGHT, Bitmap.Config.ARGB_8888);

        // Create the change operation
        SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
                .setCoverImage(coverImage)
                .setPlayedTimeMillis(getPlayTime(data))
                .setDescription(SaveGame.DESC)
                .build();

        SnapshotsClient snapshotsClient =
                Games.getSnapshotsClient(this, GoogleSignIn.getLastSignedInAccount(this));

        // Commit the operation
        snapshotsClient.commitAndClose(snapshot, metadataChange);
    }

    private final long MILLIS_PER_SECOND = 1000;
    private final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    private final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;

    private long getPlayTime(byte[] data) {
        Json json = new Json();
        SaveGame saveGame = json.fromJson(SaveGame.class, new String(data));

        String[] split = saveGame.getGlobalStats().getTime().split(":");
        long seconds = Long.parseLong(split[1]) * MILLIS_PER_SECOND;
        long minutes = Long.parseLong(split[0]) * MILLIS_PER_MINUTE;

        return seconds + minutes;
    }

    public Task<byte[]> loadSnapshot() {
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


    private static final int MAX_SNAPSHOT_RESOLVE_RETRIES = 10;

    Task<Snapshot> processSnapshotOpenResult(SnapshotsClient.DataOrConflict<Snapshot> result,
                                             final int retryCount) {

        if (!result.isConflict()) {
            // There was no conflict, so return the result of the source.
            TaskCompletionSource<Snapshot> source = new TaskCompletionSource<>();
            source.setResult(result.getData());
            return source.getTask();
        }

        // There was a conflict.  Try resolving it by selecting the newest of the conflicting snapshots.
        // This is the same as using RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED as a conflict resolution
        // policy, but we are implementing it as an example of a manual resolution.
        // One option is to present a UI to the user to choose which snapshot to resolve.
        SnapshotsClient.SnapshotConflict conflict = result.getConflict();

        Snapshot snapshot = conflict.getSnapshot();
        Snapshot conflictSnapshot = conflict.getConflictingSnapshot();

        // Resolve between conflicts by selecting the newest of the conflicting snapshots.
        Snapshot resolvedSnapshot = snapshot;

        if (snapshot.getMetadata().getLastModifiedTimestamp() <
                conflictSnapshot.getMetadata().getLastModifiedTimestamp()) {
            resolvedSnapshot = conflictSnapshot;
        }

        return Games.getSnapshotsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .resolveConflict(conflict.getConflictId(), resolvedSnapshot)
                .continueWithTask(
                        new Continuation<
                                SnapshotsClient.DataOrConflict<Snapshot>,
                                Task<Snapshot>>() {
                            @Override
                            public Task<Snapshot> then(
                                    @NonNull Task<SnapshotsClient.DataOrConflict<Snapshot>> task)
                                    throws Exception {
                                // Resolving the conflict may cause another conflict,
                                // so recurse and try another resolution.
                                if (retryCount < MAX_SNAPSHOT_RESOLVE_RETRIES) {
                                    return processSnapshotOpenResult(task.getResult(), retryCount + 1);
                                } else {
                                    throw new Exception("Could not resolve snapshot conflicts");
                                }
                            }

                        });
    }
}
