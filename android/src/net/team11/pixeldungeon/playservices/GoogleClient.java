package net.team11.pixeldungeon.playservices;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import net.team11.pixeldungeon.AndroidLauncher;

public class GoogleClient {
    public static int RC_SIGN_IN = 9001;
    public static int RC_ACHIEVEMENT_UI = 9003;
    public static int RC_LEADERBOARD_UI = 9004;
    private AndroidLauncher mActivity;

    private GoogleSignInClient mGoogleSignInClient = null;
    private GoogleSignInAccount mSignedInAccount = null;
    private Player mGooglePlayer = null;

    public GoogleClient(AndroidLauncher mActivity) {
        this.mActivity = mActivity;
        setupGoogleClient();
    }

    private void setupGoogleClient() {
        mGoogleSignInClient = GoogleSignIn.getClient(mActivity, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInSilently();
    }


    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(mActivity) != null;
    }

    public void signInSilently() {
        mGoogleSignInClient.silentSignIn().addOnCompleteListener(mActivity,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            onConnected(task.getResult());
                        }
                    }
                });
    }

    public void startSignInIntent() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        mActivity.startActivityForResult(intent, RC_SIGN_IN);
    }

    public void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(mActivity,
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
        if (mSignedInAccount != googleSignInAccount) {
            mSignedInAccount = googleSignInAccount;

            // get the playerId from the PlayersClient
            PlayersClient playersClient = Games.getPlayersClient(mActivity, googleSignInAccount);
            playersClient.getCurrentPlayer()
                    .addOnSuccessListener(new OnSuccessListener<Player>() {
                        @Override
                        public void onSuccess(Player player) {
                            mGooglePlayer = player;
                        }
                    });
            Games.getGamesClient(mActivity, googleSignInAccount)
                    .setViewForPopups(mActivity.findViewById(android.R.id.content));
            Games.getGamesClient(mActivity, googleSignInAccount)
                    .setGravityForPopups(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
        }
    }

    public GoogleSignInAccount getmSignedInAccount() {
        return mSignedInAccount;
    }
}
