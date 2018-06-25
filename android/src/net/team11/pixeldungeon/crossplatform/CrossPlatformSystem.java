package net.team11.pixeldungeon.crossplatform;

import android.content.Intent;

import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnSuccessListener;

import net.team11.pixeldungeon.AndroidLauncher;
import net.team11.pixeldungeon.R;
import net.team11.pixeldungeon.utils.crossplatform.AndroidInterface;

public class CrossPlatformSystem implements AndroidInterface {
    private String TAG = "PixelDungeon";
    private AndroidLauncher mActivity; // This is the main android activity

    public CrossPlatformSystem(AndroidLauncher mActivity){
        this.mActivity = mActivity;
    }

    @Override
    public String getUserName() {
        return mActivity.getUserName();
    }

    @Override
    public String getUserEmail() {
        return null;
    }

    @Override
    public boolean isSignedIn() {
        return mActivity.isSignedIn();
    }

    @Override
    public void signIn() {
        mActivity.startSignInIntent();
    }

    @Override
    public void signInSilently() {
        mActivity.signInSilently();
    }

    @Override
    public void signOut() {
        mActivity.signOut();
    }

    @Override
    public void earnNewAdventurer() {
        /*
        if (mActivity.getmSignedInAccount() != null) {
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_new_adventurer));
        }
        */
    }

    @Override
    public void earnLetsTryAgain() {

    }

    @Override
    public void openAchievements() {
        Games.getAchievementsClient(mActivity,mActivity.getmSignedInAccount())
                .getAchievementsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        mActivity.startActivityForResult(intent, AndroidLauncher.RC_ACHIEVEMENT_UI);
                    }
                });
    }

    @Override
    public void openLeaderboards() {
        Games.getLeaderboardsClient(mActivity,mActivity.getmSignedInAccount())
                .getLeaderboardIntent(mActivity.getString(R.string.leaderboard_best_times))
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        mActivity.startActivityForResult(intent, AndroidLauncher.RC_LEADERBOARD_UI);
                    }
                });
    }

    @Override
    public void earn10Attempts() {

    }

    @Override
    public void earn100Attempts() {

    }

    @Override
    public void earn500Attempts() {

    }

    @Override
    public void earn1000Attempts() {

    }

    @Override
    public void earnCompleteDungeon1() {

    }

    @Override
    public void earnCompleteDungeon5() {

    }

    @Override
    public void earnCompleteDungeon10() {

    }

    @Override
    public void earnCompleteDungeon15() {

    }

    @Override
    public void earnCompleteDungeon20() {

    }

    @Override
    public void earnCompleteDungeon25() {

    }

    @Override
    public void earnSimonSays() {

    }

    @Override
    public void earnColoursAligned() {

    }

    @Override
    public void earnKeyMaster() {

    }

    @Override
    public void earnLootJunkie() {

    }

    @Override
    public void earnFullInventory() {

    }
}
