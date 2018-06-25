package net.team11.pixeldungeon.utils.crossplatform;

public interface AndroidInterface {
    ////    User Information
    String getUserName();
    String getUserEmail();

    ////    Google Play Log in
    boolean isSignedIn();
    void signIn();
    void signInSilently();
    void signOut();

    ////    Google Play Calls
    void openAchievements();

    ////    Earning Achievements
    void earnNewAdventurer();
    void earnLetsTryAgain();
}
