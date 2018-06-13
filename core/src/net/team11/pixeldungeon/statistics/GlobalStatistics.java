package net.team11.pixeldungeon.statistics;

/**
 * Class to store and handle the users global statistics for the game
 * All members are static or final
 */
public class GlobalStatistics {

    private static int chests = 0;
    private final int TOTAL_CHESTS = 1;

    private static int keys = 0;
    private final int TOTAL_KEYS = 1;

    private static int coins = 0;
    private final int TOTAL_COINS = 1;

    private static int items = 0;
    private final int TOTAL_ITEMS = 1;
    //private float totalTime;

    private static int levelsCompleted = 0;
    private final int TOTAL_LEVELS = 2;



    public GlobalStatistics(){}

    public static void updateChests(){
        System.out.println("Updating chests");
        chests++;
    }

    public static void updateKeys(){
        System.out.println("Updating keys");
        keys++;
    }

    public static void updateCoins(int amount){
        System.out.println("Updating coins");
        coins += amount;
    }

    public static void upateItems(){
        System.out.println("Updating items");
        items++;
    }

    public static void updateLevelsCompleted(){
        System.out.println("Updating levelsCompleted");
        levelsCompleted++;
    }

    public static int getChests() {
        return chests;
    }

    public static int getKeys() {
        return keys;
    }

    public static int getCoins() {
        return coins;
    }

    public static int getItems() {
        return items;
    }

    public static int getLevelsCompleted() {
        return levelsCompleted;
    }

    @Override
    public String toString(){
        return "//GLOBAL STATISTICS//" + "\nLevels Completed: " + levelsCompleted + "\nChests: " + chests + "\nKeys: " + keys + "\nCoins: " + coins +
                "\nItems: " + items;
    }
}
