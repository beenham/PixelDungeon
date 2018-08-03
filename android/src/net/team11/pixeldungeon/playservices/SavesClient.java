package net.team11.pixeldungeon.playservices;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import net.team11.pixeldungeon.AndroidLauncher;
import net.team11.pixeldungeon.PixelDungeon;
import net.team11.pixeldungeon.saves.SaveGame;
import net.team11.pixeldungeon.utils.Util;

import java.io.IOException;

public class SavesClient {
    private final String TAG = "SavesClient";
    private final long MILLIS_PER_SECOND = 1000;
    private final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    private final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;

    private AndroidLauncher mActivity;
    private boolean loading;
    private boolean saving;

    public SavesClient(AndroidLauncher mActivity) {
        this.mActivity = mActivity;
        loading = false;
        saving = false;
    }

    public void saveGame() {
        saving = true;
        SnapshotsClient snapshotsClient = Games.getSnapshotsClient(mActivity, GoogleSignIn.getLastSignedInAccount(mActivity));
        snapshotsClient.open(SaveGame.SAVE_NAME, true).addOnSuccessListener(new OnSuccessListener<SnapshotsClient.DataOrConflict<Snapshot>>() {
            @Override
            public void onSuccess(SnapshotsClient.DataOrConflict<Snapshot> snapshotDataOrConflict) {
                Log.e(TAG,"Success Opening Snapshot");
                Json json = new Json();
                final SaveGame localSave = json.fromJson(SaveGame.class, Gdx.files.local(Util.PLAYER_SAVE_PATH));

                processSnapshotOpenResult(snapshotDataOrConflict, 5).addOnSuccessListener(new OnSuccessListener<Snapshot>() {
                    @Override
                    public void onSuccess(Snapshot snapshot) {
                        if (getPlayTime(localSave.getBytes()) >= snapshot.getMetadata().getPlayedTime()) {
                            writeSnapshot(snapshot, localSave.getBytes());
                            Log.e(TAG,"Current local version has more or equal play time - Saving");
                        } else {
                            Log.e(TAG,"Current local version has less play time - Not saving");
                        }
                    }
                });
            }
        })
        .addOnCompleteListener(new OnCompleteListener<SnapshotsClient.DataOrConflict<Snapshot>>() {
            @Override
            public void onComplete(@NonNull Task<SnapshotsClient.DataOrConflict<Snapshot>> task) {
                saving = false;
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
                Games.getSnapshotsClient(mActivity, GoogleSignIn.getLastSignedInAccount(mActivity));

        // Commit the operation
        snapshotsClient.commitAndClose(snapshot, metadataChange);
    }

    private long getPlayTime(byte[] data) {
        Json json = new Json();
        SaveGame saveGame = json.fromJson(SaveGame.class, new String(data));

        String[] split = saveGame.getGlobalStats().getTime().split(":");
        long seconds = Long.parseLong(split[1]) * MILLIS_PER_SECOND;
        long minutes = Long.parseLong(split[0]) * MILLIS_PER_MINUTE;

        return seconds + minutes;
    }

    public Task<byte[]> loadSnapshot() {
        loading = true;
        // Get the SnapshotsClient from the signed in account.
        SnapshotsClient snapshotsClient =
                Games.getSnapshotsClient(mActivity, GoogleSignIn.getLastSignedInAccount(mActivity));

        // In the case of a conflict, the most recently modified version of this snapshot will be used.
        int conflictResolutionPolicy = SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED;

        // Open the saved game using its name.
        return snapshotsClient.open(SaveGame.SAVE_NAME, true, conflictResolutionPolicy)
                .addOnCompleteListener(new OnCompleteListener<SnapshotsClient.DataOrConflict<Snapshot>>() {
                    @Override
                    public void onComplete(@NonNull Task<SnapshotsClient.DataOrConflict<Snapshot>> task) {
                        loading = false;
                    }
                })
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

    private Task<Snapshot> processSnapshotOpenResult(SnapshotsClient.DataOrConflict<Snapshot> result,
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

        return Games.getSnapshotsClient(mActivity, GoogleSignIn.getLastSignedInAccount(mActivity))
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

    public boolean isLoading() {
        return loading;
    }

    public boolean isSaving() {
        return saving;
    }
}
