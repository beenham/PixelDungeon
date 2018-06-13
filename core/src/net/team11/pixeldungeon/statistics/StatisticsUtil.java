package net.team11.pixeldungeon.statistics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashMap;

/**
 * Class to handle the reading and writing of the different statistics to/from files in internal storage
 */
public class StatisticsUtil {

    private static GlobalStatistics globalStatistics;
    public static  String globalLocation = Gdx.files.getLocalStoragePath() + "Global.json";

    private static HashMap<String, TotalLevelStatistics> loadedTotals = new HashMap<>();

    /**
     * Static method to parse a .json file from local storage specified by the in parameter
     * @param in : Path to the .json file
     * @return : Returns a level statistics object with the values of the .json file
     */
    public static LevelStatistics parseStatsFromFile(String in){
        Gson gson = new Gson();

        try{
            LevelStatistics levelStatistics = gson.fromJson(new FileReader(in), LevelStatistics.class);
            System.out.println("Read " + in + "successfully");
            System.out.println(levelStatistics.getLevel() + " StatisticsUtil: \n" + levelStatistics.toString());
            return levelStatistics;

        } catch (IOException ex){
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Method to create a new .json file with the values of the levelStatistics object
     * @param statistics : Object to change into .json
     * @param in : File path of the .json file
     */
    public static void createNewJson(Object statistics, String in){
        System.out.println("Creating new JSON File");

        try (Writer writer = new FileWriter(in)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(statistics, writer);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Method to write an object to a .json file
     * Used to update statistics data when level is finished
     * @param statistics : The statistics object to write
     * @param in : Path of the file to write to
     */
    public static void writeToJson(Object statistics, String in){
        Gson gson = new Gson();
        File file = new File(in);

        try{
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(statistics));
            writer.close();
            System.out.println("Finished writing to " + in);
            parseGlobalStatistics();
            System.out.println(globalStatistics);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Method used to parse the global statistics from the Global.json file
     */
    public static void parseGlobalStatistics(){
        System.out.println("PARSING GLOBAL STATS");

        Gson gson = new Gson();
        File globalFile = new File(globalLocation);

        if (globalFile.exists()){
            System.out.println("Global file exists");
            try{
                globalStatistics = gson.fromJson(new FileReader(globalLocation), GlobalStatistics.class);
                System.out.println("Read " + globalLocation + " successfully");
                System.out.println("\t" + globalStatistics.toString());
            } catch (IOException ex){
                ex.printStackTrace();
            }
        } else {
            System.out.println("Global file does not exist creating");
            globalStatistics = new GlobalStatistics();
            createNewJson(globalStatistics, globalLocation);
        }
    }

    /**
     * Method used to parse the total statistics of a level from the levelStats folder in assets.
     * Each total file should have the CSV extension and be named levelNameTotals.csv
     *
     * Method checks to see if the totals have already been loaded in and if not then they are read in
     * otherwise the value is just returned from a hashMap
     *
     *      IMPORTANT : The totals files need to be in the format
     *      LevelName,TotalChests,TotalKeys,TotalItems
     *
     * @param totalsFile : the name of the file to load including .csv extension
     * @return : TotalLevelStatistics object for the specified level
     */
    public static TotalLevelStatistics parseTotalStatistics(String totalsFile){

        if (!loadedTotals.containsKey(totalsFile)){
            TotalLevelStatistics totalLevelStatistics;

            System.out.println("Haven't already loaded " + totalsFile);

            FileHandle fileHandle = Gdx.files.internal("levelStats/"+totalsFile);

            String csvSplitBy = ",";
            String line = fileHandle.readString();
            String[] values = line.split(csvSplitBy);

            totalLevelStatistics = new TotalLevelStatistics(values[0], Integer.parseInt(values[1]),
                    Integer.parseInt(values[2]),Integer.parseInt(values[3]));

            //Update the loadedTotals hash map with the new value loaded
            loadedTotals.put(totalsFile, totalLevelStatistics);

            return totalLevelStatistics;
        } else {
            System.out.println(totalsFile + " has already been loaded");
            return loadedTotals.get(totalsFile);
        }
    }

    public static GlobalStatistics getGlobalStatistics() {
        return globalStatistics;
    }
}
