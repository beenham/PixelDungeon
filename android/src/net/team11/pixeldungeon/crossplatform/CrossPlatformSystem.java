package net.team11.pixeldungeon.crossplatform;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadata;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import net.team11.pixeldungeon.AndroidLauncher;
import net.team11.pixeldungeon.R;
import net.team11.pixeldungeon.playservices.AdmobClient;
import net.team11.pixeldungeon.playservices.GoogleClient;
import net.team11.pixeldungeon.saves.SaveGame;
import net.team11.pixeldungeon.utils.crossplatform.AndroidInterface;
import net.team11.pixeldungeon.utils.stats.GlobalStats;
import net.team11.pixeldungeon.utils.stats.LevelStats;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CrossPlatformSystem implements AndroidInterface {
    private String TAG = "PixelDungeon";
    private AndroidLauncher mActivity;
    private AdmobClient admobClient;
    private GoogleClient googleClient;

    private boolean earnAchievements = false;
    private boolean watchAds = true;

    private Snapshot currentSnapshot;

    public CrossPlatformSystem(AndroidLauncher mActivity){
        this.mActivity = mActivity;
        this.admobClient = mActivity.getAdmobClient();
        this.googleClient = mActivity.getGoogleClient();
    }

    @Override
    public String getUserName() {
        return googleClient.getUserName();
    }

    @Override
    public String getUserEmail() {
        return null;
    }

    @Override
    public boolean isSignedIn() {
        return googleClient.isSignedIn();
    }

    @Override
    public void signIn() {
        googleClient.startSignInIntent();
    }

    @Override
    public void signInSilently() {
        googleClient.signInSilently();
    }

    @Override
    public void signOut() {
        googleClient.signOut();
    }

    @Override
    public void openAchievements() {
        if (googleClient.getmSignedInAccount() != null) {
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .getAchievementsIntent()
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            mActivity.startActivityForResult(intent, GoogleClient.RC_ACHIEVEMENT_UI);
                        }
                    });
        }
    }

    @Override
    public void openLeaderboards() {
        if (googleClient.getmSignedInAccount() != null) {
            Games.getLeaderboardsClient(mActivity, googleClient.getmSignedInAccount())
                    .getAllLeaderboardsIntent()
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            mActivity.startActivityForResult(intent, GoogleClient.RC_LEADERBOARD_UI);
                        }
                    });
        }
    }

    @Override
    public void earnNewAdventurer() {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_new_adventurer));
        }
    }

    @Override
    public void earnLetsTryAgain() {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_lets_try_again__));
        }
    }

    @Override
    public void earn10Attempts() {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Log.d(TAG, "Incrementing 10 Attempts!");
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .increment(mActivity.getString(R.string.achievement_attempt_10_dungeons), 1);
        }
    }

    @Override
    public void earn100Attempts() {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Log.d(TAG, "Incrementing 100 Attempts!");
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .increment(mActivity.getString(R.string.achievement_attempt_100_dungeons), 1);
        }
    }

    @Override
    public void earn500Attempts() {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Log.d(TAG, "Incrementing 500 Attempts!");
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .increment(mActivity.getString(R.string.achievement_attempt_500_dungeons), 1);
        }
    }

    @Override
    public void earn1000Attempts() {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Log.d(TAG, "Incrementing 1000 Attempts!");
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .increment(mActivity.getString(R.string.achievement_attempt_1000_dungeons), 1);
        }
    }

    @Override
    public void earnCompleteDungeon1() {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_complete_dungeon_1));
        }
    }

    @Override
    public void earnCompleteDungeon5() {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_complete_dungeon_5));
        }
    }

    @Override
    public void earnCompleteDungeon10() {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_complete_dungeon_10));
        }
    }

    @Override
    public void earnCompleteDungeon15() {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_complete_dungeon_15));
        }
    }

    @Override
    public void earnCompleteDungeon20() {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_complete_dungeon_20));
        }
    }

    @Override
    public void earnCompleteDungeon25() {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_complete_dungeon_25));
        }
    }

    @Override
    public void earnCompletePuzzle1000() {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Log.d(TAG, "Incrementing 1000 Puzzle!");
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .increment(mActivity.getString(R.string.achievement_puzzle_master), 1);
        }
    }

    @Override
    public void earnSimonSays() {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_simon_says));
        }
    }

    @Override
    public void earnColoursAligned() {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_the_colours_have_aligned));
        }
    }

    @Override
    public void earnKeyMaster(int amount) {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Log.d(TAG, "Key master by " + amount + ".");
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .increment(mActivity.getString(R.string.achievement_key_master), amount);
        }
    }

    @Override
    public void earnLootJunkie(int amount) {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Log.d(TAG, "Loot junkie by " + amount + ".");
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .increment(mActivity.getString(R.string.achievement_loot_junkie), amount);
        }
    }

    @Override
    public void earnFullInventory() {
        if (googleClient.getmSignedInAccount() != null && earnAchievements) {
            Games.getAchievementsClient(mActivity, googleClient.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_i_cant_carry_any_more));
        }
    }

    ////    Admob Methods

    @Override
    public void showEndLevelAd() {
        if (watchAds) {
            admobClient.showEndLevelAd();
        }
    }

    @Override
    public void showRewardAd() {
        if (watchAds) {
            admobClient.showRewardAd();
        }
    }

    @Override
    public boolean isRewardAvailable() {
        return admobClient.isRewardAvailable();
    }

    ////    Saved Game Methods

    @Override
    public void saveGame(SaveGame saveGame){
        Json json = new Json();
        String value = json.toJson(saveGame);

//        System.out.println("\tSaving game: " + value);

        //Delete the old save
        if (Gdx.files.local("saves/PixelDungeon.json").exists()){
//            System.out.print("Save file already exists, DELETING: ");
            System.out.println(Gdx.files.local("saves/PixelDungeon.json").delete());
        }

        //write to local storage
        Gdx.files.local("saves/PixelDungeon.json").writeString(value, false);
//        System.out.println("Finished writing to saves/");

        if (googleClient.isSignedIn()){
            System.out.println(googleClient.getUserName() + " is signed in, saving to cloud");
        }
    }

    @Override
    public SaveGame loadSaveGame(){
        signInSilently();

        Json json = new Json();

        SaveGame localSave;
        SaveGame cloudSave;

        //Save data already exists
        if (Gdx.files.local("saves/PixelDungeon.json").exists()){

//            System.out.println("Save file exists, LOADING");

            FileHandle file = Gdx.files.local("saves/PixelDungeon.json");
            SaveGame data = json.fromJson(SaveGame.class, file);

//            System.out.println("Finished loading local, Save Data is " + data);

            HashMap<String, LevelStats> currentStats = data.getLevelStatsHashMap();

            //Check to see if need to update the level stats
            for (FileHandle fileHandle : Gdx.files.internal("stats/levels/").list()){

                LevelStats levelStats = json.fromJson(LevelStats.class, fileHandle);

                if (!data.getLevelStatsHashMap().containsKey(levelStats.getFileName())){
                    System.out.println("Updating save game with new level stats");
                    data.getLevelStatsHashMap().put(levelStats.getFileName(), levelStats);
                }
            }

            HashMap<String, LevelStats> newStats = data.getLevelStatsHashMap();

            //Update the save game
            if (!newStats.equals(currentStats)){
                saveGame(data);
            }

            //Assign the local save
            localSave = data;
//
//            //Check to see if the player is online or not
//            if (mActivity.isSignedIn()){
//                System.out.println(mActivity.getUserName() + "is online");
//                Task<byte[]> snapshot = loadSnapshot();
//
//                byte[] cloudData = snapshot.getResult();
//                System.out.println("Cloud Data is " + cloudData.toString());
//
//            }
//

            //Check to see if the cloud save is different from the local save

            return data;
        } else {
            HashMap<String, LevelStats> levelData = new HashMap<>();
            //Need to create new data for stats and stuff
            for (FileHandle file : Gdx.files.internal("stats/levels/").list()){
                LevelStats stats = json.fromJson(LevelStats.class, file);
                levelData.put(stats.getFileName(), stats);
            }

            FileHandle file = Gdx.files.internal("stats/globalStats.json");
            GlobalStats globalStats = json.fromJson(GlobalStats.class, file);

            String levelString = json.toJson(levelData);
            String globalString = json.toJson(globalStats);

            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

            SaveGame newSave = new SaveGame(levelString, globalString, timeStamp);
            saveGame(newSave);
            System.out.println("Finished creating new save: " + newSave);
            return newSave;
        }
    }

    //Messy temp stuff
    public Task<byte[]> loadSnapshot() {
        // Get the SnapshotsClient from the signed in account.
        SnapshotsClient snapshotsClient =
                Games.getSnapshotsClient(mActivity, googleClient.getmSignedInAccount());

        System.out.println(snapshotsClient.load(true).getResult().get().get(0));

        // In the case of a conflict, the most recently modified version of this snapshot will be used.
        int conflictResolutionPolicy = SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED;

        // Open the saved game using its name.
        return snapshotsClient.open("PixelDungeon", true, conflictResolutionPolicy)
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
                            currentSnapshot = snapshot;
                            // Extract the raw data from the snapshot.
//                            System.out.println("Current snapshot is: " + snapshot);
                            return snapshot.getSnapshotContents().readFully();
                        } catch (IOException e) {
                            Log.e(TAG, "Error while reading Snapshot.", e);
                        }

                        return null;
                    }
                }).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                    @Override
                    public void onComplete(@NonNull Task<byte[]> task) {
                        // Dismiss progress dialog and reflect the changes in the UI when complete.
                        // ...
                    }
                });
    }

    private Task<SnapshotMetadata> writeSnapshot(Snapshot snapshot, byte[] data, String desc) {

        // Set the data payload for the snapshot
        snapshot.getSnapshotContents().writeBytes(data);

        // Create the change operation
        SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
                .setCoverImage(null)
                .setDescription(desc)
                .build();

        SnapshotsClient snapshotsClient =
                Games.getSnapshotsClient(mActivity, GoogleSignIn.getLastSignedInAccount(mActivity));

        // Commit the operation
        return snapshotsClient.commitAndClose(snapshot, metadataChange);
    }

}
