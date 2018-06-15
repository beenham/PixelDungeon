package net.team11.pixeldungeon.utils.stats;

import java.util.Locale;

public class GlobalStats {
    private int totalTime;
    private int totalAttempts;
    private int totalCompleted;
    private int totalChestsFound;
    private int totalKeysFound;
    private int totalItemsFound;

    public String getTime() {
        return String.format(Locale.UK,"%02d:%02d", totalTime/60,totalTime%60);
    }

    public int getTotalAttempts() {
        return totalAttempts;
    }

    public int getTotalCompleted() {
        return totalCompleted;
    }

    public int getTotalChestsFound() {
        return totalChestsFound;
    }

    public int getTotalKeysFound() {
        return totalKeysFound;
    }

    public int getTotalItemsFound() {
        return totalItemsFound;
    }

    public void addTime(int time) {
        totalTime += time;
    }

    public void incrementAttempts() {
        totalAttempts++;
    }

    public void incrementCompleted() {
        totalCompleted++;
    }

    public void incrementChestsFound() {
        totalChestsFound++;
    }

    public void addChestsFound(int chests) {totalChestsFound+=chests;}

    public void incrementKeysFound() {
        totalKeysFound++;
    }

    public void addKeysFound(int keys) {totalKeysFound+=keys;}

    public void incrementItemsFound() {
        totalItemsFound++;
    }

    public void addItemsFound(int items) {totalItemsFound+=items;}
}
