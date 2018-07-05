package net.team11.pixeldungeon.utils.crossplatform;

import net.team11.pixeldungeon.saves.SaveGame;

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
    void openLeaderboards();

    ////    Earning Achievements
    void earnNewAdventurer();
    void earn10Attempts();
    void earn100Attempts();
    void earn500Attempts();
    void earn1000Attempts();

    void earnCompleteDungeon1();
    void earnCompleteDungeon5();
    void earnCompleteDungeon10();
    void earnCompleteDungeon15();
    void earnCompleteDungeon20();
    void earnCompleteDungeon25();

    void earnCompletePuzzle1000();

    void earnLetsTryAgain();
    void earnSimonSays();
    void earnColoursAligned();
    void earnKeyMaster(int amount);
    void earnLootJunkie(int amount);
    void earnFullInventory();

    void saveGame(SaveGame saveGame);
    SaveGame loadSaveGame();
}
