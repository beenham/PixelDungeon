package net.team11.pixeldungeon.utils.stats;

import java.util.HashMap;
import java.util.Locale;

public class LevelStats {
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
