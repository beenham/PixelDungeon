package net.team11.pixeldungeon.utils.stats;

import java.util.Locale;

public class GlobalStats {
    private int totalAttempts;
    private int totalChestsFound;
    private int totalCoins;
    private int currentCoins;
    private int totalCompleted;
    private int totalDeaths;
    private int totalItemsFound;
    private int totalKeysFound;
    private int totalPuzzleAttempted;
    private int totalPuzzleCompleted;
    private int totalTime;

    ///////////////
    //  Getters  //
    ///////////////

    public String getTime() {
        return String.format(Locale.UK,"%02d:%02d", totalTime/60,totalTime%60);
    }

    public int getTotalAttempts() {
        return totalAttempts;
    }

    public int getTotalChestsFound() {
        return totalChestsFound;
    }

    public int getTotalCoins() {
        return totalCoins;
    }

    public int getCurrentCoins() {
        return currentCoins;
    }

    public int getTotalCompleted() {
        return totalCompleted;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public int getTotalKeysFound() {
        return totalKeysFound;
    }

    public int getTotalItemsFound() {
        return totalItemsFound;
    }

    public int getTotalPuzzleAttempted() {
        return totalPuzzleAttempted;
    }

    public int getTotalPuzzleCompleted() {
        return totalPuzzleCompleted;
    }

    ////////////////////
    //  Incrementers  //
    ////////////////////

    public void incrementAttempts() {
        totalAttempts++;
    }

    public void incrementChestsFound() {
        totalChestsFound++;
    }

    public void incrementCompleted() {
        totalCompleted++;
    }

    public void incrementDeaths() {
        totalDeaths++;
    }

    public void incrementItemsFound() {
        totalItemsFound++;
    }

    public void incrementKeysFound() {
        totalKeysFound++;
    }

    public void incrementPuzzleAttempted() {
        totalPuzzleAttempted++;
    }

    public void incrementPuzzleCompleted() {
        totalPuzzleCompleted++;
    }

    //////////////
    //  Adders  //
    //////////////

    public void addTime(int time) {
        totalTime += time;
    }

    public void addChestsFound(int chests) {totalChestsFound+=chests;}

    public void addCoinsFound(int coins) {totalCoins+=coins;}

    public void addKeysFound(int keys) {totalKeysFound+=keys;}

    public void addItemsFound(int items) {totalItemsFound+=items;}

    public void subtractCurrentCoins(int coins) {
        currentCoins -= coins;
    }

    public void addCurrentCoins(int coins) {
        currentCoins += coins;
    }

    @Override
    public String toString() {
        return "GlobalStats{" +
                "totalAttempts=" + totalAttempts +
                ", totalChestsFound=" + totalChestsFound +
                ", totalCoins=" + totalCoins +
                ", currentCoins=" + currentCoins +
                ", totalCompleted=" + totalCompleted +
                ", totalDeaths=" + totalDeaths +
                ", totalItemsFound=" + totalItemsFound +
                ", totalKeysFound=" + totalKeysFound +
                ", totalPuzzleAttempted=" + totalPuzzleAttempted +
                ", totalPuzzleCompleted=" + totalPuzzleCompleted +
                ", totalTime=" + totalTime +
                '}';
    }
}
