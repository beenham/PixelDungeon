package net.team11.pixeldungeon.statistics;

/**
 * Class to store and handle the statistics associated with each level in the game
 * NOTE : Items in this class refer to items that the player finds OTHER than coins, keys, etc.
 */
public class LevelStatistics{

    private int attempts;

    private int numChests, numKeys, numItems;
    private int totalChests = 0, totalKeys = 0, totalItems = 0;

    private String bestTime;
    private String level;

    private boolean completed;

    public LevelStatistics(String level){
        this.level = level;
        this.attempts = 0;
        this.numChests = 0;
        this.numKeys = 0;
        this.numItems = 0;
        this.bestTime = "00.00";
        this.completed = false;
    }

    /////////////////////////////
    //      Update Methods     //
    /////////////////////////////
    public void updateChests(){
        System.out.println("Updating chests statistics\n\t-Old Value: " + numChests);
        numChests++;
        System.out.println("Updating chests statistics\n\t-New Value: " + numChests);
    }

    public void updateKeys(){
        System.out.println("Updating keys statistics\n\t-Old Value: " + numKeys);
        numKeys++;
        System.out.println("Updating keys statistics\n\t-New Value: " + numKeys);
    }

    public void updateItems(){
        System.out.println("Updating items statistics\n\t-Old Value: " + numItems);
        numItems++;
        System.out.println("Updating items statistics\n\t-New Value: " + numItems);
    }

    public void updateTotalChests(){
        System.out.println("Updating total chest statistics\n\t-Old Value: " + totalChests);
        this.totalChests++;
        System.out.println("Updating total chest statistics\n\t-New Value: " + totalChests);
    }

    public void updateTotalKeys(){
        System.out.println("Updating total keys statistics\n\t-Old Value: " + totalKeys);
        this.totalKeys++;
        System.out.println("Updating total keys statistics\n\t-New Value: " + totalKeys);
    }

    public void updateTotalItems(){this.totalItems++;}

    /////////////////////////////
    //   Getters and Setters   //
    /////////////////////////////

    public int getTotalChests() {
        return totalChests;
    }

    public void setTotalChests(int totalChests) {
        this.totalChests = totalChests;
    }

    public int getTotalKeys() {
        return totalKeys;
    }

    public void setTotalKeys(int totalKeys) {
        this.totalKeys = totalKeys;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getAttempts(){
        return this.attempts;
    }

    public int getNumChests(){
        return this.numChests;
    }

    public int getNumKeys(){
        return this.numKeys;
    }

    public int getNumItems(){
        return this.numItems;
    }

    public boolean isCompleted(){
        return this.completed;
    }

    public String getBestTime(){
        return this.bestTime;
    }

    public String getLevel(){
        return this.level;
    }

    public void setBestTime(String bestTime) {
        this.bestTime = bestTime;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString(){
        return "Completed: " + completed + "\nAttempts: " + attempts + "\nChests: " + numChests + "\nKeys: " + numKeys +
                "\nItems: " + numItems + "\nBest Time: " + bestTime;
    }
}
