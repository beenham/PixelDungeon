package net.team11.pixeldungeon.playservices;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;

import net.team11.pixeldungeon.AndroidLauncher;
import net.team11.pixeldungeon.R;
import net.team11.pixeldungeon.playservices.adlisteners.RewardAdListener;

public class AdmobClient {
    private static final String TAG = "AdmobClient";

    private AndroidLauncher mActivity;

    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;

    public AdmobClient(AndroidLauncher mActivity) {
        this.mActivity = mActivity;
        setupAdmobClient();
    }

    private void setupAdmobClient() {
        MobileAds.initialize(mActivity,mActivity.getString(R.string.admob_id));
        setupInterAd();
        setupRewardAd();
    }

    private void setupInterAd() {
        mInterstitialAd = new InterstitialAd(mActivity);
        // USING TEMP AD FOR TESTING
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        // mInterstitialAd.setAdUnitId(getString(R.string.admob_level_complete));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                loadEndLevelAd();
            }
        });
    }

    private void setupRewardAd() {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mActivity);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardAdListener(this));

        loadRewardAd();
    }

    private void loadEndLevelAd() {
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public void loadRewardAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    public void showEndLevelAd() {
        System.out.println("SHOWING METHOD");
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd.isLoaded()) {
                    System.out.println("SHOWING");
                    mInterstitialAd.show();
                }
            }
        });
    }

    public void showRewardAd() {
        System.out.println("SHOWING METHOD");
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRewardedVideoAd.isLoaded()) {
                    System.out.println("SHOWING");
                    mRewardedVideoAd.show();
                }
            }
        });
    }

    public boolean isRewardAvailable() {
        return ((RewardAdListener)mRewardedVideoAd.getRewardedVideoAdListener()).isLoaded();
    }
}
