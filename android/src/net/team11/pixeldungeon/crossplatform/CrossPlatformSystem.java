package net.team11.pixeldungeon.crossplatform;

import android.content.Intent;
import android.util.Log;

import com.badlogic.gdx.utils.Json;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.tasks.OnSuccessListener;

import net.team11.pixeldungeon.AndroidLauncher;
import net.team11.pixeldungeon.R;
import net.team11.pixeldungeon.playservices.AdmobClient;
import net.team11.pixeldungeon.playservices.GoogleClient;
import net.team11.pixeldungeon.playservices.SavesClient;
import net.team11.pixeldungeon.saves.SaveGame;
import net.team11.pixeldungeon.utils.T11Log;
import net.team11.pixeldungeon.utils.Util;
import net.team11.pixeldungeon.utils.crossplatform.AndroidInterface;

public class CrossPlatformSystem implements AndroidInterface {
    private String TAG = "PixelDungeon";
    private AndroidLauncher mActivity;
    private AdmobClient admobClient;
    private GoogleClient googleClient;
    private SavesClient savesClient;

    private boolean earnAchievements = false;
    private boolean watchAds = true;
    private boolean savingEnabled = true;

    private Snapshot currentSnapshot;

    public CrossPlatformSystem(AndroidLauncher mActivity) {
        this.mActivity = mActivity;
        this.admobClient = mActivity.getAdmobClient();
        this.googleClient = mActivity.getGoogleClient();
        this.savesClient = mActivity.getSavesClient();
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
    public void showCloudSaves() {
//        mActivity.showSavedGamesUI();
    }

    @Override
    public void saveGame() {
        if (savingEnabled) {
            if (googleClient.isSignedIn() && !savesClient.isSaving()) {
                savesClient.saveGame();
            }
        }
    }

    @Override
    public void loadSaveGame() {
        if (savingEnabled) {
            if (googleClient.isSignedIn() && !savesClient.isLoading()) {
                savesClient.loadSnapshot().addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Json json = new Json();
                        SaveGame saveGame = json.fromJson(SaveGame.class, new String(bytes));
                        Util.getInstance().saveGame(saveGame);
                        Util.getInstance().loadGame();
                    }
                });
            }
        }
    }

    @Override
    public void debugCall(int type, String tag, String message) {
        switch (type) {
            case T11Log.ERROR:
                Log.e(tag,message);
                break;
            case T11Log.INFO:
                Log.i(tag,message);
                break;
            case T11Log.DEBUG:
                Log.d(tag,message);
                break;
            case T11Log.VERBOSE:
                Log.v(tag,message);
                break;
        }
    }
}
