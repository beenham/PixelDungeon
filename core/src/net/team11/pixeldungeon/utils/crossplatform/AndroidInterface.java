package net.team11.pixeldungeon.utils.crossplatform;

public interface AndroidInterface {
    void showAlertDialog(AlertDialogCallback callback);

    ////    Google Play Log in
    boolean isSignedIn();
    void signInSilently();
    void signOut();

    ////    Earning Achievements
    void earnNewAdventurer();
}
