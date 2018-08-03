package net.team11.pixeldungeon.utils.stats;

import java.util.HashMap;
import java.util.Locale;

public class LevelStats {
    public static final String TARGET_TIME_1 = "Time One";
    public static final String TARGET_TIME_2 = "Time Two";
    public static final String TARGET_TIME_3 = "Time Three";

    private float versionNumber;
    private String fileName;
    private String levelName;
    private int totalChests;
    private int totalKeys;
    private int totalItems;

    private int bestTime;
    private int attempts;
    private int completed;

    private HashMap<String, Boolean> foundChests = new HashMap<>();
    private HashMap<String, Boolean> foundKeys = new HashMap<>();
    private HashMap<String, Boolean> foundItems = new HashMap<>();
    private HashMap<String, Integer> targetTimes = new HashMap<>();

    private boolean tutorial;

    public String getFileName() {
        return fileName;
    }

    public String getLevelName() { return levelName; }

    public int getTotalChests() {
        return totalChests;
    }

    public int getTotalKeys() {
        return totalKeys;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public String getBestTime() {
        return String.format(Locale.UK,"%02d:%02d",bestTime/60,bestTime%60);
    }

    public int getBestTimeVal() {
        return bestTime;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getCompleted() {
        return completed;
    }

    public int getFoundChests() {
        int amount = 0;
        for (String key : foundChests.keySet()) {
            if (foundChests.get(key)) {
                amount++;
            }
        }
        return amount;
    }

    public int getFoundKeys() {
        int amount = 0;
        for (String key : foundKeys.keySet()) {
            if (foundKeys.get(key)) {
                amount++;
            }
        }
        return amount;
    }

    public int getFoundItems() {
        int amount = 0;
        for (String key : foundItems.keySet()) {
            if (foundItems.get(key)) {
                amount++;
            }
        }
        return amount;
    }

    public void submitBestTime(int bestTime) {
        if (bestTime < this.bestTime) {
            this.bestTime = bestTime;
        }
    }

    public int getTargetTime(String value) {
        switch (value) {
            case TARGET_TIME_1:
                return targetTimes.get(value);
            case TARGET_TIME_2:
                return targetTimes.get(value);
            case TARGET_TIME_3:
                return targetTimes.get(value);
            default:
                return 0;
        }
    }

    public void incrementAttempts() {
        attempts++;
    }

    public void incrementCompleted() {
        completed++;
    }

    public void setChestFound(String name) {
        if (foundChests.containsKey(name)) {
            foundChests.remove(name);
            foundChests.put(name,true);
        }
    }

    public void setKeyFound(String name) {
        if (foundKeys.containsKey(name)) {
            foundKeys.remove(name);
            foundKeys.put(name,true);
        }
    }

    public void setItemFound(String name) {
        if (foundItems.containsKey(name)) {
            foundItems.remove(name);
            foundItems.put(name,true);
        }
    }

    public boolean isTutorial() {
        return tutorial;
    }

    public float getVersionNumber() {
        return versionNumber;
    }

    public void update(LevelStats newStats) {
        this.versionNumber = newStats.versionNumber;
        this.levelName = newStats.levelName;
        this.totalChests = newStats.totalChests;
        this.totalKeys = newStats.totalKeys;
        this.totalItems = newStats.totalItems;

        for (String key : newStats.foundChests.keySet()) {
            if (this.foundChests.containsKey(key)) {
                newStats.foundChests.put(key,this.foundChests.get(key));
            }
        }
        for (String key : newStats.foundKeys.keySet()) {
            if (this.foundKeys.containsKey(key)) {
                newStats.foundKeys.put(key,this.foundKeys.get(key));
            }
        }
        for (String key : newStats.foundItems.keySet()) {
            if (this.foundItems.containsKey(key)) {
                newStats.foundItems.put(key,this.foundItems.get(key));
            }
        }

        this.foundChests = newStats.foundChests;
        this.foundKeys = newStats.foundKeys;
        this.foundItems = newStats.foundItems;
        this.targetTimes = newStats.targetTimes;
        this.tutorial = newStats.tutorial;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Level Name : ").append(fileName).append('\n');
        s.append("Best Time : ").append(String.format(Locale.UK,"%02d:%02d",bestTime/60,bestTime%60)).append('\n');
        s.append("Attempts : ").append(attempts).append('\n');
        s.append("Completed : ").append(completed).append('\n');
        s.append("Total Chests : ").append(totalChests).append('\n');
        s.append("\t").append("Found : ").append('\n');
        for (String key : foundChests.keySet()) {
            s.append("\t\t").append(key).append(" : ").append(foundChests.get(key)).append('\n');
        }
        s.append("Total Keys : ").append(totalKeys).append('\n');
        s.append("\t").append("Found : ").append('\n');
        for (String key : foundKeys.keySet()) {
            s.append("\t\t").append(key).append(" : ").append(foundKeys.get(key)).append('\n');
        }
        s.append("Total Items : ").append(totalItems).append('\n');
        s.append("\t").append("Found : ").append('\n');
        for (String key : foundItems.keySet()) {
            s.append("\t\t").append(key).append(" : ").append(foundItems.get(key)).append('\n');
        }

        return s.toString();
    }
}
