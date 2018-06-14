package net.team11.pixeldungeon.statistics;


/**
 * Class to handle the storage of the total statistics for each level
 */
public class TotalLevelStatistics {
    private int totalChests;
    private int totalKeys;
    private int totalItems;

    private String level;

    public TotalLevelStatistics(String level, int totalChests, int totalKeys, int totalItems){
        this.level = level;
        this.totalChests = totalChests;
        this.totalKeys = totalKeys;
        this.totalItems = totalItems;
    }

    public int getTotalChests(){
        return this.totalChests;
    }

    public int getTotalKeys(){
        return this.totalKeys;
    }

    public int getTotalItems(){
        return this.totalItems;
    }

    @Override
    public String toString(){
        return "Level: " + level + "\nChests: " + totalChests + "\nKeys: " + totalKeys + "\nItems: " + totalItems + "\n";
    }
}
