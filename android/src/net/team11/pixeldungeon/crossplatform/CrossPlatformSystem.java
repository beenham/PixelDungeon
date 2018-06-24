package net.team11.pixeldungeon.crossplatform;

import android.content.Intent;

import com.google.android.gms.games.Games;

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
        if (mActivity.getmSignedInAccount() != null) {
            Games.getAchievementsClient(mActivity, mActivity.getmSignedInAccount())
                    .unlockImmediate(mActivity.getString(R.string.achievement_new_adventurer));
        }
    }

    @Override
    public void earnLetsTryAgain() {

    }

    @Override
    public void openAchievements() {
        Intent achieveIntent = mActivity.getPackageManager().
                getLaunchIntentForPackage("com.google.android.play.games");
        mActivity.startActivity(achieveIntent);
    }
}
