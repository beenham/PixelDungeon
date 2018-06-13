package net.team11.pixeldungeon.statistics;

/**
 * Class to store and handle the statistics associated with each level in the game
 * NOTE : Items in this class refer to items that the player finds OTHER than coins, keys, etc.
 */
public class LevelStatistics{

    private int attempts;

    private int numChests, numKeys, numItems;

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
        numChests++;
        System.out.println("Updating chests statistics\n\t-New Value: " + numChests);
    }

    public void updateKeys(){
        numKeys++;
        System.out.println("Updating keys statistics\n\t-New Value: " + numKeys);
    }

    public void updateItems(){
        numItems++;
        System.out.println("Updating items statistics\n\t-New Value: " + numItems);
    }

    /////////////////////////////
    //   Getters and Setters   //
    /////////////////////////////


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
