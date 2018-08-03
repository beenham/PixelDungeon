package net.team11.pixeldungeon.playservices.adlisteners;

import android.util.Log;

import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import net.team11.pixeldungeon.playservices.AdmobClient;
import net.team11.pixeldungeon.screens.AbstractScreen;
import net.team11.pixeldungeon.screens.ScreenManager;
import net.team11.pixeldungeon.screens.components.dialog.RewardDialog;
import net.team11.pixeldungeon.utils.Util;

public class RewardAdListener implements RewardedVideoAdListener {
    private static final String TAG = "RewardAdListener";
    private AdmobClient admobClient;
    private boolean loaded;

    public RewardAdListener(AdmobClient admobClient) {
        this.admobClient = admobClient;
        loaded = false;
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        loaded = true;
        Log.i(TAG,"Video loaded..");
    }

    @Override
    public void onRewardedVideoAdOpened() {
        loaded = false;
        Log.i(TAG,"Video opened..");
    }

    @Override
    public void onRewardedVideoStarted() {
        Log.i(TAG,"Video started..");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Log.i(TAG,"Video closed..");
        admobClient.loadRewardAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Log.i(TAG,"Video rewarded.. " + rewardItem.getType() + ": " + rewardItem.getAmount());
        Util.getInstance().getStatsUtil().getGlobalStats().addCurrentCoins(rewardItem.getAmount());
        Util.getInstance().saveGame();
        new RewardDialog(rewardItem.getAmount()).show((AbstractScreen)ScreenManager.getInstance().getScreen());
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Log.i(TAG,"Video rewarded but left..");

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {
        Log.i(TAG,"Video Completed..");
    }

    public boolean isLoaded() {
        Log.i(TAG,"Is loaded? " + loaded);
        if (!loaded) {
            admobClient.loadRewardAd();
        }
        return loaded;
    }
}
