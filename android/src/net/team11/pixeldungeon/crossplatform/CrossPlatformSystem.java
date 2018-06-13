package net.team11.pixeldungeon.crossplatform;

import android.app.AlertDialog;
import android.content.DialogInterface;


import net.team11.pixeldungeon.AndroidLauncher;
import net.team11.pixeldungeon.utils.crossplatform.AlertDialogCallback;
import net.team11.pixeldungeon.utils.crossplatform.AndroidInterface;

public class CrossPlatformSystem implements AndroidInterface {

    private AndroidLauncher mActivity; // This is the main android activity

    public CrossPlatformSystem(AndroidLauncher mActivity){
        this.mActivity = mActivity;
    }

    @Override
    public boolean isSignedIn() {
        return mActivity.isSignedIn();
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

    }

    public void showAlertDialog(final AlertDialogCallback callback){
        mActivity.runOnUiThread(new Runnable(){

            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Test");
                builder.setMessage("Testing");
                builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        callback.positiveButtonPressed();

                    }
                });
                builder.setNegativeButton("Nope", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        callback.negativeButtonPressed();

                    }

                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
